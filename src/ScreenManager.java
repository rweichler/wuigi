


import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;

/**
 * This class holds all the Screens to be displayed in the game and also switches to/from them when necessary. When creating a new screen it sets it's ScreenController to the same as this one, so to change anything in ScreenManager just call the methods in the ScreenController and it should work.
 * @author Reed Weichler
 *
 */
public class ScreenManager implements MouseListener, MouseMotionListener,KeyListener{
	
	private GameScreen game;
	private Screen menu;
	
	private boolean paused;
	private ScreenController controller;
	
	/**
	 * position of mouse on the screen
	 */
	public static Point mouse = new Point();
	
	/*public static Server server;
	public static Client client;
	
	public static Connector connection;*/

	private ArrayList<Integer> pressedKeys;
	
	
	private boolean thinked = true;
	
	/**
	 * creates a new ScreenManager with a ScreenController with a FileOpener as specified
	 * @param opener the FileOpener that will be put inside the created ScreenController
	 */
	public ScreenManager(FileOpener opener){
		pressedKeys = new ArrayList<Integer>();
		controller = new ScreenController(this,opener);
		renew();
	}
	
	/**
	 * refreshes everything to where it's like you just opened the program
	 */
	public void renew(){
		game = null;
		menu = new MainScreen();
		menu.controller = controller;
		paused = true;
		//setBackground(Color.BLACK);
		//topLeftBox = new Point(Global.W/2 - 50,Global.H -Global.GROUND_LEVEL - 100);
		//wBox = 32;
		//hBox = 100;
		////bgMusic = new AePlayWaveLoop("sound/original_theme.wav");
		////bgMusic.start();
		//main = new MainClass(this.getFontMetrics(CHAT_BOLD));
		//inGame = false;
		//pw = password.toCharArray();
		//nextChar = 0;
		//updateBox();
		//for(int i = 0; i < MARIO_COLORS.length; i++){
		//	Hero h = new Hero();
		//	h.setSpriteColor(i);
		//	MARIO_COLORS[i] = (h.IMAGE[0]).getBuffer();
		//}
	}
	
	/**
	 * corrects syncronization with drawing and thinking
	 * @return true if did think, false if not
	 */
	public boolean didThink(){
		return thinked;
	}
	
	/**
	 * draws the menu Screen and game GameScreen
	 * @param g the Graphics to be drawn to
	 */
	public void draw(Graphics g){
		//System.out.println("Draw");
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		

		if(!paused && game != null){
			game.draw(g);
		}else{
			if(menu instanceof PauseScreen)
				game.draw(g);
			menu.draw(g);
		}
		thinked = false;
		/*if(inGame){
			main.draw(g, this);
			if(connected){
				g.setFont(CHAT_BOLD);
				g.setColor(Color.WHITE);
				if(typing)
					g.drawString("You: " + typedString, 5,getHeight()-6);
				else
					g.drawString("Press ~ to chat!", 5,getHeight()-6);
			}
		}*/
	}
	
	/**
	 * starts level editor mode (new file)
	 * @param marioColor the color the Hero should be
	 */
	public void levelEditor(int marioColor){
		game = new LevelEditor();
		menu = new PauseScreen(true);
		game.controller = controller;
		menu.controller = controller;
		game.init(marioColor);
		
	}
	
	/**
	 * starts level editor mode (open file)
	 * @param marioColor the color the Hero should be
	 * @param f the file to be opened which contains the level data
	 */
	public void levelEditor(int marioColor, File f){
		game = new LevelEditor();
		boolean init;
		try{
			init = game.init(marioColor, f);
		}catch(Exception ex){
			if(menu instanceof MainScreen)
				((MainScreen)menu).loadFailed();
			game = null;
			return;
		}
		if(!init){
			game = null;
			return;
		}
		paused = false;
		menu = new PauseScreen(true);
		game.controller = controller;
		menu.controller = controller;
		
	}
	
	/**
	 * saves the level, if any to file
	 * @param f the file to be written to
	 * @return true if successfully written, false if not
	 */
	public boolean saveGame(File f){
		if(game != null){
			return game.saveGame(f);
		}
		return false;
	}
	

	/**
	 * starts single player mode
	 * @param marioColor the color the Hero should be
	 * @param f the file to be opened and read
	 */
	public void singlePlayer(int marioColor, File f){
		if(game == null || !(game instanceof SinglePlayer))
			game = new SinglePlayer();
		boolean init;
		game.loading = true;
		try{
			init = game.init(marioColor, f);
		}catch(Exception ex){
			if(menu instanceof MainScreen)
				((MainScreen)menu).loadFailed();
			game = null;
			return;
		}
		if(!init){
			game = null;
			return;
		}
		paused = false;
		menu = new PauseScreen(false);
		game.controller = controller;
		menu.controller = controller;
	}
	
	/**
	 * called every frame, updates think in the GameScreen and menu Screen.
	 */
	public void think(){
		//System.out.println("Think");
		if(!paused){
			game.think();
		}else{
			menu.think();
		}
		thinked = true;
	}
	
	/**
	 * pauses/unpauses the game, if one is in session
	 * @param pause if true, the game pauses. if false, it unpauses
	 */
	public void pause(boolean pause){
		paused = pause;
	}
	
	/**
	 * called when a key is pressed
	 * @param event the KeyEvent from keyPressed/keyReleased
	 * @param pressed is true if the key was pressed down, is false if not
	 */
	public void key(KeyEvent event, boolean pressed){
		if(!paused){
			game.key(event, pressed);
		}else{
			menu.key(event, pressed);
		}
	}

	/**
	 * called when a mouse button is pressed
	 * @param event the MouseEvent from mousePressed/mouseReleased
	 * @param pressed is true if the mouse was pressed down, is false if not
	 */
	public void mouse(MouseEvent event, boolean pressed){
		if(paused){
			menu.mouse(event, pressed);
		}else{
			game.mouse(event,pressed);
		}
	}
	
	public void keyPressed(KeyEvent event){
		int code = event.getKeyCode();
		if(pressedKeys.size() != 0 && pressedKeys.get(pressedKeys.size()-1).equals(code) || pressedKeys.indexOf(code) != -1)
			return;
		pressedKeys.add(code);
		key(event,true);
	}
	public void keyReleased(KeyEvent event){
		int code = event.getKeyCode();
		int index = pressedKeys.indexOf(code);
		if(index != -1)
			pressedKeys.remove(index);
		key(event,false);
	}
	
	/*private int findKey(int value, int low, int high) {
		if (high < low)
			return -1;
		if(low == high){
			if(pressedKeys.get(low) == value){
				return low;
			}else{
				return -1;
			}
		}
		int mid = low + (high - low) / 2;
		if (pressedKeys.get(mid) > value)
			return findKey(value, low, mid-1);
		else if (pressedKeys.get(mid) < value)
			return findKey(value, mid+1, high);
		return mid; // found
	}*/
	
	public void keyTyped(KeyEvent event){}
	
	public void mouseClicked(MouseEvent event){}
	public void mousePressed(MouseEvent event){
		mouse(event,true);
	}
	public void mouseReleased(MouseEvent event){
		mouse(event,false);
	}
	public void mouseExited(MouseEvent event){}
	public void mouseEntered(MouseEvent event){}
	
	public void mouseMoved(MouseEvent event){
		mouse.setLocation(event.getX(),event.getY());	
	}
	public void mouseDragged(MouseEvent event){
		mouseMoved(event);
	}
	
}