package models;

import javafx.stage.Stage;
import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import application.Database;
import entities.COA;
import entities.LglEntity;
import entities.TractnsModel;
import views.OneColumnTableView;
import forms.DialogElement;
import forms.TableElement;
import forms.TableOutput;
import interfaces.Utilities;

import static interfaces.Utilities.getListElementBy;

/**
 * Class LegalEntityModel 
 * @author Peter Cross
 *
 */
public class LegalEntityModel extends RegistryItemModel
{
    protected static  LinkedHashSet  list;       // List of Registry Items
    
    private static final String lglEntityStr = "Legaal Entity";
    
    /**
     * Get LegalEntityModel object by Legal Entity name
     * @param name Legal Entity name
     * @return LegalEntityModel object
     */
    public static LegalEntityModel getByName( String name )
    {
        return (LegalEntityModel) getListElementBy( list, "name", name );
        
    } // End of method ** getByName **
    
    /**
     * Gets Legal Entity Model by database entity object
     * @param entity Legal Entity database entity object
     * @return Legal Entity Model object
     */
    public static LegalEntityModel getByEntity( LglEntity entity )
    {
    	return (LegalEntityModel) getListElementBy( list, "legalEntity", entity );
    }
    
    /**
     * Get LegalEntityModel object by Legal Entity ID
     * @param ID Legal Entity ID
     * @return LegalEntityModel object
     */
    public static LegalEntityModel getByID( String ID )
    {
        return (LegalEntityModel) getListElementBy( list, "iD", ID );
        
    } // End of method ** getByID **
    
    /**
     * Creates header element settings of the form
     * @return array of header elements settings
     */
    @Override
    protected DialogElement[][] createHeader() 
    {
        DialogElement[][] header = new DialogElement[1][6];
        
        DialogElement hdr;
        
        hdr = new DialogElement( "ID  " );
        hdr.attributeName = "iD";
        hdr.valueType = "Text";
        hdr.width = 60;
        hdr.textValue = fieldTextValue( "iD" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][0] = hdr;
        
        hdr = new DialogElement( "Name     " );
        hdr.valueType = "Text";
        hdr.width = 300;
        hdr.textValue = fieldTextValue( "name" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][1] = hdr;
        
        hdr = new DialogElement( "Legal Name   " );
        hdr.valueType = "Text";
        hdr.width = 300;
        hdr.textValue = fieldTextValue( "legalName" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][2] = hdr;
        
        hdr = new DialogElement( "Phone      " );
        hdr.valueType = "Text";
        hdr.width = 200;
        hdr.textValue = fieldTextValue( "phone" );
        header[0][3] = hdr;
        
        hdr = new DialogElement( "Contact" );
        hdr.valueType = "Text";
        hdr.width = 200;
        hdr.textValue = fieldTextValue( "contact" );
        header[0][4] = hdr;
        
        hdr = new DialogElement( "Address           " );
        hdr.valueType = "Text";
        hdr.width = 300;
        hdr.textValue = fieldTextValue( "address" );
        header[0][5] = hdr;
        
        return header;
    }

    /**
     * Creates table elements of model form
     */
    @Override
    protected TableElement[][] createTable()
    {
        TableElement[][] table = new TableElement[1][2];
        TableElement tblEl;
        
        tblEl = new TableElement( "Chart Of Accounts Name" );
        tblEl.width = 200;
        tblEl.editable = false;
        tblEl.textValue = (String[]) fields.get( "chartName" );
        tblEl.valueType = "Text";
        table[0][0] = tblEl;
        
        tblEl = new TableElement( "Chart Of Accounts" );
        tblEl.width = 250;
        tblEl.editable = false;
        tblEl.textValue = (String[]) fields.get( "chOfAccs" );
        tblEl.valueType = "List";
        tblEl.list = COAModel.getItemsList();
        table[0][1] = tblEl;
        
        return table;
    }
    
    /**
     * Initializes fields of the form with given values of header and table
     * @param header Text values of the header
     * @param table Text values of table part
     */
    @Override
    protected void init( String[][] header, String[][][] table ) 
    {
        if ( header.length > 0 )
        {
            fields.set( "iD",   	header[0][0] );
            fields.set( "name", 	header[0][1] );
            fields.set( "legalName",header[0][2] );
            fields.set( "phone",   	header[0][3] );
            fields.set( "contact", 	header[0][4] );
            fields.set( "address", 	header[0][5] );
        }
        
        if ( table != null )
        {
            int numCharts = table[0].length;
            
            String[] chartNames = new String[numCharts],
            		chOfAccs = new String[numCharts];
            
            for ( int i = 0; i < numCharts; i++ )
            {
            	chartNames[i] = table[0][i][0];
            	chOfAccs[i] = table[0][i][1];
            }
            
            fields.set( "chartName", chartNames );
            fields.set( "chOfAccs", chOfAccs );
        }
        else
        {
        	fields.set( "chartName", new String[] {} );
            fields.set( "chOfAccs", new String[] {} );
        }
    }
    
    /**
     * Displays model dialog form
     */
    @Override
    public void display()
    {
        // Create header elements array and add it to attributes list
        attributesList.set( "header", createHeader() );
        // Create table elements array and add it to attributes list
        attributesList.set( "table", createTable() );
        
        // Add output results to attributes list
        attributesList.set( "output", getOutput() );
    }
    
    /**
     * Invokes model dialog form and returns results of interaction with the form
     */
    @Override
    public TableOutput getOutput()
    {
        // Display input form and get the results of input
        TableOutput result =  new OneColumnTableView( this, lglEntityStr, 5 ).result();
        
        // Assign the input results to object properties
        init( result.header, result.table );
        
        // If there is data entered or changed
    	if ( result.header != null && result.header.length > 0 && result.header[0].length > 0 )
    		// Save data to database
    		saveToDB();
    	
        // Return the input result
        return result;
        
    } // End of method ** getOutput **
    
    /**
     * Returns Legal Entity database entity object for current model
     */
    public LglEntity getLegalEntity()
    {
    	return fields.get( "legalEntity" );
    }
    
    /**
     * Gets string array of Legal Entity ChOfAccs
     * @return String array
     */
    public String[] getChartOfAccounts()
    {
    	return (String[]) fields.get( "chOfAccs" );
    }
    
    /**
     * Gets names for legal entity ChOfAccs
     * @return
     */
    public String[] getChartNames()
    {
    	return (String[]) fields.get( "chartName" );
    }
    
    /**
     * Saves Legal Entity data to Database
     */
    protected void saveToDB()
    {
    	String	iD = fields.get( "iD" ),						// Legal Entity ID in the system
                name = fields.get( "name" ),					// Common name
                legalName = fields.get( "legalName" ),			// Official legal name
                phone = fields.get( "phone" ),					// Phone number
                contact = fields.get( "contact" ),				// Contact in the company
                address = fields.get( "address" );				// Company address
    	
    	List<String> chartNames = new ArrayList<String>();
    	for ( String chName : (String[]) fields.get( "chartName" ) )
    		chartNames.add( chName );
    	
    	ArrayList<COA> chOfAccs = new ArrayList<>();
    	for ( String chart : (String[]) fields.get( "chOfAccs" ) )
    	{
    		COAModel chModel = COAModel.getByName( chart );
    		
    		if ( chModel != null )
    			chOfAccs.add( chModel.getChOfAccs() );
    	}	
    	
    	// Get Legal Entity object from the fields of current model
    	LglEntity legalEntity = fields.get( "legalEntity" );
    	
    	// If Legal Entity object is not created yet
    	if ( legalEntity == null  )
        {
            // Create instance of Legal Entity
            legalEntity = new LglEntity( iD, name, legalName, phone, contact, address, chartNames, chOfAccs );
            fields.set( "legalEntity", legalEntity );
        }
            
    	// Otherwise
    	else
            // Update Legal Entity information
            legalEntity.update( iD, name, legalName, phone, contact, address, chartNames, chOfAccs );
    	
    	try
    	{
    		// Persist Legal Entity data to database
    		Database.persistToDB( legalEntity );
    	}
    	catch ( Exception e ) 
    	{
    		System.out.println( "Error with persisting data" );
    	}
    }
    
    /**
	 * Gets Legal Entities from database
	 * @return List of LglEntity objects
	 */
    public static List<LglEntity> getFromDB()
    {
    	return getFromDB( "LglEntity" );
    }
    
    /**
     * Removes Legal Entity data from database
     * @throws Exception 
     */
    @Override
    public void removeFromDB() throws Exception
    { 
    	// Get Entity Manager
    	EntityManager em = Database.getEntityManager();
    	
    	// Get Legal Entity from the fields
    	LglEntity legalEntity = fields.get( "legalEntity" );
    	
    	// If Legal Entity is created as an object
    	if ( legalEntity != null )
    	{
    		List<TractnsModel> tms = null;
    		
    		try
        	{
        		// Do query for TractnsSimulationModel entity in DB and return results of query
    			tms = (List<TractnsModel>) em.createQuery( "SELECT t FROM TractnsModel AS t WHERE t.lglEntity = :lglEntity" )
    				    .setParameter( "lglEntity", legalEntity )
    				    .getResultList();
            }
        	catch ( Exception e ) { }
    		
    		if ( tms != null && tms.size() > 0 )
    		{
    			int choice = Utilities.getYesNo( "There are Transaction Models for selected Legal Entity.\nDo you want to delete this entity with all Transaction Models?" );
    			
    			if ( choice == 0  )
    			{
    				for ( TractnsModel tm : tms  )
        				Database.removeFromDB( tm );
        			
        			// Remove Legal Entity data from database
    	            Database.removeFromDB( legalEntity ) ;
    	            list.remove( this );
    			}
    			else
    				throw new Exception( "Can't delete" );
    		}
    		else
    		{
    			// Remove Legal Entity data from database
	            Database.removeFromDB( legalEntity );
	            list.remove( this );
    		}
	    }
    	else
    		list.remove( this );
    }
    
    /**
     * Returns the list of stored items
     * @return List of stored items
     */
    public static LinkedHashSet getItemsList()
    {
        return list;
    }
    
    /**
     * Creates list object to store items and adds hard-coded items
     * @return Created list
     */
    public static LinkedHashSet[] createList()
    {
        if ( list == null )
            createNewList();
        
        return new LinkedHashSet[] { list };
    }
    
    /**
     * Creates new list of Legal Entity models
     */
    public static void createNewList()
    {
        list = new LinkedHashSet<>();
        	
        createNewList( list, "LegalEntityModel" );
    }
    
    /**
     * Class default constructor
     */
    public LegalEntityModel()
    {
        super( lglEntityStr );
        
        list.add( this );
    }
    
    /**
     * Class constructor for displaying Legal Entity on the stage 
     * @param stage Stage where to display
     * @throws Exception
     */
    public LegalEntityModel( Stage stage ) throws Exception
    {
        super( stage, lglEntityStr, null );
        
        if ( stage != null )
            list.add( this );
    }
    
    /**
     * Create instance of LegalEntityModel 
     * @param le LglEntity database entity object
     * @return LegalEntityModel object
     */
    public static LegalEntityModel getInstance( Object le )
    {
    	return new LegalEntityModel( (LglEntity) le );
    }
    
    /**
     * Class constructor for recreating Legal Entity from database data
     * @param le Legal Entity database entity object
     */
    public LegalEntityModel( LglEntity le )
    {
    	super( lglEntityStr );
    	fields.set( "legalEntity", le );
    	
    	fields.set( "iD", le.getId() );
        fields.set( "name", le.getName() );
        fields.set( "legalName", le.getLegalName()  );
        fields.set( "phone", le.getPhone()  );
        fields.set( "contact", le.getContact() );
        fields.set( "address", le.getAddress() );
        
        fields.set( "chartName", le.getChartNames() ); 
		fields.set( "chOfAccs", le.getChOfAccs() );
    }

} // End of class ** LegalEntityModel **