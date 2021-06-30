//@author Lasse Klüver
package com.me.tabletwars;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.me.tabletwars.gameobjects.GameObject;
import com.me.tabletwars.gameobjects.Unit;

public class Player extends GameObject {

	private ArrayList<Unit> ownedUnits = new ArrayList<Unit>();
	
	private int curSelected = 0;
	
	public Player() {
		super();
	}
	
	public Unit getUnitByID(int id) {
		Unit u = null;
		for (Unit unit : this.ownedUnits) 
			if(unit.getObjectID() == id)
				u = unit;
		return u;
	}
	
	public void addUnit(Unit unit) {
		this.ownedUnits.add(unit);
	}
	
	public void removeUnit(Unit unit) {
		this.curSelected = -1;
		this.ownedUnits.remove(unit);
	}
	
	public ArrayList<Unit> getUnitList() {
		return this.ownedUnits;
	}
	
	public Unit getNextSelectedUnit() {
		if(curSelected + 1 < ownedUnits.size())
			this.curSelected++;
		else
			this.curSelected = 0;
		
		return this.ownedUnits.get(this.curSelected);
	}
	
	public void Render(SpriteBatch batch) {
		for (Unit unit : this.ownedUnits) 
			unit.render(batch);
	}
}
