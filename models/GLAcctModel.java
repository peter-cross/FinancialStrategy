package models;

import javafx.stage.Stage;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import application.Database;
import entities.COA;
import entities.GL;
import views.OneColumnTableView;
import forms.DialogElement;
import forms.TableElement;
import forms.TableOutput;
import interfaces.Lambda.OnElementChange;

import static interfaces.Utilities.createModelClass;
import static interfaces.Utilities.getListElementBy;
import static interfaces.Utilities.getListIndex;

/**
 * Class GLAcctModel - Creates G/L Acct object
 * @author Peter Cross
 */
public class GLAcctModel extends RegistryItemModel
{
    /*          Properties   	                                                                                      */
    /******************************************************************************************************************/  
    private static  LinkedHashSet[] list;  // List of G/L accounts for each ChOfAccs
    
    private static	ArrayList<COAModel> charts;	// List of ChOfAccs
    
    private final static String glAcctStr = "G/L Account";
	
    /**
     * Class constructor
     * @param chartName ChOfAccs' name
     */
    public GLAcctModel( String chartName )
	{
		super( glAcctStr, chartName );
	}
	
    /**
     * Class constructor
     * @param st Stage object where to display
     * @param chartName ChOfAccs' name
     * @throws Exception
     */
    public GLAcctModel( Stage st, String chartName ) throws Exception
	{
		super( st, glAcctStr, chartName );
	}
	
    /**
     * Class constructor
     * @param gl G/L Acct entity object
     * @param chartName ChOfAccs' name
     */
    public GLAcctModel( GL gl, String chartName )
    {
    	super( glAcctStr, chartName );
    	setTab( chartName );
        	
		fields.set( "glAcct", gl );
		
		fields.set( "glNumber", gl.getGlNumber() );
		fields.set( "name", gl.getName() );
		fields.set( "type", gl.getType() );
		fields.set( "acctGrp", gl.getAcctGrp() );
		fields.set( "quantity", gl.getQuantity() );
		fields.set( "frgnCrcy", gl.getFrgnCrcy() );
		fields.set( "contraAcct", gl.getContraAcct() );
		fields.set( "analyticsControl", gl.getAnalyticsControl() );
		fields.set( "analyticsType", gl.getAnalyticsType() );
		fields.set( "chOfAccs", gl.getChOfAccs() );
	}
    
    /**
	 * Sets tab name and corresponding parameters
	 * @param tabName Tab name
	 */
    @Override
	protected void setTab( String tabName )
	{
		this.tabName = tabName;
		
		COAModel chartsModel = COAModel.getByName( tabName );
		tabNum = getListIndex( COAModel.getItemsList() , chartsModel );
		
		COA chOfAccs = null;
		
		if ( chartsModel != null )
			chOfAccs = chartsModel.getChOfAccs();
		
		if ( chOfAccs != null )
			fields.set( "chOfAccs", chOfAccs );
	}
	
    /**
     * Gets G/L Acct Model by G/L acct code
     * @param code G/L acct code
     * @return G/L Acct Model object
     */
    public static GLAcctModel getByCode( String code )
    {
        return (GLAcctModel) getListElementBy( list[0], "glNumber", code );
        
    } // End of method ** getByCode **
      
    /**
     * Gets G/L Acct Model by G/L acct code
     * @param code G/L acct code
     * @return G/L Acct Model object
     * @param chart ChOfAccs' name
     * @return G/L Acct Model object
     */
    public static GLAcctModel getByCode( String code, String chart )
    {
        int chartIndex = COAModel.getIndexByName( chart );
        
        return getByCode( code, chartIndex );
        
    } // End of method ** getByCode **
    
    /**
     * Gets G/L Acct Model by G/L acct number and ChOfAccs' index
     * @param code G/L acct number
     * @param chartIndex Index of ChOfAccs
     * @return G/L Acct Model
     */
    public static GLAcctModel getByCode( String code, int chartIndex )
    {
        if ( chartIndex != -1 )
            return (GLAcctModel) getListElementBy( list[chartIndex], "glNumber", code );
        else
            return null;
        
    } // End of method ** getByCode **
    
    /**
     * Gets string representation of class object
     * @return G/L Acct's number
     */
    public String toString()
    {
        return (String) fields.get( "glNumber" );    
    }
    
    /**
     * Gets name of G/L acct
     * @return G/L acct name
     */
    public String getName()
    {
    	return (String) fields.get( "name" ); 
    }
    
    /**
     * Gets G/L acct database entity object
     * @return G/L acct entity
     */
    public GL getGLAcct()
    {
    	return (GL) fields.get( "glAcct" );
    }
    
    /**
     * Creates header structure of the form
     * @return Array of form header elements
     */
    protected DialogElement[][] createHeader()
    {
        // Array for header elements
        DialogElement[][] header = new DialogElement[1][7];
        // Helper object for creating new dialog elements
        DialogElement hdr;
        
        // 1st header element on 1st tab
        hdr = new DialogElement( "G/L Number" );
        hdr.attributeName = "glNumber";
        hdr.width = 60;
        hdr.shortName = "G/L #";
        hdr.textValue = fieldTextValue( "glNumber" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][0] = hdr;
        
        // 2nd header element on 1st tab
        hdr = new DialogElement( "Account Name" );
        hdr.attributeName = "name";
        hdr.width = 255;
        // If field text value is specified - pass it to the form
        hdr.textValue = fieldTextValue( "name" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][1] = hdr;
        
        // 3rd header element on 1st tab
        hdr = new DialogElement( "Type of Account" );
        hdr.attributeName = "type";
        hdr.shortName = "Acct Type";
        // Possible text choices for the field
        hdr.textChoices = new String[]{ "Balance Sheet", "Income Statement" };
        hdr.editable = false;
        // If field text value is specified - pass it to the form
        hdr.textValue = fieldTextValue( "type" );
        hdr.validation = validationCode( hdr.labelName );
        // Lambda expression that gets invoked on element change event
        hdr.onChange = onTypeOfAcctChange();
        header[0][2] = hdr;
        
        // 4th header element on 1st tab
        hdr = new DialogElement( "Account Group" );
        hdr.attributeName = "acctGrp";
        hdr.valueType = "Tree";
        // If field text value is specified - pass it to the form
        hdr.textValue = fieldTextValue( "acctGrp" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][3] = hdr;
        
        // 5th header element on 1st tab
        hdr = new DialogElement( "Quantity" );
        hdr.attributeName = "quantity";
        hdr.shortName = "Q-ty";
        hdr.checkBoxlabel = "Quantity Tracking";
        // If field text value is specified - pass it to the form
        hdr.textValue = fieldTextValue( "quantity" );
        header[0][4] = hdr;
        	
        // 6th header element on 1st tab
        hdr = new DialogElement( "Foreign Currency" );
        hdr.attributeName = "frgnCrcy";
        hdr.shortName = "Curr";
        hdr.checkBoxlabel = "Foreign Currency Operations";
        // If field text value is specified - pass it to the form
        hdr.textValue = fieldTextValue( "frgnCrcy" );
        header[0][5] = hdr;
        
        // 7th header element on 1st tab
        hdr = new DialogElement( "Contra Account" );
        hdr.attributeName = "contraAcct";
        hdr.shortName = "Cntr";
        hdr.checkBoxlabel = "Contra Account";
        // If field text value is specified - pass it to the form
        hdr.textValue = fieldTextValue( "contraAcct" );
        header[0][6] = hdr;
        
        return header;
    
    } // End of method ** createHeader **
	
    /**
     * Returns Lambda expression for Actions on Type of Acct's change
     * @return Lambda expression
     */
    private OnElementChange onTypeOfAcctChange()
    {
        return ( elementsList ) -> 
        {
        	ComboBox acctTypeField = (ComboBox) elementsList.get( "Type of Account" );
        	
        	if ( acctTypeField == null )
        		return;
        	
        	// Get acct type
        	Object fieldValue = acctTypeField.getValue();
        	String accType;
        	
        	if ( fieldValue != null )
        		accType = fieldValue.toString();
        	else
        		return;

        	// Field name that is effected by element change event
            String fieldName = "Account Group";
            
            // Get reference to element field on the form
            TextField field  = (TextField) elementsList.get( fieldName );
            
            // If field is created - clear the field
            if ( field != null )
                field.setText( "" );

            // Get reference to stage object of the form
            Stage st = (Stage) elementsList.get( "stage" );

            // If acct type is specified
            if ( !accType.isEmpty() )
                // If acct type is Balance Sheet
                if ( accType == "Balance Sheet" )
                    // Create Balance Sheet Tree object and pass it to the form
                    elementsList.set( fieldName + "Object", new BSTree( st ) );
                // If acct type is Income Statement
                else if ( accType == "Income Statement" )
                    // Create Income Statement Tree object and pass it to the form
                    elementsList.set( fieldName + "Object", new ISTree( st ) );
        };
    }
    
    /**
     * Creates table structure of the form
     * @return Array of table elements
     */
    protected TableElement[][] createTable()
    {
        // Array of table elements
        TableElement[][] table = new TableElement[1][3];
        // Helper object for creating new table elements
        TableElement tbl;

        // 1st column of the table on the 1st tab
        tbl = new TableElement( "Analytical Dimensions" );
        tbl.width = 200;
        tbl.editable = false;
        tbl.valueType = "List";
        // If there are specified values of the column - pass them to the form
        tbl.textValue = (String[]) fields.get( "analyticsControl" );
        tbl.validation = validationCode( tbl.columnName );
        // Possible text choices of the column
        tbl.textChoices = new String[dimension.size()];
        for ( int i = 0; i < dimension.size(); i++ )
            tbl.textChoices[i] = dimension.getKey(i);
        table[0][0] = tbl;
        
        // 2nd column of the table on the 1st tab
        tbl = new TableElement( "Type" );
        tbl.width = 50;
        tbl.editable = false;
        // If there are specified values of the column - pass them to the form
        tbl.textValue = (String[]) fields.get( "analyticsType" );
        tbl.validation = validationCode( tbl.columnName );
        // Possible text choices of the column
        tbl.textChoices = new String[] { "Balance", "Period" };
        table[0][1] = tbl;
        
        return table;
    
    } // End of method ** createTable **
	
    /**
     * Saves input information into object attributes
     * @param header Array of input from header
     * @param table Array of input from table
     */
    protected void init( String[][] header, String[][][] table )
    {
        // If header is specified
        if ( header.length > 0 )
            try
            {
                fields.set( "glNumber",  header[0][0] );
                fields.set( "name",  header[0][1] );
                fields.set( "type",  header[0][2] );
                fields.set( "acctGrp",  header[0][3] );
                
                fields.set( "quantity",  Byte.parseByte( header[0][4] ) );
                fields.set( "frgnCrcy",  Byte.parseByte( header[0][5] ) );
                fields.set( "contraAcct",  Byte.parseByte( header[0][6] ) );
            }
            catch ( Exception e ) {}
        
        // If table is specified
        if ( table != null && table.length > 0 )
        {
            // Get the number of acct analytical dimensions
            int numAnalytics = table[0].length;

            // Create arrays for analytical dimensions and analytics tracking type
            String[] analyticsControl = new String[numAnalytics];
            String[] analyticsType = new String[numAnalytics];

            // Loop for each Analytical Dimension
            for ( int i = 0; i < numAnalytics; i++ )
            {
                analyticsControl[i] = table[0][i][0];
                analyticsType[i] = table[0][i][1];
            }
            
            fields.set( "analyticsControl", analyticsControl );
            fields.set( "analyticsType", analyticsType );
        }
        else
        {
            fields.set( "analyticsControl", new String[]{} );
            fields.set( "analyticsType", new String[]{} );
        }
    } // End of method ** init **
	
    /**
     * Displays dialog form
     */
    public void display()
    {
        // Create header elements array and add it to attributes list
        attributesList.set( "header", createHeader() );
        // Create table elements array and add it to attributes list
        attributesList.set( "table", createTable() );
        // Add output results to attributes list
        attributesList.set( "output", getOutput() );
    
    } // End of method ** display **
    
    /**
     * Display input dialog form for new object and get output results
     * @return TableOutput object with output results
     */
    @Override
    public TableOutput getOutput()
    {
        // Number of analytical dimensions
        byte numDmns = 5;
        
        String[] analyticsControl = (String[]) fields.get( "analyticsControl" );
        
        // If analytical dimensions are specified
        if ( analyticsControl != null )
            // Assign the number of analytical dimensions
            numDmns = (byte) Math.max( 5, analyticsControl.length );
        
        // Display input form and get the results of input
        TableOutput result =  new OneColumnTableView( this, glAcctStr, numDmns ).result();
        
        // Assign the input results to object properties
        init( result.header, result.table );
        
        if ( result != null )
        {
        	saveToDB();
        	
        	if ( !list[tabNum].contains( this ) )
				list[tabNum].add( this );
        }
        else if ( !list[tabNum].contains( this ) )
			fields = null;
        
        // Return the input result
        return result;
        
    } // End of method ** getOutput **
    
    /**
     * Get List of G/L Accts in ArrayList
     * @return ArrayList with list of G/L Accts
     */
    public static LinkedHashSet[] getItemsList()
    {
        return list;
    }
    
    /**
	 * Creates list of GLAcctModel objects
	 */
    public static  LinkedHashSet[] createList()
    {
		if ( list == null )
        	initEmptyList();
        else
        	createNewList();
        	
        return list;
    }
    
	/**
	 * Initializes empty list of RegistryItems
	 */
	private static void initEmptyList()
	{
		Class c = createModelClass( "COAModel" );

		try
        {
            // Get list of ChOfAccs
            charts = new ArrayList( ((LinkedHashSet[]) c.getMethod( "createList" ).invoke( null ))[0] );

            int numCharts = Math.max( charts.size(), 1 );

            // Create arrays for Legal Entities list with size equal to number of Legal Entities
            list = new LinkedHashSet[ numCharts ];
        }
        catch ( Exception e )
        {
            // Create arrays for one ChOfAccs
            list = new LinkedHashSet[1];
        }
        
        // Create ArrayList for list of ChOfAccs for each ChOfAccs
        for ( int i = 0; i < list.length; i++ )
            list[i] = new LinkedHashSet<>();

        initFromDB();
	}
	
	/**
	 * Creates new list of Registry Items
	 */
	private static void createNewList()
	{
		Class c = createModelClass( "COAModel" );

		int numCharts = 1;
    	ArrayList<COAModel>  newCharts = null;
    	
    	try
        {
            // Get new list of ChOfAccs
    		newCharts = new ArrayList( ((LinkedHashSet[]) c.getMethod( "createList" ).invoke( null ))[0] );

    		numCharts = Math.max( newCharts.size(), 1 );
        }
        catch ( Exception e ) { }
    	
    	LinkedHashSet[] newList = new LinkedHashSet[numCharts];
		
		for ( int i = 0; i < numCharts; i++ )
    	{
			int idx = -1;
			
			if ( newCharts.size() > 0 )
				idx = charts.indexOf( newCharts.get(i) );
        		
    		if ( idx >= 0 )
    			newList[i] = list[idx];
    		else
    			newList[i] = new LinkedHashSet<>();
    	}
		
		list = newList;
		charts = newCharts;
	}
	
	/**
	 * Initializes G/L Acct Models by data from database
	 */
	private static void initFromDB()
	{
		// Get list of G/L Accts from database
		List<GL> dbGLAccts = getFromDB();

		// If list is not empty
        if ( dbGLAccts != null && dbGLAccts.size() > 0 )
            // Loop for each G/L Acct
            for ( GL gl : dbGLAccts )
            {
                // Create ChOfAccs object from G/L Acct
                COA chart = gl.getChOfAccs();

                int chartNum = 0;

                if ( chart != null )
                {
                	COAModel  chartModel = COAModel.getByChOfAccs( chart );

                    if ( chartModel != null )
                    	chartNum = getListIndex( COAModel.getItemsList(), chartModel );
                }

                list[chartNum].add( new GLAcctModel( gl, chart.getName() ) );
            }
    }
	
	/**
	 * Gets G/L Accts from database
	 * @return List of GL objects
	 */
	protected static List<GL> getFromDB()
    {
    	return getFromDB( "GL" );
    }
    
	/**
	 * Saves Model data to database
	 */
    @Override
    protected void saveToDB()
    {
    	String	glNumber = fields.get( "glNumber" ),	// G/L Acct number
                name = fields.get( "name" ),			// G/L Acct name
                type = fields.get( "type" ),			// G/L Acct type
                acctGrp = fields.get( "acctGrp" );		// Acct Group in Balance Sheet or Income Statement
                
    	int 	quantity = Integer.parseInt( fieldTextValue( "quantity" ) ),			// Quantity flag
    			frgnCrcy = Integer.parseInt( fieldTextValue( "frgnCrcy" ) ),			// Foreign Crcy flag
    			contraAcct = Integer.parseInt( fieldTextValue( "contraAcct" ) );		// Contra account flag
    	
    	COA chOfAccs = fields.get( "chOfAccs" );
    	
    	List<String> analyticsControl = new ArrayList<String>();
    	
    	for ( String s : (String[]) fields.get( "analyticsControl" ) )
    		analyticsControl.add( s );
    	
    	List<String> analyticsType = new ArrayList<String>();
    	
    	for ( String s : (String[]) fields.get( "analyticsType" ) )
    		analyticsType.add( s );
    	
    	// Get G/L Acct object from the fields of current model
    	GL glAcct = fields.get( "glAcct" );
    	
    	// If G/L Acct object is not created yet
    	if ( glAcct == null  )
        {
            // Create instance of G/L Acct
    		glAcct = new GL( glNumber, name, type, acctGrp, quantity, frgnCrcy, contraAcct, analyticsControl, analyticsType, chOfAccs );
            fields.set( "glAcct", glAcct );
        }
            
    	// Otherwise
    	else
            // Update G/L Acct information
    		glAcct.update( glNumber, name, type, acctGrp, quantity, frgnCrcy, contraAcct, analyticsControl, analyticsType, chOfAccs );
    	
    	try
    	{
    		// Persist G/L Acct data to database
    		Database.persistToDB( glAcct );
    	}
    	catch ( Exception e ) 
    	{
    		System.out.println( "Error with persisting data" );
    	};
    }
    
    /**
     * Removes G/L Acct Models data from database 
     */
    @Override
    public void removeFromDB()
    {
    	GL glAcct  = fields.get( "glAcct" );
    	
    	// If EntityManager object is created and TractnsSimulationModel argument is specified
        if ( glAcct != null  )
        {
        	COA chart = glAcct.getChOfAccs();
        	
        	if ( chart != null )
        	{
        		COAModel chartModel = COAModel.getByChOfAccs( chart );
        		
        		int idx = charts.indexOf( chartModel );
            	if ( idx >= 0 )
            		list[idx].remove( this );
        	}
        	
        	Database.removeFromDB( glAcct );
        }
    }
    
    /*                        Constructors                                                                                  */
    /************************************************************************************************************************/
    /**
     * Class default constructor
     */
	public GLAcctModel()
    {
        super( glAcctStr );
    }
    
	/**
	 * Class constructor 
	 * @param chOfAccsNumber Index of ChOfAccs
	 */
    public GLAcctModel( int chOfAccsNumber )
    {
        super( glAcctStr );
        list[chOfAccsNumber].add( this );
    }
    
    /**
     * Class constructor
     * @param stage Stage object where to display form
     * @throws Exception
     */
    public GLAcctModel( Stage stage ) throws Exception
    {
        super( stage, glAcctStr );
        
        if ( stage != null )
        	list[0].add( this );
    }	
    
    /**
     * Class constructor
     * @param stage Stage object where to display form
     * @param chOfAccsNumber Index of ChOfAccs
	 * @throws Exception
     */
    public GLAcctModel( Stage stage, int chOfAccsNumber ) throws Exception
    {
        super( stage, glAcctStr );
        
        if ( stage != null )
        	list[chOfAccsNumber].add( this );
    }
    
} // End of class ** GLAcctModel **