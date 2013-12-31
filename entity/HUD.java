package entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import api.Drawable;

public class HUD implements Drawable {

	private Player player;
	private BufferedImage image;
	private Font font;

	public HUD(Player player) {
		this.player = player;
		try {
			this.image = ImageIO.read(getClass().getResourceAsStream(
					"/Resources/HUD/hud.gif"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.font = new Font("Arial", Font.PLAIN, 14);
	}

	@Override
	public boolean update() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void draw(Graphics2D graphics) {
		graphics.setColor(Color.WHITE);
		graphics.drawImage(this.image, 0, 10, null);
		graphics.setFont(this.font);
		graphics.drawString(
				this.player.getHealth() + "/" + this.player.getMaxHealth(), 30,
				25);
		graphics.drawString(
				this.player.getFire() / 100 + "/" + this.player.getMaxFire()
						/ 100, 30, 45);
	}

}
