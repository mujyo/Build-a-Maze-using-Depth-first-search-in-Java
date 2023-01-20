/**
 * 4 directions with values to calculate the next move
 */
public enum Direction {
  NORTH(new Coordinate(-1, 0)),
  SOUTH(new Coordinate(1, 0)),
  EAST(new Coordinate(0, 1)),
  WEST(new Coordinate(0, -1));

  public final Coordinate c;

  /**
   * the value of a direction
   */
  private Direction(Coordinate c){
    this.c = c;
  }
}