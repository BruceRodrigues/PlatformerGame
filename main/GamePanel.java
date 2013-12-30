package main;

import gamestate.GameStateManager;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable, KeyListener {

	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 320;
	public static final int HEIGHT = 240;
	public static final int SCALE = 2;

	private Thread gameThread;
	private boolean running;
	private int FPS = 60;
	private long targetTime = 1000 / this.FPS;

	private BufferedImage image;
	private Graphics2D graphics;

	private GameStateManager manager;

	public GamePanel() {
		super();
		this.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		this.setFocusable(true);
		this.setVisible(true);
		this.requestFocus();

	}

	@Override
	public void addNotify() {
		super.addNotify();
		if (this.gameThread == null) {
			this.gameThread = new Thread(this);
			addKeyListener(this);
			this.gameThread.start();
		}
	}

	private void init() {
		this.manager = new GameStateManager();
		this.image = new BufferedImage(WIDTH, HEIGHT,
				BufferedImage.TYPE_INT_RGB);
		this.graphics = (Graphics2D) this.image.getGraphics();
		this.running = true;
	}

	@Override
	public void run() {
		init();

		long start, elapsed, wait;

		while (this.running) {

			start = System.nanoTime();

			update();
			draw();
			drawToScreen();

			elapsed = System.nanoTime() - start;
			wait = this.targetTime - elapsed / 1000000;

			if (wait < 0) {
				wait = 0;
			}
			try {
				Thread.sleep(wait);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void update() {
		this.manager.update();
	}

	private void draw() {
		this.manager.draw(this.graphics);
	}

	private void drawToScreen() {
		Graphics g = getGraphics();
		g.drawImage(this.image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		g.dispose();
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		this.manager.keyPressed(e.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		this.manager.keyReleased(e.getKeyCode());
	}

}
