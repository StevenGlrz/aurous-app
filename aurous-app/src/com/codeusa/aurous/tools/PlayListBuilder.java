package com.codeusa.aurous.tools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Window.Type;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Element;

import com.codeusa.aurous.swinghacks.GhostText;
import com.codeusa.aurous.ui.frames.AurousFrame;
import com.codeusa.aurous.ui.listeners.ContextMenuMouseListener;
import com.codeusa.aurous.utils.playlist.PlayListUtils;

/**
 * @author Andrew
 *
 */
public class PlayListBuilder {
	public static JTextArea playListTextArea;
	private static JTextArea lines;
	private static JFrame frame;
	private boolean newLining = false;
	public static JButton buildListButton;
	public static JTextField playListNameTextField;
	private final String ln = System.getProperty("line.separator");
	public static JLabel loadingIcon;

	/**
	 * @wbp.parser.entryPoint
	 */
	public PlayListBuilder() {

		PlayListBuilder.frame = new JFrame();
		PlayListBuilder.frame.getContentPane().setBackground(
				new Color(32, 33, 35));
		PlayListBuilder.frame.setIconImage(Toolkit.getDefaultToolkit()
				.getImage(
						PlayListBuilder.class
								.getResource("/resources/poptart.png")));
		PlayListBuilder.frame.setType(Type.UTILITY);

		PlayListBuilder.frame.setResizable(false);
		PlayListBuilder.frame.setTitle("Playlist Builder");
		PlayListBuilder.frame.getContentPane().setLayout(null);
		PlayListBuilder.frame.setPreferredSize(new Dimension(400, 545));
		PlayListBuilder.frame
				.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		loadingIcon = new JLabel("");

		loadingIcon.setBackground(Color.WHITE);
		loadingIcon.setHorizontalAlignment(SwingConstants.CENTER);
		loadingIcon.setIcon(new ImageIcon(PlayListBuilder.class
				.getResource("/resources/loading1.gif")));
		loadingIcon.setBounds(0, 0, 394, 516);
		loadingIcon.setVisible(false);

		frame.getContentPane().add(loadingIcon);
		final JScrollPane builderScrollPane = new JScrollPane();
		builderScrollPane.setPreferredSize(new Dimension(400, 530));
		builderScrollPane.setBounds(0, 84, 394, 339);
		PlayListBuilder.frame
				.addWindowListener(new java.awt.event.WindowAdapter() {
					@Override
					public void windowClosing(
							final java.awt.event.WindowEvent windowEvent) {
						final int confirm = JOptionPane.showOptionDialog(
								PlayListBuilder.frame,
								"Are You Sure You Want to Close this Builder?",
								"Exit Confirmation", JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, null, null);
						if (confirm == 0) {
							PlayListUtils.builderOpen = false;
							PlayListBuilder.frame.dispose();
						}

					}
				});
		playListTextArea = new JTextArea();
		playListTextArea.setBackground(Color.DARK_GRAY);
		playListTextArea.setForeground(Color.WHITE);
		playListTextArea.setFont(new Font("Consolas", Font.PLAIN, 13));
		playListTextArea.addMouseListener(new ContextMenuMouseListener());
		playListTextArea.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent e) {

			}
		});
		playListTextArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(final KeyEvent e) {
				final int c = e.getKeyCode();
				if (c == KeyEvent.VK_PASTE) {

					System.out.println("dadada");

				}
			}
		});
		lines = new JTextArea("1");
		lines.setForeground(Color.WHITE);

		lines.setBackground(Color.LIGHT_GRAY);
		lines.setEditable(false);

		playListTextArea.getDocument().addDocumentListener(
				new DocumentListener() {
					public String getText() {
						final int caretPosition = playListTextArea
								.getDocument().getLength();
						final Element root = playListTextArea.getDocument()
								.getDefaultRootElement();
						String text = "1"
								+ System.getProperty("line.separator");
						for (int i = 2; i < (root
								.getElementIndex(caretPosition) + 2); i++) {
							text += i + System.getProperty("line.separator");
						}
						return text;
					}

					@Override
					public void changedUpdate(final DocumentEvent de) {
						lines.setText(getText());
					}

					@Override
					public void insertUpdate(final DocumentEvent de) {
						PlayListBuilder.this.newLining = false;
						SwingUtilities.invokeLater(() -> {
							if (PlayListBuilder.this.newLining == false) {
								playListTextArea
								.append(PlayListBuilder.this.ln);
								lines.setText(getText());
								PlayListBuilder.this.newLining = true;
							}
						});

					}

					@Override
					public void removeUpdate(final DocumentEvent de) {
						lines.setText(getText());

					}

				});
		PlayListBuilder.frame.getContentPane().setLayout(null);

		builderScrollPane.setViewportView(playListTextArea);
		builderScrollPane.setRowHeaderView(lines);
		builderScrollPane
		.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		PlayListBuilder.frame.getContentPane().add(builderScrollPane);

		final JLabel instructionsLabel = new JLabel(
				"<html><body>Paste the links of the songs you want to import<br>Make sure each one is on a new line like so: <br>https://www.youtube.com/watch?v=lqY4jkWCmKY<br>https://www.youtube.com/watch?v=hH9Y9SPZYTI</br></body></html>");
		instructionsLabel.setForeground(Color.WHITE);
		instructionsLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
		instructionsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		instructionsLabel.setBounds(57, 0, 285, 82);
		PlayListBuilder.frame.getContentPane().add(instructionsLabel);

		final JLabel enterPlayListNameLabel = new JLabel("Enter Playlist Name:");
		enterPlayListNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		enterPlayListNameLabel.setForeground(Color.WHITE);
		enterPlayListNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		enterPlayListNameLabel.setBounds(24, 423, 343, 23);
		PlayListBuilder.frame.getContentPane().add(enterPlayListNameLabel);

		PlayListBuilder.playListNameTextField = new JTextField();
		playListNameTextField.setHorizontalAlignment(SwingConstants.CENTER);
		playListNameTextField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
		PlayListBuilder.playListNameTextField.setColumns(10);
		PlayListBuilder.playListNameTextField.setBounds(0, 449, 394, 33);
		PlayListBuilder.frame.getContentPane().add(
				PlayListBuilder.playListNameTextField);

		buildListButton = new JButton("build");
		buildListButton.setForeground(Color.WHITE);
		buildListButton.setBackground(Color.DARK_GRAY);
		buildListButton
		.addActionListener(e -> {
			if (playListTextArea.getText().trim().isEmpty()) {
				JOptionPane.showMessageDialog(frame,
						"You must add links to build a playlist",
						"Error", JOptionPane.ERROR_MESSAGE);
				return;
			} else if (PlayListBuilder.playListNameTextField.getText()
					.trim().isEmpty()) {
				JOptionPane.showMessageDialog(frame,
						"Please enter a name for your playlist",
						"Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			loadingIcon.setVisible(true);
			playListTextArea.setEditable(false);
			buildListButton.setEnabled(false);
			playListNameTextField.setEditable(false);
			final String items = playListTextArea.getText();

			PlayListUtils.buildPlayList(items,
					playListNameTextField.getText());

		});
		buildListButton.setBounds(147, 493, 89, 23);

		PlayListBuilder.frame.getContentPane().add(buildListButton);

		PlayListBuilder.frame.pack();

		PlayListBuilder.frame.setVisible(true);
		PlayListUtils.builderOpen = true;
		final GhostText ghostText = new GhostText("FMA OST",
				PlayListBuilder.playListNameTextField);
		ghostText.setHorizontalAlignment(SwingConstants.CENTER);
		ghostText.setHorizontalTextPosition(SwingConstants.CENTER);
		PlayListBuilder.frame.setLocationRelativeTo(AurousFrame.jfxPanel);
	}

	public static void openBuilder() {
		if (PlayListUtils.builderOpen == true) {
			frame.toFront();
			frame.repaint();
			return;
		}
		EventQueue.invokeLater(() -> {
			try {
				final PlayListBuilder window = new PlayListBuilder();
				PlayListBuilder.frame.setVisible(true);

			} catch (final Exception e) {
				e.printStackTrace();
			}
		});
	}
}