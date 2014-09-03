package com.codeusa.poptart.tools;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window.Type;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.codeusa.poptart.swinghacks.GhostText;
import com.codeusa.poptart.ui.frames.PopTartFrame;
import com.codeusa.poptart.utils.playlist.PlayListUtils;

/**
 * @author Andrew
 *
 */
public class PlayListImporter {

	private static JFrame frmPlaylistImporter;
	private JTextField playListNameField;
	private String playListURL = "";
	private JButton redditServiceButton;
	public static JButton importPlayListButton;
	public static JProgressBar importProgressBar;
	public static JLabel importInstrucLabel;
	public static JLabel lblEnterAPlaylist;

	public static void openImporter() {
		if (PlayListUtils.importerOpen == true) {
			frmPlaylistImporter.toFront();
			frmPlaylistImporter.repaint();
			return;
		}
		EventQueue.invokeLater(() -> {
			try {
				final PlayListImporter window = new PlayListImporter();
				frmPlaylistImporter.setVisible(true);
			} catch (final Exception e) {
				e.printStackTrace();
			}
		});
	}

	public PlayListImporter() {
		initialize();
	}

	private void initialize() {
		frmPlaylistImporter = new JFrame();
		frmPlaylistImporter.setResizable(false);
		frmPlaylistImporter.setType(Type.UTILITY);
		frmPlaylistImporter.setIconImage(Toolkit.getDefaultToolkit().getImage(
				PlayListImporter.class.getResource("/resources/poptart.png")));
		frmPlaylistImporter.setTitle("Playlist Importer");
		frmPlaylistImporter.getContentPane().setBackground(
				new Color(32, 33, 35));
		frmPlaylistImporter.setBounds(100, 100, 379, 372);
		frmPlaylistImporter
				.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frmPlaylistImporter.getContentPane().setLayout(null);
		frmPlaylistImporter
				.addWindowListener(new java.awt.event.WindowAdapter() {
					@Override
					public void windowClosing(
							final java.awt.event.WindowEvent windowEvent) {
						final int confirm = JOptionPane
								.showOptionDialog(
										frmPlaylistImporter,
										"Are You Sure You Want to Close this Importer?",
										"Exit Confirmation",
										JOptionPane.YES_NO_OPTION,
										JOptionPane.QUESTION_MESSAGE, null,
										null, null);
						if (confirm == 0) {
							PlayListUtils.importerOpen = false;
							frmPlaylistImporter.dispose();
						}

					}
				});

		final JPanel servicePanel = new JPanel();
		servicePanel.setBackground(Color.DARK_GRAY);
		servicePanel.setBounds(0, 67, 373, 65);
		frmPlaylistImporter.getContentPane().add(servicePanel);
		servicePanel.setLayout(null);

		importProgressBar = new JProgressBar();
		importProgressBar.setVisible(false);
		importProgressBar.setStringPainted(true);
		importProgressBar.setBounds(0, 180, 373, 37);
		frmPlaylistImporter.getContentPane().add(importProgressBar);
		final JLabel playListURLLabel = new JLabel("");
		playListURLLabel.setForeground(Color.WHITE);
		playListURLLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		playListURLLabel.setBounds(223, 311, 150, 14);
		frmPlaylistImporter.getContentPane().add(playListURLLabel);

		final JButton youTubeServiceButton = new JButton("");
		youTubeServiceButton.addActionListener(e -> {

			PlayListImporter.this.playListURL = PlayListUtils
					.importPlayListPrompt();
			playListURLLabel.setText(PlayListImporter.this.playListURL);

		});

		youTubeServiceButton.setCursor(Cursor
				.getPredefinedCursor(Cursor.HAND_CURSOR));
		youTubeServiceButton.setBorderPainted(false);
		youTubeServiceButton.setBorder(null);
		youTubeServiceButton.setMargin(new Insets(0, 0, 0, 0));
		youTubeServiceButton.setContentAreaFilled(false);
		youTubeServiceButton.setIcon(new ImageIcon(PlayListImporter.class
				.getResource("/resources/w_youtube.png")));
		youTubeServiceButton.setRolloverIcon(new ImageIcon(
				PlayListImporter.class
						.getResource("/resources/w_youtube_h.png")));

		youTubeServiceButton.setBounds(138, 11, 35, 35);

		servicePanel.add(youTubeServiceButton);

		redditServiceButton = new JButton("");
		redditServiceButton.addActionListener(e -> {
			PlayListImporter.this.playListURL = PlayListUtils
					.importPlayListPrompt();
			playListURLLabel.setText(PlayListImporter.this.playListURL);

		});
		redditServiceButton.setCursor(Cursor
				.getPredefinedCursor(Cursor.HAND_CURSOR));
		redditServiceButton.setIcon(new ImageIcon(PlayListImporter.class
				.getResource("/resources/w_reddit.png")));
		redditServiceButton
				.setRolloverIcon(new ImageIcon(PlayListImporter.class
						.getResource("/resources/w_reddit_h.png")));
		redditServiceButton.setMargin(new Insets(0, 0, 0, 0));
		redditServiceButton.setContentAreaFilled(false);
		redditServiceButton.setBorderPainted(false);
		redditServiceButton.setBorder(null);
		redditServiceButton.setBounds(183, 11, 35, 35);
		servicePanel.add(redditServiceButton);

		final JLabel lblSelectAService = new JLabel("Select a Service");
		lblSelectAService.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		lblSelectAService.setHorizontalAlignment(SwingConstants.CENTER);
		lblSelectAService.setForeground(Color.WHITE);
		lblSelectAService.setBounds(54, 11, 251, 50);
		frmPlaylistImporter.getContentPane().add(lblSelectAService);

		this.playListNameField = new JTextField();
		this.playListNameField.setBorder(UIManager
				.getBorder("TextField.border"));
		this.playListNameField.setHorizontalAlignment(SwingConstants.CENTER);
		this.playListNameField.setFont(new Font("Segoe UI", Font.PLAIN, 23));
		this.playListNameField.setBounds(0, 180, 373, 37);
		frmPlaylistImporter.getContentPane().add(this.playListNameField);
		final GhostText ghostText = new GhostText("FMA OST",
				this.playListNameField);
		ghostText.setHorizontalAlignment(SwingConstants.CENTER);
		ghostText.setHorizontalTextPosition(SwingConstants.CENTER);
		this.playListNameField.setColumns(10);

		lblEnterAPlaylist = new JLabel("Enter a Playlist Name");
		lblEnterAPlaylist.setHorizontalAlignment(SwingConstants.CENTER);
		lblEnterAPlaylist.setForeground(Color.WHITE);
		lblEnterAPlaylist.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		lblEnterAPlaylist.setBounds(54, 133, 251, 50);
		frmPlaylistImporter.getContentPane().add(lblEnterAPlaylist);

		importPlayListButton = new JButton("");
		importPlayListButton.addActionListener(e -> {
			if (!PlayListImporter.this.playListURL.trim().isEmpty()
					&& !PlayListImporter.this.playListNameField.getText()
							.trim().isEmpty()) {

				importProgressBar.setVisible(true);

				PlayListUtils.getImportRules(this.playListURL,
						this.playListNameField.getText());

			} else {
				JOptionPane.showMessageDialog(frmPlaylistImporter,
						"No playlist url or name detected", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
		});
		importPlayListButton.setCursor(Cursor
				.getPredefinedCursor(Cursor.HAND_CURSOR));
		importPlayListButton.setBorderPainted(false);
		importPlayListButton.setBorder(null);
		importPlayListButton.setMargin(new Insets(0, 0, 0, 0));
		importPlayListButton.setContentAreaFilled(false);
		importPlayListButton.setIcon(new ImageIcon(PlayListImporter.class
				.getResource("/resources/ic_export_import.png")));
		importPlayListButton.setBounds(149, 261, 64, 64);
		frmPlaylistImporter.getContentPane().add(importPlayListButton);

		importInstrucLabel = new JLabel("Import Playlist");
		importInstrucLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		importInstrucLabel.setForeground(Color.WHITE);
		importInstrucLabel.setHorizontalAlignment(SwingConstants.CENTER);
		importInstrucLabel.setBounds(94, 228, 181, 22);
		frmPlaylistImporter.getContentPane().add(importInstrucLabel);
		frmPlaylistImporter.setLocationRelativeTo(PopTartFrame.jfxPanel);
		PlayListUtils.importerOpen = true;

	}
}
