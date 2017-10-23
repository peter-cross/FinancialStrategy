package models;

import javafx.stage.Stage;
import entities.HashMap;
import forms.TreeDialog;
import interfaces.AcctingGrps;

/**
 * Class ISTree - Creates IncStt Tree object
 * @author Peter Cross
 */
public class ISTree extends TreeDialog implements AcctingGrps
{
	private static String incSttStr = HashMap.getByKey( "IncStt" );
	
	public ISTree( Stage stage  )
    {
        super( stage, incSttStr + " Groups", incStt );
    }

    public ISTree( Stage stage, int width )
    {
        super( stage, incSttStr + " Groups", incStt, width );
    }
	
} // End of class ** ISTree **