import java.util.regex.Pattern;

public class MineSweeperFrontEnd extends BaseTextProgram {
    private MineSweeper mineSweeper;
    private boolean gameOver = false;

    public MineSweeperFrontEnd() {
        print("Welcome to MineSweeper!");
        initialiseGame();
    }

    private void initialiseGame(){
        int rows = askGridRows();
        int cols = askGridColumns();
        mineSweeper = new MineSweeper(rows, cols);
        displayInstructions();
        startGame();
    }

    private int askGridRows() {
        int maxRows = MineGrid.MAX_ROWS;
        print("Enter the number of rows for the mine grid:");
        int rows = inputInt("Enter a number between 1 and "+maxRows+": ");
        while (rows < 1 || rows > 20) {
            rows = inputInt("Invalid number of rows. Please enter a number between 1 and "+maxRows+".");
        }
        return rows;
    }

    private int askGridColumns() {
        int maxCols = MineGrid.MAX_COLS;
        print("Enter the number of columns for the mine grid:");
        int rows = inputInt("Enter a number between 1 and "+maxCols+": ");
        while (rows < 1 || rows > 20) {
            rows = inputInt("Invalid number of columns. Please enter a number between 1 and "+maxCols+".");
        }
        return rows;
    }

    private void displayInstructions() {
        print("Instructions:");
        print("1. Use R to reveal a node and F to flag a node.");
        print("2. Enter the row and column indices to perform the action.");
    }

    private void startGame() {
        startPosition();
        final String CLEAR_SCREEN = "\033[H\033[2J";


        while (!gameOver) {
            print(CLEAR_SCREEN);
            printGrid();
            String action = getAction();
            switch (action) {
                case "Q" -> endGame();
                case ("R") -> askRevealPosition();
                case "F" -> askFlagPosition();
            }
            if (mineSweeper.areAllNodesRevealed()) {
                gameOver = true;
                endGame();
            }
        }
    }

    private void pickDifficulty() {
        print("Choose a difficulty level:");
        print("1. Easy (10% bombs)");
        print("2. Medium (20% bombs)");
        print("3. Hard (30% bombs)");
        int choice = inputInt("Enter your choice (1-3): ");
        while (choice < 1 || choice > 3) {
            choice = inputInt("Invalid choice. Please enter a number between 1 and 3: ");
        }
        mineSweeper.setDifficulty(choice);
    }

    private void startPosition(){
        printGrid();
        print("Please enter where you would like to start the game.");
        String coordinates = askNodeCoordinates();
        int col = letterToNumber(coordinates.charAt(0));
        int row = Integer.parseInt(coordinates.substring(1)) - 1; // Convert to 0-indexed
        mineSweeper.startGame(row, col);
    }

    private void endGame() {
        print("Game Over! Thanks for playing.");
        printGrid();
        gameOver = true;
    }

    private void askRevealPosition(){
        String coordinates = askNodeCoordinates();
        int[] rowCol = coordinatesToRowCol(coordinates);
        MineSweeperMessages message = mineSweeper.revealNode(rowCol[0], rowCol[1]);
        if (message == MineSweeperMessages.REVEALED_NODE) {
            print("Node is already revealed.");
        } else if (message == MineSweeperMessages.BOMB_NODE) {
            print("Game Over! You hit a bomb at (" + coordinates + ").");
            printGrid();
            // Reveal all nodes and end the game
            mineSweeper.revealAllNodes();
            gameOver = true;
        } else if (message == MineSweeperMessages.NODE_NOW_REVEALED){
            int bombs = mineSweeper.howManyBombsNearbyTile(rowCol[0], rowCol[1]);
            print("Node at (" + coordinates+ ") revealed. Bombs nearby: " + bombs);
        }else {
            print("There was an error revealing the node at (" + coordinates + ").");
        }
    }

    private void askFlagPosition() {
        String coordinates = askNodeCoordinates();
        int[] rowCol = coordinatesToRowCol(coordinates);
        MineSweeperMessages message = mineSweeper.flagNode(rowCol[0], rowCol[1]);
        if (message == MineSweeperMessages.REVEALED_NODE) {
            print("Node is already revealed. Cannot flag.");
        } else {
            print("Node at (" + coordinates + ") flagged.");
        }
    }

    private String askNodeCoordinates() {
        boolean valid = false;
        String input = "";
        while (!valid) {
            input = inputString("Enter the coordinates of the node (e.g., A1): ");
            input = input.toUpperCase().trim(); // Convert to uppercase and trim whitespace
            // Check if the input is valid
            if (!isValidCoordinates(input)) {
                print("Invalid coordinates. Please enter in the format A1, B2, etc.");
            } else if (!isCoordinatesInRange(input)) {
                print("Coordinates out of range. Please enter coordinates within the grid.");
            } else {
                valid = true;
            }
        }
        return input;
    }

    /**
     * Validates the coordinates input by the player.
     * @param input the input string to validate
     * @return true if valid, false otherwise
     */
    private boolean isValidCoordinates(String input) {
        String pattern = "^[A-Z]\\d{1,2}$";
        Pattern regex = Pattern.compile(pattern);
        return regex.matcher(input).matches();
    }

    /**
     * Checks to see if the coordinates given are in range of the grid.
     * @param input the input string containing coordinates (e.g., A1 or B21)
     * @return true if the coordinates are in range, false otherwise
     */
    private boolean isCoordinatesInRange(String input) {
        int[] rowCol = coordinatesToRowCol(input);
        return !mineSweeper.isOutOfBounds(rowCol[0], rowCol[1]);
    }

    /**
     * Converts coordinates from the format A1, B2, etc. to row and column indices.
     * @param coordinates the coordinates in the format A1, B2, etc.
     * @return an array containing the row and column indices index 0 is row, index 1 is column
     */
    private int[] coordinatesToRowCol(String coordinates) {
        char letter = coordinates.charAt(0);
        int number = Integer.parseInt(coordinates.substring(1)) - 1; // Convert to 0-indexed
        int col = letterToNumber(letter);
        return new int[]{number, col};
    }

    private String getAction() {
        String[] validActions = {"R", "F", "Q"};
        String input= inputString("Enter your action (R to reveal, F to flag, Q to quit): ").toUpperCase();
        while (!isStringInArray(validActions,input)) {
            print("Invalid action. Please enter R to reveal, F to flag, or Q to quit.");
            input = inputString("Enter your action (R to reveal, F to flag, Q to quit): ").toUpperCase();
        }
        return input;
    }

    /**
     * Prints the current state of the mine grid.
     */
    public void printGrid() {
        int rows = mineSweeper.getRows();
        int cols = mineSweeper.getCols();

        // Column header (with spacing for row numbers)
        System.out.print("    "); // Padding for row numbers
        for (int col = 0; col < cols; col++) {
            System.out.printf(" %2s", numberToLetter(col)); // Column letters with padding
        }
        System.out.println();

        // Grid content
        for (int row = 0; row < rows; row++) {
            System.out.printf("%2d |", row + 1); // Row number with left padding
            for (int col = 0; col < cols; col++) {
                MineSweeperMessages message = mineSweeper.getNode(row, col); // Use row, col here
                String cell;
                if (message == MineSweeperMessages.BOMB_AND_REVEALED) {
                    cell = "B"; // Bomb revealed
                } else if (message ==MineSweeperMessages.FLAGGED_NODE) {
                    cell = "F";
                } else if (message == MineSweeperMessages.REVEALED_NODE) {
                    int bombsNearby = mineSweeper.howManyBombsNearbyTile(row, col);
                    cell = String.valueOf(bombsNearby); // Empty or number of bombs nearby
                } else {
                    cell = ".";
                }
                System.out.printf("  %s", cell); // Print cell with spacing
            }
            System.out.println();
        }
    }

    private char numberToLetter(int number) {
        if (number < 0 || number >= 26) {
            throw new IllegalArgumentException("Number must be between 0 and 25");
        }
        return (char) ('A' + number);
    }

    private int letterToNumber(char letter) {
        if (letter < 'A' || letter > 'Z') {
            throw new IllegalArgumentException("Letter must be between A and Z");
        }
        return (letter - 'A');

    }
}
