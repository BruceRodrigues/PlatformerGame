package entity;

import tilemap.TileMap;
import api.Drawable;

public abstract class Enemy extends MapObject implements Drawable {

	protected int health, maxHealth;
	protected boolean dead;
	protected int damage;

	protected boolean flinching;
	protected long flinchTimer;

	public Enemy(TileMap tm) {
		super(tm);
	}

	public boolean idDead() {
		return this.dead;
	}

	public int getDamage() {
		return this.damage;
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
		this.flinchTimer = System.nanoTime();
	}

}
