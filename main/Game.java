package main;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

/*
 * This game was made folling the ForeignGuyMike tutorial.
 * http://www.youtube.com/user/ForeignGuyMike
 */

public class Game {

	public static void main(String args[]) {
		JFrame window = new JFrame("Platformer Game");
		window.setContentPane(new GamePanel());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.pack();
		window.setVisible(true);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		window.setLocation(dim.width / 2 - window.getSize().width / 2,
				dim.height / 2 - window.getSize().height / 2);
	}

}
