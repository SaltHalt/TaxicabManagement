package queue;

public class Queue<T> {

    private Element<T> front;//The front of the queue
    private Element<T> back;//The end of the queue
    private int length;
    private final Element<T> START = null;
    private final Element<T> END = null;
    
    public Queue() {
        length = 0;
        front = null;
        back = null;
    }

    /**
     * Add an item to the priority queue
     */
    public void add(T value) {
        Element<T> newItem = new Element<>(END, value);
        if (length == 0) {//if list empty
            front = newItem;
        } else  {//if item belongs at the end
            back.next(newItem);
        }
        back = newItem;
        length++;
    }
    
    /**
     * Removes the first item from the queue, and returns its value.
     */
    public T pop() {
        T output = null;
        if (length==0) {
            throw new RuntimeException("Queue empty");
        } else if (length == 1) {//if there's only one item in the queue
            output = front.value();
            front = END;
            back = START;
        } else {//if there's more than one item in the queue
            output = front.value();
            front = front.next();
        }
        length--;
        return output;
    }
    /**
     * Returns the length of the list
     */
    public int length() {
        return length;
    }
    
    @Override
    public String toString() {
        String output = "[";
        Element<T> current = front;
        while (current != END) {
            output += current.toString() + ",";
            current = current.next();
        }
        output = output.substring(0, output.length() - 1); //removes last two characters
        output += "]";
        return output;
    }
}
