package entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import application.Database;
import foundation.Cipher;

/**
 * Entity implementation class for : LglEntityModel database data
 * @author Peter Cross
 * 
 */
@Entity
public class LglEntity 
{
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	private long 	lglEntityId;
	
	private String	iD;				// Lgl Entity ID in the system
	private String	name;			// Common name
	private String	lglName;		// Official legal name
	private String	phone;			// Phone number
	private String	contact;		// Contact in the company
	private String	address;		// Address
	
	// ChOfAccs for Lgl Entity
	@OneToMany( fetch=FetchType.EAGER, cascade=CascadeType.PERSIST )
	private Vector<LglEntityCharts> lglEntityCharts;
	
	/**
	 * Mandatory class constructor
	 */
	public LglEntity() 
	{
		super();
	}
	
	/**
	 * Class constructor with all required info for creating Lgl Entity object
	 * @param iD ID for user
	 * @param name Common name
	 * @param lglName Lgl name of lgl entity
	 * @param phone Phone number
	 * @param contact Contact person
	 * @param address Address of Lgl Entity
	 */
	public LglEntity( String iD, String name, String lglName, String phone, String contact, String address, List<String> chartNames, ArrayList<COA> chOfAccs ) 
	{
		update( iD, name, lglName, phone, contact, address, chartNames, chOfAccs );
	}
	
	/**
	 * Updates information of existing lgl entity
	 * @param iD ID for user
	 * @param name Common name
	 * @param lglName Lgl name of lgl entity
	 * @param phone Phone number
	 * @param contact Contact person
	 * @param address Address of Lgl Entity
	 */
	public void update( String iD, String name, String lglName, String phone, String contact, String address, List<String> chartNames, ArrayList<COA> chOfAccs )
	{
		// Save provided info and crypt sensitive info
		this.iD = iD;
		this.name = Cipher.crypt( name );
		this.lglName = Cipher.crypt( lglName );
		this.phone = Cipher.crypt( phone );
		this.contact = Cipher.crypt( contact );
		this.address = Cipher.crypt( address );
		
		if ( lglEntityCharts != null && lglEntityCharts.size() > 0 )
			Database.removeFromDB( lglEntityCharts );
		
		lglEntityCharts = new Vector<LglEntityCharts>();
		LglEntityCharts lglEntityChartsLine;
		
		for ( int i = 0; i < chOfAccs.size(); i++ )
		{
			lglEntityChartsLine = new LglEntityCharts( i, chartNames.get(i), chOfAccs.get(i) );
			lglEntityCharts.add( lglEntityChartsLine );
		}
	}
	
	// Returns ID of Lgl Entity
	public String getId()
	{
		return iD;
	}
	
	// Returns name of Lgl Entity
	public String getName()
	{
		return Cipher.decrypt( name );
	}
	
	// Returns legal name of Lgl Entity
	public String getLglName()
	{
		return Cipher.decrypt( lglName );
	}
	
	// Returns phone of Lgl Entity
	public String getPhone()
	{
		return Cipher.decrypt( phone );
	}
	
	// Returns contact person info of Lgl Entity
	public String getContact()
	{
		return Cipher.decrypt( contact );
	}
	
	// Returns address of Lgl Entity
	public String getAddress()
	{
		return Cipher.decrypt( address );
	}
	
	// Returns array of ChOfAccs names specific for Lgl entity 
	public String[] getChartNames()
	{
		String[] list = new String[ lglEntityCharts.size() ];
		
		for ( int i = 0; i < list.length; i++ )
		{
			LglEntityCharts chart = lglEntityCharts.get(i);
			list[chart.getLineNum()] = chart.getChartName();
		}
			
		return list;
	}
	
	// Returns array of Lgl entity ChOfAccs names in the system
	public String[] getChOfAccs()
	{
		String[] list = new String[ lglEntityCharts.size() ];
		
		for ( int i = 0; i < list.length; i++ )
		{
			LglEntityCharts chart = lglEntityCharts.get(i);
			list[chart.getLineNum()] = chart.getChOfAccs();
		}
		
		return list;
	}
}