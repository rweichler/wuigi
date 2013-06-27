



import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.*;

/**
 * It's a Goomba. It kills a player if it touches it from the side or bottom. If the hero hits it on top, it is killed.
 * @author Reed Weichler
 *
 */
public class TGoomba extends TEnemy{
	
	private boolean gotHit;
	private boolean rightFoot;
	private double time2Dai;
	private double lastX;
	
	private static final int DIE_TIME = 20;
	
	private static final AePlayWave STOMP = new AePlayWave("sound/stomp.wav");
	private static final String GOOMBA_SPRITE_PATH = "images/sprites/goomba/";
	
	private Sprite[] GOOMBA = {
		new Sprite(GOOMBA_SPRITE_PATH+"walk.gif",32,32),
		new Sprite(GOOMBA_SPRITE_PATH+"dead.gif",32,32),
		
	};
	

	private static final Color[] aboveGround = {
		new Color(231,90,16),
		new Color(247,214,181),
		Color.BLACK,
	};	
	private static final Color[] underGround = {
		new Color(0,136,136),
		new Color(0,255,255),
		new Color(0,64,88),
	};
	
	
	public TGoomba(){
		this(0,0);
	}
	
	public TGoomba(double x, double y){
		this(x,y,32,32);
	}
	
	public TGoomba(double x, double y, int width, int height){
		super(x,y,width,height);
		gotHit = false;
		lastX = 0;
		rightFoot = true;
	}
	
	public void init(){
		super.init();
		vel.x = -1;
	}
	
	public void makeSpriteUnderground(){
		for(Sprite s: GOOMBA){
			s.replaceColors(aboveGround,underGround);
		}
	}
	
	public BufferedImage preview(){
		return GOOMBA[0].getBuffer();
	}
	
	public BufferedImage figureOutDrawImage(){
		
		Sprite img;
		
		if(gotHit){
			img = GOOMBA[1];
		}else{
			img = GOOMBA[0];
		}
		if(dying() && !gotHit){
			if(rightFoot)
				return img.flipXY();
			else
				return img.flipY();
		}else{
			if(rightFoot)
				return img.flipX();
			else
				return img.getBuffer();
		}
	}
	
	public boolean isInanimate(){
		return false;
	}
	
	public boolean enableGravity(){return true;}
	
	public void think(){
		if(gotHit){
			time2Dai -= Wuigi.time();
			if(time2Dai < 1)
				kill();
		}else{
			super.think();
			if(pos.x > lastX + 10 || pos.x < lastX - 10){
				rightFoot = !rightFoot;
				lastX = pos.x;
			}
		}
	}
	public byte killDirection(){
		return FROM_BELOW | FROM_SIDE;
	}
	
	public void onTouch(Thing t){
		if(gotHit) return;
		super.onTouch(t);
	}
	public void heroTouch(Hero hero){
		if(hero.vel.y < -1){
			STOMP.start();
			gotHit();
			stomp(hero);
		}
	}
	
	private void gotHit(){
		gotHit = true;
		time2Dai = DIE_TIME;
	}
	
	
}