package forms;

import interfaces.Encapsulation;

/**
 * Class TableOutput - to pass results of table input dialog
 * @author Peter Cross
 *
 */
public class TableOutput implements Encapsulation
{
    /*          Properties   	                                                                                      */
    /******************************************************************************************************************/
    public String[][]		header;		// To store header output
    public String[][][]		table;		// To store table output

    /*          Constructors                                                                                          */
    /******************************************************************************************************************/
    public TableOutput()
    {
        header = new String[][]{ {} };
        table = new String[][][]{ { {} } };
    }
    
    public TableOutput( int headElms, int rows, int cols, int tabs )
    {
        header = new String[1][headElms];
        
        if ( rows > 0 && cols > 0 && tabs > 0 )
            table = new String[tabs][rows][cols];
    }
	
} // End of class ** TableOutput **