import java.util.ArrayList;
import java.util.Random;
/**
 * "Teleporter" is a subclass of the "Item" class and a part
 * of the "World of Zuul: The Lesser Evil" game
 * 
 * Teleporter - an Item which allows the player's current room
 * to be alterted
 *
 * @author Anton Luca-Dorin
 * @version 2017.12.08
 */
public class Teleporter extends Item
{
    private ArrayList<Room> rooms;
    private Random random;
    
    /**
     * Create a teleporter. A teleporter weighs alot,
     * so it can't be moved.
     * @param name the teleporter's name
     * @param desc the teleporter's description
     * @param movable if the teleporter can be moved or not
     */
    public Teleporter(String name, String desc, boolean movable) {
        super(name, desc, movable, 300);
        this.rooms = new ArrayList();
        this.random = new Random();
    }
    
    /**
     * Override the stub method of the parent class.
     * Add a room to the teleporters list of rooms
     * to which the player will be able to teleport
     * 
     * @param room the room to add to the list
     */
    @Override
    public void addToRooms(Room room) {
        rooms.add(room);
    }
    
    /**
     * Override the stub method of the parent class
     * Give a room to which the player can teleport to.
     * @return room a room to which the player can teleport
     */
    @Override
    public Room getARoom() {
        if(rooms.isEmpty()) {
             return null;
        } else {
             int nextRoomIndex = random.nextInt(rooms.size());
             return rooms.get(nextRoomIndex);
        }
    }
}
