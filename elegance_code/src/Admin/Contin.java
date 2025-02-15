package Admin;
import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Set;
import java.util.Vector;

import javax.swing.JOptionPane;


public class Contin {
	
	

	public int getContinNum() {
		return continNum;
	}
	public void setContinNum(int continNum) {
		this.continNum = continNum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		
		this.color = color;
	}
	public String getColorcode() {
		return colorcode;
	}
	public void setColorcode(String colorcode) {
		this.colorcode = colorcode;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Set getSerie() {
		return serie;
	}
	public void setSerie(Set serie) {
		this.serie = serie;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	public String getSeries() {
		return series;
	}
	public void setSeries(String series) {
		this.series = series;
	}
	
	
	int continNum;
	String name = ""+continNum;
	Color color;
	String colorcode;
	String type;
	String remarks;
	Set serie;
	int count;
	String series;
	String code;
	
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public static String getColorName(Color color){
		String cname = "";
		if(color.equals(Color.red)) cname = "red";
		if(color.equals(Color.green)) cname = "green";
		if(color.equals(Color.blue)) cname = "blue";
		if(color.equals(Color.magenta)) cname = "magenta";
		if(color.equals(Color.orange)) cname = "orange";
		if(color.equals(Color.yellow)) cname = "yellow";
		if(color.equals(Color.cyan)) cname = "cyan";
		if(color.equals(Color.black)) cname = "black";
		
		if(color.equals(Color.pink)) cname = "pink";
		
		return cname;
	}
	
	public static Color getColorByName(String name){
		Color color = Color.blue;
		if(name.equals("red")) color = Color.red;
		if(name.equals("green")) color = Color.green;
		if(name.equals("blue")) color = Color.blue;
		if(name.equals("magenta")) color = Color.magenta;
		if(name.equals("orange")) color = Color.orange;
		if(name.equals("yellow")) color = Color.yellow;
		if(name.equals("cyan")) color = Color.cyan;
		if(name.equals("black")) color = Color.black;
		
		if(name.equals("pink")) color = Color.pink;
		
		
		
		
		return color;
	}
	
	public void generateColor()
	{
		
		// 0-red, 1-green, 2-blue, 3-magenta, 4-orange, 5-yellow, 6-black,7-cyan,8-pink
		if (continNum % 9 == 0) color = Color.red;
		if (continNum % 9 == 1) color = Color.green;
		if (continNum % 9 == 2) color = Color.blue;
		if (continNum % 9 == 3) color = Color.magenta;
		if (continNum % 9 == 4) color = Color.orange;
		if (continNum % 9 == 5) color = Color.yellow;
		if (continNum % 9 == 6) color = Color.black;
		if (continNum % 9 == 7) color = Color.cyan;
		if (continNum % 9 == 8) color = Color.pink;
		
		colorcode = getColorName(color) +"-"+ (continNum/9+1);
		
	}
	
	public void save(){
		
		Connection con = null;

		try
		{
			con = DriverManager.getConnection ( 
				    DatabaseProperties.CONNECTION_STRING,
				    DatabaseProperties.USERNAME,
				    DatabaseProperties.PASSWORD
				 );
	
			PreparedStatement pst =
				con.prepareStatement ( 
				    "select CON_Number from contin where CON_Number = ?"
				 );
			pst.setInt ( 1, continNum );

			ResultSet rs = pst.executeQuery (  );

			if ( rs.next (  ) )
			{
				rs.close();
				pst.close();
				
				pst = con.prepareStatement ( 
					    "update contin set CON_AlternateName=?, CON_AlternateName2=?, " +
					    "CON_Remarks=?, type=?, series=?, count=?  where CON_Number=?"
					 );
				pst.setString ( 1, name );
				pst.setString ( 2, colorcode );
				pst.setString ( 3, remarks );
				pst.setString ( 4, type );
				pst.setString ( 5, series );
				pst.setInt ( 6, count );
				pst.setInt ( 7, continNum );
				
				
				pst.executeUpdate (  );

			}else{
				rs.close();
				pst.close();
				pst = con.prepareStatement ( 
					    "insert into contin (CON_Number,CON_AlternateName,CON_AlternateName2," +
					    "CON_Remarks,type,series,count) values (?,?,?,?,?,?,?)"
					 );
				pst.setString ( 2, name );
				pst.setString ( 3, colorcode );
				pst.setString ( 4, remarks );
				pst.setString ( 5, type );
				pst.setString ( 6, series );
				pst.setInt ( 7, count );
				pst.setInt ( 1, continNum );
				
				
				pst.executeUpdate (  );
				
				
			}
			
			
		}
		catch ( Exception ex )
		{
			ex.printStackTrace (  );
		}
		
	}
	
	
	
	public static int newContinNum (  )
	{
		Connection con = null;

		try
		{
			con = DriverManager.getConnection ( 
				    DatabaseProperties.CONNECTION_STRING,
				    DatabaseProperties.USERNAME,
				    DatabaseProperties.PASSWORD
				 );

			Statement stat = con.createStatement (  );
			ResultSet rs  =
				stat.executeQuery ( "select distinct CON_Number from contin" );
			Vector    v   = new Vector(  );
			int       max = 0;

			while ( rs.next (  ) )
			{
				int num = rs.getInt ( "CON_Number" );

				if ( num > max )
				{
					max = num;
				}

				v.addElement ( new Integer( num ) );
			}

			if ( con.isClosed (  ) == false )
			{
				con.close (  );
			}

			for ( int i = 0; i < v.size (  ); i++ )
			{
				if ( v.contains ( new Integer( i + 1 ) ) == false )
				{
					return ( i + 1 );
				}
			}

			return max + 1;
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

			if ( con != null )
			{
				con = null;
			}

			return -1;
		}
	}
	
	
	
	
	public Contin (int continNum){
		
		Connection con = null;
		this.continNum = continNum;

		try
		{
			con = DriverManager.getConnection ( 
				    DatabaseProperties.CONNECTION_STRING,
				    DatabaseProperties.USERNAME,
				    DatabaseProperties.PASSWORD
				 );

			Statement stm = con.createStatement (  );
			ResultSet rs =
				stm.executeQuery ( 
				    "Select * FROM contin WHERE CON_Number = " + continNum
				 );

			if ( rs.next (  ) )
			{
				name = rs.getString ( "CON_AlternateName" );
				colorcode = rs.getString ( "CON_AlternateName2" );
				type = rs.getString("type");
				series = rs.getString("series");
				remarks = rs.getString("CON_Remarks");
				count = rs.getInt("count");
				
				if ((colorcode==null) || (colorcode.indexOf("-")<0))	generateColor();
				String[] cc = colorcode.split("-");
				color = getColorByName(cc[0]);
				code = cc[1];
				
				

				if ( rs.wasNull (  ) || name == null || name.compareTo("") == 0 || name.compareToIgnoreCase("null") == 0)
				{
					name = continNum+"";
				}
			}

			if ( !con.isClosed (  ) )
			{
				con.close (  );
			}
		}
		catch ( Exception ex )
		{
			ex.printStackTrace (  );
			
		}
		finally
		{
			if ( con != null )
			{
				con = null;
			}
		}
	}
	
	
	
	public Contin(){
		continNum = newContinNum();
		generateColor();
		name = "con"+continNum;
		type = "neuron";
		remarks="";
		count=0;
		series="";
	}

}
