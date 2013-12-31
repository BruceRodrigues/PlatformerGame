package tilemap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import main.GamePanel;
import tilemap.Tile.Type;
import api.Drawable;

public class TileMap implements Drawable {

	private double x, y;

	private int xmin, ymin, xmax, ymax;

	private double tween;

	private int[][] map;
	private int tileSize;
	private int numRows, numCols;
	private int width, height;

	private BufferedImage tileSet;
	private int numTileAcross;
	private Tile[][] tiles;

	private int rowOffset, colOffset;
	private int numRowsToDraw, numColsToDraw;

	public TileMap(int tileSize) {
		this.tileSize = tileSize;
		this.numRowsToDraw = GamePanel.HEIGHT / tileSize + 2;
		this.numColsToDraw = GamePanel.WIDTH / tileSize + 2;
		this.tween = 0.07;
	}

	public void loadTiles(String s) {
		try {
			this.tileSet = ImageIO.read(getClass().getResourceAsStream(s));
			this.numTileAcross = this.tileSet.getWidth() / this.tileSize;
			this.tiles = new Tile[2][this.numTileAcross];

			BufferedImage subImage;
			for (int col = 0; col < this.numTileAcross; col++) {
				subImage = this.tileSet.getSubimage(col * this.tileSize, 0,
						this.tileSize, this.tileSize);
				this.tiles[0][col] = new Tile(subImage, Type.NORMAL);
				subImage = this.tileSet.getSubimage(col * this.tileSize,
						this.tileSize, this.tileSize, this.tileSize);
				this.tiles[1][col] = new Tile(subImage, Type.BLOCKED);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadMaps(String s) {

		try {
			InputStream in = getClass().getResourceAsStream(s);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			this.numCols = Integer.parseInt(br.readLine());
			this.numRows = Integer.parseInt(br.readLine());
			this.map = new int[this.numRows][this.numCols];
			this.width = this.numCols * this.tileSize;
			this.height = this.numRows * this.tileSize;

			this.xmin = GamePanel.WIDTH - this.width;
			this.xmax = 0;
			this.ymin = GamePanel.HEIGHT - this.height;
			this.ymax = 0;

			String delims = "\\s+";
			for (int row = 0; row < this.numRows; row++) {
				String line = br.readLine();
				String[] tokens = line.split(delims);
				for (int col = 0; col < this.numCols; col++) {
					this.map[row][col] = Integer.parseInt(tokens[col]);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getTileSize() {
		return this.tileSize;
	}

	public int getX() {
		return (int) this.x;
	}

	public int getY() {
		return (int) this.y;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public Type getType(int row, int col) {
		int rc = this.map[row][col];
		int r = rc / this.numTileAcross;
		int c = rc % this.numTileAcross;
		return this.tiles[r][c].getType();
	}

	public void setPosition(double x, double y) {
		this.x += (x - this.x) * this.tween;
		this.y += (y - this.y) * this.tween;

		this.fixBounds();
		this.colOffset = (int) -this.x / this.tileSize;
		this.rowOffset = (int) -this.y / this.tileSize;
	}

	public void fixBounds() {
		if (this.x < this.xmin) {
			this.x = this.xmin;
		}
		if (this.x > this.xmax) {
			this.x = this.xmax;
		}
		if (this.y < this.ymin) {
			this.y = this.ymin;
		}
		if (this.y > this.ymax) {
			this.y = this.ymax;
		}

	}

	@Override
	public boolean update() {
		return true;
	}

	@Override
	public void draw(Graphics2D graphics) {
		for (int row = this.rowOffset; row < this.rowOffset
				+ this.numRowsToDraw; row++) {
			if (row >= this.numRows) {
				break;
			}
			for (int col = this.colOffset; col < this.colOffset
					+ this.numColsToDraw; col++) {
				if (col >= this.numCols) {
					break;
				}
				if (this.map[row][col] == 0) {
					continue;
				}

				int rc = this.map[row][col];
				int r = rc / this.numTileAcross;
				int c = rc % this.numTileAcross;

				graphics.drawImage(this.tiles[r][c].getImage(), (int) this.x
						+ col * this.tileSize, (int) this.y + row
						* this.tileSize, null);
			}
		}
	}
}
