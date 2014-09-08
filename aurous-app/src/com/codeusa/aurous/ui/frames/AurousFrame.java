package com.codeusa.aurous.ui.frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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

import com.boxysystems.jgoogleanalytics.FocusPoint;
import com.boxysystems.jgoogleanalytics.JGoogleAnalyticsTracker;
import com.codeusa.aurous.player.Settings;
import com.codeusa.aurous.player.scenes.MediaPlayerScene;
import com.codeusa.aurous.tools.DiscoMixer;
import com.codeusa.aurous.tools.PlayListBuilder;
import com.codeusa.aurous.tools.PlayListImporter;
import com.codeusa.aurous.ui.panels.ControlPanel;
import com.codeusa.aurous.ui.panels.PlayListPanel;
import com.codeusa.aurous.ui.panels.TabelPanel;
import com.codeusa.aurous.utils.Utils;
import com.codeusa.aurous.utils.media.MediaUtils;
import com.codeusa.aurous.utils.playlist.PlayListUtils;

/**
 * @author Andrew
 *
 */
public class AurousFrame implements WindowListener {

	public JFrame aurousFrame;
	private JMenu fileMenu;
	public static JFXPanel jfxPanel;
	public static Scene scene;
	private JMenuItem exitMenuItem;

	/**
	 * Create the application.
	 */
	public AurousFrame() {
		initialize();
		this.aurousFrame.addWindowListener(this);

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
					"Aurous");
			final com.apple.eawt.Application macApp = com.apple.eawt.Application
					.getApplication();
			macApp.setDockIconImage(new ImageIcon(this.getClass().getResource(
					"/resources/aurouslogo.png")).getImage());
		}
		this.aurousFrame = new JFrame();
		this.aurousFrame.setResizable(true);
		this.aurousFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(
				AurousFrame.class.getResource("/resources/aurouslogo.png")));
		this.aurousFrame.setTitle("Aurous");
		this.aurousFrame.getContentPane().setBackground(new Color(36, 35, 40));
		this.aurousFrame.setSize(new Dimension(800, 600));
		this.aurousFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.aurousFrame.getContentPane().setLayout(new BorderLayout());

		try {
			final JPanel mediaControlsPanel = new ControlPanel();
			this.aurousFrame.getContentPane().add(mediaControlsPanel,
					BorderLayout.SOUTH);

			jfxPanel = new JFXPanel();

			jfxPanel.setVisible(false);
			jfxPanel.setPreferredSize(new Dimension(1, 1));
			jfxPanel.setMaximumSize(new Dimension(1, 1));
			mediaControlsPanel.add(jfxPanel);

		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		final JPanel playListPanel = new PlayListPanel();
		this.aurousFrame.getContentPane()
		.add(playListPanel, BorderLayout.WEST);

		final JMenuBar menuBar = new JMenuBar();
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
		this.aurousFrame.getContentPane().add(menuBar);

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
		this.aurousFrame.setJMenuBar(menuBar);

		final JPanel tabelPanel = new TabelPanel();
		this.aurousFrame.getContentPane().add(tabelPanel, BorderLayout.CENTER);

		// Create columns names
		Platform.runLater(() -> {
			try {

				initFX(jfxPanel);
			} catch (final Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		Utils.centerFrameOnMainDisplay(this.aurousFrame);

		this.aurousFrame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(
					final java.awt.event.WindowEvent windowEvent) {
				Settings.saveSettings(false);

			}
		});
		
		 final JGoogleAnalyticsTracker tracker = new JGoogleAnalyticsTracker(
		  "Aurous App", "2.1.6", "UA-53956512-1");
		 
		 final FocusPoint focusPoint = new FocusPoint("AppStart");
		  
		 tracker.trackAsynchronously(focusPoint);
		 
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
