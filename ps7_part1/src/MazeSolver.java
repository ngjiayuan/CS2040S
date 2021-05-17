import java.util.ArrayList;

public class MazeSolver implements IMazeSolver {
	private static final int NORTH = 0, SOUTH = 1, EAST = 2, WEST = 3;
	private static final int[][] DELTAS = new int[][] {
		{ -1, 0 }, // North
		{ 1, 0 }, // South
		{ 0, 1 }, // East
		{ 0, -1 } // West
	};

	private Maze maze;
	private boolean[][] visited;
	private int endRow, endCol;
	// for pathSearch
	private Integer pathLength;
	// store the number of rooms that can be reached for
	// each step corresponding to the index.
	// an ArrayList is used so that an arbitrary sized array can be initialised.
	private ArrayList<Integer> distList;

	public MazeSolver() {
		// TODO: Initialize variables.
		this.maze = null;
		this.visited = null;
		this.distList = null;
	}

	@Override
	public void initialize(Maze maze) {
		// TODO: Initialize the solver.
		this.maze = maze;
		this.visited = new boolean[maze.getRows()][maze.getColumns()];
		// initialise row and col fields in Room
		for (int i = 0; i < maze.getRows(); i++) {
			for (int j = 0; j < maze.getColumns(); j++) {
				maze.getRoom(i, j).row = i;
				maze.getRoom(i, j).col = j;
			}
		}
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		// TODO: Find shortest path.
		initialize(maze);
		this.endRow = endRow;
		this.endCol = endCol;

		if (maze == null) {
			throw new Exception("Initialise the maze first");
		}
		if (startRow < 0 || startCol < 0 || startRow >= maze.getRows() || startCol >= maze.getColumns() ||
				endRow < 0 || endCol < 0 || endRow >= maze.getRows() || endCol >= maze.getColumns()) {
			throw new IllegalArgumentException("Invalid start/end coordinate");
		}

		// reset all visited to false for a new search
		// reset all rooms onPath to false for a new search
		for (int i = 0; i < maze.getRows(); i++) {
			for (int j = 0; j < maze.getColumns(); j++) {
				this.visited[i][j] = false;
				maze.getRoom(i, j).onPath = false;
			}
		}

		bfs(startRow, startCol);
		return pathLength;
	}

	// taken from MazeSolverNaive
	private boolean canGo(int row, int col, int dir) {
		switch (dir) {
			case NORTH:
				return !maze.getRoom(row, col).hasNorthWall();
			case SOUTH:
				return !maze.getRoom(row, col).hasSouthWall();
			case EAST:
				return !maze.getRoom(row, col).hasEastWall();
			case WEST:
				return !maze.getRoom(row, col).hasWestWall();
		}

		return false;
	}

	private void bfs(int startRow, int startCol) {
		// reset pathLength to 0 everytime bfs is called
		pathLength = 0;
		Room startRoom = maze.getRoom(startRow, startCol);
		Room endRoom = maze.getRoom(this.endRow, this.endCol);
		ArrayList<Room> frontier = new ArrayList<>();
		boolean hasSolution = false;
		// reset distList each time a new bfs is called (different start point)
		this.distList = new ArrayList<>();

		this.visited[startRow][startCol] = true;
		startRoom.onPath = true;
		frontier.add(startRoom);
		// step 0 is 1
		distList.add(1);

		while (! frontier.isEmpty()) {
			ArrayList<Room> newFrontier = new ArrayList<>();
			for (Room currRoom : frontier) {
				if (currRoom.equals(endRoom)) {
					hasSolution = true;
				}
				for (int dir = 0; dir < 4; dir++) {
					if (canGo(currRoom.row, currRoom.col, dir)) {
						Room nextRoom = maze.getRoom(currRoom.row + DELTAS[dir][0],
								currRoom.col + DELTAS[dir][1]);
						if (!visited[nextRoom.row][nextRoom.col]) {
							this.visited[nextRoom.row][nextRoom.col] = true;
							nextRoom.parent = currRoom;
							newFrontier.add(nextRoom);
						}
					}
				}
			}
			// add num of rooms reachable in each num of steps
			this.distList.add(newFrontier.size());
			frontier = newFrontier;
		}

		// if there is a solution retrace from endRoom using parent pointer
		if (hasSolution) {
			Room currRoom = endRoom;
			while (!currRoom.onPath) { // while not startRoom
				currRoom.onPath = true;
				currRoom = currRoom.parent;
				pathLength++;
			}
			currRoom.onPath = true;
		} else {
			pathLength = null;
		}
	}

	@Override
	public Integer numReachable(int k) throws Exception {
		// TODO: Find number of reachable rooms.
		if (k < 0) {
			throw new IllegalArgumentException("k cannot be negative");
		}
		if (k >= distList.size()) {
			return 0;
		} else {
			return distList.get(k);
		}
	}

	public static void main(String[] args) {
		try {
			Maze maze = Maze.readMaze("maze-empty.txt");
			IMazeSolver solver = new MazeSolver();
			solver.initialize(maze);

			System.out.println(solver.pathSearch(0, 0, 0, 3));
			MazePrinter.printMaze(maze);

			for (int i = 0; i <= 9; ++i) {
				System.out.println("Steps " + i + " Rooms: " + solver.numReachable(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
