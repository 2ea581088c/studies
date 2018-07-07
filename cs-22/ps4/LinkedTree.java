/*
 * LinkedTree.java
 *
 * Computer Science E-22
 *
 * Modifications and additions by:
 *     name:
 *     username:
 */

import java.lang.reflect.Array;
import java.util.*;

/**
 * LinkedTree - a class that represents a binary tree containing data
 * items with integer keys.  If the nodes are inserted using the
 * insert method, the result will be a binary search tree.
 */
public class LinkedTree {
    // An inner class for the nodes in the tree
    private class Node {
        private int key;         // the key field
        private Object data;     // the rest of the data item
        private Node left;       // reference to the left child/subtree
        private Node right;      // reference to the right child/subtree
        private Node parent;     // reference to the parent

        private Node(int key, Object data, Node left, Node right, Node parent) {
            this.key = key;
            this.data = data;
            this.left = left;
            this.right = right;
            this.parent = parent;
        }

        private Node(int key, Object data) {
            this(key, data, null, null, null);
        }
    }

    // the root of the tree as a whole
    private Node root;

    public LinkedTree() {
        root = null;
    }

    /**
     * This constructor should create a LinkedTree containing the specified keys and data items;
     * each element of the keys array, keys[i], should be paired with the corresponding element
     * of the data-items array, dataItems[i]. For full credit, the resulting tree should be a balanced
     * binary search tree. You may assume that there are no duplicates – i.e., no repeated keys.
     */

    // implementation : recursively calls leftHalf() and rightHalf() to insert additional nodes into the tree
    // leftHalf() - selects the middle element within range of elements - if an even number of elements is given it
    //              will select the smaller of the two middle elements
    // rightHalf() - selects the larger of the two middle elements where there are an even number of elements

    public LinkedTree(int[] keys, Object[] dataItems) {
        if (keys.length == 0) {
            root = null;
        } else if (keys.length == 1) {
            root = new Node(keys[0], dataItems[0]);
        } else {
            // quicksort of both arrays to sort keys in order
            SortHelper.quickSort(keys, dataItems);

            // create root of the tree using middle of the array
            root = new Node(keys[(keys.length - 1) / 2], dataItems[(dataItems.length - 1) / 2]);

            // recursively insert middle of range of remaining keys with helper method
            // to insert the first and last elements, need to consider if there is an available element between
            // [-1, 1] and [keys.length - 2, keys.length]
            leftHalf(this, keys, dataItems, -1, (keys.length - 1) / 2);
            rightHalf(this, keys, dataItems, (keys.length - 1) / 2, keys.length);
        }
    }

    // helper function that inserts one smaller and one larger key than root node
    private void leftHalf(LinkedTree tree, int[] keys, Object[] dataItems, int first, int last) {
        if (last == 0)
            return;

        // recursive function to insert key/data pairs
        int diff = last - first;

        if (diff != 0) {
            diff /= 2;
            tree.insert(keys[diff + first], dataItems[diff + first]);

            if (diff > 1)   // if any more remaining elements to insert within the range
                leftHalf(tree, keys, dataItems, first, diff + first);
            if (last - (diff + first) > 1)
                rightHalf(tree, keys, dataItems, diff + first, last);
        }
    }

    private void rightHalf(LinkedTree tree, int[] keys, Object[] dataItems, int first, int last) {
        if (first == keys.length)
            return;

        int diff = last - first;
        if (diff != 0) {
            if (diff % 2 == 0) {
                diff /= 2;
                tree.insert(keys[diff + first], dataItems[diff + first]);
            } else {
                diff = diff/2 + 1;
                tree.insert(keys[diff + first], dataItems[diff + first]);
            }

            if (diff > 1)
                leftHalf(tree, keys, dataItems, first, diff + first);
            if (last - (diff + first) > 1)
                rightHalf(tree, keys, dataItems, diff + first, last);
        }
    }


    /**
     * Prints the keys of the tree in the order given by a preorder traversal.
     * Invokes the recursive preorderPrintTree method to do the work.
     */
    public void preorderPrint() {
        if (root != null)
            preorderPrintTree(root);
    }

    /*
     * Recursively performs a preorder traversal of the tree/subtree
     * whose root is specified, printing the keys of the visited nodes.
     * Note that the parameter is *not* necessarily the root of the
     * entire tree.
     */
    private static void preorderPrintTree(Node root) {
        System.out.print(root.key + " ");
        if (root.left != null)
            preorderPrintTree(root.left);
        if (root.right != null)
            preorderPrintTree(root.right);
    }

    /**
     * Prints the keys of the tree in the order given by a postorder traversal.
     * Invokes the recursive postorderPrintTree method to do the work.
     */
    public void postorderPrint() {
        if (root != null)
            postorderPrintTree(root);
    }

    /*
     * Recursively performs a postorder traversal of the tree/subtree
     * whose root is specified, printing the keys of the visited nodes.
     * Note that the parameter is *not* necessarily the root of the
     * entire tree.
     */
    private static void postorderPrintTree(Node root) {
        if (root.left != null)
            postorderPrintTree(root.left);
        if (root.right != null)
            postorderPrintTree(root.right);
        System.out.print(root.key + " ");
    }

    /**
     * Prints the keys of the tree in the order given by an inorder traversal.
     * Invokes the recursive inorderPrintTree method to do the work.
     */
    public void inorderPrint() {
        if (root != null)
            inorderPrintTree(root);
    }

    /*
     * Recursively performs an inorder traversal of the tree/subtree
     * whose root is specified, printing the keys of the visited nodes.
     * Note that the parameter is *not* necessarily the root of the
     * entire tree.
     */
    private static void inorderPrintTree(Node root) {
        if (root.left != null)
            inorderPrintTree(root.left);
        System.out.print(root.key + " ");
        if (root.right != null)
            inorderPrintTree(root.right);
    }

    /*
     * Inner class for temporarily associating a node's depth
     * with the node, so that levelOrderPrint can print the levels
     * of the tree on separate lines.
     */
    private class NodePlusDepth {
        private Node node;
        private int depth;

        private NodePlusDepth(Node node, int depth) {
            this.node = node;
            this.depth = depth;
        }
    }

    /**
     * Prints the keys of the tree in the order given by a
     * level-order traversal.
     */
    public void levelOrderPrint() {
        LLQueue<NodePlusDepth> q = new LLQueue<NodePlusDepth>();

        // Insert the root into the queue if the root is not null.
        if (root != null)
            q.insert(new NodePlusDepth(root, 0));

        // We continue until the queue is empty.  At each step,
        // we remove an element from the queue, print its value,
        // and insert its children (if any) into the queue.
        // We also keep track of the current level, and add a newline
        // whenever we advance to a new level.
        int level = 0;
        while (!q.isEmpty()) {
            NodePlusDepth item = q.remove();

            if (item.depth > level) {
                System.out.println();
                level++;
            }
            System.out.print(item.node.key + " ");

            if (item.node.left != null)
                q.insert(new NodePlusDepth(item.node.left, item.depth + 1));
            if (item.node.right != null)
                q.insert(new NodePlusDepth(item.node.right, item.depth + 1));
        }
    }

    /**
     * Searches for the specified key in the tree.
     * Invokes the recursive searchTree method to perform the actual search.
     */
    public Object search(int key) {
        Node n = searchTree(root, key);
        return (n == null ? null : n.data);
    }

    /*
     * Recursively searches for the specified key in the tree/subtree
     * whose root is specified. Note that the parameter is *not*
     * necessarily the root of the entire tree.
     */
    private static Node searchTree(Node root, int key) {
        if (root == null)
            return null;
        else if (key == root.key)
            return root;
        else if (key < root.key)
            return searchTree(root.left, key);
        else
            return searchTree(root.right, key);
    }

    /**
     * Inserts the specified (key, data) pair in the tree so that the
     * tree remains a binary search tree.
     */
    public void insert(int key, Object data) {
        // Find the parent of the new node.
        Node parent = null;
        Node trav = root;
        while (trav != null) {
            parent = trav;
            if (key < trav.key)
                trav = trav.left;
            else
                trav = trav.right;
        }

        // Insert the new node.
        Node newNode = new Node(key, data);
        if (parent == null) {   // the tree was empty
            root = newNode;
        } else if (key < parent.key) {
            parent.left = newNode;
            newNode.parent = parent;
        } else {
            parent.right = newNode;
            newNode.parent = parent;
        }
    }

    /**
     * Deletes the node containing the (key, data) pair with the
     * specified key from the tree and return the associated data item.
     */
    public Object delete(int key) {
        // Find the node to be deleted and its parent.
        Node parent = null;
        Node trav = root;
        while (trav != null && trav.key != key) {
            parent = trav;
            if (key < trav.key)
                trav = trav.left;
            else
                trav = trav.right;
        }

        // Delete the node (if any) and return the removed data item.
        if (trav == null)   // no such key
            return null;
        else {
            Object removedData = trav.data;
            deleteNode(trav, parent);
            return removedData;
        }
    }

    /**
     * Deletes the node specified by the parameter toDelete.  parent
     * specifies the parent of the node to be deleted.
     */
    private void deleteNode(Node toDelete, Node parent) {
        if (toDelete.left != null && toDelete.right != null) {
            // Case 3: toDelete has two children.
            // Find a replacement for the item we're deleting -- as well as
            // the replacement's parent.
            // We use the smallest item in toDelete's right subtree as
            // the replacement.
            Node replaceParent = toDelete;
            Node replace = toDelete.right;
            while (replace.left != null) {
                replaceParent = replace;
                replace = replace.left;
            }

            // Replace toDelete's key and data with those of the
            // replacement item.
            toDelete.key = replace.key;
            toDelete.data = replace.data;

            // Recursively delete the replacement item's old node.
            // It has at most one child, so we don't have to
            // worry about infinite recursion.
            deleteNode(replace, replaceParent);
        } else {
            // Cases 1 and 2: toDelete has 0 or 1 child
            Node toDeleteChild;
            if (toDelete.left != null)
                toDeleteChild = toDelete.left;
            else
                toDeleteChild = toDelete.right;  // null if it has no children

            toDeleteChild.parent = toDelete.parent;

            if (toDelete == root)
                root = toDeleteChild;
            else if (toDelete.key < parent.key)
                parent.left = toDeleteChild;
            else
                parent.right = toDeleteChild;
        }
    }

    /**
     * Determines the depth of the node with the specified key,
     * returning -1 if there is no such node.
     * <p>
     * a depth() method that uses iteration instead of recursion to
     * determine the depth of the node with a specified key.
     * iterative method should take advantage of the fact that the
     * tree is a binary search tree, and it should return -1 if the specified key is not found in the tree
     */
    public int depth(int key) {
        /*** implement this method for PS 4 ***/
        int d = 0;
        Node trav = root;
        while (trav != null) {
            if (key == trav.key) {
                return d;
            } else if (key < trav.key) {
                trav = trav.left;
                d++;
            } else {
                trav = trav.right;
                d++;
            }
        }

        if (trav == null) {
            return -1;
        }

        return d;
    }

    /**
     * Determines if this tree is isomorphic to the other tree,
     * returning true if they are isomorphic and false if they are not.
     * Calls the private helper method isomorphic() to do the work.
     * <p>
     * You should ***NOT*** change this method. Instead, you should
     * implement the private helper method found below.
     */
    public boolean isomorphicTo(LinkedTree other) {
        if (other == null)
            throw new IllegalArgumentException("parameter must be non-null");

        return isomorphic(this.root, other.root);
    }

    /**
     * Determines if the trees with the specified root nodes are
     * isomorphic, returning true if they are and false if they are not.
     */
    private static boolean isomorphic(Node root1, Node root2) {
        /*** implement this method for PS 4 ***/
        // base case of null Node with no children
        if (root1 == null || root2 == null) {
            return (root1 == null && root2 == null);
        }

        // checks each node's children recursively to make sure both nodes are isomorphic
        boolean left = isomorphic(root1.left, root2.left);
        boolean right = isomorphic(root1.right, root2.right);
        return left && right;
    }

    /**
     * Returns a preorder iterator for this tree.
     */
    public LinkedTreeIterator preorderIterator() {
        return new PreorderIterator();
    }

    /**
     * Returns an inorder iterator for this tree.
     */
    public LinkedTreeIterator inorderIterator() {
        /*** implement this method for PS 4 ***/
        return new InOrderIterator();
    }

    /*** inner class for inorder iterator ***/
    private class InOrderIterator implements LinkedTreeIterator {
        private Node nextNode;
        private boolean isLeftChild;

        private InOrderIterator() {
            nextNode = root;
            while (nextNode.left != null) {
                nextNode = nextNode.left;
            }
        }

        public boolean hasNext() {
            return (nextNode != null);
        }

        public int next() {
            if (nextNode == null)
                throw new NoSuchElementException();

            // Store a copy of the key to be returned.
            int key = nextNode.key;

            // Advance nextNode.
            if (nextNode.left == null) {
                if (nextNode.right != null) {
                    nextNode = nextNode.right;
                    while (nextNode.left != null) {
                        nextNode = nextNode.left;
                    }
                } else if (nextNode.parent != null) {
                    if (nextNode.parent.left == nextNode) {
                        nextNode = nextNode.parent;
                    } else {
                        while (nextNode.parent.right == nextNode) {
                            nextNode = nextNode.parent;
                            if (nextNode == null) {
                                break;
                            }
                        }

                        nextNode = nextNode.parent;
                    }
                } else {
                    nextNode = null;
                }
            } else if (nextNode.right != null) {
                nextNode = nextNode.right;
                while (nextNode.left != null) {
                    nextNode = nextNode.left;
                }
            } else {
                if (nextNode.parent != null) {
                    if (nextNode.parent.left == nextNode) {
                        nextNode = nextNode.parent;
                    } else {
                        while (nextNode.parent.right == nextNode) {
                            nextNode = nextNode.parent;
                            if (nextNode.parent == null) {
                                break;
                            }
                        }
                        nextNode = nextNode.parent;
                    }
                } else {
                    nextNode = null;
                }
            }

            return key;
        }
    }


    /*** inner class for a preorder iterator ***/
    private class PreorderIterator implements LinkedTreeIterator {
        private Node nextNode;

        private PreorderIterator() {
            // The traversal starts with the root node.
            nextNode = root;
        }

        public boolean hasNext() {
            return (nextNode != null);
        }

        public int next() {
            if (nextNode == null)
                throw new NoSuchElementException();

            // Store a copy of the key to be returned.
            int key = nextNode.key;

            // Advance nextNode.
            if (nextNode.left != null)
                nextNode = nextNode.left;
            else if (nextNode.right != null)
                nextNode = nextNode.right;
            else {
                // We've just visited a leaf node.
                // Go back up the tree until we find a node
                // with a right child that we haven't seen yet.
                Node parent = nextNode.parent;
                Node child = nextNode;
                while (parent != null &&
                        (parent.right == child || parent.right == null)) {
                    child = parent;
                    parent = parent.parent;
                }

                if (parent == null)
                    nextNode = null;  // the traversal is complete
                else
                    nextNode = parent.right;
            }

            return key;
        }
    }


    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

/*        LinkedTree tree = new LinkedTree();
        tree.insert(7, "root node");
        tree.insert(9, "7's right child");
        tree.insert(5, "7's left child");
        tree.insert(2, "5's left child");
        tree.insert(8, "9's left child");
        tree.insert(6, "5's right child");
        tree.insert(4, "2's right child");

        System.out.print("\n preorder: ");
        tree.preorderPrint();
        System.out.println();*/

/*        System.out.print("postorder: ");
        tree.postorderPrint();
        System.out.println();

        System.out.print("  inorder: ");
        tree.inorderPrint();
        System.out.println();*/

/*        System.out.println( " level order print : ");
        tree.levelOrderPrint();
        System.out.println();*/

//        int[] keys = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
//        Object[] dataItems = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
//        int[] keys = {1};
//        Object[] dataItems = {1};

        LinkedTree tree2 = new LinkedTree(keys, dataItems);
        tree2.levelOrderPrint();




/*        // test inOrderIterator()
        LinkedTreeIterator iter = tree.inorderIterator();
        while (true) {
            System.out.println(iter.next());
        }*/

/*        // to test depth() method
		while (true) {
			System.out.println("enter key : ");
			int key = in.nextInt();
			System.out.println("depth = " + tree.depth(key));
		}*/

/*        LinkedTree tree2 = new LinkedTree();
		tree2.insert(1, "root node");

		// to test isomorphic()
		if (tree.isomorphicTo(tree2)) {
			System.out.println("is iso");
		} else {
			System.out.println("is not iso");
		}*/


/*        System.out.print("\nkey to search for: ");
		int key = in.nextInt();
		in.nextLine();
		Object data = tree.search(key);
		if (data != null)
			System.out.println(key + " = " + data);
		else
			System.out.println("no such key in tree");*/

/*        System.out.print("\nkey to delete: ");
		int key = in.nextInt();
		in.nextLine();
		Object data = tree.delete(key);
		if (data != null)
			System.out.println("removed " + data);
		else
			System.out.println("no such key in tree");

		System.out.print("\n preorder: ");
		tree.preorderPrint();
		System.out.println();*/

/*        System.out.print("postorder: ");
		tree.postorderPrint();
		System.out.println();

		System.out.print("  inorder: ");
		tree.inorderPrint();
		System.out.println();*/

    }
}


