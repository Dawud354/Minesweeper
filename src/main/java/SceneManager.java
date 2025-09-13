import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;


public class SceneManager {
    private final Stage stage;
    private final Scene scene;
    private final BorderPane root; // permanent root


    public SceneManager(Stage stage) {
        this.stage = stage;

        // permanent root with menu bar on top
        root = new BorderPane();
        root.setTop(createMenuBar());

        scene = new Scene(root, 800, 800);
        // connect stylesheet
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setScene(scene);
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        // Exit menu
        Menu exitMenu = new Menu("Exit");
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(e -> stage.close());
        exitMenu.getItems().add(exitItem);

        // Restart menu 
        Menu restartMenu = new Menu("Restart");
        MenuItem restartItem = new MenuItem("Restart");
        restartItem.setOnAction(e -> restartGame());
        restartMenu.getItems().add(restartItem);

        menuBar.getMenus().addAll(exitMenu, restartMenu);
        return menuBar;
    }

    private void restartGame() {
        showMenu();
    }


    /**
     * Show the main menu view in the center of the root
     * Creates a new MenuView instance and sets it in the center
     */
    public void showMenu() {
        MenuView menu = new MenuView(this);
        root.setCenter(menu.getView());
    }

    /**
     * Show the game view in the center of the root
     * Creates a new GameView instance with specified parameters and sets it in the center
     * @param rows Number of rows for the game grid
     * @param cols Number of columns for the game grid
     * @param mines Number of mines for the game
     */
    public void showGame(int rows, int cols, int mines) {
        GameView game = new GameView(this, rows, cols, mines);
        root.setCenter(game.getView());
    }
}
