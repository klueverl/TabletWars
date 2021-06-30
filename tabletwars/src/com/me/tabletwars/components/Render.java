//author: Jan-Hendrik Kahle, Lasse Klüver
package com.me.tabletwars.components;

import java.util.ArrayList;
import java.util.HashMap;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.me.tabletwars.TabletWars;

public class Render extends Component
{
	public boolean active = true;
	private float stateTime;
	private int currentFrame;
	private TextureRegion region;
	private HashMap<String, ArrayList<TextureRegion>> animations = new HashMap<String, ArrayList<TextureRegion>>();
	private ArrayList<TextureRegion> currentAnimation;
	private float animationSpeed;
	private float animationDuration;
	private boolean stopAnimationAfterTime;
	
	public void addAnimation(String id, ArrayList<TextureRegion> regions){
		animations.put(id, regions);
	}
	
	public void playAnimation(String ID, float animationSpeed){
		currentAnimation = animations.get(ID);
		this.animationSpeed = animationSpeed;
	}
	
	public void playAnimation(String ID, float animationSpeed, float animationDuration){
		currentAnimation = animations.get(ID);
		this.animationSpeed = animationSpeed;
		this.animationDuration = animationDuration;
		this.stopAnimationAfterTime = true;
	}
	
	public void stopAnimation(){
		currentAnimation = null;
		stopAnimationAfterTime = false;
	}
	
	public void renderAnimation(SpriteBatch  batch){
		Transform transform = (Transform) this.parentObject.getComponent("Transform");
		stateTime += Gdx.graphics.getDeltaTime();
		if(stateTime >= animationSpeed){
			if(currentFrame < currentAnimation.size()-1){
				currentFrame++;
			}
			else {
				currentFrame = 0;
			}
			stateTime = 0;
		}
		if(currentAnimation.get(currentFrame) == null)
		{
			currentFrame--;
		}
		TextureRegion currentRegionToDrawRegion = currentAnimation.get(currentFrame);
		batch.draw(currentRegionToDrawRegion, transform.getPosition().x, reverseYAxis(transform.getPosition().y)-transform.getSize().y, transform.getSize().x/2, transform.getSize().y /2, transform.getSize().x, transform.getSize().y, 1, 1, transform.rotation + 90, true);
		if(stopAnimationAfterTime){
			this.animationDuration -= Gdx.graphics.getDeltaTime();
			if(animationDuration <= 0){
				stopAnimation();
			}
		}
	}
	
	public void render(SpriteBatch batch)
	{
		Transform transform = (Transform) this.parentObject.getComponent("Transform");
		if(currentAnimation == null){
			if(active)
			{
				if(transform.rotation != 0){
					batch.draw(region, transform.getPosition().x, reverseYAxis(transform.getPosition().y)-transform.getSize().y, transform.getSize().x/2, transform.getSize().y /2, transform.getSize().x, transform.getSize().y, 1, 1, transform.rotation + 90, true);
				}
				else{
					batch.draw(region,transform.getPosition().x,reverseYAxis(transform.getPosition().y)-transform.getSize().y, transform.getSize().x, transform.getSize().y);
				}
			}	
		}
		else{
			renderAnimation(batch);
		}
	}
	
	public static float reverseYAxis(float value){
		int reverse = -1;
		return  value * reverse + TabletWars.h;
	}
	
	public void setRegion(TextureRegion region) {
		this.region = region;
	}
}
