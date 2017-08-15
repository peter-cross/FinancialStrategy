package views;

import javafx.stage.Stage;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;

import forms.DialogElement;
import models.RegistryItemModel;

/**
 * Class TwoColumnDialog - Dialog form with 2 columns
 * @author Peter Cross
 */
public class TwoColumnView extends OneColumnView
{
    /**
     * Adds dialog elements to the form
     */
    protected void addDialogElements()
    {
        // Create Scene object and return pane for created scene
        content = createContent();

        if ( content == null )
            return;

        // Create tab pane element
        TabPane tabPane = new TabPane();
        // Make tabs closing unavailable
        tabPane.setTabClosingPolicy( TabPane.TabClosingPolicy.UNAVAILABLE );

        GridPane grid = null;   // To store grid for current Tab
        Tab tab;         // To store current Tab object

        // Loop for each tab
        for ( int t = 0; t < dialogElement.length; t++ )
        {
            // Create grid for current tab
            grid = createGrid();
             // Create Tab element
            tab = createTab( grid, t );
            
            // Calculate how many rows should be in each column
            int rowsInColumn = (int) Math.ceil( (double) dialogElement[t].length/2 );
            
            // Loop for each dialog element
            for ( int i = 0; i < dialogElement[t].length; i++ )
            {
                // Get current dialog element
                final DialogElement el = dialogElement[t][i];

                if ( el == null )
                    continue;
                
                // Calculate column number on the grid for current dialog element
                int colNum = i/rowsInColumn * 2;
                // Calculate row number Loop for each node
                int row = ( i < rowsInColumn ? i+1 : i - rowsInColumn + 1 );
                
                // If label for current element is specified and it's not a checkbox
                if ( !el.labelName.isEmpty() && el.checkBoxlabel.isEmpty() )
                    createLabel( grid, el, colNum + 0, row );

                // If there is a check box for the current dialog element
                if ( !el.checkBoxlabel.isEmpty() )
                    createCheckBox( grid, el, colNum + 1, row );
				
                // If List reference  or text choices are passed
                else if ( el.valueType.contains( "List" ) || el.list != null )
                    createComboBox( grid, el, colNum + 1, row );

                // If it's a Tree List field
                else if ( el.valueType.contains( "Tree" ) )
                    createTreeList( grid, el, colNum + 1, row );

                // If it's a Date field
                else if ( el.valueType.contains( "Date" ) )
                    createDatePicker( grid, el, colNum + 1, row );

                // If it's a File field
                else if ( el.valueType.contains( "File" ) )
                    createFileChooser( grid, el, colNum + 1, row );

                // If it's a Numerator field
                else if ( el.valueType.contains( "Numerator" ) )
                    createNumerator( grid, el, 1, i+1 );
                
                // Otherwise
                else 
                    createTextField( grid, el, colNum + 1, row );

            } // End of for loop ** for each dialog element **		
		
            // If list of tabs is not empty
            if ( tab != null )
                // Add current tab to the Tab Pane
                tabPane.getTabs().add( tab );

        } // End of for loop ** for each tab**	
	
        // If there is more than 1 tab
        if ( dialogElement.length > 1 )
            // Add Tab pane to the Scene pane
            content.getChildren().add( tabPane );
        else
            // Add just current grid to the pane
            content.getChildren().add( grid );
    }
    
    /**
     * Class constructor
     * @param doc RegistryItemModel type document
     * @param title Form title
     */
    public TwoColumnView( RegistryItemModel doc, String title )
    {
        super( doc, title );
    }
    
    /**
     * Class constructor
     * @param stage Stage object where to display form
     * @param title Form title
     * @param tabs Array of tabs for header part
     * @param dlg Array of dialog elements to display
     */
    public TwoColumnView( Stage stage, String title, String[] tabs, DialogElement[]... dlg )
    {
        super( stage, title, tabs, dlg );
    }
    
} // End of class ** TwoColumnView **