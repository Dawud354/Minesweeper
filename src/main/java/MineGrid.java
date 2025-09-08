public class MineGrid {
    private final Node[][] grid;
    private int rows;
    private int cols;

    public MineGrid(int rows, int cols) {
        setRows(rows);
        setCols(cols);
        this.grid = new Node[rows][cols];
        initialiseGrid();
    }

    /**
     *  set rows
     */
    public void setRows(int rows) {
        if (rows < 1) {
            throw new IllegalArgumentException("Rows must be at least 1");
        }
        this.rows = rows;
    }

    /**
     *  set columns
     */
    public void setCols(int cols) {
        if (cols < 1) {
            throw new IllegalArgumentException("Columns must be at least 1");
        }
        this.cols = cols;
    }

    /**
     * Makes a blank grid of Nodes.
     * */
    private void initialiseGrid() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = new Node();
            }
        }
    }

    /**
     * Return number of rows in the grid.
     */
    public int getRows() {
        return rows;
    }

    /**
     * Return number of columns in the grid.
     */
    public int getCols() {
        return cols;
    }

    /**
     * Returns the total number of nodes in the grid.
     */
    public int getSize() {
        return rows * cols;
    }

    /**
     * Returns the Node at the specified position.
     */
    public Node getNode(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            throw new IndexOutOfBoundsException("Invalid grid position: (" + row + ", " + col + ")");
        }
        return grid[row][col];
    }

}
