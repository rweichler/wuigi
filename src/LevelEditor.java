



import java.awt.*;
import java.util.Vector;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/**
 * The Level Editor part of the game. It contains a SpawnScreen that is used to create and manipulate Things in the game.
 * @author rweichler
 *
 */
public class LevelEditor extends GameScreen{
	//private CreativeBox box;
	private SpawnScreen spawnScreen;
	private boolean mouseDown;
	private Vector<TGridded> dragSpawn;
	private boolean freeze;
	
	public LevelEditor(){
		super();
		spawnScreen = new SpawnScreen(hero);
		mouseDown = false;
		dragSpawn = null;
		freeze = false;
	}
	
	/*public void resetHero(int marioColor){
		hero.setSpriteColor(marioColor);
		resetHero();
	}*/
	public void reset(){
		hero.init();
		hero.setDeathPos();
		hero.startInvulnerable();
		setSpawn();
	}
	
	public boolean init(int marioColor, File f) throws Exception{
		boolean b = super.init(marioColor, f);
		freeze = true;
		return b;
	}
	
	public void init(int marioColor){
		super.init(marioColor);
		freeze = false;
	}


	public void draw(Graphics g) {
		currentRoom().draw(g, null, hero, spawnScreen);
		//box.draw(g,hero);
		spawnScreen.draw(g);
		
	}
	public void key(KeyEvent e, boolean pressed) {
		super.key(e,pressed);
		spawnScreen.key(e,pressed);
		
	}

	public void mouse(MouseEvent e, boolean down) {
		spawnScreen.mouse(e,down);
		if(spawnScreen.shouldToggleLevel()){
			roomIndex++;
			if(roomIndex == rooms.size()){
				roomIndex = 0;
			}
			hero.init();
			hero.startInvulnerable();
		}else if(spawnScreen.shouldToggleFreeze()){
			freeze = !freeze;
		}
		if(down){
			mouseDown = true;
			//box.start(e.getX(), e.getY());
			Thing spawn = spawnScreen.getSpawn();
			if(spawn != null){
				currentRoom().add(spawn);
				if(spawn instanceof TGridded){
					dragSpawn = new Vector<TGridded>();
					dragSpawn.add((TGridded)spawn);
				}
			}
		}else{
			mouseDown = false;
			dragSpawn = null;
		}
		
	}

	public void think() {
		super.think();
		spawnScreen.think();
		currentRoom().think(hero,true,freeze);
		Class spawn = currentRoom().shouldRemoveSpawnFromOtherRooms();
		if(spawn != null){
			for(Room room: rooms){
				if(room != currentRoom()){
					room.removeSpawns(spawn);
				}
			}
		}
		
		if(mouseDown && dragSpawn != null && dragSpawn.size() > 0 && spawnScreen.peekSpawn() != null && spawnScreen.peekSpawn() instanceof TGridded){
			TGridded peek = (TGridded)spawnScreen.peekSpawn();
			boolean touchingSomething = false;
			for(TGridded grid: dragSpawn){
				if(peek.representation().contains(grid.representation())){
					touchingSomething = true;
					break;
				}
			}
			if(!touchingSomething){
				dragSpawn.add(peek);
				currentRoom().add(spawnScreen.getSpawn());
			}
		}
		if(mouseDown && spawnScreen.peekSpawn() != null && spawnScreen.peekSpawn() instanceof TRemover){
			currentRoom().add(spawnScreen.peekSpawn());
		}
		
	}
}
