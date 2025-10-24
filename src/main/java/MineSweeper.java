import java.util.Queue;

public class MineSweeper {
    private MineGrid mineGrid;
    private int mineCount;
    private int numberOfFlags;
    private MineSweeperMessages gameStatus;


    /**
     * Constructor for MineSweeper.
     * Creates a MineGrid with the specified number of rows and columns,
     * populates it with bombs, and calculates the number of nearby bombs for each node.
     * @param rows number of rows in the grid
     * @param cols number of columns in the grid
     */
    public MineSweeper(int rows, int cols, int mineCount) {
        mineGrid = new MineGrid(rows, cols);
        this.mineCount = mineCount;
        this.numberOfFlags = 0;
    }

    /**
     * Starts a new game of MineSweeper.
     * @param startRow starting row for the game
     * @param startCols starting column for the game
     */
    public void startGame(int startRow, int startCols) {
        this.numberOfFlags = 0;
        clearGrid(); // Clear the grid before starting a new game
        revealNodesAroundStart(startRow, startCols); // Reveal nodes around the starting position
        populateGridWithBombs(); // Randomly place bombs in the grid
        hideNodesAroundStart(startRow, startCols); // Hide nodes around the starting position
        calculateNearbyBombCounts(); // Update the grid with bomb counts
        floodFill(startRow, startCols); // Flood fill from the starting position
        //System.out.println("Game started! Bombs placed and nearby counts calculated.");
    }

    /**
     * Reveals a node at the specified coordinates.
     * @param row row
     * @param col column
     * @return the revealed Node
     */
    public MineSweeperMessages revealNode(int row, int col) {
        if (isOutOfBounds(row, col)) {
            throw new IllegalArgumentException("Coordinates out of bounds");
        }
        Node node = mineGrid.getNode(row, col);
        if (node.isRevealed()) {
            if (node.isBomb()){
                return MineSweeperMessages.BOMB_AND_REVEALED; // Bomb already revealed
            }
            return MineSweeperMessages.REVEALED_NODE;
        }
        if (node.isBomb()){
            gameStatus = MineSweeperMessages.GAME_OVER;
            node.setRevealed(true); // Reveal the bomb node
            revealAllOnLoss(); // Reveal all bombs when the game is lost
            return MineSweeperMessages.BOMB_NODE;
        }
        floodFill(row, col); // Reveal surrounding nodes if the current node is not a bomb
        isGameWon(); // Check if the game is won after revealing a node
        return MineSweeperMessages.NODE_NOW_REVEALED;
    }

    /**
     * Reveals all nodes in the grid when player lost.
     */
    private void revealAllOnLoss() {
        for (int i = 0; i < mineGrid.getRows(); i++) {
            for (int j = 0; j < mineGrid.getCols(); j++) {
                Node node = mineGrid.getNode(i, j);
                if (node.isBomb()) {
                    node.setRevealed(true);
                }
            }
        }
    }

        /**
     * Reveals all nodes in the grid.
     */
    private void revealAllOnWin() {
        for (int i = 0; i < mineGrid.getRows(); i++) {
            for (int j = 0; j < mineGrid.getCols(); j++) {
                Node node = mineGrid.getNode(i, j);
                if (node.isBomb()){
                    node.setFlagged(true);
                }
            }
        }
    }

    /**
     * Toggles the flag of a node at the specified coordinates.
     * @param row row
     * @param col column
     * @return the status of the node after flagging
     */
    public MineSweeperMessages flagNode(int row, int col) {
        if (isOutOfBounds(row, col)) {
            throw new IllegalArgumentException("Coordinates out of bounds");
        }
        Node node = mineGrid.getNode(row, col);
        if (node.isRevealed()) {
            return MineSweeperMessages.REVEALED_NODE; // Cannot flag a revealed node
        }
        node.setFlagged(!node.isFlagged()); // Toggle the flagged state

        if (node.isFlagged()) {
            this.numberOfFlags++;
            return MineSweeperMessages.FLAGGED_NODE; // Node is now flagged
            
        } else {
            this.numberOfFlags--;
            return MineSweeperMessages.UNFLAGGED_NODE; // Node is now unflagged
        }
    }

    /**
     * Counts how many bombs are nearby a specific tile.
     * @param row the row of the tile
     * @param col the column of the tile
     * @return the number of bombs nearby the specified tile
     */
    public int howManyBombsNearbyTile(int row, int col) {
        if (isOutOfBounds(row, col)) {
            throw new IllegalArgumentException("Coordinates out of bounds");
        }
        Node node = mineGrid.getNode(row, col);
        return node.getBombsNearby(); // Return the number of bombs nearby the specified node
    }

    /**
     * Gets the Node at the specified coordinates.
     * @param row row
     * @param col column
     * @return the Node at the specified coordinates
     */
    public MineSweeperMessages getNode(int row, int col) {
        if (isOutOfBounds(row, col)) {
            throw new IllegalArgumentException("Coordinates out of bounds");
        }
        Node node = mineGrid.getNode(row, col);
        MineSweeperMessages messages = MineSweeperMessages.EMPTY_NODE;
        if (node.isRevealed()) {
            if (node.isBomb()) {
                messages = MineSweeperMessages.BOMB_AND_REVEALED; // Node is a bomb and revealed
            } else {
                messages = MineSweeperMessages.REVEALED_NODE;
            }
        } else if (node.isFlagged()) {
            return MineSweeperMessages.FLAGGED_NODE;
        } else if (node.isBomb()) {
            return MineSweeperMessages.BOMB_AND_HIDDEN;
        }

        return messages;
    }

    /**
     * Gets the number of rows in the grid.
     * @return number of rows
     */
    public int getRows() {
        return mineGrid.getRows();
    }

    /**
     * Gets the number of columns in the grid.
     * @return number of columns
     */
    public int getCols() {
        return mineGrid.getCols();
    }


    /**
     * Reveals the nodes around the starting position.
     * @param row row of the starting node
     * @param col column of the starting node
     */
    private void revealNodesAroundStart(int row, int col){
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int newRow = row + i;
                int newCol = col + j;
                if (isOutOfBounds(newRow, newCol)) {
                    continue; // Skip out of bounds
                }
                Node neighborNode = mineGrid.getNode(newRow, newCol);
                neighborNode.setRevealed(true); // Reveal the neighboring node
            }
        }
    }

    /**
     * Pretty silly way to do this.
     * It hides the nodes around start so flood fill works. Probably some other better way but this works.
     * @param row row of node
     * @param col column of node
     */
    private void hideNodesAroundStart(int row, int col){
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int newRow = row + i;
                int newCol = col + j;
                if (isOutOfBounds(newRow, newCol)) {
                    continue; // Skip out of bounds
                }
                Node neighborNode = mineGrid.getNode(newRow, newCol);
                neighborNode.setRevealed(false); // Hide the neighboring node
            }
        }
    }

    /**
     * Populates the grid with bombs.
     * Randomly places a certain number of bombs in the grid based on the size of the grid.
     */
    private void populateGridWithBombs() {
        // Logic to randomly place bombs in the grid
        // keeps looping until the required number of bombs are placed
        int bombsToPlace = mineCount;
        while (bombsToPlace > 0) {
            int row = (int) (Math.random() * mineGrid.getRows());
            int col = (int) (Math.random() * mineGrid.getCols());
            Node node = mineGrid.getNode(row, col);
            if( node.isRevealed()){
                continue; // Skip if the node is already revealed
            }
            if (!node.isBomb()) {
                node.setBomb(true);
                bombsToPlace--;
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
                } // nigger
            }
        }
        Node currentNode = mineGrid.getNode(row, col);
        currentNode.setBombsNearby(bombsNearby);
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

    /**
     * Flood fill algorithm to reveal all connected nodes starting from the given position.
     * It reveals all nodes that are not bombs and are connected to the starting node.
     * @param row starting row
     * @param col starting column
     */
    private void floodFill (int row, int col) {
        if (isOutOfBounds(row, col)) {
            return; // Skip out of bounds
        }
        Queue<int[]> queue = new java.util.LinkedList<>();
        queue.add(new int[]{row, col});
        while(!queue.isEmpty()){
            int[] current = queue.poll();
            int currentRow = current[0];
            int currentCol = current[1];
            Node currentNode = mineGrid.getNode(currentRow, currentCol);

            if (currentNode.isRevealed() || currentNode.isBomb()) {
                continue; // Skip already revealed or bomb nodes
            }

            currentNode.setRevealed(true); // Reveal the current node
            if (currentNode.getBombsNearby()!=0){
                continue; // reveal node but does not flood fill further
            }

            // exploring neighboring nodes
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i == 0 && j == 0){
                        continue; // Skip the current node
                    }
                    int newRow = currentRow + i;
                    int newCol = currentCol + j;
                    if (isOutOfBounds(newRow, newCol)) {
                        continue; // Skip out of bounds
                    }
                    Node neighborNode = mineGrid.getNode(newRow, newCol);
                    if (!neighborNode.isRevealed() && !neighborNode.isBomb()) {
                        queue.add(new int[]{newRow, newCol}); // Add to queue for further exploration
                    }
                }
            }
        }
    }

    private void isGameWon() {
        int rows = mineGrid.getRows();
        int cols = mineGrid.getCols();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Node node = mineGrid.getNode(i, j);
                if (!node.isBomb() && !node.isRevealed()) {
                    return; // Found a non-bomb node that is not revealed
                }
            }
        }
        gameStatus = MineSweeperMessages.GAME_WON;
        revealAllOnWin(); // Reveal all nodes when the game is won
    }


    /**
     * Resets the grid by making a new instance with same dimensions.
     */
    private void clearGrid() {
        int rows = mineGrid.getRows();
        int cols = mineGrid.getCols();
        mineGrid = new MineGrid(rows, cols);
    }

    /**
     * Gets the number of mines in the grid.
     * @return number of mines
     */
    public int getMineCount() {
        return mineCount;
    }

    /**
     * Gets the number of flags currently placed.
     * @return number of flags
     */    public int getNumberOfFlags() {
        return numberOfFlags;
    }

    /**
     * Gets the number of mines left (mines - flags).
     * @return number of mines left
     */
    public int getNumberOfMinesLeft() {
        return mineCount - numberOfFlags;
    }

    /**
     * Checks if the game is over (a bomb has been revealed).
     * @return true if game is over, false otherwise
     */
    public MineSweeperMessages getGameStatus() {
        return gameStatus;
    }
}
