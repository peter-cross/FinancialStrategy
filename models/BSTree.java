package models;

import javafx.stage.Stage;
import entities.HashMap;
import forms.TreeDialog;
import interfaces.AcctingGrps;

/**
 * Class BSTree - creates BalSht Tree object
 * @author Peter Cross
 */
public class BSTree extends TreeDialog implements AcctingGrps
{
	private static String balShtStr = HashMap.getByKey( "BalSht" );
	
    public BSTree( Stage stage )
    {
        super( stage, balShtStr + " Groups", balSht );
    }

    public BSTree( Stage stage, int width )
    {
        super( stage, balShtStr + " Groups", balSht, width );
    }
	
} // End of class ** BSGroups **