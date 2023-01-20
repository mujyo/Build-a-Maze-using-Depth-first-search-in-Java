import java.util.ArrayList;
import java.util.List;

/**
 * Since locations at the top, bottom, left and right can "wrap" to the other side,
 * we call this a wrapping room maze
 */
public class WrappingRoomMaze extends RoomMaze {

    /**
     * First, add gateways into the maze before
     * leveraging RoomMaze's constructor to get a room maze
     * @param rowNum the number of rows in the maze
     * @param colNum the number of columns in the maze
     * @param wallNum the number of walls remain in the maze
     * @param goalLocation the goal location
     * @param playerLocation the player's starting point
     */
    public WrappingRoomMaze(int rowNum, int colNum, int wallNum, Coordinate goalLocation, Coordinate playerLocation) {
        super(rowNum, colNum, wallNum, goalLocation, playerLocation);
        this.setEdge();
    }

    /**
     * First, add gateways into the maze
     * and then leverage RoomMaze's constructor to get a room maze
     */
    @Override
    public void setEdge(){
        this.addGateway();
        this.generateMaze();
    }

    /**
     * add gateways for cells in the boundary
     */
    private void addGateway(){
        for (int c = 0; c < this.getColNum(); c++) {
            int lastR = this.getRowNum() - 1;
            Cell firstCell = this.getCell(new Coordinate(lastR, c));
            Cell secondCell = this.getCell(new Coordinate(0, c));
            this.addEdge(new Edge(firstCell, secondCell));
        }

        for (int r = 0; r < this.getRowNum(); r++){
            int lastC = this.getColNum() - 1;
            Cell firstCell = this.getCell(new Coordinate(r, lastC));
            Cell secondCell = this.getCell(new Coordinate(r, 0));
            this.addEdge(new Edge(firstCell, secondCell));
        }
    }

    /**
     * transform the "out-of-boundary" index into the other side
     * @param dx the row index
     * @return normalized index
     */
    @Override
    public int normalizeRowIndex(int dx){
        if (dx == -1){
            dx = this.getRowNum() - 1;
        }
        if (dx == this.getRowNum()){
            dx = 0;
        }
        return dx;
    }

    /**
     * transform the "out-of-boundary" index into the other side
     * @param dy the column index
     * @return normalized index
     */
    @Override
    public int normalizeColIndex(int dy){
        if (dy == -1){
            dy = this.getColNum() - 1;
        }
        if (dy == this.getColNum()){
            dy = 0;
        }
        return dy;
    }

    /**
     * the next valid moves for a wrapping maze should be:
     * from all directions
     * haven't been visited yet
     * no wall between the current location and the next location
     * @return a list of directions that are valid for making the next move
     */
    @Override
    public List<Direction> getPossibleMoves() {
        List<Direction> validDirections = new ArrayList<Direction>();
        Coordinate currentLocation = this.getPlayerLoc().getCoordinate();
        for (Direction d: Direction.values()) {
            int cx = currentLocation.getX();
            int cy = currentLocation.getY();
            int dx = this.normalizeRowIndex(cx + d.c.getX());
            int dy = this.normalizeColIndex(cy + d.c.getY());

            Edge tmpEdge1 = new Edge(this.getCell(new Coordinate(cx, cy)), this.getCell(new Coordinate(dx, dy)));
            Edge tmpEdge2 = new Edge(this.getCell(new Coordinate(dx, dy)), this.getCell(new Coordinate(cx, cy)));
            if (!this.isWall(tmpEdge1) && !this.isWall(tmpEdge2) && !this.getCell(new Coordinate(dx, dy)).getIsVisited()){
                validDirections.add(d);
            }
        }
        return validDirections;
    }
}
