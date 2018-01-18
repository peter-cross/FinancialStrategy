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
	
	private String	glNumber;		// G/L Acct Number
	private String	name;			// G/L Acct Name
	private String	type;			// Type of G/L Acct
	private String	acctGrp;		// BalSh or IncSt acct grp to which G/L acct.belongs
	
	private int		quantity;		// Quantity flag - indicates if quantitative accnting is necessary
	private int		frgnCrcy;		// Foreign Crcy Flag - indicates if posting amounts in frgn crcy is necessary
	private int		contraAcct;		// Contra Acct flag
	
	@ManyToOne( fetch=FetchType.EAGER )
	private ChOfAccs chOfAccs;
	
	@OneToMany( fetch=FetchType.EAGER, cascade=CascadeType.ALL )
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
	 * @param glNumber Number of G/L acct
	 * @param name G/L acct name
	 * @param type G/L acct type
	 * @param acctGrp Acct group to which belongs G/L acct 
	 * @param quantity Quantity flag
	 * @param frgnCrcy Frgn Crcy flag
	 * @param contraAcct Contra-acct flag
	 * @param analyticsControl List of Analytics Controls
	 * @param analyticsType List of types for Analytics Controls
	 * @param chOfAccs ChOfAccs to which G/L acct belongs
	 */
	public GL( Object... args )
	{
		update( args );
	}
	
	/**
	 * Updates G/L acct object attributes
	 * @param glNumber Number of G/L acct
	 * @param name G/L acct name
	 * @param type G/L acct type
	 * @param acctGrp Acct grp to which belongs G/L acct 
	 * @param quantity Quantity flag
	 * @param frgnCrcy Frgn Crcy flag
	 * @param contraAcct Contra-acct flag
	 * @param analyticsControl List of Analytics Controls
	 * @param analyticsType List of types for Analytics Controls
	 * @param chOfAccs ChOfAccs to which G/L acct belongs
	 */
	public void update( Object... args )
	{
		this.glNumber = Cipher.crypt( (String) args[0] );
		this.name = Cipher.crypt( (String) args[1] );
		this.type = Cipher.crypt( (String) args[2] );
		this.acctGrp = Cipher.crypt( (String) args[3] );
		
		this.quantity = (Integer) args[4];
		this.frgnCrcy = (Integer) args[5];
		this.contraAcct = (Integer) args[6];
		
		List<String> analyticsControl = (List<String>) args[7];
		List<String> analyticsType = (List<String>) args[8];
		chOfAccs = (ChOfAccs) args[9];
		
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
	
	// Returns G/L number for G/L acct
	public String getGlNumber()
	{
		return Cipher.decrypt( glNumber );
	}
	
	// Returns G/L acct names
	public String getName()
	{
		return Cipher.decrypt( name );
	}
	
	// Returns G/L acct type
	public String getType()
	{
		return Cipher.decrypt( type );
	}
	
	// Returns Grp name to which G/L acct belongs
	public String getAcctGrp()
	{
		return Cipher.decrypt( acctGrp );
	}
	
	// Returns G/L acct quantity flag
	public int getQuantity()
	{
		return quantity;
	}
	
	// Returns G/L acct foreign crcy flag
	public int getFrgnCrcy()
	{
		return frgnCrcy;
	}
	
	// Returns G/L acct contra-acct flag
	public int getContraAcct()
	{
		return contraAcct;
	}
	
	// Returns Array of Analytics Controls for G/L acct
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
	
	// Returns Array of types for G/L acct Analytics Controls
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
		
	// Returns ChOfAccs to which G/L acct belongs
	public ChOfAccs getChOfAccs()
	{
		return chOfAccs;
	}
}