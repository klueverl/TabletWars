//@author Lasse Klüver
package com.me.tabletwars.components;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.me.tabletwars.Pathfinding;
import com.me.tabletwars.gameobjects.Tile;

public class TilebasedCharacterController extends Component {
	
	private static float speed = 150.0f;
	private ArrayList<Vector2> path = new ArrayList<Vector2>();;
	private Transform transform;
	
	public TilebasedCharacterController(Transform transform){
		this.transform = transform;
	}
	
	public boolean checkIfTargetReached(Vector2 target){
		if(transform.position.dst(target) > 0){
			return false;
		}
		return true;
	}
	
	void getNextPosition(){
		path.remove(0);
	}
	
	public void move(Tile startTile, Tile targetTile){
		Transform targetTileTransform = (Transform)targetTile.getComponent("Transform");
		Vector2 target = targetTileTransform.getPosition();
		if(!checkIfTargetReached(target)){
			if(path.size() > 0 ){
				Vector2 currentTarget = path.get(0);
				
				if(transform.position.dst(currentTarget) > 0){
					if(transform.position.dst(currentTarget) < 2f){
						transform.position = new Vector2(currentTarget.x, currentTarget.y);
						BoundingBox box = (BoundingBox)this.getParent().getComponent("BoundingBox");
						box.setRect(new Rectangle(transform.position.x, transform.position.y, transform.getSize().x, transform.getSize().y));
					}
					if(this.transform.position.x - currentTarget.x > 0){
						transform.position.x -= Gdx.graphics.getDeltaTime() * speed;
						transform.rotation = 90;
					}
					else if(transform.position.x - currentTarget.x < 0){
						transform.position.x += Gdx.graphics.getDeltaTime() * speed;
						transform.rotation = 270;
					}
					else if(transform.position.y - currentTarget.y > 0){
						transform.position.y -= Gdx.graphics.getDeltaTime() * speed;
						transform.rotation = 0;
					}
					else if(transform.position.y - currentTarget.y < 0){
						transform.position.y += Gdx.graphics.getDeltaTime() * speed;
						transform.rotation = 180;
					}
				}
				else{
					getNextPosition();
				}
			}
			else{
				Transform startTransform = (Transform)startTile.getComponent("Transform");
				Transform targetTransform = (Transform)targetTile.getComponent("Transform");
				int  startx = (int)startTransform.getPosition().x / 64;
				int  starty = (int)startTransform.getPosition().y / 64;
				int targetx = (int)targetTransform.getPosition().x /64;
				int targety = (int)targetTransform.getPosition().y / 64;
				
				Pathfinding astar = new Pathfinding(startx, starty, targetx, targety);
				astar.findPath();
				path = astar.path;
				astar = null;
			}
		}
	}
}
