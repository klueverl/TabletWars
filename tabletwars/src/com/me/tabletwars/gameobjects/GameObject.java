//@author: Lasse Klüver
// objectID, nextID, name & getter / setter by jhkahle

package com.me.tabletwars.gameobjects;
import java.util.*;

import com.me.tabletwars.components.Component;

public class GameObject {
	
	protected int objectId;
	protected static int nextId = 0;
	protected String name;
	private ArrayList<Component> components = new ArrayList<Component>();
	
	public GameObject() {
		this.objectId = nextId;
		nextId++;
		this.name = this.getClass().getName() + "_" + objectId;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public int getObjectID() {
		return objectId;
	}
	//Add a Component to the ArrayList components of this GameObject
	public void addComponent(Component component){
		component.setParent(this);
		components.add(component);
	}
	//Remove a Component from the ArrayList components of this GameObject
	public void removeComponent(String component){
		components.remove(getIndexOfComponent(component));
	}
	//Get a Component by its name as String from the ArrayList components of this GameObject
	public Component getComponent(String component){
		return components.get(getIndexOfComponent(component));
	}
	//Get the index of a Component in the ArrayList components of this GameObject by its name as String
	int getIndexOfComponent(String component) {
		for(int i = 0; i < components.size(); i++){
			Class<? extends Component> currentComponentClass = components.get(i).getClass();
			if(currentComponentClass.getName().endsWith(component)){
				return i;
			}
		}
		return 0;
	}
}
