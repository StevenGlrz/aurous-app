package com.codeusa.poptart.player.scenes;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;

import com.codeusa.poptart.ui.panels.PlayerControlPanel;
import com.codeusa.poptart.utils.media.MediaUtils;

public class MediaPlayerScene {

	public static Media media;

	public static MediaPlayer player;

	// private static StreamFunctions sFunctions = new StreamFunctions();

	public static MediaView view;

	public static Scene createScene(final String sourceURL) throws Throwable {
		final Group root = new Group();
		root.autosize();
		MediaUtils.activeMedia = sourceURL;
		final String trailer = MediaUtils.getMediaURL(sourceURL);

		MediaPlayerScene.media = new Media(trailer);

		MediaPlayerScene.player = new MediaPlayer(MediaPlayerScene.media);
		MediaPlayerScene.view = new MediaView(MediaPlayerScene.player);
		MediaPlayerScene.view.setFitWidth(1);
		MediaPlayerScene.view.setFitHeight(1);
		MediaPlayerScene.view.setPreserveRatio(false);

		// System.out.println("media.width: "+media.getWidth());

		final Scene scene = new Scene(root, 1, 1, Color.BLACK);

		MediaPlayerScene.player.play();

		MediaPlayerScene.player.setOnReady(() -> {
			PlayerControlPanel.durationSlider.setValue(0);

		});
		MediaPlayerScene.player.currentTimeProperty().addListener(
				(observableValue, duration, current) -> {

					final long currentTime = (long) current.toMillis();

					final long totalDuration = (long) MediaPlayerScene.player
							.getMedia().getDuration().toMillis();
					MediaPlayerScene.updateTime(currentTime, totalDuration);

				});

		// PlayerUtils.activeYoutubeVideo = youtubeVideo;
		if (sourceURL.equals("https://www.youtube.com/watch?v=kGubD7KG9FQ")) {
			player.pause();
		}
		return (scene);
	}

	private static void updateTime(final long currentTime,
			final long totalDuration) {

		final int percentage = (int) (((currentTime * 100.0) / totalDuration) + 0.5); // jesus
		final double seconds = currentTime / 1000.0;
		PlayerControlPanel.durationSlider.setValue(percentage);
		PlayerControlPanel.currentTimeLabel.setText(MediaUtils
				.calculateTime((int) seconds));
		// god
		if (percentage == 100) {

			MediaUtils.handleEndOfStream();
		}

	}

}
