package me.aurous.player;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import me.aurous.ui.panels.ControlPanel;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Andrew
 *
 */
public class Settings {
	private static String avatarURL = "todo";
	private static String userName = "Aurous User";
	private static int volume = 50; // done
	private static String profileURL = "todo";
	private static boolean displayAlert = true;
	private static boolean streamLowQuality = false;
	private static String lastPlayList = ""; // done
	private static boolean savePlayBack = false;

	public static boolean isSavePlayBack() {
		return savePlayBack;
	}

	public static void setSavePlayBack(final boolean savePlayBack) {
		Settings.savePlayBack = savePlayBack;
	}

	public static String getLastPlayList() {
		return lastPlayList;
	}

	public static void setLastPlayList(final String lastPlayList) {
		Settings.lastPlayList = lastPlayList;
	}

	public static String getUserName() {
		return userName;
	}

	public static void setUserName(final String userName) {
		Settings.userName = userName;
	}

	public static String getProfileURL() {
		return profileURL;
	}

	public static void setProfileURL(final String profileURL) {
		Settings.profileURL = profileURL;
	}

	public static boolean isDisplayAlert() {
		return displayAlert;
	}

	public static void setDisplayAlert(final boolean displayAlert) {
		Settings.displayAlert = displayAlert;
	}

	public static boolean isStreamLowQuality() {
		return streamLowQuality;
	}

	public static void setStreamLowQuality(final boolean streamLowQuality) {
		Settings.streamLowQuality = streamLowQuality;
	}

	public static int getVolume() {
		return volume;
	}

	public static void setVolume(final int volume) {
		Settings.volume = volume;
	}

	public static String getAvatarURL() {
		return avatarURL;
	}

	public static void setAvatarURL(final String avatarURL) {
		Settings.avatarURL = avatarURL;
	}

	public static void saveSettings(final boolean reload) {
		final JSONObject obj = new JSONObject();
		obj.put("low_quality", isStreamLowQuality());
		obj.put("volume", new Integer(getVolume()));
		obj.put("display_alert", isDisplayAlert());
		obj.put("avatar", new String(getAvatarURL()));
		obj.put("profile_url", new String(getProfileURL()));
		obj.put("username", new String(getUserName()));
		obj.put("last_playlist", new String(getLastPlayList()));
		obj.put("save_playback", isSavePlayBack());

		try {

			final FileWriter file = new FileWriter(
					"data/settings/settings.json");
			file.write(obj.toString());
			file.flush();
			file.close();

		} catch (final IOException e) {
			e.printStackTrace();
		}

		if (reload) {
			loadSettings();
		}

	}

	public static void loadSettings() {

		String jsonData = "";
		BufferedReader br = null;
		try {
			String line;
			br = new BufferedReader(new FileReader(
					"data/settings/settings.json"));
			while ((line = br.readLine()) != null) {
				jsonData += line + "\n";
			}
		} catch (final IOException e) {
			saveSettings(true);
			return;
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (final IOException ex) {
				ex.printStackTrace();
			}
		}
		try {
			final JSONObject json = new JSONObject(jsonData);

			final String username = json.getString("username");
			setUserName(username);

			final boolean isLowQuality = json.getBoolean("low_quality");
			setStreamLowQuality(isLowQuality);

			final boolean displayAlert = json.getBoolean("display_alert");
			setDisplayAlert(displayAlert);

			final boolean savePlayBack = json.getBoolean("display_alert");
			setSavePlayBack(savePlayBack);

			final String lastPlayListLocal = json.getString("save_playback");
			setLastPlayList(lastPlayListLocal);

			final int volume = json.getInt("volume");
			setVolume(volume);
			ControlPanel.volume().setValue(getVolume());
		} catch (final JSONException e) {
			saveSettings(false);

		}

	}

	static String readFile(final String path, final Charset encoding)
			throws IOException {
		final byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
}
