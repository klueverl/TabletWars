//@author Lasse Klüver, Jan-Hendrik Kahle
package com.me.tabletwars.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.me.tabletwars.ContentLoader;
import com.me.tabletwars.TabletWars;
import com.me.tabletwars.components.BoundingBox;
import com.me.tabletwars.components.Transform;
import com.me.tabletwars.enums.GAMESTATE;
import com.me.tabletwars.gameobjects.Unit;

public class CamManager implements GestureListener {

	public static boolean zoom = true;
	
	public OrthographicCamera cam;
	private float velX, velY;
	private boolean flinging = false;
	private boolean zoomedIn = true;
	private boolean zoomedOut = false;
	private Vector2 lastCameraPosition;
		
	public CamManager(OrthographicCamera camera)
	{
		this.cam = camera;
	}
	
	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		this.flinging = false;
		return false;
	}
	@Override
	public boolean tap(float x, float y, int count, int button) {
		return false;
	}
	@Override
	public boolean longPress(float x, float y) {
		return false;
	}
	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		if(RoundHandler.getCurrentGamestate() != GAMESTATE.PLACEMENT)
		{
		flinging = true;
		velX = cam.zoom * velocityX * 0.4f;
		velY = cam.zoom * velocityY * 0.4f;
		}
		return false;
	}
	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		return false;
	}
	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		return false;
	}
	@Override
	public boolean zoom(float initialDistance, float distance) {
		zoom = true;
		return false;
	}
	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		zoom = false;
		return false;
	}
	
	public void update () {
		handleCameraMovement();
		
		handleCameraZoom();
	}

	private void handleCameraZoom() {
		if(!zoom  && !zoomedOut ){
			zoomedIn = false;
			zoomedOut = true;
			zoomOut();
		}
		else if (!zoomedIn &&  zoom){
			zoomedIn = true;
			zoomedOut = false;
			zoomBackIn();
		}
	}

	private void zoomOut(){
		float zoomFactor = 1024 / cam.viewportHeight;
		cam.viewportWidth = cam.viewportWidth * zoomFactor;
		cam.viewportHeight = cam.viewportHeight * zoomFactor;
		lastCameraPosition = new Vector2(cam.position.x, -cam.position.y);
		setCameraPosition(new Vector2(1024, 32));
		
		cam.update();
	}
	
	private void zoomBackIn(){
		cam.viewportWidth = TabletWars.w;
		cam.viewportHeight = TabletWars.h;
		setCameraPosition(lastCameraPosition);
		cam.update();
	}
	
	private void handleCameraMovement() {
		if (flinging) {
			flingCamera();
		}
		else {
			GAMESTATE curGamestate = RoundHandler.getCurrentGamestate();
			if(curGamestate == GAMESTATE.SELECTION || curGamestate == GAMESTATE.ATTACK) {
				handleButtonInputMovement();
			}
		}
	}

	private void flingCamera() {
		velX *= 0.98f;
		velY *= 0.98f;
		if(!checkIfCamTranslationIsValidOnAxis(-velX * Gdx.graphics.getDeltaTime(), true))
		{
			velX = 0;
		}
		else if (!checkIfCamTranslationIsValidOnAxis(velY * Gdx.graphics.getDeltaTime(), false)) {
			velY = 0;
		}
		cam.position.add(-velX * Gdx.graphics.getDeltaTime(), velY * Gdx.graphics.getDeltaTime(), 0);
		if (Math.abs(velX) < 0.01f) {			
			velX = 0;
		}
		if (Math.abs(velY) < 0.01f) {		
			velY = 0;
		}
	}
	
	private void handleButtonInputMovement(){
		float x = 0; 
		float y = 0;
		
		if(Gdx.input.isTouched()){
			BoundingBox box = new BoundingBox(new Rectangle(TabletWars.w /2, 0, 64, 64));
			box.touchFunctionGUI();
			if(box.touched()){
				y = 10;
			}
			box = new BoundingBox(new Rectangle(TabletWars.w /2, TabletWars.h - 64, 64, 64));
			box.touchFunctionGUI();
			if(box.touched()){
				y = -10;
			}
			box = new BoundingBox(new Rectangle(0, TabletWars.h /2, 64, 64));
			box.touchFunctionGUI();
			if(box.touched()){
				x = -10;
			}
			box = new BoundingBox(new Rectangle(TabletWars.w -64, TabletWars.h /2, 64, 64));
			box.touchFunctionGUI();
			if(box.touched()){
				x = 10;
			}
			if(!checkIfCamTranslationIsValidOnAxis(x, true)){
				x = 0;
			}
			if(!checkIfCamTranslationIsValidOnAxis(y, false)){
				y = 0;
			}
		}
		cam.translate(new Vector3(x,y,0));
		cam.update();
	}
	
	private void setCameraPosition(Vector2 position){
		cam.position.set(position.x, -position.y, 0);
		correctCamera();
		cam.update();
	}
	
	private void correctCamera(){
		ContentLoader content = ContentLoader.getInstance();
		int mapSizeX = (content.mapSizeX) * content.tileSize;
		int mapSizeY = (content.mapSizeY) * content.tileSize;
		float camPosX = cam.position.x;
		float camPosY = cam.position.y;
		
		if(camPosX + cam.viewportWidth * 0.5f >= mapSizeX){
			cam.position.x = mapSizeX - cam.viewportWidth * 0.5f;
		}
		else if(camPosX - cam.viewportWidth * 0.5f <= 0){
			cam.position.x = cam.viewportWidth * 0.5f;
		}
		if(camPosY + cam.viewportHeight * 0.5f  >= mapSizeY * 0.5f -32){
			cam.position.y = mapSizeY * 0.5f - cam.viewportHeight * 0.5f - 32;
		}
		if(camPosY - cam.viewportHeight * 0.5f <= mapSizeY * -0.5f - 32){
			cam.position.y = mapSizeY * -0.5f + cam.viewportHeight * 0.5f - 32;
		}
	}
	
	private boolean checkIfCamTranslationIsValidOnAxis(float value, boolean xAxis){
		ContentLoader content = ContentLoader.getInstance();
		int mapSizeX = (content.mapSizeX) * content.tileSize;
		int mapSizeY = (content.mapSizeY) * content.tileSize;
		
	
		boolean isInBounds = true;
		if(xAxis){
			float camPosX = cam.position.x;
			if(value > 0){
				if(camPosX + cam.viewportWidth * 0.5f >= mapSizeX){
					cam.position.x = mapSizeX - cam.viewportWidth * 0.5f;
					isInBounds = false;
				}
			}
			else if(value < 0){
				if(camPosX - cam.viewportWidth * 0.5f <= 0){
					cam.position.x = cam.viewportWidth * 0.5f;
					isInBounds = false;
				}
			}
		}
		else{
			float camPosY = cam.position.y;
			if(value > 0){
				if(camPosY + cam.viewportHeight * 0.5f  >= mapSizeY * 0.5f -64){
					cam.position.y = mapSizeY * 0.5f - cam.viewportHeight * 0.5f - 32;
					isInBounds = false;
				}
			}
			else if(value < 0){
				if(camPosY - cam.viewportHeight * 0.5f <= mapSizeY * -0.5f - 32){
					cam.position.y = mapSizeY * -0.5f + cam.viewportHeight * 0.5f - 32;
					isInBounds = false;
				}
			}
		}
		return isInBounds;
	}
	
	public void switchPlayerCamera(boolean player1){
		if(player1){
			lerpPosition(new Vector2(1000, -cam.viewportHeight));
		}
		else{
			lerpPosition(new Vector2(1000, 1024 - cam.viewportHeight * 1.5f));
		}
	}
	
	private void lerpPosition(Vector2 position){
		if(position.dst(new Vector2(cam.position.x, cam.position.y)) > 10){
			if(cam.position.x < position.x){
				if(checkIfCamTranslationIsValidOnAxis(10, true)){
					cam.translate(new Vector3(5,0,0));
					//boundsReachedX = false;
				}
			}
			if(cam.position.x > position.x){
				if(checkIfCamTranslationIsValidOnAxis(-10, true)){
					cam.translate(new Vector3(-5,0,0));
					//boundsReachedX = false;
				}
			}
			if(cam.position.y < -position.y){
				if(checkIfCamTranslationIsValidOnAxis(10, false)){
					cam.translate(new Vector3(0,5,0));
					//boundsReachedY = false;
				}
			}
			if(cam.position.y > -position.y){
				if(checkIfCamTranslationIsValidOnAxis(-10, false)){
					cam.translate(new Vector3(0,-5,0));
					//boundsReachedY = false;
				}
			}
			correctCamera();
		}
	}
	
	public void FocusUnit(Unit unitToFocus){
		if(unitToFocus != null){
			Transform unitToFocusTransform = (Transform)unitToFocus.getComponent("Transform");
			Vector2  position = unitToFocusTransform.getPosition();
			setCameraPosition(new Vector2(position.x, position.y - cam.viewportHeight));
		}
	}
	
	public Vector2 getCurrentOffset(){
		float x = cam.position.x;
		float y = cam.position.y;
		return new Vector2(x - cam.viewportWidth * 0.5f, (y - cam.viewportHeight * 0.5f) * -1);
	}
}
