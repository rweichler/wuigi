import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Represents a tool used in the level editor. By default, it has a width and height of 1 and is killed right after
 * it is put into the room (after it performs the desired action, of course).<br/>
 * <br/>
 * These are visible in the Level Editor but are invisible in SinglePlayer
 * @author Reed Weichler
 *
 */
public class TTool extends Thing {

	private Sprite image;
	
	public TTool(){
		this(1,1);
	}
	
	public TTool(int width, int height){
		this(width,height,null);
	}
	
	public TTool(int width, int height, Color color){
		super(0,0,width,height);
		image = new Sprite(width,height);
		if(color != null){
			Graphics g = image.getBuffer().getGraphics();
			g.setColor(color);
			g.fillRect(0,0,width,height);
		}
	}
	public BufferedImage figureOutDrawImage(){
		return image.getBuffer();
	}
	
	public boolean enableGravity(){
		return false;
	}
	
	public void think(){
		super.think();
		kill();
	}
	
}
