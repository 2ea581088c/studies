/*
 * CapitalGainCalc.java
 *
 * Computer Science E-22
 * Modified by: <your name>, <your e-mail address>
 */

import java.util.*;

/**
 * A class for calculating capital gains on stock sales.
 */
public class CapitalGainCalc {
    /* 
     * An inner class for storing information about a stock purchase.
     */
    private class Purchase {
        private int numShares;
        private int price;

        Purchase(int numShares, int price) {
            this.numShares = numShares;
            this.price = price;
        }
    }

    /* Put your fields/instance variables below. */
    private int totalShares;
    private LLQueue<Purchase> q;
    
    public CapitalGainCalc() {
        /* Put your implementation of the constructor below. */
        this.totalShares = 0;
        this.q = new LLQueue<Purchase>();
    }

    public void processPurchase(int numShares, int price) {
        /* Put your implementation of this method below. */
        q.insert(new Purchase(numShares, price));
        totalShares += numShares;
    }

    public int processSale(int numSharesToSell, int price) {
        /* Replace the line below with your implementation of this method. */
        if (numSharesToSell > totalShares) {
            throw new IllegalArgumentException("**You don't have " + numSharesToSell + " to sell**");
        }

        int gain = numSharesToSell * price;
        Purchase sold = null;

        while (numSharesToSell > 0) {
            sold = q.remove();
            numSharesToSell -= sold.numShares;
            gain -= sold.numShares * sold.price;
            totalShares -= sold.numShares;
        }
        // add back new Purchase object if excess shares remaining
        if (numSharesToSell < 0) {
            numSharesToSell *= -1;
            processPurchase(numSharesToSell, sold.price);
            gain += numSharesToSell * sold.price;
        }

        return gain;
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        CapitalGainCalc calc = new CapitalGainCalc();
        int totalGain = 0;
        int gain = 0;

        while (true) {
            // Get the choice of operation.
            System.out.print("\n(1) purchase, (2) sale, or (3) done: ");
            int choice = scan.nextInt();
            if (choice == 3)
                break;
            if (choice != 1 && choice != 2) {
                System.err.println("Please enter 1, 2, or 3.");
                continue;
            } 

            // Get the number of shares and the price.
            System.out.print("number of shares: ");
            int numShares = scan.nextInt();
            System.out.print("price: ");
            int price = scan.nextInt();
            if (numShares <= 0 || price <= 0) {
                System.err.println("Please enter positive numbers.");
                continue;
            }

            if (choice == 1)
                calc.processPurchase(numShares, price);
            else {
                try {
                    gain = calc.processSale(numShares, price);
                    System.out.println("Capital gain on sale: $" + gain);
                    totalGain += gain;
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getMessage());
                }
            }
        }               

        System.out.println("Total capital gain: $" + totalGain);
    }
}
