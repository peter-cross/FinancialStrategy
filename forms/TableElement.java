package forms;

import java.util.LinkedHashSet;

import interfaces.Encapsulation;
import interfaces.Lambda.ElementValidation;
import interfaces.Lambda.OnElementChange;

/**
 * Class TableElement - to store parameters for creating TableView columns
 * @author Peter Cross
 *
 */
public class TableElement implements Encapsulation
{
    /*          Properties   	                                                                                      */
    /******************************************************************************************************************/
    public String                   columnName;		// Column name for the element
    public boolean                  editable;		// Editable column or not
    public int                      width;		// Width of the column

    public String                   valueType;		// Value type of the element
    public String[]                 textChoices;	// Array containing list of choices
    public LinkedHashSet            list;		// Reference to list
    public int                      checkBox;		// Check box value
    public String                   checkBoxlabel;	// Label for check box
    public String[]                 textValue;		// Text values of the element

    public ElementValidation        validation;		// Lambda expression for element validation
    public OnElementChange          onChange;		// Lambda expression invoked on element change
    
    
    /*          Constructors                                                                                          */
    /******************************************************************************************************************/
    public TableElement( )
    {
        columnName = "";
        textChoices = new String[]{};
        textValue = new String[] { "" };
        checkBox = -1;
        checkBoxlabel = "";
        editable = true;
        valueType = "";
    }
	
    public TableElement( String columnName )
    {
        this();
        this.columnName = columnName;
    }
	
} // End of class ** TableElement **