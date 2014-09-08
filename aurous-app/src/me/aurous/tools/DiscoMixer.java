package me.aurous.tools;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Window.Type;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import me.aurous.swinghacks.GhostText;
import me.aurous.ui.UISession;
import me.aurous.utils.playlist.PlayListUtils;
import me.aurous.utils.playlist.YouTubeDiscoUtils;

/**
 * @author Andrew
 *
 */
public class DiscoMixer {

	public static JFrame discoFrame;
	public static JTextField queryField;
	public static JProgressBar discoProgressBar;
	public static JButton discoBuildButton;
	public static JButton top100Button;

	/**
	 * Launch the application.
	 */
	public static void openDisco() {
		if (PlayListUtils.discoOpen == true) {
			discoFrame.toFront();
			discoFrame.repaint();
			return;
		}
		EventQueue.invokeLater(() -> {
			try {
				final DiscoMixer window = new DiscoMixer();
				DiscoMixer.discoFrame.setVisible(true);

			} catch (final Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Create the application.
	 */
	public DiscoMixer() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		discoFrame = new JFrame();
		discoFrame.setTitle("Disco Mixer");
		discoFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(
				DiscoMixer.class.getResource("/resources/aurouslogo.png")));
		discoFrame.setType(Type.UTILITY);
		discoFrame.setResizable(false);
		discoFrame.setBounds(100, 100, 606, 239);
		discoFrame
		.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		discoFrame.getContentPane().setLayout(null);
		discoFrame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(
					final java.awt.event.WindowEvent windowEvent) {
				final int confirm = JOptionPane.showOptionDialog(discoFrame,
						"Are You Sure You Want to Close Disco Mixer?",
						"Exit Confirmation", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, null, null);
				if (confirm == 0) {
					PlayListUtils.discoOpen = false;
					discoFrame.dispose();
				}

			}
		});

		final JLabel logoLabel = new JLabel("");
		logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		logoLabel.setIcon(new ImageIcon(DiscoMixer.class
				.getResource("/resources/fmw.png")));
		logoLabel.setBounds(10, 0, 580, 70);
		discoFrame.getContentPane().add(logoLabel);

		discoProgressBar = new JProgressBar();
		discoProgressBar.setStringPainted(true);
		discoProgressBar.setBounds(113, 119, 380, 49);
		discoProgressBar.setVisible(false);
		discoFrame.getContentPane().add(discoProgressBar);

		queryField = new JTextField();
		queryField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		queryField.setHorizontalAlignment(SwingConstants.CENTER);
		queryField.setBounds(113, 119, 380, 44);
		discoFrame.getContentPane().add(queryField);
		queryField.setColumns(10);

		final JLabel instructionsLabel = new JLabel(
				"Enter an Artist, Song or Choose from the Top 100!");
		instructionsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		instructionsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		instructionsLabel.setBounds(23, 81, 541, 27);
		discoFrame.getContentPane().add(instructionsLabel);

		discoBuildButton = new JButton("Disco!");
		discoBuildButton.addActionListener(e -> {
			if (!queryField.getText().trim().isEmpty()) {
				discoProgressBar.setVisible(true);
				YouTubeDiscoUtils.buildDiscoPlayList(queryField.getText());
			} else {
				JOptionPane.showMessageDialog(discoFrame,
						"Please enter search query", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
		});
		discoBuildButton.setForeground(Color.BLACK);
		discoBuildButton.setBounds(197, 174, 100, 26);
		discoFrame.getContentPane().add(discoBuildButton);

		top100Button = new JButton("Top Hits!");
		top100Button.addActionListener(e -> {
			discoProgressBar.setVisible(true);
			YouTubeDiscoUtils.buildTopPlayList();
		});
		top100Button.setForeground(Color.BLACK);
		top100Button.setBounds(307, 174, 100, 26);
		discoFrame.getContentPane().add(top100Button);
		PlayListUtils.discoOpen = true;
		final GhostText ghostText = new GhostText("Ghost B.C.", queryField);
		ghostText.setHorizontalAlignment(SwingConstants.CENTER);
		discoFrame.setLocationRelativeTo(UISession.getPresenter().jfxPanel);
	}
}
