package application;

import javafx.stage.Stage;
import javafx.event.Event;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import views.NodeView;
import views.RegistryView;
import interfaces.Constants;
import interfaces.Utilities;
import foundation.AssociativeList;

import static interfaces.Buttons.newMenuButton;

import entities.HashMap;

/**
 * Class Controller - GUI controller implementation
 * @author Peter Cross
 *
 */
public class Controller implements Constants, Utilities
{
	private static RegistryView  primaryView;	// Primary view stage
    private static RegistryView  secondaryView;	// Secondary view stage
    private static Stage 		 stage;			// Stage object for displaying GUI
	
    // List of registries to display menu items
    private static AssociativeList registries;
    
    /**
     * Class constructor
     * @param st Stage to display GUI
     */
    private Controller( Stage st )
	{
		stage = st;
		
		setRegistries();
		
		// Open TrActn Models Journal first
		openTransactionModelsJournal();
	}
	
    // Returns instance of controller
    public static Controller getInstance( Stage st )
	{
		return new Controller(st);
	}
	
    // Returns Application Stage object where GUI is displayed
    public static Stage getStage()
    {
    	return stage;
    }
    
    // Opens TrActn Models Journal without any events
    private void openTransactionModelsJournal()
    {
    	if ( secondaryView != null )
	    	// Hide Secondary View
	    	secondaryView.close();
    	
    	displayRegistry( "Transactions Simulation Models", primaryView );
    }
    
    /**
     * Displays a registry in a window
     * @param registryName Name of a registry to display
     */
    public static void displayRegistry( String registryName )
    {
    	displayRegistry( registryName, secondaryView );
    }
    
    /**
     * Displays a registry in a window
     * @param registryName Name of a registry to display
     * @view View to display
     */
    public static void displayRegistry( String registryName, RegistryView  view )
    {
        try
        {
            // Get Registry object for specified registry name and display it
        	view = registries.get( registryName );
        	view.display( WIDTH, HEIGHT );
        }
        catch ( Exception e ) { }
    }
    
    /**
     * Displays a registry in a window
     * @param registryName Name of a registry to display
     * @param width Window width
     */
    public static void displayRegistry( String registryName, int width )
    {
    	displayRegistry( registryName, width, secondaryView );
    }
    
    /**
     * Displays a registry in a window
     * @param registryName Name of a registry to display
     * @param width Window width
     * @view View to display
     */
    public static void displayRegistry( String registryName, int width, RegistryView  view )
    {
        try
        {
            // Get Registry object for specified registry name and display it
        	view = registries.get( registryName );
        	view.display( width );
        }
        catch ( Exception e ) { }
    }
    
    /**
     * Creates Button object for invoking Menu
     * @return Create Button object
     */
    private ButtonBase btnReferences()
    {
    	SubMenu menu = new SubMenu( "" );
    	
    	MenuItem[] menuItems = new MenuItem[7];
    	menuItems[0] = menu.createMenuItem( "Legal Entities", WIDTH * 0.95 );
    	menuItems[1] = menu.createMenuItem( "List of Charts Of Accounts", WIDTH * 0.5 );
    	menuItems[2] = menu.createMenuItem( "Charts Of Accounts", WIDTH * 0.5 );
    	menuItems[3] = menu.createMenuItem( "Currencies", WIDTH * 0.4 );
    	menuItems[4] = menu.createMenuItem( "Transactions Descriptions", WIDTH * 0.5 );
    	menuItems[5] = menu.createMenuItem( "Clock", this::clock );
    	menuItems[6] = menu.createMenuItem( "About", this::about );
    	
    	// Create Menu Button and return it
    	return newMenuButton( "Menu", menuItems );
    }
    
    /**
     * Sets registries to display through menu
     */
    private void setRegistries()
    {
    	registries = new AssociativeList();
    	
    	registries.set( "Transactions Simulation Models", 	new RegistryView( stage, "Transactions Simulation Models", "TractnsSimulationModel", "LglEntityModel", btnReferences() ) );
    	registries.set( "Legal Entities",  	  				new RegistryView( stage, "Legal Entities", "LglEntityModel" ) );
    	registries.set( "List of Charts Of Accounts", 		new RegistryView( stage, "List of Charts Of Accounts", "ChOfAccsModel" ) );
    	registries.set( "Charts Of Accounts", 				new RegistryView( stage, "Charts Of Accounts", "GLAcctModel", "ChOfAccsModel" ) );
    	registries.set( "Currencies", 						new RegistryView( stage, "List of Currencies", "CrcyModel" ) );
    	registries.set( "Transactions Descriptions", 		new RegistryView( stage, "List of Transactions Descriptions" , "TractnsDscrModel" ) );
    }
    
    /**
     * Displays About window
     * @param e Event
     */
    private void about( Event e )
    {
    	Utilities.displayAbout();
    }
    
    /**
     * Invokes Clock to display
     * @param e Event
     * @return Dialog object
     */
    private NodeView clock( Event e )
    {
    	NodeView dlg = new NodeView( "Clock", 480, 480 );
    	dlg.display( Clock.getInstance() );
    	
    	return dlg;
    }    
}