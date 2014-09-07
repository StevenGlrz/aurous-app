package com.codeusa.aurous.ui.models;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

/**
 * @author Andrew
 *
 */
public class Csv2TableModel {

	/**
	 *
	 * @param dtm
	 *            The DefaultTableModel to save to stream
	 * @param out
	 *            The stream to which to save
	 *
	 */
	public static void defaultTableModelToStream(final DefaultTableModel dtm,
			final Writer out) throws IOException {
		final String LINE_SEP = System.getProperty("line.separator");
		final int numCols = dtm.getColumnCount();
		final int numRows = dtm.getRowCount();

		// Write headers
		String sep = "";

		for (int i = 0; i < numCols; i++) {
			out.write(sep);
			out.write(dtm.getColumnName(i));
			sep = ",";
		}

		out.write(LINE_SEP);

		for (int r = 0; r < numRows; r++) {
			sep = "";

			for (int c = 0; c < numCols; c++) {
				out.write(sep);
				out.write(dtm.getValueAt(r, c).toString());
				sep = ",";
			}

			out.write(LINE_SEP);
		}
	}

	/**
	 *
	 *
	 * @param in
	 *            A CSV input stream to parse
	 * @param headers
	 *            A Vector containing the column headers. If this is null, it's
	 *            assumed that the first row contains column headers
	 *
	 * @return A DefaultTableModel containing the CSV values as type String
	 */
	public static DefaultTableModel createTableModel(final Reader in,
			Vector<Object> headers) {
		DefaultTableModel model = null;
		Scanner s = null;

		try {
			try {
				final Vector<Vector<Object>> rows = new Vector<Vector<Object>>();
				s = new Scanner(in);
				if (!s.hasNext()) {
					return null;
				}

				while (s.hasNextLine()) {
					rows.add(new Vector<Object>(Arrays.asList(s.nextLine()
							.split("(?<!\\\\),", -1))));

				}

				if (headers == null) {
					headers = rows.remove(0);

					model = new DefaultTableModel(rows, headers) {

						/**
						 *
						 */
						private static final long serialVersionUID = -611084662140321390L;

						@Override
						public boolean isCellEditable(final int row,
								final int column) {

							return false;
						}
					};
				} else {

					model = new DefaultTableModel(rows, headers) {

						/**
						 *
						 */
						private static final long serialVersionUID = -1929719310445045512L;

						@Override
						public boolean isCellEditable(final int row,
								final int column) {

							return false;
						}
					};
				}

				return model;
			} catch (final Exception e) {
				return null;
			}

		} finally {

			s.close();
		}
	}
}