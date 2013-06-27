import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

/**
 * Represents a hole in the ground that Things in the game can fall through and die.
 * @author Reed Weichler
 *
 */
public class GroundHole extends TColoredBlock {
	
	private static final BufferedImage preview = new Sprite("images/sprites/tools/hole.png").getBuffer();
	
	private byte adjacentSkies;
	
	public GroundHole(){
		super(Backdrop.SKY_COLOR, FROM_NONE);
		adjacentSkies = FROM_NONE;
		height = height*3/2;
	}
	public void makeSpriteUnderground(){
		setColor(Backdrop.UNDERGROUND_COLOR);
	}
	public void init(){
		init(Backdrop.SKY_COLOR,FROM_NONE);
		revive();
	}
	public void think(){
		super.think();
		if(getGridPos().y != -1){
			setGridPos(getGridPos().x, -1);
		}
		
	}
	public BufferedImage preview(){
		return preview;
	}
	
	
	private boolean hasAdjacentSky(byte direction){
		if(direction == FROM_NONE)return false;
		return (adjacentSkies & direction) != 0;
	}
	
	public void addAdjacent(TGridded other){
		super.addAdjacent(other);
		byte direction = getDirection(other);
		if(direction == FROM_NONE)return;
		if(!hasAdjacentSky(direction)){
			adjacentSkies += direction;
		}
	}
	
	

	public void removeAdjacent(TGridded other){
		super.removeAdjacent(other);
		byte direction = getDirection(other);
		if(direction == FROM_NONE)return;
		if(hasAdjacentSky(direction)){
			adjacentSkies -= direction;
		}
	}
	public void onTouch(Thing t){
		if(t instanceof TGridded && !(t instanceof GroundHole || t instanceof TBGBlock) && getGridPos().equals(((TGridded)t).getGridPos())){
			kill();
		}
		if(!t.enableGravity()) return;
		if((t.pos.x >= pos.x || hasAdjacentSky(FROM_LEFT)) && (t.pos.x + t.width <= pos.x + width || hasAdjacentSky(FROM_RIGHT))){
			t.falling = true;
		}else{
			if(t.pos.y >= pos.y + height*2/3 - 10){
				t.falling = false;
			}
		}
	}
	public int[] getDrawCoords(Hero hero){
		int[] c = super.getDrawCoords(hero);
		c[3] = Wuigi.screenHeight - c[1];
		return c;
	}
	
	
}
