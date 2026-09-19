package stack;

import java.util.Arrays;

public class Stack {

    private int[] stack;
    private int topOfStack; //points to the item on top of the stack
    private int maxSize;

    /**
     * Instantiates a stack.
     * @param size the maximum size of the stack
     */
    public Stack(int size) {
        maxSize = size;
        stack = new int[maxSize];
        topOfStack = -1;
    }
    /**
     * Pushes the given item onto the stack.
     */
    public void push(int item) {
        if (size() >= maxSize) {
            throw new RuntimeException("Stack overflow");
        } else{
            stack[topOfStack + 1] = item;
            topOfStack++;
        }
    }
    /**
     * Pops an item from the stack, and returns this item.
     */
    public int pop() {
        int value;
        if (isEmpty()) {
            throw new RuntimeException("Stack empty");
        } else {
            topOfStack--;
            value = stack[topOfStack + 1];
        }
        return value;
    }
    /**
     * Returns if the stack is empty.
     */
    public boolean isEmpty() {
        return size() == 0;
    }
    /**
     * Returns the current size of the stack
     */
    public int size(){
        return topOfStack+1;
    }
    @Override
    public String toString(){
        return Arrays.toString(stack);
    }
}
