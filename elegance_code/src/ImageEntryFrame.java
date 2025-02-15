import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;


/**
 * The frame using which the data entry is done.
 *
 * @author zndavid
 * @version 1.0
 */
public class ImageEntryFrame
	extends JFrame
{
	private GridBagLayout gridBagLayout1          = new GridBagLayout(  );
	//private JLabel        numberLabel             = new JLabel(  );
	private JLabel        directoryLabel          = new JLabel(  );
	private JLabel        wormLabel               = new JLabel(  );
	private JLabel        seriesLabel             = new JLabel(  );
	//private JLabel        printLabel              = new JLabel(  );
	//private JLabel        negativeLabel           = new JLabel(  );
	//private JLabel        sectionLabel            = new JLabel(  );
	private JLabel        enteredLabel            = new JLabel(  );
	private JLabel        dateLabel               = new JLabel(  );
	private JButton       okButton                = new JButton(  );
	private JTextField    prename       = new JTextField(  );
	private JTextField    firstImgNumber       = new JTextField(  );
	private JTextField    lastImgNumber       = new JTextField(  );
	private JTextField    postname       = new JTextField(  );
	private JTextField    directoryTextField      = new JTextField(  );
	private JTextField    wormTextField           = new JTextField(  );
	private JTextField    seriesTextField         = new JTextField(  );
	//private JTextField    printNumberTextField    = new JTextField(  );
	//private JTextField    negativeNumberTextField = new JTextField(  );
	//private JTextField    sectionNumberTextField  = new JTextField(  );
	private JTextField    enteredByTextField      = new JTextField(  );
	private JTextField    dateEnteredTextField    = new JTextField(  );
	private JButton       cancelButton            = new JButton(  );
	private JLabel        fileNameLabel           = new JLabel(  );
	private JLabel        fileNameLabel2           = new JLabel("e.g. (PAG001.tif ~ PAG099.tif)");
	private JLabel        prenameLabel           = new JLabel( "PAG" );
	private JLabel        postnameLabel           = new JLabel( ".tif" );
	private JLabel        firstImgNumberLabel           = new JLabel( "001" );
	private JLabel        lastImgNumberLabel           = new JLabel( "099" );
	private JLabel        from           = new JLabel( " from " );
	private JLabel        to           = new JLabel( " to " );
	private JLabel        from1           = new JLabel( " from " );
	private JLabel        to1          = new JLabel( " to " );
	private JButton       browseButton            = new JButton(  );
	private JFileChooser  dirBrowseButton            = new JFileChooser(  );

	/**
	 * Creates a new ImageEntryFrame object.
	 */
	public ImageEntryFrame (  )
	{
		try
		{
			jbInit (  );
		}
		catch ( Exception e )
		{
			e.printStackTrace (  );
		}
	}

	private void jbInit (  )
		throws Exception
	{
		this.setTitle ( "Enter The Image Data" );
		this.setSize ( new Dimension( 500, 363 ) );
		this.getContentPane (  ).setLayout ( gridBagLayout1 );
		//numberLabel.setText("Image Number*");
		directoryLabel.setText ( "Directory" );
		wormLabel.setText ( "Specimen" );
		seriesLabel.setText ( "Series" );
		//printLabel.setText ( "Print Number" );
		
		//sectionLabel.setText ( "Section Number" );
		enteredLabel.setText ( "Entered By" );
		dateLabel.setText ( "Date Entered" );

		ResultSet rs = getLatestData (  );
		//numberLabel.setBackground ( new Color( 206, 206, 230 ) );

		if ( rs.next (  ) )
		{
			//imageNumberTextField.setText ( rs.getString ( "IMG_Number" ) );
			//filenameTextField.setText ( rs.getString ( "IMG_File" ) );
			directoryTextField.setText ( rs.getString ( "IMG_Directory" ) );
			wormTextField.setText ( rs.getString ( "IMG_Worm" ) );
			seriesTextField.setText ( rs.getString ( "IMG_Series" ) );
			//printNumberTextField.setText ( rs.getString ( "IMG_PrintNumber" ) );
			//negativeNumberTextField.setText ( rs.getString ( "IMG_NegativeNumber" ) );
			//sectionNumberTextField.setText ( rs.getString ( "IMG_SectionNumber" ) );
			enteredByTextField.setText ( rs.getString ( "IMG_EnteredBy" ) );
			dateEnteredTextField.setText ( rs.getString ( "IMG_DateEntered" ) );
		}

		okButton.setText ( "OK" );
		okButton.addActionListener ( 
		    new ActionListener(  )
			{
				public void actionPerformed ( ActionEvent e )
				{
					okButton_actionPerformed ( e );
				}
			}
		 );
		cancelButton.setText ( "Cancel" );
		cancelButton.addActionListener ( 
		    new ActionListener(  )
			{
				public void actionPerformed ( ActionEvent e )
				{
					cancelButton_actionPerformed ( e );
				}
			}
		 );
		fileNameLabel.setText ( "File Name" );
		browseButton.setText ( "Browse" );
		dirBrowseButton.setToolTipText("Specify directory containing .tif images");
		dirBrowseButton.addActionListener ( 
			    new ActionListener(  )
				{
					public void actionPerformed ( ActionEvent e )
					{
						browseButton_actionPerformed ( e );
					}
				}
			 );
		
		
		browseButton.addActionListener ( 
		    new ActionListener(  )
			{
				public void actionPerformed ( ActionEvent e )
				{
					browseButton_actionPerformed ( e );
				}
			}
		 );
		//this.getContentPane (  ).add(numberLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
		this.getContentPane (  ).add(directoryLabel, new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
		this.getContentPane (  ).add(wormLabel, new GridBagConstraints(0, 4, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
		this.getContentPane (  ).add(seriesLabel, new GridBagConstraints(0, 5, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
		//this.getContentPane (  ).add(printLabel, new GridBagConstraints(0, 5, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
		//this.getContentPane (  ).add(negativeLabel, new GridBagConstraints(0, 7, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
		//this.getContentPane (  ).add(sectionLabel, new GridBagConstraints(0, 7, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
		this.getContentPane (  ).add(enteredLabel, new GridBagConstraints(0, 9, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
		this.getContentPane (  ).add(dateLabel, new GridBagConstraints(0, 10, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
		this.getContentPane (  ).add(okButton, new GridBagConstraints(2, 11, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		//this.getContentPane (  ).add(imageNumberTextField, new GridBagConstraints(1, 0, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 77, 0));
		
		
		this.getContentPane (  ).add(fileNameLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
		this.getContentPane (  ).add(prename, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.getContentPane (  ).add(from, new GridBagConstraints(2, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.getContentPane (  ).add(firstImgNumber, new GridBagConstraints(3, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.getContentPane (  ).add(to, new GridBagConstraints(4, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.getContentPane (  ).add(lastImgNumber, new GridBagConstraints(5, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.getContentPane (  ).add(postname, new GridBagConstraints(6, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		
		this.getContentPane (  ).add(fileNameLabel2, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
		this.getContentPane (  ).add(prenameLabel, new GridBagConstraints(1, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.getContentPane (  ).add(from1, new GridBagConstraints(2, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.getContentPane (  ).add(firstImgNumberLabel, new GridBagConstraints(3, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.getContentPane (  ).add(to1, new GridBagConstraints(4, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.getContentPane (  ).add(lastImgNumberLabel, new GridBagConstraints(5, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.getContentPane (  ).add(postnameLabel, new GridBagConstraints(6, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		
		this.getContentPane (  ).add(directoryTextField, new GridBagConstraints(1, 3, 7, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.getContentPane (  ).add(wormTextField, new GridBagConstraints(1, 4, 7, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.getContentPane (  ).add(seriesTextField, new GridBagConstraints(1, 5, 7, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		//this.getContentPane (  ).add(printNumberTextField, new GridBagConstraints(1, 5, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		//this.getContentPane (  ).add(negativeNumberTextField, new GridBagConstraints(1, 7, 7, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		//this.getContentPane (  ).add(sectionNumberTextField, new GridBagConstraints(1, 7, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.getContentPane (  ).add(enteredByTextField, new GridBagConstraints(1, 9, 7, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.getContentPane (  ).add(dateEnteredTextField, new GridBagConstraints(1, 10, 7, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		this.getContentPane (  ).add(cancelButton, new GridBagConstraints(3, 11, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		
		this.getContentPane (  ).add(browseButton, new GridBagConstraints(-1, 3, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		
		
	}

	private void cancelButton_actionPerformed ( ActionEvent e )
	{
		//this.hide (  );
		this.dispose (  );
	}

	private void browseButton_actionPerformed ( ActionEvent e )
	{
		String str = getDirectoryFromUser();

		if ( str!=null )
		{
			File file = new File( str );
			directoryTextField.setText ( file.getAbsolutePath() );			
		}
	}

	private static class DirectoriesOnlyFilter extends FileFilter {
		public boolean accept(File f) {
			return f.isDirectory();
		}
		
		public String getDescription() {
			return "All Directories";
		}
	}
	
	private Map<String,String> backup(String... names) {
		Map<String,String> m=new HashMap<String,String>();
		for(String name: names) {
			m.put(name, (String) UIManager.get("FileChooser."+name));
		}
		return m;
	}
	
	private void restore(Map<String,String> map) {
		for(Map.Entry<String, String> e:map.entrySet()) {
			UIManager.put("FileChooser."+e.getKey(),e.getValue());
		}
		
	}
	private String getDirectoryFromUser() {
		
	    Map<String,String> backup = backup("filesOfTypeLabelText","fileNameLabelText","openButtonToolTipText");
		
		try {

			UIManager.put("FileChooser.filesOfTypeLabelText", "Types:");
			UIManager.put("FileChooser.fileNameLabelText", "Directory:");
			UIManager.put("FileChooser.openButtonToolTipText", "Select directory");

			JFileChooser chooser = new JFileChooser();

			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

			for (FileFilter ff : chooser.getChoosableFileFilters()) {
				chooser.removeChoosableFileFilter(ff);
			}

			chooser.setFileFilter(new DirectoriesOnlyFilter());

			chooser.setApproveButtonText("OK");

			int returnVal = chooser.showOpenDialog(null);

			if (returnVal != JFileChooser.APPROVE_OPTION)
				return null;

			File file = chooser.getSelectedFile();

			if (!file.exists()) {
				JOptionPane.showMessageDialog(null, "The file selected does not exist.\n Please select an existing file", "File does not exist",
						JOptionPane.INFORMATION_MESSAGE);

				return null;
			}

			return file.getAbsolutePath();
		} finally {
			restore(backup);
		}
	}
	
	/**
	 * Goes to the database and fetches the latest data from the Image table.
	 *
	 * @return A ResultSet object containing the latest information from the ImageTable.
	 *         In case of an error null is returned.
	 */
	public ResultSet getLatestData (  )
	{
		Connection con = null;
		ResultSet  rs = null;

		try
		{
			con = EDatabase.borrowConnection ( 
				    
				   
				    
				 );

			Statement stm = con.createStatement (  );
			rs = stm.executeQuery ( 
				    "SELECT ID, IMG_Number, IMG_File, IMG_Directory, "
				    + "IMG_Worm, IMG_Series, IMG_PrintNumber, IMG_NegativeNumber, "
				    + "IMG_SectionNumber, IMG_EnteredBy, IMG_DateEntered FROM image ORDER BY ID DESC"
				 );
		}
		catch ( Exception ex )
		{
			ex.printStackTrace (  );
		} finally {
			EDatabase.returnConnection(con);
		}



		return rs;
	}

	private void okButton_actionPerformed ( ActionEvent e )
	{
		Connection con = null;

		try
		{
			con = EDatabase.borrowConnection ( 
				    
				   
				    
				 );

			PreparedStatement pst =
				con.prepareStatement ( 
				    "select * from image where IMG_Directory = ? and IMG_File = ?"
				 );
			pst.setString ( 1, directoryTextField.getText (  ) );
			pst.setString ( 2, prename.getText (  )+firstImgNumber.getText (  )+postname.getText (  ) );

			ResultSet rs = pst.executeQuery (  );

			if ( rs.next (  ) )
			{
				JOptionPane.showMessageDialog ( 
				    null,
				    "There is already a file with the specified name in the database.\nPlease recheck the file chosen."
				 );

				return;
			}

			pst = con.prepareStatement ( 
				    "select * from image where IMG_Worm = ? and IMG_Series = ? and IMG_PrintNumber = ?"
				 );
			pst.setString ( 1, wormTextField.getText (  ) );
			pst.setString ( 2, seriesTextField.getText (  ) );
			pst.setString ( 3, firstImgNumber.getText (  ) );
			rs = pst.executeQuery (  );

			if ( rs.next (  ) )
			{
				JOptionPane.showMessageDialog ( 
				    null,
				    "There is already an image with the specified worm, series and print number in the database.\nPlease recheck the information submitted."
				 );

				return;
			}

			java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(  );

			format.applyPattern ( "yyyy-MM-dd" );

			Date date = new Date( System.currentTimeMillis (  ) );

			//java.util.Date              date    = format.parse( str1, new java.text.ParsePosition( 0 ) );
			//if ( date == null )
			//{
			//	JOptionPane.showMessageDialog( null, "Enter the date in the format dd/MM/yyyy", "Error", JOptionPane.ERROR_MESSAGE );
			//}
			try
			{
				date =
					new java.sql.Date( 
					    ( format.parse ( 
					        dateEnteredTextField.getText (  ),
					        new java.text.ParsePosition( 0 )
					     ) ).getTime (  )
					 );
			}
			catch ( NullPointerException npe )
			{
				JOptionPane.showMessageDialog ( 
				    null,
				    "Enter the date in the format yyyy-MM-dd",
				    "Error",
				    JOptionPane.ERROR_MESSAGE
				 );

				return;
			}

			
			if( firstImgNumber.getText (  ).equals(""))
			{
                JOptionPane.showMessageDialog ( 
				    null,
				    "You have not entered a value for the file name. Please check.",
				    "Error",
				    JOptionPane.ERROR_MESSAGE
				 );

				return;
            }
			if( directoryTextField.getText (  ).equals(""))
			{
                JOptionPane.showMessageDialog ( 
				    null,
				    "You have not entered a value for the directory name. Please check.",
				    "Error",
				    JOptionPane.ERROR_MESSAGE
				 );

				return;
            }
			if( wormTextField.getText (  ).equals(""))
			{
                JOptionPane.showMessageDialog ( 
				    null,
				    "You have not entered a value for the worm. Please check.",
				    "Error",
				    JOptionPane.ERROR_MESSAGE
				 );

				return;
            }
			if( seriesTextField.getText (  ).equals(""))
			{
                JOptionPane.showMessageDialog ( 
				    null,
				    "You have not entered a value for the series number. Please check.",
				    "Error",
				    JOptionPane.ERROR_MESSAGE
				 );

				return;
            }
			
		
			if( enteredByTextField.getText (  ).equals(""))
			{
                JOptionPane.showMessageDialog ( 
				    null,
				    "You have not entered a value for 'entered by'. Please check.",
				    "Error",
				    JOptionPane.ERROR_MESSAGE
				 );

				return;
            }
			
			int last = Integer.parseInt(lastImgNumber.getText (  )); 
			int first = Integer.parseInt(firstImgNumber.getText (  ));
			
			for (int ii=first;ii<=last;ii++)
			{
				
				pst = con.prepareStatement ( 
					    "insert into image (IMG_Number, IMG_File, "
					    + "IMG_Directory, IMG_Worm, IMG_Series, "
					    + "IMG_PrintNumber, IMG_NegativeNumber, "
					    + "IMG_SectionNumber, IMG_EnteredBy, "
					    + "IMG_DateEntered, IMG_zoomValue, IMG_brightnessValue, "
					    + "IMG_contrastValue, IMG_rotatedValue) values "
					    + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1.0, 0, 0, 0)"
					 );
				pst.setString ( 1, wormTextField.getText()+seriesTextField.getText (  )+ ii );
				
				if (ii<10) {pst.setString ( 1, wormTextField.getText()+seriesTextField.getText (  )+ "00"+ii  );}
				else if (ii<100) {pst.setString ( 1, wormTextField.getText()+seriesTextField.getText (  )+ "0"+ii );}
				else {pst.setString ( 1, wormTextField.getText()+seriesTextField.getText (  )+ ii );}
				
				if (ii<10) {pst.setString ( 2, prename.getText()+ "00"+ ii+postname.getText() );}
				else if (ii<100) {pst.setString ( 2, prename.getText()+"0"+ ii+postname.getText() );}
				else {pst.setString ( 2, prename.getText()+ ii+postname.getText() );}
				
				
				pst.setString ( 3, directoryTextField.getText (  ) );
				pst.setString ( 4, wormTextField.getText (  ) );
				pst.setString ( 5, seriesTextField.getText (  ) );
				pst.setString ( 6, ii+"" );
				pst.setString ( 7, "-1" );

				//			pst.setString ( 8, sectionNumberTextField.getText (  ) );
				pst.setInt ( 8, ii );
				pst.setString ( 9, enteredByTextField.getText (  ) );
				pst.setDate ( 10, date );

				pst.executeUpdate (  );	
			}
			
			
		
		} catch(com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException ue) {
			JOptionPane.showMessageDialog ( 
				    null,
				    "File already exists in database",
				    "Error",
				    JOptionPane.ERROR_MESSAGE
				 );

				return;
		}
		catch ( Exception ex )
		{
			ex.printStackTrace (  );
		} finally {
			EDatabase.returnConnection(con);
			this.dispose();

		}

	}

    private void modifyButton_actionPerformed(ActionEvent e)
    {
        Connection con = null;

		try
		{
			con = EDatabase.borrowConnection ( 
				    
				   
				    
				 );

			java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(  );

			format.applyPattern ( "yyyy-MM-dd" );

			Date date = new Date( System.currentTimeMillis (  ) );

			//java.util.Date              date    = format.parse( str1, new java.text.ParsePosition( 0 ) );
			//if ( date == null )
			//{
			//	JOptionPane.showMessageDialog( null, "Enter the date in the format dd/MM/yyyy", "Error", JOptionPane.ERROR_MESSAGE );
			//}
			try
			{
				date =
					new java.sql.Date( 
					    ( format.parse ( 
					        dateEnteredTextField.getText (  ),
					        new java.text.ParsePosition( 0 )
					     ) ).getTime (  )
					 );
			}
			catch ( NullPointerException npe )
			{
				JOptionPane.showMessageDialog ( 
				    null,
				    "Enter the date in the format yyyy-MM-dd",
				    "Error",
				    JOptionPane.ERROR_MESSAGE
				 );

				return;
			}

			int sectionNumber = 0;

		
		
			if( firstImgNumber.getText (  ).equals(""))
			{
                JOptionPane.showMessageDialog ( 
				    null,
				    "You have not entered a value for the file name. Please check.",
				    "Error",
				    JOptionPane.ERROR_MESSAGE
				 );

				return;
            }
			if( directoryTextField.getText (  ).equals(""))
			{
                JOptionPane.showMessageDialog ( 
				    null,
				    "You have not entered a value for the directory name. Please check.",
				    "Error",
				    JOptionPane.ERROR_MESSAGE
				 );

				return;
            }
			if( wormTextField.getText (  ).equals(""))
			{
                JOptionPane.showMessageDialog ( 
				    null,
				    "You have not entered a value for the worm. Please check.",
				    "Error",
				    JOptionPane.ERROR_MESSAGE
				 );

				return;
            }
			if( seriesTextField.getText (  ).equals(""))
			{
                JOptionPane.showMessageDialog ( 
				    null,
				    "You have not entered a value for the series number. Please check.",
				    "Error",
				    JOptionPane.ERROR_MESSAGE
				 );

				return;
            }
			/*
			if( negativeNumberTextField.getText (  ).equals(""))
            {
                JOptionPane.showMessageDialog ( 
				    null,
				    "You have not entered a value for the negative number. Please check.",
				    "Error",
				    JOptionPane.ERROR_MESSAGE
				 );

				return;
            }*/
			if( enteredByTextField.getText (  ).equals(""))
			{
                JOptionPane.showMessageDialog ( 
				    null,
				    "You have not entered a value for 'entered by'. Please check.",
				    "Error",
				    JOptionPane.ERROR_MESSAGE
				 );

				return;
            }
			
			PreparedStatement pst = con.prepareStatement ( 
				    "UPDATE image set IMG_File = ?, "
				    + "IMG_Directory = ?, IMG_Worm = ?, IMG_Series = ?, "
				    + "IMG_PrintNumber = ?, IMG_NegativeNumber = ?, "
				    + "IMG_SectionNumber = ?, IMG_EnteredBy = ?, "
				    + "IMG_DateEntered = ? WHERE IMG_Number = ?"
				 );
			pst.setString ( 1, prename.getText (  )+firstImgNumber.getText (  )+postname.getText (  ) );
			pst.setString ( 2, directoryTextField.getText (  ) );
			pst.setString ( 3, wormTextField.getText (  ) );
			pst.setString ( 4, seriesTextField.getText (  ) );
			pst.setString ( 5, firstImgNumber.getText (  ));
			//pst.setString ( 6, negativeNumberTextField.getText (  ) );
			pst.setString ( 6, "-1" );
			//			pst.setString ( 8, sectionNumberTextField.getText (  ) );
			pst.setInt ( 7, sectionNumber );
			pst.setString ( 8, enteredByTextField.getText (  ) );
			pst.setDate ( 9, date );
            pst.setString ( 10, wormTextField.getText()+seriesTextField.getText (  )+ firstImgNumber.getText (  ) );
			if ( pst.executeUpdate (  ) > 0 )
			{
				JOptionPane.showMessageDialog ( null, "Data entered successfully" );
			}
			else
			{
				JOptionPane.showMessageDialog ( null, "Data entry failed" );
			}
		}
		catch ( Exception ex )
		{
			ex.printStackTrace (  );
		} finally {
			EDatabase.returnConnection(con);
		}

		
		this.dispose();
    }
}
