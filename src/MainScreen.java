


import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;





public class MainScreen extends Screen {
	
	///private boolean choosingMultiplayer = false;
	
	private Room room;
	
	//private String ip = null; //ip you're trying to connect to. when null, you're not trying to connect to a server, when initialized, this means you're trying to connect to a server.
	private int selectedMario;
	
	private final BufferedImage[] MARIO_COLORS = {
		null,
		null,
		null,
		null,
		null,
		null
	};
	
	private TextButton	editorButton,
						newGame,
						loadGame,
						singleButton,
						//multiButton,
						title,
						couldntLoad,
						quit;
	
	private static final int SPACE_FROM_TOP = 150, SPACE_BETWEEN = 2;
	
	private static final Font FONT_TITLE = new Font("Courier", Font.PLAIN, 100);
	
	private boolean editorSelected, loadFailed;
	
	public MainScreen(){
		editorSelected = false;
		loadFailed = false;
		for(int i = 0; i < MARIO_COLORS.length; i++){
			Hero h = new Hero();
			h.setSpriteColor(i);
			MARIO_COLORS[i] = (h.IMAGE[0]).getBuffer();
		}
		selectedMario = (int)(Math.random()*6);
		boolean underground = Math.random() > 0.5;
		room = new Room(underground, -1);
		room.add(new TGoomba(500,0,32,32));
		TKoopa koopa = new TKoopa(3000,0);
		room.add(koopa);
		koopa.makeShell(true);
		koopa.vel.x = -TKoopa.SHELL_SPEED;
		room.add(new TBlock(32*5,32*4, TBlock.QUESTION_BLOCK));
		room.add(new TBlock(32*4,32*2, TBlock.BRICK));
		room.add(new TBlock(32*3,32*2, TBlock.BRICK));
		room.add(new TBlock(32*5,32*2, TBlock.BRICK));
		room.add(new TBlock(32*6,32*2, TBlock.QUESTION_BLOCK_DEACTIVATED));
		room.add(new TBlock(32*7,32*2, TBlock.BRICK));
		TPipe pipe = new TPipe();
		pipe.setPos(-32*7, 32*5);
		TPirhana pirhana = new TPirhana();
		room.add(pipe);
		room.add(pirhana);
		pipe.addPirhana(pirhana);
		room.add(new TBlock(-32*5, 0, TBlock.BRICK, null, true));
		
		newGame = new TextButton("NEW", Wuigi.FONT_BIG);
		loadGame = new TextButton("LOAD", Wuigi.FONT_BIG);
		editorButton = new TextButton("LEVEL EDITOR", Wuigi.FONT_BIG);
		singleButton = new TextButton("SINGLEPLAYER", Wuigi.FONT_BIG);
		couldntLoad = new TextButton("LOAD FAILED", Wuigi.FONT_BIG, TextButton.TITLE);
		//multiButton = new TextRect("ONLINE", Wuigi.FONT_BIG);
		title = new TextButton("WUIGI", FONT_TITLE, Color.WHITE);
		quit = new TextButton("QUIT", Wuigi.FONT_BIG);
		int height = editorButton.getHeight();
		
		title.setPos(0, SPACE_FROM_TOP - title.getHeight() - 50);
		editorButton.setPos(0, SPACE_FROM_TOP);
		newGame.setPos(0, SPACE_FROM_TOP);
		loadGame.setPos(newGame.getWidth() + SPACE_BETWEEN*20, SPACE_FROM_TOP);
		singleButton.setPos(0, SPACE_FROM_TOP + height + SPACE_BETWEEN);
		//multiButton.setPos(0, SPACE_FROM_TOP + (height + SPACE_BETWEEN)*2);
		couldntLoad.setPos(0,SPACE_FROM_TOP + (height + SPACE_BETWEEN) * 2);
		quit.setPos(0, SPACE_FROM_TOP + (height + SPACE_BETWEEN)*4);
	}
	
	public void loadFailed(){
		loadFailed = true;
		editorSelected = false;
	}
	
	public void draw(Graphics g) {
		room.draw(g,null,null);
    	
    	title.draw(g);
    	
		if(!editorSelected){
	    	editorButton.draw(g);
		}else{
			loadGame.draw(g);
			newGame.draw(g);
		}
		if(loadFailed){
			couldntLoad.draw(g);
		}
	    singleButton.draw(g);
	    //multiButton.draw(g);
	    quit.draw(g);
	    
	    //unused multiplayer code
	    /*g.setColor(Color.WHITE);
		g.setFont(Wuigi.FONT_BIG);
		if(connection != null && connection.disconnect){
				g.setColor(Color.RED);
				g.setFont(Wuigi.FONT_PLAIN);
				g.drawString("Lost connection", 5,360);
			}
		}else if(ip != null){ //you chose client
			g.setFont(Wuigi.FONT_BIG);
			g.setColor(Color.RED);
			g.drawString("Type the IP:", 5,340);
			g.setColor(Color.WHITE);
			g.drawString(ip, 5,380);
		}else{
			g.setFont(Wuigi.FONT_BIG);
			if(ScreenPanel.mouse.y > 300 && ScreenPanel.mouse.y < 340)
				g.setColor(Color.GREEN);
			g.drawString("Make a Call", 5,340);
			if(ScreenPanel.mouse.y > 340 && ScreenPanel.mouse.y < 380)
				g.setColor(Color.GREEN);
			else
				g.setColor(Color.WHITE);
			g.drawString("Wait for Call", 5,380);
		}
		g.setFont(Wuigi.FONT_BIG);
		if(connection != null && connection.waitingForConnect){
			g.setColor(Color.RED);
			g.drawString("Waiting for connection", 5,Wuigi.H-10);
		}*/
		((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
		for(int i = 0; i < 6; i++){
			//figure out coordinates of mario
			int x = i, y= 0;
			if(i > 2){
				x -= 3;
				y = 1;
			}

			if(i == selectedMario)
				((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
			//draw the mario itself
			g.drawImage(
				MARIO_COLORS[i],
				428 + x*128,
				100 + y*80,
				80,
				80,
				null//(java.awt.image.ImageObserver)this
			);

			if(i == selectedMario)
				((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
		}
		((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

	}

	public void key(KeyEvent e, boolean down) {
		int code = e.getKeyCode();
		//char c = e.getKeyChar();
		
		if(!down) return; //continue only if you're pressing the key down
		
		
		
		
		//if(ip == null){
		if(code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE || code == KeyEvent.VK_ESCAPE){
			controller.levelEditor(selectedMario);
		}
		return; //continue only if you're typing the IP of the server to connect to
		//}
		
		/*if(code == KeyEvent.VK_ENTER ){
			Connector connection = ScreenPanel.connection;
			connection = new Client(ip, 3456);
			connection.waitingForConnect = true;
			connection.start();
			return;
		}
		if(code == KeyEvent.VK_BACK_SPACE ){
			if(ip.length() > 0)
				ip = (new StringBuffer(ip).deleteCharAt(ip.length()-1)).toString();
			return;
		}
		if(c != KeyEvent.CHAR_UNDEFINED){
			ip += c;
			return;
		}*/

	}

	public void mouse(MouseEvent e, boolean down) {
		if(down)return;
		int x = e.getX(), y = e.getY();
		if(quit.contains(x,y)){
			System.exit(0);
		}
		if(!editorSelected){
			if(editorButton.contains(x,y)){
				//controller.loadGame(selectedMario);
				editorSelected = true;
			}
		}else{
			if(loadGame.contains(x,y)){
				loadFailed = false;
				controller.loadLevelEditor(selectedMario);
			}else if(newGame.contains(x,y)){
				controller.levelEditor(selectedMario);
			}
		}
		if(singleButton.contains(x,y)){
			loadFailed = false;
			controller.singlePlayer(selectedMario);
		}
		/*Connector connection = ScreenPanel.connection;
		if(multiButton.contains(x,y) && !choosingMultiplayer){
			choosingMultiplayer = true;
		}else if(choosingMultiplayer && connection == null && ip == null){
			if(y > 300 && y < 340){
				ip = ""; //this means that we chose client
			}else if(y > 340 && y < 380){
				connection = new Server(3456);
				connection.waitingForConnect = true;
				connection.start();
			}
		}*/
		
		for(int i = 0; i < 6; i++){
			int xpos = i, ypos= 0;
			if(i > 2){
				xpos -= 3;
				ypos = 1;
			}
			if(x > 428 + xpos*128 && x < 508 + xpos*128 &&
				y > 100 + ypos*80 && y < 180 + ypos*80){
				selectedMario = i;
				break;
			}
		}

	}

	public void think() {
		/*Connector connection = ScreenPanel.connection;
		if(connection != null && connection.waitingForConnect && !connection.connected){
			String m = connection.getLastMessage();
			if(m != null){
				if(m.equals("connect")){
					connection.connected = true;
					connection.waitingForConnect = false;
					controller.levelEditor(selectedMario);
				}else if(m.equals("disconnect")){
					connection.disconnect();
				}else{
					connection.message(m); //throw the message back in
				}
			}
		}*/
		room.think(null,true);

	}

}
