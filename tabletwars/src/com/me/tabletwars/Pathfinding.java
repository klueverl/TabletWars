//@author Lasse Klüver
package com.me.tabletwars;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.math.Vector2;

public class Pathfinding {
	private Node[][] nodes = new Node[32][16];
	private int startX;
	private int startY;
	private int targetX;
	private int targetY;
	private ArrayList<Node> openList = new ArrayList<Node>();
	public ArrayList<Vector2> path = new ArrayList<Vector2>();
	private boolean targetFound = false;
	
	public Pathfinding(int startX, int startY, int targetX, int targetY){
		this.startX = startX;
		this.startY = startY;
		this.targetX = targetX;
		this.targetY = targetY;
	}
	
	void fillNodesMap(){
		this.nodes = null; 
		this.nodes = new Node[32][16];
		for(int x = 0; x < 32; x++){
			for(int y = 0; y < 16; y++){
				this.nodes[x][y] = new Node(x, y, null);
			}
		}
	}
	
	public void findPath(){
		this.openList.clear();
		this.path.clear();
		this.targetFound = false;
		fillNodesMap();
		this.openList.add(nodes[startX][startY]);
		while(!targetFound){
			addNeighBours();
		}
	}
	
	void addNeighBours(){
		Node current = this.openList.get(0);
		if(checkIfTargetFound(current)){
			traceBackPath();
		}
		else{
			ContentLoader loader = ContentLoader.getInstance();
			
			for(int x = -1; x < 2; x++){
				for(int y = -1; y < 2; y++){
					if((x == 0 && y != 0) || (x != 0 && y == 0)){
						if(current.x + x >= 0 && current.x + x < 32 && current.y + y >= 0 && current.y +y < 16){
							if(!loader.mapTiles[current.x+x][current.y+y].isObstacle()){
								if(!nodes[current.x+x][current.y+y].visited){
									this.nodes[current.x+x][current.y+y].lastNode = current;
									this.nodes[current.x+x][current.y+y].visited = true;
									this.openList.add(nodes[current.x+x][current.y+y]);
								}
							}
						}
					}
				}
			}
			
			this.openList.remove(0);
		}
	}
	
	boolean checkIfTargetFound(Node nodeToCheck){
		boolean temp;
		if(nodeToCheck == nodes[targetX][targetY]){
			temp = true;
		}
		else{
			temp = false;
		}
		this.targetFound = temp;
		return temp;
	}
	
	void traceBackPath(){
		this.path.add(new Vector2(targetX * 64, targetY * 64));
		Node curNode = nodes[targetX][targetY];
		while(curNode.lastNode != null){
				curNode=curNode.lastNode;
				this.path.add(new Vector2(curNode.x * 64, curNode.y * 64));
		}
		Collections.reverse(this.path);
	}
}
