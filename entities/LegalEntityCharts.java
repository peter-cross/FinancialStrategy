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
	private COA coa; // ChOfAccs 
	
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
	 * @param chartName ChOfAccs' name specific to Legal entity
	 * @param chOfAccs ChOfAccs
	 */
	public LegalEntityCharts( int lineNum, String chartName, COA chOfAccs )
	{
		this.lineNum = lineNum;
		this.chartName = Cipher.crypt( chartName );
		this.coa = chOfAccs;
	}
	
	// Returns Line number in screen form
	public int getLineNum()
	{
		return lineNum;
	}
	
	// Returns ChOfAcc's name specific to Legal entity
	public String getChartName()
	{
		return Cipher.decrypt( chartName );
	}
	
	// Returns ChOfAccs' name
	public String getChOfAccs()
	{
		return coa.getName();
	}
}