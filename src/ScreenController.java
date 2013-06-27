import java.io.File;

/**
 * This class controls the ScreenManager. Every Screen has one of these.
 * @author Reed Weichler
 *
 */
public class ScreenController {
	private ScreenManager screenPanel;
	private FileOpener opener;
	private File level;
	private File currentLevel;
	private int marioImage;
	/**
	 * Creates a new instance of ScreenController, controlling manager and using opener as the file opener.
	 * @param manager the ScreenManager to be controlled
	 * @param opener the FileOpener to be used when opening files
	 */
	public ScreenController(ScreenManager manager, FileOpener opener){
		this.screenPanel = manager;
		this.opener = opener;
		level = null;
		marioImage = -1;
	}
	/**
	 * pauses / unpauses the game
	 * @param pause true if wants to pause, false if wants to unpause
	 */
	public void pause(boolean pause){
		screenPanel.pause(pause);
	}
	/**
	 * creates a new LevelEditor
	 * @param marioImage the color of the Hero
	 */
	public void levelEditor(int marioImage){
		screenPanel.levelEditor(marioImage);
		pause(false);
	}
	
	/**
	 * creates a new SinglePlayer
	 * @param marioImage the color of the Hero
	 */
	public void singlePlayer(int marioImage){
		level = opener.openFile();
		currentLevel = level;
		this.marioImage = marioImage;
		screenPanel.singlePlayer(marioImage, level);
	}
	
	/**
	 * called when RESET LEVEL is pressed in the PauseScreen in SinglePlayer. It resets the current level back to what it originally was so it can be played through again
	 */
	public void resetSinglePlayer(){
		screenPanel.singlePlayer(marioImage, currentLevel);
	}
	
	/**
	 * returns to the main menu
	 */
	public void mainMenu(){
		screenPanel.renew();
	}
	
	/**
	 * If in level editor mode, prompts the user to select a file and saves the level to that file
	 * @return true if game was saved, false if not
	 */
	public boolean saveGame(){
		return screenPanel.saveGame(opener.saveFile());
	}
	
	/**
	 * Prompts the user to select a file and opens the Level editor to edit that file
	 * @param marioImage the color of the Hero
	 */
	public void loadLevelEditor(int marioImage){
		screenPanel.levelEditor(marioImage,opener.openFile());
	}
	
	/**
	 * called when in SinglePlayer the Hero reaches the goal. Tries to open the next level using the .wcfg. If it cannot be found then it returns to the main menu
	 */
	public void nextLevel(){
		
		String lvl = level.getPath();
		String path, filename;
		{
			String[] split = lvl.split("/");
			filename = split[split.length - 1];
			if(split.length > 1){
				StringBuffer buffer = new StringBuffer();
				for(int i = 0; i < split.length - 1; i++){
					buffer.append(split[i]);
					buffer.append('/');
				}
				path = buffer.toString();
			}else{
				path = "";
			}
		}
		String file = path + filename.substring(0,filename.length() - "wuigi".length()) + "wcfg";
		boolean loaded = false;
		if(opener.readFile(file)){
			String line;
			while((line = opener.readLine()) != null){
				if(line.equals(currentLevel.getName())){
					line = opener.readLine();
					if(line == null){
						break;
					}
					try {
						File fcLevel = new File(path + line);
						if(!fcLevel.getPath().equals(currentLevel.getPath()) && opener.readFile(fcLevel)){	
							currentLevel = fcLevel;
							screenPanel.singlePlayer(marioImage, currentLevel);
							loaded = true;
							break;
						}else{
							continue;
						}
					} catch (Exception e) {
						System.err.println(e.getMessage());
						loaded = false;
						break;
					}
				}
			}
		}
		if(!loaded){
			mainMenu();
		}
		
	}
}
