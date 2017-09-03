package views;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
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
import entities.ChartOfAccounts;
import entities.GLAccount;
import entities.LegalEntity;
import entities.TAccount;
import entities.Transaction;
import entities.TransactionsModel;
import models.ChartOfAccountsModel;
import models.GLAccountModel;
import models.LegalEntityModel;
import models.TransactionsGraphics;
import forms.DialogElement;
import foundation.AssociativeList;
import foundation.TaskTimer;
import foundation.UserDialog;
import interfaces.Utilities;

import static interfaces.Utilities.enterTAccountInfo;

/**
 * Class TransactionsSimulationModel - Modeling accounting transactions
 * @author Peter Cross
 *
 */
public class TransactionsModelView extends NodeView implements Utilities
{
	public static final int  COLS = 28, // Number of columns
							 ROWS = 16; // Number of rows
	
	private static final int STROKE = 1; 			// Line stroke
	private static final int SHIFT_X = 1 + STROKE,	// +7 Shift on canvas horizontally
			  				 SHIFT_Y = 4 + STROKE; 	// +16 Shift on canvas vertically	
	private static double 	 CELL_WIDTH,  			// Cell width
							 CELL_HEIGHT; 			// Cell height
	
	private static TransactionsModel		transactionsModel;	// To store transactions Model entity object
	private static Vector<Transaction>		transactions;		// To store all transactions
	private static Vector<TAccount>			accounts;			// To store T-accounts
	private LegalEntity						legalEntity;		// Legal entity of Transactions Model
	
	private static ArrayList<TAccount>		toAddTAccounts;		// T-Accounts to add
	private static ArrayList<Transaction>	toAddTransactions;	// Transactions to add
	private static ArrayList<TAccount>		toDelTAccounts;		// T-Accounts to delete
	private static ArrayList<Transaction>	toDelTransactions;	// Transactions to delete
	
	private ArrayList<TAccount>				selectedTAccounts;	// To store selected T-accounts
	private AssociativeList					output;				// Results of input to pass
	private TextField 						title;				// Model Title
	
	private long 			lastMouseClickTime = 0l;// To store time stamp when last mouse click happened 
	private MouseEvent 		mouseEvent;				// To store last mouse event
	private MouseEventType  eventType;				// To store last mouse event type
	
	private boolean 		newAbout = false;		// To indicate that About box was displayed for new model
	private String[]		tabs;					// Tab names
    
	private TabPane 		tabPane;				// TabPane object for document with multiple tabs
	private AssociativeList fields;
	
	private ArrayList<ArrayList<TAccount>>[] grid; 	// To store cell accounts as a grid
	private TransactionsGraphics[]			 tg;	// Graphics object for displaying transactions
	
	/**
	 * Class default constructor
	 */
	private TransactionsModelView()
	{
		super( "Transactions Model" );
		
		createArrays();
		
		// Create TextField for the model title
		title = new TextField();
		title.setPrefWidth( 300 );
	}
	
	/**
	 * Class constructor
	 * @param fields Stored fields of the document
	 */
	private TransactionsModelView( AssociativeList fields )
	{
		this();
		
		this.fields = fields;
		
		setLegalEntity();
		
		// Set dialog graphics
		setGraphics();
		
		// Invoke initialization
		init();
	
		setTransactionsModel();
		
		if ( transactionsModel == null )
			return;
		
		setAccountsAndTransactions();
		
		// Restore grid and Model's Transactions
		restoreGridAndTransactions();
	}
	
	/**
	 * Sets Transactions Model for the view
	 */
	private void setTransactionsModel()
	{
		// Get Transactions Model
		transactionsModel = fields.get( "transactionsModel" );
		
		if ( transactionsModel == null )
			return;
		
		// Get name of Transactions Model
		String titleTxt = transactionsModel.getName();
		
		// Set title of the model on dialog form
		if ( titleTxt != null && !titleTxt.isEmpty() )
			title.setText( titleTxt );
		
		// Set Transactions Model as field for output
		output.set( "transactionsModel", transactionsModel );
	}
	
	/**
	 * Sets Legal Entity for model and Tabs for Legal Entity Charts Of Accounts
	 */
	private void setLegalEntity()
	{
		// Try to get Legal Entity database entity object from specified associative list
		legalEntity = fields.get( "legalEntity" );
		
		// If Legal Entity database entity object is specified
		if ( legalEntity != null )
		{
			// Get Legal Entity Model by Legal Entity database entity object
			LegalEntityModel entityModel = LegalEntityModel.getByEntity( legalEntity );
			
			// If Legal Entity Model is specified
			if ( entityModel != null )
				// Get Chart Of Accounts names  from Legal Entity Model
				tabs = entityModel.getChartNames();
			else
				// Get Chart Of Accounts names  from Legal Entity database entity object
				tabs = legalEntity.getChartOfAccounts();
	    }
		else
        	tabs = new String[] {};
	}
	
	/**
	 * Gets T-Accounts and Transactions for Transactions Model 
	 */
	private void setAccountsAndTransactions()
	{
		if ( transactionsModel == null )
			return;
				
		// Get transactions of Transactions Model
		Vector<Transaction> trs = transactionsModel.getTransactions();
				
		if ( trs != null )
			transactions = trs;
		
		// Get T-accounts of Transactions Model
		Vector<TAccount> accts = transactionsModel.getTAccounts();
				
		if ( accts != null )
			accounts = accts;
	}
	
	/**
	 * Sets Graphics context for Transactions Model
	 */
	private void setGraphics()
	{
		int tgSize = Math.max( 1, tabs.length );
		
		tg = new TransactionsGraphics[tgSize];
		
		for ( int i = 0; i < tgSize; i++ )
        {
            // Create Transactions Graphics object
            tg[i] = new TransactionsGraphics(this);
                
            // Add event handler for Mouse Click event
            tg[i].addEventHandler( MouseEvent.MOUSE_CLICKED, this::clickedEventHandler );
        }
		
		// Get cell width and height
		CELL_WIDTH  = TransactionsGraphics.getCellWidth();
		CELL_HEIGHT = TransactionsGraphics.getCellHeight();
		
		// Set Graphics object for TAccount and Transaction objects
		TAccount.setGraphics(tg);
		Transaction.setGraphics(tg);
		
		String[] charts = {};
		
		// If Legal Entity database entity is specified
		if ( legalEntity != null )
			// Get names of Charts Of Accounts from Legal Entity database entity object
			charts = legalEntity.getChartOfAccounts();
		
		TAccount.setChartsOfAccounts( charts );
		Transaction.setChartsOfAccounts( charts );
	}
	
	/**
	 * Creates all arrays for model instance
	 */
	private void createArrays()
	{
		// Create ArrayList for transactions
		transactions = new Vector<>();
		
		// Create ArrayList for T-Accounts
		accounts = new Vector<>();
		
		// Create ArrayList for selected T-accounts
		selectedTAccounts = new ArrayList<>();
		
		toAddTAccounts = new ArrayList<>();
		toAddTransactions = new ArrayList<>();
		toDelTAccounts = new ArrayList<>();
		toDelTransactions = new ArrayList<>();
		
		output = new AssociativeList();
	}
	
	/**
	 * Restores grid and Model Transactions
	 */
	private void restoreGridAndTransactions()
	{
		// Create ArrayList for model T-accounts
		ArrayList<TAccount> acctList = new ArrayList<>();
		
		// Loop for each model transaction
		for ( Transaction tr : transactions )
		{
			TAccount trDt = tr.getDt();
			TAccount trCr = tr.getCr();
			
			// If Dt and Cr accounts are specified
			if ( trCr != null && trDt != null )
			{
				tr.drawTransaction();
				
				// Add accounts to list of model T-accounts
				acctList.add( trCr );
				acctList.add( trDt );
			}
		}
			
		// Loop for each model T-account
		for ( TAccount acct : accounts )
		{
			int idx = acct.chartIndex();
			
			// Set T-account in grid
			grid[idx].get( acct.getRow() ).set( acct.getColumn(), acct );
			
			// If T-account is not in list
			if ( !acctList.contains( acct ) )
				// Draw it 
				acct.drawTAccount();
		}			
	}
	
	/**
	 * Creates instance of the class
	 * @return Instance of this class
	 */
	public static TransactionsModelView getInstance()
	{
		return new TransactionsModelView();
	}
	
	/**
	 * Creates instance of the class
	 * @param fields Fields with initial values
	 * @return Instance of this class
	 */
	public static TransactionsModelView getInstance( AssociativeList fields )
	{
		return new TransactionsModelView( fields );
	}
	
	/**
	 * Returns dialog's Transaction Model
	 */
	public static TransactionsModel getTransactionsModel()
	{
		return transactionsModel;
	}
	
	/**
	 * Returns Transaction Model's transactions 
	 */
	public static Vector<Transaction> getTransactions()
	{
		Vector<Transaction> trList = new Vector( transactions );
		trList.addAll( toAddTransactions );
		trList.removeAll( toDelTransactions );
		
		return trList;
	}
	
	/**
	 * Returns transactions list for specified Chart Of Accounts
	 * @param chartNum Chart Of Accounts index
	 * @return List of transactions for specified Chart Of Accounts
	 */
	public static Vector<Transaction> getTransactions( int chartNum )
	{
		Vector<Transaction> trList = getTransactions();
		
		Iterator<Transaction> it = trList.iterator();
		
		List<Transaction> toRemove = new ArrayList<>();
		
		while ( it.hasNext() )
		{
			Transaction tr = it.next();
			
			if ( tr.chartIndex() != chartNum )
				toRemove.add(tr);
		}
		
		trList.removeAll( toRemove );
		
		return trList;
	}
	
	/**
	 * Returns Transaction Model's T-accounts 
	 */
	public static Vector<TAccount> getTAccounts()
	{
		Vector<TAccount> tAccList = new Vector( accounts );
		tAccList.addAll( toAddTAccounts );
		tAccList.removeAll( toDelTAccounts );
		
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
				// Assign event type Mouse Click event
				eventType = MouseEventType.CLICK;
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
		ArrayList<TAccount> arr;
		
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
	    	
			// Remove last mouse event from the variable
    		mouseEvent = null;
    	}
		// If About box for model did not show up yet
		else if ( !newAbout )
		{
			// If there are no T-accounts (i.e. it's a completely new model)
			if ( accounts.size() == 0 )
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
		// If in current cell there is no T-Account
		if ( getCurrentCell(e) == null )
			// Invoke editing cell
			editCell(e);
		
		// Otherwise
		else
			// Invoke selecting cell
			selectCell(e);
	}
	
	/**
	 * Get T-Account of current cell
	 * @param e Mouse event
	 * @return T-Account
	 */
	private TAccount getCurrentCell( MouseEvent e )
	{
		// Get column and row of current cell
		int col = getColumn(e), 
			row = getRow(e);
		
		if ( col >= COLS || row >= ROWS )
			return null;
		
		int tabNum = selectedTabNumber();
		
		// Get T-Account from current cell and return it
		return (TAccount) grid[tabNum].get(row).get(col);
	}
	
	/**
	 * Sets T-Account in current cell
	 * @param e Mouse event
	 * @param acct T-Account to set
	 */
	private void setCellTAccount( MouseEvent e, TAccount acct )
	{
		// Get column and row of current cell
		int col = getColumn(e), 
			row = getRow(e);
		
		if ( col >= COLS || row >= ROWS )
			return;
		
		int tabNum = selectedTabNumber();
		
		// Set T-Account for specified column
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
	 * Draws T-Account on canvas
	 * @param e Mouse event
	 */
	private void drawTAccount( MouseEvent e )
	{
		// Get T-account of current cell
		TAccount acct = getCurrentCell(e);
		
		if ( acct != null )
			// Draw T-account
			acct.drawTAccount();
		else
		{
			// Get current column and row numbers
			int col = getColumn(e);
			int row = getRow(e);
			
			int tabNum = selectedTabNumber();
			
			// Draw just image of simple T-account
			tg[tabNum].drawTAccount( row, col );
		}
	}
	
	/**
	 * Adds T-Account to the list of accounts to add
	 * @param tAcc T-Account to add
	 */
	public static void addToAddTAccounts( TAccount tAcc )
	{
		toAddTAccounts.add( tAcc );
	}
	
	/**
	 * Adds T-Account to the list of accounts to delete
	 * @param tAcc T-Account to add
	 */
	public static void addToDelTAccounts( TAccount tAcc )
	{
		toDelTAccounts.add( tAcc );
	}
	
	/**
	 * Gets list of T-Accounts to add
	 * @return List of T-Accounts
	 */
	public static ArrayList<TAccount> getToAddTAccounts()
	{
		return toAddTAccounts;
	}
	
	/**
	 * Gets list of T-Accounts to delete
	 * @return List of T-Accounts
	 */
	public static ArrayList<TAccount> getToDelTAccounts()
	{
		return toDelTAccounts;
	}
	
	/**
	 * Adds transaction to the list of transactions to add
	 * @param t Transaction to add
	 */
	public static void addToAddTransactions( Transaction t )
	{
		toAddTransactions.add( t );
	}
	
	/**
	 * Adds transaction to the list of transactions to delete
	 * @param t Transaction to add
	 */
	public static void addToDelTransactions( Transaction t )
	{
		toDelTransactions.add( t );
	}
	
	/**
	 * Gets list of Transactions to add
	 * @return List of transactions
	 */
	public static ArrayList<Transaction> getToAddTransactions()
	{
		return toAddTransactions;
	}
	
	/**
	 * Gets list of Transactions to delete
	 * @return List of transactions
	 */
	public static ArrayList<Transaction> getToDelTransactions()
	{
		return toDelTransactions;
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
		
		// Get T-account of current cell
		TAccount tAcc = getCurrentCell(e);
		
		// If T-account is specified
		if ( tAcc != null )
			// Delete T-account from current cell
			tAcc.deleteTAccount();
		
		else
			// Loop through list of all transactions
			for ( Transaction t : getTransactions() )
				// If in current row there is transaction
				if ( t.getRow() == row )
					// If current column belongs to transaction
					if ( col > t.getCr().getColumn() && col < t.getDt().getColumn() )
					{
						// Delete transaction in current row
						t.deleteTransaction();
						break;
					}
		
		int tabNum = selectedTabNumber();
		
		// Clear content of current cell
		tg[tabNum].clearCellContent( row, col );
		
		// Set current cell T-Account to null
		setCellTAccount( e, null );
	}
	
	/**
	 * Edits current cell
	 * @param e Mouse event
	 */
	private void editCell( MouseEvent e )
	{
		// Draw T-account
		drawTAccount( e );
  	  	
		ChartOfAccounts chart = selectedChartOfAccounts();
		int chartIndex = 0;
		
		if ( chart != null )
			chartIndex = ChartOfAccountsModel.getIndexByName( chart.getName() );
		
		// Enter T-account info through the dialog window
        String[] tAccInfo = enterTAccountInfo( this, chartIndex, fields );
        
        // Get G/L account number and name
        String glCode = tAccInfo[0];
        String accName = tAccInfo[1];
        
        TAccount acct;
        
        // If G/L account number is specified
        if ( glCode == null || glCode.isEmpty() )
	        // Enter T-account name and create object for T-account
			acct = new TAccount( accName, e, chart );
        else
        {
        	GLAccount glAcc = getGLAccount( glCode, chartIndex );
        	
        	// Create T-account database entity object
        	acct = new TAccount( accName, e, chart, glAcc );
        }
		
		// Add created T-account to the list of T-accounts
		toAddTAccounts.add( acct );
	  	
		// Draw T-account name
		acct.drawAccountName();
		
	  	// Set T-account for current cell
	  	setCellTAccount( e, acct );
	}
	
	/**
	 * Gets G/L Account database entity by G/L code and index of Chart Of Accounts
	 * @param glCode G/L code
	 * @param chartIndex Index of Chart Of Accounts
	 * @return G/L Account database entity
	 */
	private GLAccount getGLAccount( String glCode, int chartIndex )
	{
		// Get G/L account model by G/L account number and index of Chart Of Accounts
    	GLAccountModel glModel = GLAccountModel.getByCode( glCode, chartIndex );
    	
    	GLAccount glAcc = null;
    	
    	// If G/L account model is specified
    	if ( glModel != null )
    		// Get G/L account database entity from G/L account model
    		glAcc = glModel.getGLAccount();
    	
    	return glAcc;
	}
	
	/**
	 * Selects current cell
	 * @param e Mouse event
	 */
	private void selectCell( MouseEvent e )
	{
		// Get T-account of current cell
		TAccount acct = getCurrentCell(e);
		
		// If there is T-account in selected cell
		if ( acct != null )
		{
			// Add T-account to list of selected T-accounts
			selectedTAccounts.add( acct );
			
			// If there are 2 T-accounts selected
			if ( selectedTAccounts.size() > 1 )
				// Draw transaction with selected T-accounts
				drawTransaction();
				
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
	 * Draws transaction for selected accounts
	 */
	private void drawTransaction()
	{
		TAccount acct1, acct2;
		
		if ( selectedTAccounts.size() == 2 )
		{
			acct1 = selectedTAccounts.get(0);
			acct2 = selectedTAccounts.get(1);
			
			drawTransaction( acct1, acct2 );
			
			selectedTAccounts.clear();
		}
	}
	
	/**
	 * Draws transaction for specified T-accounts 
	 * @param acct1 First T-account
	 * @param acct2 Second T-account
	 */
	private void drawTransaction( TAccount acct1, TAccount acct2 )
	{
		int acct1Col = acct1.getColumn();
		int acct2Col = acct2.getColumn();
		
		// Check if 1st account is what should be credit account of transaction
		if ( acct1Col > acct2Col )
		{
			// Swap T-accounts
			TAccount tmp = acct1;
			acct1 = acct2;
			acct2 = tmp;
		}
		
		// Create transaction with provided T-accounts
		Transaction tr = new Transaction( acct2, acct1 );
		
		// Add transaction to the list of transactions
		toAddTransactions.add( tr );
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
            
            // Add Transactions graphics and buttons to the content box
            box.getChildren().addAll( tabPane, formButtons() );
        }
        else
        {
        	GridPane gridPane = createGridPane();
        	gridPane.getChildren().add( tg[0] );
        	
        	// Add Transactions graphics and buttons to the content box
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
    	
    	// Add TransactionsGraphics Canvas object to display on GridPane
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
    	for ( Transaction tr : toDelTransactions )
    		transactions.remove( tr );
    	
    	for ( TAccount tAcc : toDelTAccounts )
    		accounts.remove( tAcc );
    	
    	Database.removeFromDB( toDelTransactions );
    	Database.removeFromDB( toDelTAccounts );
    	
    	toDelTransactions.clear();
    	toDelTAccounts.clear();
    	
    	transactions.addAll( toAddTransactions );
    	accounts.addAll( toAddTAccounts );
    	
    	String titleTxt = title.getText();
            
        if ( transactionsModel == null )
    		transactionsModel = new TransactionsModel( titleTxt, accounts, transactions, legalEntity );
        else
        	transactionsModel.setName( titleTxt );
    	
    	output.set( "transactionsModel", transactionsModel );
    	output.set( "title" , titleTxt );
    	output.set( "legalEntity", legalEntity );
    
    	close();
    }
    
    /**
     * Event handler for Cancel button
     * @param e Event
     */
    private void btnCancelEventHandler( Event e )
    {
    	checkSavedTransactions();
    	
    	checkNewTransactions();
    	
    	output = null;
        
    	close();
    }
    
    /**
     * Checks saved transactions if button Cancel is pressed
     */
    private void checkSavedTransactions()
    {
    	TAccount dt, cr;
    	int row;
    	List<Integer> corrAcc;
    	
    	// Loop through all saved transactions
    	for ( Transaction tr : transactions )
    	{
    		dt = tr.getDt();
    		cr = tr.getCr();
    		row = tr.getRow();
    		
    		corrAcc = dt.getCorrCr();
    		
    		if ( !corrAcc.contains(row) )
    			corrAcc.add( row );
    		
    		corrAcc = cr.getCorrDt();
    		
    		if ( !corrAcc.contains(row) )
    			corrAcc.add( row );
    	}
    }
    
    /**
     * Checks new transactions if button Cancel is pressed
     */
    private void checkNewTransactions()
    {
    	TAccount dt, cr;
    	int row;
    	List<Integer> corrAcc;
    	
    	// Loop through created new transactions to add
    	for ( Transaction tr : toAddTransactions )
    	{
    		dt = tr.getDt();
    		cr = tr.getCr();
    		row = tr.getRow();
    		
    		corrAcc = dt.getCorrCr();
    		
    		if ( accounts.contains(dt) && corrAcc.contains(row) )
    			corrAcc.remove( (Integer)row );
    		
    		corrAcc = cr.getCorrDt();
    		
    		if ( accounts.contains(cr) && corrAcc.contains(row) )
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
    									"<Dbl Click on Transaction> - delete transaction" };
    	Utilities.displayMessage( boxTxt );
    }
    
    /**
     * Displays the Transactions Model form
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
     * Gets selected Chart Of Accounts
     * @return Selected Chart Of Accounts object
     */
    private ChartOfAccounts selectedChartOfAccounts()
    {
        int tabNum = selectedTabNumber();
        
        if ( tabs.length == 0 )
            return null;
        
        // Get names of Charts of Accounts from Legal Entity database entity object
        String[] charts = legalEntity.getChartOfAccounts();
		
        // Get Chart Of Accounts Model by name of Chart Of Accounts
        ChartOfAccountsModel chartModel = ChartOfAccountsModel.getByName( charts[tabNum] );
        
        // If Chart Of Accounts Model is specified
        if ( chartModel != null )
        	// Get Chart Of Accounts database entities for Chart Of Accounts Model and return it
            return chartModel.getChartOfAccounts();
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
    	CLICK, DOUBLE_CLICK;
    }
	
} // End of class ** TransactionsModelView **