import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * A tool that is not removed instantly from the room after creation. By default it has a width and height of 32.
 * @author Reed Weichler
 *
 */
public class TVisibleTool extends TTool {
	

	public TVisibleTool(){
		this(32,32);
	}
	
	public TVisibleTool(int width, int height){
		this(width,height,null);
	}
	
	public TVisibleTool(int width, int height, Color color){
		super(width,height,color);
	}
	
	public void think(){
		boolean dead = this.killed();
		super.think();
		if(!dead){
			revive();
		}
	}
	
}
