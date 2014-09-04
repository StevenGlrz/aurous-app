package com.codeusa.poptart.grabbers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.codeusa.poptart.player.Settings;
import com.codeusa.poptart.utils.media.MediaUtils;

/**
 * @author Andrew
 *
 */
public class YouTubeGrabber {
	public static String staticPlayerCode = "";

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
			staticPlayerCode = MediaUtils.getHTML("http://s.ytimg.com/yts/jsbin/"
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

				final String Sign = YouTubeGrabber.signDecipher(
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

}
