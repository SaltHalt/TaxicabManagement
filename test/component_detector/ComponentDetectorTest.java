package component_detector;

import java.util.ArrayList;
import java.util.Arrays;
import main.Database;
import org.junit.Test;
import static org.junit.Assert.*;

public class ComponentDetectorTest {
    /**
     * Test 99 - Binary search test.
     */
    @Test
    public void testBinarySearch() {
        ComponentDetector detector = new ComponentDetector();
        ArrayList<Integer> array = new ArrayList<>(Arrays.asList(
                new Integer[]{-1,3,4,11,16,30,40,56,103}));
        assertEquals(5, detector.binarySearch(array, 30));
        assertEquals(-1, detector.binarySearch(array, 6));
    }
    /**
     * Test 100 - Get components test
     * This test uses the Set 2 database, which is made to mimic the
     * graph used in the Disconnected Components Detector Dry Run.
     * Thus, the results of that dry-run, {{1,4,3,2},{6,8},{7,9},{10}},
     * are used here. The PlaceIDs have been replaced by their respective
     * postcodes.
     */
    @Test
    public void testGetComponents() {
        Database database = new Database("Set2.db");
        ComponentDetector detector = new ComponentDetector(database);
        String[][] expComponents = new String[][]{
            {"A1 3CI","D8D 5PP","CC1 2KJ","B33 5BH"},
            {"EE88 1CX","G5 6FI"},
            {"FF4F 7TR","H7H 9MN"},
            {"I1 3ZX"}};
        assertArrayEquals(expComponents, detector.getComponents());
        database.closeConnection();
    }
    /**
     * Test 101 - Is connected test
     * The set 2 database is a disconnected database.
     * Set 4, as seen from its graph, is a connected database.
     */
    @Test
    public void testIsConnected() {
        Database database = new Database("Set2.db");
        ComponentDetector detector = new ComponentDetector(database);
        assertFalse(detector.isConnected());
        database.closeConnection();
        
        database = new Database("Set4.db");
        detector = new ComponentDetector(database);
        assertTrue(detector.isConnected());
        database.closeConnection();
    }
}
