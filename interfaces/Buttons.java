package interfaces;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public interface Buttons 
{
	/**
     * Creates new action button
     * @param btnName Name of the button
     * @param handler Event Handler for the button
     * @return Button object with Event Handler
     */
    default Button newActionButton( String btnName, EventHandler handler )
    {
    	// Create new button object
    	Button btn = new Button( btnName );
    	// Set button padding
        btn.setPadding( new Insets( 5, 20, 5, 20 ) );
        // Set event handler for press button event
        btn.setOnAction( handler );
        
        return btn;
    }
    
    /**
     * Adds buttons to Pane object of the Scene
     * @param btn Array of buttons
     */
    default void addButtonsToPane( Button[] btn, Pane pane )
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

        // Add OK and Cancel buttons to buttons box
        buttons.getChildren().addAll( btn );
		
        // Add buttons box to the pane
        pane.getChildren().addAll( buttons );
    }
 }
