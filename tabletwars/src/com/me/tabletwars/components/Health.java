/*
 * @author Lasse Klüver, Jan-Hendrik Kahle
 */
package com.me.tabletwars.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.me.tabletwars.TabletWars;
import com.me.tabletwars.gameobjects.Unit;

public class Health extends Component{
	
	private float maxHealth;
	private float curHealth;
	private TextureRegion healthBarTexture;
	
	public float getCurHealth(){
		return curHealth;
	}
	public float getMaxHealth(){
		return maxHealth;
	}
	public float getHealthPercentage(){
		return curHealth/maxHealth;
	}
	public Health(float maxHealth){
		this.maxHealth = maxHealth;
		this.curHealth = maxHealth;
	}
	public void reduceHealth(float amount){
		this.curHealth -= amount;
		if(this.curHealth <= 0){
			die();
		}
	}
	public void die(){
		boolean playerOneUnit = false;

		for(Unit unit : TabletWars.player1Units){
			if(unit == this.parentObject){
				playerOneUnit = true;
			}
		}
		if(playerOneUnit){
			TabletWars.player1Units.remove(this.parentObject);
		}
		else{
			TabletWars.player2Units.remove(this.parentObject);
		}
		TabletWars.units.remove(this.parentObject);	
		Unit unit = (Unit)this.parentObject;
		unit.getOccupiedTile().removeOccupyUnit();
	}
	
/*
 * @author jhkahle	
 */
	public void renderHealthBar(SpriteBatch batch) {
		Transform transform = (Transform) this.parentObject.getComponent(Transform.class.getName());
		batch.draw(healthBarTexture, transform.position.x, Render.reverseYAxis(transform.position.y+transform.getSize().y*0.1f), transform.getSize().x * this.getHealthPercentage(), transform.getSize().y * 0.1f);
	}
	
/*
 * @author jhkahle
 */
	public void setHealthbarTexture(TextureRegion region) {
		this.healthBarTexture = region;
	}
}
