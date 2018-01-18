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
	private ChOfAccs chOfAccs; // ChOfAccs 
	
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
	public LglEntityCharts( Object... args )
	{
		this.lineNum = (Integer) args[0];
		this.chartName = Cipher.crypt( (String) args[1] );
		this.chOfAccs = (ChOfAccs) args[2];
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
		return chOfAccs.getName();
	}
}