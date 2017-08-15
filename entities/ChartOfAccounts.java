package entities;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import foundation.Cipher;

/**
 * Entity implementation class for entity ChartOfAccounts
 *
 */
@Entity
public class ChartOfAccounts 
{
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	private long chartOfAccountsId;
	
	private String	code;		// Chart Of Accounts code 
	private String	name;		// Chart Of Accounts' name
	
	@ManyToOne( fetch=FetchType.EAGER )
	private Currency currency;	// Currency of Chart Of Accounts
	
	/**
	 * Class mandatory constructor
	 */
	public ChartOfAccounts() 
	{
		super();
	}
   
	/**
	 * Class constructor
	 * @param code Chart Of Accounts code
	 * @param name Chart Of Accounts name
	 * @param currency Currency for Chart Of Accounts
	 */
	public ChartOfAccounts( String	code, String name, Currency currency ) 
	{
		update( code, name, currency );
	}
	
	/**
	 * Updates attributes of class object
	 * @param code Chart Of Accounts code
	 * @param name Chart Of Accounts name
	 * @param currency Currency for Chart Of Accounts
	 */
	public void update( String code, String name, Currency currency ) 
	{
		this.code = Cipher.crypt( code );
		this.name = Cipher.crypt( name );
		this.currency = currency;
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
	public Currency getCurrency()
	{
		return currency;
	}		
}