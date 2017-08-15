package models;

import javafx.stage.Stage;

import java.util.LinkedHashSet;
import java.util.List;

import application.Database;
import entities.Currency;
import forms.DialogElement;

import static interfaces.Utilities.getListElementBy;

/**
 * Class CurrencyModel - to store a list of foreign currencies
 * @author Peter Cross
 */
public class CurrencyModel extends RegistryItemModel
{
    private static  LinkedHashSet list;       // List of Items
    
    /**
     * Gets string representation of class object
     * @return CurrencyModel Code
     */
    public String toString()
    {
        return (String) fields.get( "code" );
    }
    
    /**
     * Get CurrencyModel object by CurrencyModel code
     * @param code CurrencyModel Code
     * @return CurrencyModel object
     */
    public static CurrencyModel getByCode( String code )
    {
        return (CurrencyModel) getListElementBy( list, "code", code );
        
    } // End of method ** getByCode **
    
    /**
     * gets Currency Model by Currency entity object
     * @param currency Currency entity object
     * @return Currency Model object
     */
    public static CurrencyModel getByCurrency( Currency currency )
    {
    	return (CurrencyModel) getListElementBy( list, "currency", currency );
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
     * Returns Currency database entity object for current model
     */
    public Currency  getCurrency ()
    {
    	return fields.get( "currency" );
    }
    
    /**
     * Get List of Currencies in ArrayList
     * @return ArrayList with list of Currencies
     */
    public static LinkedHashSet getItemsList()
    {
        return list;
    }
    
    /**
     * Creates List of Currencies
     * @return Array with List of Currencies
     */
    public static  LinkedHashSet[] createList()
    {
    	if ( list == null )
            createNewList();
        
        return new LinkedHashSet[] { list };
    }
    
    /**
     * Creates new list of Currency  Models
     */
    public static void createNewList()
    {
        list = new LinkedHashSet<>();
        
        createNewList( list, "CurrencyModel" );
    }
    
    /**
	 * Gets Currencies from database
	 * @return List of Currency objects
	 */
    public static List<Currency> getFromDB()
    {
    	return getFromDB( "Currency" );
    }
    
    /**
     * Saves Currency data to Database
     */
    protected void saveToDB()
    {
    	String	code = fields.get( "code" ),	// Currency code in the system
                name = fields.get( "name" );	// Common name
       
    	// Get Currency object from the fields of current model
    	Currency currency = fields.get( "currency" );
    	
    	// If Currency object is not created yet
    	if ( currency == null  )
        {
            // Create instance of Legal Entity
    		currency = new Currency( code, name );
            fields.set( "currency", currency );
        }
            
    	// Otherwise
    	else
            // Update Legal Entity information
    		currency.update( code, name );
    	
    	try
    	{
    		// Persist Legal Entity data to database
    		Database.persistToDB( currency );
    	}
    	catch ( Exception e ) 
    	{
    		System.out.println( "Error with persisting data" );
    	}
    }
    
    /**
     * Removes Currency data from database
     * @throws Exception 
     */
    @Override
    public void removeFromDB() throws Exception
    { 
    	removeFromDB( "currency" );
    	
    	list.remove( this );
    }
    
    /**
     * Gets instance of created CurrencyModel object
     * @param c Currency entity object
     * @return CurrencyModel object
     */
    public static CurrencyModel getInstance( Object c )
    {
    	return new CurrencyModel( (Currency) c );
    }
    
    /**
     * Class default constructor
     */
    public CurrencyModel()
    {
        super( "Currency" );
        list.add( this );
    }
    
    /**
     * Class constructor
     * @param stage Stage object where to display
     * @throws Exception
     */
    public CurrencyModel( Stage stage ) throws Exception
    {
        super( stage, "Currency", null );
        
        if ( stage != null )
        	list.add( this );
    }    
    
    /**
     * Class constructor
     * @param c Currency entity object
     */
    public CurrencyModel( Currency c )
    {
    	super( "Currency" );
        
    	fields.set( "currency", c );
    	fields.set( "code", c.getCode() );
        fields.set( "name", c.getName() );
    }
} // End of class ** CurrencyModel **