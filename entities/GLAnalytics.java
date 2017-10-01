package entities;

import javax.persistence.*;

import foundation.Cipher;

/**
 * Entity implementation class for entity GLAnalytics
 * @author Peter Cross
 */
@Entity
public class GLAnalytics
{
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	private long glAnalyticsId;
	
	private int lineNum;		// Line number in screen form
	
	String analyticsControl;	// Analytics Control name
	String analyticsType;		// Analytics Control type
	
	/**
	 * Class mandatory constructor
	 */
	public GLAnalytics()
	{
		super();
	}
	
	/**
	 * Class constructor
	 * @param lineNum Line number in screen form
	 * @param analyticsControl Analytics Control name
	 * @param analyticsType Analytics Control type
	 */
	public GLAnalytics( int lineNum, String analyticsControl, String analyticsType )
	{
		this.lineNum = lineNum;
		this.analyticsControl = Cipher.crypt( analyticsControl );
		this.analyticsType = Cipher.crypt( analyticsType );
	}
	
	// Returns line number in screen form
	public int getLineNum()
	{
		return lineNum;
	}
	
	// Returns Analytics Control name
	public String getAnalyticsControl()
	{
		return Cipher.decrypt( analyticsControl );
	}
	
	// Returns Analytics Control type
	public String getAnalyticsType()
	{
		return Cipher.decrypt( analyticsType );
	}
}