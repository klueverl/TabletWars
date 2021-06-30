//@author Lasse Klüver & gamestatemanagement by Jan-Hendrik Kahle
package com.me.tabletwars.manager;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.me.tabletwars.GuiContainer;
import com.me.tabletwars.TabletWars;
import com.me.tabletwars.components.ActionPoints;
import com.me.tabletwars.components.Fortify;
import com.me.tabletwars.components.Render;
import com.me.tabletwars.enums.GAMESTATE;
import com.me.tabletwars.factory.MenuFactory;
import com.me.tabletwars.gameobjects.GuiElement;
import com.me.tabletwars.gameobjects.Unit;

public class RoundHandler {
	public Unit selectedUnit;
	private static GAMESTATE gamestate = GAMESTATE.MENU;
	public boolean playerOnesTurn = true;
	private ActionPointsManager apManager = new ActionPointsManager();
	private PlacementManager map = new PlacementManager(this);
	private AttackManager attackManager = new AttackManager(this, apManager);
	private MovementManager moveManager = new MovementManager(apManager, this);
	public GuiContainer chooseUnitMenu = new GuiContainer();
	public GuiContainer unitTitle = new GuiContainer();
	private MenuFactory guiFactory = new MenuFactory();
	private boolean focusedPlayer;
	private int playerOneRoundCounter = 0;
	private int playerTwoRoundCounter = 0;
	
	public void switchPlayer(CamManager camManager){
		if(playerOnesTurn)
			playerTwoRoundCounter++;				
		else
			playerOneRoundCounter++;
		playerOnesTurn = !playerOnesTurn;
		
		if(gamestate != GAMESTATE.PLACEMENT)
			focusUnitOne(camManager);
		
		apManager.restoreAp();
		resetUsedFortify();
		chooseUnitMenu = guiFactory.createUnitSelectionMenu(playerOnesTurn);
		chooseUnitMenu.addGuiElement(guiFactory.createEndTurnButton());
	}
	
	private void resetUsedFortify(){
		if(playerOneRoundCounter >= 2){
			playerOneRoundCounter = 0;
			resetUsedFortify(TabletWars.player1Units);
			
		}
		else if(playerTwoRoundCounter >= 2){
			playerTwoRoundCounter = 0;
			resetUsedFortify(TabletWars.player2Units);
		}
	}
	
	private void resetUsedFortify(ArrayList<Unit> units){
		for(Unit unit : units){
			Fortify fortify = (Fortify)unit.getComponent("Fortify");
			fortify.setUsedFortify(false);
		}
	}
	
	public void handleActions(CamManager camManager){
		if(gamestate == GAMESTATE.PLACEMENT){
			camManager.switchPlayerCamera(playerOnesTurn);
			map.placement(camManager);
		}
		if(gamestate == GAMESTATE.INGAME){
			if(selectedUnit == null){
				getSelectedUnit(camManager.getCurrentOffset(), camManager);
			}
		}
		if(gamestate == GAMESTATE.SELECTION){
			if(!focusedPlayer){
				focusedPlayer = true;
				camManager.FocusUnit(selectedUnit);
			}
		}
		if(gamestate == GAMESTATE.MOVEMENT){
			if(selectedUnit != null)
			moveManager.handleMovement(selectedUnit, camManager);
		}
		if(gamestate == GAMESTATE.ATTACK){
			if(selectedUnit != null)
			attackManager.handleAttack(selectedUnit, camManager);
		}
		
		if(gamestate == GAMESTATE.RESTART){
			resetGame();
			gamestate = GAMESTATE.PLACEMENT;
		}
		
		if(gamestate == GAMESTATE.RETURN){
			resetGame();
			gamestate = GAMESTATE.MENU;
		}
	}

	public void focusUnitOne(CamManager camManager){
		setNewGameState(GAMESTATE.INGAME, camManager);
		if(playerOnesTurn){
			camManager.FocusUnit(TabletWars.player1Units.get(0));
		}
		else{
			camManager.FocusUnit(TabletWars.player2Units.get(0));
		}
	}
	
	private boolean checkIfNextPlayersTurn(){
		int playersAp = 0;
		boolean nextPlayerturn = false;
		if(playerOnesTurn){
			for(Unit unit: TabletWars.player1Units){
				ActionPoints points = (ActionPoints)unit.getComponent("ActionPoints");
				if(points.getAp() > 0){
					playersAp ++;
				}
			}
			if(playersAp <= 0){
				nextPlayerturn = true;
			}
		}
		else{
			for(Unit unit: TabletWars.player2Units){
				ActionPoints points = (ActionPoints)unit.getComponent("ActionPoints");
				if(points.getAp() > 0){
					playersAp++;
				}
			}
			if(playersAp <= 0){
				nextPlayerturn = true;
			}
		}
		return nextPlayerturn;
	}
	
	private void getSelectedUnit(Vector2 currentCamOffset, CamManager camManager){
		ArrayList<Unit> unitsToCheck = new ArrayList<Unit>();
		if(playerOnesTurn){
			unitsToCheck = TabletWars.player1Units;
		}
		else{
			unitsToCheck = TabletWars.player2Units;
		}
		for(Unit unit:unitsToCheck){
			com.me.tabletwars.components.BoundingBox box = (com.me.tabletwars.components.BoundingBox)unit.getComponent("BoundingBox");
			box.touchFunction(currentCamOffset);
			ActionPoints points = (ActionPoints)unit.getComponent("ActionPoints");
			if(points.getAp() > 0){
				if(box.touched()){
					if(Gdx.input.justTouched()){
						this.selectedUnit = unit;
						setNewGameState(GAMESTATE.SELECTION, camManager);
					}
				}
			}
		}
	}
	//jhkahle
	public static GAMESTATE getCurrentGamestate(){
		return gamestate;
	}
	//jhkahle
	public void setNewGameState(GAMESTATE gamestate, CamManager camManager) {
		focusedPlayer = false;		
		switch (gamestate) {
		case SELECTION:
			selectionStateActions();
			break;
		case MOVEMENT:
			movementStateActions();
			break;
		case ATTACK:
			attackStateActions();
			break;
		case INGAME:
			ingameStateActions(camManager);
			break;
		case FORTIFY:
			fortifyStateActions(camManager);
			break;
		case ZOOM:
			zoomStateActions();
			break;
		case ENDTURN:
			switchPlayer(camManager);
			break;
		case MENU:
			MainLoopManager.curHowToPage = 0;		
		default:
			RoundHandler.gamestate = gamestate;
			break;
		}
	}

	private void zoomStateActions() {
		if(CamManager.zoom)
			CamManager.zoom = false;
		else
			CamManager.zoom = true;
	}

	private void attackStateActions() {
		attackManager.showAttackAreas(selectedUnit);
		RoundHandler.gamestate = GAMESTATE.ATTACK;
	}

	private void ingameStateActions(CamManager camManager) {
		if(selectedUnit != null)
			setCurrentUnitSelection();
		else 
			RoundHandler.gamestate = GAMESTATE.INGAME;
		
		if(checkIfNextPlayersTurn())
			switchPlayer(camManager);
	}

	private void setCurrentUnitSelection() {
		ActionPoints points = (ActionPoints)selectedUnit.getComponent(ActionPoints.class.getName());
		if(points.getAp() > 0)
			selectUnit();
		else 
			deselectUnit();
	}

	private void deselectUnit() {
		selectedUnit.getOccupiedTile().disableGUI();
		selectedUnit = null;
		RoundHandler.gamestate = GAMESTATE.INGAME;
	}

	private void selectUnit() {
		selectedUnit.getOccupiedTile().enableGUI();
		RoundHandler.gamestate = GAMESTATE.SELECTION;
		selectionGUIUpdate();
	}

	private void movementStateActions() {
		RoundHandler.gamestate = GAMESTATE.MOVEMENT;
		if(selectedUnit != null)
			moveManager.showMovementArea(selectedUnit);
		
		moveManager.touched= true;
	}

	private void fortifyStateActions(CamManager camManager) {
		Fortify fortify = (Fortify)selectedUnit.getComponent("Fortify");
		ActionPoints points = (ActionPoints)selectedUnit.getComponent(ActionPoints.class.getName());
		if(points.getAp() >= 2 && !fortify.getUsedFortify())
			fortifySelectedUnit(camManager, fortify, points);
	}

	private void fortifySelectedUnit(CamManager camManager, Fortify fortify,
			ActionPoints points) {
		fortify.setFortify(true);
		Render render = (Render)selectedUnit.getComponent("Render");
		render.playAnimation("Fortify", 0.1f);
		points.reduceAp(2);
		selectedUnit.getOccupiedTile().disableGUI();
		RoundHandler.gamestate = GAMESTATE.INGAME;
		selectedUnit = null;
		
		if(checkIfNextPlayersTurn())
			switchPlayer(camManager);
	}

	private void selectionStateActions() {
		moveManager.clearMovementArea(selectedUnit);
		attackManager.cleanAttackAreas(selectedUnit);
		selectedUnit.getOccupiedTile().enableGUI();
		RoundHandler.gamestate = GAMESTATE.SELECTION;
		unitTitle.clearContainer();
		unitTitle.addGuiContainer(MenuFactory.createUnitMenuTitle(playerOnesTurn, selectedUnit.getUnittype()));
		
		selectionGUIUpdate();
	}

	private void selectionGUIUpdate() {
		Fortify fortify = (Fortify)selectedUnit.getComponent("Fortify");
		GuiElement parentMenuElement = MainLoopManager.getMenuElementByName("Fortify");
		GuiElement disabledElement = parentMenuElement.getChildByName("Fortify-Disable");
		disabledElement.changeGUIactiveState(fortify.getUsedFortify());
		
		ActionPoints points = (ActionPoints)selectedUnit.getComponent(ActionPoints.class.getName());
		if(points.getAp() < 2.0)
			disabledElement.changeGUIactiveState(true);
		
		parentMenuElement = MainLoopManager.getMenuElementByName("Attack");
		disabledElement = parentMenuElement.getChildByName("Attack-Disable");
		disabledElement.changeGUIactiveState(!selectedUnit.canAttack);
	}
	
	private void resetGame(){
		for(Unit unit : TabletWars.units){
			unit.getOccupiedTile().removeOccupyUnit();
		}
		TabletWars.units.clear();
		TabletWars.player1Units.clear();
		TabletWars.player2Units.clear();
		this.map = new PlacementManager(this);
		this.map.setPlacementcounter(0);
	}
}
