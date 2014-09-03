package com.codeusa.poptart.ui.panels;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Insets;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import com.codeusa.poptart.tools.DiscoMixer;
import com.codeusa.poptart.ui.frames.AboutFrame;
import com.codeusa.poptart.ui.frames.SettingsFrame;

/**
 * @author Andrew
 *
 */
public class FunctionsPanel extends JPanel {
	/**
	 *
	 */
	private static final long serialVersionUID = -4108786754024537789L;

	public FunctionsPanel() throws IOException {
		setLayout(null);
		final Image playListButtonIcon = ImageIO.read(this.getClass()
				.getResource("/resources/btaddplaylist.png"));

		final Image playListButtonHover = ImageIO.read(this.getClass()
				.getResource("/resources/btaddplaylist_h.png"));

		final Image downLoadButtonIcon = ImageIO.read(this.getClass()
				.getResource("/resources/saveAs.png"));

		final Image downLoadButtonHover = ImageIO.read(this.getClass()
				.getResource("/resources/saveAs.png"));

		final Image settingsButtonIcon = ImageIO.read(this.getClass()
				.getResource("/resources/systemSettings.png"));

		final Image settingsButtonHover = ImageIO.read(this.getClass()
				.getResource("/resources/systemSettings.png"));

		final Image helpButtonIcon = ImageIO.read(this.getClass().getResource(
				"/resources/help.png"));

		final Image helpButtonHover = ImageIO.read(this.getClass().getResource(
				"/resources/help.png"));

		final Image searchButtonIcon = ImageIO.read(this.getClass()
				.getResource("/resources/magnifier.png"));

		final Image searchButtonHover = ImageIO.read(this.getClass()
				.getResource("/resources/magnifier.png"));

		final JButton downloadMediaButton = new JButton("Download");
		downloadMediaButton.setForeground(Color.WHITE);
		downloadMediaButton.setBounds(10, 11, 97, 21);
		downloadMediaButton.setCursor(Cursor
				.getPredefinedCursor(Cursor.HAND_CURSOR));
		downloadMediaButton.setBorderPainted(false);
		downloadMediaButton.setBorder(null);
		downloadMediaButton.setMargin(new Insets(0, 0, 0, 0));
		downloadMediaButton.setContentAreaFilled(false);
		downloadMediaButton.setIcon(new ImageIcon(downLoadButtonIcon));
		downloadMediaButton.setRolloverIcon(new ImageIcon(downLoadButtonHover));
		add(downloadMediaButton);

		final JButton searchButton = new JButton("Disco Search");
		searchButton.addActionListener(e -> DiscoMixer.openDisco());
		searchButton.setBounds(117, 11, 97, 21);
		searchButton.setForeground(Color.WHITE);

		searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		searchButton.setBorderPainted(false);
		searchButton.setBorder(null);
		searchButton.setMargin(new Insets(0, 0, 0, 0));
		searchButton.setContentAreaFilled(false);
		searchButton.setIcon(new ImageIcon(searchButtonIcon));
		searchButton.setRolloverIcon(new ImageIcon(searchButtonHover));
		add(searchButton);

		final JButton settingsButton = new JButton("Settings");
		settingsButton.addActionListener(e -> SettingsFrame.openSettings());
		settingsButton.setBounds(666, 11, 97, 21);
		settingsButton.setForeground(Color.WHITE);

		settingsButton
		.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		settingsButton.setBorderPainted(false);
		settingsButton.setBorder(null);
		settingsButton.setMargin(new Insets(0, 0, 0, 0));
		settingsButton.setContentAreaFilled(false);
		settingsButton.setIcon(new ImageIcon(settingsButtonIcon));
		settingsButton.setRolloverIcon(new ImageIcon(settingsButtonHover));
		add(settingsButton);

		final JButton helpButton = new JButton("Help");
		helpButton.addActionListener(e -> AboutFrame.showAbout());
		helpButton.setBounds(577, 11, 97, 21);
		helpButton.setForeground(Color.WHITE);

		helpButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		helpButton.setBorderPainted(false);
		helpButton.setBorder(null);
		helpButton.setMargin(new Insets(0, 0, 0, 0));
		helpButton.setContentAreaFilled(false);
		helpButton.setIcon(new ImageIcon(helpButtonIcon));
		helpButton.setRolloverIcon(new ImageIcon(helpButtonHover));
		add(helpButton);

		final JSeparator miscSeparator = new JSeparator();
		miscSeparator.setOrientation(SwingConstants.VERTICAL);
		miscSeparator.setBounds(587, 0, 2, 50);
		add(miscSeparator);
	}

}
