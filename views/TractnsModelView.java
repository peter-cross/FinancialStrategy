package views;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.util.Vector;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import application.Database;
import application.Main;
import entities.ChOfAccs;
import entities.HashMap;
import entities.GL;
import entities.LglEntity;
import entities.TAcct;
import entities.TrActn;
import entities.TractnsModel;
import models.ChOfAccsModel;
import models.GLAcctModel;
import models.LglEntityModel;
import models.TractnsGraphics;
import foundation.AssociativeList;
import foundation.TaskTimer;
import foundation.UserDialog;
import interfaces.Utilities;

import static interfaces.Utilities.enterTAcctInfo;
import static interfaces.Utilities.hash;

/**
 * Class TractnsSimulationModel - Modeling accting tractns
 * @author Peter Cross
 *
 */
public class TractnsModelView extends NodeView implements Utilities
{
	public static final int  COLS = 28, // Number of columns
							 ROWS = 16; // Number of rows
	
	private static final int STROKE = 1; 			// Line stroke
	private static final int SHIFT_X = 1 + STROKE,	// +7 Shift on canvas horizontally
			  				 SHIFT_Y = 4 + STROKE; 	// +16 Shift on canvas vertically	
	private static double 	 CELL_WIDTH,  			// Cell width
							 CELL_HEIGHT; 			// Cell height
	
	private static TractnsModel				tractnsModel;		// To store tractns Model entity object
	private static Vector<TrActn>			tractns;			// To store all tractns
	private static Vector<TAcct>			accts;				// To store T-accts
	private LglEntity						lglEntity;			// Legal entity of Tractns Model
	
	private static ArrayList<TAcct>			toAddTAccts;		// T-Accts to add
	private static ArrayList<TAcct>			toDelTAccts;		// T-Accts to delete
	private static ArrayList<TrActn>		toAddTractns;		// Tractns to add
	private static ArrayList<TrActn>		toDelTractns;		// Tractns to delete
	
	private ArrayList<TAcct>				selectedTAccts;		// To store selected T-accts
	private AssociativeList					output;				// Results of input to pass
	private TextField 						title;				// Model Title
	
	private long 			lastMouseClickTime = 0l;// To store time stamp when last mouse click happened 
	private MouseEvent 		mouseEvent;				// To store last mouse event
	private MouseEventType  eventType;				// To store last mouse event type
	
	private boolean 		newAbout = false;		// To indicate that About box was displayed for new model
	private String[]		tabs;					// Tab names
    
	private TabPane 		tabPane;				// TabPane object for document with multiple tabs
	private AssociativeList fields;
	
	private ArrayList<ArrayList<TAcct>>[] grid; 	// To store cell accts as a grid
	private TractnsGraphics[]			  tg;		// Graphics object for displaying tractns
	
	private static String tractnsModelField = "tractnsModel";
	
	/**
	 * Class default constructor
	 */
	private TractnsModelView()
	{
		super( hash("TransModel") );
		
		createArrays();
		
		// Create TextField for the model title
		title = new TextField();
		title.setPrefWidth( 300 );
	}
	
	/**
	 * Class constructor
	 * @param fields Stored fields of the document
	 */
	private TractnsModelView( AssociativeList fields )
	{
		this();
		
		this.fields = fields;
		
		setLglEntity();
		
		// Set dialog graphics
		setGraphics();
		
		// Invoke initialization
		init();
	
		setTractnsModel();
		
		if ( tractnsModel == null )
			return;
		
		setAccountsAndTractns();
		
		// Restore grid and Model's Tractns
		restoreGridAndTractns();
	}
	
	/**
	 * Sets Tractns Model for the view
	 */
	private void setTractnsModel()
	{
		// Get Tractns Model
		tractnsModel = fields.get( tractnsModelField );
		
		if ( tractnsModel == null )
			return;
		
		// Get name of Tractns Model
		String titleTxt = tractnsModel.getName();
		
		// Set title of the model on dialog form
		if ( titleTxt != null && !titleTxt.isEmpty() )
			title.setText( titleTxt );
		
		// Set Tractns Model as field for output
		output.set( tractnsModelField, tractnsModel );
	}
	
	/**
	 * Sets Legal Entity for model and Tabs for Legal Entity ChOfAccs
	 */
	private void setLglEntity()
	{
		// Try to get Legal Entity database entity object from specified associative list
		lglEntity = fields.get( "lglEntity" );
		
		// If Legal Entity database entity object is specified
		if ( lglEntity != null )
		{
			// Get Legal Entity Model by Legal Entity database entity object
			LglEntityModel entityModel = LglEntityModel.getByEntity( lglEntity );
			
			// If Legal Entity Model is specified
			if ( entityModel != null )
				// Get ChOfAccs names  from Legal Entity Model
				tabs = entityModel.getChartNames();
			else
				// Get ChOfAccs names  from Legal Entity database entity object
				tabs = lglEntity.getChOfAccs();
	    }
		else
        	tabs = new String[] {};
	}
	
	/**
	 * Gets T-Accts and Tractns for Tractns Model 
	 */
	private void setAccountsAndTractns()
	{
		if ( tractnsModel == null )
			return;
				
		// Get tractns of Tractns Model
		Vector<TrActn> trs = tractnsModel.getTractns();
				
		if ( trs != null )
			tractns = trs;
		
		// Get T-accts of Tractns Model
		Vector<TAcct> accts = tractnsModel.getTAccs();
				
		if ( accts != null )
			this.accts = accts;
	}
	
	/**
	 * Sets Graphics context for Tractns Model
	 */
	private void setGraphics()
	{
		int tgSize = Math.max( 1, tabs.length );
		
		tg = new TractnsGraphics[tgSize];
		
		for ( int i = 0; i < tgSize; i++ )
        {
            // Create Tractns Graphics object
            tg[i] = new TractnsGraphics(this);
                
            // Add event handler for Mouse Click event
            tg[i].addEventHandler( MouseEvent.MOUSE_CLICKED, this::clickedEventHandler );
        }
		
		// Get cell width and height
		CELL_WIDTH  = TractnsGraphics.getCellWidth();
		CELL_HEIGHT = TractnsGraphics.getCellHeight();
		
		// Set Graphics object for TAcct and TrActn objects
		TAcct.setGraphics(tg);
		TrActn.setGraphics(tg);
		
		String[] charts = {};
		
		// If Legal Entity database entity is specified
		if ( lglEntity != null )
			// Get names of ChOfAccs from Legal Entity database entity object
			charts = lglEntity.getChOfAccs();
		
		TAcct.setChOfAccs( charts );
		TrActn.setChOfAccs( charts );
	}
	
	/**
	 * Creates all arrays for model instance
	 */
	private void createArrays()
	{
		// Create ArrayList for tractns
		tractns = new Vector<>();
		
		// Create ArrayList for T-Accts
		accts = new Vector<>();
		
		// Create ArrayList for selected T-accts
		selectedTAccts = new ArrayList<>();
		
		toAddTAccts = new ArrayList<>();
		toDelTAccts = new ArrayList<>();
		toAddTractns = new ArrayList<>();
		toDelTractns = new ArrayList<>();
		
		output = new AssociativeList();
	}
	
	/**
	 * Restores grid and Model Tractns
	 */
	private void restoreGridAndTractns()
	{
		// Create ArrayList for model T-accts
		ArrayList<TAcct> acctList = new ArrayList<>();
		
		// Loop for each model tractn
		for ( TrActn tr : tractns )
		{
			TAcct trDx = tr.getDx();
			TAcct trCx = tr.getCx();
			
			// If Dx and Cx accts are specified
			if ( trCx != null && trDx != null )
			{
				tr.drawTractn();
				
				// Add accts to list of model T-accts
				acctList.add( trCx );
				acctList.add( trDx );
			}
		}
			
		// Loop for each model T-acct
		for ( TAcct acct : accts )
		{
			int idx = acct.chartIndex();
			
			// Set T-acct in grid
			grid[idx].get( acct.getRow() ).set( acct.getColumn(), acct );
			
			// If T-acct is not in list
			if ( !acctList.contains( acct ) )
				// Draw it 
				acct.drawTAcct();
		}			
	}
	
	/**
	 * Creates instance of the class
	 * @return Instance of this class
	 */
	public static TractnsModelView getInstance()
	{
		return new TractnsModelView();
	}
	
	/**
	 * Creates instance of the class
	 * @param fields Fields with initial values
	 * @return Instance of this class
	 */
	public static TractnsModelView getInstance( AssociativeList fields )
	{
		return new TractnsModelView( fields );
	}
	
	/**
	 * Returns dialog's TrActn Model
	 */
	public static TractnsModel getTractnsModel()
	{
		return tractnsModel;
	}
	
	/**
	 * Returns TrActn Model's tractns 
	 */
	public static Vector<TrActn> getTractns()
	{
		Vector<TrActn> trList = new Vector( tractns );
		trList.addAll( toAddTractns );
		trList.removeAll( toDelTractns );
		
		return trList;
	}
	
	/**
	 * Returns tractns list for specified ChOfAccs
	 * @param chartNum ChOfAccs' index
	 * @return List of tractns for specified ChOfAccs
	 */
	public static Vector<TrActn> getTractns( int chartNum )
	{
		Vector<TrActn> trList = getTractns();
		
		Iterator<TrActn> it = trList.iterator();
		
		List<TrActn> toRemove = new ArrayList<>();
		
		while ( it.hasNext() )
		{
			TrActn tr = it.next();
			
			if ( tr.chartIndex() != chartNum )
				toRemove.add(tr);
		}
		
		trList.removeAll( toRemove );
		
		return trList;
	}
	
	/**
	 * Returns TrActn Model's T-accts 
	 */
	public static Vector<TAcct> getTAccts()
	{
		Vector<TAcct> tAccList = new Vector( accts );
		tAccList.addAll( toAddTAccts );
		tAccList.removeAll( toDelTAccts );
		
		return tAccList;
	}
	
	/**
	 * Handles MouseClick event
	 * @param e Event on which this method is invoked
	 */
	private void clickedEventHandler( Event e )
	{
		// Period in milliseconds between mouse clicks to detect DoubleClick event
		final long DBL_CLK_TIME = 250;
		
		try
		{
			// If DoubleClick happened and clicks are within acceptable time interval
			if ( ((MouseEvent) e).getClickCount() == 2 && System.currentTimeMillis() - lastMouseClickTime < DBL_CLK_TIME  )
				// Assign event type Mouse DoubleClick event
				eventType = MouseEventType.DOUBLE_CLICK;
			
			// Otherwise
			else
				if ( ((MouseEvent) e).getButton() == MouseButton.PRIMARY )
					// Assign event type Mouse Click event
					eventType = MouseEventType.CLICK;
				else
					// Assign event type Mouse Right Click event
					eventType = MouseEventType.RIGHT_CLICK;
		}
		catch ( Exception ex )
		{
			// Assign event type Mouse Click event
			eventType = MouseEventType.CLICK;
		}
		
		// Cast event to MouseEvent and save it
		mouseEvent = (MouseEvent) e;
		
		// Save time stamp when event happened
		lastMouseClickTime = System.currentTimeMillis();
	}
	
	/**
	 * Initializes grid and timer
	 */
	private void init()
	{
		// Time in milliseconds for timer delay
		final int TIMER_DELAY = 300;
		
		createEmptyGrid();
		
		int tabsSize = Math.max( 1, tabs.length );
		
		for ( int i = 0; i < tabsSize; i++ )
			// Draw grid on canvas
			tg[i].drawGrid();
		
		// Create Task Timer and assign method to invoke periodically with specified time delay
		new TaskTimer().schedule( this::handleMouseEvent, TIMER_DELAY );
	}
	
	/**
	 * Creates empty grid
	 */
	private void createEmptyGrid()
	{
		ArrayList<TAcct> arr;
		
		int tabsSize = Math.max( 1, tabs.length );
		
		grid = new ArrayList[tabsSize];
		
		for ( int i = 0; i < tabsSize; i++ )
		{
			// Create ArrayList for the grid
			grid[i] = new ArrayList<>();
			
			// Loop for each grid row
			for ( int r = 0; r < ROWS; r++ )
			{
				// Create ArrayList for row columns
				arr = new ArrayList<>();
				
				// Loop for each grid column
				for ( int c = 0; c < COLS; c++ )
					// Add empty cell for current column
					arr.add( null );
					
				// Add row columns
				grid[i].add( arr );
			}
		}
	}
	
	/**
	 * Handles mouse events
	 */
	private void handleMouseEvent()
	{
        // Save last mouse event
        MouseEvent e = mouseEvent;
		
        // If there was a mouse event
        if ( e != null )
        {
			// If this is Click event
    		if ( eventType == MouseEventType.CLICK )
    			// Invoke user dialog in which Cell Click event will be handled
    			new UserDialog( () -> onClickCell(e) ).start();
			
    		// If this is DoubleClick event
			else if ( eventType == MouseEventType.DOUBLE_CLICK )
				// Clear grid cell on which click happened
	    		clearCell(e);	
	    	
    		// If this is RightClick event
			else if ( eventType == MouseEventType.RIGHT_CLICK )
				new UserDialog( () -> onRightClickCell(e) ).start();
	    	
			// Remove last mouse event from the variable
    		mouseEvent = null;
    	}
		// If About box for model did not show up yet
		else if ( !newAbout )
		{
			// If there are no T-accts (i.e. it's a completely new model)
			if ( accts.size() == 0 )
				// Display About box through Event Dispatch Thread of UserDialog
				new UserDialog( () -> btnAboutEventHandler(e) ).start();
	    	
			// Mark that About box was displayed already for new model
			newAbout = true;
		}
	}
	
	/**
	 * Handles MouseClick event
	 * @param e Mouse event
	 */
	private void onClickCell( MouseEvent e )
	{
		// If in current cell there is no T-Acct
		if ( getCurrentCell(e) == null )
			// Invoke editing cell
			editCell(e);
		
		// Otherwise
		else
			// Invoke selecting cell
			selectCell(e);
	}
	
	/**
	 * Handles mouse Right Click event
	 * @param e
	 */
	private void onRightClickCell( MouseEvent e )
	{
		TAcct tAcc = getCurrentCell( e );
		int chIdx = 0; // Default ChOfAccs number 
		
		ChOfAccs chart = selectedChOfAccs();
		
		// If there is selected ChOfAccs
		if ( chart != null )
			// Assign index of selected ChOfAccs
			chIdx = ChOfAccsModel.getIndexByName( chart.getName() );
		
		// If in current cell there is T-acct
		if ( tAcc != null )
		{
			// Get G/L # of T-acct
			GL gl = tAcc.getGL();
			String glNum = "";
			
			// If there is G/L # for T-acct
			if ( gl != null )
				glNum = gl.getGlNumber();
			
			String accName = tAcc.getName();
			
			// Enter T-acct info through the dialog window
	        String[] tAccInfo = enterTAcctInfo( this, chIdx, fields, glNum, accName );
	        
	        // Get G/L # and name
	        String glCode = tAccInfo[0];
	        String acctName = tAccInfo[1];
	        
	        // If G/L # is specified
	        if ( glCode == null || glCode.isEmpty() )
		        // Enter T-acct name and create object for T-acct
	        	tAcc.update( acctName, e, chart );
	        else
	        {
	        	gl = getGLAcct( glCode, chIdx );
	        	
	        	// Create T-acct database entity object
	        	tAcc.update( acctName, e, chart, gl );
	        }
			
	        tAcc.drawTAcct();
	    }
		else
		{
			// Get current column and row number
			int row = getRow(e);
			int col = getColumn(e);
			
			// Loop through list of all tractns
			for ( TrActn t : getTractns() )
				// If in current row there is tractn
				if ( t.getRow() == row )
				{
					int cxCol = t.getCx().getColumn();
					int dxCol = t.getDx().getColumn();
					
					// If current column belongs to tractn
					if ( col > cxCol  && col < dxCol )
					{
						String dscr = t.getDescription();
						
						String[] transInfo = Utilities.enterTractnInfo( (NodeView)tg[chIdx].getOwner(), dscr );
						
						for ( int c = cxCol + 1; c < dxCol; c++ )
							tg[chIdx].clearCellContent( row, c );
						
						// Enter transaction description
						t.setDescription( transInfo[1] );
						
						t.drawTractn();
						
						break;
					}
				}
					
		}
	}
	
	/**
	 * Get T-Acct of current cell
	 * @param e Mouse event
	 * @return T-Acct
	 */
	private TAcct getCurrentCell( MouseEvent e )
	{
		// Get column and row of current cell
		int col = getColumn(e), 
			row = getRow(e);
		
		if ( col >= COLS || row >= ROWS )
			return null;
		
		int tabNum = selectedTabNumber();
		
		// Get T-Acct from current cell and return it
		return (TAcct) grid[tabNum].get(row).get(col);
	}
	
	/**
	 * Sets T-Acct in current cell
	 * @param e Mouse event
	 * @param acct T-Acct to set
	 */
	private void setCellTAcct( MouseEvent e, TAcct acct )
	{
		// Get column and row of current cell
		int col = getColumn(e), 
			row = getRow(e);
		
		if ( col >= COLS || row >= ROWS )
			return;
		
		int tabNum = selectedTabNumber();
		
		// Set T-Acct for specified column
		grid[tabNum].get(row).set( col, acct );
	}
	
	/**
	 * Gets column number for current cell 
	 * @param e Mouse event
	 * @return Column number
	 */
	public static int getColumn( MouseEvent e )
	{
		// Get X coordinate for mouse position
		double x = e.getX();
		
		int col = 0; // To store calculated column number
		
		// If mouse is in visible zone
		if ( x > SHIFT_X )
			// Calculate column number and store it
			col = (int) Math.floor( (x - SHIFT_X)/CELL_WIDTH );
		
		return col;
	}
	
	/**
	 * Gets row number for current cell
	 * @param e Mouse event
	 * @return Row number
	 */
	public static int getRow( MouseEvent e )
	{
		// Get mouse Y coordinate
		double y = e.getY();
		
		int row = 0; // To store calculated row number
		
		// If mouse is in visible zone
		if ( y > SHIFT_Y )
			// Calculate row number
			row = (int) Math.floor( (y - SHIFT_Y)/CELL_HEIGHT );
		
		return row;
	}
	
	/**
	 * Draws T-Acct on canvas
	 * @param e Mouse event
	 */
	private void drawTAcct( MouseEvent e )
	{
		// Get T-acct of current cell
		TAcct acct = getCurrentCell(e);
		
		if ( acct != null )
			// Draw T-acct
			acct.drawTAcct();
		else
		{
			// Get current column and row numbers
			int col = getColumn(e);
			int row = getRow(e);
			
			int tabNum = selectedTabNumber();
			
			// Draw just image of simple T-acct
			tg[tabNum].drawTAcct( row, col );
		}
	}
	
	/**
	 * Adds T-Acct to the list of accts to add
	 * @param tAcc T-Acct to add
	 */
	public static void addToAddTAccts( TAcct tAcc )
	{
		toAddTAccts.add( tAcc );
	}
	
	/**
	 * Adds T-Acct to the list of accts to delete
	 * @param tAcc T-Acct to add
	 */
	public static void addToDelTAccts( TAcct tAcc )
	{
		toDelTAccts.add( tAcc );
	}
	
	/**
	 * Gets list of T-Accts to add
	 * @return List of T-Accts
	 */
	public static ArrayList<TAcct> getToAddTAccts()
	{
		return toAddTAccts;
	}
	
	/**
	 * Gets list of T-Accts to delete
	 * @return List of T-Accts
	 */
	public static ArrayList<TAcct> getToDelTAccts()
	{
		return toDelTAccts;
	}
	
	/**
	 * Adds tractn to the list of tractns to add
	 * @param t TrActn to add
	 */
	public static void addToAddTractns( TrActn t )
	{
		toAddTractns.add( t );
	}
	
	/**
	 * Adds tractn to the list of tractns to delete
	 * @param t TrActn to add
	 */
	public static void addToDelTractns( TrActn t )
	{
		toDelTractns.add( t );
	}
	
	/**
	 * Gets list of Tractns to add
	 * @return List of tractns
	 */
	public static ArrayList<TrActn> getToAddTractns()
	{
		return toAddTractns;
	}
	
	/**
	 * Gets list of Tractns to delete
	 * @return List of tractns
	 */
	public static ArrayList<TrActn> getToDelTractns()
	{
		return toDelTractns;
	}
	
	/**
	 * Clears current cell
	 * @param e Mouse event
	 */
	private void clearCell( MouseEvent e )
	{
		// Get current column and row number
		int row = getRow(e);
		int col = getColumn(e);
		
		// Get T-acct of current cell
		TAcct tAcc = getCurrentCell(e);
		
		// If T-acct is specified
		if ( tAcc != null )
			// Delete T-acct from current cell
			tAcc.deleteTAcct();
		
		else
			// Loop through list of all tractns
			for ( TrActn t : getTractns() )
				// If in current row there is tractn
				if ( t.getRow() == row )
					// If current column belongs to tractn
					if ( col > t.getCx().getColumn() && col < t.getDx().getColumn() )
					{
						// Delete tractn in current row
						t.deleteTractn();
						break;
					}
		
		int tabNum = selectedTabNumber();
		
		// Clear content of current cell
		tg[tabNum].clearCellContent( row, col );
		
		// Set current cell T-Acct to null
		setCellTAcct( e, null );
	}
	
	/**
	 * Edits current cell
	 * @param e Mouse event
	 */
	private void editCell( MouseEvent e )
	{
		// Draw T-acct
		drawTAcct( e );
  	  	
		ChOfAccs chart = selectedChOfAccs();
		int chartIndex = 0;
		
		if ( chart != null )
			chartIndex = ChOfAccsModel.getIndexByName( chart.getName() );
		
		// Enter T-acct info through the dialog window
        String[] tAccInfo = enterTAcctInfo( this, chartIndex, fields );
        
        // Get G/L number and name
        String glCode = tAccInfo[0];
        String accName = tAccInfo[1];
        
        TAcct acct;
        
        // If G/L number is specified
        if ( glCode == null || glCode.isEmpty() )
	        // Enter T-acct name and create object for T-acct
			acct = new TAcct( accName, e, chart );
        else
        {
        	GL glAcc = getGLAcct( glCode, chartIndex );
        	
        	// Create T-acct database entity object
        	acct = new TAcct( accName, e, chart, glAcc );
        }
		
		// Add created T-acct to the list of T-accts
		toAddTAccts.add( acct );
	  	
		// Draw T-acct name
		acct.drawAcctName();
		
	  	// Set T-acct for current cell
	  	setCellTAcct( e, acct );
	}
	
	/**
	 * Gets G/L Acct database entity by G/L code and index of ChOfAccs
	 * @param glCode G/L code
	 * @param chartIndex Index of ChOfAccs
	 * @return G/L Acct database entity
	 */
	private GL getGLAcct( String glCode, int chartIndex )
	{
		// Get G/L acct model by G/L acct number and index of ChOfAccs
    	GLAcctModel glModel = GLAcctModel.getByCode( glCode, chartIndex );
    	
    	GL glAcc = null;
    	
    	// If G/L acct model is specified
    	if ( glModel != null )
    		// Get G/L acct database entity from G/L acct model
    		glAcc = glModel.getGLAcct();
    	
    	return glAcc;
	}
	
	/**
	 * Selects current cell
	 * @param e Mouse event
	 */
	private void selectCell( MouseEvent e )
	{
		// Get T-acct of current cell
		TAcct acct = getCurrentCell(e);
		
		// If there is T-acct in selected cell
		if ( acct != null )
		{
			// Add T-acct to list of selected T-accts
			selectedTAccts.add( acct );
			
			// If there are 2 T-accts selected
			if ( selectedTAccts.size() > 1 )
				// Draw tractn with selected T-accts
				drawTractn();
				
			else
			{
				// Get current cell column and row number
				int col = getColumn(e);
				int row = getRow(e);
				
				int tabNum = selectedTabNumber();
				
				// Highlight selected cell
				tg[tabNum ].hightlightCell( row, col );
			}
		}
	}
	
	/**
	 * Draws tractn for selected accts
	 */
	private void drawTractn()
	{
		TAcct acct1, acct2;
		
		if ( selectedTAccts.size() == 2 )
		{
			acct1 = selectedTAccts.get(0);
			acct2 = selectedTAccts.get(1);
			
			drawTractn( acct1, acct2 );
			
			selectedTAccts.clear();
		}
	}
	
	/**
	 * Draws tractn for specified T-accts 
	 * @param acct1 First T-acct
	 * @param acct2 Second T-acct
	 */
	private void drawTractn( TAcct acct1, TAcct acct2 )
	{
		int acct1Col = acct1.getColumn();
		int acct2Col = acct2.getColumn();
		
		// Check if 1st acct is what should be credit acct of tractn
		if ( acct1Col > acct2Col )
		{
			// Swap T-accts
			TAcct tmp = acct1;
			acct1 = acct2;
			acct2 = tmp;
		}
		
		// Create tractn with provided T-accts
		TrActn tr = new TrActn( acct2, acct1 );
		
		// Add tractn to the list of tractns
		toAddTractns.add( tr );
	}
	
	/**
     * Creates content to display in the dialogue
     * @return Pane object with content
     */
    protected <T> VBox createContent()
    {
    	VBox box = new VBox();
    	
        if ( tabs.length > 0 )
        {
            // Create tab pane element
            tabPane = new TabPane();
            
            // Make tabs closing unavailable
            tabPane.setTabClosingPolicy( TabPane.TabClosingPolicy.UNAVAILABLE );

            // Loop for each form tab
            for ( int i = 0; i < tabs.length; i++ )
            	// Create Tab with Gridpane and add tab to TabPane object
            	tabPane.getTabs().add( createTabPane(i) );
            
            // Add Tractns graphics and buttons to the content box
            box.getChildren().addAll( tabPane, formButtons() );
        }
        else
        {
        	GridPane gridPane = createGridPane();
        	gridPane.getChildren().add( tg[0] );
        	
        	// Add Tractns graphics and buttons to the content box
            box.getChildren().addAll( gridPane, formButtons() );
        }
        
    	return box;
    }
	
    /**
     * Creates GridPane for Tab
     * @param tabNum Tab number
     * @return Tab object
     */
    private Tab createTabPane( int tabNum )
    {
    	// Create GridPane for the tab
    	GridPane gridPane = createGridPane();
    	
    	// Add TractnsGraphics Canvas object to display on GridPane
    	gridPane.getChildren().add( tg[tabNum] );
    	
    	// Create Tab object for the tab and return it
    	return createTab( gridPane, tabNum );
    }
    
    /**
     * Creates buttons for the form
     * @return Box object containing buttons
     */
    protected HBox formButtons()
    {
    	Label label = new Label( "Title:" );
        
        HBox buttons = new HBox();
        buttons.setPadding( new Insets( 10, 10, 10, 10 ) );
        buttons.setSpacing( 5 );
        buttons.setAlignment( Pos.CENTER_RIGHT );
        
        ObservableList<Node> btnsBox = buttons.getChildren();
        
        btnsBox.addAll( label, title );
        btnsBox.addAll( createButtons() );
        
        return buttons;
    }
    
    /**
     * Creates list of buttons for the form
     * @return ArrayList of buttons
     */
    private ArrayList<Button> createButtons()
    {
    	ArrayList<Button> btns = new ArrayList<>();
    	
    	btns.add( createBtnOK() );
    	btns.add( createBtnAbout() );
    	btns.add( createBtnCancel() );
    	
    	return btns;
    }
    
    /**
     * Creates OK button
     * @return Button object for created button
     */
    private Button createBtnOK()
    {
    	Button btn = new Button( "OK" );
        btn.setPadding( new Insets( 5, 28, 5, 28 ) );
        
        // Set event handler for the button
        btn.setOnAction( this::btnOkEventHandler );
        
    	return btn;
    }
    
    /**
     * Creates Cancel button
     * @return Button object for created button
     */
    private Button createBtnCancel()
    {
    	Button btn = new Button( "Cancel" );
        btn.setPadding( new Insets( 5, 20, 5, 20 ) );
        
        // Set event handler for the button
        btn.setOnAction( this::btnCancelEventHandler );
        
        return btn;
    }
    
    /**
     * Creates About button
     * @return Button object for created button
     */
    private Button createBtnAbout()
    {
    	Button btn = new Button( "About" );
    	btn.setPadding( new Insets( 5, 23, 5, 23 ) );
    	
    	// Set event handler for the button
        btn.setOnAction( this::btnAboutEventHandler );
    	
    	return btn;
    }
    
    /**
     * Event handler for OK button
     * @param e Event
     */
    private void btnOkEventHandler( Event e )
    {
    	for ( TrActn tr : toDelTractns )
    		tractns.remove( tr );
    	
    	for ( TAcct tAcc : toDelTAccts )
    		accts.remove( tAcc );
    	
    	Database.removeFromDB( toDelTractns );
    	Database.removeFromDB( toDelTAccts );
    	
    	toDelTractns.clear();
    	toDelTAccts.clear();
    	
    	tractns.addAll( toAddTractns );
    	accts.addAll( toAddTAccts );
    	
    	String titleTxt = title.getText();
            
        if ( tractnsModel == null )
    		tractnsModel = new TractnsModel( titleTxt, accts, tractns, lglEntity );
        else
        	tractnsModel.setName( titleTxt );
    	
    	output.set( tractnsModelField, tractnsModel );
    	output.set( "title" , titleTxt );
    	output.set( "lglEntity", lglEntity );
    
    	close();
    }
    
    /**
     * Event handler for Cancel button
     * @param e Event
     */
    private void btnCancelEventHandler( Event e )
    {
    	checkSavedTractns();
    	
    	checkNewTractns();
    	
    	output = null;
        
    	close();
    }
    
    /**
     * Checks saved tractns if button Cancel is pressed
     */
    private void checkSavedTractns()
    {
    	TAcct dx, cx;
    	int row;
    	List<Integer> corrAcc;
    	
    	// Loop through all saved Tractns
    	for ( TrActn tr : tractns )
    	{
    		dx = tr.getDx();
    		cx = tr.getCx();
    		row = tr.getRow();
    		
    		corrAcc = dx.getCorrCx();
    		
    		if ( !corrAcc.contains(row) )
    			corrAcc.add( row );
    		
    		corrAcc = cx.getCorrDx();
    		
    		if ( !corrAcc.contains(row) )
    			corrAcc.add( row );
    	}
    }
    
    /**
     * Checks new tractns if button Cancel is pressed
     */
    private void checkNewTractns()
    {
    	TAcct dx, cx;
    	int row;
    	List<Integer> corrAcc;
    	
    	// Loop through created new tractns to add
    	for ( TrActn tr : toAddTractns )
    	{
    		dx = tr.getDx();
    		cx = tr.getCx();
    		row = tr.getRow();
    		
    		corrAcc = dx.getCorrCx();
    		
    		if ( accts.contains(dx) && corrAcc.contains(row) )
    			corrAcc.remove( (Integer)row );
    		
    		corrAcc = cx.getCorrDx();
    		
    		if ( accts.contains(cx) && corrAcc.contains(row) )
    			corrAcc.remove( (Integer)row );
    	}
    }
    
    /**
     * Event handler for About button
     * @param e Event
     */
    private void btnAboutEventHandler( Event e )
    {
    	String[] boxTxt = new String[]{ "                     " + Main.TITLE + "\n\n              Transactions Modeling Software", 
    									"                         Hints on usage: ",
    									"<Click on empty cell> - create T-Account", 
    									"<Click on T-Account> - select T-Account to create transaction",
    									"<Dbl Click on T-Account> - delete T-Account with tranactions",
    									"<Dbl Click on TrActn> - delete transaction" };
    	Utilities.displayMessage( boxTxt );
    }
    
    /**
     * Displays the Tractns Model form
     * @return Result of input
     */
    public <T> T result()
    {
     	// Create content and display it
    	display( createContent() );
    	
     	return (T)output;
    }
   
    /**
     * Creates Grid object for the form
     * @return GridPane object
     */
    protected GridPane createGridPane()
    {
       // Create grid for current tab
       GridPane grid = new GridPane();
       
       grid.setPadding( new Insets( 10, 10, 10, 10 ) );
       grid.setHgap( 5 );
       grid.setVgap( 5 );
       
       grid.setStyle("-fx-border-style: solid inside;"
                    +"-fx-border-color: #FFF;"
                    +"-fx-border-width: 1 0 0 0;");
       return grid;
       
    } // End of method ** createGridPane **
	
    /**
     * Creates Tab object for the grid
     * @param grid Grid object of the form
     * @param tabNum Tab number
     * @return Created Tab object
     */
    protected Tab createTab( GridPane grid, int tabNum )
    {
       // Create Tab element
       Tab tab = new Tab();

       // If Tabs list is specified
       if ( tabs != null )
           // Set Tab name from Tabs list
           tab.setText( tabs[tabNum] );
       else
           // Set numbered Tab name
           tab.setText( "Tab " + tabNum );

       // Set grid as Tab content
       tab.setContent( grid );

       return tab;
       
    } // End of method ** createTab **
	
    /**
     * Gets selected tab number 
     * @param tabPane TabPane object for current Pane
     * @return Selected tab number
     */
    private int selectedTabNumber()
    {
    	// If there are tabs
        if ( tabPane != null )
            // Get tab number from selected tab
            return tabPane.getSelectionModel().getSelectedIndex();
        else
            return 0;
    }
    
    /**
     * Gets selected ChOfAccs
     * @return Selected ChOfAccs object
     */
    private ChOfAccs selectedChOfAccs()
    {
        int tabNum = selectedTabNumber();
        
        if ( tabs.length == 0 )
            return null;
        
        // Get names of ChOfAccs from Legal Entity database entity object
        String[] charts = lglEntity.getChOfAccs();
		
        // Get ChOfAccs Model by name of ChOfAccs
        ChOfAccsModel chartModel = ChOfAccsModel.getByName( charts[tabNum] );
        
        // If ChOfAccs Model is specified
        if ( chartModel != null )
        	// Get ChOfAccs database entities for ChOfAccs Model and return it
            return chartModel.getChOfAccs();
        else
            return null;
    }
    
    /**
	 * Enumeration for mouse events
	 * @author Peter Cross
	 *
	 */
	private enum MouseEventType
    {
    	CLICK, DOUBLE_CLICK, RIGHT_CLICK;
    }
	
} // End of class ** TractnsModelView **