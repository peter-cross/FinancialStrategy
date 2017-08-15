package forms;

import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Modality;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;

import java.net.URL;

import interfaces.Encapsulation;

/**
 * Class ListDialog - creates drop-down list dialog
 * @author Peter Cross
 */
public class ListDialog extends Stage implements Encapsulation
{
    /*          Properties   	                                                                                      */
    /******************************************************************************************************************/
    protected Stage 	stage;          // Stage object for output
    protected String	returnValue;    // Return value
    protected Node		btn;            // Button that evoked the dialog
    protected String[] 	list;           // List of choices

    private int		width = 100,    // Default dialog width
                    height = 100;   // Default dialog height
    
    /*          Methods                                                                                               */
    /******************************************************************************************************************/
    /**
     * Creates list content
     * @param <T> Type of the list
     * @return Parent node object
     */
    protected <T> Parent createContent()
    {
        // Create ListView object
        ListView<T> listView = new ListView<>();
        listView.setPrefHeight( height );
        listView.setMaxWidth( width );

        // Create observable list based on passed list of choices
        ObservableList items = FXCollections.observableArrayList( list );

        // Set observable list items for list view object
        listView.setItems( items );

        // Get selection model for list view
        MultipleSelectionModel model = listView.getSelectionModel();

        // Add event listener for selected item property
        model.selectedItemProperty().addListener( (observable, newValue, oldValue) -> 
        {
            // Convert selected value to string and assign to return value variable
            returnValue = (String)oldValue;
            
            // Close dialog window
            close();	
       
        } );

        return listView;
        
    } // End of method ** createContent **
	
    /**
     * Creates Scene object for the list dialog
     * @return Created Scene object
     */
    private Scene createScene()
    {
        // Create content and pass it to the scene
        Scene scene = new Scene( createContent() );

        // Get StylSheet object
        URL styleSheet = getClass().getResource( "/application/application.css" );

        // If StyleSheet object is specified
        if ( styleSheet != null )
        {
            // Get style object of the scene
            ObservableList<String> style = scene.getStylesheets();

            // Add StyleSheet to the style object
            style.add( styleSheet.toExternalForm() );
        }

        return scene;
        
    } // End of method ** createScene **
	
    /**
     * Displays List Dialog form
     */
    protected void displayForm()
    {
        // Create scene
        Scene scene = createScene();

        // Set scene for the stage
        setScene( scene );

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

        // Display and wait of click button event
        showAndWait();
        
    } // End of method ** displayForm **
	
    /**
     * Displays the form and returns the result of input
     * @param <T> List Type parameter
     * @param btn Button evoked the form
     * @return Selected value
     */
    public <T> T result( Node btn )
    {
        this.btn = btn;

        // Display dialog form
        displayForm();

        return (T)returnValue;
    }
	
    /*          Constructors                                                                                          */
    /******************************************************************************************************************/
    /**
     * Class constructor
     * @param stage Stage object where to display
     * @param list Array with list of choices
     */
    public ListDialog( Stage stage, String[] list )
    {
        this.stage = stage;
        this.list = list;

        this.initStyle( StageStyle.UNDECORATED );
        this.initModality( Modality.APPLICATION_MODAL  );
    }
	
    /**
     * Class constructor
     * @param stage Stage object where to display
     * @param list Array with list of choices
     * @param width Dialog width
     * @param height Dialog height
     */
    public ListDialog( Stage stage, String[] list, int width, int height )
    {
        this( stage, list );

        this.width = width;
        this.height = height;
    }

} // End of class ListDialog