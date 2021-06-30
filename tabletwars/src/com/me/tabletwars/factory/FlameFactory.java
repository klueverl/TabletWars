/*
 * @author Jan-Hendrik Kahle
 */
package com.me.tabletwars.factory;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.me.tabletwars.ContentLoader;
import com.me.tabletwars.components.ActionPoints;
import com.me.tabletwars.components.Attack;
import com.me.tabletwars.components.BoundingBox;
import com.me.tabletwars.components.Fortify;
import com.me.tabletwars.components.Health;
import com.me.tabletwars.components.Render;
import com.me.tabletwars.components.SoundComponent;
import com.me.tabletwars.components.TilebasedCharacterController;
import com.me.tabletwars.components.Transform;
import com.me.tabletwars.enums.ANIMATIONTYPE;
import com.me.tabletwars.enums.BARTYPE;
import com.me.tabletwars.enums.UNITTYPE;
import com.me.tabletwars.gameobjects.Projectile;
import com.me.tabletwars.gameobjects.Unit;

public class FlameFactory extends AbstractUnitFactory {
	private static int maxHP;
	private static int damage;
	private static int movementRange;
	
	public FlameFactory() {
		this.unittype = UNITTYPE.FLAMETHROWER;
	}
	
	@Override
	public Unit create(Vector2 position, boolean belongsToPlayerOne) {
		Unit unit = new Unit();
		unit.setUnitOwner(belongsToPlayerOne);
		unit.setUnitType(UNITTYPE.FLAMETHROWER);
		unit.setMovementRange(movementRange);
		addComponents(position, unit);		
		unit.setName(UNITTYPE.FLAMETHROWER.toString());
		Projectile.animationArrayList = ContentLoader.getInstance().getFireAnimation();
		return unit;
	}

	public static void setParameters(int damage, int health, int movementRange) {
		FlameFactory.damage = damage;
		FlameFactory.maxHP = health;
		FlameFactory.movementRange = movementRange;		
	}
	
	private void addComponents(Vector2 position, Unit unit){
		int size = ContentLoader.getInstance().getTileSize();
		
		Transform transform = new Transform(position, new Vector2(1,1),new Vector2(size, size) );
		
		Rectangle boxRectangle = new Rectangle(position.x, position.y, size, size);
		BoundingBox boundingBox = new BoundingBox(boxRectangle);
		
		Health health  = new Health(maxHP);
		if(unit.isOwnedByPlayerOne()) 
			health.setHealthbarTexture(ContentLoader.getInstance().getSimpleColorTexture(BARTYPE.RED));
		else
			health.setHealthbarTexture(ContentLoader.getInstance().getSimpleColorTexture(BARTYPE.GREEN));
		
		Attack attack = new Attack(damage);
		TilebasedCharacterController characterController = new TilebasedCharacterController(transform);
		Fortify fortify = new Fortify();
		ActionPoints actionPoints= new ActionPoints(2);
		
		Render render  = new Render();
		TextureRegion region = ContentLoader.getInstance().getUnitTexture(UNITTYPE.FLAMETHROWER, ANIMATIONTYPE.IDLE , unit.isOwnedByPlayerOne());
		render.setRegion(region);
		render.addAnimation("walk",AnimationFactory.createWalkAnimationList(unit.getUnittype(), unit.isOwnedByPlayerOne()));
		render.addAnimation("shoot",AnimationFactory.createShootAnimationList(unit.getUnittype(), unit.isOwnedByPlayerOne()));
		render.addAnimation("Fortify", AnimationFactory.createFortifyAnimationList(unittype, unit.isOwnedByPlayerOne()));
		
		SoundComponent sound = new SoundComponent(ContentLoader.getInstance().getUnitSound(unittype));
		
		unit.addComponent(render);	
		unit.addComponent(transform);
		unit.addComponent(boundingBox);
		unit.addComponent(health);
		unit.addComponent(attack);
		unit.addComponent(characterController);
		unit.addComponent(fortify);
		unit.addComponent(actionPoints);
		unit.addComponent(sound);
	}

}
