package entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import java.util.Vector;

import foundation.Cipher;

/**
 * Class TransactionsModel - entity implementation for Transactions Simulation Model
 * @author Peter Cross
 *
 */
@Entity
public class TransactionsModel 
{
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	private long transactionsModelId;
	
	// Name of Transactions Model
	private String name;
	
	// T-accounts included in Transactions Model
	@OneToMany( fetch=FetchType.EAGER, cascade=CascadeType.REMOVE )
	private Vector<TAcct> taccts;
	
	// Transactions of Transactions Model
	@OneToMany( fetch=FetchType.EAGER, cascade=CascadeType.REMOVE )
	private Vector<Transaction> transactions;
	
	// Legal Entity to which Transactions Model belongs
	@ManyToOne( fetch=FetchType.EAGER )
	private LegalEntity lglEntity;
	
	/**
	 * Class mandatory constructor
	 */
	public TransactionsModel()
	{
		super();
	}
	
	/**
	 * Class constructor with all info of Transactions Model
	 * @param name Name of Transactions Model
	 * @param taccounts List of T-accounts
	 * @param transactions List of transactions
	 */
	public TransactionsModel( String name, Vector<TAcct> taccounts, Vector<Transaction> transactions )
	{
		this.name = Cipher.crypt( name );
		this.taccts = taccounts;
		this.transactions = transactions;
	}
	
	/**
	 * Class constructor with all info for Transactions Model and Legal Entity it belongs to
	 * @param name Name of Transactions Model
	 * @param taccounts List of T-accounts
	 * @param transactions List of transactions
	 * @param lglEntity Legal Entity to which transactions model belongs
	 */
	public TransactionsModel( String name, Vector<TAcct> taccounts, Vector<Transaction> transactions, LegalEntity lglEntity )
	{
		this( name, taccounts, transactions );
		this.lglEntity = lglEntity;
	}
	
	/**
	 * Class constructor with List of T-accounts and list of transactions
	 * @param taccounts List of T-accounts
	 * @param transactions List of transactions
	 */
	public TransactionsModel( Vector<TAcct> taccounts, Vector<Transaction> transactions )
	{
		this.taccts = taccounts;
		this.transactions = transactions;
	}
	
	/**
	 * Class constructor with List of T-accounts and list of transactions and Legal Entity Transaction Model belongs to
	 * @param taccounts List of T-accounts
	 * @param transactions List of transactions
	 * @param lglEntity Legal Entity to which transactions model belongs
	 */
	public TransactionsModel( Vector<TAcct> taccounts, Vector<Transaction> transactions, LegalEntity lglEntity )
	{
		this( taccounts, transactions );
		this.lglEntity = lglEntity;
	}
	
	/**
	 * Sets name of Transactions Model
	 * @param name New name for Transactions Model
	 */
	public void setName( String name )
	{
		this.name = Cipher.crypt( name );
	}
	
	/**
	 * Returns list of Transactions Model T-accs
	 */
	public Vector<TAcct> getTAccs()
	{
		return taccts;
	}
	
	/**
	 * Returns list of Transactions Model transactions
	 */
	public Vector<Transaction> getTransactions()
	{
		return transactions;
	}
	
	/**
	 * Returns name of Transactions Model
	 */
	public String getName()
	{
		return Cipher.decrypt( name );
	}
	
	/**
	 * Returns Legal Entity of Transactions Model
	 */
	public LegalEntity getLegalEntity()
	{
		return lglEntity;
	}
	
	/**
	 * Sets Legal Entity of Transactions Model
	 * @param legalEntity Legal Entity current Transactions Model belongs to
	 */
	public void setLegalEntity( LegalEntity legalEntity )
	{
		lglEntity = legalEntity;
	}
}