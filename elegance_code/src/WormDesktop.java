import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyEventPostProcessor;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.geom.Area;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.awt.AlphaComposite;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;


/**
 * This class extends the JDesktopPane class to provide the extra functionality for
 * drawing the relations etc. The paint method is overridden here to provide the drawing
 * functionaliyt. This class also implements the KeyEventPostProcessor interface to
 * provide additional functionality in handling repaints after the delete key is
 * pressed.
 *
 * @author zndavid
 * @version 1.0
 */
public class WormDesktop
	extends JDesktopPane
	implements KeyEventPostProcessor
{
	Hashtable origins;
	Hashtable locations;
	Hashtable dimensions;
	Hashtable indices;
    Vector layers = new Vector();
    //Area clippedArea;
    Hashtable unClippedAreas;
    String selectedFrameImageNumber;
	//private static int count=0;
	public WormDesktop (  )
	{
		super(  );
		KeyboardFocusManager.getCurrentKeyboardFocusManager (  ).addKeyEventPostProcessor ( 
		    this
		 );
	}

	/**
	 * This function is the only function required to implement the KeyEventPostProcessor
	 * interface. It handles the events after the 'Delete' key is pressed.
	 *
	 * @param e a KeyEvent object
	 *
	 * @return returns false signifying that this event may be passed on to the next
	 *         KeyEventPostProcessor if any.
	 */
	public boolean postProcessKeyEvent ( KeyEvent e )
	{
		if ( e.getKeyCode (  ) == KeyEvent.VK_DELETE )
		{
			this.repaint (  );
		}

		return false;
	}

    /**
	 * This function overrides the paint method in JDesktopPane. This override helps to
	 * provide the drawing functionality.
	 *
	 * @param g the Graphics object associated with this component
	 */
	public void paint ( Graphics g )
	{
		super.paint ( g );
        JInternalFrame jif = getSelectedFrame();
        if(jif == null ) selectedFrameImageNumber = "";
        else selectedFrameImageNumber = ((ImageDisplayFrame)jif).getImageNumber();
        //calculateClippedArea();
		if ( ApplicationProperties.showAllRelations == true )
		{
			paintContins ( g );
		}
		//Util.info("did a paint for the " + count + " th time");
		//count ++;
	}

	private void paintContins ( Graphics g )
	{
		
		if (Elegance.filterOptions.isHideAll() || Elegance.filterOptions.getContinFilterType() == FilterOptions.ContinFilterType.none  ) return;
		
		//Util.info("trying to paint " + ImageDisplayPanel.listOfRelations.size());
		Graphics2D g2 = ( Graphics2D ) g;

		//g2.setPaint ( Color.YELLOW );
		g2.setStroke ( new BasicStroke( 2 ) );
		setOriginAndLocationData (  );
        calculateUnClippedAreas();

        Set<Integer> continNumbsToShow=null;
        
        if (
				Elegance.filterOptions.getContinFilterType() != FilterOptions.ContinFilterType.all 						
			) {
        	continNumbsToShow = new HashSet<Integer>(Arrays.asList(Elegance.filterOptions.getContinNums()));
		}
        
        
		Set<Integer> allContinNumbs = new HashSet<Integer>();
		for ( int i = 0; i < ImageDisplayPanel.listOfRelations.size (  ); i++ ) {
			Relation rel = ( Relation ) ImageDisplayPanel.listOfRelations.elementAt ( i );			
			allContinNumbs.add(rel.getObj1().continNumber);
		}
		
		Map<Integer, Contin> allContins = EDatabase.getContins(allContinNumbs);
        
		for ( int i = 0; i < ImageDisplayPanel.listOfRelations.size (  ); i++ )
		{
				
				Relation rel = ( Relation ) ImageDisplayPanel.listOfRelations.elementAt ( i );
				
				if (continNumbsToShow!=null) {
					if (!continNumbsToShow.contains(rel.getObj1().continNumber)) continue;//hide relations if they are disabled in "Filters (ctrl-d) control"
				}
				
				String continName=null;
				Contin contin=allContins.get(rel.getObj1().continNumber);
				if (contin!=null) continName=contin.getName();
				
				
				drawOneRelation (rel, g2, continName ); 
			  
		}
	}

	private void drawOneRelation ( 
	    Relation rel,
	    Graphics2D g,
	    String continName
	 )
	{
		//Point p1 = getPoint(rel.getObj1());
		//Point p2 = getPoint(rel.getObj2());
		Point p1 = getScreenPoint ( rel.getObj1 (  ) );
		Point p2 = getScreenPoint ( rel.getObj2 (  ) );

		if ( ( p1 == null ) || ( p2 == null ) )
		{
			return;
		}

		    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3F));
			g.setPaint ( new Color( 0, 0, 153 ));
			g.drawLine ( p1.x, p1.y, p2.x, p2.y );
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0F));
            Point locationOfLabel = new Point((p1.x+p2.x)/2, (p1.y+p2.y)/2);
            g.setColor(new Color( 0, 0, 153 ));
			String relStr = rel.getObj1().continNumber + " " + continName+" ("+rel.segmentNum+")";
            g.drawString(relStr, locationOfLabel.x, locationOfLabel.y);
		
	}

	private void setOriginAndLocationData (  )
	{
		origins        = new Hashtable(  );
		locations      = new Hashtable(  );
		dimensions     = new Hashtable(  );
		indices        = new Hashtable(  );

		JInternalFrame [] jifs = getAllFrames (  );

		for ( int i = 0; i < jifs.length; i++ )
		{
			ImageDisplayFrame idf = ( ImageDisplayFrame ) jifs [ i ];

			//if ( idf.isIcon (  ) )
			//{
			//	continue;
			//}

			//if ( idf.isClosed (  ) )
			//{
				//continue;
			//}

			ImageDisplayPanel idp = idf.getImageDisplayPanel (  );
			locations.put ( idf.imageNumber, idf.getLocation (  ) );
			dimensions.put ( idf.imageNumber, idf.getSize (  ) );
			origins.put ( 
			    idf.imageNumber,
			    new Point( idp.getXOrigin (  ), idp.getYOrigin (  ) )
			 );
			indices.put ( idf.imageNumber, new Integer( i ) );
		}
	}

	private Point getPoint ( CellObject obj1 )
	{
		String    imgNo    = obj1.imageNumber;
		Point     origin   = ( Point ) origins.get ( imgNo );
		Point     location = ( Point ) locations.get ( imgNo );
		Dimension dim      = ( Dimension ) dimensions.get ( imgNo );

		if ( ( origin == null ) || ( location == null ) || ( dim == null ) )
		{
			//Util.info("hashtable value unavailable");
			return null;
		}

		Point returnPoint = new Point(  );

		returnPoint.x     = obj1.p.x + origin.x;
		returnPoint.y     = obj1.p.y + origin.y;

		//Util.info("return point = "+ returnPoint);
		if ( ( returnPoint.x < 0 ) || ( returnPoint.y < 0 ) )
		{
			//Util.info("returnPoint out of range below 0");
			return null;
		}

		//Util.info("width = " + dim.getWidth() + " height = " + dim.getHeight());
		if ( 
		    ( returnPoint.x > dim.getWidth (  ) )
			    || ( returnPoint.y > ( dim.getHeight (  ) - 45 ) )
		 ) //45 25 for frame title and 20 for odometer 
		{
			//Util.info("returnPoint out of range above max");
			return null;
		}

		returnPoint.x += ( location.x + 5 );
		returnPoint.y += ( location.y + 25 );

		return returnPoint;
	}

	private Point getScreenPoint ( CellObject obj )
	{
		
		
		
		String    imgNo    = obj.imageNumber;
		if (locations==null || imgNo==null) return null;
		Point     location = ( Point ) locations.get ( imgNo );
		Dimension dim      = ( Dimension ) dimensions.get ( imgNo );
		Integer   index    = ( Integer ) indices.get ( imgNo );

		if ( index == null )
		{
			return null;
		}

		ImageDisplayPanel idp = (ImageDisplayPanel) ((ImageDisplayFrame) (getAllFrames()[index.intValue()])).getImageDisplayPanel();
		
		Point             returnPoint = idp.getImagePointAsScreenPoint ( obj.p );

		if ( ( returnPoint.x < 0 ) || ( returnPoint.y < 0 ) )
		{
			return null;
		}

		if ( 
		    ( 
				    returnPoint.x > ( dim.getWidth (  ) - 10 )
			     ) //5 for each border of the JInternal frame
			    
			    || ( returnPoint.y > ( dim.getHeight (  ) - 50 ) )
		 ) //50 - 25 for frame title and 25 for odometer 
		{
			return null;
		}

        Area unclippedArea = (Area)unClippedAreas.get(imgNo);
        Point actualPoint = (Point) returnPoint.clone();
        actualPoint.translate(location.x, location.y);
        if(unclippedArea== null ||  (!unclippedArea.contains(actualPoint)))
        {
            //if(unclippedArea == null ) Util.info("returning null as unclipped area = null");
            //else Util.info("returning null as unclipped area does not contain point");
            return null;
        }
        /*if(selectedFrameImageNumber.compareToIgnoreCase(imgNo) != 0)
        {
            //Util.info(imgNo + " is not selected frame");
            Rectangle rect = clippedArea.getBounds();
            //Util.info("bounds = "+rect);
            if(rect.width >0 && rect.height > 0)
            {
                Point actualPoint = (Point) returnPoint.clone();
                actualPoint.translate(location.x, location.y);
                if(clippedArea.contains(actualPoint))
                {
                    //System.out.print("returning null from clipped area for object " + obj);
                    return null;
                }   
            }
        }*/
        //else
        //{
            //Util.info("selected imgno = "+imgNo);
        //}
		returnPoint.x += ( location.x + 5 );
		returnPoint.y += ( location.y + 25 );

		return returnPoint;
	}

	private void drawPresynaptic ( 
	    Point      p1,
	    Point      p2,
	    Graphics2D g
	 )
	{
		double x     = p2.x - p1.x;
		double y     = p2.y - p1.y;
		double len   = Math.sqrt ( ( x * x ) + ( y * y ) );

		//int    xAdd1 = ( int ) ( y / len * 7.0 );
		//int    yAdd1 = ( int ) ( x / len * 7.0 );
		//int    xAdd2 = ( int ) ( x / len * 20.0 );
		//int    yAdd2 = ( int ) ( y / len * 20.0 );
		//int    xSub  = ( int ) ( x / len * 20.0 );
		//int    ySub  = ( int ) ( y / len * 20.0 );

        int    xAdd1 = ( int ) ( y / len * Math.min( 7.0, len * 0.07 ) );
		int    yAdd1 = ( int ) ( x / len * Math.min( 7.0, len * 0.07 ) );
		int    xAdd2 = ( int ) ( x / len * Math.min( 20.0, len * 0.2 ) );
		int    yAdd2 = ( int ) ( y / len * Math.min( 20.0, len * 0.2 ) );
		int    xSub  = ( int ) ( x / len * Math.min( 15.0, len * 0.1 ) );
		int    ySub  = ( int ) ( y / len * Math.min( 15.0, len * 0.1 ) );
		
		p2.x -= xSub;
		p2.y -= ySub;
		g.drawLine ( p1.x, p1.y, p2.x, p2.y );
		g.drawLine ( p2.x, p2.y, ( p2.x + xAdd1 ) - xAdd2, p2.y - yAdd1 - yAdd2 );
		g.drawLine ( p2.x, p2.y, p2.x - xAdd1 - xAdd2, ( p2.y + yAdd1 ) - yAdd2 );
	}

	private void drawGap ( 
	    Point      p1,
	    Point      p2,
	    Graphics2D g
	 )
	{
		double x    = p2.x - p1.x;
		double y    = p2.y - p1.y;
		double len  = Math.sqrt ( ( x * x ) + ( y * y ) );
		//int    yAdd = ( int ) ( x / len * 10.0 );
		//int    xAdd = ( int ) ( y / len * 10.0 );
		//int    xSub = ( int ) ( x / len * 20.0 );
		//int    ySub = ( int ) ( y / len * 20.0 );

        int    yAdd = ( int ) ( x / len * Math.min( 10.0, len * 0.1 ) );
		int    xAdd = ( int ) ( y / len * Math.min( 10.0, len * 0.1 ) );
		int    xSub = ( int ) ( x / len * Math.min( 20.0, len * 0.2 ) );
		int    ySub = ( int ) ( y / len * Math.min( 20.0, len * 0.2 ) );

		p1.x += xSub;
		p1.y += ySub;
		p2.x -= xSub;
		p2.y -= ySub;
		g.drawLine ( p1.x, p1.y, p2.x, p2.y );
		g.drawLine ( p1.x + xAdd, p1.y - yAdd, p1.x - xAdd, p1.y + yAdd );
		g.drawLine ( p2.x + xAdd, p2.y - yAdd, p2.x - xAdd, p2.y + yAdd );
	}
    /*private void calculateClippedArea()
    {
        clippedArea = null;
        clippedArea = new Area();
        JInternalFrame[] allFrames = getAllFrames();
        //for (int i = 0; i < allFrames.length; i++) 
        //{
        //    Point loc = allFrames[i].getLocation();
        //    Dimension dim = allFrames[i].getSize();
        //    Rectangle rect = new Rectangle(loc, dim);
        //    unclippedArea.exclusiveOr(new Area(rect));
        //}
        //Util.info("unclippedAreaBOUNDS = " + unclippedArea.getBounds());
        //Util.info("is singular = " + unclippedArea.isSingular());
        for (int i = 0; i < allFrames.length-1; i++) 
        {
            if(allFrames[i].isClosed()) continue;
            for(int j=i+1; j<allFrames.length; j++)
            {
                if(allFrames[j].isClosed()) continue;
                Point loc1 = allFrames[i].getLocation();
                Dimension dim1 = allFrames[i].getSize();
                Rectangle rect1 = new Rectangle(loc1, dim1);

                Point loc2 = allFrames[j].getLocation();
                Dimension dim2 = allFrames[j].getSize();
                Rectangle rect2 = new Rectangle(loc2, dim2);

                Area area1 = new Area(rect1);
                area1.intersect(new Area(rect2));
                clippedArea.add(area1);
                //Util.info("i frame rect = "+ rect1);
                //Util.info("j frame rect = "+ rect2);
            }
            
        }
        JInternalFrame jif = getSelectedFrame();
        if(jif != null)
        {
            Point loc1 = jif.getLocation();
            Dimension dim1 = jif.getSize();
            Rectangle rect1 = new Rectangle(loc1, dim1);
            clippedArea.add(new Area(rect1));
            //Util.info("sel frame rect = "+ rect1);
        }
        //Util.info("unclippedAreaBOUNDS = " + unclippedArea.getBounds());
        //Util.info("is singular = " + clippedArea.isSingular());
        //Util.info("is rectangular = " + clippedArea.isRectangular());
        //Util.info("is polygonal = " + clippedArea.isPolygonal());
    }*/
    private void calculateUnClippedAreas()
    {
        unClippedAreas = new Hashtable();
        for (int i = 0; i < layers.size(); i++) 
        {
            String imgNum1 = (String) layers.elementAt(i);
            Object obj = indices.get(imgNum1);
            if(obj == null) continue;
            JInternalFrame jif1 = (getAllFrames())[((Integer)indices.get(imgNum1)).intValue()];
            Area area;
            if(jif1.isIcon() || jif1.isClosed())
            {
                area = new Area();
            }
            else
            {
                Point loc1 = (Point)locations.get(imgNum1);
                Dimension dim1 = (Dimension)dimensions.get(imgNum1);
                Rectangle rect1 = new Rectangle(loc1, dim1);
                area = new Area(rect1);
             
                for (int j = i+1; j < layers.size(); j++) 
                {
                    String imgNum2 = (String)layers.elementAt(j);
                    Object obj2 = indices.get(imgNum2);
                    if(obj2 == null) continue;
                    JInternalFrame jif2 = (getAllFrames())[((Integer)indices.get(imgNum2)).intValue()];
                    Area area2;
                    if(jif2.isIcon() || jif2.isClosed())
                    {
                        area2 = new Area();
                    }
                    else
                    {
                        Point loc2 = (Point)locations.get(imgNum2);
                        Dimension dim2 = (Dimension)dimensions.get(imgNum2);
                        Rectangle rect2 = new Rectangle(loc2, dim2);
                        area2 = new Area(rect2);
                    }
                    area.subtract(area2);
                }
            }
            //if(area == null) Util.info("area = null");
            //else Util.info("area != null");
            unClippedAreas.put(imgNum1, area);
         }
    }

}
