//@author Lasse Klüver
package com.me.tabletwars.components;


public class Fortify extends Component {
	private boolean fortify = false;
	private boolean usedFortify = false;
	
	
	public boolean getFority(){
		return this.fortify;
	}
	public void setFortify(boolean fortify){
		this.fortify = fortify;
		if(fortify){
			setUsedFortify(true);
		}
	}
	public void setUsedFortify(boolean usedFortify){
		this.usedFortify = usedFortify;
	}
	public boolean getUsedFortify(){
		return this.usedFortify;
	}
}
