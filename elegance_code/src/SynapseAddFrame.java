import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;


import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;


/**
 * The frame using which the data entry is done.
 *
 * @author zndavid
 * @version 1.0
 */
public class SynapseAddFrame
	extends JFrame
{
	
	private int synName;
	private String username,rate;
	Date date;
	private GridBagLayout gridBagLayout1          = new GridBagLayout(  );
	
	private JLabel        idLabel             = new JLabel(  );
	private JLabel        typeLabel          = new JLabel(  );
	private JLabel        preLabel               = new JLabel(  );
	private JLabel        postLabel             = new JLabel(  );
	
	private JLabel        userLabel            = new JLabel(  );
	private JLabel        dateLabel             = new JLabel(  );
	
	private JButton       addButton                = new JButton(  );
	private JTextField    idTextField    = new JTextField(  );
	private JTextField    typeTextField       = new JTextField(  );
	private JTextField    preTextField      = new JTextField(  );
	private JTextField    postTextField           = new JTextField(  );
	private JLabel        infoLabel           = new JLabel(  );
	private JButton       cancelButton            = new JButton(  );
	private JLabel   rateLabel           = new JLabel(  );
    private ButtonGroup rateGroup = new ButtonGroup();
	
	private JRadioButton weakButton = new JRadioButton("low");
	
	private JRadioButton normalButton = new JRadioButton("medium");

	
	private JRadioButton strongButton = new JRadioButton("high");


	/**
	 * Creates a new ImageEntryFrame object.
	 */
	public SynapseAddFrame ( int synN, String userN )
	{
		this.synName = synN;
		this.username = userN;
		this.date = new Date( System.currentTimeMillis (  ) );
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
		this.setTitle ( "Add The Synaps Data" );
		this.setSize ( new Dimension( 500, 200 ) );
		this.getContentPane (  ).setLayout ( gridBagLayout1 );
		idLabel.setText("Synapse ID: "+ synName);
		infoLabel.setText(username+"                "+date);
		typeLabel.setText ( "Type" );
		preLabel.setText ( "Pre" );
		postLabel.setText ( "Post" );
		rateLabel.setText ("Certainty:");

		
		idLabel.setBackground ( new Color( 206, 206, 230 ) );
        
		

		addButton.setText ( "ADD" );
		addButton.addActionListener ( 
		    new ActionListener(  )
			{
				public void actionPerformed ( ActionEvent e )
				{
					addButton_actionPerformed ( e );
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


		 final ItemListener rateListener = new ItemListener() {
                    public void itemStateChanged( ItemEvent e )
                        {
                        if (weakButton.isSelected())
                        {
							rate = "weak";
                        }
						if (normalButton.isSelected())
                        {
							rate = "normal";
                        }
						if (strongButton.isSelected())
                        {
							rate = "strong";
                        }
						}
                        
                };

        weakButton.addItemListener( rateListener );
	    normalButton.addItemListener( rateListener );
	    strongButton.addItemListener( rateListener );

		rateGroup.add(weakButton);
		rateGroup.add(normalButton);
		rateGroup.add(strongButton);

	     this.getContentPane (  ).add(idLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));

		 this.getContentPane (  ).add(typeLabel,     new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
		 this.getContentPane (  ).add(typeTextField, new GridBagConstraints(1, 1, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		 this.getContentPane (  ).add(preLabel,      new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
		 this.getContentPane (  ).add(preTextField,  new GridBagConstraints(1, 2, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		 this.getContentPane (  ).add(postLabel, new GridBagConstraints    (0, 3, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
		 this.getContentPane (  ).add(postTextField, new GridBagConstraints(1, 3, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

		 this.getContentPane (  ).add(infoLabel, new GridBagConstraints(0, 5, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));


         	 this.getContentPane (  ).add(rateLabel, new GridBagConstraints(0, 4, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
		 this.getContentPane (  ).add(weakButton, new GridBagConstraints(1, 4, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
		 this.getContentPane (  ).add(normalButton, new GridBagConstraints(2, 4, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));
		 this.getContentPane (  ).add(strongButton, new GridBagConstraints(3, 4, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 20, 0, 0), 0, 0));


		 this.getContentPane (  ).add(addButton,    new GridBagConstraints(0, 6, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		 this.getContentPane (  ).add(cancelButton, new GridBagConstraints(2, 6, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0)); 	
	}	
	private void cancelButton_actionPerformed ( ActionEvent e )
	{
		//this.hide (  );
		this.dispose (  );
	}

	

	private void addButton_actionPerformed ( ActionEvent e )
	{
		Connection con = null;

		try
		{
			con = EDatabase.borrowConnection ( 
				    
				   
				    
				 );

			
			

			

			
			
			PreparedStatement pst = con.prepareStatement ( 
				       "insert into synrecord (OBJ_Name, type, fromObj, toObj, username, DateEntered,rate) values "
				    + "(?, ?, ?, ?, ?, ?,?)"
				 );
			pst.setInt ( 1, synName );
			pst.setString ( 2, typeTextField.getText (  ) );
			pst.setString ( 3, preTextField.getText (  ) );
			pst.setString ( 4, postTextField.getText (  ) );
			pst.setString ( 5, username );
			pst.setDate ( 6, date );
			pst.setString ( 7, rate );
			
			

			if ( pst.executeUpdate (  ) > 0 )
			{
				JOptionPane.showMessageDialog ( null, "Data Add successfully" );
			}
			else
			{
				JOptionPane.showMessageDialog ( null, "Data Add failed" );
			}
		}
		catch ( Exception ex )
		{
			ex.printStackTrace (  );
		}finally {
			EDatabase.returnConnection(con);
		}

		
	}
}

   