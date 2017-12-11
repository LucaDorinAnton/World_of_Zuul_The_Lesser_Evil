import java.util.Iterator;

/**
 * "MiniMap" governs the logic of the miniMap
 * in "The World of Zuul: The Lesser Evil"
 * it recursivley goes through all the rooms and
 * puts those which have been visited in a matrix.
 * Then it passes that matrix on to a MiniMapGUI in order
 * to display it as images.
 *
 * @author Luca-Dorin Anton
 * @version 2017.12.09
 */
public class MiniMap
{
    private Game game;
    private int miniMapSize;
    private MiniMapGUI miniMapGUI;
    private String[][] miniMapDesc;
    private int[][] miniMapVal;
    
    /**
     *  Create a MiniMap
     *  @param the game creating the MiniMap
     */
    public MiniMap(Game game) {
     this.game = game;
     this.miniMapSize = setupMiniMapSize();
     miniMapDesc = new String[miniMapSize][miniMapSize];
     miniMapVal = new int[miniMapSize][miniMapSize];
     newMiniMapGUI();
    }
    
    /**
     * Reset the fields containing the map info
     */
    public void resetMiniMapDescVal() {
        miniMapDesc = new String[miniMapSize][miniMapSize];
        miniMapVal = new int[miniMapSize][miniMapSize];
    }
    
    
    /**
     *  Crea a new MiniMapGUI for this map
     */
    public void newMiniMapGUI() {
     this.miniMapGUI = new MiniMapGUI(this);
    }
    
    /**
     * Recursivley generate the map values.
     * This works by looking at the neighbours of a room
     * and by calling this function on those which have been visited
     * by the player.
     * @param room the current room to be processed
     * @param x to x coordinate of the room in the main map matrix
     * @param y the y coordinate of the room in the main map matrix
     */
    private void recursiveMiniMap(Room room, int x, int y)
    {
        String exit;
        if(room.getVisited()) {
            //add this room to the display, as it has been visited
            miniMapDesc[x][y] = " [ " + room.getName() + " ] ";
            miniMapVal[x][y] = 1;
            //If this is the room in which the player is in, display it accordingly
            if(game.getCurrentRoom().equals(room)) {
                miniMapVal[x][y] = 2;
            }
            //Process the exits of the room
            for (Iterator<String> it = room.getExitSet().iterator(); it.hasNext(); ) 
                 {
                    exit = it.next();
                    if(exit == "west")
                        {
                            miniMapDesc[x][y-1] = " Path ";
                            miniMapVal[x][y-1] = 3;
                            if(room.getExit(exit).getVisited() && miniMapDesc[x][y-2] == null) {
                                recursiveMiniMap(room.getExit(exit), x, y-2);
                            }
                        } else if(exit == "east") {
                                   miniMapDesc[x][y+1] = " Path ";
                                   miniMapVal[x][y+1] = 3;
                                   if(room.getExit(exit).getVisited()&& miniMapDesc[x][y+2] == null) {
                                       recursiveMiniMap(room.getExit(exit), x, y+2);
                                    }
                                } else if (exit == "south"){
                                        miniMapDesc[x+1][y] = " Path ";
                                        miniMapVal[x+1][y] = 4;
                                        if(room.getExit(exit).getVisited()&& miniMapDesc[x+2][y] == null) {
                                            recursiveMiniMap(room.getExit(exit), x+2, y);
                                        }
                                    } else if(exit == "north") {
                                            miniMapDesc[x-1][y] = " Path ";
                                            miniMapVal[x-1][y] = 4;
                                            if(room.getExit(exit).getVisited()&& miniMapDesc[x-2][y] == null) {
                                            recursiveMiniMap(room.getExit(exit), x-2, y);
                                          }
                                         }                
                }
        }
        
    }
    
    /**
     * based on the number of the rooms,
     * make an estimate of how big the map should be,
     * based on the assumption that all rooms are linked 
     * side to side or one over the other
     */
    private int setupMiniMapSize()
    {
         int displaySize;
         int rooms = game.getNumberOfRooms();
         if((rooms*2-1)%2 == 0)
         {
             displaySize = rooms*2;
            }
         else 
         {
             displaySize = rooms*2 - 1;
            }
         return displaySize  ;
    } 
    
    /**
     * @return the matrix containing the descriptions for the map tooltips
     */
    public String[][] getMiniMapDesc()
    {
        return miniMapDesc;
    }
    
    /**
     * @return the matrix of values which will be turned into the map image
     */
    public int[][] getMiniMapVal()
    {
        return miniMapVal;
    }
    
    /**
     * @return the miniMap size
     */
    public int getMiniMapSize() {
        return miniMapSize;
    }
    
    /**
     * Initialize the minimap by 
     * reseting the value matrix and desc matrix
     * calculating the miniMap
     * and display the result using the GUI
     */
    public void startMiniMap() {
        resetMiniMapDescVal();
        recursiveMiniMap(game.getCurrentRoom(), miniMapSize/2, miniMapSize/2);
        if(miniMapGUI.displaySetupFinished()) {
            miniMapGUI.refreshDisplay();
            } else {
            miniMapGUI.createDisplay();
        }
        miniMapGUI.showMiniMap();
    }
    
    /**
     * show the miniMap
     */
    public void showMap() {
        miniMapGUI.show();
    }
    
    /**
     * Hide the miniMap
     */
    public void hideMap() {
        miniMapGUI.hide();
    }
    
    /**
     * @return if the map is visible
     */
    public boolean mapIsVisible() {
        return miniMapGUI.isMapVisible();
    }
    
    /**
     * hide the miniMap
     */
    public void killMiniMap() {
        if(miniMapGUI != null) {
            miniMapGUI.hideMiniMap();
        }
    }
    
    /**
     * Auxilliary method for Debugging
     * Print to the terminalthe matrix Values
     */
    private void printVals() {
        for(int i=0;i<miniMapSize;i++) {
            for(int j=0;j<miniMapSize; j++) {
                System.out.print(miniMapVal[i][j] + " ");
            }
            System.out.print("\n");
        }
        System.out.println("#############");
    }
}
    
    
