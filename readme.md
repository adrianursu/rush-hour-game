# Rush Hour Shift Game (2 Players)

The ultimate goal is to get the hero vehicle to the opposite side of the board.

## Prerequisites

Before you begin, ensure you have met the following requirements:
- You have installed the latest version of [Java JDK](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- You have installed [Git](https://git-scm.com/downloads).

## Getting Started

These instructions will get you a copy of the project up and running on your local machine in order to test the Rush Hour Shift Game.

### Cloning the Repository and Running The Application

Follow these commands in the terminal in order to clone the repository and running the application.
```bash
git clone https://github.com/adrianursu/rush-hour-game
```
```bash
cd rush-hour-game/
```
```bash
javac Main.java
```
```bash
java Main
```
## Choosing the algorithm
Running the java Main command will prompt you to choose between 5 algorithms.

| Command | Description |
|---------|-------------|
| `1`| For H-Minimax|
| `2`| For Monte-Carlo Tree Search v1 |
| `3`| For Monte-Carlo Tree Search v2 |
| `4`| For Monte-Carlo Tree Search v3 |
| `5`| For Monte-Carlo Tree Search v4|
|`6`| For playing against another human|
|`Anything Else`| If anything but 1,2,3 or 4 is entered, the program will choose Minimax Algorithm.|

## Game Controls Examples

| Command | Description |
|---------|-------------|
| `a1`    | Moves vehicle `a` 1 unit to the right/down. All vehicles are represented by lowercase letters. |
| `b-3`   | Moves vehicle `b` 3 units to the left/top. All vehicles are represented by lowercase letters. |
| `L1`    | Moves the left board 1 unit down. There are two boards that can be moved: L (left) and R (right). |
| `R-2`   | Moves the right board 2 units up. There are two boards that can be moved: L (left) and R (right). |
| `-1`    | Exits the game. |
