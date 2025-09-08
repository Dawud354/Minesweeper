import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;

public class GameView {
    private final SceneManager manager;
    private MineSweeper game;
    private GridPane gameGrid;
    private boolean firstClick;
    private Button[][] buttonGrid;

    public GameView(SceneManager manager, int gridRows, int gridCols, int mineCount) {
        this.manager = manager;
        game = new MineSweeper(gridRows, gridCols, mineCount);
        gameGrid = new GridPane();
        gameGrid.setHgap(1);
        gameGrid.setVgap(1);
        firstClick = true; // To track if it's the first click
    }

    public Parent getView() {

        // === Preview Section ===
        createGrid(); // initial preview
        // StackPane previewWrapper = new StackPane(gameGrid);
        // previewWrapper.setMaxWidth(Region.USE_PREF_SIZE); // optional

        // VBox previewBox = new VBox(5, previewWrapper);
        // previewBox.setAlignment(Pos.CENTER);
        // StackPane root = new StackPane(previewBox);
        Label status = new Label("Mines left: " + game.getMineCount());
        Label timer = new Label("Time: 0s");
        // --- Put labels side by side ---
        HBox infoBox = new HBox(20, status, timer);
        infoBox.setMaxWidth(Region.USE_PREF_SIZE);
        infoBox.setAlignment(Pos.CENTER);

        // --- Add a border/background to the box ---
        infoBox.setStyle("-fx-border-color: black; -fx-padding: 10; -fx-background-color: #f0f0f0;");

        gameGrid.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        VBox root = new VBox(10, gameGrid, infoBox);
        root.setAlignment(Pos.CENTER);
        return root;
    }

    // Optional: method to update the game grid
    private void createGrid() {
        buttonGrid = new Button[game.getRows()][game.getCols()];
        gameGrid.getChildren().clear();
        for (int row = 0; row < game.getRows(); row++) {
            for (int col = 0; col < game.getCols(); col++) {
                Button square = new Button();
                square.setPrefSize(30, 30);
                square.setStyle("-fx-border-color: black; -fx-background-color: lightgray;");
                // --- Hover detection ---
                square.setOnMouseEntered(e -> {
                    square.setStyle("-fx-border-color: black; -fx-background-color: yellow;");
                });
                square.setOnMouseExited(e -> {
                    square.setStyle("-fx-border-color: black; -fx-background-color: lightgray;");
                });
                square.setUserData(new int[] { row, col }); // Store row and col in user data
                square.setOnMouseClicked(e -> handleCellClick(e.getButton(), square));
                buttonGrid[row][col] = square;
                gameGrid.add(square, col, row);
            }
        }
    }

    private void handleCellClick(MouseButton button, Button square) {
        int[] coords = (int[]) square.getUserData();
        int row = coords[0];
        int col = coords[1];
        if (button == MouseButton.PRIMARY) {
            System.out.println("Left click on cell (" + row + ", " + col + ")");
            handleLeftClick(square, row, col);
            // square.setDisable(firstClick); // Disable button after first click
        } else if (button == MouseButton.SECONDARY) {
            System.out.println("Right click on cell (" + row + ", " + col + ")");
            handleRightClick(square,row, col);
        }
        updateGameGrid();
        printGrid();
    }

    private void updateGameGrid() {
        for (int row = 0; row < game.getRows(); row++) {
            for (int col = 0; col < game.getCols(); col++) {
                MineSweeperMessages message = game.getNode(row, col); // Use row, col here
                String cell;
                if (message == MineSweeperMessages.BOMB_AND_REVEALED) {
                    cell = "B"; // Bomb revealed
                } else if (message == MineSweeperMessages.FLAGGED_NODE) {
                    cell = "F";
                } else if (message == MineSweeperMessages.REVEALED_NODE) {
                    int bombsNearby = game.howManyBombsNearbyTile(row, col);
                    cell = String.valueOf(bombsNearby); // Empty or number of bombs nearby
                } else {
                    cell = "";
                }
                buttonGrid[row][col].setText(cell);
                if (message == MineSweeperMessages.REVEALED_NODE || message == MineSweeperMessages.BOMB_AND_REVEALED) {
                    buttonGrid[row][col].setDisable(true); // Disable button if revealed
                }
            }
        }
    }

    private void handleLeftClick(Button square, int row, int col) {
        // Implement left-click logic (e.g., reveal cell)
        System.out.println("Revealing cell at (" + row + ", " + col + ")");
        // Example: game.revealCell(row, col);

        if (firstClick) {
            game.startGame(row, col); // Place bombs and calculate counts on first click
            firstClick = false;
        } else {
            System.out.println("Cell clicked at (" + row + ", " + col + ")");
            // Implement game logic here
            MineSweeperMessages message = game.revealNode(row, col); // Use row, col here
            String cell = "/";
            if (message == MineSweeperMessages.REVEALED_NODE) {
                System.out.println("Node is already revealed.");
            } else if (message == MineSweeperMessages.BOMB_NODE) {
                System.out.println("Game Over! You hit a bomb at (" + row + ", " + col + ").");
                // Reveal all nodes and end the game
                //game.revealAllNodes();
                cell = "B"; // Bomb revealed
            } else if (message == MineSweeperMessages.NODE_NOW_REVEALED) {
                int bombs = game.howManyBombsNearbyTile(row, col);
                System.out.println("Node at (" + row + ", " + col + ") revealed. Bombs nearby: " + bombs);
                cell = String.valueOf(bombs); // Empty or number of bombs nearby
            } else {
                System.out.println("There was an error revealing the node at (" + row + ", " + col + ").");
            }
            square.setText(cell);
            square.setDisable(true); // Disable button after revealing
            //System.out.println("Cell state: " + cell);
        }
    }

    private void handleRightClick(Button square, int row, int col) {
        // Implement right-click logic (e.g., flag cell)
        System.out.println("Flagging cell at (" + row + ", " + col + ")");
        MineSweeperMessages message = game.flagNode(row, col);
        if (message == MineSweeperMessages.REVEALED_NODE) {
            System.out.println("Node is already revealed. Cannot flag.");
        } else {
            System.out.println("Node at (" + row + ", " + col + ") flagged.");
        }
    }

    /**
     * Prints the current state of the mine grid.
     */
    public void printGrid() {
        int rows = game.getRows();
        int cols = game.getCols();
        // Grid content
        for (int row = 0; row < rows; row++) {
            System.out.printf("%2d |", row + 1); // Row number with left padding
            for (int col = 0; col < cols; col++) {
                MineSweeperMessages message = game.getNode(row, col); // Use row, col here
                String cell;
                if (message == MineSweeperMessages.BOMB_AND_REVEALED) {
                    cell = "B"; // Bomb revealed
                } else if (message == MineSweeperMessages.FLAGGED_NODE) {
                    cell = "F";
                } else if (message == MineSweeperMessages.REVEALED_NODE) {
                    int bombsNearby = game.howManyBombsNearbyTile(row, col);
                    cell = String.valueOf(bombsNearby); // Empty or number of bombs nearby
                } else {
                    cell = ".";
                }
                System.out.printf("  %s", cell); // Print cell with spacing

            }
            System.out.println();
        }
    }
}
