import java.util.Set;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Class Room - a location in an adventure game.
 *
 * This class is part of the "World of Zuul: A lesser Evil" game
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  For each existing exit, the room 
 * stores a reference to the neighboring room.
 * The room also has an inventory of items.
 * It can also store a GamePlay object.
 * It can also have locked rooms for which
 * the player requires a "key" in order to traverse
 * 
 * @author  Michael Kölling and David J. Barnes and Anton Luca-Dorin
 * @version 2017.12.08
 */

public class Room 
{
    private String name;
    private String description;
    private HashMap<String, Room> exits;        // stores exits of this room.
    private ArrayList<Item> inventory;
    private boolean visited;             //flag for if the player has been in the room already
    private HashMap<String, String> lockedExits;
    private GamePlay gamePlay;      //GamePlay object

    /**
     * Create a room described "description" and named "name". Initially, it has
     * no exits.
     * @param description The room's description.
     * @param name The room's name
     */
    public Room(String name, String description)
    {
        this.name = name;
        this.description = description;
        this.inventory = new ArrayList<>();
        this.exits = new HashMap<>();
        this.lockedExits = new HashMap<>();
        this.visited = false;
    }

    /**
     * Define an exit from this room.
     * @param direction The direction of the exit.
     * @param neighbor  The room to which the exit leads.
     */
    public void setExit(String direction, Room neighbor, String key) 
    {
        exits.put(direction, neighbor);
        lockedExits.put(direction, key);
    }

    /**
     * @return The short description of the room
     * (the one that was defined in the constructor).
     */
    public String getShortDescription()
    {
        return description;
    }

    /**
     * return a description of the room also containing
     * the exits and the rooms linked to them respectivley.
     * @return A long description of this room
     */
    public String getLongDescription()
    {
        return description + ".\n" + getExitString();
    }

    /**
     * Return a string describing the room's exits
     * and the rooms linked to them, for example
     * "north - plains
     *  west - meadows".
     * @return Details of the room's exits.
     */
    private String getExitString()
    {
        String returnString = "\nFrom here you can go:";
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            returnString += "\n" + exit +  " - " + exits.get(exit).getName();
        }
        returnString += "\n";
        return returnString;
    }

    /**
     * Check if an Item with a specific name
     * is in this room's inventory
     * @param the Item name we are checking for
     * @return if we found it or not
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
     * @return the set of exits of this Room
     */
    public Set getExitSet()
    {
        return exits.keySet();
    }
    
    
    /**
     * @return the set of keys to open the locked doors
     */
    public Set getKeySet() {
        return lockedExits.keySet();
    }
    
    /**
     * @return the name of this room
     */
    public String getName() {
        return this.name;
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * @param direction The exit's direction.
     * @return The room in the given direction.
     */
    public Room getExit(String direction) 
    {
        return exits.get(direction);
    }
    
    /**
     * Returns the key the player needs if he wants
     * to travers a certain exit.
     * @param direction the exit we want to go through
     * @return the key required if we want to go through that exit
     */
    public String getKey(String direction) {
        return lockedExits.get(direction);
    }
    
    /**
     * Define that the player has visited this room
     */
    public void setVisited()
    {
        visited = true;
    }
    
    /**
     * @return if the player has visited this room
     */
    public boolean getVisited()
    {
        return visited;
        }
        
    /**
     * Define  gamePlay for this room.
     * this is not done in the constructor, as the information
     * required is not available at the time of construction
     * @param gamePlay the gamePlay for this room
     */
    public void setGamePlay(GamePlay gamePlay) {
        this.gamePlay = gamePlay;
    }
    
    /**
     * @return if thi room has gamePlay
     */
    public boolean hasGamePlay() {
        if(gamePlay == null) {
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * if the level of this room has already been completed
     */
    public boolean getGamePlayState() {
        if(gamePlay == null) {
            return false;
        } else {
            return gamePlay.isDone();
        }
    }
    
    /**
     * Starts the level of this room
     * by callig it's show method
     */
    public void startGame() {
        if( gamePlay != null) {
            gamePlay.show();
        }
    }
    
    /**
     * Stops the level of this game
     * by calling it's hide methode
     * usually when the level is done
     */
    public void stopGame() {
        if(gamePlay != null)  {
            gamePlay.hide();
        }
    }
    
    
    /**
     * @return inventory the inventory of this room
     */
    public ArrayList<Item> getInventory() {
        return inventory;
    }
    
    /**
     * @param a specific index in the Inventory of this room(assumed valid)
     * @return an item that index
     */
    public Item getItemAtIndex(int index) {
        return inventory.get(index);
    }
    
    /**
     * @param the Itam which will be added to the room's inventory
     */
    public void addToInventory(Item itemToAdd) {
        inventory.add(itemToAdd);
    }
    
    /**
     * @param the item to remove from the room's inventory
     */
    public void removeItemFromInventory(Item itemToRemove) {
        inventory.remove(itemToRemove);
    }
    
    /**
     * @param the index at which we will find the item to remove(assumed valid)
     */
    public void removeItemFromInventory(int index) {
        inventory.remove(index);
    }
    
    /**
     * Give the GamePlay object it's necessary info in order to be able to start the game
     * @param playerHealth the player's current health
     * @param maxPlayerHealth the player's maxiumu Hitpoints
     * @param shells the player's amount of ammo
     * @param hasShotgun if the player has aquired the ShotGun object
     * @param name the player's name
     */
    public void setDetails(int playerHealth, int maxPlayerHealth, int shells, boolean hasShotgun, String playerName) {
        if( gamePlay != null) {
            gamePlay.setDetails(playerHealth, maxPlayerHealth, shells, hasShotgun, playerName);
        }
    }
    
    /**
     * @return the health of the player, according to the rooms GamePlay object
     */
    public int getHealth() {
        if(gamePlay != null) {
            return gamePlay.getPlayerHealth();
        } else {
            return 0;   
        }
    }

    /**
     * @return the shotgun shells of the player, according to this rooms GamePlay object
     */
    public int getShells() {
        if(gamePlay != null) {
            return gamePlay.getShells();
        }
        else {
            return 0;
        }
    }
}
