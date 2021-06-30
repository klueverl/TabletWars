//@author Lasse Klüver
package com.me.tabletwars.manager;

import com.me.tabletwars.TabletWars;
import com.me.tabletwars.components.ActionPoints;
import com.me.tabletwars.gameobjects.Unit;

public class ActionPointsManager {
	
	public void restoreAp(){
		for(Unit unit:TabletWars.units){
			ActionPoints points = (ActionPoints)unit.getComponent("ActionPoints");
			points.restoreAp();
			resetCanAttack(unit);
		}
	}
	
	private void resetCanAttack(Unit unit){
		unit.canAttack = true;
	}
	
	public boolean checkIfHasAp(Unit unit){
		boolean hasAp = false;
		ActionPoints points = (ActionPoints)unit.getComponent("ActionPoints");
		if(points.getAp() > 0){
			hasAp = true;
		}
		return hasAp;
	}
}
