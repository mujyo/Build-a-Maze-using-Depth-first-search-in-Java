import java.util.*;
import java.util.List;

/**
 * create a maze by generating cells, tear down walls and set up parent for each cell
 */
public abstract class AbstractMaze implements Maze {
  public static final double GOLD_PROBABILITY = 0.2f;
  public static final double THIEF_PROBABILITY = 0.1f;
  static final double COIN_LOSS_RATIO = 0.1f;

  private Coordinate playerLocation;
  private double coin;

  private final int rowNum;
  private final int colNum;
  private final int wallNum;
  private Coordinate goalLocation;

  // number of locations in the maze as n = numberOfRows x numberOfColumns
  private int cellNum;

  private Cell[][] cell;
  private HashSet<Edge> edgeSet = new HashSet<>();
  private int[] parent;

    /**
     * initialize a maze
     * @param rowNum number of rows
     * @param colNum number of columns
     * @param wallNum number of remaining walls
     * @param goalLocation the goal's coordinate in the maze
     * @param playerLocation the starting point of the player
     */
  public AbstractMaze(int rowNum, int colNum, int wallNum, Coordinate goalLocation, Coordinate playerLocation){
    if (rowNum <= 0){
        throw new IllegalArgumentException("Invalid row number");
    }else{
        this.rowNum = rowNum;
    }

    if (colNum <= 0){
        throw new IllegalArgumentException("Invalid column number");
    }else{
        this.colNum = colNum;
    }

    if (wallNum < 0 || wallNum > ((rowNum - 1) * colNum + rowNum * (colNum - 1)) - rowNum * colNum + 1){
      throw new IllegalArgumentException("Invalid wall number");
    }else{
      this.wallNum = wallNum;
    }

    if (goalLocation.getX() < rowNum && goalLocation.getY() < colNum){
        this.goalLocation = goalLocation;
    }else{
        throw new IllegalArgumentException("Invalid goal");
    }

    this.cell = new Cell[rowNum][colNum];
    this.cellNum = this.rowNum * this.colNum;
    this.parent = new int[this.cellNum];

    // 20% of locations (at random) have gold coins in them that the player can pick up
    List<Integer> rewardCellIds =
            this.generateNonDupRandom((int) Math.round(this.cellNum * this.GOLD_PROBABILITY),
                    0, this.cellNum - 1, new ArrayList<Integer>());

    // 10% of locations (at random) have a thief that takes some of the player's gold coins
    List<Integer> penaltyCellIds =
            this.generateNonDupRandom((int) Math.round(this.cellNum * this.THIEF_PROBABILITY),
                    0, this.cellNum - 1, rewardCellIds);

    this.generateCells(rewardCellIds, penaltyCellIds);

    if (playerLocation.getX() < rowNum && playerLocation.getY() < colNum){
        this.setPlayer(this.cell[playerLocation.getX()][playerLocation.getY()]);
    }else{
        throw new IllegalArgumentException("Invalid starting point");
    }
  }

    /**
     * generate cells in the maze
     * by distributing coins and thieves,
     * setting up parent list
     * and recording initial edges
     * @param rewardCellIds random integers for distributing coins within cells in the maze
     * @param penaltyCellIds random integerd for distributing thieves within cells in the maze
     */
  @Override
  public void generateCells(List<Integer> rewardCellIds, List<Integer> penaltyCellIds){
    int setId = 0;
    for (int r_id = 0; r_id < this.rowNum; r_id++){
      for (int c_id = 0; c_id < this.colNum; c_id++){
        boolean hasGold = false;
        boolean hasThief = false;
        if (rewardCellIds.contains(setId)){
          hasGold = true;
        }
        if (penaltyCellIds.contains(setId)){
          hasThief = true;
        }
        // create a new cell
        Cell newCell = new Cell(setId, new Coordinate(r_id, c_id), hasGold, hasThief, COIN_LOSS_RATIO);
        this.cell[r_id][c_id] = newCell;

        // give the new cell a regionId
        this.parent[setId] = setId;

        // add right edge and bottom edge
        if (r_id > 0){
          Cell firstCell = this.cell[r_id - 1][c_id];
          Cell secondCell = this.cell[r_id][c_id];
          this.edgeSet.add(new Edge(firstCell, secondCell));
        }

        // add bottom edge
        if (c_id > 0){
          Cell firstCell = this.cell[r_id][c_id - 1];
          Cell secondCell = this.cell[r_id][c_id];
          this.edgeSet.add(new Edge(firstCell, secondCell));
        }
        setId += 1;
      }
    }
  }

    /**
     * tearing down walls by performing union-find algorithm
     * the result is a perfect maze with edge number specified by the formula
     * the starting point and the goal should share the same parent id in the end of union-find
     */
  @Override
  public void generateMaze(){
    HashSet<Edge> remainEdgeSet = this.getEdgeSet();
    HashSet<Edge> delEdgeSet = new HashSet<>();
    for (Edge e: this.edgeSet) {
        int sourceRegionId = e.getSource().getRegionId();
        int destinRegionId = e.getDestination().getRegionId();
        int parentSource = this.find(sourceRegionId);
        int parentDestin = this.find(destinRegionId);
        if (parentSource != parentDestin) {
            this.union(sourceRegionId, destinRegionId);
            delEdgeSet.add(e);
        }
    }
    remainEdgeSet.removeAll(delEdgeSet);
    this.setEdgeSet(remainEdgeSet);
  }

    /**
     * get valid next move based on the player's current location
     * check if the cell is visited
     * check if the next index is out of boundary
     * check if there is a wall between the current location and each future location
     * @return a list of Directions denotes the valid directions for the next move
     */
  @Override
  public List<Direction> getPossibleMoves() {
    List<Direction> validDirections = new ArrayList<Direction>();
    Coordinate currentLocation = this.getPlayerLoc().getCoordinate();
    for (Direction d: Direction.values()) {
      int cx = currentLocation.getX();
      int cy = currentLocation.getY();
      int dx = cx + d.c.getX();
      int dy = cy + d.c.getY();
      if (dx >=0 && dx < this.rowNum && dy >= 0 && dy < this.colNum){
        Edge tmpEdge1 = new Edge(this.getCell(new Coordinate(cx, cy)), this.getCell(new Coordinate(dx, dy)));
        Edge tmpEdge2 = new Edge(this.getCell(new Coordinate(dx, dy)), this.getCell(new Coordinate(cx, cy)));
        if (!this.isWall(tmpEdge1) && !this.isWall(tmpEdge2) && !this.cell[dx][dy].getIsVisited()){
          validDirections.add(d);
        }
      }
    }
    return validDirections;
  }

    /**
     * tell if there is a solid edge between two cells
     * @param e an edge connects the current location and a future location
     * @return true if there exist a wall
     */
  public boolean isWall(Edge e){
    for (Edge remainE: this.edgeSet){
      if (e.toString().equals(remainE.toString())){
        return true;
      }
    }
    return false;
  }

    /**
     * move to a cell from the current location
     * after moving to a cell, we must:
     * lose coins if there is a thief in the cell
     * gain coin if there is a coin in the cell
     * remove the coin from the cell after gaining coin
     * set the cell as visited
     * @param dx row index for the cell
     * @param dy column index for the cell
     */
  private void moveToCell(int dx, int dy){
    this.playerLocation = new Coordinate(dx, dy);

    // calculate coin
    if (this.cell[dx][dy].getHasThief()){
      this.loseCoins();
    }
    this.pickupCoins(this.cell[dx][dy].getGold());

    // remove gold
    this.cell[dx][dy].removeGold();

    // set visit
    this.cell[dx][dy].setIsVisited(true);
  }

    /**
     * move toward a given direction
     * @param direction the direction to move.
     */
  @Override
  public void move(Direction direction) {
    int curX = this.playerLocation.getX();
    int curY = this.playerLocation.getY();
    int dx = this.normalizeRowIndex(curX + direction.c.getX());
    int dy = this.normalizeColIndex(curY + direction.c.getY());
    this.moveToCell(dx, dy);
  }

    /**
     * after performing back-tracking, we should recover the maze as it was before
     * @param prevCell the cell visited when performing BT
     * @param prevCoin the coin in cell before the player enter into the cell
     * @param playerCoinBeforeBT the previous location of the player
     * @param playerLocBeforeBT the previous sum of coin of the player
     */
  public void cancelMove(Cell prevCell, int prevCoin, int playerCoinBeforeBT, Coordinate playerLocBeforeBT){
    // recover gold
    prevCell.setGold(prevCoin);

    // recover player coin
    this.coin = playerCoinBeforeBT;

    // recover visit
    prevCell.setIsVisited(false);

    // recover player's current location
    this.playerLocation = playerLocBeforeBT;
  }

    /**
     * a player "collects" gold by entering a room that has gold which is then removed from the room
     * @param coins defined as a 1 in this implementation
     */
  @Override
  public void pickupCoins(int coins) {
    this.coin += coins;
  }

    /**
     * simulate the situation that the player's coins are robbed by the thief
     * a player "loses" 10% of their gold by entering a room with a thief
     */
  @Override
  public void loseCoins() {
    this.coin = this.coin * (1 - this.COIN_LOSS_RATIO);
  }

    /**
     * find the parent id of a given regionId
     * @param x the given regionId
     * @return the parent id
     */
 public int find(int x){
    // recursive find and path compression
   if (this.parent[x] == x){
     return x;
   }
   return find(this.parent[x]);
  }

    /**
     * set x's parent id as y's parent id
     * @param x
     * @param y
     */
  public void union(int x, int y) {
    if (x == y){
      throw new RuntimeException("cannot union nodes from the same set");
    }
    int xRoot = this.find(x);
    int yRoot = this.find(y);
    this.parent[xRoot] = yRoot;
  }

    /**
     * generate random integers to perform distribution of coins and thieves
     * @param n the number of integers needed
     * @param min the lower bound
     * @param max the upper bound
     * @param unvailableInt leave integers in unvailableInt away in order to assure non-duplication
     * @return a list of randomly selected integers each represents the index of cells in the maze
     */
  public List<Integer> generateNonDupRandom(int n, int min, int max, List<Integer> unvailableInt) {
    List<Integer> rns = new ArrayList<>();
    Random random = new Random();
    while (rns.size() < n){
      int randomNumber = random.nextInt((max - min) + 1) + min;
      if (!rns.contains(randomNumber) && !unvailableInt.contains(randomNumber)){
        rns.add(randomNumber);
      }
    }
    return rns;
  }

    /**
     * return dx in case of non-wrapping maze
     * this function is override in wrappingRoomMaze
     * @param dx the row index
     * @return the normalized dx
     */
  @Override
  public int normalizeRowIndex(int dx) {
    return dx;
  }

    /**
     * return dx in case of non-wrapping maze
     * this function is override in wrappingRoomMaze
     * @param dy the col index
     * @return the normalized dy
     */
  @Override
  public int normalizeColIndex(int dy) {
    return dy;
  }


  /**
   * Getter Function: to retrieve a given cell from the maze
   * @param c coordinate of the cell of interest
   * @return the cell of interest
   */
  public Cell getCell(Coordinate c){
    return this.cell[c.getX()][c.getY()];
  }

    /**
     * Getter Function: to retrieve a given cell from the maze
     * (for convenience)
     * @param x row index
     * @param y col index
     * @return the cell of interest
     */
  public Cell getCell(int x, int y){
    return this.cell[x][y];
  }

    /**
     * Getter Function: get the goal
     * @return the goal
     */
  public Cell getGoalLoc(){
    return this.getCell(this.goalLocation);
  }

    /**
     * Getter Function: get the current location of the player
     * @return the current location of the player
     */
  @Override
  public Cell getPlayerLoc(){
    return this.getCell(this.playerLocation);
  }

    /**
     * Getter Function: get the wall number
     * @return the wall number
     */
  public int getWallNum(){
    return this.wallNum;
  }

    /**
     * Getter Function: get the set of edge
     * @return the set of edge
     */
  public HashSet<Edge> getEdgeSet(){
    return this.edgeSet;
  }

    /**
     * Getter Function: set value for setEdgeSet
     * @param edgeSet the modified edgeSet
     */
  public void setEdgeSet(HashSet<Edge> edgeSet) {
    this.edgeSet = edgeSet;
  }

    /**
     * add an edge to the edgeSet
     * @param e a new edge
     */
  public void addEdge(Edge e){
    this.edgeSet.add(e);
  }

    /**
     * Getter Function: get the number of rows in the maze
     * @return the number of rows
     */
  public int getRowNum(){
    return this.rowNum;
  }

    /**
     * Getter Function: get the number of columns in the maze
     * @return the number of columns
     */
  public int getColNum(){
    return this.colNum;
  }

    /**
     * Getter Function: get the current coins a player possess
     * @return the amount of coins
     */
  @Override
  public int getCoins() {
    return (int) Math.round(this.coin);
  }

    /**
     * set the player to a specific location
     * @param loc start location (or somewhere else in the maze during navigation).
     */
  @Override
  public void setPlayer(Cell loc) {
    this.moveToCell(loc.getCoordinate().getX(), loc.getCoordinate().getY());
  }

    /**
     * set the goal of this maze
     * @param goal goal location.
     */
  @Override
  public void setGoal(Cell goal) {
    this.goalLocation = goal.getCoordinate();
  }

    /**
     * report detailed information about cells and edges in the maze
     * @return the text
     */
  @Override
  public String toString(){
    String str = "Cell INFO: \n";
    for (int r = 0; r < this.rowNum; r++){
      for (int c = 0; c < this.colNum; c++){
        str += this.cell[r][c].toString();
      }
    }

    str += "\n Edge INFO: \n";
    for (Edge e: this.edgeSet){
      str += e.toString();
    }
    return str;
  }
  // other variables that are needed for building the maze...
  // implement those defined in Maze, also a few more methods
  // that for building the maze
}
