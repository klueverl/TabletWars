/*
 * @author Jan-Hendrik Kahle
 */
package com.me.tabletwars.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.me.tabletwars.ContentLoader;
import com.me.tabletwars.GuiContainer;
import com.me.tabletwars.TabletWars;
import com.me.tabletwars.components.Render;
import com.me.tabletwars.enums.GAMESTATE;
import com.me.tabletwars.factory.MenuFactory;
import com.me.tabletwars.gameobjects.GuiElement;
import com.me.tabletwars.gameobjects.Tile;
import com.me.tabletwars.gameobjects.Unit;

public class MainLoopManager {
	
	private ContentLoader content;
	
	public static int curHowToPage = 0;
	private MenuFactory menuFactory = new MenuFactory();
	public static GuiContainer menuContainer;
	private GuiContainer howToContainer = new GuiContainer();
	private boolean loadComplete = false;
	private GuiElement zoomIn;
	private GuiElement zoomOut;

	public MainLoopManager() {
		content = ContentLoader.getInstance();
		menuContainer = menuFactory.createUnitMenu();
		menuContainer.addGuiContainer(menuFactory.createMainMenu());
		menuContainer.addGuiContainer(menuFactory.createCredits());
		menuContainer.addGuiContainer(menuFactory.createScrollArrows());
		howToContainer.addGuiContainer(menuFactory.createHowToMenu());
		zoomIn = menuFactory.createZoomElement(true);
		zoomOut = menuFactory.createZoomElement(false);
	}
	
	public void update(CamManager managerc, RoundHandler handler) {
						
		if(!loadComplete)
		{
		loadComplete = content.createMapTiles();		
		}
		else{
			if(Gdx.input.justTouched()) {
				menuContainer.checkInput(handler, managerc);
				handler.chooseUnitMenu.checkInput(handler, managerc);
				browseHowTo(managerc, handler);
				zoomIn.buttonTouch(handler, managerc);
				zoomOut.buttonTouch(handler, managerc);
			}
			activateCurrentHowToPage();
			zoomIn.changeGUIactiveState(!CamManager.zoom);
			zoomOut.changeGUIactiveState(CamManager.zoom);
		}		
		managerc.update();
		handler.handleActions(managerc);
	}

	private void browseHowTo(CamManager managerc, RoundHandler handler) {
		int currentCheck = curHowToPage;
		if(howToContainer.checkInput(handler, managerc))
		{
			GuiElement curElement = howToContainer.getSpecificGuiElement("HowTo"+currentCheck +".");
			if(currentCheck != 9)
			{
				if(curElement.getChildByName("Next" + currentCheck).buttonTouch(handler, managerc))
				{
					curHowToPage++;
				}
			}
			if(currentCheck != 0) {
				if (curElement.getChildByName("Prev" + currentCheck).buttonTouch(handler, managerc)) {
				curHowToPage--;
				}
			}
		}
		
	}
	
	public void render(SpriteBatch batch, RoundHandler handler, CamManager camManager) {
		
		batch.draw(content.getMapTexture(), 0, Render.reverseYAxis(0) - 1024, 2048, 1024);
		
		if(RoundHandler.getCurrentGamestate() == GAMESTATE.GAMEOVER) {
			renderGameOver(batch, camManager.getCurrentOffset());
		}
		else {
			if (RoundHandler.getCurrentGamestate() == GAMESTATE.MENU) {
				
			}
			else {
				if (RoundHandler.getCurrentGamestate() == GAMESTATE.CREDITS) {
					
				}
				else {
					if(loadComplete)
					{			
						renderIngame(batch,handler,camManager.getCurrentOffset(), camManager.cam);						
					}
				}
			}
		}
		howToContainer.render(batch, camManager.getCurrentOffset(), handler);
		menuContainer.render(batch, camManager.getCurrentOffset(), handler);
	}
	
	private static void renderGameOver(SpriteBatch batch, Vector2 offset) {
		TextureRegion region = ContentLoader.getInstance().getGameOverTexture(TabletWars.gameOver);
		batch.draw(region, (TabletWars.w / 2 - region.getRegionWidth() / 2) + offset.x, Render.reverseYAxis((TabletWars.h / 2- region.getRegionHeight() / 2) + offset.y));
	}

	private void activateCurrentHowToPage() {
		for(int i =0; i <= 9; i++)
		{
				howToContainer.getSpecificGuiElement("HowTo"+i +".").changeGUIactiveState(i==curHowToPage);
		}
	}
	
	private void renderIngame(SpriteBatch batch, RoundHandler handler, Vector2 offset, Camera cam) {
		for (Tile[] tiles : content.mapTiles) {
			for (Tile tile : tiles) {
				tile.render(batch, handler);
			}
		}
		for(Unit unit : TabletWars.units){
			unit.render(batch);
		}
		for(int i =0; i < TabletWars.projectiles.size(); i++){
			TabletWars.projectiles.get(i).render(batch);
		}
		if(CamManager.zoom){
			handler.chooseUnitMenu.render(batch, offset, handler);
			handler.unitTitle.render(batch, offset, handler);
		}		
		zoomIn.render(batch, new Vector2(offset.x + cam.viewportWidth - TabletWars.w, offset.y - cam.viewportHeight + TabletWars.h), handler);
		zoomOut.render(batch, offset, handler);
	    com.me.tabletwars.manager.PlacementManager.placementContainer.render(batch, offset, handler);
	}	
	
	public static GuiElement getMenuElementByName(String menuItemName) {
		return menuContainer.getSpecificGuiElement(menuItemName);
	}
}
