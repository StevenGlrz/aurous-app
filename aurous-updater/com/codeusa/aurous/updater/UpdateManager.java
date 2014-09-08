package com.codeusa.aurous.updater;

import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Observable;

import javax.swing.JFrame;
import javax.swing.JProgressBar;

// This class downloads a file from a URL.
public class UpdateManager extends Observable implements Runnable {

	public static final int CANCELLED = 3;

	public static final int COMPLETE = 2;

	// These are the status codes.
	public static final int DOWNLOADING = 0;
	public static final int ERROR = 4;
	// Max size of download buffer.
	private static final int MAX_BUFFER_SIZE = 1024;
	public static final int PAUSED = 1;
	// These are the status names.
	public static final String STATUSES[] = { "Downloading", "Paused",
			"Complete", "Cancelled", "Error" };

	private final JProgressBar bar;
	private int downloaded; // number of bytes downloaded
	private final JFrame frame;
	private final GlobalUtils gUtils = new GlobalUtils();
	private final String path; // download URL
	private int size; // size of download in bytes
	private int status; // current status of download
	private final URL url; // download URL

	// Constructor for UpdateManager.

	public UpdateManager(final JFrame jFrame, final URL url, final String path,
			final JProgressBar bar) {
		this.url = url;
		this.frame = jFrame;
		this.path = path;
		this.bar = bar;
		this.size = -1;
		this.downloaded = 0;
		this.status = DOWNLOADING;

		// Begin the download.
		this.download();
	}

	// Cancel this download.
	public void cancel() {
		this.status = CANCELLED;
		this.stateChanged();
	}

	// Start or resume downloading.
	private void download() {
		final Thread thread = new Thread(this);
		thread.start();
	}

	// Mark this download as having an error.
	private void error() {
		this.status = ERROR;
		this.stateChanged();
	}

	// Get file name portion of URL.
	private String getFileName(final URL url) {
		final String fileName = url.getFile();
		return fileName.substring(fileName.lastIndexOf('/') + 1);
	}

	// Get this download's progress.
	public float getProgress() {
		return ((float) this.downloaded / this.size) * 100;
	}

	// Get this download's size.
	public int getSize() {
		return this.size;
	}

	// Get this download's status.
	public int getStatus() {
		return this.status;
	}

	// Get this download's URL.
	public String getUrl() {
		return this.url.toString();
	}

	// Pause this download.
	public void pause() {
		this.status = PAUSED;
		this.stateChanged();
	}

	// Resume this download.
	public void resume() {
		this.status = DOWNLOADING;
		this.stateChanged();
		this.download();
	}

	// UpdateManager file.
	@Override
	public void run() {
		RandomAccessFile file = null;
		InputStream stream = null;

		try {
			// Open connection to URL.
			final HttpURLConnection connection = (HttpURLConnection) this.url
					.openConnection();

			// Specify what portion of file to download.
			connection.setRequestProperty("Range", "bytes=" + this.downloaded
					+ "-");

			// Connect to server.
			connection.connect();

			// Make sure response code is in the 200 range.
			if ((connection.getResponseCode() / 100) != 2) {
				this.error();
			}

			// Check for valid content length.
			final int contentLength = connection.getContentLength();
			if (contentLength < 1) {
				this.error();
			}

			/*
			 * Set the size for this download if it hasn't been already set.
			 */
			if (this.size == -1) {
				this.size = contentLength;
				this.stateChanged();
			}

			// Open file and seek to the end of it.
			file = new RandomAccessFile(this.path + this.getFileName(this.url),
					"rw");
			file.seek(this.downloaded);

			stream = connection.getInputStream();
			while (this.status == DOWNLOADING) {
				/*
				 * Size buffer according to how much of the file is left to
				 * download.
				 */
				byte buffer[];
				if ((this.size - this.downloaded) > MAX_BUFFER_SIZE) {
					buffer = new byte[MAX_BUFFER_SIZE];
				} else {
					buffer = new byte[this.size - this.downloaded];
				}

				// Read from server into buffer.
				final int read = stream.read(buffer);
				if (read == -1) {
					break;
				}

				// Write buffer to file.
				file.write(buffer, 0, read);
				this.downloaded += read;
				this.bar.setValue((int) this.getProgress());
				if (((int) this.getProgress() >= 0)
						&& ((int) this.getProgress() <= 25)) {
					this.bar.setString("Getting Ingredients "
							+ (int) this.getProgress() + "%");
				} else if (((int) this.getProgress() >= 26)
						&& ((int) this.getProgress() <= 50)) {
					this.bar.setString("Removing Old Egg Shells "
							+ (int) this.getProgress() + "%");

				} else if (((int) this.getProgress() >= 51)
						&& ((int) this.getProgress() <= 75)) {
					this.bar.setString("Preheating Oven "
							+ (int) this.getProgress() + "%");
				} else if (((int) this.getProgress() >= 76)
						&& ((int) this.getProgress() <= 100)) {
					this.bar.setString("Finishing Up "
							+ (int) this.getProgress() + "%");
				}

				this.stateChanged();
			}

			/*
			 * Change status to complete if this point was reached because
			 * downloading has finished.
			 */
			if (this.status == DOWNLOADING) {
				this.status = COMPLETE;
				this.stateChanged();
			}
		} catch (final Exception e) {
			this.error();
		} finally {
			// Close file.
			if (file != null) {
				try {
					file.close();
				} catch (final Exception e) {
				}
			}

			// Close connection to server.
			if (stream != null) {
				try {
					stream.close();
				} catch (final Exception e) {
				}
			}
		}
		if (this.path.contains("scripts")) {

			this.gUtils.unZipScripts();

		}
		if (this.path.contains("updates")) {

			this.gUtils.unZipApp();

		}
		if (this.status == COMPLETE) {
			if (this.path.contains("updates")
					|| ((this.gUtils.checkForAppUpdates() == false) && this.path
							.contains("scripts"))) {
				this.frame.setVisible(false);
				this.gUtils.runPopTartTime();
			}
		}
	}

	// Notify observers that this download's status has changed.
	private void stateChanged() {
		this.setChanged();
		this.notifyObservers();
	}
}