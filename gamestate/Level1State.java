package gamestate;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import main.GamePanel;
import tilemap.Background;
import tilemap.TileMap;
import entity.Enemy;
import entity.HUD;
import entity.Player;
import entity.enemies.Slugger;

public class Level1State extends GameState {

	private TileMap tileMap;
	private Background background;

	private Player player;
	private List<Enemy> enemies;
	private HUD hud;

	public Level1State(GameStateManager gsm) {
		super(gsm);
	}

	@Override
	public void init() {
		this.tileMap = new TileMap(30);
		this.tileMap.loadTiles("/Resources/Tilesets/grasstileset.gif");
		this.tileMap.loadMaps("/Resources/Maps/level1-1.map");
		this.tileMap.setPosition(0, 0);
		this.background = new Background("/Resources/Backgrounds/grassbg1.gif",
				0.1);
		this.player = new Player(this.tileMap);
		this.hud = new HUD(this.player);
		this.player.setPosition(100, 100);
		this.enemies = new ArrayList<Enemy>();

		Slugger slugger = new Slugger(this.tileMap);
		slugger.setPosition(150, 150);
		this.enemies.add(slugger);

	}

	@Override
	public void update() {
		this.player.update();
		this.tileMap.setPosition(GamePanel.WIDTH / 2 - this.player.getX(),
				GamePanel.HEIGHT / 2 - this.player.getY());

		this.background.setPosition(this.tileMap.getX(), this.tileMap.getY());

		for (int i = 0; i < this.enemies.size(); i++) {
			this.enemies.get(i).update();
			// if (this.enemies.get(i).idDead()) {
			// this.enemies.remove(i);
			// i--;
			// }
		}
	}

	@Override
	public void draw(Graphics2D graphics) {
		graphics.setColor(Color.white);
		graphics.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

		this.background.draw(graphics);

		this.tileMap.draw(graphics);

		this.player.draw(graphics);

		this.hud.draw(graphics);

		for (Enemy e : this.enemies) {
			e.draw(graphics);
		}
	}

	@Override
	public void keyPressed(int k) {
		if (k == KeyEvent.VK_LEFT) {
			this.player.setLeft(true);
		} else if (k == KeyEvent.VK_RIGHT) {
			this.player.setRight(true);
		} else if (k == KeyEvent.VK_UP) {
			this.player.setUp(true);
		} else if ((k == KeyEvent.VK_DOWN)) {
			this.player.setDown(true);
		} else if (k == KeyEvent.VK_SPACE) {
			this.player.setJumping(true);
		} else if (k == KeyEvent.VK_A) {
			this.player.setScratching();
		} else if ((k == KeyEvent.VK_S)) {
			this.player.setFiring();
		} else if (k == KeyEvent.VK_D) {
			this.player.setGliding(true);
		}
	}

	@Override
	public void keyReleased(int k) {
		if (k == KeyEvent.VK_LEFT) {
			this.player.setLeft(false);
		} else if (k == KeyEvent.VK_RIGHT) {
			this.player.setRight(false);
		} else if (k == KeyEvent.VK_UP) {
			this.player.setUp(false);
		} else if ((k == KeyEvent.VK_DOWN)) {
			this.player.setDown(false);
		} else if (k == KeyEvent.VK_SPACE) {
			this.player.setJumping(false);
		} else if (k == KeyEvent.VK_D) {
			this.player.setGliding(false);
		}
	}

}
