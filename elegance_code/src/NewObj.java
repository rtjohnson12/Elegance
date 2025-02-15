
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;



class Img2 {
	private String imgNum;
	private String dir,file;

	public Img2(String imgNum, String dir, String file) {
		
		this.imgNum = imgNum;
		this.setDir(dir);
		this.setFile(file);
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getDir() {
		return dir;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getFile() {
		return file;
	}

	
}

class Obj2 {
	private String name,imgNum;
	private int x, y;

	public Obj2(String name, int x, int y, String imgNum) {
		this.name=name;
		this.setX(x);
		this.setY(y);
		this.setImgNum(imgNum);
		
	}

	public void setImgNum(String imgNum) {
		this.imgNum = imgNum;
	}

	public String getImgNum() {
		return imgNum;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getX() {
		return x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getY() {
		return y;
	}

	


}



class NewObj {
	
	static LinkedHashMap objs;
	
	static HashMap imgs;
	


	public static void main(String[] args) 
	throws SQLException,
	ClassNotFoundException, java.lang.InstantiationException,
	java.lang.IllegalAccessException 
	{
	
		objs = new LinkedHashMap(400000);
		imgs = new LinkedHashMap(50000);
		
		Connection con = EDatabase.borrowConnection(
				
				);
		String jsql;
		PreparedStatement pst;
		ResultSet rs;
		jsql = "select OBJ_Name,OBJ_X,OBJ_Y,IMG_Number from object";
		pst = con.prepareStatement(jsql);
		
		rs = pst.executeQuery();
		while (rs.next()) 
		{

			String name = rs.getString(1);
			int x = rs.getInt(2);
			int y = rs.getInt(3);
			String imgNum = rs.getString(4);
			
			Obj2 o = new Obj2 (name,x,y,imgNum);
			objs.put(name,o);
			
		}
		rs.close();
		pst.close();
		
		jsql = "select IMG_Number,IMG_Directory,IMG_File from image";
		pst = con.prepareStatement(jsql);
		
		rs = pst.executeQuery();
		while (rs.next()) 
		{

			String imgNum = rs.getString(1);
			String dir = rs.getString(2);
			String file = rs.getString(3);
			
			Img2 im = new Img2(imgNum,dir,file);
			imgs.put(imgNum, im);
			
		}
		rs.close();
		pst.close();
		
		Set key = objs.entrySet();
		Iterator iter = key.iterator();
		while(iter.hasNext())
		{
			Map.Entry entry = (Map.Entry) iter.next();
			String name = (String) entry.getKey();
			String imgNum = ((Obj2)(objs.get(name))).getImgNum();
			int x = ((Obj2)(objs.get(name))).getX();
			int y = ((Obj2)(objs.get(name))).getY();
			if (imgs.containsKey(imgNum)){
			String dir = ((Img2)(imgs.get(imgNum))).getDir();
			String file = ((Img2)(imgs.get(imgNum))).getFile();
			saveObj(name,x,y,dir,file);
			}
			else {ELog.info(imgNum);}
		}// end of iter of obj
		
		
		
	}
	private static void saveObj(String name, int x , int y, String dir, String file)throws SQLException, ClassNotFoundException,
	java.lang.InstantiationException, java.lang.IllegalAccessException

       {

				
		Connection con = EDatabase.borrowConnection ( 
				  
				 
				 );
		String jsql;
		PreparedStatement pstmt;
		
		
			jsql = "insert into obj3 (name,x,y,directory,filename) values (?,?,?,?,?)";
			pstmt = con.prepareStatement(jsql);
			pstmt.setString(1,name);	
			pstmt.setInt(2, x);
			pstmt.setInt(3, y);
			pstmt.setString(4, dir);
			pstmt.setString(5, file);
			pstmt.executeUpdate();
			pstmt.close();
	 }
	
}