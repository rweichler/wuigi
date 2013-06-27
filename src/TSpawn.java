import java.awt.Color;
import java.awt.image.BufferedImage;
/**
 * Represents where the player should spawn in SinglePlayer
 * @author Reed Weichler
 *
 */
public class TSpawn extends TVisibleTool {
	
	private static final Sprite PREVIEW = new Sprite("images/sprites/tools/spawn.png");
	
	public BufferedImage preview(){
		return PREVIEW.getBuffer();
	}

	public BufferedImage figureOutDrawImage(){
		return preview();
	}
	public void onTouch(Thing t){
		
	}
}
