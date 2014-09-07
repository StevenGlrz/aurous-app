package com.codeusa.aurous;

import java.awt.EventQueue;
import java.text.ParseException;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.codeusa.aurous.ui.frames.AurousFrame;

import de.javasoft.plaf.synthetica.SyntheticaAluOxideLookAndFeel;

/**
 * @author Andrew
 *
 */
public class Aurous {
//legacy
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
				final AurousFrame window = new AurousFrame();
				window.aurousFrame.setVisible(true);

			} catch (final Exception e) {
				e.printStackTrace();
			}
		});

	}

}
