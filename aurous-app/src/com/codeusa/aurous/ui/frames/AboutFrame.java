package com.codeusa.aurous.ui.frames;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window.Type;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import com.codeusa.aurous.utils.Utils;
import com.codeusa.aurous.utils.playlist.PlayListUtils;

/**
 * @author Andrew
 *
 */
public class AboutFrame {

	private static JFrame frmAbout;
	private JLabel titleLabel;
	private JLabel copyRightLabel;
	private JLabel aboutLabel;
	private JSeparator separator;
	private JLabel socialLabel;
	private JSeparator separator_1;
	private JButton blogButton;
	private JButton twitterButton;
	private JButton youTubeButton;
	private JButton faceBookButton;
	private JButton donateButton;
	private JButton fokMeButton;
	private JLabel andrewsAvatar;

	/**
	 * Launch the application.
	 */
	public static void showAbout() {
		if (PlayListUtils.aboutOpen == true) {
			frmAbout.toFront();
			frmAbout.repaint();
			return;
		}
		EventQueue.invokeLater(() -> {
			try {
				final AboutFrame window = new AboutFrame();
				AboutFrame.frmAbout.setVisible(true);
			} catch (final Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AboutFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAbout = new JFrame();
		frmAbout.setIconImage(Toolkit.getDefaultToolkit().getImage(
				AboutFrame.class.getResource("/resources/poptart.png")));
		frmAbout.setType(Type.UTILITY);
		frmAbout.setTitle("About");
		frmAbout.setResizable(false);
		frmAbout.setBounds(100, 100, 370, 400);
		frmAbout.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frmAbout.getContentPane().setLayout(null);
		frmAbout.getContentPane().setBackground(new Color(32, 33, 35));
		frmAbout.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(
					final java.awt.event.WindowEvent windowEvent) {

				PlayListUtils.aboutOpen = false;
				frmAbout.dispose();

			}
		});

		titleLabel = new JLabel("Aurous 2.1.6");
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		titleLabel.setBounds(90, 11, 202, 27);
		frmAbout.getContentPane().add(titleLabel);

		copyRightLabel = new JLabel("Copyright \u00A9 2014, Codeusa Software");
		copyRightLabel.setHorizontalAlignment(SwingConstants.CENTER);
		copyRightLabel.setForeground(Color.WHITE);
		copyRightLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		copyRightLabel.setBounds(58, 49, 247, 14);
		frmAbout.getContentPane().add(copyRightLabel);

		aboutLabel = new JLabel(
				"<html><center>Aurous is maintained by one mentally unstable guy with way too much ambition and a habit of premature optimization.<br>If you're interested in helping out check out the links below. </center></html>");
		aboutLabel.setForeground(Color.WHITE);
		aboutLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		aboutLabel.setHorizontalAlignment(SwingConstants.LEFT);
		aboutLabel.setBounds(10, 74, 344, 100);
		frmAbout.getContentPane().add(aboutLabel);

		separator = new JSeparator();
		separator.setBounds(10, 186, 344, 2);
		frmAbout.getContentPane().add(separator);

		socialLabel = new JLabel("Social");
		socialLabel.setHorizontalAlignment(SwingConstants.CENTER);
		socialLabel.setForeground(Color.WHITE);
		socialLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		socialLabel.setBounds(90, 245, 177, 27);
		frmAbout.getContentPane().add(socialLabel);

		separator_1 = new JSeparator();
		separator_1.setBounds(10, 283, 344, 2);
		frmAbout.getContentPane().add(separator_1);

		blogButton = new JButton();
		blogButton.addActionListener(e -> {
			try {
				Utils.openURL(new URL("http://blog.aurous.me/"));
			} catch (final Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		blogButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		blogButton.setBorderPainted(false);
		blogButton.setBorder(null);
		blogButton.setMargin(new Insets(0, 0, 0, 0));
		blogButton.setContentAreaFilled(false);
		blogButton.setIcon(new ImageIcon(AboutFrame.class
				.getResource("/resources/tumblr.png")));
		blogButton.setBounds(20, 296, 64, 64);
		frmAbout.getContentPane().add(blogButton);

		twitterButton = new JButton();
		twitterButton.addActionListener(e -> {
			try {
				Utils.openURL(new URL("http://twitter.com/codeusasoftware"));
			} catch (final Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		twitterButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		twitterButton.setIcon(new ImageIcon(AboutFrame.class
				.getResource("/resources/twitter.png")));
		twitterButton.setMargin(new Insets(0, 0, 0, 0));
		twitterButton.setContentAreaFilled(false);
		twitterButton.setBorderPainted(false);
		twitterButton.setBorder(null);
		twitterButton.setBounds(108, 296, 64, 64);
		frmAbout.getContentPane().add(twitterButton);

		youTubeButton = new JButton();
		youTubeButton.addActionListener(e -> {
			try {
				Utils.openURL(new URL("http://youtube.com/codeusasoftware"));
			} catch (final Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		youTubeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		youTubeButton.setIcon(new ImageIcon(AboutFrame.class
				.getResource("/resources/youtube.png")));
		youTubeButton.setMargin(new Insets(0, 0, 0, 0));
		youTubeButton.setContentAreaFilled(false);
		youTubeButton.setBorderPainted(false);
		youTubeButton.setBorder(null);
		youTubeButton.setBounds(195, 296, 64, 64);
		frmAbout.getContentPane().add(youTubeButton);

		faceBookButton = new JButton();
		faceBookButton.addActionListener(e -> {
			try {
				Utils.openURL(new URL("http://facebook.com/codeusasoftware"));
			} catch (final Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		faceBookButton
				.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		faceBookButton.setIcon(new ImageIcon(AboutFrame.class
				.getResource("/resources/facebook.png")));
		faceBookButton.setMargin(new Insets(0, 0, 0, 0));
		faceBookButton.setContentAreaFilled(false);
		faceBookButton.setBorderPainted(false);
		faceBookButton.setBorder(null);
		faceBookButton.setBounds(279, 296, 64, 64);
		frmAbout.getContentPane().add(faceBookButton);

		donateButton = new JButton();
		donateButton.addActionListener(e -> {
			try {
				Utils.openURL(new URL("http://blog.aurous.me/donate"));
			} catch (final Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		donateButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		donateButton.setIcon(new ImageIcon(AboutFrame.class
				.getResource("/resources/paypal.png")));
		donateButton.setMargin(new Insets(0, 0, 0, 0));
		donateButton.setContentAreaFilled(false);
		donateButton.setBorderPainted(false);
		donateButton.setFocusPainted(false);
		donateButton.setBorder(BorderFactory.createEmptyBorder());
		donateButton.setBounds(10, 199, 166, 33);
		frmAbout.getContentPane().add(donateButton);

		fokMeButton = new JButton();
		fokMeButton
				.addActionListener(e -> {
					try {
						Utils.openURL(new URL(
								"https://github.com/Codeusa/aurous-app"));
					} catch (final Exception e1) {
						// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		fokMeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		fokMeButton.setIcon(new ImageIcon(AboutFrame.class
				.getResource("/resources/btn_github_lg.png")));
		fokMeButton.setMargin(new Insets(0, 0, 0, 0));
		fokMeButton.setContentAreaFilled(false);
		fokMeButton.setBorderPainted(false);
		fokMeButton.setBorder(null);
		fokMeButton.setBounds(195, 199, 159, 33);
		frmAbout.getContentPane().add(fokMeButton);

		andrewsAvatar = new JLabel("");
		andrewsAvatar.setIcon(new ImageIcon(AboutFrame.class
				.getResource("/resources/andrew.jpg")));
		andrewsAvatar.setBounds(0, 0, 80, 63);
		frmAbout.getContentPane().add(andrewsAvatar);
		PlayListUtils.aboutOpen = true;
		frmAbout.setLocationRelativeTo(AurousFrame.jfxPanel);
	}
}
