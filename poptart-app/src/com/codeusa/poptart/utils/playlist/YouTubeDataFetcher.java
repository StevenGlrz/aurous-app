package com.codeusa.poptart.utils.playlist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.codeusa.poptart.player.Settings;
import com.codeusa.poptart.utils.media.MediaUtils;

/**
 * @author Andrew
 *
 */

public class YouTubeDataFetcher {

	private static String YOUTUBE_REGEX = "^[^v]+v=(.{11}).*";
	private static String API_URL = "http://gdata.youtube.com/feeds/api/videos/";

	/**
	 * @author Andrew
	 *
	 *         gets a YouTube video ID from a url
	 *
	 */
	public static String getYouTubeID(final String url) {
		final String id = "";
		final String expression = "^.*((youtu.be"
				+ "\\/)"
				+ "|(v\\/)|(\\/u\\/w\\/)|(embed\\/)|(watch\\?))\\??v?=?([^#\\&\\?]*).*"; // var
																							// regExp
																							// =
																							// /^.*((youtu.be\/)|(v\/)|(\/u\/\w\/)|(embed\/)|(watch\?))\??v?=?([^#\&\?]*).*/;
		System.out.println(expression);

		final Pattern pattern = Pattern.compile(YOUTUBE_REGEX);
		final Matcher matcher = pattern.matcher(url);

		if (matcher.matches()) {

			return matcher.group(1);
		}

		return id;
	}

	/**
	 * @author Andrew
	 *
	 *         Gets remote JSON via the GDATA Api which should never time out
	 *         since no key is needed
	 *
	 */
	public static String getVideoJSON(final String videoID) {
		URL url; // The URL to read
		HttpURLConnection conn; // The actual connection to the web page
		BufferedReader rd; // Used to read results from the web page
		String line; // An individual line of the web page HTML
		String result = ""; // A long string containing all the HTML
		final String gData = String.format("%s%s%s", API_URL, videoID,
				"?alt=json");
		if (gData.equals("http://gdata.youtube.com/feeds/api/videos/?alt=json")) {
			return "";
		}
		// System.out.println(gData);
		try {
			url = new URL(gData);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			rd = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			while ((line = rd.readLine()) != null) {
				result += line;
			}
			rd.close();
		} catch (final Exception e) {
			return "";
		}
		return result;
	}

	/**
	 * @author Andrew
	 *
	 *         Formats the id into the YouTube cover image format as highest
	 *         resolution possible. If format changes will get from the JSON
	 */
	public static String getCoverArt(final String id) {
		final String coverArt;
		if (checkStatus("http://i.ytimg.com/vi/" + id + "/maxresdefault.jpg") != 404) {
			coverArt = String.format(
					"https://i.ytimg.com/vi/%s/maxresdefault.jpg", id);
		} else {
			coverArt = String.format("https://i.ytimg.com/vi/%s/hqdefault.jpg",
					id);
		}

		return coverArt;
	}

	private static int checkStatus(final String url) {
		try {
			final URL u = new URL(url);
			final HttpURLConnection huc = (HttpURLConnection) u
					.openConnection();
			huc.setRequestMethod("GET"); // OR huc.setRequestMethod ("HEAD");
			huc.connect();
			final int code = huc.getResponseCode();
			return code;
		} catch (final IOException e) {
			return 400;
		}
	}

	/**
	 * @author Andrew
	 *
	 *         Pulls the title of the video down from JSON
	 *
	 */
	public static String getVideoTitle(final String JSON_DATA) {
		String title = "";
		try {
			final JSONObject json = new JSONObject(JSON_DATA);
			final JSONObject dataObject = json.getJSONObject("entry");
			final JSONObject mediaGroup = dataObject
					.getJSONObject("media$group");
			final JSONObject media_title = mediaGroup
					.getJSONObject("media$title");
			title = StringEscapeUtils.escapeHtml4(media_title.getString("$t")
					.replaceAll("[^\\x20-\\x7e]", ""));
			title = StringEscapeUtils.unescapeHtml4(title);

			if (title.contains(",")) {
				title = escapeComma(title);
			}
			return title;
		} catch (final JSONException e) {
			e.printStackTrace();
		}

		return title;
	}

	/**
	 * @author Andrew
	 *
	 *         Extracts the artist from the video title
	 *
	 */
	public static String getArtist(final String JSON_DATA,
			final String videoTitle) {
		final Pattern pattern = Pattern.compile("^.*?(?=-)");
		final Matcher matcher = pattern.matcher(videoTitle);
		while (matcher.find()) {
			return matcher.group(0).trim();
		}
		final String artist = getUploader(JSON_DATA);

		return artist.trim();
	}

	/**
	 * @author Andrew
	 *
	 *         Extracts the artist from the video title
	 *
	 */
	public static String getSongtitle(final String videoTitle) {
		final Pattern pattern = Pattern.compile("\\-(.*)$");
		final Matcher matcher = pattern.matcher(videoTitle);
		while (matcher.find()) {
			return matcher.group(1).trim();
		}
		return videoTitle.trim();
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
	 *         Gets the total seconds a youtube video last
	 *
	 */
	public static String getDuration(final String JSON_DATA) {
		final String duration = "";
		try {
			final JSONObject json = new JSONObject(JSON_DATA);
			final JSONObject dataObject = json.getJSONObject("entry");
			final JSONObject mediaGroup = dataObject
					.getJSONObject("media$group");
			final JSONObject yt_duration = mediaGroup
					.getJSONObject("yt$duration");
			final int total_duration_seconds = Integer.parseInt(yt_duration
					.getString("seconds"));
			return MediaUtils.calculateTime(total_duration_seconds);
		} catch (final JSONException e) {
			e.printStackTrace();
		}

		return duration;
	}

	/**
	 * @author Andrew
	 *
	 *         Gets the total seconds a youtube video last
	 *
	 */
	public static String getUploader(final String JSON_DATA) {
		final String duration = "";
		try {
			final JSONObject json = new JSONObject(JSON_DATA);
			final JSONObject dataObject = json.getJSONObject("entry");
			final JSONArray author = dataObject.getJSONArray("author");
			final JSONObject userInfo = author.getJSONObject(0);
			final JSONObject userNameObject = userInfo.getJSONObject("name");
			final String username = userNameObject.getString("$t");

			return username;
		} catch (final JSONException e) {
			e.printStackTrace();
		}

		return duration;
	}

	private static String formatYouTubeURL(final String id) {

		final String url = "https://www.youtube.com/watch?v=%s";
		final String rebuiltURL = String.format(url, id);

		return rebuiltURL;
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
	public static String buildPlayListLine(String youTubeVideo) {

		// System.out.println(youTubeVideo);
		if (!youTubeVideo.startsWith("http")) {
			return "";
		}

		final String id = getYouTubeID(youTubeVideo);
		youTubeVideo = formatYouTubeURL(id);

		final String JSON_DATA = getVideoJSON(id);
		if (JSON_DATA.isEmpty()) {
			return "";
		}
		final String thumbNail = getCoverArt(id);
		final String youTubeTitle = getVideoTitle(JSON_DATA);
		final String artist = (getArtist(JSON_DATA, youTubeTitle));
		final String songTitle = (getSongtitle(youTubeTitle));
		final String duration = getDuration(JSON_DATA);
		final String date = getDate();
		final String user = Settings.getUserName();
		final String line = String.format("%s, %s, %s, %s, %s, %s, %s, %s",
				songTitle, artist, duration, date, user, "", thumbNail,
				youTubeVideo);
		return line;
	}

	private static String escapeComma(final String str) {
		return str.replace(",", "\\,");
	}

}
