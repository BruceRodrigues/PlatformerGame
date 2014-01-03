package entity;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import tilemap.TileMap;
import api.Drawable;
import audio.AudioPlayer;

public class Player extends MapObject implements Drawable {

	public int health, maxHealth;

	private int fire, maxFire;

	private boolean dead;

	private boolean flinching;
	private long flinchingTime;

	private boolean firing;
	private int fireCost;
	private int fireBallDamage;
	private ArrayList<FireBall> fireBalls;

	private boolean scratching;
	private int scratchDamage;
	private int scratchRange;

	private ArrayList<Point> history;

	private boolean gliding;

	private boolean backOnTime;

	private Map<String, AudioPlayer> sfx;

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

		this.moveSpeed = 0.5;
		this.maxSpeed = 1.6;
		this.stopSpeed = 0.4;
		this.fallSpeed = 0.15;
		this.maxFallSpeed = 4.0;
		this.jumpStart = -4.8;
		this.stopJumpSpeed = 0.3;

		this.facingRight = true;

		this.backOnTime = false;

		this.health = this.maxHealth = 5;
		this.fire = this.maxFire = 2500;

		this.fireCost = 200;
		this.fireBallDamage = 5;

		this.scratchDamage = 8;
		this.scratchRange = 40;

		this.fireBalls = new ArrayList<FireBall>();

		this.history = new ArrayList<Point>();

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
								* this.height, this.width * 2, this.height);
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

		this.sfx = new HashMap<String, AudioPlayer>();
		this.sfx.put("jump", new AudioPlayer("/Resources/SFX/jump.mp3"));
		this.sfx.put("scratch", new AudioPlayer("/Resources/SFX/scratch.mp3"));
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
		this.scratching = true;
	}

	public void setGliding(boolean b) {
		this.gliding = b;
	}

	public void setBackOnTime(boolean b) {
		this.backOnTime = b;
	}

	@Override
	public boolean update() {

		// update position
		getNextPosition();
		checkTileMapCollision();
		if (!this.backOnTime) {
			setPosition(this.xtemp, this.ytemp);
		} else {
			if (!this.history.isEmpty()) {
				Point e = this.history.remove(this.history.size() - 1);
				setPosition(e.getX(), e.getY());
			}
		}
		if (!this.backOnTime) {
			this.history.add(this.history.size() % 100, new Point(
					(int) this.xtemp, (int) this.ytemp));

		}

		for (int i = 0; i < this.fireBalls.size(); i++) {
			this.fireBalls.get(i).update();
			if (this.fireBalls.get(i).shouldRemove()) {
				this.fireBalls.remove(i);
				i--;
			}
		}

		if (this.currentAction == FIREBALL && this.animation.hasPlayedOnce()) {
			this.firing = false;
		}
		if (this.currentAction == SCRATCHING && this.animation.hasPlayedOnce()) {
			this.scratching = false;
		}

		// fireball atack
		this.fire++;
		if (this.fire > this.maxFire) {
			this.fire = this.maxFire;
		}
		if (this.firing && this.currentAction != FIREBALL) {
			if (this.fire > this.fireCost) {
				this.fire -= this.fireCost;
				FireBall fireball = new FireBall(this.tileMap, this.facingRight);
				fireball.setPosition(this.x, this.y);
				this.fireBalls.add(fireball);
			}
		}

		if (this.flinching) {
			long elapsed = (System.nanoTime() - this.flinchingTime) / 1000000;
			if (elapsed > 1000) {
				this.flinching = false;
			}
		}

		// set animation
		if (this.scratching) {
			if (this.currentAction != SCRATCHING) {
				this.sfx.get("scratch").play();
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

		for (FireBall fireball : this.fireBalls) {
			fireball.draw(graphics);
		}

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
			this.sfx.get("jump").play();
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

	public void checkAtack(List<Enemy> enemies) {

		for (int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			// check scratch
			if (this.scratching) {
				if (this.facingRight) {
					if (e.getX() > this.x
							&& e.getX() < this.x + this.scratchRange
							&& e.getY() > this.y - this.height / 2
							&& e.getY() < this.y + this.height / 2) {
						e.hit(this.scratchDamage);
					}
				}
			} else {
				if (e.getX() < this.x && e.getX() > this.x - this.scratchRange
						&& e.getY() > this.y - this.height / 2
						&& e.getY() < this.y + this.height / 2) {
					e.hit(this.scratchDamage);
				}
			}
			// fireballs
			for (int j = 0; j < this.fireBalls.size(); j++) {
				if (this.fireBalls.get(j).intersects(e)) {
					e.hit(this.fireBallDamage);
					this.fireBalls.get(j).setHit();
				}
			}

			if (intersects(e)) {
				hit(e.getDamage());
			}
		}

	}

	public void hit(int damage) {
		if (this.dead || this.flinching) {
			return;
		}
		this.health -= damage;
		if (this.health < 0) {
			this.health = 0;
		}
		if (this.health == 0) {
			this.dead = true;
		}

		this.flinching = true;
		this.flinchingTime = System.nanoTime();
	}

}
