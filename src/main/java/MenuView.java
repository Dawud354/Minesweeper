import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
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
    }

    public Parent getView() {
        // === Difficulty Options ===
        ToggleGroup difficultyGroup = new ToggleGroup();
        RadioButton easy = new RadioButton("Beginner");
        RadioButton medium = new RadioButton("Intermediate");
        RadioButton hard = new RadioButton("Expert");

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

        VBox difficultyBox = new VBox(10, new Label("Difficulty:"), easy, medium, hard);
        difficultyBox.setAlignment(Pos.CENTER);

        // === Details Section ===
        GridPane details = new GridPane();
        details.setHgap(10);
        details.setVgap(5);

        sizeLabel = new Label(gridCols + " x " + gridRows);
        minesLabel = new Label(String.valueOf(mineCount));
        details.addRow(0, new Label("Size:"), sizeLabel);
        details.addRow(1, new Label("Mines:"), minesLabel);
        StackPane detailsWrapper = new StackPane(details);
        detailsWrapper.setMaxWidth(Region.USE_PREF_SIZE); // optional: shrink to fit

        VBox detailsBox = new VBox(5, new Label("Details:"), detailsWrapper);
        detailsBox.setAlignment(Pos.CENTER);

        // === Preview Section ===
        previewGrid = new GridPane();
        previewGrid.setHgap(2);
        previewGrid.setVgap(2);
        updatePreview(); // initial preview
        StackPane previewWrapper = new StackPane(previewGrid);
        previewWrapper.setMaxWidth(Region.USE_PREF_SIZE); // optional

        VBox previewBox = new VBox(5, new Label("Preview:"), previewWrapper);
        previewBox.setAlignment(Pos.CENTER);

        // === Start Button ===
        Button startBtn = new Button("Start Game");
        startBtn.setOnAction(e -> manager.showGame(gridRows, gridCols, mineCount));

        // === Main content stack ===
        VBox content = new VBox(20,
                difficultyBox,
                new Separator(),
                detailsBox,
                new Separator(),
                previewBox,
                new Separator(),
                startBtn);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(20));

        // === Root centers content ===
        StackPane root = new StackPane(content);

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
        sizeLabel.setText(gridCols + " x " + gridRows);
        minesLabel.setText(String.valueOf(mineCount));
        updatePreview();
    }

    // Optional: method to update the preview grid
    private void updatePreview() {
        previewGrid.getChildren().clear();
        for (int row = 0; row < gridRows; row++) {
            for (int col = 0; col < gridCols; col++) {
                Region square = new Region();
                square.setPrefSize(20, 20);
                square.setStyle("-fx-border-color: black; -fx-background-color: lightgray;");
                previewGrid.add(square, col, row);
            }
        }
    }
}
