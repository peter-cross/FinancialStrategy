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
 * Class TractnsModel - entity implementation for Transactions Simulation Model
 * @author Peter Cross
 *
 */
@Entity
public class TractnsModel 
{
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	private long tractnsModelId;
	
	// Name of Transactions Model
	private String name;
	
	// T-accts included in Transactions Model
	@OneToMany( fetch=FetchType.EAGER, cascade=CascadeType.REMOVE )
	private Vector<TAcct> taccts;
	
	// Transactions of Transactions Model
	@OneToMany( fetch=FetchType.EAGER, cascade=CascadeType.REMOVE )
	private Vector<TrActn> tractns;
	
	// Legal Entity to which Transactions Model belongs
	@ManyToOne( fetch=FetchType.EAGER )
	private LglEntity lglEntity;
	
	/**
	 * Class mandatory constructor
	 */
	public TractnsModel()
	{
		super();
	}
	
	/**
	 * Class constructor with all info of Transactions Model
	 * @param name Name of Transactions Model
	 * @param taccts List of T-accts
	 * @param transactions List of tractns
	 */
	public TractnsModel( String name, Vector<TAcct> taccts, Vector<TrActn> tractns )
	{
		this.name = Cipher.crypt( name );
		this.taccts = taccts;
		this.tractns = tractns;
	}
	
	/**
	 * Class constructor with all info for Transactions Model and Legal Entity it belongs to
	 * @param name Name of Transactions Model
	 * @param taccounts List of T-accts
	 * @param transactions List of transactions
	 * @param lglEntity Legal Entity to which transactions model belongs
	 */
	public TractnsModel( String name, Vector<TAcct> taccts, Vector<TrActn> transactions, LglEntity lglEntity )
	{
		this( name, taccts, transactions );
		this.lglEntity = lglEntity;
	}
	
	/**
	 * Class constructor with List of T-accts and list of transactions
	 * @param taccounts List of T-accts
	 * @param transactions List of transactions
	 */
	public TractnsModel( Vector<TAcct> taccts, Vector<TrActn> tractns )
	{
		this.taccts = taccts;
		this.tractns = tractns;
	}
	
	/**
	 * Class constructor with List of T-accts and list of transactions and Legal Entity TrActn Model belongs to
	 * @param taccounts List of T-accts
	 * @param transactions List of transactions
	 * @param lglEntity Legal Entity to which transactions model belongs
	 */
	public TractnsModel( Vector<TAcct> taccts, Vector<TrActn> transactions, LglEntity lglEntity )
	{
		this( taccts, transactions );
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
	 * Returns list of Transactions Model T-accts
	 */
	public Vector<TAcct> getTAccs()
	{
		return taccts;
	}
	
	/**
	 * Returns list of Transactions Model transactions
	 */
	public Vector<TrActn> getTransactions()
	{
		return tractns;
	}
	
	/**
	 * Returns name of Transactions Model
	 */
	public String getName()
	{
		return Cipher.decrypt( name );
	}
	
	/**
	 * Returns Lgl Entity of Transactions Model
	 */
	public LglEntity getLglEntity()
	{
		return lglEntity;
	}
	
	/**
	 * Sets Lgl Entity of Transactions Model
	 * @param lglEntity Legal Entity current Transactions Model belongs to
	 */
	public void setLegalEntity( LglEntity lglEntity )
	{
		this.lglEntity = lglEntity;
	}
}