
import java.awt.Rectangle;

/**
 * Represents a TBlock that can be walked through
 * @author Reed Weichler
 *
 */
public class TBGBlock extends TBlock {
	public TBGBlock(){
		super();
	}
	public TBGBlock(byte img) {
		super(img);
	}
	public TBGBlock(double x, double y, byte img, TItem item, boolean movesWhenHit){
		super(x,y,img,item,movesWhenHit);
	}
	public TBGBlock(double x, double y, byte img){
		super(x,y,img);
	}
	
	public TBGBlock(double x, double y, byte img, TItem item){
		super(x,y,img,item);
	}
	public void onTouch(Thing t){
		return;
	}
}
