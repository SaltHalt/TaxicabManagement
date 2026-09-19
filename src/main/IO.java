package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IO {

    private Database database;

    public IO(Database database) {
        this.database = database;
    }

    /**
     * Generates a prompt to be shown to the user of the form: Please enter the
     * {@code dataItem} of the {@code entity}
     */
    public String makePrompt(String dataItem, String entity) {
        return String.format("Please enter the %s of the %s.", dataItem, entity);
    }

    /**
     * Gets an input from the user Performs basic data validation for inputs
     *
     * @param prompt The prompt given to the user
     * @return User's input
     */
    public String getUserInput(String prompt) {
        System.out.println(prompt);
        String input = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            input = reader.readLine();//get input from console
        } catch (IOException ex) {
            System.out.println("Error occurred, when taking in user input.");
            System.out.println("Prompt shown to user: "+ prompt);
            System.out.println("What led to the error:\n");
            ex.printStackTrace();
            Logger.getLogger(IO.class.getName()).log(Level.SEVERE, null, ex);
        }
        String validInputFormat = "[a-zA-Z0-9 \\.\\+]+";
        if (!input.matches(validInputFormat)) {
            if (input.equals("")) {
                System.out.println("Please don't enter a blank input.");
            } else {
                System.out.println("Please use only alphanumeric characters, spaces, plus signs and decimal points.");
            }
            input = getUserInput(prompt);
        }
        return input;
    }

    /**
     * Gets a real, positive input from the user.
     */
    public float getRealInput(String prompt) {
        String input = getUserInput(prompt);
        String validRealFormat = "[0-9]+(\\.[0-9]+)?";
        if (!input.matches(validRealFormat)) {
            System.out.println("Please enter a positive real value (integers are acceptable).");
            input = getUserInput(prompt);
        }
        return Float.parseFloat(input);
    }

    /**
     * Gets a postcode input from the user Checks input follows postcode format
     */
    private String getPostcodeInput(String prompt) {
        String input = getUserInput(prompt);
        input = input.toUpperCase();
        String validPostcodeFormat = "[A-Z]{1,2}[0-9][A-Z0-9]? [0-9][A-Z]{2}";
        if (!input.matches(validPostcodeFormat)) {
            System.out.println("Please enter a valid postcode.");
            input = getPostcodeInput(prompt);
        }
        return input;
    }

    /**
     * Gets a taxicab registration number from the user Checks input follows
     * registration number format
     */
    private String getRegstNumInput(String prompt) {
        String input = getUserInput(prompt);
        input = input.toUpperCase();
        String validRegstNumFormat = "[A-Z]{2}[0-9]{2} [A-Z]{3}";
        if (!input.matches(validRegstNumFormat)) {
            System.out.println("Please enter a car registration number.");
            input = getRegstNumInput(prompt);
        }
        return input;
    }

    /**
     * Gets a name input from the user
     */
    private String getNameInput(String prompt) {
        String input = getUserInput(prompt);
        String validNameFormat = "[a-zA-Z ]{1,255}";
        if (!input.matches(validNameFormat)) {
            if (input.length() > 255) {
                System.out.println("Please don't enter a name that is longer than 255 characters.");
            } else {
                System.out.println("Please use only input alphabetic characters and spaces.");
            }
            input = getNameInput(prompt);
        }
        return input;
    }

    /**
     * Gets a phone number input from the user Checks that phone number follows
     * postcode format
     */
    private String getPhoneNumInput(String prompt) {
        String input = getUserInput(prompt);
        String validPhoneNumFormat = "\\+?[0-9 ]{11,18}";
        if (!input.matches(validPhoneNumFormat)) {
            if (input.replace("+", "").length() < 11 || input.replace("+", "").length() > 18) {
                System.out.println("Please enter a phone number that has 11 to 18 spaces and digits.");
            } else {
                System.out.println("Please only input numbers/spaces. A leading ‘+’ sign is optional.");
            }
            input = getPhoneNumInput(prompt);
        }
        return input;
    }

    /**
     * Outputs a menu to the user Ensures that the user chooses a valid option.
     *
     * @param options - list of options in menu
     * @return - which option that the user chose
     */
    public int getMenuInput(String menuName, String[] options) {
        String line = "";
        for(int i = 0; i < 84; i++){//prints horizontal line
            line += "-";
        }
        System.out.println(line+"\n"+menuName+"\n");
        int optionChosen;
        System.out.println("Please choose an option");
        for (int i = 0; i < options.length; i++) {
            System.out.println("[" + (i+1) + "] " + options[i]);
        }
        String input = getUserInput("");
        if (!input.matches("\\d+")) {//checks if input isn't a postive integer
            System.out.println("Please enter a positive integer.");
            optionChosen = getMenuInput(menuName,options);
        } else {
            optionChosen = Integer.parseInt(input);
            if (optionChosen == 0 || optionChosen > options.length) {//checks if input does correspond to an existing option
                System.out.println("Please enter an input that corresponds to one of the menu options.");
                optionChosen = getMenuInput(menuName,options);
            }
        }
        System.out.print("\n");
        return optionChosen;
    }

    /**
     * Gets a postcode from the user. Ensures that the inputted postcode does or
     * doesn't match an already existing place.
     * @param prompt - should contain the the prompt to get the place's postcode
     * @param shouldInputAlreadyBeInTable - if true, then the method ensures
     * that the user inputs a postcode that already matches an existing place if
     * false, then the method ensures that the user inputs a postcode that
     * doesn't match any existing place
     * if this is true, ensure that the places table isn't empty
     */
    public String getPositionInput(String prompt, Boolean shouldInputAlreadyBeInTable) {
        String postcode = getPostcodeInput(prompt);
        boolean isInputInTable = database.doesTableContain("tblPlace", postcode);
        if (!(shouldInputAlreadyBeInTable) && isInputInTable) {
            System.out.println("There is already a place with that postcode");
            System.out.println("Please enter a postcode that isn’t already used");
            postcode = getPositionInput(prompt, shouldInputAlreadyBeInTable);
        } else if (shouldInputAlreadyBeInTable && !(isInputInTable)) {
            System.out.println("There is no place with that postcode in the table");
            System.out.println("Please enter a postcode that is already a place");
            postcode = getPositionInput(prompt, shouldInputAlreadyBeInTable);
        }
        return postcode;
    }

    /**
     * Gets a customer name and phone number from the user.
     *
     * @param prompt - should contain the the prompt to get the customer name
     * and phone number from user
     * @param shouldInputAlreadyBeInTable - if true, then the method ensures
     * that the user inputs a name-phone number pair that already matches an
     * existing customer if false, then the method ensures that the user inputs
     * a name-phone number pair that doesn't match any existing customer
     * if this is true, ensure that the customer table isn't empty
     * @return a string array containing first the customer name and secondly
     * the phone number
     */
    public String[] getCustomerInput(String[] prompt, Boolean shouldInputAlreadyBeInTable) {
        String name = getNameInput(prompt[0]);
        String phoneNum = getPhoneNumInput(prompt[1]);
        String[] customer = new String[]{name, phoneNum};
        boolean isInputInTable = database.doesTableContain("tblCustomer", customer);
        if (!(shouldInputAlreadyBeInTable) && isInputInTable) {
            System.out.println("There is already a customer with that name and phone number");
            System.out.println("Please enter a name and phone number that aren’t both used up");
            customer = getCustomerInput(prompt, shouldInputAlreadyBeInTable);
        } else if (shouldInputAlreadyBeInTable && !(isInputInTable)) {
            System.out.println("There is no customer with that name and phone number.");
            System.out.println("Please enter a name and phone number that are associated with a customer.");
            customer = getCustomerInput(prompt, shouldInputAlreadyBeInTable);
        }
        return customer;
    }

    /**
     * Gets a registration number from the user.
     * @param prompt - should contain the the prompt to get the taxicab registration number
     * @param shouldInputAlreadyBeInTable - if true, then the method ensures
     * that the user inputs a registration number that already matches an
     * existing taxicab if false, then the method ensures that the user inputs a
     * postcode that doesn't match any existing taxicab
     * if this is true, ensure that the taxicab table isn't empty
     */
    public String getTaxicabInput(String prompt, Boolean shouldInputAlreadyBeInTable) {
        String regstNum = getRegstNumInput(prompt);
        boolean isInputInTable = database.doesTableContain("tblTaxicab", regstNum);
        if (!(shouldInputAlreadyBeInTable) && isInputInTable) {
            System.out.println("There is already a taxicab with that registration number.");
            System.out.println("Please enter a registration number that is not used up.");
            regstNum = getTaxicabInput(prompt, shouldInputAlreadyBeInTable);
        } else if (shouldInputAlreadyBeInTable && !(isInputInTable)) {
            System.out.println("There is no taxicab with that registration number.");
            System.out.println("Please enter a registration number that is associated with a taxicab.");
            regstNum = getTaxicabInput(prompt, shouldInputAlreadyBeInTable);
        }
        return regstNum;
    }
    /**
     * Outputs the given headers and data in a table.
     */
    public void outputStringArray(String[] headers, String[][] data) {
        int outputWidth = 0;
        for(String header : headers){
            String element = padString(header) + "|";
            System.out.print(element);
            outputWidth += element.length();
        }
        System.out.print("\n");
        String line = "";
        for(int i = 0; i < outputWidth; i++){//prints hearder's border
            line += "-";
        }
        System.out.println(line);//outputs a horizontal border
        for (String[] row : data) {
            for (String item : row) {
                System.out.print(padString(item) + "|");
            }
            System.out.print("\n");
        }
    }

    /**
     * Pads the specified string upto a length of 16 with spaces, and then adds
     * an extra 2 spaces before and after. If the inputted string is longer than
     * 16 characters, then this method only adds an extra 2 spaces before and
     * after.
     */
    private String padString(String item) {
        final int COLUMN_WIDTH = 16;
        while (item.length() < COLUMN_WIDTH) {//pads item upto a length of COLUMN_WIDTH
            item += " ";
        }
        item = "  " + item + "  "; //adds 2 extra spaces before and after
        return item;
    }
}
