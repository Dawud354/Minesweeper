import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Separator;
import javafx.scene.control.Spinner;

public class MenuView {

    private final SceneManager manager;

    // make these fields so other methods can update them
    private GridPane previewGrid;
    private HBox sizeBox;
    private HBox minesBox;
    private Spinner<Integer> rowsSpinner;
    private Spinner<Integer> colsSpinner;
    private Spinner<Integer> minesSpinner;
    private int gridRows;
    private int gridCols;
    private int mineCount;

    public MenuView(SceneManager manager) {
        this.manager = manager;

    }

    public Parent getView() {
        // === Difficulty Options ===
        ToggleGroup difficultyGroup = new ToggleGroup();
        RadioButton easy = new RadioButton("Beginner");
        RadioButton medium = new RadioButton("Intermediate");
        RadioButton hard = new RadioButton("Expert");
        RadioButton custom = new RadioButton("Custom");

        easy.getStyleClass().add("radio-button");
        medium.getStyleClass().add("radio-button");
        hard.getStyleClass().add("radio-button");
        custom.getStyleClass().add("radio-button");

        easy.setToggleGroup(difficultyGroup);
        medium.setToggleGroup(difficultyGroup);
        hard.setToggleGroup(difficultyGroup);
        custom.setToggleGroup(difficultyGroup);

        easy.setOnAction(e -> handleDifficultyChange(easy));
        medium.setOnAction(e -> handleDifficultyChange(medium));
        hard.setOnAction(e -> handleDifficultyChange(hard));
        custom.setOnAction(e -> handleDifficultyChange(custom));

        easy.setSelected(true); // default
        // Set default values (not most elegant way, but straightforward)
        gridRows = 9;
        gridCols = 9;
        mineCount = 10;

        Label difficultyLabel = new Label("Select Difficulty:");
        difficultyLabel.getStyleClass().add("section-header");
        VBox difficultyBox = new VBox(10, difficultyLabel, easy, medium, hard, custom);
        difficultyBox.getStyleClass().add("difficulty-box");
        difficultyBox.setAlignment(Pos.CENTER);

        // === Details Section ===
        GridPane details = new GridPane();
        details.setHgap(10);
        details.setVgap(5);

        rowsSpinner = new Spinner<>(5, 25, gridRows);
        colsSpinner = new Spinner<>(5, 25, gridCols);
        minesSpinner = new Spinner<>(1, 99, mineCount);

        rowsSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            gridRows = newValue;
            updatePreview();
        });
        colsSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            gridCols = newValue;
            updatePreview();
        });
        minesSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            mineCount = newValue;
            updatePreview();
        });

        rowsSpinner.disableProperty().set(true);
        colsSpinner.disableProperty().set(true);
        minesSpinner.disableProperty().set(true);

        minesBox = new HBox(5, new Label("Mines: "), minesSpinner);
        sizeBox = new HBox(5, new Label("Size: " ), rowsSpinner, new Label("x"), colsSpinner);

        sizeBox.setAlignment(Pos.CENTER);
        minesBox.setAlignment(Pos.CENTER);

        //sizeLabel.getStyleClass().add("detail-label");
        //minesLabel.getStyleClass().add("detail-label");
        details.addRow(0, sizeBox);
        details.addRow(1, minesBox);
        StackPane detailsWrapper = new StackPane(details);
        detailsWrapper.setMaxWidth(Region.USE_PREF_SIZE); // optional: shrink to fit

        VBox detailsBox = new VBox(5, new Label("Details:"), detailsWrapper);
        detailsBox.getStyleClass().add("section-header");
        detailsBox.setAlignment(Pos.CENTER);

        // === Preview Section ===
        previewGrid = new GridPane();
        previewGrid.getStyleClass().add("preview-grid");
        updatePreview(); // initial preview
        previewGrid.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        // === Start Button ===
        Button startBtn = new Button("Start Game");
        startBtn.getStyleClass().add("start-button");
        startBtn.setOnAction(e -> submitButtonHandler());
        /*
         * // === Main content stack ===
         * VBox content = new VBox(20,
         * difficultyBox,
         * new Separator(),
         * detailsBox,
         * new Separator(),
         * previewBox,
         * new Separator(),
         * startBtn);
         * content.setAlignment(Pos.CENTER);
         * content.setPadding(new Insets(20));
         * 
         * // === Root centers content ===
         * StackPane root = new StackPane(content);
         * 
         * 
         */

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        Label previewLabel = new Label("Preview:");
        previewLabel.getStyleClass().add("section-header");
        VBox topSection = new VBox(20,
                difficultyBox,
                new Separator(),
                detailsBox,
                new Separator(),
                previewLabel
        );
        topSection.setAlignment(Pos.TOP_CENTER);


        VBox middleSection = new VBox(10,
                previewGrid);
        middleSection.setAlignment(Pos.CENTER);

        VBox bottomSection = new VBox(20,
                new Separator(),
                startBtn);
        bottomSection.setAlignment(Pos.BOTTOM_CENTER);

        // Place preview in the middle
        //StackPane previewWrapper = new StackPane(previewGrid);
        //previewWrapper.setMaxSize(400, 400); // preview won't grow beyond this
        //previewWrapper.setPrefSize(400, 400);

        root.setTop(topSection);
        root.setCenter(middleSection);
        root.setBottom(bottomSection);

        return root;
    }

    private void handleDifficultyChange(RadioButton selected) {
        System.out.println("Selected difficulty: " + selected.getText());
        
        if (selected.getText().equals("Custom")) {
            rowsSpinner.disableProperty().set(false);
            colsSpinner.disableProperty().set(false);
            minesSpinner.disableProperty().set(false);
        } else{
            rowsSpinner.disableProperty().set(true);
            colsSpinner.disableProperty().set(true);
            minesSpinner.disableProperty().set(true);
        }
        switch (selected.getText()) {
            case "Beginner" -> {
                gridRows = 9;
                gridCols = 9;
                mineCount = 10;
            }
            case "Intermediate" -> {
                gridRows = 16;
                gridCols = 16;
                mineCount = 40;
            }
            case "Expert" -> {
                gridRows = 24;
                gridCols = 24;
                mineCount = 99;
            }
        }
        // Update details and preview based on selection
        updateDetails();
        updatePreview();
    }

    private void updateDetails() {
        System.out.println("Updating details: " + gridRows + "x" + gridCols + ", Mines: " + mineCount);
        rowsSpinner.getValueFactory().setValue(gridRows);
        colsSpinner.getValueFactory().setValue(gridCols);
        minesSpinner.getValueFactory().setValue(mineCount);

    }

    // Optional: method to update the preview grid
    private void updatePreview() {
        previewGrid.getChildren().clear();
        for (int row = 0; row < gridRows; row++) {
            for (int col = 0; col < gridCols; col++) {
                Region square = new Region();
                //square.setPrefSize(20, 20);
                square.getStyleClass().add("square");
                previewGrid.add(square, col, row);
            }
        }
    }

    private void submitButtonHandler() {
        // Handle start game action
        if (mineCount > gridRows * gridCols - 10) {
            // Invalid configuration
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Invalid Configuration");
            alert.setHeaderText("Too many mines!");
            alert.showAndWait();
            return;
        }
        manager.showGame(gridRows, gridCols, mineCount);
    }
}
