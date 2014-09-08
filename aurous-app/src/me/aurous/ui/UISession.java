package me.aurous.ui;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import me.aurous.player.scenes.MediaPlayerScene;
import me.aurous.ui.frames.AurousFrame;

/**
 * Created by Kenneth on 9/4/2014.
 */
public class UISession {

	private static AurousFrame presenter;

	public static AurousFrame getPresenter() {
		return presenter;
	}

	public static void setPresenter(final AurousFrame presenter) {
		UISession.presenter = presenter;
	}

	private static MediaPlayer mediaPlayer;

	public static MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}

	public static void setMediaPlayer(final MediaPlayer mediaPlayer) {
		UISession.mediaPlayer = mediaPlayer;
	}

	private static MediaView mediaView;

	public static MediaView getMediaView() {
		return mediaView;
	}

	public static void setMediaView(final MediaView mediaView) {
		UISession.mediaView = mediaView;
	}

	private static Media media;

	public static Media getMedia() {
		return media;
	}

	public static void setMedia(final Media media) {
		UISession.media = media;

	}

	private static MediaPlayerScene mediaScene;

	public static MediaPlayerScene getMediaPlayerScene() {
		return mediaScene;
	}

	public static void setMediaPlayerScene(final MediaPlayerScene scene) {
		UISession.mediaScene = scene;

	}

	private static JFXPanel jfxPanel;

	public static JFXPanel getJFXPanel() {
		return jfxPanel;
	}

	public static void setJFXPanel(final JFXPanel fxPanel) {
		UISession.jfxPanel = fxPanel;

	}
}
