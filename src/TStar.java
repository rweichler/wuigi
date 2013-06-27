



import java.awt.image.*;
/**
 * When equipped, the player becomes invulerable for a short period of time and flashes seizure-inducing colors.
 * @author Reed Weichler
 *
 */
public class TStar extends TItem{
	private Sprite IMAGE = new Sprite("images/sprites/star.gif");
	
	
	public void giveItem(Hero h){
		h.startStar();
	}
	
	public BufferedImage preview(){
		return IMAGE.getBuffer();
	}
	
	public BufferedImage figureOutDrawImage(){
		return IMAGE.getBuffer();
	}
	
	public void onBlockExit(){
		super.onBlockExit();
		vel.x = 3;
		vel.y = Math.abs(vel.x*2);
	}
	
	public void bumpY(){
		if(vel.y > 0){
			vel.y = -10;
		}else{
			vel.y = 10;
		}
	}
}