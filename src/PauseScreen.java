


import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;


/**
 * Represents the screen that comes up when there is a current game and is paused.
 * @author Reed Weichler
 *
 */
public class PauseScreen extends Screen {
	
	private static final int	TOP = 150,
								SPACING = 5;
	
	private TextButton resume,mainMenu,save,quit;
	
	private TextButton areYouSure,unsavedData,yes,no;
	
	private boolean mainMenuSelected,quitSelected,canSave;
	
	public PauseScreen(boolean canSave){
		this.canSave = canSave;
		mainMenuSelected = false;
		
		resume = new TextButton("RESUME", Wuigi.FONT_BIG);
		mainMenu = new TextButton("MAIN MENU", Wuigi.FONT_BIG);
		quit = new TextButton("QUIT", Wuigi.FONT_BIG);
		areYouSure = new TextButton("ARE YOU SURE?", Wuigi.FONT_BIG, TextButton.TITLE,TextButton.TITLE);
		int textheight = resume.getHeight();
		if(canSave){
			unsavedData = new TextButton("UNSAVED DATA WILL BE LOST (FOREVER)", Wuigi.FONT_PLAIN, TextButton.TITLE,TextButton.TITLE);
			save = new TextButton("SAVE LEVEL", Wuigi.FONT_BIG);
		}else{
			unsavedData = new TextButton("ALL PROGRESS WILL BE LOST", Wuigi.FONT_PLAIN, TextButton.TITLE,TextButton.TITLE);
			save = new TextButton("RESET LEVEL", Wuigi.FONT_BIG);
		}
		yes = new TextButton("CONTINUE", Wuigi.FONT_BIG);
		no = new TextButton("GO BACK", Wuigi.FONT_BIG);
		
		resume.setPos((Wuigi.screenWidth - resume.getWidth())/2,		TOP);
		mainMenu.setPos((Wuigi.screenWidth - mainMenu.getWidth())/2,	TOP + (SPACING + textheight)*1);
		quit.setPos((Wuigi.screenWidth - quit.getWidth())/2,TOP + (SPACING + textheight)*5);
		save.setPos((Wuigi.screenWidth - save.getWidth())/2,TOP + (SPACING + textheight)*3);
		unsavedData.setPos((Wuigi.screenWidth - unsavedData.getWidth())/2,	TOP + (SPACING + textheight)*2);

		areYouSure.setPos((Wuigi.screenWidth - areYouSure.getWidth())/2,	TOP + (SPACING + textheight)*1);
		yes.setPos(Wuigi.screenWidth/2 - yes.getWidth() - 20,TOP + (SPACING + textheight)*3);
		no.setPos(Wuigi.screenWidth/2 + 20,TOP + (SPACING + textheight)*3);
	}
	
	public void draw(Graphics g) {
		g.setColor(new Color(0,0,0,150));
		g.fillRect(0, 0, Wuigi.screenWidth, Wuigi.screenHeight);
		if(!(mainMenuSelected || quitSelected)){
			mainMenu.draw(g);
			resume.draw(g);
			save.draw(g);
			quit.draw(g);
		}else{
			areYouSure.draw(g);
			unsavedData.draw(g);
			yes.draw(g);
			no.draw(g);
		}
	}

	@Override
	public void key(KeyEvent e, boolean down) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE && down){
			controller.pause(false);
			mainMenuSelected = false;
		}

	}

	@Override
	public void mouse(MouseEvent e, boolean down) {
		if(down)return;
		Point mouse = e.getPoint();
		if(mainMenuSelected){
			if(yes.contains(mouse)){
				controller.mainMenu();
			}else if(no.contains(mouse)){
				mainMenuSelected = false;
			}
		}else if(quitSelected){
			if(yes.contains(mouse)){
				System.exit(0);
			}else if(no.contains(mouse)){
				quitSelected = false;
			}
		}else{
			if(resume.contains(mouse)){
				controller.pause(false);
			}else if(mainMenu.contains(mouse)){
				mainMenuSelected = true;
			}else if(quit.contains(mouse)){
				quitSelected = true;
			}else if(save.contains(mouse)){
				if(canSave)
					controller.saveGame();
				else
					controller.resetSinglePlayer();
			}
		}
	}

	public void think() {

	}

}
