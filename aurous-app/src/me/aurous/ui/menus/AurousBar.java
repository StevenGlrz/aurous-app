package me.aurous.ui.menus;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import me.aurous.player.Settings;
import me.aurous.tools.DiscoMixer;
import me.aurous.tools.PlayListBuilder;
import me.aurous.tools.PlayListImporter;
import me.aurous.ui.frames.AboutFrame;
import me.aurous.ui.frames.SettingsFrame;
import me.aurous.utils.playlist.PlayListUtils;

public class AurousBar extends JMenuBar {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3829530752614750645L;
	private JMenu fileMenu;
	private JMenuItem exitMenuItem;
  public AurousBar() {
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
		add(this.fileMenu);
		

		final JMenu playListMenu = new JMenu("Playlist");
		add(playListMenu);

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
		add(toolsMenu);

		final JMenuItem searchItem = new JMenuItem("Search");
		toolsMenu.add(searchItem);

		final JMenuItem discoItem = new JMenuItem("Disco Mode");
		discoItem.addActionListener(arg0 -> DiscoMixer.openDisco());
		toolsMenu.add(discoItem);

		final JMenu helpMenu = new JMenu("Help");
		add(helpMenu);

		final JMenuItem aboutItem = new JMenuItem("About");
		aboutItem.addActionListener(arg0 -> AboutFrame.showAbout());
		helpMenu.add(aboutItem);
		
  }
}
