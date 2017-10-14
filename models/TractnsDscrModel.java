package models;

import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.List;

import application.Database;
import entities.Crcy;
import entities.TractnsDscr;
import forms.DialogElement;
import javafx.stage.Stage;

/**
 * Class TractnsDscrModel - Transactions Description Model
 * @author Peter Cross
 *
 */
public class TractnsDscrModel extends RegistryItemModel
{
	private static  LinkedHashSet list;       // List of Items
    
    private static final String tractsDscrStr = "Transaction Description";

    public TractnsDscrModel()
    {
    	super( tractsDscrStr );
    	list.add( this );
    }
    
    public TractnsDscrModel( Stage st ) throws Exception
    {
    	super( st, tractsDscrStr, null );
    	
    	if ( st != null )
    		list.add( this );
    }
    
    public TractnsDscrModel( TractnsDscr tractnsDscr )
    {
    	super( tractsDscrStr );
    	
    	fields.set( "tractnsDscr", tractnsDscr );
    	fields.set( "code", tractnsDscr.getCode() );
    	fields.set( "description", tractnsDscr.getDescription() );
    }
    
	@Override
	protected void init(String[][] header, String[][][] table) 
	{
		if ( header.length == 0 )
            return;
        
        fields.set( "code", header[0][0] );
        fields.set( "description", header[0][1] );
	}

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

	public TractnsDscr getTractnsDscr()
	{
		return fields.get( "tractnsDscr" );
	}
	
	public static LinkedHashSet getItemsList()
    {
        return list;
    }
    
    public static  LinkedHashSet[] createList()
    {
    	if ( list == null )
            createNewList();
        
        return new LinkedHashSet[] { list };
    }
    
    public static void createNewList()
    {
        list = new LinkedHashSet<>();
        
        createNewList( list, "TractnsDscrModel" );
    }
    
    public static TractnsDscrModel getInstance( Object c )
    {
    	return new TractnsDscrModel( (TractnsDscr) c );
    }
    
    public static List<TractnsDscr> getFromDB()
    {
    	return getFromDB( "TractnsDscr" );
    }
    
	@Override
	protected void saveToDB() 
	{
		String	code = fields.get( "code" ),				// 
                description = fields.get( "description" );	// 
       
    	TractnsDscr tractnsDscr = fields.get( "tractnsDscr" );
    	
    	if ( tractnsDscr == null  )
        {
            // Create instance of Transactions Description Entity
    		tractnsDscr = new TractnsDscr( code, description );
            fields.set( "tractnsDscr", tractnsDscr );
        }
            
    	// Otherwise
    	else
            // Update Transactions Description Entity information
    		tractnsDscr.update( code, description );
    	
    	try
    	{
    		// Persist Transactions Description Entity data to database
    		Database.persistToDB( tractnsDscr );
    	}
    	catch ( Exception e ) 
    	{
    		System.out.println( "Error with persisting data" );
    	}
	} 
	
	@Override
    public void removeFromDB() throws Exception
    { 
    	removeFromDB( "tractnsDscr" );
    	
    	list.remove( this );
    }
	
	public String toString()
    {
        return (String) fields.get( "description" );
    }   
}