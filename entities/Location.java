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
	
	public Location( String iD, String name, String address, String phone, String contact )
	{
		update( iD, name, address, phone, contact );
	}
	
	public void update( String iD, String name, String address, String phone, String contact )
	{
		// Save provided info and crypt sensitive info
		this.iD = iD;
		this.name = Cipher.crypt( name );
		this.address = Cipher.crypt( address );
		this.phone = Cipher.crypt( phone );
		this.contact = Cipher.crypt( contact );
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