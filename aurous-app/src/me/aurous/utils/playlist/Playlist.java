package me.aurous.utils.playlist;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import me.aurous.player.Settings;
import me.aurous.services.PlaylistService;
import me.aurous.services.impl.RedditService;
import me.aurous.services.impl.SoundCloudService;
import me.aurous.services.impl.YouTubeService;
import me.aurous.swinghacks.GhostText;
import me.aurous.tools.DiscoMixer;
import me.aurous.tools.PlayListBuilder;
import me.aurous.tools.PlayListImporter;
import me.aurous.ui.listeners.ContextMenuMouseListener;
import me.aurous.utils.media.MediaUtils;

/**
 * @author Andrew
 *
 */
public class Playlist {

	private static final Playlist INSTANCE = new Playlist();

	public static Playlist getPlaylist() {
		return INSTANCE; // TODO Find a better place to place the playlist instance
	}

	public boolean builderOpen;
	public boolean importerOpen;
	public boolean settingsOpen;
	public boolean discoOpen;
	public boolean aboutOpen;

	private List<PlaylistService> platforms = new LinkedList<>();
	private ExecutorService executorService = Executors.newCachedThreadPool();

	private Playlist() {
		platforms.add(new YouTubeService());
		platforms.add(new RedditService());
		platforms.add(new SoundCloudService());
	}

	/**
	 * @author Andrew
	 *
	 *         Removes a row from the JTable while deleting the line from the
	 *         playlist
	 */
	public void removeSelectedRows(final JTable table) {
		final DefaultTableModel model = (DefaultTableModel) table.getModel();
		final int[] rows = table.getSelectedRows();
		removeLineFromPlayList(Settings.getLastPlayList(), (String) table.getValueAt(rows[0], 7));

		for (int i = 0; i < rows.length; i++) {
			model.removeRow(rows[i] - i);
		}
	}

	public void disableDiscoInterface() {
		DiscoMixer.discoBuildButton.setEnabled(false);
		DiscoMixer.queryField.setEnabled(false);
		DiscoMixer.top100Button.setEnabled(false);
	}

	public void resetDiscoInterface() {
		DiscoMixer.discoProgressBar.setValue(0);
		DiscoMixer.discoProgressBar.setVisible(false);

		DiscoMixer.discoBuildButton.setEnabled(true);
		DiscoMixer.queryField.setEnabled(true);
		DiscoMixer.top100Button.setEnabled(true);
	}

	public void resetImporterInterface() {
		PlayListImporter.importProgressBar.setValue(0);
		PlayListImporter.importProgressBar.setVisible(false);
		PlayListImporter.importInstrucLabel.setText("Import Playlist");
		PlayListImporter.lblEnterAPlaylist.setText("Enter a Playlist Name");
		PlayListImporter.importPlayListButton.setEnabled(true);
	}

	public void disableImporterInterface() {
		PlayListImporter.importProgressBar.setVisible(true);

		PlayListImporter.importPlayListButton.setEnabled(false);
		PlayListImporter.importInstrucLabel.setText("Importing Playlist");
		PlayListImporter.lblEnterAPlaylist.setText("");
	}

	public void buildPlaylistFor(String name, String url, String playlistName) {
		PlaylistService service = getPlaylistService(name);
		if (service != null) {
			executorService.execute(() -> service.buildPlaylist(url, playlistName));
		}
	}

	public void getImportRules(final String sourceURL, final String playListName) {
		PlaylistService service = getPlaylistService(sourceURL);

		if (service != null) {
			executorService.execute(() -> service.buildPlaylist(sourceURL, playListName));
		} else {
			JOptionPane.showMessageDialog(null, "No importer found!", "Error", JOptionPane.ERROR_MESSAGE);
			PlayListImporter.importProgressBar.setVisible(false);
		}
	}

	public String getAddRules(final String sourceURL) {
		PlaylistService service = getPlaylistService(sourceURL);
		if (service != null) {
			return service.buildPlayListLine(sourceURL);
		}
		JOptionPane.showMessageDialog(null, "No importer found!", "Error", JOptionPane.ERROR_MESSAGE);
		return "";
	}

	/**
	 * popup panel to add url to playlist
	 */
	public void additionToPlayListPrompt() {

		if ((Settings.getLastPlayList() == null)
				|| Settings.getLastPlayList().isEmpty()) {
			JOptionPane.showMessageDialog(new JFrame(),
					"You do not have any playlist loaded!", "Uh oh",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		final JTextField urlField = new JTextField();
		urlField.addMouseListener(new ContextMenuMouseListener());
		urlField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent e) {

			}
		});
		new GhostText("https://www.youtube.com/watch?v=TU3b1qyEGsE", urlField);
		urlField.setHorizontalAlignment(SwingConstants.CENTER);
		final JPanel panel = new JPanel(new GridLayout(0, 1));

		panel.add(new JLabel("Paste media url"));
		panel.add(urlField);

		final int result = JOptionPane.showConfirmDialog(null, panel,
				"Add to Playlist", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);

		if (result == JOptionPane.OK_OPTION) {
			addUrlToPlayList(urlField.getText());
		} else {

		}

	}

	private void addUrlToPlayList(final String url) {
		if (url.isEmpty()) {
			return;
		}
		if (url.contains("&list=")) {
			return;
		} else {
			try {
				final String filename = Settings.getLastPlayList();

				final FileWriter fw = new FileWriter(filename, true); // the
				// true
				final String data = getAddRules(url);

				fw.write("\n" + data);// appends

				fw.close();

			} catch (final IOException ioe) {
				System.err.println("IOException: " + ioe);
			}
		}

	}

	/**
	 * popup panel to create a playlist
	 */
	public String importPlayListPrompt() {
		final JTextField urlField = new JTextField();
		final GhostText gText = new GhostText("Enter service url", urlField);

		urlField.addMouseListener(new ContextMenuMouseListener());
		urlField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent e) {

			}
		});
		urlField.setHorizontalAlignment(SwingConstants.CENTER);
		gText.setHorizontalAlignment(SwingConstants.CENTER);

		final JPanel panel = new JPanel(new GridLayout(0, 1));

		panel.add(new JLabel("Enter a PlayList URL"));
		panel.add(urlField);
		final int result = JOptionPane.showConfirmDialog(null, panel,
				"Add to Service", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);

		if (result == JOptionPane.OK_OPTION) {
			if (!urlField.getText().isEmpty()) {
				return urlField.getText();
			}
		} else {

		}
		return "";

	}

	public void buildPlayList(final String playListItems,
			final String playListName) {
		final Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					final String name = "data/playlist/" + playListName
							+ ".plist";
					final String header = "Title, Artist, Time, Date Added, User, Album, ALBUMART_INDEX, link";
					final File file = new File(name);

					final PrintWriter printWriter = new PrintWriter(file);
					final String[] lines = playListItems.split("\n");
					printWriter.println(header);
					for (final String line : lines) {
						if (builderOpen) {

							final String playListItem = MediaUtils.getBuiltString(line.trim());
							if (!playListItem.isEmpty()) {

								printWriter.println(playListItem);
							} else {
								continue;
							}

						} else {

							printWriter.close();
							deletePlayList(name);
							return;
						}

					}
					printWriter.close();
					PlayListBuilder.loadingIcon.setVisible(false);
					PlayListBuilder.playListTextArea.setEditable(true);
					PlayListBuilder.buildListButton.setEnabled(true);
					PlayListBuilder.playListNameTextField.setEditable(true);
				} catch (final FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		thread.start();

	}

	public void deletePlayList(final JList<?> list)

	{
		final String path = list.getSelectedValue().toString();
		try {

			final File file = new File(path);

			if (file.delete()) {
				System.out.println(file.getName() + " is deleted!");
			} else {
				System.out.println("Delete operation is failed.");
			}

		} catch (final Exception e) {

			e.printStackTrace();

		}
	}

	public void deletePlayList(final String path)

	{

		try {

			final File file = new File(path);

			if (file.delete()) {
				System.out.println(file.getName() + " is deleted!");
			} else {
				System.out.println("Delete operation is failed.");
			}

		} catch (final Exception e) {

			e.printStackTrace();

		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void watchPlayListDirectory(final JList<?> displayList) {

		try {
			final WatchService watcher = FileSystems.getDefault()
					.newWatchService();
			final Path dir = Paths.get("data/playlist/");
			dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

			System.out.println("Watch Service registered for dir: "
					+ dir.getFileName());

			while (true) {
				WatchKey key;
				try {
					key = watcher.take();
				} catch (final InterruptedException ex) {
					return;
				}

				for (final WatchEvent<?> event : key.pollEvents()) {
					final WatchEvent.Kind<?> kind = event.kind();

					final WatchEvent<Path> ev = (WatchEvent<Path>) event;
					final Path fileName = ev.context();

					System.out.println(kind.name() + ": " + fileName);
					java.awt.EventQueue
					// i dont question the Java API, it works now.
					.invokeLater(() -> {

						final DefaultListModel playListModel = new DefaultListModel();

						final File[] playListFolder = new File(
								"data/playlist/").listFiles();
						if ((kind == ENTRY_CREATE)
								|| ((kind == ENTRY_DELETE)
										&& (playListModel != null) && (playListFolder != null))) {

							for (final File file : playListFolder) {
								playListModel.addElement(file);
							}
							displayList.setModel(playListModel);
							displayList.updateUI();
							System.out
							.println("Playlist change detected");
						}
					});

				}

				final boolean valid = key.reset();
				if (!valid) {
					break;
				}
			}

		} catch (final IOException ex) {
			System.err.println(ex);
		}

	}

	/**
	 * @author Andrew
	 *
	 *         Deletes line from PlayList
	 */
	public void removeLineFromPlayList(final String file,
			final String lineToRemove) {

		try {

			final File inFile = new File(file);

			if (!inFile.isFile()) {
				System.out.println("Parameter is not an existing file");
				return;
			}

			// Construct the new file that will later be renamed to the original
			// filename.
			final File tempFile = new File(inFile.getAbsolutePath() + ".tmp");

			final BufferedReader br = new BufferedReader(new FileReader(file));
			final PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

			String line = null;

			// Read from the original file and write to the new
			// unless content matches data to be removed.
			while ((line = br.readLine()) != null) {

				if (!line.contains(lineToRemove)) {

					pw.println(line);
					pw.flush();
				}
			}
			pw.close();
			br.close();

			// Delete the original file
			if (!inFile.delete()) {
				System.out.println("Could not delete file");
				return;
			}

			// Rename the new file to the filename the original file had.
			if (!tempFile.renameTo(inFile)) {
				System.out.println("Could not rename file");
			}
			// loadPlayList(PlayerUtils.currentPlayList);
		} catch (final FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (final IOException ex) {
			ex.printStackTrace();
		}
	}

	public PlaylistService getPlaylistService(String context) {
		for (PlaylistService service : platforms) {
			if (context.contains(service.getName())) {
				return service;
			}
		}
		return null;
	}
}
