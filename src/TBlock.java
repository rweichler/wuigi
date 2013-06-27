



import java.awt.*;
import java.awt.image.*;

public class TBlock extends TGridded{

	public static final int WIDTH = 32;
	private double oldY,oldX;
	private byte hit;
	private byte image;
	private TItem item;
	private boolean spawning;
	private boolean movesWhenHit;
	
	public static final String BLOCK_PATH = "images/sprites/block/";
	
	private Sprite[] BLOCK = {
			new Sprite(BLOCK_PATH+"brick.png"),
			new Sprite(BLOCK_PATH+"q_activated.gif"),
			new Sprite(BLOCK_PATH+"q_deactivated.gif"),
			new Sprite(BLOCK_PATH+"floor.png"),
			new Sprite(BLOCK_PATH+"step.png"),
			new Sprite(BLOCK_PATH+"shroom/left.png"),
			new Sprite(BLOCK_PATH+"shroom/mid.png"),
			new Sprite(BLOCK_PATH+"shroom/right.png"),
			new Sprite(BLOCK_PATH+"shroom/top.png"),
			new Sprite(BLOCK_PATH+"shroom/bottom.png"),
	};
	
	public static final byte
		BRICK_BROWN = 0,
		BRICK = 0,
		QUESTION_BLOCK = 1,
		QUESTION_BLOCK_DEACTIVATED = 2,
		FLOOR = 3,
		STEP = 4,
		SHROOM_LEFT = 5,
		SHROOM_MID = 6,
		SHROOM_RIGHT = 7,
		SHROOM_TOP = 8,
		SHROOM_BOTTOM = 9;

	private static final Color[] aboveGround = {
		new Color(200,76,12),
		new Color(252,188,176)
	};	
	private static final Color[] underGround = {
		new Color(0,128,135),
		new Color(149,252,240)
		
	};
	
	public TBlock(double x, double y, byte image){
		this(x,y,image,null,false);
	}
	
	public TBlock(double x, double y, byte image, TItem item){
		this(x,y,image,item,true);
	}
	
	public TBlock(byte image){
		this(0,0,image,null,false);
	}
	
	public TBlock(){
		this(STEP);
	}
	/**
	 * Creates a TBlock with the specified x and y coordinates, image, contained item, and whether or not if it moves when it's hit.
	 * @param x
	 * @param y
	 * @param image
	 * @param item
	 * @param movesWhenHit
	 */
	public TBlock(double x, double y, byte image, TItem item, boolean movesWhenHit){
		super(x,y,WIDTH,WIDTH);
		this.movesWhenHit = movesWhenHit;
		init();
		spawning = false;
		hit = FROM_NONE;
		this.image = image;
		this.item = item;
	}
	
	public void makeSpriteUnderground(){
		for(Sprite s: BLOCK){
			s.replaceColors(aboveGround,underGround);
		}
		//System.out.println(BLOCK[image]);
	}
	
	public void init(){
		super.init();
		oldX = pos.x;
		oldY = pos.y;
	}
	public void init(Serializer s){
		image = (byte)s.ints[super.numInts()];
		movesWhenHit = s.bools[super.numBools()];
		Class c = s.classes[super.numClasses()];
		if(c != null){
			try {
				item = (TItem)(c.newInstance());
			} catch (Exception e) {
				item = null;
				e.printStackTrace();
			}
		}
		super.init(s);
	}
	public Serializer serialize(){
		Serializer s = super.serialize();
		s.ints[super.numInts()] = image;
		s.bools[super.numBools()] = movesWhenHit;
		if(item != null){
			s.classes[super.numClasses()] = item.getClass();
		}
		return s;
	}
	public int numInts(){return super.numInts() + 1;}
	public int numBools(){return super.numBools() + 1;}
	public int numClasses(){return super.numClasses() + 1;}
	
	/*public void setPos(double x, double y){
		super.setPos(x,y);
		oldX = x;
		oldY = y;
		System.out.println(oldX);
	}*/
	
	public BufferedImage preview(){
		return BLOCK[image].getBuffer();
	}

	public void onTouch(Thing t){
		if(!t.isStatic() && movesWhenHit && vel.x == 0 && vel.y == 0 && hit == FROM_NONE && !spawning){
			
			//System.out.println(pos.x + " " + oldX);
			
			byte where = fromWhere(t);
			
			if(where == FROM_BELOW && t instanceof Hero){
				hit(where);
				vel.y = 3;
			}else{
				if( (where == FROM_LEFT || where == FROM_RIGHT) && t.isFast() ){
					hit(where);
					if(where == FROM_LEFT){
						vel.x = 3;
					}else{
						vel.x = -3;
					}
				}
			}
		}
		super.onTouch(t);
	}
	/**
	 * Called when a player or a game object hits the block (for spawning TItems)
	 * @param where
	 * Where it hit.
	 */
	public void hit(byte where){
		hit = where;
		//oldX = pos.x;
		//oldY = pos.y;
	}
	
	private void spawnItem(){
		addSpawn(item);
		item = null;
		spawning = false;
		movesWhenHit = false;
	}
	
	private void beginSpawn(){
		if(item == null)return;
		spawning = true;
		item.setPos(pos);
		image = QUESTION_BLOCK_DEACTIVATED;
	}
	
	
	public void think(){
		super.think();
		
		if(spawning){
			item.think();
			//System.out.println(item.pos.y + " > " + pos.y + " + " + height);
			if(item.pos.y > pos.y + height){
				item.onBlockExit();
				spawnItem();
			}
		}
		
		if(pos.y > oldY + WIDTH/2 && hit == FROM_BELOW && vel.y > 0){
			vel.y = -3;
		}else if(pos.y < oldY && hit == FROM_BELOW && vel.y < 0){
			vel.y = 0;
			hit = FROM_NONE;
			pos.y = oldY;
			beginSpawn();
		}else if(pos.x > oldX + WIDTH/2 && hit == FROM_LEFT && vel.x > 0){
			vel.x = -3;
		}else if(pos.x < oldX && hit == FROM_LEFT && vel.x < 0){
			vel.x = 0;
			hit = FROM_NONE;
			pos.x = oldX;
			beginSpawn();
		}else if(pos.x < oldX - WIDTH/2 && hit == FROM_RIGHT && vel.x < 0){
			vel.x = 3;
		}else if(pos.x > oldX && hit == FROM_RIGHT && vel.x > 0){
			vel.x = 0;
			hit = FROM_NONE;
			pos.x = oldX;
			beginSpawn();
		}
	}
	
	public boolean isStatic(){
		return !movesWhenHit;
	}
	
	/**
	 * determines if this can contain a new TItem
	 * @return true if this can contain a new TItem, false if not
	 */
	public boolean canAcceptItem(){
		return item == null && (movesWhenHit || image == TBlock.QUESTION_BLOCK_DEACTIVATED);
	}
	
	/**
	 * makes this contain a new Item
	 * @param item the TItem to contain
	 */
	public void addItem(TItem item){
		if(image == QUESTION_BLOCK_DEACTIVATED){
			image = QUESTION_BLOCK;
			movesWhenHit = true;
		}
		this.item = item;
	}	
	
	public BufferedImage figureOutDrawImage(){
		return BLOCK[image].getBuffer();
	}
	
	public void draw(Graphics g, ImageObserver o, Hero hero){
		if(inPlayerView(hero) && spawning)
			item.draw(g,o,hero);
		super.draw(g,o,hero);
	}

}