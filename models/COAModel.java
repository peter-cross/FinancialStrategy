package models;

import javafx.stage.Stage;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import forms.DialogElement;
import foundation.AssociativeList;
import application.Database;
import entities.COA;
import entities.Currency;

import static interfaces.Utilities.getListElementBy;

/**
 * Class COAModel - To create and store ChOfAccs
 * @author Peter Cross
 */
public class COAModel extends RegistryItemModel
{
    protected static  LinkedHashSet list;       // List of Items
    
    /**
     * Gets ChOfAcc by its name
     * @param name Name of a ChOfAcc
     * @return COA Model object
     */
    public static COAModel getByName( String name )
    {
        return (COAModel) getListElementBy( list, "name", name );
    
    } // End of method ** getByName **
    
    /**
     * Gets ChOfAcc by its code
     * @param code Code of a ChOfAccs
     * @return COA Model object
     */
    public static COAModel getByCode( String code )
    {
        return (COAModel) getListElementBy( list, "code", code );
    
    } // End of method ** getByCode **
    
    /**
     * Gets COA Model by COA object
     * @param chOfAcc COA object
     * @return COA Model
     */
    public static COAModel getByChOfAccs( COA chOfAcc )
    {
    	return (COAModel) getListElementBy( list, "COA", chOfAcc );
    }
    
    /**
     * Gets index of ChOfAcc by its code
     * @param code ChOfAcc's code
     * @return Index of ChOfAcc
     */
    public static int getIndexByCode( String code )
    {
        AssociativeList fields;
        
        try
        {
            Iterator it = list.iterator();
            
            int i = 0;
            while ( it.hasNext() )
            {
                fields = ((COAModel) it.next()).getFields();
                
                if ( fields !=  null && (String) fields.get( "code" ) == code )
                    return i;
                else
                    i++;
            }
        }
        catch ( Exception e )
        {
            return -1;
        }
        
        return -1;
    }
    
    /**
     * Gets ChOfAcc's index by ChOfAcc's name
     * @param name Name of ChOfAcc
     * @return Index of ChOfAcc
     */
    public static int getIndexByName( String name )
    {
        AssociativeList fields;
        
        try
        {
            Iterator it = list.iterator();
            
            int i = 0;
            while ( it.hasNext() )
            {
                fields = ((COAModel) it.next()).getFields();
                
                String fieldsName = (String) fields.get( "name" );
                
                if ( fields !=  null && fieldsName.equals( name ) )
                    return i;
                else
                    i++;
            }
        }
        catch ( Exception e )
        {
            return -1;
        }
        
        return -1;
    }
    
    /**
     * Creates dialog elements for dialog header
     * @return Array of Header dialog elements
     */
    @Override
    protected DialogElement[][] createHeader() 
    {
        // Create array for header dialog elements
        DialogElement[][] header = new DialogElement[1][3];
        // Helper object for creating new dialog elements
        DialogElement hdr;
        
        hdr = new DialogElement( "Code" );
        hdr.valueType = "Text";
        hdr.width = 70;
        hdr.textValue = fieldTextValue( "code" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][0] = hdr;
        
        hdr = new DialogElement( "Name         " );
        hdr.valueType = "Text";
        hdr.width = 200;
        hdr.textValue = fieldTextValue( "name" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][1] = hdr;
        
        hdr = new DialogElement( "Currency" );
        hdr.valueType = "List";
        hdr.list = CurrencyModel.createList()[0];
        hdr.width = 70;
        hdr.editable = false;
        
        Currency currency = fields.get( "currency" );
        hdr.textValue = ( currency !=  null ? currency.toString() : "" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][2] = hdr;
        
        return header;
    }

    /**
     * Returns ChOfAcc's database entity object for current model
     */
    public COA getChOfAccs()
    {
    	return fields.get( "COA" );
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
        
        CurrencyModel curModel =  CurrencyModel.getByCode( header[0][2] );
        
        if ( curModel != null )
            fields.set( "currency", curModel.getCurrency() );
    }

    /**
     * Get List of ChOfAcc's in ArrayList
     * @return ArrayList with list of ChOfAcc's
     */
    public static LinkedHashSet getItemsList()
    {
        return list;
    }
    
    /**
     * Creates List of ChOfAcc's
     * @return Array with List of ChOfAcc's
     */
    public static  LinkedHashSet[] createList()
    {
    	if ( list == null )
            createNewList();
        
        return new LinkedHashSet[] { list };
    }
    
    /**
     * Creates new list of ChOfAcc's models
     */
    public static void createNewList()
    {
        list = new LinkedHashSet<>();
        
        createNewList( list, "COAModel" );
    }
    
    /**
     * Saves ChOfAcc's data to Database
     */
    protected void saveToDB()
    {
    	String	 code	  = fields.get( "code" ),	// Currency code in the system
                 name 	  = fields.get( "name" );	// Common name
        Currency currency = fields.get( "currency" );
       
    	// Get COA object from the fields of current model
    	COA chOfAcc = fields.get( "COA" );
    	
    	// If Currency object is not created yet
    	if ( chOfAcc == null  )
        {
            // Create instance of Legal Entity
    		chOfAcc = new COA( code, name, currency );
            fields.set( "chartOfAccounts", chOfAcc );
        }
        // Otherwise
    	else
            // Update ChOfAccs information
    		chOfAcc.update( code, name, currency );
    	
    	try
    	{
    		// Persist ChOfAccs data to database
    		Database.persistToDB( chOfAcc );
    	}
    	catch ( Exception e ) 
    	{
    		System.out.println( "Error with persisting data" );
    	}
    }
    
    /**
     * Removes ChOfAccs data from database
     * @throws Exception 
     */
    @Override
    public void removeFromDB() throws Exception
    { 
    	removeFromDB( "COA" );
    	
    	list.remove( this );
    }
    
    /**
	 * Gets ChOfAccs from database
	 * @return List of COA objects
	 */
    public static List<COA> getFromDB()
    {
    	return getFromDB( "COA" );
    }
    
    /**
     * Gets instance of created COAModel
     * @param chart COA entity object
     * @return
     */
    public static COAModel getInstance( Object chart )
    {
    	return new COAModel( (COA) chart );
    }
    
    /**
     * Class default constructor
     */
    public COAModel()
    {
        super( "Chart of Accounts" );
        
        list.add( this );
    }
    
    /**
     * Class constructor
     * @param stage Stage object where to display form
     * @throws Exception
     */
    public COAModel( Stage stage ) throws Exception
    {
    	super( stage, "Chart of Accounts", null );
        
        if ( stage != null )
        	list.add( this );
    }
    
    /**
     * Class constructor
     * @param chart ChOfAccs entity object 
     */
    public COAModel( COA chart )
    {
    	super( "Chart Of Accounts" );
        
    	fields.set( "COA", chart );
    	fields.set( "code", chart.getCode() );
        fields.set( "name", chart.getName() );
        fields.set( "currency", chart.getCurrency() );
    }
    
} // End of class ** COAModel **