package entity;

import java.awt.Rectangle;

import main.GamePanel;
import tilemap.Tile.Type;
import tilemap.TileMap;

public abstract class MapObject {

	protected TileMap tileMap;
	protected int tileSize;
	protected double xmap, ymap;

	protected double x, y;
	protected double dx, dy;

	protected int width, height;

	protected int cwidth, cheight;

	protected int currRow;
	protected int currCol;
	protected double xdest, ydest;
	protected double xtemp, ytemp;

	protected boolean topLeft, topRight, bottomLeft, bottomRight;

	protected Animation animation;
	protected int currentAction;
	protected int previousAction;
	protected boolean facingRight;

	protected boolean left, right, up, down;
	protected boolean jumping, falling;

	protected double moveSpeed, maxSpeed, stopSpeed, fallSpeed, maxFallSpeed;

	protected double jumpStart, stopJumpSpeed;

	public MapObject(TileMap tm) {
		this.tileMap = tm;
		this.tileSize = tm.getTileSize();
	}

	public boolean intersects(MapObject o) {
		return this.getRectangle().intersects(o.getRectangle());
	}

	public Rectangle getRectangle() {
		return new Rectangle((int) this.x - this.cwidth, (int) this.y
				- this.cheight, this.cwidth, this.cheight);
	}

	public void checkTileMapCollision() {
		this.currCol = (int) this.x / this.tileSize;
		this.currRow = (int) this.y / this.tileSize;

		this.xdest = this.x + this.dx;
		this.ydest = this.y + this.dy;

		this.xtemp = this.x;
		this.ytemp = this.y;

		calculateCorners(this.x, this.ydest);

		if (this.dy < 0) {
			if (this.topLeft || this.topRight) {
				this.dy = 0;
				this.ytemp = this.currRow * this.tileSize + this.cheight / 2;
			} else {
				this.ytemp += this.dy;
			}
		} else if (this.dy > 0) {
			if (this.bottomLeft || this.bottomRight) {
				this.dy = 0;
				this.falling = false;
				this.ytemp = (this.currRow + 1) * this.tileSize - this.cheight
						/ 2;
			} else {
				this.ytemp += this.dy;
			}
		}

		calculateCorners(this.xdest, this.y);
		if (this.dx < 0) {
			if (this.topLeft || this.bottomLeft) {
				this.dx = 0;
				this.xtemp = this.currCol * this.tileSize + this.cwidth / 2;
			} else {
				this.xtemp += this.dx;
			}
		} else if (this.dx > 0) {
			if (this.topRight || this.bottomRight) {
				this.dx = 0;
				this.xtemp = (this.currCol + 1) * this.tileSize - this.cwidth
						/ 2;
			} else {
				this.xtemp += this.dx;
			}
		}

		if (!this.falling) {
			calculateCorners(this.x, this.ydest + 1);
			if (!this.bottomLeft && !this.bottomRight) {
				this.falling = true;
			}
		}
	}

	public void calculateCorners(double x, double y) {
		int leftTile = (int) (x - this.cwidth / 2) / this.tileSize;
		int rightTile = (int) (x + this.cwidth / 2 - 1) / this.tileSize;
		int topTile = (int) (y - this.cheight / 2) / this.tileSize;
		int bottomTile = (int) (y + this.cheight / 2 - 1) / this.tileSize;

		this.topLeft = this.tileMap.getType(topTile, leftTile) == Type.BLOCKED;
		this.topRight = this.tileMap.getType(topTile, rightTile) == Type.BLOCKED;
		this.bottomLeft = this.tileMap.getType(bottomTile, leftTile) == Type.BLOCKED;
		this.bottomRight = this.tileMap.getType(bottomTile, rightTile) == Type.BLOCKED;

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

	public int getCWidth() {
		return this.cwidth;
	}

	public int getCHeight() {
		return this.cheight;
	}

	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void setVector(double x, double y) {
		this.dx = x;
		this.dy = y;
	}

	public void setMapPosition() {
		this.xmap = this.tileMap.getX();
		this.ymap = this.tileMap.getY();
	}

	public void setRight(boolean b) {
		this.right = b;
	}

	public void setUp(boolean b) {
		this.up = b;
	}

	public void setLeft(boolean b) {
		this.left = b;
	}

	public void setDown(boolean b) {
		this.down = b;
	}

	public void setJumping(boolean b) {
		this.jumping = b;
	}

	public boolean notOnScreen() {
		return this.x + this.xmap + this.width < 0
				|| this.x + this.xmap - this.width > GamePanel.WIDTH
				|| this.y + this.ymap + this.height < 0
				|| this.y + this.ymap - this.height > GamePanel.HEIGHT;
	}

}
