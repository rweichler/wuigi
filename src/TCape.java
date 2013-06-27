import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
/**
 * When equipped, the Hero gets the temporary ability to fly.
 * @author Reed Weichler
 *
 */
public class TCape extends TItem{
	public static final Sprite IMAGE = new Sprite("images/sprites/cape.png"); //broadcast packet
	
	public void giveItem(Hero h){
		h.startCape();
	}
	
	public BufferedImage preview(){
		return IMAGE.getBuffer();
	}
	
	public BufferedImage figureOutDrawImage(){
		return IMAGE.getBuffer();
	}
	
}
