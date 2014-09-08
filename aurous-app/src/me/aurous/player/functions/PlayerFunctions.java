package me.aurous.player.functions;

import java.awt.Image;
import java.io.IOException;
import java.util.Random;

import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;

import me.aurous.ui.UISession;
import me.aurous.ui.panels.ControlPanel;
import me.aurous.ui.panels.TabelPanel;
import me.aurous.utils.media.MediaUtils;

/**
 * @author Andrew
 *
 */
public class PlayerFunctions {

	public boolean switching = false;
	public static boolean isPaused;
	public static boolean repeat = false;
	public static boolean shuffle = false;

	public static void seekNext() {
		final JTable table = TabelPanel.table;
		if (table != null) {
			final int total = table.getRowCount();
			final int idx = table.getSelectedRow();
			if (total == 0) {
				return;
			} else if ((idx == -1) && (total == 0)) {
				return;
			} else if ((idx == -1) && (total > 0)) {
				table.setRowSelectionInterval(0, 0);
				MediaUtils.switchMedia(table);

			} else if ((idx + 1) == total) {
				table.setRowSelectionInterval(0, 0);
				MediaUtils.switchMedia(table);

			} else {

				try {
					table.setRowSelectionInterval(0, idx + 1);
					MediaUtils.switchMedia(table);
				} catch (final Exception e) {
					table.setRowSelectionInterval(0, 0);
					MediaUtils.switchMedia(table);
				}

			}
		}

	}

	public static void pause(final JButton mediaStateButton) {
		try {
			if (UISession.getMediaPlayer() != null) {
				final Image playBackPaused = ImageIO.read(mediaStateButton
						.getClass().getResource("/resources/btplay2.png"));
				final Image playBackPausedHover = ImageIO.read(mediaStateButton
						.getClass().getResource("/resources/btplay2_h.png"));
				mediaStateButton.setIcon(new ImageIcon(playBackPaused));
				mediaStateButton.setRolloverIcon(new ImageIcon(
						playBackPausedHover));
				UISession.getMediaPlayer().pause();
				isPaused = true;
				mediaStateButton.setToolTipText("Play");
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public static void play(final JButton mediaStateButton) {
		try {
			if (UISession.getMediaPlayer() != null) {
				final Image playBackPaused = ImageIO.read(mediaStateButton
						.getClass().getResource("/resources/btpause2.png"));
				final Image playBackPausedHover = ImageIO.read(mediaStateButton
						.getClass().getResource("/resources/btpause2_h.png"));
				mediaStateButton.setIcon(new ImageIcon(playBackPaused));
				mediaStateButton.setRolloverIcon(new ImageIcon(
						playBackPausedHover));

				UISession.getMediaPlayer().play();
				isPaused = false;
				mediaStateButton.setToolTipText("Pause");
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public static void seekPrevious() {
		final JTable table = TabelPanel.table;
		if (table != null) {
			final int total = table.getRowCount();
			final int idx = table.getSelectedRow();
			if (total == 0) {
				return;
			} else if ((idx == -1) && (total == 0)) {
				return;
			} else if ((idx == -1) && (total > 0)) {
				table.setRowSelectionInterval(0, total - 1);
				MediaUtils.switchMedia(table);

			} else if (idx <= 0) {
				table.setRowSelectionInterval(0, total - 1);
				MediaUtils.switchMedia(table);

			} else {
				try {
					table.setRowSelectionInterval(0, idx - 1);
					MediaUtils.switchMedia(table);
				} catch (final Exception e) {
					table.setRowSelectionInterval(0, 0);
					MediaUtils.switchMedia(table);
				}

			}
		}
	}

	public static void handleSpecialLabels(final boolean isRepeat) {
		final JLabel repeatStatusLabel = ControlPanel.repeat();
		final JLabel shuffleStatusLabel = ControlPanel.shuffle();
		if (isRepeat) {
			if (repeatStatusLabel.isEnabled()) {
				if (repeat == true) {
					repeat = false;
					repeatStatusLabel.setText("Repeat : Off");
					shuffleStatusLabel.setEnabled(true);

				} else {
					repeat = true;
					shuffle = false;
					shuffleStatusLabel.setEnabled(false);
					repeatStatusLabel.setText("Repeat : On");
					shuffleStatusLabel.setText("Shuffle : Off");
				}
			}
		} else {
			if (shuffleStatusLabel.isEnabled()) {
				if (shuffle == true) {
					shuffle = false;
					shuffleStatusLabel.setText("Shuffle : Off");
					repeatStatusLabel.setEnabled(true);

				} else {
					shuffle = true;
					repeat = false;
					repeatStatusLabel.setEnabled(false);
					shuffleStatusLabel.setText("Shuffle : On");
					repeatStatusLabel.setText("Repeat : Off");
				}
			}
		}
	}

	public static void repeat() {
		final JTable table = TabelPanel.table;
		if (table != null) {
			if (table.getRowCount() > 0) {
				final int index = table.getSelectedRow();
				table.setRowSelectionInterval(0, index);
				MediaUtils.switchMedia(table);

			} else {

			}
		}
	}

	public static void seek(final int value) {
		final MediaPlayer player = UISession.getMediaPlayer();
		if (player != null) {
			final double d = value / 100D;
			player.seek(Duration.millis(player.getMedia().getDuration()
					.toMillis()
					* d));
		}
	}

	public static void shuffle() {

		final JTable table = TabelPanel.table;
		final int totalIndexs = table.getRowCount();
		final int randomIndex = new Random().nextInt(totalIndexs);
		table.setRowSelectionInterval(0, randomIndex);
		MediaUtils.switchMedia(table);

	}

}
