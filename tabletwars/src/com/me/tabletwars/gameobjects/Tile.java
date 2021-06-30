/*
 * @author Jan-Hendrik Kahle
 */
package com.me.tabletwars.gameobjects;

import java.util.ArrayList;
import java.util.HashSet;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.me.tabletwars.components.Render;
import com.me.tabletwars.enums.DIRECTION;
import com.me.tabletwars.enums.UNITTYPE;
import com.me.tabletwars.manager.RoundHandler;

public class Tile extends GameObject {

	protected Tile leftTile;
	protected Tile rightTile;
	protected Tile upperTile;
	protected Tile downTile;
	protected GuiElement gui;
	private Unit occupyingUnit;
	private boolean obstacle;
	private static boolean obstacleFound = false;
	private static boolean attackAreaSearch = false;
	private int distanceTraveled;
	
	public Tile() {
		super();
	}
	
	public void setGUI(GuiElement element){
		this.gui = element;
	}
	
	public void enableGUI() {
		this.gui.changeGUIactiveState(true);
	}
	
	public void disableGUI() {
		this.gui.changeGUIactiveState(false);
	}
	
	public void render(SpriteBatch batch, RoundHandler handler) {
		Render render = (Render) this.getComponent(Render.class.getName());
		render.render(batch);
		if(!this.obstacle || (this.obstacle && this.occupyingUnit != null))
			gui.render(batch, Vector2.Zero, handler);
	}
	
	public void setObstacle(boolean obstacle) {
		this.obstacle = obstacle;
		if(this.obstacle)
		{
			Render render = (Render)this.getComponent("Render");
			render.active = false;
		}
	}
				
	public boolean isObstacle() {
		return this.obstacle;
	}
	
	public void  setNeighborTile(DIRECTION direction, Tile tile) {
		switch (direction) {
		case DOWN:
			this.downTile = tile;
			break;
		case LEFT:
			this.leftTile = tile;
			break;
		case RIGHT:
			this.rightTile = tile;
			break;
		case UP:
			this.upperTile = tile;
			break;
		default:
			break;	
		}
	}
	
	public Tile getNeighborTile(DIRECTION direction) {
		
		Tile tile;
		switch(direction) {
		case DOWN:
			tile = this.downTile;
			break;
		case LEFT:
			tile = this.leftTile;
			break;
		case RIGHT:
			tile = this.rightTile;
			break;
		case UP:
			tile = this.upperTile;
			break;
		default:
			tile = null;
			break;
		}		
		return tile;
	}
	
	public DIRECTION getOppositeDirection(DIRECTION direction) {
		
		DIRECTION oppositDirection;
		switch(direction)
		{
		case DOWN:
			oppositDirection = DIRECTION.UP;
			break;
		case LEFT:
			oppositDirection = DIRECTION.RIGHT;	
			break;
		case RIGHT:
			oppositDirection = DIRECTION.LEFT;
			break;
		case UP:
			oppositDirection = DIRECTION.DOWN;
			break;
		default:
			oppositDirection = null;
		}		
		return oppositDirection;
	}
	
	public Unit getOccupyUnit() {
		return this.occupyingUnit;		
	}
	
	public void setOccupyUnit(Unit occupyingUnit) {
		this.occupyingUnit = occupyingUnit;
		this.obstacle = true;
	}
	
	public void removeOccupyUnit() {
		this.occupyingUnit = null;
		this.obstacle = false;
	}
	
	/* <summary>
	 * get all Tiles,while avoiding obstacles, in movementrange from origintile
	 * </summary>
	 */
	public ArrayList<Tile> getTilesInMovementRange(int movementRange)
	{
		ArrayList<Tile> tileList = new ArrayList<Tile>();
			
		movementRange--;
		
		createListOfTiles(tileList, movementRange);
				
		return tileList;
	}
	
	/* <summary>
	 * Get all Tiles within the Unit-specific Area, flamethrower partly ignores obstacles.
	 */
	public ArrayList<Tile> getAttackArea(UNITTYPE type, DIRECTION direction) {
		
		ArrayList<Tile> attackAreaList = new ArrayList<Tile>();
		
		switch (type) {
		case FLAMETHROWER:
			attackAreaList = flamethrowerArea(direction);
			break;
		case SHOTGUN:
			attackAreaList = shotgunArea(direction);
			break;
		case SNIPER:
			attackAreaList = sniperArea(direction);
			break;
		case SOLDIER:
			attackAreaList = soldierArea(direction);
			break;
		default:
			attackAreaList = null;
			break;
		}	
		
		return attackAreaList;
	}
	
	/* <summary>
	 * tries to add Tile to tileList, an obstacle returns a false boolean.
	 * </summary>
	 */
	private boolean addToList(ArrayList<Tile> tileList) {
		if(!this.obstacle)
		{
			tileList.add(this);
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/* <summary>
	 * Returns all tiles within a 3x2 field 1 field away from "cast"-Tile	
	 * </summary>
	 * 		 [][]
	 * 	[x]	 [][]
	 * 		 [][]
	 */	
	private ArrayList<Tile> flamethrowerArea(DIRECTION direction) {
		
		Tile tempTile1 = null;
		Tile tempTile2 = null;
		DIRECTION[] dir = DIRECTION.values();
		ArrayList<Tile> tileList = new ArrayList<Tile>();
		
		if(this.getNeighborTile(direction) != null) {
			
			if(!this.getNeighborTile(direction).obstacle) {
				if(addToListWithObstacles(tileList, this.getNeighborTile(direction).getNeighborTile(direction))) {
					
					tempTile1 = this.getNeighborTile(direction).getNeighborTile(direction);
					
					if (addToListWithObstacles(tileList,tempTile1.getNeighborTile(direction))) {
						
						tempTile2 = tempTile1.getNeighborTile(direction);
					}
					
					for (DIRECTION direction2 : dir) {
						if(direction2 != direction && direction2 != getOppositeDirection(direction))
						{
							if(tempTile1 != null)
							addToListWithObstacles(tileList, tempTile1.getNeighborTile(direction2));
							if(tempTile2 != null)
							addToListWithObstacles(tileList, tempTile2.getNeighborTile(direction2));
						}
					}
				}
			}
		}
		return tileList;
	}
		
	/* <summary>
	 * Returns all tiles within a T-shape from "cast"-Tile 
	 * </summary>
	 * 	     []
	 * 	[x][][]
	 * 	     []
	 */	
	private ArrayList<Tile> shotgunArea(DIRECTION direction)
	{	
		Tile tempTile = null;
		DIRECTION[] dir = DIRECTION.values();
		ArrayList<Tile> tileList = new ArrayList<Tile>();
		
		if(this.getNeighborTile(direction) != null) {
			
			if((this.getNeighborTile(direction).obstacle &&  this.getNeighborTile(direction).getOccupyUnit() != null) || !this.getNeighborTile(direction).obstacle ) {
				
				if(addToListWithObstacles(tileList, this.getNeighborTile(direction))) {
					
					tempTile = this.getNeighborTile(direction);
					if(addToListWithObstacles(tileList, tempTile.getNeighborTile(direction))) {
						
						tempTile = tempTile.getNeighborTile(direction);
						
						for (DIRECTION direction2 : dir) {
							
							if(direction2 != direction && direction2 != getOppositeDirection(direction)) {
								
								addToListWithObstacles(tileList, tempTile.getNeighborTile(direction2));
							}
						}
					}
				}
			}
		}
		return tileList;
	}
	
	/* <summary>
	 * shoots in straight line, can't shoot through obstacles
	 * </summary>
	 */	
	private ArrayList<Tile> soldierArea(DIRECTION direction) {
		attackAreaSearch = true;
		ArrayList<Tile> tileList = addTileInDirection(4, direction);
		attackAreaSearch = false;
		return tileList;
	}
	
	/* <summary>
	 * Skips first 3 Tiles, then like soldier, can't shoot through obstacles
	 * </summary>
	 */
	private ArrayList<Tile> sniperArea(DIRECTION direction) {
		Tile startTile = this;
		ArrayList<Tile> tileList = new ArrayList<Tile>();
		
		for(int i = 0; i < 3; i++) {
			if(startTile.getNeighborTile(direction) != null)
				if (startTile.getNeighborTile(direction).obstacle) {
					startTile = null;
					break;
				}
				else {
					startTile = startTile.getNeighborTile(direction);
				}	
			else {
				break;
			}
		}		
		if(startTile != null) {
			attackAreaSearch = true;
			tileList = startTile.addTileInDirection(4, direction);	
			attackAreaSearch = false;
		}
		return tileList;
	}
	
	private boolean addToListWithObstacles(ArrayList<Tile> tileList, Tile tile) {
		
		if(tile != null)
		{
			tileList.add(tile);
			return true;
		}
		return false;
	}
	
	/* <summary>
	 * getTilesInMovementRange : Returns a list with all Tiles in one direction (-obstacles).
	 * </summary>
	 */
	private ArrayList<Tile>  addTileInDirection(int distance, DIRECTION direction) {
		ArrayList<Tile> tempTiles = new ArrayList<Tile>();
		if(this.getNeighborTile(direction) != null)
		{
			if(distance > 0)
			{
				if(!this.getNeighborTile(direction).addToList(tempTiles))
				{
					if(attackAreaSearch) {
						if(this.getNeighborTile(direction).occupyingUnit != null) {
							addToListWithObstacles(tempTiles, this.getNeighborTile(direction));
						}
						distance = 0;
					}
					else {
						distance = 0;
						obstacleFound = true;
					}
				}
				else {
					this.getNeighborTile(direction).distanceTraveled = distance;
					distance--;
				}			
				tempTiles.addAll(this.getNeighborTile(direction).addTileInDirection(distance, direction));
			}
		}
		else {
			obstacleFound = true;
		}
		return tempTiles;
	}
	
	/* <summary>
	 * getTilesInReach : Collects all Tiles in tileList, 
	 * if an obstacle is found while adding Tiles in counter-clockwise movement
	 * the movement is reversed and done again.
	 * Doubled tiles added this way are removed at the end.
	 */
	private void createListOfTiles(ArrayList<Tile> tileList, int distance) {
	
		tileList.addAll(addTilesInArea(distance, DIRECTION.UP, DIRECTION.LEFT));
		tileList.addAll(addTilesInArea(distance, DIRECTION.DOWN, DIRECTION.RIGHT));
		tileList.addAll(addTilesInArea(distance, DIRECTION.RIGHT, DIRECTION.UP));
		tileList.addAll(addTilesInArea(distance, DIRECTION.LEFT, DIRECTION.DOWN));
		
		if(obstacleFound)
		{
			tileList.addAll(addTilesInArea(distance, DIRECTION.UP, DIRECTION.RIGHT));
			tileList.addAll(addTilesInArea(distance, DIRECTION.DOWN, DIRECTION.LEFT));
			tileList.addAll(addTilesInArea(distance, DIRECTION.RIGHT, DIRECTION.DOWN));
			tileList.addAll(addTilesInArea(distance, DIRECTION.LEFT, DIRECTION.UP));
			
			removeDuplicates(tileList);
			obstacleFound = false;
		}
	}
	
	/* <summary>
	 * getTilesInReach : First adds Tiles in originalDirection, 
	 * then adds Tiles in secondDirection from the already added Tiles.
	 * </summary>
	 */
	private ArrayList<Tile> addTilesInArea(int distance, DIRECTION originalDirection, DIRECTION secondDirection) {
		
		ArrayList<Tile> tiles = new ArrayList<Tile>();

		if(this.getNeighborTile(originalDirection) != null)
		{
			this.getNeighborTile(originalDirection).addToList(tiles);
			ArrayList<Tile> tempList = new ArrayList<Tile>();
			
			if (this.getNeighborTile(originalDirection).obstacle) {
				distance = 0;
				obstacleFound = true;
			}
					
			tempList.addAll(this.getNeighborTile(originalDirection).addTileInDirection(distance, originalDirection));
			
			tiles.addAll(tempList);
			tiles.addAll(addCCTiles(tiles, distance, secondDirection, originalDirection));
		}
		else
		{
			obstacleFound = true;
		}
		return tiles;
	}
	
	/* <summary>
	 * getTilesInReach : Adds every Tile(in reach) in direction starting from every Tile that is already in tileList.
	 * </summary>
	 */
	private ArrayList<Tile> addCCTiles(ArrayList<Tile> tileList, int distance, DIRECTION direction, DIRECTION originalDirection) {
		
		ArrayList<Tile> tempList = new ArrayList<Tile>();
		ArrayList<Tile> finalList = new ArrayList<Tile>();
			
		int tempdist = distance;
		for (Tile tile : tileList) {
			tempList.addAll(tile.addTileInDirection(tempdist, direction));
			tempdist--;			
		}
		for (Tile tile : tempList) {
			finalList.addAll(tile.addTileInDirection(tile.distanceTraveled-1, originalDirection));
			finalList.addAll(tile.addTileInDirection(tile.distanceTraveled-1, getOppositeDirection(originalDirection)));
		}
		finalList.addAll(tempList);
		return finalList;
	}	
	
	/* <summary>
	 * removes Duplicate Tiles from the parameter list
	 * </summary>
	 */
	private static void removeDuplicates(ArrayList<Tile> arrayList) {
		HashSet<Tile> hashSet = new HashSet<Tile>(arrayList);
		arrayList.clear();
		arrayList.addAll(hashSet);
	}
	
}

