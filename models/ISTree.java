package models;

import javafx.stage.Stage;
import forms.TreeDialog;
import interfaces.AcctingGrps;
import static interfaces.Utilities.hash;

/**
 * Class ISTree - Creates IncStt Tree object
 * @author Peter Cross
 */
public class ISTree extends TreeDialog implements AcctingGrps
{
	private static String incSttStr = hash( "IncStt" );
	
	public ISTree( Stage stage  )
    {
        super( stage, incSttStr + " Groups", incStt );
    }

    public ISTree( Stage stage, int width )
    {
        super( stage, incSttStr + " Groups", incStt, width );
    }
	
} // End of class ** ISTree **