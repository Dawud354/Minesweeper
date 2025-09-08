import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Separator;

public class MenuView {

    private final SceneManager manager;

    // make these fields so other methods can update them
    private GridPane previewGrid;
    private Label sizeLabel;
    private Label minesLabel;
    private int gridRows;
    private int gridCols;
    private int mineCount;

    public MenuView(SceneManager manager) {
        this.manager = manager;
        sizeLabel = new Label();
        minesLabel = new Label();
    }

    public Parent getView() {
        // === Difficulty Options ===
        ToggleGroup difficultyGroup = new ToggleGroup();
        RadioButton easy = new RadioButton("Beginner");
        RadioButton medium = new RadioButton("Intermediate");
        RadioButton hard = new RadioButton("Expert");

        easy.getStyleClass().add("radio-button");
        medium.getStyleClass().add("radio-button");
        hard.getStyleClass().add("radio-button");

        easy.setToggleGroup(difficultyGroup);
        medium.setToggleGroup(difficultyGroup);
        hard.setToggleGroup(difficultyGroup);

        easy.setOnAction(e -> handleDifficultyChange(easy));
        medium.setOnAction(e -> handleDifficultyChange(medium));
        hard.setOnAction(e -> handleDifficultyChange(hard));

        easy.setSelected(true); // default
        // Set default values (not most elegant way, but straightforward)
        gridRows = 9;
        gridCols = 9;
        mineCount = 10;

        Label difficultyLabel = new Label("Select Difficulty:");
        difficultyLabel.getStyleClass().add("section-header");
        VBox difficultyBox = new VBox(10, difficultyLabel, easy, medium, hard);
        difficultyBox.getStyleClass().add("difficulty-box");
        difficultyBox.setAlignment(Pos.CENTER);

        // === Details Section ===
        GridPane details = new GridPane();
        details.setHgap(10);
        details.setVgap(5);

        updateDetails(); // initial details
        sizeLabel.getStyleClass().add("detail-label");
        minesLabel.getStyleClass().add("detail-label");
        details.addRow(0, sizeLabel);
        details.addRow(1, minesLabel);
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
        startBtn.setOnAction(e -> manager.showGame(gridRows, gridCols, mineCount));
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
        sizeLabel.setText("Grid: " + gridCols + " x " + gridRows);
        minesLabel.setText("Mines: " + String.valueOf(mineCount));
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
}
