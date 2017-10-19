package entities;

import javax.persistence.*;

import foundation.Cipher;

/**
 * Entity implementation class for entity LglEntityCharts
 * @author Peter Cross
 */
@Entity
public class LglEntityCharts 
{
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	private long lglEntityChartsId;
	
	private int lineNum;		// Line number in screen form
	
	private String chartName;	// ChOfAccs's name specific to Lgl entity
	
	@ManyToOne( fetch=FetchType.EAGER )
	private ChOfAccs coa; // ChOfAccs 
	
	/**
	 * Class mandatory constructor
	 */
	public LglEntityCharts()
	{
		super();
	}
	
	/**
	 * Class constructor
	 * @param lineNum Line number in screen form
	 * @param chartName ChOfAccs' name specific to Legal entity
	 * @param chOfAccs ChOfAccs
	 */
	public LglEntityCharts( int lineNum, String chartName, ChOfAccs chOfAccs )
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
	
	// Returns ChOfAcc's name specific to Lgl entity
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