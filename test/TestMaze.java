import org.junit.Before;
import org.junit.Test;

/**
 * Tests
 */
public class TestMaze {
    private AbstractMaze mazePerfect;
    private AbstractMaze mazeRoom;
    private AbstractMaze mazWrapping;
    private Driver d;

    /**
     * create instances for three types of maze
     */
    @Before
    public void setUp(){
        mazePerfect = new PerfectMaze(3, 3, new Coordinate(2, 2), new Coordinate(0, 0));
        mazeRoom = new RoomMaze(3, 3, 2, new Coordinate(2, 2), new Coordinate(0, 0));
        mazWrapping = new WrappingRoomMaze(3, 3, 2, new Coordinate(2, 2), new Coordinate(0, 0));
        d = new Driver();
    }

    /**
     * if a perfect maze is correctly created, it should meet the following criteria:
     * the number of edge should follow the formula
     * the starting point and the goal location should have the same parent id
     * there exists one and only one path from starting point to the goal location
     */
    @Test
    public void testPerfectMazeGeneration(){
        // Test for union-find: In perfect maze, starting point and goal should share the same parentId
        Cell startPoint = this.mazePerfect.getPlayerLoc();
        Cell goal = this.mazePerfect.getGoalLoc();
        assert (this.mazePerfect.find(startPoint.getRegionId()) == this.mazePerfect.find(goal.getRegionId()));

        // Test for union-find: formula for wall num
        assert (this.mazePerfect.getEdgeSet().size() == ((this.mazePerfect.getRowNum() - 1) * this.mazePerfect.getColNum() + this.mazePerfect.getRowNum() * (this.mazePerfect.getColNum() - 1)) - this.mazePerfect.getRowNum() * this.mazePerfect.getColNum() + 1);

        // Perfect maze should only have one unique path from starting point to goal
        d.navigateMaze(this.mazePerfect);
        assert (d.getPathNum() == 1);
    }

    /**
     * test whether the wall number has reach our threshold
     */
    @Test
    public void testNonPerfectGeneration(){
        // Continuously tear down walls until it reach Arg@wallNum
        assert (this.mazeRoom.getEdgeSet().size() == this.mazeRoom.getWallNum());
        assert (this.mazWrapping.getEdgeSet().size() == this.mazWrapping.getWallNum());
    }

    /**
     * invalid wall number should trigger an exception
     */
    @Test
    public void testValidWallNum(){
        // Arg@wallNum is valid
        try{
            mazeRoom = new RoomMaze(3, 3, 5, new Coordinate(2, 2), new Coordinate(0, 0));
        }catch(IllegalArgumentException err){
            System.out.println(err.toString());
        }

    }

    /**
     * invalid index should trigger an exception
     */
    @Test
    public void testValidLoc(){
        try{
            mazeRoom = new RoomMaze(3, 3, 2, new Coordinate(3, 3), new Coordinate(0, 0));
        }catch(IllegalArgumentException err){
            System.out.println(err.toString());
        }
    }

    /**
     * there should exist one or more feasible paths in case of non-perfect maze
     */
    @Test
    public void testNonPerfectMazeNum(){
        d.navigateMaze(this.mazeRoom);
        assert (d.getPathNum() >= 1);

        d.navigateMaze(this.mazWrapping);
        assert (d.getPathNum() >= 1);
    }
}
