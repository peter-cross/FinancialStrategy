package forms;

import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.event.Event;
import javafx.event.EventHandler;
import java.net.URL;
import java.time.LocalDate;

/**
 * Class DateDialog - dialog form for entering dates
 * @author Peter Cross
 */
public class DateDialog extends Stage
{
    /*
        Attributes
    */
    private Stage           stage;			// Stage where to display
    private Node            btn;			// Button object
    private String          initValue;		// Initial date value
    private String          output;			// Output date value
    private Scene           scene;			// Scene object to display
    private int             width = 90,    	// Window width
                            height = 150;   // Window height 
    /*
        Methods
    */
    
    /**
     * Opens dialog to select a date
     * @param btn Button object which invokes the dialogue
     * @return Selected date
     */
    public <T> T result( Node btn )
    {
        this.btn = btn;
        displayForm();
        
        return (T) output;
    }
    
    /**
     * Displays date input dialogue
     */
    protected void displayForm()
    {
        setScene( createScene() );
        
       // Get layout bounds
        Bounds bounds = btn.getLayoutBounds();

        // Get scene object of the button
        Scene sc = btn.getScene();

        // Get minimum X and Y scene bounds relative to local scene
        Point2D point = btn.localToScene( bounds.getMinX(), bounds.getMinY() );
        // Create scene coordinate object
        Point2D sceneCoord = new Point2D( sc.getX(), sc.getY() );
        // Create window coordinate object
        Point2D windowCoord = new Point2D( sc.getWindow().getX(), sc.getWindow().getY() );

        // Calculate absolute X coordinate for the dialog
        double x = point.getX() + sceneCoord.getX() + windowCoord.getX();
        // Calculate absolute Y coordinate for the dialog
        double y = point.getY() + sceneCoord.getY() + windowCoord.getY();

        // Set X and Y coordinates for the dialog
        setX( x + bounds.getWidth()- width );
        setY( y );

        showAndWait();
    }
    
    /**
     * Creates scene for displaying dialogue content
     * @return Created Scene object
     */
    protected Scene createScene()
    {
        Scene scene = new Scene( createContent() );
        
        URL styleSheet = getClass().getResource( "/application/application.css" );
        
        if ( styleSheet != null )
        {
            ObservableList<String> style = scene.getStylesheets();
            
            style.add( styleSheet.toExternalForm() );
        }
        
        return scene;
    }
	
    /**
     * Creates content to display in the dialogue
     * @return Pane object with content
     */
    protected <T> Pane createContent()
    {
        final int MAX_WIDTH = 90;

        Pane pane = new Pane( );   
        
        GridPane grid = createGrid();
            
        // Create date picker element
        DatePicker datePicker = new DatePicker();
        
        if ( initValue != null && !initValue.isEmpty() )
            datePicker.setValue( LocalDate.parse(initValue) );
        
        // Specify column width for date picker
        datePicker.setMaxWidth( MAX_WIDTH );
        
        datePicker.setOnAction( new EventHandler()
        {
            @Override
            public void handle( Event event ) 
            {
                output = datePicker.getValue().toString();
                close();
            }
        } );
        
        GridPane.setConstraints( datePicker, 0, 0 );
        grid.getChildren().add( datePicker );
        
        HBox btns = formButtons();
        
        pane.getChildren().add( grid );
        
        return pane;
    }
    
    /**
     * Creates grid for displaying dialog objects
     * @return Grid Pane object
     */
    GridPane createGrid()
    {
        GridPane grid = new GridPane();
        
        grid.setPadding( new Insets( 0, 0, 8, 0 ) );
        grid.setVgap( 2 );
        grid.setHgap( 2 );
        grid.setStyle( "-fx-border-style: solid inside;"
                      +"-fx-border-color: #FFF;"
                      +"-fx-border-width: 1 0 0 0;" );
        return grid;
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
    
    /**
     * Constructor
     * @param st Stage object
     * @param init Value to initialize dialogue
     */
    public DateDialog( Stage st, String init )
    {
        stage = st;
        initValue = init;
        
        this.initStyle( StageStyle.UNDECORATED );
        this.initModality( Modality.APPLICATION_MODAL  );
    }
    
} // End of class ** DateDialog **