package gamestate;

import java.awt.Graphics2D;

public abstract class GameState {

	protected GameStateManager manager;

	public GameState(GameStateManager gsm) {
		this.manager = gsm;
		this.init();
	}

	public abstract void init();

	public abstract void update();

	public abstract void draw(Graphics2D graphics);

	public abstract void keyPressed(int k);

	public abstract void keyReleased(int k);

}
