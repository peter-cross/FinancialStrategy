package interfaces;

import javafx.event.Event;

import foundation.AssociativeList;

/**
 * Interface Lambda - Keeps lambda expressions for different classes in one place
 * @author Peter Cross
 */
public interface Lambda 
{
    // Interface for menu action listener
    public interface MenuItemAction
    {
    	void run( Event e );
    }
	
    // Interface for dialog listener
    public interface DialogAction
    {
    	void run( DialogAction next );
    }
	
    // Interface for element validation
    public interface ElementValidation
    {
        /**
         * Validates dialog element's field
         * @param elementValue String value of validated field
         * @return True if validation went successfully, or false otherwise
         */
        boolean run( String elementValue );
    }
    
    // Interface to invoke on element change
    public interface OnElementChange
    {
    	//void run( Object element, AssociativeList elementsList );
    	void run( AssociativeList elementsList );
    }
    
    // Interface to invoke on element focus
    public interface OnElementFocus
    {
    	void run( Object element );
    }
	
} // End of interface ** Lambda **