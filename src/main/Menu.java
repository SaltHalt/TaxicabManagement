package main;

import dijkstra.Dijkstra;
import hungarian.Hungarian;
import component_detector.ComponentDetector;
import java.util.Arrays;

public class Menu {

    private Database database;
    private IO io;

    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.mainMenu();
    }

    public void mainMenu() {
        database = new Database("Set5.db");
        io = new IO(database);
        final String[] MAIN_MENU = new String[]{
            "Add/Edit/Remove a Customer",
            "Add/Edit/Remove a Taxicab",
            "Add/Remove a Place",
            "Add/Remove a Road",
            "View Data",
            "Find disconnected places",
            "Show taxicab-customer allocation",
            "Get taxicab directions",
            "Exit"};
        final int CUSTOMER = 1;
        final int TAXICAB = 2;
        final int PLACE = 3;
        final int ROAD = 4;
        final int VIEW_DATA = 5;
        final int DISCONNECTED_COMPONENTS = 6;
        final int ALLOCATION = 7;
        final int TAXICAB_DIRECTIONS = 8;
        final int EXIT_PROGRAM = 9;
        int choice;
        do {
            io.getUserInput("Please press a button to continue");
            choice = io.getMenuInput("MAIN MENU", MAIN_MENU);
            switch (choice) {
                case CUSTOMER:
                    customerMenu();
                    break;
                case TAXICAB:
                    taxicabMenu();
                    break;
                case PLACE:
                    placeMenu();
                    break;
                case ROAD:
                    roadMenu();
                    break;
                case VIEW_DATA:
                    viewDataMenu();
                    break;
                case DISCONNECTED_COMPONENTS:
                    viewDisconnectedComponents();
                    break;
                case ALLOCATION:
                    viewAllocation();
                    break;
                case TAXICAB_DIRECTIONS:
                    taxicabDirectionsMenu();
                    break;
            }
            System.out.println("\n");
        } while (choice != EXIT_PROGRAM);
        System.out.println("Program terminated");
        database.closeConnection();
    }

    public void customerMenu() {
        final String[] CUSTOMER_MENU = new String[]{
            "Add a Customer",
            "Edit a Customer's Position",
            "Remove a Customer",
            "Exit"};
        final int ADD_CUSTOMER = 1;
        final int EDIT_CUSTOMER = 2;
        final int REMOVE_CUSTOMER = 3;
        int choice = io.getMenuInput("CUSTOMER MENU", CUSTOMER_MENU);
        switch (choice) {
            case ADD_CUSTOMER:
                addCustomer();
                break;
            case EDIT_CUSTOMER:
                editCustomer();
                break;
            case REMOVE_CUSTOMER:
                removeCustomer();
                break;
        }
    }

    public void addCustomer() {
        System.out.println("ADD CUSTOMER\n");
        if (database.isTableEmpty("tblPlace")) {//if there aren't any places, then there is nowhere to place the customers at
            System.out.println("There are no places in the system.\nAdd a place before adding a customer");
        } else {
            String namePrompt = io.makePrompt("full name", "customer");
            String phoneNumberPrompt = io.makePrompt("phone number", "customer");
            String positionPrompt = io.makePrompt("postcode of the position", "customer"
                    + "\nThe position must already be a registered location");
            String[] nameAndPhoneNum = io.getCustomerInput(new String[]{namePrompt, phoneNumberPrompt}, false);
            String position = io.getPositionInput(positionPrompt, true);
            database.addCustomer(nameAndPhoneNum[0], nameAndPhoneNum[1], position);
            System.out.println("Customer added");
        }
    }

    /**
     * Allows the user to edit a customer's position
     */
    public void editCustomer() {
        System.out.println("EDIT CUSTOMER\n");
        if (database.isTableEmpty("tblCustomer")) {//if there are no customers to edit
            System.out.println("There are no customers in the system.");
        } else {
            String namePrompt = io.makePrompt("full name", "customer");
            String phoneNumberPrompt = io.makePrompt("phone number", "customer");
            String positionPrompt = io.makePrompt("postcode of the new position", "customer"
                    + "\nThe position must already be a registered location");
            String[] nameAndPhoneNum = io.getCustomerInput(new String[]{namePrompt, phoneNumberPrompt}, true);
            String newPosition = io.getPositionInput(positionPrompt, true);
            database.editPosition("tblCustomer", newPosition, nameAndPhoneNum);
            System.out.println("Customer's position edited");
        }
    }

    public void removeCustomer() {
        System.out.println("REMOVE CUSTOMER\n");
        if (database.isTableEmpty("tblCustomer")) {//if there are no customers to delete
            System.out.println("There are no customers in the system.");
        } else {
            String namePrompt = io.makePrompt("full name", "customer");
            String phoneNumberPrompt = io.makePrompt("phone number", "customer");
            String[] nameAndPhoneNum = io.getCustomerInput(new String[]{namePrompt, phoneNumberPrompt}, true);
            database.removeItem("tblCustomer", nameAndPhoneNum);
            System.out.println("Customer removed");
        }
    }

    public void taxicabMenu() {
        final String[] TAXICAB_MENU = new String[]{
            "Add a Taxicab",
            "Edit a Taxicab's Position",
            "Remove a Taxicab",
            "Exit"};
        final int ADD_TAXICAB = 1;
        final int EDIT_TAXICAB = 2;
        final int REMOVE_TAXICAB = 3;
        int choice = io.getMenuInput("TAXICAB MENU", TAXICAB_MENU);
        switch (choice) {
            case ADD_TAXICAB:
                addTaxicab();
                break;
            case EDIT_TAXICAB:
                editTaxicab();
                break;
            case REMOVE_TAXICAB:
                removeTaxicab();
                break;
        }
    }

    public void addTaxicab() {
        System.out.println("ADD TAXICAB\n");
        if (database.isTableEmpty("tblPlace")) {//if there aren't any places, then there is nowhere to place the taxicabs at
            System.out.println("There are no places in the system.\nAdd a place before adding a taxicab");
        } else {
            String regstNumPrompt = io.makePrompt("registration number", "taxicab");
            String positionPrompt = io.makePrompt("postcode of the position", "taxicab"
                    + "\nThe position must already be a registered location");
            String regstNum = io.getTaxicabInput(regstNumPrompt, false);
            String position = io.getPositionInput(positionPrompt, true);
            database.addTaxicab(regstNum, position);
            System.out.println("Taxicab added");
        }
    }

    public void editTaxicab() {
        System.out.println("EDIT TAXICAB\n");
        if (database.isTableEmpty("tblTaxicab")) {//if there are no taxicabs to edit
            System.out.println("There are no taxicabs in the system.");
        } else {
            String regstNumPrompt = io.makePrompt("registration number", "taxicab");
            String positionPrompt = io.makePrompt("postcode of the new position", "taxicab"
                    + "\nThe position must already be a registered location");
            String regstNum = io.getTaxicabInput(regstNumPrompt, true);
            String newPosition = io.getPositionInput(positionPrompt, true);
            database.editPosition("tblTaxicab", newPosition, regstNum);
            System.out.println("Taxicab's position edited");
        }
    }

    public void removeTaxicab() {
        System.out.println("REMOVE TAXICAB\n");
        if (database.isTableEmpty("tblTaxicab")) {//if there are no taxicabs to remove
            System.out.println("There are no taxicabs in the system.");
        } else {
            String regstNumPrompt = io.makePrompt("registration number", "taxicab");
            String regstNum = io.getTaxicabInput(regstNumPrompt, true);
            database.removeItem("tblTaxicab", regstNum);
            System.out.println("Taxicab removed");
        }
    }

    public void placeMenu() {
        final String[] PLACE_MENU = new String[]{
            "Add a Place",
            "Remove a Place",
            "Exit"
        };
        final int ADD_PLACE = 1;
        final int REMOVE_PLACE = 2;
        int choice = io.getMenuInput("PLACE MENU", PLACE_MENU);
        switch (choice) {
            case ADD_PLACE:
                addPlace();
                break;
            case REMOVE_PLACE:
                removePlace();
                break;
        }
    }

    public void addPlace() {
        System.out.println("ADD PLACE\n");
        String positionPrompt = io.makePrompt("postcode", "place");
        String position = io.getPositionInput(positionPrompt, false);
        database.addPlace(position);
        System.out.println("Place added");
    }

    public void removePlace() {
        System.out.println("REMOVE PLACE\n");
        if (database.isTableEmpty("tblPlace")) {//if there are no places to remove
            System.out.println("There are no places in the system.");
        } else {
            String positionPrompt = io.makePrompt("postcode", "place");
            String position = io.getPositionInput(positionPrompt, true);
            if (database.isPlaceOccupied(position)) {//place record can't be removed if it is being referenced by other tables
                System.out.println("This place is occupied by a taxicab, customer, or connected to a road, and can thus not be removed."
                        + "\nRemove the taxicab, customer or road first.");
            } else {
                database.removeItem("tblPlace", position);
                System.out.println("Place removed");
            }
        }
    }

    public void roadMenu() {
        final String[] ROAD_MENU = new String[]{
            "Add a Road",
            "Remove a Road",
            "Exit"
        };
        final int ADD_ROAD = 1;
        final int REMOVE_ROAD = 2;
        int choice = io.getMenuInput("ROAD MENU", ROAD_MENU);
        switch (choice) {
            case ADD_ROAD:
                addRoad();
                break;
            case REMOVE_ROAD:
                removeRoad();
                break;
        }
    }

    public void addRoad() {
        System.out.println("ADD ROAD\n");
        if (database.isTableEmpty("tblPlace")) {//if there are no places to make a road between
            System.out.println("There are no places in the system.\nAdd a place before adding a taxicab.");
        } else {
            String startPositionPrompt = io.makePrompt("start position", "road");
            String endPositionPrompt = io.makePrompt("end position", "road");
            String lengthPrompt = io.makePrompt("length", "road");
            String place0 = io.getPositionInput(startPositionPrompt, true);
            String place1 = io.getPositionInput(endPositionPrompt, true);
            float length = io.getRealInput(lengthPrompt);
            boolean isInputInTable = database.doesTableContain("tblRoad", place0, place1);
            if (place0.equals(place1)) {
                System.out.println("You cannot make a road that joins a place to itself.");
            } else if (isInputInTable) {
                System.out.println("There is already a road joining those two places.");
            } else {
                database.addRoad(place0, place1, length);
                System.out.println("Road added");
            }
        }
    }

    public void removeRoad() {
        System.out.println("REMOVE ROAD\n");
        if (database.isTableEmpty("tblRoad")) {//if there are no roads to remove
            System.out.println("There are no roads in the system.");
        } else {
            String startPositionPrompt = io.makePrompt("start position", "road");
            String endPositionPrompt = io.makePrompt("end position", "road");
            String place0 = io.getPositionInput(startPositionPrompt, true);
            String place1 = io.getPositionInput(endPositionPrompt, true);
            boolean isInputInTable = database.doesTableContain("tblRoad", place0, place1);
            if (!isInputInTable) {
                System.out.println("There is no road joining those two places.");
            } else {
                database.removeItem("tblRoad", place0, place1);
                System.out.println("Road removed");
            }
        }
    }

    public void viewDataMenu() {
        final String[] VIEW_DATA_MENU = new String[]{
            "View Customers",
            "View Taxicabs",
            "View Places",
            "View Roads",
            "Exit"};
        final int CUSTOMER = 1;
        final int TAXICAB = 2;
        final int PLACE = 3;
        final int ROAD = 4;
        int choice = io.getMenuInput("VIEW DATA MENU", VIEW_DATA_MENU);
        String[][] results = null;
        switch (choice) {
            case CUSTOMER:
                results = database.returnTable("tblCustomer");
                break;
            case TAXICAB:
                results = database.returnTable("tblTaxicab");
                break;
            case PLACE:
                results = database.returnTable("tblPlace");
                break;
            case ROAD:
                results = database.returnTable("tblRoad");
                break;
        }
        String[] headers = results[0];
        String[][] data = Arrays.copyOfRange(results, 1, results.length);
        io.outputStringArray(headers, data);
    }

    public void viewDisconnectedComponents() {
        System.out.println("FIND DISCONNECTED COMPONENTS\n");
        if (database.isTableEmpty("tblPlace")) {
            System.out.println("There are no places in the system.");
        } else {
            ComponentDetector detector = new ComponentDetector(database);
            if (detector.isConnected()) {
                System.out.println("All places are connected together");
            } else {
                System.out.println("Not all places are connected together");
                String[][] components = detector.getComponents();
                for (int i = 0; i < components.length; i++) {
                    System.out.println("Component " + (i+1) + ":");
                    String[] postcodes = MergeSort.sort(components[i]);
                    for (String postcode : postcodes) {
                        System.out.println(postcode);
                    }
                    System.out.print("\n");
                }
            }
        }
    }

    public void viewAllocation() {
        System.out.println("SHOW TAXICAB-CUSTOMER ALLOCATION\n");
        if (database.isTableEmpty("tblPlace")) {
            System.out.println("There are no places in the system.");
        } else if (database.isTableEmpty("tblTaxicab")) {
            System.out.println("There are no taxicabs in the system.");
        } else if (database.getSizeOfTable("tblCustomer") != database.getSizeOfTable("tblTaxicab")) {
            System.out.println("There is not an equal number of customers and taxicabs."
                    + "Thus, an allocation cannot be performed.");
        } else {
            ComponentDetector detector = new ComponentDetector(database);
            if (!detector.isConnected()) {
                System.out.println("The places are disconnected, therefore, allocation cannot be performed ."
                        + "\nCreate roads to connect disconnected places to each other.");
            } else {
                Dijkstra dijkstra = new Dijkstra(database);
                float[][] distances = dijkstra.generateDistanceArray();
                Hungarian hungarian = new Hungarian(database, distances);
                float totalDist = hungarian.getTotalDistance();
                System.out.println("Total distance travelled is " + String.format("%.2f", totalDist) + "km");
                String[][] allocations = hungarian.getAllocation();
                String[][] sortedAllocations = MergeSort.sort(allocations);
                String[] headers = new String[]{"Taxicab", "Customer Name", "PhoneNumber", "Distance\\km"};
                io.outputStringArray(headers, sortedAllocations);
            }
        }
    }

    public void taxicabDirectionsMenu() {
        System.out.println("GET TAXICAB DIRECTIONS\n");
        if (database.isTableEmpty("tblPlace")) {
            System.out.println("There are no places in the system.");
        } else if (database.isTableEmpty("tblTaxicab")) {
            System.out.println("There are no taxicabs in the system.");
        } else if (database.getSizeOfTable("tblCustomer") != database.getSizeOfTable("tblTaxicab")) {
            System.out.println("There is not an equal number of customers and taxicabs. Thus, an allocation cannot be performed.");
        } else {
            ComponentDetector detector = new ComponentDetector(database);
            if (!detector.isConnected()) {
                System.out.println("The places are disconnected, therefore, allocation cannot be performed."
                        + "\nCreate roads to connect disconnected places to each other.");
            } else {
                Dijkstra dijkstra = new Dijkstra(database);
                float[][] distances = dijkstra.generateDistanceArray();
                Hungarian hungarian = new Hungarian(database, distances);
                String regstNumPrompt = io.makePrompt("registration number", "taxicab to get directions for");
                String regstNum = io.getTaxicabInput(regstNumPrompt, true);
                int taxicabID = database.getIDOfItem("tblTaxicab", regstNum);
                int customerID = hungarian.getCustomerAllocatedToTaxicab(taxicabID);
                Float distance = hungarian.getDistanceOfTaxicab(taxicabID);
                int[] path = dijkstra.getPathFromTaxicabToCustomer(taxicabID, customerID);
                System.out.printf("%.2fkm path of %s to %s (%s):\n", distance, //%.2f rounds distance to two decimal places
                        database.getStringValueFromID("RegstNum", "tblTaxicab", taxicabID), 
                        database.getStringValueFromID("CustName", "tblCustomer", customerID), 
                        database.getStringValueFromID("PhoneNum", "tblCustomer", customerID));
                System.out.println("Start");
                for (int place : path) {//print path
                    System.out.println(database.getStringValueFromID("Postcode", "tblPlace", place));
                }
                System.out.println("End");
            }
        }
    }

}
