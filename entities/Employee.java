package entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import foundation.Cipher;

@Entity
public class Employee 
{
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	private long 	employeeId;
	
	private String	iD;				// Employee ID in the system
	private String	name;			// Common name
	private String	phone;			// Phone number
	private String	address;		// Address

	public Employee()
	{
		super();
	}
	
	public Employee( String... args )
	{
		update( args );
	}
	
	
	public void update( String... args )
	{
		iD = Cipher.crypt( args[0] );
		name = Cipher.crypt( args[1] );
		phone = Cipher.crypt( args[2] );
		address = Cipher.crypt( args[3] );
	}
	
	// Returns ID of Employee
	public String getId()
	{
		return Cipher.decrypt( iD );
	}
	
	// Returns name of Lgl Entity
	public String getName()
	{
		return Cipher.decrypt( name );
	}
		
	public String getPhone()
	{
		return Cipher.decrypt( phone );
	}
	
	// Returns address of Employee
	public String getAddress()
	{
		return Cipher.decrypt( address );
	}		
}