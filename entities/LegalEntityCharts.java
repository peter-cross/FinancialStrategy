package entities;

import javax.persistence.*;

import foundation.Cipher;

/**
 * Entity implementation class for entity LegalEntityCharts
 * @author Peter Cross
 */
@Entity
public class LegalEntityCharts 
{
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	private long legalEntityChartsId;
	
	private int lineNum;		// Line number in screen form
	
	private String chartName;	// Chart Of Account's name specific to Legal entity
	
	@ManyToOne( fetch=FetchType.EAGER )
	private ChartOfAccounts chartOfAccounts; // Chart Of Accounts 
	
	/**
	 * Class mandatory constructor
	 */
	public LegalEntityCharts()
	{
		super();
	}
	
	/**
	 * Class constructor
	 * @param lineNum Line number in screen form
	 * @param chartName Chart Of Account's name specific to Legal entity
	 * @param chartOfAccounts Chart Of Accounts
	 */
	public LegalEntityCharts( int lineNum, String chartName, ChartOfAccounts chartOfAccounts )
	{
		this.lineNum = lineNum;
		this.chartName = Cipher.crypt( chartName );
		this.chartOfAccounts = chartOfAccounts;
	}
	
	// Returns Line number in screen form
	public int getLineNum()
	{
		return lineNum;
	}
	
	// Returns Chart Of Account's name specific to Legal entity
	public String getChartName()
	{
		return Cipher.decrypt( chartName );
	}
	
	// Returns Chart Of Accounts' name
	public String getChartOfAccounts()
	{
		return chartOfAccounts.getName();
	}
}