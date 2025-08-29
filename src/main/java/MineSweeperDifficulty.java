public enum MineSweeperDifficulty {
    EASY (0.1),
    MEDIUM (0.2),
    HARD (0.3);
    private final double bombPercentage;
    MineSweeperDifficulty(double bombPercentage) {
        this.bombPercentage = bombPercentage;
    }
    public double getBombPercentage() {
        return bombPercentage;
    }

}
