public class Node {
    private int bombsNearby;
    private boolean isBomb;
    private boolean isRevealed;
    private boolean isFlagged;

    public Node() {
        this.bombsNearby = 0;
        this.isBomb = false;
        this.isRevealed = false;
        this.isFlagged = false;
    }

    public int getBombsNearby() {
        return bombsNearby;
    }

    public void setBombsNearby(int bombsNearby) {
        this.bombsNearby = bombsNearby;
    }

    public boolean isBomb() {
        return isBomb;
    }

    public void setBomb(boolean isBomb) {
        this.isBomb = isBomb;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void setRevealed(boolean isRevealed) {
        this.isRevealed = isRevealed;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setFlagged(boolean isFlagged) {
        this.isFlagged = isFlagged;
    }

    @Override
    public String toString() {
        if (isRevealed) {
            if (isBomb) {
                return "B"; // Bomb
            } else {
                return String.valueOf(bombsNearby); // Number of bombs nearby
            }
        } else if (isFlagged) {
            return "F"; // Flagged
        } else {
            return "."; // Hidden
        }
    }
}
