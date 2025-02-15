import java.util.Vector;

import javax.swing.JOptionPane;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import java.sql.*;

/**
 * An implementation of the TableModel interface to provide the functionality required
 * for the table in ObjectFrame
 *
 * @author zndavid
 * @version 1.0
 */
public class ContinTableModel
	implements TableModel
{
	ResultSet rs = null;
	/**
	 * Creates a new ObjectTableModel object.
	 *
	 * @param newData The data as a Vector of Vectors
	 * @param newColumnNames The column names in a Vector
	 */
	public ContinTableModel ()
	{
        Connection con  = null;
        try
        {
            con =
                EDatabase.borrowConnection ( 
                
               
                
                );
            PreparedStatement pst = con.prepareStatement("Select CON_Number, CON_AlternateName, CON_Remarks from contin where CON_Number > 0", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = pst.executeQuery();
        }
        catch ( Exception ex )
        {
            ex.printStackTrace (  );
            JOptionPane.showMessageDialog ( 
                null,
                ex.getMessage (  ),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        } finally {
			EDatabase.returnConnection(con);
		}
    }

	/**
	 * Gets the number of rows.
	 *
	 * @return The number of rows
	 */
	public int getRowCount (  )
	{
        try 
        {
			int rowcount = 0;
			rs.beforeFirst();
			while (rs.next()) 
            {
				rowcount++;
			}
			return rowcount;
		} 
        catch (Exception e) 
        {
			e.printStackTrace();
			return (-1);
		}
	}

	/**
	 * Gets the number of columns.
	 *
	 * @return The number of columns
	 */
	public int getColumnCount (  )
	{
		return 3;
	}

	/**
	 * Gets the column name corresponding the index provided.
	 *
	 * @param columnIndex The zero based index of the column
	 *
	 * @return The column name
	 */
	public String getColumnName ( int columnIndex )
	{
		if(columnIndex == 0) return "Contin Number";
        else if(columnIndex == 1) return "Alternate Name";
        else return "Remarks";
	}

	/**
	 * Gets the column class corresponding the index provided.
	 *
	 * @param columnIndex The zero based index of the column
	 *
	 * @return The column class
	 */
	public Class getColumnClass ( int columnIndex )
	{
		return "".getClass();
	}

	/**
	 * returns whether a cell is editable or not. This function always returns false by
	 * which the whole table becomes uneditable.
	 *
	 * @param rowIndex index of the row.
	 * @param columnIndex index of the column.
	 *
	 * @return Always false
	 */
	public boolean isCellEditable ( 
	    int rowIndex,
	    int columnIndex
	 )
	{
		if (columnIndex == 0) return false;
        return true;
	}

	/**
	 * Gets the value of a specific location in the table.
	 *
	 * @param rowIndex row index
	 * @param columnIndex column index
	 *
	 * @return The value.
	 */
	public Object getValueAt ( 
	    int rowIndex,
	    int columnIndex
	 )
	{
		try 
        {
			rs.absolute(rowIndex + 1);
            if(columnIndex == 0) return rs.getString("CON_Number");
            else if(columnIndex == 1) return rs.getString("CON_AlternateName");
            else return rs.getString("CON_Remarks");
		} catch (Exception e) {
			ELog.info("Error " + e);
			e.printStackTrace();
			return ("Error");
		}

	}
	/**
	 * Sets the value of a specific location in the table.
	 *
	 * @param aValue The value
	 * @param rowIndex the row index
	 * @param columnIndex the column index
	 */
	public void setValueAt ( 
	    Object aValue,
	    int    rowIndex,
	    int    columnIndex
	 ) 
     {
        try 
        {
            int rowcount;
            if (columnIndex > 2) 
            {
                return;
            }
            if (columnIndex == 0) 
            {
    			return;
            } 
            else 
            {
    			rowcount = 0;
                rs.beforeFirst();
                while (rs.next()) 
                {
    				rowcount++;
                }
                if (rowIndex <= rowcount) 
                {
    				rs.absolute(rowIndex + 1);
                    rs.updateObject(columnIndex + 1, aValue);
                    rs.updateRow();                    
                }
            }    
        } 
        catch (Exception e) 
        {
    		e.printStackTrace();
        }
     }

	/**
	 * Adds a table model listener. Currently, this function does nothing
	 *
	 * @param l the table model listener
	 */
	public void addTableModelListener ( TableModelListener l ) {}

	/**
	 * removes a table model listener. Currently, this function does nothing.
	 *
	 * @param l The table model listener
	 */
	public void removeTableModelListener ( TableModelListener l ) {}
}
