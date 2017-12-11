import javax.swing.*;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.Toolkit;
import java.awt.Dimension;

/**
 * "GamePlayGUI" is part of the "World of Zuul: The Lesser Evil" game;
 * 
 * This graphical user interface serves the purpose of displaying the level the player is currently
 * at and also serves as a means of capturing and processing KeyPress events.
 * 
 * It uses Java Swing and the GridLayout
 *
 * @author Anton Luca-Dorin
 * @version 2017.12.08
 */
public class GamePlayGUI extends JFrame
{
    private BufferedImage[] images; //image source
    private int[][] display; //main display matrix
    private int displaySize;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JLabel playerLabel;
    private JLabel healthLabel;
    private JLabel enemiesLabel;
    private JLabel shellsLabel;
    private JLabel controlsLabel;
    private JLabel moveLabel;
    private JLabel attackLabel;
    private JLabel dashLabel;
    private JLabel dashRechargeLabel;
    private JProgressBar dashRechargeBar;
    private JLabel extraText;
    private GamePlay gamePlay; 

    /**
     * Create a GamePlayGUI object and set it up
     * 
     * @param size the size of the display
     * @param gamePlay the GamePlay object for this instance of GamePlayGUI
     */
    public GamePlayGUI(int size, GamePlay gamePlay) {
        display = new int[size][size];
        images = new BufferedImage[4];
        this.gamePlay = gamePlay;
        this.displaySize = size;
        loadImages();
        setupGUI();
    }

    /**
     * Initialize Swing components and add them correctly
     * Initialize KeyListener Frame
     * Update the display
     */
    private void setupGUI() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(700, 450);
        int xPos = screen.width/2 - this.getWidth()/2;
        int yPos = screen.height/2 - this.getHeight()/2;
        this.setLocation(xPos, yPos);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.setTitle("World of Zuul: The Lesser Evil");
        this.setResizable(false);

        MyKeyListener myKeyL = new MyKeyListener();
        myKeyL.setGamePlay(gamePlay);
        myKeyL.addKeyListener(myKeyL);
        myKeyL.setFocusable(true);
        JPanel mainPanel = new JPanel();
        leftPanel = new JPanel();
        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        leftPanel.setSize(400, 400);
        leftPanel.setLayout(new GridLayout(displaySize, displaySize));

        playerLabel = new JLabel(gamePlay.getPlayerName());
        healthLabel = new JLabel("Health: " + gamePlay.getPlayerHealth() + "/" + gamePlay.getMaxPlayerHealth());
        enemiesLabel = new JLabel("Enemies left: " + gamePlay.getEnemiesCount());
        shellsLabel = new JLabel("Shotgun Shells: " + gamePlay.getShells());
        controlsLabel = new JLabel("Controls:");
        moveLabel = new JLabel("Move with: WASD");
        attackLabel = new JLabel("Your attack is Dash: Shift + WASD");
        dashLabel = new JLabel("Shoot a Shotgun Shell: Alt + WASD");
        dashRechargeLabel = new JLabel("Dash Recharge: ");
        dashRechargeBar = new JProgressBar(0, 100);
        dashRechargeBar.setValue(100);
        dashRechargeBar.setString("Dash Ready!");
        dashRechargeBar.setStringPainted(true);
        extraText = new JLabel("");

        rightPanel.add(playerLabel);
        rightPanel.add(healthLabel);
        rightPanel.add(enemiesLabel);
        rightPanel.add(shellsLabel);
        rightPanel.add(controlsLabel);
        rightPanel.add(moveLabel);
        rightPanel.add(attackLabel);
        rightPanel.add(dashLabel);
        rightPanel.add(dashRechargeLabel);
        rightPanel.add(dashRechargeBar);
        rightPanel.add(extraText);

        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);
        myKeyL.add(mainPanel);
        this.add(myKeyL);
        update();
    }

    /**
     * update the display by getting the required
     * information from the gamePlay Object and then
     * translating the integer matrix into Images, which get
     * correctly added to the GridLayout
     */
    public void update() {
        display = gamePlay.getDisplay();
        leftPanel.removeAll();
        for(int i=0;i<displaySize;i++) {
            for(int j=0;j<displaySize;j++) {
                JLabel label = new JLabel(new ImageIcon(images[display[i][j]]));
                leftPanel.add(label);
            }
        } 
        healthLabel.setText("Health: " + gamePlay.getPlayerHealth() + "/" + gamePlay.getMaxPlayerHealth());
        enemiesLabel.setText("Enemies left: " + gamePlay.getEnemiesCount());
        shellsLabel.setText("Shotgun Shells: " + gamePlay.getShells());
        setDashRechargeValue(gamePlay.getDashRecharge());
        this.repaint();
        this.revalidate();
    }
    
    /**
     * Load the Images from the project folder
     */
    private void loadImages() {
        try {                
           images[0] = ImageIO.read(getClass().getResource("floor.jpg"));
       } catch (IOException ex) {
            System.out.println("Error!");
       }
       
       try {                
           images[1] = ImageIO.read(getClass().getResource("wall.jpg"));
       } catch (IOException ex) {
            System.out.println("Error!");
       }
       
       try {                
           images[2] = ImageIO.read(getClass().getResource("enemy.jpg"));
       } catch (IOException ex) {
            System.out.println("Error!");
       }
       
       try {                
           images[3] = ImageIO.read(getClass().getResource("player.jpg"));
       } catch (IOException ex) {
            System.out.println("Error!");
       }
    }
    
    /**
     * show the gui
     */
    public void makeVisible() {
        this.setVisible(true);
    }
    
    /**
     * hide the gui
     */
    public void makeNotVisible() {
        this.setVisible(false);
    }
    
    /**
     * @param text the new Text for the extraText label
     */
    public void setExtraText(String text) {
        extraText.setText(text);
    }
    
    /**
     * Properly dsiplay the dashRechargeBar according to the current dashRechargeValue
     * @aram value the current dashRechargeValue
     */
    public void setDashRechargeValue(int value) {
        if(value ==0) {
            dashRechargeBar.setValue(0);
            dashRechargeBar.setString("0%");
        } else if(value == 1) {
            dashRechargeBar.setValue(33);
            dashRechargeBar.setString("33%");
        } else if(value == 2) {
            dashRechargeBar.setValue(66);
            dashRechargeBar.setString("66%");
        } else if(value == 3) {
            dashRechargeBar.setValue(100);
            dashRechargeBar.setString("Dash Ready!");
        }
    }
}
