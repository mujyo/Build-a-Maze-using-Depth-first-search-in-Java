public class PerfectMaze extends AbstractMaze {
    /**
     * A perfect maze is defined as a maze which has one and only one path from any point i
     * in the maze to any other point in the maze.
     * This means that the maze has no inaccessible sections, no circular paths, no open area
     * @param rowNum the number of rows in the maze
     * @param colNum the number of columns in the maze
     * @param goalLocation the goal location
     * @param playerLocation the player's starting point
     */
    public PerfectMaze(int rowNum, int colNum, Coordinate goalLocation, Coordinate playerLocation) {
        // the wall number of a perfect maze should not be passed into the constructor
        // the valid wall number should follow the math below
        super(rowNum, colNum,((rowNum - 1) * colNum + rowNum * (colNum - 1)) - rowNum * colNum + 1, goalLocation, playerLocation);
        this.generateMaze();
    }
    // seems not much to be put here, maybe just a constructor? It's up to you.
  // But it's good to have this hierarchical structure of different mazes for
}
