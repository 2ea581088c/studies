public class LLQueue<T> implements Queue<T> {
    private class Node {
        private T item;
        private Node next;

        private Node(T i, Node n) {
            this.item = i;
            this.next = n;
        }
    }

    private Node front;
    private Node rear;

    public LLQueue() {
        front = null;
        rear = null;
    }

    public boolean insert(T item) {
        Node newNode = new Node(item, null);

        if (isEmpty()) {
            front = newNode;
            rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }

        return true;
    }

    public boolean isEmpty() {
        return (front == null);
    }

    public boolean isFull() {
        return false;
    }

    public T peek() {
        return front.item;
    }

    public T remove() {
        if (isEmpty()) {
            return null;
        }

        T removed = front.item;
        if (front == rear) {
            front = null;
            rear = null;
        } else {
            front = front.next;
        }
        return removed;
    }
}

