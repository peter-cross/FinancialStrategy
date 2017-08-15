package views;

import models.RegistryItemModel;

/**
 * Class OneColumnTableDialog - Dialog Form to work with simple tables
 * @author Peter Cross
 *
 */
public class OneColumnTableView extends OneColumnView
{
    /*          Properties   	                                                                                      */
    /******************************************************************************************************************/
    private int			numRows;	// Number of rows passed
    private String[]	tabs;		// Tab names
    
    /**
     * Returns form fields in text format
     * @param <T> - Type parameter
     * @return Object containing form fields in text format
     */
    @Override
    public <T> T result()
    {
        return new TableDialogView(this, regItem).result();
        
    } // End of method ** result **

    /*          Constructors                                                                                          */
    /******************************************************************************************************************/
    
    /**
     * Class constructor
     * @param doc RegistryItemModel type document
     * @param title Title for the form
     * @param numRows Number of rows in table part to display
     */
    public OneColumnTableView( RegistryItemModel doc, String title, int numRows )
    {
        super( doc, title );
        regItem = doc;
        this.numRows = numRows;
    }
    
    /*          Constants                                                                                             */
    /******************************************************************************************************************/
    private static final long serialVersionUID = 1L;
    
} // End of class ** OneColumnTableDialog **