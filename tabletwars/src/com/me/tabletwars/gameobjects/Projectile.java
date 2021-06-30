//@autho Lasse Klüver
package com.me.tabletwars.gameobjects;


import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.me.tabletwars.TabletWars;
import com.me.tabletwars.components.Render;
import com.me.tabletwars.components.Transform;

public class Projectile extends GameObject{
	float duration;
	public static ArrayList<TextureRegion> animationArrayList;
	
	public Projectile(Unit attackingUnit, float duration) {
		super();
		this.addComponent(new Transform(new Vector2(), new Vector2(1,1), new Vector2(192, 192)));
		this.addComponent(new Render());
		this.duration = duration;
		iniate(attackingUnit);
	}
	
	void iniate(Unit attackingUnit){
		Transform transform = (Transform)attackingUnit.getComponent("Transform");
		Transform thisTransform = (Transform)this.getComponent("Transform");
		thisTransform.rotation = transform.rotation;
		
		switch(thisTransform.rotation){
		case 0:
			thisTransform.position = new Vector2(transform.position.x - 55, transform.position.y - 180);
			break;
		case 90:
			thisTransform.position = new Vector2(transform.position.x - 172, transform.position.y - 73);
			break;
		case 180:
			thisTransform.position = new Vector2(transform.position.x - 73, transform.position.y + 52);
			break;
		case 270:
			thisTransform.position = new Vector2(transform.position.x + 50, transform.position.y - 56);
			break;
		}
		Render render = (Render) this.getComponent(Render.class.getName());
		render.addAnimation("animation", animationArrayList);
		render.playAnimation("animation", 0.05f);
		
		TabletWars.projectiles.add(this);
	}
	
	public void render(SpriteBatch batch){
		duration -= Gdx.graphics.getDeltaTime();
		Render render = (Render) this.getComponent(Render.class.getName());
		render.render(TabletWars.batch);
		if(duration < 0){
			TabletWars.projectiles.remove(this);
		}
	}
}
