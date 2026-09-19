package main;

import org.junit.Test;
import stack.Stack;
import static org.junit.Assert.*;

public class StackTest {
    /**Test 3*/
    @Test
    public void testPush() {
        Stack stack = new Stack(5);
        stack.push(1);
        stack.push(3);
        stack.push(2);
        assertEquals("[1, 3, 2, 0, 0]", stack.toString());
    }
    /**Test 4*/
    @Test
    public void testPop() {
        Stack stack = new Stack(5);
        stack.push(1);
        stack.push(3);
        stack.push(2);
        assertEquals(2, stack.pop());
    }
    /**Test 5*/
    @Test
    public void testIsEmpty() {
        Stack stack = new Stack(5);
        stack.push(1);
        assertFalse(stack.isEmpty());
        stack.pop();
        assertTrue(stack.isEmpty());
    }
    /**Test 6*/
    @Test
    public void testSize() {
        Stack stack = new Stack(5);
        stack.push(1);
        stack.push(3);
        stack.push(2);
        stack.pop();
        assertEquals(2, stack.size());
    }
}
