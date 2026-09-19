package hungarian;

import main.Database;
import org.junit.Test;
import static org.junit.Assert.*;

public class HungarianTest {
    /**
     * Test 107 - Primed zero in row test
     * There is no primed zeros in the third row.
     */
    @Test
    public void testGetPositionOfFirstPrimedZeroInRow() {
        int size = 4;
        boolean[][] isPrimed = new boolean[][]{
            {true, false, false, false},
            {false, true, false, true},
            {false, false, false, false},
            {false, false, true, false}
        };
        Hungarian hungarian = new Hungarian(size, null, null, isPrimed, null, null);
        assertEquals(1, hungarian.getPositionOfFirstPrimedZeroInRow(1));
        assertEquals(-1, hungarian.getPositionOfFirstPrimedZeroInRow(2));
    }
    /**
     * Test 108 - Starred zero in column test
     * There is no starred zeros in the third column.
     */
    @Test
    public void testGetPositionOfFirstStarredZeroInColumn() {
        int size = 4;
        boolean[][] isStarred = new boolean[][]{
            {true, false, false, false},
            {false, true, false, true},
            {false, false, false, true},
            {false, false, false, false}
        };
        Hungarian hungarian = new Hungarian(size, null, isStarred, null, null, null);
        assertEquals(1, hungarian.getPositionOfFirstStarredZeroInColumn(3));
        assertEquals(-1, hungarian.getPositionOfFirstStarredZeroInColumn(2));
    }
    /**
     * Test 109 - Starred zero in row test
     * There is no starred zeros in the third row.
     */
    @Test
    public void testGetPositionOfFirstStarredZeroInRow() {
        int size = 4;
        boolean[][] isStarred = new boolean[][]{
            {true, false, false, false},
            {false, true, false, true},
            {false, false, false, false},
            {false, false, true, false}
        };
        Hungarian hungarian = new Hungarian(size, null, isStarred, null, null, null);
        assertEquals(1, hungarian.getPositionOfFirstStarredZeroInRow(1));
        assertEquals(-1, hungarian.getPositionOfFirstStarredZeroInRow(2));
    }
    /**
     * Test 110 - Uncovered zero position test
     * In the first costs matrix, there are two uncovered zeros, at {2,1} and {2,4}.
     * The first zero should be returned.
     * 
     * In the second costs matrix, there aren't any uncovered zeros, so {-1,-1}
     * should be returned.
     */
    @Test
    public void testGetPositionOfUncoveredZero() {
        int size = 5;
        boolean[] isColumnCovered = new boolean[]{true, false, true, true, false};
        boolean[] isRowCovered = new boolean[]{true, false, false, true, true};
        float[][] costs = new float[][]{
            {0, 2, 0, 2, 2},
            {1, 2, 3, 3, 3},
            {1, 0, 3, 5, 0},
            {6, 7, 8, 0, 1},
            {4, 3, 2, 0, 2}
        };
        Hungarian hungarian = new Hungarian(size, costs, null, null, isColumnCovered, isRowCovered);
        assertArrayEquals(new int[]{2, 1}, hungarian.getPositionOfUncoveredZero());

        costs = new float[][]{
            {0, 2, 0, 2, 2},
            {1, 2, 3, 3, 3},
            {1, 2, 3, 5, 3},
            {6, 7, 8, 0, 1},
            {4, 3, 2, 0, 2}
        };
        hungarian = new Hungarian(size, costs, null, null, isColumnCovered, isRowCovered);
        assertArrayEquals(new int[]{-1, -1}, hungarian.getPositionOfUncoveredZero());
    }
    /**
     * Test 111 - Cover columns test.
     */
    @Test
    public void testCoverColumnsContainingStarredZeros() {
        int size = 4;
        boolean[][] isStarred = new boolean[][]{
            {true, false, false, false},
            {false, false, false, true},
            {false, false, false, false},
            {false, false, true, false}
        };
        boolean[] isColumnCovered = new boolean[size];
        Hungarian hungarian = new Hungarian(size, null, isStarred, null, isColumnCovered, null);
        
        boolean[] expIsColumnCovered = new boolean[]{true, false, true, true};
        hungarian.coverColumnsContainingStarredZeros();
        assertArrayEquals(expIsColumnCovered, hungarian.getIsColumnCovered());
    }
    /**
     * Test 112 - All columns covered test.
     */
    @Test
    public void testAreAllColumnsCovered() {
        boolean[] isColumnCovered = new boolean[]{true, false, true, true};
        int size = 4;
        Hungarian hungarian = new Hungarian(size, null, null, null, isColumnCovered, null);
        assertFalse(hungarian.areAllColumnsCovered());
        
        isColumnCovered = new boolean[]{true, true, true, true};
        hungarian = new Hungarian(size, null, null, null, isColumnCovered, null);
        assertTrue(hungarian.areAllColumnsCovered());
    }
    /**
     * Test 113 - Star zeros test
     * The starred zeros should be at {0,0}, {2,1}, {3,3}
     */
    @Test
    public void testStarZeros() {
        int size = 5;
        float[][] costs = new float[][]{
            {0, 2, 0, 2, 2},
            {1, 2, 3, 3, 3},
            {1, 0, 3, 5, 0},
            {6, 7, 8, 0, 1},
            {4, 3, 2, 0, 2}
        };
        boolean[][] expIsStarred = new boolean[][]{
            {true, false, false, false, false},
            {false, false, false, false, false},
            {false, true, false, false, false},
            {false, false, false, true, false},
            {false, false, false, false, false}
        };
        boolean[][] isStarred = new boolean[size][size];
        Hungarian hungarian = new Hungarian(size, costs, isStarred, null, null, null);
        hungarian.starZeros();
        assertArrayEquals(expIsStarred, hungarian.getIsStarred());
    }
    /**
     * Test 114 - Add value to row test.
     */
    @Test
    public void testAddValueToRow() {
        int size = 5;
        float[][] costs = new float[][]{
            {0, 2, 0, 2, 2},
            {1, 2, 3, 3, 3},
            {1, 2, 3, 5, 3},
            {6, 7, 8, 0, 1},
            {4, 3, 2, 0, 2}
        };
        Hungarian hungarian = new Hungarian(size, costs, null, null, null, null);
        hungarian.addValueToRow(1.2f, 3);
        float[][] expCosts = new float[][]{
            {0, 2, 0, 2, 2},
            {1, 2, 3, 3, 3},
            {1, 2, 3, 5, 3},
            {7.2f, 8.2f, 9.2f, 1.2f, 2.2f},
            {4, 3, 2, 0, 2}
        };
        final float FP_ERROR = 0.01f;
        assertArrayEquals(expCosts[0], hungarian.getCosts()[0], FP_ERROR);
        assertArrayEquals(expCosts[1], hungarian.getCosts()[1], FP_ERROR);
        assertArrayEquals(expCosts[2], hungarian.getCosts()[2], FP_ERROR);
        assertArrayEquals(expCosts[3], hungarian.getCosts()[3], FP_ERROR);
        assertArrayEquals(expCosts[4], hungarian.getCosts()[4], FP_ERROR);
    }
    /**
     * Test 115 - Add value to column test.
     */
    @Test
    public void testAddValueToColumn() {
        int size = 5;
        float[][] costs = new float[][]{
            {0, 2, 0, 2, 2},
            {1, 2, 3, 3, 3},
            {1, 2, 3, 5, 3},
            {6, 7, 8, 0, 1},
            {4, 3, 2, 0, 2}
        };
        Hungarian hungarian = new Hungarian(size, costs, null, null, null, null);
        hungarian.addValueToColumn(0.5f, 1);
        float[][] expCosts = new float[][]{
            {0, 2.5f, 0, 2, 2},
            {1, 2.5f, 3, 3, 3},
            {1, 2.5f, 3, 5, 3},
            {6, 7.5f, 8, 0, 1},
            {4, 3.5f, 2, 0, 2}
        };
        final float FP_ERROR = 0.01f;
        assertArrayEquals(expCosts[0], hungarian.getCosts()[0], FP_ERROR);
        assertArrayEquals(expCosts[1], hungarian.getCosts()[1], FP_ERROR);
        assertArrayEquals(expCosts[2], hungarian.getCosts()[2], FP_ERROR);
        assertArrayEquals(expCosts[3], hungarian.getCosts()[3], FP_ERROR);
        assertArrayEquals(expCosts[4], hungarian.getCosts()[4], FP_ERROR);
    }
    /**
     * Test 116 - Smallest cost in row test.
     */
    @Test
    public void testGetSmallestCostFromRow() {
        int size = 5;
        float[][] costs = new float[][]{
            {0, 2, 0, 2, 2},
            {1, 2, 3, 3, 3},
            {1, 2, 3, 5, 3},
            {6, 7, 8, 0, 1},
            {4, 3, 2, 0, 2}
        };
        Hungarian hungarian = new Hungarian(size, costs, null, null, null, null);
        assertEquals(1, hungarian.getSmallestCostFromRow(2), 0.01);
    }
    /**
     * Test 117 - Smallest uncovered cost test.
     * Here, the smallest uncovered cost is 2.
     */
    @Test
    public void testGetSmallestCostFromUncoveredCosts() {
        int size = 4;
        boolean[] isColumnCovered = new boolean[]{true, false, true, true, false};
        boolean[] isRowCovered = new boolean[]{true, false, false, true, true};
        float[][] costs = new float[][]{
            {0, 2, 0, 2, 2},
            {1, 2, 3, 3, 3},
            {1, 2, 3, 5, 3},
            {6, 7, 8, 0, 1},
            {4, 3, 2, 0, 2}
        };
        Hungarian hungarian = new Hungarian(size, costs, null, null, isColumnCovered, isRowCovered);
        assertEquals(2, hungarian.getSmallestCostFromUncoveredCosts(), 0.01);
    }
    /**
     * Test 118 - Binary search test.
     * 41 is not in the list, so -1 should be returned when trying to find 41.
     */
    @Test
    public void testBinarySearch() {
        Hungarian hungarian = new Hungarian(0, null, null, null, null, null);
        int[] array = new int[]{5, 12, 16, 20, 22, 23, 24, 40};
        assertEquals(3, hungarian.binarySearch(array, 20));
        assertEquals(-1, hungarian.binarySearch(array, 41));
    }
    /**
     * Test 119 - Perform allocation test.
     * The first set of costs come from first the Hungarian walkthrough (see
     * the section Hungarian, Central Idea).
     * Thus, the first allocation reached should match the allocation reached in
     * that walkthrough.
     * 
     * The second set of costs come from second  the Hungarian walkthrough (see
     * the section Hungarian, More Detailed).
     * Thus, the second allocation reached should match the allocation reached 
     * in that walkthrough.
     */
    @Test
    public void testPerformAllocation() {
        float[][] costs = new float[][]{
            {3, 4, 1},
            {10, 2, 12},
            {1, 5, 6}
        };
        Hungarian hungarian = new Hungarian(null, costs);
        boolean[][] expAllocation = new boolean[][]{
            {false, false, true},
            {false, true, false},
            {true, false, false}
        };
        assertArrayEquals(expAllocation, hungarian.getIsStarred());

        costs = new float[][]{
            {0.5f, 1.2f, 1.7f},
            {1.4f, 2.5f, 0.3f},
            {1, 2, 4.1f}
        };
        hungarian = new Hungarian(null, costs);
        expAllocation = new boolean[][]{
            {false, true, false},
            {false, false, true},
            {true, false, false}
        };
        assertArrayEquals(expAllocation, hungarian.getIsStarred());
    }
    /**
     * Test 120 - Get Allocation test.
     * The set of costs here are the distances between each taxicab and customer
     * in the Set 5 database.
     * The allocation arrived at should be T1-C3 (7.6km), T2-C1 (4.3km), T3-C2 
     * (11km).
     */
    @Test
    public void testGetAllocation() {
        Database database = new Database("Set5.db");
        float[][] distanceArray = {
            {7.5f,11.5f,7.6f},
            {4.3f,8.3f,4.4f},
            {9,11,7.9f}
        };
        Hungarian hungarian = new Hungarian(database, distanceArray);
        String[][] expAllocation = new String[][]{
            {"FF99 ACD", "Lindsie Raymund", "+020 660 88888", "7.60"},
            {"ZI09 ERN", "Plimmory Crim", "078 552 68686", "4.30"},
            {"RY22 LJO", "Bobb Tone", "21 003 4442 5678", "11.00"}
        };
        String[][] result = hungarian.getAllocation();
        assertArrayEquals(expAllocation, result);
    }
    /**
     * Test 121 - Customer allocated to taxicab test.
     * The set of costs here are the distances between each taxicab and customer
     * in the Set 5 database.
     * Taxicab 2 should be allocated to Customer 1.
     */
    @Test
    public void testGetCustomerAllocatedToTaxicab() {
        Database database = new Database("Set5.db");
        float[][] distanceArray = {
            {7.5f,11.5f,7.6f},
            {4.3f,8.3f,4.4f},
            {9,11,7.9f}
        };
        Hungarian hungarian = new Hungarian(database, distanceArray);
        int taxicabID = 2;
        assertEquals(1, hungarian.getCustomerAllocatedToTaxicab(taxicabID));
    }
    
    /**
     * Test 122 - Distance travelled by taxicab test.
     * The set of costs here are the distances between each taxicab and customer
     * in the Set 5 database.
     * Taxicab 2 should travel 4.3km.
     */
    @Test
    public void testGetDistanceOfTaxicab() {
        Database database = new Database("Set5.db");
        float[][] distanceArray = {
            {7.5f,11.5f,7.6f},
            {4.3f,8.3f,4.4f},
            {9,11,7.9f}
        };
        Hungarian hungarian = new Hungarian(database, distanceArray);
        int taxicabID = 3;
        assertEquals(11, hungarian.getDistanceOfTaxicab(taxicabID), 0.01);
    }
    /**
     * Test 123 - Total distance travelled by all taxicabs test.
     * The set of costs here are the distances between each taxicab and customer
     * in the Set 5 database.
     * Altogether, the taxicabs should travel 22.9km.
     */
    @Test
    public void testGetTotalDistance() {
        Database database = new Database("Set5.db");
        float[][] distanceArray = {
            {7.5f,11.5f,7.6f},
            {4.3f,8.3f,4.4f},
            {9,11,7.9f}
        };
        Hungarian hungarian = new Hungarian(database, distanceArray);
        assertEquals(22.9f, hungarian.getTotalDistance(), 0.01f);
    }
}
