import javafx.application.Application;
import javafx.stage.Stage;

public class MainGUI extends Application {

    @Override
    public void start(Stage stage) {
        SceneManager manager = new SceneManager(stage);
        manager.showMenu(); // show menu first
        stage.setTitle("Minesweeper");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args); // This triggers the JavaFX runtime
    }
}
