/**
 * David Shannon
 * TCSS 305 -- Assignment 6a, Tetris
 * Spring 2018
 */

package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 * Class for the GUI.
 * 
 * @author davidshannon
 * @version 1
 */
public class TetrisGUI implements PropertyChangeListener, ActionListener, KeyListener {
    
    /** Initial size of square, in pixels. */
    private static final int INIT_SQ_SIZE = 20;
    
    /** The width factor of the board. */
    private static final int WIDTH_FACTOR = 10;
    
    /** The height factor of the board. */
    private static final int HEIGHT_FACTOR = 20;
    
    /** Factor by which timer delay decreases.*/
    private static final double DELAY_FACTOR = 0.85;
    
    /** int used for index of menu item event. */
    private static final int NEW_GAME = 0;
    
    /** int used for index of menu item event. */
    private static final int END_GAME = 1;
    
    /** Int used for index of menu item event. */
    private static final int HELP = 2;
    
    /** String used for pause property change listener. */
    private static final String PAUSE = "pause";
    
    /** String used for unpause property change listener. */
    private static final String UNPAUSE = "unpause";
    
    /** String used for game over property change listener. */
    private static final String GAME_OVER = "game over";
    
   
    /** Initial delay of the timer. */
    private final int myInitialTimerDelay;
    
    /** Boolean that indicates whether or not game is paused. */
    private boolean myPaused;
    
    /** Support for firing property change events from this class. */
    private final PropertyChangeSupport myPCS = new PropertyChangeSupport(this);
    
    /** JFrame upon which game board is placed. */
    private final JFrame myJFrame;
    
    /** JPanel for representing Graphical Info. */
    private final GraphicalPanel myGraphicalPanel;
    
    /** JPanel for representing Game Board. */
    private final GamePanel myGamePanel;
    
    /** Timer used to control down movement of pieces. */
    private final Timer myTimer;
    
    /** JMenu used for holding JMenuItems. */
    private final JMenuBar myMenuBar;
    
    /** Array of menu items. */
    private final List<Object> myMenuArray;
    


    /** 
     * Constructor for the board/GUI. 
     * 
     * @param theDelay the time delay for the program's timer. 
     */
    public TetrisGUI(final int theDelay) {
        myJFrame = new JFrame("Tetris");
        myGamePanel = new GamePanel(INIT_SQ_SIZE);
        myGraphicalPanel = new GraphicalPanel(INIT_SQ_SIZE);
        myPaused = false;
        myInitialTimerDelay = theDelay;
        myTimer = new Timer(theDelay, this); 
        myMenuBar = new JMenuBar();
        myMenuArray = new ArrayList<Object>();
    }
    
    /** Method for building/starting the board. */
    protected void start() {

        myGamePanel.setPlaying(false); 
        myTimer.setRepeats(true);
        setUpJMenu();
        setUpJFrame();
   
    }
    
    /**
     * Private helper method for establishing JMenu.
     */
    private void setUpJMenu() {

        final JMenuItem newgame = new JMenuItem("New Game");
        final JMenuItem endgame = new JMenuItem("End Game");
        final JMenuItem help = new JMenuItem("Help");
        
        newgame.addActionListener(this);
        endgame.addActionListener(this);
        help.addActionListener(this);
        
        myMenuArray.add(newgame);
        myMenuArray.add(endgame);
        myMenuArray.add(help);
        
        myMenuBar.add(newgame);
        myMenuBar.add(endgame);
        myMenuBar.add(help);
        
        myMenuBar.setVisible(true);
        endgame.setEnabled(false);
                        
    }
    
    
    
    /**
     * Private helper method for establishing JFrame.
     */
    private void setUpJFrame() {

        myJFrame.add(myGamePanel, BorderLayout.CENTER);
        myJFrame.add(myGraphicalPanel, BorderLayout.EAST);
        myJFrame.setJMenuBar(myMenuBar);
        myJFrame.pack();
        myJFrame.setMinimumSize(new Dimension(INIT_SQ_SIZE * (WIDTH_FACTOR * 2 + 1),
                                              INIT_SQ_SIZE * HEIGHT_FACTOR));
        myJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myJFrame.setLocationRelativeTo(null); 
        myJFrame.setResizable(false);
        myJFrame.setVisible(true);
        
        this.addPropertyChangeListener(this);
        this.addPropertyChangeListener(myGamePanel);
        myGamePanel.addKeyListener(this);
        myGamePanel.getMyBoard().addPropertyChangeListener(this);
        myGamePanel.addPropertyChangeListener(this);
        myGamePanel.addPropertyChangeListener(myGamePanel);
        myGraphicalPanel.addPropertyChangeListener(this);
        myGamePanel.getMyBoard().newGame(); // starts the game        
        myGraphicalPanel.setVisible(false);

    }
    
    
    

    @Override
    public void actionPerformed(final ActionEvent theEvent) {
        final Object source = theEvent.getSource();
        
        if (source.equals(myTimer)) {
            myGamePanel.getMyBoard().down();
            myGraphicalPanel.updateScore();
            
        //if "HELP" pressed
        } else if (source.toString().equals(myMenuArray.get(HELP).toString()))  { 
            
            myPCS.firePropertyChange(PAUSE, false, true);
            JOptionPane.showMessageDialog(null, "  CONTROLS FOR THE GAME:\n"
                            + "Move left: left arrow and 'a'\n"
                            + "Move right: right arrow and 'd'\n"
                            + "Rotate: up arrow and 'w'\n"
                            + "Move Down: down arrow and 's'\nDrop: space\n "
                            + "      ------------------"
                            + "\n Press 'p' to pause, 'u' to unpause\n"
                            + "       ------------------\n SCORING ALGORITHM: \n"
                            + "Drop = 10\n1 Line cleared = 100\n2 Lines cleared = 200\n"
                            + "3 Lines cleared = 400\n4 Lines cleared = 800", 
                            "Game Controls", JOptionPane.PLAIN_MESSAGE);  
            myPCS.firePropertyChange(UNPAUSE, false, true);
            
        //if "NEW GAME" pressed
        }  else if (source.toString().equals(myMenuArray.get(NEW_GAME).toString())) { 
            ((Component) myMenuArray.get(1)).setEnabled(true);  //enables endgame
            ((Component) myMenuArray.get(0)).setEnabled(false); //disables newgame
            myGamePanel.getMyBoard().addPropertyChangeListener(myGraphicalPanel);
            myGamePanel.getMyBoard().addPropertyChangeListener(myGamePanel);
            myGraphicalPanel.setVisible(true);
            myGamePanel.getMyBoard().newGame(); // starts the game
            myGamePanel.setPlaying(true);
            myGraphicalPanel.resetScores();
            myTimer.setDelay(myInitialTimerDelay);
            myTimer.start();                    // starts the timer
            
        //if "END GAME" pressed
        } else if (source.toString().equals(myMenuArray.get(END_GAME).toString())) { 
            myPCS.firePropertyChange(GAME_OVER, false, true);
        }
    }
    
    
    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        final String propertyName = theEvent.getPropertyName();
        if (GAME_OVER.equals(propertyName)) {
            myTimer.stop();
            myGamePanel.setPlaying(false);
            JOptionPane.showMessageDialog(null, "Game Over!", "well done...",
                                          JOptionPane.INFORMATION_MESSAGE);
            ((Component) myMenuArray.get(0)).setEnabled(true);     //enables newgame
            ((Component) myMenuArray.get(1)).setEnabled(false);    //disables endgame  
        } else if ("level up".equals(propertyName)) {
            myTimer.setDelay((int) (myTimer.getDelay() * DELAY_FACTOR));
            myJFrame.repaint();
        } else if (PAUSE.equals(propertyName)) {
            myTimer.stop();
            if (myGamePanel.isPlaying()) {
                myPaused = true;
            }
            myGamePanel.setPlaying(false);
        } else if (UNPAUSE.equals(propertyName) && myPaused) {
            myGamePanel.setPlaying(true);
            myTimer.start();
            myPaused = false;
        }
    }
    
    @Override
    public void keyPressed(final KeyEvent theEvent) {
        final int key = theEvent.getKeyCode();
        
        if (key == KeyEvent.VK_P && myGamePanel.isPlaying()) {
            myPCS.firePropertyChange(PAUSE, false, true);
        } else if (key == KeyEvent.VK_U && (myGamePanel.isPlaying() || myPaused)) {
            myPCS.firePropertyChange(UNPAUSE, false, true);
        //the following moved to this class to deal w/ cyclomatic complexity in GamePanel class
        } else if (key == KeyEvent.VK_SPACE && myGamePanel.isPlaying()) {
            myGamePanel.getMyBoard().drop();    
        }
        
    }
    
    @Override
    public void keyTyped(final KeyEvent theEvent) { }

    @Override
    public void keyReleased(final KeyEvent theEvent) { }
    
    
    
    // methods required for PropertyChange support

    /**
     * Adds a listener for property change events from this class.
     * 
     * @param theListener a PropertyChangeListener to add.
     */
    public void addPropertyChangeListener(final PropertyChangeListener theListener) {
        myPCS.addPropertyChangeListener(theListener);
    }
    
    /**
     * Removes a listener for property change events from this class.
     * 
     * @param theListener a PropertyChangeListener to remove.
     */
    public void removePropertyChangeListener(final PropertyChangeListener theListener) {
        myPCS.removePropertyChangeListener(theListener);
    }
    
   
}
