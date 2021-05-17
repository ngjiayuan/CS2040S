import java.util.ArrayList;
// used a special testcase provided by another student Zhang Shichen to test the solution
// Discussed with other student Lee Jia Yi on solution

public class MazeSolverWithPower implements IMazeSolverWithPower {
	private static final int NORTH = 0, SOUTH = 1, EAST = 2, WEST = 3;
	private static int[][] DELTAS = new int[][] {
		{ -1, 0 }, // North
		{ 1, 0 }, // South
		{ 0, 1 }, // East
		{ 0, -1 } // West
	};

	private Maze maze;
	private boolean[][] visited;
	private int endRow, endCol;
	private Integer pathLength;
	private ArrayList<Integer> distList;


	public MazeSolverWithPower() {
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
			for (Room currNode : frontier) {
				if (currNode.equals(endRoom)) {
					hasSolution = true;
				}
				for (int dir = 0; dir < 4; dir++) {
					if (canGo(currNode.row, currNode.col, dir)) {
						Room nextRoom = maze.getRoom(currNode.row + DELTAS[dir][0],
								currNode.col + DELTAS[dir][1]);
						if (!visited[nextRoom.row][nextRoom.col]) {
							this.visited[nextRoom.row][nextRoom.col] = true;
							nextRoom.parent = currNode;
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
			Room currNode = endRoom;
			while (!currNode.onPath) { // while not startRoom
				currNode.onPath = true;
				currNode = currNode.parent;
				pathLength++;
			}
			currNode.onPath = true;
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

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow,
							  int endCol, int superpowers) throws Exception {
		// TODO: Find shortest path with powers allowed.
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

		superBFS(startRow, startCol, superpowers);
		return pathLength;
	}

	// WORKING SOLUTION
	// IDEA IS TO TREAT EACH NODE THAT IS REACHABLE WITH A DIFFERENT SUPERPOWER AS A DIFFERENT NODE
	// USED ANOTHER NODE CLASS INSTEAD OF ROOM CLASS TO PREVENT SAME PARENTS
	// NUMREACHABLE MUST ACCOUNT FOR DUPLICATE COORDINATES NODE (NO DOUBLE COUNT)
	public void superBFS(int startRow, int startCol, int superpowers) {
		// reset pathLength to 0 everytime bfs is called
		pathLength = 0;
		Node startNode = new Node(startRow, startCol, superpowers);
		Room startRoom = maze.getRoom(startRow, startCol);
		Node endNode = null;
		ArrayList<Node> frontier = new ArrayList<>();
		boolean hasSolution = false;
		// reset distList each time a new bfs is called (different start point)
		this.distList = new ArrayList<>();

		startRoom.onPath = true;
		frontier.add(startNode);
		this.visited[startRow][startCol] = true;
		// step 0 is 1
		distList.add(1);

		boolean[][][] powerVisited = new boolean[maze.getRows()][maze.getColumns()][superpowers + 1];
		powerVisited[startRow][startCol][superpowers] = true;

		while (! frontier.isEmpty()) {
			ArrayList<Node> nextFrontier = new ArrayList<>();
			// count the number of duplicates
			int counter = 0;
			for (Node currNode : frontier) {
				if (currNode.row == this.endRow && currNode.col == this.endCol && endNode == null) {
					endNode = currNode;
					hasSolution = true;
				}

				for (int dir = 0; dir < 4; dir++) {
					if (!(currNode.row + DELTAS[dir][0] < 0 ||
							currNode.row + DELTAS[dir][0] >= maze.getRows() ||
							currNode.col + DELTAS[dir][1] < 0 ||
							currNode.col + DELTAS[dir][1] >= maze.getColumns())) {
						int nextRow = currNode.row + DELTAS[dir][0];
						int nextCol = currNode.col + DELTAS[dir][1];

						if (canGo(currNode.row, currNode.col, dir)) {
							if (!powerVisited[nextRow][nextCol][currNode.superpowers]) {
								Node nextNode = new Node(nextRow, nextCol, currNode.superpowers);
								powerVisited[nextRow][nextCol][nextNode.superpowers] = true;
								nextNode.parent = currNode;
								nextFrontier.add(nextNode);
								if (!this.visited[nextRow][nextCol]) { // if coordinates visited for first time
									this.visited[nextRow][nextCol] = true;
								} else { // if coordinates visited again, count the number of duplicates
									counter++;
								}
							}
						} else if (currNode.superpowers > 0) {
							if (!powerVisited[nextRow][nextCol][currNode.superpowers - 1]) {
								Node nextNode = new Node(nextRow, nextCol, currNode.superpowers - 1);
								powerVisited[nextRow][nextCol][nextNode.superpowers] = true;
								nextNode.parent = currNode;
								nextFrontier.add(nextNode);
								if (!this.visited[nextRow][nextCol]) { // if coordinates visited for first time
									this.visited[nextRow][nextCol] = true;
								} else { // if coordinates visited again, count the number of duplicates
									counter++;
								}
							}
						}
					}
				}
			}
			this.distList.add(nextFrontier.size() - counter);
			frontier = nextFrontier;
		}

		// trace the path using node
		// convert node coordinates to room coordinates
		if (hasSolution) {
			Node currNode = endNode;
			Room currRoom = maze.getRoom(currNode.row, currNode.col);
			while (!currRoom.onPath) { // while not startRoom
				currRoom.onPath = true;
				currNode = currNode.parent;
				currRoom = maze.getRoom(currNode.row, currNode.col);
				pathLength++;
			}
		} else {
			pathLength = null;
		}
	}

	private static class Node {
		public int row, col;
		public int superpowers;
		public Node parent;

		public Node(int row, int col, int superpowers) {
			this.row = row;
			this.col = col;
			this.superpowers = superpowers;
		}
	}

	public static void main(String[] args) {
		try {
			Maze maze = Maze.readMaze("maze-shichen.txt");
			IMazeSolverWithPower solver = new MazeSolverWithPower();
			solver.initialize(maze);

			System.out.println(solver.pathSearch(0, 0, 4, 1, 2));
			MazePrinter.printMaze(maze);

			for (int i = 0; i <= 9; ++i) {
				System.out.println("Steps " + i + " Rooms: " + solver.numReachable(i));
			}

			Maze maze2 = Maze.readMaze("maze-sample.txt");
			IMazeSolverWithPower solver2 = new MazeSolverWithPower();
			solver.initialize(maze2);

			System.out.println(solver.pathSearch(0, 0, 3, 3, 2));
			MazePrinter.printMaze(maze2);

			for (int i = 0; i <= 9; ++i) {
				System.out.println("Steps " + i + " Rooms: " + solver.numReachable(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// NAIVE SOLUTION FAILS PRIVATE TESTCASE
//	public void superBFS(int startRow, int startCol, int superpowers) {
//		// reset pathLength to 0 everytime bfs is called
//		pathLength = 0;
//		Room startRoom = maze.getRoom(startRow, startCol);
//		Room endRoom = maze.getRoom(this.endRow, this.endCol);
//		ArrayList<Room> frontier = new ArrayList<>();
//		Room end = null;
//		boolean hasSolution = false;
//		// reset distList each time a new bfs is called (different start point)
//		this.distList = new ArrayList<>();
//
//		visited[startRow][startCol] = true;
//		startRoom.onPath = true;
//		// initialise superpower
//		startRoom.superpowers = superpowers;
//		frontier.add(startRoom);
//		// step 0 is 1
//		distList.add(1);
//
//		while (! frontier.isEmpty()) {
//			ArrayList<Room> newFrontier = new ArrayList<>();
//			for (Room currNode : frontier) {
//				if (currNode.equals(endRoom) && end == null) {
//					end = currNode;
//					hasSolution = true;
//				}
////				System.out.println(currNode + " Superpowers left:" + currNode.superpowers);
//				for (int dir = 0; dir < 4; dir++) {
//					// if cango dont use superpower
//					if (! (currNode.row + DELTAS[dir][0] < 0 ||
//							currNode.row + DELTAS[dir][0] >= maze.getRows() ||
//							currNode.col + DELTAS[dir][1] < 0 ||
//							currNode.col + DELTAS[dir][1] >= maze.getColumns())) {
//						Room nextRoom = maze.getRoom(currNode.row + DELTAS[dir][0],
//								currNode.col + DELTAS[dir][1]);
//						if (! visited[nextRoom.row][nextRoom.col]) {
//							if (canGo(currNode.row, currNode.col, dir) || currNode.superpowers > 0) {
//								if (canGo(currNode.row, currNode.col, dir)) {
//									nextRoom.superpowers = currNode.superpowers;
//								} else {
//									nextRoom.superpowers = currNode.superpowers - 1;
//								}
//								nextRoom.parent = currNode;
//								visited[nextRoom.row][nextRoom.col] = true;
//								newFrontier.add(nextRoom);
//							}
//						}
//					}
//				}
//			}
//			this.distList.add(newFrontier.size());
//			frontier = newFrontier;
//		}
//		if (hasSolution) {
//			Room currNode = end;
//			while (!currNode.onPath) { // while not startRoom
//				currNode.onPath = true;
//				currNode = currNode.parent;
//				pathLength++;
//			}
//		} else {
//			pathLength = null;
//		}
//	}
}
