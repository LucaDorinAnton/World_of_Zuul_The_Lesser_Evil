import java.util.ArrayList;
/**
 * "Player" is a part of the "World of Zuul: A Lesser Evil" game.
 * 
 * It models the player character, storing information about
 * his health, his location and his inventory.
 *
 * @author Anton Luca-Dorin
 * @version 2017.12.09
 */
public class Player
{
    private String name;
    private ArrayList<Room> path; // stack for remembering the way the player went
    private Room currentRoom;
    private ArrayList<Item> inventory;
    private int health;
    private int maxHealth;
    private final int maxCarryWeight;
    private int currentCarryWeight;
    
    /**
     * Create a new Player
     * @param health the player's current health
     * @parm maxHealth the player's maxiumu Hit Points
     */
    public Player( int health, int maxHealth)
    {
        this.path = new ArrayList<>();
        this.inventory = new ArrayList<>();
        this.health = health;
        this.maxHealth = maxHealth;
        this.maxCarryWeight = 100;
        this.currentCarryWeight = 0;
    }
    
    /**
     * @return the room in which the player currently is
     */
    public Room getCurrentRoom()
    {
     return path.get(path.size()-1);   
    }
    
    /**
     * @param room the room to add to the path of the player
     */
    public void addToPath(Room room) {
        path.add(room);
    }
    
    /**
     * Take the last room from the path stack
     * (the room in which the player is currently in)
     * and remove it from the queue
     */
    public void removeFromPath() {
        path.remove(path.size()-1);
    }
    
    /**
     * @return lenght the lenght of the player's path
     */
    public int pathSize() {
        return path.size();
    }
    
    /**
     * add an Item to the player's inventory
     * @param item the Item to add
     * @return if the item has been successully added 
     */
    public boolean addToInventory(Item itemToAdd) {
        if(currentCarryWeight + itemToAdd.getWeight() <= maxCarryWeight) {
            currentCarryWeight += itemToAdd.getWeight();
            inventory.add(itemToAdd);
            return true;
        } else {
            return false;
        }

    }
    
    /**
     * Remove an item at a specified index from
     * the players inventory
     * @param index the index at which we want to remove the item
     */
    public void removeFromInventoryAtIndex(int index) {
        currentCarryWeight -= inventory.get(index).getWeight();
        inventory.remove(index);
    }
    
    /**
     * @return the player's Inventory
     */
    public ArrayList getInventory() {
        return inventory;
    }
    
    /**
     * @param index the index at which we want to search for(assumed valid)
     * @return item the Item at the specified index
     */
    public Item getItemAtIndex(int index) {
        return inventory.get(index);
    }
    
    /**
     * @param name item name for which we chek if it is in the inventory
     * @result if the item is in the inventory or not
     */
    public boolean checkForItemInInventory(String itemName) {
        for(Item item : inventory) {
            if(item.getName().equals(itemName)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * @param name item name to remove
     */
    public void removeItemFromInventory(String itemName) {
        for(Item item : inventory) {
            if(item.getName().equals(itemName)) {
                 currentCarryWeight -= item.getWeight();
                inventory.remove(item);
                break;
            }
        }
    }
    
    /**
     * @param new name of the player
     */
    public void setName(String name) {
     this.name = name;   
    }
    
    /**
     * @return the name of the player
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * @return the player's maximum carry weight
     */
    public int getMaxCarryWeight() {
        return maxCarryWeight;
    }
    
    /**
     * @return how much the player is currently carrying
     */
    public int getCurrentCarryWeight() {
        return currentCarryWeight;
    }
    
    /**
     * @param amount how much to set the current carry weight of the player
     */
    public void setCurrentCarryWeight(int amount) {
        currentCarryWeight = amount;
    }
    
    /**
     * @return the maxiumum health of the player
     */
    public int getMaxHealth() {
        return maxHealth;
    }
    
    /**
     * @return the current health of the player
     */
    public int getCurrentHealth() {
        return health;
    }
    
    /**
     * @param amount how much to add to the player's maxiumum health
     */
    public void addToMaxHealth(int amount) {
        maxHealth += amount;
    }
    
    /**
     * @param amount how much to add to the player's health
     */
    public void addToHealth(int amount) {
        health += amount;
        if(health > maxHealth) {
            health = maxHealth;
        }
    }
    
    /**
     * @param room room to teleport the player to
     */
    public void teleport(Room room) {
        currentRoom = room;
        path = new ArrayList<>();
        path.add(room);
    }
    
    /**
     * @param health the new value of the player's current health
     */
    public void setCurrentHealth(int health) {
        this.health = health;
    }
}
