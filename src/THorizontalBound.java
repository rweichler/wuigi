import java.awt.Color;
import java.awt.image.BufferedImage;


/**
 * An infinitely tall and 32-unit wide structure that impermeable by all in-game objects. Seen as orange in the Level editor.
 * @author Reed Weichler
 *
 */
public class THorizontalBound extends TBound {

	private static final Sprite PREVIEW = new Sprite("images/sprites/tools/hbound.png");

	public BufferedImage preview(){
		return PREVIEW.getBuffer();
	}
	
	public THorizontalBound(){
		
	}
	public THorizontalBound(Color color){
		super(color);
	}
	
	public boolean touching(Thing t){

		if(t.dying())return false;
		double
			x1 = pos.x,
			x2 = t.pos.x;
		double
			w1 = width + x1,
			w2 = t.width + x2;
		
		if(
			((
				w1 >= x2 &&
				x1 <= w2
			)||(
				w2 >= x1 &&
				x2 <=  w1
			))
				
		){
			return true;
		}
		return false;
	}
	
	public byte fromWhere(Thing t){
		if(pos.x + width <= t.posLast.x && t.vel.x < 0){
			return FROM_RIGHT;
		}else if(pos.x >= t.posLast.x + t.width && t.vel.x > 0){
			return FROM_LEFT;
		}
		return FROM_NONE;
	}
	
	public int[] getDrawCoords(Hero hero){
		int[] c = super.getDrawCoords(hero);
		c[1] = 0;
		c[3] = Wuigi.screenHeight;
		return c;
	}
	

	public boolean inPlayerView(Hero hero){
		return Wuigi.screenWidth-hero.viewX()+pos.x+width > 0 && Wuigi.screenWidth-hero.viewX()+pos.x < Wuigi.screenWidth;
	}
}
