package me.aurous.services.impl;

import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.JOptionPane;

import me.aurous.player.Settings;
import me.aurous.services.PlaylistService;
import me.aurous.tools.DiscoMixer;
import me.aurous.tools.PlayListImporter;
import me.aurous.utils.Internet;
import me.aurous.utils.media.MediaUtils;
import me.aurous.utils.playlist.Playlist;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author Andrew
 *
 */
public class YouTubeService implements PlaylistService {
	
	public static String staticPlayerCode = "";

	public void buildPlaylist(final String url, final String playListName) {
		final Playlist playlist = Playlist.getPlaylist();
		try {
			if (url.contains("playlist?")) {
				// fsyprint("Fetching %s...", url);
				String last = "";
				final String out = "data/playlist/" + playListName
						+ ".plist";
				final Document doc = Jsoup.connect(url).get();
				final Elements links = doc.select("a[href]");
				final File playListOut = new File(out);
				final FileOutputStream fos = new FileOutputStream(
						playListOut);
				final BufferedWriter bw = new BufferedWriter(
						new OutputStreamWriter(fos));
				final String header = "Title, Artist, Time, Date Added, User, Album, ALBUMART_INDEX, link";
				int iterations = 0;

				bw.write(header);
				bw.newLine();
				for (final Element link : links) {
					if (playlist.importerOpen
							|| playlist.discoOpen) {
						if (DiscoMixer.discoProgressBar != null) {

							iterations += 1;

							final int percent = (int) ((iterations * 100.0f) / links
									.size());
							System.out.println(percent);
							DiscoMixer.discoProgressBar
							.setValue(percent);
							playlist.disableDiscoInterface();
						} else if (PlayListImporter.importProgressBar != null) {
							iterations += 1;

							final int percent = (int) ((iterations * 100.0f) / links
									.size());

							PlayListImporter.importProgressBar
							.setValue(percent);
							playlist.disableImporterInterface();
						}
						if (link.attr("abs:href").contains("watch?v=")
								&& (link.text().length() > 0)
								&& !link.text().contains("Play all")
								&& !link.text().contains("views")
								&& !link.attr("abs:href").equals(last)) {
							// System.out.println(formatYoutubeURL(link.attr("abs:href")));
							final String mediaLine = buildPlayListLine(link.attr("abs:href"));
							if (!mediaLine.isEmpty()) {
								bw.write(mediaLine);
								bw.newLine();
								last = link.attr("abs:href");

							}

						}
					} else {

						bw.close();
						playlist.deletePlayList(out);
						if (PlayListImporter.importProgressBar != null) {
							playlist.resetImporterInterface();
						} else if (DiscoMixer.discoProgressBar != null) {
							playlist.resetDiscoInterface();
						}
						return;
					}
				}
				bw.close();
				if (PlayListImporter.importProgressBar != null) {
					playlist.resetImporterInterface();
				} else if (DiscoMixer.discoProgressBar != null) {
					playlist.resetDiscoInterface();
				}

			} else {
				JOptionPane.showMessageDialog(null,
						"Invalid URL Detected must contain playlist?",
						"Error", JOptionPane.ERROR_MESSAGE);
				if (PlayListImporter.importProgressBar != null) {
					playlist.resetImporterInterface();
				} else if (DiscoMixer.discoProgressBar != null) {
					playlist.resetDiscoInterface();
				}
			}
		} catch (HeadlessException | IOException e) {
			JOptionPane.showMessageDialog(null, e.toString(), "Error",
					JOptionPane.ERROR_MESSAGE);
			if (PlayListImporter.importProgressBar != null) {
				playlist.resetImporterInterface();
			} else if (DiscoMixer.discoProgressBar != null) {
				playlist.resetDiscoInterface();
			}
			e.printStackTrace();
		}
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
	@Override
	public String buildPlayListLine(String youTubeVideo) {

		// System.out.println(youTubeVideo);
		if (!youTubeVideo.startsWith("http")) {
			return "";
		}

		final String id = YouTubeDataFetcher.getYouTubeID(youTubeVideo);
		youTubeVideo = YouTubeDataFetcher.formatYouTubeURL(id);

		final String JSON_DATA = YouTubeDataFetcher.getVideoJSON(id);
		if (JSON_DATA.isEmpty()) {
			return "";
		}
		final String thumbNail = YouTubeDataFetcher.getCoverArt(id);
		final String youTubeTitle = YouTubeDataFetcher.getVideoTitle(JSON_DATA);
		final String artist = YouTubeDataFetcher.getArtist(JSON_DATA, youTubeTitle);
		final String songTitle = YouTubeDataFetcher.getSongtitle(youTubeTitle);
		final String duration = YouTubeDataFetcher.getDuration(JSON_DATA);
		final String date = YouTubeDataFetcher.getDate();
		final String user = Settings.getUserName();
		final String line = String.format("%s, %s, %s, %s, %s, %s, %s, %s",
				songTitle, artist, duration, date, user, "", thumbNail,
				youTubeVideo);
		return line;
	}

	@Override
	public String getName() {
		return "youtube";
	}

	private static List<String> ExtractUrls(final String html)
			throws UnsupportedEncodingException {
		final List<String> streams = new ArrayList<String>();
		final List<String> signatures = new ArrayList<String>();
		String playerVersion = "";
		Pattern pattern = Pattern
				.compile("\\\\/\\\\/s.ytimg.com\\\\/yts\\\\/jsbin\\\\/html5player-(.+?)\\.js");
		Matcher matcher = pattern.matcher(html);
		while (matcher.find()) {
			playerVersion = matcher.group(1).toString();

		}

		if (staticPlayerCode.equals("")) {
			staticPlayerCode = Internet.text("http://s.ytimg.com/yts/jsbin/"
					+ "html5player-" + playerVersion.replace("\\", "") + ".js");
		}

		pattern = Pattern
				.compile("\"url_encoded_fmt_stream_map\":\\s+\"(.+?)\"");

		matcher = pattern.matcher(html);
		String unescapedHtml = "";
		while (matcher.find()) {

			unescapedHtml = matcher.group(1);

		}

		pattern = Pattern
				.compile("(^url=|(\\\\u0026url=|,url=))(.+?)(\\\\u0026|,|$)");

		matcher = pattern.matcher(unescapedHtml);

		while (matcher.find()) {
			streams.add(URLDecoder.decode(matcher.group(3), "UTF-8"));

		}

		pattern = Pattern
				.compile("(^s=|(\\\\u0026s=|,s=))(.+?)(\\\\u0026|,|$)");

		matcher = pattern.matcher(unescapedHtml);

		while (matcher.find()) {

			signatures.add(URLDecoder.decode(matcher.group(3), "UTF-8"));

		}
		final List<String> urls = new ArrayList<String>();
		for (int i = 0; i < (streams.size() - 1); i++) {
			String URL = streams.get(i).toString();

			if (signatures.size() > 0) {

				final String Sign = YouTubeService.signDecipher(
						signatures.get(i).toString(), staticPlayerCode);

				URL += "&signature=" + Sign;
				// System.out.println(URL.trim());
			}

			urls.add(URL.trim());

		}

		return urls;
	}

	public static String getYouTubeStream(final String html) {
		String lowQualityMP4 = "null";
		String highQualityMP4 = "null";
		try {

			final List<String> list = ExtractUrls(html);
			for (final String url : list) {
				if (url.contains("itag=18")) {
					lowQualityMP4 = url;
				} else if (url.contains("itag=22")) {
					highQualityMP4 = url;
				}
			}
			if (Settings.isStreamLowQuality() == true) {
				return lowQualityMP4;
			}
			if (highQualityMP4.equals("null")) {
				return lowQualityMP4;
			} else {
				return highQualityMP4;
			}

		} catch (final UnsupportedEncodingException e) {

			e.printStackTrace();
		}
		return highQualityMP4;

	}

	private static String signDecipher(final String signature,
			final String playercode) {
		try {
			final ScriptEngine engine = new ScriptEngineManager()
			.getEngineByName("nashorn");
			engine.eval(new FileReader("data/scripts/decrypt.js"));
			final Invocable invocable = (Invocable) engine;

			final Object result = invocable.invokeFunction("getWorkingVideo",
					signature, playercode);
			return (String) result;
		} catch (ScriptException | FileNotFoundException
				| NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "error";
	}

	private static class YouTubeDataFetcher {

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
			final String gData = String.format("%s%s%s", API_URL, videoID, "?alt=json");
			if (gData.equals("http://gdata.youtube.com/feeds/api/videos/?alt=json")) {
				return "";
			}
			// System.out.println(gData);
			try {
				url = new URL(gData);
				conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
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

		public static String formatYouTubeURL(final String id) {
			return String.format("https://www.youtube.com/watch?v=%s", id);
		}

		private static String escapeComma(final String str) {
			return str.replace(",", "\\,");
		}

	}
}
