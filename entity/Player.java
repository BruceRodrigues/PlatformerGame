package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import tilemap.TileMap;
import api.Drawable;

public class Player extends MapObject implements Drawable {

	public int health, maxHealth;

	private int fire, maxFire;

	private boolean dead;

	private boolean flinching;
	private long flinchingTime;

	private boolean firing;
	private int fireCost;
	private int fireBallDamage;
	// private ArrayList<FireBall> fireBalls;

	private boolean scrathing;
	private int scratchDamage;
	private int scratchRange;

	private boolean gliding;

	private ArrayList<BufferedImage[]> sprites;
	// number of frames for each animation
	private final int[] numFrames = { 2, 8, 1, 2, 4, 2, 5 };

	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int GLIDING = 4;
	private static final int FIREBALL = 5;
	private static final int SCRATCHING = 6;

	public Player(TileMap tm) {
		super(tm);

		this.width = 30;
		this.height = 30;
		this.cwidth = 20;
		this.cheight = 20;

		this.moveSpeed = 0.3;
		this.maxSpeed = 1.6;
		this.stopSpeed = 0.4;
		this.fallSpeed = 0.15;
		this.maxFallSpeed = 4.0;
		this.jumpStart = -4.8;
		this.stopJumpSpeed = 0.3;

		this.facingRight = true;

		this.health = this.maxHealth = 5;
		this.fire = this.maxFire = 2500;

		this.fireCost = 200;
		this.fireBallDamage = 5;

		this.scratchDamage = 8;
		this.scratchRange = 40;

		try {
			BufferedImage spriteSheet = ImageIO.read(getClass()
					.getResourceAsStream(
							"/Resources/Sprites/Player/playersprites.gif"));

			this.sprites = new ArrayList<BufferedImage[]>();

			for (int i = 0; i < 7; i++) {
				BufferedImage[] bi = new BufferedImage[this.numFrames[i]];
				for (int j = 0; j < this.numFrames[i]; j++) {
					bi[j] = spriteSheet.getSubimage(j * this.width, i
							* this.height, this.width, this.height);
					if (i == 6) {
						bi[j] = spriteSheet.getSubimage(j * this.width * 2, i
								* this.height, this.width, this.height);
					}
				}
				this.sprites.add(bi);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.animation = new Animation();
		this.currentAction = IDLE;
		this.animation.setFrames(this.sprites.get(IDLE));
		this.animation.setDelay(400);
	}

	public int getHealth() {
		return this.health;
	}

	public int getMaxHealth() {
		return this.maxHealth;
	}

	public int getFire() {
		return this.fire;
	}

	public int getMaxFire() {
		return this.maxFire;
	}

	public void setFiring() {
		this.firing = true;
	}

	public void setScratching() {
		this.scrathing = true;
	}

	public void setGliding(boolean b) {
		this.gliding = b;
	}

	@Override
	public boolean update() {

		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(this.xtemp, this.ytemp);

		// set animation
		if (this.scrathing) {
			if (this.currentAction != SCRATCHING) {
				this.currentAction = SCRATCHING;
				this.animation.setFrames(this.sprites.get(SCRATCHING));
				this.animation.setDelay(50);
				this.width = 60;
			}
		} else if (this.firing) {
			if (this.currentAction != FIREBALL) {
				this.currentAction = FIREBALL;
				this.animation.setFrames(this.sprites.get(FIREBALL));
				this.animation.setDelay(100);
				this.width = 30;
			}
		} else if (this.dy > 0) {
			if (this.gliding) {
				if (this.currentAction != GLIDING) {
					this.currentAction = GLIDING;
					this.animation.setFrames(this.sprites.get(GLIDING));
					this.animation.setDelay(100);
					this.width = 30;
				}
			} else if (this.currentAction != FALLING) {
				this.currentAction = FALLING;
				this.animation.setFrames(this.sprites.get(FALLING));
				this.animation.setDelay(100);
				this.width = 30;
			}
		} else if (this.dy < 0) {
			if (this.currentAction != JUMPING) {
				this.currentAction = JUMPING;
				this.animation.setFrames(this.sprites.get(JUMPING));
				this.animation.setDelay(-1);
				this.width = 30;
			}
		} else if (this.left || this.right) {
			if (this.currentAction != WALKING) {
				this.currentAction = WALKING;
				this.animation.setFrames(this.sprites.get(WALKING));
				this.animation.setDelay(40);
				this.width = 30;
			}
		} else {
			if (this.currentAction != IDLE) {
				this.currentAction = IDLE;
				this.animation.setFrames(this.sprites.get(IDLE));
				this.animation.setDelay(400);
				this.width = 30;

			}
		}

		this.animation.update();

		if (this.currentAction != SCRATCHING && this.currentAction != FIREBALL) {
			if (this.right) {
				this.facingRight = true;
			} else if (this.left) {
				this.facingRight = false;
			}
		}

		return true;
	}

	@Override
	public void draw(Graphics2D graphics) {
		setMapPosition();

		// draw player
		if (this.flinching) {
			long elapsed = (System.nanoTime() - this.flinchingTime) / 1000000;
			if (elapsed / 100 % 2 == 0) {
				return;
			}
		}

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
		} else {
			if (this.dx > 0) {
				this.dx -= this.stopSpeed;
				if (this.dx < 0) {
					this.dx = 0;
				}
			} else if (this.dx < 0) {
				this.dx += this.stopSpeed;
				if (this.dx > 0) {
					this.dx = 0;
				}
			}
		}

		// cannot move while atacking
		if ((this.currentAction == SCRATCHING || this.currentAction == FIREBALL)
				&& !(this.jumping || this.falling)) {
			this.dx = 0;
		}

		if (this.jumping && !this.falling) {
			this.dy = this.jumpStart;
			this.falling = true;
		}

		if (this.falling) {
			if (this.dy > 0 && this.gliding) {
				this.dy += this.fallSpeed * 0.1;
			} else {
				this.dy += this.fallSpeed;
			}

			if (this.dy > 0) {
				this.jumping = false;
			} else if (this.dy < 0 && !this.jumping) {
				this.dy += this.stopJumpSpeed;
			}
			if (this.dy > this.maxFallSpeed) {
				this.dy = this.maxFallSpeed;
			}
		}
	}

}
