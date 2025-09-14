import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private final Image flagImage = new Image(getClass().getResource("/images/flag.png").toExternalForm());
    private final Image bombImage = new Image(getClass().getResource("/images/mine.png").toExternalForm());

    public GameView(SceneManager manager, int gridRows, int gridCols, int mineCount) {
        this.manager = manager;
        game = new MineSweeper(gridRows, gridCols, mineCount);
        gameGrid = new GridPane();
        gameGrid.setHgap(1);
        gameGrid.setVgap(1);
        firstClick = true; // To track if it's the first click
    }

    /**
     * Create and return the main view for the game
     * Consists of instruction label, game grid, and info section
     * @return Parent node containing the game view
     */
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

        // === Info Section ===
        statusLabel = new Label("Mines left: " + game.getNumberOfMinesLeft());
        gameTimer = new GameTimer();
        statusLabel.getStyleClass().add("detail-label");
        // --- Put labels side by side ---
        HBox infoBox = new HBox(20, statusLabel, gameTimer);
        infoBox.setMaxWidth(Region.USE_PREF_SIZE);
        infoBox.setAlignment(Pos.CENTER);
        infoBox.getStyleClass().add("game-view-box");

        BorderPane root = new BorderPane();
        VBox topSection = new VBox(10, instructionBox, new Separator());
        VBox centerSection = new VBox(10, gameGrid);
        VBox bottomSection = new VBox(10, new Separator(), infoBox);

        // middle section has not style as the grid pane already has it
        topSection.getStyleClass().add("top-section");
        bottomSection.getStyleClass().add("bottom-section");

        topSection.setAlignment(Pos.CENTER);
        centerSection.setAlignment(Pos.CENTER);
        bottomSection.setAlignment(Pos.CENTER);

        root.setCenter(centerSection);
        root.setTop(topSection);
        root.setBottom(bottomSection);
        return root;
    }

    /**
     * Create the grid of buttons representing the game board
     * Each button corresponds to a cell in the MineSweeper game
     */
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
                square.setMaxSize(25, 25);
                buttonGrid[row][col] = square;
                gameGrid.add(square, col, row);
            }
        }
    }

    /**
     * Handle cell click based on mouse button
     * Calls appropriate method for left or right click
     * 
     * @param button MouseButton used
     * @param square Button clicked
     */
    private void handleCellClick(MouseButton button, Button square) {
        int[] coords = (int[]) square.getUserData();
        int row = coords[0];
        int col = coords[1];
        if (button == MouseButton.PRIMARY) {
            handleLeftClick(square, row, col);
        } else if (button == MouseButton.SECONDARY) {
            handleRightClick(square, row, col);
        }

        updateGameGrid();

        if (game.getGameStatus() == MineSweeperMessages.GAME_WON) {
            wonGame();
        }
    }

    /**
     * Update the game grid buttons based on current game state
     * Shows numbers, flags, bombs as needed
     */
    private void updateGameGrid() {
        for (int row = 0; row < game.getRows(); row++) {
            for (int col = 0; col < game.getCols(); col++) {
                MineSweeperMessages message = game.getNode(row, col); // Use row, col here
                if (message == MineSweeperMessages.REVEALED_NODE || message == MineSweeperMessages.BOMB_AND_REVEALED) {
                    buttonGrid[row][col].setDisable(true); // Disable button if revealed
                }
                if (message == MineSweeperMessages.EMPTY_NODE || message == MineSweeperMessages.BOMB_AND_HIDDEN) {
                    buttonGrid[row][col].setText("");
                    buttonGrid[row][col].setGraphic(null); // Remove any graphic
                }
                if (message == MineSweeperMessages.REVEALED_NODE) {
                    buttonGrid[row][col].setGraphic(null); // Remove any graphic
                    int bombsNearby = game.howManyBombsNearbyTile(row, col);
                    putNumberOfMinesOnButton(buttonGrid[row][col], bombsNearby);
                }
                if (message == MineSweeperMessages.BOMB_AND_REVEALED) {
                    putImageOnButton(buttonGrid[row][col], bombImage);
                } else if (message == MineSweeperMessages.FLAGGED_NODE) {
                    putImageOnButton(buttonGrid[row][col], flagImage);
                }
            }
        }
    }

    /**
     * Put an image on a mine button, resizing it to fit
     * @param button Button to put image on
     * @param image Image to put on button
     */
    private void putImageOnButton(Button button, Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        button.setGraphic(imageView);
    }

    /**
     * Put number of mines on a button with appropriate color
     * @param button Button to put number on
     * @param number Number of mines nearby
     */
    private void putNumberOfMinesOnButton(Button button, int number) {
        Color color = Color.BLACK; // Default color
        color = getColorForNumber(number);
        if (number == 0) {
            button.setText(""); // Empty cell for 0 bombs nearby
        } else {
            button.setText(String.valueOf(number)); // Show number of bombs nearby
        }
        button.setTextFill(color);
    }

    /**
     * Get color for number of mines
     * 1 - Blue, 2 - Green, 3 - Red, 4 - Dark Blue, 5 - Brown, 6 - Cyan, 7 - Black, 8 - Gray
     * Default to Black if out of range
     * @param number Number of mines
     * @return Color for the number of mines
     */
    private Color getColorForNumber(int number) {
        return switch (number) {
            case 1 -> Color.BLUE;
            case 2 -> Color.GREEN;
            case 3 -> Color.RED;
            case 4 -> Color.DARKBLUE;
            case 5 -> Color.BROWN;
            case 6 -> Color.CYAN;
            case 7 -> Color.BLACK;
            case 8 -> Color.GRAY;
            default -> Color.BLACK; // fallback
        };
    }

    /**
     * Handle left click on a square
     * If first click, start game and timer
     * If bomb clicked, end game
     * @param square Button representing the square
     * @param row Row index of the square
     * @param col Column index of the square
     */
    private void handleLeftClick(Button square, int row, int col) {
        if (firstClick) {
            game.startGame(row, col); // Place bombs and calculate counts on first click
            gameTimer.start(); // Start the timer on first click
            firstClick = false;
        } else {
            // check if node is flagged and then ignore left click
            if (game.getNode(row, col) == MineSweeperMessages.FLAGGED_NODE) {
                return;
            }
            MineSweeperMessages message = game.revealNode(row, col); // Use row, col here
            if (message == MineSweeperMessages.BOMB_NODE) {
                if (game.getGameStatus() == MineSweeperMessages.GAME_OVER) {
                    lostGame();
                }
            } 
        }
    }

    /**
     * Handle losing the game
     * Stop timer, disable all buttons, show message
     */
    private void lostGame() {
        stopGame();
        instructionLabel.setText("Game Over! You hit a bomb.");
    }

    /**
     * Handle winning the game
     * Stop timer, disable all buttons, show message
     */
    private void wonGame() {
        stopGame();
        instructionLabel.setText("Congratulations! You've won the game!");
    }

    /**
     * Stop the game and timer, disable all buttons
     * Called on game over or win
     */
    private void stopGame() {
        gameTimer.stop();
        // Disable all buttons
        updateGameGrid();
        for (int row = 0; row < game.getRows(); row++) {
            for (int col = 0; col < game.getCols(); col++) {
                buttonGrid[row][col].setDisable(true);
            }
        }
    }

    /**
     * Handle right click on a square to flag/unflag
     * Update mines left count
     * @param square Button representing the square
     * @param row Row index of the square
     * @param col Column index of the square
     */
    private void handleRightClick(Button square, int row, int col) {
        MineSweeperMessages message = game.flagNode(row, col);
        statusLabel.setText("Mines left: " + game.getNumberOfMinesLeft());
        
    }
}
