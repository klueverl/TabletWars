// @author Lasse Klüver
package com.me.tabletwars;

public class Node{
	public Node lastNode;
	public int x;
	public int y;
	public boolean visited = false;
	
	public Node(int x, int y, Node lastNode){
		this.x = x;
		this.y = y;
		this.lastNode = lastNode;
	}
}
