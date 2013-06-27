



import javax.swing.*;
import java.awt.*;
/**
 * Stores game constants and initializes the game
 * @author Reed Weichler
 *
 */
public class Wuigi extends JPanel{
	
	public static String print = "nothing";
	
	public static JPanel panel;
	
	private static final long serialVersionUID = 8930735783398997076L;
	private static long lastTime;
	private static long timePassed;
	private static final long REFRESH_RATE = 15000000;
	public static final int
		GROUND_LEVEL = 50,
		COMPLETE_JUMP_TIME = 20;

	public static final Font	FONT_HUGE = new Font("Arial Black", Font.PLAIN, 80),
								FONT_BIG = new Font("Arial Black", Font.PLAIN, 40),
								FONT_PLAIN = new Font("Tahoma", Font.PLAIN, 20),
								FONT_CHAT = new Font("Tahoma", Font.PLAIN, 12),
								FONT_CHAT_BOLD = new Font("Tahoma", Font.BOLD, 12);
	public static int
		screenWidth = 800,
		screenHeight = 600;
	
	public static final double
		GRAVITY = 0.701;
	
	private ScreenManager manager;
	
	/**
	 * returns the amount of time that has passed since the last screen refresh
	 * @return the amount of time that has passed since the last screen refresh
	 */
	public static double time(){
		double d = 1.0*timePassed/REFRESH_RATE;
		//if we lagged too much then pretend no time has passed
		if(d > 10){
			return 0;
		}
		return d;
	}
	
	public static void updateTime(){
		timePassed = System.nanoTime() - lastTime;
		lastTime = System.nanoTime();
	}
	
	/**
	 * used for in-game objects to round for drawing to the screen (x coordinate)
	 * @param x
	 * @return
	 */
	public static int scaleW(double x){
		return (int)(x + 0.5);
	}
	/**
	 * used for in-game objects to round for drawing to the screen (y coordinate)
	 * @param y
	 * @return
	 */
	public static int scaleH(double y){
		return (int)(y + 0.5);
	}
	
	public void paintComponent(Graphics g){
		if(!manager.didThink()) return;
		super.paintComponent(g);
		manager.draw(g);
		g.setColor(Color.YELLOW);
		g.setFont(FONT_BIG);
		/*if(timePassed == 0) return;
		System.out.println(timePassed);
		int x = (int)(1000000000/timePassed);
		g.drawString("FPS: " + x, Wuigi.W - 200, Wuigi.H - 50);*/
	}
	
	/**
	 * makes the game go full screen
	 * @param w JFrame to be made full screen
	 */
	public void makeFullScreen(JFrame w){
		w.setUndecorated(true);
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		screenWidth = screensize.width;
		screenHeight = screensize.height;
	}
	
	/**
	 * creates the entire game
	 */
	public Wuigi(){
		//(new AePlayWaveLoop("sound/eyeoftiger/eyeoftigere.wav")).start();
		setBackground(Color.BLACK);
		manager = new ScreenManager(new FileOpener(this));
		this.setFocusable(true);
		this.addMouseMotionListener(manager);
		this.addMouseListener(manager);
		this.addKeyListener(manager);
		JFrame w = new JFrame("Wuigi");
		makeFullScreen(w);
		w.setBounds(0, 0, screenWidth, screenHeight);
		w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		w.getContentPane().add(this);
		w.setVisible(true);
		w.setResizable(false);
		
		lastTime = System.nanoTime() - 10000000;
		
		//Serializer s = new Serializer();
		//s.ints.add(10);
		//System.out.println(s);
		
		//FileOpener opener = new FileOpener(w);
		//opener.saveFile("heh");
		while(true){
			updateTime();
			//try {
			manager.think();
			//} catch (Exception e) {
				//System.out.println("There's been an exception while trying to call think(): " + e.getMessage());e.printStackTrace();
			//}
		
			if(timePassed/REFRESH_RATE > 1){
				//do not sleep as we are late rendering
			}else{
				try { 
					Thread.sleep(5);
				} catch (Exception e) {
					
				}
			}
			repaint();
			
		}
		
	}
	
	public static void main(String[] args){
		panel = new Wuigi();
    }
    
}