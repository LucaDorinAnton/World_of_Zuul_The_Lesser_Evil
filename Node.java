
/**
 * "Node" is a part of the "World of Zuul: The Lesser Evil" game.
 *
 *It represents a tool for calculating the path
 *between an enemy and the player in the GamePlay class.
 *
 *It stores a location in a bi-dimensional array and
 *the distance from the starting point of the calculation
 *
 * @author Anton Luca-Dorin
 * @version 2017.12.09
 */
public class Node
{
    private int x,y,dist;

    /**
     * Constructor for objects of class Node
     * @param x the x coordinate of the Node
     * @param y the y coordinate of the Node
     * @param dist the distance of the Node from the starting point of the calculation expressed in steps
     */
    public Node(int x,int y,int dist)
    {
        this.x = x;
        this.y = y;
        this.dist = dist;
    }
    
    /**
     * Give this Node new coordinates
     * @param x the new x coordinate
     * @param y the new y coordinate
     */
    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return the x value of this Node
     */
    public int getX(){
        return x;   
    }

    /**
     * @return the y value of this Node
     */
    public int getY(){
        return y;
    }

    /**
     * @return the distance of this Node from the start point of the calculation
     */
    public int getDist() {
        return dist;
    }
    
    /**
     * @param the new distance of the Node from the starting point of the calculation
     */
    public void setDist(int newDist) {
        dist = newDist;  
    }
}
