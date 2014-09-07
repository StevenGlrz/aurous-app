package com.codeusa.aurous.utils;

import java.awt.Color;
import java.awt.Component;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.codeusa.aurous.ui.models.Csv2TableModel;
import com.codeusa.aurous.ui.panels.PlayListPanel;
import com.codeusa.aurous.ui.panels.TabelPanel;

/**
 * @author Andrew
 *
 */
public class ModelUtils {

	public static final int TITLE_INDEX = 0;
	public static final int ARTIST_INDEX = 1;
	public static final int ALBUM_INDEX = 5;
	public static final int ALBUMART_INDEX = 6;
	public static final int TIME_INDEX = 2;
	public static final int DATE_INDEX = 3;
	public static final int OWNER_INDEX = 4;
	public static final int LINK_INDEX = 7;
	public static boolean playListLoaded;
	public static JTable table;

	public static class InteractiveRenderer extends DefaultTableCellRenderer {
		/**
		 *
		 */
		private static final long serialVersionUID = 8185342590581318945L;
		protected int interactiveColumn;

		public InteractiveRenderer(final int interactiveColumn) {
			this.interactiveColumn = interactiveColumn;
		}

		@Override
		public Component getTableCellRendererComponent(final JTable table,
				final Object value, final boolean isSelected,
				final boolean hasFocus, final int row, final int column) {
			final Component c = super.getTableCellRendererComponent(table,
					value, isSelected, hasFocus, row, column);

			setOpaque(true);
			setBackground(new Color(18, 19, 21));
			setForeground(new Color(229, 229, 229));

			if ((column == this.interactiveColumn) && hasFocus) {

			}

			return c;
		}
	}

	public class InteractiveTableModelListener implements TableModelListener {
		@Override
		public void tableChanged(final TableModelEvent evt) {
			if (evt.getType() == TableModelEvent.UPDATE) {
				final int column = evt.getColumn();
				final int row = evt.getFirstRow();
				System.out.println("row: " + row + " column: " + column);
				table.setColumnSelectionInterval(column + 1, column + 1);
				table.setRowSelectionInterval(row, row);

			}
		}
	}

	public static void loadPlayList(final String fileLocation) {
		try {

			table = TabelPanel.table;
			DefaultTableModel tableModel = TabelPanel.tableModel;
			final String datafile = fileLocation;
			final FileReader fin = new FileReader(datafile);

			tableModel = Csv2TableModel.createTableModel(fin, null);
			if (tableModel == null) {
				JOptionPane.showMessageDialog(null,
						"Error loading playlist, corrupted or unfinished.",
						"Error", JOptionPane.ERROR_MESSAGE);

				ModelUtils.loadPlayList("data/scripts/blank.plist");
				PlayListPanel.canSetLast = false;
				return;
			} else {
				PlayListPanel.canSetLast = true;
			}
			table.setModel(tableModel);
			final TableColumn hiddenLink = table.getColumnModel().getColumn(
					LINK_INDEX);
			hiddenLink.setMinWidth(2);
			hiddenLink.setPreferredWidth(2);
			hiddenLink.setMaxWidth(2);
			hiddenLink.setCellRenderer(new InteractiveRenderer(LINK_INDEX));
			final TableColumn hiddenAlbumArt = table.getColumnModel()
					.getColumn(ALBUMART_INDEX);
			hiddenAlbumArt.setMinWidth(2);
			hiddenAlbumArt.setPreferredWidth(2);
			hiddenAlbumArt.setMaxWidth(2);
			hiddenAlbumArt.setCellRenderer(new InteractiveRenderer(
					ALBUMART_INDEX));

		} catch (final FileNotFoundException e) {
			System.out.println("afaafvava");
			ModelUtils.loadPlayList("data/scripts/blank.plist");
		}
	}

}
