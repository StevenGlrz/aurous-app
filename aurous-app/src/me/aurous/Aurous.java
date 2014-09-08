package me.aurous;

import java.awt.EventQueue;
import java.text.ParseException;

import javax.swing.UnsupportedLookAndFeelException;

import me.aurous.ui.UISession;
import me.aurous.ui.frames.AurousFrame;
import de.javasoft.plaf.synthetica.SyntheticaAluOxideLookAndFeel;

/**
 * @author Andrew
 *
 */
public class Aurous {

	public static void main(final String[] args) {
		try {
			javax.swing.UIManager
					.setLookAndFeel(new SyntheticaAluOxideLookAndFeel());
		} catch (UnsupportedLookAndFeelException | ParseException e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(() -> {
			try {
				final AurousFrame window = new AurousFrame();
				UISession.setPresenter(window);
				window.aurousFrame.setVisible(true);

			} catch (final Exception e) {
				e.printStackTrace();
			}
		});

	}

}
