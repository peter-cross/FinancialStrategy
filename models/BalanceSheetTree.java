package models;

import javafx.stage.Stage;

import forms.TreeDialog;
import interfaces.AccountingGroups;

/**
 * Class BalanceSheetTree - creates Balance Sheet Tree object
 * @author Peter Cross
 */
public class BalanceSheetTree extends TreeDialog implements AccountingGroups
{
    public BalanceSheetTree( Stage stage )
    {
        super( stage, "Balance Sheet Groups", balanceSheet );
    }

    public BalanceSheetTree( Stage stage, int width )
    {
        super( stage, "Balance Sheet Groups", balanceSheet, width );
    }
	
} // End of class ** BalanceSheetGroups **