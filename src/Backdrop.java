


import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;


import java.awt.Color;

/**
 * Represents the Background in the game
 * @author Reed Weichler
 *
 */
public class Backdrop{
	
	private static final Image
		SKY = (new ImageIcon("images/clouds.gif")).getImage(),
		BRICK = (new ImageIcon("images/undergroundbrick.gif")).getImage(),
		GRASS = (new ImageIcon("images/grass.jpg")).getImage();
	
	private boolean underground;
	
	public static final Color
		SKY_COLOR = new Color(63,191,255),
		UNDERGROUND_COLOR = Color.BLACK;
	
	private static final double
		SKY_MULTI = 0.1,
		GROUND_MULTI = 1.0;
	
	/**
	 * Creates a new Backdrop.
	 * @param underground true if it should be an underground theme, false if not
	 */
	public Backdrop(boolean underground){
		this.underground = underground;
    }
	
	/**
	 * determines if the theme is underground
	 * @return true if the theme is underground
	 */
	public boolean isUnderground(){
		return underground;
	}
    /**
     * draws the backdrop to the screen
     * @param g
     * @param o
     * @param hero to be used to check against for parallax scrolling
     */
    public void draw(Graphics g, java.awt.image.ImageObserver o, Hero hero){
		double viewX = hero.viewX();
		double viewY = hero.viewY();
		if(underground)
			g.setColor(UNDERGROUND_COLOR);
		else
			g.setColor(Backdrop.SKY_COLOR);
		g.fillRect(0, 0, Wuigi.screenWidth, Wuigi.screenHeight);
    	Image ground;
    	int groundHeight,groundWidth;
    	//g.setColor(SKY_COLOR);
    	if(!underground){
    		ground = GRASS;
	    	groundHeight = ground.getHeight(o);
	    	groundWidth = ground.getWidth(o);
	    	int skyHeight = SKY.getHeight(o)*3,
	    		skyWidth = SKY.getWidth(o)*3;
	    		
	    	
	    	double x = -viewX*SKY_MULTI;
	    	double y = (viewY - (Wuigi.screenHeight - Wuigi.GROUND_LEVEL))*SKY_MULTI;
	    	while(x > 0)
	    		x -= skyWidth;
	    	((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
	    	for(; x < Wuigi.screenWidth; x+=skyWidth){
	    		g.drawImage(SKY, Wuigi.scaleW(x),Wuigi.scaleH(y),Wuigi.scaleW(skyWidth),Wuigi.scaleH(skyHeight),o);
	    	}
	    	((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));


			/*((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
	    	
	    	x = -viewX*BUILDINGS_MULTI;
	    	y = viewY - buildingHeight;
	    	while(x > 0)
	    		x -= buildingWidth;
	    	for(; x < Wuigi.W; x+=buildingWidth){
	    		g.drawImage(BUILDINGS, Wuigi.scaleW(x),Wuigi.scaleH(y),Wuigi.scaleW(buildingWidth),Wuigi.scaleH(buildingHeight),o);
	    	}
	    	

			((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.00f));
			*/
    	}else{
    		ground = BRICK;
	    	groundHeight = ground.getHeight(o)*2;
	    	groundWidth = ground.getWidth(o)*2;
    	}
    	
    	
    	for(double y = viewY; y < Wuigi.screenHeight; y+=groundHeight){
    		double x = viewX;
	    	x *= -GROUND_MULTI;
	    	while(x > 0)
	    		x -= groundWidth;
	    		
	    	for(; x < Wuigi.screenWidth; x+=groundWidth){
	    		g.drawImage(ground, Wuigi.scaleW(x),Wuigi.scaleH(y),Wuigi.scaleW(groundWidth),Wuigi.scaleH(groundHeight),o);
	    	}
    	}
    }
	
	
}