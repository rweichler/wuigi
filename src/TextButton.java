


import java.awt.*;
import java.awt.geom.*;

/**
 * Helper class used to draw Strings to the screen.
 * @author Reed Weichler
 *
 */
public class TextButton {
	private String str;
	private Font font;
	private Rectangle bounds;
	private Color highlight, color;
	
	/**
	 * default colors
	 */
	public static final Color	TITLE = Color.RED,
								TEXT = Color.WHITE,
								HIGHLIGHT = new Color(180,180,180);
	
	/**
	 * Creates a new TextButton with the specified attributes
	 * @param str text to be displayed
	 * @param font font of text
	 * @param x the very left part of the text
	 * @param y the top part of the text
	 * @param color default color
	 * @param highlight color when the mouse hovers over
	 */
	public TextButton(String str, Font font, int x, int y, Color color, Color highlight){
		this.str = str;
		this.color = color;
		this.highlight = highlight;
		this.font = font;
		Rectangle stringBounds = stringBounds(str,font);
		bounds = new Rectangle(x,y,stringBounds.width, stringBounds.height);
	}
	public TextButton(String str, Font font, int x, int y){
		this(str,font);
		setPos(x,y);
	}
	
	public TextButton(String str, Font font, int x, int y, Color color){
		this(str,font,x,y,color,color);
	}

	public TextButton(String str, Font font, Color color, Color highlight){
		this(str,font,0,0,color,highlight);
	}
	public TextButton(String str, Font font, Color color){
		this(str,font,color,color);
	}
	public TextButton(String str, Font font){
		this(str,font,TEXT,HIGHLIGHT);
	}
	
	/**
	 * determines if p is within the bounds of this
	 * @return true if p is within the bounds of this, false if not
	 */
	public boolean contains(Point p){
		return bounds.contains(p);
	}
	/**
	 * determines if a point at coordinates (x,y) is within the bounds of this
	 * @return true if (x,y) is within the bounds of this, false if not
	 */
	public boolean contains(int x, int y){
		return bounds.contains(x,y);
	}
	public int getWidth(){
		return bounds.width;
	}
	public int getHeight(){
		return bounds.height;
	}
	public void setPos(Point p){
		bounds.setLocation(p);
	}
	public void setPos(int x, int y){
		bounds.setLocation(x,y);
	}
	
	public void setText(String str){
		this.str = str;
		Rectangle stringBounds = stringBounds(str,font);
		bounds = new Rectangle(bounds.x,bounds.y,stringBounds.width, stringBounds.height);
		
	}
	
	//EXTREMELY HACKY. I throw up a little when I look at this code.
	private Rectangle stringBounds(String str, Font font){
		Graphics g = (new Sprite(1,1)).getBuffer().getGraphics();
		Rectangle2D stringBounds = g.getFontMetrics(font).getStringBounds(str,g);
		return new Rectangle(0,0,(int)(stringBounds.getWidth() + 0.5),(int)((stringBounds.getHeight() + 0.5)/2));
	}
	
	/**
	 * draws this to g 
	 * @param mouse coordinates of the mouse
	 */
	public void draw(Graphics g, Point mouse){
		g.setFont(font);
		if(contains(mouse)){
			g.setColor(highlight);
		}else{
			g.setColor(color);
		}
		g.drawString(str, bounds.x, bounds.y + bounds.height);
	}
	/**
	 * draws this to g, mouse coordinates are determined from ScreenPanel.mouse
	 */
	public void draw(Graphics g){
		draw(g,ScreenManager.mouse);
	}
	
}
