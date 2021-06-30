// @author Lasse Klüver
package com.me.tabletwars.components;

import com.badlogic.gdx.audio.Sound;

public class SoundComponent extends Component{
	private Sound sound;
	
	public SoundComponent(Sound sound){
		this.sound = sound;
	}
	
	public void play(float volume){
		sound.play(volume);
	}
	
	public void loop(float volume){
		sound.loop(volume);
	}
	
	public void stop(){
		sound.stop();
	}
}
