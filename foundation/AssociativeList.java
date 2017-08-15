package foundation;

import java.util.ArrayList;

import interfaces.Utilities;

/**
 * Class AssociativeList  - to store list of data in format key -> value 
 * @author Peter Cross
 */
public class AssociativeList implements Utilities
{
    /*          Properties   	                                                                                      */
    /******************************************************************************************************************/
    private ArrayList<Data>     value;      // Associative list values
    private ArrayList<String>   key;        // Associative list keys
    
    /*          Methods                                                                                               */
    /******************************************************************************************************************/
    
    /**
     * Get size of Associative List
     * @return size of Associative List
     */
    public int size()
    {
        return key.size();
    }
    
   /**
     * Sets the pair: key - value for associative list
     * @param key Key value
     * @param value Value to set up
     */
    public void set( String key, Object value )
    {
        set( keyIndex(key) , value );

    } // End of method ** set **

    /**
     * Sets value by key index
     * @param index Key index
     * @param value Value to set up
     */
    public void set( int index, Object value )
    {
        if ( index >= this.value.size() )
            this.value.add( Data.create( value ) );
        else
            this.value.set( index, Data.create( value ) ); 
      
    } // End of method ** set **
	
    /**
     * Get Value by key
     * @param key String key
     * @return Value in associative list by key
     */
    public <T> T get( String key )
    {
        // Find the key index in the key array
        int index = this.key.indexOf( key );

        // If key is not found
        if ( index < 0 )
            return null;
        else
        {
        	// Find data value by index
            Data d = value.get( index );

            if ( d != null )
            	return (T) d.get();
            else
                return null;	
        }       
    } // End of method ** get **
	
    /**
     * Get value by key index
     * @param index Key index
     * @return Value found in associative array
     */
    public <T> T get( int index )
    {
        // If key is not valid
        if ( index < 0 )
            return null;

        return (T) value.get( index ).get();
        
    } // End of method ** get **
	
    /**
     * Get key by index number
     * @param index Index number in the list
     * @return String Key
     */
    public String getKey( int index )
    {
        return key.get(index);
    }
    
    /**
     * Get Associative List size
     * @return List Size
     */
    public int listSize()
    {
        return key.size();
    }
    
    /**
     * Remove value by key
     * @param key Value key to find
     */
    public void remove( String key )
    {
        // Find key index
        int index = this.key.indexOf( key );

        // If key is found
        if ( index >= 0 )
            // Remove value from the array
            this.value.remove(index);

    } // End of method ** remove **
	
    /**
     * Find key index
     * @param key Key to find
     * @return Key index
     */
    public int keyIndex( String key )
    {
        // Find key index
        int index = this.key.indexOf( key );

        // If key is found
        if ( index >= 0 )
            return index;
        
        // If not found
        else
        {
            // Add ket to the keys list
            this.key.add( key );
            
            // Return the position of added key
            return this.key.size() - 1;
        }
    } // End of method ** keyIndex **
	
    /*          Constructors                                                                                          */
    /******************************************************************************************************************/
    /**
     * Class default constructor
     */
    public AssociativeList()
    {
        value =  new ArrayList<>(); 
        key = new ArrayList<>(); 
    }
    
    /**
     * Class constructor
     * @param list List with values
     */
    public AssociativeList( ArrayList list )
    {
        value = list;
        key = null;
    }
    
} // End of class ** AssociativeList **