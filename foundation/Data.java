package foundation;

import interfaces.Encapsulation;
import static interfaces.Utilities.createDataClass;

/**
 * Class Data
 * @author Peter Cross
 *
 */
public class Data implements Encapsulation
{
    /*          Properties   	                                                                                      */
    /******************************************************************************************************************/
    protected Object value;

    /*          Methods                                                                                               */
    /******************************************************************************************************************/

    /**
     * Creates object of class Data
     * @param value Object to assign value to
     * @return Object of class Data
     */
    public static Data create( Object value )
    {
        return new Data( value );
    }
	
    /**
     * Gets value stored in object of this class
     * @return Object value
     */
    public Object get()
    {
        return value;
    }
	
    /**
     * Sets value for object of this class
     * @param value Object value to assign to
     */
    public void set( Object value )
    {
        this.value = value;
    }
    
    /**
     * Gets value property stored in object of this class
     * @return Object value
     */
    public Object valueProperty()
    {
        return value;
    }
	
    /**
     * Gets value property stored in object of this class
     * @return Object value
     */
    public Object getValue()
    {
        return value;
    }
		    	
    /**
     * Removes value of object of this class
     */
    public void remove()
    {
        this.value = null;
    }
	
    /*          Constructors                                                                                          */
    /******************************************************************************************************************/
    private Data ( String type )
    {
        try 
        {
            Class classType = createDataClass( type );
            this.value = classType.newInstance();
        } 
        catch ( Exception e ) 
        {
            this.value = new Object();
        }
    }

    private Data ( Object value )
    {
        this.value = value;
    }
	
} // End of class ** Data **