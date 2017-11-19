package models;

import javafx.stage.Stage;

import static interfaces.Utilities.getListElementBy;

import java.util.LinkedHashSet;
import java.util.List;

import application.Database;
import entities.Location;
import forms.DialogElement;

/**
 * Class LocationModel
 * @author Peter Cross
 */
public class LocationModel extends RegistryItemModel
{
    protected static  LinkedHashSet  list;       // List of Items
    
    
    public static LocationModel getByCrcy( Location lctn )
    {
    	return (LocationModel) getListElementBy( list, "lctn", lctn );
    }
    
    /**
     * Creates dialog elements for the form header
     * @return Array of dialog elements for form header
     */
    @Override
    protected DialogElement[][] createHeader() 
    {
        DialogElement[][] header = new DialogElement[1][5];
        
        DialogElement hdr;
        
        hdr = new DialogElement( "ID  " );
        hdr.attributeName = "iD";
        hdr.valueType = "Number";
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
        
        hdr = new DialogElement( "Address         " );
        hdr.valueType = "Text";
        hdr.width = 300;
        hdr.textValue = fieldTextValue( "address" );
        header[0][2] = hdr;
    
        hdr = new DialogElement( "Phone" );
        hdr.valueType = "Text";
        hdr.width = 200;
        hdr.textValue = fieldTextValue( "phone" );
        header[0][3] = hdr;
        
        hdr = new DialogElement( "Contact" );
        hdr.valueType = "Text";
        hdr.width = 200;
        hdr.textValue = fieldTextValue( "contact" );
        header[0][4] = hdr;
        
        return header;
    }

    /**
     * Initializes form fields with header and table values
     * @param header Header text values
     * @param table Table text values
     */
    @Override
    protected void init( String[][] header, String[][][] table ) 
    {
        if ( header.length > 0 )
        {
            fields.set( "iD", header[0][0] );
            fields.set( "name", header[0][1]  );
            fields.set( "address", header[0][2] );
            fields.set( "phone",  header[0][3] );
            fields.set( "contact", header[0][4] );
        }
    }

    public Location  getLocation ()
    {
    	return fields.get( "lctn" );
    }
    
    public static LinkedHashSet getItemsList()
    {
        return list;
    }
    
    public static LinkedHashSet[] createList()
    {
    	if ( list == null )
            createNewList();
        
        return new LinkedHashSet[] { list };
    }
    
    public static void createNewList()
    {
        list = new LinkedHashSet<>();
        
        createNewList( list, "LocationModel" );
    }
    
    public LocationModel()
    {
        super( "LocationModel" );
        list.add( this );
    }
    
    public LocationModel( Stage stage ) throws Exception
    {
        super( stage, "LocationModel" );
        
        if ( stage != null )
        	list.add( this );
    }

    public static List<Location> getFromDB()
    {
    	return getFromDB( "Location" );
    }
    
    @Override
	protected void saveToDB() 
	{
    	String	iD = fields.get( "iD" ),						// Location ID in the system
                name = fields.get( "name" ),					// Common name
                phone = fields.get( "phone" ),					// Phone number
                contact = fields.get( "contact" ),				// Contact in the company
                address = fields.get( "address" );				// Company address
    	
    	// Get Location object from the fields of current model
    	Location lctn = fields.get( "lctn" );
    	
    	// If Location object is not created yet
    	if ( lctn == null  )
        {
            // Create instance of Location Entity
    		lctn = new Location( iD, name, address, phone, contact );
            fields.set( "lctn", lctn );
        }
            
    	// Otherwise
    	else
            // UpdateLocation Entity information
    		lctn.update( iD, name, address, phone, contact );
    	
    	try
    	{
    		// Persist Location Entity data to database
    		Database.persistToDB( lctn );
    	}
    	catch ( Exception e ) 
    	{
    		System.out.println( "Error with persisting data" );
    	}
	}
    
    @Override
    public void removeFromDB() throws Exception
    { 
    	removeFromDB( "lctn" );
    	
    	list.remove( this );
    }
     
    public static LocationModel getInstance( Object l )
    {
    	return new LocationModel( (Location) l );
    }
    
    public LocationModel( Location l )
    {
    	super( "LocationModel" );
        
    	fields.set( "lctn", l );
    	fields.set( "iD", l.getId() );
        fields.set( "name", l.getName() );
    	fields.set( "address", l.getAddress() );
        fields.set( "phone", l.getPhone() );
        fields.set( "contact", l.getContact() );
    }
} // End of class ** LocationModel **