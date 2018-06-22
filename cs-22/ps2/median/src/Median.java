/*
 * Median.java
 *
 * Starter code by Computer Science E-22 Staff
 * 
 * Modifed by:      <your name>, <your e-mail address>
 * Date modified:   <current date>
 */

import java.util.Arrays;

public class Median {

    // sort the median value into the right place in array.
    // in odd-length arrays the value should be in arr[arr.length/2] with int division.
    // in even-length arrays the values should be in arr[arr.length/2] and arr[arr.length/2 - 1]
    // quicksort partitions - if one subarray contains < 1/2 of all elements it cannot contain the median value



    /*
     * swap - swap the values of the array elements at 
     * position a and position b of the array to which arr refers.
     * Used by the partition method below.
     */
    private static void swap(int[] arr, int a, int b) {
        int temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }
    
    /* partition - helper method for your recursive median-finding method */
    private static int partition(int[] arr, int first, int last) {
        int pivot = arr[(first + last)/2];
        int i = first - 1;  // index going left to right
        int j = last + 1;   // index going right to left
        
        while (true) {
            do {
                i++;
            } while (arr[i] < pivot);
            do {
                j--;
            } while (arr[j] > pivot); 
            
            if (i < j)
                swap(arr, i, j);
            else
                return j;   // index of last element in the left subarray
        }                   
    }

    /* 
     * findMedian - "wrapper" method for your recursive median-finding method.
     * It just makes the initial call to that method, passing it
     * whatever initial parameters make sense.
     */
    public static void findMedian(int[] arr) {
        // add an appropriate call to your recursive method
        if (arr.length % 2 == 1) {
            medianSort(arr, 0, arr.length - 1, arr.length / 2);
            System.out.println(String.valueOf(arr[arr.length / 2]));
        } else {
            medianSort(arr, 0, arr.length - 1, arr.length / 2);
            medianSort(arr, 0, arr.length - 1, (arr.length / 2) - 1);
            System.out.println(String.valueOf(arr[arr.length / 2 - 1]) + " " + arr[arr.length / 2]);
        }
    }
    
    /* 
     * Put the definition of your recursive median-finding method below. 
     */

    private static void medianSort(int[] arr, int first, int last, int median) {
        int split = partition(arr, first, last);

        // find index within array that median(s) should be sorted to and recursively sort the subarray that contains
        // the median(s)
        // sort the left subarray if median is within it and subarray not completely sorted
        if (median <= split && last - first != 0)
                medianSort(arr, first, split, median);
        // sort the right subarray if median is within it and subarray not completely sorted
        if (median > split && last - first != 0)
                medianSort(arr, split + 1, last, median);
    }


    
    
    
    public static void main(String[] args) {
        // the median of this array is 15
//        int[] oddLength = {0,5,1,3,4,6,2};
//        int[] oddLength = {4, 18, 12, 34, 7, 42, 15, 22, 5};
//        findMedian(oddLength);
//        System.out.println(Arrays.toString(oddLength));
        
        // the median of this array is the average of 15 and 18 = 16.5
//        int[] evenLength = {4, 18, 12, 34, 7, 42, 15, 22, 5, 27};
//        findMedian(evenLength);
//        System.out.println(Arrays.toString(evenLength));
        
        
        /* Put code to test your method here. */
        
    }
}

