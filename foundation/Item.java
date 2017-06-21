package foundation;

import interfaces.Encapsulation;

/**
 * Class Item
 * @author Peter Cross
 *
 */
public abstract class Item implements Encapsulation 
{
    /*          Methods                                                                                               */
    /******************************************************************************************************************/

    /**
     * Clones objects inherited from class Item
     */
    public Class<? extends Item> clone()
    {
        try
        {
            // Invoke clone method from super class and cast to class type which extended Item class
            return (Class<? extends Item>) super.clone();	
        }
        catch ( Exception e )
        {
            return null;
        }
    }

} // End of class ** Item **