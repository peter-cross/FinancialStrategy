package views;

import models.RegistryItemModel;

/**
 * Class TwoColumnTableDialog - Dialog Form for 2-column header part with table part
 * @author Peter Cross
 */
public class TwoColumnTableView extends TwoColumnView
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
     * @param title Form title
     * @param numRows Number of rows in table part
     */
    public TwoColumnTableView( RegistryItemModel doc, String title, int numRows )
    {
        super( doc, title );
        regItem = doc;
        this.numRows = numRows;    
    }
    
    /**
     * Class constructor
     * @param doc RegistryItemModel type document
     * @param title Form title
     * @param numRows Number of rows in table part
     * @param tableTabs Array of Tabs for table part
     */
    public TwoColumnTableView( RegistryItemModel doc, String title, int numRows, String[] tableTabs )
    {
        super( doc, title );
        regItem = doc;
        this.numRows = numRows;
        this.tabs = tableTabs;
    }
    
    /*       Constants                                                                                                     */
    /******************************************************************************************************************/
    private static final long serialVersionUID = 1L;
    
} // End of class ** TwoColumnTableView **