package tilemap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import api.Drawable;

public class Background implements Drawable {

	private BufferedImage image;

	private double x, y, dx, dy;

	private double moveScale;

	public Background(String image, double ms) {

		try {
			this.image = ImageIO.read(getClass().getResourceAsStream(image));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.moveScale = ms;
	}

	public void setPosition(double x, double y) {
		this.x = (x * this.moveScale) % GamePanel.WIDTH;
		this.y = (y * this.moveScale) % GamePanel.HEIGHT;
	}

	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}

	@Override
	public boolean update() {
		this.x += this.dx;
		this.y += this.dy;
		return true;
	}

	@Override
	public void draw(Graphics2D graphics) {
		graphics.drawImage(this.image, (int) this.x, (int) this.y, null);
		if (this.x < 0) {
			graphics.drawImage(this.image, (int) this.x + GamePanel.WIDTH,
					(int) this.y, null);
		} else if (this.x > 0) {
			graphics.drawImage(this.image, (int) this.x - GamePanel.WIDTH,
					(int) this.y, null);
		}
	}

}
