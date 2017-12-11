import java.util.ArrayList;
import java.util.Random;
/**
 * "Entity" is a class of the "World of Zuul:The Lesser Evil" game.
 * 
 * It represents a character, or a person,in the world of the game.
 *
 *
 * @author Anton Luca-Dorin
 * @version 2017.12.09
 */
public class Entity
{
    private boolean canMove;
    private Room currentRoom;
    private String name;
    private ArrayList<Room> rooms;
    private Random random;
    
    /**
     * Create an Entity
     * @param canMove if the Entity has the ability to move around
     * @param currentRoom the room in which the Entity currently is
     * @param name the name of the Entity
     */
    public Entity(boolean canMove, Room currentRoom, String name) {
     this.canMove = canMove;
     this.currentRoom = currentRoom;
     this.name = name;
     this.rooms = new ArrayList<>();
     random = new Random();
    }
    
    /**
     * @param add a room to the Entity's list of rooms between which it can move
     */
    public void addRoom(Room room) {
        rooms.add(room);
    }
    
    /**
     * Try to move the Entity.
     * If the Entity can Move, try 3 times to randomly get an Exit
     * thorugh which it can pass. If it fails, it stays put.
     */
    public void move() {
        if(canMove) {
            for(int i=0;i<3;i++) {
                int dir = random.nextInt(4);
                Room nextRoom = null;
                if(dir == 0) {
                    nextRoom = currentRoom.getExit("north");
                } else if(dir == 1) {
                    nextRoom = currentRoom.getExit("south");
                }else if(dir == 2) {
                    nextRoom = currentRoom.getExit("west");             
                }else {
                    nextRoom = currentRoom.getExit("east");
                }
               
                if(nextRoom != null && rooms.contains(nextRoom)) {
                    currentRoom = nextRoom;
                    break;
                }
            }
        }
    }
    
    public Room getCurrentRoom() {
        return currentRoom; 
    }
    
    public String getName() {
        return name;
    }
}
