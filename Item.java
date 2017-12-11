
/**
 * The "Item" class represents an Item in the
 * game "World of Zuul: The Lesser Evil"
 * An item can be pciked up, has weight
 * and properties.
 * Items can be found or obtained from characters
 * "Item" has three subclasses with difrent special
 * abilities
 *
 * @author Anton Luca-Dorin
 * @version 2017.12.08
 */
public class Item
{
    private String name;
    private String desc;
    private boolean movable;
    private int weight;
    
    /**
     * Create an item
     * @param name the item's name
     * @param desc the item's description
     * @param movable can the item be moved?
     * @param weight the weight of the item
     */
    public Item( String name, String desc, boolean movable, int weight) {
        setName(name);
        setDesc(desc);
        setCanBePickedUp(movable);
        this.weight = weight;
    }
    
    
    /**
     * @return the item's name
     */
    public String getName() {
        return name;   
    }
    
    /**
     * @return the item's description
     */
    public String getDesc() {
        return desc;
    }
    
    /**
     * @return the item's weight
     */
    public int getWeight() {
        return weight;
    }
    
    /**
     * @param String to set the item's new name
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * @param String to set the item's new description
     */
    public void setDesc(String desc) {
     this.desc = desc;
    }
    
    /**
     * @param value to set the item's new movable state
     */
    public void setCanBePickedUp(boolean movable) {
        this.movable = movable;
    }
    
    
    /**
     * @return if the item can be moved
     */
    public boolean getMovable() {
        return movable;
    }
    
    /**!Disclaimer: stub method for subclasses
     * @return the HP bonus of the item
     */
    public int getHpBoost(){
        return 0;
    }
    
    /**!Disclaimer: stub method for subclasses
     * @return the maxHP bonus of the item
     */
    public int getMaxHpBoost() {
        return 0;
    }
    
    /**!Disclaimer: stub method for subclasses
     * @param add a room to the teleporter item's
     * list of rooms to which the player can teleport
     */
    public void addToRooms(Room room) {
        
    }
    
    /**!Disclaimer: stub method for subclasses
     * @return a random room
     * from the teleporter's list of
     * rooms.
     */
    public Room getARoom() {
        return null;
    }
}
