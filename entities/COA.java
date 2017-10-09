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
	
	private String	code;		// ChOfAccs' code 
	private String	name;		// ChOfAccs' name
	
	@ManyToOne( fetch=FetchType.EAGER )
	private Crcy crcy;	// Crcy of ChOfAccs
	
	/**
	 * Class mandatory constructor
	 */
	public COA() 
	{
		super();
	}
   
	/**
	 * Class constructor
	 * @param code ChOfAccs code
	 * @param name ChOfAccs name
	 * @param currency Crcy for ChOfAccs
	 */
	public COA( String	code, String name, Crcy crcy ) 
	{
		update( code, name, crcy );
	}
	
	/**
	 * Updates attributes of class object
	 * @param code ChOfAccs' code
	 * @param name ChOfAccs' name
	 * @param currency Crcy for ChOfAccs
	 */
	public void update( String code, String name, Crcy crcy ) 
	{
		this.code = Cipher.crypt( code );
		this.name = Cipher.crypt( name );
		this.crcy = crcy;
	}
	
	// Returns ChOfAccs' code
	public String getCode()
	{
		return Cipher.decrypt( code );
	}
	
	// Returns ChOfAccs' name
	public String getName()
	{
		return Cipher.decrypt( name );
	}		
	
	// Returns ChOfAccs' crcy
	public Crcy getCrcy()
	{
		return crcy;
	}		
}