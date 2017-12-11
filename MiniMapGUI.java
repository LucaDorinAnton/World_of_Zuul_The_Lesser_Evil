import javax.swing.*;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
/**
 * "MiniMapGUI" is a class of the "World of Zuul: The lesser Evil" game.
 * 
 * It represents a Window which can convert a matrix of int values to
 * a series of images which represents a map of the game.
 * 
 * It uses Java Swing and the GridLayout
 *
 * @author Anton Luca-Dorin
 * @version 2017.12.08
 */
public class MiniMapGUI extends JFrame
{
    private MiniMap display;    //from where we get our values
    private JPanel mainPanel;   //where the images are displayed
    private String[][] displayDesc; //the display tooltips
    private int[][] displayVal;     //      and values
    private int displaySize;        
    private BufferedImage[] images; // the images used
    private boolean displaySetupFinished;   //if we finished creating the display
    
    /**
     * Create a MiniMapGUI
     * @param display the MiniMap from which the GUI gets it's display
     */
    public MiniMapGUI(MiniMap display) {
        this.display = display;
        getDisplayParameters();
        mainPanel = new JPanel();
        this.images = new BufferedImage[5];
        loadImages();
        displaySetupFinished = false;
    }
    
    /**
     * get the information needed to create the display
     * from the miniMap
     */
    private void getDisplayParameters() {
        this.displayDesc = display.getMiniMapDesc();
        this.displayVal = display.getMiniMapVal();
        this.displaySize = display.getMiniMapSize();
    }
    
    /**
     * Initialize the display
     */
    public void createDisplay() {
        getDisplayParameters();
        setupDisplay();
    }
    
    /**
     * Set up the display
     */
    public void setupDisplay() {
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.setSize(displaySize*25,displaySize*25);
        this.setResizable(false);
        this.setTitle("MiniMap");
        mainPanel.setLayout(new GridLayout(displaySize,displaySize));
        
        addImages();
        
        this.add(mainPanel);
        displaySetupFinished = true;
    }
    
    /**
     * recalculate the display
     */
    public void refreshDisplay() {
        getDisplayParameters();
        mainPanel.removeAll();
        addImages();
        this.revalidate();
    }
    
    /**
     * based on the matrix of the miniMap
     * Load the correct image in the correct slot of the GUI,
     * whit it;s correct Tooltip
     */
    private void addImages() {
                for(int i=0;i<displaySize;i++) {
            for(int j=0;j<displaySize;j++) {
                JLabel label = new JLabel(new ImageIcon(images[displayVal[i][j]]));
                label.setToolTipText(displayDesc[i][j]);
                mainPanel.add(label);
            }
        }
    }
    
    /**
     * display this GUI
     */
    public void showMiniMap() {
        this.setVisible(true);
    }
    
    /**
     * Hide this GUI
     */
    public void hideMiniMap() {
        this.setVisible(false);
    }
    
    /**
     * Load the required images from the project folder
     */
    public void loadImages() {
        try {                
           images[0] = ImageIO.read(getClass().getResource("no_room.jpg"));
       } catch (IOException ex) {
            System.out.println("Error!");
       }
       
        try {                
           images[1] = ImageIO.read(getClass().getResource("visited_room.jpg"));
       } catch (IOException ex) {
            System.out.println("Error!");
       }
       
        try {                
           images[2] = ImageIO.read(getClass().getResource("current_room.jpg"));
       } catch (IOException ex) {
            System.out.println("Error!");
       }
       
        try {                
           images[3] = ImageIO.read(getClass().getResource("Path_WE.jpg"));
       } catch (IOException ex) {
            System.out.println("Error!");
       }
       
        try {                
           images[4] = ImageIO.read(getClass().getResource("Path_NS.jpg"));
       } catch (IOException ex) {
            System.out.println("Error!");
       }
    }
    
    /**
     * return if the GUI is visible
     */
    public boolean isMapVisible() {
        return this.isVisible();
    }
    
    /**
     * return if the GUI has finished it's setup process
     */
    public boolean displaySetupFinished() {
        return displaySetupFinished;
    }
}
