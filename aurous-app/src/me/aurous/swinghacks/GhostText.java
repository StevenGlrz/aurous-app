package me.aurous.swinghacks;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

/**
 * The GhostText class will display a prompt over top of a text component when
 * the Document of the text field is empty. The Show property is used to
 * determine the visibility of the prompt.
 *
 * The Font and foreground Color of the prompt will default to those properties
 * of the parent text component. You are free to change the properties after
 * class construction. However I am in this case already overriding to lightgrey
 *
 */
public class GhostText extends JLabel implements FocusListener,
DocumentListener {
	public enum Show {
		ALWAYS, FOCUS_GAINED, FOCUS_LOST;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = -7612600032497272772L;

	private final JTextComponent component;
	private final Document document;

	private int focusLost;
	private Show show;
	private boolean showPromptOnce;

	public GhostText(final String text, final JTextComponent component) {
		this(text, component, Show.ALWAYS);
	}

	public GhostText(final String text, final JTextComponent component,
			final Show show) {
		this.component = component;
		setShow(show);
		this.document = component.getDocument();

		setText(text);

		setFont(component.getFont());

		setForeground(Color.LIGHT_GRAY);
		setBorder(new EmptyBorder(component.getInsets()));
		setHorizontalAlignment(SwingConstants.LEFT);

		component.addFocusListener(this);
		this.document.addDocumentListener(this);

		component.setLayout(new BorderLayout());
		component.add(this);
		checkForPrompt();
	}

	/**
	 * Convenience method to change the alpha value of the current foreground
	 * Color to the specifice value.
	 *
	 * @param alpha
	 *            value in the range of 0 - 1.0.
	 */
	public void changeAlpha(final float alpha) {
		this.changeAlpha((int) (alpha * 255));
	}

	/**
	 * Convenience method to change the alpha value of the current foreground
	 * Color to the specifice value.
	 *
	 * @param alpha
	 *            value in the range of 0 - 255.
	 */
	public void changeAlpha(int alpha) {
		alpha = alpha > 255 ? 255 : alpha < 0 ? 0 : alpha;

		final Color foreground = getForeground();
		final int red = foreground.getRed();
		final int green = foreground.getGreen();
		final int blue = foreground.getBlue();

		final Color withAlpha = new Color(red, green, blue, alpha);
		super.setForeground(withAlpha);
	}

	@Override
	public void changedUpdate(final DocumentEvent e) {
	}

	/**
	 * Convenience method to change the style of the current Font. The style
	 * values are found in the Font class. Common values might be: Font.BOLD,
	 * Font.ITALIC and Font.BOLD + Font.ITALIC.
	 *
	 * @param style
	 *            value representing the the new style of the Font.
	 */
	public void changeStyle(final int style) {
		setFont(getFont().deriveFont(style));
	}

	/**
	 * Check whether the prompt should be visible or not. The visibility will
	 * change on updates to the Document and on focus changes.
	 */
	private void checkForPrompt() {
		// Text has been entered, remove the prompt

		if (this.document.getLength() > 0) {
			setVisible(false);
			return;
		}

		// Prompt has already been shown once, remove it

		if (this.showPromptOnce && (this.focusLost > 0)) {
			setVisible(false);
			return;
		}

		// Check the Show property and component focus to determine if the
		// prompt should be displayed.

		if (this.component.hasFocus()) {
			if ((this.show == Show.ALWAYS) || (this.show == Show.FOCUS_GAINED)) {
				setVisible(true);
			} else {
				setVisible(false);
			}
		} else {
			if ((this.show == Show.ALWAYS) || (this.show == Show.FOCUS_LOST)) {
				setVisible(true);
			} else {
				setVisible(false);
			}
		}
	}

	@Override
	public void focusGained(final FocusEvent e) {
		checkForPrompt();
	}

	@Override
	public void focusLost(final FocusEvent e) {
		this.focusLost++;
		checkForPrompt();
	}

	/**
	 * Get the Show property
	 *
	 * @return the Show property.
	 */
	public Show getShow() {
		return this.show;
	}

	// Implement FocusListener

	/**
	 * Get the showPromptOnce property
	 *
	 * @return the showPromptOnce property.
	 */
	public boolean getShowPromptOnce() {
		return this.showPromptOnce;
	}

	@Override
	public void insertUpdate(final DocumentEvent e) {
		checkForPrompt();
	}

	// Implement DocumentListener

	@Override
	public void removeUpdate(final DocumentEvent e) {
		checkForPrompt();
	}

	/**
	 * Set the prompt Show property to control when the promt is shown. Valid
	 * values are:
	 *
	 * Show.AWLAYS (default) - always show the prompt Show.Focus_GAINED - show
	 * the prompt when the component gains focus (and hide the prompt when focus
	 * is lost) Show.Focus_LOST - show the prompt when the component loses focus
	 * (and hide the prompt when focus is gained)
	 *
	 * @param show
	 *            a valid Show enum
	 */
	public void setShow(final Show show) {
		this.show = show;
	}

	/**
	 * Show the prompt once. Once the component has gained/lost focus once, the
	 * prompt will not be shown again.
	 *
	 * @param showPromptOnce
	 *            when true the prompt will only be shown once, otherwise it
	 *            will be shown repeatedly.
	 */
	public void setShowPromptOnce(final boolean showPromptOnce) {
		this.showPromptOnce = showPromptOnce;
	}
}