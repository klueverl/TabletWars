//@author Lasse KLüver
package com.me.tabletwars.components;
import com.badlogic.gdx.math.Vector2;

public class Transform extends Component {
	public Vector2 position;
	public int rotation;
	private Vector2 scale;
	private Vector2 size;

	public Transform(Vector2 position, Vector2 scale, Vector2 size){
		this.position = position;
		this.scale = scale;
		this.size = size;
	}
	
	public Vector2 getPosition(){
		return position;
	}
	
	public Vector2 getScale(){
		return scale;
	}
	
	public Vector2 getSize(){
		return size;
	}
}
