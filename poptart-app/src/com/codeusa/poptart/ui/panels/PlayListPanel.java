package com.codeusa.poptart.ui.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import com.codeusa.poptart.player.Settings;
import com.codeusa.poptart.player.functions.PlayerFunctions;
import com.codeusa.poptart.utils.ModelUtils;
import com.codeusa.poptart.utils.playlist.PlayListUtils;

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
	JPopupMenu popup;

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
						FunctionsPanel.class
								.getResource("/resources/music.png")));

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
		setLayout(null);

		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 200, 220);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.setViewportBorder(null);
		scrollPane
		.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane
		.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		final JList<?> displayList = new JList<Object>(new File(
				"data/playlist/").listFiles());

		displayList.setForeground(Color.WHITE);
		displayList.setBackground(new Color(34, 35, 39));

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
		scrollPane.getViewport().setBackground((new Color(32, 33, 35)));
		displayList.addMouseListener(mouseListener);
		scrollPane.setViewportView(displayList);
		final Thread thread = new Thread(
				() -> PlayListUtils.watchPlayListDirectory(displayList));
		// start the thread
		thread.start();

		add(scrollPane);
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

}