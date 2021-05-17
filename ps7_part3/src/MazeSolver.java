import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.Function;

public class MazeSolver implements IMazeSolver {
	private static final int TRUE_WALL = Integer.MAX_VALUE;
	private static final int EMPTY_SPACE = 0;
	private static final List<Function<Room, Integer>> WALL_FUNCTIONS = Arrays.asList(
			Room::getNorthWall,
			Room::getEastWall,
			Room::getWestWall,
			Room::getSouthWall
	);
	private static final int[][] DELTAS = new int[][] {
			{ -1, 0 }, // North
			{ 0, 1 }, // East
			{ 0, -1 }, // West
			{ 1, 0 } // South
	};

	private Maze maze;

	public MazeSolver() {
		// TODO: Initialize variables.
	}

	@Override
	public void initialize(Maze maze) {
		// TODO: Initialize the solver.
		this.maze = maze;
	}

	private PriorityQueue<Node> pq;
	private Integer[][] fearLevel;

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		// TODO: Find minimum fear level.
		if (maze == null) {
			throw new Exception("Initialise the maze first");
		}
		pq = new PriorityQueue<>();
		Node startNode = new Node(startRow, startCol);
		startNode.fear = 0;
		pq.add(startNode);
		fearLevel = new Integer[maze.getRows()][maze.getColumns()];
		for (int i = 0; i < maze.getRows(); i++) {
			for (int j = 0; j < maze.getColumns(); j++) {
				fearLevel[i][j] = Integer.MAX_VALUE;
			}
		}
		fearLevel[startRow][startCol] = 0;
		while (!pq.isEmpty()) {
			Node curr = pq.poll();
			for (int dir = 0; dir < 4; dir++) {
				if (!(curr.row + DELTAS[dir][0] < 0 ||
						curr.row + DELTAS[dir][0] >= maze.getRows() ||
						curr.col + DELTAS[dir][1] < 0 ||
						curr.col + DELTAS[dir][1] >= maze.getColumns())) {
					relax(curr, new Node(curr.row + DELTAS[dir][0],
							curr.col + DELTAS[dir][1]));
				}
			}
		}
		if (fearLevel[endRow][endCol] == Integer.MAX_VALUE) {
			return null;
		}
		return fearLevel[endRow][endCol];
	}

	private void relax(Node from, Node to) {
		int row = from.row - to.row;
		int col = from.col - to.col;
		int wall = Integer.MAX_VALUE;
		if (row > 0) {
			wall = maze.getRoom(from.row, from.col).getNorthWall();
		} else if (row < 0) {
			wall = maze.getRoom(from.row, from.col).getSouthWall();
		} else {
			if (col > 0) {
				wall = maze.getRoom(from.row, from.col).getWestWall();
			} else if (col < 0) {
				wall = maze.getRoom(from.row, from.col).getEastWall();
			}
		}
		if (wall == TRUE_WALL) {
			return;
		} else if (wall == EMPTY_SPACE) {
			wall = 1;
		}
		if (fearLevel[to.row][to.col] > fearLevel[from.row][from.col] + wall) {
			fearLevel[to.row][to.col] = fearLevel[from.row][from.col] + wall;
			to.fear = fearLevel[to.row][to.col];
			pq.add(to);
		}
	}

	private static class Node implements Comparable<Node> {
		public int row, col;
		public Integer fear;

		public Node(int row, int col) {
			this.row = row;
			this.col = col;
		}

		@Override
		public int compareTo(Node node) {
			return Integer.compare(this.fear, node.fear);
		}

//		@Override
//		public boolean equals(Object obj) {
//			if (obj instanceof Node) {
//				Node node = (Node) obj;
//				return (row == node.row && col == node.col && fear.equals(node.fear));
//			}
//			return false;
//		}
	}

	@Override
	public Integer bonusSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		// TODO: Find minimum fear level given new rules.
		if (maze == null) {
			throw new Exception("Initialise the maze first");
		}
		pq = new PriorityQueue<>();
		Node startNode = new Node(startRow, startCol);
		startNode.fear = 0;
		pq.add(startNode);
		fearLevel = new Integer[maze.getRows()][maze.getColumns()];
		for (int i = 0; i < maze.getRows(); i++) {
			for (int j = 0; j < maze.getColumns(); j++) {
				fearLevel[i][j] = Integer.MAX_VALUE;
			}
		}
		fearLevel[startRow][startCol] = 0;
		while (!pq.isEmpty()) {
			Node curr = pq.poll();
			for (int dir = 0; dir < 4; dir++) {
				if (!(curr.row + DELTAS[dir][0] < 0 ||
						curr.row + DELTAS[dir][0] >= maze.getRows() ||
						curr.col + DELTAS[dir][1] < 0 ||
						curr.col + DELTAS[dir][1] >= maze.getColumns())) {
					newRelax(curr, dir);
				}
			}
		}
		if (fearLevel[endRow][endCol] == Integer.MAX_VALUE) {
			return null;
		}
		return fearLevel[endRow][endCol];
	}

	private void newRelax(Node from, int dir) {
		int newRow = from.row + DELTAS[dir][0];
		int newCol = from.col + DELTAS[dir][1];
		int wall = WALL_FUNCTIONS.get(dir).apply(maze.getRoom(from.row, from.col));
		if (wall == TRUE_WALL) {
			return;
		}
		Node newNode = new Node(newRow, newCol);
		// if scariness level of wall greater than fearLevel,
		// update fearLevel to scariness level of wall
		if (wall > fearLevel[from.row][from.col]) {
			if (fearLevel[newRow][newCol] > wall) {
				fearLevel[newRow][newCol] = wall;
				if (wall == EMPTY_SPACE) {
					fearLevel[newRow][newCol]++;
				}
				newNode.fear = fearLevel[newRow][newCol];
				pq.add(newNode);
			}
		// else fearLevel remain unchanged
		} else {
			if (fearLevel[newRow][newCol] > fearLevel[from.row][from.col]) {
				fearLevel[newRow][newCol] = fearLevel[from.row][from.col];
				if (wall == EMPTY_SPACE) {
					fearLevel[newRow][newCol]++;
				}
				newNode.fear = fearLevel[newRow][newCol];
				pq.add(newNode);
			}
		}
	}

	int sRow;
	int sCol;
	boolean visited;

	@Override
	public Integer bonusSearch(int startRow, int startCol, int endRow, int endCol, int sRow, int sCol) throws Exception {
		// TODO: Find minimum fear level given new rules and special room.
		if (maze == null) {
			throw new Exception("Initialise the maze first");
		}
		pq = new PriorityQueue<>();
		Node startNode = new Node(startRow, startCol);
		startNode.fear = 0;
		pq.add(startNode);
		fearLevel = new Integer[maze.getRows()][maze.getColumns()];
		for (int i = 0; i < maze.getRows(); i++) {
			for (int j = 0; j < maze.getColumns(); j++) {
				fearLevel[i][j] = Integer.MAX_VALUE;
			}
		}
		fearLevel[startRow][startCol] = 0;
		// initialise new variables
		this.sRow = sRow;
		this.sCol = sCol;
		this.visited = false;
		while (!pq.isEmpty()) {
			Node curr = pq.poll();
			for (int dir = 0; dir < 4; dir++) {
				if (!(curr.row + DELTAS[dir][0] < 0 ||
						curr.row + DELTAS[dir][0] >= maze.getRows() ||
						curr.col + DELTAS[dir][1] < 0 ||
						curr.col + DELTAS[dir][1] >= maze.getColumns())) {
					newerRelax(curr, dir);
				}
			}
		}
		if (fearLevel[endRow][endCol] == Integer.MAX_VALUE) {
			return null;
		}
		return fearLevel[endRow][endCol];
	}

	private void newerRelax(Node from, int dir) {
		int newRow = from.row + DELTAS[dir][0];
		int newCol = from.col + DELTAS[dir][1];
		int wall = WALL_FUNCTIONS.get(dir).apply(maze.getRoom(from.row, from.col));
		if (wall == TRUE_WALL) {
			return;
		}
		Node newNode = new Node(newRow, newCol);
		// check if the next Room is the special room
		if (newRow == sRow && newCol == sCol && !visited) {
			visited = true; // sufficient to visit once, prevents infinite loop
			fearLevel[newRow][newCol] = -1;
			newNode.fear = fearLevel[newRow][newCol];
			pq.add(newNode);
		}
		// special consideration when in special room but next room is EMPTY_SPACE
		if (fearLevel[from.row][from.col] == -1 && wall == EMPTY_SPACE) {
			fearLevel[newRow][newCol] = 0;
			newNode.fear = fearLevel[newRow][newCol];
			pq.add(newNode);
		} else if (wall > fearLevel[from.row][from.col]) {
			if (fearLevel[newRow][newCol] > wall) {
				fearLevel[newRow][newCol] = wall;
				if (wall == EMPTY_SPACE) {
					fearLevel[newRow][newCol]++;
				}
				newNode.fear = fearLevel[newRow][newCol];
				pq.add(newNode);
			}
		} else {
			if (fearLevel[newRow][newCol] > fearLevel[from.row][from.col]) {
				fearLevel[newRow][newCol] = fearLevel[from.row][from.col];
				if (wall == EMPTY_SPACE) {
					fearLevel[newRow][newCol]++;
				}
				newNode.fear = fearLevel[newRow][newCol];
				pq.add(newNode);
			}
		}
	}

	public static void main(String[] args) {
		try {
			Maze maze = Maze.readMaze("haunted-maze-simple.txt");
			IMazeSolver solver = new MazeSolver();
			solver.initialize(maze);

			System.out.println(solver.bonusSearch(0, 1, 0, 3));
			System.out.println(solver.bonusSearch(0, 1, 0, 3, 0, 4));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
