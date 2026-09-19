package dijkstra;

import hashtable.Hashtable;
import main.Database;
import org.junit.Test;
import static org.junit.Assert.*;

public class DijkstraTest {
    /**
     * Test 105 - getDistancesFromPlace Test
     * The Set 4 database is modelled after the Dijkstra dry run.
     * This test calls getDistancesFromPlace() from place 6, identical to the dry run.
     * Thus, the resulting costs hashtable should match that of the dry run.
     */
    @Test
    public void testGetDistancesFromPlace() {
        final float ERROR = 0.01f; //floating point error
        Database database = new Database("Set4.db");
        int start = 6;
        Dijkstra instance = new Dijkstra(database);
        Hashtable<Integer, Float> distances = instance.getDistancesFromPlace(start);
        assertEquals(0.5f, distances.item(1), ERROR); //0.01 there due to floating point error
        assertEquals(0.8f, distances.item(2), ERROR);
        assertEquals(1.3f, distances.item(3), ERROR);
        assertEquals(2.1f, distances.item(4), ERROR);
        assertEquals(1.7f, distances.item(5), ERROR);
        assertEquals(0.0f, distances.item(6), ERROR);
        assertEquals(3.5f, distances.item(7), ERROR);
        assertEquals(2.3f, distances.item(8), ERROR);
        assertEquals(2.3f, distances.item(9), ERROR);
    }
    /**
     * Test 106 - Path test
     * The Set 4 database is modelled after the Dijkstra dry run.
     * Taxicab 1 is at Place 6; Customer 1 is at Place 9.
     * This test finds the shortest path from Taxicab 1 to Customer 1, which
     * mimics the scenario for the Dijkstra dry-run.
     * Thus the getPathFromTaxicabToCustomer() should return a result that matches
     * that dry-run.
     */
    @Test
    public void testGetPathFromTaxicabToCustomer() {
        Database database = new Database("Set4.db");
        Dijkstra instance = new Dijkstra(database);
        int taxicabID = 1;
        int customerID = 1;
        int[] result = instance.getPathFromTaxicabToCustomer(taxicabID, customerID);
        assertArrayEquals(new int[]{6,1,2,4,9}, result);
    }
    /**
     * Test 107 - Distances array test
     * By performing Dijsktra's algorithm manually, the distances array given is
     * reached.
     */
    @Test
    public void testGenerateDistanceArray() {
        final float FP_ERROR = 0.01f;
        Database database = new Database("Set5.db");
        Dijkstra dijkstra = new Dijkstra(database);
        float[][] expArray = {
            {7.5f,11.5f,7.6f},
            {4.3f,8.3f,4.4f},
            {9,11,7.9f}
        };
        float[][] distanceArray = dijkstra.generateDistanceArray();
        assertArrayEquals(expArray[0], distanceArray[0], FP_ERROR);
        assertArrayEquals(expArray[0], distanceArray[0], FP_ERROR);
        assertArrayEquals(expArray[0], distanceArray[0], FP_ERROR);
    }
    
}
