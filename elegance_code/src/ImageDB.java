import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Vector;
import java.util.zip.DataFormatException;

/**
 * An implementation of the DBConnector interface that provides the DBConnector
 * functionality for the Image table in the database.
 * 
 * @author zndavid
 * @version 1.0
 */
public class ImageDB implements DBConnector {
	int id;
	String imgNumber;
	String fileName;
	String directory;
	String worm;
	String series;
	String printNumber;
	String negativeNumber;
	String sectionNumber;
	String enteredBy;
	Date dateEntered;

	/**
	 * inserts/updates the current record in the database.
	 * 
	 * @return number of rows changed
	 * 
	 * @throws SQLException
	 *             an exception thrown in case of an error in interacting with
	 *             the database.
	 * @throws DataFormatException
	 *             an exception thrown in case of some of the data being in the
	 *             wrong format.
	 */
	public static int getPrintNumByImgNum(String imgNum) throws SQLException {
		Connection con = null;
		try {
			con = EDatabase.borrowConnection(

			);
			Statement stmt = con.createStatement();

			ResultSet rs = stmt.executeQuery("select IMG_PrintNumber from image where IMG_Number='" + imgNum + "'");
			if (rs.next()) {
				int printNum = rs.getInt(1);
				rs.close();
				con.close();
				return printNum;
			} else
				return -1;

		} catch (SQLException ex) {
			ex.printStackTrace();
			throw ex;
		} finally {
			EDatabase.returnConnection(con);
		}

	}

	public static String getWormByImgNum(String imgNum) throws SQLException {
		Connection con = null;

		try {
			con = EDatabase.borrowConnection();

			Statement stmt = con.createStatement();

			ResultSet rs = stmt.executeQuery("select IMG_Worm from image where IMG_Number='" + imgNum + "'");
			rs.next();
			String worm = rs.getString(1);
			rs.close();
			con.close();
			return worm;

		} catch (SQLException ex) {
			ex.printStackTrace();
			throw ex;
		} finally {
			EDatabase.returnConnection(con);
		}

	}

	public static String getFileByImgNum(String imgNum) throws SQLException {
		Connection con = null;

		try {
			con = EDatabase.borrowConnection();
			Statement stmt = con.createStatement();

			ResultSet rs = stmt.executeQuery("select IMG_File from image where IMG_Number='" + imgNum + "'");
			rs.next();
			String file = rs.getString(1);
			rs.close();
			con.close();
			return file;

		} catch (SQLException ex) {
			ex.printStackTrace();
			throw ex;
		} finally {
			EDatabase.returnConnection(con);
		}

	}

	public static String getSeriesByImgNum(String imgNum) throws SQLException {
		Connection con = null;
		try {
			con = EDatabase.borrowConnection(

			);
			Statement stmt = con.createStatement();

			ResultSet rs = stmt.executeQuery("select IMG_Series from image where IMG_Number='" + imgNum + "'");
			rs.next();
			String series = rs.getString(1);
			rs.close();
			con.close();
			return series;

		} catch (SQLException ex) {
			ex.printStackTrace();
			throw ex;
		} finally {
			EDatabase.returnConnection(con);
		}

	}

	public static String getDirectoryByImgNum(String imgNum) throws SQLException {
		Connection con = null;
		try

		{
			con = EDatabase.borrowConnection();
			Statement stmt = con.createStatement();

			ResultSet rs = stmt.executeQuery("select IMG_Directory from image where IMG_Number='" + imgNum + "'");
			rs.next();
			String directory = rs.getString(1);
			rs.close();
			con.close();
			return directory;

		} catch (SQLException ex) {
			ex.printStackTrace();
			throw ex;
		} finally {
			EDatabase.returnConnection(con);
		}

	}

	public static String getNextImgNum(String imgNum) throws SQLException {
		Connection con = null;
		try

		{
			con = EDatabase.borrowConnection(

			);
			Statement stmt = con.createStatement();
			int printNum = getPrintNumByImgNum(imgNum);
			String worm = getWormByImgNum(imgNum);
			String series = getSeriesByImgNum(imgNum);
			ResultSet rs = stmt.executeQuery("select IMG_Number from image where IMG_Worm='" + worm + "' and IMG_Series='" + series + "' and IMG_PrintNumber>"
					+ printNum + " order by IMG_SectionNumber ASC");
			if (rs.next()) {
				String nextImgNum = rs.getString(1);
				rs.close();
				con.close();
				return nextImgNum;
			} else {
				return "image >" + imgNum;
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
			throw ex;
		} finally {
			EDatabase.returnConnection(con);
		}

	}

	public static String getPrevImgNum(String imgNum) throws SQLException {
		Connection con = null;
		try {
			con = EDatabase.borrowConnection(

			);
			Statement stmt = con.createStatement();
			int printNum = getPrintNumByImgNum(imgNum);
			String worm = getWormByImgNum(imgNum);
			String series = getSeriesByImgNum(imgNum);
			ResultSet rs = stmt.executeQuery("select IMG_Number from image where IMG_Worm='" + worm + "' and IMG_Series='" + series + "' and IMG_PrintNumber<"
					+ printNum + " order by IMG_SectionNumber desc ");
			if (rs.next()) {
				String nextImgNum = rs.getString(1);
				rs.close();

				return nextImgNum;
			} else {
				return "image <" + imgNum;
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
			throw ex;
		} finally {
			EDatabase.returnConnection(con);
		}

	}

	public int insertRecord() throws SQLException, DataFormatException {
		if ((imgNumber == null) || (imgNumber.compareTo("") == 0)) {
			throw new DataFormatException("Primary key 'Image Number' is NULL");
		}
		Connection con = null;
		try {
			con = EDatabase.borrowConnection(

			);
			Statement stmt = con.createStatement();

			return stmt
					.executeUpdate("INSERT into image (IMG_Number, IMG_File, IMG_Directory, IMG_Worm, IMG_Series, IMG_PrintNumber, IMG_NegativeNumber, IMG_SectionNumber, IMG_EnteredBy, IMG_DateEntered) values ( "
							+ "'"
							+ imgNumber
							+ "', '"
							+ fileName
							+ "', '"
							+ directory
							+ "', '"
							+ worm
							+ "', '"
							+ series
							+ "', '"
							+ printNumber
							+ "', '"
							+ negativeNumber + "', '" + sectionNumber + "', '" + enteredBy + "', '" + dateEntered + "' )");
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw ex;
		} catch (Exception ex1) {
			ex1.printStackTrace();
			throw new DataFormatException("Unknown data");
		} finally {
			EDatabase.returnConnection(con);
		}
	}

	/**
	 * selects records from the database and returns them as a vector
	 * 
	 * @return the records as a Vector
	 * 
	 * @throws SQLException
	 *             an exception thrown in case of an error in interacting with
	 *             the database
	 */
	public Vector selectRecords() throws SQLException {
		return selectRecords(false);
	}

	/**
	 * selects records from the database and returns them as a vector
	 * 
	 * @param useLike
	 *            a boolean describing whether to use the 'LIKE' option when the
	 *            SQL query is formed
	 * 
	 * @return The records as a Vector
	 * 
	 * @exception SQLException
	 *                an exception thrown in case of an error in interacting
	 *                with the database
	 */
	public Vector selectRecords(boolean useLike) throws SQLException {
		Connection con = null;
		try {
			con = EDatabase.borrowConnection(

			);
			String sql = "";
			boolean flag = false;

			sql += "select ID, IMG_Number, IMG_File, IMG_Directory, IMG_Worm, IMG_Series, IMG_PrintNumber, IMG_NegativeNumber, IMG_SectionNumber, IMG_EnteredBy, IMG_DateEntered from image where ";

			if (id != 0) {
				if (useLike) {
					sql += ("ID LIKE '" + id + "' ");
				} else {
					sql += ("ID = '" + id + "' ");
				}

				flag = true;
			}

			if ((imgNumber != null) && ("".compareTo(imgNumber) != 0)) {
				if (flag) {
					sql += "AND ";
				}

				if (useLike) {
					sql += ("IMG_Number LIKE '" + imgNumber + "' ");
				} else {
					sql += ("IMG_Number = '" + imgNumber + "' ");
				}

				flag = true;
			}

			if ((fileName != null) && ("".compareTo(fileName) != 0)) {
				if (flag) {
					sql += "AND ";
				}

				if (useLike) {
					sql += ("IMG_File LIKE '" + fileName + "' ");
				} else {
					sql += ("IMG_File = '" + fileName + "' ");
				}

				flag = true;
			}

			if ((directory != null) && ("".compareTo(directory) != 0)) {
				if (flag) {
					sql += "AND ";
				}

				if (useLike) {
					sql += ("IMG_Directory LIKE '" + directory + "' ");
				} else {
					sql += ("IMG_Directory = '" + directory + "' ");
				}

				flag = true;
			}

			if ((worm != null) && ("".compareTo(worm) != 0)) {
				if (flag) {
					sql += "AND ";
				}

				if (useLike) {
					sql += ("IMG_Worm LIKE '" + worm + "' ");
				} else {
					sql += ("IMG_Worm = '" + worm + "' ");
				}

				flag = true;
			}

			if ((series != null) && ("".compareTo(series) != 0)) {
				if (flag) {
					sql += "AND ";
				}

				if (useLike) {
					sql += ("IMG_Series LIKE '" + series + "' ");
				} else {
					sql += ("IMG_Series = '" + series + "' ");
				}

				flag = true;
			}

			if ((printNumber != null) && ("".compareTo(printNumber) != 0)) {
				if (flag) {
					sql += "AND ";
				}

				if (useLike) {
					sql += ("IMG_PrintNumber LIKE '" + printNumber + "' ");
				} else {
					sql += ("IMG_PrintNumber = '" + printNumber + "' ");
				}

				flag = true;
			}

			if ((negativeNumber != null) && ("".compareTo(negativeNumber) != 0)) {
				if (flag) {
					sql += "AND ";
				}

				if (useLike) {
					sql += ("IMG_NegativeNumber LIKE '" + negativeNumber + "' ");
				} else {
					sql += ("IMG_NegativeNumber = '" + negativeNumber + "' ");
				}

				flag = true;
			}

			if ((sectionNumber != null) && ("".compareTo(sectionNumber) != 0)) {
				if (flag) {
					sql += "AND ";
				}

				if (useLike) {
					sql += ("IMG_SectionNumber LIKE '" + sectionNumber + "' ");
				} else {
					sql += ("IMG_SectionNumber = '" + sectionNumber + "' ");
				}

				flag = true;
			}

			if ((enteredBy != null) && ("".compareTo(enteredBy) != 0)) {
				if (flag) {
					sql += "AND ";
				}

				if (useLike) {
					sql += ("IMG_EnteredBy LIKE '" + enteredBy + "' ");
				} else {
					sql += ("IMG_EnteredBy = '" + enteredBy + "' ");
				}

				flag = true;
			}

			if ((dateEntered != null) && ("".compareTo(dateEntered.toString()) != 0)) {
				if (flag) {
					sql += "AND ";
				}

				if (useLike) {
					sql += ("IMG_DateEntered LIKE '" + dateEntered + "' ");
				} else {
					sql += ("IMG_DateEntered = '" + dateEntered + "' ");
				}

				flag = true;
			}

			PreparedStatement pst = con.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			Vector v = new Vector();

			while (rs.next()) {
				ImageDB imgdb = new ImageDB();

				imgdb.id = rs.getInt("ID");
				imgdb.imgNumber = rs.getString("IMG_Number");
				imgdb.fileName = rs.getString("IMG_File");
				imgdb.directory = rs.getString("IMG_Directory");
				imgdb.worm = rs.getString("IMG_Worm");
				imgdb.series = rs.getString("IMG_Series");
				imgdb.printNumber = rs.getString("IMG_PrintNumber");
				imgdb.negativeNumber = rs.getString("IMG_NegativeNumber");
				imgdb.sectionNumber = rs.getString("IMG_SectionNumber");
				imgdb.enteredBy = rs.getString("IMG_EnteredBy");
				imgdb.dateEntered = rs.getDate("IMG_DateEntered");

				v.addElement(imgdb);
			}

			return v;
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw ex;
		} finally {
			EDatabase.returnConnection(con);
		}
	}

	/**
	 * deletes records from the database.
	 * 
	 * @return number of records deleted
	 * 
	 * @throws SQLException
	 *             an exception thrown in case of an error
	 */
	public int deleteRecords() throws SQLException {
		Connection con = null;

		try {
			con = EDatabase.borrowConnection();
			String sql = "";
			boolean flag = false;

			sql += "DELETE FROM image where ";

			if (id != 0) {
				sql += ("ID = '" + id + "' ");
				flag = true;
			}

			if ((imgNumber != null) && ("".compareTo(imgNumber) != 0)) {
				if (flag) {
					sql += "AND ";
				}

				sql += ("IMG_Number = '" + imgNumber + "' ");
				flag = true;
			}

			if ((fileName != null) && ("".compareTo(fileName) != 0)) {
				if (flag) {
					sql += "AND ";
				}

				sql += ("IMG_File = '" + fileName + "' ");
				flag = true;
			}

			if ((directory != null) && ("".compareTo(directory) != 0)) {
				if (flag) {
					sql += "AND ";
				}

				sql += ("IMG_Directory = '" + directory + "' ");
				flag = true;
			}

			if ((worm != null) && ("".compareTo(worm) != 0)) {
				if (flag) {
					sql += "AND ";
				}

				sql += ("IMG_Worm = '" + worm + "' ");
				flag = true;
			}

			if ((series != null) && ("".compareTo(series) != 0)) {
				if (flag) {
					sql += "AND ";
				}

				sql += ("IMG_Series = '" + series + "' ");
				flag = true;
			}

			if ((printNumber != null) && ("".compareTo(printNumber) != 0)) {
				if (flag) {
					sql += "AND ";
				}

				sql += ("IMG_PrintNumber = '" + printNumber + "' ");
				flag = true;
			}

			if ((negativeNumber != null) && ("".compareTo(negativeNumber) != 0)) {
				if (flag) {
					sql += "AND ";
				}

				sql += ("IMG_NegativeNumber = '" + negativeNumber + "' ");
				flag = true;
			}

			if ((sectionNumber != null) && ("".compareTo(sectionNumber) != 0)) {
				if (flag) {
					sql += "AND ";
				}

				sql += ("IMG_SectionNumber = '" + sectionNumber + "' ");
				flag = true;
			}

			if ((enteredBy != null) && ("".compareTo(enteredBy) != 0)) {
				if (flag) {
					sql += "AND ";
				}

				sql += ("IMG_EnteredBy = '" + enteredBy + "' ");
				flag = true;
			}

			if ((dateEntered != null) && ("".compareTo(dateEntered.toString()) != 0)) {
				if (flag) {
					sql += "AND ";
				}

				sql += ("IMG_DateEntered = '" + dateEntered + "' ");
				flag = true;
			}

			Statement st = con.createStatement();

			return st.executeUpdate(sql);
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw ex;
		}  finally {
			EDatabase.returnConnection(con);
		}
	}
}
