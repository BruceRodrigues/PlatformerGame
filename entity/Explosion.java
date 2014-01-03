package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import api.Drawable;

public class Explosion implements Drawable {

	private int x, y;
	private int xmap, ymap;

	private int width, height;

	private Animation animation;
	private BufferedImage[] sprites;

	private boolean remove;

	public Explosion(int x, int y) {
		this.x = x;
		this.y = y;

		this.width = 30;
		this.height = 30;

		try {
			BufferedImage spritesheet = ImageIO.read(getClass()
					.getResourceAsStream(
							"/Resources/Sprites/Enemies/explosion.gif"));

			this.sprites = new BufferedImage[6];
			for (int i = 0; i < this.sprites.length; i++) {
				this.sprites[i] = spritesheet.getSubimage(i * this.width, 0,
						this.width, this.height);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.animation = new Animation();
		this.animation.setFrames(this.sprites);
		this.animation.setDelay(70);
	}

	@Override
	public boolean update() {
		this.animation.update();
		if (this.animation.hasPlayedOnce()) {
			this.remove = true;
		}
		return true;
	}

	public boolean shouldRemove() {
		return this.remove;
	}

	public void setMapPosition(int x, int y) {
		this.xmap = x;
		this.ymap = y;
	}

	@Override
	public void draw(Graphics2D graphics) {
		graphics.drawImage(this.animation.getImage(), this.x + this.xmap
				- this.width / 2, this.y + this.ymap - this.height / 2, null);
	}

}
