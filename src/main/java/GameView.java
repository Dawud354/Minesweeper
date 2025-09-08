import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.geometry.Pos;
import javafx.scene.Parent;

public class GameView {
    private final SceneManager manager;
    private MineSweeper game;
    private GridPane gameGrid;
    private boolean firstClick;

    public GameView(SceneManager manager, int gridRows, int gridCols, int mineCount) {
        this.manager = manager;
        game = new MineSweeper(gridRows, gridCols, mineCount);
        gameGrid = new GridPane();
        gameGrid.setHgap(2);
        gameGrid.setVgap(2);
        firstClick = true; // To track if it's the first click
    }

    public Parent getView() {

        // === Preview Section ===
        updateGrid(); // initial preview
        // StackPane previewWrapper = new StackPane(gameGrid);
        // previewWrapper.setMaxWidth(Region.USE_PREF_SIZE); // optional

        // VBox previewBox = new VBox(5, previewWrapper);
        // previewBox.setAlignment(Pos.CENTER);
        // StackPane root = new StackPane(previewBox);
        Label status = new Label("Mines left: 10");
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
    private void updateGrid() {
        gameGrid.getChildren().clear();
        for (int row = 0; row < game.getRows(); row++) {
            for (int col = 0; col < game.getCols(); col++) {
                Region square = new Region();
                square.setPrefSize(20, 20);
                square.setStyle("-fx-border-color: black; -fx-background-color: lightgray;");
                // --- Hover detection ---
                square.setOnMouseEntered(e -> {
                    square.setStyle("-fx-border-color: black; -fx-background-color: yellow;");
                });

                square.setOnMouseExited(e -> {
                    square.setStyle("-fx-border-color: black; -fx-background-color: lightgray;");
                });
                // Detect clicks
                final int r = row;
                final int c = col;

                square.setOnMouseClicked(e -> handleCellClick(e.getButton(), r, c));

                gameGrid.add(square, col, row);
            }
        }
    }

    private void handleCellClick(MouseButton button ,int row, int col) {
        if (button == MouseButton.PRIMARY) {
            System.out.println("Left click on cell (" + row + ", " + col + ")");
            handleLeftClick(row, col);
        } else if (button == MouseButton.SECONDARY) {
            System.out.println("Right click on cell (" + row + ", " + col + ")");
            handleRightClick(row, col);
        }
        printGrid();
    }

    private void handleLeftClick(int row, int col) {
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
            System.out.println("Cell state: " + cell);
        }
    }
    private void handleRightClick(int row, int col) {
        // Implement right-click logic (e.g., flag cell)
        System.out.println("Flagging cell at (" + row + ", " + col + ")");
        // Example: game.flagCell(row, col);
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
