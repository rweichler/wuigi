import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * This represents an AWP Item. When equipped, the player can shoot enemies. There is no gore.
 * @author Reed Weichler
 *
 */
public class TAWP extends TItem {
	/**
	 * draw image of AWP.
	 */
	public static final Sprite IMAGE = new Sprite("images/sprites/awp.png");
	private static final Sprite PREVIEW = new Sprite("images/sprites/awp_preview.png");
	/**
	 * The amount of time for the White screen effect to last
	 */
	public static final int SHOOT_TIME = 500;
	/**
	 * Font for ammo display.
	 */
	public static final Font AMMO = new Font("Courier", Font.PLAIN, 20);
	
	
	public TAWP(){
		super(0,0,IMAGE.getWidth(), IMAGE.getHeight());
	}
	
	public BufferedImage preview(){
		return PREVIEW.getBuffer();
	}
	
	public BufferedImage figureOutDrawImage(){
		return IMAGE.getBuffer();
	}
	
	public void giveItem(Hero hero) {
		hero.giveAWP();
	}

}
