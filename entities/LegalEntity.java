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
 * Entity implementation class for : LegalEntityModel database data
 * @author Peter Cross
 * 
 */
@Entity
public class LegalEntity 
{
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	private long 	legalEntityId;
	
	private String	iD;				// Legal Entity ID in the system
	private String	name;			// Common name
	private String	legalName;		// Official legal name
	private String	phone;			// Phone number
	private String	contact;		// Contact in the company
	private String	address;		// Address
	
	// Charts of Accounts for Legal Entity
	@OneToMany( fetch=FetchType.EAGER, cascade=CascadeType.PERSIST )
	private Vector<LegalEntityCharts> legalEntityCharts;
	
	/**
	 * Mandatory class constructor
	 */
	public LegalEntity() 
	{
		super();
	}
	
	/**
	 * Class constructor with all required info for creating Legal Entity object
	 * @param iD ID for user
	 * @param name Common name
	 * @param legalName Legal name of legal entity
	 * @param phone Phone number
	 * @param contact Contact person
	 * @param address Address of Legal Entity
	 */
	public LegalEntity( String iD, String name, String legalName, String phone, String contact, String address, List<String> chartNames, ArrayList<COA> chOfAccs ) 
	{
		update( iD, name, legalName, phone, contact, address, chartNames, chOfAccs );
	}
	
	/**
	 * Updates information of existing legal entity
	 * @param iD ID for user
	 * @param name Common name
	 * @param legalName Legal name of legal entity
	 * @param phone Phone number
	 * @param contact Contact person
	 * @param address Address of Legal Entity
	 */
	public void update( String iD, String name, String legalName, String phone, String contact, String address, List<String> chartNames, ArrayList<COA> chOfAccs )
	{
		// Save provided info and crypt sensitive info
		this.iD = iD;
		this.name = Cipher.crypt( name );
		this.legalName = Cipher.crypt( legalName );
		this.phone = Cipher.crypt( phone );
		this.contact = Cipher.crypt( contact );
		this.address = Cipher.crypt( address );
		
		if ( legalEntityCharts != null && legalEntityCharts.size() > 0 )
			Database.removeFromDB( legalEntityCharts );
		
		legalEntityCharts = new Vector<LegalEntityCharts>();
		LegalEntityCharts legalEntityChartsLine;
		
		for ( int i = 0; i < chOfAccs.size(); i++ )
		{
			legalEntityChartsLine = new LegalEntityCharts( i, chartNames.get(i), chOfAccs.get(i) );
			legalEntityCharts.add( legalEntityChartsLine );
		}
	}
	
	// Returns ID of Legal Entity
	public String getId()
	{
		return iD;
	}
	
	// Returns name of Legal Entity
	public String getName()
	{
		return Cipher.decrypt( name );
	}
	
	// Returns legal name of Legal Entity
	public String getLegalName()
	{
		return Cipher.decrypt( legalName );
	}
	
	// Returns phone of Legal Entity
	public String getPhone()
	{
		return Cipher.decrypt( phone );
	}
	
	// Returns contact person info of Legal Entity
	public String getContact()
	{
		return Cipher.decrypt( contact );
	}
	
	// Returns address of Legal Entity
	public String getAddress()
	{
		return Cipher.decrypt( address );
	}
	
	// Returns array of ChOfAccs names specific for Legal entity 
	public String[] getChartNames()
	{
		String[] list = new String[ legalEntityCharts.size() ];
		
		for ( int i = 0; i < list.length; i++ )
		{
			LegalEntityCharts chart = legalEntityCharts.get(i);
			list[chart.getLineNum()] = chart.getChartName();
		}
			
		return list;
	}
	
	// Returns array of Legal entity ChOfAccs names in the system
	public String[] getChOfAccs()
	{
		String[] list = new String[ legalEntityCharts.size() ];
		
		for ( int i = 0; i < list.length; i++ )
		{
			LegalEntityCharts chart = legalEntityCharts.get(i);
			list[chart.getLineNum()] = chart.getChOfAccs();
		}
		
		return list;
	}
}