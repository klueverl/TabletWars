//@author : Lasse Klüver
package com.me.tabletwars.manager;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.me.tabletwars.TabletWars;
import com.me.tabletwars.components.ActionPoints;
import com.me.tabletwars.components.Attack;
import com.me.tabletwars.components.BoundingBox;
import com.me.tabletwars.components.Render;
import com.me.tabletwars.components.SoundComponent;
import com.me.tabletwars.components.Transform;
import com.me.tabletwars.enums.DIRECTION;
import com.me.tabletwars.enums.GAMESTATE;
import com.me.tabletwars.enums.UNITTYPE;
import com.me.tabletwars.gameobjects.Projectile;
import com.me.tabletwars.gameobjects.Tile;
import com.me.tabletwars.gameobjects.Unit;

public class AttackManager {
	private RoundHandler handler;
	private ActionPointsManager manager;
	private final static float flamethrowerAnimationDuration = 2;
	private final static float soldierAnimationDuration = 0.5f;
	private final static float shotgunAnimationDuration = 0.1f;
	private final static float sniperAnimationDuration = 0.1f;
	
	public AttackManager(RoundHandler handler, ActionPointsManager manager){
		this.handler = handler;
		this.manager = manager;
	}
	
	void handleAttack(Unit attackingUnit, CamManager camManager){
		if(manager.checkIfHasAp(attackingUnit) && attackingUnit.canAttack){
			ArrayList<Unit> targets = new ArrayList<Unit>();
			targets = getTargets(attackingUnit, camManager);
			if(targets.size() > 0){
				playUnittypeDependendAnimation(attackingUnit);
				SoundComponent sound = (SoundComponent)attackingUnit.getComponent("SoundComponent");
				sound.play(0.3f);
				Attack attack = (Attack)attackingUnit.getComponent("Attack");
				attack.attack(targets);
				attackingUnit.canAttack = false;
				cleanAttackAreas(attackingUnit);
				ActionPoints points = (ActionPoints)attackingUnit.getComponent("ActionPoints");
				points.reduceAp(1);
				if(checkGameOver()){
					handler.setNewGameState(GAMESTATE.GAMEOVER, camManager);
				}
				else{
					handler.setNewGameState(GAMESTATE.INGAME, camManager);
				}
			}
		}
	}
	private boolean checkGameOver(){
		boolean temp = false;
		if(TabletWars.player1Units.size()== 0){
			temp = true;
			TabletWars.gameOver = false;
		}
		else if(TabletWars.player2Units.size()== 0){
			temp = true;
			TabletWars.gameOver = true;
		}
		return temp;
	}
	
	private void playUnittypeDependendAnimation(Unit attackingUnit){
		UNITTYPE type = attackingUnit.getUnittype();
		float animationDuration = 0;
		
		if(type == UNITTYPE.FLAMETHROWER){
			animationDuration = flamethrowerAnimationDuration;
			@SuppressWarnings("unused")
			Projectile projectile = new Projectile(attackingUnit, flamethrowerAnimationDuration);
		}
		else if(type == UNITTYPE.SHOTGUN){
			animationDuration = shotgunAnimationDuration;
		}
		else if(type == UNITTYPE.SNIPER){
			animationDuration = sniperAnimationDuration;
		}
		else if(type == UNITTYPE.SOLDIER){
			animationDuration = soldierAnimationDuration;
		}
		if(type != UNITTYPE.FLAMETHROWER)
		playAnimation(attackingUnit, animationDuration);
	}
	
	private void playAnimation(Unit attackingUnit, float duration){
		Render render = (Render)attackingUnit.getComponent("Render");
		render.playAnimation("shoot", 0.1f, duration);
	}
	
	private ArrayList<Unit> getTargets(Unit attackingUnit, CamManager camManager){
		ArrayList<Unit> units = new ArrayList<Unit>();
		for(ArrayList<Tile>attackArea : getAttackAreas(attackingUnit)){
			for(Tile currentTile : attackArea){
				BoundingBox boundingBox = (BoundingBox)currentTile.getComponent("BoundingBox");
				boundingBox.touchFunction(camManager.getCurrentOffset());
				if(boundingBox.touched()){
					Transform transform = (Transform)attackingUnit.getComponent("Transform");
					if(Gdx.input.justTouched()){
						if(attackingUnit.getUnittype() == UNITTYPE.FLAMETHROWER || attackingUnit.getUnittype() == UNITTYPE.SHOTGUN){
							units = getUnitsOnTiles(attackArea);
						}
						else{
							Unit unit = currentTile.getOccupyUnit();
							if(unit != null){
								units.add(currentTile.getOccupyUnit());
							}
						}
				
						for(DIRECTION dir : DIRECTION.values()){
							if(getAttackAreaInDirection(attackingUnit, dir).size() != 0 && attackArea.size() != 0){
								if(getAttackAreaInDirection(attackingUnit, dir).get(0).getObjectID() == attackArea.get(0).getObjectID()){
									if(dir == DIRECTION.RIGHT){
										transform.rotation = 270;
									}
									else if(dir == DIRECTION.LEFT){
										transform.rotation = 90;
									}
									else if(dir == DIRECTION.UP){
										transform.rotation = 0;
									}
									else if(dir == DIRECTION.DOWN){
										transform.rotation = 180;
									}
								}
							}
						}
					}
				}
			}
		}
		return units;
	}
	
	private ArrayList<Unit> getUnitsOnTiles(ArrayList<Tile> tiles){
		ArrayList<Unit> units = new ArrayList<Unit>();
		for(Tile tile : tiles){
			Unit unit = tile.getOccupyUnit();
			if(unit != null){
				units.add(unit);
			}
		}
		return units;
	}
	
	public void showAttackAreas(Unit attackingUnit){
		for(ArrayList<Tile> attackArea: getAttackAreas(attackingUnit)){
			for(Tile currentTile : attackArea){
				currentTile.enableGUI();
			}
		}
	}

	public void cleanAttackAreas(Unit attackingUnit){
		for(ArrayList<Tile> attackArea: getAttackAreas(attackingUnit)){
			for(Tile currentTile : attackArea){
				currentTile.disableGUI();
			}
		}
		attackingUnit.getOccupiedTile().disableGUI();
	}
	
	
	ArrayList<ArrayList<Tile>> getAttackAreas(Unit attackingUnit){
		ArrayList<ArrayList<Tile>> tempList = new ArrayList<ArrayList<Tile>>();
		tempList.add(getAttackAreaInDirection(attackingUnit, DIRECTION.UP));
		tempList.add(getAttackAreaInDirection(attackingUnit, DIRECTION.DOWN));
		tempList.add(getAttackAreaInDirection(attackingUnit, DIRECTION.RIGHT));
		tempList.add(getAttackAreaInDirection(attackingUnit, DIRECTION.LEFT));
		return tempList;
	}

	ArrayList<Tile> getAttackAreaInDirection(Unit attackingUnit, DIRECTION dir){
		Tile startTile = attackingUnit.getOccupiedTile();
		ArrayList<Tile> tempList = new ArrayList<Tile>();
		tempList = startTile.getAttackArea(attackingUnit.getUnittype(), dir);
		return tempList;
	}
}

