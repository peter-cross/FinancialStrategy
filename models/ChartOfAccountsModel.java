package models;

import javafx.stage.Stage;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import forms.DialogElement;
import foundation.AssociativeList;
import application.Database;
import entities.ChartOfAccounts;
import entities.Currency;

import static interfaces.Utilities.getListElementBy;

/**
 * Class ChartOfAccountsModel - To create and store Charts of Accounts
 * @author Peter Cross
 */
public class ChartOfAccountsModel extends RegistryItemModel
{
    protected static  LinkedHashSet list;       // List of Items
    
    /**
     * Gets Chart of Accounts by its name
     * @param name Name of a Chart of Accounts
     * @return Chart of Accounts object
     */
    public static ChartOfAccountsModel getByName( String name )
    {
        return (ChartOfAccountsModel) getListElementBy( list, "name", name );
    
    } // End of method ** getByCode **
    
    /**
     * Gets Chart of Accounts by its code
     * @param code Code of a Chart of Accounts
     * @return Chart of Accounts object
     */
    public static ChartOfAccountsModel getByCode( String code )
    {
        return (ChartOfAccountsModel) getListElementBy( list, "code", code );
    
    } // End of method ** getByCode **
    
    /**
     * Gets Chart Of Accounts Model by Chart Of Accounts object
     * @param chartOfAccounts Chart Of Accounts object
     * @return Chart Of Accounts Model
     */
    public static ChartOfAccountsModel getByChartOfAccounts( ChartOfAccounts chartOfAccounts )
    {
    	return (ChartOfAccountsModel) getListElementBy( list, "chartOfAccounts", chartOfAccounts );
    }
    
    /**
     * Gets index of Chart Of Account by its code
     * @param code Chart Of Account's code
     * @return Index of Chart Of Account
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
                fields = ((ChartOfAccountsModel) it.next()).getFields();
                
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
     * Gets Chart of Accounts' index by Chart of Accounts' name
     * @param name Name of Chart of Accounts
     * @return Index of Chart of Accounts
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
                fields = ((ChartOfAccountsModel) it.next()).getFields();
                
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
     * Returns Chart Of Accounts database entity object for current model
     */
    public ChartOfAccounts getChartOfAccounts()
    {
    	return fields.get( "chartOfAccounts" );
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
     * Get List of Chart of Accounts in ArrayList
     * @return ArrayList with list of Chart of Accounts
     */
    public static LinkedHashSet getItemsList()
    {
        return list;
    }
    
    /**
     * Creates List of Chart of Accounts
     * @return Array with List of Chart of Accounts
     */
    public static  LinkedHashSet[] createList()
    {
    	if ( list == null )
            createNewList();
        
        return new LinkedHashSet[] { list };
    }
    
    /**
     * Creates new list of Chart of Accounts' models
     */
    public static void createNewList()
    {
        list = new LinkedHashSet<>();
        
        createNewList( list, "ChartOfAccountsModel" );
    }
    
    /**
     * Saves Chart Of Accounts data to Database
     */
    protected void saveToDB()
    {
    	String	 code	  = fields.get( "code" ),	// Currency code in the system
                 name 	  = fields.get( "name" );	// Common name
        Currency currency = fields.get( "currency" );
       
    	// Get ChartOfAccounts object from the fields of current model
    	ChartOfAccounts chartOfAccounts = fields.get( "chartOfAccounts" );
    	
    	// If Currency object is not created yet
    	if ( chartOfAccounts == null  )
        {
            // Create instance of Legal Entity
    		chartOfAccounts = new ChartOfAccounts( code, name, currency );
            fields.set( "chartOfAccounts", chartOfAccounts );
        }
        // Otherwise
    	else
            // Update Chart Of Accounts information
    		chartOfAccounts.update( code, name, currency );
    	
    	try
    	{
    		// Persist Chart Of Accounts data to database
    		Database.persistToDB( chartOfAccounts );
    	}
    	catch ( Exception e ) 
    	{
    		System.out.println( "Error with persisting data" );
    	}
    }
    
    /**
     * Removes Chart Of Accounts data from database
     * @throws Exception 
     */
    @Override
    public void removeFromDB() throws Exception
    { 
    	removeFromDB( "chartOfAccounts" );
    	
    	list.remove( this );
    }
    
    /**
	 * Gets Chart Of Accounts from database
	 * @return List of ChartOfAccounts objects
	 */
    public static List<ChartOfAccounts> getFromDB()
    {
    	return getFromDB( "ChartOfAccounts" );
    }
    
    /**
     * Gets instance of created ChartOfAccountsModel
     * @param chart ChartOfAccounts entity object
     * @return
     */
    public static ChartOfAccountsModel getInstance( Object chart )
    {
    	return new ChartOfAccountsModel( (ChartOfAccounts) chart );
    }
    
    /**
     * Class default constructor
     */
    public ChartOfAccountsModel()
    {
        super( "Chart of Accounts" );
        
        list.add( this );
    }
    
    /**
     * Class constructor
     * @param stage Stage object where to display form
     * @throws Exception
     */
    public ChartOfAccountsModel( Stage stage ) throws Exception
    {
    	super( stage, "Chart of Accounts", null );
        
        if ( stage != null )
        	list.add( this );
    }
    
    /**
     * Class constructor
     * @param chart Chart Of Accounts entity object 
     */
    public ChartOfAccountsModel( ChartOfAccounts chart )
    {
    	super( "Chart Of Accounts" );
        
    	fields.set( "chartOfAccounts", chart );
    	fields.set( "code", chart.getCode() );
        fields.set( "name", chart.getName() );
        fields.set( "currency", chart.getCurrency() );
    }
    
} // End of class ** ChartOfAccountsModel **