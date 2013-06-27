



import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;



/**
 * The most important feature of the Level Editor. This holds all of the spawnable Things in the game and has the ability to add them to the game.
 * @author Reed Weichler
 *
 */
public class SpawnScreen extends Screen {
	
	private boolean visible;
	private int chosenThing;
	private Hero hero;
	private int hoveredThing;
	private boolean toggleLevel, toggleFreeze;
	
	private TextButton changeLevel, freezeTime;
	
	private static final Color  YELLOW = new Color(255,255,0,120),
								GREEN = new Color(0,255,0,120),
								RED = new Color(255,0,0,120);
	
	private Thing[] things = {
		new TGoomba(0, 0),
		new TKoopa(0,0),
		new TPirhana(),
		new TPipe(0,0),
		new TBlock(0,0,TBlock.BRICK_BROWN,null),
		new TBlock(TBlock.FLOOR),
		new TBlock(TBlock.STEP),
		new TBlock(TBlock.QUESTION_BLOCK_DEACTIVATED),
		null,
		null,
		new TBlock(TBlock.SHROOM_LEFT),
		new TBlock(TBlock.SHROOM_MID),
		new TBlock(TBlock.SHROOM_RIGHT),
		new TBGBlock(TBlock.SHROOM_TOP),
		new TBGBlock(TBlock.SHROOM_BOTTOM),
		null,
		null,
		null,
		null,
		null,
		new TStar(),
		new TCape(),
		new TMetalCap(),
		new TAWP(),
		null,
		null,
		null,
		null,
		null,
		null,
		new TSpawn(),
		new TGoal(),
		new THorizontalBound(),
		new TVerticalBound(),
		new TLinker(),
		new TRemover(),
		new TColoredBlock(),
		new GroundHole(),
	};
	
	public SpawnScreen(Hero hero){
		visible = false;
		toggleLevel = false;
		chosenThing = 0;
		hoveredThing = -1;
		changeLevel = new TextButton("TOGGLE UNDERGROUND", Wuigi.FONT_PLAIN, 160, 160 + 48*(things.length - 1 + 10)/10);
		freezeTime = new TextButton("TOGGLE TIME FREEZE", Wuigi.FONT_PLAIN, 160, 160 + 48*(things.length - 1 + 10)/10 + 20 + changeLevel.getHeight());
		this.hero = hero;
	}

	public void draw(Graphics g) {
		if(visible){
			((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
			for(int i = 0; i < things.length; i++){
				if(things[i] == null)continue;
				BufferedImage img = things[i].preview();
				int x = 160+(i%10)*48;
				int y = 160+(i/10)*48;
				if(i == hoveredThing)
					((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
				int width = 32, height = 32*img.getHeight()/img.getWidth();
				if(i == chosenThing){
					x -= width/2;
					y -= height/2;
					width *= 2;
					height *= 2;
				}
				
				g.drawImage(img,x,y,width,height,null);
				if(i == hoveredThing)
					((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
			}
		}else if(chosenThing != -1){
		
			Thing t = things[chosenThing];
			//makes the graphics object draw opaquely
			((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
			t.draw((Graphics2D)g,null,hero);
		}
		//set it back to normal
		((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		if(visible){
			changeLevel.draw(g);
			freezeTime.draw(g);
		}
	}
	/**
	 * returns true if this can be seen on-screen
	 * @return
	 */
	public boolean isVisible(){
		return visible;
	}

	public void key(KeyEvent e, boolean down) {
		int code = e.getKeyCode();
		switch(code){
			//make menu visible on these buttons
			case KeyEvent.VK_Q:
			case KeyEvent.VK_SHIFT:
			case KeyEvent.VK_TAB:
			case KeyEvent.VK_ALT:
			case KeyEvent.VK_CONTROL:
				visible = down;
			break;
		}
		if(down){
			switch(code){
				//convenience shortcut buttons
				case KeyEvent.VK_BACK_SPACE:
				case KeyEvent.VK_R:
					chosenThing =25; //remover index
				break;
				case KeyEvent.VK_G:
					chosenThing = 0; //goomba index
				break;
				case KeyEvent.VK_K:
					chosenThing = 1; //koopa index
				break;
				case KeyEvent.VK_P:
					chosenThing = 3; //pipe index
				break;
				case KeyEvent.VK_B:
					
					if(chosenThing < 4 || chosenThing > 6)
						chosenThing = 4; //q block index
					else
						chosenThing++;
				break;
				case KeyEvent.VK_C:
					if(chosenThing == 26){
						TColoredBlock.cycleColors();
						things[chosenThing].init();
					}else{
						chosenThing = 26;
					}
				break;
			}
		}
	}
	/**
	 * returns true if this should toggle whether or not all the Things in the game should stop moving
	 * @return
	 */
	public boolean shouldToggleFreeze(){
		boolean temp = toggleFreeze;
		toggleFreeze = false;
		return temp;
	}
	
	/**
	 * returns true if this should change the landscape
	 * @return
	 */
	public boolean shouldToggleLevel(){
		boolean temp = toggleLevel;
		toggleLevel = false;
		return temp;
	}
	
	
	/**
	 * returns the color t should be highlighted (used for tools)
	 * @param t the thing that is checked against
	 * @return a color if <b>t</b> should be highlighted, null if it shouldn't
	 */
	public Color highlightColor(Thing t){
		Thing chosen = things[chosenThing];
		if(!(chosen.touching(t) && t.touching(chosen))){
			return null;
		}
		if(chosen instanceof TRemover){
			return RED;
		}else if(chosen instanceof TLinker){
			TLinker linker = (TLinker)chosen;
			if(t.canLink(linker.getLink())){
				return YELLOW;
			}
		}else if(chosen instanceof TPirhana){
			if(t instanceof TPipe && ((TPipe)t).getPirhana() == null){
				return YELLOW;
			}
		}else if(chosen instanceof TItem && t instanceof TBlock && ((TBlock)t).canAcceptItem()){
			return GREEN;
		}
		return null;
	}
	
	/**
	 * gets the Thing that should be spawned and returns it. The returned Thing is removed from the SpawnScreen and new instance of it is created to take its place.
	 * @return the Thing to be spawned
	 */
	public Thing getSpawn(){
		if(visible)return null;
		Thing temp = things[chosenThing];
		if(temp instanceof TLinker && ((TLinker)temp).getLink() == null){
			//do not make a new one.
		//	((TLinker)temp).makeInWorld();
		}else{
			try {
				things[chosenThing] = temp.getClass().newInstance();
			} catch (Exception e) {
				things[chosenThing] = null;
				e.printStackTrace();
			}
		}	
		things[chosenThing].init(temp.serialize());
		return temp;
	}
	/**
	 * returns the Thing that is selected in the SpawnScreen
	 * @return the Thing that is selected in the SpawnScreen
	 */
	public Thing peekSpawn(){
		return things[chosenThing];
	}

	public void mouse(MouseEvent e, boolean down) {
		int x = e.getX(), y = e.getY();
		if(down && visible){
			int temp = getIndex(x, y);
			if(temp == chosenThing){
				if(things[chosenThing] instanceof TColoredBlock){
					TColoredBlock.cycleDirections();
					things[chosenThing].init();
				}
			}else if(temp != -1){
				chosenThing = temp;
			}
			if(changeLevel.contains(x,y)){
				toggleLevel = true;
			}else if(freezeTime.contains(x,y)){
				toggleFreeze =  true;
			}
		}
	}

	public void think() {
		Thing t = things[chosenThing];
		t.setSpawnPos(ScreenManager.mouse.x +hero.pos.x - (Wuigi.screenWidth/2.0 + hero.xOffset()),Wuigi.screenHeight + hero.pos.y + 32 - (ScreenManager.mouse.y + hero.yOffset()));
		t.vel.x = 0;
		t.vel.y = 0;
		t.acc.x = 0;
		t.acc.y = 0;
		t.think();
		if(!visible)return;
		hoveredThing = getIndex(ScreenManager.mouse.x, ScreenManager.mouse.y);
	}
	private int getIndex(int x, int y){
		for(int i = 0; i < things.length; i++){
			if(things[i] == null)continue;
			int tx = 160+(i%10)*48;
			int ty = 160+(i/10)*48;
			Rectangle rect = new Rectangle(tx,ty,32,32);
			if(rect.contains(x,y)){
				return i;
			}
		}
		return -1;
	}

}
