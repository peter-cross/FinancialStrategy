package entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import foundation.Cipher;

/**
 * Class TractnsDscr - database entity for Transactions description
 * @author Peter Cross
 *
 */
@Entity
public class TractnsDscr 
{
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	private int tractnsDscrId;	// Transaction description ID for DB
	
	private String code;		// Transaction description code
	private String description;	// Transaction description
	
	/**
	 * Mandatory constructor
	 */
	public TractnsDscr()
	{
		super();
	}
	
	/**
	 * Primary class constructor
	 * @param code Code of Transaction Description
	 * @param description The description of transaction
	 */
	public TractnsDscr( String code, String description )
	{
		update( code, description );
	}	
	
	/**
	 * Updates class object attributes
	 * @param code Code of Transaction Description
	 * @param description The description of transaction
	 */
	public void update( String code, String description )
	{
		this.code = Cipher.crypt( code );
		this.description = Cipher.crypt( description );
	}
	
	/**
	 * Returns code of Transaction description
	 */
	public String getCode()
	{
		return Cipher.decrypt( code );
	}
	
	/**
	 * Returns description of transaction description object
	 */
	public String getDescription()
	{
		return Cipher.decrypt( description );
	}
	
	/**
	 * Returns string representation of transaction description object
	 */
	public String toString()
	{
		return getDescription();
	}	
}