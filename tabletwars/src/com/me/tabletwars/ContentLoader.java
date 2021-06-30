/*
 * @author Jan-Hendrik Kahle
 */
package com.me.tabletwars;

import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.me.tabletwars.enums.ANIMATIONTYPE;
import com.me.tabletwars.enums.BARTYPE;
import com.me.tabletwars.enums.BORDERTYPE;
import com.me.tabletwars.enums.BUTTONTYPE;
import com.me.tabletwars.enums.DIRECTION;
import com.me.tabletwars.enums.MENUITEM;
import com.me.tabletwars.enums.UNITTYPE;
import com.me.tabletwars.factory.FlameFactory;
import com.me.tabletwars.factory.ShotgunFactory;
import com.me.tabletwars.factory.SniperFactory;
import com.me.tabletwars.factory.SoldierFactory;
import com.me.tabletwars.factory.TileFactory;
import com.me.tabletwars.gameobjects.Tile;

public class ContentLoader {

	private static ContentLoader instance;
	private Texture mapTexture;
	private Texture unitTextureAtlas;
	private Texture guiTextureAtlas;
	private Texture tileTextureAtlas;
	private Texture screenTextureAtlas;
	private Texture fireAtlas;
	private Texture menuBackground;
	private Element mapXML;
	private Element balanceXML;
	private Element unitAtlasXML;
	private Element guiAtlasXML;
	private Element borderXML;
	private Element screenXML;
	private Element fireXML;
	private int curLoadingFrame = 0;
	private Array<Element> tileElements;
	public Tile[][] mapTiles;
	public int tileSize;
	public int mapSizeX;
	public int mapSizeY;
	private TileFactory tileFactory;
	private ArrayList<Sound> unitSounds = new ArrayList<Sound>();
	private Music backgroundMusic;
	
	private ContentLoader() {
		loadContent();
		tileFactory = new TileFactory();
	}
	
	public static ContentLoader getInstance() {
		
		if(instance == null)
			instance = new ContentLoader();
			
		return instance;
	}
	
	public int getTileSize() {
		return tileSize;
	}
	
	private void loadContent() {
		loadXML();
		loadTextures();
		loadMusicAndSounds();
		setFactoryParameters();
	}
	
	private void loadXML() {
		balanceXML = LoadXMLFromFile("data/XML/balance.xml");
		mapXML = LoadXMLFromFile("data/XML/map.xml");
		unitAtlasXML = LoadXMLFromFile("data/XML/units.xml");
		borderXML = LoadXMLFromFile("data/XML/borders.xml");
		guiAtlasXML = LoadXMLFromFile("data/XML/guiAtlas.xml");
		screenXML = LoadXMLFromFile("data/XML/final.xml");
		fireXML = LoadXMLFromFile("data/XML/flameAnimation.xml");
	}
	
	private Element LoadXMLFromFile(String filePath) {
		XmlReader reader = new XmlReader();
		Element element = null;
		try {
			element = reader.parse(Gdx.files.internal(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return element;
	}
	
	private void setFactoryParameters() {
		
		String dmgString = "damage";
		String healthString = "maxHealth";
		String moveString = "movRange";
		int damage, maxHealth, movementRange;
		
		
		Element currentElement = balanceXML.getChildByNameRecursive("Soldier");
		damage = currentElement.getIntAttribute(dmgString);
		maxHealth = currentElement.getIntAttribute(healthString);
		movementRange = currentElement.getIntAttribute(moveString);
		SoldierFactory.setParameters(damage, maxHealth, movementRange);
		
		currentElement = balanceXML.getChildByNameRecursive("Sniper");
		damage = currentElement.getIntAttribute(dmgString);
		maxHealth = currentElement.getIntAttribute(healthString);
		movementRange = currentElement.getIntAttribute(moveString);
		SniperFactory.setParameters(damage, maxHealth, movementRange);
		
		currentElement = balanceXML.getChildByNameRecursive("Shotgun");
		damage = currentElement.getIntAttribute(dmgString);
		maxHealth = currentElement.getIntAttribute(healthString);
		movementRange = currentElement.getIntAttribute(moveString);
		ShotgunFactory.setParameters(damage, maxHealth, movementRange);
		
		currentElement = balanceXML.getChildByNameRecursive("Flamethrower");
		damage = currentElement.getIntAttribute(dmgString);
		maxHealth = currentElement.getIntAttribute(healthString);
		movementRange = currentElement.getIntAttribute(moveString);
		FlameFactory.setParameters(damage, maxHealth, movementRange);

	}
	
	private void loadMusicAndSounds() {
		loadSounds();
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("data/Music/music1.mp3"));
	}
	
	private void loadSounds() {
		this.unitSounds.add(Gdx.audio.newSound(Gdx.files.internal("data/Music/sniper.mp3")));
		this.unitSounds.add(Gdx.audio.newSound(Gdx.files.internal("data/Music/flame.mp3")));
		this.unitSounds.add(Gdx.audio.newSound(Gdx.files.internal("data/Music/soldier.mp3")));
		this.unitSounds.add(Gdx.audio.newSound(Gdx.files.internal("data/Music/shotgun.mp3")));
		this.unitSounds.add(Gdx.audio.newSound(Gdx.files.internal("data/Music/footsteps.mp3")));
	}
	
	public Music getBackgroundMusic() {
		return this.backgroundMusic;
	}
	
	public Sound getUnitSound(UNITTYPE unittype) {
		return this.unitSounds.get(unittype.ordinal());
	}
	
	public Sound getFootStepSound() {
		return this.unitSounds.get(4);
	}
	
	private void loadTextures() {
		loadTileTextures();
		loadUnitTextures();
		loadGUITextures();
		loadScreenTextures();
		loadMapTexture();
		loadMenuBackgroundTexture();
		loadFireAtlas();
	}
	
	private void loadFireAtlas(){
		fireAtlas = new Texture(Gdx.files.internal("data/TextureAtlas/flameAnimation.png"));
	}
	
	private void loadMenuBackgroundTexture() {
		menuBackground = new Texture(Gdx.files.internal("data/TextureAtlas/startScreen.png"));
	}

	private void loadScreenTextures() {
		screenTextureAtlas = new Texture(Gdx.files.internal("data/TextureAtlas/final.png"));
	}
	
	private void loadGUITextures() {
		guiTextureAtlas = new Texture(Gdx.files.internal("data/TextureAtlas/guiAtlas.png"));
	}
	
	private void loadTileTextures() {
	 tileTextureAtlas =	new Texture(Gdx.files.internal("data/TextureAtlas/borders.png"));
	}
	
	private void loadUnitTextures() {
		unitTextureAtlas = new Texture(Gdx.files.internal("data/TextureAtlas/units.png"));
	}
	
	private void loadMapTexture() {
		mapTexture = new Texture(Gdx.files.internal("data/TextureAtlas/map.jpg"));
	}
	
	public Texture getMapTexture() {
		return mapTexture;
	}

	public Texture getBackgroundTexture() {
		return menuBackground;
	}
	public TextureRegion getUnitTexture(UNITTYPE unittype, ANIMATIONTYPE animationtype, boolean blueIsOwner) {
		
		TextureRegion region = null;
		
		
		switch (unittype) {
		case FLAMETHROWER:
			region = getUnitTexture("flame", blueIsOwner, animationtype);
			break;
		case SHOTGUN:
		case SOLDIER:
			region = getUnitTexture("soldier", blueIsOwner, animationtype);
			break;
		case SNIPER:
			region = getUnitTexture("sniper",  blueIsOwner, animationtype);
			break;
		}
				
		return region;
	}
	
	public ArrayList<TextureRegion> getFireAnimation(){
		ArrayList<TextureRegion> regions = new ArrayList<TextureRegion>();
		for(Element element : fireXML.getChildrenByName("sprite")){
			TextureRegion r =new TextureRegion(fireAtlas, element.getInt("x"), element.getInt("y"), element.getInt("w"), element.getInt("h"));
			regions.add(r);
		}
		return regions;
	}

	public TextureRegion getGameOverTexture(boolean blueWon) {
		TextureRegion region = null;
		Element gameOver = null;
		
		Element bothScreens = screenXML.getChildByName("gameover");
		
		for (Element element : bothScreens.getChildrenByName("sprite")) {
			if(element.getBooleanAttribute("n") == blueWon) {
				gameOver = element;
			}
		}
		
		region = loadTextureFromAtlas(gameOver, screenTextureAtlas);
		
		return region;
	}
	
	public TextureRegion getCreditsTexture() {
		TextureRegion region = null;
		
		Element credits = screenXML.getChildByName("credits");
		
		region = loadTextureFromAtlas(credits, screenTextureAtlas);
		
		return region;		
	}
	
	protected TextureRegion getUnitTexture(String unitType, boolean ownedByBlue, ANIMATIONTYPE animationType) {
		
		TextureRegion region = null;
		Element unit = null;
		
		String factionString = ownedByBlue ? "blue" : "green";
		
		Element faction = unitAtlasXML.getChildByName(factionString);
		
		for (Element element : faction.getChildrenByName(unitType)) {
			
			if(element.getIntAttribute("n") == animationType.ordinal())
			{
				unit = element;
				break;
			}
		}
		
		region = loadTextureFromAtlas(unit, unitTextureAtlas);
		
		return region;		
	}

	private TextureRegion loadTextureFromAtlas(Element informationElement, Texture textureAtlas) {
		TextureRegion region;
		int posX = informationElement.getInt("x");
		int posY = informationElement.getInt("y");
		int width = informationElement.getInt("w");
		int height = informationElement.getInt("h");
		
		region = new TextureRegion(textureAtlas, posX, posY, width, height);
		return region;
	}
	
	public TextureRegion getPlayerFont(boolean bluePlayer) {
		TextureRegion region = null;
		
		int posX = bluePlayer ? 32 : 16;
		int posY = bluePlayer ? 512 : 640;
		int width = bluePlayer ? 160-32 : 170-16 ;
		int height = 128;
		
		region = new TextureRegion(screenTextureAtlas, posX, posY, width, height);
		
		return region;
	}
	
	public TextureRegion getTileTexture(BORDERTYPE borderColor) {
		TextureRegion region = null;
		Element border = null;
		
		for(Element element : borderXML.getChildrenByName("border")) {
			
			if(element.getIntAttribute("n") == borderColor.ordinal()){
				border = element;
			}
		}
		
		region = loadTextureFromAtlas(border, tileTextureAtlas);
		
		return region;
	}

	public TextureRegion getFontTexture(MENUITEM item) {
		TextureRegion region = null;
		Element gui = guiAtlasXML.getChildByName("font");
		
		
		for(Element element : gui.getChildrenByName("sprite")) {
			
			if(element.getIntAttribute("n") == item.ordinal()){
				gui = element;
				break;
			}
		}
		
		region = loadTextureFromAtlas(gui, guiTextureAtlas);
		
		return region;
	}
	
	public TextureRegion getButtonTexture(BUTTONTYPE state) {
		TextureRegion region = null;
		Element gui = guiAtlasXML.getChildByName("texture");
		Element buttonTexture = null;
		
		for(Element element : gui.getChildrenByName("sprite")) {
			
			if(element.getIntAttribute("n") == state.ordinal()){
				buttonTexture = element;
				break;
			}
		}
		
		region = loadTextureFromAtlas(buttonTexture, guiTextureAtlas);
		
		return region;
	}
	
	public TextureRegion getSimpleColorTexture(BARTYPE type) {
		TextureRegion region = null;
		Element gui = guiAtlasXML.getChildByName("bars");
		
		
		for(Element element : gui.getChildrenByName("sprite")) {
			
			if(element.getIntAttribute("n") == type.ordinal()){
				gui = element;
				break;
			}
		}
		
		region = loadTextureFromAtlas(gui, guiTextureAtlas);
		
		return region;
	}
	
	public TextureRegion getTutTextureRegion(int textureID){
		String textureName = "Tut" + textureID;
		Texture tex = new Texture(Gdx.files.internal("data/TutTextures/" + textureName + ".png"));
		TextureRegion region = new TextureRegion(tex);
		return region;
	}
	
	/* <summary>
	 * Author Jan-Hendrik Kahle
	 * Create Map : Create Tilemap from the list of Elements from the XML 
	 * </summary>
	 */
	public boolean createMapTiles() {
		
		boolean complete = false;
		
		if(tileElements == null) {
			setMapParametres();
		}
		
		complete = fillMapArray();
		
		if(complete) {
			setAdjacentTiles();
		}
		return complete;
	}

	private void setMapParametres() {
		Element root = mapXML;
		this.tileElements = root.getChildrenByName("Tile");
		this.mapSizeX = root.getChildByName("mapSize").getIntAttribute("x");
		this.mapSizeY = root.getChildByName("mapSize").getIntAttribute("y");
		this.tileSize = root.getChildByName("mapSize").getIntAttribute("tileSize");
		
		this.mapTiles = new Tile[mapSizeX][mapSizeY];
	}
	
	private boolean fillMapArray() {
		
		boolean complete = true;
		
		for (int curTileIndex = (curLoadingFrame*10) ; curTileIndex < tileElements.size; curTileIndex++) {
			
			if(curTileIndex == ((curLoadingFrame+1)*10))
			{
				complete = false;
				curLoadingFrame++;
				break;
			}
			
			Element tile = tileElements.get(curTileIndex);
			
			boolean obstacle = tile.getBooleanAttribute("obstacle");
			int posiX = tile.getIntAttribute("x");
			int posiY = tile.getIntAttribute("y");
			Vector2 position = new Vector2(0.0f + (float)posiX * (float)tileSize, 0.0f + (float)posiY * (float)tileSize);
			
			tileFactory.setBaseTexture(getTileTexture(BORDERTYPE.BLACK));
			Tile curTile = tileFactory.create(position,obstacle);
			
			mapTiles[posiX][posiY] = curTile;
		}
		
		
		return complete;
	}

	/* <summary>
	 * Author Jan-Hendrik Kahle
	 * Create Map : Set the adjacent Tile-Pointers for the respective Tiles 
	 * </summary>
	 */
	private void setAdjacentTiles() {
		for (int x = 0; x <= (mapSizeX-1); x++)
		{
			for(int y = 0; y <= (mapSizeY-1); y++)
			{
				Tile baseTile = mapTiles[x][y];
				if(!((x+1)>=mapSizeX))  {
					Tile rightTile = mapTiles[x+1][y];
					baseTile.setNeighborTile(DIRECTION.RIGHT, rightTile);
					rightTile.setNeighborTile(DIRECTION.LEFT, baseTile);
				}
				if (!((y+1)>=mapSizeY)) {
					Tile bottomTile = mapTiles[x][y+1];
					baseTile.setNeighborTile(DIRECTION.DOWN, bottomTile);					
					bottomTile.setNeighborTile(DIRECTION.UP, baseTile);
				}																
				baseTile.setName(("TileX " + x + " TileY: " + y));
				
			}
		}
	}	
}
