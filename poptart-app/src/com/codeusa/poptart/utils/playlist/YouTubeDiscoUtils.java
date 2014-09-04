package com.codeusa.poptart.utils.playlist;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringEscapeUtils;

import com.codeusa.poptart.tools.DiscoMixer;
import com.codeusa.poptart.utils.media.MediaUtils;

/**
 * @author Andrew
 *
 */
public class YouTubeDiscoUtils {
	private static String TOP_100_URL = "https://www.youtube.com/playlist?list=MCUS";
	private static String DISCO_API_URL = "https://www.youtube.com/disco?action_search=1&query=%s";
	private static String PLAYLIST_URL = "https://www.youtube.com/playlist?list=%s";

	private static String getDiscoJson(final String query) {
		String JSON_URL = String.format(DISCO_API_URL, query);
		JSON_URL = JSON_URL.replace(" ", "%20");
		final String json_data = MediaUtils.getHTML(JSON_URL);

		return json_data;
	}

	private static String getPlayListURL(final String json) {
		String playListID = MediaUtils.getBetween(json, "\\u0026list=", "\\u0026");
		final String discoPlayList = String.format(PLAYLIST_URL, playListID);

		return discoPlayList;
	}

	public static void buildTopPlayList() {
		YouTubePlayListImporter.importYoutubePlayList(TOP_100_URL,
				"YouTube Daily Top Tracks");

	}

	public static void buildDiscoPlayList(final String query) {
		final String json = getDiscoJson(query);
		if (json.isEmpty()) {
			JOptionPane.showMessageDialog(DiscoMixer.discoFrame,
					"There was an error getting some needed data, try again!",
					"Error", JOptionPane.ERROR_MESSAGE);
			DiscoMixer.discoProgressBar.setVisible(false);
			return;
		}

		final String url = getPlayListURL(json);
		final String title = getPlayListTitle(url);
		if (title.equals("Oops! Something went wrong.")) {
			JOptionPane
					.showMessageDialog(
							DiscoMixer.discoFrame,
							"Unable to locate any playlist for this artist/song \n But don't worry some may show up soon!",
							"Error", JOptionPane.ERROR_MESSAGE);
			DiscoMixer.discoProgressBar.setVisible(false);
			return;
		}
		YouTubePlayListImporter.importYoutubePlayList(url, title);

	}

	private static String getPlayListTitle(final String url) {
		final String html = MediaUtils.getHTML(url);
		final String pattern1 = "<title>";
		final String pattern2 = "- YouTube</title>";
		String title = "";

		final Pattern p = Pattern.compile(Pattern.quote(pattern1) + "(.*?)"
				+ Pattern.quote(pattern2));
		final Matcher m = p.matcher(html);
		while (m.find()) {

			title = m.group(1);

		}
		title = StringEscapeUtils.escapeHtml4(title.trim().replaceAll(
				"[^\\x20-\\x7e]", ""));
		title = StringEscapeUtils.unescapeHtml4(title);

		return title;
	}


}
