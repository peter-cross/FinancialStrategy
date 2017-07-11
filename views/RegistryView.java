package views;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.beans.value.ObservableValue;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.Locale;
import java.util.List;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.lang.reflect.Constructor;
import java.text.DecimalFormat;
import java.net.URL;

import application.Main;
import forms.DialogElement;
import foundation.AssociativeList;
import foundation.Cipher;
import foundation.Data;
import interfaces.Buttons;
import interfaces.Constants;
import interfaces.Encapsulation;
import interfaces.Utilities;
import models.RegistryItemModel;

import static interfaces.Buttons.newActionButton;
import static interfaces.Utilities.attrName;
import static interfaces.Utilities.createModelClass;
import static interfaces.Utilities.getByIndex;

/**
 * Class RegistryView - class for creating registry views for Registry Items
 * @author Peter Cross
 */
public class RegistryView extends Stage implements Buttons, Encapsulation, Constants, Utilities
{
    /*          Properties   	                                                                                      */
    /******************************************************************************************************************/
    /*                                                                                                                */
    private Stage                               		stage;          // Stage object of the window
    private Stage										owner;          // Window owner
    
    private Scene                               		scene;          // Scene object to display content
    private String                              		title;          // Window title
    private String                              		valueType;      // Value type of Registry Items
    private String[]                            		tabs;           // Tabs
    private String                              		tabsType;       // Value type of Tab Items
    private int                                         numCols = 0;    // Number of columns to display
    private Button                                      modeBtn;		// Mode button ( switching modes )
    
    private LinkedHashSet<RegistryItemModel>[]          list;      // List of Registry Items to display
    private ArrayList<TableView<ArrayList<Data>>>       table;     // Table to display content
    private ArrayList<ObservableList<ArrayList<Data>>>  data;      // data model of TableView object
    
    /*          Methods                                                                                               */
    /******************************************************************************************************************/
    /*                                                                                                                */
    
    /**
     * Displays the registry form
     */
    public void display()
    {
        display( WIDTH*0.95 );
         
    } // End of method * display **
    
    /**
     * Displays the registry form in a window of specified width
     * @param width Form width
     */
    public void display( double width )
    {
    	// Create Scene, set up scene settings and get current scene Pane object
    	displayPane( setScene( width ) );
    }
    
    /**
     * Displays the registry form in a window of specified width and height
     * @param width Form width
     * @param height Form height
     */
    public void display( double width, double height )
    {
    	// Create Scene, set up scene settings and get current scene Pane object
    	displayPane( setScene( width, height ) );
    }
    
    /**
     * Displays form pane
     * @param pane Pane object to display
     */
    private void displayPane( Pane pane )
    {
    	// If Pane object was not created
        if ( pane == null )
            return;
        
        // If tabs are specified but Table Views are not created yet
        if ( tabs != null && tabs.length > 0 && table.isEmpty() )
        {
        	TabPane tabPane = tabPaneForMultipleTabs();
        	
        	// Add tab pane to pane
            pane.getChildren().add( tabPane );
            
            // Add buttons to the form
            addTableButtons( tabPane );
        }
        // If there are no tabs specified
        else
            // If TableView is not created yet
            if ( table.isEmpty() )
            {
                TableView tbl = createTableView(0);
                
                if ( tbl == null )
                	return;
            	
            	// Create TableView object and add it to the pane
                pane.getChildren().add( tbl );
                
                // Add buttons to the form
                addTableButtons( null );
            }
        
        pane.requestFocus();
        
        // Display form
        displayForm();
    }
    
    /**
     * Creates TabPane for multiple Tabs
     * @return TabPane object
     */
    private TabPane tabPaneForMultipleTabs()
    {
    	// Create Pane for tabs
        TabPane tabPane = createTabPane();
        
        // Loop for each tab
        for ( int i = 0; i < tabs.length; i++ )
        {
            // Create TableView for current tab
            createTableView(i);
                
            // Create Tab object for current table
            Tab tab = createTab(i);
            
            // Add current tab to the tab pane
            tabPane.getTabs().add( tab );
        }
        
        return tabPane;
    }
    
    /**
     * Adds buttons to the form
     * @param tabPane Tab pane to which add buttons
     */
    private void addTableButtons( TabPane tabPane )
    {
        // Create array for table button objects
        Button[] btn = new Button[6];
        
        // Create New button element with Event Handler
        btn[0] = newActionButton( "New", eventNewRegistryItem( tabPane ) );
        
        // Create Edit button element with Event Handler
        btn[1] = newActionButton( "Edit", eventEditRegistryItem( tabPane ) );
        
        // Create Delete button element with Event Handler
        btn[2] = newActionButton( "Delete", eventDeleteRegistryItem( tabPane ) );
        
        // Create Print button object with Event Handler
        btn[3] = newActionButton( "About ", e -> displayAbout() );
        
        // Create button for Mode switch
        btn[4] = modeBtn; 
        
        // Create Close button object with Event Handler
        btn[5] = newActionButton( "Close ", eventCloseRegistry() );
        
        // Get pane from Scene object
        Pane pane = (Pane) scene.getRoot();

        // Add array with buttons to Pane object
        addButtonsToPane( btn, pane );
        
    } // End of method ** addTableButtons **
    
    /**
     * Creates Table View for the scene
     * @param tabNum Tab number
     * @return Table View object
     */
    private TableView createTableView( int tabNum )
    {
        RegistryItemModel   item = null;    // To store a sample of Registry Item
        AssociativeList     attributesList; // To store Registry Item's attributes list
        DialogElement[][]   header;         // To store header elements of Registry Items
        String[]            column;         // to store column names to display
        int 				numRows;
    
        double  columnsWidth = 0; // To store calculated columns width
        
        String[] attributeName;
        
        // If there is a filled list
        if ( list != null && list[tabNum] != null && list[tabNum].size() > 0 )
        {
            // Get 1st registry item from the list
            item = getByIndex( list[tabNum], 0 );
            
            // Assign number of rows as list size plus one ( for new item )
            numRows = list[tabNum].size() + 1;
        }
        // Otherwise - just create a new item
        else
        {
            // Create new Registry Item without displaying it
            item = newRegistryItem( tabNum, null );
            
            // Assign number of rows one ( for new item )
            numRows = 1;
        }
        
        // If Registry Item sample is specified
        if ( item != null )
        {
            // Get attributes list for Registry Item sample
            attributesList = item.getAttributesList();
            // Get header elements for Registry Item sample
            header = (DialogElement[][]) attributesList.get( "header" );
            
            // Create String array for column names
            column = new String[ header.length * header[0].length ];
            // Create String array for current RegistryItemModel object attribute names
            attributeName = new String[ header.length * header[0].length ];
            
            int numCol = 0;
            
            // Go through each header tab in Registry Item
            for ( DialogElement[] hdr : header ) 
            {
                // Go through each header element in current tab for Registry Item
                for ( DialogElement itm : hdr ) 
                {
                    // If header element is specified
                    if ( itm != null ) 
                    {
                        // If there is attribute name specified
                        if ( itm.attributeName != null && !itm.attributeName.isEmpty() ) 
                            attributeName[numCol] = itm.attributeName;
                        // Otherwise - assume it's the label name
                        else 
                            attributeName[numCol] = itm.labelName;
                        
                        // If for the header element short name is specified
                        if ( itm.shortName != null && !itm.shortName.isEmpty() ) 
                            // Store short name to display
                            column[numCol] = itm.shortName;
                        // Otherwise
                        else 
                            // Store Label name
                            column[numCol] = itm.labelName;
                        
                        // Add name's to display length to columns width
                        columnsWidth += column[numCol++].length();
                        
                    } // End if header element is specified
                    
                } // End for ** Go through each header element in current tab **
                
            } // End for ** Go through each tab **
            
            // Save calculated number of columns
            numCols = numCol;
            
            columnsWidth *= 1.007; // Adjustment to remove horizontal scroll bar
            
        } // End If ** Registry Item sample is specified **
        
        // Otherwise, if Registry Item sample is not specified for some reason
        else
            return null;
        
        // Create array for Table View data model
        ArrayList<ArrayList<Data>> matrix = createTableMatrix( tabNum, attributeName );
        
        if ( matrix == null )
        	return null;
        
        // Create TableView object with data model
        TableView<ArrayList<Data>> tbl = createTableViewFromMatrix( tabNum, matrix );
        
        if ( tbl == null )
        	return null;
        
        // Add created TableView object to table ArrayList
        table.add( tabNum, tbl );
        
        // Create Table Columns for TableView
        createTableColumns( header, column, columnsWidth, tbl );
        
        // Highlight 1st table row
        tbl.getSelectionModel().selectFirst();
        
        tbl.setFocusTraversable( true );
		
        // Return table for specified tab
        return tbl;
        
    } // End of method ** createTableView **
    
    /**
     * Displays the form
     */
    public void displayForm()
    {
        // Get URL object for the style sheet
        URL styleSheet = getClass().getResource( "/application/application.css" );

        // If style sheet is specified
        if ( styleSheet != null )
        {
            // Get style sheet object from the scene
            ObservableList<String> style = scene.getStylesheets();

            // If style is not specified yet
            if ( style.isEmpty() )
                // Add style sheet to style attribute of the stage
                style.add( styleSheet.toExternalForm() );	
        }
	
        // Set created Scene object as scene to display
        setScene( scene );
        // Set title of the window
        setTitle( Main.TITLE );
        
        if ( owner != null )
        	try
	        {
        		initOwner( owner );	
	        }
        	catch ( Exception e ) {}
        
        try
        {
            // Display registry view
            show();
        }
        catch ( Exception e )
        {
            System.out.println( "Can't display stage : " + e.getMessage() );
        }
    } // End of method ** displayForm **

    /**
     * Updates table content after changes
     * @param tabNum Tab number on which table located
     */
    public void updateTableView( int tabNum )
    {
        TableView<ArrayList<Data>> tbl = table.get(tabNum);
        
        // Update Table View 
        tbl.setItems( null );
        tbl.layout();
        tbl.setItems( data.get(tabNum) );        
    }
    
    /**
     * Adds new Registry Item to the display list and data model
     * @param tabNum Tab number
     * @param newItem RegistryItemModel object to add
     */    
    protected void add( int tabNum, RegistryItemModel newItem )
    {
        // Create TableView object for selected tab
        TableView<ArrayList<Data>> tbl = table.get(tabNum);
        
        // Get items for current table view
        ObservableList items = (ObservableList) tbl.getItems();
        
        // If new RegistryItemModel is specified
        if ( newItem != null )
        {
        	// Add new RegistryItemModel to table view
        	setInTableView( items, newItem );
        	
        	// Add empty row to table view
        	addEmptyRow( items );
        	
        	// Update Table View
        	updateTableView( tabNum );
            
            // Highlight current row number 
            tbl.getSelectionModel().select( items.size()-2 );
            
        } // End If ** new RegistryItemModel is specified **
            
    } // End of method ** add **
    
    /**
     * Sets Registry Item in Table View
     * @param items Items of Table View
     * @param regItem Registry Item to set in Table View
     * @param rowNum Row number
     */
    private void setInTableView( ObservableList items, RegistryItemModel regItem, int rowNum )
    {
    	// Get current row of items list
        ArrayList<Data> row = (ArrayList) items.get( rowNum );
        
        // Get attributes list for new RegistryItemModel
        AssociativeList attributesList = regItem.getAttributesList();
        
        // Get header elements of the new RegistryItemModel
        DialogElement[][] header = (DialogElement[][]) attributesList.get( "header" );
        
        // Get fields attribute of Registry Item
        AssociativeList fields = regItem.getFields();
        
        // Columns counter
        int colCounter = 0; 
        
        // Go through all header elements and create array elements for data model
        // Loop through header tabs
        for ( int t = 0; t < header.length; t++ )
        	// Loop through elements on the Tab
            for ( int e = 0; e < header[t].length; e++ )
            	// If did not reach the Registry number of columns
                if ( colCounter < numCols )
                	// Set in current column Data object with value of header element
                    row.set( colCounter++, Data.create( fields.get( attrName(header[t][e]) ) ) );
    }
    
    /**
     * Sets Registry Item in the last row of Table View
     * @param items Items of Table View
     * @param regItem Registry Item to set in Table View
     */
    private void setInTableView( ObservableList items, RegistryItemModel regItem )
    {
    	setInTableView( items, regItem, items.size() - 1 );
    }
    
    /**
     * Adds empty row to the Data Model of Table View
     * @param items Data Model items of Table Views
     */
    private void addEmptyRow( ObservableList items )
    {
    	// Add another Data array object for new display list item
        ArrayList<Data> newRow = new ArrayList<>();
        
        // For each column to display
        for ( int j = 0; j < numCols; j++ )
            // Add null values to the new row
        	newRow.add( null );
        
        // Add new empty row
        items.add( newRow );
    }
    
    /**
     * Removes Registry Item from display list
     * @param tabNum Tab number
     * @param itemToRemove Registry Item to remove
     */
    protected void remove( int tabNum, RegistryItemModel  itemToRemove )
    {
        list[tabNum].remove( itemToRemove );
    
    } // End of method ** remove **
    
    /**
     * Creates new Registry Item
     * @param tabNum Tab number
     * @param st Stage to display created item on
     * @return Created new Registry Item
     */
    protected RegistryItemModel newRegistryItem( int tabNum, Stage st )
    {
        // If value type for new Registry Item is not specified
        if ( valueType == null || valueType.isEmpty() )
            return null;
        
        RegistryItemModel obj = null;    // To store created new RegistryItemModel object
        
        try 
        {
        	// Get class for new RegistryItemModel object
            Class cls = createModelClass( valueType );
            
            // If there are no tabs in current registry
            if ( tabs == null || tabs.length == 0 )
            {
                // Get class constructor with parameter stage
                Constructor cnstr = cls.getConstructor( Stage.class );

                // Create RegistryItemModel object with class constructor
                obj = (RegistryItemModel) cnstr.newInstance( st );
            }
            else
            {
                // Get class constructor with parameter stage and entity
                Constructor cnstr = cls.getConstructor( Stage.class, String.class );
                
                // Create RegistryItemModel object with class constructor
                obj = (RegistryItemModel) cnstr.newInstance( st, tabs[tabNum] );
            }
            
            if ( obj != null )
	            // Add created Registry Item to Registry List
	            add( tabNum, obj );
        } 
        catch ( Exception ex ) {}
        
        return obj;
        
    } // End of method ** newRegistryItem **
    
    /**
     * Opens dialog to edit RegistryItemModel object
     * @param tabNum Tab number
     */
    protected void editRegistryItem( int tabNum )
    {
        // Get TableView object for current tab
        TableView<ArrayList<Data>> tbl = table.get(tabNum);
        
        // Get items for for current table view
        ObservableList items = (ObservableList) tbl.getItems();
    
        RegistryItemModel regItem; // To store RegistryItemModel to edit
        
        // Get current Table View row number
        int rowNum = tbl.getSelectionModel().getSelectedIndex();
        
        // If row number is specified and is valid
        if ( rowNum >= 0 && rowNum < list[tabNum].size() )
            // Get RegistryItemModel to edit from display list
            regItem = getByIndex( list[tabNum], rowNum  );
        else
            return;
        
        try 
        {
        	// Get attributes list of Registry Item
        	AssociativeList attrList = regItem.getAttributesList();
        	
        	// Set stage attribute to current Registry form
        	attrList.set( "stage", this );
        	
        	// Open dialog window to edit RegistryItemModel
            regItem.display(); 
            
            // Set RegistryItemModel in TableView in specified row
            setInTableView( items, regItem, rowNum );
            
            // Update Table
            updateTableView( tabNum );
            
            // Highlight current row number 
            tbl.getSelectionModel().select( rowNum );
        } 
        catch ( Exception e ) {} 
        
    } // End of method ** editRegistryItem **
    
    /**
     * Deletes current Registry Item
     * @param tabNum Tab number
     */
    protected void deleteRegistryItem( int tabNum )
    {
        // Get TableView object for selected tab
        TableView<ArrayList<Data>> tbl = table.get(tabNum);
        
        // Get items for for current table view
        ObservableList items = (ObservableList)tbl.getItems();
	
        // Get current Table View row number
        int rowNum = tbl.getSelectionModel().getSelectedIndex();
        
        // If either row number is not specified or it's just a free row for new RegistryItemModel
        if ( rowNum < 0 ^ rowNum == list[tabNum].size() )
            return;
        
        // Ask user to confirm deleting current Registry Item
        if ( getYesNo( "Do you want to delete current item?" ) != YES )
            return;
            
        // Remove from TableView RegistryItem in specified row
        removeFromTableView( items, tabNum, rowNum );
        
        // Update TableView
        updateTableView( tabNum );
            
        // If there is RegistryItemModel before deleted row
        if ( rowNum - 1 >= 0 )
            // Highlight RegistryItemModel before deleted row
            tbl.getSelectionModel().select( rowNum - 1 );
        
        // otherwise
        else
            // Highlight RegistryItemModel which goes after deleted row
            tbl.getSelectionModel().select( rowNum );
        
    } // End of method ** deleteRegistryItem **
    
    /**
     * Removes from TableView RegistryItemModel in specified row
     * @param items Items of TableView Data model
     * @param tabNum Tab number
     * @param rowNum Row number
     */
    private void removeFromTableView( ObservableList items, int tabNum, int rowNum )
    {
    	// Remove current row from Table View
        items.remove( rowNum );
        
        RegistryItemModel regItem = getByIndex( list[tabNum], rowNum  );
        
        try
        {
        	regItem.removeFromDB();
        }
        catch ( Exception e )
        {
        	regItem = null;
        }
        
        // Remove current row from display list
        list[tabNum].remove( rowNum );
    }
    
    /**
     * Creates Scene object and prepares scene for displaying something
     * @param width Width of window
     * @return Pane object for created Scene
     */
    protected Pane setScene( double width )
    {
    	return setScene( width, HEIGHT - 3 * taskBarHeight );
    }
    
    /**
     * Creates Scene object and prepares scene for displaying something
     * @param width Width of window
     * @param height Height of window
     * @return Pane object for created Scene
     */
    protected Pane setScene( double width, double height )
    {
        // Preferred height for display window by default
        final int PREF_HEIGHT = (int) height; 
        final int PREF_WIDTH = 800;
        
        Pane pane; // To store Pane object for created scene
        
        // If Scene object is not created yet
        if ( scene == null )
        {
            // Create vertical box for scene content
            VBox sceneBox = new VBox();
            
            // Set scene preferred width and height
            sceneBox.setPrefWidth( width < PREF_WIDTH ? width : Math.max(width, PREF_WIDTH) );
            sceneBox.setPrefHeight( PREF_HEIGHT );
            
            // Create Scene object with vertical box as content and store it in property variable
            scene = new Scene( sceneBox ); 
            
            // Create label element for the title
            Label ttl = new Label( title );

            // Set font for the label
            ttl.setFont( new Font( "Arial", 15 ) );
            ttl.setMaxWidth( Long.MAX_VALUE );
            
            // Position title in the middle
            ttl.setAlignment( Pos.CENTER );

            // Create box element for the title
            VBox box = new VBox( ttl );
            
            // Specify style for box rendering
            box.setStyle("-fx-border-style: solid inside;"
                        +"-fx-border-color: #DDD;"
                        +"-fx-background-color: #F7F7F7;"
                        +"-fx-border-width: 0 0 1 0;"
                        +"-fx-padding: 15;");
            
            // Get pane element from Scene object
            pane = (Pane) scene.getRoot();
            // Add box element to the pane
            pane.getChildren().add( box );
        }
        
        // Otherwise, if Scene object exists
        else
            // Get pane element for existing Scene object
            pane = (Pane) scene.getRoot();
        
        // Set default date settings
        Locale.setDefault( Locale.CANADA );

        return pane;
        
    } // End of method ** setScene **
    
    /**
     * Creates TextField object in specified column
     * @param column Column in which to create TextField object
     * @param tblEl Table element with TextField parameters
     */
    private void createTextField( TableColumn column, DialogElement dlgEl )
    {
        final String elType; // To store column element type
        
        // If CheckBox label is specified for current column
        if ( !dlgEl.checkBoxlabel.isEmpty() )
        {
            // Set column alignment to the center
            column.setStyle("-fx-alignment: center;");
            // Store CheckBox element type
            elType = "CheckBox";
        }
        // If Number type value is specified
        else if ( dlgEl.valueType.contains( "Number" ) )
        {
            // Set column alignment to the right
            column.setStyle("-fx-alignment: center-right;");
            // Store Number element type
            elType = "Number";
        }
        // Otherwise
        else 
            // Element type to display - Text
            elType = "Text";
        
        // Set Cell Factory for current column
        column.setCellFactory( textFieldCellFactory(elType) );
    
    } // End of method ** createTextField **
    
    
    /**
     * Creates Cell Factory for TextField object
     * @param elType Type of TextField object
     * @return Cell Factory for TextField object
     */
    private Callback textFieldCellFactory( String elType )
    {
    	return e -> 
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
                    {
                    	// Get text value of Table Cell
                    	String strVal = getTableCellTextValue( this, elType );
                    	
                    	setText( strVal );
                        
                    } // End If ** table cell is not empty **
                    
                } // End of method ** updateItem **
                
            }; // End of ** Anonymous class overriding TextFieldTableCell **
            
            // Set String Converter for the TextField
            textField.setConverter( textFieldStringConverter() );
	
            // Return created text field
            return textField;	
        };
    }
    
    /**
     * Gets text value for TextField table cell
     * @param cell TextField table cell
     * @param elType Cell element type 
     * @return String with text value
     */
    private String getTableCellTextValue( TextFieldTableCell cell, String elType )
    {
    	String strVal = "";
    	
    	// Get Table View current row Data Model array
        ArrayList<Data> row = (ArrayList) cell.getTableRow().getItem();
        // Get column number from table view
        int colNum = cell.getTableView().getColumns().indexOf( cell.getTableColumn() );

        // If column number is specified and data model cell is not empty
        if ( row != null && colNum != -1 && row.size() > 0 && row.get(0) != null  )
        {
            // Get Cell Data object for current column number
        	Data cellData = row.get( colNum );
        	
        	// If there is something in current cell
            if ( cellData != null )
            	// Get text representation of cell value
            	strVal = cellTextValue( cellData.get() );
            
            // If element to display is CheckBox
            if ( elType == "CheckBox" && !strVal.isEmpty() )
                // If CheckBox is checked
                if ( Double.parseDouble( strVal ) > 0 )
                    // Assign + symbol to display in column
                    strVal = "+";
                // Otherwise
                else
                    // Display just empty cell
                    strVal = "";
        }
    	
        return strVal;
    }
    
    /**
     * Gets Text representation of Cell Value
     * @param cellVal Value stored in Cell
     * @return Text representation of Cell Value
     */
    private String cellTextValue( Object cellVal )
    {
    	String strVal;
    	
    	// If the value in current cell is a string
        if ( cellVal instanceof String )
            // Get cell value from Data model array cell
            strVal = (String) cellVal;
        
        // If the value in current cell is a text field
        else if ( cellVal instanceof TextField )
            // Get text value of the text field in current cell
            strVal = ((TextField) cellVal).getText();
        
        // If the value in current cell is a number
        else if ( cellVal instanceof Number )
        	// Get text value of the number
        	strVal = cellNumberTextValue( (Number)cellVal );
        
        // Otherwise
        else
            try
            {
                // Try to convert current value to string
                strVal = cellVal.toString();
            }
            catch ( Exception e )
            {
            	strVal = "";
            }
    
    	return strVal;
    }
    
    /**
     * Gets text representation of Number stored in Cell
     * @param cellVal Number stored in Cell
     * @return Text representation of Number
     */
    private String cellNumberTextValue( Number cellVal )
    {
    	// Create formatter for decimal numbers
    	DecimalFormat formatter = new DecimalFormat( "#,###.00" );
        String strVal;
    	
    	try
        {
            // If the value in current cell is zero
            if ( (double) cellVal == 0 )
                strVal = "";
            
            // If it's a number without decimals
            else if ( (double) cellVal == Math.ceil( (double)cellVal ) )
            	// Format integer number according to formatter
                strVal = formatter.format( (int) Math.floor( (double)cellVal ) );
            
            // Otherwise, if it's a double value with decimals
            else
                // Format number with decimals
            	strVal = formatter.format( cellVal );
        }
        catch ( Exception e )
        {
            // If the value in current cell is an integer
            try
            {
                // If the value is zero
                if ( (int) cellVal == 0 )
                    strVal = "";
                
                // Otherwise, if non-zero value
                else
                    strVal = formatter.format( cellVal );
            }
            catch ( Exception e1 )
            {
                // Try to use toString method to convert to String
            	strVal = cellVal.toString();
            }
        }
    	
    	return strVal;
    }
    
    /**
     * Creates String Converter for TextField
     * @return Anonymous class for String Converter
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
     * Creates Columns for TableView
     * @param header Array with Header elements of RegistryItemModel
     * @param column Array of columns
     * @param columnsWidth Columns width
     * @param tbl TableView in which to create columns
     */
    private void createTableColumns( DialogElement[][] header, String[] column, double  columnsWidth, TableView<ArrayList<Data>> tbl )
    {
    	int colCntr = 0;    // Columns counter
        
    	if ( tbl == null )
    		return;
    	
        // Go through each tab in header part
        for ( DialogElement[] hdr : header ) 
            // Go through each header element in current tab
            for ( DialogElement itm : hdr ) 
                // If header element is specified
                if ( itm != null) 
                {
                    // Create TableColumn object for current header element with name from column array
                    TableColumn clmn = new TableColumn( column[colCntr] );
                    
                    // Create text field for current column based on header element's parameters
                    createTextField( clmn, itm );
                    // Bind column width property reative to column's name length
                    clmn.prefWidthProperty().bind( tbl.widthProperty().multiply( column[colCntr].length()/columnsWidth) );
                    // Add current column to the table
                    tbl.getColumns().add( clmn );
                    
                    // Increment columns counter
                    colCntr++;
                }
    }
    
    /**
     * Create TableView from Matrix with Data Model
     * @param tabNum Tab number on which to create the TableView
     * @param matrix Matrix with Data Model
     * @return Created TableView
     */
    private TableView<ArrayList<Data>> createTableViewFromMatrix( int tabNum, ArrayList<ArrayList<Data>> matrix )
    {
    	final int CELL_SIZE = 25;
    	final double SCALE_FACTOR = 0.85;
    	
    	if ( matrix == null )
    		return null;
    	
    	// Create data model array from created Data array
        data.add( tabNum, FXCollections.observableList( (List) matrix ) );
        
        // Create TableView object with data model passed to constructor
        TableView<ArrayList<Data>> tbl = new TableView<>( data.get( tabNum ) );
        
        // Make table non-editable
        tbl.setEditable( false );
        // Set placeholder for empty rows
        tbl.setPlaceholder( new Label("") );
        
        tbl.setFixedCellSize( CELL_SIZE );
        // Get table view bindable to the number of items
        ObservableValue bindSize = Bindings.size( tbl.getItems() ).multiply( tbl.getFixedCellSize() ).add( tbl.getFixedCellSize() );
        
        // Make Table Hight bindale to number of rows
        tbl.prefHeightProperty().bind( bindSize );
        
        // Set minimum Height
        tbl.setMinHeight( HEIGHT * SCALE_FACTOR );
        
    	return tbl;
    }
    
    /**
     * Create Matrix with Data Model for Table View
     * @param tabNum Tab number for Table View
     * @param attributeName Array with RegistryItemModel attribute names
     * @return Data Model for Table View
     */
    private ArrayList<ArrayList<Data>> createTableMatrix( int tabNum, String[] attributeName )
    {
    	if ( list == null )
    		return null;
    	
    	// Create array for Table View data model
        ArrayList<ArrayList<Data>> matrix = new ArrayList<>();
        
        RegistryItemModel  item = null; // To store retrieved RegistryItemModel
    	
    	// Loop for each item in list to display 
        for ( int i = 0; i < list[tabNum].size(); i++ )
        {
            // Get next item from list to display
            item = getByIndex( list[tabNum], i );
            
            // If item is not specified
            if ( item ==  null )
                continue;
            
            // Get fields attribute for current Registry Item
            AssociativeList fields = item.getFields();
            
            // Create ArrayList for current row
            ArrayList<Data> curRow = getCurrRowColumns( attributeName, fields );
            
            // Add current row to the matrix array
            matrix.add( curRow );
            
        } // End of Loop ** for each list to display item **
        
        // Create ArrayList object for current row to fill empty cells in new row
        ArrayList<Data> curRow = new ArrayList<>();
        
        // For each column to display
        for ( int j = 0; j < numCols; j++ )
            curRow.add( null );
        
        // Add empty row to the matrix
        matrix.add( curRow );
        
        return matrix;
    }
    
    /**
     * Creates ArrayList for current row columns
     * @param attributeName Array of attribute names for columns
     * @param fields Fields attribute for RegistryItemModel
     * @return Created list of columns with values
     */
    private ArrayList<Data> getCurrRowColumns( String[] attributeName, AssociativeList fields )
    {
    	// Create ArrayList for current row
        ArrayList<Data> curRow = new ArrayList<>();
        
        String colName; // To store column names
        
        // For each column to display
        for ( int j = 0; j < numCols; j++ )
        {
            // Get column name, trim it and remove spaces
            colName = attributeName[j].trim().replace( " ", "" );
            
            // Convert 1st character to lower case
            colName = colName.substring(0, 1).toLowerCase() + colName.substring(1);
            
            // Create data model array element obtained from attributes list
            curRow.add( Data.create( fields.get( colName ) ) );
        }
        
    	return curRow;
    }
    
    /**
     * Creates Event Handler for creating new Registry Item
     * @param tabPane TabPane object for current Tab
     * @return Event Handler for creating new Registry Item
     */
    private EventHandler<ActionEvent> eventNewRegistryItem( TabPane tabPane )
    {
    	return e -> 
        {
            int tabNum = selectedTabNumber( tabPane );
            
            // Create new Registry Item for specified tab
            newRegistryItem( tabNum, stage );
        };
    }
    
    /**
     * Creates Event Handler for editing Registry Item
     * @param tabPane TabPane object for current Tab
     * @return Event Handler for editing Registry Item
     */
    private EventHandler<ActionEvent> eventEditRegistryItem( TabPane tabPane )
    {
    	return e -> 
        {
        	int tabNum = selectedTabNumber( tabPane );
            
            // Edit current Item on specified Tab
            editRegistryItem( tabNum );
        };
    }
    
    /**
     * Creates Event Handler for deleting Registry Item
     * @param tabPane TabPane object for current tab
     * @return Event Handler for deleting Registry Item
     */
    private EventHandler<ActionEvent> eventDeleteRegistryItem( TabPane tabPane )
    {
    	return e -> 
        {
        	int tabNum = selectedTabNumber( tabPane );
            
            // Delete current Registry Item on specified tab
            deleteRegistryItem( tabNum );
        };
    }
    
    /**
     * Creates Event Handler for closing Registry Form
     * @return Event Handler for closing Registry Form
     */
    private EventHandler<ActionEvent> eventCloseRegistry()
    {
    	return e -> close();
    }
    
    /**
     * Creates Event Handler for printing Registry Item
     * @param tabPane TabPane object for current Tab
     * @return Event Handler for printing Registry Item
     */
    private EventHandler<ActionEvent> eventPrintRegistryItem( TabPane tabPane )
    {
    	return e -> {};
    }
    
    /**
     * Gets name of selected tab
     * @param tabPane Tab pane object
     * @return Selected tab name
     */
    private String selectedTabName( TabPane tabPane )
    {
    	return tabs[ selectedTabNumber(tabPane) ];
    }
    
    /**
     * Gets selected tab number 
     * @param tabPane Tabpane object for current Pane
     * @return
     */
    private int selectedTabNumber( TabPane tabPane )
    {
    	// If there are tabs
        if ( tabPane != null )
            // Get tab number from selected tab
        	return tabPane.getSelectionModel().getSelectedIndex();
        else
        	return 0;
    }
    
    /**
     * Creates Tab Pane object
     * @return Created Tab Pane
     */
    private TabPane createTabPane()
    {
        TabPane tabPane = new TabPane();
        
        // Make closing tab unavailable
        tabPane.setTabClosingPolicy( TabPane.TabClosingPolicy.UNAVAILABLE );

        return tabPane;
        
    } // End of method ** createTabPane **

    /**
     * Creates Tab object
     * @param tabNum Tab number
     * @return Created Tab
     */
    private Tab createTab( int tabNum )
    {
        // Create Tab object for current tab
        Tab tab = new Tab();

        // If there is array of tab names
        if ( tabs != null )
            // Set name for current tab
            tab.setText( tabs[tabNum] );
        else
            // Set numbered tab name
            tab.setText( tabsType + " " + tabNum );

        // Set selected table as content of current tab
        tab.setContent( table.get(tabNum) );
        
        return tab;
    
    } // End of method ** createTab **
    
    
    /*          Constructors                                                                                          */
    /******************************************************************************************************************/
    /*                                                                                                                */
    /**
     * Class constructor with specified Registry title and type of RegistryIem
     * @param stage Stage where to display
     * @param title Registry title 
     * @param valueType RegistryIem type
     */
    public RegistryView( Stage stage, String title, String valueType )
    {
        this.table = new ArrayList<>(1);
        this.data = new ArrayList<>(1);
        this.owner = stage;
        this.stage = this;
        this.title = title;
        this.valueType = valueType;
        
        Class c = createModelClass( valueType );
        
        Cipher.getInstance();
    	
        try
        {
            // Get list of Value Type objects
            list = (LinkedHashSet[]) c.getMethod( "createList" ).invoke( null );
        }
        catch ( Exception e ) {}
        
        modeBtn = null;
    }
    
    public RegistryView( Stage stage, String title, String valueType, Button btn )
    {
        this(stage, title, valueType);
        
        modeBtn = btn;
    }
    
    /**
     * Class constructor with specified Registry title, type of RegistryIem and Tabs type
     * @param stage Stage where to display
     * @param title Registry title 
     * @param valueType RegistryIem type
     * @param tabsType Type of tabs items
     */
    public RegistryView( Stage stage, String title, String valueType, String tabsType )
    {
        this( stage, title, valueType );
        
        HashSet tabList =  null;
        
        Class c = createModelClass( tabsType );
        
        try
        {
            // Get list of Tabs
            tabList = ((LinkedHashSet[]) c.getMethod( "createList" ).invoke( null ))[0];
        }
        catch ( Exception e ) {}
        
        c = createModelClass( valueType );
        
        try
        {
            // Get list of Value Type objects
            list = (LinkedHashSet[]) c.getMethod( "createList" ).invoke( null );
        }
        catch ( Exception e ) {}
        
        int arrSize = ( tabList != null ? tabList.size() : 1 );
        
        // Create main arrays for Table View
        this.table = new ArrayList<>( arrSize );
        this.data = new ArrayList<>( arrSize );
        this.tabs = new String[ arrSize ];
        this.tabsType = tabsType;

        // If there are tabs in the registry
        if ( tabList != null && arrSize > 0 )
        {
            Iterator it = tabList.iterator();
            int i = 0;
            
            // Loop through each tab
            while ( it.hasNext() )
                // Get Tab name from tab list
                this.tabs[i++] = it.next().toString();
        }
    }
    
    public RegistryView( Stage stage, String title, String valueType, String tabsType, Button btn )
    {
        this(stage, title, valueType, tabsType);
        
        modeBtn = btn;
    }
    
} // End of class ** Registry **