//@author Lasse Klüver
package com.me.tabletwars.manager;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.me.tabletwars.ContentLoader;
import com.me.tabletwars.GuiContainer;
import com.me.tabletwars.TabletWars;
import com.me.tabletwars.components.BoundingBox;
import com.me.tabletwars.components.Transform;
import com.me.tabletwars.factory.AbstractUnitFactory;
import com.me.tabletwars.factory.FlameFactory;
import com.me.tabletwars.factory.MenuFactory;
import com.me.tabletwars.factory.ShotgunFactory;
import com.me.tabletwars.factory.SniperFactory;
import com.me.tabletwars.factory.SoldierFactory;
import com.me.tabletwars.gameobjects.Tile;
import com.me.tabletwars.gameobjects.Unit;

public class PlacementManager{
	private static int placementcounter = 0;
	private int amountOfUnitsToPlace = 8;
	private RoundHandler handler;
	public static GuiContainer placementContainer  = new GuiContainer();
	
	public void setPlacementcounter(int i){
		placementcounter = i;
	}
	
	
	public PlacementManager(RoundHandler handler){
		this.handler = handler;
	}
	
	ArrayList<Tile> getSelectionArea(){
		int selectionAreaSize = 4;
		ArrayList<Tile> tempArrayList = new ArrayList<Tile>();
		for(int x = 13; x <= 20; x++){
			for(int y = 0; y < selectionAreaSize; y++){
				if(handler.playerOnesTurn){
					tempArrayList.add(ContentLoader.getInstance().mapTiles[x][y]);
					ContentLoader.getInstance().mapTiles[x][y].enableGUI();
				}
				else{
					tempArrayList.add(ContentLoader.getInstance().mapTiles[x][ContentLoader.getInstance().mapSizeY - 1 -y]);
					ContentLoader.getInstance().mapTiles[x][ContentLoader.getInstance().mapSizeY - 1 -y].enableGUI();
				}
			}
		}
		return tempArrayList;
	}
	
	AbstractUnitFactory getCurrentFactory(){
		if(placementcounter < 2){
			return new SoldierFactory();
		}
		else if(placementcounter < 4){
			return new ShotgunFactory();
		}
		else if(placementcounter < 6){
			return new SniperFactory();
		}
		else if(placementcounter <= 8){
			return new FlameFactory();
		}
		return null;
	}
	
	void switchGamestateAfterPlacement(CamManager camManager){
		if(placementcounter >= amountOfUnitsToPlace){
			ArrayList<Tile> selectionArea = getSelectionArea();
			for (Tile tile : selectionArea) {
				tile.disableGUI();
			}
			handler.focusUnitOne(camManager);
		}
	}
	
	public void placement(CamManager camManager){
		camManager.switchPlayerCamera(handler.playerOnesTurn);
		ArrayList<Tile> selectionArea = getSelectionArea();
		switchGamestateAfterPlacement(camManager);
		placementContainer.clearContainer();
		placementContainer.addGuiContainer(MenuFactory.createPlaceUnitUI(handler.playerOnesTurn, getCurrentFactory().getUnittype()));
		for(Tile currentTile:selectionArea){
			BoundingBox currentBoundingBox = (BoundingBox)currentTile.getComponent("BoundingBox");
			Transform currentTransform = (Transform)currentTile.getComponent("Transform");
			currentBoundingBox.touchFunction(camManager.getCurrentOffset());
			if(Gdx.input.justTouched()) {
				if(currentBoundingBox.touched() && !currentTile.isObstacle()){
					buildUnit(currentTile, currentTransform);	
					stopRenderSelectionArea(selectionArea);
					placementcounter++;
					handler.switchPlayer(camManager);
					break;
				}
			}
		}
	}

	private void stopRenderSelectionArea(ArrayList<Tile> selectionArea){
		for (Tile tile : selectionArea) {
			tile.disableGUI();
		}
	}
	private void buildUnit(Tile currentTile,Transform currentTransform) {
		Unit tempUnit = (Unit) getCurrentFactory().create(new Vector2(currentTransform.getPosition().x,currentTransform.getPosition().y), handler.playerOnesTurn);
		tempUnit.setOccupiedTile(currentTile);
		currentTile.setOccupyUnit(tempUnit);
		TabletWars.units.add(tempUnit);
		if(handler.playerOnesTurn){	
			TabletWars.player1Units.add(tempUnit);
		}
		else{
			TabletWars.player2Units.add(tempUnit);
		}
	}
}
