package maze.api;

import java.util.Objects;

/**
 * @author Alex Giazitzis
 */
public class Cell implements Cloneable {
	private        int     row;
	private        int     column;
	private        boolean path;
	private static String  notPathSymbol;
	private        boolean toExit;
	private static String  toExitSymbol;
	private        boolean traversed;

	Cell(int row, int column) {
		this.row = row;
		this.column = column;
		path = false;
		toExit = false;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public boolean isPath() {
		return path;
	}

	public void setPath(final boolean path) {
		this.path = path;
	}

	public void setToExit(final boolean toExit) {
		this.toExit = toExit;
	}

	public static String getNotPathSymbol() {
		return notPathSymbol;
	}

	public static String getToExitSymbol() {
		return toExitSymbol;
	}

	public static void setNotPathSymbol(final String notPathSymbol) {
		Cell.notPathSymbol = notPathSymbol;
	}

	public static void setToExitSymbol(final String toExitSymbol) {
		Cell.toExitSymbol = toExitSymbol;
	}

	public boolean isTraversed() {
		return traversed;
	}

	public void setTraversed(final boolean traversed) {
		this.traversed = traversed;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Cell cell = (Cell) o;
		return row == cell.row && column == cell.column && path == cell.path;
	}

	@Override
	public int hashCode() {
		return Objects.hash(row, column, path);
	}

	@Override
	public String toString() {
		return path
		       ? (toExit
		          ? toExitSymbol
		          : "  ")
		       : notPathSymbol;
	}

	@Override
	public Cell clone() {
		try {
			return (Cell) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new AssertionError();
		}
	}
}