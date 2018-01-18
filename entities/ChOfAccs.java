package entities;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import foundation.Cipher;

/**
 * Entity implementation class for entity ChOfAccs
 *
 */
@Entity
public class ChOfAccs 
{
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	private long chOfAccsId;
	
	private String	code;		// ChOfAccs' code 
	private String	name;		// ChOfAccs' name
	
	@ManyToOne( fetch=FetchType.EAGER )
	private Crcy crcy;	// Crcy of ChOfAccs
	
	/**
	 * Class mandatory constructor
	 */
	public ChOfAccs() 
	{
		super();
	}
   
	/**
	 * Class constructor
	 * @param code ChOfAccs code
	 * @param name ChOfAccs name
	 * @param currency Crcy for ChOfAccs
	 */
	public ChOfAccs( Object... args ) 
	{
		update( args );
	}
	
	/**
	 * Updates attributes of class object
	 * @param code ChOfAccs' code
	 * @param name ChOfAccs' name
	 * @param currency Crcy for ChOfAccs
	 */
	public void update( Object... args ) 
	{
		code = Cipher.crypt( (String) args[0] );
		name = Cipher.crypt( (String) args[1] );
		crcy = (Crcy) args[2];
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