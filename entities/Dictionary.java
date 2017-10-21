package entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import application.Database;
import foundation.Cipher;

@Entity
public class Dictionary 
{
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	private long dictionaryId;
	
	private String code;
	private String value;
	
	private static  List<Dictionary> list;       // List of Items
    	
	public Dictionary()
	{
		super();
	}
	
	public Dictionary( String key, String value )
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
			for ( Dictionary d : list )
				if ( d.code.equals( key ) )
					return Cipher.decrypt( d.value );
		
		return "";
	}
	
	public static  List[] createList()
    {
    	if ( list == null )
            createNewList();
        
        return new List[] { list };
    }
    
	public static void createNewList()
    {
        list = new ArrayList<Dictionary>();
        
        list.add( new Dictionary( "CA", "Current Assets" ) );
        list.add( new Dictionary( "LTA", "Long-Term Assets" ) );
        list.add( new Dictionary( "CL", "Current Liabilities" ) );
        list.add( new Dictionary( "LTL", "Long-Term Liabilities" ) );
        list.add( new Dictionary( "SHE", "Shareholder's Equity" ) );
        list.add( new Dictionary( "CCE", "Cash and Cash Equivalents" ) );
        list.add( new Dictionary( "STI", "Short-Term Investments" ) );
        list.add( new Dictionary( "AR", "Accounts Receivable" ) );
        list.add( new Dictionary( "INV", "Inventory" ) );
        list.add( new Dictionary( "PE", "Prepaid Expenses" ) );
        list.add( new Dictionary( "CR", "Customers Receivable" ) );
        list.add( new Dictionary( "TXR", "Taxes Receivable" ) );
        list.add( new Dictionary( "NR", "Notes Receivable" ) );
        list.add( new Dictionary( "DR", "Dividends Receivable" ) );
        list.add( new Dictionary( "MRZ", "Merchandize" ) );
        list.add( new Dictionary( "DM", "Direct Materials" ) );
        list.add( new Dictionary( "WIP", "Work-In-Progress" ) );
        list.add( new Dictionary( "FG", "Finished Goods" ) );
        list.add( new Dictionary( "FA", "Fixed Assets" ) );
        list.add( new Dictionary( "NTA", "Non-Tangible Assets" ) );
        list.add( new Dictionary( "LTI", "Long-Term Investments" ) );
        list.add( new Dictionary( "DPR", "Depreciation" ) );
        list.add( new Dictionary( "FAD", "Fixed Assets Depreciation" ) );
        list.add( new Dictionary( "NTAD", "Non-Tangible Assets Amortization" ) );
        list.add( new Dictionary( "SVP", "Suppliers and Vendors Payable" ) );
        list.add( new Dictionary( "CCP", "Credit Cards Payable" ) );
        list.add( new Dictionary( "SR", "Salaries Payable" ) );
        list.add( new Dictionary( "PP", "Pension Payable" ) );
        list.add( new Dictionary( "TXP", "Taxes Payable" ) );
        list.add( new Dictionary( "AOE", "Accrued Operating Expenses" ) );
        list.add( new Dictionary( "IP", "Interest Payable" ) );
        list.add( new Dictionary( "CPLTD", "Current portion of Long-Term Debt" ) );
        list.add( new Dictionary( "NP", "Notes Payable" ) );
        list.add( new Dictionary( "BP", "Bonds Payable" ) );
        list.add( new Dictionary( "FPITX", "Fines, Penalties, Interest on Taxes" ) );
        list.add( new Dictionary( "DIVP", "Dividends Payable" ) );
        list.add( new Dictionary( "INTX", "Income Taxes" ) );
        list.add( new Dictionary( "SLTX", "Sales Taxes" ) );
        list.add( new Dictionary( "PRTX", "Payroll Taxes" ) );
        list.add( new Dictionary( "OTTX", "Other Taxes" ) );
        list.add( new Dictionary( "PSDIV", "Preferred Shares Dividends" ) );
        list.add( new Dictionary( "CSDIV", "Common Shares Dividends" ) );
        list.add( new Dictionary( "CRL", "Credit Loans" ) );
        list.add( new Dictionary( "WL", "Warranty Liabilities" ) );
        list.add( new Dictionary( "CST", "Common Stock" ) );
        list.add( new Dictionary( "RE", "Retained Earnings" ) );
        list.add( new Dictionary( "SLR", "Sales Revenue" ) );
        list.add( new Dictionary( "INTI", "Interest Income" ) );
        list.add( new Dictionary( "INVI", "Investment Income" ) );
        list.add( new Dictionary( "FEG", "Foreign Exchange Gains" ) );
        list.add( new Dictionary( "COGS", "Cost Of Goods Sold" ) );
        list.add( new Dictionary( "OPE", "Operating Expenses" ) );
        list.add( new Dictionary( "DPR", "Depreciation" ) );
        list.add( new Dictionary( "FEXL", "Foreign Exchange Losses" ) );
        list.add( new Dictionary( "OPRI", "Operating Income" ) );
        list.add( new Dictionary( "INVL", "Investment Losses" ) );
        list.add( new Dictionary( "IBIT", "Income Before Interest And Taxes" ) );
        list.add( new Dictionary( "INTE", "Interest Expense" ) );
        list.add( new Dictionary( "INCTX", "Income Taxes" ) );
        list.add( new Dictionary( "NI", "Net Income" ) );
        list.add( new Dictionary( "CRSL", "Credit Sales" ) );
        list.add( new Dictionary( "CSSL", "Cash Sales" ) );
        list.add( new Dictionary( "RD", "R & D" ) );
        list.add( new Dictionary( "DSN", "Design" ) );
        list.add( new Dictionary( "PCH", "Purchasing" ) );
        list.add( new Dictionary( "PRD", "Production" ) );
        list.add( new Dictionary( "MKT", "Marketing" ) );
        list.add( new Dictionary( "DST", "Distribution" ) );
        list.add( new Dictionary( "CSS", "Customer Service" ) );
        list.add( new Dictionary( "ADM", "Administration" ) );
        list.add( new Dictionary( "BalSht", "Balance Sheet" ) );
        list.add( new Dictionary( "IncStt", "Income Statement" ) );
        list.add( new Dictionary( "ChOfAccs", "Chart Of Accounts" ) );
        list.add( new Dictionary( "Crcy", "Currency" ) );
        list.add( new Dictionary( "GlAcct", "G/L Account" ) );
        list.add( new Dictionary( "GlNumber", "G/L Number" ) );
        list.add( new Dictionary( "AcctName", "Account Name" ) );
        list.add( new Dictionary( "AccType", "Type of Account" ) );
        list.add( new Dictionary( "AccGrp", "Account Group" ) );
        list.add( new Dictionary( "FrgnCrcy", "Foreign Currency" ) );
        list.add( new Dictionary( "ContraAcct", "Contra Account" ) );
        list.add( new Dictionary( "LglEntity", "Legal Entity" ) );
        list.add( new Dictionary( "LglName", "Legal Name" ) );
        list.add( new Dictionary( "TractsDscr", "Transaction Description" ) );
        list.add( new Dictionary( "TransModel", "Transactions Model" ) );
        
        
        
        //Database.persistToDB( list );
    }   
}