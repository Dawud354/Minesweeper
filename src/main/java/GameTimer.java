import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class GameTimer extends Label {
    private int seconds = 0;
    private Timeline timeline;

    public GameTimer() {
        super("Time: 0"); // initial label text
        // Initialize the timeline to update every second
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> update()));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    /**
     * Updates the timer label every second.
     */
    private void update() {
        seconds++;
        setText("Time: " + seconds);
    }

    /**
     * Starts the timer.
     */
    public void start() {
        seconds = 0;
        setText("Time: 0");
        timeline.playFromStart();
    }

    /**
     * Stops the timer.
     */
    public void stop() {
        timeline.stop();
    }

    /**
     * Resets the timer to zero and stops it.
     */
    public void reset() {
        stop();
        seconds = 0;
        setText("Time: 0");
    }

    /**
     * Returns the elapsed time in seconds.
     */
    public int getSeconds() {
        return seconds;
    }
}
