package me.aurous.ui.listeners;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPopupMenu;
import javax.swing.text.JTextComponent;

/**
 * @author Andrew
 *
 */
public class ContextMenuMouseListener extends MouseAdapter {
	private enum Actions {
		COPY, CUT, PASTE, SELECT_ALL, UNDO
	}

	private final Action copyAction;
	private final Action cutAction;
	private Actions lastActionSelected;
	private final Action pasteAction;
	private final JPopupMenu popup = new JPopupMenu();

	private String savedString = "";
	private final Action selectAllAction;
	private JTextComponent textComponent;

	private final Action undoAction;;

	public ContextMenuMouseListener() {
		this.undoAction = new AbstractAction("Undo") {

			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(final ActionEvent ae) {
				ContextMenuMouseListener.this.textComponent.setText("");
				ContextMenuMouseListener.this.textComponent
				.replaceSelection(ContextMenuMouseListener.this.savedString);

				ContextMenuMouseListener.this.lastActionSelected = Actions.UNDO;
			}
		};

		this.popup.add(this.undoAction);
		this.popup.addSeparator();

		this.cutAction = new AbstractAction("Cut") {

			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(final ActionEvent ae) {
				ContextMenuMouseListener.this.lastActionSelected = Actions.CUT;
				ContextMenuMouseListener.this.savedString = ContextMenuMouseListener.this.textComponent
						.getText();
				ContextMenuMouseListener.this.textComponent.cut();
			}
		};

		this.popup.add(this.cutAction);

		this.copyAction = new AbstractAction("Copy") {

			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(final ActionEvent ae) {
				ContextMenuMouseListener.this.lastActionSelected = Actions.COPY;
				ContextMenuMouseListener.this.textComponent.copy();
			}
		};

		this.popup.add(this.copyAction);

		this.pasteAction = new AbstractAction("Paste") {

			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(final ActionEvent ae) {
				ContextMenuMouseListener.this.lastActionSelected = Actions.PASTE;
				ContextMenuMouseListener.this.savedString = ContextMenuMouseListener.this.textComponent
						.getText();
				ContextMenuMouseListener.this.textComponent.paste();

			}
		};

		this.popup.add(this.pasteAction);
		this.popup.addSeparator();

		this.selectAllAction = new AbstractAction("Select All") {

			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(final ActionEvent ae) {
				ContextMenuMouseListener.this.lastActionSelected = Actions.SELECT_ALL;
				ContextMenuMouseListener.this.textComponent.selectAll();
			}
		};

		this.popup.add(this.selectAllAction);
	}

	@Override
	public void mouseClicked(final MouseEvent e) {
		if (e.getModifiers() == InputEvent.BUTTON3_MASK) {
			if (!(e.getSource() instanceof JTextComponent)) {
				return;
			}

			this.textComponent = (JTextComponent) e.getSource();
			this.textComponent.requestFocus();

			final boolean enabled = this.textComponent.isEnabled();
			final boolean editable = this.textComponent.isEditable();
			final boolean nonempty = !((this.textComponent.getText() == null) || this.textComponent
					.getText().equals(""));
			final boolean marked = this.textComponent.getSelectedText() != null;

			final boolean pasteAvailable = Toolkit.getDefaultToolkit()
					.getSystemClipboard().getContents(null)
					.isDataFlavorSupported(DataFlavor.stringFlavor);

			this.undoAction
			.setEnabled(enabled
					&& editable
					&& ((this.lastActionSelected == Actions.CUT) || (this.lastActionSelected == Actions.PASTE)));
			this.cutAction.setEnabled(enabled && editable && marked);
			this.copyAction.setEnabled(enabled && marked);
			this.pasteAction.setEnabled(enabled && editable && pasteAvailable);
			this.selectAllAction.setEnabled(enabled && nonempty);

			int nx = e.getX();

			if (nx > 500) {
				nx = nx - this.popup.getSize().width;
			}

			this.popup.show(e.getComponent(), nx,
					e.getY() - this.popup.getSize().height);
		}
	}
}