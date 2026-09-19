package main;

import hashtable.Hashtable;
import org.junit.Test;
import static org.junit.Assert.*;

public class HashtableTest {
    //Test 10
    @Test
    public void testAdd() {
        Hashtable<Integer, Integer> hashtable = new Hashtable<>(5);
        hashtable.add(2, -4);
        hashtable.add(7, -1);
        hashtable.add(-1, 3);
        assertEquals("{-1:3,2:-4,7:-1}",hashtable.toString());
    }
    //Test 11
    @Test
    public void testItem() {
        Hashtable<Integer, Integer> hashtable = new Hashtable<>(5);
        hashtable.add(2, -4);
        hashtable.add(7, -1);
        hashtable.add(-1, 3);
        assertEquals(-1,(int) hashtable.item(7));
    }
    //Test 12
    @Test
    public void testSetValue() {
        Hashtable<Integer, Integer> hashtable = new Hashtable<>(5);
        hashtable.add(2, -4);
        hashtable.add(7, -1);
        hashtable.add(-1, 3);
        hashtable.setValue(-1,17);
        assertEquals("{-1:17,2:-4,7:-1}",hashtable.toString());
    }
    //Test 13
    @Test
    public void testContains() {
        Hashtable<Integer, Integer> hashtable = new Hashtable<>(5);
        hashtable.add(2, -4);
        hashtable.add(7, -1);
        hashtable.add(-1, 3);
        assertTrue(hashtable.contains(2));
        assertFalse(hashtable.contains(0));
    }  
}
