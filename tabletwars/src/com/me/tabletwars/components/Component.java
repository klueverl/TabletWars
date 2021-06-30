//@author Lasse Klüver
package com.me.tabletwars.components;

import com.me.tabletwars.gameobjects.GameObject;

public class Component {
	protected GameObject parentObject;
	
	public void setParent(GameObject parent){
		this.parentObject = parent;
	}
	public GameObject getParent(){
		return this.parentObject;
	}
}
