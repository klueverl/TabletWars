/*
 *  @author Jan-Hendrik Kahle
 */

package com.me.tabletwars.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.me.tabletwars.components.ActionPoints;
import com.me.tabletwars.components.Health;
import com.me.tabletwars.components.Render;
import com.me.tabletwars.enums.UNITTYPE;

public class Unit extends GameObject {

	private UNITTYPE unittype;
	private boolean belongsToPlayerOne;
	private int movementRange;
	private Tile occupiedTile;
	public boolean canAttack = true;

	public Unit(){	
		super();
	}
	
	public void setMovementRange(int range){
		this.movementRange = range;
	}
	
	public int getMovementRange(){
		return movementRange;
	}
	
	public void setUnitOwner(boolean belongsToPlayerOne) {
		this.belongsToPlayerOne = belongsToPlayerOne;
	}
	public boolean isOwnedByPlayerOne(){
		return this.belongsToPlayerOne;
	}
	
	public void setUnitType(UNITTYPE unittype) {
		this.unittype = unittype;
	}
	
	public Tile getOccupiedTile() {
		return this.occupiedTile;
	}
	
	public void setOccupiedTile(Tile occupiedTile) {
		if(this.occupiedTile != null)
			this.occupiedTile.removeOccupyUnit();
		this.occupiedTile = occupiedTile;
	}
	
	public UNITTYPE getUnittype() {
		return this.unittype;
	}
	
	public void render(SpriteBatch batch) {
		Render render = (Render)this.getComponent(Render.class.getName());
		Health health = (Health)this.getComponent(Health.class.getName());
		ActionPoints points = (ActionPoints)this.getComponent(ActionPoints.class.getName());
		
		render.render(batch);
		health.renderHealthBar(batch);	
		points.render(batch);
	}
}