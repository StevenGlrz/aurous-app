package com.codeusa.aurous.updater;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.JFrame;

import org.apache.commons.io.IOUtils;

public class GlobalUtils {

	/**
	 * Center a frame on the main display
	 *
	 * @param frame
	 *            The frame to center
	 */
	public void centerFrameOnMainDisplay(final JFrame frame) {
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

	public boolean checkForAppUpdates() {
		try {
			final String localVersion = this.readFile("appversion.txt",
					StandardCharsets.UTF_8);
			final String remoteVersion = this.readRemoteFile(localVersion,
					"http://codeusa.net/apps/poptart/appversion.txt");
			if (localVersion.equals(remoteVersion)) {
				return false;
			} else {
				return true;
			}

		} catch (final IOException e) {
			return false;
		}
	}

	public boolean checkForScriptUpdates() {
		try {
			final String localVersion = this.readFile("decryptversion.txt",
					StandardCharsets.UTF_8);
			final String remoteVersion = this.readRemoteFile(localVersion,
					"http://codeusa.net/apps/poptart/decryptversion.txt");
			if (localVersion.equals(remoteVersion)) {

				return false;
			} else {
				return true;
			}
		} catch (final IOException e) {
			return false;
		}
	}

	private String readFile(final String path, final Charset encoding)
			throws IOException {
		final byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	private String readRemoteFile(final String local, final String path) {
		try {
			// Create a URL for the desired page
			final URL url = new URL(path);

			// Read all the text returned by the server
			final BufferedReader in = new BufferedReader(new InputStreamReader(
					url.openStream()));
			final String str = in.readLine();
			in.close();
			return str;
		} catch (final MalformedURLException e) {
			System.out.println("Dada");

			return local;
		} catch (final IOException e) {
			System.out.println("da525");
			return local;
		}
	}

	public void runPopTartTime() {
		try {
			final Process ps = Runtime.getRuntime().exec(
					new String[] { "java", "-jar", "Aurous.jar" });
			ps.waitFor();
			final java.io.InputStream is = ps.getInputStream();
			final byte b[] = new byte[is.available()];
			is.read(b, 0, b.length);
			System.out.println(new String(b));
			System.exit(0);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setNewVersion(final String path, final String remote) {

		try {
			final String remoteVersion = this.readRemoteFile("1", remote);
			final File appVersion = new File(path);
			final FileWriter appWriter = new FileWriter(appVersion, false); // true
			// to
			// append
			// false to overwrite.
			appWriter.write(remoteVersion);
			appWriter.close();
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void unZipApp() {

		try {
			final File f = new File("updates/app.zip");
			if (f.exists() && !f.isDirectory()) { /* do something */
				this.unZipFile("updates/app.zip", "./");
				f.delete();
				this.setNewVersion("appversion.txt",
						"http://codeusa.net/apps/poptart/appversion.txt");
			} else {

			}
		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void unZipFile(final String zipFilePath,
			final String destinationPath) throws IOException {
		ZipInputStream zis = null;
		try {

			zis = new ZipInputStream(new FileInputStream(zipFilePath));
			ZipEntry entry;

			while ((entry = zis.getNextEntry()) != null) {

				// Create a file on HDD in the destinationPath directory
				// destinationPath is a "root" folder, where you want to extract
				// your ZIP file
				final File entryFile = new File(destinationPath,
						entry.getName());
				if (entry.isDirectory()) {

					if (entryFile.exists()) {
						System.out.println(String.format(
								"Directory {0} already exists!", entryFile));
					} else {
						entryFile.mkdirs();
					}

				} else {

					// Make sure all folders exists (they should, but the safer,
					// the better ;-))
					if ((entryFile.getParentFile() != null)
							&& !entryFile.getParentFile().exists()) {
						entryFile.getParentFile().mkdirs();
					}

					// Create file on disk...
					if (!entryFile.exists()) {
						entryFile.createNewFile();
					}

					// and rewrite data from stream
					OutputStream os = null;
					try {
						os = new FileOutputStream(entryFile);

						IOUtils.copy(zis, os);
					} finally {
						IOUtils.closeQuietly(os);
					}
				}
			}
		} finally {
			IOUtils.closeQuietly(zis);
		}
	}

	public void unZipScripts() {
		try {
			final File f = new File("./data/scripts/decrypt.zip");
			if (f.exists() && !f.isDirectory()) { /* do something */

				this.unZipFile("./data/scripts/decrypt.zip", "./data/scripts/");
				f.delete();
				this.setNewVersion("decryptversion.txt",
						"http://codeusa.net/apps/poptart/decryptversion.txt");
			} else {

			}
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
