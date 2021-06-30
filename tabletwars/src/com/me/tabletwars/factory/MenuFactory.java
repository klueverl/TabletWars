/*
 * @author Jan-Hendrik Kahle
 */
package com.me.tabletwars.factory;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import com.me.tabletwars.ContentLoader;
import com.me.tabletwars.GuiContainer;
import com.me.tabletwars.TabletWars;
import com.me.tabletwars.components.BoundingBox;
import com.me.tabletwars.components.Render;
import com.me.tabletwars.components.Transform;
import com.me.tabletwars.enums.BORDERTYPE;
import com.me.tabletwars.enums.BUTTONTYPE;
import com.me.tabletwars.enums.GAMESTATE;
import com.me.tabletwars.enums.MENUITEM;
import com.me.tabletwars.enums.UNITTYPE;
import com.me.tabletwars.gameobjects.GuiElement;
import com.me.tabletwars.gameobjects.Unit;

public class MenuFactory {

	private final static int BUTTON_WIDTH = 128;
	private final static int BUTTON_HEIGHT = 32;
	
	public MenuFactory() {
	}
	
	public GuiContainer createMainMenu()
	{
		GuiContainer mainMenu = new GuiContainer();
		mainMenu.addGuiElement(createBackground(GAMESTATE.MENU));
		mainMenu.addGuiElement(createStartButton());
		mainMenu.addGuiElement(createCreditsButton());
		mainMenu.addGuiElement(createHowToButton());
		mainMenu.addGuiElement(createMenuButton());
		mainMenu.addGuiElement(createContinueButton());
		mainMenu.addGuiElement(createPauseStartButton(GAMESTATE.PAUSE));
		mainMenu.addGuiElement(createPauseMenuButton(GAMESTATE.PAUSE));
		mainMenu.addGuiElement(createPauseMenuButton(GAMESTATE.GAMEOVER));
		mainMenu.addGuiElement(createPauseStartButton(GAMESTATE.GAMEOVER));
		
		
		return mainMenu;
	}
	
	public GuiContainer createUnitMenu() {
		GuiContainer menu = new GuiContainer();
		menu.addGuiElement(createMoveButton());
		menu.addGuiElement(createAttackButton());
		menu.addGuiElement(createFortifyButton());
		menu.addGuiElement(createCancelButton(GAMESTATE.SELECTION, GAMESTATE.INGAME, new Vector2(10, 50 + BUTTON_HEIGHT*4)));
		menu.addGuiElement(createCancelButton(GAMESTATE.MOVEMENT, GAMESTATE.SELECTION, new Vector2(10, 50 + BUTTON_HEIGHT*4)));
		menu.addGuiElement(createCancelButton(GAMESTATE.ATTACK, GAMESTATE.SELECTION, new Vector2(10, 50 + BUTTON_HEIGHT*4)));
		return menu;
	}
	
	public GuiContainer createHowToMenu() {
		GuiContainer howTo = new GuiContainer();
		
		Vector2 cancelVector2 = new Vector2(20.0f , TabletWars.h - BUTTON_HEIGHT * 2 - 20.0f );
		howTo.addGuiElement(createCancelButton(GAMESTATE.HOWTO, GAMESTATE.MENU, cancelVector2));
		howTo.addGuiElement(createFirstHowTo());
		for(int howToId = 1; howToId < 9; howToId++) {
			howTo.addGuiElement(createHowToPage(howToId));
		}
		
		howTo.addGuiElement(createLastHowTo());
		
		howTo.addGuiElement(createCancelButton(GAMESTATE.HOWTO, GAMESTATE.MENU, cancelVector2));
		
		return howTo;
	}

	public static GuiContainer createPlaceUnitUI(boolean bluePlayer, UNITTYPE unittype) {
		GAMESTATE renderGamestate = GAMESTATE.PLACEMENT;
		GuiContainer unitUI = new GuiContainer();
		
		float posY = TabletWars.h / 2 - BUTTON_WIDTH;
		
		GuiElement place = new GuiElement(renderGamestate, renderGamestate);
		addComponents(place, TabletWars.w / 2 - BUTTON_WIDTH * 1.25f, posY);
		Render render = new Render();
		render.setRegion(ContentLoader.getInstance().getFontTexture(MENUITEM.PLACE));
		place.addComponent(render);
		BoundingBox curBox = (BoundingBox)place.getComponent(BoundingBox.class.getName());
		curBox.setRect(new Rectangle(0, 0, 0, 0));
		
		GuiElement player = new GuiElement(renderGamestate, renderGamestate);
		addComponents(player, TabletWars.w / 2 - BUTTON_WIDTH * 0.40f, posY - BUTTON_HEIGHT * 0.5f, BUTTON_WIDTH / 2, BUTTON_WIDTH / 2);
		render = new Render();
		render.setRegion(ContentLoader.getInstance().getPlayerFont(bluePlayer));
		player.addComponent(render);
		place.attachChildElement(player);
		
		GuiElement unit = new GuiElement(renderGamestate, renderGamestate);
		addComponents(unit, TabletWars.w / 2 + BUTTON_HEIGHT / 2, posY);
		render = new Render();
		render.setRegion(ContentLoader.getInstance().getFontTexture(parseUnitTypeToMenuitem(unittype)));
		unit.addComponent(render);
		place.attachChildElement(unit);
		
		unitUI.addGuiElement(place);
		
		return unitUI;
	}
	
	public static GuiContainer createUnitMenuTitle(boolean bluePlayer, UNITTYPE unittype) {
		GuiContainer titleContainer = new GuiContainer();
		GAMESTATE renderGamestate = GAMESTATE.SELECTION;
		GAMESTATE render2 = GAMESTATE.MOVEMENT;
		GAMESTATE render3 = GAMESTATE.ATTACK;
		BORDERTYPE backgroundType = bluePlayer ? BORDERTYPE.BLUE : BORDERTYPE.GREEN;
		GuiElement backgroundElement = new GuiElement(renderGamestate, renderGamestate);
		GuiElement borderElement = new GuiElement(renderGamestate, renderGamestate);
		
		float posX = 10;
		float posY = 10;
		
		addComponents(backgroundElement, posX, posY);
		Render render = new Render();
		render.setRegion(ContentLoader.getInstance().getTileTexture(backgroundType));
		backgroundElement.addComponent(render);
		backgroundElement.setName("Background UnitTitle");
		BoundingBox curBox = (BoundingBox)backgroundElement.getComponent(BoundingBox.class.getName());
		curBox.setRect(new Rectangle(0, 0, 0, 0));
		
		addComponents(borderElement, posX, posY);
		render = new Render();
		render.setRegion(ContentLoader.getInstance().getTileTexture(BORDERTYPE.BLACK));
		borderElement.addComponent(render);
		borderElement.setName("Border UnitTitle");
		
		GuiElement backgroundMove = createBackground(render2, backgroundType,
				posX, posY);
		
		GuiElement backgroundAttack = createBackground(render3, backgroundType, posX, posY);
		
		backgroundElement.attachChildElement(borderElement);
		addFont(backgroundElement, renderGamestate, renderGamestate, parseUnitTypeToMenuitem(unittype));
		addFont(backgroundMove, render2, render2, parseUnitTypeToMenuitem(unittype));
		addFont(backgroundAttack, render3, render3, parseUnitTypeToMenuitem(unittype));
		
		
		titleContainer.addGuiElement(backgroundElement);
		titleContainer.addGuiElement(backgroundMove);
		titleContainer.addGuiElement(backgroundAttack);
		return titleContainer;
	}
	
	private static GuiElement createBackground(GAMESTATE render2,
			BORDERTYPE backgroundType, float posX, float posY) {
		Render render;
		BoundingBox curBox;
		GuiElement backgroundMove = new GuiElement(render2, render2);
		addComponents(backgroundMove, posX, posY);
		render = new Render();
		render.setRegion(ContentLoader.getInstance().getTileTexture(backgroundType));
		backgroundMove.addComponent(render);
		backgroundMove.setName("Background UnitTitle - Move");
		curBox = (BoundingBox)backgroundMove.getComponent(BoundingBox.class.getName());
		curBox.setRect(new Rectangle(0, 0, 0, 0));
		
		GuiElement borderMove = new GuiElement(render2, render2);
		addComponents(borderMove, posX, posY);
		render = new Render();
		render.setRegion(ContentLoader.getInstance().getTileTexture(BORDERTYPE.BLACK));
		borderMove.addComponent(render);
		borderMove.setName("Border UnitTitle - Move");
		
		backgroundMove.attachChildElement(borderMove);
		
		return backgroundMove;
	}
	
	public GuiContainer createScrollArrows() {
		GuiContainer arrows = new GuiContainer();
		arrows.addGuiElement(createArrow(180, 0, (int)(TabletWars.h / 2)));
		arrows.addGuiElement(createArrow(90, (int)(TabletWars.w / 2), 0));
		arrows.addGuiElement(createArrow(360,(int)TabletWars.w -64, (int)(TabletWars.h / 2)));
		arrows.addGuiElement(createArrow(270,(int)(TabletWars.w / 2), (int)TabletWars.h -64));
		
		return arrows;
	}
	
	public GuiContainer createUnitSelectionMenu(boolean forPlayerOne) {
		GuiContainer selectionMenu = new GuiContainer();
		float offsetButtons = 10.0f;
		
		ArrayList<Unit> playerUnits = forPlayerOne ? TabletWars.player1Units : TabletWars.player2Units;
		ArrayList<UNITTYPE> items = new ArrayList<UNITTYPE>();
		BUTTONTYPE state = forPlayerOne ? BUTTONTYPE.BLUE_NORMAL : BUTTONTYPE.GREEN_NORMAL;
		BUTTONTYPE inactiveState = forPlayerOne ? BUTTONTYPE.BLUE_INACTIVE : BUTTONTYPE.GREEN_INACTIVE;
		
		fillListForEachRespectiveUnitType(playerUnits, items);
		
		float startPositionX =  TabletWars.cam.viewportWidth - (BUTTON_WIDTH + offsetButtons + BUTTON_WIDTH /4);
		float startPositionY = TabletWars.cam.viewportHeight - (BUTTON_HEIGHT*items.size() + offsetButtons * items.size() + BUTTON_HEIGHT /2);
		
		int count = 0;
		
		for (UNITTYPE unittype : items) {
			float curPositionY = startPositionY + BUTTON_HEIGHT * count + offsetButtons * count;
			GuiElement element = new GuiElement(unittype);
			addComponents(element, startPositionX, curPositionY);
			Render render = new Render();
			render.setRegion(ContentLoader.getInstance().getButtonTexture(state));
			element.addComponent(render);
			element.setName("Choose " + unittype.toString());
			
			GuiElement inactivElement = new GuiElement(unittype);
			addComponents(inactivElement, startPositionX, curPositionY);
			render = new Render();
			render.setRegion(ContentLoader.getInstance().getButtonTexture(inactiveState));
			inactivElement.addComponent(render);
			inactivElement.setName("Inactive");
			inactivElement.changeGUIactiveState(false);
			element.attachChildElement(inactivElement);
			
			addFont(element, GAMESTATE.INGAME, GAMESTATE.SELECTION, parseUnitTypeToMenuitem(unittype));
			
			selectionMenu.addGuiElement(element);
			count++;
		}		
		
		return selectionMenu;
	}

	public GuiElement createEndTurnButton() {
		GuiElement endTurnButton = new GuiElement(GAMESTATE.INGAME, GAMESTATE.ENDTURN);
		endTurnButton.setName("Endturn-Background");
		GuiElement endTurnTexture = new GuiElement(GAMESTATE.INGAME, GAMESTATE.ENDTURN);
		endTurnTexture.setName("Endturn-Symbol");
		
		TextureRegion buttonRegion = ContentLoader.getInstance().getTileTexture(BORDERTYPE.RED);
		TextureRegion textureRegion = ContentLoader.getInstance().getButtonTexture(BUTTONTYPE.ENDTURN);
		
		float height = textureRegion.getRegionHeight();
		float width = textureRegion.getRegionWidth();
		float posX = 10.0f;
		float posY = TabletWars.h - height - 10.0f;
		
		addComponents(endTurnButton, posX, posY, width, height);
		addComponents(endTurnTexture, posX, posY, width, height);
		
		Render render = new Render();
		render.setRegion(buttonRegion);
		endTurnButton.addComponent(render);
		
		render = new Render();
		render.setRegion(textureRegion);
		endTurnTexture.addComponent(render);
		
		endTurnButton.attachChildElement(endTurnTexture);
		
		return endTurnTexture;
	}
	
	public GuiContainer createCredits() {
		GuiContainer credits = new GuiContainer();
		credits.addGuiElement(createBackground(GAMESTATE.CREDITS));
		credits.addGuiElement(createCreditButtons());
		
		return credits;
	}
	
	private GuiElement createCreditButtons() {
		
		Vector2 cancelLocation = new Vector2(TabletWars.w - BUTTON_WIDTH - 10, TabletWars.h - BUTTON_HEIGHT - 10);
		GuiElement back = createCancelButton(GAMESTATE.CREDITS, GAMESTATE.MENU, cancelLocation);
		GuiElement credits = new GuiElement(GAMESTATE.CREDITS, GAMESTATE.CREDITS);
		credits.setName("Creditscreen");
		
		TextureRegion region = ContentLoader.getInstance().getCreditsTexture();
		
		float buttonPositionX = TabletWars.w / 2 - region.getRegionWidth() / 2;
		float buttonPositionY = TabletWars.h / 2 - region.getRegionHeight() / 2;
		addComponents(credits, buttonPositionX, buttonPositionY, region.getRegionWidth(), region.getRegionHeight());
	
		Render render = new Render();
		render.setRegion(region);
		credits.addComponent(render);
		
		back.attachChildElement(credits);
		
		return back;
		
	}
	
	private GuiElement createArrow(int rotation, int posX, int posY) {
		GuiElement arrow = new GuiElement(GAMESTATE.SELECTION, GAMESTATE.SELECTION);
		GuiElement attackArrow = new GuiElement(GAMESTATE.ATTACK, GAMESTATE.ATTACK);
		
		addArrowComponents(rotation, posX, posY, arrow);
		addArrowComponents(rotation, posX, posY, attackArrow);
		
		arrow.attachChildElement(attackArrow);
		
		return arrow;
	}

	private void addArrowComponents(int rotation, int posX, int posY,
			GuiElement arrow) {
		TextureRegion region = ContentLoader.getInstance().getButtonTexture(BUTTONTYPE.ARROW);
		addComponents(arrow, posX, posY, region.getRegionWidth(), region.getRegionHeight());
		Render render = new Render();
		render.setRegion(region);
		arrow.addComponent(render);
		BoundingBox curBox = (BoundingBox) arrow.getComponent(BoundingBox.class.getName());
		curBox.setRect(new Rectangle(0,0,0,0));
		Transform transform = (Transform) arrow.getComponent(Transform.class.getName());
		transform.rotation = rotation;
	}
	
	public GuiElement createZoomElement(boolean zoomIn) {
		GuiElement zoom = new GuiElement(GAMESTATE.INGAME, GAMESTATE.ZOOM);
		BUTTONTYPE zoomType = zoomIn ? BUTTONTYPE.ZOOMIN : BUTTONTYPE.ZOOMOUT;
		zoom.setName("Zoom : " + zoomIn);
		
		TextureRegion region = ContentLoader.getInstance().getButtonTexture(zoomType);
		
		float width = !zoomIn ? region.getRegionWidth() : region.getRegionWidth() * 2;
		float height = !zoomIn ? region.getRegionHeight() : region.getRegionHeight() * 2;
		
		float buttonPositionX = TabletWars.cam.viewportWidth - width - 10.0f;
		float buttonPositionY = 0 + 10.0f;
		
		addComponents(zoom, buttonPositionX, buttonPositionY, width, height);
		
		Render render = new Render();
		render.setRegion(region);
		zoom.addComponent(render);
		
		zoom.changeGUIactiveState(!zoomIn);
		
		return zoom;
	}
	
	public static GuiElement createTileOverlay(Transform tileTransform) {
		GAMESTATE blueRenderState = GAMESTATE.MOVEMENT;
		GAMESTATE redRenderState = GAMESTATE.ATTACK;
		GAMESTATE greenRenderState = GAMESTATE.PLACEMENT;
		GAMESTATE secondGreenRender = GAMESTATE.SELECTION;
		GuiElement overlayGreen = new GuiElement(greenRenderState, greenRenderState);
		GuiElement overlayRed = new GuiElement(redRenderState, redRenderState);
		GuiElement overLayBlue = new GuiElement(blueRenderState, blueRenderState);
		GuiElement overLayGreen2 = new GuiElement(secondGreenRender, secondGreenRender);
		
		overlayGreen.addComponent(tileTransform);
		setOverlayColor(overlayGreen, BORDERTYPE.GREEN);
		
		overLayBlue.addComponent(tileTransform);
		setOverlayColor(overLayBlue, BORDERTYPE.BLUE);
		
		overlayRed.addComponent(tileTransform);
		setOverlayColor(overlayRed, BORDERTYPE.RED);
		
		overLayGreen2.addComponent(tileTransform);
		setOverlayColor(overLayGreen2, BORDERTYPE.GREEN);
		
		overlayGreen.attachChildElement(overLayBlue);
		overlayGreen.attachChildElement(overlayRed);
		overlayGreen.attachChildElement(overLayGreen2);
		
		
		return overlayGreen;
	}

	private GuiElement createMoveButton() {
		GAMESTATE movRender = GAMESTATE.SELECTION;
		GAMESTATE lockedRender = GAMESTATE.MOVEMENT;
		GAMESTATE inactiveRender = GAMESTATE.ATTACK;
		MENUITEM movFont = MENUITEM.MOVE;
		GuiElement move = new GuiElement(movRender, lockedRender);
		GuiElement lockedState = new GuiElement(lockedRender, movRender);
		GuiElement inactiveState = new GuiElement(inactiveRender, inactiveRender);
		
		float buttonPositionX = 10;
		float buttonPositionY = 20 + BUTTON_HEIGHT;
		
		addComponents(move, buttonPositionX, buttonPositionY);
		Render render = new Render();
		render.setRegion(ContentLoader.getInstance().getButtonTexture(BUTTONTYPE.NORMAL));
		move.addComponent(render);
		move.setName("Move");
		addFont(move, movRender, lockedRender, movFont);
		
		
		addComponents(inactiveState, buttonPositionX, buttonPositionY);
		render = new Render();
		render.setRegion(ContentLoader.getInstance().getButtonTexture(BUTTONTYPE.INACTIVE));
		inactiveState.addComponent(render);
		inactiveState.setName("Move-Inactive");
		addFont(inactiveState, inactiveRender, inactiveRender, movFont);
		
		
		addComponents(lockedState, buttonPositionX, buttonPositionY);
		render = new Render();
		render.setRegion(ContentLoader.getInstance().getButtonTexture(BUTTONTYPE.SELECTED));
		lockedState.addComponent(render);
		lockedState.setName("Move-Locked");
		addFont(lockedState, lockedRender, movRender, movFont);
		
		
		
		move.attachChildElement(inactiveState);
		move.attachChildElement(lockedState);
		
		return move;
	}
	
	private GuiElement createAttackButton() {
		GAMESTATE attRender = GAMESTATE.SELECTION;
		GAMESTATE lockedRender = GAMESTATE.ATTACK;
		GAMESTATE inactiveRender = GAMESTATE.MOVEMENT;
		MENUITEM movFont = MENUITEM.ATTACK;
		GuiElement attack = new GuiElement(attRender, lockedRender);
		GuiElement lockedState = new GuiElement(lockedRender, attRender);
		GuiElement inactiveState = new GuiElement(inactiveRender, inactiveRender);
		GuiElement disabledAttack = new GuiElement(attRender, attRender);
		
		float buttonPositionX = 10;
		float buttonPositionY = 30 + BUTTON_HEIGHT * 2;
		
		addComponents(attack, buttonPositionX, buttonPositionY);
		Render render = new Render();
		render.setRegion(ContentLoader.getInstance().getButtonTexture(BUTTONTYPE.NORMAL));
		attack.addComponent(render);
		attack.setName("Attack");
		addFont(attack, attRender, lockedRender, movFont);
		
		addComponents(inactiveState, buttonPositionX, buttonPositionY);
		render = new Render();
		render.setRegion(ContentLoader.getInstance().getButtonTexture(BUTTONTYPE.INACTIVE));
		inactiveState.addComponent(render);
		inactiveState.setName("Attack-Inactive");
		addFont(inactiveState, inactiveRender, inactiveRender, movFont);
		
		addComponents(lockedState, buttonPositionX, buttonPositionY);
		render = new Render();
		render.setRegion(ContentLoader.getInstance().getButtonTexture(BUTTONTYPE.SELECTED));
		lockedState.addComponent(render);
		lockedState.setName("Attack-Locked");
		addFont(lockedState, lockedRender, attRender, movFont);
		
		addComponents(disabledAttack, buttonPositionX, buttonPositionY);
		render = new Render();
		render.setRegion(ContentLoader.getInstance().getButtonTexture(BUTTONTYPE.INACTIVE));
		disabledAttack.addComponent(render);
		disabledAttack.setName("Attack-Disable");
		addFont(disabledAttack, attRender, attRender, movFont);
		disabledAttack.changeGUIactiveState(false);
		
		attack.attachChildElement(inactiveState);
		attack.attachChildElement(lockedState);
		attack.attachChildElement(disabledAttack);
		
		return attack;
	}
	
	private GuiElement createFortifyButton() {
		
		GAMESTATE fortifyRender = GAMESTATE.SELECTION;
		GAMESTATE inactiveRender = GAMESTATE.MOVEMENT;
		GAMESTATE inactiveRender2 = GAMESTATE.ATTACK;
		MENUITEM fortifyFont = MENUITEM.FORTIFY;

		GuiElement fortify = new GuiElement(fortifyRender, GAMESTATE.FORTIFY);
		GuiElement inactive1 = new GuiElement(inactiveRender, inactiveRender);
		GuiElement inactive2 = new GuiElement(inactiveRender2, inactiveRender2);
		GuiElement disabledFortify = new GuiElement(fortifyRender, GAMESTATE.FORTIFY);
		
		float buttonPositionX = 10;
		float buttonPositionY = 40 + BUTTON_HEIGHT * 3;
		
		addComponents(fortify, buttonPositionX, buttonPositionY);
		Render render = new Render();
		render.setRegion(ContentLoader.getInstance().getButtonTexture(BUTTONTYPE.NORMAL));
		fortify.addComponent(render);
		fortify.setName("Fortify");
		addFont(fortify, fortifyRender, GAMESTATE.FORTIFY, fortifyFont);
		
		addComponents(inactive1, buttonPositionX, buttonPositionY);
		render = new Render();
		render.setRegion(ContentLoader.getInstance().getButtonTexture(BUTTONTYPE.INACTIVE));
		inactive1.addComponent(render);
		inactive1.setName("Fortify-Inactive");
		addFont(inactive1, inactiveRender, inactiveRender, fortifyFont);
		
		addComponents(inactive2, buttonPositionX, buttonPositionY);
		render = new Render();
		render.setRegion(ContentLoader.getInstance().getButtonTexture(BUTTONTYPE.INACTIVE));
		inactive2.addComponent(render);
		inactive2.setName("Fortify-Inactive");
		addFont(inactive2, inactiveRender2, inactiveRender2, fortifyFont);
		
		addComponents(disabledFortify, buttonPositionX, buttonPositionY);
		render = new Render();
		render.setRegion(ContentLoader.getInstance().getButtonTexture(BUTTONTYPE.INACTIVE));
		disabledFortify.addComponent(render);
		disabledFortify.setName("Fortify-Disable");
		addFont(disabledFortify, fortifyRender, GAMESTATE.FORTIFY, fortifyFont);
		disabledFortify.changeGUIactiveState(false);
		
		fortify.attachChildElement(inactive1);
		fortify.attachChildElement(inactive2);
		fortify.attachChildElement(disabledFortify);
		
		return fortify;
	}
	
	private GuiElement createCancelButton(GAMESTATE renderGamestate, GAMESTATE previosGamestate, Vector2 position) {
		GuiElement cancel = new GuiElement(renderGamestate, previosGamestate);

		addComponents(cancel, position.x, position.y);
		Render render = new Render();
		render.setRegion(ContentLoader.getInstance().getButtonTexture(BUTTONTYPE.NORMAL));
		cancel.addComponent(render);
		cancel.setName("Cancel");
		addFont(cancel, renderGamestate, previosGamestate, MENUITEM.CANCEL);
		
		return cancel;
	}
	
	private GuiElement createBackground(GAMESTATE renderGamestate) {
		GuiElement background = new GuiElement(renderGamestate,renderGamestate);
		background.setName(renderGamestate.toString() + " - Background");
		
		TextureRegion backRegion= new TextureRegion(ContentLoader.getInstance().getBackgroundTexture());
		addComponents(background, 0, 0,backRegion.getRegionWidth() , backRegion.getRegionHeight());

		Render render = new Render();
		render.setRegion(backRegion);
		background.addComponent(render);

		BoundingBox box = (BoundingBox)background.getComponent(BoundingBox.class.getName());
		box.setRect(new Rectangle(0, 0, 0, 0));
		
		return background;
	}
	
	private GuiElement createStartButton() {
			
		GuiElement start = new GuiElement(GAMESTATE.MENU, GAMESTATE.PLACEMENT);
		start.setName("StartButton");
		
		float buttonPositionX = TabletWars.w  - BUTTON_WIDTH - BUTTON_WIDTH / 2;
		float buttonPositionY = TabletWars.h  - BUTTON_HEIGHT * 3 - 30.0f;
		addComponents(start, buttonPositionX, buttonPositionY);
		
		Render render = new Render();
		render.setRegion(ContentLoader.getInstance().getButtonTexture(BUTTONTYPE.NORMAL));
		start.addComponent(render);
		
		addFont(start, GAMESTATE.MENU, GAMESTATE.PLACEMENT, MENUITEM.START);
		
		return start;
	}
	
	private GuiElement createHowToButton() {
		GuiElement start = new GuiElement(GAMESTATE.MENU, GAMESTATE.HOWTO);
		start.setName("HowToButton");
		
		float buttonPositionX = TabletWars.w  - BUTTON_WIDTH - BUTTON_WIDTH / 2;
		float buttonPositionY = TabletWars.h  - BUTTON_HEIGHT * 2 - 20.0f;
		addComponents(start, buttonPositionX, buttonPositionY);
		
		Render render = new Render();
		render.setRegion(ContentLoader.getInstance().getButtonTexture(BUTTONTYPE.NORMAL));
		start.addComponent(render);
		
		addFont(start, GAMESTATE.MENU, GAMESTATE.HOWTO, MENUITEM.HOWTO);
		
		return start;
	}
	
	private GuiElement createCreditsButton()
	{
		GuiElement credits = new GuiElement(GAMESTATE.MENU, GAMESTATE.CREDITS);
		credits.setName("Credits");
		
		float buttonPositionX = TabletWars.w  - BUTTON_WIDTH - BUTTON_WIDTH / 2;
		float buttonPositionY = TabletWars.h  - BUTTON_HEIGHT - 10.0f;
		addComponents(credits, buttonPositionX, buttonPositionY);
		
		Render render = new Render();
		render.setRegion(ContentLoader.getInstance().getButtonTexture(BUTTONTYPE.NORMAL));
		credits.addComponent(render);
		
		addFont(credits, GAMESTATE.MENU, GAMESTATE.CREDITS, MENUITEM.CREDITS);
		
		return credits;
	}
	
	private GuiElement createMenuButton(){
		GuiElement menu = new GuiElement(GAMESTATE.INGAME, GAMESTATE.PAUSE);
		float posX = 0;
		float posY = 0;
		addComponents(menu, posX, posY);
		Render render = new Render();
		render.setRegion(ContentLoader.getInstance().getButtonTexture(BUTTONTYPE.NORMAL));
		menu.addComponent(render);
		addFont(menu, GAMESTATE.INGAME, GAMESTATE.PAUSE, MENUITEM.MENU);

		return menu;
	}
	
	private GuiElement createContinueButton(){
		GuiElement continueElement = new GuiElement(GAMESTATE.PAUSE, GAMESTATE.INGAME);
		float posX = TabletWars.w * 0.5f - 50;
		float posY = TabletWars.h * 0.5f - 16 - 42;
		addComponents(continueElement, posX, posY);
		Render render = new Render();
		render.setRegion(ContentLoader.getInstance().getButtonTexture(BUTTONTYPE.NORMAL));
		continueElement.addComponent(render);
		addFont(continueElement, GAMESTATE.PAUSE, GAMESTATE.INGAME, MENUITEM.CONTINUE);

		return continueElement;
	}
	
	private GuiElement createPauseStartButton(GAMESTATE renderState){
		GuiElement continueElement = new GuiElement(renderState, GAMESTATE.RESTART);
		float posX = TabletWars.w * 0.5f - 50;
		float posY = TabletWars.h * 0.5f - 16;
		addComponents(continueElement, posX, posY);
		Render render = new Render();
		render.setRegion(ContentLoader.getInstance().getButtonTexture(BUTTONTYPE.NORMAL));
		continueElement.addComponent(render);
		addFont(continueElement, renderState, GAMESTATE.INGAME , MENUITEM.START);
		return continueElement;
	}
	
	private GuiElement createPauseMenuButton(GAMESTATE renderGamestate){
		GuiElement continueElement = new GuiElement(renderGamestate, GAMESTATE.RETURN);
		float posX = TabletWars.w * 0.5f - 50;
		float posY = TabletWars.h * 0.5f - 16 + 42;
		addComponents(continueElement, posX, posY);
		Render render = new Render();
		render.setRegion(ContentLoader.getInstance().getButtonTexture(BUTTONTYPE.NORMAL));
		continueElement.addComponent(render);
		addFont(continueElement, GAMESTATE.PAUSE, GAMESTATE.INGAME, MENUITEM.MENU);

		return continueElement;
	}
	
	//TODO Regions in HowToS integrieren
	private GuiElement createFirstHowTo() {
		GuiElement howTo = new GuiElement(GAMESTATE.HOWTO, GAMESTATE.HOWTO);
		howTo.setName("HowTo" + 0 + ".");
		
		addComponents(howTo, 0, 0, TabletWars.w, TabletWars.h);
		Render render = new Render();
		render.setRegion(ContentLoader.getInstance().getTutTextureRegion(1));
		howTo.addComponent(render);
		
		howTo.attachChildElement(createNextButton(0));
		GuiElement dummy = createPreviousButton(0);
		dummy.changeGUIactiveState(false);
		howTo.attachChildElement(dummy);
		
		
		return howTo;
	}
	
	private GuiElement createHowToPage(int pageNumber) {
		GuiElement howToPage = new GuiElement(GAMESTATE.HOWTO, GAMESTATE.HOWTO);
		howToPage.setName("HowTo" + pageNumber + ".");
		
		addComponents(howToPage, 0, 0,TabletWars.w,TabletWars.h);
		Render render = new Render();
		render.setRegion(ContentLoader.getInstance().getTutTextureRegion(pageNumber+1));
		howToPage.addComponent(render);
		howToPage.attachChildElement(createNextButton(pageNumber));
		howToPage.attachChildElement(createPreviousButton(pageNumber));
		
		return howToPage;
	}
	
	private GuiElement createLastHowTo() {
		GuiElement howTo = new GuiElement(GAMESTATE.HOWTO, GAMESTATE.HOWTO);
		howTo.setName("HowTo" + 9 + ".");
		
		addComponents(howTo, 0, 0,TabletWars.w,TabletWars.h);
		Render render = new Render();
		render.setRegion(ContentLoader.getInstance().getTutTextureRegion(10));
		howTo.addComponent(render);
		
		howTo.attachChildElement(createPreviousButton(9));
		GuiElement dummy = createNextButton(9);
		dummy.changeGUIactiveState(false);
		howTo.attachChildElement(dummy);
		
		return howTo;
	}
	
	private GuiElement createNextButton(int howToID) {
		GuiElement next = new GuiElement(GAMESTATE.HOWTO, GAMESTATE.HOWTO);
		next.setName("Next" + howToID);
		
		float positionX = TabletWars.w - BUTTON_WIDTH - 20.0f;
		float positionY = TabletWars.h - BUTTON_HEIGHT - 10.0f;
		addComponents(next, positionX, positionY);
		Render render = new Render();
		render.setRegion(ContentLoader.getInstance().getButtonTexture(BUTTONTYPE.NORMAL));
		next.addComponent(render);
		addFont(next, GAMESTATE.HOWTO, GAMESTATE.HOWTO, MENUITEM.NEXT);
		
		return next;
	}
	
	private GuiElement createPreviousButton(int howToID) {
		GuiElement prev = new GuiElement(GAMESTATE.HOWTO, GAMESTATE.HOWTO);
		prev.setName("Prev" + howToID);
		
		float positionX = 20.0f;
		float positionY = TabletWars.h - BUTTON_HEIGHT - 10.0f;
		addComponents(prev, positionX, positionY);
		Render render = new Render();
		render.setRegion(ContentLoader.getInstance().getButtonTexture(BUTTONTYPE.NORMAL));
		prev.addComponent(render);
		addFont(prev, GAMESTATE.HOWTO, GAMESTATE.HOWTO, MENUITEM.PREVIOUS);
		
		return prev;
	}
	
	private static void addComponents(GuiElement element, float posX, float posY) {
		
		float buttonPositionX = posX;
		float buttonPositionY = posY;
		Vector2 position = new Vector2(buttonPositionX, buttonPositionY);
		Vector2 size = new Vector2(BUTTON_WIDTH, BUTTON_HEIGHT);
		Transform transform = new Transform(position, new Vector2(1, 1), size);
		Rectangle rectangle = new Rectangle(buttonPositionX, buttonPositionY, BUTTON_WIDTH, BUTTON_HEIGHT);
		BoundingBox box = new BoundingBox(rectangle);
		
		element.addComponent(box);
		element.addComponent(transform);		
	}
	
	private static void addComponents(GuiElement element, float posX, float posY, float w, float h) {
		
		float buttonPositionX = posX;
		float buttonPositionY = posY;
		Vector2 position = new Vector2(buttonPositionX, buttonPositionY);
		Vector2 size = new Vector2(w, h);
		Transform transform = new Transform(position, new Vector2(1, 1), size);
		Rectangle rectangle = new Rectangle(buttonPositionX, buttonPositionY, w, h);
		BoundingBox box = new BoundingBox(rectangle);
		
		element.addComponent(box);
		element.addComponent(transform);		
	}
	
	private static void addFont(GuiElement parent, GAMESTATE renderState, GAMESTATE followingGamestate, MENUITEM item) {
		GuiElement font = new GuiElement(renderState,followingGamestate);
		Render render = new Render();
		render.setRegion(ContentLoader.getInstance().getFontTexture(item));
		Transform transform = (Transform)parent.getComponent("Transform");
		BoundingBox box = new BoundingBox(new Rectangle(0, 0, 0, 0));
		font.addComponent(box);
		font.addComponent(transform);
		font.addComponent(render);
		font.setName(item.toString() + " Font" + parent.getName());
		parent.attachChildElement(font);
	}
	
	private void fillListForEachRespectiveUnitType(ArrayList<Unit> playerUnits,
			ArrayList<UNITTYPE> items) {
		for (Unit unit : playerUnits){
			UNITTYPE curType;
			curType = unit.getUnittype();
			
			if(!items.contains(curType)){
				items.add(curType);
			}
		}
	}
	
	private static MENUITEM parseUnitTypeToMenuitem(UNITTYPE unittype) {
		MENUITEM item = null;
		
		switch (unittype) {
			case FLAMETHROWER:
				item = MENUITEM.FLAMETHROWER;
			break;
			case SOLDIER:
				item = MENUITEM.SOLDIER;
				break;
			case SNIPER:
				item = MENUITEM.SNIPER;
				break;
			case SHOTGUN:
				item = MENUITEM.SHOTGUN;
				break;
			default:
				break;
		}
		return item;
	}

	private static void setOverlayColor(GuiElement element, BORDERTYPE bordertype) {
		Render render = new Render();
		render.setRegion(ContentLoader.getInstance().getTileTexture(bordertype));
		element.addComponent(render);
	}
}
