package com.codeusa.aurous.ui.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import com.codeusa.aurous.player.Settings;
import com.codeusa.aurous.player.functions.PlayerFunctions;
import com.codeusa.aurous.ui.frames.SettingsFrame;
import com.codeusa.aurous.utils.Images;
import com.codeusa.aurous.utils.Internet;
import com.codeusa.aurous.utils.ModelUtils;
import com.codeusa.aurous.utils.playlist.PlayListUtils;

/**
 * @author Andrew
 *
 */
public class PlayListPanel extends JPanel implements ActionListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 7941760374900934885L;
	public static boolean canSetLast = false;
	private final Color background = new Color(34, 35, 39);
	JPopupMenu popup;
	private static JLabel albumArtLabel;
	private static JLabel songInformation;

	public static class MyCellRenderer extends DefaultListCellRenderer {

		/**
		 *
		 */
		private static final long serialVersionUID = 1976041664275487088L;

		@Override
		public Component getListCellRendererComponent(final JList<?> list,
				final Object value, final int index, final boolean isSelected,
				final boolean cellHasFocus) {
			if (value instanceof File) {

				final File file = (File) value;

				setText(file.getName().replace(".plist", ""));
				setIcon(new ImageIcon(
						PlayListPanel.class.getResource("/resources/music.png")));

				if (isSelected) {
					setBackground(list.getSelectionBackground());
					setForeground(list.getSelectionForeground());
				} else {
					setBackground(list.getBackground());
					setForeground(list.getForeground());
				}
				setEnabled(list.isEnabled());
				setFont(list.getFont());
				setOpaque(true);
				if (file.getName().contains("blank.plist")) {

				}
			}
			return this;
		}
	}

	// public static void run() {

	// EventQueue.invokeLater(() -> new PlayListPanel());

	// }

	// private final PlayListFunctions plFunctions = new PlayListFunctions();

	public PlayListPanel() {
		setAlignmentX(Component.LEFT_ALIGNMENT);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		setBorder(new EtchedBorder());

		setBackground(background);
		setPreferredSize(new Dimension(200, getHeight()));

		final JList<?> displayList = new JList<Object>(new File(
				"data/playlist/").listFiles());

		displayList.setBackground(background);
		displayList.setForeground(Color.WHITE);
		displayList.setBorder(new EmptyBorder(5, 5, 5, 5));

		displayList
		.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		displayList.setCellRenderer(new MyCellRenderer());
		displayList.setLayoutOrientation(javax.swing.JList.HORIZONTAL_WRAP);
		displayList.setName("displayList");
		displayList.setVisibleRowCount(-1);
		displayList.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(final KeyEvent e) {
				final int c = e.getKeyCode();
				if (c == KeyEvent.VK_DELETE) {

					PlayListUtils.deletePlayList(displayList);

				} else if (c == KeyEvent.VK_ADD) {

				} else if (c == KeyEvent.VK_LEFT) {

				} else if (c == KeyEvent.VK_RIGHT) {

				} else if (c == KeyEvent.VK_ENTER) {

				}
			}
		});
		final MouseListener mouseListener = new MouseAdapter() {
			@Override
			public void mouseReleased(final MouseEvent e) {
				if (e.isPopupTrigger()) {

					final JList<?> list = (JList<?>) e.getSource();
					list.setSelectedIndex(list.locationToIndex(e.getPoint()));

					popup.show(e.getComponent(), e.getX(), e.getY());
				}
			}

			@Override
			public void mouseClicked(final MouseEvent mouseEvent) {
				if (mouseEvent.getClickCount() == 2) {
					final int index = displayList.locationToIndex(mouseEvent
							.getPoint());
					if (index >= 0) {
						final Object o = displayList.getModel().getElementAt(
								index);
						// PlayListFrame.this.plFunctions.loadPlayList(o
						// .toString());
						final String playlist = o.toString();
						ModelUtils.loadPlayList(playlist);
						if (canSetLast == true) {
							canSetLast = false;
							Settings.setLastPlayList(playlist);

						}

						System.out.println("Dhouble-clicked on: " + playlist);
					}
				}
			}
		};

		final JScrollPane scrollPane = new JScrollPane(displayList);
		scrollPane
		.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(new Dimension(200, getHeight()));
		scrollPane.setBorder(null);

		albumArtLabel = new JLabel();

		albumArtLabel.setBorder(new EtchedBorder());
		albumArtLabel.setBorder(BorderFactory.createEmptyBorder());
		albumArtLabel.setHorizontalAlignment(SwingConstants.LEFT);
		setAlbumArt(new ImageIcon(SettingsFrame.class
				.getResource("/resources/album-placeholder.png")).getImage());

		songInformation = new JLabel();
		songInformation.setHorizontalAlignment(SwingConstants.LEFT);
		songInformation.setForeground(Color.WHITE);

		add(scrollPane);

		add(Box.createRigidArea(new Dimension(0, 5)));

		add(albumArtLabel);
		add(songInformation);

		popup = new JPopupMenu();
		final JMenuItem playItem = new JMenuItem("Play");
		playItem.addActionListener(this);
		popup.add(playItem);
		final JMenuItem loadItem = new JMenuItem("Load");
		loadItem.addActionListener(this);
		popup.add(loadItem);
		final JMenuItem deleteItem = new JMenuItem("Delete");
		deleteItem.addActionListener(this);
		popup.add(deleteItem);
		final JMenuItem shareItem = new JMenuItem("Share");
		shareItem.addActionListener(this);
		popup.add(shareItem);

		displayList.addMouseListener(mouseListener);
		final Thread thread = new Thread(
				() -> PlayListUtils.watchPlayListDirectory(displayList));
		// start the thread
		thread.start();

	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		final Component c = (Component) e.getSource();
		final JPopupMenu popup = (JPopupMenu) c.getParent();
		final JList<?> list = (JList<?>) popup.getInvoker();
		final Object o = list.getSelectedValue();
		final String playlist = o.toString();
		switch (e.getActionCommand()) {
		case "Delete":
			PlayListUtils.deletePlayList(list);
			break;
		case "Play":
			ModelUtils.loadPlayList(playlist);
			PlayerFunctions.seekNext();
			break;
		case "Load":
			ModelUtils.loadPlayList(playlist);

			break;
		case "Share":
			// System.out.println("Sharing");
			break;
		}
		// System.out.println(table.getSelectedRow() + " : " +
		// table.getSelectedColumn());
	}

	public static void setAlbumArt(final Image image) {
		albumArtLabel.setIcon(new ImageIcon(scale(image, 200, 200)));
	}

	public static void setSongInformation(final String title,
			final String artist) {
		final String information = String.format(
				"<html><strong>%s</strong><br>%s</html>", title, artist);
		songInformation.setText(information);
	}

	private static Image scale(final Image image, final int width,
			final int height) {
		return image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
	}

}