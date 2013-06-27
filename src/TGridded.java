

import java.awt.*;
import java.awt.geom.Point2D;
public class TGridded extends Thing{
	
	private Point gridPos;
	private byte adjacents;
	
	public TGridded(){
		this(0,0);
	}
	
	public TGridded(double x, double y){
		this(x,y,32,32);
	}
	
	public TGridded(double x, double y, int width, int height){
		super(x,y,width,height);
		gridPos = new Point(getGridCoord(x),getGridCoord(y));
		adjacents = Thing.FROM_NONE;
		this.width = getGridCoord(width)*32;
		this.height = getGridCoord(height)*32;
		setPos(x,y);
	}
	
	public void setPos(double x, double y){
		gridPos.x = getGridCoord(x);
		gridPos.y = getGridCoord(y);
		pos.x = gridPos.x*32;
		pos.y = gridPos.y*32;
	}
	
	public void setSpawnPos(double x, double y){
		super.setSpawnPos(x - 16, y + 16);
	}
	
	
	/**
	 * sets the Gridded representation of the current position to (x,y)
	 * @param x
	 * @param y
	 */
	public void setGridPos(int x, int y){
		gridPos.setLocation(x,y);
		pos.setLocation(x*32,y*32);
	}
	
	/**
	 * determines the Gridded representation of the current position
	 * @return the Gridded representation of the current position
	 */
	public Point getGridPos(){
		return new Point(gridPos);
	}
	
	/**
	 * determines if this has an adjacent gridded Thing in the specified direction
	 * @param direction direction to check
	 * @return true if there is a  gridded Thing in the specified direction, false if not
	 */
	public boolean hasAdjacent(byte direction){
		if(direction == FROM_NONE)return false;
		return (adjacents & direction) != 0;
	}

	/**
	 * add other to the list of adjacent blocks
	 * @param other
	 */
	public void addAdjacent(TGridded other){
		byte direction = getDirection(other);
		if(direction == FROM_NONE)return;
		if(!hasAdjacent(direction)){
			adjacents += direction;
		}
	}
	
	/**
	 * remove other from the list of adjacent blocks
	 * @param other
	 */
	public void removeAdjacent(TGridded other){
		byte direction = getDirection(other);
		if(direction == FROM_NONE)return;
		if(hasAdjacent(direction)){
			adjacents -= direction;
		}
	}
	
	public boolean touching(Thing t){
		boolean touching = super.touching(t);
		if(!touching){
			return false;
		}else{
			byte from = fromWhere(t);
			if(hasAdjacent(from))
				return false;
			return true;
		}
	}
	
	public boolean isStatic(){
		return adjacents != 0;
	}
	
	/**
	 * determines the Gridded representation of this and puts it in a new Rectangle
	 * @return the new Rectangle
	 */
	public Rectangle representation(){
		return new Rectangle(gridPos.x,gridPos.y,width/32,height/32);
	}
	
	/**
	 * used to bypass the TGridded method of touching, calls Thing.touch()
	 */
	public boolean supertouching(Thing t){
		return super.touching(t);
	}
	
	/**
	 * determines the direction othergrid is relative to this
	 * @param othergrid the other gridded object
	 * @return the direction othergrid is relative to this
	 */
	public byte getDirection(TGridded othergrid){
		Rectangle other = othergrid.representation();
		if(othergrid instanceof TBGBlock) return FROM_NONE;
		int x = gridPos.x;
		int y = gridPos.y;
		
		//horizontally next to eachother
		Point	up = new Point(x, y + 1),
				down = new Point(x,y-1),
				left = new Point(x - 1, y),
				right = new Point(x + 1, y);
		if(other.contains(up))
			return FROM_ABOVE;
		if(other.contains(down))
			return FROM_BELOW;
		if(other.contains(left))
			return FROM_LEFT;
		if(other.contains(right))
			return FROM_RIGHT;
		return FROM_NONE;
	}
	/**
	 * determines the grid coordinate from a world coordinate
	 * @param x the world coordinate
	 * @return the determined grid coordinate
	 */
	public static int getGridCoord(double x){
		if(x > 0)
			return (int)(x  + 16)/32;
		else
			return (int)(x -16)/32;
	}
	
}