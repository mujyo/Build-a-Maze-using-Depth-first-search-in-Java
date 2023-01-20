import java.util.ArrayList;
import java.util.List;

/**
 * driver program is used to perform back-tracking
 */
public class Driver {
    private int row;
    private int col;
    private int maxCoin;
    private List<Integer> optimalPath;
    private int pathNum = 0;

    /**
     * specify the size of the maze
     * specify whether the maze is perfect or a room maze, and whether it is wrapping or non-wrapping
     * if it is non-perfect, specify the number of walls remaining
     * specify the starting point of the player
     *  specify the goal location
     * @param maze the maze to explore
     */
    public void navigateMaze(AbstractMaze maze){
        row = maze.getRowNum();
        col = maze.getColNum();
        maxCoin = -Integer.MAX_VALUE;
        optimalPath = new ArrayList<Integer>();
        List<Integer> path = new ArrayList<Integer>();
        path.add(maze.getPlayerLoc().getRegionId());
        this.dfs(0, 0, 0, path, maze);
        System.out.println("==========End of Exploration==========");
        System.out.println("Maximum Coin: " + this.maxCoin);
        System.out.println("Optimal Path: " + this.optimalPath);
    }

    /**
     * get the maximum of coins a player can possibly get at the end of exploration
     * @return the amount of coins
     */
    public int getMaxCoin(){
        return this.maxCoin;
    }

    /**
     * the optimal path in order to get the maximum coins
     * @return path is represented by a list of cell's regionId
     */
    public List<Integer> getOptimalPath(){
        return this.optimalPath;
    }

    /**
     * the number of all possible paths that can get the player from starting point to the goal
     * @return the number of paths
     */
    public int getPathNum(){
        return this.pathNum;
    }

    /**
     * back-tracking
     * @param x row index
     * @param y column index
     * @param coin coin number so far
     * @param path path so far
     * @param maze the maze to explore
     */
    private void dfs(int x, int y, int coin, List<Integer> path, AbstractMaze maze){
        if (x == maze.getGoalLoc().getCoordinate().getX() && y == maze.getGoalLoc().getCoordinate().getY()){
            if (coin > this.maxCoin){
                this.optimalPath.clear();
                for (int p : path){
                    this.optimalPath.add(p);
                }
                pathNum += 1;
                this.maxCoin = coin;
            }
            return;
        }

        Coordinate playerLocBeforeBT = maze.getPlayerLoc().getCoordinate();
        int playerCoinBeforeBT = maze.getCoins();
        List<Direction> nextMove = maze.getPossibleMoves();
        for (Direction d: nextMove){
            int nextX = maze.normalizeRowIndex(d.c.getX() + maze.getPlayerLoc().getCoordinate().getX());
            int nextY = maze.normalizeColIndex(d.c.getY() + maze.getPlayerLoc().getCoordinate().getY());

            Cell curCell = maze.getCell(new Coordinate(nextX, nextY));
            int cellCoinBeforeBT = curCell.getGold();
            maze.move(d);
            path.add(curCell.getRegionId());
            dfs(nextX, nextY, maze.getCoins(), path, maze);
            path.remove(path.size() - 1);
            maze.cancelMove(curCell, cellCoinBeforeBT, playerCoinBeforeBT, playerLocBeforeBT);
        }
    }

    /**
     * create instances for the three type of maze
     * and then set the navigation on
     */
    private void setUp(){
        AbstractMaze maze;
        maze = new PerfectMaze(3, 3, new Coordinate(2, 2), new Coordinate(0, 0));
        //maze = new RoomMaze(3, 3, 2, new Coordinate(2, 2), new Coordinate(0, 0));
        //maze = new WrappingRoomMaze(3, 3, 2, new Coordinate(2, 2), new Coordinate(0, 0));
        //System.out.println(this.maze.toString());
        this.navigateMaze(maze);
    }

    /**
     * main function to drive the whole project
     * @param args
     */
    public static void main(String[] args){
        Driver d = new Driver();
        d.setUp();
    }
}
