package views;

import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.time.LocalDate;
import java.io.File;

import models.RegistryItemModel;
import forms.DialogElement;
import forms.TreeDialog;
import foundation.AssociativeList;
import foundation.Item;
import interfaces.Encapsulation;
import interfaces.Utilities;
import interfaces.Lambda.DialogAction;

import static interfaces.Utilities.createModelClass;

/**
 * Class OneColumnDialog - One column input dialog form
 * @author Peter Cross
 *
 */
public class OneColumnView extends NodeView implements Encapsulation, Utilities
{
    /*          Properties   	                                                                                      */
    /******************************************************************************************************************/
    protected AssociativeList		attributesList;     // Form attributes List
    protected RegistryItemModel		regItem;            // Original Registry Item
    protected DialogElement[][] 	dialogElement;		// Array of dialog elements
    
    protected Pane 			content;		// Content pane
    protected Scene			scene;			// Dialog element scene
    protected DialogAction 	code;			// Lambda expression for field value validation

    protected String		title;			// Form title
    protected String[][]	elementContent;	// Content of form fields in text format
    protected String[]		tabs;			// Tab names
    
    /*          Methods                                                                                              
     ****************************************************************************************************************
     */

    /**
     * Gets Scene object for the dialogue
     * @return Scene object
     */
    public Scene getDialogScene()
    {
        return scene;
    }
    
    /**
     * Get Registry Item for the dialogue
     * @return Registry Item
     */
    public RegistryItemModel getRegistryItem()
    {
        return regItem;
    }
    
    /**
     * Creates change listener for dialog fields
     * @param el Dialog element parameters
     * @return Created ChangeListener object
     */
    private ChangeListener fieldChangeListener( DialogElement el )
    {
    	return new ChangeListener() 
        {
            @Override
            public void changed( ObservableValue observable, Object oldValue, Object curValue ) 
            {
               // Invoke specified lambda expression with parameters
               el.onChange.run( attributesList ); 
            }
        };
    }
    
    /**
     * Sets change listener for ComboBox
     * @param comboBox ComboBox dialog element
     * @param el Description parameters of ComboBox
     */
    private void setOnChangeListener( ComboBox<String> comboBox, DialogElement el )
    {
        // If action on dialog elemnt change is specified
        if ( el.onChange != null )
            // Add event listener on dialog element change 
        	comboBox.valueProperty().addListener( fieldChangeListener(el) );
    }
    
    /**
     * Sets what to run if TextField value is changed
     * @param textField TextField dialog element
     * @param el TextField element description parameters
     */
    private void setOnChangeListener( TextField textField, DialogElement el )
    {
    	if ( el.onChange != null )
    		textField.textProperty().addListener( fieldChangeListener(el) );
    }
    
    /**
     * Sets default choice for ComboBox dialog element
     * @param comboBox  ComboBox dialog element
     * @param el Dialog element's description parameters
     * @param textChoices List of text choices for ComboBox
     */
    private void setDefaultChoice( ComboBox<String> comboBox, DialogElement el, String[] textChoices )
    {
        if ( el.defaultChoice < 0 )
            // If text value is specified
            if ( el.textValue != null && !el.textValue.isEmpty() )
            {
                // Find the key index in the key array
                int ind = Utilities.indexOf( textChoices, el.textValue );

                // If text value is found in the list of choices
                if ( ind != -1 )
                    // Assign it as a default choice
                    el.defaultChoice = ind;
            }
            // If there is the only choice in the list of choices
            else if ( textChoices.length == 1 )
                el.defaultChoice = 0;
            else
                el.defaultChoice = -1;
                    
        if ( el.defaultChoice >= 0 )
        {
            // Set current value of combo box from specified default choice
            comboBox.setValue( textChoices[el.defaultChoice] );
            
            // If action on dialog element change is specified
            if ( el.onChange != null )
                // Invoke specified lambda expression with parameters
                el.onChange.run( attributesList );
        }
    }
    
    /**
     * Sets column width for ComboBox
     * @param comboBox ComboBox dialog element
     * @param el Dialog element's description parameters
     */
    private void setColumnWidth( ComboBox<String> comboBox, DialogElement el )
    {
        // If element's width is specified
        if ( el.width > 0 )
            comboBox.setPrefWidth( el.width );
        // If it's a UnitModel dialog element
        else if ( el.labelName.contains( "UnitModel" ) )
            // Set column with less that normally
            comboBox.setPrefWidth(105);
        // Otherwise
        else
            comboBox.setPrefWidth(250);
    }
    
    /**
     * Gets values of List in the form of string array
     * @param list List of LinkedHashSet values
     * @return String array of values
     */
    private String[] getListValues( LinkedHashSet list )
    {
        // Create array for reference list values
        String[] values = new String[list.size()];

        Iterator it = list.iterator();
        
        int p = 0;
        Item itm;
        
        // Loop for each list element
        while ( it.hasNext() )
        {
            itm = (Item) it.next();
            // Convert its value to string
            values[p++] = itm.toString();
        }
        
        return values;
    }
    
    /**
     * Gets string array of text choices for dialog element
     * @param el Dialog element's description parameters
     * @return String array of text choices
     */
    private String[] getTextChoices( DialogElement el )
    {
        // If there are choices specified for the current element
        if ( el.textChoices.length > 0 )
            return el.textChoices;
        
        // If List reference is passed
        else if ( el.list != null)
            // Assign String values as text choices
            return getListValues( el.list );  
        
        else
            return new String[]{};
    }
            
    /**
     * Fills Combo Box with elements list
     * @param el Current dialog element
     * @return Filled combo box
     */
    protected ComboBox fillComboBox( DialogElement el )
    {
        String[] textChoices = getTextChoices( el );  // To store text choices for combo box

        // Create observable list from list of choices
        ObservableList<String> options = FXCollections.observableArrayList( textChoices );

        // Create combo box element for list of choices
        ComboBox<String> comboBox = new ComboBox<>( options );

        // Make combo box editable
        comboBox.setEditable( el.editable );

        setOnChangeListener( comboBox, el );
        
        setDefaultChoice( comboBox, el, textChoices );
        
        setColumnWidth( comboBox, el );
        
        return comboBox;

    } // End of method ** fillComboBox **

    /**
     * Creates content for the form
     * @return Pane of created scene
     */
    protected Pane createContent()
    {
    	// If dialog elements are not specified - just exit
        if (  dialogElement == null || dialogElement.length < 1 )
            return null;
        
        Pane pane;
        
        // If scene is not created yet
        if ( scene == null )
        	// Create Scene object for current dialog form
            pane = createScenePane();
        
        // Otherwise
        else
            // Get Pane element from scene
            pane = (Pane) scene.getRoot();
        
        // Store form properties in attributes list
        attributesList.set( "this", this );
        attributesList.set( "stage", this );
        attributesList.set( "scene", scene );
        attributesList.set( "dialogElement", dialogElement );
        
        // Set default date settings
        Locale.setDefault( Locale.CANADA );

        // Return scene Pane object
        return pane;
        
    } // End of method ** createContent **
	
    /**
     * Creates Pane for new Scene
     * @return Pane object
     */
    private Pane createScenePane()
    {
    	// Create vertical box for scene content
        VBox sceneBox = new VBox();
        sceneBox.setStyle( "-fx-padding: 5;" );
        
        // Create scene object with vertical box as content
        scene = new Scene( sceneBox );
        // Get pane element from scene
        Pane pane = (Pane) scene.getRoot();
        
        // Create label element for title
        Label ttl = new Label( title );
        // Set mono space font for the label
        ttl.setFont( new Font( "Arial", 15 ) );
        ttl.setMaxWidth( Long.MAX_VALUE );
        // Position title in the middle
        ttl.setAlignment( Pos.CENTER );
        
        // Create box element for the title
        VBox title = new VBox( ttl );
        
        // Specify style for box rendering
        title.setStyle("-fx-border-style: solid inside;"
                    +"-fx-border-color: #DDD;"
                    +"-fx-background-color: transparent;"
                    +"-fx-border-width: 0 0 1 0;"
                    +"-fx-padding: 10 15 15 15;");
        // Add box element to pane
        pane.getChildren().add( title );
    	
    	return pane;
    }
    
    /**
     * Creates Label for dialog element on the form
     * @param grid Grid object of the form
     * @param el Dialog element
     * @param col Column number
     * @param row Row number
     */
    protected void createLabel( GridPane grid, DialogElement el, int col, int row )
    {
        // Create label element for current element
        Label label = new Label( el.labelName.trim() + ":" );
        label.setMaxWidth( Long.MAX_VALUE );
        label.setAlignment( Pos.CENTER_RIGHT );

        // Set font for the label
        label.setFont( new Font( "Arial", 13 ) );

        // Position label element in corresponding grid 
        GridPane.setConstraints( label, col, row );

        // Add label element to the grid
        grid.getChildren().add( label );
        
    } // End of method ** createLabel **
	
    /**
     * Creates CheckBox element on the form
     * @param grid Grid object of the form
     * @param el Dialog element
     * @param col Column number
     * @param row Row number
     */
    protected void createCheckBox( GridPane grid, DialogElement el, int col, int row )
    {
        // Create check box element with label and checking it on or off according to passed value
        CheckBox checkBox = new CheckBox( el.checkBoxlabel );

        // If dialog element text value is specified
        if ( el.textValue != null && !el.textValue.isEmpty() )
            try
            {
                el.checkBox = Integer.parseInt( el.textValue );
            }
            catch ( Exception e )
            {
                el.checkBox = 0;
            }

        // Set Selected property for the CheckBox
        checkBox.setSelected( el.checkBox == 1 );

        // Save check box in attributes list
        attributesList.set( el.labelName, checkBox );
		
        // Set grid for the check box
        GridPane.setConstraints( checkBox, col, row );
        
        // Add check box to the grid
        grid.getChildren().add( checkBox );
        
    } // End of method ** createCheckBox **
	
    /**
     * Creates ComboBox element on the form
     * @param grid Grid object of the form
     * @param el Dialog element
     * @param col Column number
     * @param row Row number
     */
    protected void createComboBox( GridPane grid, DialogElement el, int col, int row )
    {
        final int PREF_WIDTH = 255;
        
        // Create combo box element and fill it with elements list
        ComboBox comboBox = fillComboBox( el );
        
        // If dialog element's width is specified
        if ( el.width > 0 )
            // Set combo box width
            comboBox.setMaxWidth( el.width );
        else
            comboBox.setPrefWidth( PREF_WIDTH );

        // Save combo box in attributes list under label name
        attributesList.set( el.labelName, comboBox );

        // Position combo box in the grid
        GridPane.setConstraints( comboBox, col, row );

        // Add combo box to the grid
        grid.getChildren().add( comboBox );
        
    } // End of method ** createComboBox **
	
    /**
     * Creates DatePicker element on the form
     * @param grid Grid object of the form
     * @param el Dialog element
     * @param col Column number
     * @param row Row number
     */
    protected void createDatePicker( GridPane grid, DialogElement el, int col, int row )
    {
        final int MAX_WIDTH = 105;

        // Create date picker element
        DatePicker datePicker = new DatePicker();
        // Specify column width for date picker
        datePicker.setMaxWidth( MAX_WIDTH );

        // If dialog element's text value is specified
        if ( el.textValue != null && !el.textValue.isEmpty() )
            // Parse text value to local date and set date pickers value
            datePicker.setValue( LocalDate.parse( el.textValue ) );

        // If dialog element's width is specified
        if ( el.width > 0 )
            // Set date picker's width
            datePicker.setMaxWidth( el.width );

        // Add date picker element to elements list
        attributesList.set( el.labelName, datePicker );

        // Specify grid for date picker element
        GridPane.setConstraints( datePicker, col, row );

        // Add date picker element to the grid
        grid.getChildren().add( datePicker );
        
    } // End of method ** createDatePicker **
	
    /**
     * Creates Tree List element on the form
     * @param grid Grid object of the form
     * @param el Dialog element
     * @param col Column number
     * @param row Row number
     */
    protected void createTreeList( GridPane grid, DialogElement el, int col, int row )
    {
        // Create text field element
        TextField textField = textFieldForTreeList();
        
        // Create button element
        Button btn = new Button( "..." );
        btn.setPrefWidth( 10 );
        // Set event handler on press button event
        btn.setOnAction( treeBtnEventHandler( el, btn, textField ) );
        
        // Create horizontal box element for TreeItem field
        HBox treeItemField = new HBox(); 
        // Add text field and button to box element
        treeItemField.getChildren().addAll( textField, btn );

        // If dialog element's text value is specified
        if ( el.textValue != null && !el.textValue.isEmpty() )
            // Set text field to specified text value
            textField.setText( el.textValue );
		
        // If element's width is specified
        if ( el.width > 0 )
            // Set width of the tree item
            treeItemField.setMaxWidth( el.width );

        // Save field value and field type to associative elements list
        attributesList.set( el.labelName, textField );

        // Specify grid for TreeItem field
        GridPane.setConstraints( treeItemField, col, row );

        // Add TreeItem field to the grid
        grid.getChildren().add( treeItemField );
        
    } // End of method ** createTreeList **
    
    /**
     * Creates TextField field to TreeList dialog element
     * @return TexField object
     */
    private TextField textFieldForTreeList()
    {
    	final int   PREF_WIDTH = 230,
                	PREF_HEIGHT = 20;

	    // Create text field element
	    TextField textField = new TextField();
	    textField.setPrefWidth( PREF_WIDTH );
	    textField.setEditable( false );
	    textField.setPrefHeight( PREF_HEIGHT );
	    textField.setStyle("-fx-background-color: #F7F7F7;"
	                      +"-fx-border-width: 1 0 1 1;"
	                      +"-fx-border-color: #AAA;"
	                      +"-fx-padding: 3;");
    	return textField;
    }
    
    /**
     * Creates Event Handler for Tree button
     * @param el Dialog element parameters
     * @param btn Tree button object
     * @param field TextField for results of selecting by Tree button
     * @return EventHandler object
     */
    private EventHandler<ActionEvent> treeBtnEventHandler( DialogElement el, Button btn, final TextField field )
    {
    	return e -> 
        {
            // Get TreeDialog object created and saved in attributes list
            TreeDialog obj = (TreeDialog) attributesList.get( el.labelName + "Object" );

            // Invoke result method to display the tree dialog form and get result of selection
            String result = obj.result( btn );

            // If something was selected
            if ( result != null && !result.isEmpty() )
                // Save full file path to text field
                field.setText( result );
        };
    }
	
    /**
     * Creates File Chooser element on the form
     * @param grid Grid object of the form
     * @param el Dialog element
     * @param col Column number
     * @param row Row number
     */
    protected void createFileChooser( GridPane grid, DialogElement el, int col, int row )
    {
        // Create text field element
        TextField textField = textFieldForFileChooser();
        
        // Create button element
        Button btn = new Button( "..." );
        btn.setPrefWidth( 10 );

        // Create horizontal box element for file field
        HBox fileField = new HBox(); 
        // Add text field and button to box element
        fileField.getChildren().addAll( textField, btn );

        // If dialog element's text value is specified
        if ( el.textValue != null && !el.textValue.isEmpty() )
            // Set text field to specified text value
            textField.setText( el.textValue );
	
        // If dialog element's width is specified
        if ( el.width > 0 )
            // Set text field's width
            fileField.setMaxWidth( el.width );

        // Save current stage element
        final Stage st = (Stage) this;
        
        // If it's a dialog to save a file
        if ( el.valueType.contains( "Save" ) )
            // Set event handler on press button event
        	btn.setOnAction( saveToFileEventHandler( st, textField ) );
        
        // If it's a dialog to open a file
        else if ( el.valueType.contains( "Open" ) )
            // Set event handler on press button event
        	btn.setOnAction( openFileEventHandler( st, textField ) );
        
        // Save text field to associative elements list
        attributesList.set( el.labelName, textField );

        // Specify grid for file field
        GridPane.setConstraints( fileField, col, row );

        // Add file field to the grid
        grid.getChildren().add( fileField );
        
    } // End of method ** createFileChooser **

    /**
     * Creates TextField field for FileChooser dialog element
     * @return Created TextField object
     */
    private TextField textFieldForFileChooser()
    {
    	final int PREF_WIDTH = 330;
    	final int PREF_HEIGHT = 20;

        // Create text field element
	    TextField textField = new TextField();
	    textField.setPrefWidth( PREF_WIDTH );
	    textField.setPrefHeight( PREF_HEIGHT );
        textField.setEditable( false );
	    textField.setStyle("-fx-background-color: #F7F7F7;"
	                      +"-fx-border-width: 1 0 1 1;"
	                      +"-fx-border-color: #AAA;"
	                      +"-fx-padding: 3;");
	    return textField;
    }
    
    /**
     * Creates Event Handler for Save To File action
     * @param st Stage object
     * @param field TextField object for file path
     * @return EventHandler object
     */
    private EventHandler<ActionEvent> saveToFileEventHandler( final Stage st, final TextField field )
    {
    	return e -> 
        {
        	// Create file chooser element
            FileChooser fileChooser = new FileChooser();

            // Set title for file chooser window
            fileChooser.setTitle( "Save to file" );

            // Display Save file dialog window
            File file = fileChooser.showSaveDialog(st);

            // If file was specified
            if ( file != null )
                // Save full file path to text field
                field.setText( file.getPath() );
        };
    }
    
    /**
     * Creates Event Handler for Open File action
     * @param st Stage object
     * @param field TextField object for file path
     * @return EventHandler object
     */
    private EventHandler<ActionEvent> openFileEventHandler( final Stage st, final TextField field )
    {
    	return e -> 
        {
        	// Create file chooser element
            FileChooser fileChooser = new FileChooser();

            // Set file chooser window title
            fileChooser.setTitle( "Open file" );

            // Open file chooser dialog window
            File file = fileChooser.showOpenDialog(st);

            // If file was specified
            if ( file != null )
                // Save file full path to text field
                field.setText( file.getPath() );
        };
    }
    
    /**
     * Creates Numerator element on the form
     * @param grid Grid object of the form
     * @param el Dialog element
     * @param col Column number
     * @param row Row number
     */
    protected void createNumerator( GridPane grid, DialogElement el, int col, int row )
    {
        // Create text field element
        TextField textField = textFieldForNumerator();
        
        // Create button element
        Button btn = new Button( "#" );
        btn.setPrefWidth( 30 );

        // Create horizontal box element for numerator field
        HBox numeratorField = new HBox(); 
        // Add text field and button to box element
        numeratorField.getChildren().addAll( textField, btn );

        // If dialog element's text value is specified
        if ( el.textValue != null && !el.textValue.isEmpty() )
            // Set text field to specified text value
            textField.setText( el.textValue );
	
        // If dialog element's width is specified
        if ( el.width > 0 )
            // Set text field's width
            numeratorField.setMaxWidth( el.width );

        // Set event handler on press button event
        btn.setOnAction( numeratorEventHandler( textField ) );
        
        // Save text field to associative elements list
        attributesList.set( el.labelName, textField );

        // Specify grid for file field
        GridPane.setConstraints( numeratorField, col, row );

        // Add file field to the grid
        grid.getChildren().add( numeratorField );
    }
    
    /**
     * Creates TextField field for Numerator dialog element
     * @return Created TextField object
     */
    private TextField textFieldForNumerator()
    {
    	final int   PREF_WIDTH = 100,
                	PREF_HEIGHT = 20;

	    // Create text field element
	    TextField textField = new TextField();
	    textField.setPrefWidth( PREF_WIDTH );
	    textField.setPrefHeight( PREF_HEIGHT );
	    textField.setEditable( true );
	    textField.setStyle("-fx-background-color: #F7F7F7;"
	                      +"-fx-border-width: 1 0 1 1;"
	                      +"-fx-border-color: #AAA;"
	                      +"-fx-padding: 3;");
	    return textField;
    }
    
    /**
     * Creates Event Handler for Numerator action
     * @param field TextField object for Numerator value
     * @return EventHandler object for ActionEvent
     */
    private EventHandler<ActionEvent> numeratorEventHandler( final TextField field )
    {
    	return e -> 
        {
            try
            {
                Class c = createModelClass( regItem.getClass().getSimpleName() );
                
                String numerator = (String)c.getMethod( "getNumerator" ).invoke( regItem );
                
                if ( numerator != null )
                {
                    int i = 0;
                    int num = -1;
                    
                    while( i < numerator.length() )
                    {
                        if ( !Character.isDigit( numerator.charAt(i) ) )
                        {
                            i++;
                            continue;
                        }
                        
                        try
                        {
                            num = Integer.parseInt( numerator.substring(i) );
                            break;
                        }
                        catch ( Exception ex )
                        {
                            i++;
                        }
                    }
                    
                    String newNumber = numerator.substring(0, i) 
                    				 + String.format( "%0" + (numerator.length() - i) + "d",  num+1 );
                    
                    field.setText( newNumber );
                }
            }
            catch ( Exception ex ) { }
        };
    }
    
    /**
     * Creates Text Field element on the form
     * @param grid Grid object of the form
     * @param el Dialog element
     * @param col Column number
     * @param row Row number
     */
    protected void createTextField( GridPane grid, DialogElement el, int col, int row )
    {
        final int   NUMBER_MAX_WIDTH = 105,
                    TEXT_MAX_WIDTH = 300;

        // Create text field element
        TextField textField = new TextField();

        // If it's a number field
        if ( el.valueType.contains( "Number" ) )
            // Set field alignment to the right
            textField.setAlignment( Pos.CENTER_RIGHT );
        
        // If dialog element's width is specified
        if ( el.width > 0 )
        {
            // Set text field' width
            textField.setMaxWidth( el.width );
            textField.setMinWidth( el.width );
        }
        else if ( el.valueType.contains( "Number" ) )
           // Set number field width
            textField.setMaxWidth( NUMBER_MAX_WIDTH );
        else
        {
            // Set text field' default width
            textField.setMaxWidth( TEXT_MAX_WIDTH );
            textField.setMinWidth( TEXT_MAX_WIDTH );
        }
        
        // If dialog element's value is specified
        if ( el.textValue != null && !el.textValue.isEmpty() )
            // Set text field to specified text value
            textField.setText( el.textValue );
	
        setOnChangeListener( textField, el );
        
        // Save text field in associative elements list
        attributesList.set( el.labelName, textField );

        // Set grid for the text field
        GridPane.setConstraints( textField, col, row );

        // Add text field to the grid
        grid.getChildren().add( textField );
        
    } // End of method ** createTextField **
	
    /**
     * Creates Grid object for the form
     * @return GridPane object
     */
    protected GridPane createGrid()
    {
        // Create grid for current tab
        GridPane grid = new GridPane();
        
        grid.setPadding( new Insets( 10, 10, 10, 10 ) );
        grid.setHgap( 5 );
        grid.setVgap( 5 );
        
        grid.setStyle("-fx-border-style: solid inside;"
                     +"-fx-border-color: #FFF;"
                     +"-fx-border-width: 1 0 0 0;");
        return grid;
        
    } // End of method ** createGrid **
	
    /**
     * Creates Tab object for the grid
     * @param grid Grid object of the form
     * @param tabNum Tab number
     * @return Created Tab object
     */
    protected Tab createTab( GridPane grid, int tabNum )
    {
        // Create Tab element
        Tab tab = new Tab();

        // If Tabs list is specified
        if ( tabs != null )
            // Set Tab name from Tabs list
            tab.setText( tabs[tabNum] );
        else
            // Set numbered Tab name
            tab.setText( "Tab " + tabNum );

        // Set grid as Tab content
        tab.setContent( grid );

        return tab;
        
    } // End of method ** createTab **
	
    /**
     * Creates Grid element based on dialog element's parameters 
     * @param grid GridPane object
     * @param el Dialog element's parameters 
     * @param row Row number for the element
     */
    private void createGridElement( GridPane grid, DialogElement el, int row )
    {
        // If dialog element is not specified
        if ( el == null )
            return;

        // If label for current element is specified and it's not a checkbox
        if ( !el.labelName.isEmpty() && el.checkBoxlabel.isEmpty() )
            createLabel( grid, el, 0,  row );

        // If there is a check box for the current dialog element
        if ( !el.checkBoxlabel.isEmpty() )
            createCheckBox( grid, el, 1,  row );
        
        // If List reference or text choices are passed
        else if ( el.valueType.contains( "List" ) || el.list != null || el.textChoices.length > 0 )
            createComboBox( grid, el, 1,  row );
        
        // If it's a Tree List field
        else if ( el.valueType.contains( "Tree" ) )
            createTreeList( grid, el, 1,  row );
        
        // If it's a Date field
        else if ( el.valueType.contains( "Date" ) )
            createDatePicker( grid, el, 1,  row );
        
        // If it's a File field
        else if ( el.valueType.contains( "File" ) )
            createFileChooser( grid, el, 1,  row );
        
        // If it's a Numerator field
        else if ( el.valueType.contains( "Numerator" ) )
            createNumerator( grid, el, 1,  row );
        
        // Otherwise
        else 
            createTextField( grid, el, 1,  row );
        
        if ( el.onChange != null )
        	el.onChange.run( attributesList );
    }
    
    /**
     * Adds dialog elements to the form
     */
    protected void addDialogElements()
    {
        // Set scene for the form
        content = createContent();
        
        if ( content == null )
            return;

        // Create tab pane element
        TabPane tabPane = new TabPane();
        // Make tabs closing unavailable
        tabPane.setTabClosingPolicy( TabPane.TabClosingPolicy.UNAVAILABLE );

        GridPane grid = null;
        Tab tab;

        // Loop for each tab
        for ( int t = 0; t < dialogElement.length; t++ )
        {
            // Create grid for current tab
            grid = createGrid();
            
            // Loop for each dialog element
            for ( int i = 0; i < dialogElement[t].length; i++ )
                createGridElement( grid, dialogElement[t][i], i+1 );
            		
            // Create Tab element
            tab = createTab( grid, t );
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
    
    } // End of method ** addDialogElements **
	
    /**
     * Gets text value for the field
     * @param obj Form object
     * @return Field text value
     */
    protected String getFieldValue( Object obj )
    {
        String el;

        // If it's a combo box
        if ( obj instanceof ComboBox )
        	el = getValueOf( (ComboBox) obj );
        	
        // If it's a check box
        else if ( obj instanceof CheckBox )
        	el = getValueOf( (CheckBox) obj );
        	
        // If it's a date field
        else if ( obj instanceof DatePicker )
        	el = getValueOf( (DatePicker) obj );
        	
        // Otherwise - assume it's a text field
        else
        	el = getValueOf( (TextField) obj );
        	
        return el;
        
    } // End of method ** getFieldValue **
	
    /**
     * Gets text value of ComboBox element
     * @param comboBox ComboBox element
     * @return Text value
     */
    private String getValueOf( ComboBox comboBox )
    {
    	String el;

    	// Get text value of current combo box value selected
        el = (String) comboBox.getSelectionModel().getSelectedItem();

        // If value is specified
        if ( el == null )
            // Get text value of combo box and save to element content array
            el = comboBox.getEditor().getText();	
    	
        return el;
    }
    
    /**
     * Gets text value of CheckBox element
     * @param checkBox CheckBox element
     * @return Text value
     */
    private String getValueOf( CheckBox checkBox )
    {
    	// Get text value of current check box
        return ( checkBox.isSelected() ? "1" : "0" );
    }
    
    /**
     * Gets text value of DatePicker element
     * @param datePicker DatePicker element
     * @return Text value
     */
    private String getValueOf( DatePicker datePicker )
    {
    	String el = "";
    	
        // Get value of date picker
        Object val = datePicker.getValue();

        // If value is specified
        if ( val != null )
            // Convert value to string and save to element content array
            el = val.toString();
        
        return el;
    }
    
    /**
     * Gets text value of TextField element
     * @param textField TextField element
     * @return Text value
     */
    private String getValueOf( TextField textField )
    {
    	// Get text value of text field
        return textField.getText();
    }
    
    /**
     * Validates dialog form elements
     * @return True if dialog elements passed validation, or false otherwise
     */
    public boolean validateDialogElements()
    {
        // Create array for element values
        elementContent = new String[dialogElement.length][];

        DialogElement el;	// To hold current dialog element

        // Loop for each tab
        for ( int t = 0; t < dialogElement.length; t++ )
        {
            // Create array for current dialog element
            elementContent[t] = new String[dialogElement[t].length];

            // Loop for each dialog element
            for ( int i = 0; i < dialogElement[t].length; i++ )
            {
                // Get current dialog element
                el = dialogElement[t][i];
		
                // If dialog element is not specified
                if ( el == null )
                    continue;

                // Get value for current dialog element by label name
                Object obj = attributesList.get( el.labelName );

                // Get text field's value and store in output array
                elementContent[t][i] = getFieldValue( obj );

                // If there is lambda expression for validating field value
                if ( el.validation != null )
                    // If the current element does not pass validation
                    if ( !el.validation.run( elementContent[t][i] ) )
                        // return false
                        return false;
				
            } // End for loop

        } // End for each tab loop

        // Store element content in attributes list
        attributesList.set( "elementContent", elementContent );
        
        // If reached this point without errors - return true
        return true;

    } // End of method ** validateDialogElements **
	
    /**
     * Adds buttons to form
     */
    protected void addButtons()
    {
        // Create OK button element
    	Button btnOK = buttonOK();
        
        // Create Cancel button object
    	Button btnCancel = buttonCancel();
    	
        // Get pane from Scene object
        Pane pane = (Pane) scene.getRoot();

        // Create horizontal box object
        HBox buttons = new HBox();

        buttons.setPadding( new Insets( 5, 12, 15, 12 ) );
        buttons.setSpacing( 5 );
        
        // Align buttons box to the right
        buttons.setAlignment( Pos.CENTER_RIGHT );

        // Add OK and Cancel buttons to buttons box
        buttons.getChildren().addAll( btnOK, btnCancel );
		
        // Add buttons box to the pane
        pane.getChildren().addAll( buttons );

    } // End of method ** addButtons **
	
    /**
     * Creates button OK object
     * @return Button object
     */
    private Button buttonOK()
    {
    	Button btn = new Button( "OK" );
        btn.setPadding( new Insets( 5, 20, 5, 20 ) );

        // Set event handler for press button event
        btn.setOnAction( btnOkEventHandler() );
        
        return btn;
    }
    
    /**
     * Creates button Cancel object
     * @return Button object
     */
    private Button buttonCancel()
    {
    	Button btn = new Button( "Cancel " );
        btn.setPadding( new Insets( 5, 20, 5, 20 ) );

        // Set event handler for press button event
        btn.setOnAction( e -> close() );
        
        return btn;
    }
    
    /**
     * Creates Event Handler for OK button action
     * @return EventHandler object for ActionEvent
     */
    private EventHandler<ActionEvent> btnOkEventHandler()
    {
    	return e -> 
        {
            // If dialog elements passed validation
            if ( validateDialogElements() )
            {
                // If there is another lambda expression to execute specified
                if ( code != null )
                    // Run that lambda expression
                    code.run( null );

                // Close dialog window
                close();
            }
        };
    }
    
    /**
     * Get dialog elements attributes list
     * @return Attributes List
     */
    protected AssociativeList getDialogAttributesList()
    {
        return attributesList;
    }
    
    /**
     * Displays form content
     */
    public void display()
    {
    	display( content );
    }
    
    /**
     * Returns form fields in text format
     * @return Array of form fields
     */
    public <T> T result()
    {
        addButtons();
        
        display();
    	
        // Return array containing dialog elements values in text form
        return (T) elementContent;
    }
	
    /*          Constructors                                                                                          */
    /******************************************************************************************************************/
    
    /**
     * Class default constructor
     */
    public OneColumnView()
    {
    	super( "" );
    	
    	addDialogElements();
    }
    
    /**
     * Class constructor with specified RegistryItemModel and item title
     * @param doc RegistryItemModel  object
     * @param title Title for dialog form
     */
    public OneColumnView( RegistryItemModel doc, String title )
    {
    	super( title );
    	
    	this.title = title;
        this.regItem = doc;
        attributesList = doc.getAttributesList();
        
        this.tabs = (String[]) attributesList.get( "headerTabs" );
        this.dialogElement = (DialogElement[][]) attributesList.get( "header" );
        this.owner = (Stage) attributesList.get( "stage" );
        
        addDialogElements();
    }
    
    /**
     * Class constructor for multi-tab dialog form
     * @param stage Stage where to display
     * @param title Dialog form title
     * @param tabs Array of tabs
     * @param dlg Dialog elements for each tab
     */
    public OneColumnView( Stage stage, String title, String[] tabs, DialogElement[]... dlg )
    {
    	super( title );
    	
    	owner = stage;
        this.title = title;
        this.tabs = tabs;
        attributesList = new AssociativeList();
        dialogElement = dlg;

        addDialogElements();
    }
	
    /**
     * Class constructor for simple dialog form with code to execute when the form is entered
     * @param stage Stage where to display
     * @param title Dialog form title
     * @param dlg Array of dialog elements
     * @param code Action code to execute
     */
    public OneColumnView( Stage stage, String title, DialogElement[] dlg, DialogAction code )
    {
        this( stage, title, dlg );
        this.code = code;
    }
    
    /**
     * Class constructor for simple dialog form 
     * @param stage Stage where to display
     * @param title Dialog form title
     * @param dlg Array of dialog elements
     */
    public OneColumnView( Stage stage, String title, DialogElement[] dlg )
    {
    	super( title );
    	
    	owner = stage;
        this.title = title;
    	this.dialogElement = new DialogElement[][]{dlg};
    	attributesList = new AssociativeList();
        
    	addDialogElements();
    }

    /**
     * Class constructor for simple dialog form without title
     * @param stage Stage where to display
     * @param dlgEl Array of dialog elements
     */
    public OneColumnView( Stage stage, DialogElement[] dlgEl )
    {
    	super( "" );
    	
    	owner = stage;
        title = "";
    	dialogElement = new DialogElement[][]{ dlgEl };
    	attributesList = new AssociativeList();
        
    	addDialogElements();
    }
    
} // End of class ** OneColumnDialog **