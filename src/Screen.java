


import java.awt.*;
import java.awt.event.*;
/**
 * This represents an in-game Screen. Like a menu, or a pause screen, or the game itself.
 * These are switches to and from eachother mainly in the ScreenManager, but Screens can
 * contain and draw other Screens if the situation calls for it.
 * @author Reed Weichler
 *
 */
public abstract class Screen { 
	/**
	 * the ScreenController that controls the parent ScreenManager
	 */
	public ScreenController controller;
	/**
	 * Draws this screen to g
	 * @param g Graphics to be drawn to
	 */
	public abstract void draw(Graphics g);
	/**
	 * called when a key is pressed or released
	 * @param e event from keyPressed/keyReleased
	 * @param down true if pressed down, false if unpressed
	 */
	public abstract void key(KeyEvent e, boolean down);
	/**
	 * called when a mouse button is pressed or released
	 * @param e event from mousePressed/mouseReleased
	 * @param down true if pressed down, false if unpressed
	 */
	public abstract void mouse(MouseEvent e, boolean down);
	/**
	 * called every frame
	 */
	public abstract void think();
}
