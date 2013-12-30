package gamestate;

import gamestate.GameStateManager.State;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import tilemap.Background;

public class MenuState extends GameState {

	private enum Option {
		START("Start"), HELP("Help"), QUIT("Quit");

		String name;

		Option(String s) {
			this.name = s;
		}
	}

	private Option currentChoice = Option.START;

	private Color titleColor;
	private Font titleFont;
	private Font font;

	private Background background;

	public MenuState(GameStateManager manager) {
		super(manager);

		this.background = new Background("/Backgrounds/menubg.gif", 1);
		this.background.setVector(-0.1, 0);
		this.titleColor = new Color(0, 0, 125);
		this.titleFont = new Font("Century Gothic", Font.PLAIN, 28);
		this.font = new Font("Arial", Font.PLAIN, 12);
	}

	@Override
	public void init() {

	}

	@Override
	public void update() {
		this.background.update();
	}

	@Override
	public void draw(Graphics2D graphics) {
		this.background.draw(graphics);

		graphics.setColor(this.titleColor);
		graphics.setFont(this.titleFont);
		graphics.drawString("Platformer Game", 80, 70);

		graphics.setFont(this.font);
		for (Option option : Option.values()) {
			if (option == this.currentChoice) {
				graphics.setColor(new Color(100, 0, 255));
			} else {
				graphics.setColor(new Color(0, 0, 0));
			}
			graphics.drawString(option.name, 145, 140 + option.ordinal() * 15);
		}

	}

	private void selectMenu() {
		switch (this.currentChoice) {
		case START:
			this.manager.setState(State.LEVEL1STATE);
			break;
		case HELP:
			break;
		default:
			System.exit(0);
			break;
		}
	}

	@Override
	public void keyPressed(int k) {
		switch (k) {
		case KeyEvent.VK_ENTER:
			this.selectMenu();
			break;
		case KeyEvent.VK_UP:
			if (this.currentChoice != Option.START) {
				this.currentChoice = Option.values()[this.currentChoice
						.ordinal() - 1];
			}
			break;
		case KeyEvent.VK_DOWN:
			if (this.currentChoice != Option.QUIT) {
				this.currentChoice = Option.values()[this.currentChoice
						.ordinal() + 1];
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void keyReleased(int k) {
	}

}
