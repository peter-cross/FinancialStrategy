package models;

import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Vector;

import entities.TAcct;
import entities.TrActn;

import static interfaces.Constants.HEIGHT;
import static interfaces.Constants.WIDTH;

/**
 * Class TractnsGraphics - graphic tools for drawing T-accts and transactions
 * @author Peter Cross
 *
 */
public class TractnsGraphics extends Canvas
{
	public static final int  COLS = 28, // Number of columns
			 				 ROWS = 16; // Number of rows

	private static final int STROKE = 1; // Line stroke
	
	private static final int SHIFT_X = 1 + STROKE, // Shift on canvas horizontally
					 		 SHIFT_Y = 4 + STROKE; // Shift on canvas vertically	
	
	private static double 	 CELL_WIDTH,  	// cell width
				 			 CELL_HEIGHT; 	// cell height

	private GraphicsContext  gc;			// To store graphic context
	private Stage			 owner;			// To store parent window object
	
	/**
	 * Class constructor
	 * @param owner Parent window object
	 */
	public TractnsGraphics( Stage owner )
	{
		// Create canvas object
		super( WIDTH*0.99, HEIGHT*0.80 );
		
		// Calculate cell width and height
		CELL_WIDTH  = (getWidth() - SHIFT_X - 2)/COLS;
		CELL_HEIGHT = (getHeight() - SHIFT_Y - 2)/ROWS;
		
		// Get 2D graphic context from inherited class
		gc = getGraphicsContext2D();
		this.owner = owner;
	}

	/**
	 * Return Parent window object
	 */
	public Stage getOwner()
	{
		return owner;
	}
	
	/**
	 * Returns cell width
	 */
	public static double  getCellWidth()
	{
		return CELL_WIDTH;
	}
	
	/**
	 * Returns cell height
	 */
	public static double getCellHeight()
	{
		return CELL_HEIGHT;
	}
	
	/**
	 * Draws grid on the canvas
	 */
	public void drawGrid()
	{
		// Clear whole canvas
     	gc.clearRect( 0, 0, getWidth(), getHeight() );
         	
     	// Assign stroke color, stroke size and fill color
		gc.setStroke( Color.LAVENDER );
		gc.setLineWidth( STROKE );
        gc.setFill( Color.MINTCREAM );
        
        // Begin drawing path
		gc.beginPath();
		
		// Draw vertical and horizontal lines
		drawHorizontalLines();
		drawVerticalLines();
		
		// Display lines that were drawn
		displayDrawnLines();
	}
	
	/**
	 * Draws horizontal lines on canvas
	 */
	private void drawHorizontalLines()
	{
		// Loop for each grid row
		for ( int r = 0; r < ROWS; r++ )
		{
			// Draw horizontal line for the width of the window
			gc.moveTo( 0, SHIFT_Y + r*CELL_HEIGHT  );
			gc.lineTo( getWidth(), SHIFT_Y + r*CELL_HEIGHT ) ;
		}
		
		// Draw final horizontal line
		gc.moveTo( 0, SHIFT_Y + ROWS*CELL_HEIGHT  );
		gc.lineTo( getWidth(), SHIFT_Y + ROWS*CELL_HEIGHT ) ;
	}
	
	/**
	 * Displays lines drawn on canvas
	 */
	public void displayDrawnLines()
	{
		// Close drawing path
		gc.closePath();
		
		// Display drawn lines
		gc.stroke();
	}
	
	/**
	 * Draws vertical lines
	 */
	private void drawVerticalLines()
	{
		// Loop for each grid column
		for ( int c = 0; c < COLS; c++ )
		{
			// Draw vertical line for current column
			gc.moveTo( SHIFT_X + c*CELL_WIDTH , SHIFT_Y + 1 );
			gc.lineTo( SHIFT_X + c*CELL_WIDTH , getHeight() );
		}
		
		// Draw final vertical line
		gc.moveTo( SHIFT_X + COLS*CELL_WIDTH , SHIFT_Y + 1 );
		gc.lineTo( SHIFT_X + COLS*CELL_WIDTH , getHeight() );
	}
	
	/**
	 * Draws T-Acct on canvas
	 */
	public void drawTAcct( int row, int col )
	{
		setTAcctDrawingSettings();
		
		drawTAcctHorizontalLine( row, col );
		
		drawTAcctVerticalLine( row, col );
		
		displayDrawnLines();
	}
	
	/**
	 * Draw horizontal line for T-acct
	 * @param row Row of T-acct
	 * @param col Column of T-acct
	 */
	private void drawTAcctHorizontalLine( int row, int col )
	{
		// Draw horizontal line for T-Acct
		gc.moveTo( SHIFT_X + col*CELL_WIDTH  + CELL_WIDTH*0.06, SHIFT_Y + row*CELL_HEIGHT + CELL_HEIGHT*0.3 ) ;
		gc.lineTo( SHIFT_X + col*CELL_WIDTH  + CELL_WIDTH*0.94, SHIFT_Y + row*CELL_HEIGHT + CELL_HEIGHT*0.3 );
	}
	
	/**
	 * Draws vertical line for T-acct
	 * @param row Row of T-acct
	 * @param col Column of T-acct
	 */
	private void drawTAcctVerticalLine( int row, int col )
	{
		// Draw vertical line for T-account
		gc.moveTo( SHIFT_X + col*CELL_WIDTH + CELL_WIDTH*0.5, SHIFT_Y + row*CELL_HEIGHT + CELL_HEIGHT*0.3 ) ;
		gc.lineTo( SHIFT_X + col*CELL_WIDTH + CELL_WIDTH*0.5, SHIFT_Y + (row+1)*CELL_HEIGHT ) ;
	}
	
	/**
	 * Sets drawing settings for drawing T-acct on canvas
	 */
	public void setTAcctDrawingSettings()
	{
		// Set stroke and line width for drawing
		gc.setStroke( Color.BROWN );
        gc.setLineWidth( STROKE*4 );
        
        // Start drawing path
        gc.beginPath();
	}
	
	/**
	 * Draws Vertical Line for T-Acct for the height of the cell
	 * @param row Row number
	 * @param col Column number
	 */
	public void drawAcctVerticalLine( int row, int col )
	{
		// Set drawing settings for drawing T-acct
		setTAcctDrawingSettings();
		
		// Draw vertical line for T-acct
		gc.moveTo( SHIFT_X + col*CELL_WIDTH + CELL_WIDTH*0.5, SHIFT_Y - 2*STROKE + row*CELL_HEIGHT ) ;
		gc.lineTo( SHIFT_X + col*CELL_WIDTH + CELL_WIDTH*0.5, SHIFT_Y - 2*STROKE + (row+1)*CELL_HEIGHT ) ;
		
		// Display drawn lines
		displayDrawnLines();
	}
	
	/**
	 * Draws Horizontal transaction line
	 * @param row Row number
	 * @param col Column number
	 */
	public void drawHorizontalTractnLine( int row, int col )
	{
		// Draw horizontal line for transaction
		gc.moveTo( SHIFT_X - 2*STROKE + col*CELL_WIDTH, SHIFT_Y + row*CELL_HEIGHT + CELL_HEIGHT*0.8 ) ;
		gc.lineTo( SHIFT_X + 2*STROKE + (col+1)*CELL_WIDTH, SHIFT_Y + row*CELL_HEIGHT + CELL_HEIGHT*0.8 ) ;
	}
	
	/**
	 * Draws text on canvas
	 * @param text Text string to draw
	 * @param row Row to start
	 * @param col Column to start
	 * @param heightRatio Height ratio coefficient to specify height within starting cell
	 */
	public void drawText( String text, int row, int col, double heightRatio )
	{
		// Calculate X coordinate where to display text
		double x = SHIFT_X + col*CELL_WIDTH + STROKE;
		
		// Calculate Y coordinate where to display text
		double y = SHIFT_Y + row*CELL_HEIGHT + CELL_HEIGHT*heightRatio + STROKE;
		
		// Set fill color for the text
		gc.setFill( Color.BLACK );
		
		// Display text in specified coordinates
        gc.fillText( text, x, y );
	}
	
	/**
	 * Draws text on canvas
	 * @param text Text string to draw
	 * @param row Row to start
	 * @param col Column to start
	 */
	public void drawText( String text, int row, int col )
	{
		drawText( text, row, col, 0 );
	}
	
	/**
	 * Clears cell content
	 * @param row Cell row
	 * @param col Cell column
	 */
	public void clearCellContent( int row, int col )
	{
		if ( row < 0 || col < 0 )
			return;
		
		// Clear rectangle area
		gc.clearRect( SHIFT_X + col*CELL_WIDTH, SHIFT_Y + row*CELL_HEIGHT, CELL_WIDTH + STROKE, CELL_HEIGHT );
		
		// Set stroke color and size
		gc.setStroke( Color.LAVENDER );
        gc.setLineWidth( STROKE );
        
        gc.beginPath();
        
        // Draw top horizontal line
        gc.moveTo( SHIFT_X + col*CELL_WIDTH , SHIFT_Y + row*CELL_HEIGHT );
		gc.lineTo( SHIFT_X + (col+1)*CELL_WIDTH , SHIFT_Y + row*CELL_HEIGHT );
		
		// Draw left vertical line
		gc.moveTo( SHIFT_X + col*CELL_WIDTH , SHIFT_Y + row*CELL_HEIGHT );
		gc.lineTo( SHIFT_X + col*CELL_WIDTH , SHIFT_Y + STROKE + (row+1)*CELL_HEIGHT );
		
		// Draw right vertical line
		gc.moveTo( SHIFT_X + (col+1)*CELL_WIDTH , SHIFT_Y + row*CELL_HEIGHT );
		gc.lineTo( SHIFT_X + (col+1)*CELL_WIDTH , SHIFT_Y + STROKE + (row+1)*CELL_HEIGHT );
		
		// Draw bottom horizontal line
        gc.moveTo( SHIFT_X + col*CELL_WIDTH , SHIFT_Y + (row+1)*CELL_HEIGHT );
		gc.lineTo( SHIFT_X + (col+1)*CELL_WIDTH , SHIFT_Y + (row+1)*CELL_HEIGHT );
		
		displayDrawnLines();
	}
	
	/**
	 * Draws T-acct in highlighted color
	 * @param row Row
	 * @param col Column
	 */
	public void hightlightCell( int row, int col )
	{
		// Set stroke color and line width
		gc.setStroke( Color.CADETBLUE );
		gc.setLineWidth( STROKE*4 );
      
		// Start drawing path
		gc.beginPath();
        
		drawTAcctHorizontalLine( row, col );
		drawTAcctVerticalLine( row, col );
		
		displayDrawnLines();
	}
	
	/**
	 * Draws T-acct with transaction left part
	 * @param row Row where to draw
	 * @param col Column where to draw
	 */
	public void drawTractnLeftTAcct( int row, int col )
	{
		clearCellContent( row, col );
		
		setTAcctDrawingSettings();
		
		drawTAcctHorizontalLine( row, col );
		
		// Draw vertical line for T-acct
		gc.moveTo( SHIFT_X + col*CELL_WIDTH + CELL_WIDTH*0.5, SHIFT_Y + row*CELL_HEIGHT + CELL_HEIGHT*0.3 ) ;
		gc.lineTo( SHIFT_X + col*CELL_WIDTH + CELL_WIDTH*0.5, SHIFT_Y - 2*STROKE + (row+1)*CELL_HEIGHT ) ;
	
		// Draw horizontal line for transaction
		gc.moveTo( SHIFT_X + col*CELL_WIDTH + CELL_WIDTH*0.6, SHIFT_Y + row*CELL_HEIGHT + CELL_HEIGHT*0.8 ) ;
		gc.lineTo( SHIFT_X + 2*STROKE + (col+1)*CELL_WIDTH, SHIFT_Y + row*CELL_HEIGHT + CELL_HEIGHT*0.8 ) ;
		
		displayDrawnLines();
	}
	
	/**
	 * Draws transaction left part without T-acct
	 * @param row1 Row where to start
	 * @param row2 Row where to finish
	 * @param col Column
	 */
	public void drawTractnLeftPart( int row1, int row2, int col )
	{
		if ( row1 > row2 )
			return;
		
		clearCellContent( row2, col );
		
		setTAcctDrawingSettings();
		
        // Loop for each row of T-Acct
		for ( int r = row1; r <= row2; r++ )
        {
        	// Draw vertical line for T-acct
    		gc.moveTo( SHIFT_X + col*CELL_WIDTH + CELL_WIDTH*0.5, SHIFT_Y - 2*STROKE + r*CELL_HEIGHT ) ;
    		gc.lineTo( SHIFT_X + col*CELL_WIDTH + CELL_WIDTH*0.5, SHIFT_Y - 2*STROKE + (r+1)*CELL_HEIGHT ) ;
    	}
        
		// Draw horizontal line for transaction
		gc.moveTo( SHIFT_X + col*CELL_WIDTH + CELL_WIDTH*0.6, SHIFT_Y + row2*CELL_HEIGHT + CELL_HEIGHT*0.8 ) ;
		gc.lineTo( SHIFT_X + 2*STROKE + (col+1)*CELL_WIDTH, SHIFT_Y + row2*CELL_HEIGHT + CELL_HEIGHT*0.8 ) ;
		
		displayDrawnLines();
	}
	
	/**
	 * Draws T-acct with transaction right part
	 * @param row Row where to draw
	 * @param col Column where to draw
	 */
	public void drawTractnRightTAcct( int row, int col )
	{
		clearCellContent( row, col );
		
		setTAcctDrawingSettings();
		
		drawTAcctHorizontalLine( row, col );
		
		// Draw vertical line for T-acct
		gc.moveTo( SHIFT_X + col*CELL_WIDTH + CELL_WIDTH*0.5, SHIFT_Y + row*CELL_HEIGHT + CELL_HEIGHT*0.3 ) ;
		gc.lineTo( SHIFT_X + col*CELL_WIDTH + CELL_WIDTH*0.5, SHIFT_Y - 2*STROKE + (row+1)*CELL_HEIGHT ) ;
	
		// Draw horizontal line for transaction
		gc.moveTo( SHIFT_X - 2*STROKE + col*CELL_WIDTH, SHIFT_Y + row*CELL_HEIGHT + CELL_HEIGHT*0.8 ) ;
		gc.lineTo( SHIFT_X + col*CELL_WIDTH + CELL_WIDTH*0.4, SHIFT_Y + row*CELL_HEIGHT + CELL_HEIGHT*0.8 ) ;
		
		displayDrawnLines();
	}
	
	/**
	 * Draws T-acct with right and left parts of transaction
	 * @param row Row where to draw
	 * @param col Column where to draw
	 */
	public void drawTractnTAcct( int row, int col )
	{
		clearCellContent( row, col );
		
		setTAcctDrawingSettings();
		
		drawTAcctHorizontalLine( row, col );
		
		// Draw vertical line for T-acct
		gc.moveTo( SHIFT_X + col*CELL_WIDTH + CELL_WIDTH*0.5, SHIFT_Y + row*CELL_HEIGHT + CELL_HEIGHT*0.3 ) ;
		gc.lineTo( SHIFT_X + col*CELL_WIDTH + CELL_WIDTH*0.5, SHIFT_Y - 2*STROKE + (row+1)*CELL_HEIGHT ) ;
	
		// Draw horizontal line for Left transaction
		gc.moveTo( SHIFT_X - 2*STROKE + col*CELL_WIDTH, SHIFT_Y + row*CELL_HEIGHT + CELL_HEIGHT*0.8 ) ;
		gc.lineTo( SHIFT_X - 2*STROKE + col*CELL_WIDTH + CELL_WIDTH*0.4, SHIFT_Y + row*CELL_HEIGHT + CELL_HEIGHT*0.8 ) ;
		
		// Draw horizontal line for Right transaction
		gc.moveTo( SHIFT_X + col*CELL_WIDTH + CELL_WIDTH*0.6, SHIFT_Y + row*CELL_HEIGHT + CELL_HEIGHT*0.8 ) ;
		gc.lineTo( SHIFT_X + 2*STROKE + (col+1)*CELL_WIDTH, SHIFT_Y + row*CELL_HEIGHT + CELL_HEIGHT*0.8 ) ;
		
		displayDrawnLines();
	}
	
	/**
	 * Draws transaction right part without T-acct
	 * @param row1 Row where to start
	 * @param row2 Row where to finish
	 * @param col Column
	 */
	public void drawTractnRightPart( int row1, int row2, int col )
	{
		if ( row1 > row2 )
			return;
		
		clearCellContent( row2, col );
		
		setTAcctDrawingSettings();
		
		// Loop for each row of T-Acct
        for ( int r = row1; r <= row2; r++ )
        {
        	// Draw vertical line for T-acct
    		gc.moveTo( SHIFT_X + col*CELL_WIDTH + CELL_WIDTH*0.5, SHIFT_Y - 2*STROKE + r*CELL_HEIGHT ) ;
    		gc.lineTo( SHIFT_X + col*CELL_WIDTH + CELL_WIDTH*0.5, SHIFT_Y - 2*STROKE + (r+1)*CELL_HEIGHT ) ;
    	}
        
		// Draw horizontal line for transaction
		gc.moveTo( SHIFT_X - 2*STROKE + col*CELL_WIDTH, SHIFT_Y + row2*CELL_HEIGHT + CELL_HEIGHT*0.8 ) ;
		gc.lineTo( SHIFT_X + col*CELL_WIDTH + CELL_WIDTH*0.4, SHIFT_Y + row2*CELL_HEIGHT + CELL_HEIGHT*0.8 ) ;
		
		displayDrawnLines();
	}
	
	/**
	 * Draws transaction right and left parts without T-acct
	 * @param row1 Row where to start
	 * @param row2 Row where to finish
	 * @param col Column
	 */
	public void drawTractnTwoWayPart( int row1, int row2, int col )
	{
		if ( row1 > row2 )
			return;
		
		// Loop for each row of T-acct
		for ( int r = row1; r <= row2; r++ )
	        clearCellContent( r, col );
		
		setTAcctDrawingSettings();
		
		// Loop for each row of T-acct
		for ( int r = row1; r <= row2; r++ )
        {
        	// Draw vertical line for T-acct
    		gc.moveTo( SHIFT_X + col*CELL_WIDTH + CELL_WIDTH*0.5, SHIFT_Y + r*CELL_HEIGHT ) ;
    		gc.lineTo( SHIFT_X + col*CELL_WIDTH + CELL_WIDTH*0.5, SHIFT_Y - 2*STROKE + (r+1)*CELL_HEIGHT ) ;
    	}
        
        // Draw horizontal line for Left transaction
        gc.moveTo( SHIFT_X + col*CELL_WIDTH, SHIFT_Y + row2*CELL_HEIGHT + CELL_HEIGHT*0.8 ) ;
		gc.lineTo( SHIFT_X + col*CELL_WIDTH + CELL_WIDTH*0.4, SHIFT_Y + row2*CELL_HEIGHT + CELL_HEIGHT*0.8 ) ;
		
		// Draw horizontal line for Right transaction
		gc.moveTo( SHIFT_X - 2*STROKE + col*CELL_WIDTH + CELL_WIDTH*0.6, SHIFT_Y + row2*CELL_HEIGHT + CELL_HEIGHT*0.8 ) ;
		gc.lineTo( SHIFT_X + (col+1)*CELL_WIDTH, SHIFT_Y + row2*CELL_HEIGHT + CELL_HEIGHT*0.8 ) ;
		
		displayDrawnLines();
	}
	
	/**
	 * Draws middle part of transaction
	 * @param row Row where to draw
	 * @param col1 Column where to start
	 * @param col2 Column where to finish
	 */
	public void drawTractnMiddlePart( int row, int col1, int col2 )
	{
		setTAcctDrawingSettings();
		        
        // Draw horizontal line for transaction
		gc.moveTo( SHIFT_X + col1*CELL_WIDTH, SHIFT_Y + row*CELL_HEIGHT + CELL_HEIGHT*0.8 ) ;
		gc.lineTo( SHIFT_X + (col2+1)*CELL_WIDTH, SHIFT_Y + row*CELL_HEIGHT + CELL_HEIGHT*0.8 ) ;
		
		displayDrawnLines();      
	}
	
	/**
	 * Draws transaction description on canvas
	 * @param Description TrActn description
	 * @param row Cell row
	 * @param col Cell column
	 */
	private void drawTractnDescription( String description, int row, int col )
	{
		drawText( description, row, col, 0.6 );
	}
	
	/**
	 * Draws transit transaction for specified row and T-acct
	 * @param row Row of transit transaction
	 */
	public void drawTransitTractn( int row, int col, Vector<TrActn> transactions )
	{
		setTAcctDrawingSettings();
		
		// Loop for each transaction from the list 
		for ( TrActn t : transactions  )
		{
			int trRow = t.getRow();
			TAcct trCx = t.getCx();
			TAcct trDx = t.getDx();
					
			// if transaction is in specified row and specified column is between transactions Cx and Dx columns
			if ( trRow == row && (col > trCx.getColumn() && col < trDx.getColumn()) )
			{
				// In specified row and column draw transaction horizontal line
				drawHorizontalTractnLine( row, col );
				break;
			}
		}
		
		displayDrawnLines();
	}
} // End of class ** TractnsGraphics **