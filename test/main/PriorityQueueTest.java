package main;

import org.junit.Test;
import priority_queue.PriorityQueue;
import static org.junit.Assert.*;

public class PriorityQueueTest {

    //Test 14
    @Test
    public void testAdd() {
        PriorityQueue<Integer, Integer> queue = new PriorityQueue<>();
        queue.add(3, 5);
        queue.add(7, 4);
        queue.add(3, 10);
        queue.add(3, -2);
        assertEquals("[{3,-2},{7,4},{3,5},{3,10}]", queue.toString());
    }

    //Test 15
    @Test
    public void testPop() {
        PriorityQueue<Integer, Integer> queue = new PriorityQueue<>();
        assertTrue(queue.isEmpty());
        queue.add(3, 5);
        queue.add(7, 4);
        queue.add(3, 10);
        queue.add(3, -2);
        assertEquals(3, (int) queue.pop());
    }

    //Test 16
    @Test
    public void testRemove() {
        PriorityQueue<Integer, Integer> queue = new PriorityQueue<>();
        assertTrue(queue.isEmpty());
        queue.add(3, 5);
        queue.add(7, 4);
        queue.add(3, 10);
        queue.add(3, -2);
        queue.remove(7);
        assertEquals("[{3,-2},{3,5},{3,10}]", queue.toString());
    }

    //Test 17
    @Test
    public void testIsEmpty() {
        PriorityQueue<Integer, Integer> queue = new PriorityQueue<>();
        queue.add(3, 5);
        queue.add(7, 4);
        assertFalse(queue.isEmpty());
        queue.pop();
        queue.pop();
        assertTrue(queue.isEmpty());
    }

    //Test 18
    @Test
    public void testContains() {
        PriorityQueue<Integer, Integer> queue = new PriorityQueue<>();
        queue.add(3, 5);
        queue.add(7, 4);
        queue.add(3, 10);
        queue.add(3, -2);
        assertFalse(queue.contains(4));
        assertTrue(queue.contains(7));
    }
    //Test 19
    @Test
    public void testPriorityOf() {
        PriorityQueue<Integer, Integer> queue = new PriorityQueue<>();
        queue.add(3, 5);
        queue.add(7, 4);
        queue.add(3, 10);
        queue.add(3, -2);
        assertEquals(4, (int) queue.priorityOf(7));
    }
    //Test 20
    
    @Test
    public void testFirst() {
        PriorityQueue<Integer, Integer> queue = new PriorityQueue<>();
        queue.add(3, 5);
        queue.add(7, 4);
        queue.add(3, 10);
        queue.add(3, -2);
        assertEquals(3, (int) queue.first());
    }

}
