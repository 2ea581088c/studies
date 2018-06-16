/**
 * In a file named StringRecursion.java, implement the methods described below, and then create a main() method
 * to test these methods. Your methods must be recursive; no credit will be given for methods that employ iteration.
 * In addition, global variables (variables declared outside of the method) are not allowed. You may find it helpful
 * to employ the substring, charAt, and length methods of the String class as part of your solutions.
 * */

public class StringRecursion {
    public static void main(String[] args) {
//        printLetters("Rabbit");
//        System.out.println(replace("", 'r', 'y'));
        String str = "12345";
        System.out.println(indexOf('P', str));

    }

    // This method should use recursion to print the individual characters in the string str, separated by commas.
    // All characters in the string should be printed, not just the letters.
    public static void printLetters(String str) {
        if (str == null || str == "") {
            return;
        }

            System.out.print(str.charAt(0));

            // if not last character, print ', '
            if (str.length() != 1) {
                System.out.print(", ");
                printLetters(str.substring(1));
        }
    }


    // This method should use recursion to return a String that is formed by replacing all occurrences of the character
    // oldChar in the string str with the character newChar
    public static String replace(String str, char oldChar, char newChar) {
        if (str.equals(null)) {
            return null;
        }
        if (str.length() == 0) {
            return "";
        }

        // 'loop' over str by recursively concatenating
            return (str.charAt(0) == oldChar) ? newChar + replace(str.substring(1), oldChar, newChar)
                    : str.charAt(0) + replace(str.substring(1), oldChar, newChar);
    }


    // This method should use recursion to find and return the index of the first occurrence of the character
    // ch in the string str, or -1 if ch does not occur in str
    public static int indexOf(char ch, String str) {
        if (str.equals(null) || str.equals("")) {
            return -1;
        }
        // 'loop' over str by recursively shortening substring
        if (str.charAt(0) == ch) {
            return 0;
        }
        return (indexOf(ch, str.substring(1)) == -1) ? -1 : 1 + indexOf(ch, str.substring(1));
/*
        int i;
        // if reaches the end of str without finding a letter, returns (1 + -2) == -1
        if (indexOf(ch, str.substring(1)) == -1) {
            i = -2;
        }
        else {i = 1 + indexOf(ch, str.substring(1));}
        return i;*/

    }
}
