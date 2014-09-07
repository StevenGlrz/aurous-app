package com.codeusa.aurous.ui.models;

import javax.swing.DefaultListSelectionModel;
import javax.swing.ListSelectionModel;

/**
 * @author Andrew
 *
 */
public class ForcedListSelectionModel extends DefaultListSelectionModel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1428691988149641448L;

	public ForcedListSelectionModel() {
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	@Override
	public void clearSelection() {
	}

	@Override
	public void removeSelectionInterval(final int index0, final int index1) {
	}

}