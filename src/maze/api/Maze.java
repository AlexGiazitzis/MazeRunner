package maze.api;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author Alex Giazitzis
 */
public class Maze implements Cloneable {
	private              int        rows;
	private              int        columns;
	private              Cell[][]   grid;
	private static final Random     gen = new Random();
	private              List<Cell> candidates;

	public Maze(Path file) {
		List<String> lines;
		try {
			lines = Files.readAllLines(file);
		} catch (IOException e) {
			System.out.println("The file ... does not exist");
			return;
		}
		rows = lines.size();
		columns = lines.get(0).length() / 2; // since each cell is represented by two characters
		candidates = new ArrayList<>(); // needed done here else NPE when cloning
		initGrid(lines);

	}

	public Maze(int dimension) {
		this(dimension, dimension);
	}

	public Maze(int dimX, int dimY) {
		rows = dimX;
		columns = dimY;
		candidates = new ArrayList<>();
		initGrid();
		generate();
	}

	private void initGrid() {
		grid = new Cell[rows][columns];
		for (int x = 0; x < rows; x++) {
			for (int y = 0; y < columns; y++) {
				grid[x][y] = new Cell(x, y);
			}
		}
	}

	private void initGrid(List<String> fileLines) {
		grid = new Cell[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0, index = 0; j < columns; j++, index += 2) {
				String cell = fileLines.get(i).substring(index, index + 2);
				if ("  ".equals(cell)) {
					grid[i][j] = new Cell(i, j);
					grid[i][j].setPath(true);
				} else if (Cell.getNotPathSymbol().equals(cell)) {
					grid[i][j] = new Cell(i, j);
				} else {
					System.out.println("Cannot load the maze. It has an invalid format");
					return;
				}
			}
		}

	}

	private void generate() {
		int start = pickEntrance(false);
		generateMaze(start);
		pickEntrance(true);
	}

	// entryPoint = false : left side entrance, = true : right side entrance
	private int pickEntrance(boolean entryPoint) {
		if (entryPoint) {
			List<Cell> candidates = getExitCandidates();
			int        choice     = gen.nextInt(candidates.size());
			int        x          = candidates.get(choice).getRow();
			int        y          = candidates.get(choice).getColumn();
			grid[x][y + 1].setPath(true);
			return -2; // arbitrarily picked value, gets ignored anyway.
		}
		int coordinate = -1;
		coordinate = gen.nextInt(columns - 2) + 1;
		grid[coordinate][0].setPath(true);
		return coordinate;

	}

	// exit candidates picked based on which cells on the one before last are actual paths
	private List<Cell> getExitCandidates() {
		List<Cell> candidates = new ArrayList<>();
		for (int x = 1; x < rows - 1; x++) {
			if (grid[x][columns - 2].isPath()) {
				candidates.add(grid[x][columns - 2]);
			}
		}
		return candidates;

	}

	private void generateMaze(int start) {
		grid[start][1].setPath(true);
		candidates.addAll(getCandidates(start, 1));

		while (candidates.size() > 0) {
			int choice = gen.nextInt(candidates.size());
			int x      = candidates.get(choice).getRow();
			int y      = candidates.get(choice).getColumn();
			candidates.get(choice).setPath(true);
			candidates.remove(choice);
			candidates.addAll(getCandidates(x, y));
			candidates.removeIf(Cell::isPath);
			checkAndFixCandidacyValidity();
		}
	}

	private List<Cell> getCandidates(int x, int y) {
		int        left       = Math.max(y - 1, 1);
		int        right      = Math.min(y + 1, columns - 2);
		int        above      = Math.max(x - 1, 1);
		int        below      = Math.min(x + 1, rows - 2);
		List<Cell> candidates = new ArrayList<>();

		if (left != y && !grid[x][left - 1].isPath() && !grid[x - 1][left].isPath() &&
		    !grid[x + 1][left].isPath()) {
			candidates.add(grid[x][left]);
		}
		if (right != y && !grid[x][right + 1].isPath() && !grid[x - 1][right].isPath() &&
		    !grid[x + 1][right].isPath()) {
			candidates.add(grid[x][right]);
		}
		if (above != x && !grid[above - 1][y].isPath() && !grid[above][y - 1].isPath() &&
		    !grid[above][y + 1].isPath()) {
			candidates.add(grid[above][y]);
		}
		if (below != x && !grid[below + 1][y].isPath() && !grid[below][y - 1].isPath() &&
		    !grid[below][y + 1].isPath()) {
			candidates.add(grid[below][y]);
		}

		candidates = candidates.stream().distinct().collect(Collectors.toList());
		return candidates;
	}

	private void checkAndFixCandidacyValidity() {
		int pos = 0;
		for (int i = 0; i < candidates.size(); i++) {
			Cell c           = candidates.get(pos);
			int  left        = Math.max(c.getColumn() - 1, 1);
			int  right       = Math.min(c.getColumn() + 1, columns - 2);
			int  above       = Math.max(c.getRow() - 1, 1);
			int  below       = Math.min(c.getRow() + 1, rows - 2);
			int  pathsAround = 0;

			if (left != c.getColumn() && grid[c.getRow()][left].isPath()) {
				pathsAround++;
			}
			if (right != c.getColumn() && grid[c.getRow()][right].isPath()) {
				pathsAround++;
			}
			if (above != c.getRow() && grid[above][c.getColumn()].isPath()) {
				pathsAround++;
			}
			if (below != c.getRow() && grid[below][c.getColumn()].isPath()) {
				pathsAround++;
			}

			if (pathsAround > 1) {
				candidates.remove(pos);
				continue;
			}
			pos++;
		}
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}

	public Cell[][] getGrid() {
		return grid;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (int x = 0; x < rows; x++) {
			for (int y = 0; y < columns; y++) {
				str.append(grid[x][y]);
			}
			if (x == rows - 1) {
				continue;
			}
			str.append("\n");
		}
		return str.toString();
	}


	@Override
	public Maze clone() {
		try {
			Maze clone = (Maze) super.clone();
			clone.grid = this.grid.clone();
			clone.candidates.clear();
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError();
		}
	}
}
