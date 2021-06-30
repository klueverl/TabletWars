/*
 * @author Jan-Hendrik Kahle
 */
package com.me.tabletwars;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.me.tabletwars.gameobjects.GuiElement;
import com.me.tabletwars.manager.CamManager;
import com.me.tabletwars.manager.RoundHandler;

public class GuiContainer {

	private ArrayList<GuiElement> elements = new ArrayList<GuiElement>();
	
	public GuiContainer(){
	}
	
	public void addGuiElement(GuiElement element)
	{
		this.elements.add(element);
	}
	
	public void addGuiContainer(GuiContainer container) {
		this.elements.addAll(container.elements);
	}
	
	public void render(SpriteBatch batch, Vector2 currentCamOffset, RoundHandler handler) {
		for (GuiElement element : this.elements) {
			element.render(batch,currentCamOffset, handler);
		}
	}
	public boolean checkInput(RoundHandler handler, CamManager camManager) {
		for (GuiElement element : this.elements) {
			if(element.buttonTouch(handler, camManager)) {
				return true;
			}
		}
		return false;
	}
	public void clearContainer(){
		elements.clear();
	}
	
	public GuiElement getSpecificGuiElement(String elementName) {
		GuiElement result = null;
		for (GuiElement element : elements) {
			if(element.getName().contains(elementName) || element.getName() == elementName)
			{
				result = element;
			}
		}
		return result;
	}
	
	public void enableGUI(boolean enable) {
		for (GuiElement element : elements) {
			element.changeGUIactiveState(enable);
		}
	}
}
