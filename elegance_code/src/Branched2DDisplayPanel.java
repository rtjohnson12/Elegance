import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import java.util.Iterator;
import java.util.TreeSet;
import javax.swing.JOptionPane;
import java.util.Vector;

import java.sql.*;

public class Branched2DDisplayPanel
	extends GraphicalDisplayPanel
{
	private static final int               EXTRA_SPACE           = 25;
	private static final int               BORDER                = 5;
	private static final int               WORM_LINE_THICKNESS   = 3;
	private static final int               SYMBOL_LINE_THICKNESS = 1;
	private static final int               SYMBOL_LINE_LENGTH    = 30;
    private static final int               OBJECT_CIRCLE_RADIUS  = 2;
    private static final int               CONTIN_PIPE_RADIUS  = 3;
    private static final int               MINIMUM_SPACE_TO_BE_LEFT_FOR_MARKUP  = 100;
    private static final Font              NUMBER_FONT           =
		new Font( "Default", Font.BOLD, 12 );
	private static final Font              HEADING_FONT =
		new Font( "Monospaced", Font.BOLD, 18 );
	private static final FontRenderContext FONT_RENDER_CTXT =
		new FontRenderContext( null, false, false );
	//private static final Font              SYMBOL_FONT =
	//	NUMBER_FONT.deriveFont ( 
	//	    AffineTransform.getRotateInstance ( Math.toRadians ( -90.0 ) )
	//	 );
    private static final Font              SYMBOL_FONT = NUMBER_FONT;
    private Vector listOfRelations;
    private boolean horizontal = true;
    private int maxYOfObject=0, minYOfObject=1000000, maxZOfObject=0, minZOfObject=1000000;
    private float xScale = 1;
    private float yScale = 1;
    private String continName = "";
    private int continNumber;
    private int fromLocationX = 0;
    private int toLocationX = 0;
    private int toLocationY = 0;
    //private int currentSynapseDrawingLocationX = 0;
    //private int currentSynapseDrawingLocationY = 0;
        
    

    public Branched2DDisplayPanel(int newContinNumber)
    {
        continNumber = newContinNumber;
        listOfRelations = getListOfExtendedRelations(continNumber);
        //for (int i = 0; i < listOfRelations.size(); i++) 
        //{
        //    Util.info(listOfRelations.elementAt(i));
        //}
        continName = Utilities.getContinName(new Integer(continNumber));
        continNameText = continName;
        preferredTitle = "2-Dimensional Graphical Display - Contin " + continName;
        this.repaint();
    }
    public boolean isDrawable()
    {
        return true;
    }
    public void changeVisualization (  )
	{
		horizontal = !horizontal;
		repaint (  );
	}
    public void paint ( Graphics g )
	{
        if(listOfRelations.size() <= 0) return;
        //if(to == from || to-from <=0) return;
		Graphics2D g2;

		if ( g instanceof Graphics2D )
		{
			g2 = ( Graphics2D ) g;
		}
		else
		{
			return;
		}

		if ( !horizontal )
		{
			g2.translate ( this.getWidth (  ) / 2.0f, this.getHeight (  ) / 2.0f );
			g2.rotate ( Math.toRadians ( 90 ) );
			g2.translate ( -this.getHeight (  ) / 2.0f, -this.getWidth (  ) / 2.0f );
        }
        
        //clearArea(g2);
        Vector listOfObjects = getListOfObjects();
        if(listOfObjects.size() == 0) return;
        for (int i = 0; i < listOfObjects.size(); i++) 
        {
            ExtendedCellObject excell = (ExtendedCellObject) listOfObjects.elementAt(i);
            if(excell.p.y > maxYOfObject) maxYOfObject = excell.p.y;
            if(excell.p.y < minYOfObject) minYOfObject = excell.p.y;
            if(excell.sectionNumber > maxZOfObject) maxZOfObject = excell.sectionNumber;
            if(excell.sectionNumber < minZOfObject) minZOfObject = excell.sectionNumber;
            //Util.info(listOfObjects.elementAt(i));
        }
        drawLabelledHorizontalLine(g2);
        xScale = (float)(toLocationX-fromLocationX)/ (float)(maxZOfObject - minZOfObject);
        //yScale = (float)(getHeight() - 2*EXTRA_SPACE - MINIMUM_SPACE_TO_BE_LEFT_FOR_MARKUP) / (float)(maxYOfObject - minYOfObject);
        //yScale = (float)(getHeight() - 2*EXTRA_SPACE - MINIMUM_SPACE_TO_BE_LEFT_FOR_MARKUP) / (float)(maxYOfObject);
        yScale = (float)(toLocationY - EXTRA_SPACE - MINIMUM_SPACE_TO_BE_LEFT_FOR_MARKUP) / (float)(maxYOfObject);
        paintAllRelations(g2);
    }
    public Vector getListOfExtendedRelations(int continNumber)
    {
        Vector     returner = new Vector(  );
		Connection con = null;

		try
		{
			con = EDatabase.borrowConnection ( 
				    
				   
				    
				 );

			PreparedStatement pst =
				con.prepareStatement ( 
				    "Select REL_ImgNumber1, REL_ObjName1, REL_ImgNumber2, REL_ObjName2,  REL_Type from relationship where ( REL_ConNumber1 = ? OR REL_ConNumber2 = ? ) AND REL_Type = ?"
				 );
			pst.setInt ( 1, continNumber );
			pst.setInt ( 2, continNumber );
			pst.setString ( 3, GlobalStrings.CONTINUOUS );

			ResultSet rs = pst.executeQuery (  );

			while ( rs.next (  ) )
			{
				ExtendedCellObject obj1 = new ExtendedCellObject(rs.getString("REL_ImgNumber1"), rs.getString("REL_ObjName1"));
                ExtendedCellObject obj2 = new ExtendedCellObject(rs.getString("REL_ImgNumber2"), rs.getString("REL_ObjName2"));
                ExtendedRelation exrel = new ExtendedRelation(obj1, obj2, rs.getString("REL_Type"));
                returner.addElement(exrel);
			}

			
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

		
		}finally {
			EDatabase.returnConnection(con);
		}

		return returner;
    }
    private Vector getListOfObjects()
    {
        Vector returner = new Vector();
        for (int i = 0; i < listOfRelations.size(); i++) 
        {
            ExtendedRelation exrel = (ExtendedRelation) listOfRelations.elementAt(i);
            if(exrel.obj1 != null && returner.contains(exrel.obj1) == false)
            {
                returner.addElement(exrel.obj1);
            }
            if(exrel.obj2 != null && returner.contains(exrel.obj2) == false)
            {
                returner.addElement(exrel.obj2);
            }
        }
        return returner;
    }
    private void clearArea(Graphics2D g2)
    {
        g2.setBackground(Color.WHITE);
        g2.clearRect(getX(), getY(), getWidth(), getHeight());
    }
    private void paintAllObjects(Vector listOfObjects, Graphics2D g2)
    {
        for (int i = 0; i < listOfObjects.size(); i++) 
        {
            ExtendedCellObject excell = (ExtendedCellObject) listOfObjects.elementAt(i);
            int xLoc = (int)((excell.sectionNumber - minZOfObject)*xScale);
            int yLoc = (int)((excell.p.y - minYOfObject)*yScale);
        }
    }
    private void paintAllRelations(Graphics2D g2)
    {
        for (int i = 0; i < listOfRelations.size(); i++) 
        {
            ExtendedRelation exrel = (ExtendedRelation) listOfRelations.elementAt(i);
            Point p1 = paintObject(exrel.obj1, g2);
            Point p2 = paintObject(exrel.obj2, g2);
            g2.setStroke(new BasicStroke(SYMBOL_LINE_THICKNESS));
            g2.drawLine(p1.x, p1.y-CONTIN_PIPE_RADIUS, p2.x, p2.y-CONTIN_PIPE_RADIUS);
            g2.drawLine(p1.x, p1.y+CONTIN_PIPE_RADIUS, p2.x, p2.y+CONTIN_PIPE_RADIUS);
            drawSynapses(exrel.obj1, p1, g2);
            drawSynapses(exrel.obj2, p2, g2);
        }
    }
    private Point paintObject(ExtendedCellObject excell, Graphics2D g2)
    {
        int xCoor = (int)((excell.sectionNumber - minZOfObject)*xScale);
        //int yCoor = (int)((excell.p.y - minYOfObject)*yScale);
        int yCoor = (int)((excell.p.y)*yScale);

        int xLoc = xCoor + fromLocationX;
        int yLoc = toLocationY - yCoor;

        g2.fillOval(xLoc-OBJECT_CIRCLE_RADIUS, yLoc - OBJECT_CIRCLE_RADIUS, 2*OBJECT_CIRCLE_RADIUS, 2*OBJECT_CIRCLE_RADIUS);
        return new Point(xLoc, yLoc);
    }
    private void drawLabelledHorizontalLine ( Graphics2D g2 )
	{
		int panelWidth  = this.getWidth (  );
		int panelHeight = this.getHeight (  );

		if ( !horizontal )
		{
			panelWidth      = this.getHeight (  );
			panelHeight     = this.getWidth (  );
		}

		g2.setColor ( Color.WHITE );
		g2.fillRect ( 0, 0, panelWidth, panelHeight );

		if ( 
		    ( panelHeight <= ( 2 * EXTRA_SPACE ) )
			    || ( panelWidth <= ( 2 * EXTRA_SPACE ) )
		 )
		{
			return;
		}

		panelWidth -= ( 2 * EXTRA_SPACE );
		panelHeight -= ( 2 * EXTRA_SPACE );

		toLocationY = panelHeight - EXTRA_SPACE;

		//Util.info("toLocationY = "+toLocationY);
		g2.setColor ( Color.black );
		g2.setStroke ( new BasicStroke( WORM_LINE_THICKNESS ) );
		g2.setFont ( NUMBER_FONT );

		//String            fromText     = "" + from;
        String            fromText     = "" + minZOfObject;
		TextLayout        tl           =
			new TextLayout( fromText, NUMBER_FONT, FONT_RENDER_CTXT );
		Rectangle2D.Float bound        = ( Rectangle2D.Float ) tl.getBounds (  );
		int               xAdjustment1 = ( BORDER * 2 ) + ( int ) bound.width;

		//int yAdjustment1 = BORDER + (int) (bound.height/2.0f);
		int yAdjustment1 = ( int ) ( bound.height / 2.0f );

		tl.draw ( g2, EXTRA_SPACE + BORDER, toLocationY + yAdjustment1 );

		String toText = "" + maxZOfObject;
		tl        = new TextLayout( toText, NUMBER_FONT, FONT_RENDER_CTXT );
		bound     = ( Rectangle2D.Float ) tl.getBounds (  );

		int xAdjustment2 = ( BORDER * 2 ) + ( int ) bound.width;
		int yAdjustment2 = ( int ) ( bound.height / 2.0f );

		tl.draw ( 
		    g2,
		    ( EXTRA_SPACE + panelWidth ) - xAdjustment2,
		    toLocationY + yAdjustment2
		 );

		g2.drawLine ( 
		    EXTRA_SPACE + xAdjustment1,
		    toLocationY,
		    ( EXTRA_SPACE + panelWidth ) - xAdjustment2 - BORDER,
		    toLocationY
		 );

		fromLocationX     = EXTRA_SPACE + xAdjustment1;
		toLocationX       = ( EXTRA_SPACE + panelWidth ) - xAdjustment2 - BORDER;

		String headerText = continName.equals ( "" ) ? ( "" + continNumber ) : continName;
		tl        = new TextLayout( headerText, HEADING_FONT, FONT_RENDER_CTXT );
		bound     = ( Rectangle2D.Float ) tl.getBounds (  );

		int xAdjustment3 = ( int ) ( bound.width / 2.0f );
		int yAdjustment3 = ( int ) ( bound.height / 2.0f );

		tl.draw ( 
		    g2,
		    ( panelWidth / 2 ) - xAdjustment3,
		    toLocationY + EXTRA_SPACE + yAdjustment3
		 );
	}
    private void drawSynapses(ExtendedCellObject excell, Point loc, Graphics2D g2)
    {
        Vector listOfSynapses = excell.getListOfSynapses();
        int currentSynapseDrawingLocationX = loc.x;
        int currentSynapseDrawingLocationY = loc.y - BORDER;
        for (int i = 0; i < listOfSynapses.size(); i++) 
        {
            //Util.info(listOfSynapses.elementAt(i));
            BranchedContinSynpase bcs = (BranchedContinSynpase) listOfSynapses.elementAt(i);
            Point fromPoint = new Point(currentSynapseDrawingLocationX, currentSynapseDrawingLocationY);
            Point toPoint = new Point(currentSynapseDrawingLocationX, currentSynapseDrawingLocationY - SYMBOL_LINE_LENGTH);
            g2.setStroke(new BasicStroke(SYMBOL_LINE_THICKNESS));
            if(bcs.type.compareToIgnoreCase(GlobalStrings.PRESYNAPTIC) == 0 || bcs.type.compareToIgnoreCase(GlobalStrings.MULTIPLE_PRESYNAPTIC) == 0)
            {
                drawPresynaptic(fromPoint, toPoint, g2);
            }
            else if(bcs.type.compareToIgnoreCase(GlobalStrings.POSTSYNAPTIC) == 0 || bcs.type.compareToIgnoreCase(GlobalStrings.MULTIPLE_POSTSYNAPTIC) == 0)
            {
                drawPresynaptic(toPoint, fromPoint, g2);
            }
            else if(bcs.type.compareToIgnoreCase(GlobalStrings.GAP_JUNCTION) == 0)
            {
                drawGap(toPoint, fromPoint, g2);
            }
            else
            {
                g2.drawLine(fromPoint.x, fromPoint.y, toPoint.x, toPoint.y);
            }
            int textHeight = (int) drawText(bcs.continName, toPoint.x, toPoint.y, g2);
            currentSynapseDrawingLocationY -= 2*BORDER + SYMBOL_LINE_LENGTH + textHeight;
            
        }
        
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
		int    xAdd1 = ( int ) ( y / len * 3.0 );
		int    yAdd1 = ( int ) ( x / len * 3.0 );

		int    xAdd2 = ( int ) ( x / len * 6.0 );
		int    yAdd2 = ( int ) ( y / len * 6.0 );
		int    xSub  = ( int ) ( x / len * 2.0 );
		int    ySub  = ( int ) ( y / len * 2.0 );

		//int xAdd2 = ( int ) ( x / len * 12.0 );
		//int yAdd2 = ( int ) ( y / len * 12.0 );
		//int xSub  = ( int ) ( x / len * 7.0 );
		//int ySub  = ( int ) ( y / len * 7.0 );
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
		double x   = p2.x - p1.x;
		double y   = p2.y - p1.y;
		double len = Math.sqrt ( ( x * x ) + ( y * y ) );

		//int    yAdd = ( int ) ( x / len * 10.0 );
		//int    xAdd = ( int ) ( y / len * 10.0 );
		//int    xSub = ( int ) ( x / len * 20.0 );
		//int    ySub = ( int ) ( y / len * 20.0 );
		int yAdd = ( int ) ( x / len * 6.0 );
		int xAdd = ( int ) ( y / len * 6.0 );
		int xSub = ( int ) ( x / len * 2.0 );
		int ySub = ( int ) ( y / len * 2.0 );
		p1.x += xSub;
		p1.y += ySub;
		p2.x -= xSub;
		p2.y -= ySub;
		g.drawLine ( p1.x, p1.y, p2.x, p2.y );
		g.drawLine ( p1.x + xAdd, p1.y - yAdd, p1.x - xAdd, p1.y + yAdd );
		g.drawLine ( p2.x + xAdd, p2.y - yAdd, p2.x - xAdd, p2.y + yAdd );
	}
    private float drawText ( 
	    String     text,
        int horizontalLocationOfDrawingPoint,
        int verticcalLocationOfDrawingPoint,
	    Graphics2D g2
	 )
	{
		if ( ( text == null ) || text.equals ( "" ) )
		{
			return 0;
		}

		if ( horizontalLocationOfDrawingPoint <= 0 )
		{
			return 0;
		}

		TextLayout        tl    = new TextLayout( text, SYMBOL_FONT, FONT_RENDER_CTXT );
		Rectangle2D.Float bound = ( Rectangle2D.Float ) tl.getBounds (  );

		float xAdjustment = bound.width / 2.0f;

        float horLoc = horizontalLocationOfDrawingPoint - xAdjustment > 0 ? horizontalLocationOfDrawingPoint - xAdjustment : 1;
		tl.draw ( 
		    g2,
		    horLoc,
		    verticcalLocationOfDrawingPoint - WORM_LINE_THICKNESS - 1
		 );

		//tl.draw(g2, 100.0f, 100.0f);
		//return bound.height;
		//tl        = new TextLayout( text, NUMBER_FONT, FONT_RENDER_CTXT );
		//bound     = ( Rectangle2D.Float ) tl.getBounds (  );

		//Util.info("bound = " + bound);
		return bound.height;
	}
}
