package entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import foundation.Cipher;

@Entity
public class Location 
{
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	private long 	locationId;
	
	private String	iD;				// Location ID in the system
	private String	name;			// Common name
	private String	address;		// Address
	private String	phone;			// Phone number
	private String	contact;		// Contact name at location
	
	public Location()
	{
		super();
	}
	
	public Location( String... args )
	{
		update( args );
	}
	
	public void update( String... args )
	{
		// Save provided info and crypt sensitive info
		iD = args[0];
		name = Cipher.crypt( args[1] );
		address = Cipher.crypt( args[2] );
		phone = Cipher.crypt( args[3] );
		contact = Cipher.crypt( args[4] );
	}
	
	// Returns ID of Location
	public String getId()
	{
		return iD;
	}
	
	// Returns name of Location
	public String getName()
	{
		return Cipher.decrypt( name );
	}
		
	// Returns phone of Location
	public String getPhone()
	{
		return Cipher.decrypt( phone );
	}
	
	// Returns contact person info of Location
	public String getContact()
	{
		return Cipher.decrypt( contact );
	}
	
	// Returns address of Location
	public String getAddress()
	{
		return Cipher.decrypt( address );
	}			
}