/**
 * the edge between two cells
 */
public class Edge {
    private Cell source;
    private Cell destination;
    public Edge(Cell source, Cell destination){
        this.source = source;
        this.destination = destination;
    }

    /**
     * Getter Function: get access to the source
     * @return the source
     */
    public Cell getSource(){
        return this.source;
    }

    /**
     * Getter Function: get access to the destination
     * @return the destination
     */
    public Cell getDestination(){
        return this.destination;
    }

    /**
     * report detailed information of an edge
     * @return text to print out
     */
    @Override
    public String toString(){
        String str = "(" + this.getSource().getCoordinate().getX() +
                ", " + this.getSource().getCoordinate().getY() + ") " +
                "-> " + "(" + this.getDestination().getCoordinate().getX() +
                ", " + this.getDestination().getCoordinate().getY() + ") \n";
        return str;
    }
}
