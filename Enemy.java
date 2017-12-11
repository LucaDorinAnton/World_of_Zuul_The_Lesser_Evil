
/**
 * "Enemy" is a part of the "world of Zuul: A lesser Evil" game.
 * 
 *An Enemy is a point on the grid of a GamePlay object.
 *
 * @author Anton Luca-Dorin
 * @version 2017.12.08
 */
public class Enemy
{
    private int x;
    private int y;
    
    /**
     * Constructor for objects of class Enemy
     * @param x the x value of the Enemy
     * @param y the y value of the Enemy
     */
    public Enemy(int x,int y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * @param x the new X value of the Enemy
     * @param y the new Y value of the Enemy
     */
   public void setXY(int x, int y) {
       this.x = x;
       this.y = y;
    }
    
   /**
    * @return x the x value of the Enemy
    */
   public int getX(){
     return x;   
   }
    
   /**
    * @return y the y value of the Enemy
    */
   public int getY(){
     return y;
   }

}
