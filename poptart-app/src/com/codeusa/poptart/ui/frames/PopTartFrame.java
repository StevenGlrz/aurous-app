package com.codeusa.poptart.ui.frames;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import com.codeusa.poptart.player.Settings;
import com.codeusa.poptart.player.scenes.MediaPlayerScene;
import com.codeusa.poptart.tools.DiscoMixer;
import com.codeusa.poptart.tools.PlayListBuilder;
import com.codeusa.poptart.tools.PlayListImporter;
import com.codeusa.poptart.ui.panels.AlbumArtPanel;
import com.codeusa.poptart.ui.panels.FunctionsPanel;
import com.codeusa.poptart.ui.panels.PlayListMetaPanel;
import com.codeusa.poptart.ui.panels.PlayListSelectPanel;
import com.codeusa.poptart.ui.panels.PlayListTablePanel;
import com.codeusa.poptart.ui.panels.PlayerControlPanel;
import com.codeusa.poptart.ui.panels.SongMetaPanel;
import com.codeusa.poptart.utils.Utils;
import com.codeusa.poptart.utils.media.MediaUtils;
import com.codeusa.poptart.utils.playlist.PlayListUtils;

/**
 * @author Andrew
 *
 */
public class PopTartFrame implements WindowListener {

	public JFrame popTartFrame;
	private JMenu fileMenu;
	public static JFXPanel jfxPanel;
	public static Scene scene;
	private JMenuItem exitMenuItem;

	/**
	 * Create the application.
	 */
	public PopTartFrame() {
		initialize();
		this.popTartFrame.addWindowListener(this);

	}

	private void initFX(final JFXPanel fxPanel) throws Throwable {
		// This method is invoked on the JavaFX thread
		scene = MediaPlayerScene
				.createScene("https://www.youtube.com/watch?v=kGubD7KG9FQ");

		fxPanel.setScene(scene);

	}

	/**
	 * Initialize the contents of the frame.
	 *
	 * @wbp.parser.entryPoint
	 */
	private void initialize() {
		if (com.sun.javafx.Utils.isMac()) {
			// take the menu bar off the jframe
			System.setProperty("apple.laf.useScreenMenuBar", "true");

			// set the name of the application menu item
			System.setProperty(
					"com.apple.mrj.application.apple.menu.about.name",
					"Project Poptart");
			final com.apple.eawt.Application macApp = com.apple.eawt.Application
					.getApplication();
			macApp.setDockIconImage(new ImageIcon(this.getClass().getResource(
					"/resources/poptart.png")).getImage());
		}
		this.popTartFrame = new JFrame();
		this.popTartFrame.setResizable(false);
		this.popTartFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(
				PopTartFrame.class.getResource("/resources/poptart.png")));
		this.popTartFrame.setTitle("Project Poptart");
		this.popTartFrame.getContentPane().setBackground(new Color(36, 35, 40));
		this.popTartFrame.setBounds(100, 100, 799, 600);
		this.popTartFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.popTartFrame.setBackground(new Color(37, 37, 37));
		this.popTartFrame.getContentPane().setLayout(null);

		final JPanel albumArtPanel = new AlbumArtPanel();
		albumArtPanel.setBackground(new Color(37, 37, 37));
		albumArtPanel.setBounds(0, 289, 200, 181);
		this.popTartFrame.getContentPane().add(albumArtPanel);
		albumArtPanel.setLayout(null);

		final JPanel songMetaPanel = new SongMetaPanel();
		songMetaPanel.setBackground(new Color(28, 29, 33));
		songMetaPanel.setBounds(0, 467, 200, 58);
		this.popTartFrame.getContentPane().add(songMetaPanel);

		try {
			final JPanel mediaControlsPanel = new PlayerControlPanel();
			mediaControlsPanel.setBackground(new Color(32, 33, 35));
			mediaControlsPanel.setBounds(0, 524, 828, 50);
			this.popTartFrame.getContentPane().add(mediaControlsPanel);

			jfxPanel = new JFXPanel();
			jfxPanel.setBounds(775, 34, 10, 10);
			jfxPanel.setVisible(false);
			mediaControlsPanel.add(jfxPanel);

		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		final JPanel playListPanel = new PlayListSelectPanel();
		playListPanel.setBackground(new Color(34, 35, 39));
		playListPanel.setForeground(Color.LIGHT_GRAY);
		playListPanel.setBounds(0, 65, 200, 220);
		this.popTartFrame.getContentPane().add(playListPanel);

		final JPanel playListMeta = new PlayListMetaPanel();
		playListMeta.setBackground(new Color(18, 19, 21));
		playListMeta.setBounds(200, 64, 603, 102);
		this.popTartFrame.getContentPane().add(playListMeta);

		final JMenuBar menuBar = new JMenuBar();
		menuBar.setForeground(Color.GRAY);
		menuBar.setBounds(0, 0, 791, 21);
		menuBar.setBackground(new Color(36, 35, 40));
		this.fileMenu = new JMenu("File");
		this.exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.addActionListener(e -> {
			Settings.saveSettings(false);
			System.exit(0);
		});

		final JMenuItem settingsItem = new JMenuItem("Settings");
		fileMenu.add(settingsItem);
		settingsItem.addActionListener(arg0 -> SettingsFrame.openSettings());
		this.fileMenu.add(this.exitMenuItem);
		// add menus to menubar
		menuBar.add(this.fileMenu);
		this.popTartFrame.getContentPane().add(menuBar);

		final JMenu playListMenu = new JMenu("Playlist");
		menuBar.add(playListMenu);

		final JMenuItem buildPlayListOption = new JMenuItem("Build Playlist");
		buildPlayListOption.addActionListener(arg0 -> PlayListBuilder
				.openBuilder());
		playListMenu.add(buildPlayListOption);

		final JMenuItem importPlayListOption = new JMenuItem("Import Playlist");
		importPlayListOption.addActionListener(arg0 -> PlayListImporter
				.openImporter());
		playListMenu.add(importPlayListOption);

		final JMenuItem importSingleItem = new JMenuItem(
				"Add to Current Playlist");
		importSingleItem.addActionListener(arg0 -> PlayListUtils
				.additionToPlayListPrompt());
		playListMenu.add(importSingleItem);

		final JMenu toolsMenu = new JMenu("Tools");
		menuBar.add(toolsMenu);

		final JMenuItem searchItem = new JMenuItem("Search");
		toolsMenu.add(searchItem);

		final JMenuItem discoItem = new JMenuItem("Disco Mode");
		discoItem.addActionListener(arg0 -> DiscoMixer.openDisco());
		toolsMenu.add(discoItem);

		final JMenu helpMenu = new JMenu("Help");
		menuBar.add(helpMenu);

		final JMenuItem aboutItem = new JMenuItem("About");
		aboutItem.addActionListener(arg0 -> AboutFrame.showAbout());
		helpMenu.add(aboutItem);

		try {
			final JPanel functionsPanel = new FunctionsPanel();
			functionsPanel.setBackground(new Color(31, 30, 35));
			functionsPanel.setBounds(0, 21, 803, 43);
			this.popTartFrame.getContentPane().add(functionsPanel);
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Settings.loadSettings();

		final JPanel loadedPlayListPanel = new PlayListTablePanel();
		loadedPlayListPanel.setBorder(null);
		loadedPlayListPanel.setBackground(new Color(37, 37, 37));
		loadedPlayListPanel.setBounds(200, 163, 597, 362);

		this.popTartFrame.getContentPane().add(loadedPlayListPanel);
		// Create columns names
		Platform.runLater(() -> {
			try {

				initFX(jfxPanel);
			} catch (final Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		Utils.centerFrameOnMainDisplay(this.popTartFrame);

		this.popTartFrame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(
					final java.awt.event.WindowEvent windowEvent) {
				Settings.saveSettings(false);

			}
		});
		/*
		 * final JGoogleAnalyticsTracker tracker = new JGoogleAnalyticsTracker(
		 * "Project Poptart App", "2.0.0", "UA-53956512-1");
		 * 
		 * final FocusPoint focusPoint = new FocusPoint("AppStart");
		 * 
		 * tracker.trackAsynchronously(focusPoint);
		 */
	}

	@Override
	public void windowOpened(final WindowEvent e) {
		MediaUtils.isOutOfFocus = false;

	}

	@Override
	public void windowClosing(final WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(final WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(final WindowEvent e) {
		MediaUtils.isOutOfFocus = true;

	}

	@Override
	public void windowDeiconified(final WindowEvent e) {
		MediaUtils.isOutOfFocus = false;

	}

	@Override
	public void windowActivated(final WindowEvent e) {
		MediaUtils.isOutOfFocus = false;

	}

	@Override
	public void windowDeactivated(final WindowEvent e) {
		MediaUtils.isOutOfFocus = true;

	}
}
