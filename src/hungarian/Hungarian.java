package hungarian;

import java.util.Arrays;
import main.Database;
import queue.Queue;

/**
 * Generates an optimal allocation of taxicabs to customers
 */
public class Hungarian {

    private Database database;
    private final int NOT_FOUND = -1;
    /**
     * How many taxicabs/customer there are
     */
    private final int SIZE;
    /**
     * A 2D array denoting the costs/distances between taxicabs (rows) and (customers).
     */
    private float[][] costs;
    /**
     * Records the position of starred zeros. A true value indicates the
     * existence of a starred zero.
     */
    private boolean[][] isStarred;
    /**
     * Records the position of primed zeros. A true value indicates the
     * existence of a primed zero.
     */
    private boolean[][] isPrimed;
    /**
     * Records which columns are covered. A true value indicates that the column
     * at that index is covered.
     */
    private boolean[] isColumnCovered;
    /**
     * Records which rows are covered. A true value indicates that the row at
     * that index is covered.
     */
    private boolean[] isRowCovered;
    /**
     * An unaltered record of the distances between each taxicab and customer.
     */
    private final float[][] ORIGINAL_COSTS;
    
    /**Used for testing*/
    Hungarian(int size, float[][] costs, boolean[][] isStarred, boolean[][] isPrimed, boolean[] isColumnCovered, boolean[] isRowCovered){
        this.database = null;
        this.SIZE = size;
        this.costs = costs;
        this.isStarred = isStarred;
        this.isPrimed = isPrimed;
        this.isColumnCovered = isColumnCovered;
        this.isRowCovered = isRowCovered;
        ORIGINAL_COSTS = null;
    }
    /**Used for testing*/
    float[][] getCosts(){
        return costs;
    }
    /**Used for testing*/
    boolean[][] getIsStarred(){
        return isStarred;
    }
    /**Used for testing*/
    boolean[] getIsColumnCovered(){
        return isColumnCovered;
    }
    /**Used for testing*/
    boolean[] getIsRowCovered(){
        return isRowCovered;
    }
    /**
     * This constructor will also find an optimal allocation of taxicabs to
     * customers, which minimises the total distance travelled by taxicabs. You
     * must ensure that all places are connected together before instantiating
     * this class. Also check that there are an equal number of taxicabs and customers.
     *
     * @param database this will be used to get taxicab registration names,
     * customer names and phone numbers
     * @param originalCosts a 2D array that contains the distances between
     * each taxicab and customer The row index of a distance will refer to the
     * index of the taxicab ID of the taxicab, when all taxicabs are sorted by
     * their ID. Likewise, the column index of a distance will refer to the
     * index of the customer ID of the customer, when all customers are sorted
     * by their ID.
     */
    public Hungarian(Database database, float[][] originalCosts) {
        this.database = database;
        this.ORIGINAL_COSTS = originalCosts;
        SIZE = originalCosts.length;
        performAllocation();
    }

    /**
     * Determines an optimal allocation of taxicabs to customers, which
     * minimises the total distance travelled by taxicabs.
     */
    private void performAllocation() {
        costs = new float[SIZE][SIZE];
        for(int i = 0; i < SIZE; i++){
            costs[i] = ORIGINAL_COSTS[i].clone();
        }
        isStarred = new boolean[SIZE][SIZE]; //default value in array is false;
        isPrimed = new boolean[SIZE][SIZE];
        isColumnCovered = new boolean[SIZE];
        isRowCovered = new boolean[SIZE];
        for (int i = 0; i < SIZE; i++) {//subtracts smallest cost of each row, from each row
            float smallestCost = getSmallestCostFromRow(i);
            addValueToRow(-1 * smallestCost, i); //# multiplied by -1, so it gets subtracted;
        }
        starZeros();//stars zeros, such that no starred zeros share a column
        coverColumnsContainingStarredZeros();
        while (!areAllColumnsCovered()) {//loop repeats until allocation found
            int[] uncoveredZero; //stores position of zero
            int starredZeroColumn;
            boolean doesPrimedZeroShareRowWithStarredZero;
            do {
                while (getPositionOfUncoveredZero()[0] == NOT_FOUND) { //repeats until an uncovered zero exists
                    makeMoreZeros();
                }
                uncoveredZero = getPositionOfUncoveredZero();
                isPrimed[uncoveredZero[0]][uncoveredZero[1]] = true;//primes uncovered zero
                starredZeroColumn = getPositionOfFirstStarredZeroInRow(uncoveredZero[0]);
                doesPrimedZeroShareRowWithStarredZero = (starredZeroColumn != NOT_FOUND);
                if (doesPrimedZeroShareRowWithStarredZero) {
                    isRowCovered[uncoveredZero[0]] = true;
                    isColumnCovered[starredZeroColumn] = false;
                }
            } while (doesPrimedZeroShareRowWithStarredZero);
            changeStarredZeros(uncoveredZero);
            coverColumnsContainingStarredZeros();
        }
    }

    /**
     * Finds a chain of primed and starred zeros, and swaps them, thus
     * increasing the number of columns that contain a starred zero.
     *
     * @param primedZero a 2D array containing the position a primed zero. The
     * first value refers to the row index and the second value refers to the
     * column index.
     */
    private void changeStarredZeros(int[] primedZero) {
        Queue<int[]> zeroList = new Queue();//each item is 2D int array denoting the zero’s position;
        zeroList.add(primedZero); //add first primed zero
        while (getPositionOfFirstStarredZeroInColumn(primedZero[1]) != NOT_FOUND) {//whilst there is a starred zero in the primed zero's column
            int starredZeroRow = getPositionOfFirstStarredZeroInColumn(primedZero[1]);
            int[] starredZero = new int[]{starredZeroRow, primedZero[1]};
            zeroList.add(starredZero); //add starred zero
            int newPrimedZeroColumn = getPositionOfFirstPrimedZeroInRow(starredZeroRow);
            primedZero = new int[]{starredZeroRow, newPrimedZeroColumn};
            zeroList.add(primedZero);//add primed zero
        }
        int length = zeroList.length();
        for (int i = 0; i < length; i++) {
            int[] currentZero = zeroList.pop();
            if (i % 2 == 0) {//zeros at even indices are primed
                isStarred[currentZero[0]][currentZero[1]] = true; //star primed zero
            } else {//zeros at odd indices are starred
                isStarred[currentZero[0]][currentZero[1]] = false; //unstar starred zero
            }
        }
        isPrimed = new boolean[SIZE][SIZE]; //remove all primed zeros
        isColumnCovered = new boolean[SIZE]; //uncover all columns
        isRowCovered = new boolean[SIZE]; //uncover all rows
    }

    /**
     * Makes more uncovered zeros. Finds the smallest uncovered cost, subtracts
     * this from each uncovered column, and adds it to each covered row.
     */
    private void makeMoreZeros() {
        float smallestCost = getSmallestCostFromUncoveredCosts();
        for (int index = 0; index < SIZE; index++) {
            if (isRowCovered[index]) {
                addValueToRow(smallestCost, index);
            }
            if (!isColumnCovered[index]) {
                addValueToColumn(-1 * smallestCost, index); //multiplication by -1 allows it to do subtraction
            }
        }
    }

    /**
     * Returns a String array containing information about the optimal
     * allocation of taxicabs to customers. The first column contains the
     * registration numbers of each taxicab. The second column contains the
     * customer names of the customers allocated to each taxicab. The third
     * column contains the phone numbers of the customers allocated to each
     * taxicab. The forth column contains the distance (in km) that each taxicab
     * takes to reach its customer.
     */
    public String[][] getAllocation() {
        String[][] allocation = new String[SIZE][4];
        int[] taxicabIDs = database.getListOfIDs("tblTaxicab");
        int[] customerIDs = database.getListOfIDs("tblCustomer");
        for (int i = 0; i < SIZE; i++) {
            int taxicabID = taxicabIDs[i];
            int j = getPositionOfFirstStarredZeroInRow(i);
            int customerID = customerIDs[j];
            allocation[i][0] = database.getStringValueFromID("RegstNum", "tblTaxicab", taxicabID);
            allocation[i][1] = database.getStringValueFromID("CustName", "tblCustomer", customerID);
            allocation[i][2] = database.getStringValueFromID("PhoneNum", "tblCustomer", customerID);
            allocation[i][3] = String.format("%.2f", ORIGINAL_COSTS[i][j]);//add distance between taxicab and customer, %.2f rounds distance to 2dp
        }
        return allocation;
    }

    /**
     * Returns the customer allocated to a specified taxicab.
     *
     * @param taxicabID the taxicab's ID
     * @return The customer's ID
     */
    public int getCustomerAllocatedToTaxicab(int taxicabID) {
        int[] taxicabIDs = database.getListOfIDs("tblTaxicab");
        int taxicabIndex = binarySearch(taxicabIDs, taxicabID);
        int allocatedCustomerIndex = getPositionOfFirstStarredZeroInRow(taxicabIndex);
        int customerID = database.getListOfIDs("tblCustomer")[allocatedCustomerIndex];
        return customerID;
    }

    /**
     * Returns the distance that a given taxicab will travel to reach its
     * allocated customer.
     *
     * @param taxicabID the taxicab's ID
     */
    public float getDistanceOfTaxicab(int taxicabID) {
        int[] taxicabIDs = database.getListOfIDs("tblTaxicab");
        int taxicabIndex = binarySearch(taxicabIDs, taxicabID);
        int allocatedCustomerIndex = getPositionOfFirstStarredZeroInRow(taxicabIndex);
        float distanceTravelledByTaxicab = ORIGINAL_COSTS[taxicabIndex][allocatedCustomerIndex];
        return distanceTravelledByTaxicab;
    }

    /**
     * Finds the position of a specified item in an ordered array. If the item
     * isn't found, then NOT_FOUND is returned.
     *
     * @return the index of the item in the array
     */
    int binarySearch(int[] array, int item) {
        int indexItem = NOT_FOUND;
        int middleIndex = array.length / 2; //integer division rounds down the result
        if (array.length != 0) {//if the array is empty, then the item isn't in the array and NOT_FOUND is returned
            int middleItem = array[middleIndex];
            if (item == middleItem) {
                indexItem = middleIndex;
            } else if (item < middleItem) {//item is in the left half, if it is in the list
                int[] leftHalf = Arrays.copyOfRange(array, 0, middleIndex);
                indexItem = binarySearch(leftHalf, item);//if NOT_FOUND is returned, indexItem is assigned NOT_FOUND
            } else {//item is in the right half, if it is in the list
                int[] rightHalf = Arrays.copyOfRange(array, middleIndex + 1, array.length);
                int indexOfItemInRightHalf = binarySearch(rightHalf, item);
                indexItem = (indexOfItemInRightHalf == NOT_FOUND) ? NOT_FOUND : middleIndex + 1 + indexOfItemInRightHalf; //if NOT_FOUND is returned, indexItem is assigned NOT_FOUND
            }
        }
        return indexItem;
    }

    /**
     * Sums up the total distance travelled by all taxicabs when following the
     * optimal allocation
     */
    public float getTotalDistance() {
        float totalDistance = 0;
        for (int taxicab = 0; taxicab < SIZE; taxicab++) {
            int allocatedCustomer = getPositionOfFirstStarredZeroInRow(taxicab);
            totalDistance += ORIGINAL_COSTS[taxicab][allocatedCustomer];
        }
        return totalDistance;
    }

    /**
     * Adds the given value to each cost in the given row
     */
    void addValueToRow(float value, int row) {
        for (int j = 0; j < SIZE; j++) {//traverse row horizontally
            costs[row][j] += value;
        }
    }

    /**
     * Adds the given value to each cost in the given column
     */
    void addValueToColumn(float value, int column) {
        for (int i = 0; i < SIZE; i++) {//traverse column vertically
            costs[i][column] += value;
        }
    }

    /**
     * Returns the smallest cost in the given row
     */
    float getSmallestCostFromRow(int row) {
        float smallestCost = Float.MAX_VALUE;
        for (int j = 0; j < SIZE; j++) {//traverse row horizontally
            if (costs[row][j] < smallestCost) {
                smallestCost = costs[row][j];
            }
        }
        return smallestCost;
    }

    /**
     * Finds the smallest costs that is not covered by any row or column.
     */
    float getSmallestCostFromUncoveredCosts() {
        float smallestCost = Float.MAX_VALUE;
        for (int i = 0; i < SIZE; i++) {//traverse each row
            if (!isRowCovered[i]) {//skip row if its covered
                for (int j = 0; j < SIZE; j++) {//traverse each column
                    if (!isColumnCovered[j] && (costs[i][j] < smallestCost)) {//skips column if covered
                        smallestCost = costs[i][j];
                    }
                }
            }
        }
        return smallestCost;
    }

    /**
     * Stars zeros, such that each column contains at most one starred zero.
     */
    void starZeros() {
        for (int i = 0; i < SIZE; i++) {//traverses each row
            boolean starrableZeroInRowFound = false;
            int j = 0;
            while (j < SIZE && !starrableZeroInRowFound) {//traverses each column until a zero has been starred
                boolean columnDoesntContainStarredZero = (getPositionOfFirstStarredZeroInColumn(j) == NOT_FOUND);
                if (costs[i][j] == 0 && columnDoesntContainStarredZero) {//checks if there are no other starred zeros in the column
                    isStarred[i][j] = true;//star this zero
                    starrableZeroInRowFound = true;
                }
                j++;
            }
        }
    }

    /**
     * Gets the column index of the first primed zero in a specified row. The
     * first primed zero is the primed zero that is in the smallest column
     * index. If there is no primed zero in the row, then NOT_FOUND is returned.
     *
     * @return the column index of the primed zero
     */
    int getPositionOfFirstPrimedZeroInRow(int row) {
        int positionOfPrimedZero = NOT_FOUND;
        int j = 0;
        while (j < SIZE && positionOfPrimedZero == NOT_FOUND) {//traverse each column until zero found
            if (isPrimed[row][j]) {
                positionOfPrimedZero = j;
            }
            j++;
        }
        return positionOfPrimedZero;
    }

    /**
     * Gets the row index of the first starred zero in a specified column. The
     * first starred zero is the starred zero that is in the smallest row index.
     * If there is no starred zero in the column, then NOT_FOUND is returned.
     *
     * @return the row index of the starred zero
     */
    int getPositionOfFirstStarredZeroInColumn(int column) {
        int positionOfStarredZero = NOT_FOUND;
        int i = 0;
        while (i < SIZE && positionOfStarredZero == NOT_FOUND) {//traverse each row until zero found
            if (isStarred[i][column]) {
                positionOfStarredZero = i;
            }
            i++;
        }
        return positionOfStarredZero;
    }

    /**
     * Gets the column index of the first starred zero in a specified row. The
     * first starred zero is the starred zero that is in the smallest column
     * index. If there is no starred zero in the row, then NOT_FOUND is
     * returned.
     *
     * @return the column index of the starred zero
     */
    int getPositionOfFirstStarredZeroInRow(int row) {
        int positionOfStarredZero = NOT_FOUND;
        int j = 0;
        while (j < SIZE && positionOfStarredZero == NOT_FOUND) {//traverse each column until zero found
            if (isStarred[row][j]) {
                positionOfStarredZero = j;
            }
            j++;
        }
        return positionOfStarredZero;
    }

    /**
     * Returns the position of the first zero, that is not in a covered row or
     * column. The first uncovered zero here is the uncovered zero that is in
     * the smallest row index, and then in the smallest column index. If there
     * is no uncovered zero, then the return value will be filled with NOT_FOUND
     * values.
     *
     * @return a 2D int array; the 0-index contains the row number and the
     * 1-index contains the column number.
     */
    int[] getPositionOfUncoveredZero() {
        boolean isZeroFound = false;
        int[] position = new int[]{NOT_FOUND, NOT_FOUND};
        int i = 0;
        while (i < SIZE && !isZeroFound) {//traversing each row, finishes if zero found
            int j = 0;
            while (j < SIZE && !isRowCovered[i] && !isZeroFound) {//traversing each column, finishes if zero found
                if (costs[i][j] == 0 && !isColumnCovered[j]) {
                    position = new int[]{i, j};
                    isZeroFound = true;
                }
                j++;
            }
            i++;
        }
        return position;
    }

    /**
     * Covers all columns that contain starred zeros
     */
    void coverColumnsContainingStarredZeros() {
        for (int j = 0; j < SIZE; j++) {//traversing each column
            boolean doesColumnContainStarredZero = (getPositionOfFirstStarredZeroInColumn(j) != NOT_FOUND);
            if (doesColumnContainStarredZero) {
                isColumnCovered[j] = true;
            }
        }
    }

    /**
     * Returns whether all columns are covered
     */
    boolean areAllColumnsCovered() {
        boolean areAllColumnsCovered = true;
        int j = 0;
        while (j < SIZE && areAllColumnsCovered) {//traversing each column
            if (!isColumnCovered[j]) {
                areAllColumnsCovered = false;
            }
            j++;
        }
        return areAllColumnsCovered;
    }
}
