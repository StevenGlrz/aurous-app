package com.codeusa.poptart.ui.panels;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * @author Andrew
 *
 */
public class AlbumArtPanel extends JPanel {

	public static JLabel albumArtLabel;
	/**
	 *
	 */
	private static final long serialVersionUID = 1017435181777263829L;

	public AlbumArtPanel() {
		setLayout(null);

		albumArtLabel = new JLabel("ALBUM ART");
		albumArtLabel.setIcon(new ImageIcon(AlbumArtPanel.class
				.getResource("/resources/album-placeholder.png")));
		albumArtLabel.setHorizontalAlignment(SwingConstants.CENTER);
		albumArtLabel.setBounds(0, 0, 200, 181);
		add(albumArtLabel);

	}
}
