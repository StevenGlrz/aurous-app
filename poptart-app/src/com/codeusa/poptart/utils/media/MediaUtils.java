package com.codeusa.poptart.utils.media;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JTable;

import com.codeusa.poptart.grabbers.YouTubeGrabber;
import com.codeusa.poptart.notifiers.NowPlayingNotification;
import com.codeusa.poptart.player.Settings;
import com.codeusa.poptart.player.functions.PlayerFunctions;
import com.codeusa.poptart.player.scenes.MediaPlayerScene;
import com.codeusa.poptart.ui.frames.PopTartFrame;
import com.codeusa.poptart.ui.panels.AlbumArtPanel;
import com.codeusa.poptart.ui.panels.PlayListTablePanel;
import com.codeusa.poptart.ui.panels.PlayerControlPanel;
import com.codeusa.poptart.ui.panels.SongMetaPanel;
import com.codeusa.poptart.utils.playlist.YouTubeDataFetcher;

/**
 * @author Andrew
 *
 */
public class MediaUtils {
	private static boolean muted = false;
	public static String activeMedia;
	public static boolean isOutOfFocus;

	/**
	 * @author Andrew
	 *
	 *         handles a given site and returns the stream url
	 */
	public static String getMediaURL(final String sourceURL) {
		if (sourceURL.isEmpty()) {
			return "";
		}
		if (sourceURL.contains("youtube")) {
			return YouTubeGrabber.getYouTubeStream(getHTML(sourceURL));
		} else if (sourceURL.contains("soundcloud")) {
			return "";
		} else {
			return "";
		}
	}

	/**
	 * @author Andrew
	 *
	 *         handles a given site and returns the stream url
	 */
	public static String getBuiltString(final String sourceURL) {
		if (sourceURL.contains("youtube") || sourceURL.contains("youtu.be")) {

			return YouTubeDataFetcher.buildPlayListLine(sourceURL);
		} else if (sourceURL.contains("soundcloud")) {
			return "";
		} else {
			return "";
		}
	}

	public static void muteToggle() {
		if (muted) {
			muted = false;
			PlayerControlPanel.volumeSlider.setValue(Settings.getVolume());
		} else {
			muted = true;
			Settings.setVolume(PlayerControlPanel.volumeSlider.getValue());
			PlayerControlPanel.volumeSlider.setValue(0);

		}

	}

	/**
	 * @author Andrew
	 *
	 *         calls all needed functions for a switching of media
	 */
	public static void switchMedia(final JTable target) {
		switchMediaStream(target);
		switchMediaMeta(target);
		switchMediaCover(target);
		PlayerFunctions.play(PlayerControlPanel.mediaStateButton);

	}

	/**
	 * @author Andrew
	 *
	 *         sets all the meta information for the active song
	 */
	private static void switchMediaMeta(final JTable target) {
		final int row = target.getSelectedRow();

		final String title = target.getValueAt(row, 0).toString().trim();
		final String artist = target.getValueAt(row, 1).toString().trim();
		final String time = (String) target.getValueAt(row, 2);
		final String html1 = "<html><body style='width: ";
		final String html2 = "px'>";
		SongMetaPanel.songTitleLabel.setText(html1 + "150" + html2 + title);
		SongMetaPanel.songArtistLabel.setText(artist);
		PlayerControlPanel.totalTimeLabel.setText(time);
		if (Settings.isSavePlayBack()) {
			try {
				final File parentDir = new File("./data/livestream/");
				parentDir.mkdir();
				Files.write(Paths.get("./data/livestream/artist.txt"),
						artist.getBytes());
				Files.write(Paths.get("./data/livestream/title.txt"),
						title.getBytes());
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * @author Andrew
	 *
	 *         switches the album cover
	 */
	private static void switchMediaCover(final JTable target) {
		try {
			final int row = target.getSelectedRow();

			final String albumArt = (String) target.getValueAt(row, 6);
			final ImageIcon icon = new ImageIcon(new URL(albumArt));

			AlbumArtPanel.albumArtLabel.setIcon(new ImageIcon(
					((icon.getImage()).getScaledInstance(210, 181,
							Image.SCALE_SMOOTH))));
			if (Settings.isSavePlayBack()) {
				try {

					final Image img = icon.getImage();

					final BufferedImage bi = new BufferedImage(
							img.getWidth(null), img.getHeight(null),
							BufferedImage.TYPE_INT_RGB);

					final Graphics2D g2 = bi.createGraphics();
					g2.drawImage(img, 0, 0, null);
					g2.dispose();
					ImageIO.write(bi, "jpg", new File(
							"./data/livestream/art.jpg"));
				} catch (final IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} catch (final MalformedURLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @author Andrew
	 *
	 *         switches the album cover
	 */
	public static void copyMediaURL(final JTable target) {
		final int row = target.getSelectedRow();

		final String link = (String) target.getValueAt(row, 7);
		final StringSelection stringSelection = new StringSelection(link);
		final Clipboard clpbrd = Toolkit.getDefaultToolkit()
				.getSystemClipboard();
		clpbrd.setContents(stringSelection, null);
	}

	/**
	 * @author Andrew
	 *
	 *         Handles when a stream ends
	 */
	public static void handleEndOfStream() {

		final JTable table = PlayListTablePanel.table;
		if (table.getRowCount() == 0) {
			return;
		}
		if ((PlayerFunctions.repeat == false)
				&& (PlayerFunctions.shuffle == false)) {
			PlayerFunctions.seekNext();
		} else if (PlayerFunctions.repeat == true) {
			PlayerFunctions.repeat();
		} else if (PlayerFunctions.shuffle == true) {
			PlayerFunctions.shuffle();

		}

		if (isOutOfFocus && Settings.isDisplayAlert()) {
			final int row = table.getSelectedRow();

			final String albumArt = (String) table.getValueAt(row, 6);
			try {

				final NowPlayingNotification np = new NowPlayingNotification(
						SongMetaPanel.songTitleLabel.getText(),
						SongMetaPanel.songArtistLabel.getText(), albumArt);
				np.displayAlert();
			} catch (final MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (final InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * @author Andrew
	 *
	 *         switches the active stream with the new selected one
	 */
	private static void switchMediaStream(final JTable target) {
		Platform.runLater(() -> {
			try {
				final int row = target.getSelectedRow();
				final String sourceURL = (String) target.getValueAt(row, 7);
				MediaPlayerScene.player.stop();
				PopTartFrame.jfxPanel.setScene(null);
				PopTartFrame.scene = null;
				MediaPlayerScene.player.dispose();

				PopTartFrame.scene = MediaPlayerScene.createScene(sourceURL);
				PopTartFrame.jfxPanel.setScene(PopTartFrame.scene);

				MediaPlayerScene.player
				.setVolume(((double) PlayerControlPanel.volumeSlider
						.getValue() / 100));
			} catch (final Throwable ei) {
				ei.printStackTrace();
			}
		});
	}

	/**
	 * @author Andrew
	 *
	 *         Formats seconds into a readable string Need to change my hard
	 *         code to remove extra 0's and use regex.
	 */
	public static String calculateTime(final int seconds) {
		final int day = (int) TimeUnit.SECONDS.toDays(seconds);
		final long hours = TimeUnit.SECONDS.toHours(seconds)
				- TimeUnit.DAYS.toHours(day);
		final long minute = TimeUnit.SECONDS.toMinutes(seconds)
				- TimeUnit.DAYS.toMinutes(day)
				- TimeUnit.HOURS.toMinutes(hours);
		final long second = TimeUnit.SECONDS.toSeconds(seconds)
				- TimeUnit.DAYS.toSeconds(day)
				- TimeUnit.HOURS.toSeconds(hours)
				- TimeUnit.MINUTES.toSeconds(minute);

		// i must be tired because wtf was i doing
		String duration = String.format("%02d:%02d:%d:%02d", day == 0 ? 5185618
				: day, hours == 0 ? 5185618 : hours, minute, second);
		duration = duration.replace("5185618:", "");
		return duration;
	}

	/**
	 * @author Andrew
	 *
	 *         Gets "html" of remote url
	 */
	public static String getHTML(final String urlToRead) {
		URL url; // The URL to read
		HttpURLConnection conn; // The actual connection to the web page
		BufferedReader rd; // Used to read results from the web page
		String line; // An individual line of the web page HTML
		String result = ""; // A long string containing all the HTML
		try {
			url = new URL(urlToRead);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			rd = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			while ((line = rd.readLine()) != null) {
				result += line;
			}
			rd.close();
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
