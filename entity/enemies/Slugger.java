package entity.enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import tilemap.TileMap;
import entity.Animation;
import entity.Enemy;

public class Slugger extends Enemy {

	private BufferedImage[] sprites;

	public Slugger(TileMap tm) {
		super(tm);

		this.moveSpeed = 0.3;
		this.maxSpeed = 0.3;
		this.fallSpeed = 0.2;
		this.maxFallSpeed = 10.0;

		this.width = 30;
		this.height = 30;

		this.cwidth = 20;
		this.cheight = 20;

		this.health = this.maxHealth = 2;
		this.damage = 1;

		try {
			BufferedImage spritesheet = ImageIO.read(getClass()
					.getResourceAsStream(
							"/Resources/Sprites/Enemies/slugger.gif"));

			this.sprites = new BufferedImage[3];
			for (int i = 0; i < this.sprites.length; i++) {
				this.sprites[i] = spritesheet.getSubimage(i * this.width, 0,
						this.width, this.height);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		this.animation = new Animation();
		this.animation.setFrames(this.sprites);
		this.animation.setDelay(300);

		this.right = true;
	}

	private void getNextPosition() {
		if (this.left) {
			this.dx -= this.moveSpeed;
			if (this.dx < -this.maxSpeed) {
				this.dx = -this.maxSpeed;
			}
		} else if (this.right) {
			this.dx += this.moveSpeed;
			if (this.dx > this.maxSpeed) {
				this.dx = this.maxSpeed;
			}
		}

		if (this.falling) {
			this.dy += this.fallSpeed;
		}
	}

	@Override
	public boolean update() {
		getNextPosition();
		checkTileMapCollision();
		setPosition(this.xtemp, this.ytemp);
		if (this.flinching) {
			long elapsed = (System.nanoTime() - this.flinchTimer) / 1000000;
			if (elapsed > 400) {
				this.flinching = false;
			}
		}

		// if hits a wall, go to other direction
		if (this.right && this.dx == 0) {
			this.right = false;
			this.left = true;
			this.facingRight = false;
		} else if (this.left && this.dx == 0) {
			this.right = true;
			this.left = false;
			this.facingRight = true;
		}

		this.animation.update();

		return true;
	}

	@Override
	public void draw(Graphics2D graphics) {

		if (notOnScreen()) {
			// return;
		}
		setMapPosition();

		if (this.facingRight) {
			graphics.drawImage(this.animation.getImage(), (int) (this.x
					+ this.xmap - this.width / 2),
					(int) (this.y + this.ymap - this.height / 2), null);
		} else {
			graphics.drawImage(this.animation.getImage(), (int) (this.x
					+ this.xmap - this.width / 2 + this.width), (int) (this.y
					+ this.ymap - this.height / 2), -this.width, this.height,
					null);
		}
	}

}
