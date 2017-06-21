package foundation;

import interfaces.Encapsulation;

/**
 * Class List
 * @author Peter Cross
 */
public abstract class List implements Encapsulation
{
    /*          Properties   	                                                                                      */
    /******************************************************************************************************************/
    protected Item[] 	list;       // List array
    protected int 	pointer;    // List pointer

    /*          Methods                                                                                               */
    /******************************************************************************************************************/

    /**
     * Gets list reference
     * @return List reference
     */
    public Item[] getList()
    {
        return list;
    }
    
    /**
     * Gets List pointer
     * @return List pointer value
     */
    public int getListPointer()
    {
        return pointer;
    }
	
    /**
     * Searches for string in an array
     * @param key String to find
     * @return The array position in which the requested string was found
     */
    public int indexOf( String key )
    {
        // Get reference copy for the list
        Item[] arr = list;

        // If array is not created
        if ( arr.length == 0 )
            return -1;

        int i = 0; // Loop counter

        String name;

        // Loop while the search key is found or the end of list
        while ( i < arr.length )
        {
            // If empty array element
            if ( arr[i] == null )
            {
                // Increment loop counter and continue
                i++;
                continue;	
            }

            // Get array item name
            name = arr[i].get( "name" );

            // If current element is different from search key
            if ( name == null || name.compareToIgnoreCase( key ) != 0 )
                i++; // Increment loop counter

            // If key is found - return current index
            else
                return i;
        }

        // Return -1 if key is not found
        return -1;

    } // End of method ** indexOf **
	
    /**
     * Adds a string to a list
     * @param owner List owner
     * @param str String to add to the array
     * @return New pointer value
     */
    public int addToList( Object owner, String str )
    {
        // Get reference copy for the list
        Item[] arr = list;

        // Check if there is available space in the list
        if ( pointer < arr.length )
        {
            // Assign string to array element
            arr[pointer].set( "name", str );
            arr[pointer].set( "owner", owner );
            pointer++;
        }

        // Return new pointer
        return pointer;

    } // End of method ** addToList **
	
    /**
     * Removes a string from a list
     * @param str String to remove from the array
     * @return New pointer value
     */
    public int removeFromList( String str )
    {
        // Check if the array contains the required string
        int index = indexOf( str );

        // Get reference copy for the list
        Item[] arr = list;

        // If the string is found
        if ( index >= 0 )
        {
            // Loop for the rest of the list
            for ( int i = index+1; i < pointer-1; i++ )
                // Move array elements forward by 1 position
                arr[i-1] = arr[i];

            // Decrease pointer by 1 to indicate the released element
            pointer--;
        }

        // Return new pointer value
        return pointer;

    } // End of method ** removeFromList **
	
    /**
     * Clones class object
     */
    public Class<? extends List> clone()
    {
        try
        {
            return (Class<? extends List>) super.clone();
        }
        catch ( Exception e )
        {
            return null;
        }
    }
	
    /*          Constructor                                                                                         */
    /******************************************************************************************************************/
    public List()
    {
        pointer = 0;
        list = new Item[1];
    }
	
} // End of class ** List **