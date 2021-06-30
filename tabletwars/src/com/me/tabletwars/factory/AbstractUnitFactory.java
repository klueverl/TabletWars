/*
 * @author Jan-Hendrik Kahle
 */
package com.me.tabletwars.factory;

import com.badlogic.gdx.math.Vector2;
import com.me.tabletwars.enums.UNITTYPE;
import com.me.tabletwars.gameobjects.Unit;

public abstract class AbstractUnitFactory {
	
	protected UNITTYPE unittype;
	
	public abstract Unit create(Vector2 position, boolean belongsToPlayerOne);
	public UNITTYPE getUnittype() {
		return unittype;
	}
}
