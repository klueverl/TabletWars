/*
 * @author Jan-Hendrik Kahle
 */
package com.me.tabletwars.factory;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.me.tabletwars.ContentLoader;
import com.me.tabletwars.components.BoundingBox;
import com.me.tabletwars.components.Render;
import com.me.tabletwars.components.Transform;
import com.me.tabletwars.gameobjects.Tile;

public class TileFactory {

	private TextureRegion tiletTexture;
	
	public TileFactory(){
	}
	
	public Tile create(Vector2 position, boolean obstacle) {
		Tile tile = new Tile();
		
		addTileComponents(position, tile);
		tile.setObstacle(obstacle);
		setTileRenderActive(tile);
		
		
		return tile;
	}

	private void addTileComponents(Vector2 position, Tile tile) {
		int size = ContentLoader.getInstance().getTileSize();
		
		tile.setName((position.x / size) + "/" + (position.y / size));
		
		Transform transform = new Transform(position, new Vector2(1, 1),new Vector2(size,size));		
		Rectangle boxRectangle = new Rectangle(position.x, position.y, size, size);
		BoundingBox boundingBox = new BoundingBox(boxRectangle);
		Render render = new Render();
		render.setRegion(tiletTexture);
		
		tile.setGUI(MenuFactory.createTileOverlay(transform));
		
		tile.addComponent(render);
		tile.addComponent(boundingBox);
		tile.addComponent(transform);
		
	}
	
	public void setBaseTexture(TextureRegion textureRegion) {
		this.tiletTexture = textureRegion;
	}


	private void setTileRenderActive(Tile tile) {
		
		if(tile.isObstacle())
		{
			Render render = (Render)tile.getComponent(Render.class.getName());
			render.active = false;
		}
		tile.disableGUI();
	}
}
