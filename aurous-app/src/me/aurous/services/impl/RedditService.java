package me.aurous.services.impl;

import java.awt.HeadlessException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.swing.JOptionPane;

import me.aurous.services.PlaylistService;
import me.aurous.tools.PlayListImporter;
import me.aurous.utils.media.MediaUtils;
import me.aurous.utils.playlist.Playlist;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author Andrew
 *
 */
public class RedditService implements PlaylistService {
	private static String COMMENT_QUERY = "?limit=1000"; // is it 500 or 1000?

	private static String addQueryToURL(final String url) {
		return String.format(url + "%s", COMMENT_QUERY);
	}

	public void buildPlaylist(String url1, final String playListName) {
		if (url1.contains("comments")) {
			url1 = addQueryToURL(url1);
		}
		final String url = url1;
		final Playlist playlist = Playlist.getPlaylist();
		try {
			if (url.contains("reddit")) {
				System.out.println("dacac");
				// print("Fetching %s...", url);
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
				bw.write(header);
				bw.newLine();
				int iterations = 0;
				for (final Element link : links) {
					if (playlist.importerOpen) {
						if (PlayListImporter.importProgressBar != null) {

							iterations += 1;

							final int percent = (int) ((iterations * 100.0f) / links
									.size());
							System.out.println(percent);
							PlayListImporter.importProgressBar
							.setValue(percent);
							playlist.disableImporterInterface();
						}
						if (!link.attr("abs:href").equals(last)) {

							final String mediaLine = MediaUtils
									.getBuiltString(link
											.attr("abs:href"));
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
						}
						return;
					}
				}
				bw.close();
				if (PlayListImporter.importProgressBar != null) {
					playlist.resetImporterInterface();
				}

			} else {
				JOptionPane.showMessageDialog(null,
						"Invalid URL Detected, is this reddit?",
						"Error", JOptionPane.ERROR_MESSAGE);
				if (PlayListImporter.importProgressBar != null) {
					playlist.resetImporterInterface();
				}
			}
		} catch (HeadlessException | IOException e) {
			JOptionPane.showMessageDialog(null,
					"HTTP client error, please try again.", "Error",
					JOptionPane.ERROR_MESSAGE);
			if (PlayListImporter.importProgressBar != null) {
				playlist.resetImporterInterface();
			}
			// e.printStackTrace();
		}
	}

	@Override
	public String buildPlayListLine(String context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return "reddit";
	}

}
