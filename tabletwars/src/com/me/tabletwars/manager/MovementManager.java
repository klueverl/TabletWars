//@author Lasse KLüver
package com.me.tabletwars.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.me.tabletwars.ContentLoader;
import com.me.tabletwars.components.ActionPoints;
import com.me.tabletwars.components.Render;
import com.me.tabletwars.components.SoundComponent;
import com.me.tabletwars.components.TilebasedCharacterController;
import com.me.tabletwars.components.Transform;
import com.me.tabletwars.enums.GAMESTATE;
import com.me.tabletwars.gameobjects.Tile;
import com.me.tabletwars.gameobjects.Unit;

public class MovementManager {
	private ActionPointsManager apManager;
	private Tile targetTile;
	private RoundHandler handler;
	public boolean touched;
	private SoundComponent soundComponent;
	
	public MovementManager(ActionPointsManager apManager, RoundHandler handler){
		this.apManager = apManager;
		this.handler = handler;
		soundComponent = new SoundComponent(ContentLoader.getInstance().getFootStepSound());
	}
	
	public void handleMovement(Unit unitToMove, CamManager camManager){
			if(targetTile == null && apManager.checkIfHasAp(unitToMove)){
				targetTile = getMovementTargetTile(unitToMove, camManager.getCurrentOffset());
			}
			if(targetTile != null){
				Render render = (Render)unitToMove.getComponent("Render");
				render.playAnimation("walk", 0.1f);
				clearMovementArea(unitToMove);
				performMovement(unitToMove, camManager);
			}
	}
	
	void performMovement(Unit movingUnit, CamManager camManager){
		TilebasedCharacterController controller = (TilebasedCharacterController)movingUnit.getComponent("TilebasedCharacterController");
		controller.move(movingUnit.getOccupiedTile(), targetTile);
		movingUnit.getOccupiedTile().setObstacle(false);
		com.me.tabletwars.components.Transform tr = (com.me.tabletwars.components.Transform)targetTile.getComponent("Transform");
		if(controller.checkIfTargetReached(tr.getPosition())){
			Render render = (Render)movingUnit.getComponent("Render");
			render.stopAnimation();
			soundComponent.stop();
			movingUnit.setOccupiedTile(targetTile);
			movingUnit.removeComponent("TilebasedCharacterController");
			Transform trans = (Transform)movingUnit.getComponent("Transform");
			movingUnit.addComponent(new TilebasedCharacterController(trans));
			targetTile.setOccupyUnit(movingUnit);
			ActionPoints points = (ActionPoints)movingUnit.getComponent("ActionPoints");
			points.reduceAp(1);
			targetTile = null;
			MainLoopManager.menuContainer.enableGUI(true);
			handler.setNewGameState(GAMESTATE.INGAME, camManager);
		}
	}
	
	Tile getMovementTargetTile(Unit movingUnit, Vector2 currentCameraOffset){
		int movementRange = movingUnit.getMovementRange();
		if(!Gdx.input.isTouched()){
			touched = false;
		}
		for(Tile currentTile:movingUnit.getOccupiedTile().getTilesInMovementRange(movementRange)){
			com.me.tabletwars.components.BoundingBox box = (com.me.tabletwars.components.BoundingBox)currentTile.getComponent("BoundingBox");
			box.touchFunction(currentCameraOffset);
			if(box.touched()){
				if(Gdx.input.justTouched()){
					if(!touched){
						soundComponent.loop(1f);
						MainLoopManager.menuContainer.enableGUI(false);
						return currentTile;
					}
				}
			}
		}
		return null;
	}
	
	public void showMovementArea(Unit movingUnit){
		int movementRange = movingUnit.getMovementRange();
		Tile tile = movingUnit.getOccupiedTile();
		for(Tile currentTile : tile.getTilesInMovementRange(movementRange)){
			currentTile.enableGUI();
		}
	}
	
	public void clearMovementArea(Unit movingUnit){
		int movementRange = movingUnit.getMovementRange();
		for (Tile tile : movingUnit.getOccupiedTile().getTilesInMovementRange(movementRange)) {
			tile.disableGUI();
		}
		movingUnit.getOccupiedTile().disableGUI();
	}
}
