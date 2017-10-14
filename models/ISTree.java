package models;

import javafx.stage.Stage;

import forms.TreeDialog;
import interfaces.AccountingGroups;

/**
 * Class ISTree - Creates Income Statement Tree object
 * @author Peter Cross
 */
public class ISTree extends TreeDialog implements AccountingGroups
{
    public ISTree( Stage stage  )
    {
        super( stage, "Income Statement Groups", incomeStatement );
    }

    public ISTree( Stage stage, int width )
    {
        super( stage, "Income Statement Groups", incomeStatement, width );
    }
	
} // End of class ** IncomeStatementGroups **