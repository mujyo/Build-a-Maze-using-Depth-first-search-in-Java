/**
 * the location of the cell
 */
public class Coordinate {
    // row index
    private int x;
    // column index
    private int y;

    /**
     * constructor
     * @param x row index
     * @param y column index
     */
    public Coordinate(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     * Getter Function: get access to the row index
     * @return the index
     */
    public int getX(){
        return this.x;
    }

    /**
     * Getter Function: get access to the column index
     * @return the index
     */
    public int getY(){
        return this.y;
    }
}
