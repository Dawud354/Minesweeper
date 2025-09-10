import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
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
    private Label statusLabel;
    private Label instructionLabel;
    private GameTimer gameTimer;

    public GameView(SceneManager manager, int gridRows, int gridCols, int mineCount) {
        this.manager = manager;
        game = new MineSweeper(gridRows, gridCols, mineCount);
        gameGrid = new GridPane();
        gameGrid.setHgap(1);
        gameGrid.setVgap(1);
        firstClick = true; // To track if it's the first click
    }

    public Parent getView() {
        // === Instructions Section ===
        instructionLabel = new Label("Welcome to MineSweeper! Left Click to Reveal, Right Click to Flag/Unflag.");
        instructionLabel.getStyleClass().add("section-header");
        HBox instructionBox = new HBox(20, instructionLabel);
        instructionBox.setMaxWidth(Region.USE_PREF_SIZE);
        instructionBox.setAlignment(Pos.CENTER);
        instructionBox.getStyleClass().add("game-view-box");

        // === Grid Section ===
        createGrid(); // initial preview
        gameGrid.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        statusLabel = new Label("Mines left: " + game.getNumberOfMinesLeft());
        gameTimer = new GameTimer();
        statusLabel.getStyleClass().add("detail-label");
        //timer.getStyleClass().add("detail-label");
        // --- Put labels side by side ---
        HBox infoBox = new HBox(20, statusLabel, gameTimer);
        infoBox.setMaxWidth(Region.USE_PREF_SIZE);
        infoBox.setAlignment(Pos.CENTER);
        infoBox.getStyleClass().add("game-view-box");

        BorderPane root = new BorderPane();
        VBox topSection = new VBox(10, instructionBox, new Separator());
        VBox centerSection = new VBox(10, gameGrid);
        VBox bottomSection = new VBox(10, new Separator(), infoBox);

        topSection.getStyleClass().add("top-section");
        bottomSection.getStyleClass().add("bottom-section");


        //topSection.getStyleClass().add("game-view-box");
        //centerSection.getStyleClass().add("game-view-box");
        //bottomSection.getStyleClass().add("game-view-box");

        topSection.setAlignment(Pos.CENTER);
        centerSection.setAlignment(Pos.CENTER);
        bottomSection.setAlignment(Pos.CENTER);

        root.setCenter(centerSection);
        root.setTop(topSection);
        root.setBottom(bottomSection);
        return root;
    }

    // Optional: method to update the game grid
    private void createGrid() {
        buttonGrid = new Button[game.getRows()][game.getCols()];
        gameGrid.getChildren().clear();
        for (int row = 0; row < game.getRows(); row++) {
            for (int col = 0; col < game.getCols(); col++) {
                Button square = new Button();
                square.setFocusTraversable(false);
                square.getStyleClass().add("square");
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
        if(game.getGameStatus() == MineSweeperMessages.GAME_WON) {
            wonGame();
        }
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
            gameTimer.start(); // Start the timer on first click
            firstClick = false;
        } else {
            System.out.println("Cell clicked at (" + row + ", " + col + ")");
            // check if node is flagged and then ignore left click
            if (game.getNode(row, col) == MineSweeperMessages.FLAGGED_NODE) {
                System.out.println("Node is flagged. Cannot reveal.");
                return;
            }
            // Implement game logic here
            MineSweeperMessages message = game.revealNode(row, col); // Use row, col here
            String cell = "/";
            if (message == MineSweeperMessages.BOMB_NODE) {
                if (game.getGameStatus() == MineSweeperMessages.GAME_OVER) {
                    System.out.println("Game Over! You hit a bomb at (" + row + ", " + col + ").");
                    lostGame();
                }
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

    private void lostGame() {
        gameTimer.stop();
        updateGameGrid();
        // Disable all buttons
        for (int row = 0; row < game.getRows(); row++) {
            for (int col = 0; col < game.getCols(); col++) {
                buttonGrid[row][col].setDisable(true);
            }
        }
        instructionLabel.setText("Game Over! You hit a bomb.");
    }

    private void wonGame() {
        gameTimer.stop();
        // Disable all buttons
        updateGameGrid();
        for (int row = 0; row < game.getRows(); row++) {
            for (int col = 0; col < game.getCols(); col++) {
                buttonGrid[row][col].setDisable(true);
            }
        }
        instructionLabel.setText("Congratulations! You've won the game!");
    }

    private void handleRightClick(Button square, int row, int col) {
        // Implement right-click logic (e.g., flag cell)
        System.out.println("Flagging cell at (" + row + ", " + col + ")");
        MineSweeperMessages message = game.flagNode(row, col);
        if (message == MineSweeperMessages.REVEALED_NODE) {
            System.out.println("Node is already revealed. Cannot flag.");
        } else {
            System.out.println("Node at (" + row + ", " + col + ") flagged.");
            statusLabel.setText("Mines left: " + game.getNumberOfMinesLeft());
            // Update button text to show flag
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
