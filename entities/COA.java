package entities;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import foundation.Cipher;

/**
 * Entity implementation class for entity COA
 *
 */
@Entity
public class COA 
{
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	private long coaId;
	
	private String	code;		// Chart Of Accounts code 
	private String	name;		// Chart Of Accounts' name
	
	@ManyToOne( fetch=FetchType.EAGER )
	private Crcy crcy;	// Crcy of Chart Of Accounts
	
	/**
	 * Class mandatory constructor
	 */
	public COA() 
	{
		super();
	}
   
	/**
	 * Class constructor
	 * @param code Chart Of Accounts code
	 * @param name Chart Of Accounts name
	 * @param currency Crcy for Chart Of Accounts
	 */
	public COA( String	code, String name, Crcy currency ) 
	{
		update( code, name, currency );
	}
	
	/**
	 * Updates attributes of class object
	 * @param code Chart Of Accounts code
	 * @param name Chart Of Accounts name
	 * @param currency Crcy for Chart Of Accounts
	 */
	public void update( String code, String name, Crcy currency ) 
	{
		this.code = Cipher.crypt( code );
		this.name = Cipher.crypt( name );
		this.crcy = currency;
	}
	
	// Returns Chart Of Accounts' code
	public String getCode()
	{
		return Cipher.decrypt( code );
	}
	
	// Returns Chart Of Accounts' name
	public String getName()
	{
		return Cipher.decrypt( name );
	}		
	
	// Returns Chart Of Accounts' currency
	public Crcy getCurrency()
	{
		return crcy;
	}		
}