package interfaces;

import javafx.stage.Stage;
import models.RegistryModel;
import views.OneColumnView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Optional;
import java.util.Scanner;

import application.Main;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.text.SimpleDateFormat;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

import forms.DialogElement;
import foundation.AssociativeList;
import foundation.Item;
import interfaces.Lambda.ElementValidation;

/**
 * Interface Utilities
 * @author Peter Cross
 *
 */
public interface Utilities extends Encapsulation
{
	
	/**
     * Displays About box when the program starts
     */
    default void openAboutBox()
    {
    	try 
    	{
			Thread.sleep(100);
		} 
    	catch (InterruptedException e) 
    	{}
    	
    	displayAbout();
    }
    
    /** 
    * Displays About box
     */
    default void displayAbout()
    {
    	String[] boxTxt = new String[]{ "                     " + Main.TITLE 
				  + "\n\n",
				  "Developer: Peter Cross",
				  "email: peter.cross@email.com" };
    	displayMessage( boxTxt );
    }
    
    
	/**
	 * Invokes dialog for entering text info
	 * @return Entered text info
	 */
	public static String enterTextInfo( Stage owner, String infoType  )
	{
		// Create dialog element for dialog field
		DialogElement dlg = new DialogElement( infoType );
		
		// Invoke OneColumnDialog window and return entered information
		String[][] result = new OneColumnView( owner, "Enter " + infoType, new DialogElement[]{dlg} ).result();
		
		// If there is entered information - return it, otherwise - just return empty string
		return result != null ? result[0][0] : "";
	}
	
	/**
	 * Rounds double number
	 * @param number Number to round
	 * @param decimals Precision of rounding
	 * @return Rounded double number
	 */
	public static double round( double number, int decimals )
    {
    	double powerOfTen = Math.pow(10, decimals);
    	
    	return (double) Math.round( number * powerOfTen ) / powerOfTen;
    }
	
	/**
	 * Gets field text value
	 * @param fields Fields attribute of data document
	 * @param fieldName Field name
	 * @return Text value of the field
	 */
	public static String fieldTextValue( AssociativeList fields, String fieldName )
    {
        return ( fields.get( fieldName )!= null ? "" + fields.get( fieldName ) : "" );
    }
    
    /**
     * Gets attribute name for a dialogue element
     * @param dlg Dialog Element
     * @return Name of form attribute
     */
    public static String attrName( DialogElement dlg )
    {
        String name = "";
        
        if ( dlg != null ) 
            // If there is attribute name specified
            if ( dlg.attributeName != null && !dlg.attributeName.isEmpty() ) 
                name = dlg.attributeName;
            // Otherwise - assume it's the label name
            else 
            {
                name = dlg.labelName.trim().replace( " ", "" );
                
                // Convert 1st character to lower case
                name = name.substring(0, 1).toLowerCase() + name.substring(1);
            }
        
        return name;
    }
    
    /**
     * Gets list element by index number
     * @param list List of Registry Items
     * @param index Index number
     * @return Registry Item
     */
    public static RegistryModel getByIndex( LinkedHashSet<RegistryModel> list, int index )
    {
        RegistryModel itm = null;
        
        Iterator it = list.iterator();
        int i = 0;
        
        while ( it.hasNext() )
            if ( i++ == index )
                itm = (RegistryModel) it.next();
            else
                it.next();
                
        return itm;
    }
    
    /**
     * Gets List element by value of field name
     * @param list  List from which to get a field
     * @param field Field name
     * @param value Value to find
     * @return RegistryModel descendant object
     */
    public static RegistryModel getListElementBy( HashSet list, String field, String value )
    {
        RegistryModel elm;
        AssociativeList fields;
        
        Iterator it = list.iterator();
        
        // Loop through the list
        while ( it.hasNext() )
        {
            // Get Item from List and cast to RegistryModel
            elm = (RegistryModel) it.next();
            
            // Get fields attribute of the RegistryModel
            fields = ((RegistryModel) elm).getFields();
            
            String fieldValue = ((String) fields.get(field)).trim();
            
            // If field value matches
            if ( fieldValue.equals(value) )
                return elm;
        }
        
        return null;
    }
    
    /**
     * Get numeric number of a field
     * @param fieldname Name of the field
     * @return String value of the field
     */
    default String getFieldNumber( String fieldname )
    {
        // Get attribute field
        AssociativeList fields = ((RegistryModel) this).getFields();
        
        if ( fields == null )
            return "";
        
        // Get field value
        Object obj = fields.get( fieldname );
        
        if ( obj == null )
            return "";
        else 
            try
            {
                // If object is convertible to double value
                if ( (double) obj > 0 )
                    // If there are decimals in the number
                    if ( (double) obj > Math.floor( (double) obj ) )
                        // Return number with decimals
                        return "" + (double) obj;
                    else
                        // Reurn number as integer
                        return "" + (int)((Double) obj).intValue();
            }
            catch ( Exception e )
            {
                // If object is convertible to int
                if ( (int) obj > 0 )
                    // Return number as integer
                    return "" + (int) obj;
                else
                    return "";
            }
            
        return "";
        
    } // End of method ** getFieldNumber **
    
    /**
     * Creates object of Class related to program Data
     * @param valueType String containing class name
     * @return  Class object
     */
    public static Class createModelClass( String valueType )
    {
        return tryCreateModelClass( "", valueType );
    }
    
    /**
     * Creates object of Class related to program Data
     * @param pckg Program package where the class might be located
     * @param valueType String containing class name
     * @return  Class object
     */
    static Class tryCreateModelClass( String pckg, String valueType )
    {
        Class cls = null; // To store created class 
        
        if ( pckg.isEmpty() )
            try 
            {   
                // Create class object by its name of data object
                cls = Class.forName( valueType );
            }
            catch ( Exception e )
            {
                // Try invoke method again for package data
                return tryCreateModelClass( "models", valueType );
            }
        else if ( !pckg.contains(".") )
            try 
            {   
                // Create class object by its name of data object
                cls = Class.forName( pckg + "." + valueType );
            }
            catch ( Exception e )
            {
                try
                {
                    // Try invoke method again for subpackage references
                    cls = Class.forName( pckg + ".references." + valueType );
                }
                catch ( Exception e1 )
                {
                    try
                    {
                        // Try invoke method again for subpackage references
                        cls = Class.forName( pckg + ".dimensions." + valueType );
                    }
                    catch ( Exception e2 )
                    {
                        try
                        {
                            cls = Class.forName( pckg + ".accgroups." + valueType );
                        }
                        catch ( Exception e3 )
                        { }    
                    }                
                }
            }
        else
            try 
            {   
                // Create class object by its name of data object
                cls = Class.forName( pckg + "." + valueType );
            }
            catch ( Exception e )
            { }
        
        return cls;
        
    } // End of method ** tryCreateModelClass **
    
    /**
     * Sets attribute list for the object
     */
    default void setAttributesList( )
    {
        // Get class object
        Class c = this.getClass();
        
        // Get object's attribute list
        AssociativeList attributesList = ((RegistryModel) this).getAttributesList();
        
        // Get declared fields
        Field[] field = c.getDeclaredFields();
        
        // Loop for each field
        for ( Field fld : field ) 
            try 
            {
                // Set field values in attributes list under their names
                attributesList.set( fld.getName(), this.get( fld.getName() ) );
            }
            catch ( Exception e )
            {}
        
    } // End of method ** setAttributesList **
    
    /**
     * Creates lambda expression for validating fields of dialog forms
     * @param field Field name
     * @return Lambda expression for pair of field and operation 
     */
    default ElementValidation validationCode( String field )
    {
    	return ( String value ) ->  // Value - the field value to verify
        {
            if ( value == null )
                value = "";
            else
                value = value.trim(); // Remove spaces from both sides

            switch ( field )
            {
                default:
                    // If input is empty string
                    if ( value.isEmpty() )
                        return validationMessage( "The '" + field + "' field should not be empty " );
            }

            // Everything is good
            return true;

        }; // End of lambda expression
        
    } // End of method ** validationCode **
    
    /**
     * Displays a message that dialog element didn't pass validation
     * @param msg Message to display
     * @return False always
     */
    default boolean validationMessage( String msg )
    {
    	// Display message
        displayMessage( msg );

        return false;
    }
	
    /**
     * Saves content of string to a file
     * @param output String to save to a file
     * @param prompt Prompt message
     */
    default boolean saveToFile( Stage form, String output, String prompt )
    {
        String      filename; 	// To store entered file name
        File        file;	// To store object for working with file
        boolean     condition;	// To store condition 
        PrintWriter pw;		// To store output stream object
        String[][]  result;	// To store result from input dialog
 		
        // Create array for dialog elements
        DialogElement[] el = new DialogElement[1];

        // Create dialog element with label File name
        el[0] = new DialogElement( "File name" );
        el[0].valueType = "FileSave";

        // Assign lambda expression to validate the dialog element
        el[0].validation = ( String val ) -> 
        {
            val = val.trim();	// Remove spaces from both sides

            // If input is empty string
            if ( val.isEmpty() )
            {
                displayMessage( "The fie name should not be empty " );
                return false;
            }

            // Everything is good
            return true;

        }; // End of lambda expression
 		
        // If there is something to save
        if ( !output.isEmpty() )
        {
            // Loop
            do
            {
            	// Display Dialog window with dialog elements defined above and get input results as a string array
                result  = new OneColumnView( form, prompt, el ).result();

                // If there is valid input from dialog
                if ( result != null )
                    // Get file name from 1st array element
                    filename = result[0][0];
                else
                    // If Cancel button was pressed
                    return false;
         		
                // If filename does not contain extension
                if ( !filename.contains(".") )
                    // Add to file name extension of text file
                    filename += ".txt";

                // Create File object to work with file
                file = new File(filename);

                // If file exists
                if ( file.exists() )
                {
                    // Display a message 
                    displayMessage( "", String.format( "The file %s already exists. \n Type another one ...", filename ) );
                    
                    // Assign condition for loop
                    condition = file.exists();	
                }
                else
                    condition = false;

            } while ( condition );

            try 
            {
                // Create PrintWriter object to work with output stream
                pw = new PrintWriter(file);

                // Write output string to file
                pw.print( output.trim() );

                // Close output stream
                pw.close();
                
                return true;
            } 
            catch ( FileNotFoundException e ) 
            {                
                return false;
            }
            
        } // End if ** output **
        
        return false;
        
    } // End of method ** saveToFile **
	
    /**
     * Counts the number of non-empty array elements
     * @param arr Array object
     * @return Number of non-empty array elements
     */
    static int arrayCount( Object[] arr )
    {
        int i = arr.length;

        while ( i > 0 && arr[i-1] == null )
            i--;

        return i;
    }
	
    /**
     * Displays message in a dialog box
     * @param msg Message to display
     */
    default void displayMessage( String[] msg ) 
    {
    	// If it's a valid array
        if ( msg == null )
            return;
        
        // If only one message line in array
        else if ( arrayCount( msg ) == 1 )
            displayMessage( msg[0] );
        else
        {
            String output = ""; // To store output string

            // Loop for each element of the string array
            for ( int i = 1; i < msg.length; i++ )
                // If string is specified and not empty
                if ( msg[i] != null && !msg[i].isEmpty() )
                {
                    // Add message string to output
                    output += msg[i];

                    // If it does not contain end-of-line sign
                    if ( !msg[i].contains( "\n" ) )
                        // Add end-of-line symbol if it's not the last line
                        output += ( i < msg.length-1 ? "\n" : "" );
                }
		
            // Display output string in message box
            displayMessage( msg[0], output );
        }
			
    } // End of method ** displayMessage **
    
    /**
     * Displays message in a dialog box
     * @param msg Message to display
     */
    default void displayMessage( String msg ) 
    {
    	displayMessage( "", msg );
    }
	
    /**
     * Displays message in a dialog box
     * @param header Header of the message
     * @param msg Message to display
     */
    default void displayMessage( String header, String msg ) 
    {
        // If message string is specified and not empty
        if ( msg != null && !msg.isEmpty() )
        {
            // Create Alert box 
            Alert alert = new Alert( AlertType.INFORMATION );

            alert.setTitle( "" );
            // Set header
            alert.setHeaderText( header );
            // Set message as alert box content
            alert.setContentText( msg );

            // Display and wait
            alert.showAndWait();	
        }
		
    } // End of method ** displayMessage **
    
    /**
     * Displays available choices as buttons and gets selected choice
     * @param promptMsg Prompt message to display
     * @param options Available options array
     * @return Selected option index
     */
    default int getChoice( String promptMsg, Object[] options )
    {
        // Create Alert box 
        Alert alert = new Alert( AlertType.CONFIRMATION );
        
    	alert.setTitle( "" );
        // Set prompt message as header text
    	alert.setHeaderText( promptMsg  );
     	alert.setContentText( "" );
     	
     	// Create array for buttons
     	ButtonType[] button = new ButtonType[options.length];
     	
     	// Loop for each choice option
     	for ( int i = 0; i < options.length; i++ )
            // If option is specified
            if ( options[i] != null )
            {
                // Create button with choice option
                button[i] = new ButtonType( (String)options[i] );
                // Add choice button
                alert.getButtonTypes().set( i, button[i] );
            }
     	
     	// Display alert box and wait
     	Optional<ButtonType> result = alert.showAndWait();
    	
     	// Loop for each option choice
     	for ( int i = 0; i < options.length; i++ )
            // If result is one of the buttons
            if ( result.get() == button[i] )
                // Return button index
                return i;
     	
     	return -1;
    	
    } // End of method ** getChoice **
    
    /**
     * Gets Yes/No confirmation from user
     * @param promptMsg Statement to confirm
     * @return Selected choice
     */
    default int getYesNo( String promptMsg )
    {
        // Get selected choice and return it
    	return getChoice( promptMsg, new String[] { "Yes", "No" } );
        
    } // End of method ** getYesNo **
    
    /**
     * Checks if the string date is a valid date
     * @param date String containing date
     * @return True if it's a valid date or false otherwise
     */
    default boolean isValidDate( String date )
    {
        // If date is not specified
        if ( date.isEmpty() )
            return false;

        // If can not recognize that date is specified in YYYY-MM-DD format
        if ( !date.contains("-") )
            return false;

        // Create scanner object to parse string
        Scanner in = new Scanner( date ).useDelimiter( "-" );

        // Get year from string
        int year = Integer.parseInt( in.next() );

        // Get month from string
        int month = Integer.parseInt( in.next() );

        // Get day from string
        int day = Integer.parseInt( in.next() );

        // Close Scanner
        in.close();
		
        // If valid month and day numbers
        if ( month >= 1 && month <= 12 && day >= 1 && day <= 31 )
        {
            // If month is Apr, Jun, Sep or Nov
            if ( month == 4 ^ month == 6 ^ month == 9 ^ month == 11 )
            {
                // 30 days in Apr, Jun, Sep, Nov
                return day <= 30;
            }
            // If Feb
            else if ( month == 2 )
            {
                // 28/29 days in Feb
                if ( day <= 29 )
                {
                    // If it's a leap year at the beginning of century
                    if (  year%100 == 0 && year%400 != 0 && day <= 29  )
                        return true;
                    // If it's a regular leap year
                    else if ( year%4 == 0 && day <= 29  )
                        return true;
                    // If just a regular year
                    else 
                        return day <= 28;
                }
                else
                    return false;
            }
            // Any other month
            else
                return true;
        }
        // If month or day is not a valid number
        else
            return false;

    } // End of method ** isValidDate **
	
    /**
     * Gets current date
     * @return String containing current date
     */
    default String currentDate()
    {
        // Create SimpleDateFormat object with template YYYY-MM-DD
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");

        // Format current date according to template and return it
        return ft.format( new Date() );
    }
	
    /**
     * Gets current time
     * @return String containing current time
     */
    default String currentTime()
    {
        // Create SimpleDateFormat object with template HH:MM:SS AM/PM ZONE
        SimpleDateFormat ft = new SimpleDateFormat("hh:mm:ss a zzz");

        // Return current time according to template
        return ft.format( new Date() );
    }
	
    /**
     * Allocates additional memory for an array
     * @param arr Array to allocate memory
     * @return New array reference
     */
    @SuppressWarnings("unchecked")
    default <T> T[] allocateMemory( T[] arr )
    {
        // Verify array length to make sure it's not zero
        int length = ( arr.length > 0 ? arr.length : 1 );

        // Create new array object
        T[] newArr;

        try 
        {
            // Get array class
            Class<? extends Object> classType = arr[0].getClass();

            // Allocate memory for new array with specified class type
            newArr = (T[]) Array.newInstance( classType, length*2 );
        } 
        catch ( Exception e ) 
        {
            // Allocate memory for new array without specifying class type
            newArr = (T[]) new Object[ length*2 ];
        } 

        // Copy current array to new array
        System.arraycopy( arr, 0, newArr, 0, arr.length );

        // Return reference to new array
        return newArr;

    } // End of method ** allocateMemory **
	
    /**
     * Allocates additional memory for an array
     * @param arr Array of double numbers to allocate memory
     * @return New array reference
     */
    default double[] allocateMemory( double[] arr )
    {
        int length = ( arr.length > 0 ? arr.length : 1 );

        // Create new array with double length of current size
        double[] newArr = new double[ length * 2 ];

        // Copy current array to new array
        System.arraycopy( arr, 0, newArr, 0, arr.length );

        // Return reference to new array
        return newArr;

    } // End of method ** allocateMemory **
	
    /**
     * Searches a string in sorted array
     * @param arr Array to search in
     * @param key Key to search
     * @return Index of array element with found key or -1 if not found
     */
    default int search( Item[] arr, String key )
    {
        // Size of one string chunk
        int chunkLength = (int) Math.ceil( Math.sqrt(arr.length) ); 

        // Result of string comparison
        int comparisonResult;

        // Minimum value of comparison
        int minComparisonResult = -1000;

        // Pointer on minimum value of comparison
        int pointer = 0;

        String name;

        // 1st iteration for comparing 1st element of each chunk
        for ( int i = 0; i < arr.length; i += chunkLength )
        {
            // Get array element name
            name = arr[i].get( "name" );

            // Result of comparison of array string to key 
            comparisonResult = name.compareToIgnoreCase(key);

            // If strings are equal
            if ( comparisonResult == 0 )
            {
                // Return the array current index
                return i;
            }
            // If the key is inside the current array chunk
            else if ( comparisonResult < 0 && comparisonResult > minComparisonResult )
            {
                // Save the result of comparison
                minComparisonResult = comparisonResult;

                // Save the array index
                pointer = i;

            } // End if

        } // End for loop for 1st iteration
		
        // 2nd iteration
        for ( int i = pointer+1; i < Math.min( pointer+chunkLength-1, arr.length ); i++ )
        {
            // Get array element name
            name = arr[i].get( "name" );

            // If the key is found
            if ( name.compareToIgnoreCase(key) == 0 )
                // Return the index of current position
                return i;
        }

        // If the key is not found - return -1
        return -1;
	
    } // End of method ** search **
	
    /**
     * Searches for string in an array
     * @param arr Array of strings to look at
     * @param key String to find
     * @return The array position in which the requested string was found
     */
    default int indexOf( String[] arr, String key )
    {
        // If array is not created
        if ( arr.length == 0 )
            return -1;

        int i = 0; // Loop counter

        // Loop while the search string is found or the end of list
        while ( i < arr.length )
        {
            // If empty array element
            if ( arr[i] == null )
            {
                // Increment loop counter and continue
                i++;
                continue;	
            }
			
            // If current element is different from search key
            if ( arr[i].compareToIgnoreCase( key ) != 0 )
                i++; // Increment loop counter

            // If key is found - return current index
            else
                return i;
        }

        // Return -1 if key is not found
        return -1;

    } // End of method ** indexOf **
	
    /**
    * Sorts array by selection sort method
    * @param arr Array of Item elements to sort
    */
    default void selectionSort( Item[] arr, int arrLength )
    {      
        String name;

        // Loop for each array element
        for ( int i = 0; i < arrLength; i++ )
           // Loop for each array element after the current element in outer loop
           for( int j = i+1; j < arrLength; j++ )
           {
                // Get array element name
                name = arr[i].get( "name" );

                // If element at position i is greater than element at position j
                if ( name.compareToIgnoreCase( arr[j].get( "name" ) ) > 0 )
                     // Swap elements at positions i and j
                     swap( arr, i, j );
           }

    } // End of method ** selectionSort **
   
   /**
    * Swaps array elements
    * @param arr Array to swap in
    * @param i First element to swap
    * @param j Second element to swap
    */
    default void swap( Object[] arr, int i, int j )
    {
        // Store value of array element at position i
        Object val = arr[i];

        // Assign element at position j to element at position i
        arr[i] = arr[j];

        // Assign stored array element to array element at position j
        arr[j] = val;
   
    } // End of method ** swap **

    /**
     * Swaps array elements
     * @param arr Array to swap in
     * @param i First element to swap
     * @param j Second element to swap
     */
    default void swap( double[] arr, int i, int j )
    {
        // Store value of array element at position i
        double val = arr[i];

        // Assign element at position j to element at position i
        arr[i] = arr[j];

        // Assign stored array element to array element at position j
        arr[j] = val;
   
    } // End of method ** swap **
	
} // End of interface ** Utilities **