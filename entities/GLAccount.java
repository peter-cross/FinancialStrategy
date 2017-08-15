package entities;

import javax.persistence.*;

import java.util.List;
import java.util.Vector;

import application.Database;
import foundation.Cipher;

/**
 * Entity implementation class for GLAccount Model entity
 * @author Peter Cross
 */
@Entity
public class GLAccount 
{
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	private long 	glAccountId;
	
	private String	glNumber;			// G/L Account Number
	private String	name;				// G/L Account Name
	private String	type;				// Type ofG/L Account
	private String	accountGroup;		// Balance Sheet or Income Statement account group to which G/L acct.belongs
	
	private int		quantity;			// Quantity flag - indicates if quantitative accounting is necessary
	private int		foreignCurrency;	// Foreign Currency Flag - indicates if posting amounts in foreign currency is necessary
	private int		contraAccount;		// Contra Account flag
	
	@ManyToOne( fetch=FetchType.EAGER )
	private ChartOfAccounts chartOfAccounts;
	
	@OneToMany( fetch=FetchType.EAGER, cascade=CascadeType.PERSIST )
	private Vector<GLAccountAnalytics> analytics;
	
	/**
	 * Mandatory class constructor
	 */
	public GLAccount() 
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
	 * @param foreignCurrency Foreign Currency flag
	 * @param contraAccount Contra-account flag
	 * @param analyticsControl List of Analytics Controls
	 * @param analyticsType List of types for Analytics Controls
	 * @param chartOfAccounts Chart of Accounts to which G/L account belongs
	 */
	public GLAccount( String glNumber, String name, String type, String accountGroup, int quantity, int foreignCurrency, int contraAccount, List<String> analyticsControl, List<String> analyticsType, ChartOfAccounts chartOfAccounts )
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
	 * @param foreignCurrency Foreign Currency flag
	 * @param contraAccount Contra-account flag
	 * @param analyticsControl List of Analytics Controls
	 * @param analyticsType List of types for Analytics Controls
	 * @param chartOfAccounts Chart of Accounts to which G/L account belongs
	 */
	public void update( String glNumber, String name, String type, String accountGroup, int quantity, int foreignCurrency, int contraAccount, List<String> analyticsControl, List<String> analyticsType, ChartOfAccounts chartOfAccounts )
	{
		this.chartOfAccounts = chartOfAccounts;
		
		this.glNumber = Cipher.crypt( glNumber );
		this.name = Cipher.crypt( name );
		this.type = Cipher.crypt( type );
		this.accountGroup = Cipher.crypt( accountGroup );
		
		this.quantity = quantity;
		this.foreignCurrency = foreignCurrency;
		this.contraAccount = contraAccount;
		
		if ( analytics != null && analytics.size() > 0 )
			Database.removeFromDB( analytics );
		
		analytics = new Vector<GLAccountAnalytics>();
		GLAccountAnalytics analyticsLine;
		
		for ( int i = 0; i < analyticsControl.size(); i++ )
		{
			analyticsLine = new GLAccountAnalytics( i, analyticsControl.get(i), analyticsType.get(i) ); 
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
		return Cipher.decrypt( accountGroup );
	}
	
	// Returns G/L account quantity flag
	public int getQuantity()
	{
		return quantity;
	}
	
	// Returns G/L account foreign currency flag
	public int getForeignCurrency()
	{
		return foreignCurrency;
	}
	
	// Returns G/L account contra-account flag
	public int getContraAccount()
	{
		return contraAccount;
	}
	
	// Returns Array of Analytics Controls for G/L account
	public String[] getAnalyticsControl()
	{
		String[] list = new String[ analytics.size() ];
		
		for ( int i = 0; i < list.length; i++ )
		{
			GLAccountAnalytics glAcctAnalytics = analytics.get(i);
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
			GLAccountAnalytics glAcctAnalytics = analytics.get(i);
			list[glAcctAnalytics.getLineNum()] = glAcctAnalytics.getAnalyticsType();
		}
			
		return list;
	}
		
	// Returns Chart Of Accounts to which G/L account belongs
	public ChartOfAccounts getChartOfAccounts()
	{
		return chartOfAccounts;
	}
}