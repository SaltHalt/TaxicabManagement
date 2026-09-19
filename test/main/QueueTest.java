package main;

import org.junit.Test;
import queue.Queue;
import static org.junit.Assert.*;

public class QueueTest {
    //Test 7
    @Test
    public void testAdd() {
        Queue<Integer> queue = new Queue<>();
        queue.add(3);
        queue.add(1);
        queue.add(-5);
        queue.add(17);
        assertEquals("[3,1,-5,17]",queue.toString());
    }
    //Test 8
    @Test
    public void testPop() {
        Queue<Integer> queue = new Queue<>();
        queue.add(3);
        queue.add(1);
        queue.add(-5);
        queue.add(17);
        Integer expected = 3;
        assertEquals(expected,queue.pop());
    }
    //Test 9
    @Test
    public void testLength() {
        Queue<Integer> queue = new Queue<>();
        queue.add(3);
        queue.add(1);
        queue.add(-5);
        queue.add(17);
        queue.pop();
        assertEquals(3,queue.length());
    }    
}
