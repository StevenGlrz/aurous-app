package me.aurous.grabbers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import me.aurous.player.Settings;
import me.aurous.utils.Internet;
import me.aurous.utils.media.MediaUtils;

import org.apache.commons.lang3.StringEscapeUtils;

import com.sun.javafx.Utils;

public class SoundCloudGrabber {

	private static String TITLE_PATTERN = "\"title\":\"";
	private static String ALBUM_ART_PATTERN = "\"og:title\" /><meta content=\"";
	private static String USERNAME_PATTERN = "{\"username\":\"";
	private static String DURATION_PATTERN = ",\"duration\":";
	private static String STREAM_PATTERN = "\"streamUrl\":\"";
	private static String REDIRECTED_PATTERN = "redirected</a>.&lt;/body&gt;&lt;/html&gt;";
	private static String STREAM_DOMAIN = "media.soundcloud";

	/**
	 * @author Andrew Returns the title of the Soundcloud url provided
	 *
	 * @return title
	 */
	private static String getSongTitle(final String HTML) {
		String title = "";

		title = MediaUtils.getBetween(HTML, TITLE_PATTERN, "\",\"");
		title = StringEscapeUtils.escapeHtml4(title.replaceAll(
				"[^\\x20-\\x7e]", ""));
		title = Utils.convertUnicode(title);
		title = StringEscapeUtils.unescapeHtml4(title);

		if (title.contains(",")) {
			title = escapeComma(title);
		}
		return title.trim();
	}

	private static String escapeComma(final String str) {
		return str.replace(",", "\\,");
	}

	/**
	 * @author Andrew
	 *
	 *         Formats all needed variables into a CSV which will be added to
	 *         the selected playList Album name is missing so we will leave it
	 *         blank, need to find a suitable api
	 * @return
	 *
	 */
	public static String buildPlayListLine(String soundCloudURL) {
		soundCloudURL = soundCloudURL.replace("https://", "http://");

		if (!soundCloudURL.startsWith("http")) {
			return "";
		}
		if (soundCloudURL.contains("/sets/")) { // need a lot of regex for
			// playlist importing so lets
			// hold off
			return "";
		}

		final String HTML = Internet.text(soundCloudURL);

		if (HTML.isEmpty()) {

			return "";
		}
		final String streamURL = (getStreamURL(HTML));
		final String songTitle = (getSongTitle(HTML));
		final String coverArt = getCoverArt(HTML);
		final String artist = getArtist(HTML);
		final String duration = getDuration(HTML);
		final String date = getDate();
		final String user = Settings.getUserName();
		final String line = String.format("%s, %s, %s, %s, %s, %s, %s, %s",
				songTitle, artist, duration, date, user, "", coverArt,
				streamURL);

		return line;
	}

	/**
	 * @author Andrew
	 *
	 *         Gets the stream URL from the HTML, doesn't need to be scraped
	 *         every time since the stream url has been constant for over 2
	 *         years
	 * @return streamURL
	 *
	 */
	public static String getStreamURL(final String HTML) {

		final String streamURL = MediaUtils.getBetween(HTML, STREAM_PATTERN,
				"\",");

		return streamURL.trim();

	}

	/**
	 * @author Andrew
	 *
	 *         Return a previous stream if it matches the STREAM_DOMAIN pattern,
	 *         else get it again
	 * @return streamURL
	 *
	 */
	public static String getCachedURL(String url) {

		if (url.contains(STREAM_DOMAIN)) {
			return url.trim();
		}

		url = url.replace("https://", "http://");
		String html = Internet.text(url);

		if (html.contains(REDIRECTED_PATTERN)) { // just in case
			url = MediaUtils
					.getBetween(html, "<a href=\"", "\">redirected</a>");
			html = Internet.text(url);
		}

		final String streamURL = MediaUtils.getBetween(html, STREAM_PATTERN,
				"\",");

		return streamURL.trim();

	}

	/**
	 * @author Andrew
	 *
	 *         Extracts the artist from HTML
	 *
	 */
	public static String getArtist(final String HTML) {
		String artist = "";
		artist = MediaUtils.getBetween(HTML, USERNAME_PATTERN, "\",\"");
		return artist;
	}

	/**
	 * @author Andrew
	 *
	 *         Calculates a Spotify-like shorthand date
	 *
	 */
	public static String getDate() {
		final Date now = new Date();

		final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM d",
				Locale.US);
		final String asWeek = dateFormat.format(now);
		return asWeek;
	}

	/**
	 * @author Andrew
	 *
	 *         Gets the total milli seconds a Soundcloud stream is and converts
	 *         it
	 *
	 */
	public static String getDuration(final String HTML) {
		final long duration = Integer.parseInt(MediaUtils.getBetween(HTML,
				DURATION_PATTERN, ",\""));
		final double seconds = duration / 1000.0;
		final int total_duration_seconds = (int) seconds;

		return MediaUtils.calculateTime(total_duration_seconds).trim();
	}

	/**
	 * @author Andrew
	 *
	 *         Fetches the albumArt URL from scraping the html
	 *
	 * @return covertArt
	 */
	private static String getCoverArt(final String HTML) {
		String coverArt = "";
		coverArt = MediaUtils.getBetween(HTML, ALBUM_ART_PATTERN,
				"\" property=\"og:i");

		return coverArt.trim();
	}

}