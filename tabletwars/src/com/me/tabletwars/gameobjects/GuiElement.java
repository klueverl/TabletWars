/*
 * @author Jan-Hendrik Kahle
 */
package com.me.tabletwars.gameobjects;

import java.util.ArrayList;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.me.tabletwars.Player;
import com.me.tabletwars.TabletWars;
import com.me.tabletwars.components.ActionPoints;
import com.me.tabletwars.components.BoundingBox;
import com.me.tabletwars.components.Render;
import com.me.tabletwars.components.Transform;
import com.me.tabletwars.enums.GAMESTATE;
import com.me.tabletwars.enums.UNITTYPE;
import com.me.tabletwars.manager.CamManager;
import com.me.tabletwars.manager.RoundHandler;

public class GuiElement extends GameObject {

	private GAMESTATE renderGamestate;
	private GAMESTATE followingGamestate;
	private UNITTYPE unitToSelect = null;
	private Unit nextSeletedUnit = null;
	private ArrayList<GuiElement> childElements = new ArrayList<GuiElement>();
	private boolean guiActive = true;
	
	public GuiElement() {
		super();
	}
	
	public GuiElement(GAMESTATE renderGamestate, GAMESTATE followingGamestate)
	{
		super();
		this.followingGamestate = followingGamestate;
		this.renderGamestate = renderGamestate;
	}
	
	public GuiElement(UNITTYPE unitToSelect) {
		super();
		this.followingGamestate = GAMESTATE.SELECTION;
		this.renderGamestate = GAMESTATE.INGAME;
		this.unitToSelect = unitToSelect;
	}
	
	public GuiElement(Unit unit) {
		super();
		this.followingGamestate = GAMESTATE.SELECTION;
		this.renderGamestate = GAMESTATE.INGAME;
		this.nextSeletedUnit = unit;
	}
	
	public void setNewRenderState(GAMESTATE renderState) {
		this.renderGamestate = renderState;
	}
	
	public void attachChildElement(GuiElement element)
	{
		this.childElements.add(element);
	}
	
	public void render(SpriteBatch batch, Vector2 currentCamOffset, RoundHandler handler)
	{
		if(guiActive) 
			renderGUI(batch, currentCamOffset, handler);
	}

	private void renderGUI(SpriteBatch batch, Vector2 currentCamOffset,
			RoundHandler handler) {
		if(this.unitToSelect != null) 
			activateInactiveButton(handler);

		if(RoundHandler.getCurrentGamestate() == this.renderGamestate) 
			positionGuiElementAndRender(batch, currentCamOffset);
		
		if(this.childElements != null) 
			for (GuiElement child : this.childElements) 
				child.render(batch, currentCamOffset, handler);
	}

	private void positionGuiElementAndRender(SpriteBatch batch,
			Vector2 currentCamOffset) {
		Transform transform =(Transform)this.getComponent(Transform.class.getName());
		Vector2 relativePosition = transform.position;
		transform.position = new Vector2(transform.position.x + currentCamOffset.x, transform.position.y + currentCamOffset.y);
		Render render =  (Render)this.getComponent(Render.class.getName());
		render.render(batch);	
		transform.position = relativePosition;
	}
	
	private void activateInactiveButton(RoundHandler handler) {
		Unit unit = null;
		if(handler.playerOnesTurn) {
			for (Unit u : TabletWars.player1Units) 
				if (u.getUnittype() == this.unitToSelect) {
					unit = u;
					break;
				}
			
		}
		else {
			for (Unit u : TabletWars.player2Units) {
				if (u.getUnittype() == this.unitToSelect) {
					unit = u;
					break;
				}
			}
		}
		if(unit != null) {
		ActionPoints points = (ActionPoints)unit.getComponent(ActionPoints.class.getName());
			if (points.getAp() == 0) {
				for (GuiElement element : this.childElements) {
					if (element.getName() == "Inactive") {
						element.changeGUIactiveState(true);
						break;
					}
				}
			}
		}
	}
	
	public boolean buttonTouch(RoundHandler handler, CamManager camManager)
	{
		boolean touched = false;
		if (touchAccepted()) {	
			if (this.unitToSelect != null) {
				touched = selectCorrectUnit(handler, camManager);
			}
			else {
				handleDefaultActions(handler, camManager);
				touched = true;
			}
			
		}
		return touched;
	}

	public boolean buttonTouch(RoundHandler handler, CamManager camManager, Player player) {
		boolean touched = false;	
			if(touchAccepted()) {
				if(this.nextSeletedUnit != null) {
					selectNextUnit(handler, camManager, player);
				}
				else {
					handleDefaultActions(handler, camManager);
				}
				touched = true;
			}		
		return touched;
	}
	
	private void handleDefaultActions(RoundHandler handler,
			CamManager camManager) {
		if(this.name == "Cancel" && RoundHandler.getCurrentGamestate() == GAMESTATE.SELECTION)
		{
			handler.selectedUnit.getOccupiedTile().disableGUI();
			handler.selectedUnit = null;
		}
		handler.setNewGameState(this.followingGamestate, camManager);	
	}
	
	public void changeGUIactiveState(boolean guiActive) {
		this.guiActive = guiActive;
	}
	
	private boolean selectCorrectUnit(RoundHandler handler, CamManager camManager) {
		Unit unit = null;
		boolean unitSelected = false;
		if(handler.playerOnesTurn){
			for (Unit u : TabletWars.player1Units) {
				if (u.getUnittype() == this.unitToSelect) {
					unit = u;
					break;
				}
			}
		}
		else {
			for (Unit u : TabletWars.player2Units) {
				if (u.getUnittype() == this.unitToSelect) {
					unit = u;
					break;
				}
			}
		}
		if(unit != null) {
			ActionPoints points = (ActionPoints)unit.getComponent(ActionPoints.class.getName());
			if(points.getAp() > 0) {
				handler.selectedUnit = unit;
				handler.setNewGameState(followingGamestate, camManager);
				unitSelected = true;
			}
		
		}
		return unitSelected;
	}
	
	private void selectNextUnit(RoundHandler handler, CamManager camManager, Player player) {
		boolean unitSelected = false;
		while(!unitSelected) {
			ActionPoints points = (ActionPoints)this.nextSeletedUnit.getComponent(ActionPoints.class.getName());
			if(points.getAp() > 0) {
				handler.selectedUnit = this.nextSeletedUnit;
				handler.setNewGameState(this.followingGamestate, camManager);
				unitSelected = true;
			}
			this.nextSeletedUnit = player.getNextSelectedUnit();
		}
	}
	
	public GuiElement getChildByName(String childName) {
		GuiElement result = null;
		for (GuiElement element : childElements) {
			if (element.getName().contains(childName) || element.getName() == childName) {
				result = element;
			}
		}
		return result;
	}
	
	private boolean touchAccepted() {
		boolean accepted = false;
		if(guiActive && this.renderGamestate == RoundHandler.getCurrentGamestate()) {
			BoundingBox box = (BoundingBox) this.getComponent(BoundingBox.class.getName());
			box.touchFunctionGUI();
			accepted = box.touched();
		}
		
		return accepted;
	}
}
