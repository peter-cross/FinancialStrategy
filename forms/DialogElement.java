package forms;

import java.util.LinkedHashSet;

import interfaces.Encapsulation;
import interfaces.Lambda.ElementValidation;
import interfaces.Lambda.OnElementChange;

/**
 * Class DialogElement - for creating elements of dialog forms
 * @author Peter Cross
 *
 */
public class DialogElement implements Encapsulation
{
    /*          Properties   	                                                                                      */
    /******************************************************************************************************************/
    public String               labelName;		// Label for the element
    public String               shortName;      // Short name for the element
    public String               valueType;		// Value type of the element
    public String               attributeName;  // Short name for the element
    
    public int                  checkBox;		// Check box value
    public String               checkBoxlabel;	// Label for check box

    public String[]             textChoices;	// Array containing list of choices
    public LinkedHashSet        list;			// Reference to list
    public int					defaultChoice;	// Default choice for list of choices

    public String 				textValue;		// Text value of the element
    public boolean				editable;		// Editable column or not
    public ElementValidation	validation;		// Lambda expression with validation code
    public OnElementChange		onChange;		// Lambda expression invoked on element change
    public int					width;			// Width of the field

    /*          Constructors                                                                                          */
    /******************************************************************************************************************/
    
    /**
     * Class default constructor
     */
    public DialogElement( )
    {
        labelName = "";
        textChoices = new String[]{};
        textValue = "";
        checkBox = -1;
        checkBoxlabel = "";
        editable = true;
        defaultChoice = -1;
        valueType = "";
    }
	
    /**
     * Class constructor
     * @param label Label for dialog element
     */
    public DialogElement( String label )
    {
        this();
        labelName = label;
    }
	
} // End of class ** DialogElement **