/**
 * David Shannon
 * TCSS 305 -- Assignment 6a, Tetris
 * Spring 2018
 */

package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.JPanel;
import model.Board;


/**
 * Class for the game panel. 
 * 
 * @author davidshannon
 * @version 1
 */
public class GamePanel extends JPanel implements PropertyChangeListener, KeyListener {

    /** Generated serial version ID. */
    private static final long serialVersionUID = 1860979716621182121L;
    
    /** The first index to be graphically displayed from myStrings. */
    private static final int START_IDX = 5;
    
    /** The width factor of the board. */
    private static final int WIDTH_FACTOR = 10;
    
    /** Boolean that indicates whether game is in process or not. */
    private boolean myPlaying;
    
    /** Support for firing property change events from this class. */
    private final PropertyChangeSupport myPCS = new PropertyChangeSupport(this);

    /** The size in pixels of a side of one "square" on the grid. */
    private int mySquareSize;
    
    /** Game Board to be placed on GamePanel. */
    private final Board myBoard;
    
    /** Width of the game board. */
    //private final int myWidth;
    
    /** Height of the game board. */
    //private final int myHeight;
    
    /** String representation of the board to be displayed. */
    private String myStringBoard;
    
    /** Array of each line from myStringBoard. */
    private String[] myStrings;
    
    
   
    /** 
     * Constructor for the Game Panel. 
     * 
     * @param theSquareSize size of each individual tetris block.
     */
    public GamePanel(final int theSquareSize) {
        super();
        mySquareSize = theSquareSize;
        myBoard = new Board();
        myPlaying = false;
        //myWidth = mySquareSize * WIDTH_FACTOR;
        //myHeight = mySquareSize * HEIGHT_FACTOR;
        
        
        setUpPanel();
    }
    
    /**
     * Method for changing size of square.
     * @param theSize The new size of a single block.
     */
    public void setSquareSize(final int theSize) {
        mySquareSize = theSize;
    }
    
    /**
     * Method for changing status of game.
     * @param thePlaying whether or not game is in play.
     */
    public void setPlaying(final boolean thePlaying) {
        myPlaying = thePlaying;
    }
    
    /**
     * Method for returning whether or not game is in play. 
     * @return isPlaying
     */
    public boolean isPlaying() {
        return myPlaying;
    }
    
    /**
     * Private helper method for setting up panel.
     */
    private void setUpPanel() {
        this.setFocusable(true);
        this.addKeyListener(this);
        //this.setPreferredSize(new Dimension(myWidth, myHeight));
        myBoard.addPropertyChangeListener(this);  
    }
    
    /**
     * Paints the game board. Triggered by repaint() method call.
     * 
     * @param theGraphics The graphics context to use for painting.
     */
    @Override
    public void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        final Graphics2D g2d = (Graphics2D) theGraphics;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        
        myStrings = myStringBoard.split("\n");

        for (int x = START_IDX; x < myStrings.length - 1; x++) {
            for (int y = 1; y < WIDTH_FACTOR + 1; y++) {
                final Rectangle2D.Double block =
                                new Rectangle2D.Double((double) (y - 1) * mySquareSize,
                                                       (double) (x - START_IDX) * mySquareSize,
                                                       (double) mySquareSize,
                                                       (double) mySquareSize);
                if (myStrings[x].charAt(y) == ' ') {
                    g2d.setColor(Color.BLACK);
                    g2d.fill(block);
                }  else {
                    g2d.setColor(getColor(myStrings[x].charAt(y)));
                    g2d.fill(block);
                    g2d.setColor(Color.white);
                    g2d.draw(block);
                }
            }
        }
    }

    /**
     * Method for returning myBoard.
     * @return myBoard
     */
    public Board getMyBoard() {
        return myBoard;
    }
    
    /**
     * Private method for retrieving color of each piece. *
     * @return the color of the block
     * @param theChar the block type
     */
    private Color getColor(final char theChar) {
        Color color = Color.WHITE;
        if (theChar == 'I') {
            color = Color.PINK;
        } else if (theChar == 'J') {
            color = Color.BLUE;
        } else if (theChar == 'L') {
            color = Color.ORANGE;
        } else if (theChar == 'O') { 
            color = Color.YELLOW;
        } else if (theChar == 'S') {
            color = Color.GREEN;
        } else if (theChar == 'T') {
            color = Color.CYAN;
        } else if (theChar == 'Z') {
            color = Color.RED;
        }
        
        return color;
        
    }


    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        final String propertyName = theEvent.getPropertyName();
        
        if ("piece moved".equals(propertyName)) {
            myStringBoard = (String) theEvent.getNewValue();
            repaint();
            //System.out.println("==========");
            //System.out.println(myStringBoard);
        //} //else if ("game over".equals(propertyName)) {
            //final JOptionPane gameover = new JOptionPane("Game Over!");
            //JOptionPane.showMessageDialog(null, "Game Over!", "well done...",
            //                              JOptionPane.INFORMATION_MESSAGE);
            //this.removeKeyListener(this);
            
        }     
    }


    @Override
    public void keyPressed(final KeyEvent theEvent) {
        
        final int key = theEvent.getKeyCode();
        if (myPlaying) {
            if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
                myBoard.left();
            } else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
                myBoard.right();
            } else if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
                myBoard.rotate();
            } else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
                myBoard.down();
            }
        }
//        } else if (key == KeyEvent.VK_SPACE) {
//            myBoard.drop();
//        } 
        
    }


    @Override
    public void keyReleased(final KeyEvent theEvent) {
    }


    @Override
    public void keyTyped(final KeyEvent theEvent) {
    }
    
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
