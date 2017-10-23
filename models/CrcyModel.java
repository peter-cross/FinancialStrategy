package models;

import javafx.stage.Stage;

import java.util.LinkedHashSet;
import java.util.List;

import application.Database;
import entities.Crcy;
import entities.HashMap;
import forms.DialogElement;

import static interfaces.Utilities.getListElementBy;

/**
 * Class CrcyModel - to store a list of foreign crcies
 * @author Peter Cross
 */
public class CrcyModel extends RegistryItemModel
{
    private static  LinkedHashSet list;       // List of Items
    
    private static final String crcyStr = HashMap.getByKey( "Crcy" );
    
    /**
     * Gets string representation of class object
     * @return CrcyModel Code
     */
    public String toString()
    {
        return (String) fields.get( "code" );
    }
    
    /**
     * Get CrcyModel object by CrcyModel code
     * @param code CrcyModel Code
     * @return CrcyModel object
     */
    public static CrcyModel getByCode( String code )
    {
        return (CrcyModel) getListElementBy( list, "code", code );
        
    } // End of method ** getByCode **
    
    /**
     * gets Crcy Model by Crcy entity object
     * @param currency Crcy entity object
     * @return Crcy Model object
     */
    public static CrcyModel getByCrcy( Crcy crcy )
    {
    	return (CrcyModel) getListElementBy( list, "crcy", crcy );
    }
    
    /**
     * Creates dialog elements for dialog header
     * @return Array of Header dialog elements
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
        
        hdr = new DialogElement( "Name" );
        hdr.valueType = "Text";
        hdr.width = 200;
        hdr.textValue = fieldTextValue( "name" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][1] = hdr;
        
        return header;
    }

    /**
     * Saves input information into object attributes
     * @param header Array of input from header
     * @param table Array of input from table
     */
    @Override
    protected void init( String[][] header, String[][][] table ) 
    {
        if ( header.length == 0 )
            return;
        
        fields.set( "code", header[0][0] );
        fields.set( "name", header[0][1] );
    }

    /**
     * Returns Crcy database entity object for current model
     */
    public Crcy  getCrcy ()
    {
    	return fields.get( "crcy" );
    }
    
    /**
     * Get List of Crcies in ArrayList
     * @return ArrayList with list of Crcies
     */
    public static LinkedHashSet getItemsList()
    {
        return list;
    }
    
    /**
     * Creates List of Crcies
     * @return Array with List of Crcies
     */
    public static  LinkedHashSet[] createList()
    {
    	if ( list == null )
            createNewList();
        
        return new LinkedHashSet[] { list };
    }
    
    /**
     * Creates new list of Crcy  Models
     */
    public static void createNewList()
    {
        list = new LinkedHashSet<>();
        
        createNewList( list, "CrcyModel" );
    }
    
    /**
	 * Gets Curcy's from database
	 * @return List of Crcy objects
	 */
    public static List<Crcy> getFromDB()
    {
    	return getFromDB( "Crcy" );
    }
    
    /**
     * Saves Crcy data to Database
     */
    protected void saveToDB()
    {
    	String	code = fields.get( "code" ),	// Crcy code in the system
                name = fields.get( "name" );	// Common name
       
    	// Get Crcy object from the fields of current model
    	Crcy crcy = fields.get( "crcy" );
    	
    	// If Crcy object is not created yet
    	if ( crcy == null  )
        {
            // Create instance of Crcy 
    		crcy = new Crcy( code, name );
            fields.set( "crcy", crcy );
        }
            
    	// Otherwise
    	else
            // Update Crcy information
    		crcy.update( code, name );
    	
    	try
    	{
    		// Persist Crcy data to database
    		Database.persistToDB( crcy );
    	}
    	catch ( Exception e ) 
    	{
    		System.out.println( "Error with persisting data" );
    	}
    }
    
    /**
     * Removes Crcy data from database
     * @throws Exception 
     */
    @Override
    public void removeFromDB() throws Exception
    { 
    	removeFromDB( "crcy" );
    	
    	list.remove( this );
    }
    
    /**
     * Gets instance of created CrcyModel object
     * @param c Crcy entity object
     * @return CrcyModel object
     */
    public static CrcyModel getInstance( Object c )
    {
    	return new CrcyModel( (Crcy) c );
    }
    
    /**
     * Class default constructor
     */
    public CrcyModel()
    {
        super( crcyStr );
        list.add( this );
    }
    
    /**
     * Class constructor
     * @param stage Stage object where to display
     * @throws Exception
     */
    public CrcyModel( Stage stage ) throws Exception
    {
        super( stage, crcyStr, null );
        
        if ( stage != null )
        	list.add( this );
    }    
    
    /**
     * Class constructor
     * @param c Crcy entity object
     */
    public CrcyModel( Crcy c )
    {
    	super( crcyStr );
        
    	fields.set( "crcy", c );
    	fields.set( "code", c.getCode() );
        fields.set( "name", c.getName() );
    }
} // End of class ** CrcyModel **