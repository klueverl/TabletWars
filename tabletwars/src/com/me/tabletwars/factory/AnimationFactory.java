/*
 * @author Jan-Hendrik Kahle
 */
package com.me.tabletwars.factory;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.me.tabletwars.ContentLoader;
import com.me.tabletwars.enums.ANIMATIONTYPE;
import com.me.tabletwars.enums.UNITTYPE;

public class AnimationFactory {

	private static ContentLoader content = ContentLoader.getInstance();
	public AnimationFactory() {
	}
	
	public static ArrayList<TextureRegion> createWalkAnimationList(UNITTYPE unittype, boolean blueIsOwner) {
		ArrayList<TextureRegion> walkAnimationList = new ArrayList<TextureRegion>();
		walkAnimationList.add(content.getUnitTexture(unittype, ANIMATIONTYPE.WALK1, blueIsOwner));
		walkAnimationList.add(content.getUnitTexture(unittype, ANIMATIONTYPE.WALK2, blueIsOwner));
		return walkAnimationList;
	}
	
	public static ArrayList<TextureRegion> createShootAnimationList(UNITTYPE unittype, boolean blueIsOwner) {
		ArrayList<TextureRegion> walkAnimationList = new ArrayList<TextureRegion>();
		walkAnimationList.add(content.getUnitTexture(unittype, ANIMATIONTYPE.SHOOT, blueIsOwner));
		walkAnimationList.add(content.getUnitTexture(unittype, ANIMATIONTYPE.IDLE, blueIsOwner));
		return walkAnimationList;
	}
	
	public static ArrayList<TextureRegion> createFortifyAnimationList(UNITTYPE unittype, boolean blueIsOwner) {
		ArrayList<TextureRegion> fortifyAnimationList = new ArrayList<TextureRegion>();
		fortifyAnimationList.add(content.getUnitTexture(unittype, ANIMATIONTYPE.DEFEND, blueIsOwner));
		return fortifyAnimationList;
	}
}
