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
        while (!gameOver) {
            printGrid();
            String action = getAction();
            switch (action) {
                case "Q" -> endGame();
                case ("R") -> askRevealPosition();
                case "F" -> askFlagPosition();
            }
        }
    }

    private void endGame() {
        print("Game Over! Thanks for playing.");
        printGrid();
    }

    private void askRevealPosition(){
        int row = askPlayerRow();
        int col = askPlayerColumns();
        Node node = mineSweeper.revealNode(row, col);
        if (node.isRevealed()) {
            print("Node is already revealed.");
        } else if (node.isBomb()) {
            print("Game Over! You hit a bomb at (" + (row + 1) + ", " + (col + 1) + ").");
            gameOver = true;
        } else {
            node.setRevealed(true);
            print("Node at (" + (row + 1) + ", " + (col + 1) + ") revealed. Bombs nearby: " + node.getBombsNearby());
        }
    }

    private void askFlagPosition() {
        int row = askPlayerRow();
        int col = askPlayerColumns();
        Node node = mineSweeper.revealNode(row, col);
        if (node.isRevealed()) {
            print("Node is already revealed. Cannot flag.");
        } else {
            node.setFlagged(true);
            print("Node at (" + (row + 1) + ", " + (col + 1) + ") flagged.");
        }
    }


    /**
     * Asks the player for the row number of the node they want to reveal or flag.
     * @return the row number (1-indexed)
     */
    private int askPlayerRow() {
        int maxRows = mineSweeper.getRows();
        print("Enter row number of the node");
        int rows = inputInt("Enter a number between 1 and "+maxRows+": ");
        if (rows < 1 || rows > 20) {
            rows = inputInt("Invalid number. Please enter a number between 1 and "+maxRows+".");
        }
        return rows - 1;
    }

    /**
     * Asks the player for the column number of the node they want to reveal or flag.
     * @return the column number (1-indexed)
     */
    private int askPlayerColumns() {
        int maxCols = mineSweeper.getCols();
        print("Enter column number of the node");
        int rows = inputInt("Enter a number between 1 and "+maxCols+": ");
        if (rows < 1 || rows > 20) {
            rows = inputInt("Invalid number. Please enter a number between 1 and "+maxCols+".");
        }
        return rows - 1;
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
        mineSweeper.printGrid();
    }
}
