# Minesweeper

A classic Minesweeper game built in Java. Click the tiles to uncover safe squares and avoid the mines!

For the latest release please click [here](https://github.com/YourUsername/Minesweeper/releases)

---

## Game Modes

* **Easy** 
* **Medium** 
* **Hard** 
* **Custom:** Set your own board size and number of mines (max 25×25 and 99 mines)

---

## How to Play

* Use your **mouse** to click on tiles:

  * **Left-click** to reveal a square
  * **Right-click** to flag a mine
* Clear all safe squares without detonating any mines to win.

---

## Installation / Running the Game

### Windows:

* Download `Minesweeper.exe` from the release page.
* Double-click to run the game.

### Cross-platform (Java `.jar`):

* Download `Minesweeper.jar` from the release page.
* Make sure you have **Java installed** (version 22 or higher recommended).
* Run with the command:

```bash
java -jar Minesweeper.jar
```

> The `.jar` includes all required libraries (JavaFX), so no extra setup is needed.

### Build from Source

* The project uses **Maven** and includes a `pom.xml`.
* To build and run the game from source:

```bash
mvn exec:java
```

* Make sure Maven is installed and your system has Java set up correctly.
* This is mainly for testing, development, or modifying the game.

---

## Links / Credits

- **Project repository:** [GitHub Repo](https://github.com/Dawud354/Minesweeper)
- **Report issues or suggestions:** Use the GitHub Issues tab on the repository.  
- Built in Java with JavaFX.
