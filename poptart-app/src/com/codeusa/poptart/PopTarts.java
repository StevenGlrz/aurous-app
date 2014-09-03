package com.codeusa.poptart;

import java.awt.EventQueue;
import java.text.ParseException;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.codeusa.poptart.ui.frames.PopTartFrame;

import de.javasoft.plaf.synthetica.SyntheticaAluOxideLookAndFeel;

/**
 * @author Andrew
 *
 */
public class PopTarts {

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
				final PopTartFrame window = new PopTartFrame();
				window.popTartFrame.setVisible(true);

			} catch (final Exception e) {
				e.printStackTrace();
			}
		});

	}

}
