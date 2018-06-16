###########################
# 6.00.2x Problem Set 1: Space Cows 

from ps1_partition import get_partitions
import time

#================================
# Part A: Transporting Space Cows
#================================

def load_cows(filename):
    """
    Read the contents of the given file.  Assumes the file contents contain
    data in the form of comma-separated cow name, weight pairs, and return a
    dictionary containing cow names as keys and corresponding weights as values.

    Parameters:
    filename - the name of the data file as a string

    Returns:
    a dictionary of cow name (string), weight (int) pairs
    """

    cow_dict = dict()

    f = open(filename, 'r')
    
    for line in f:
        line_data = line.split(',')
        cow_dict[line_data[0]] = int(line_data[1])
    return cow_dict


# Problem 1
def greedy_cow_transport(cows,limit=10):
    """
    Uses a greedy heuristic to determine an allocation of cows that attempts to
    minimize the number of spaceship trips needed to transport all the cows. The
    returned allocation of cows may or may not be optimal.
    The greedy heuristic should follow the following method:

    1. As long as the current trip can fit another cow, add the largest cow that will fit
        to the trip
    2. Once the trip is full, begin a new trip to transport the remaining cows

    Does not mutate the given dictionary of cows.

    Parameters:
    cows - a dictionary of name (string), weight (int) pairs
    limit - weight limit of the spaceship (an int)
    
    Returns:
    A list of lists, with each inner list containing the names of cows
    transported on a particular trip and the overall list containing all the
    trips
    """
    #helper function here
    # list ordered from heaviest cows to least heavy. [[cows],weight], [same], etc
    
    #if limit >0 and 
    
    newcow = {}
    for cow in cows:
        newcow[cow] = cows[cow]
    #copy of cows

    #helper function to return list of each trip, then use this until all cows moved. if limit not high enough returns None
    def trip(cows, limit):
        herd = []
        #if enough space for cows to be added
        limcheck = limit
        remaining = limit
        while len(cows)>0 and min(cows.values()) <= remaining:
            for cow in cows:
                if cows[cow] == limcheck:
                    herd.append(cow)
                    del(cows[cow])
                    remaining -= limcheck
                    limcheck = remaining +1
                    break
                        
            
            limcheck -= 1
                    
        if herd != []:
            return herd
            
    #function removes cows from trip from total list so must feed it newcow list only
    
    triplist = []
    
    while newcow != {}:
        triplist.append(trip(newcow, limit))
        
        
    return triplist
        
        
        









# Problem 2
def brute_force_cow_transport(cows,limit=10):
    """
    Finds the allocation of cows that minimizes the number of spaceship trips
    via brute force.  The brute force algorithm should follow the following method:

    1. Enumerate all possible ways that the cows can be divided into separate trips
    2. Select the allocation that minimizes the number of trips without making any trip
        that does not obey the weight limitation
            
    Does not mutate the given dictionary of cows.

    Parameters:
    cows - a dictionary of name (string), weight (int) pairs
    limit - weight limit of the spaceship (an int)
    
    Returns:
    A list of lists, with each inner list containing the names of cows
    transported on a particular trip and the overall list containing all the
    trips
    """
    # TODO: Your code here
    cowlist = list(get_partitions(cows.keys()))
    #all combinations of cow names
    
    #merge sort here so that list is ordered from least no of trips(ie lowest len()) to highest
    
            
    def merge(l, r):
        i, j = 0, 0
        merged = []
        while i < len(l) and j < len(r):
            if len(l[i]) < len(r[j]):
                merged.append(l[i])
                i+=1
            else:
                merged.append(r[j])
                j+=1
        while (i < len(l)):
                merged.append(l[i])
                i+=1
        while (j < len(r)):
                merged.append(r[j])
                j += 1
        return merged
                
    def mergesorted(list):
        if len(list)<2:
            return list[:]
        else:
            middle = len(list)//2
            l = mergesorted(list[:middle])
            r = mergesorted(list[middle:])
            return merge(l, r)
            
        
    sorted = mergesorted(cowlist)
    
    #iterate over the list to find the first one that doesnt exceed weight limits. lookup values of cows in dict
        
    def besttrip(list):
        for trips in sorted:
            for trip in trips:
                weight = 0
                for cow in trip:
                    weight += cows[cow]
                if weight > limit:
                    break
                if trip == trips[(len(trips))-1]:
                    return trips
                    
    return besttrip(sorted)
        
    
    

        
# Problem 3
def compare_cow_transport_algorithms():
    """
    Using the data from ps1_cow_data.txt and the specified weight limit, run your
    greedy_cow_transport and brute_force_cow_transport functions here. Use the
    default weight limits of 10 for both greedy_cow_transport and
    brute_force_cow_transport.
    
    Print out the number of trips returned by each method, and how long each
    method takes to run in seconds.

    Returns:
    Does not return anything.
    """
    # TODO: Your code here
    start = time.time()
    brute = brute_force_cow_transport(cows)
    end = time.time()
    print('brute')
    print(len(brute))
    print(end-start)

    start2 = time.time()
    greedy = greedy_cow_transport(cows)
    end2 = time.time()
    print('greedy')
    print(len(greedy))
    print(end2-start2)

"""
Here is some test data for you to see the results of your algorithms with. 
Do not submit this along with any of your answers. Uncomment the last two
lines to print the result of your problem.
"""

cows = load_cows("ps1_cow_data.txt")
limit=100
print(cows)

print(greedy_cow_transport(cows, limit))
print(brute_force_cow_transport(cows, limit))





