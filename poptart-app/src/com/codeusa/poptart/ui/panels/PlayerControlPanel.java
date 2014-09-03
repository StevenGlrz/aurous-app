package com.codeusa.poptart.ui.panels;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicSliderUI;

import com.codeusa.poptart.player.Settings;
import com.codeusa.poptart.player.functions.PlayerFunctions;
import com.codeusa.poptart.player.scenes.MediaPlayerScene;
import com.codeusa.poptart.utils.media.MediaUtils;

/**
 * @author Andrew
 *
 */
public class PlayerControlPanel extends JPanel {

	public static JSlider durationSlider;
	public static JSlider volumeSlider;
	public static JLabel repeatStatusLabel;
	public static JLabel currentTimeLabel;
	public static JLabel shuffleStatusLabel;
	public static JButton mediaStateButton;
	public static JLabel totalTimeLabel;

	public PlayerControlPanel() throws IOException {
		setBackground(new Color(32, 33, 35));
		final Image previousButtonIcon = ImageIO.read(this.getClass()
				.getResource("/resources/btprev.png"));
		final Image previousRollOver = ImageIO.read(this.getClass()
				.getResource("/resources/btprev_h.png"));

		ImageIO.read(this.getClass().getResource("/resources/btplay2.png"));
		ImageIO.read(this.getClass().getResource("/resources/btplay2_h.png"));

		final Image playBackPaused = ImageIO.read(this.getClass().getResource(
				"/resources/btpause2.png"));
		final Image playBackPausedHover = ImageIO.read(this.getClass()
				.getResource("/resources/btpause2_h.png"));

		final Image forwardButtonIcon = ImageIO.read(this.getClass()
				.getResource("/resources/btnext.png"));
		final Image forwardButtonHover = ImageIO.read(this.getClass()
				.getResource("/resources/btnext_h.png"));

		final Image soundButtonIcon = ImageIO.read(this.getClass().getResource(
				"/resources/btvolume.png"));
		final Image soundButtonHover = ImageIO.read(this.getClass()
				.getResource("/resources/btvolume_h.png"));

		setLayout(null);

		final JButton previousButton = new JButton("");
		previousButton.addActionListener(e -> PlayerFunctions.seekPrevious());
		previousButton.setBounds(10, 0, 63, 50);
		previousButton
				.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		previousButton.setBorderPainted(false);
		previousButton.setBorder(null);
		previousButton.setMargin(new Insets(0, 0, 0, 0));
		previousButton.setContentAreaFilled(false);
		previousButton.setIcon(new ImageIcon(previousButtonIcon));
		previousButton.setRolloverIcon(new ImageIcon(previousRollOver));
		add(previousButton);

		mediaStateButton = new JButton("");
		mediaStateButton.addActionListener(e -> {
			final boolean isPaused = PlayerFunctions.isPaused;
			if (!isPaused) {
				PlayerFunctions.pause(mediaStateButton);
			} else {
				PlayerFunctions.play(mediaStateButton);
			}
		});
		mediaStateButton.setBounds(63, 0, 63, 50);
		mediaStateButton.setCursor(Cursor
				.getPredefinedCursor(Cursor.HAND_CURSOR));
		mediaStateButton.setBorderPainted(false);
		mediaStateButton.setBorder(null);
		mediaStateButton.setMargin(new Insets(0, 0, 0, 0));
		mediaStateButton.setContentAreaFilled(false);

		mediaStateButton.setIcon(new ImageIcon(playBackPaused));
		mediaStateButton.setRolloverIcon(new ImageIcon(playBackPausedHover));
		add(mediaStateButton);

		final JButton nextButton = new JButton("");
		nextButton.addActionListener(e -> PlayerFunctions.seekNext());
		nextButton.setBounds(118, 0, 63, 50);
		nextButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		nextButton.setBorderPainted(false);
		nextButton.setBorder(null);
		nextButton.setMargin(new Insets(0, 0, 0, 0));
		nextButton.setContentAreaFilled(false);

		nextButton.setIcon(new ImageIcon(forwardButtonIcon));
		nextButton.setRolloverIcon(new ImageIcon(forwardButtonHover));
		nextButton.setToolTipText("Next");
		add(nextButton);

		volumeSlider = new JSlider();
		volumeSlider.addChangeListener(e -> {
			if (MediaPlayerScene.player != null) {
				MediaPlayerScene.player.setVolume(((double) volumeSlider
						.getValue() / 100));
				Settings.setVolume(volumeSlider.getValue());
			}
		});
		volumeSlider.setBackground(Color.DARK_GRAY);
		volumeSlider.setBounds(226, 15, 85, 23);

		add(volumeSlider);

		currentTimeLabel = new JLabel("0:00");
		currentTimeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		currentTimeLabel.setForeground(Color.WHITE);
		currentTimeLabel.setBounds(312, 19, 50, 14);
		add(currentTimeLabel);

		durationSlider = new JSlider(/*
		 * your options here if desired
		 */) {
			/**
			 *
			 */
			private static final long serialVersionUID = -4931644654633925931L;

			{
				final MouseListener[] listeners = getMouseListeners();
				for (final MouseListener l : listeners) {
					removeMouseListener(l); // remove UI-installed TrackListener
				}
				final BasicSliderUI ui = (BasicSliderUI) getUI();
				final BasicSliderUI.TrackListener tl = ui.new TrackListener() {
					// this is where we jump to absolute value of click
					@Override
					public void mouseClicked(final MouseEvent e) {
						final Point p = e.getPoint();
						final int value = ui.valueForXPosition(p.x);

						setValue(value);
						System.out.println(value);
						PlayerFunctions.seek(value);
					}

					// disable check that will invoke scrollDueToClickInTrack
					@Override
					public boolean shouldScroll(final int dir) {
						return false;
					}
				};
				addMouseListener(tl);
			}
		};
		durationSlider.setBackground(Color.DARK_GRAY);
		durationSlider.setBounds(362, 13, 281, 26);

		add(durationSlider);

		totalTimeLabel = new JLabel("0:00");
		totalTimeLabel.setHorizontalAlignment(SwingConstants.LEFT);
		totalTimeLabel.setForeground(Color.WHITE);
		totalTimeLabel.setBounds(643, 19, 50, 14);
		add(totalTimeLabel);

		final JButton soundToggleButton = new JButton("");
		soundToggleButton.addActionListener(e -> MediaUtils.muteToggle());
		soundToggleButton.setBounds(180, 0, 46, 50);
		soundToggleButton.setCursor(Cursor
				.getPredefinedCursor(Cursor.HAND_CURSOR));
		soundToggleButton.setBorderPainted(false);
		soundToggleButton.setBorder(null);
		soundToggleButton.setMargin(new Insets(0, 0, 0, 0));
		soundToggleButton.setContentAreaFilled(false);

		soundToggleButton.setIcon(new ImageIcon(soundButtonIcon));
		soundToggleButton.setRolloverIcon(new ImageIcon(soundButtonHover));
		add(soundToggleButton);

		final JSeparator leadingProgressSeperator = new JSeparator();
		leadingProgressSeperator.setOrientation(SwingConstants.VERTICAL);
		leadingProgressSeperator.setBounds(315, 0, 1, 50);
		add(leadingProgressSeperator);

		final JSeparator trailingProgressSeperator = new JSeparator();
		trailingProgressSeperator.setOrientation(SwingConstants.VERTICAL);
		trailingProgressSeperator.setBounds(692, 0, 1, 50);
		add(trailingProgressSeperator);

		shuffleStatusLabel = new JLabel("Shuffle : Off");
		shuffleStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);
		shuffleStatusLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(final MouseEvent arg0) {
				if (shuffleStatusLabel.isEnabled()) {
					PlayerFunctions.handleSpecialLabels(false);
				}
			}
		});
		shuffleStatusLabel.setCursor(Cursor
				.getPredefinedCursor(Cursor.HAND_CURSOR));
		shuffleStatusLabel.setForeground(Color.WHITE);
		shuffleStatusLabel.setBounds(703, 4, 78, 14);
		add(shuffleStatusLabel);

		repeatStatusLabel = new JLabel("Repeat : Off");
		repeatStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);
		repeatStatusLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(final MouseEvent arg0) {
				if (repeatStatusLabel.isEnabled()) {
					PlayerFunctions.handleSpecialLabels(true);
				}

			}
		});
		repeatStatusLabel.setCursor(Cursor
				.getPredefinedCursor(Cursor.HAND_CURSOR));
		repeatStatusLabel.setForeground(Color.WHITE);
		repeatStatusLabel.setBounds(703, 29, 78, 14);
		add(repeatStatusLabel);
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 2823722567425016521L;
}
