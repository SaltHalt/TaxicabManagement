package main;

import org.junit.Test;
import static org.junit.Assert.*;

public class MergeSortTest {

    /**
     * Test 1.
     * A mix of uncapitalised and capitalised words are used to check
     * whether the list is sorted independently of capitalisation
     */
    @Test
    public void testSort_StringArr() {
        String[] listToSort = {"dog", "cat", "car", "Leopard", "floor"};
        String[] expResult = {"car", "cat", "dog", "floor", "Leopard"}; //sorted list
        String[] result = MergeSort.sort(listToSort);
        assertArrayEquals(expResult, result);
    }

    /**
     * Test 2.
     * A mix of uncapitalised and capitalised words are used to check
     * whether the array is sorted independently of capitalisation
     */
    @Test
    public void testSort_StringArrArr() {
        String[][] array = {
            {"treble", "note", "2"},
            {"tree", "cart", "7.1"},
            {"plier", "door", "-3.1"},
            {"Burger", "M50", "2.2"},
            {"stand", "table", "a4"},
            {"car", "convert", "0.7"}};
        String[][] expResult = { //manually sorted array
            {"Burger", "M50", "2.2"},
            {"car", "convert", "0.7"},
            {"plier", "door", "-3.1"},
            {"stand", "table", "a4"},
            {"treble", "note", "2"},
            {"tree", "cart", "7.1"}};
        String[][] result = MergeSort.sort(array);
        assertArrayEquals(expResult, result);
    }

}
