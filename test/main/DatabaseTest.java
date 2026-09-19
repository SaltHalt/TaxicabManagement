package main;

import org.junit.Test;
import static org.junit.Assert.*;

public class DatabaseTest {

    /**
     * Test 46 - Table Size test. tblPlace in Set 1 contains 6 places.
     */
    @Test
    public void testGetSizeOfTable() {
        Database database = new Database("Set1.db");
        assertEquals(6, database.getSizeOfTable("tblPlace"));
        database.closeConnection();
    }

    /**
     * Test 47 - isTableEmpty()test. tblRoad in Set 1 isn't empty. tblCustomer
     * in Set 3 is empty.
     */
    @Test
    public void testIsTableEmpty() {
        Database database = new Database("Set1.db");
        assertFalse(database.isTableEmpty("tblRoad"));
        database.closeConnection();
        database = new Database("Set3.db");
        assertTrue(database.isTableEmpty("tblCustomer"));
        database.closeConnection();
    }
    /**
     * Test 48 - Get ID test. 
     * In Set 1: 
     *  tblPlace contains a place with ID 4 and postcode 'WF4 4PP'. 
     *  tblCustomer contains a place with ID 2, name 'Bobb Tone' and phone number 
     *      '21 003 4442 5678'. 
     *  tblTaxicab contains a place with ID 3 and registration number 'RY22 LJO'.
     */
    @Test
    public void testGetIDOfItem() {
        Database database = new Database("Set1.db");
        assertEquals(4, database.getIDOfItem("tblPlace", "WF4 4PP"));
        assertEquals(2, database.getIDOfItem("tblCustomer", "Bobb Tone", "21 003 4442 5678"));
        assertEquals(3, database.getIDOfItem("tblTaxicab", "RY22 LJO"));
        database.closeConnection();
    }
    /**
     * Test 49 - Does Table Contain test. 
     * In Set 1: 
     *  tblPlace doesn't contain a place with postcode 'AA0 8FF'. 
     *  tblRoad does contain a place starting at 'M5 4EE' and ending at 'BA6 9BE'. 
     *  tblRoad doesn't contain a place starting at 'M5 4EE' and ending at 'WF4 4PP'.
     *  tblCustomer contains a place with ID 2, name 'Plimmory Crim' and phone number 
     *      '078 552 68686'. 
     *  tblTaxicab contains a place with ID 3 and registration number 'IJ83 MNB'.
     */
    @Test
    public void testDoesTableContain() {
        Database database = new Database("Set1.db");
        assertFalse(database.doesTableContain("tblPlace", "AA0 8FF"));

        assertTrue(database.doesTableContain("tblRoad", "M5 4EE", "BA6 9BE"));
        assertFalse(database.doesTableContain("tblRoad", "M5 4EE", "WF4 4PP"));

        assertTrue(database.doesTableContain("tblCustomer", "Plimmory Crim", "078 552 68686"));

        assertFalse(database.doesTableContain("tblTaxicab", "IJ83 MNB"));
        database.closeConnection();
    }
    /**
     * Test 50 - Is Place Occupied test. 
     * In Set 1: 
     *  The place with postcode 'DI88 9XP' doesn't have any taxicabs or customers on 
     *      it, nor do any roads connect to it.
     *  The place with postcode 'WF4 4PP' doesn't have any taxicabs or customers on 
     *      it, but 1 road does connect to it.
     */
    @Test
    public void testIsPlaceOccupied() {
        Database database = new Database("Set1.db");
        assertFalse(database.isPlaceOccupied("DI88 9XP"));
        assertTrue(database.isPlaceOccupied("WF4 4PP"));
        database.closeConnection();
    }
    /**
     * Test 51 - List of IDs test.
     * tblCustomer in Set 1 contains customers with IDs 1,2,3
     * tblPlace in Set 2 contains customers with IDs 1,2,3,4,6,7,8,9,10
     * 
     * The second test shows that the getListOfIDs method can handle when certain 
     *      IDs (i.e. ID 5) are missing, which may occur if the user has deleted
     *      items in the past.
     */
    @Test
    public void testGetListOfIDs() {
        Database database = new Database("Set1.db");
        assertArrayEquals(new int[]{1, 2, 3}, database.getListOfIDs("tblCustomer"));
        database.closeConnection();
        database = new Database("Set2.db");
        assertArrayEquals(new int[]{1, 2, 3, 4, 6, 7, 8, 9, 10}, database.getListOfIDs("tblPlace"));
        database.closeConnection();
    }
    /**
     * Test 52 - List of Positions test.
     * tblCustomer in Set 1 contains customers with IDs 1,2,3 and positions 3,5,1, respectively.
     */
    @Test
    public void testGetListOfPositions() {
        Database database = new Database("Set1.db");
        assertArrayEquals(new int[]{3, 5, 1}, database.getListOfPositions("tblTaxicab"));
        database.closeConnection();
    }
    /**
     * Test 53 - PositionID from ID test.
     * tblCustomer in Set 1 contains a customer with ID 2, whose position is 3
     */
    @Test
    public void testGetPositionIDFromID() {
        Database database = new Database("Set1.db");
        assertEquals(3, database.getPositionIDFromID("tblCustomer", 2));
        database.closeConnection();
    }
    /**
     * Test 54 - String Value from ID test.
     * tblCustomer in Set 1 contains a customer with ID 3, whose name is 'Lindsie Raymund'
     */
    @Test
    public void testGetStringValueFromID() {
        Database database = new Database("Set1.db");
        assertEquals("Lindsie Raymund", database.getStringValueFromID("CustName", "tblCustomer", 3));
        database.closeConnection();
    }
    /**
     * Test 55 - List of Neighbours test.
     * In tblCustomer, Set 2:
     * There is a place with ID 1, whose neighbours have IDs 2,3,4
     * Similarly, there is a place with ID 10, which doesn't have any neighbours.
     * The second test shows that the listOfNeighbours method can handle when a place 
     *  has no neighbours - which could occur if a place was recently added.
     */
    @Test
    public void testListOfNeighbours() {
        Database database = new Database("Set2.db");
        assertArrayEquals(new int[]{2, 3, 4}, database.listOfNeighbours(1));
        assertArrayEquals(new int[]{}, database.listOfNeighbours(10));
        database.closeConnection();

    }
    /**
     * Test 56 - Distance Between Places test.
     * Set 1 contains two places with IDs 2 and 4, which are connected together by a 
     *  road of length 0.09m.
     */
    @Test
    public void testGetDistanceBetweenPlaces() {
        Database database = new Database("Set1.db");
        assertEquals(0.09f, database.getDistanceBetweenPlaces(2, 4), 0.0);
        database.closeConnection();
    }
}
