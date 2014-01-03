package gamestate;

import java.awt.Graphics2D;

import api.Drawable;

public class GameStateManager implements Drawable {

	public enum State {
		MENUSTATE, LEVEL1STATE
	}

	private GameState[] gameStates;
	private State currentState;

	private static int NUMGAMESTATES = 2;

	public GameStateManager() {
		this.gameStates = new GameState[NUMGAMESTATES];
		this.currentState = State.MENUSTATE;
		loadState(this.currentState);
	}

	private void loadState(State current) {
		switch (current) {
		case MENUSTATE:
			this.gameStates[current.ordinal()] = (new MenuState(this));
			break;
		case LEVEL1STATE:
			this.gameStates[current.ordinal()] = (new Level1State(this));
			break;
		default:
			break;
		}
	}

	private void unloadState(State current) {
		this.gameStates[current.ordinal()] = null;
	}

	public void setState(State state) {
		unloadState(this.currentState);
		this.currentState = state;
		loadState(this.currentState);
		// this.gameStates[this.currentState.ordinal()].init();
	}

	@Override
	public boolean update() {
		if (this.gameStates[this.currentState.ordinal()] != null) {
			this.gameStates[this.currentState.ordinal()].update();
			return true;
		}
		return false;
	}

	@Override
	public void draw(Graphics2D graphics) {
		if (this.gameStates[this.currentState.ordinal()] != null) {
			this.gameStates[this.currentState.ordinal()].draw(graphics);
		}
	}

	public void keyPressed(int k) {
		this.gameStates[this.currentState.ordinal()].keyPressed(k);
	}

	public void keyReleased(int k) {
		this.gameStates[this.currentState.ordinal()].keyReleased(k);
	}

}
