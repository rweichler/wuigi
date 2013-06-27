import java.util.Vector;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.ImageObserver;

/**
 * This represents a room that contains all of the Things that are in the game. This handles all interactions between each Thing and also the Hero, if there is one.
 * @author Reed Weichler
 *
 */
public class Room {
	
	private Vector<Thing> things;
	private Backdrop backdrop;
	
	private int index;
	private Class removeSpawn;
	
	private int numRemoves;
	
	/**
	 * creates a new Room
	 * @param underground true if the theme is underground, false if not
	 * @param index the index in the array of the parent GameScreen
	 */
	public Room(boolean underground, int index){
		this.index = index;
		backdrop = new Backdrop(underground);
		things = new Vector<Thing>();
		numRemoves = 0;
		removeSpawn = null;
	}
	/**
	 * draws this to the screen. by default it does not handle any spawnscreen
	 * @param g
	 * @param o
	 * @param hero
	 */
	public void draw(Graphics g, ImageObserver o, Hero hero){
		draw(g,o,hero,null);
	}

	/**
	 * draws this to the screen, and handles the SpawnScreen if not null
	 * @param g
	 * @param o
	 * @param hero
	 * @param spawn
	 */
	public void draw(Graphics g, ImageObserver o, Hero hero, SpawnScreen spawn){
		boolean shouldDrawHero = true;
		if(hero == null){
			hero = new Hero();
			shouldDrawHero = false;
		}
		backdrop.draw(g, o, hero);
		if(hero.piping() && shouldDrawHero){
			hero.draw(g, o);
		}
		boolean alreadyHighlighted = spawn == null;
		for(int i = 0; i < things.size(); i++){
			Thing t = things.get(i);
			//if(t == null)continue;
			if(!(spawn == null && t instanceof TTool))
				t.draw(g, o, hero);
			if(!alreadyHighlighted){
				Color color = spawn.highlightColor(t);
				if(color != null){
					g.setColor(color);
					int[] c = t.getDrawCoords(hero);
					g.fillRect(c[0],c[1],c[2],c[3]);
					alreadyHighlighted = true;
				}
			}
		}
		if(!hero.piping() && shouldDrawHero){
			hero.draw(g, o);
		}
	}
	/**
	 * converts this to something that can be written to a file
	 * @return the Serializable  object that will be converted into a file
	 */
	public Vector<Serializer> serialize(){
		Vector<Serializer> serializers = new Vector<Serializer>();
		for(Thing t: things){
			serializers.add(t.serialize());
		}
		return serializers;
	}
	/**
	 * called every frame
	 * @param hero
	 * @param editMode true if in LevelEditor
	 */
	public void think(Hero hero, boolean editMode){
		think(hero,editMode,false);
	}
	/**
	 * called every frame
	 * @param hero
	 * @param editMode true if in LevelEditor
	 * @param shouldFreeze if true, then none of the Things that are contained within this will think
	 */
	public void think(Hero hero, boolean editMode,boolean shouldFreeze){
		if(hero != null)
			hero.think();
		//ArrayList<Thing> removeQueue = new ArrayList<Thing>();
		for( int i = 0; i < things.size(); i++ ){
			Thing t = things.get(i);
			if(t == null) continue;
			if(t.killed()){
				//removeQueue.add(t);
				remove(t);
				i--;
				continue;
			}
			
			if( hero != null && !hero.dying() && !(shouldFreeze && t instanceof TEnemy) && t.touching(hero) && hero.touching(t) ){
				t.onTouch(hero);
				hero.onTouch(t);
			}
			/*if((client != null || server != null) && otherHero.touching(t)){
				otherHero.onTouch(t);
				t.onTouch(otherHero);
			}*/
			for( int j = i + 1; j < things.size(); j++ ){
				Thing t2 = things.get(j);
				if(t2 == null || t == t2 || t.isStatic() && t2.isStatic())continue;
				if( t2.touching(t) && t.touching(t2)){
					t.onTouch(t2);
					t2.onTouch(t);
					
				}
			}
			if(!shouldFreeze && !t.isStatic() && (editMode || !(t instanceof TEnemy) || t.inPlayerView(hero)))
				t.think();
			Thing add = t.getSpawn();
			if(add != null)
				add(add);

		}
		if(hero != null){
			Thing add = hero.getSpawn();
			if(add != null)
				add(add);
		}
		/*if(removeQueue.size() > 0){
			for(Thing t: removeQueue){
				if(things.contains(t)){
					things.remove(t);
					if(undo.contains(t)){
						undo.remove(t);
					}
				}
			}
		}*/
		//things.remove(t);
		//if(undo.contains(t)) undo.remove(t);
		//i--;
	}
	
	/**
	 * sets up the spawn for the player
	 * @param hero
	 * @return
	 */
	public boolean setSpawn(Hero hero){
		for(Thing t: things){
			if(t instanceof TSpawn){
				hero.setPos(t.pos.x,t.pos.y);
				return true;
			}
		}
		return false;
	}
	/**
	 * adds a thing to this
	 * @param add
	 */
	public void add(Thing add){
		add(add,true);
	}
	/**
	 * adds a thing to this
	 * @param add Thing to be added
	 * @param shouldInit if false, then add.init() is not called
	 */
	public void add(Thing add, boolean shouldInit){
		if(shouldInit)
			add.init();
		if(backdrop.isUnderground()){
			add.makeSpriteUnderground();
		}
		if(add instanceof TGridded){
			TGridded addgrid = (TGridded)add;
			for(Thing t: things){
				if(!(t instanceof TGridded)) continue;
				TGridded tgrid = (TGridded)t;
				addgrid.addAdjacent(tgrid);
				tgrid.addAdjacent((TGridded)add);
			}
		}
		if(add instanceof TItem){
			TItem item = (TItem)add;
			for(Thing t: things){
				if(t instanceof TBlock && ((TBlock)t).canAcceptItem() && t.touching(add)){
					((TBlock)t).addItem(item);
					return;
				}
			}
			item.onBlockExit();
		}
		if(add instanceof TPipe){
			((TPipe)add).room = index;
		}
		if(add instanceof TSpawn || add instanceof TGoal){
			removeSpawnFromOtherRooms(add.getClass());
		}
		if(add instanceof GroundHole){
			things.add(0,add);
		}else{
			things.add(add);
		}
	}
	/**
	 * makes every other Room contained in the parent GameScreen remove its spawn if it contains any
	 * @param spawn
	 */
	public void removeSpawnFromOtherRooms(Class spawn){
		removeSpawn = spawn;
		removeSpawns(spawn);
	}
	/**
	 * determines if the spawn should be removed
	 * @return the class type of the spawn if it has one, null if not
	 */
	public Class shouldRemoveSpawnFromOtherRooms(){
		Class temp = removeSpawn;
		removeSpawn = null;
		return temp;
	}
	/**
	 * removes all spawns / goals, specified by the class
	 * @param spawn can be either a TGoal or a TSpawn, this instance to be removed
	 */
	public void removeSpawns(Class spawn){
		for(Thing t: things){
			if(t.getClass().equals(spawn)){
				remove(t);
				break;
			}
		}
	}
	/**
	 * removes <b>remove</b> from this
	 * @param remove Thing to be removed
	 */
	public void remove(Thing remove){
		if(remove instanceof TGridded){
			TGridded removegrid = (TGridded)remove;
			for(Thing t: things){
				if(!(t instanceof TGridded)) continue;
				TGridded tgrid = (TGridded)t;
				tgrid.removeAdjacent(removegrid);
			}
		}
		things.remove(remove);
	}
	
}
