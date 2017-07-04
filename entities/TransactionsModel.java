package entities;

import java.util.Vector;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import foundation.Cipher;

/**
 * Class TransactionsModelData - stores Transactions' Models
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
	private Vector<TAccount> taccounts;
	
	// Transactions of Transactions Model
	@OneToMany( fetch=FetchType.EAGER, cascade=CascadeType.REMOVE )
	private Vector<Transaction> transactions;
	
	/**
	 * Class default constructor
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
	public TransactionsModel( String name, Vector<TAccount> taccounts, Vector<Transaction> transactions )
	{
		this.name = Cipher.crypt(name);
		this.taccounts = taccounts;
		this.transactions = transactions;
	}
	
	/**
	 * Class constructor with List of T-accounts and list of transactions
	 * @param taccounts
	 * @param transactions
	 */
	public TransactionsModel( Vector<TAccount> taccounts, Vector<Transaction> transactions )
	{
		this.taccounts = taccounts;
		this.transactions = transactions;
	}
	
	/**
	 * Sets name of Transactions Model
	 * @param name New name for Transactions Model
	 */
	public void setName( String name )
	{
		this.name = Cipher.crypt(name);
	}
	
	/**
	 * Returns list of Transactions Model T-accounts
	 */
	public Vector<TAccount> getTAccounts()
	{
		return taccounts;
	}
	
	/**
	 * Returns list of Transactions Model sransactions
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
		return Cipher.decrypt(name);
	}
}