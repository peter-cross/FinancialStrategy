package entities;

import javax.persistence.*;

import java.util.List;
import java.util.Vector;

import application.Database;
import foundation.Cipher;

/**
 * Entity implementation class for GL Model entity
 * @author Peter Cross
 */
@Entity
public class GL 
{
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	private long 	glId;
	
	private String	glNumber;			// G/L Account Number
	private String	name;				// G/L Account Name
	private String	type;				// Type ofG/L Account
	private String	acctGrp;		// Balance Sheet or Income Statement account group to which G/L acct.belongs
	
	private int		quantity;			// Quantity flag - indicates if quantitative accounting is necessary
	private int		frgnCrcy;	// Foreign Crcy Flag - indicates if posting amounts in foreign currency is necessary
	private int		contraAcct;		// Contra Account flag
	
	@ManyToOne( fetch=FetchType.EAGER )
	private COA coa;
	
	@OneToMany( fetch=FetchType.EAGER, cascade=CascadeType.PERSIST )
	private Vector<GLAnalytics> analytics;
	
	/**
	 * Mandatory class constructor
	 */
	public GL() 
	{
		super();
	}
   
	/**
	 * Class constructor
	 * @param glNumber Number of G/L account
	 * @param name G/L account name
	 * @param type G/L account type
	 * @param accountGroup Account group to which belongs G/L account 
	 * @param quantity Quantity flag
	 * @param foreignCurrency Foreign Crcy flag
	 * @param contraAccount Contra-account flag
	 * @param analyticsControl List of Analytics Controls
	 * @param analyticsType List of types for Analytics Controls
	 * @param chartOfAccounts Chart of Accounts to which G/L account belongs
	 */
	public GL( String glNumber, String name, String type, String accountGroup, int quantity, int foreignCurrency, int contraAccount, List<String> analyticsControl, List<String> analyticsType, COA chartOfAccounts )
	{
		update( glNumber, name, type, accountGroup, quantity, foreignCurrency, contraAccount, analyticsControl, analyticsType, chartOfAccounts );
	}
	
	/**
	 * Updates G/L account object attributes
	 * @param glNumber Number of G/L account
	 * @param name G/L account name
	 * @param type G/L account type
	 * @param accountGroup Account group to which belongs G/L account 
	 * @param quantity Quantity flag
	 * @param foreignCurrency Foreign Crcy flag
	 * @param contraAccount Contra-account flag
	 * @param analyticsControl List of Analytics Controls
	 * @param analyticsType List of types for Analytics Controls
	 * @param chartOfAccounts Chart of Accounts to which G/L account belongs
	 */
	public void update( String glNumber, String name, String type, String accountGroup, int quantity, int foreignCurrency, int contraAccount, List<String> analyticsControl, List<String> analyticsType, COA coa )
	{
		this.coa = coa;
		
		this.glNumber = Cipher.crypt( glNumber );
		this.name = Cipher.crypt( name );
		this.type = Cipher.crypt( type );
		this.acctGrp = Cipher.crypt( accountGroup );
		
		this.quantity = quantity;
		this.frgnCrcy = foreignCurrency;
		this.contraAcct = contraAccount;
		
		if ( analytics != null && analytics.size() > 0 )
			Database.removeFromDB( analytics );
		
		analytics = new Vector<GLAnalytics>();
		GLAnalytics analyticsLine;
		
		for ( int i = 0; i < analyticsControl.size(); i++ )
		{
			analyticsLine = new GLAnalytics( i, analyticsControl.get(i), analyticsType.get(i) ); 
			analytics.add( analyticsLine );
		}
	}
	
	// Returns G/L number for G/L account
	public String getGlNumber()
	{
		return Cipher.decrypt( glNumber );
	}
	
	// Returns G/L account names
	public String getName()
	{
		return Cipher.decrypt( name );
	}
	
	// Returns G/L account type
	public String getType()
	{
		return Cipher.decrypt( type );
	}
	
	// Returns Group name to which G/L account belongs
	public String getAccountGroup()
	{
		return Cipher.decrypt( acctGrp );
	}
	
	// Returns G/L account quantity flag
	public int getQuantity()
	{
		return quantity;
	}
	
	// Returns G/L account foreign currency flag
	public int getForeignCurrency()
	{
		return frgnCrcy;
	}
	
	// Returns G/L account contra-account flag
	public int getContraAccount()
	{
		return contraAcct;
	}
	
	// Returns Array of Analytics Controls for G/L account
	public String[] getAnalyticsControl()
	{
		String[] list = new String[ analytics.size() ];
		
		for ( int i = 0; i < list.length; i++ )
		{
			GLAnalytics glAcctAnalytics = analytics.get(i);
			list[glAcctAnalytics.getLineNum()] = glAcctAnalytics.getAnalyticsControl();
		}
			
		return list;
	}
	
	// Returns Array of types for G/L account Analytics Controls
	public String[] getAnalyticsType()
	{
		String[] list = new String[ analytics.size() ];
		
		for ( int i = 0; i < list.length; i++ )
		{
			GLAnalytics glAcctAnalytics = analytics.get(i);
			list[glAcctAnalytics.getLineNum()] = glAcctAnalytics.getAnalyticsType();
		}
			
		return list;
	}
		
	// Returns ChOfAccs to which G/L account belongs
	public COA getChOfAccs()
	{
		return coa;
	}
}