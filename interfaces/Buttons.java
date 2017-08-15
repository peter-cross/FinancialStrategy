package interfaces;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

/**
 * Interface Buttons - Methods to add and manipulate buttons on Registry Forms
 * @author Peter Cross
 */
public interface Buttons 
{
	/**
     * Creates new action button
     * @param btnName Name of the button
     * @param handler Event Handler for the button
     * @return Button object with Event Handler
     */
    public static Button newActionButton( String btnName, EventHandler handler )
    {
    	// Create new button object
    	Button btn = new Button( btnName );
    	// Set button padding
        btn.setPadding( new Insets( 5, 20, 5, 20 ) );
        // Set event handler for press button event
        btn.setOnAction( handler );
        btn.setFocusTraversable( true );
		
        return btn;
    }
    
    /**
     * Creates menu button
     * @param btnName Button name
     * @param menuItems Menu items for button menu
     * @return Button menu
     */
    public static MenuButton newMenuButton( String btnName, MenuItem[] menuItems )
    {
    	MenuButton btn = new MenuButton( btnName, null, menuItems );
    	
    	// Set button padding
        btn.setPadding( new Insets( 1, 5, 1, 5 ) );
        
        btn.setFocusTraversable( true );
		
        return btn;
    }
    
    /**
     * Adds buttons to Pane object of the Scene
     * @param btn Array of buttons
     */
    default void addButtonsToPane( ButtonBase[] btn, Pane pane )
    {
    	final int BUTTONS_PADDING = 12;
    	final int BUTTONS_SPACING = 5;
    	
    	// Create horizontal box object
        HBox buttons = new HBox();

        // Set padding settings for the buttons
        buttons.setPadding( new Insets( BUTTONS_PADDING, BUTTONS_PADDING, BUTTONS_PADDING, BUTTONS_PADDING ) );
        buttons.setSpacing( BUTTONS_SPACING );

        // Align buttons box to the right
        buttons.setAlignment( Pos.CENTER_RIGHT );

        for ( ButtonBase b : btn )
            if ( b != null )
                buttons.getChildren().add( b );
		
        // Add buttons box to the pane
        pane.getChildren().addAll( buttons );
    }
 }