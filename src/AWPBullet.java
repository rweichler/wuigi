import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * represents the bullet fired from the TAWP when it's equipped to the player
 * @author rweichler
 *
 */
public class AWPBullet extends Thing {
	
	private Line2D.Double line;
	
	/**
	 * creates a new AWPBullet trajectory with the following endpoints
	 * @param x1 x coordinate of player
	 * @param y1 y coordinate of player
	 * @param x2 x coordinate of mouse click
	 * @param y2 y coordinate of mouse click
	 */
	public AWPBullet(double x1, double y1, double x2, double y2){
		//double angle = Math.atan((y2-y1)/(x2-x1));
		double dx = x2 - x1;
		double dy = y2 - y1;
		double currentmag = dx*dx + dy*dy;
		double desiredmag = Wuigi.screenWidth*Wuigi.screenWidth + Wuigi.screenHeight*Wuigi.screenHeight;
		
		double multi = Math.sqrt(desiredmag/currentmag);
		
		x2 = x1 + dx*multi;
		y2 = y1 + dy*multi;
		
		line = new Line2D.Double(x1,y1,x2,y2);
	}
	
	/**
	 * called every frame, just kills this since it's a bullet and shouldn't exist for longer than a fraction of a second
	 */
	public void think(){
		kill();
	}
	/**
	 * kills anything that is touching this, using the touching method to ensure that it's called
	 */
	public boolean touching(Thing t){
		if(!(t instanceof TEnemy))return false;
		Rectangle2D.Double rect = new Rectangle2D.Double(t.pos.x, t.pos.y, t.width, t.height);
		if(rect.intersectsLine(line)){
			
			double x = line.x2 - line.x1;
			double y = line.y2 - line.y1;

			double mag = x*x + y*y;
			double newmag = 15;
			
			x *= Math.sqrt(newmag*newmag/mag);
			y *= Math.sqrt(newmag*newmag/mag);
			
			t.kill(new Point2D.Double(x,y));
			return true;
		}
		return false;
		
	}
}
