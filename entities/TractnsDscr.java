package entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import foundation.Cipher;

/**
 * Class TractnsDscr - database entity for Tractns description
 * @author Peter Cross
 *
 */
@Entity
public class TractnsDscr 
{
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	private int tractnsDscrId;	// Tractn description ID for DB
	
	private String code;		// Tractn description code
	private String description;	// Tractn description
	
	/**
	 * Mandatory constructor
	 */
	public TractnsDscr()
	{
		super();
	}
	
	/**
	 * Primary class constructor
	 * @param code Code of Tractn Description
	 * @param description The description of tractn
	 */
	public TractnsDscr( String... args )
	{
		update( args );
	}	
	
	/**
	 * Updates class object attributes
	 * @param code Code of Tractn Description
	 * @param description The description of tractn
	 */
	public void update( String... args )
	{
		code = Cipher.crypt( args[0] );
		description = Cipher.crypt( args[1] );
	}
	
	/**
	 * Returns code of Tractn description
	 */
	public String getCode()
	{
		return Cipher.decrypt( code );
	}
	
	/**
	 * Returns description of tractn description object
	 */
	public String getDescription()
	{
		return Cipher.decrypt( description );
	}
	
	/**
	 * Returns string representation of tractn description object
	 */
	public String toString()
	{
		return getDescription();
	}	
}