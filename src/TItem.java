
/**
 * This is an Item that can be spawned in game, can be equipped to a player and can be put inside TBlocks.
 * @author rweichler
 *
 */
public abstract class TItem extends Thing{
	
	private boolean exited;
	
	public TItem(int width, int height){
		this(0,0,width,height);
	}
	
	/**
	 * Creates a new TItem with a width of 32 and height of 32
	 */
	public TItem(){
		this(0,0,32,32);
	}
	
	public TItem(double x, double y, int width, int height){
		super(x,y,width,height);
		exited = false;
	}
	
	public void init(){
		super.init();
		if(!dying())
			vel.y = 1;
	}
	
	
	public void onTouch(Thing t){
		if(t instanceof Hero){
			giveItem((Hero)t);
			kill();
		}
	}
	/**
	 * called when a Hero touches this
	 * @param hero the Hero that touched this
	 */
	public abstract void giveItem(Hero hero);
	
	/*public boolean touching(Thing t){
		if(!exited) return false;
		return super.touching(t);
	}*/
	
	public boolean enableGravity(){
		return exited;
	}
	
	/**
	 * called when this exits its parent TBlock
	 */
	public void onBlockExit(){
		exited = true;
		if(!dying()){
			vel.y = 0;
			vel.x = 0;
		}
	}
	
	/*public void draw(Graphics2D g, ImageObserver o, Hero hero){
		//if(exitingBlock)
		//	block.draw(g,o,hero);
	}*/
}