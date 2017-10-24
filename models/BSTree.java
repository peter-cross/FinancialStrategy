package models;

import javafx.stage.Stage;
import forms.TreeDialog;
import interfaces.AcctingGrps;
import static interfaces.Utilities.$;

/**
 * Class BSTree - creates BalSht Tree object
 * @author Peter Cross
 */
public class BSTree extends TreeDialog implements AcctingGrps
{
	private static String balShtStr = $( "BalSht" );
	
    public BSTree( Stage stage )
    {
        super( stage, balShtStr + " Groups", balSht );
    }

    public BSTree( Stage stage, int width )
    {
        super( stage, balShtStr + " Groups", balSht, width );
    }
	
} // End of class ** BSGroups **