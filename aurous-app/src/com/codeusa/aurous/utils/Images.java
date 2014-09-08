package com.codeusa.aurous.utils;

import java.awt.Image;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * Created by Kenneth on 9/4/2014.
 */
public class Images {

	private static Map<String, Image> IMAGE_MAP = new HashMap<>();

	private static String[][] images = {
		{ "play", "Actions-media-playback-start-icon.png" },
		{ "volume-low", "Status-audio-volume-low-icon.png" },
		{ "volume-medium", "Status-audio-volume-medium-icon.png" },
		{ "volume-high", "Status-audio-volume-high-icon.png" },
		{ "volume-mute", "Status-audio-volume-muted-icon.png" },
		{ "pause", "Actions-media-playback-pause-icon.png" },
		{ "placeholder", "album-placeholder.png"},
		
		{ "poptart", "aurouslogo.png" }

	};

	static {
		for (final String[] image : images) {
			IMAGE_MAP.put(image[0], load(image[1]));
		}
	}

	public static Image get(final String name) {
		return IMAGE_MAP.get(name);
	}

	private static Image load(final String name) {
		try {
			// System.out.println(this.getclass().getResource("/resources/" +
			// name));
			return ImageIO.read(Images.class.getResourceAsStream("/resources/"
					+ name));
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
