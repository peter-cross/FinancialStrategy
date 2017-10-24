package models;

import javafx.stage.Stage;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import forms.DialogElement;
import foundation.AssociativeList;
import application.Database;
import entities.ChOfAccs;
import entities.Crcy;
import entities.HashMap;

import static interfaces.Utilities.$;
import static interfaces.Utilities.getListElementBy;

/**
 * Class ChOfAccsModel - To create and store ChOfAccs
 * @author Peter Cross
 */
public class ChOfAccsModel extends RegistryItemModel
{
    protected static  LinkedHashSet list;       // List of Items
    
    private static final String chOfAccsStr = $( "ChOfAccs" );
    
    /**
     * Gets ChOfAcc by its name
     * @param name Name of a ChOfAcc
     * @return ChOfAccs Model object
     */
    public static ChOfAccsModel getByName( String name )
    {
        return (ChOfAccsModel) getListElementBy( list, "name", name );
    
    } // End of method ** getByName **
    
    /**
     * Gets ChOfAcc by its code
     * @param code Code of a ChOfAccs
     * @return ChOfAccs Model object
     */
    public static ChOfAccsModel getByCode( String code )
    {
        return (ChOfAccsModel) getListElementBy( list, "code", code );
    
    } // End of method ** getByCode **
    
    /**
     * Gets ChOfAccs Model by ChOfAccs object
     * @param chOfAcc ChOfAccs object
     * @return ChOfAccs Model
     */
    public static ChOfAccsModel getByChOfAccs( ChOfAccs chOfAcc )
    {
    	return (ChOfAccsModel) getListElementBy( list, "chOfAcc", chOfAcc );
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
                fields = ((ChOfAccsModel) it.next()).getFields();
                
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
                fields = ((ChOfAccsModel) it.next()).getFields();
                
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
        
        hdr = new DialogElement( $( "Crcy" ));
        hdr.attributeName = "crcy";
        hdr.valueType = "List";
        hdr.list = CrcyModel.createList()[0];
        hdr.width = 70;
        hdr.editable = false;
        
        Crcy crcy = fields.get( "crcy" );
        hdr.textValue = ( crcy !=  null ? crcy.toString() : "" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][2] = hdr;
        
        return header;
    }

    /**
     * Returns ChOfAcc's database entity object for current model
     */
    public ChOfAccs getChOfAccs()
    {
    	return fields.get( "chOfAcc" );
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
        
        CrcyModel curModel =  CrcyModel.getByCode( header[0][2] );
        
        if ( curModel != null )
            fields.set( "crcy", curModel.getCrcy() );
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
        
        createNewList( list, "ChOfAccsModel" );
    }
    
    /**
     * Saves ChOfAcc's data to Database
     */
    protected void saveToDB()
    {
    	String	 code	  = fields.get( "code" ),	// Crcy code in the system
                 name 	  = fields.get( "name" );	// Common name
        Crcy crcy = fields.get( "crcy" );
       
    	// Get ChOfAccs object from the fields of current model
    	ChOfAccs chOfAcc = fields.get( "chOfAcc" );
    	
    	// If Crcy object is not created yet
    	if ( chOfAcc == null  )
        {
            // Create instance of Legal Entity
    		chOfAcc = new ChOfAccs( code, name, crcy );
            fields.set( "chOfAcc", chOfAcc );
        }
        // Otherwise
    	else
            // Update ChOfAccs information
    		chOfAcc.update( code, name, crcy );
    	
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
    	removeFromDB( "ChOfAccs" );
    	
    	list.remove( this );
    }
    
    /**
	 * Gets ChOfAccs from database
	 * @return List of ChOfAccs objects
	 */
    public static List<ChOfAccs> getFromDB()
    {
    	return getFromDB( "ChOfAccs" );
    }
    
    /**
     * Gets instance of created ChOfAccsModel
     * @param chOfAcc ChOfAccs entity object
     * @return
     */
    public static ChOfAccsModel getInstance( Object chOfAcc )
    {
    	return new ChOfAccsModel( (ChOfAccs) chOfAcc );
    }
    
    /**
     * Class default constructor
     */
    public ChOfAccsModel()
    {
        super( chOfAccsStr );
        
        list.add( this );
    }
    
    /**
     * Class constructor
     * @param stage Stage object where to display form
     * @throws Exception
     */
    public ChOfAccsModel( Stage stage ) throws Exception
    {
    	super( stage, chOfAccsStr, null );
        
        if ( stage != null )
        	list.add( this );
    }
    
    /**
     * Class constructor
     * @param chart ChOfAccs entity object 
     */
    public ChOfAccsModel( ChOfAccs chart )
    {
    	super( chOfAccsStr );
        
    	fields.set( "chOfAcc", chart );
    	fields.set( "code", chart.getCode() );
        fields.set( "name", chart.getName() );
        fields.set( "crcy", chart.getCrcy() );
    }
    
} // End of class ** ChOfAccsModel **