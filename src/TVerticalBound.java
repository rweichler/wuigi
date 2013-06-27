import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * An infinitely wide and 32-unit tall structure that impermeable by all in-game objects. Seen as orange in the Level editor.
 * @author Reed Weichler
 *
 */
public class TVerticalBound extends TBound {

	private static final Sprite PREVIEW = new Sprite("images/sprites/tools/vbound.png");

	public BufferedImage preview(){
		return PREVIEW.getBuffer();
	}
	public void think(){
		boolean dead = this.killed();
		super.think();
		if(!dead){
			revive();
		}
	}
	
	public boolean touching(Thing t){

		if(t.dying())return false;
		double
			y1 = pos.y,
			y2 = t.pos.y;
		double
			h1 = height + y1,
			h2 = t.height + y2;
		
		if(
			((
				h1 >= y2 &&
				y1 <= h2
			)||(
				h2 >= y1 &&
				y2 <= h1
			))
				
		){
			return true;
		}
		return false;
	}
	
	public byte fromWhere(Thing t){
		if(pos.y + height <= t.posLast.y && t.vel.y < 0){
			return FROM_ABOVE;
		}else if(pos.y >= t.posLast.y + t.height && t.vel.y > 0){
			return FROM_BELOW;
		}
		return FROM_NONE;
	}
	
	public int[] getDrawCoords(Hero hero){
		int[] c = super.getDrawCoords(hero);
		c[0] = 0;
		c[2] = Wuigi.screenWidth;
		return c;
	}
	

	public boolean inPlayerView(Hero hero){
		return hero.viewY()-pos.y > 0 && hero.viewY()-pos.y-height < Wuigi.screenHeight;
	}
}
