/**
 * Represents a single room in the maze.
 */
public class Room {
	private final boolean westWall, eastWall, northWall, southWall;
	public boolean onPath;
	// included parent pointer
	public Room parent;
	// included Room row and col
	public int row, col;

	Room(boolean north, boolean south, boolean east, boolean west) {
		northWall = north;
		southWall = south;
		eastWall = east;
		westWall = west;

		onPath = false;
		parent = null;
	}

	/**
	 * @return true iff there is a wall to the west of the room
	 */
	public boolean hasWestWall() {
		return westWall;
	}

	/**
	 * @return true iff there is a wall to the east of the room
	 */
	public boolean hasEastWall() {
		return eastWall;
	}

	/**
	 * @return true iff there is a wall to the north of the room
	 */
	public boolean hasNorthWall() {
		return northWall;
	}

	/**
	 * @return true iff there is a wall to the south of the room
	 */
	public boolean hasSouthWall() {
		return southWall;
	}
}
