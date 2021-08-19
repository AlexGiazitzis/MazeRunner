package maze.api;

import java.util.*;

/**
 * @author Alex Giazitzis
 */
public class Solver {
    private static       Maze        inputMaze;
    private static       Maze        mazeClone;
    private static       Cell[][]    grid;
    private static final Queue<Cell> queue = new ArrayDeque<>();

    public static void solve(final Maze maze) {
        if (maze.equals(inputMaze)) {
            System.out.println(mazeClone);
            return;
        }
        inputMaze = maze;
        mazeClone = maze.clone();
        grid = mazeClone.getGrid();
        Cell pos = null;

        for (Cell[] row : grid) {
            if (row[0].isPath()) {
                pos = row[0];
            }
        }
        queue.add(pos);
        try {
            queue.addAll(recursive(pos));
            turnToExitPath();
            System.out.println(mazeClone);
        } catch (NullPointerException e) {
            System.out.println("Route to exit could not be found.");
            e.printStackTrace();
        }
    }

    private static Queue<Cell> recursive(Cell pos) {
        Queue<Cell> queue = new ArrayDeque<>();
        queue.add(pos);
        pos.setTraversed(true);

        Deque<Cell> stack = new ArrayDeque<>(addSurroundPaths(pos));
        int size = stack.size();
        if (size == 0 && pos.getColumn() == mazeClone.getColumns() - 1) {
            return queue;
        }

        for (int i = 0; i < size; i++) {
            if (!stack.isEmpty() && stack.peek().getColumn() == -1) {
                queue.clear();
                return queue;
            }
            Queue<Cell> q = recursive(stack.pop());
            if (q.isEmpty()) {
                continue;
            }
            queue.addAll(q);
        }
        if (queue.size() == 1 && queue.peek() == pos) {
            queue.clear();
        }
        return queue;
    }

    private static List<Cell> addSurroundPaths(final Cell pos) {
        int up    = Math.max(pos.getRow() - 1, 0);
        int down  = Math.min(pos.getRow() + 1, grid.length - 1);
        int left  = Math.max(pos.getColumn() - 1, 0);
        int right = Math.min(pos.getColumn() + 1, grid[0].length - 1);

        List<Cell> paths = new ArrayList<>();

        // TODO: 19-Aug-21 ALL IFS IN A FUNCTION
        if (grid[up][pos.getColumn()].isPath() && !grid[up][pos.getColumn()].isTraversed()) {
            paths.add(grid[up][pos.getColumn()]);
        }
        if (grid[down][pos.getColumn()].isPath() && !grid[down][pos.getColumn()].isTraversed()) {
            paths.add(grid[down][pos.getColumn()]);
        }
        if (grid[pos.getRow()][left].isPath() && !grid[pos.getRow()][left].isTraversed()) {
            paths.add(grid[pos.getRow()][left]);
        }
        if (grid[pos.getRow()][right].isPath() && !grid[pos.getRow()][right].isTraversed()) {
            paths.add(grid[pos.getRow()][right]);
        }

        if (paths.size() == 0) {
            // if dead end, add impossible cell to signify that the route needs to be discarded, workaround as null value can't be used with Deque/ArrayDeque
            paths.add(new Cell(-1,-1));
        }
        if (pos.getColumn() == grid[0].length - 1) {
            paths.clear();
        }
        return paths;
    }

    private static void turnToExitPath() {
        while (!queue.isEmpty()) {
            Cell c = queue.poll();
            c.setToExit(true);
        }
    }
}
