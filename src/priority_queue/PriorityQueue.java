package priority_queue;

/**
 * A priority queue, where higher priority items are placed further back in the
 * queue.
 */
public class PriorityQueue<V, P extends Comparable<P>> {

    private Element<V, P> front;//The front of the priority queue
    private Element<V, P> back;//The end of the priority queue
    private final Element<V, P> START = null;
    private final Element<V, P> END = null;
    private final int NOT_FOUND = -1;
    private int length;
    
    public PriorityQueue() {
        length = 0;
        front = null;
        back = null;
    }

    /**
     * Add an item to the priority queue
     */
    public void add(V item, P priority) {
        if (isEmpty()) {
            front = new Element<V, P>(item, priority, START, END);
            back = front;
        } else if (priority.compareTo(front.priority()) < 0) {//if item belongs at the beginning
            front = new Element<>(item, priority, START, front);
            front.next().previous(front);
        } else if (priority.compareTo(back.priority()) >= 0) {//if item belongs at the end
            back = new Element<>(item, priority, back, END);
            back.previous().next(back);
        } else {//if item belongs in between
            Element<V, P> current = front;
            while (priority.compareTo(current.priority())>=0) {
                current = current.next();
            }
            Element<V, P> newElement = new Element<>(item, priority, current.previous(), current);
            current.previous().next(newElement);
            current.previous(newElement);
        }
        length++;
    }
    
    /**
     * Returns whether the queue is empty
     */
    public boolean isEmpty() {//Return is the queue is empty
        return front == END;
    }
    
    /**
     * Removes the first item from the queue, and returns its value.
     */
    public V pop() {
        V output = null;
        if (isEmpty()) {
            throw new UnsupportedOperationException("Trying to pop from empty queue.");
        } else if (length == 1) {
            output = front.value();
            front = END;
            back = START;
            length--;
        } else {
            output = front.value();
            front = front.next();
            front.previous(START);
            length--;
        }
        return output;
    }
    
    @Override
    public String toString() {
        String output = "[";
        Element<V, P> current = front;
        while (current != END) {
            output += "{" + current.toString() + "},";
            current = current.next();
        }
        output = output.substring(0, output.length() - 1);
        output += "]";
        return output;
    }

    /**
     * Removes the first element containing the inputted value, and returns this value.
     */
    public void remove(V value) {      
        Element<V,P> current = front;
        int index = 0;
        while (!(current == END || value.equals(current.value()))) {
            current = current.next();
            index++;
        }
        if (index == NOT_FOUND) {
            throw new IllegalArgumentException("Value not found in priority queue");
        } else if(index == 0 && length == 1){
            front = END;
            back = START;
        } else if (index == 0) {
            front = current.next();
            front.previous(START);
        } else if (index == length - 1) {
            Element<V,P> previousElement = current.previous();
            previousElement.next(END);
            back = previousElement;
        } else {
            Element<V,P> previousElement = current.previous();
            Element<V,P> nextElement = current.next();
            previousElement.next(nextElement);
            nextElement.previous(previousElement);
        }
        length--;
    }
    /**
     * Returns whether the list contains the specified value
     */
    public boolean contains(V value){
        boolean isValueFound = false;
        Element<V,P> current = front;
        while(!(current == END || isValueFound)){
            isValueFound = current.value().equals(value);
            current = current.next();
        }
        return isValueFound;
    }
    /**
     * Finds the priority of a first instance of this value in the queue.
     * If the queue isn't in the queue, then null is returned.
     */
    public P priorityOf(V value){
        Element<V,P> current = front;
        while(!(current == END || current.value().equals(value))){
            current = current.next();
        }
        return (current==END)? null : current.priority();
    }
    /**
     * Gets the front value in the queue.
     */
    public V first(){
        return front.value();
    }
}
