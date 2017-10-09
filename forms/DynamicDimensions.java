package forms;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedHashSet;

import models.GLAcctModel;
import views.NodeView;
import foundation.AssociativeList;
import foundation.Item;
import interfaces.Constants;

import static interfaces.Constants.HEIGHT;
import static interfaces.Constants.WIDTH;
import static interfaces.Utilities.createModelClass;

/**
 * Class DynamicDimensions 
 * @author Peter Cross
 */
public class DynamicDimensions extends NodeView  implements Constants
{
    /*
        Attributes
    */
	private static String   title = "G/L Account Dimensions";
    
	private Stage           stage;			// Stage object where to display
    private Node            btn;			// Button object
    private String[]        output;			// Output results
    private Scene           scene;			// Scene object on which to display
    private int             width = 255,    // Window width
                            height = 150;   // Window height 
    private AssociativeList attributesList;	// Passed attributes list
    
    private GLAcctModel  glAccount;		// G/L account for which to enter dimensions
    private AssociativeList initValue;		// Initial values for the dimension
    private String[]        init;			// String array of initial values
    
    /*
        Methods
    */
    
    /**
     * Opens dialog to select dimensions of G/L account
     * @param btn Button object which invokes the dialogue
     * @return Selected dimensions
     */
    public <T> T result( Node btn )
    {
        this.btn = btn;
        
        display( createContent() );
    	
        return (T) output;
    }
    
    /**
     * Creates content to display in the dialogue
     * @return Pane object with content
     */
    protected <T> Pane createContent()
    {
        Pane pane = new Pane();   
        
        GridPane grid = createGrid();
        
        Label ttl = new Label( title );
        
        ttl.setFont( new Font( "Arial", 15 ) );
        ttl.setMaxWidth( Long.MAX_VALUE );
        ttl.setAlignment( Pos.CENTER_LEFT );
        
        VBox ttlBox = new VBox( ttl );
        
        ttlBox.setStyle( "-fx-border-style: solid inside;"
                        +"-fx-border-color: #DDD;"
                        +"-fx-background-color: transparent;"
                        +"-fx-border-width: 0 0 0 0;"
                        +"-fx-padding: 15;" );
        
        GridPane.setConstraints( ttlBox, 1, 0 );
        grid.getChildren().add( ttlBox );
        
        String[] analytics = (String[]) glAccount.getFields().get( "analyticsControl" );
        
        for ( int i = 0; i < analytics.length; i++ )
        {
            if ( analytics[i] == null || analytics[i].isEmpty() )
                continue;
            
            if ( init != null && init.length > i )
                initValue.set( analytics[i], init[i] );
            
            createLabel( grid, analytics[i], 0, i+1 );
            createComboBox( grid, analytics[i], 1, i+1 );
        }
        
        HBox btns = formButtons();
        
        GridPane.setConstraints( btns, 1, analytics.length+1 );
        grid.getChildren().add( btns );
        
        pane.getChildren().add( grid );
        
        return pane;
    }
    
    /**
     * Creates Pane to display dialogue content
     * @return Pane object
     */
    protected Pane createPane()
    {
        Label ttl = new Label( title );
        
        ttl.setFont( new Font( "Arial", 15 ) );
        ttl.setMaxWidth( Long.MAX_VALUE );
        ttl.setAlignment( Pos.CENTER );
        
        VBox ttlBox = new VBox( ttl );
        
        ttlBox.setStyle( "-fx-border-style: solid inside;"
                        +"-fx-border-color: #DDD;"
                        +"-fx-background-color: transparent;"
                        +"-fx-border-width: 0 0  1 0;"
                        +"-fx-padding: 15;" );
        
        Pane pane = new Pane( );
        
        pane.setStyle( "-fx-padding: 5;" );
        
        return pane;
    }
    
    /**
     * Creates grid for displaying dialog objects
     * @return Grid Pane object
     */
    GridPane createGrid()
    {
        GridPane grid = new GridPane();
        
        grid.setPadding( new Insets( 10, 10, 10, 10 ) );
        grid.setVgap( 5 );
        grid.setHgap( 5 );
        grid.setStyle( "-fx-border-style: solid inside;"
                      +"-fx-border-color: #FFF;"
                      +"-fx-border-width: 1 0 0 0;" );
        return grid;
    }
    
    /**
     * Creates Label object for the dialogue
     * @param grid  Grid object
     * @param label Label content
     * @param col Column number
     * @param row Row number
     */
    protected void createLabel( GridPane grid, String label, int col, int row )
    {
        Label lbl = new Label( label + ":" );
        lbl.setMaxWidth( Long.MAX_VALUE );
        lbl.setAlignment( Pos.CENTER_RIGHT );
        lbl.setFont( new Font( "Arial", 13 ) );
        
        GridPane.setConstraints( lbl, col, row );
        grid.getChildren().add( lbl );
    }
    
    /**
     * Creates ComboBox object for the dialogue
     * @param grid  Grid object
     * @param analytics Name of Analytics Dimension
     * @param col Column number
     * @param row Row number
     */
    protected void createComboBox( GridPane grid, String analytics, int col, int row )
    {
        final int PREF_WIDTH = 300;
        
        Class cls = null;
        Method m;
        LinkedHashSet list;
        
        ComboBox<String> comboBox = null;
        String[] choices = new String[] {};
        
        try
        {
            cls = createModelClass( dimension.get( analytics ) );   
        }
        catch ( Exception e ) {}
        
        if ( cls != null )
        {
            try
            {
                m = cls.getDeclaredMethod( "getItemsList" ); 
                list = (LinkedHashSet) m.invoke( null );
                
                int listSize = list.size();
                choices = new String[listSize];
                Item item;
            
                Iterator it = list.iterator();
                int i = 0;
                
                while ( it.hasNext() )
                {
                    item = (Item) it.next();
                    choices[i++] = item.toString();
                }
                
                ObservableList<String> options = FXCollections.observableArrayList( choices );
                comboBox = new ComboBox<>( options );
            }
            catch ( Exception e ) {}    
        }
         
        if ( comboBox != null )
        {
            comboBox.setPrefWidth( PREF_WIDTH );   
            comboBox.setEditable( false );
            
            String init = (String) initValue.get( analytics );
            
            if ( init != null && !init.isEmpty() )
                comboBox.setValue( init );
            
            GridPane.setConstraints( comboBox, col, row);
            grid.getChildren().add( comboBox );
            
            attributesList.set( analytics, comboBox );
        }   
    }
    
    /**
     * Get Value of a dimension from Combo Box
     * @param comboBox ComboBox to get dimension from
     * @return String representation of Dimension value
     */
    private String getDimensionValue( ComboBox comboBox)
    {
        String value = "";
        
        // Get text value of current combo box value selected
        value = (String) comboBox.getSelectionModel().getSelectedItem();

        // If value is of complex type
        if ( value == null )
            // Get text value of combo box and save it to variable
            value = comboBox.getEditor().getText();
        
        return value;
    }
    
    /**
     * Creates buttons for the form
     * @return Box object containing buttons
     */
    protected HBox formButtons()
    {
        Button btnOK = new Button( "OK" );
        btnOK.setPadding( new Insets( 5, 20, 5, 20 ) );
        
        btnOK.setOnAction( e -> 
        {
            String[] analytics = (String[]) glAccount.getFields().get( "analyticsControl" );
            output = new String[analytics.length];
            
            for ( int i = 0; i < analytics.length; i++ )
            {
                if ( analytics[i] == null || analytics[i].isEmpty() )
                    continue;
                
                // Get value for current dialog element by attribute name
                output[i] = getDimensionValue( (ComboBox) attributesList.get( analytics[i]) );
            }
            close();
        } );
        
        Button btnCancel = new Button( "Cancel" );
        btnCancel.setPadding( new Insets( 5, 20, 5, 20 ) );
        btnCancel.setOnAction( e -> close() );
        
        HBox buttons = new HBox();
        buttons.setPadding( new Insets( 10, 0, 5, 0 ) );
        buttons.setSpacing( 5 );
        buttons.setAlignment( Pos.CENTER_RIGHT );
        
        buttons.getChildren().addAll( btnOK, btnCancel );
        
        return buttons;
    }
    
    /*
        Constructors
    */
    /**
     * Class constructor
     * @param account G/L account object
     * @param stage Stage object
     * @param initValue Initial values for the dimension
     */
    public DynamicDimensions( GLAcctModel account, Stage stage, String initValue )
    {
        super( title );
    	
    	this.glAccount = account;
        this.stage = stage;
        
        this.initValue = new AssociativeList();
        this.init = initValue.split( "/" );
        
        attributesList = new AssociativeList();
    }    
    
} // End of class DynamicDimensions 