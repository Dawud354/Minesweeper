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

        scene = new Scene(root, 800, 600);
        stage.setScene(scene);
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(e -> stage.close());
        fileMenu.getItems().add(exitItem);

        Menu helpMenu = new Menu("Help");
        MenuItem aboutItem = new MenuItem("About");
        aboutItem.setOnAction(e -> System.out.println("About clicked"));
        helpMenu.getItems().add(aboutItem);

        menuBar.getMenus().addAll(fileMenu, helpMenu);
        return menuBar;
    }


    // Swap only the center
    public void showMenu() {
        MenuView menu = new MenuView(this);
        root.setCenter(menu.getView());
    }

    public void showGame(int rows, int cols, int mines) {
        GameView game = new GameView(this, rows, cols, mines);
        root.setCenter(game.getView());
    }
}
