package views;

import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.io.File;
import java.lang.reflect.Constructor;
import java.time.LocalDate;

import models.GLAcctModel;
import models.RegistryItemModel;

import forms.DateDialog;
import forms.DynamicDimensions;
import forms.ListDialog;
import forms.DialogElement;
import forms.TableElement;
import forms.TableOutput;
import forms.TreeDialog;

import foundation.AssociativeList;
import foundation.Data;
import foundation.Item;

import interfaces.Buttons;
import interfaces.Encapsulation;
import interfaces.Lambda.DialogAction;
import interfaces.Lambda.ElementValidation;

import static interfaces.Utilities.createModelClass;
import static interfaces.Utilities.arrayCount;
import static interfaces.Buttons.newActionButton;
import static interfaces.Utilities.hash;

/**
 *
 * @author Peter Cross
 */
public class TableDialogView implements Buttons, Encapsulation
{
    final static int PREF_HEIGHT = 250,
                     PREF_WIDTH = 300;
    
    final private OneColumnView 						tblObj;
    
    private TableElement[][]                            tableElement;       // Array of table elements passed to the form
    private AssociativeList                             dlgElementsList;    // Dialog attributes list
    private int                                         maxCols;
    private int                                         ctrlBtnWidth = 12;  // Control button width
    private int                                         numRows;            // Number of rows passed
    private AssociativeList[][]                         elementsList;       // Associative array of table elements
    private Stage                                       stage;              // Get current Stage object
    private String[]                                    tabs;               // Tab names
   
    // ArrayList of TableView objects for each tab
    private ArrayList<TableView<ArrayList<Data>>>       table;
    // Data model object for current table dialog
    private ArrayList<ObservableList<ArrayList<Data>>>  data; 
            
    /**
     * Returns form fields in text format
     * @param <T> - Type parameter
     * @return Object containing form fields in text format
     */
    public <T> T result()
    {
        // Get dialog elements for current object
        DialogElement[][] dialogElement = (DialogElement[][]) dlgElementsList.get( "dialogElement" );
        
        // Add buttons to the form
        addTableButtons();
        
        // Display Table dialog form
        tblObj.display();

        int headElms = arrayCount( dialogElement[0] );  // Number of head elements
        
        return getTableItemsAndReturn( headElms );
        
    } // End of method ** result **
    
    /**
     * Gets items in table part of the form and returns them
     * @param headElms Number of head elements in the form
     * @return Info in table part of the form
     */
    private <T> T getTableItemsAndReturn( int headElms )
    {
    	// Create array for row counters
        int[] rows = new int[tableElement.length];
        int maxRows = 0;
        ObservableList items;
        int numRows; // Number of rows passed
        
        // Loop to count the number of rows on each tab and find the max
        // Loop for each table element
        for ( int t = 0; t < tableElement.length; t++ )
        {
            // Set rows counter to zero
            rows[t] = 0;
            // Get items for for current table view
            items = (ObservableList) table.get(t).getItems();
            
            numRows = items.size();
            
            // Loop for each row specified through passed parameter
            for ( int i = 0; i < numRows; i++ )
                // If in the row there is some value entered
                if ( rowEntered(i, t) )
                    // Increment row counter
                    rows[t]++;

            // If for current table element rows counter is greater than maxRows 
            if ( rows[t] > maxRows )
                // Save current rows counter to maxRows 
                maxRows = rows[t];
            
        } // End of loop ** for each table element **
		
        // Return output object
        return (T)resultOutput( headElms, maxRows );
    }
    
    /**
     * Returns input in Table part of form
     * @param headElms Number of head elements
     * @param maxRows Maximum number of rows
     * @return Object containing info in table part of the form
     */
    private TableOutput resultOutput( int headElms, int maxRows )
    {
    	// Create output object
        TableOutput output = new TableOutput( headElms, maxRows, maxCols, tableElement.length );
        
        // Get element content for current dialog form
        String[][]  elementContent = (String[][]) dlgElementsList.get( "elementContent" );
        
        System.arraycopy( elementContent[0], 0, output.header[0], 0, headElms ); // Get text value of head element

        ObservableList items;
    	
        // Loop for each tab
        for ( int t = 0; t < tableElement.length; t++ )
        {
            // Get items for for current table view
            items = (ObservableList) table.get(t).getItems();
            
            numRows = items.size();
            
            // Loop for each row number 
            for ( int i = 0, r = 0; i < numRows; i++ )
                // If there is some value entered in the row
                if ( rowEntered(i, t) )
                {
                    // Loop for each column 
                    for ( int c = 0; c < tableElement[t].length; c++ )
                        if ( tableElement[t][c] != null )
                            // Get current cell text value and store in output object
                            output.table[t][r][c] = (String) getCellValue( c+1, i, t );

                    // Increment non-empty row counter
                    r++;
                }
        }
        
        return output;
    }
    
    /**
     * Adds buttons to the form
     * @param tabPane Tab pane to which add buttons
     */
    private void addTableButtons()
    {
    	// Create array for table button objects
        Button[] btn = new Button[2];
        
        // Create OK button element with Event Handler
        btn[0] = newActionButton( "OK", eventOKButton() );
        
        // Create Cancel button element with Event Handler
        btn[1] = newActionButton( "Cancel ", eventCancelButton() );
        
        // Get dialog's Scene object
        Scene  scene = (Scene) dlgElementsList.get( "scene" );

        // Get Pane from Scene object
        Pane pane = (Pane) scene.getRoot();
    	
    	// Add array with buttons to Pane object
        addButtonsToPane( btn, pane );
        
    } // End of method ** addTableButtons **
    
    /**
     * Event handler for OK button
     * @return Event handler lambda expression
     */
    private EventHandler eventOKButton()
    {
    	return e ->
        {
            // If table elements pass validation
            if ( validateTableElements() ) 
                // If dialog elements passed validation
                if ( tblObj.validateDialogElements() ) 
                {
                    // Get dialog action rode lambda expression
                    DialogAction  code = (DialogAction) dlgElementsList.get( "code" );

                    // If there is another lambda expression to execute specified
                    if ( code != null )
                        // Run that lambda expression
                        code.run( null );

                    // Close dialog window
                    tblObj.close();
                }
        };
    }
    
    /**
     * Event handler for Cancel button
     * @return Event handler as lambda expression
     */
    private EventHandler eventCancelButton()
    {
    	return e -> 
        {
            // Get rid of output information
            dlgElementsList.set( "dialogElement", null );
            
            // Close dialog window
            tblObj.close();
        };
    }
    
    /**
     * Validates table elements
     * @return False if something is wrong, or true otherwise
     */
    private boolean validateTableElements()
    {
        // Loop for each tab
        for ( int t = 0; t < tableElement.length; t++ )
        {
            // Get items for for current table view
        	ObservableList items = (ObservableList) table.get(t).getItems();
        	int numRows = items.size();
        	ElementValidation elVldtn;
        	
            // Loop for each table row
            for ( int i = 0; i < numRows; i++ )
            {
            	int arrSize = arrayCount( tableElement[t] );
                
                // Loop for each table column
                for ( int j = 0; j < arrSize; j++ )
                {
                    // Get text value of current cell
                	String val = (String) getCellValue( j+1, i, t );

                    // If cell value is not specified
                    if ( val == null )
                        val = "";

                    elVldtn = tableElement[t][j].validation;
                    
                    // If there is lambda expression for validating field value and row is not empty
                    if ( elVldtn != null && rowEntered(i, t) )
                        // If the current element does not pass validation
                        if ( !elVldtn.run( val ) )
                            return false;

                } // End for loop ** each table column **
            }                
        } // End for loop ** t **

        return true;

    } // End of method ** validateTableElements **

    /**
     * Gets table cell value
     * @param colNum Column number
     * @param rowNum Row number
     * @param tabNum Tab number
     * @return Table cell value
     */
    @SuppressWarnings("unchecked")
    private Object getCellValue( int colNum, int rowNum, int tabNum )
    {
        // Create Observable List object for table items list
        ObservableList<ArrayList<Data>> items = table.get(tabNum).getItems();

        // Get current row value
        ArrayList<Data> row = (ArrayList<Data>) items.get( rowNum );

        Data cellVal = (Data) row.get(colNum);
        
        // Current cell is specified
        if ( cellVal != null )
            // Return current cell value
            return cellVal.get();
        else
            return null;

    } // End of method ** getCellValue **
    
    /**
     * Checks if there is any info entered in the row
     * @param rowNum Row number
     * @param tabNum Tab number
     * @return True if at least in one column there is some value entered, or false otherwise
     */
    private boolean rowEntered( int rowNum, int tabNum )
    {
        // Count number of non-empty elements as array size
    	int arrSize = arrayCount( tableElement[tabNum] );
        
        // Loop for each table column
        for ( int i = 0; i < arrSize; i++  )
            // If value in the cell is specified
            if ( getCellValue( i, rowNum, tabNum ) != null )
                // Return true because there is at least one cell with specified value
                return true;

        // No specified cells in the row
        return false;

    } // End of method ** rowEntered **
    
    /**
     * Checks if there is any info entered in the row
     * @param row Array containing row information
     * @return True if at least in one column there is some value entered, or false otherwise
     */
    private boolean rowEntered( ArrayList<Data> row )
    {
        // Loop for each row array element
        for ( Data rowVal : row )
            // If value is not empty
            if ( rowVal != null )
                return true;
                
        return false;
        
    } // End of method ** rowEntered **
    
    /**
     * Adds to the form Table elements
     */
    @SuppressWarnings( { "unchecked", "unused", "rawtypes" } )
    private void addTableElements()
    {
        // If no table elements passed or If table elements are not specified  - just exit
        if ( tableElement == null || (tableElement.length < 1 ^ tableElement[0].length < 1) )
            return;

        TableCell cell;     // To hold current table cell		
        int barWidth = 15;	// Set scroll bar width
        
        // Create main arrays: table, elementsList, data
        createMainArrays();
	
        // Create associative list for current tab
        AssociativeList elList = new AssociativeList();
        // Set this object as TableDialog
        elList.set( "TableDialog", this );
        
        // Create Tab pane object
        TabPane tabPane = createTabPane(); 

        // Loop for each table tab
        for ( int i = 0; i < tableElement.length; i++ )
        {
            // If array of Tabs is specified
        	if ( tabs != null && tabs.length > 0 )
        		// Set current tab as Tab
                elList.set( "Tab", tabs[i] );
            
        	// Add TableView for current Tab 
            addTableView( i, elList );
            
            // Create Tab object for current table and Add current tab to the tab pane
            tabPane.getTabs().add( createTab(i) );
           
        } // End of loop for each table tab
		
        // Add TabPane to the form
        addTabPane( tabPane );

    } // End of method ** addTableElements **
    
    /**
     * Adds TableView to the form
     * @param tabNum Tab number
     * @param elList Elements List
     */
    private void addTableView( int tabNum, AssociativeList elList )
    {
    	// Get TableView object for current tab
        TableView<ArrayList<Data>> curTable = createTableView(tabNum);
    
        // Get width of all columns
        int columnsWidth = calculateColumnsWidth(tabNum);
        
        // Add empty column to to current table
        addEmptyColumn( curTable, columnsWidth );

        // Get the number of columns
        int cols = arrayCount( tableElement[tabNum] );

        // Create Associative list for current Tab
        elementsList[tabNum] = new AssociativeList[cols];
         
        // Loop for each table column
        for ( int j = 0; j < cols; j++ )
        {
        	// Create associative list for current column
            elementsList[tabNum][j] = new AssociativeList();

            // Add table column for current Table and table element
            addTableColumn( curTable, tableElement[tabNum][j], elList, columnsWidth );
            
        } // End of Loop  ** for each table column **
		
        // Add Control Button column for current Table
        addCtrlBtnColumn( curTable, columnsWidth );
    }
    
    /**
     * Adds empty column to TableView
     * @param tbl TableView
     * @param columnsWidth Width of all columns
     */
    private void addEmptyColumn( TableView tbl, int columnsWidth )
    {
    	// Add empty column to to current table
        TableColumn emptyColumn = new TableColumn( "" );
        
        // Bind width of current column to width of TableView
        emptyColumn.prefWidthProperty().bind( tbl.widthProperty().multiply( (double) 8/columnsWidth ) );
        
        tbl.getColumns().add( emptyColumn );
    }
    	
    /**
     * Adds column to specified TableView
     * @param tbl TableView
     * @param tblEl Table element
     * @param elList Elements List
     * @param columnsWidth Width of all columns
     */
    private void addTableColumn( TableView tbl, TableElement tblEl, AssociativeList elList, int columnsWidth )
    {
    	// Create new column
        TableColumn column = createTableColumn( tblEl, elList );

        // Set preferred width with value passed to the form
        column.prefWidthProperty().bind( tbl.widthProperty().multiply( (double) tblEl.width/columnsWidth  ) );

        // Add column to to current table
        tbl.getColumns().add( column );
    }
    
    /**
     * Adds Control Button column to TableView
     * @param tbl TableView
     * @param columnsWidth
     */
    private void addCtrlBtnColumn( TableView tbl, int columnsWidth )
    {
    	// Create Ctrl Button column
        TableColumn column = createCtrlBtnColumn();

        // Set preferred width with value passed to the form proportioned to all columns width
        column.prefWidthProperty().bind( tbl.widthProperty().multiply( (double) ctrlBtnWidth/columnsWidth  ) );

        // Add column to to current table
        tbl.getColumns().add( column );

        // Set height for current table
        tbl.setPrefHeight( Math.min( numRows * 33, PREF_HEIGHT ));

        // Highlight 1st table row
        tbl.getSelectionModel().select(0);
    }
    
    /**
     * Adds TabPane to the form
     * @param tabPane TabPane to add
     */
    private void addTabPane( TabPane tabPane )
    {
    	// Get current Scene object
        Scene  scene = (Scene) dlgElementsList.get( "scene" );
        
        // Get pane from scene object
        Pane pane = (Pane) scene.getRoot();

        // If there is more than one table
        if ( table.size() > 1 )
            // Add tab pane to pane
            pane.getChildren().add( tabPane );
        else
            // Add current table to pane
            pane.getChildren().add( table.get(0) );
    }
    
    /**
     * Creates TableView column
     * @param tblEl Table element for the column
     * @param elList Elements list
     * @return TableColumn object
     */
    private TableColumn createTableColumn( TableElement tblEl, AssociativeList elList )
    {
    	// Create new column
        TableColumn column = new TableColumn( tblEl.columnName );

        // Get current table element column name and store it as label name
        final String labelName = tblEl.columnName;
        
        ArrayList<String> initVal = new ArrayList<>();
        String[] elTxtVal = tblEl.textValue;
        
        // If there is text value specified for column rows
        if ( elTxtVal != null && elTxtVal.length > 0 )
        	// Loop for text values for each row
            for ( int v = 0; v < elTxtVal.length; v++ )
            	// Add text value to ArrayList for initialization
                initVal.add( elTxtVal[v] );
        
        // Create data element for current column
        elList.set( column.getText(), initVal );
        
        // Create Table element
        createTableElement( column, tblEl, elList );
        
        // Set event handlers for current column
        setEditEventHandlers( column, tblEl, elList );

        return column;
    }
    
    /**
     * Creates Table Element
     * @param column Table column in which to create field specified in Table element
     * @param tblEl Table element with parameters for column fields
     * @param elList Elements List
     */
    private void createTableElement( TableColumn column, TableElement tblEl, AssociativeList elList )
    {
    	String elValType = tblEl.valueType;
        
    	// If List reference  or text choices are passed
        if ( tblEl.textChoices.length > 0 || tblEl.list != null )
            createComboBox( column, tblEl, elList );
        
        // If field should contain a date
        else if ( elValType.contains( "Date" ) )
            createDateDialog( column, tblEl, elList );
		
        // If field should contain File type field
        else if ( elValType.contains( "File" ) )
            createFileChooser( column, tblEl, elList );

        // If field should contain Tree List type field
        else if ( elValType.contains( "Tree" ) )
            createTreeList( column, tblEl, elList );
        
        // If field should contain Analytical Dimensions field
        else if ( elValType.contains( "Dimension" ) )
            createDimensions( column, tblEl, elList );
        
        // Otherwise - it's a text field    
        else
            createTextField( column, tblEl, elList );
    }
    
    /**
     * Creates Tab Pane object
     * @return Created Tab Pane
     */
    private TabPane createTabPane()
    {
        TabPane tabPane = new TabPane();
        
        // Set preferred height for Tab Pane
        tabPane.setPrefHeight( PREF_HEIGHT );

        // Make closing tab unavailable
        tabPane.setTabClosingPolicy( TabPane.TabClosingPolicy.UNAVAILABLE );

        return tabPane;
        
    } // End of method ** createTabPane **

    /**
     * Creates main arrays for table dialog
     */
    private void createMainArrays()
    {
        // Maximum columns in tables
        maxCols = 0;

        // Loop for each table element
        for ( TableElement[] tblEl : tableElement ) 
            // If table length is more than maximum column width
            if ( tblEl.length > maxCols ) 
                // Save table length to maximum column width
                maxCols = tblEl.length;
        
        int numTabs = tableElement.length;
        
        // Create AssociativeList for Elements List 
        elementsList = new AssociativeList[numTabs][];
        
        // Create ArrayList for TableViews of each Tab
        table = new ArrayList<>( numTabs );
        
        // Create ArrayList for data Models for each TableView
        data = new ArrayList<>( numTabs );
        
    } // End of method ** createMainArrays **
    
    /**
     * Updates table content after changes
     * @param tabNum Tab number on which table located
     */
    public void updateTableView( TableView<ArrayList<Data>> tbl )
    {
        int tabNum = table.indexOf( tbl );
        
        // Update Table View 
        tbl.setItems( null );
        tbl.layout();
        tbl.setItems( data.get(tabNum) );        
    }
    
    /**
     * Creates TableView object
     * @param tabNum Tab number where to create TableView object
     */
    private TableView<ArrayList<Data>> createTableView( int tabNum )
    {
        // Create array matrix for data model
        ArrayList<ArrayList<Data>> matrix = createMatrix( tabNum );
        
        // Create data model array from created Data array
        data.add( tabNum, FXCollections.observableList( (List) matrix ) );
        
        // Create TableView object
        TableView<ArrayList<Data>> newTable = new TableView<>( data.get(tabNum) );
        newTable.setEditable( true );
        
        // Place holder for empty cells
        newTable.setPlaceholder( new Label("") );
        // Add new table for current tab
        table.add( tabNum, newTable );
        
        // Create associative list array for elements list
        elementsList[tabNum] = new AssociativeList[numRows];
        
        return newTable;
    
    } // End of method ** createTableView **
    
    /**
     * Creates Matrix Data Model for TableView
     * @param tabNum Tab number
     * @return Matrix with Data Model
     */
    private ArrayList<ArrayList<Data>> createMatrix( int tabNum )
    {
    	// Array of data values for each row
        ArrayList<Data>[] rows = new ArrayList[numRows];         
        
        // Get the number of columns for current table
        int numCol = tableElement[tabNum].length;
        
        // Loop for each table row
        for ( int r = 0; r < numRows; r++ )
            // Create ArrayList for current row
        	rows[r] = addEmptyDataRow( numCol+1 );
        
        // Loop for each column
        for ( int i = 1; i <= numCol; i++ )
        {
            // If current table element is not specified
            if ( tableElement[tabNum][i-1] == null )
                continue;

            // Get array of text values specified for current table element
            String[] textValue = tableElement[tabNum][i-1].textValue;

            // If the array for text values is not specified
            if ( textValue == null || textValue.length == 0 )
                continue;
            
            // For each row of current column
            for ( int j = 0; j < textValue.length; j++ )
            	rows[j].set( i, Data.create( textValue[j] ) );
            
        } // End of For loop for each table element
        
        // Create Data matrix and return
        return dataMatrix( rows );
    }
    
    /**
     * Creates Matrix for Data Model
     * @param rows
     * @return
     */
    private ArrayList<ArrayList<Data>> dataMatrix( ArrayList<Data>[] rows )
    {
    	// Create ArrayList for Data Models of each TableView
    	ArrayList<ArrayList<Data>> matrix = new ArrayList<>();
        
        // Loop for each row of Data for Data Model
    	for ( ArrayList<Data> row : rows )
    		// Add row to matrix
        	matrix.add( row );
        
        return matrix;
    }
    
    /**
     * Adds empty Data row
     * @param numCol Number of columns
     * @return Row with empty Data elements
     */
    private ArrayList<Data> addEmptyDataRow( int numCol )
    {
    	// Create ArrayList for current row
    	ArrayList<Data> row = new ArrayList<>(numCol);
        
        // Fill current row with null values
        for ( int k = 0; k < numCol; k++ )
        	row.add( null );
        
        return row;
    }
    
    /**
     * Calculates the width of all columns
     * @param tabNum Tab number on which table is located
     * @return  The width of all columns
     */
    private int calculateColumnsWidth( int tabNum )
    {
        int barWidth = 8; // Set scroll bar width
        int columnsWidth =  barWidth + ctrlBtnWidth; // For empty column and  control button column
        
        // Get the number of columns for table in specified tab
        int cols = arrayCount( tableElement[tabNum] );
        
        // Calculate width of all table columns for current tab
        for ( int j = 0; j < cols; j++ )
            columnsWidth += tableElement[tabNum][j].width;		

        // Add additional pixels for column separators
        columnsWidth += tableElement[tabNum].length + 2;  

        // Get items for for current table view
        ObservableList items = (ObservableList) table.get(tabNum).getItems();
	
        // If there are more items in table than rows displayed
        if ( items.size() >= numRows )
            // Add additionally scroll bar width
            columnsWidth += barWidth;
                
        return columnsWidth;
        
    } // End of method ** calculateColumnsWidth **
    
    /**
     * Creates ComboBox object in specified column
     * @param column Column in which to create ComboBox object
     * @param tblEl Table element with ComboBox parameters
     * @param elList Associative list with table elements values
     */
    private void createComboBox( TableColumn column, TableElement tblEl, AssociativeList elList )
    {
    	String[] textChoices = null; // Array for text choices
		LinkedHashSet tblLst = tblEl.list;
    	
        // If there are choices specified for the current element
        if ( tblEl.textChoices.length > 0 )
            textChoices = tblEl.textChoices;

        // If List reference is passed
        else if ( tblLst != null )
        {
            Item itm;
            
            // Create string array for text choices
            textChoices = new String[ tblLst.size() ];
            
            Iterator it = tblLst.iterator();
            int p = 0;
            
            // Fill array of text choices with text values
            while ( it.hasNext() )
            {
                itm = (Item) it.next();
                
                // Convert Item to String and add to array of text choices
                textChoices[p++] = itm .toString();
            }
        }
		
        // Create Cell Factory for Combo Box
        Callback cellFactory = comboBoxCellFactory( column, tblEl, elList, textChoices );
        
        // Set cell factory for current column
        column.setCellFactory( cellFactory );
    
    } // End of method ** createComboBox **
	
    /**
     * Cell Factory for Combo Box
     * @param column Column for ComboBox
     * @param tblEl TableElement with parameters for ComboBox
     * @param elList Elements List
     * @param choices List of choices for ComboBox
     * @return Cell Factory as Lambda expression
     */
    private Callback comboBoxCellFactory( TableColumn column, TableElement tblEl, AssociativeList elList, String[] choices )
    {
    	return (e) -> 
        { 
            // Get TableView when user clicks a cell
        	TableView tbl =  ((TableColumn) e).getTableView();
            
            // Get items for for current table view
            ObservableList tblItems = (ObservableList) tbl.getItems();
	    
            // Create combo box object for list of choices 
            // and define anonymous class overriding ComboBoxTableCell
            ComboBoxTableCell comboBox = createComboBoxTableCell( column, tblEl, choices, elList, tblItems );
            
            // Set editable attribute for combo box
            comboBox.setComboBoxEditable( tblEl.editable );
            
            // Return created combo box
            return comboBox;	
        };
    }
    
    /**
     * Runs OnChange lambda expression
     * @param tblEl Table Element
     * @param elList Elements List
     */
    private void runOnChange( TableElement tblEl, AssociativeList elList )
    {
    	// If there is action to be performed on element change
        if ( tblEl.onChange != null )
            // Invoke lambda expression when element changes
            tblEl.onChange.run( elList );	
    }
    
    /**
     * Creates custom ComboBox TableCell
     * @param column Table column
     * @param tblEl Table element
     * @param choices Array of choices
     * @param elList Elements list
     * @param tblItems Table Items
     * @return Custom ComboBox TableCell
     */
    private ComboBoxTableCell createComboBoxTableCell( TableColumn column, TableElement tblEl, String[] choices, AssociativeList elList, ObservableList tblItems )
    {
    	return new ComboBoxTableCell( choices )
        {
            // Gets called when cell item is updated
            @Override
            public void updateItem( Object item, boolean empty )
            {
                // Invoke parent class method
                super.updateItem( item, empty );

                // If cell is empty
                if ( !empty )
                	updateComboBoxItem( this, column, tblItems, elList );
                	
                // If item is specified 
                if ( item != null && !empty )
                	runOnChange( tblEl, elList );
                
            } // End of method ** updateItem **
			
            // Gets called when end-user starts editing table cell
            @Override
            public void startEdit()
            {
                // Create cell text label
                setText( null );
                // Invoke parent class method
                super.startEdit();

                ArrayList<String> filteredChoices = elList.get( column.getText() );
                        
             } // End of method ** startEdit **
            
        }; // End of ** anonymous class overriding ComboBoxTableCell **
    }
    
    /**
     * Update ComboBox Item
     * @param cell Cell in which ComboBox is located
     * @param column Column of ComboBox
     * @param tblItems List of Table Items
     * @param elList Elements List
     */
    private void updateComboBoxItem( ComboBoxTableCell cell, TableColumn column, ObservableList tblItems, AssociativeList elList )
    {
    	// Get column number from table view
        int colNum = cell.getTableView().getColumns().indexOf( cell.getTableColumn() );
        // Get current row number
        int rowNum = cell.getTableRow().getIndex();
            
        ArrayList<Data> row = getDataRow( rowNum, cell, tblItems );
        
        String cellVal = "";
        
        // If column number is specified and cell model value is not empty
        if ( colNum != -1 && row != null && row.size() > 0 && row.get(colNum) != null  )
            // Get cell model value to set text to that value 
            cellVal = (String) row.get(colNum).get();
        
        cell.setText( cellVal );
        
        // If Cell value is defined and not empty
        if ( cellVal != null && !cellVal.isEmpty() )
        {   
            // Get element value for current column
        	ArrayList<String> attr = elList.get( column.getText() );
            
        	setCellAttr( attr, rowNum, cellVal );
        } 
    }
    
    /**
     * Sets Cell's attribute
     * @param attr ArrayList of attributes for the cell
     * @param rowNum Row number
     * @param txtVal Text value of attribute
     */
    private void setCellAttr( ArrayList<String> attr, int rowNum, String txtVal )
    {
    	// If current column element value is not defined
        if ( attr == null )
        	// Create there empty ArrayList
            attr = new ArrayList<>();
        
        // If current row number is greater than size of Column ArrayList
        if ( rowNum >= attr.size() )
        	// Add Cell value to ArrayList
            attr.add( txtVal );
        else
        	// Set Cell value for current row
            attr.set( rowNum, txtVal );
    }
    
    /**
     * Gets Data row for table view items
     * @param rowNum Row number
     * @param cell Current cell
     * @param tblItems Table items of table view
     * @return ArrayList with Data items of specified row
     */
    private ArrayList<Data> getDataRow( int rowNum, ComboBoxTableCell cell, ObservableList tblItems )
    {
    	ArrayList<Data> row;
        
        // If row number is specified
        if ( rowNum >= 0 )
            // Get row from data model
            row = (ArrayList<Data>) tblItems.get( rowNum );
        else
            // Get row from table view
            row = (ArrayList<Data>) cell.getTableRow().getItem();
        
        return row;
    }
    
    /**
     * Creates DatePicker object for specified column
     * @param column Column in which to create DatePicker object
     * @param tblEl Table element with DatePicker parameters
     * @param elList Associative list with table elements values
     */
    private void createDatePicker( TableColumn column, TableElement tblEl, AssociativeList elList )
    {
        column.setCellFactory( e -> 
        { 
            TextFieldTableCell<ArrayList<Data>, String> datePicker = new TextFieldTableCell<ArrayList<Data>, String>()
            {
                @Override
                public void startEdit()
                {
                	startEditDatePicker( this );
                }
                
                @Override
                // Gets called when cell item is updated
                public void updateItem( String item, boolean empty )
                {
                    // Invoke parent class method
                    super.updateItem( item, empty );
					
                    // If item is not empty
                    if ( !empty )
                    	updateDatePickerItem( this );
                    
                    // If item is specified 
                    if ( item != null && !empty )
                    	runOnChange( tblEl, elList );
                    
                } // End of method ** updateItem **
            };
            // Create data element for current column
            elList.set( column.getText(), datePicker );

            // Return created text field
            return datePicker;	
        } );
    } // End of method ** createDatePicker **
	
    /**
     * Updates not empty DatePicker Item
     * @param cell Cell with DatePicker
     */
    private void updateDatePickerItem( TextFieldTableCell<ArrayList<Data>, String> cell )
    {
    	// Get row from table view
        ArrayList<Data> row = (ArrayList<Data>) cell.getTableRow().getItem();
        // Get column number from table view
        int colNum = cell.getTableView().getColumns().indexOf( cell.getTableColumn() );

        // If column number is specified and cell model value is not empty
        if ( colNum != -1 && row != null && row.size() > 0 && row.get(colNum) != null  )
        {
            // Create new DatePicker object
            DatePicker datePicker = new DatePicker();

            // Set value of created DatePicker to what is stored in current data model cell
            datePicker.setValue( LocalDate.parse( (String) row.get(colNum).get() ) );
            
            // Set cell graphic to created DatePicker
            cell.setText( (String) row.get(colNum).get() );
        }
        else
        	cell.setGraphic( null );
    }
    
    /**
     * Starts editing DatePicker item
     * @param cell Cell with DatePicker
     */
    private void startEditDatePicker( TextFieldTableCell<ArrayList<Data>, String> cell )
    {
    	// Create date picker object and invoke DatePicker window
        DatePicker date = new DatePicker();
        // Set entered date in the cell as cell graphic
        cell.setGraphic( date ); 
        
        // Get row of Data Model for current cell
        ArrayList<Data> row = (ArrayList<Data>) cell.getTableRow().getItem();
            
        // Get column number from table view
        int colNum = cell.getTableView().getColumns().indexOf( cell.getTableColumn() );
        
        // Get entered value of the date
        Object val = date.getValue();
        String dateVal = "";
        
        // If value is specified
        if ( val != null )
            // Convert value to string and save to element content array
            dateVal = val.toString();
        
        // Set text representation of date in the cell
        cell.setText( dateVal );
        
        // Set Data object for entered date in current row
        row.set( colNum, Data.create(dateVal) );
    }
    
    /**
     * Creates FileChooser object in specified column
     * @param column Column in which to create FileChooser object
     * @param tblEl Table element with FileChooser parameters
     * @param elList Associative list with table elements values
     */
    private void createFileChooser( TableColumn column, TableElement tblEl, AssociativeList elList )
    {
        // Set column cell factory
        column.setCellFactory( e -> 
        { 
            // Create file picker object with anonymous class extending TableCell
            TableCell filePicker = new TableCell()
            {
            	@Override  
                // Gets invoked when user starts editing table cell
                public void startEdit() 
                {  
            		startEditFileChooser( this );
            	}
				
                @Override
                // Gets called when cell item is updated
                public void updateItem( Object item, boolean empty )
                {
                    // Invoke parent class method
                    super.updateItem( item, empty );
					
                    // If item is not empty
                    if ( !empty  )
                    	updateFileChooserItem( this );
                    	
                    else
                        setGraphic( null );

                    // If item is specified 
                    if ( item != null && !empty )
                    	runOnChange( tblEl, elList );
                    
                } // End of method ** updateItem **
                
            }; // End of ** file picker object with anonymous class extending TableCell **

            // Set file picker Editable attribute
            filePicker.setEditable( tblEl.editable );

            // Create data element for current column
            elList.set( column.getText(), filePicker );

            // Return created file picker
            return filePicker;
            
        } ); // End of ** column cell factory **
    
    } // End of method ** createFileChooser **
	
    /**
     * Updates File Chooser Item in Table Cell
     * @param cell Table Cell
     */
    private void updateFileChooserItem( TableCell cell )
    {
    	// Get current table view row
        ArrayList<Data> row = (ArrayList<Data>) cell.getTableRow().getItem();
        
        // Get current column number
        int colNum = cell.getTableView().getColumns().indexOf( cell.getTableColumn() );

        // If column number is specified and cell model value is not empty
        if ( colNum != -1 && row != null && row.size() > 0 && row.get(colNum) != null && !((String) row.get(colNum).get()).isEmpty() )
        {
            // Create text field object
            TextField textField = createTextField();

            // Create button object
            Button btn = createFileChooserBtn( textField );

            // Create horizontal box for file field
            HBox fileField = new HBox(); 
            textField.setPrefHeight(20);
            // Add text field and button to file field box
            fileField.getChildren().addAll( textField, btn );

            // Set value of created text field to what is stored in current data model cell
            textField.setText( (String) row.get(colNum).get() );

            // Set cell graphic to what is in file field
            cell.setGraphic( fileField );
        }
        else
        	cell.setGraphic( null );
    }
    
    /**
     * Gets invoked when user starts editing File Chooser Item
     * @param cell Table Cell
     */
    private void startEditFileChooser( TableCell cell )
    {
    	// Create text field object
        TextField textField = createTextField();

        // Create button object
        Button btn = createFileChooserBtn( textField );

        // Create horizontal box for file field
        HBox fileField = new HBox(); 
        
        // Add text field and button to file field box
        fileField.getChildren().addAll( textField, btn );

        // Set file field as graphic to display
        cell.setGraphic( fileField ); 
    }
    
    /**
     * Creates TextField object
     * @return Created object
     */
    private TextField createTextField()
    {
        // Create text field object
        TextField textField = new TextField();
        
        // Set width of text field
        textField.setPrefWidth( PREF_WIDTH );
        
        // Set height of text field
        textField.setPrefHeight(20);
        
        // Make text field non-editable
        textField.setEditable( false );
        
        // Set style for Text Field
        textField.setStyle("-fx-border-width: 0 0 0 0;"
                          +"-fx-padding: 3;");
        return textField;
    
    } // End of method ** createTextField **
	
    /**
     * Creates FileChooser Button object
     * @param textField Text field where to place selected file
     * @return Created button object
     */
    private Button createFileChooserBtn( TextField textField )
    {
        // Create button object to select file
        Button btn = new Button( "..." );
        btn.setPrefWidth( 15 );
        btn.setPrefHeight( 20 );
        btn.setStyle( "-fx-padding: 2;" );

        // Set event handler for press button event
        btn.setOnAction( fileChooserBtnEvent(textField) );

        return btn;
        
    } // End of method ** createFileChooserBtn **
	
    /**
     * Creates File Chooser Button Event Handler
     * @param textField Text Field for the button
     * @return Event Handler
     */
    private EventHandler fileChooserBtnEvent( TextField textField )
    {
    	// Create file chooser object
        FileChooser fileChooser = new FileChooser();
        // Set title for file chooser window
        fileChooser.setTitle( "Select file" );

        return e -> 
        {
            // Display Dialog window for file chooser
            File file = fileChooser.showOpenDialog( stage );

            // If file is specified
            if ( file != null )
                // Set full file path as text field text
                textField.setText( file.getPath() );
        };
    }
    
    /**
     * Creates TreeList object in specified column
     * @param column Column in which to create TreeList object
     * @param tblEl Table element with TreeList parameters
     * @param elList Associative list with table elements values
     */
    private void createTreeList( TableColumn column, TableElement tblEl, AssociativeList elList )
    {
        // Set column cell factory
        column.setCellFactory( e -> 
        { 
            // Create Tree List object with anonymous class extending TableCell
            TableCell treeList = new TableCell()
            {
                // Gets invoked when user starts editing table cell
                @Override
                public void startEdit() 
                {  
                	startEditTreeList( this, tblEl, elList );
                }
				
                // Gets called when cell item is updated
                @Override
                public void updateItem( Object item, boolean empty )
                {
                    // Invoke parent class method
                    super.updateItem( item, empty );

                    // If item is not empty
                    if ( !empty )
                    	updateTreeListItem( this, tblEl, elList );
                    else
                        setGraphic( null );

                    // If item is specified 
                    if ( item != null && !empty )
                    	runOnChange( tblEl, elList );
                
                } // End of method ** updateItem **
                
            }; // End of ** Tree List object with anonymous class extending TableCell **
			
            // Set Tree List Editable attribute
            treeList.setEditable( tblEl.editable );

            // Create data element for current column
            elList.set( column.getText(), treeList );

            // Return created Tree List
            return treeList;
            
        } ); // End of ** column cell factory **
    
    } // End of method ** createTreeList **

    /**
     * Updates TreeList Item in Table Cell
     * @param cell Table Cell
     * @param tblEl Table Element with parameters
     * @param elList Elements List
     */
    private void updateTreeListItem( TableCell cell, TableElement tblEl, AssociativeList elList )
    {
    	// Get current TableView row
    	ArrayList<Data> row = (ArrayList<Data>) cell.getTableRow().getItem();
        // Get current column number
    	int colNum = cell.getTableView().getColumns().indexOf( cell.getTableColumn() );

        // If column number is specified and cell model value is not empty
        if ( colNum != -1 && row.get(colNum) != null && !((String) row.get(colNum).get()).isEmpty() )
        {
            // Create text field object
            TextField textField = createTextField();

            // Create button object
            Button btn = createTreeListBtn( textField, tblEl, elList );

            // Create horizontal box for tree list field
            HBox treeListField = new HBox(); 
            textField.setPrefHeight(20);
            // Add text field and button to tree list field box
            treeListField.getChildren().addAll( textField, btn );

            // Set text field to what is stored in data model cell
            textField.setText( (String) row.get(colNum).get() );

            // Set cell graphic to created tree list field
            cell.setGraphic( treeListField );
        }
        else
        	cell.setGraphic( null );
    }
    
    /**
     * Gets invoked when user starts editing  TreeList Item
     * @param cell Table Cell with TreeList
     * @param tblEl Table Element with parameters
     * @param elList Elements List
     */
    private void startEditTreeList( TableCell cell, TableElement tblEl, AssociativeList elList )
    {
    	// Create text field object
        TextField textField = createTextField();

        // Create button object
        Button btn = createTreeListBtn( textField, tblEl, elList );

        // Create horizontal box for tree list field
        HBox treeListField = new HBox(); 
        
        // Add text field and button to tree list box
        treeListField.getChildren().addAll( textField, btn );

        // Set tree list field as graphic to display
        cell.setGraphic( treeListField ); 
    }
    
    /**
     * Creates TreeList select Button object
     * @param textField Text field in which selected tree element will be placed
     * @param tblEl Table element with TreeList parameters
     * @param elList Associative list with table elements values
     * @return Created select Button object
     */
    private Button createTreeListBtn( TextField textField, TableElement tblEl, AssociativeList elList )
    {
        // Create button object to select from tree list
        Button btn = new Button( "..." );
        btn.setPrefWidth( 15 );
        btn.setPrefHeight( 20 );
        btn.setStyle( "-fx-padding: 2;" );

        // Set event handler for press button event
        btn.setOnAction( treeListBtnEventHandler( btn, textField, tblEl ) );
        
        // Return created button
        return btn;
    
    } // End of method ** createTreeListBtn **
	
    /**
     * Event Handler for TreeList Button
     * @param btn Button object
     * @param textField Text field for the TreeList
     * @param tblEl Table Element with parameters for TreeList
     * @return EventHadler object
     */
    private EventHandler treeListBtnEventHandler( Button btn, TextField textField, TableElement tblEl )
    {
    	return e -> 
        {
        	TreeDialog obj = null; // To store created TreeDialog object

            // If value type is specified
            if ( !tblEl.valueType.isEmpty() )
            {
                // Create class object by its name
                Class cls = createModelClass( tblEl.valueType );
                    
                try 
                {
                    Constructor cnstr; // To store class constructor object

                    // If tree width is specified
                    if ( tblEl.width > 0 )
                    {
                        // Get class constructor with parameters stage and width
                        cnstr = cls.getConstructor( Stage.class, int.class );
                        // Create TreeDialog object with class constructor
                        obj = (TreeDialog) cnstr.newInstance( stage, tblEl.width );	
                    }
                    else
                    {
                        // Get class constructor with parameter stage
                        cnstr = cls.getConstructor( Stage.class );
                        // Create TreeDialog object with class constructor
                        obj = (TreeDialog) cnstr.newInstance( stage );
                    }
                } 
                catch ( Exception ex ) {}
            }      
         
            // If object was created
            if ( obj != null )
            {
                // Display dialog form and get the result of submit button pressed
                String result = obj.result( btn );

                // If something was selected
                if ( result != null && !result.isEmpty() )
                    // Save full file path to text field
                    textField.setText( result );	
            }
        };
    }
    
    /**
     * Creates Date dialogue form in a specified column
     * @param column Column in which to create TreeList object
     * @param tblEl Table element with TreeList parameters
     * @param elList Associative list with table elements values
     */
    private void createDateDialog( TableColumn column, TableElement tblEl, AssociativeList elList )
    {
        column.setCellFactory( e -> 
        { 
            TextFieldTableCell<ArrayList<Data>, String> datePicker = new TextFieldTableCell<ArrayList<Data>, String>()
            {
                @Override
                public void startEdit()
                {
                	startEditDateDialog( this );
                }
                
                @Override
                // Gets called when cell item is updated
                public void updateItem( String item, boolean empty )
                {
                    // Invoke parent class method
                    super.updateItem( item, empty );

                    // If table cell is not empty
                    if ( !empty )
                    	updateDateDialogItem( this );
                    
                    // If item is specified 
                    if ( item != null && !empty )
                    	runOnChange( tblEl, elList );	
                    
                } // End of ** method updateItem **
            };
            // Create data element for current column
            elList.set( column.getText(), datePicker );

            // Return created text field
            return datePicker;	
        } );
    }
    
    /**
     * Gets invoked when Date dialog item starts editing
     * @param cell Table cell with Date dialog item
     */
    private void startEditDateDialog( TextFieldTableCell cell )
    {
    	// Get data model for current row 
        ArrayList<Data> row = (ArrayList<Data>) cell.getTableRow().getItem();

        // Get column number from table view
        int colNum = cell.getTableView().getColumns().indexOf( cell.getTableColumn() );
        
        Data cellVal = row.get(colNum);
        
        String initVal = ( cellVal != null ? (String) cellVal.get() : "" );
            
        String result = new DateDialog( stage, initVal ).result( cell );

        if ( result != null )
        {
        	cell.setText( result );
            
            row.set( colNum, Data.create( result ) );
        }
    }
    
    /**
     * Gets invoked when Table cell with Date dialog item is updated
     * @param cell Table cell
     */
    private void updateDateDialogItem( TextFieldTableCell cell )
    {
    	// Get data model for current row 
        ArrayList<Data> row = (ArrayList<Data>) cell.getTableRow().getItem();
        
        // Get column number from table view
        int colNum = cell.getTableView().getColumns().indexOf( cell.getTableColumn() );

        // If column number is specified and data model cell is not empty
        if ( colNum != -1 && row != null && row.size() > 0 && row.get(colNum) != null  )
        	cell.setText( (String) row.get(colNum).get() );
        else
        	cell.setText( "" );
    }
    
    /**
     * Creates Dimensions dialogue form in a specified column
     * @param column Column in which to create TreeList object
     * @param tblEl Table element with TreeList parameters
     * @param elList Associative list with table elements values
     */
    private void createDimensions( TableColumn column, TableElement tblEl, AssociativeList elList )
    {
        column.setCellFactory( e -> 
        { 
            TextFieldTableCell<ArrayList<Data>, String> dimensions;
            
            dimensions = new TextFieldTableCell<ArrayList<Data>, String>()
            {
                @Override
                public void startEdit()
                {
                	startEditDimensions( this, column, elList );
                }
                
                @Override
                // Gets called when cell item is updated
                public void updateItem( String item, boolean empty )
                {
                    // Invoke parent class method
                    super.updateItem( item, empty );

                    // If table cell is not empty
                    if ( !empty )
                    	updateDimensionsItem( this );
                    
                    // If item is specified 
                    if ( item != null && !empty )
                    	runOnChange( tblEl, elList );
                    
                } // End of method ** updateItem **
            };
            
            // Return created text field
            return dimensions;	
        } );
    }
    
    /**
     * Gets invoked when Table cell with Dimensions item starts editing
     * @param cell Table cell
     * @param column Table column
     * @param elList Elements List
     */
    private void startEditDimensions( TextFieldTableCell cell, TableColumn column, AssociativeList elList )
    {
    	int index = cell.getTableRow().getIndex();
        
        ArrayList<String> dimsAttr = (ArrayList<String>) elList.get( column.getText() ); 
        ArrayList<String> acctAttr = (ArrayList<String>) elList.get( hash("GlAcc") );
        
        String initVal = "", accnt = "";
        GLAcctModel account;
        
        if ( dimsAttr.size() > 0 && index < dimsAttr.size() )
            initVal = dimsAttr.get( index );
       
        if ( acctAttr.size() > 0 && index < acctAttr.size() )
            accnt = acctAttr.get( index );
        
        String tab = (String) elList.get( "Tab" );
        
        if ( tab != null && !tab.isEmpty() )
            account = GLAcctModel.getByCode( accnt, tab );
        else
            account = GLAcctModel.getByCode( accnt );

        String[] result = new DynamicDimensions( account, stage, initVal ).result( cell );

        if ( result != null && result.length > 0 )
        	setDisplString( cell, result, dimsAttr );
    }
    
    /**
     * Set Display String for Dimensions item
     * @param cell Table cell
     * @param arr String array to display
     * @param dimsAttr Dimensions Attribute value
     */
    private void setDisplString( TextFieldTableCell cell, String[] arr, ArrayList<String> dimsAttr )
    {
    	String displStr = "";

        for ( int i = 0; i < arr.length; i++ )
            displStr += arr[i] + ( i < arr.length-1 ? "/" : "" );

        cell.setText( displStr );
        
        ArrayList<Data> row = (ArrayList<Data>) cell.getTableRow().getItem();
        
        // Get column number from table view
        int colNum = cell.getTableView().getColumns().indexOf( cell.getTableColumn() );

        row.set( colNum, Data.create(displStr) );
        
        if ( dimsAttr == null )
            dimsAttr = new ArrayList<>();

        int index = cell.getTableRow().getIndex();
        
        if ( index >= dimsAttr.size() )
            dimsAttr.add( displStr );
        else
            dimsAttr.set( index, displStr );
    }
    
    /**
     * Gets invoked when Table cell Dimensions item is updated
     * @param cell Table cell
     */
    private void updateDimensionsItem( TextFieldTableCell cell )
    {
    	// Get data model for current row 
        ArrayList<Data> row = (ArrayList<Data>) cell.getTableRow().getItem();
        
        // Get column number from table view
        int colNum = cell.getTableView().getColumns().indexOf( cell.getTableColumn() );

        // If column number is specified and data model cell is not empty
        if ( colNum != -1 && row != null && row.size() > 0 && row.get(colNum) != null  )
        	cell.setText( (String) row.get(colNum).get() );
        else
        	cell.setText( "" );
    }
    
    /**
     * Creates TextField object in specified column
     * @param column Column in which to create TextField object
     * @param tblEl Table element with TextField parameters
     * @param elList Associative list with table elements values
     */
    private void createTextField( TableColumn column, TableElement tblEl, AssociativeList elList )
    {
        // If the field should contain a number
        if ( tblEl.valueType.contains( "Number" ) )
            // Set column alignment to the right
            column.setStyle( "-fx-alignment: center-right;" );
        
        // Set current column Cell Factory
        column.setCellFactory( event -> 
        { 
            // Create text field object
            // and define anonymous class overriding TextFieldTableCell
            TextFieldTableCell<ArrayList<Data>, String> textField = new TextFieldTableCell<ArrayList<Data>, String>()
            {
                @Override
                // Gets called when cell item is updated
                public void updateItem( String item, boolean empty )
                {
                    // Invoke parent class method
                    super.updateItem( item, empty );

                    // If table cell is not empty
                    if ( !empty )
                    	updateTextFieldItem( this, column, elList );
                    
                    // If item is specified 
                    if ( item != null && !empty )
                    	runOnChange( tblEl, elList );	
                    
                } // End of ** method updateItem **
                
            }; // End of ** anonymous class overriding TextFieldTableCell **
	
            // Set String Converter for current text field
            textField.setConverter( textFieldStringConverter() ); 
            
            // Return created text field
            return textField;	
            
        } ); // End of ** column Cell Factory **
    
    } // End of method ** createTextField **
    
    /**
     * Returns String Converter for Text Field
     * @return String Converter
     */
    private StringConverter textFieldStringConverter()
    {
    	return new StringConverter()
        {
            @Override
            public Object fromString( String string ) 
            {
                return string;
            }

            @Override
            public String toString( Object object ) 
            {
                return ( object != null ? object.toString() : "" );
            }
        };
    }
    
    /**
     * Gets invoked when Text Field item is updated
     * @param cell Table Cell
     * @param column Table column
     * @param elList Elements List
     */
    private void updateTextFieldItem( TextFieldTableCell cell, TableColumn column, AssociativeList elList )
    {
    	elList.set( "TableCell", cell );
    	
        // Get data model for current row 
        ArrayList<Data> row = (ArrayList<Data>) cell.getTableRow().getItem();
        
        // Get column number from table view
        int colNum = cell.getTableView().getColumns().indexOf( cell.getTableColumn() );
        
        String cellText = "";
        
        // If column number is specified and data model cell is not empty
        if ( colNum != -1 && row != null && row.size() > 0 && row.get(colNum) != null  )
        	cellText = (String) row.get(colNum).get();
        
        cell.setText( cellText );
        
        // Create data element for current column
        elList.set( column.getText(), cellText );
    }
    
    /**
     * Sets event handlers for specified column
     * @param column Column for which to set event handlers
     * @param tblEl Table element with column field parameters
     * @param elList Associative list with table elements values
     */
    private void setEditEventHandlers( TableColumn column, TableElement tblEl, AssociativeList elList )
    {
        // Set event handler for start edit event
        column.setOnEditStart( onEditStartHandler() ); 
        
        // Set event handler for on edit commit event
        column.setOnEditCommit( onEditCommitHandler( column, tblEl, elList ) );
    
    } // End of method ** setEditEventHandlers **
    
    
    /**
     * Event Handler that gets invoked when finishing editing
     * @param column Table column
     * @param tblEl Table Element with parameters
     * @param elList Elements List
     * @return Event Handler
     */
    private EventHandler onEditCommitHandler( TableColumn column, TableElement tblEl, AssociativeList elList )
    {
    	return event -> 
        { 
            setRowDataValue( event, column );
            
            runOnChange( tblEl, elList ); 
        };
    }
    
    /**
     * Sets Data value for current Table row
     * @param event Event
     * @param column Table column
     */
    private void setRowDataValue( Event event, TableColumn column )
    {
    	// Get current table view
        TableView tblView = ((TableColumn.CellEditEvent) event).getTableView();

        // Get items for for current table view
        ObservableList items = (ObservableList) tblView.getItems();
		
        // Get row number for current table position
        int rowNum = ((TableColumn.CellEditEvent) event).getTablePosition().getRow();

        // Get new value for current table cell
        String newValue = (String)((TableColumn.CellEditEvent) event).getNewValue();

        // Get current row data array
        ArrayList<Data> row = (ArrayList<Data>) items.get( rowNum );
        
        // Get current column number
        int colNum = tblView.getColumns().indexOf( column );

        // If data value is not specified for current cell
        if ( row.get( colNum ) == null )
            // Create Data object for current cell
            row.set( colNum, Data.create( newValue ) );
        else
            // Set value of current cell to new value
            row.get( colNum ).set( newValue );
    }
    
    /**
     * Event handler that gets invoked when starting editing table cell
     * @return Event Handler
     */
    private EventHandler onEditStartHandler()
    {
    	return event -> 
        {
            TableColumn col = (TableColumn) event.getSource();
            
            // Get current row number
            int rowNum = ((TableColumn.CellEditEvent) event).getTablePosition().getRow();
        };
    }
    
    /**
     * Creates column for Table line control button
     * @return Created column
     */
    private TableColumn createCtrlBtnColumn()
    {
        // Create new column
        TableColumn column = new TableColumn( "*" );
	
        // Set column cell factory
        column.setCellFactory( ctrlBtnCellFactory() ); 

        return column;
    
    } // End of method ** createCtrlBtnColumn **
    
    /**
     * Creates Cell factory for Control button cell
     * @return Callback with Cell Factory
     */
    private Callback ctrlBtnCellFactory()
    {
    	return e -> 
        { 
            // Create button object with anonymous class extending TableCell
            TableCell controlButton = new TableCell()
            {
            	TableView tbl =  ((TableColumn) e).getTableView();
                
                @Override  
                public void startEdit() 
                {  
                	startEditCtrBtnCell( this, tbl );
                }
				
                // Gets called when cell item is updated
                @Override
                public void updateItem( Object item, boolean empty )
                {
                    // Invoke parent class method
                    super.updateItem( item, empty );
			
                    // If item is not empty
                    if ( !empty )
                    	updateCtrBtnCellItem( this, tbl );
                    else
                        setGraphic( null );
                    
                } // End of method ** updateItem **
                
            }; // End of ** anonymous class extending TableCell **
		
            // Set control button Editable attribute
            controlButton.setEditable( true );

            // Return created control buttons
            return  controlButton;
        };
    }
    
    /**
     * Gets invoked when Control Button cell starts editing
     * @param cell Table Cell
     * @param tbl Table View
     */
    private void startEditCtrBtnCell( TableCell cell, TableView tbl )
    {
    	// Get items for for current table view
        ObservableList tblItems = (ObservableList) tbl.getItems();
    
        // Display List Dialog and get the result of selection
        String menuItem = selectCtrlBtnMenu( cell );

        // Get row number for current table position
        int rowNum  = tbl.getSelectionModel().getSelectedIndex();

        try
        {
            // If Selected Ins menu item
            if ( menuItem.startsWith( "Ins" ) )
            	insertTblRow( menuItem, tblItems, rowNum );
            
            // If selected Delete menu item
            else if ( menuItem == "Delete" )
	            try
	            {
	            	// Remove current row
	                tblItems.remove( rowNum );
	            }
	            catch ( Exception e ) {}
        }
        catch ( Exception e ) {}
    }
    
    /**
     * Gets invoked when Control Button cell is updated
     * @param cell Table Cell
     * @param tbl Table View
     */
    private void updateCtrBtnCellItem( TableCell cell, TableView tbl )
    {
    	// Get items for for current table view
        ObservableList tblItems = (ObservableList) tbl.getItems();
    
        // Get current TableView row number
        int rowNum = cell.getTableRow().getIndex();

        ArrayList<Data> row;

        // If row number is specified
        if ( rowNum >= 0 )
            // Get row from data model
            row = (ArrayList<Data>) tblItems.get(rowNum);
        else
            // Get row from table view
            row = (ArrayList<Data>) cell.getTableRow().getItem();
        
        // If row is not empty
        if ( row != null && rowEntered( row ) )
        	cell.setText( "*" );
        else
        	cell.setGraphic( null );
    }
    
    /**
     * Inserts Table rows into Table View 
     * @param menuItem Menu Item that invoked to insert row
     * @param tblItems TableItems of TableView
     * @param rowNum Row number
     */
    private void insertTblRow( String menuItem, ObservableList tblItems, int rowNum )
    {
    	// Set number of columns as the length of underlying data model array row
        final int numCols = ( tblItems != null ? ((ArrayList<Data>) tblItems.get(0)).size() : 0 );

        // Create ArrayList for new row
        ArrayList<Data> newRow = new ArrayList<>();

        for (  int i = 0; i < numCols; i++) 
            // Fill all columns with null value
            newRow.add(null);

        // If it's Insert before menu item
        if ( menuItem.endsWith( "before" ) )
            // Add new row before current row
            tblItems.add( rowNum, newRow );

        // If it's Insert after menu item
        else if ( menuItem.endsWith( "after" ) )
            // Add new row after current row
            tblItems.add( rowNum+1, newRow );
    }
    
    /**
     * Creates menu for Control Button 
     * @param btn Control Button object
     * @return Result of selecting menu item
     */
    private String selectCtrlBtnMenu( Node btn )
    {
    	// Create Control button menu options
        String[] menu = new String[]{ "Ins before", "Ins after", "Delete", "Esc" };

        // Display List Dialog and get the result of selection
        return new ListDialog( stage, menu, 75, 100 ).result( btn );
    }
    
    /**
     * Creates Tab object
     * @param tabNum Tab number
     * @return Created Tab
     */
    private Tab createTab( int tabNum )
    {
        // Create Tab object for current tab
        Tab tab = new Tab( );

        // If there is array of tab names
        if ( tabs != null )
            // Set name for current tab
            tab.setText( tabs[tabNum] );
        else
            // Set current column name as tab name
            tab.setText( tableElement[tabNum][0].columnName );

        // Set table as content of current cell
        tab.setContent( table.get(tabNum) );

        return tab;
    
    } // End of method ** createTab **
    
    /**
     * Class Constructor
     * @param outerThis Outer Dialog object that invoked creation of this object
     * @param doc RegistryItem document that needs to be created
     */
    public TableDialogView( OneColumnView outerThis, RegistryItemModel doc )
    {
        tblObj = outerThis;
        dlgElementsList = tblObj.getDialogAttributesList();
        numRows = tblObj.get( "numRows" );
        stage = outerThis;
        tabs = tblObj.get( "tabs" );
        
        AssociativeList attr = doc.getAttributesList();    
        tableElement = (TableElement[][]) attr.get( "table" );
        
        addTableElements();
    }
    
} // End of class ** TableDialogView **