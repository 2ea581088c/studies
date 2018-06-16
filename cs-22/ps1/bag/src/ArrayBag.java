/* 
 * ArrayBag.java
 * 
 * Author:          Computer Science E-22 staff
 * Modified by:     <your name>, <your e-mail address>
 * Date modified:   <current date>
 */

import java.util.*;

/**
 * An implementation of a Bag ADT using an array.
 */
public class ArrayBag implements Bag {
    /** 
     * The array used to store the items in the bag.
     */
    private Object[] items;
    
    /** 
     * The number of items in the bag.
     */
    private int numItems;
    
    public static final int DEFAULT_MAX_SIZE = 50;
    
    /**
     * Default, no-arg constructor - creates a new, empty ArrayBag with 
     * the default maximum size.
     */
    public ArrayBag() {
        items = new Object[DEFAULT_MAX_SIZE];
        numItems = 0;
    }
    
    /** 
     * A constructor that creates a new, empty ArrayBag with the specified
     * maximum size.
     */
    public ArrayBag(int maxSize) {
        if (maxSize <= 0)
            throw new IllegalArgumentException("maxSize must be > 0");
        items = new Object[maxSize];
        numItems = 0;
    }
    
    /** 
     * add - adds the specified item to the Bag.  Returns true on
     * success and false if there is no more room in the Bag.
     */
    public boolean add(Object item) {
        if (item == null)
            throw new IllegalArgumentException("item must be non-null");
        if (numItems == items.length)
            return false;              // no more room!
        else {
            items[numItems] = item;
            numItems++;
            return true;
        }
    }
    
    /** 
     * remove - removes one occurrence of the specified item (if any)
     * from the Bag.  Returns true on success and false if the
     * specified item (i.e., an object equal to item) is not in the Bag.
     */
    public boolean remove(Object item) {
        for (int i = 0; i < numItems; i++) {
            if (items[i].equals(item)) {
                // Shift the remaining items left by one.
                for (int j = i; j < numItems - 1; j++) 
                    items[j] = items[j + 1];
                items[numItems - 1] = null;
                
                numItems--;
                return true;
            }
        }
        
        return false;  // item not found
    }
    
    /**
     * contains - returns true if the specified item is in the Bag, and
     * false otherwise.
     */
    public boolean contains(Object item) {
        for (int i = 0; i < numItems; i++) {
            if (items[i].equals(item))
                return true;
        }
        
        return false;
    }
    
    /**
     * containsAll - does this ArrayBag contain all of the items in
     * otherBag?  Returns false if otherBag is null or empty. 
     */
    public boolean containsAll(Bag otherBag) {
        if (otherBag == null || otherBag.numItems() == 0)
            return false;
        
        Object[] otherItems = otherBag.toArray();
        for (int i = 0; i < otherItems.length; i++) {
            if (!contains(otherItems[i]))
                return false;
        }
        
        return true;
    }
    
    /**
     * numItems - returns the number of items in the Bag.
     */
    public int numItems() {
        return numItems;
    }
    
    /**
     * grab - returns a reference to a randomly chosen in the Bag.
     */
    public Object grab() {
        if (numItems == 0)
            throw new NoSuchElementException("the bag is empty");
        int whichOne = (int)(Math.random() * numItems);
        return items[whichOne];
    }
    
    /**
     * toArray - return an array containing the current contents of the bag
     */
    public Object[] toArray() {
        Object[] copy = new Object[numItems];
        
        for (int i = 0; i < numItems; i++)
            copy[i] = items[i];
        
        return copy;
    }
    
    /**
     * toString - converts this ArrayBag into a readable String object.
     * Overrides the Object version of this method.
     */
    public String toString() {
        String str = "{";
        
        for (int i = 0; i < numItems; i++)
            str = str + " " + items[i];
        str = str + " }";
        
        return str;
    }

    /**    int capacity() - returns the maximum number of items that the ArrayBag is able to holdd
    */
    public int capacity() {
        return items.length;
    }

    /** boolean isFull() - returns true if the ArrayBag is full, and false otherwise.
     */
     public boolean isFull() {
         return (items.length == numItems);
     }

    /** void increaseCapacity(int increment) - increases the maximum capacity of the bag by the specified amount.
     * For example, if b has a maximum capacity of 10,
     * then b.increaseCapacity(5) should give b a maximum capacity of 15.
     */
    public void increaseCapacity(int increment) {
        if (increment == 0) {
            return;
        } else if (increment < 0) {
            throw new IllegalArgumentException("increment must be greater than 0");
        }

        Object[] copy = new Object[this.numItems + increment];

        // copy existing items into new array
        for (int i = 0; i < this.numItems; i++) {
            copy[i] = this.items[i];
        }

        // store reference into original array
        this.items = copy;
    }

    /** boolean removeItems(Bag other) - attempts to remove from the called ArrayBag all occurrences of the items
     * found in the parameter other.  Returns true if one or more items are removed, and false otherwise.
     */
    public boolean removeItems(Bag other) {
        if (other == null) {
            throw new IllegalArgumentException("Bag other is null");
        }

        // loop over Bag other to check each item against all items in ArrayBag
        boolean hasRemoved = false;

        for (int i = 0; i < other.numItems(); i++) {
            while (this.remove(other.toArray()[i])) {
                hasRemoved = true;
            }
        }

        return hasRemoved;
    }

    /** Bag unionWith(Bag other) - creates and returns an ArrayBag containing one occurrence of any item that
     * is found in either the called object or the parameter other. the resulting bag should not include any duplicates.
     */
    public Bag unionWith(Bag other) {
        if (other == null) {
            throw new IllegalArgumentException("Bag other is null");
        }

        // Give the new ArrayBag a maximum size that is the sum of the two bag’s maximum sizes
        // impossible as Bag does not implement capacity() method
        Bag b = new ArrayBag(other.numItems() + this.capacity());

        // loop over each bag to add items if not yet contained within Bag b
        for (int i = 0; i < other.numItems(); i++) {
            if (!b.contains(other.toArray()[i])) {
                b.add(other.toArray()[i]);
            }
        }

        for (int i = 0; i < this.numItems(); i++) {
            if (!b.contains(this.items[i])) {
                b.add(this.items[i]);
            }
        }

        return b;
    }


    
    /* Test the ArrayBag implementation. */
    public static void main(String[] args) {
/*        // Create a Scanner object for user input.
        Scanner in = new Scanner(System.in);
        
        // Create an ArrayBag named bag1.
        System.out.print("Size of bag 1: ");
        int size = in.nextInt();
        Bag bag1 = new ArrayBag(size);
        in.nextLine();    // consume the rest of the line
        
        // Read in strings, add them to bag1, and print out bag1.
        String itemStr;        
        for (int i = 0; i < size; i++) {
            System.out.print("item " + i + ": ");
            itemStr = in.nextLine();
            bag1.add(itemStr);
        }
        System.out.println("bag 1 = " + bag1);
        System.out.println();
        
        // Select a random item and print it.
        Object item = bag1.grab();
        System.out.println("grabbed " + item);
        System.out.println();
        
        // Iterate over the objects in bag1, printing them one per
        // line.
        Object[] items = bag1.toArray();
        for (int i = 0; i < items.length; i++)
            System.out.println(items[i]);
        System.out.println();
        
        // Get an item to remove from bag1, remove it, and reprint the bag.
        System.out.print("item to remove: ");
        itemStr = in.nextLine();
        if (bag1.contains(itemStr))
            bag1.remove(itemStr);
        System.out.println("bag 1 = " + bag1);
        System.out.println();*/

        ArrayBag b1 = new ArrayBag(10);
        ArrayBag b2 = new ArrayBag();
//        b2.add(2);
//        b2.add(2);
//        b2.add(3);
//        b2.add(5);
//        b2.add(7);
//        b2.add(7);
//        b1.add(2);
//        b1.add(3);
//        b1.add(4);
//        b1.add(5);
//        b1.add(6);
//        b1.add(7);

        Bag b3 = b1.unionWith(b2);

        System.out.println(b3);
    }
}
