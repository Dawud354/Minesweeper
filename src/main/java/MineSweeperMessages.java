public enum MineSweeperMessages {
    INVALID_COORDINATES("Coordinates out of bounds"),
    GAME_OVER("Game Over"),
    GAME_WON("All tiles revealed"),
    GAME_ONGOING("Game is ongoing"),
    INVALID_MOVE("Invalid move. Please try again."),
    FLAGGED_NODE("Node is flagged. Cannot reveal."),
    REVEALED_NODE("Node is already revealed."),
    NODE_NOW_REVEALED("Node is now revealed"),
    EMPTY_NODE("Node is empty"),
    BOMB_NODE("Bomb"),
    BOMB_AND_REVEALED("Bomb revealed"),
    BOMB_AND_HIDDEN("Bomb hidden"),
    UNFLAGGED_NODE("Node is unflagged");

    private final String message;

    MineSweeperMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
