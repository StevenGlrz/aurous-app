package com.codeusa.poptart.ui.panels;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * @author Andrew
 *
 */
public class SongMetaPanel extends JPanel {
	public static JLabel songTitleLabel;
	public static JLabel songArtistLabel;

	public SongMetaPanel() {
		setBackground(new Color(27, 25, 32));
		setLayout(null);

		songTitleLabel = new JLabel("");
		songTitleLabel.setHorizontalAlignment(SwingConstants.LEFT);
		songTitleLabel.setForeground(Color.WHITE);
		songTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
		songTitleLabel.setBounds(0, 0, 200, 32);
		add(songTitleLabel);

		songArtistLabel = new JLabel("");
		songArtistLabel.setHorizontalAlignment(SwingConstants.LEFT);
		songArtistLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		songArtistLabel.setForeground(Color.WHITE);
		songArtistLabel.setBounds(0, 37, 200, 21);
		add(songArtistLabel);
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 8264025748800505559L;
}
