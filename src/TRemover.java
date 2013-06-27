import java.awt.image.BufferedImage;

/**
 * removes objects from the game
 * @author Reed Weichler
 *
 */
public class TRemover extends TTool {
	private Sprite preview = new Sprite("images/sprites/tools/del.png");
	public TRemover(){
		super();
	}
	public void onTouch(Thing t){
		t.kill();
		kill();
	}
	public BufferedImage preview(){
		return preview.getBuffer();
	}
}
