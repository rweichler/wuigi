import java.awt.image.BufferedImage;

/**
 * When equipped, the Hero can be hit by an enemy without dying. This is removed from the Hero and is killed if that happens.
 * @author rweichler
 *
 */
public class TMetalCap extends TItem {
	public static final Sprite IMAGE = new Sprite("images/sprites/MetalCap.png"); //broadcast packet
	
	public TMetalCap(){
		super(32, 32*IMAGE.getHeight()/IMAGE.getWidth());
	}
	
	public void giveItem(Hero h){
		h.startMetal();
	}
	
	public BufferedImage preview(){
		return IMAGE.getBuffer();
	}
	
	public BufferedImage figureOutDrawImage(){
		return IMAGE.getBuffer();
	}
}
