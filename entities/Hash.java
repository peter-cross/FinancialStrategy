package entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import application.Database;
import foundation.Cipher;

@Entity
public class Hash 
{
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	private long hashId;
	
	private String code;
	private String value;
	
	private static  List<Hash> list;       // List of Items
    	
	public Hash()
	{
		super();
	}
	
	public Hash( String key, String value )
	{
		update( key, value );
	}
	
	public void update( String key, String value )
	{
		this.code = key;
		this.value = Cipher.crypt( value );
	}

	public static String getByKey( String key )
	{
		if ( key.isEmpty() )
			return "";
		
		if ( list != null )
			for ( Hash d : list )
				if ( d.code.equals( key ) )
					return Cipher.decrypt( d.value );
		
		return "";
	}
	
	public static  List[] createList()
    {
    	if ( list == null )
            //createNewList();
    		loadListFromDB();
        
        return new List[] { list };
    }
    
	/*
	public static void createNewList()
    {
        list = new ArrayList<Hash>();
        
        list.add( new Hash( "CA", "Current Assets" ) );
        list.add( new Hash( "LTA", "Long-Term Assets" ) );
        list.add( new Hash( "CL", "Current Liabilities" ) );
        list.add( new Hash( "LTL", "Long-Term Liabilities" ) );
        list.add( new Hash( "SHE", "Shareholder's Equity" ) );
        list.add( new Hash( "CCE", "Cash and Cash Equivalents" ) );
        list.add( new Hash( "STI", "Short-Term Investments" ) );
        list.add( new Hash( "AR", "Accounts Receivable" ) );
        list.add( new Hash( "INV", "Inventory" ) );
        list.add( new Hash( "PE", "Prepaid Expenses" ) );
        list.add( new Hash( "CR", "Customers Receivable" ) );
        list.add( new Hash( "TXR", "Taxes Receivable" ) );
        list.add( new Hash( "NR", "Notes Receivable" ) );
        list.add( new Hash( "DR", "Dividends Receivable" ) );
        list.add( new Hash( "MRZ", "Merchandize" ) );
        list.add( new Hash( "DM", "Direct Materials" ) );
        list.add( new Hash( "WIP", "Work-In-Progress" ) );
        list.add( new Hash( "FG", "Finished Goods" ) );
        list.add( new Hash( "FA", "Fixed Assets" ) );
        list.add( new Hash( "NTA", "Non-Tangible Assets" ) );
        list.add( new Hash( "LTI", "Long-Term Investments" ) );
        list.add( new Hash( "DPR", "Depreciation" ) );
        list.add( new Hash( "FAD", "Fixed Assets Depreciation" ) );
        list.add( new Hash( "NTAD", "Non-Tangible Assets Amortization" ) );
        list.add( new Hash( "SVP", "Suppliers and Vendors Payable" ) );
        list.add( new Hash( "CCP", "Credit Cards Payable" ) );
        list.add( new Hash( "SR", "Salaries Payable" ) );
        list.add( new Hash( "PP", "Pension Payable" ) );
        list.add( new Hash( "TXP", "Taxes Payable" ) );
        list.add( new Hash( "AOE", "Accrued Operating Expenses" ) );
        list.add( new Hash( "IP", "Interest Payable" ) );
        list.add( new Hash( "CPLTD", "Current portion of Long-Term Debt" ) );
        list.add( new Hash( "NP", "Notes Payable" ) );
        list.add( new Hash( "BP", "Bonds Payable" ) );
        list.add( new Hash( "FPITX", "Fines, Penalties, Interest on Taxes" ) );
        list.add( new Hash( "DIVP", "Dividends Payable" ) );
        list.add( new Hash( "INTX", "Income Taxes" ) );
        list.add( new Hash( "SLTX", "Sales Taxes" ) );
        list.add( new Hash( "PRTX", "Payroll Taxes" ) );
        list.add( new Hash( "OTTX", "Other Taxes" ) );
        list.add( new Hash( "PSDIV", "Preferred Shares Dividends" ) );
        list.add( new Hash( "CSDIV", "Common Shares Dividends" ) );
        list.add( new Hash( "CRL", "Credit Loans" ) );
        list.add( new Hash( "WL", "Warranty Liabilities" ) );
        list.add( new Hash( "CST", "Common Stock" ) );
        list.add( new Hash( "RE", "Retained Earnings" ) );
        list.add( new Hash( "SLR", "Sales Revenue" ) );
        list.add( new Hash( "INTI", "Interest Income" ) );
        list.add( new Hash( "INVI", "Investment Income" ) );
        list.add( new Hash( "FEG", "Foreign Exchange Gains" ) );
        list.add( new Hash( "COGS", "Cost Of Goods Sold" ) );
        list.add( new Hash( "OPE", "Operating Expenses" ) );
        list.add( new Hash( "DPR", "Depreciation" ) );
        list.add( new Hash( "FEXL", "Foreign Exchange Losses" ) );
        list.add( new Hash( "OPRI", "Operating Income" ) );
        list.add( new Hash( "INVL", "Investment Losses" ) );
        list.add( new Hash( "IBIT", "Income Before Interest And Taxes" ) );
        list.add( new Hash( "INTE", "Interest Expense" ) );
        list.add( new Hash( "INCTX", "Income Taxes" ) );
        list.add( new Hash( "NI", "Net Income" ) );
        list.add( new Hash( "CRSL", "Credit Sales" ) );
        list.add( new Hash( "CSSL", "Cash Sales" ) );
        list.add( new Hash( "RD", "R & D" ) );
        list.add( new Hash( "DSN", "Design" ) );
        list.add( new Hash( "PCH", "Purchasing" ) );
        list.add( new Hash( "PRD", "Production" ) );
        list.add( new Hash( "MKT", "Marketing" ) );
        list.add( new Hash( "DST", "Distribution" ) );
        list.add( new Hash( "CSS", "Customer Service" ) );
        list.add( new Hash( "ADM", "Administration" ) );
        list.add( new Hash( "BalSht", "Balance Sheet" ) );
        list.add( new Hash( "IncStt", "Income Statement" ) );
        list.add( new Hash( "ChOfAccs", "Chart Of Accounts" ) );
        list.add( new Hash( "Crcy", "Currency" ) );
        list.add( new Hash( "GlAcc", "G/L Acct" ) );
        list.add( new Hash( "GlAcct", "G/L Account" ) );
        list.add( new Hash( "GlNumber", "G/L Number" ) );
        list.add( new Hash( "AcctName", "Account Name" ) );
        list.add( new Hash( "AccType", "Type of Account" ) );
        list.add( new Hash( "AccGrp", "Account Group" ) );
        list.add( new Hash( "FrgnCrcy", "Foreign Currency" ) );
        list.add( new Hash( "ContraAcct", "Contra Account" ) );
        list.add( new Hash( "LglEntity", "Legal Entity" ) );
        list.add( new Hash( "LglName", "Legal Name" ) );
        list.add( new Hash( "TractsDscr", "Transaction Description" ) );
        list.add( new Hash( "TransModel", "Transactions Model" ) );
        list.add( new Hash( "A1str", "Bank Accounts" ) );
        list.add( new Hash( "A1", "BankAccount" ) );
        list.add( new Hash( "A2str", "Business Partners" ) );
        list.add( new Hash( "A2", "BusinessPartner" ) );
        list.add( new Hash( "A3str", "Contracts" ) );
        list.add( new Hash( "A3", "Contract" ) );
        list.add( new Hash( "A4str", "Employees" ) );
        list.add( new Hash( "A4", "Employee" ) );
        list.add( new Hash( "A5str", "Expenses" ) );
        list.add( new Hash( "A5", "Expense" ) );
        list.add( new Hash( "A6str", "Inventory" ) );
        list.add( new Hash( "A6", "Inventory" ) );
        list.add( new Hash( "A7str", "Legal Entities" ) );
        list.add( new Hash( "A7", "LglEntity"  ) );
        list.add( new Hash( "A8str", "Locations" ) );
        list.add( new Hash( "A8", "Location" ) );
        list.add( new Hash( "A9str", "Long Term Assets" ) );
        list.add( new Hash( "A9", "LongTermAsset" ) );
        list.add( new Hash( "A10str", "Marketable Securities" ) );
        list.add( new Hash( "A10", "Securities" ) );
        list.add( new Hash( "A11str", "Products" ) );
        list.add( new Hash( "A11", "Product" ) );
        list.add( new Hash( "A12str", "Shareholders" ) );
        list.add( new Hash( "A12", "Shareholder" ) );
        list.add( new Hash( "A13str", "Warranties" ) );
        list.add( new Hash( "A13", "Warranty" ) );
        list.add( new Hash( "A14str", "Business Lines" ) );
        list.add( new Hash( "A14", "BusinessLine" ) );
        
        //Database.persistToDB( list );
    }   
	*/
	
	private static void loadListFromDB()
	{
		List<Hash> lst = getFromDB();
		
		if ( lst == null )
			return;
		
		list = new ArrayList<Hash>();
		
		for ( Hash newHsh : lst )
			list.add( newHsh );
	}
	
	
	private static List<Hash> getFromDB()
    {
    	// Get Entity Manager
    	EntityManager em = Database.getEntityManager();
    	
        if ( em != null )
        	try
        	{
        		List lst = em.createQuery( "SELECT c FROM Hash AS c" ).getResultList();
        		// Do query for Entity class in DB and return results of query
        		return (List<Hash>)lst;
            }
        	catch ( Exception e )
        	{
        		return null;
        	}
		
        return null;
    }
}