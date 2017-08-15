package models;

import javafx.stage.Stage;

import forms.TreeDialog;
import interfaces.AccountingGroups;

/**
 * Class IncomeStatementTree - Creates Income Statement Tree object
 * @author Peter Cross
 */
public class IncomeStatementTree extends TreeDialog implements AccountingGroups
{
    public IncomeStatementTree( Stage stage  )
    {
        super( stage, "Income Statement Groups", incomeStatement );
    }

    public IncomeStatementTree( Stage stage, int width )
    {
        super( stage, "Income Statement Groups", incomeStatement, width );
    }
	
} // End of class ** IncomeStatementGroups **