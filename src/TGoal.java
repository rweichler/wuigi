import java.awt.Color;
import java.awt.image.BufferedImage;
/**
 * Represents the Goal the player must touch if he/she wants to win
 * @author Reed Weichler
 *
 */
public class TGoal extends THorizontalBound{

	private static final Sprite PREVIEW = new Sprite("images/sprites/tools/goal.png");
	private static final Color color = new Color(0,255,0,180);
	public TGoal(){
		super(color);
	}
	
	public BufferedImage preview(){
		return PREVIEW.getBuffer();
	}
	public void onTouch(Thing t){
		
	}
}