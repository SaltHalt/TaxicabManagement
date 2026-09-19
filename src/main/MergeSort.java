package main;

import java.util.Arrays;

public class MergeSort {
    /**
     * Sorts the specified list by alphabetical order, using a mergesort.
     */
    public static String[] sort(String[] list){
        String[][] array = new String[list.length][1];
        for (int i = 0; i < list.length; i++) {
            array[i][0] = list[i];
        }
        array = sort(array);
        for (int i = 0; i < array.length; i++){
            list[i] = array[i][0];
        }
        return list;
    }
    
    /**
     * Alphabetically sorts an array by the first column, using a mergesort.
     */
    public static String[][] sort(String[][] array){
        int halfList = array.length/2;
        String[][] leftArray = Arrays.copyOfRange(array, 0, halfList);
        String[][] rightArray = Arrays.copyOfRange(array, halfList, array.length);
        return merge(leftArray,rightArray);
    }
    /**Merge two arrays into a one, sorted array.*/
    private static String[][] merge(String[][] left, String[][] right){
        left = (left.length<=1)? left : sort(left); //sorts left array
        right = (right.length<=1)? right : sort(right); //sorts right array
        
        int totalNoOfItems = left.length + right.length;
        String[][] mergedList = new String[totalNoOfItems][];
        int indexLeft = 0; //points to the next item to be added from the left array
        int indexRight = 0;//points to the next item to be added from the right array
        for (int noOfItemsAdded = 0; noOfItemsAdded < totalNoOfItems; noOfItemsAdded++){
            //noOfItemsAdded is how many items have been added so far to merged list
            if(indexLeft >= left.length){//is left array exhausted
                mergedList[noOfItemsAdded] = right[indexRight];
                indexRight++;
            } else if (indexRight >= right.length){//is right array exhausted
                mergedList[noOfItemsAdded] = left[indexLeft];
                indexLeft++;
            } else if (left[indexLeft][0].compareToIgnoreCase(right[indexRight][0]) <= 0){//is next item in left array comes before first item in the right array
                mergedList[noOfItemsAdded] = left[indexLeft];
                indexLeft++;
            } else{//is next item in right array comes before first item in the left array
                mergedList[noOfItemsAdded] = right[indexRight];
                indexRight++;
            }
        }
        return mergedList;
    }
}