package forms;

import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Modality;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import java.net.URL;

import foundation.AssociativeList;
import foundation.Data;
import foundation.Item;
import foundation.TreeList;
import interfaces.Constants;
import interfaces.Encapsulation;

/**
 * Class TreeTableDialog - Displays Table dialog with Tree
 * @author Peter Cross
 *
 */
public class TreeDialog extends Stage implements Encapsulation, Constants
{
    protected Stage 	stage;          // Stage object for window
    protected String	title;          // Window title
    protected TreeList	nodes;          // Nodes to display in window
    protected Node	btn;            // Button which evoked this dialog
    protected String	returnValue;    // Selected value
    
    private int		width = 254,    // Window width
                        height = 150;   // Window heihgt
    /**
     * Creates items of Tree dialog
     * @param <T>   Type parameter
     * @param root  Root node
     * @param nodes List of nodes to display
     */
    private <T> void createTreeItems( TreeItem<T> root, TreeList nodes )
    {
        // Get list of items to display
        Item[] list = nodes.getList();
        
        // Get pointer value for display items
        int pointer = nodes.getListPointer();
        
        TreeList    curNodes;           // To store current nodes
        String      curItemValue;       // To store current item value

        foundation.TreeItem curItem;    // To store current item
        
        // Create array for Tree items
        TreeItem[]  treeItem = new TreeItem[pointer];

        // Loop for each display item
        for ( int i = 0; i < pointer; i++ )
        {
            // Get next item from display list
            curItem = (foundation.TreeItem ) list[i];
            // Get the String value of current display item
            curItemValue = (String) ((Data) curItem.get( "value" )).get();
            // Get list of children nodes for current display item
            curNodes = curItem.get( "nodes" );
            // Create TreeItem object for current display item
            treeItem[i] = new TreeItem<>( curItemValue );	
            
            // If there is a list of children nodes to display for current display item
            if ( curNodes != null && curNodes.getListPointer() > 0 )
                // Evoke method again with current display item as root node and list of children nodes to display
                createTreeItems( treeItem[i], curNodes );
            
        } // End of Loop ** for each display item **

        // Add array of Tree items to the root node
        root.getChildren().addAll( treeItem );
        
    } // End of method ** createTreeItems **
	
    /**
     * Creates tree of display tree items
     * @param <T> Type parameter
     * @return  Root item of created tree
     */
    private <T> TreeItem<T> createTree()
    {
        // Create TreeItem object for the Tree root with specified title
        TreeItem<T> root = (TreeItem<T>) new TreeItem<>( title );

        // Make Tree List expanded
        root.setExpanded( true );

        // Create Tree items for the Tree with specified root item and list of children nodes
        createTreeItems( root, nodes );

        // Return tree root item
        return root;
        
    } // End of ** createTree **
	
    /**
     * Creates content of Tree dialog window
     * @param <T> Type parameter
     * @return Parent node with content
     */
    @SuppressWarnings("unchecked")
    protected <T> Parent createContent()	
    {
        // Create tree to display and create tree view object with tree to display as a content
        TreeView<T> treeView = new TreeView<>( createTree() );
        // Make tree view non-editable
        treeView.setEditable( false );
        // Set up preferred heihgt  and width
        treeView.setPrefHeight( height );
        treeView.setMaxWidth( width );

        // Get selection model for current tree view
        MultipleSelectionModel model = treeView.getSelectionModel();

        // Set event listener for selecting tree item
        model.selectedItemProperty().addListener( (observable, newValue, oldValue) -> 
        {
            // Get current selected value of tree item
            TreeItem<String> item = (TreeItem<String>) oldValue;

            // Check if there are children nodes for current item
            ObservableList children = item.getChildren();

            // If there are no children nodes for current selected item
            if ( children.isEmpty() )
            {
                // Assign value of current selected item to returnValue property
                returnValue = item.getValue();
                
                // Close tree window dialog
                close();	
            }
        } ); // End of ** event listener for selecting tree item **

        // Return created tree view object
        return treeView;
        
    } // End of method *** createContent **
	
    /**
     * Creates Scene object for current Stage
     * @return Created Scene object
     */
    protected Scene createScene()
    {
        // If list of nodes empty
        if ( nodes == null )
            return null;

        // Create content for tree dialog and create scene object with created content inside
        Scene scene = new Scene( createContent() );

        // Get style sheet object from specified CSS file
        URL styleSheet = getClass().getResource( "/application/application.css" );

        // If specified CSS file found and style sheet object created
        if ( styleSheet != null )
        {
            // Get list of stylesheets for created Scene object
            ObservableList<String> style = scene.getStylesheets();

            // Add stylesheet to the scne's  list of stylesheets
            style.add( styleSheet.toExternalForm() );
        }

        // Return created scene object
        return scene;
        
    } // End of method ** createScene **
	
    /**
     * Displays Tree dialog form
     */
    protected void displayForm()
    {
        // Set title for current stage
        setTitle( title );

        // Create scene object with contenet
        Scene scene = createScene();

        // Set created scene as current scene
        setScene( scene );

        // Get Layout bounds object for button which evoked this tree dialog
        Bounds bounds = btn.getLayoutBounds();

        // Get Scene object for the button 
        Scene sc = btn.getScene();

        // Get coordinates of the button relative to scene
        Point2D point = btn.localToScene( bounds.getMinX(), bounds.getMinY() );
        // Get coordinates of button scene 
        Point2D sceneCoord = new Point2D( sc.getX(), sc.getY() );
        // Get coordinates of scene window
        Point2D windowCoord = new Point2D( sc.getWindow().getX(), sc.getWindow().getY() );

        // Calculate absolute coorinates of the button
        double x = point.getX() + sceneCoord.getX() + windowCoord.getX();
        double y = point.getY() + sceneCoord.getY() + windowCoord.getY();

        // Set coordinates for current Tree dialog window
        setX( x + bounds.getWidth()- width );
        setY( y + bounds.getHeight() );

        // Display Tree dialog window and wait for user to select tree item
        showAndWait();
        
    } // End of method ** displayForm **
	
    /**
     * Displays the form and returns the result of selection
     * @param <T> Type parameter
     * @param btn Button object which evoked this tree dialog
     * @return Selected tree item
     */
    public <T> T result( Node btn )
    {
        // Save evoked button reference
        this.btn = btn;

        // Display form
        displayForm();

        // Return selected value
        return (T) returnValue;
        
    } // End of method ** result **
	
    /**
     * Fills nodes with tree items
     * @param grpList Group List for nested items
     */
    private void fillNodesList( AssociativeList grpList )
    {
        // Get root items for the group list
        String[] items = (String[]) grpList.get( "root" );

        // Fill nodes list for current tree dialog
        fillNodesList( items, grpList, nodes );
        
    } // End of method ** fillNodesList **
	
    /**
     * Fills Nodes List with items
     * @param items List of items for current level
     * @param grpList Group List for nested items
     * @param tree Tree List to fill
     */
    private void fillNodesList( String[] items, AssociativeList grpList, TreeList tree )
    {
        String[] 	nodesList;  // List of nodes 
        Item[]		treeItem;   // List of tree items

        // Loop for each node
        for ( int i = 0; i < items.length; i++ )
        {
            // Get nodes list for current root item
            nodesList = (String[]) grpList.get( items[i] );

            // Add current root item to the tree
            tree.addItem( items[i] );

            // If nodes list for current item is not empty
            if ( nodesList != null )
            {
                // Get list of tree items
                treeItem = tree.getList();

                // Fill nodes list with specified nodes list and nested nodes for current tree item
                fillNodesList( nodesList, grpList, ((foundation.TreeItem) treeItem[i]).nodes );
            }
        } // End of Loop ** for each node **

    } // End of method ** fillNodesList **
	
        
    public TreeDialog( Stage stage, String title, AssociativeList grpList )
    {
        this.stage = stage;
        this.title = title;
        this.nodes = new TreeList();
        this.returnValue = "";

        this.initStyle( StageStyle.UNDECORATED );
        this.initModality( Modality.APPLICATION_MODAL  );

        fillNodesList( grpList );
    }
	
    public TreeDialog( Stage stage, String title, AssociativeList grpList, int width )
    {
        this( stage, title, grpList );

        this.width = width;
    }

    public TreeDialog( Stage stage, String title, AssociativeList grpList, int width, int height )
    {
        this( stage, title, grpList, width );

        this.height = height;
    }
	
} // End of class ** TreeTableDialog **