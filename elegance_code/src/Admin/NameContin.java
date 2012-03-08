package Admin;


import java.sql.*;
import java.util.*;
import java.lang.*;
import javax.swing.*;
class NameContin

{
int objName, continNum=0;
String continName;
public NameContin(int objNumber)
	{
    objName = objNumber;
    nameContin ( );
    }

public static int getNewContinNumber (  )
{
	Connection con = null;
	int num =0;

	try
	{
		con = DriverManager.getConnection ( 
			    DatabaseProperties.CONNECTION_STRING,
			    DatabaseProperties.USERNAME,
			    DatabaseProperties.PASSWORD
			 );

		Statement stat = con.createStatement (  );
		ResultSet rs  =
			stat.executeQuery ( "select MAX(CON_Number) from contin" );
		

		if ( rs.next (  ) )
		{
			num = rs.getInt (1);

			
		}

		if ( con.isClosed (  ) == false )
		{
			con.close (  );
		}

		
		return num + 1;
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

public void nameContin ( )
	{
      
	  Connection con = null;
      Statement st = null;
      PreparedStatement pst = null;
      ResultSet rs = null;
	  String name = null;

		try
		{	  Class.forName("com.mysql.jdbc.Driver").newInstance();		
			 
			  con = DriverManager.getConnection ( DatabaseProperties.CONNECTION_STRING,  DatabaseProperties.USERNAME,  DatabaseProperties.PASSWORD );
              st = con.createStatement ();
              rs = st.executeQuery("select CON_Number from object where OBJ_Name="+objName);
			  if (rs.next())
				  {
			       continNum = rs.getInt(1);
                  }
				  rs.close();
				  st.close();
                  if (continNum<=0)
			      {
				   continNum = findContinNum();
				   if (continNum <= 0)
				       {
					   continNum = getNewContinNumber();
                       pst = con.prepareStatement ("insert into contin (CON_Number,CON_Alternatename) values(?,?)");
                       pst.setInt(1, continNum);
                       String nname = JOptionPane.showInputDialog ( null, "This is new contin,please give it a contin name");
                       pst.setString(2,nname);
			           pst.executeUpdate();
					   pst.close();
				       }
					
				 
				   
				        pst = con.prepareStatement ("update object set CON_number="+continNum+" where OBJ_Name = "+ objName);
			            pst.executeUpdate();
						pst.close();
					  
				   if ( con.isClosed (  ) == false )
					{
						con.close (  );
					}
			      }
			     }
				  catch ( Exception ex )
				 {
					ex.printStackTrace (  );

					if ( con != null )
					{
						con = null;
					}
				 }
                  
    }

public int findContinNum()
	{
	Connection con = null;
    Statement st = null, st1 = null;
    PreparedStatement pst = null;
    ResultSet rs = null, rs1 = null;
	int friend = 0, conN = 0, flag = 1;
	int objN1 = objName, objN2 = objName;
	//JOptionPane.showMessageDialog(null, "objN1 and objN2 are "+objName, "continName", JOptionPane.INFORMATION_MESSAGE);
    try
		{
			
			  con = DriverManager.getConnection ( DatabaseProperties.CONNECTION_STRING,  DatabaseProperties.USERNAME,  DatabaseProperties.PASSWORD );
              
			  while (flag == 1)
			  {
			  st = con.createStatement ();
              rs = st.executeQuery("select ObjName2 from relationship where ObjName1="+objN1);					      
			  if ( rs.next (  ) )
					    {
				           
			            friend = rs.getInt("ObjName2");
                        //JOptionPane.showMessageDialog(null, "post friend is "+friend, "friend", JOptionPane.INFORMATION_MESSAGE);
                        }
			            else
						{
						flag = 0;
						}
			  rs.close();
			  st.close();

			  st = con.createStatement ();
              rs = st.executeQuery("select CON_Number from object where OBJ_Name="+friend);					      
			  if ( rs.next (  ) )
					    {   
			            conN = rs.getInt("CON_Number");
						//JOptionPane.showMessageDialog(null, "conN is "+conN, "continNum", JOptionPane.INFORMATION_MESSAGE);
                        }
			  rs.close();
			  st.close();
              
			  if (conN >0)
			  {
				  return conN;
			  }
			  objN1=friend;
              }

              friend = 0;
			  flag = 1;
		      
			  while (flag == 1)
			  {
			  st = con.createStatement ();
              rs = st.executeQuery("select ObjName1 from relationship where ObjName2="+objN2);					      
			  if ( rs.next (  ) )
					    {
				           
			            friend = rs.getInt("ObjName1");
						//JOptionPane.showMessageDialog(null, "pre friend is "+friend, "friend", JOptionPane.INFORMATION_MESSAGE);
                        }
						else
						{
						flag = 0;
						}
			  rs.close();
			  st.close();

			  st = con.createStatement ();
              rs = st.executeQuery("select CON_Number from object where OBJ_Name="+friend);					      
			  if ( rs.next (  ) )
					    {   
			            conN = rs.getInt("CON_Number");
						//JOptionPane.showMessageDialog(null, "conN is "+conN, "continNum", JOptionPane.INFORMATION_MESSAGE);
                        }
			  rs.close();
			  st.close();
              
			  if (conN >0)
			  {
				  return conN;
			  }
			  objN2 = friend;
              }
						   

            if ( con.isClosed (  ) == false )
			{
				con.close (  );
			}
	     }
		  catch ( Exception ex )
		 {
			ex.printStackTrace (  );

			if ( con != null )
			{
				con = null;
			}
		 }
    return 0;
    }

}


