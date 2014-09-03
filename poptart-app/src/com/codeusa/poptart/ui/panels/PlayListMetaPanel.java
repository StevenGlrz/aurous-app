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
public class PlayListMetaPanel extends JPanel {
	public PlayListMetaPanel() {
		setLayout(null);

		final JLabel lblNewLabel = new JLabel(
				"This panel will come when the new online services is live!");
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);

		lblNewLabel.setBounds(0, 0, 591, 102);
		add(lblNewLabel);
	}

	/**
	 *
	 */
	private static final long serialVersionUID = -3655942417315079292L;
}
