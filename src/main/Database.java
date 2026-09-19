package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {

    private Connection conn = null;

    /**
     * Opens a connection to a {@code TaxicabDatabase.db}
     * @param filePath the relative file url to the database file
     */
    public Database(String filePath) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + filePath);
            conn.setAutoCommit(false);//So transactions need to be committed manually.
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Error occurred when connecting to database.");
            System.out.println("Database relative filepath: "+ filePath);
            System.out.println("What led to the error:\n");
            ex.printStackTrace();
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Closes the connection
     */
    public void closeConnection() {
        try {
            conn.close();
        } catch (SQLException ex) {
            System.out.println("Error occurred when disconnecting from database.");
            System.out.println("What led to the error:\n");
            ex.printStackTrace();
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Executes the passed SQL statement
     */
    private void executeSqlStatement(String sql) {
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(sql);
            statement.close();
            conn.commit();
        } catch (SQLException ex) {
            System.out.println("Error occurred when executing an SQL statement.");
            System.out.println("SQL statement: " + sql);
            System.out.println("What led to the error:\n");
            ex.printStackTrace();
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns the string scalar produced after querying the passed SQL query
     */
    private String getStringScalar(String sql) {
        String scalar = "";
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();
            scalar = resultSet.getString(1);
            resultSet.close();
            statement.close();
        } catch (SQLException ex) {
            System.out.println("Error occurred when querying the database to get a string value.");
            System.out.println("SQL Query: " + sql);
            System.out.println("What led to the error:\n");
            ex.printStackTrace();
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return scalar;
    }

    /**
     * Returns the integer scalar produced after querying the passed SQL query.
     * Returns -1 if the query returns no scalar.
     */
    private int getIntScalar(String sql) {
        int scalar = -1;
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();
            scalar = resultSet.getInt(1);
            resultSet.close();
            statement.close();
        } catch (SQLException ex) {
            System.out.println("Error occurred when querying the database to get an integer value.");
            System.out.println("SQL Query: " + sql);
            System.out.println("What led to the error:\n");
            ex.printStackTrace();
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return scalar;
    }

    /**
     * Returns the real scalar produced after querying the passed SQL query.
     * Returns -1 if the query returns no scalar.
     */
    private float getRealScalar(String sql) {
        float scalar = -1;
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();
            scalar = resultSet.getFloat(1);
            resultSet.close();
            statement.close();
        } catch (SQLException ex) {
            System.out.println("Error occurred when querying the database to get a real value.");
            System.out.println("SQL Query: " + sql);
            System.out.println("What led to the error:\n");
            ex.printStackTrace();
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return scalar;
    }

    /**
     * Returns the integer array produced after querying the passed SQL query
     */
    private int[] getIntArray(String sql) {
        int[] intArray = null;
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            ArrayList<Integer> results = new ArrayList<>();
            while(resultSet.next()){//put column into arraylist
                results.add(resultSet.getInt(1));
            }
            
            intArray = new int[results.size()];
            for(int i = 0; i < results.size(); i++){
                intArray[i] = results.get(i);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException ex) {
            System.out.println("Error occurred when querying the database to get a table column.");
            System.out.println("SQL Query: " + sql);
            System.out.println("What led to the error:\n");
            ex.printStackTrace();
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return intArray;
    }

    /**
     * Returns the results of the passed SQL query, as a 2D string array.
     * The first row contains a list of the headers in the query
     */
    private String[][] getQueryResults(String sqlQuery) {
        String[][] output = null;
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            ResultSetMetaData metadata = resultSet.getMetaData();
            int columnCount = metadata.getColumnCount();
            ArrayList<String[]> results = new ArrayList<>();
            String[] row = new String[columnCount];
            for (int j = 1; j <= columnCount; j++) {//adds field headers
                row[j-1] = metadata.getColumnLabel(j);
            }
            results.add(row.clone());
            while (resultSet.next()) {//iterates through records
                for (int i = 1; i <= columnCount; i++) {//adds a record of data
                    row[i-1] = resultSet.getString(i);
                }
                results.add(row.clone());
            }
            output = new String[results.size()][columnCount];
            for(int i = 0; i < results.size(); i++){//convert arraylist into static array
                output[i] = results.get(i);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException ex) {
            System.out.println("Error occurred when querying the database to get its contents.");
            System.out.println("SQL Query: " + sqlQuery);
            System.out.println("What led to the error:\n");
            ex.printStackTrace();
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return output;
    }

    /**
     * Returns the size of the specified table
     */
    public int getSizeOfTable(String table) {
        String tableSizeSQLQuery = "SELECT COUNT(*) FROM " + table;
        return getIntScalar(tableSizeSQLQuery);
    }

    /**
     * @return - whether the specified table is empty
     */
    public boolean isTableEmpty(String table) {
        return getSizeOfTable(table) == 0;
    }

    /**
     * Returns the ID of an entity
     */
    public int getIDOfItem(String entity, String... attributes) {
        String sql = null;
        switch (entity) {
            case "tblPlace":
                sql = String.format("SELECT PlaceID FROM tblPlace WHERE Postcode = '%s'", attributes[0]);
                break;
            case "tblTaxicab":
                sql = String.format("SELECT TaxiID FROM tblTaxicab WHERE RegstNum = '%s'", attributes[0]);
                break;
            case "tblCustomer":
                sql = String.format("SELECT CustID FROM tblCustomer WHERE CustName = '%s' AND PhoneNum = '%s'", attributes[0], attributes[1]);
                break;
        }
        return getIntScalar(sql);
    }

    /**
     * Adds a record to the given table in the database. String attributes
     * should be passed already surrounded by quote marks
     *
     * @param entity - which table is being added to
     * @param attributes - the attributes of the new record
     */
    private void addItem(String entity, String... attributes) {
        String values = String.join(", ", attributes);
        String addItemSQL = String.format("INSERT INTO %s VALUES(%s)", entity, values);
        executeSqlStatement(addItemSQL);
    }

    /**
     * Adds a place
     *
     * @param postcode - the postcode of the place
     */
    public void addPlace(String postcode) {
        postcode = "'" + postcode + "'";
        String[] attributes = new String[]{postcode};
        addItem("tblPlace(Postcode)", attributes);
    }

    /**
     * Adds a road Ensure that the provided postcodes already exist as places
     *
     * @param place1 - the postcode of the first place of the road
     * @param place2 - the postcode of the second place of the road
     * @param length - the length of the road, in km
     */
    public void addRoad(String place1, String place2, Float length) {
        String placeId1 = String.valueOf(getIDOfItem("tblPlace", new String[]{place1}));
        String placeId2 = String.valueOf(getIDOfItem("tblPlace", new String[]{place2}));
        addItem("tblRoad", placeId1, placeId2, String.valueOf(length));
        addItem("tblRoad", placeId2, placeId1, String.valueOf(length));
    }

    /**
     * Adds a taxicab Ensure that the provided postcodes already exist as places
     *
     * @param regstNum - the registration number of the taxicab
     * @param position - the position of the taxicab, as a postcode
     */
    public void addTaxicab(String regstNum, String position) {
        regstNum = "'" + regstNum + "'";
        String positionId = String.valueOf(getIDOfItem("tblPlace", new String[]{position}));
        addItem("tblTaxicab(RegstNum,Position)", regstNum, positionId);
    }

    /**
     * Adds a taxicab Ensure that the provided postcodes already exist as places
     *
     * @param name - the name of the customer
     * @param phoneNum - the phone number of the customer
     * @param position - the position of the customer, as a postcode
     */
    public void addCustomer(String name, String phoneNum, String position) {
        name = "'" + name + "'";
        phoneNum = "'" + phoneNum + "'";
        String positionId = String.valueOf(getIDOfItem("tblPlace", new String[]{position}));
        addItem("tblCustomer(CustName,PhoneNum,Position)", name, phoneNum, positionId);
    }

    /**
     * Changes the position of the given entity Ensure that both the entity
     * (taxicab or customer) and place (position) already exist
     *
     * @param entity - whether to edit the position of a taxicab or customer
     * @param attributes - attributes used to identify a record
     * @param newPosition - the new position, provided as a postcode
     */
    public void editPosition(String entity, String newPosition, String... attributes) {
        int positionId = getIDOfItem("tblPlace", new String[]{newPosition});
        String sql = String.format("UPDATE %s SET Position = %d WHERE ", entity, positionId);
        switch (entity) {
            case "tblTaxicab":
                sql += String.format("RegstNum = '%s'", attributes[0]);
                break;
            case "tblCustomer":
                sql += String.format("CustName = '%s' AND PhoneNum = '%s'", attributes[0], attributes[1]);
                break;
        }
        executeSqlStatement(sql);
    }

    /**
     * Removes an entity
     *
     * @param entity - what type of entity to delete
     * @param attributes - the attributes used to identify an entity
     */
    public void removeItem(String entity, String... attributes) {
        String sql = "DELETE FROM " + entity + " WHERE ";
        switch (entity) {
            case "tblPlace":
                sql += String.format("Postcode = '%s'", attributes[0]);
                break;
            case "tblRoad":
                int placeID1 = getIDOfItem("tblPlace", attributes[0]);
                int placeID2 = getIDOfItem("tblPlace", attributes[1]);
                sql += String.format("(StartPlace = '%s' AND EndPlace = '%s') OR (StartPlace = '%s' AND EndPlace = '%s')", placeID1, placeID2, placeID2, placeID1);
                break;
            case "tblTaxicab":
                sql += String.format("RegstNum = '%s'", attributes[0]);
                break;
            case "tblCustomer":
                sql += String.format("CustName = '%s' AND PhoneNum = '%s'", attributes[0], attributes[1]);
                break;
        }
        executeSqlStatement(sql);
    }

    /**
     * Returns the whether or not a table contains a certain entity
     */
    public boolean doesTableContain(String entity, String... attributes) {
        String sql = "SELECT COUNT(*) FROM " + entity + " WHERE ";
        switch (entity) {
            case "tblPlace":
                sql += String.format("Postcode = '%s'", attributes[0]);
                break;
            case "tblRoad":
                //table aliasing used in this query so that two different placeIDs can be acquired simultaneously
                sql = "SELECT COUNT(*) FROM tblPlace place1, tblPlace place2, tblRoad WHERE "
                        + "StartPlace = place1.PlaceID AND EndPlace = place2.PlaceID AND "
                        + String.format("place1.Postcode = '%s' AND place2.Postcode = '%s'", attributes[0], attributes[1]);
                break;
            case "tblTaxicab":
                sql += String.format("RegstNum = '%s'", attributes[0]);
                break;
            case "tblCustomer":
                sql += String.format("CustName = '%s' AND PhoneNum = '%s'", attributes[0], attributes[1]);
                break;
        }

        return getIntScalar(sql) != 0;
    }

    /**
     * Returns whether there is a customer, taxicab or road on/connecting to the
     * specified postcode
     */
    public boolean isPlaceOccupied(String postcode) {
        String sql = "SELECT COUNT(*) "
                + "FROM tblPlace, tblRoad, tblCustomer, tblTaxicab "
                + "WHERE Postcode = '" + postcode + "' "
                + "AND (StartPlace = PlaceID OR EndPlace = PlaceID "
                + "OR tblCustomer.Position = PlaceID "
                + "OR tblTaxicab.Position = PlaceID)";
        return getIntScalar(sql) != 0;
    }

    /**
     * Returns the contents of the specified table.
     * The first row contains a list of the headers in the query
     */
    public String[][] returnTable(String entity) {
        String sql = null;
        switch (entity) {
            case "tblPlace":
                sql = "SELECT Postcode FROM tblPlace ORDER BY Postcode";
                break;
            case "tblRoad":
                //table aliasing used in this query so that two different postcodes can be acquired simultaneously
                sql = "SELECT place1.Postcode AS StartPostcode, place2.Postcode AS EndPostcode, Length AS 'Length/km' "
                        + "FROM tblPlace place1, tblPlace place2, tblRoad "
                        + "WHERE place1.PlaceID = tblRoad.StartPlace AND place2.PlaceID = tblRoad.EndPlace";
                break;
            case "tblTaxicab":
                sql = "SELECT RegstNum, Postcode "
                        + "FROM tblTaxicab, tblPlace "
                        + "WHERE tblTaxicab.Position = tblPlace.PlaceID "
                        + "ORDER BY RegstNum";
                break;
            case "tblCustomer":
                sql = "SELECT CustName, PhoneNum, Postcode "
                        + "FROM tblCustomer, tblPlace "
                        + "WHERE tblCustomer.Position = tblPlace.PlaceID "
                        + "ORDER BY CustName";
                break;
        }
        return getQueryResults(sql);
    }

    /**
     * Returns an ordered list of primary keys of the specified table
     */
    public int[] getListOfIDs(String entity) {
        String sql = null;
        switch (entity) {
            case "tblPlace":
                sql = "SELECT PlaceID FROM tblPlace ORDER BY PlaceID";
                break;
            case "tblTaxicab":
                sql = "SELECT TaxiID FROM tblTaxicab ORDER BY TaxiID";
                break;
            case "tblCustomer":
                sql = "SELECT CustID FROM tblCustomer ORDER BY CustID";
                break;
        }
        return getIntArray(sql);
    }

    /**
     * Returns a list of positions occupied by taxicabs or customers, ordered by
     * primary key
     */
    public int[] getListOfPositions(String entity) {
        String sql = null;
        switch (entity) {
            case "tblTaxicab":
                sql = "SELECT Position FROM tblTaxicab ORDER BY TaxiID";
                break;
            case "tblCustomer":
                sql = "SELECT Position FROM tblCustomer ORDER BY CustID";
                break;
        }
        return getIntArray(sql);
    }

    /**
     * Gets the position of a certain taxicab or customer, from its ID
     *
     * @return the primary key of the position of the entity
     */
    public int getPositionIDFromID(String entity, int id) {
        String sql = "SELECT Position FROM " + entity + " WHERE ";
        switch (entity) {
            case "tblTaxicab":
                sql += "TaxiID = " + id;
                break;
            case "tblCustomer":
                sql += "CustID = " + id;
                break;
        }
        return getIntScalar(sql);
    }

    /**
     * Returns the string attribute of a certain entity
     *
     * @param fieldName - the name of the attribute to return
     * @param entity - whether the entity is a place, taxicab or customer
     * @param id - the ID of the entity
     */
    public String getStringValueFromID(String fieldName, String entity, int id) {
        String sql = String.format("SELECT %s FROM %s WHERE ", fieldName, entity);
        switch (entity) {
            case "tblPlace":
                sql += "PlaceID = " + id;
                break;
            case "tblTaxicab":
                sql += "TaxiID = " + id;
                break;
            case "tblCustomer":
                sql += "CustID = " + id;
                break;
        }
        return getStringScalar(sql);
    }

    /**
     * Returns a list of the IDs of the places that neighbour the specified
     * place
     */
    public int[] listOfNeighbours(int placeID){
        String sql = "SELECT EndPlace FROM tblRoad WHERE StartPlace = " + placeID + " ORDER BY EndPlace";
        return getIntArray(sql);
    }

    /**
     * Returns the distance between the two specified places
     */
    public float getDistanceBetweenPlaces(int placeID1, int placeID2) {
        String sql = String.format("SELECT Length FROM tblRoad WHERE StartPlace =  %d AND EndPlace = %d", placeID1, placeID2);
        return getRealScalar(sql);
    }
}
