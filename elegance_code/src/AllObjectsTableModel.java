import java.util.Vector;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 * An implementation of the TableModel interface to provide the functionality
 * required for the table in RelationFrame
 * 
 * @author zndavid
 * @version 1.0
 */
public class AllObjectsTableModel implements TableModel {
	private Vector data;

	/**
	 * Creates a new AllObjectsTableModel object.
	 */
	public AllObjectsTableModel(Vector newData) {
		data = newData;
	}

	public void setData(Vector newData) {
		data = newData;
	}

	/**
	 * Gets the number of rows.
	 * 
	 * @return The number of rows
	 */
	public int getRowCount() {
		// return ImageDisplayPanel.getNumberOfRelations ( );
		return data.size();
	}

	/**
	 * Gets the number of columns.
	 * 
	 * @return The number of columns
	 */
	public int getColumnCount() {
		return 6;
	}

	/**
	 * Gets the column name corresponding the index provided.
	 * 
	 * @param columnIndex
	 *            The zero based index of the column
	 * 
	 * @return The column name
	 */
	public String getColumnName(int columnIndex) {
		if (columnIndex == 0) {
			return "Image No.";
		} else if (columnIndex == 1) {
			return "Object Name";
		} else if (columnIndex == 2) {
			return "X";
		} else if (columnIndex == 3) {
			return "Y";
		} else if (columnIndex == 4) {
			return "Contin No";
		} else if (columnIndex == 5) {
			return "Remarks";
		} else {
			return null;
		}
	}

	/**
	 * Gets the column class corresponding the index provided.
	 * 
	 * @param columnIndex
	 *            The zero based index of the column
	 * 
	 * @return The column class
	 */
	public Class getColumnClass(int columnIndex) {
		if (columnIndex == 2 || columnIndex == 3)
			return new Integer(0).getClass();
		else
			return "".getClass();
	}

	/**
	 * returns whether a cell is editable or not. Except for remarks which are
	 * editable, this function always returns false by which the whole table
	 * becomes uneditable.
	 * 
	 * @param rowIndex
	 *            index of the row.
	 * @param columnIndex
	 *            index of the column.
	 * 
	 * @return Always false except for remarks
	 */
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (columnIndex == 5)
			return true;
		return false;
	}

	/**
	 * Gets the value of a specific location in the table.
	 * 
	 * @param rowIndex
	 *            row index
	 * @param columnIndex
	 *            column index
	 * 
	 * @return The value.
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (rowIndex >= getRowCount()) {
			return null;
		}

		Vector v = (Vector) data.elementAt(rowIndex);

		return v.elementAt(columnIndex);
	}

	/**
	 * Sets the value of a specific location in the table.
	 * 
	 * @param aValue
	 *            The value
	 * @param rowIndex
	 *            the row index
	 * @param columnIndex
	 *            the column index
	 */
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if (columnIndex == 5) {
			Vector row = (Vector) data.elementAt(rowIndex);
			CellObject obj = new CellObject((String) row.elementAt(1));
			obj.saveRemarks((String) aValue);
			row.setElementAt(aValue, columnIndex);
			data.setElementAt(row, rowIndex);
		}
	}

	/**
	 * Adds a table model listener. Currently, this function does nothing
	 * 
	 * @param l
	 *            the table model listener
	 */
	public void addTableModelListener(TableModelListener l) {
	}

	/**
	 * removes a table model listener. Currently, this function does nothing.
	 * 
	 * @param l
	 *            The table model listener
	 */
	public void removeTableModelListener(TableModelListener l) {
	}
}
