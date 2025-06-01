public class MineSweeper {
    private MineGrid mineGrid;


    /**
     * Constructor for MineSweeper.
     * Creates a MineGrid with the specified number of rows and columns,
     * populates it with bombs, and calculates the number of nearby bombs for each node.
     * @param rows
     * @param cols
     */
    public MineSweeper(int rows, int cols) {
        mineGrid = new MineGrid(rows, cols);
        System.out.println("MineSweeper grid created with " + mineGrid.getRows() + " rows and " + mineGrid.getCols() + " columns.");
        populateGridWithBombs();
        calculateNearbyBombCounts();
    }

    private void populateGridWithBombs() {
        // Logic to randomly place bombs in the grid
        int randomBombs = (int) (0.2 * mineGrid.getSize());
        // keeps looping until the required number of bombs are placed
        while (randomBombs > 0) {
            int row = (int) (Math.random() * mineGrid.getRows());
            int col = (int) (Math.random() * mineGrid.getCols());
            Node node = mineGrid.getNode(row, col);
            if (!node.isBomb()) {
                node.setBomb(true);
                randomBombs--;
            }
        }
    }

    /**
     * Goes through the grid and updates the count of nearby bombs for each node.
     */
    private void calculateNearbyBombCounts(){
        // Logic to update the grid after bombs are placed
        for (int i = 0; i < mineGrid.getRows(); i++) {
            for (int j = 0; j < mineGrid.getCols(); j++) {
                Node currentNode = mineGrid.getNode(i, j);
                if (currentNode.isBomb()) {
                    continue; // Skip bomb nodes
                }
                updateNearbyCounts(i, j); // Update counts for non-bomb nodes
            }
        }
    }

    /**
     * Updates the count of nearby bombs for a specific node.
     * @param row row of node
     * @param col column of node
     */
    private void updateNearbyCounts(int row, int col) {
        if (isOutOfBounds(row, col)) {
            return; // Skip out of bounds
        }
        // Logic to update the count of nearby bombs for each node
        int bombsNearby = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0){
                    continue; // Skip the current node
                }
                int newRow = row + i;
                int newCol = col + j;
                if (isOutOfBounds(newRow, newCol)) {
                    continue; // Skip out of bounds
                }
                Node neighborNode = mineGrid.getNode(newRow, newCol);
                if (neighborNode.isBomb()) {
                    bombsNearby++;
                }
            }
        }
        Node currentNode = mineGrid.getNode(row, col);
        currentNode.setBombsNearby(bombsNearby);
    }

    /**
     * Reveals a node at the specified coordinates.
     * @param row row
     * @param col column
     * @return the revealed Node
     */
    public Node revealNode(int row, int col) {
        if (isOutOfBounds(row, col)) {
            throw new IllegalArgumentException("Coordinates out of bounds");
        }
        Node node = mineGrid.getNode(row, col);
        if (node.isRevealed()) {
            return node; // Already revealed
        }
        node.setRevealed(true);
        return node;
    }

    /**
     * Gets the Node at the specified coordinates.
     * @param row row
     * @param col column
     * @return the Node at the specified coordinates
     */
    public Node getNode(int row, int col) {
        if (isOutOfBounds(row, col)) {
            throw new IllegalArgumentException("Coordinates out of bounds");
        }
        return mineGrid.getNode(row, col);
    }

    public int getRows() {
        return mineGrid.getRows();
    }
    public int getCols() {
        return mineGrid.getCols();
    }


    // TODO: TODELETE
    public void printGrid() {
        for (int i = 0; i < mineGrid.getRows(); i++) {
            for (int j = 0; j < mineGrid.getCols(); j++) {
                Node node = mineGrid.getNode(i, j);
                if (node.isBomb()) {
                    System.out.print("B "); // Print bomb
                } else {
                    System.out.print(node.getBombsNearby() + " "); // Print number of bombs nearby
                }
            }
            System.out.println();
        }
    }

    /**
     * Checks if the given row and column are out of bounds of the grid.
     * @param row row
     * @param col column
     * @return true if out of bounds, false otherwise
     */
    public boolean isOutOfBounds(int row, int col) {
        return row < 0 || row >= mineGrid.getRows() || col < 0 || col >= mineGrid.getCols();
    }
}
