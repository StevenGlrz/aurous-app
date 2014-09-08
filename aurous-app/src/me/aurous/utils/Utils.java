package me.aurous.utils;

import java.awt.Desktop;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.io.File;
import java.net.URL;

import javax.swing.JFrame;

/**
 *
 * @author https://github.com/nikkiii
 * @author Andrew
 *
 *
 */
public class Utils {

	/**
	 *
	 * An enum containing operating system types
	 *
	 *
	 *
	 */
	public static enum OperatingSystem {
		LINUX, SOLARIS, WINDOWS, MAC, UNKNOWN
	}

	/**
	 * Center a frame on the main display
	 *
	 * @param frame
	 *            The frame to center
	 */
	public static void centerFrameOnMainDisplay(final JFrame frame) {
		final GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		final GraphicsDevice[] screens = ge.getScreenDevices();
		if (screens.length < 1) {
			return; // Silently fail.
		}
		final Rectangle screenBounds = screens[0].getDefaultConfiguration()
				.getBounds();
		final int x = (int) ((screenBounds.getWidth() - frame.getWidth()) / 2);
		final int y = (int) ((screenBounds.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);
	}

	/**
	 * A method to get the unix time...
	 *
	 * @return The current time in seconds
	 */
	public static long currentTimeSeconds() {
		return (System.currentTimeMillis() / 1000);
	}

	/**
	 * Finds the first supported program in the list (for UNIX-like platforms
	 * only).
	 *
	 * @param kind
	 *            The kind of program, used in the exception message if no
	 *            suitable program could be found.
	 * @param names
	 *            The array of program names to try.
	 * @return The first supported program from the array of names.
	 * @throws Exception
	 *             if no supported program could be found.
	 */
	private static String findSupportedProgram(final String kind,
			final String[] names) throws Exception {
		for (final String name : names) {
			final Process process = Runtime.getRuntime().exec(
					new String[] { "which", name });
			if (process.waitFor() == 0) {
				return name;
			}
		}

		throw new Exception("Unable to find supported " + kind);
	}

	/**
	 * Get the current platform
	 *
	 * @return The current platform
	 */
	public static OperatingSystem getPlatform() {
		final String osName = System.getProperty("os.name").toLowerCase();
		if (osName.contains("win")) {
			return OperatingSystem.WINDOWS;
		}
		if (osName.contains("mac")) {
			return OperatingSystem.MAC;
		}
		if (osName.contains("solaris")) {
			return OperatingSystem.SOLARIS;
		}
		if (osName.contains("sunos")) {
			return OperatingSystem.SOLARIS;
		}
		if (osName.contains("linux")) {
			return OperatingSystem.LINUX;
		}
		if (osName.contains("unix")) {
			return OperatingSystem.LINUX;
		}
		return OperatingSystem.UNKNOWN;
	}

	/**
	 * Get the system architecture
	 *
	 * @return The system architecture integer
	 */
	public static int getSystemArch() {
		final String archs = System.getProperty("os.arch");
		return Integer.parseInt(archs.substring(archs.length() - 2));
	}

	/**
	 * Open a file using {@link Desktop} if supported, or a manual
	 * platform-specific method if not.
	 *
	 * @param file
	 *            The file to open.
	 * @throws Exception
	 *             if the file could not be opened.
	 */
	public static void openFile(final File file) throws Exception {
		final Desktop desktop = Desktop.isDesktopSupported() ? Desktop
				.getDesktop() : null;
		if ((desktop != null) && desktop.isSupported(Desktop.Action.OPEN)) {
			desktop.open(file);
		} else {
			final OperatingSystem system = Utils.getPlatform();
			switch (system) {
			case MAC:
			case WINDOWS:
				Utils.openURL(file.toURI().toURL());
				break;
			default:
				final String fileManager = Utils.findSupportedProgram(
						"file manager", Utils.FILE_MANAGERS);
				Runtime.getRuntime().exec(
						new String[] { fileManager, file.getAbsolutePath() });
				break;
			}
		}
	}

	/**
	 * Open a URL using java.awt.Desktop or a couple different manual methods
	 *
	 * @param url
	 *            The URL to open
	 * @throws Exception
	 *             If an error occurs attempting to open the url
	 */
	public static void openURL(final URL url) throws Exception {
		final Desktop desktop = Desktop.isDesktopSupported() ? Desktop
				.getDesktop() : null;
				if ((desktop != null) && desktop.isSupported(Desktop.Action.BROWSE)) {
					desktop.browse(url.toURI());
				} else {
					final OperatingSystem system = Utils.getPlatform();
					switch (system) {
					case MAC:
						Class.forName("com.apple.eio.FileManager")
						.getDeclaredMethod("openURL",
								new Class[] { String.class })
								.invoke(null, new Object[] { url.toString() });
						break;
					case WINDOWS:
						Runtime.getRuntime()
						.exec(new String[] { "rundll32",
								"url.dll,FileProtocolHandler", url.toString() });
						break;
					default:
						final String browser = Utils.findSupportedProgram("browser",
								Utils.BROWSERS);
						Runtime.getRuntime().exec(
								new String[] { browser, url.toString() });
						break;
					}
				}
	}

	/**
	 * Trim the string's start/finish of the specified character
	 *
	 * @param str
	 *            The string
	 * @param ch
	 *            The character
	 * @return The trimmed string
	 */
	public static String trim(String str, final char ch) {
		if ((str == null) || str.isEmpty()) {
			return str;
		} else if (str.length() == 1) {
			return str.charAt(0) == ch ? "" : str;
		}
		try {
			if (str.charAt(0) == ch) {
				str = str.substring(1);
			}
			final int l = str.length() - 1;
			if (str.charAt(l) == ch) {
				str = str.substring(0, l);
			}
			return str;
		} catch (final Exception e) {
			return str;
		}
	}

	/**
	 * Make each word in a string uppercase
	 *
	 * @param string
	 *            The string to parse
	 * @return The formatted string
	 */
	public static String ucwords(final String string) {
		final StringBuilder out = new StringBuilder();
		final String[] split = string.split(" ");
		for (int i = 0; i < split.length; i++) {
			final String str = split[i];
			out.append(Character.toUpperCase(str.charAt(0)));
			if (str.length() > 1) {
				out.append(str.substring(1).toLowerCase());
			}
			if (i < (split.length - 1)) {
				out.append(" ");
			}
		}
		return out.toString();
	}

	static final String errMsg = "Error opening browser";
	/**
	 * Open a URL in the users default browser
	 *
	 * @param url
	 *            The URL to open
	 * @throws Exception
	 *             If an error occurs attempting to open the url
	 */
	/*
	 * public static void openURL(String url) { try { //attempt to use Desktop
	 * library from JDK 1.6+ Class<?> d = Class.forName("java.awt.Desktop");
	 * d.getDeclaredMethod("browse", new Class[] {java.net.URI.class}).invoke(
	 * d.getDeclaredMethod("getDesktop").invoke(null), new Object[]
	 * {java.net.URI.create(url)}); //above code mimicks:
	 * java.awt.Desktop.getDesktop().browse() } catch (Exception ignore) {
	 * //library not available or failed String osName =
	 * System.getProperty("os.name"); try { if (osName.startsWith("Mac OS")) {
	 * Class.forName("com.apple.eio.FileManager").getDeclaredMethod( "openURL",
	 * new Class[] {String.class}).invoke(null, new Object[] {url}); } else if
	 * (osName.startsWith("Windows")) Runtime.getRuntime().exec(
	 * "rundll32 url.dll,FileProtocolHandler " + url); else { //assume Unix or
	 * Linux String browser = null; for (String b : BROWSERS) if (browser ==
	 * null && Runtime.getRuntime().exec(new String[] {"which",
	 * b}).getInputStream().read() != -1) Runtime.getRuntime().exec(new String[]
	 * {browser = b, url}); if (browser == null) throw new
	 * Exception(Arrays.toString(BROWSERS)); } } catch (Exception e) {
	 * JOptionPane.showMessageDialog(null, errMsg + "\n" + e.toString()); } }
	 *
	 *
	 * }
	 */// bad codei s bad,

	/**
	 * The cached operating system
	 */
	public static final OperatingSystem SYSTEM = Utils.getPlatform();

	/**
	 * A list of popular file managers
	 */
	private static final String[] FILE_MANAGERS = new String[] { "xdg-open",
			"nautilus", "dolphin", "thunar", "pcmanfm", "konqueror" };

	/**
	 * A list of popular browsers
	 */
	private static final String[] BROWSERS = new String[] { "xdg-open",
			"google-chrome", "chromium", "firefox", "opera", "epiphany",
			"konqueror", "conkeror", "midori", "kazehakase", "mozilla" };

}