import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

/**
 * links objects together so they can interact. For example, this links pipes together so the player can teleport from one to the other
 * @author rweichler
 *
 */
public class TLinker extends TTool {
	
	Thing link;
	private Sprite preview = new Sprite("images/sprites/tools/link.png");
	
	public TLinker(){
		super();
		link = null;
		//isInWorld = false;
	}
	public BufferedImage preview(){
		return preview.getBuffer();
	}
	
	/**
	 * 
	 * @return the link that's in the queue, if there is none then null
	 */
	public Thing getLink(){
		return link;
	}
	
	public boolean canLink(Thing t){
		return link != null && link.canLink(t);
	}
	
	public void onTouch(Thing t){
		if(t.canLink(link)){
			if(link == null){
				link = t;
			}else{
				t.link(link);
				link.link(t);
			}
		}
	}
	
	public void draw(Graphics g, ImageObserver o, Hero hero){
		super.draw(g,o,hero);
		if(link == null || !link.inPlayerView(hero))return;
		int[] c = link.getDrawCoords(hero);
		g.setColor(Color.YELLOW);
		g.fillRect(c[0],c[1],c[2],c[3]);
	}
	
}
