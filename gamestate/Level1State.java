package gamestate;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import main.GamePanel;
import tilemap.Background;
import tilemap.TileMap;
import audio.AudioPlayer;
import entity.Enemy;
import entity.Explosion;
import entity.HUD;
import entity.Player;
import entity.enemies.Slugger;

public class Level1State extends GameState {

	private TileMap tileMap;
	private Background background;

	private Player player;
	private List<Enemy> enemies;
	private List<Explosion> explosions;
	private HUD hud;
	private AudioPlayer audio;

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
		this.explosions = new ArrayList<Explosion>();

		this.audio = new AudioPlayer("/Resources/Music/level1-1.mp3");
		this.audio.play();

		populateEnemies();

	}

	private void populateEnemies() {
		Slugger s;
		Point[] points = new Point[] { new Point(860, 200),
				new Point(1525, 200), new Point(1680, 200),
				new Point(1800, 200), new Point(150, 150) };

		for (int i = 0; i < points.length; i++) {
			s = new Slugger(this.tileMap);
			s.setPosition(points[i].getX(), points[i].getY());
			this.enemies.add(s);

		}

	}

	@Override
	public void update() {
		this.player.update();
		this.tileMap.setPosition(GamePanel.WIDTH / 2 - this.player.getX(),
				GamePanel.HEIGHT / 2 - this.player.getY());

		this.background.setPosition(this.tileMap.getX(), this.tileMap.getY());

		this.player.checkAtack(this.enemies);

		for (int i = 0; i < this.enemies.size(); i++) {
			this.enemies.get(i).update();
			if (this.enemies.get(i).idDead()) {
				Enemy e = this.enemies.remove(i);
				i--;
				this.explosions.add(new Explosion(e.getX(), e.getY()));
			}
		}

		for (int i = 0; i < this.explosions.size(); i++) {
			this.explosions.get(i).update();
			if (this.explosions.get(i).shouldRemove()) {
				this.explosions.remove(i);
				i--;
			}
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

		for (Explosion explosion : this.explosions) {
			explosion.setMapPosition(this.tileMap.getX(), this.tileMap.getY());
			explosion.draw(graphics);
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
		} else if (k == KeyEvent.VK_SHIFT) {
			this.player.setBackOnTime(true);
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
		} else if (k == KeyEvent.VK_SHIFT) {
			this.player.setBackOnTime(false);
		}
	}

}
