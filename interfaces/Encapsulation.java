package interfaces;

import java.lang.reflect.Field;

/**
 * Interface Encapsulation - To get access to class object properties
 * @author Peter Cross
 */
public interface Encapsulation extends Cloneable
{
    /**
     * Gets property value of an object
     * @param property Property name
     * @return Property value
     */
    @SuppressWarnings("unchecked")
    default <T> T get( String property )
    {
        Field field;

        try
        {
            // Get class field declared in class by property name
            field = getClass().getDeclaredField( property );

            // Get access attribute
            boolean access = field.isAccessible();

            // Make field accessible
            field.setAccessible( true );

            // Get field value
            T value = (T) field.get( this );
			
            // Restore field access attribute
            field.setAccessible( access );

            // Return property value
            return value;
        }
        catch ( Exception e )
        { 
            try
            {
                // Get inherited field property value
                field = getClass().getField( property );

                // Get access attribute
                boolean access = field.isAccessible();
				
                // Make field accessible
                field.setAccessible( true );

                // Get field value
                T value = (T) field.get( this );

                // Restore field access attribute
                field.setAccessible( access );

                // Return property value
                return value;
            }
            catch ( Exception e1 )
            {
                return null;
            }
        }
    } // End of method ** get **
	
    /**
     * Sets property value of an object
     * @param property Property name
     * @param value Property value to set up
     */
    default <T> void set( String property, T value )
    {
        Field field;

        try
        {
            // Get field declared inside class
            field = getClass().getDeclaredField( property );

            // Get access property value
            boolean access = field.isAccessible();

            // Make field accessible
            field.setAccessible( true );

            // Set field value
            field.set( this, value );
			
            // Restore access property attribute
            field.setAccessible( access );
        }
        catch ( Exception e )
        { 
            try
            {
                // Get inherited field
                field = getClass().getField( property );

                // Get access property value
                boolean access = field.isAccessible();

                // Make field accessible
                field.setAccessible( true );

                // Set field value
                field.set( this, value );

                // Restore access property attribute
                field.setAccessible( access );
            }
            catch ( Exception e1 ) { }
        }
		
    } // End of method ** set **
	
} // End of interface ** Encapsulation **