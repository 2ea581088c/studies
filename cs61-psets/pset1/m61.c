#define M61_DISABLE 1
#include "m61.h"
#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include <inttypes.h>
#include <assert.h>

// static fields for function m61_getstatistics
static unsigned long long nallocs = 0;
static unsigned long long nfrees = 0;
static unsigned long long allocbytes = 0;
static unsigned long long freebytes = 0;
static unsigned long long fail = 0;
static unsigned long long failsize = 0;
static char* heap_min = NULL;
static char* heap_max = NULL;


// struct for header and footer metadata to check boundary write integrity. assuming 16 byte alignment for payload.
// markers should be initialised as -1 or 0xff
typedef struct {
    unsigned long marker[4];
} header;
typedef struct {
    unsigned char marker;
} footer;

// linked list with dummy head node for tracking currently allocated pointers
// ptr, file and line are from parameters of alloc() functions. size is size of allocated block.
// next is the pointer to next plist node. nextAlloc is the next allocated pointer (to check for memcpy boundary overwrites)
// set file to be NULL if ptr has been previously freed. update nextAlloc whenever free() and malloc() called

static struct plist{
    struct plist* next;

    void* ptr;
    void* nextAlloc;
    const char* file;
    unsigned long long size;
    int line;
} listhead;

// // union to maintain 16 byte alignment for struct plist
// typedef union align {

// }

// struct plist* pcheck (void* check, plist* trav)
// detects attempt to free memory within an allocated block but not at pointer returned by malloc
// checks if void* check lies within range of memory allocated for pointer at trav
// returns 0 if doesn't lie within allocated regions and 1 if pointer lies within allocated region

int pcheck (void* check, struct plist* trav) {
    uintptr_t start = (uintptr_t)trav->ptr;
    uintptr_t end = (uintptr_t)trav->ptr + (uintptr_t)trav->size;
    uintptr_t address = (uintptr_t) check;
    if ((start < address) && (address <= end)) {
        return 1;
    }
    return 0;
}

// int zeroed(void* ptr, size_t sz)
// checks if data has been previously zeroed by a call to free()
// returns 0 if data has not been zeroed or 1 if it has been zeroed
// int zeroed (void* ptr, size_t sz) {
//     char* c = (char*)ptr;
//     for (int i = 0; i < sz; i++) {
//         if (c != 0x00) {
//             return 0;
//         }
//         c ++;
//     }
//     return 1;
// }



/// m61_malloc(sz, file, line)
///    Return a pointer to `sz` bytes of newly-allocated dynamic memory.
///    The memory is not initialized. If `sz == 0`, then m61_malloc may
///    either return NULL or a unique, newly-allocated pointer value.
///    The allocation request was at location `file`:`line`.

void* m61_malloc(size_t sz, const char* file, int line) {
    (void) file, (void) line;   // avoid uninitialized variable warnings

    //int overflow - check sz+sizeof(header)+size > sz
    if (sz + sizeof(header) + sizeof(footer) <= sz || !sz) {
        fail ++;
        failsize += sz;
        return NULL;
    }

    struct plist* pl = (struct plist*)malloc(sizeof(struct plist));
    header* hptr = malloc(sizeof(header) + sz + sizeof(footer));
    void* ptr = NULL;
    if (hptr) {
        ptr = (void*) ((uintptr_t)hptr + sizeof(header));
        footer* fptr = (footer*) ((uintptr_t)ptr + sz);
        // update statistics
        nallocs ++;
        allocbytes += sz;
        if ((uintptr_t)hptr < (uintptr_t)heap_min || !heap_min) {
            heap_min = (char*)hptr;
        }
        uintptr_t largest = (uintptr_t)fptr + (uintptr_t)sizeof(footer);
        if (largest > (uintptr_t)heap_max || !heap_max) {
            heap_max = (char*)largest;
        }

        // set header and footer data
        fptr->marker = 0xff;

        hptr->marker[0] = -1;
        hptr->marker[1] = -1;
        hptr->marker[2] = -1;
        hptr->marker[3] = -1;

        // insert plist node with metadata
        struct plist* trav = &listhead;
        while (trav->next != NULL) {
            trav = trav->next;
        }
        if (pl) {
            pl->ptr = ptr;
            pl->file = file;
            pl->line = line;
            pl->size = sz;
            trav->next = pl;
            trav->nextAlloc = ptr;
        }

    } else {
        free(pl);
        fail ++;
        failsize += sz;
    }

    return ptr;
}


/// m61_free(ptr, file, line)
///    Free the memory space pointed to by `ptr`, which must have been
///    returned by a previous call to m61_malloc and friends. If
///    `ptr == NULL`, does nothing. The free was called at location
///    `file`:`line`.

void m61_free(void *ptr, const char *file, int line) {
    (void) file, (void) line;   // avoid uninitialized variable warnings
    if (ptr) {
        //check that pointer in pointer list and update pointer list
        struct plist* trav = listhead.next;
        struct plist* trail = &listhead;
        while (trav != NULL) {
            if (trav->ptr == ptr) {
                //check for double frees and allocation in linked list
                if (trav->file == NULL || trail->nextAlloc != ptr) {
                    printf("MEMORY BUG%s:%i: invalid free of pointer %p\n", trav->file, trav->line, ptr);
                    abort();
                }
                // check header and footer metadata
                header* hptr = ((header*)ptr) - 1;
                footer* fptr = (footer*) ((uintptr_t)ptr + (uintptr_t)trav->size);
                if (fptr->marker != 0xff ||
                    hptr->marker[0] != -1 || hptr->marker[1] != -1 || hptr->marker[2] != -1 || hptr->marker[3] != -1) {
                    printf("MEMORY BUG: detected wild write during free of pointer %p\n", ptr);
                    abort();
                }


                // update statistics and change file to NULL to mark as freed
                freebytes += trav->size;
                nfrees ++;
                trav->file = NULL;

                // update nextAlloc in linked list
                if (trav->next) {
                    trail->nextAlloc = (trav->next)->ptr;
                } else {
                    trail->nextAlloc = NULL;
                }

                base_free(hptr);
                break;
            // check if pointer is inside an allocated region
            } else if (trav->ptr && (trail->nextAlloc == trav->ptr) && pcheck(ptr, trav)) {
                printf("MEMORY BUG: %s:%i: invalid free of pointer %p, not allocated\n", file, line, ptr);
                printf("  %s:%i: %p is 128 bytes inside a %llu byte region allocated here\n", file, trav->line, ptr, trav->size);
                abort();
            }
            trail = trav;
            trav = trav->next;
        }

        // error message if reached end of list and pointer not in heap
        if (!trav) {
            printf("MEMORY BUG: invalid free of pointer %p, not in heap\n", ptr);
            abort();
        }
    }
}


/// m61_realloc(ptr, sz, file, line)
///    Reallocate the dynamic memory pointed to by `ptr` to hold at least
///    `sz` bytes, returning a pointer to the new block. If `ptr` is NULL,
///    behaves like `m61_malloc(sz, file, line)`. If `sz` is 0, behaves
///    like `m61_free(ptr, file, line)`. The allocation request was at
///    location `file`:`line`.

void* m61_realloc(void* ptr, size_t sz, const char* file, int line) {
    void* new_ptr = NULL;
    if (sz) {
        new_ptr = m61_malloc(sz, file, line);
    }
    if (ptr && new_ptr) {
        // Copy the data from `ptr` into `new_ptr`.
        // To do that, we must figure out the size of allocation `ptr`.
        // Your code here (to fix test014).

        // check ptr was allocated or if ptr within an allocated block
        struct plist* trav = listhead.next;
        while (trav) {
            if (trav->ptr == ptr) {
                break;
            } else if ((trav->ptr && pcheck(ptr, trav)) || !trav->next) {
                printf("MEMORY BUG: invalid realloc of pointer %p\n", ptr);
                abort();
            }
            trav = trav->next;
        }

        //access metadata for allocation sizes and copy data to new_ptr
        size_t psz = trav->size;
        if (psz >= sz) {
            memcpy(new_ptr, ptr, sz);
        } else {
            memcpy(new_ptr, ptr, psz);
        }
    }
    m61_free(ptr, file, line);
    return new_ptr;
}


/// m61_calloc(nmemb, sz, file, line)
///    Return a pointer to newly-allocated dynamic memory big enough to
///    hold an array of `nmemb` elements of `sz` bytes each. The memory
///    is initialized to zero. If `sz == 0`, then m61_malloc may
///    either return NULL or a unique, newly-allocated pointer value.
///    The allocation request was at location `file`:`line`.

void* m61_calloc(size_t nmemb, size_t sz, const char* file, int line) {
    // Your code here (to fix test016).
    // check int overflow - nmemb*sz > nmemb
    size_t s = sz;
    size_t n = 0;
    size_t m = 0;
    while (s > 0) {
        m = n;
        n += nmemb;
        if (m > n) {
            fail ++;
            failsize += (unsigned long long)nmemb * (unsigned long long)sz;
            return NULL;
        }
        s --;
    }

    void* ptr = m61_malloc(nmemb * sz, file, line);
    if (ptr) {
        memset(ptr, 0, nmemb * sz);
    }
    return ptr;
}


/// m61_getstatistics(stats)
///    Store the current memory statistics in `*stats`.

void m61_getstatistics(struct m61_statistics* stats) {
    // Stub: set all statistics to enormous numbers
    memset(stats, 255, sizeof(struct m61_statistics));
    // set all statistics
    stats->nactive = nallocs - nfrees;
    stats->ntotal = nallocs;
    stats->total_size = allocbytes;
    stats->nfail = fail;
    stats->fail_size = failsize;
    stats->heap_min = heap_min;
    stats->heap_max = heap_max;
    stats->active_size = allocbytes - freebytes;
}


/// m61_printstatistics()
///    Print the current memory statistics.

void m61_printstatistics(void) {
    struct m61_statistics stats;
    m61_getstatistics(&stats);

    printf("malloc count: active %10llu   total %10llu   fail %10llu\n",
           stats.nactive, stats.ntotal, stats.nfail);
    printf("malloc size:  active %10llu   total %10llu   fail %10llu\n",
           stats.active_size, stats.total_size, stats.fail_size);
}


/// m61_printleakreport()
///    Print a report of all currently-active allocated blocks of dynamic
///    memory.

void m61_printleakreport(void) {
    // Your code here.
    struct plist* trav = &listhead;
    while (trav->next != NULL) {
        trav = trav->next;
        if (trav->ptr && trav->file) {
            printf("LEAK CHECK: %s:%i: allocated object %p with size %llu\n", trav->file, trav->line, trav->ptr, trav->size);
        }
    }
}
