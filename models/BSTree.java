package models;

import javafx.stage.Stage;

import forms.TreeDialog;
import interfaces.AccountingGroups;

/**
 * Class BSTree - creates Balance Sheet Tree object
 * @author Peter Cross
 */
public class BSTree extends TreeDialog implements AccountingGroups
{
    public BSTree( Stage stage )
    {
        super( stage, "Balance Sheet Groups", balanceSheet );
    }

    public BSTree( Stage stage, int width )
    {
        super( stage, "Balance Sheet Groups", balanceSheet, width );
    }
	
} // End of class ** BalanceSheetGroups **