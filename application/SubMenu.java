package application;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import interfaces.Lambda.MenuItemAction;

/**
 * Class SubMenu - for creating arbitrary submenus 
 * @author Peter Cross
 *
 */
public class SubMenu extends Menu 
{
	/**
	 * Class constructor
	 * @param name Name that should appear in menu
	 */
	public SubMenu( String name )
    {
        super( name );
    }
    
	/**
	 * Adds Menu item
	 * @param itm MenuItem object to add to submenu
	 */
    public void addMenuItem( MenuItem itm )
    {
        getItems().add( itm );
    }
    
    /**
     * Adds menu item
     * @param itemName Item name to add
     * @param width Width of window where it will be displayed
     */
    public void addMenuItem( String itemName, double width  )
    {
        addMenuItem( itemName, e -> Controller.displayRegistry( itemName, (int) width ) );

    } // End of method ** addMenuItem **

    /**
     * Adds menu item
     * @param itemName Item name to add
     */
    public void addMenuItem( String itemName )
    {
        addMenuItem( itemName, e -> Controller.displayRegistry( itemName ) );
        
    } // End of method ** addMenuItem **

    /**
     * Adds menu item to the menu
     * @param menu Menu to add the item to
     * @param itemName Name of menu item
     * @param code Lambda expression with action to perform for menu item
     */
    public void addMenuItem( String itemName,  MenuItemAction code )
    {
        // Create menu item object
        MenuItem menuItem = new MenuItem( itemName );

        // Set up event handler on selection of Menu Item
        menuItem.setOnAction( code::run );

        // Add created menu item to the menu
        getItems().add( menuItem );

    } // End of method ** addMenuItem **
    
    /**
     * Creates menu item
     * @param itemName Name of menu item
     * @param width Width of window where item will be displayed
     * @return Created MenuItem object
     */
    public MenuItem createMenuItem( String itemName, double width )
    {
    	return createMenuItem( itemName, e -> Controller.displayRegistry( itemName, (int) width ) );
        
    } // End of method ** createMenuItem **

    /**
     * Creates menu item
     * @param itemName Name of menu item
     * @return Created MenuItem object
     */
    public MenuItem createMenuItem( String itemName )
    {
    	return createMenuItem( itemName, e -> Controller.displayRegistry( itemName ) );
        
    } // End of method ** createMenuItem **

    /**
     * Creates menu item
     * @param itemName Name of menu item
     * @param code Action to perform when menu item is clicked
     * @return Created MenuItem object
     */
    public MenuItem createMenuItem( String itemName,  MenuItemAction code )
    {
    	// Create menu item object
        MenuItem menuItem = new MenuItem( itemName );

        // Set up event handler on selection of Menu Item
        menuItem.setOnAction( code::run );
        
        return menuItem;
    }
}