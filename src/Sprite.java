
import java.awt.*;
import java.awt.image.*;
import javax.swing.ImageIcon;
/**
 * This is a helper class for manipulating images.
 * @author Reed Weichler
 *
 */
public class Sprite{
	private BufferedImage imgB;
	
	private BufferedImage transparency;
	
	private Image img;
	int width,height;
	
	/**
	 * Creates a new Sprite from the a path.
	 * @param filename String representation of the path to the image.
	 */
	public Sprite(String filename){
		this(filename,-1, -1);
	}
	
	/**
	 * Creates a new Sprite from a path and scales it to the specified width and height
	 * @param filename String represenatation of the path to the image.
	 * @param width Width to be scaled to
	 * @param height Height to be scaled to
	 */
	public Sprite(String filename, int width, int height){
		img = (new ImageIcon(filename)).getImage();
		if(width == -1)
			this.width = img.getWidth(null);  
		else
			this.width = width;
		
		if(height == -1)
			this.height = img.getHeight(null);
		else
			this.height = height;
		
		bufferImage();
	}
	/**
	 * Creates a new Sprite from an existing BufferedImage
	 * @param image image that should be made
	 */
	public Sprite(BufferedImage image){
		this(image,-1,-1);
	}
	/**
	 * Creates a new Sprite from an existing BufferdImage and scales it to the specified width and height
	 * @param image image that should be made
	 * @param width Width to be scaled to
	 * @param height Height to be scaled to
	 */
	public Sprite(BufferedImage image, int width, int height){
		if(width == -1)
			this.width = image.getWidth();  
		else
			this.width = width;
		
		if(height == -1)
			this.height = image.getHeight();
		else
			this.height = height;
		img = Toolkit.getDefaultToolkit().createImage(image.getSource()); 
		bufferImage();
	}
	
	/**
	 * Creates a blank sprite with the specified width and height
	 * @param width Width to be scaled to
	 * @param height Height to be scaled to
	 */
	public Sprite(int width, int height){
		this(new BufferedImage(width <= 0? 1 : width,height <= 0? 1 : height,BufferedImage.TYPE_INT_ARGB));
	}
	
	private void bufferImage(){
		imgB = 
			new BufferedImage(
			width, 
			height, 
			BufferedImage.TYPE_INT_ARGB);

		renew();
	}
	
	/**
	 * Undoes any changes made to the image by this class
	 */
	public void renew(){
		Graphics g = imgB.createGraphics();
		//((Graphics2D)g).rotate(Math.toRadians(45.0));
		g.drawImage(img, 0, 0, width,height, null);
		g.dispose();
	}
	
	private BufferedImage blank(){
		
		return new BufferedImage(width, height,  imgB.getType());
	}
	
	/**
	 * Replaces all colors found in the image in find with the respective color from replace
	 * @param find Colors to be searched for and replaced
	 * @param replace Colors to replace the respective Color in find
	 */
	public void replaceColors(Color[] find, Color[] replace){
		if(find[0].equals(replace[0]))return;
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				int rgb = imgB.getRGB(x,y);
				for(int i = 0; i < find.length; i++){
					if(rgb == find[i].getRGB()){
						setPixel(x,y,replace[i]);
					}
				}
			}
		}
	}
	
	public String toString(){
		StringBuffer s = new StringBuffer();
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				s.append(getPixel(x,y));
			}
		}
		return s.toString();
	}
	
	/**
	 * Creates a BufferedImage that is a horizontal flip of the current image and returns it
	 * @return the flipped image
	 */
	public BufferedImage flipX(){
		
		BufferedImage r = blank();
		Graphics2D g2d = (Graphics2D)(r.createGraphics());
		g2d.drawImage(imgB, 0, 0, width, height, width, 0, 0, height, null);
		return r;  
	}

	/**
	 * Creates a BufferedImage that is a vertical flip of the current image and returns it
	 * @return the flipped image
	 */
	public BufferedImage flipY(){
		BufferedImage r = blank();
		Graphics2D g2d = (Graphics2D)(r.createGraphics());
		g2d.drawImage(imgB, 0, 0, width, height, 0, height, width, 0, null);
		return r;  
	}

	/**
	 * Creates a BufferedImage that is a horizontal & vertical flip of the current image and returns it
	 * @return the flipped image
	 */
	public BufferedImage flipXY(){
		BufferedImage x = flipX();
		BufferedImage r = blank();
		Graphics2D g2d = (Graphics2D)(r.createGraphics());
		g2d.drawImage(x, 0, 0, width, height, 0, height, width, 0, null);
		return r;  
	}
	
	/**
	 * gets the color of the pixel on the image at the specified coordinates
	 * @param x x coordinate of pixel
	 * @param y y coordinate of pixel
	 * @return a new Color that is the representation of the Color of the image at the pixel
	 */
	public Color getPixel(int x, int y){
		int rgb = imgB.getRGB(x,y);
		return new Color((rgb & 0x00ff0000) >> 16, (rgb & 0x0000ff00) >> 8, rgb & 0x000000ff, (int)((rgb & 0xFF000000)>>>24));
	}
	
	/**
	 * sets the color of a pixel on the image at the specified coordinates
	 * @param x x coordinate of pixel
	 * @param y y coordinate of pixel
	 * @param c color to be drawn
	 */
	public void setPixel(int x, int y, Color c){
		Graphics g = imgB.createGraphics();
		g.setColor(c);
		g.fillRect(x,y,1,1);
		g.dispose();
	}
	
	/**
	 * gets original Image that was passed in
	 * @return the original image
	 */
	public Image getImage(){
		return img;
	}
	/**
	 * gets the manipulated BufferedImage and returns it
	 * @return the manipulated BufferedImage
	 */
	public BufferedImage getBuffer(){
		return imgB;
	}
	
	public int getWidth(){
		return width;
	}
	public int getHeight(){
		return height;
	}
	
}