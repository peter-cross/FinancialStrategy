package interfaces;

import java.util.ArrayList;

/**
 * Interface ItemsList
 * @author Peter Cross
 */
public interface ItemsList 
{
    static  ArrayList  list = new ArrayList<>();  // List of Items
    
    /**
     * Gets items list
     * @return Items list in ArrayList
     */
    public static ArrayList getItemsList()
    {
        return list;
    }
    
    /**
     * Creates array for a list
     * @return Array of ArrayList
     */
    public static ArrayList[] createList()
    {
        return new ArrayList[] { list };
    }
    
} // End of interface ** ItemsList **