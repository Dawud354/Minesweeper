import java.util.Random;
import java.util.Scanner;

public class BaseTextProgram {

    // method to see if a string is a positive number
    //
    public static boolean isValidPositiveInt(String numText) {
        boolean isValid = true;
        for (int i = 0; i < numText.length(); i++) {
            // goes though each character in string to check if it is not a digit
            if (!Character.isDigit(numText.charAt(i))) {
                isValid = false;
            }
        }
        return isValid;
    }// END isValidPositiveInt

    /**
     *  This checks if a string passed is in alphanumeric or not
     *
     * @param text string passed in
     * @return true if string is alphanumeric
     */
    public static boolean isAlphanumeric(String text) {
        if (text == null) {
            return false;
        }
        return text.matches("[a-zA-Z0-9 ]+");
    }

    /**
     * Checks if a string passed in follows basic english grammar
     * @return true if string is in english
     */
    public static boolean isValidSentence(String text) {
        if (text == null) {
            return false;
        }
        return text.matches("[a-zA-Z0-9 !?.,']+");
    }


    // method to get input from user
    //
    public static String inputString(String message) {
        print(message);
        return inputString();
    } // END inputString

    // method to get input from user
    //
    public static String inputString() {
        Scanner sc = new Scanner(System.in);
        String text = sc.nextLine();
        while (text.isEmpty() || text.isBlank()) {
            print("Please do not give blank inputs");
            text = sc.nextLine();
        }
        return text;
    } // END inputString

    public static int inputInt(String message) {
        String numText;
        int num;
        // checks if user inputted number
        // keeps looping until user does
        numText = inputString(message);
        while (!isValidInteger(numText)) {
            numText = inputString("Please enter an integer");

        }
        num = Integer.parseInt(numText);
        return num;
    }

    public static boolean isValidInteger(String input) {
        return input.matches("-?\\d+");
    }

    // method to get double from user
    //
    public static double inputPositiveDouble() {
        String numText;
        double num;
        // checks if user inputted number
        // keeps looping until user does
        numText = inputString();
        while (!isValidPositiveDouble(numText)) {
            print("Please enter a positive double");
            numText = inputString();

        }
        num = Double.parseDouble(numText);
        return num;

    } //END inputPositiveDouble

    // method to see if a string is a positive double
    //
    public static boolean isValidPositiveDouble(String numText) {
        boolean isValid = true;
        for (int i = 0; i < numText.length(); i++) {
            // goes though each character in string to check if it is not a digit
            if (!Character.isDigit(numText.charAt(i)) && numText.charAt(i) != '.') {
                isValid = false;
            }
        }
        return isValid;
    }// END isValidPositiveDouble

    // returns a random int between the 2 values passed in
    //
    public static int randomInt(int lowBound, int highBound) {
        Random rand = new Random();
        return rand.nextInt(highBound - lowBound) + lowBound;
    } // END randomInt

    // takes in a string and outputs result
    //
    public static void print(String text) {
        System.out.println(text);
        return;
    } // END print

    // takes in a string and outputs result
    //
    public static void print(int num) {
        System.out.println(num);
        return;
    } // END print

    // Checks if an int is in an array
    //
    public static boolean isIntegerInArray(int[] numArray, int numToFind) {
        for (int num : numArray) {
            if (num == numToFind) {
                return true;
            }
        }
        return false;
    } // END isIntegerInArray

    // Checks if a string is in an array
    //
    public static boolean isStringInArray(String[] stringArray, String stringToFind) {
        for (String str : stringArray) {
            if (str != null && str.equals(stringToFind)) {
                return true;
            }
        }
        return false;
    } // END isStringInArray


}
