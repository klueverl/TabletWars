//author: Lasse Klüver
package com.me.tabletwars.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class BoundingBox extends Component {
	private boolean isTouched; 
	private Rectangle rectangle;
	
	public BoundingBox(Rectangle rect)
	{
		this.rectangle = rect;
		this.isTouched = false;
	}
	
	
	public Rectangle getRect(){
		return this.rectangle;
	}
	
	public void setRect(Rectangle rect){
		this.rectangle = rect;
	}
	
	public boolean touched() {
		return isTouched;
	}
	
	public void touchFunction(Vector2 offset) {
		if(this.rectangle.contains(calculateInputPosition().x + offset.x, calculateInputPosition().y + offset.y)) {
			isTouched = true;
		}
		else {
			isTouched = false;
		}
	}
	
	public void touchFunctionGUI() {
		if(this.rectangle.contains(calculateInputPosition().x, calculateInputPosition().y)) {
			isTouched = true;
		}
		else {
			isTouched = false;
		}
	}
	private Vector2 calculateInputPosition(){
		float xFactor = 800f / Gdx.graphics.getWidth();
		float yFactor = 480f / Gdx.graphics.getHeight();
		return new Vector2(Gdx.input.getX() * xFactor, Gdx.input.getY() * yFactor);
	}
}


