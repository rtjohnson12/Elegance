import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

import javax.media.jai.JAI;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaSizeName;

import javax.swing.JOptionPane;
import javax.swing.JPanel;


/**
 * This abstract class extends the JPanel class and is used for 1DDisplay and 2DDisplay.
 *
 * @author zndavid
 * @version 1.0
 */
public abstract class GraphicalDisplayPanel
	extends JPanel
	implements Printable
{
	private BufferedImage bim       = null;
	private boolean       firstTime = true;
    protected String preferredTitle = "";
    protected String continNameText = "";

	//private static int MARGIN = 50;

	/**
	 * Prints the panel using a printer. This functions uses the new Printing API of java
	 * and can be run only on java version 1.4 onwards.
	 */
	public void print (  )
	{
		DocFlavor                flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
		Doc                      doc  = new SimpleDoc( this, flavor, null );
		PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet(  );

		//aset.add(Finishings.EDGE_STITCH_LEFT);
		//aset.add(Finishings.EDGE_STITCH_TOP);
		aset.add ( MediaSizeName.ISO_A4 );

		//PrintService[] services = PrintServiceLookup.lookupPrintServices(flavor, null);
		PrintService [] services =
			PrintServiceLookup.lookupPrintServices ( flavor, aset );

		if ( services.length > 0 )
		{
			DocPrintJob job = services [ 0 ].createPrintJob (  );
			ELog.info ( "printer being used = " + services [ 0 ].getName (  ) );

			try
			{
				job.print ( doc, aset );
			}
			catch ( PrintException pe ) {}
		}

		firstTime = true;
	}

	/**
	 * Saves the drawing on the panel as a JPEG image.
	 */
	public void save (  )
	{
		//String fileName = Utilities.getFileNameFromUser ( "GraphicalDisplay.jpg" );
        String fileName = "";
        if(this instanceof OneDDisplayPanel)
            fileName = Utilities.getFileNameFromUser ( "1D_Contin_" + continNameText + ".jpg" );
        else
            fileName = Utilities.getFileNameFromUser ( "2D_Contin_" + continNameText + ".jpg" );

		if ( ( fileName == null ) || fileName.equals ( "" ) )
		{
			return;
		}
        BufferedImage bim1 = new BufferedImage( 
				    this.getWidth (  ),
				    this.getHeight (  ),
				    BufferedImage.TYPE_INT_RGB
				 );

		Graphics2D g2 = ( Graphics2D ) bim1.getGraphics (  );
		paint ( g2 );
		JAI.create ( "filestore", bim1, fileName, "JPEG" );
		JOptionPane.showMessageDialog ( null, "The image was successfully saved" );
	}

	/**
	 * Changes the visualization.
	 */
	

    public abstract boolean isDrawable();

	/**
	 * This function is required to implement the Printable interface.
	 *
	 * @param graphics The graphics associated with the printer
	 * @param pageFormat a Page format object with information about the format of the
	 *        page
	 * @param pageIndex a zero based index of the page number.
	 *
	 * @return PAGE_EXISTS if the page exists and the printing is done properly and
	 *         NO_SUCH_PAGE otherwise.
	 *
	 * @throws PrinterException Any exception while printing is thrown,
	 *
	 */
	public int print ( 
	    Graphics   graphics,
	    PageFormat pageFormat,
	    int        pageIndex
	 )
		throws PrinterException
	{
		//if ( firstTime )
		//{
			bim = new BufferedImage( 
				    this.getWidth (  ),
				    this.getHeight (  ),
				    BufferedImage.TYPE_INT_RGB
				 );

			Graphics2D g2 = ( Graphics2D ) bim.getGraphics (  );
			paint ( g2 );
			//firstTime = false;
			//Util.info ( 
			//    "image Width = " + bim.getWidth (  ) + "height= " + bim.getHeight (  )
			// );
			//Util.info ( 
			//    "page Width = " + pageFormat.getImageableWidth (  ) + "height= "
			//    + pageFormat.getImageableHeight (  )
			// );
		//}

		int pageWidth  = ( int ) pageFormat.getImageableWidth (  );
		int pageHeight = ( int ) pageFormat.getImageableHeight (  );

		//int pageWidth = (int)pageFormat.getImageableWidth() - MARGIN;
		//int pageHeight = (int)pageFormat.getImageableHeight()- MARGIN;
		int numberOfHorizontalPagesPossible =
			( int ) Math.ceil ( ( double ) bim.getWidth (  ) / ( double ) pageWidth );
		int numberOfVerticalPagesPossible =
			( int ) Math.ceil ( ( double ) bim.getHeight (  ) / ( double ) pageHeight );
		int totalNumberOfPages =
			numberOfHorizontalPagesPossible * numberOfVerticalPagesPossible;

		if ( pageIndex >= totalNumberOfPages )
		{
			return Printable.NO_SUCH_PAGE;
		}
		else
		{
			int horizontalPageNumber = pageIndex % numberOfHorizontalPagesPossible;
			int verticalPageNumber = pageIndex / numberOfHorizontalPagesPossible;
			int horizontalPixelNo  = horizontalPageNumber * pageWidth;
			int verticalPixelNo    = verticalPageNumber * pageHeight;

			if ( 
			    ( horizontalPixelNo > bim.getWidth (  ) )
				    || ( verticalPixelNo > bim.getHeight (  ) )
			 )
			{
				return Printable.NO_SUCH_PAGE;
			}

			int           widthOfSubImage =
				Math.min ( bim.getWidth (  ) - horizontalPixelNo, pageWidth );
			int           heightOfSubImage =
				Math.min ( bim.getHeight (  ) - verticalPixelNo, pageHeight );
			BufferedImage sub =
				bim.getSubimage ( 
				    horizontalPixelNo,
				    verticalPixelNo,
				    widthOfSubImage,
				    heightOfSubImage
				 );
			( ( Graphics2D ) graphics ).drawRenderedImage ( 
			    sub,
			    AffineTransform.getTranslateInstance ( 
			        pageFormat.getImageableX (  ),
			        pageFormat.getImageableY (  )
			     )
			 );

			return Printable.PAGE_EXISTS;
		}
	}

    public String getPreferredTitle()
    {
        return preferredTitle;
    }

    public void setPreferredTitle(String newPreferredTitle)
    {
        preferredTitle = newPreferredTitle;
    }
}
