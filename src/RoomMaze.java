/**
 * In the non-perfect maze,each cell in the grid also represent a location in the maze,
 * but there can be multiple paths between any two cells.
 */
public class RoomMaze extends AbstractMaze {
    /**
     * Leverage the constructor of AbstractMaze and then call the setEdge function to continue to tear down walls
     * until the wallNum reach our the threshold
     * @param rowNum the number of rows in the maze
     * @param colNum the number of columns in the maze
     * @param wallNum the number of walls remain in the maze
     * @param goalLocation the goal location
     * @param playerLocation the player's starting point
     */
    public RoomMaze(int rowNum, int colNum, int wallNum, Coordinate goalLocation, Coordinate playerLocation) {
        super(rowNum, colNum, wallNum, goalLocation, playerLocation);
        this.setEdge();
    }

    /**
     * First, leverage the super's generateMaze() to get a perfect maze
     * and then call its generateMaze() to continue to tear down walls
     * until the number of walls meets with criterion
     */
    public void setEdge(){
        this.generateMaze();
    }

    /**
     * continue to remove walls until the number of walls meets with criterion
     */
    @Override
    public void generateMaze(){
        super.generateMaze();
        int count = 0;
        int removeWallNum = this.getEdgeSet().size() - this.getWallNum();
        while (!this.getEdgeSet().isEmpty()){
           this.getEdgeSet().remove(this.getEdgeSet().iterator().next());
           count++;
           if (count == removeWallNum){
               break;
           }
        }
    }
    // see the comment in PerfectMaze
}
