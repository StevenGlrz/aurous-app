package com.codeusa.aurous.updater;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Window.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import com.sun.javafx.Utils;

import de.javasoft.plaf.synthetica.SyntheticaAluOxideLookAndFeel;

public class UpdaterFrame {

	/**
	 * Launch the application.
	 */
	public static void main(final String[] args) {
		try {
			UIManager.setLookAndFeel(new SyntheticaAluOxideLookAndFeel());
		} catch (final UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		} catch (final ParseException e1) {
			e1.printStackTrace();
		}
		EventQueue.invokeLater(() -> {
			try {
				final UpdaterFrame window = new UpdaterFrame();
				window.updateFrame.setVisible(true);
			} catch (final Exception e) {
				e.printStackTrace();
			}
		});
	}

	UpdateManager dlManager;

	GlobalUtils gUtils = new GlobalUtils();
	private JFrame updateFrame;

	/**
	 * Create the application.
	 */
	public UpdaterFrame() {
		this.initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		if (Utils.isMac()) {
			System.setProperty("java.awt.headless", "true");
			// take the menu bar off the jframe
			System.setProperty("apple.laf.useScreenMenuBar", "true");

			// set the name of the application menu item
			System.setProperty(
					"com.apple.mrj.application.apple.menu.about.name",
					"Aurous Updater");
		}
		this.updateFrame = new JFrame();
		this.updateFrame.setResizable(false);
		this.updateFrame.setTitle("Grabbing Some Breakfast...");
		this.updateFrame.setType(Type.UTILITY);
		this.updateFrame.setBounds(100, 100, 450, 107);
		this.updateFrame
		.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.updateFrame.getContentPane().setLayout(null);

		final JProgressBar progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		progressBar.setMaximum(100);
		progressBar.setFont(new Font("Lucida Console", Font.BOLD, 11));
		progressBar.setStringPainted(true);
		progressBar.setValue(0);
		progressBar.setBounds(0, 0, 444, 78);
		this.gUtils.centerFrameOnMainDisplay(this.updateFrame);
		this.updateFrame.getContentPane().add(progressBar);
		if ((this.gUtils.checkForAppUpdates() == false)
				&& (this.gUtils.checkForScriptUpdates() == false)) {
			this.updateFrame.setVisible(false);
			this.gUtils.runPopTartTime();
			return;
		}
		if (this.gUtils.checkForScriptUpdates() == true) {
			try {
				this.dlManager = new UpdateManager(this.updateFrame, new URL(
						"http://codeusa.net/apps/poptart/decrypt.zip"),
						"data/scripts/", progressBar);

			} catch (final MalformedURLException e) {
				e.printStackTrace();
			}

		}
		if (this.gUtils.checkForAppUpdates() == true) {
			try {
				this.dlManager = new UpdateManager(this.updateFrame, new URL(
						"http://codeusa.net/apps/poptart/app.zip"), "updates/",
						progressBar);
			} catch (final MalformedURLException e) {
				e.printStackTrace();
			}
		}
	}
}
