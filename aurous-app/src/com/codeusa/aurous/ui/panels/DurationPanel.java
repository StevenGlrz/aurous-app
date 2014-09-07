package com.codeusa.aurous.ui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI;

import com.codeusa.aurous.player.functions.PlayerFunctions;
import com.codeusa.aurous.player.scenes.MediaPlayerScene;

public class DurationPanel extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 8242889578389979949L;
	public String current, maximum;
	public JSlider seek;

	private JLabel currentTime, maximumTime;

	public DurationPanel() {

		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		current = "0:00";
		maximum = "0:00";
		currentTime = new JLabel(current);
		currentTime.setForeground(Color.WHITE);
		maximumTime = new JLabel(maximum);
		maximumTime.setForeground(Color.WHITE);

		seek = new JSlider() {
			private static final long serialVersionUID = -4931644654633925931L;

			{
				final MouseListener[] listeners = getMouseListeners();
				for (final MouseListener l : listeners) {
					removeMouseListener(l); // remove UI-installed TrackListener
				}
				final BasicSliderUI ui = (BasicSliderUI) getUI();
				final BasicSliderUI.TrackListener tl = ui.new TrackListener() {
					// this is where we jump to absolute value of click
					@Override
					public void mouseClicked(final MouseEvent e) {
						final Point p = e.getPoint();
						final int value = ui.valueForXPosition(p.x);

						setValue(value);
						System.out.println(value);
						PlayerFunctions.seek(value);
					}

					// disable check that will invoke scrollDueToClickInTrack
					@Override
					public boolean shouldScroll(final int dir) {
						return false;
					}
				};
				addMouseListener(tl);
			}
		};

		seek.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(final MouseEvent e) {
				if (MediaPlayerScene.player != null) {
					PlayerFunctions.seek(seek.getValue());
				}
			}
		});
		seek.setPreferredSize(new Dimension(250, 25));
		setOpaque(false);
		add(currentTime);
		add(Box.createRigidArea(new Dimension(3, 0)));
		add(seek);
		add(Box.createRigidArea(new Dimension(3, 0)));
		add(maximumTime);
		add(Box.createRigidArea(new Dimension(5, 0)));
	}

	public JLabel current() {
		return currentTime;
	}

	public JLabel total() {
		return maximumTime;
	}

	public JSlider seek() {
		return seek;
	}

}
