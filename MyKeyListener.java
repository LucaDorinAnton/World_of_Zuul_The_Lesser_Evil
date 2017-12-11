import javax.swing.*;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.event.*;
/**
 * "MyKeyListener" is part of the "World of Zuul: The Lesser Evil" game.
 * 
 * The MyKeyListener servers 2 purposes:
 * it stores all the contents of a GamePlayGUI object and
 * it transmits key press events to the GamePlay object
 * 
 * It uses an implementation of the interafce KeyListener to accomplish this last purpose
 *
 * @author Anton Luca-Dorin
 * @version 2017.12.07
 */
public class MyKeyListener extends JPanel implements KeyListener
{
    private GamePlay gamePlay;
    
    /**
     * @param gamePlay the gamePlay Object assigne to this KeyListener
     */
    public void setGamePlay(GamePlay gamePlay) {
            this.gamePlay = gamePlay;
    }
    
    /**
     *  This methods triggers when the user types a key on his keyboard
     *  @param e the KeyEvent which was triggered
     */
    public void keyTyped(KeyEvent e) {
        // This method is not needed for the project
    }
    
    /**
     * This method triggers whenever the user presses a key on his keyboard
     * @param e the KeyEvent which was triggered
     */
    public void keyPressed(KeyEvent e) {
        // This method is not needed for the project
    }
    
    /**
     * This method triggers whenever the user releases a key on his keyboard
     * @param e the KeyEvent which was triggered
     */
    public void keyReleased(KeyEvent e) {
        if(e.getKeyChar()=='W' || e.getKeyChar()=='w') {
            gamePlay.play("UP", e.isShiftDown(), e.isAltDown());
        }  else if(e.getKeyChar()=='S' || e.getKeyChar()=='s') {
            gamePlay.play("DOWN", e.isShiftDown(), e.isAltDown());
        } else if(e.getKeyChar()=='A' || e.getKeyChar()=='a') {
            gamePlay.play("LEFT", e.isShiftDown(), e.isAltDown());
        } else if(e.getKeyChar()=='D' || e.getKeyChar()=='d') {
            gamePlay.play("RIGHT", e.isShiftDown(), e.isAltDown());
        }
    }
}