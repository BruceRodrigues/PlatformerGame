package gamestate;

import java.awt.Color;
import java.awt.Graphics2D;

import main.GamePanel;
import tilemap.TileMap;

public class Level1State extends GameState {

	private TileMap tileMap;

	public Level1State(GameStateManager gsm) {
		super(gsm);
	}

	@Override
	public void init() {
		this.tileMap = new TileMap(30);
		this.tileMap.loadTiles("/Resources/Tilesets/grasstileset.gif");
		this.tileMap.loadMaps("/Resources/Maps/level1-1.map");
		this.tileMap.setPosition(0, 0);
	}

	@Override
	public void update() {
	}

	@Override
	public void draw(Graphics2D graphics) {
		graphics.setColor(Color.white);
		graphics.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

		this.tileMap.draw(graphics);
	}

	@Override
	public void keyPressed(int k) {
	}

	@Override
	public void keyReleased(int k) {
	}

}
