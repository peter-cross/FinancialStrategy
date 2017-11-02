package models;

import javafx.stage.Stage;
import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import application.Database;
import entities.ChOfAccs;
import entities.LglEntity;
import entities.TractnsModel;
import views.OneColumnTableView;
import forms.DialogElement;
import forms.TableElement;
import forms.TableOutput;
import interfaces.Utilities;

import static interfaces.Utilities.getListElementBy;
import static interfaces.Utilities.hash;

/**
 * Class LglEntityModel 
 * @author Peter Cross
 *
 */
public class LglEntityModel extends RegistryItemModel
{
    protected static  LinkedHashSet  list;       // List of Registry Items
    
    private static final String lglEntityStr = hash( "LglEntity" );
    
    /**
     * Get LglEntityModel object by Lgl Entity name
     * @param name Lgl Entity name
     * @return LglEntityModel object
     */
    public static LglEntityModel getByName( String name )
    {
        return (LglEntityModel) getListElementBy( list, "name", name );
        
    } // End of method ** getByName **
    
    /**
     * Gets Lgl Entity Model by database entity object
     * @param entity Lgl Entity database entity object
     * @return Lgl Entity Model object
     */
    public static LglEntityModel getByEntity( LglEntity entity )
    {
    	return (LglEntityModel) getListElementBy( list, "lglEntity", entity );
    }
    
    /**
     * Get LglEntityModel object by Lgl Entity ID
     * @param ID Lgl Entity ID
     * @return LglEntityModel object
     */
    public static LglEntityModel getByID( String ID )
    {
        return (LglEntityModel) getListElementBy( list, "iD", ID );
        
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
        
        hdr = new DialogElement( hash( "LglName" ) +  "   " );
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
        
        tblEl = new TableElement( hash("ChOfAccs") + " Name" );
        tblEl.width = 200;
        tblEl.editable = false;
        tblEl.textValue = (String[]) fields.get( "chartName" );
        tblEl.valueType = "Text";
        table[0][0] = tblEl;
        
        tblEl = new TableElement( hash("ChOfAccs") );
        tblEl.width = 250;
        tblEl.editable = false;
        tblEl.textValue = (String[]) fields.get( "chOfAccs" );
        tblEl.valueType = "List";
        tblEl.list = ChOfAccsModel.getItemsList();
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
     * Returns Lgl Entity database entity object for current model
     */
    public LglEntity getLglEntity()
    {
    	return fields.get( "lglEntity" );
    }
    
    /**
     * Gets string array of Lgl Entity ChOfAccs
     * @return String array
     */
    public String[] getChartOfAccounts()
    {
    	return (String[]) fields.get( "chOfAccs" );
    }
    
    /**
     * Gets names for Lgl entity ChOfAccs
     * @return
     */
    public String[] getChartNames()
    {
    	return (String[]) fields.get( "chartName" );
    }
    
    /**
     * Saves Lgl Entity data to Database
     */
    protected void saveToDB()
    {
    	String	iD = fields.get( "iD" ),						// Lgl Entity ID in the system
                name = fields.get( "name" ),					// Common name
                legalName = fields.get( "legalName" ),			// Official legal name
                phone = fields.get( "phone" ),					// Phone number
                contact = fields.get( "contact" ),				// Contact in the company
                address = fields.get( "address" );				// Company address
    	
    	List<String> chartNames = new ArrayList<String>();
    	for ( String chName : (String[]) fields.get( "chartName" ) )
    		chartNames.add( chName );
    	
    	ArrayList<ChOfAccs> chOfAccs = new ArrayList<>();
    	for ( String chart : (String[]) fields.get( "chOfAccs" ) )
    	{
    		ChOfAccsModel chModel = ChOfAccsModel.getByName( chart );
    		
    		if ( chModel != null )
    			chOfAccs.add( chModel.getChOfAccs() );
    	}	
    	
    	// Get Lgl Entity object from the fields of current model
    	LglEntity lglEntity = fields.get( "lglEntity" );
    	
    	// If Lgl Entity object is not created yet
    	if ( lglEntity == null  )
        {
            // Create instance of Lgl Entity
            lglEntity = new LglEntity( iD, name, legalName, phone, contact, address, chartNames, chOfAccs );
            fields.set( "lglEntity", lglEntity );
        }
            
    	// Otherwise
    	else
            // Update Lgl Entity information
            lglEntity.update( iD, name, legalName, phone, contact, address, chartNames, chOfAccs );
    	
    	try
    	{
    		// Persist Lgl Entity data to database
    		Database.persistToDB( lglEntity );
    	}
    	catch ( Exception e ) 
    	{
    		System.out.println( "Error with persisting data" );
    	}
    }
    
    /**
	 * Gets Lgl Entities from database
	 * @return List of LglEntity objects
	 */
    public static List<LglEntity> getFromDB()
    {
    	return getFromDB( "LglEntity" );
    }
    
    /**
     * Removes Lgl Entity data from database
     * @throws Exception 
     */
    @Override
    public void removeFromDB() throws Exception
    { 
    	// Get Entity Manager
    	EntityManager em = Database.getEntityManager();
    	
    	// Get Lgl Entity from the fields
    	LglEntity lglEntity = fields.get( "lglEntity" );
    	
    	// If Lgl Entity is created as an object
    	if ( lglEntity != null )
    	{
    		List<TractnsModel> tms = null;
    		
    		try
        	{
        		// Do query for TractnsSimulationModel entity in DB and return results of query
    			tms = (List<TractnsModel>) em.createQuery( "SELECT t FROM TractnsModel AS t WHERE t.lglEntity = :lglEntity" )
    				    .setParameter( "lglEntity", lglEntity )
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
        			
        			// Remove Lgl Entity data from database
    	            Database.removeFromDB( lglEntity ) ;
    	            list.remove( this );
    			}
    			else
    				throw new Exception( "Can't delete" );
    		}
    		else
    		{
    			// Remove Lgl Entity data from database
	            Database.removeFromDB( lglEntity );
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
     * Creates new list of Lgl Entity models
     */
    public static void createNewList()
    {
        list = new LinkedHashSet<>();
        	
        createNewList( list, "LglEntityModel" );
    }
    
    /**
     * Class default constructor
     */
    public LglEntityModel()
    {
        super( lglEntityStr );
        
        list.add( this );
    }
    
    /**
     * Class constructor for displaying Lgl Entity on the stage 
     * @param stage Stage where to display
     * @throws Exception
     */
    public LglEntityModel( Stage stage ) throws Exception
    {
        super( stage, lglEntityStr, null );
        
        if ( stage != null )
            list.add( this );
    }
    
    /**
     * Create instance of LglEntityModel 
     * @param le LglEntity database entity object
     * @return LglEntityModel object
     */
    public static LglEntityModel getInstance( Object le )
    {
    	return new LglEntityModel( (LglEntity) le );
    }
    
    /**
     * Class constructor for recreating Lgl Entity from database data
     * @param le Lgl Entity database entity object
     */
    public LglEntityModel( LglEntity le )
    {
    	super( lglEntityStr );
    	fields.set( "lglEntity", le );
    	
    	fields.set( "iD", le.getId() );
        fields.set( "name", le.getName() );
        fields.set( "legalName", le.getLglName()  );
        fields.set( "phone", le.getPhone()  );
        fields.set( "contact", le.getContact() );
        fields.set( "address", le.getAddress() );
        
        fields.set( "chartName", le.getChartNames() ); 
		fields.set( "chOfAccs", le.getChOfAccs() );
    }

} // End of class ** LglEntityModel **