package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import tilemap.TileMap;
import api.Drawable;

public class FireBall extends MapObject implements Drawable {

	private boolean hit;
	private boolean remove;
	private BufferedImage[] sprites;
	private BufferedImage[] hitSprites;

	public FireBall(TileMap tm, boolean right) {
		super(tm);

		this.facingRight = right;

		this.moveSpeed = 3.8;
		if (right) {
			this.dx = this.moveSpeed;
		} else {
			this.dx = -this.moveSpeed;
		}

		this.width = 30;
		this.height = 30;
		this.cwidth = 14;
		this.cheight = 14;

		try {
			BufferedImage spriteSheet = ImageIO.read(getClass()
					.getResourceAsStream(
							"/Resources/Sprites/Player/fireball.gif"));

			this.sprites = new BufferedImage[4];
			for (int i = 0; i < this.sprites.length; i++) {
				this.sprites[i] = spriteSheet.getSubimage(i * this.width, 0,
						this.width, this.height);
			}
			this.hitSprites = new BufferedImage[3];
			for (int i = 0; i < this.hitSprites.length; i++) {
				this.hitSprites[i] = spriteSheet.getSubimage(i * this.width,
						this.height, this.width, this.height);
			}

			this.animation = new Animation();
			this.animation.setFrames(this.sprites);
			this.animation.setDelay(70);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void setHit() {
		if (this.hit) {
			return;
		}
		this.hit = true;
		this.animation.setFrames(this.hitSprites);
		this.animation.setDelay(70);
		this.dx = 0;
	}

	public boolean shouldRemove() {
		return this.remove;
	}

	@Override
	public boolean update() {
		checkTileMapCollision();
		setPosition(this.xtemp, this.ytemp);

		if (this.dx == 0 && !this.hit) {
			setHit();
		}

		this.animation.update();
		if (this.hit && this.animation.hasPlayedOnce()) {
			this.remove = true;
		}
		return true;
	}

	@Override
	public void draw(Graphics2D graphics) {
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
