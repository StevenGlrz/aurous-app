package me.aurous.ui.panels;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

import me.aurous.player.Settings;
import me.aurous.player.functions.PlayerFunctions;
import me.aurous.ui.UISession;
import me.aurous.utils.media.MediaUtils;

/**
 * @author Andrew
 *
 */
public class ControlPanel extends JPanel {

	private final Color background = new Color(32, 33, 35);
	private static JSlider volume;
	private static JLabel repeatStatusLabel;
	private static JLabel shuffleStatusLabel;
	private static JButton play;
	private static DurationPanel durationPanel;

	public ControlPanel() throws IOException {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setBackground(background);
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

		final JButton previous = new JButton("");
		previous.addActionListener(e -> PlayerFunctions.seekPrevious());
		previous.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		previous.setBorderPainted(false);
		previous.setBorder(null);
		previous.setMargin(new Insets(0, 0, 0, 0));
		previous.setContentAreaFilled(false);
		previous.setIcon(new ImageIcon(previousButtonIcon));
		previous.setRolloverIcon(new ImageIcon(previousRollOver));

		play = new JButton("");
		play.addActionListener(e -> {
			final boolean isPaused = PlayerFunctions.isPaused;
			if (!isPaused) {
				PlayerFunctions.pause(play);
			} else {
				PlayerFunctions.play(play);
			}
		});
		play.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		play.setBorderPainted(false);
		play.setBorder(null);
		play.setMargin(new Insets(0, 0, 0, 0));
		play.setContentAreaFilled(false);

		play.setIcon(new ImageIcon(playBackPaused));
		play.setRolloverIcon(new ImageIcon(playBackPausedHover));

		final JButton nextButton = new JButton("");
		nextButton.addActionListener(e -> PlayerFunctions.seekNext());
		nextButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		nextButton.setBorderPainted(false);
		nextButton.setBorder(null);
		nextButton.setMargin(new Insets(0, 0, 0, 0));
		nextButton.setContentAreaFilled(false);

		nextButton.setIcon(new ImageIcon(forwardButtonIcon));
		nextButton.setRolloverIcon(new ImageIcon(forwardButtonHover));
		nextButton.setToolTipText("Next");

		final JButton mute = new JButton("");
		mute.addActionListener(e -> MediaUtils.muteToggle());
		mute.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		mute.setBorderPainted(false);
		mute.setBorder(null);
		mute.setMargin(new Insets(0, 0, 0, 0));
		mute.setContentAreaFilled(false);

		mute.setIcon(new ImageIcon(soundButtonIcon));
		mute.setRolloverIcon(new ImageIcon(soundButtonHover));
		;

		volume = new JSlider();
		volume.setPreferredSize(new Dimension(100, 25));
		volume.setMaximumSize(new Dimension(100, 25));
		volume.addChangeListener(e -> {
			if (UISession.getMediaPlayer() != null) {
				UISession.getMediaPlayer().setVolume(
						((double) volume.getValue() / 100));
				Settings.setVolume(volume.getValue());
			}
		});

		add(Box.createRigidArea(new Dimension(10, 37)));
		add(previous);
		add(Box.createRigidArea(new Dimension(5, 0)));
		add(play);
		add(Box.createRigidArea(new Dimension(5, 0)));
		add(nextButton);
		add(Box.createRigidArea(new Dimension(10, 0)));
		add(mute);
		add(Box.createRigidArea(new Dimension(5, 0)));
		add(volume);
		add(Box.createRigidArea(new Dimension(5, 0)));

		final JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
		final Dimension size = new Dimension(
				separator.getPreferredSize().width,
				separator.getMaximumSize().height);

		separator.setMaximumSize(size);
		add(separator);
		add(Box.createRigidArea(new Dimension(10, 0)));

		durationPanel = new DurationPanel();
		add(durationPanel);

		final JSeparator separator1 = new JSeparator(SwingConstants.VERTICAL);
		final Dimension size1 = new Dimension(
				separator1.getPreferredSize().width,
				separator1.getMaximumSize().height);
		separator1.setMaximumSize(size1);
		add(separator1);
		add(Box.createRigidArea(new Dimension(10, 0)));

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

		add(shuffleStatusLabel);
		add(Box.createRigidArea(new Dimension(5, 0)));
		add(repeatStatusLabel);
		add(Box.createRigidArea(new Dimension(5, 0)));

	}

	public static JLabel current() {
		return durationPanel.current();
	}

	public static JLabel total() {
		return durationPanel.total();
	}

	public static JSlider seek() {
		return durationPanel.seek();
	}

	public static JSlider volume() {
		return volume;
	}

	public static JLabel repeat() {
		return repeatStatusLabel;
	}

	public static JLabel shuffle() {
		return shuffleStatusLabel;
	}

	public static JButton play() {
		return play;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 2823722567425016521L;
}
