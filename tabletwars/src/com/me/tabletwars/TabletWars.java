/*
 * Authoren : Lasse Klüver, Jan-Hendrik Kahle
 */
package com.me.tabletwars;

import java.util.ArrayList;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector3;
import com.me.tabletwars.gameobjects.Projectile;
import com.me.tabletwars.gameobjects.Unit;
import com.me.tabletwars.manager.CamManager;
import com.me.tabletwars.manager.MainLoopManager;
import com.me.tabletwars.manager.RoundHandler;

public class TabletWars implements ApplicationListener {
	public static SpriteBatch batch;
	public static float w = 800;
	public static float h = 480;
	public static OrthographicCamera cam;
	public static ArrayList<Unit> units = new ArrayList<Unit>();
	public static ArrayList<Unit> player1Units = new ArrayList<Unit>();
	public static ArrayList<Unit> player2Units= new ArrayList<Unit>();
	public static ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	private ContentLoader content;
	private RoundHandler handler;
	private CamManager camManager;
	private Music music;
	public static boolean gameOver;
	private MainLoopManager mainManager;
	
	@Override
	public void create() {
		handler = new RoundHandler();
		batch = new SpriteBatch();
		cam = new OrthographicCamera(w, h);
		cam.translate( new Vector3(w * 0.5f, 1000, 0));
		camManager = new CamManager(cam);
		cam.update();
		content = ContentLoader.getInstance();			
		mainManager = new MainLoopManager();
		music = content.getBackgroundMusic();
		music.setLooping(true);
		music.setVolume(0.1f);
		music.play();
		GestureDetector gd = new GestureDetector(camManager);
		Gdx.input.setInputProcessor(gd);
	}
	
	@Override
	public void dispose() {

	}
	
	boolean testbool(){
		return true;
	}
	
	@Override
	public void render() {
		
		mainManager.update(camManager, handler);
		
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		cam.update();
		
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		
		mainManager.render(batch, handler,camManager);
		
		batch.end();	
	}
	
	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
