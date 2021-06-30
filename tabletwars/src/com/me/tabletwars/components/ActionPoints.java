//@author Jan-Hendrik Kahle
package com.me.tabletwars.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.me.tabletwars.ContentLoader;
import com.me.tabletwars.enums.BARTYPE;


public class ActionPoints extends Component{

	private float currentAp;
	private float maxAp;
	private boolean attackedAlready = false;
	
	public ActionPoints(int ap){
		this.maxAp = ap;
		this.currentAp = ap;
	}
	
	public float getApPercentage(){
		return currentAp / maxAp;
	}
	
	public float getAp(){
		return currentAp;
	}
	
	public void reduceAp(int amount){
		this.currentAp -= amount;
	}
	
	public void restoreAp(){
		this.currentAp = maxAp;
	}
	
	public boolean checkIfCanAttack(){
		return attackedAlready;
	}
	
	public void render(SpriteBatch batch){
		Transform transform = (Transform) this.parentObject.getComponent(Transform.class.getName());
		TextureRegion region = ContentLoader.getInstance().getSimpleColorTexture(BARTYPE.YELLOW);
		float positionY = Render.reverseYAxis(transform.position.y+transform.getSize().y*0.2f);
		float widthX = transform.getSize().x * 0.15f;
		float heightY = transform.getSize().y * 0.08f;
		
		for(int i = 0; i < this.currentAp; i++) {
			batch.draw(region, transform.position.x  + widthX * i * 1.3f,positionY , widthX, heightY);
		}
	}
}