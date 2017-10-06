package entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import foundation.Cipher;

/**
 * Class Crcy - Entity implementation for Currency Model objects
 * @author Peter Cross
 *
 */
@Entity
public class Crcy
{
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	private long 	crcyId;
	
	private String	code;	// Crcy code in the system
	private String	name;	// Crcy common name
	
	/**
	 * Class mandatory constructor
	 */
	public Crcy() 
	{
		super();
	}
	
	/**
	 * Class constructor
	 * @param code Crcy code
	 * @param name Crcy name
	 */
	public Crcy( String	code, String name ) 
	{
		update( code, name );
	}
	
	/**
	 * Updates Crcy object attributes
	 * @param code Crcy code
	 * @param name Crcy name
	 */
	public void update( String code, String name ) 
	{
		this.code = Cipher.crypt( code );
		this.name = Cipher.crypt( name );
	}
	
	// Returns Crcy code
	public String getCode()
	{
		return Cipher.decrypt( code );
	}
	
	// Returns Crcy name
	public String getName()
	{
		return Cipher.decrypt( name );
	}
    
	// Returns String representation of Crcy
    public String toString()
    {
        return getCode();
    }
}