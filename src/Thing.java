import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
/**
 * This represents an object that can be put in the game.
 * @author Reed Weichler
 *
 */
public class Thing{

	public static final byte FROM_NONE = 1;

	public static final byte FROM_ABOVE = 2;

	public static final byte FROM_BELOW = 4;

	public static final byte FROM_LEFT = 8;

	public static final byte FROM_RIGHT = 16;
	
	public static final byte FROM_EVERYWHERE = FROM_ABOVE + FROM_BELOW + FROM_LEFT + FROM_RIGHT;
	
	public static final byte FROM_SIDE = FROM_LEFT + FROM_RIGHT;
	
	private Thing spawn = null;
	
	/**
	 * position
	 */
	public Point2D.Double pos;
	/**
	 * last position
	 */
	public Point2D.Double posLast;
	/**
	 * velocity
	 */
	public Point2D.Double vel;
	/**
	 * acceleration
	 */
	public Point2D.Double acc;
	/**
	 * if this is falling to its death because of a hole in the floor
	 */
	public boolean falling;
	
	private Sprite sprite;
	
	
	public int width,height;
	private boolean killed,dying;
	
	/**
	 * returns an array of values that is used to determine where this should be drawn on the screen
	 * @param hero
	 * @return {x coord, y coord, width, height}
	 */
	public int[] getDrawCoords(Hero hero){
		int[] r = new int[4];
		r[0] = Wuigi.scaleW(Wuigi.screenWidth-(hero.viewX()-pos.x));
		//r[1] = (int)(Global.H - Global.GROUND_LEVEL - (h + pos.y) + 0.5);
		r[1] = Wuigi.scaleH(hero.viewY()-pos.y-height);
		r[2] = Wuigi.scaleW(width);
		r[3] = Wuigi.scaleH(height);
		return r;
	}
	/**
	 * returns the in-game image
	 * @return in-game image
	 */
	public BufferedImage figureOutDrawImage(){
		return sprite.getBuffer();
	}
	/**
	 * draws this to g
	 * @param g
	 * @param o
	 * @param hero
	 */
	public void draw(Graphics g, ImageObserver o, Hero hero){
		if(inPlayerView(hero)){
			g.setColor(Color.WHITE);
			int[] c = getDrawCoords(hero);
			
	
			g.drawImage(figureOutDrawImage(),c[0],c[1],c[2],c[3], o);
		}
	}
	/**
	 * determines whether or not this is in the view of the Hero specified
	 * @param hero
	 * @return true if this can be seen on screen, false if not
	 */
	public boolean inPlayerView(Hero hero){
		return Wuigi.screenWidth-hero.viewX()+pos.x+width > 0 && Wuigi.screenWidth-hero.viewX()+pos.x < Wuigi.screenWidth
		&& hero.viewY()-pos.y > 0 && hero.viewY()-pos.y-height < Wuigi.screenHeight;
	}
	/**
	 * Creates a new Thing with coordinates (x,y) and the  specified width and height
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param width width
	 * @param height height
	 */
	public Thing(double x, double y, int width, int height){
		pos = new Point2D.Double(x,y);
		sprite = new Sprite(width,height);
		posLast = new Point2D.Double(x,y);
		this.width=width;
		this.height=height;
		killed = false;
		falling = false;
		init();
	}
	/**
	 * creates a new Thing with coordinates (0,0), width of 1 and height of 1
	 */
	public Thing(){
		this(0,0,1,1);
	}
	/**
	 * converts this to a Serializer so it can be written to a file
	 * @return
	 */
	public Serializer serialize(){
		Serializer s = new Serializer(this.getClass());
		
		s.ints = new int[numInts()];
		s.doubles = new double[numDoubles()];
		s.bools = new boolean[numBools()];
		s.classes = new Class[numClasses()];
		
		s.ints[0] = width;
		s.ints[1] = height;
		
		s.doubles[0] = pos.x;
		s.doubles[1] = pos.y;
		
		return s;
	}
	/**
	 * returns number of ints that this Serializer representation should contain
	 * @return number of ints that this Serializer representation should contain
	 */
	public int numInts(){return 2;}
	/**
	 * returns number of doubles that this Serializer representation should contain
	 * @return number of doubles that this Serializer representation should contain
	 */
	public int numDoubles(){return 2;}
	/**
	 * returns number of booleans that this Serializer representation should contain
	 * @return number of booleans that this Serializer representation should contain
	 */
	public int numBools(){return 0;}
	/**
	 * returns number of classes that this Serializer representation should contain
	 * @return number of classes that this Serializer representation should contain
	 */
	public int numClasses(){return 0;}
	
	/**
	 * initializes fields based on the configuration of s
	 * @param s
	 */
	public void init(Serializer s){
		setPos(s.doubles[0],s.doubles[1]);
		width = s.ints[0];
		height = s.ints[1];
		init();
	}
	/**
	 * called when added to the room and to the spawn menu, intializes fields
	 */
	public void init(){
		if(vel == null)
			vel = new Point2D.Double();
		if(acc == null)
			acc = new Point2D.Double();
		updatePosLast();
	}
	/**
	 * adds t to spawn queue
	 * @param t
	 */
	public void addSpawn(Thing t){
		spawn = t;
	}
	/**
	 * removes the item from the spawn queue from this and returns it
	 * @return a Thing that should be added to the room. If there is none, null
	 */
	public Thing getSpawn(){
		Thing temp = spawn;
		spawn = null;
		return temp;
	}
	/**
	 * writes the last position to memory
	 */
	public void updatePosLast(){
		posLast.setLocation(pos);
	}
	/**
	 * returns true if this is moving through the air as a result of kill(double x, double y), false if not
	 * @return true if this is moving through the air as a result of kill(double x, double y), false if not
	 */
	public boolean dying(){
		/*boolean r = getDying;
		getDying = dying;
		return r;*/
		return dying;
	}
	/**
	 * determines if t is touching this
	 * @param t
	 * @return true if this is touching t
	 */
	public boolean touching(Thing t){
		if(t instanceof TBound){
			return t.touching(this);
		}
		if(dying() || t.dying())return false;
		double
			x1 = pos.x,
			y1 = pos.y,
			x2 = t.pos.x,
			y2 = t.pos.y;
		double
			w1 = width + x1,
			h1 = height + y1,
			w2 = t.width + x2,
			h2 = t.height + y2;
		
		if(
			((
				w1 >= x2 &&
				h1 >= y2 + 5 &&
				x1 <= w2 &&
				y1 <= h2
			)||(
				w2 >= x1 &&
				h2 >= y1 &&
				x2 <=  w1 &&
				y2 <= h1
			))
				
		){
			//S//ystem.out.println(getClass() + " " + fromWhere(t));
			return true;
		}

		//tubing (for low framerates)
		return false;
	}
	/**
	 * changes the in-game image of this Thing to the underground theme, if it has one
	 */
	public void makeSpriteUnderground(){
		
	}
	/**
	 * called when this Thing touches another Thing
	 * @param t the other Thing
	 */
	public void onTouch(Thing t){
		if(!t.isStatic()){
			
			byte where = fromWhere(t);
			
			if( where == FROM_ABOVE ){
				t.setPos(t.pos.x,pos.y + height);
				if(t.vel.y < 0)
					t.bumpY();
				if(t.acc.y < 0)
					t.acc.y = 0;
			}else if( where == FROM_BELOW ){
				t.setPos(t.pos.x,pos.y - t.height);
				if(t.vel.y > 0)
					t.bumpY();
				if(t.acc.y > 0)
					t.acc.y = 0;
			}else if( where == FROM_LEFT ){
				t.setPos(pos.x - t.width,t.pos.y);
				t.bumpX();
				if(t.acc.x > 0)
					t.acc.y = 0;
			}else if( where == FROM_RIGHT ){
				t.setPos(pos.x + width,t.pos.y);
				t.bumpX();
				if(t.acc.x < 0)
					t.acc.x = 0;
			}
		}
	}
	
	/**
	 * determines where a Thing came from when a collision occurs
	 * @param t the other Thing
	 * @return the direction where t came from, if it can't be determined, returns Thing.FROM_NONE
	 */
	public byte fromWhere(Thing t){
		
		//they moved into us
		if( t.posLast.y + 2 >= pos.y + height && t.vel.y <= 0 ){
			return FROM_ABOVE;
		}else if( t.posLast.y + t.height <= pos.y && t.vel.y > 0 ){
			return FROM_BELOW;
		}else if( t.posLast.x + t.width <= pos.x && t.vel.x > 0 ){
			return FROM_LEFT;
		}else if( t.posLast.x >= pos.x + width && t.vel.x < 0 ){
			return FROM_RIGHT;
		}
		
		//we moved into them
		if( t.pos.y >= posLast.y + height && vel.y > 0 ){
			return FROM_ABOVE;
		}else if( t.pos.y + t.height <= posLast.y && vel.y < 0 ){
			return FROM_BELOW;
		}else if( t.pos.x + t.width <= posLast.x && vel.x < 0 ){
			return FROM_LEFT;
		}else if( t.pos.x >= posLast.x + width && vel.x > 0 ){
			return FROM_RIGHT;
		}
		
		//both at same time
		if( t.pos.y <= pos.y + height && t.pos.y+10 >= pos.y && vel.y > t.vel.y && vel.y != 0 && t.vel.y != 0 ){
			return FROM_ABOVE;
		}else if( t.pos.y + t.height >= pos.y && t.pos.y + t.height <= pos.y + height && vel.y <  t.vel.y && vel.y != 0 && t.vel.y != 0 ){
			return FROM_BELOW;
		}else if( t.pos.x + t.width >= pos.x && t.pos.x <= pos.x && vel.x != 0 && t.vel.x != 0 ){
			return FROM_LEFT;
		}else if( t.pos.x <= pos.x + width && t.pos.x >= pos.x && vel.x != 0 && t.vel.x != 0 ){
			return FROM_RIGHT;
		}
		
		return FROM_NONE;
	}
	/**
	 * queues the parent Room, if any, to remove this
	 */
	public void kill(){
		killed = true;
	}
	/**
	 * queues the room to not remove this Thing if previously told to do so
	 */
	public void revive(){
		killed = false;
	}
	/**
	 * sets the velocity of the object to a value, once it is off screen then it is removed from the room
	 * @param vel the velocity that it will move
	 */
	public void kill(Point2D.Double vel){
		dying = true;
		this.vel.setLocation(vel);
	}
	/**
	 * links this Thing to another (pipes, etc.)
	 * @param other Thing to link to
	 */
	public void link(Thing other){
		
	}
	/**
	 * determines whether or not this Thing can link with other
	 * @param other the Thing that is checked against
	 * @return true if it can, false if it cannot
	 */
	public boolean canLink(Thing other){
		return false;
	}
	/**
	 * returns image that is to be displayed in the spawn menu
	 * @return image that is to be displayed in the spawn menu
	 */
	public BufferedImage preview(){
		return null;
	}
	/**
	 * returns whether or not this should be removed from its parent Room
	 * @return whether or not this should be removed from its parent Room
	 */
	public boolean killed(){
		return killed;
	}
	
	/**
	 * called every frame, updates velocity, position, etc
	 */
	public void think(){
		updatePosLast();
		pos.setLocation(pos.x+vel.x*Wuigi.time(), pos.y+vel.y*Wuigi.time());
		vel.setLocation(vel.x + acc.x*Wuigi.time(), vel.y + acc.y*Wuigi.time());
		if(enableGravity()){
			if(pos.y+Wuigi.GROUND_LEVEL < 0){
				kill();
				falling = false;
			}else if(pos.y > 0 || dying || falling){
				acc.y = -Wuigi.GRAVITY;
			}else if(enableGravity()){
				pos.y = 0;
				bumpY();
			}
		}
	}
	/**
	 * sets the current position
	 */
	public void setPos(double x, double y){
		pos.setLocation(x,y);
	}
	/**
	 * sets the current position
	 */
	public void setPos(Point2D.Double pos){
		setPos(pos.x,pos.y);
	}
	/**
	 * called when this Thing bumps into another Thing, changes X velocity
	 */
	public void bumpX(){
		vel.x *= -1;
	}
	/**
	 * called when this Thing bumps into another Thing, chanes Y velocity
	 */
	public void bumpY(){//if this Thing bumps into something, this is what the vector is multiplied by
		vel.y = 0;
	}
	/**
	 * determines if this has the capability to move
	 * @return true if this Thing moves, false if it does not
	 */
	public boolean isStatic(){
		return false;
	}
	/**
	 * determines if this is fast enough to kill TEnemies
	 * @return true if this Thing moves fast enough to kill TEnemies, false if it does not
	 */
	public boolean isFast(){
		return false;
	}
	
	/**
	 * sets the position when it is in the spawn menu
	 * @param x coord of mouse
	 * @param y coord of mouse
	 */
	public void setSpawnPos(double x, double y){
		setPos(x,y - height);
	}
	/**
	 * determines if this should be affected by gravity
	 * @return true if gravity should effect this Thing, false if it should not
	 */
	public boolean enableGravity(){return false;}
	
}