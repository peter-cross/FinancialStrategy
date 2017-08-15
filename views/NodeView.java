package views;

import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;

import java.net.URL;

import application.Controller;

/**
 * Class NodeView - basic dialog form
 * @author Peter Cross
 *
 */
public class NodeView extends Stage 
{
	/*********************  Properties  **************************************************************************/
	protected Stage owner;			// Parent window
	
	private Node 	content;		// Content to display
	private double width, height;	// Window width and height
	private String title;			// Window title
	
	/*********************  Methods  *****************************************************************************/
	
	/**
	 * Displays provided content
	 * @param node Content to display
	 */
	public void display( Node node )
	{
		this.content = node;
		
		// Create scene & Set scene for the stage
        setScene( createScene() );
        
        // Set form title
        setTitle( title );

        // Initialize form owner, if specified
        if ( owner != null )
        	initOwner( owner );
        
        initModality( Modality.NONE );
        
        // Display and wait of click button event
        showAndWait();
    }
	
	/**
	 * Displays provided content
	 * @param node Content to display
	 * @param width Window width
	 * @param height Window height
	 */
	public void display( Node node, double width, double height )
	{
		this.width = width;
		this.height = height;
		
		display( node );
	}
	
	/**
     * Creates Scene object for the dialog
     * @return Created Scene object
     */
    protected Scene createScene( Node content )
    {
        StackPane stackPane = new StackPane();
        
        // Add content to Pane object
        stackPane.getChildren().add( content );
        
        // Set alignment to Center
        StackPane.setAlignment( content, Pos.CENTER );
        Scene scene;
        
        if ( width > 0 && height > 0 )
        	scene = new Scene( stackPane, width, height );
        else
        	scene = new Scene( stackPane );
        
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
        
        // Make pane focus traversable
        stackPane.setFocusTraversable( true );
		
        return scene;
        
    } // End of method ** createScene **
	
    /**
     * Creates Scene object with default content
     * @return Scene object
     */
    private Scene createScene()
    {
		return createScene( content );
    }
	
    /**
     * Class constructor with window title
     * @param title Title to display
     */
	public NodeView( String title )
	{
    	this.owner = Controller.getStage();
		this.title = title;
	}
    
	/**
	 * Class constructor with Canvas to display
	 * @param canvas Canvas object
	 */
    public NodeView( Canvas canvas )
	{
		this( "" );
	}
    
    /**
     * Class constructor with window title, width and height
     * @param title Title to display
     * @param width Window width
     * @param height Window height
     */
    public NodeView( String title, double width, double height )
    {
    	this.owner = Controller.getStage();
		this.width = width;
    	this.height = height;
    	this.title = title;
   }
    
    /**
     * Class constructor with window width and height
     * @param width Window width
     * @param height Window height
     */
    public NodeView( double width, double height )
    {
    	this( "", width, height );
    }
    
} // End of class ** NodeView **