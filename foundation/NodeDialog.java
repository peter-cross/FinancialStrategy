package foundation;

import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;

import java.net.URL;

import application.Main;

/**
 * Class NodeDialog - basic dialog form
 * @author Peter Cross
 *
 */
public class NodeDialog extends Stage 
{
	/*********************  Properties  **************************************************************************/
	private Node content;	// Content to display
	private Stage owner;	// Parent window
	
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
        
        setTitle( title );

        if ( owner != null )
        	initOwner( owner );
        
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
        
        stackPane.getChildren().add( content );
        
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

        return scene;
        
    } // End of method ** createScene **
	
    private Scene createScene()
    {
		return createScene( content );
    }
	
    /**
     * Class constructor with window title
     * @param title Title to display
     */
	public NodeDialog( String title )
	{
    	this.owner = Main.stage;
		this.title = title;
	}
    
	/**
	 * Class constructor with Canvas to display
	 * @param canvas Canvas object
	 */
    public NodeDialog( Canvas canvas )
	{
		this( "" );
	}
    
    /**
     * Class constructor with window title, width and height
     * @param title Title to display
     * @param width Window width
     * @param height Window height
     */
    public NodeDialog( String title, double width, double height )
    {
    	this.owner = Main.stage;
		this.width = width;
    	this.height = height;
    	this.title = title;
   }
    
    /**
     * Class constructor with window width and height
     * @param width Window width
     * @param height Window height
     */
    public NodeDialog( double width, double height )
    {
    	this( "", width, height );
    }
}