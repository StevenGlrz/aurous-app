package com.codeusa.poptart.notifiers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;

/**
 * @author Andrew
 *
 */
public class NowPlayingNotification extends JWindow {
	/**
	 *
	 */
	private static final long serialVersionUID = 491074731263378512L;
	private final int WIDTH = 250;
	private final int HEIGHT = 65;
	private final int HEIGHT_ADD = 30;

	public NowPlayingNotification(final String title, final String artist,
			final String artURL) throws MalformedURLException {
		getContentPane().setBackground(Color.DARK_GRAY);
		getContentPane().setLayout(null);

		final JLabel mediaArtLabel = new JLabel("");
		final URL where = new URL(artURL);
		final ImageIcon anotherIcon = new ImageIcon(where);
		mediaArtLabel.setIcon(new ImageIcon(((anotherIcon.getImage())
				.getScaledInstance(84, 65, Image.SCALE_SMOOTH))));

		mediaArtLabel.setBounds(0, 0, 84, 65);
		getContentPane().add(mediaArtLabel);

		final JLabel nowPlayingLabel = new JLabel("Now Playing:");
		nowPlayingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		nowPlayingLabel.setForeground(Color.WHITE);
		nowPlayingLabel.setHorizontalAlignment(SwingConstants.CENTER);
		nowPlayingLabel.setBounds(94, 0, 156, 14);
		getContentPane().add(nowPlayingLabel);

		final JLabel songLabel = new JLabel(title);
		songLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
		songLabel.setForeground(Color.LIGHT_GRAY);
		songLabel.setHorizontalAlignment(SwingConstants.LEFT);
		songLabel.setBounds(94, 14, 156, 29);
		getContentPane().add(songLabel);

		final JLabel artistLabel = new JLabel(artist);
		artistLabel.setHorizontalAlignment(SwingConstants.LEFT);
		artistLabel.setForeground(Color.LIGHT_GRAY);
		artistLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 11));
		artistLabel.setBounds(94, 42, 156, 23);
		getContentPane().add(artistLabel);
		setSize(250, 65);
		// positionWindow();
		setAlwaysOnTop(true);
		setVisible(true);
	}

	public void displayAlert() throws InterruptedException {

		final Toolkit aToolkit = Toolkit.getDefaultToolkit();
		final Dimension screen = aToolkit.getScreenSize();
		final int xPosition = screen.width - (this.WIDTH); // Right edge of the
		// screen
		for (int i = 0; i < this.HEIGHT_ADD; i++) {

			final int yPosition = screen.height - (this.HEIGHT + i); // Bottom
			// edge
			// of
			// the
			// screen
			setBounds(xPosition, yPosition, this.WIDTH, this.HEIGHT);
			Thread.sleep(10);

		}
		Thread.sleep(3000);
		removeAlert();

	}

	// Place the window in the bottom right corner of the screen
	private void removeAlert() throws InterruptedException {
		final Toolkit aToolkit = Toolkit.getDefaultToolkit();
		final Dimension screen = aToolkit.getScreenSize();
		final int xPosition = screen.width - (this.WIDTH); // Right edge of the
		// screen
		for (int i = this.HEIGHT + this.HEIGHT_ADD; i > 0; i--) {

			final int yPosition = screen.height - (i); // Bottom edge of the
			// screen
			setBounds(xPosition, yPosition, this.WIDTH, this.HEIGHT);
			Thread.sleep(15);

		}

		setVisible(false); // you can't see me!
		dispose(); // Destroy the JFrame object

	}

}