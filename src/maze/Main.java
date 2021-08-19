package maze;

import maze.api.Cell;
import maze.api.Maze;
import maze.api.Menu;
import maze.api.Solver;
import maze.backend.FileSystem;

import java.util.Scanner;

/**
 * @author Alex Giazitzis
 */
public class Main {
	public static Maze    maze;
	public static Scanner scanner = new Scanner(System.in);
	public static boolean exit    = false;

	public static void main(String[] args) {
		Cell.setNotPathSymbol("\u2588\u2588");
		Cell.setToExitSymbol("//");

		menu();
		Menu.show();
		Menu.clear();

		menu();
		while (!exit) {
			Menu.show();
		}

	}

	public static void menu() {
		Menu.addMenuOption("1. Generate a new maze", Main::newMaze);
		Menu.addMenuOption("2. Load a maze", () -> maze = FileSystem.loadFromFile());
		if (Menu.hasRunOnce()) {
			Menu.addMenuOption("3. Save the maze", () -> FileSystem.saveToFile(maze));
			Menu.addMenuOption("4. Display the maze", () -> System.out.println(maze));
			Menu.addMenuOption("5. Find the escape", () -> Solver.solve(maze));
		}
		Menu.addMenuOption("0. Exit", () -> exit = Menu.exit());
	}

	public static void newMaze() {
		System.out.println("Enter the size of a new maze");
		int dimension = scanner.nextInt();
		maze = new Maze(dimension);
		System.out.println(maze);
	}
}
