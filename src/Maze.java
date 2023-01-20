import java.util.List;

public interface Maze {
  /**
   * Set player at the start location.
   * @param loc start location.
   */
  void setPlayer(Cell loc);

  /**
   * Set the goal location.
   *
   * @param goal goal location.
   */
  void setGoal(Cell goal);

  /**
   * Get possible directions could move to.
   *
   * @return possible directions.
   */
  List<Direction> getPossibleMoves();

  /**
   * move to the direction.
   *
   * @param direction the direction to move.
   */
  void move(Direction direction);

  // more methods if necessary ...

  /**
   *   The maze can produce the player's location
   * @return
   */
  Cell getPlayerLoc();

  /**
   *   The maze can produce the player's gold count
   * @return
   */
  int getCoins();

  /**
   *  "collects" gold by entering a room that has gold which is then removed from the room
   * @param coins defined as a 1 in this implementation
   */
  void pickupCoins(int coins);

  /**
   * "loses" 10% of their gold by entering a room with a thief
   */
  void loseCoins();

  /**
   * get the number of rows in the maze
   * @return the number of rows in the maze
   */
  int getRowNum();

  /**
   * get the number of columns in the maze
   * @return the number of columns in the maze
   */
  int getColNum();

  /**
   * tear down walls
   */
  void generateMaze();

  /**
   * generate cells, parent
   * @param rewardCellIds
   * @param penaltyCellIds
   */
  void generateCells(List<Integer> rewardCellIds, List<Integer> penaltyCellIds);

  /**
   * for wrapping room maze when index is of out range
   * @param dx the row index
   * @return transformed row index
   */
  int normalizeRowIndex(int dx);

  /**
   * for wrapping room maze when index is of out range
   * @param dy the col index
   * @return transformed col index
   */
  int normalizeColIndex(int dy);
}
