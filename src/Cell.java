/**
 * a cell contains a regionId, a coordinate, and a flag denoting whether it is visited,
 * the amount of gold it has, a flag showing whether it has thief, the penaltyRatio for robbing the player
 */
public class Cell {
  private int regionId;
  private Coordinate coordinate;
  private boolean isVisited;

  // 0 or a certain amount
  private int gold;
  private boolean hasThief;
  private final double penaltyRatio;
  private static final int GOLDAMOUNT = 1;

  /**
   * constructor function for the cell
   * @param regionId the regionId
   * @param coordinate the location
   * @param hasGold whether it has coin
   * @param hasThief whether is has thief
   * @param penaltyRatio the penalty ratio for entering a cell has thief
   */
  public Cell(int regionId, Coordinate coordinate, boolean hasGold, boolean hasThief, double penaltyRatio){
    this.regionId = regionId;
    this.coordinate = coordinate;
    this.isVisited = false;
    this.penaltyRatio = penaltyRatio;

    if (hasGold){
      this.gold = this.GOLDAMOUNT;
    }else{
      this.gold = 0;
    }
    this.hasThief = hasThief;
  }

  /**
   * Getter Function: get regionId
   * @return regionId
   */
  public int getRegionId(){
    return this.regionId;
  }

  /**
   * Getter Function: get the location
   * @return the location
   */
  public Coordinate getCoordinate(){
    return this.coordinate;
  }

  /**
   * Getter Function: whether it has been visited before
   * @return boolean flag showing whether it has been visited before
   */
  public Boolean getIsVisited(){
    return this.isVisited;
  }

  /**
   * set isVisited according to b
   * @param b boolean flag showing whether it has been visited before
   */
  public void setIsVisited(Boolean b){
    this.isVisited = b;
  }

  /**
   * Getter Function: get the amount of coin in this cell
   * @return the amount of coin in this cell
   */
  public int getGold(){
    return this.gold;
  }

  /**
   * Getter Function: set gold to zero
   */
  public void removeGold(){
    this.setGold(0);
  }

  /**
   * set gold according to g
   * @param g the amount of coin
   */
  public void setGold(int g){
    this.gold = g;
  }

  /**
   * Getter Function: whether it has thief
   * @return boolean flag showing whether it has thief
   */
  public boolean getHasThief(){
    return this.hasThief;
  }

  /**
   * report the detailed information of the cell
   * @return
   */
  @Override
  public String toString() {
    String str = "";
    str += "regionId: " + this.regionId + "\n" +
            "coin: " + this.getGold() + "\n" +
            "has thief: " + this.hasThief + "\n\n";
    return str;
  }
}

