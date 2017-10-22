package models;

import static interfaces.Utilities.getListElementBy;

import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.List;

import application.Database;
import entities.Crcy;
import entities.Hash;
import entities.TractnsDscr;
import forms.DialogElement;
import javafx.stage.Stage;

/**
 * Class TractnsDscrModel - Tractns Description Model
 * @author Peter Cross
 *
 */
public class TractnsDscrModel extends RegistryItemModel
{
	private static  LinkedHashSet list;       // List of Items
    
    private static final String tractsDscrStr = "Transaction Description";

    /**
     * Default class constructor
     */
    public TractnsDscrModel()
    {
    	super( tractsDscrStr );
    	list.add( this );
    }
    
    /**
     * Class constructor for objects to display
     * @param st Stage object where to display
     * @throws Exception
     */
    public TractnsDscrModel( Stage st ) throws Exception
    {
    	super( st, tractsDscrStr, null );
    	
    	if ( st != null )
    		list.add( this );
    }
    
    /**
     * Class constructor for creating object based on corresponding database entity object
     * @param tractnsDscr Database entity tractns description object
     */
    public TractnsDscrModel( TractnsDscr tractnsDscr )
    {
    	super( tractsDscrStr );
    	
    	fields.set( "tractnsDscr", tractnsDscr );
    	fields.set( "code", tractnsDscr.getCode() );
    	fields.set( "description", tractnsDscr.getDescription() );
    }
    
    /**
     * Returns Tractns Description Model object based on model code
     */
    public static TractnsDscrModel getByCode( String code )
    {
        return (TractnsDscrModel) getListElementBy( list, "code", code );
        
    } // End of method ** getByCode **
    
    /**
     * Gets invoked to save entered or changed info from screen form
     */
	@Override
	protected void init(String[][] header, String[][][] table) 
	{
		if ( header.length == 0 )
            return;
        
        fields.set( "code", header[0][0] );
        fields.set( "description", header[0][1] );
	}

	/**
	 * Gets invoked to object about structure of head part of the screen form
	 */
	@Override
	protected DialogElement[][] createHeader() 
	{
		// Create array for header dialog elements
        DialogElement[][] header = new DialogElement[1][2];
        // Helper object for creating new dialog elements
        DialogElement hdr;
        
        hdr = new DialogElement( "Code" );
        hdr.valueType = "Text";
        hdr.width = 60;
        hdr.textValue = fieldTextValue( "code" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][0] = hdr;
        
        hdr = new DialogElement( "Description                  " );
        hdr.valueType = "Text";
        hdr.width = 400;
        hdr.textValue = fieldTextValue( "description" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][1] = hdr;
        
        return header;
	}

	/**
	 * Returns corresponding database entity object for Tractns Description Model
	 */
	public TractnsDscr getTractnsDscr()
	{
		return fields.get( "tractnsDscr" );
	}
	
	/**
	 * Returns description of Tractns Description Model
	 */
	public String getDescription()
	{
		return fields.get( "description" );
	}
	
	/**
	 * Returns form items list as LinedHashSet object
	 */
	public static LinkedHashSet getItemsList()
    {
        return list;
    }
    
	/**
	 * Creates list for new form as LinedHashSet object if it does not exist and returns it
	 */
    public static  LinkedHashSet[] createList()
    {
    	if ( list == null )
            createNewList();
        
        return new LinkedHashSet[] { list };
    }
    
    /**
     *  Creates list of Class object items as LinedHashSet object
     */
    public static void createNewList()
    {
        list = new LinkedHashSet<>();
        
        createNewList( list, "TractnsDscrModel" );
    }
    
    /**
     * Gets instance of class object based on corresponding database entity object
     * @param c Database entity object
     * @return Created Tractns Description Model object
     */
    public static TractnsDscrModel getInstance( Object c )
    {
    	return new TractnsDscrModel( (TractnsDscr) c );
    }
    
    /**
     * Gets data about corresponding database entity objects from database and returns it as a list
     */
    public static List<TractnsDscr> getFromDB()
    {
    	return getFromDB( "TractnsDscr" );
    }
    
    /**
     * Saves data about current object into database as corresponding database entity object
     */
	@Override
	protected void saveToDB() 
	{
		String	code = fields.get( "code" ),				// 
                description = fields.get( "description" );	// 
       
    	TractnsDscr tractnsDscr = fields.get( "tractnsDscr" );
    	
    	if ( tractnsDscr == null  )
        {
            // Create instance of Tractns Description Entity
    		tractnsDscr = new TractnsDscr( code, description );
            fields.set( "tractnsDscr", tractnsDscr );
        }
            
    	// Otherwise
    	else
            // Update Tractns Description Entity information
    		tractnsDscr.update( code, description );
    	
    	try
    	{
    		// Persist Tractns Description Entity data to database
    		Database.persistToDB( tractnsDscr );
    	}
    	catch ( Exception e ) 
    	{
    		System.out.println( "Error with persisting data" );
    	}
	} 
	
	/**
	 * Removes data about current class object from database and from list of class object items
	 */
	@Override
    public void removeFromDB() throws Exception
    { 
    	removeFromDB( "tractnsDscr" );
    	
    	list.remove( this );
    }
	
	/**
	 * Returns string representation about current class object
	 */
	public String toString()
    {
        return (String) fields.get( "description" );
    }   
}