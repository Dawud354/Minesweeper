import javafx.scene.layout.BorderPane;
import javafx.scene.control.Label;
import javafx.scene.Parent;

public class GameView {
    private final SceneManager manager;
    private final int gridRows;
    private final int gridCols;
    private final int mineCount;
    private MineSweeper game;

    public GameView(SceneManager manager,int gridRows, int gridCols, int mineCount) {
        this.manager = manager;
        this.gridRows = gridRows;
        this.gridCols = gridCols;
        this.mineCount = mineCount;
        //game = new MineSweeper(gridRows, gridCols, mineCount);
    }

    public Parent getView() {
        BorderPane root = new BorderPane();
        root.setCenter(new Label("Minesweeper grid here"));
        return root;
    }
}
