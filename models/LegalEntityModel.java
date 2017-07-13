package models;

import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.transaction.SystemException;

import java.util.LinkedHashSet;
import java.util.List;

import application.Database;
import entities.LegalEntity;
import forms.DialogElement;
import forms.TableOutput;

import static interfaces.Utilities.getListElementBy;

/**
 * Class LegalEntityModel 
 * @author Peter Cross
 *
 */
public class LegalEntityModel extends RegistryItemModel
{
    protected static  LinkedHashSet  list;       // List of Registry Items
    
    /**
     * Get LegalEntityModel object by Legal Entity name
     * @param name Legal Entity name
     * @return LegalEntityModel object
     */
    public static LegalEntityModel getByName( String name )
    {
        return (LegalEntityModel) getListElementBy( list, "name", name );
        
    } // End of method ** getByName **
    
    
    public static LegalEntityModel getByEntity( LegalEntity entity )
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
        
    } // End of method ** getByName **
    
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
    }
    
    /**
     * Displays form for entering or editing model data
     */
    @Override
    public TableOutput getOutput() throws IllegalStateException, SecurityException, SystemException 
    {
    	// Invoke inherited class method
    	TableOutput output = super.getOutput();
    	String[][] header = output.header;
    	
    	// If there is data entered or changed
    	if ( header != null && header.length > 0 && header[0].length > 0 )
    		// Save data to database
    		saveToDB();
    		
    	return output;
    }
    
    /**
     * Returns Legal Entity database entity object for current model
     */
    public LegalEntity getLegalEntity()
    {
    	return fields.get( "legalEntity" );
    }
    
    /**
     * Saves Legal Entity data to Database
     */
    private void saveToDB()
    {
    	String	iD = fields.get( "iD" ),						// Legal Entity ID in the system
                name = fields.get( "name" ),					// Common name
                legalName = fields.get( "legalName" ),			// Official legal name
                phone = fields.get( "phone" ),					// Phone number
                contact = fields.get( "contact" ),				// Contact in the company
                address = fields.get( "address" );				// Company address
    	
    	// Get Legal Entity object from the fields of current model
    	LegalEntity legalEntity = fields.get( "legalEntity" );
    	
    	// If Legal Entity object is not created yet
    	if ( legalEntity == null  )
        {
            // Create instance of Legal Entity
            legalEntity = new LegalEntity( iD, name, legalName, phone, contact, address );
            fields.set( "legalEntity", legalEntity );
        }
            
    	
    	// Otherwise
    	else
            // Update Legal Entity information
            legalEntity.update( iD, name, legalName, phone, contact, address );
    	
    	try
    	{
    		// Persist Legal Entity data to database
    		Database.persistToDB( legalEntity );
    	}
    	catch ( Exception e ) 
    	{
    		System.out.println( "Error with persisting data" );
    	};
    }
    
    /**
	 * Gets Legal Entities from database
	 * @return List of LegalEntity objects
	 */
    private static List<LegalEntity> getFromDB()
    {
    	// Get Entity Manager
    	EntityManager em = Database.getEntityManager();
    	
        if ( em != null )
        	try
        	{
        		// Do query for LegalEntity entity in DB and return results of query
        		return em.createQuery( "SELECT t FROM LegalEntity AS t" ).getResultList();
            }
        	catch ( Exception e )
        	{
        		return null;
        	}
		
        return null;
    }
    
    /**
     * Removes Legal Entity data from database
     */
    @Override
    public void removeFromDB()
    { 
        // Get Legal Entity from the fields
    	LegalEntity legalEntity = fields.get( "legalEntity" );
    	
    	fields.set( "legalEntity", null );
        
        // If Legal Entity is created as an object
    	if ( legalEntity != null )
           // Remove Legal Entity data from database
            Database.removeFromDB( legalEntity ) ;
        
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
    
    public static void createNewList()
    {
        list = new LinkedHashSet<>();
        	
        // Get list of Legal Entities from database
        List<LegalEntity> dbLglEntities = getFromDB();

        // If list is not empty
        if ( dbLglEntities != null && dbLglEntities.size() > 0 )
            // Loop for each Legal Entity
            for ( LegalEntity le : dbLglEntities )
                    // Create LegalEntityModel object based on provided Legal Entity
                    list.add( new LegalEntityModel(le) );
    }
    
    /**
     * Class default constructor
     */
    public LegalEntityModel()
    {
        super( "Legal Entity" );
        list.add( this );
    }
    
    /**
     * Class constructor for displaying Legal Entity on the stage 
     * @param stage Stage where to display
     * @throws Exception
     */
    public LegalEntityModel( Stage stage ) throws Exception
    {
        super( stage, "Legal Entity", null );
        
        if ( stage != null )
            list.add( this );
    }
    
    /**
     * Class constructor for recreating Legal Entity from database data
     * @param le Legal Entity database entity object
     */
    public LegalEntityModel( LegalEntity le )
    {
    	super( "Legal Entity" );
    	fields.set( "legalEntity", le );
    	
    	fields.set( "iD", le.getId() );
        fields.set( "name", le.getName() );
        fields.set( "legalName", le.getLegalName()  );
        fields.set( "phone", le.getPhone()  );
        fields.set( "contact", le.getContact() );
        fields.set( "address", le.getAddress() );
    }

} // End of class ** LegalEntityModel **