//@author Lasse Klüver
package com.me.tabletwars.components;
import java.util.ArrayList;

import com.me.tabletwars.enums.DIRECTION;
import com.me.tabletwars.enums.UNITTYPE;
import com.me.tabletwars.gameobjects.Tile;
import com.me.tabletwars.gameobjects.Unit;

public class Attack extends Component {
	private int dmg;
	private final static float boniMultiplicator = 1.5f;
	
	public Attack(int dmg){
		this.dmg = dmg;
	}
	
	public void attack(ArrayList<Unit> targets) {
		Unit parentUnit = (Unit)this.parentObject;
		UNITTYPE parentUnittype = parentUnit.getUnittype();
		
		for(Unit target:targets) {
			if(target != null){
				Fortify fortify = (Fortify)target.getComponent("Fortify");
				if(!fortify.getFority()){
					Health currentTargetsHealth = (Health)target.getComponent("Health");
					currentTargetsHealth.reduceHealth(this.calculateDmg(parentUnittype, target));
				}
				else{
					fortify.setFortify(false);
					Render render = (Render)target.getComponent("Render");
					render.stopAnimation();
				}
			}
		}
	}	
	/*summary
	 * calculates unittype specific damage and returns it
	 */
	int calculateDmg(UNITTYPE attackingUnitsUnittype, Unit targetUnit){
		UNITTYPE targetsUnittype = targetUnit.getUnittype();
		int tempDmg = this.dmg;
		if(attackingUnitsUnittype == UNITTYPE.FLAMETHROWER){
			if(targetsUnittype == UNITTYPE.SNIPER){
				tempDmg =  (int)(this.dmg * Attack.boniMultiplicator);
			}
		}
		else if(attackingUnitsUnittype == UNITTYPE.SHOTGUN){
			Unit shotGunner = (Unit)this.parentObject;
			for(DIRECTION  dir : DIRECTION.values()){
				Tile tile = shotGunner.getOccupiedTile().getNeighborTile(dir);
				if(tile.getOccupyUnit() == targetUnit){
					tempDmg *= Attack.boniMultiplicator;
				}
			}
			if(targetsUnittype == UNITTYPE.FLAMETHROWER){
				tempDmg *= Attack.boniMultiplicator;
			}
		}
		else if(attackingUnitsUnittype == UNITTYPE.SNIPER){
			if(targetsUnittype == UNITTYPE.SOLDIER){
				tempDmg = (int)(this.dmg * Attack.boniMultiplicator);
			}
		}
		else if(attackingUnitsUnittype == UNITTYPE.SOLDIER){
			if(targetsUnittype == UNITTYPE.SHOTGUN){
				tempDmg = (int)(this.dmg * Attack.boniMultiplicator);
			}
		}
		return tempDmg;
	}
	
	
}
