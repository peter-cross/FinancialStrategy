package interfaces;

import foundation.AssociativeList;

/**
 * Interface AccountingGroups - To create structures of Balance Sheet and Income Statement
 * @author Peter Cross
 */
public interface AccountingGroups 
{
    AssociativeList balanceSheet    = balanceSheet(),       // Balance Sheet tree items
                    incomeStatement = incomeStatement();    // Income Statement tree items
    /**
     * Creates tree items for Balance Sheet
     * @return Created tree
     */
    static AssociativeList balanceSheet()
    {
        AssociativeList groupsList  = new AssociativeList();

        groupsList.set( "root", new String[]
                                { "Current Assets", 
                                  "Long-Term Assets", 
                                  "Current Liabilities", 
                                  "Long-Term Liabilities",
                                  "Shareholder's Equity" } );

        groupsList.set( "Current Assets", new String[]
                                          { "Cash and Cash Equivalents",
                                            "Short-Term Investments",
                                            "Accounts Receivable",
                                            "Inventory",
                                            "Prepaid Expenses" } );

        groupsList.set( "Accounts Receivable", new String[]
                                               { "Customers Receivable",
                                                 "Taxes Receivable",
                                                 "Notes Receivable",
                                                 "Dividends Receivable" } );

        groupsList.set( "Inventory", new String[]
                                     { "Merchandize",
                                       "Direct Materials",
                                       "Work-In-Progress",
                                       "Finished Goods" } );
		
        groupsList.set( "Long-Term Assets", new String[]
                                            { "Fixed Assets",
                                              "Non-Tangible Assets",
                                              "Long-Term Investments",
                                              "Depreciation" } );

        groupsList.set( "Depreciation", new String[]
                                        { "Fixed Assets Depreciation",
                                          "Non-Tangible Assets Amortization" } );

        groupsList.set( "Current Liabilities", new String[]
                                               { "Suppliers and Vendors Payable",
                                                 "Credit Cards Payable",
                                                 "Salaries Payable",
                                                 "Pension Payable",
                                                 "Taxes Payable",
                                                 "Accrued Operating Expenses",
                                                 "Interest Payable",
                                                 "Current portion of Long-Term Debt",
                                                 "Notes Payable",
                                                 "Bonds Payable",
                                                 "Fines, Penalties, Interest on Taxes",
                                                 "Dividends Payable" } );

        groupsList.set( "Taxes Payable", new String[]
                                         { "Income Taxes",
                                           "Sales Taxes",
                                           "Payroll Taxes",
                                           "Other Taxes" } );
		
        groupsList.set( "Dividends Payable", new String[]
                                             { "Preferred Shares Dividends",
                                               "Common Shares Dividends" } );

        groupsList.set( "Long-Term Liabilities", new String[]
                                                 { "Credit Loans",
                                                   "Warranty Liabilities" } );

        groupsList.set( "Shareholder's Equity", new String[]
                                                { "Common Stock",
                                                  "Retained Earnings" } );

        return groupsList;
	
    } // End of method ** balanceSheet **
	
    /**
     * Creates tree items for Income Statement
     * @return Created tree
     */
    static AssociativeList incomeStatement()
    {
        AssociativeList groupsList = new AssociativeList();

        groupsList.set( "root", new String[]
                                { "Sales Revenue",
                                  "Interest Income",
                                  "Investment Income",
                                  "Foreign Exchange Gains",
                                  "Cost Of Goods Sold",
                                  "Operating Expenses",
                                  "Depreciation",
                                  "Foreign Exchange Losses",
                                  "Operating Income",
                                  "Investment Losses",
                                  "Income Before Interest And Taxes",
                                  "Interest Expense",
                                  "Income Taxes",
                                  "Net Income" } );
		
        groupsList.set( "Sales Revenue", new String[]
                                         { "Credit Sales",
                                           "Cash Sales" } );
		
        groupsList.set( "Operating Expenses", new String[]
                                              { "R & D",
                                                "Design",
                                                "Purchasing",
                                                "Production",
                                                "Marketing",
                                                "Distribution",
                                                "Customer Service",
                                                "Administration" } );
        return groupsList;

    } // End of method ** incomeStatement **

} // End of interface