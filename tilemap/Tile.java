package tilemap;

import java.awt.image.BufferedImage;

public class Tile {

	public enum Type {
		NORMAL, BLOCKED;
	}

	private BufferedImage image;
	private Type type;

	public Tile(BufferedImage image, Type type) {
		this.image = image;
		this.type = type;
	}

	public Type getType() {
		return this.type;
	}

	public BufferedImage getImage() {
		return this.image;
	}

}
