package gamestate;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import api.Drawable;

public class GameStateManager implements Drawable {

	public enum State {
		MENUSTATE, LEVEL1STATE
	}

	private List<GameState> gameStates;
	private State currentState;

	public GameStateManager() {
		this.gameStates = new ArrayList<GameState>();
		this.currentState = State.MENUSTATE;
		this.gameStates.add(new MenuState(this));
		this.gameStates.add(new Level1State(this));
	}

	public void setState(State state) {
		this.currentState = state;
		this.gameStates.get(this.currentState.ordinal()).init();
	}

	@Override
	public boolean update() {
		this.gameStates.get(this.currentState.ordinal()).update();
		return true;
	}

	@Override
	public void draw(Graphics2D graphics) {
		this.gameStates.get(this.currentState.ordinal()).draw(graphics);
	}

	public void keyPressed(int k) {
		this.gameStates.get(this.currentState.ordinal()).keyPressed(k);
	}

	public void keyReleased(int k) {
		this.gameStates.get(this.currentState.ordinal()).keyReleased(k);
	}

}
