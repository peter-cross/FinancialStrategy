package models;

import javafx.stage.Stage;
import javafx.scene.control.TextField;

import javax.transaction.SystemException;

import java.util.Date;

import forms.DialogElement;
import forms.TableElement;
import forms.TableOutput;
import foundation.AssociativeList;
import foundation.Item;
import interfaces.Constants;
import interfaces.Utilities;
import views.OneColumnView;

import static interfaces.Utilities.createModelClass;

/**
 * Class RegistryItemModel - To create Items for Registry
 * @author Peter Cross
 *
 */
abstract public class RegistryItemModel extends Item implements Utilities, Constants
{
    protected AssociativeList	attributesList; // Attributes list   
    protected AssociativeList   fields;         // Items' fields
    protected String			tabName;		// Registry View Tab Name
    protected int				tabNum;			// Registry View Tab Number
    
    private String ITEM_TYPE = "";   
    
    // Assigns header and table values to corresponding object properties
    abstract protected void init( String[][] header, String[][][] table );
    
    // Creates array of header element parameters
    abstract protected DialogElement[][] createHeader();
    
    // Method stub for removing registry item from DB
    public void removeFromDB() throws Exception
    { 
    	return;
    }
    
    // Method stub for setting value of Registry View Tab Name
    protected void setTab( String tabName )
    {
    	return;
    }
    
    // Method stub for setting value of Registry View Tab Number
    protected void setTab( int tabNum )
    {
    	return;
    }
    
    // Creates array of table element parameters
    protected TableElement[][] createTable() 
    {
        return null;
    }
    
    // Displays dialog form
    public void display() throws IllegalStateException, SecurityException, SystemException 
    {
        attributesList.set( "header", createHeader() );
        attributesList.set( "output", getOutput() );
    }
    
    /**
     * Displays form for entering or editing model data
     * @return Output results of the form
     * @throws IllegalStateException
     * @throws SecurityException
     * @throws SystemException
     */
    public TableOutput getOutput() throws IllegalStateException, SecurityException, SystemException 
    {
        TableOutput result = new TableOutput();
        
        // Invoke One Column View form and get results of input
        result.header = new OneColumnView( this, ITEM_TYPE ).result();
        
        // Initialize model data with results of input
        init( result.header, null );
        
        return result;
    }
    
    /**
     * Returns attributes list for current object
     * @return Attributes list
     */
    public AssociativeList getAttributesList()
    {
        return attributesList;
    }
    
    /**
     * Gets Registry Item's fields list
     * @return Fields list
     */
    public AssociativeList getFields()
    {
        return fields;
    }
    
    /**
     * Gets string representation of class object
     * @return Registry Item's name
     */
    public String toString()
    {
        return (String) fields.get( "name" );
    }
    
    /**
     * Gets text value of dialog field
     * @param fieldName Dialog field name 
     * @return Text value of the field
     */
    protected String fieldTextValue( String fieldName )
    {
        return Utilities.fieldTextValue( fields, fieldName );
    }
    
    /**
     * Gets text value of dialog field with specified field type
     * @param fieldName Dialog field name 
     * @typeName Type name of the field
     * @return Text value of the field
     */
    protected String fieldTextValue( String fieldName, String typeName ) 
    {
        if ( typeName == "Number" )
        	return getFieldNumber( fieldName );
        
    	String textValue = "";
        
        try 
        {
        	Class c = createModelClass( typeName );
            
        	Object obj = fields.get( fieldName );
            
            if ( c!= null )
                textValue = (String) c.getMethod("toString").invoke(obj);
        }
        catch ( Exception e ) {}
       
        return textValue;
    }
    
    /**
     * Creates Fields object
     */
    protected void createFields()
    {
    	if ( fields == null )
            fields = new AssociativeList();
    }
    
    /**
     * Converts array of objects into string array
     * @param obj Array of objects
     * @return String array of objects text values
     */
    protected String[] convertToStringArray( Object[] obj )
    {
    	if ( obj == null || obj.length == 0 )
    		return new String[] {};
    	
    	if ( obj[0] instanceof String )
    		return (String[]) obj;
    	
    	else if ( obj[0] instanceof Date )
    		return convertToStringArray( (Date[]) obj );
    	
    	else if ( obj[0] instanceof Number )
    		return convertToStringArray( (Double[]) obj );
    	
    	else
    		return null;
    }
    
    /**
     * Converts array of Date values to string array
     * @param obj Array of Date values
     * @return String array
     */
    protected String[] convertToStringArray( Date[] obj )
    {
    	// If array is not specified
        if ( obj == null )
            return null;
        
        // If array is empty
        if ( obj.length == 0 )
            return new String[]{};
        
        // Create String array for output
        String[] result = new String[obj.length];
        
        for ( int i = 0; i < obj.length; i++ )
        	result[i] = fieldTextValue( obj[i].toString() );
        
        return result;
    }
    
    protected String[] convertToStringArray( String[] obj )
    {
    	return obj;
    }
    
    /**
     * Converts array of double values to string array
     * @param obj Array of double values
     * @return String array
     */
    protected String[] convertToStringArray( double[] obj )
    {
        // If array is not specified
        if ( obj == null )
            return null;
        
        // If array is empty
        if ( obj.length == 0 )
            return new String[]{};
        
        // Create String array for output
        String[] result = new String[obj.length];
        
        try 
        {
            // Try to convert 1st array element to Double object
            ((Double) obj[0]).toString();
            
            // Loop for every array object
            for ( int i = 0; i < obj.length; i++ )
            {
                // Convert value to int
                int val = (int) obj[i];
                
                // If original value is greater than int value
                if ( obj[i] > val )
                    // Convert original value to string and store in output array
                    result[i] = new String( ((Double) obj[i]).toString() );
                
                // If converted value is zero
                else if ( val == 0 )
                    // store in output array just empty string
                    result[i] = "";
                
                // Otherwise
                else
                    // Convert int value to string and store in output array
                    result[i] = new String( ((Integer) val).toString() );
                
            } // End of loop for every array object
            
        } // End of try clause
        catch ( Exception e1 )
        {
            // Return empty string array
            return new String[]{};
        }
        
        return result;
        
    } // End of method ** convertToStringArray **
    
    /**
     * Converts array of integer values to string array
     * @param obj Array of integer values
     * @return String array
     */
    protected String[] convertToStringArray( int[] obj )
    {
        // If array is not specified
        if ( obj == null )
            return null;
        
        // If array is empty
        if ( obj.length == 0 )
            return new String[]{};
        
        // Create String array for output results
        String[] result = new String[obj.length];
        
        try 
        {
            // Try to convert 1st array element to Integer object
            ((Integer) obj[0]).toString();
            
            // Loop for each array element
            for ( int i = 0; i < obj.length; i++ )
                // If array value is zero
                if ( obj[i] == 0 )
                    // Store in output array just empty string
                    result[i] = "";
                
                // Otherwise
                else
                    // Convert integer to string and store in output array
                    result[i] = new String( ((Integer) obj[i]).toString() );
        }
        catch ( Exception e )
        {
            // Return empty string array
            return new String[]{};
        }
        
        return result;
        
    } // End of method ** convertToStringArray **
    
    
    /**
     * Parses string containing double number
     * @param doubleNumber String with double number
     * @return Double number
     */
    protected double parseDouble( String doubleNumber  )
	{
		if ( doubleNumber == null || doubleNumber.isEmpty() )
			return 0;
		
		try
		{
    		return  Double.parseDouble( doubleNumber.replaceAll( ",", "" ) );
		}
		catch ( Exception e )
		{
			return 0;
		}
    }
	
    /**
     * Parses string containing int number
     * @param intNumber String with int number
     * @return Int number
     */
	protected int parseInt( String intNumber )
	{
		if ( intNumber == null || intNumber.isEmpty() )
			return 0;
		
    	try
		{
			return Integer.parseInt( intNumber.replaceAll( ",", "" ) );
		}
		catch ( Exception e )
		{
			return 0;
		}
	}
	
	/**
	 * Gets numeric value of a dialog text field containing double number
	 * @param textField Dialog Text Field object
	 * @return Double value
	 */
	protected Double getTextFieldValue( TextField textField )
	{
		double value = 0;
		
		if ( textField != null )
			value = parseDouble( textField.getText() );
		
		return value;
	}
	
	/**
	 * Sets TextField dialog object into text representation of provided object
	 * @param textField TextField dialog element
	 * @param value Object to show in TextField
	 */
	protected void setTextFieldValue( TextField textField, Object value )
	{
		if ( textField != null )
			textField.setText( value.toString() );
	}
	
	/*                        Constructors                                                                                  */
    /************************************************************************************************************************/
    private RegistryItemModel()
    {
    	createFields();
        
    	attributesList = new AssociativeList();
        attributesList.set( "header", createHeader() );
        attributesList.set( "table", createTable() );    
    }
    
    public RegistryItemModel( String itemType )
    {
        this();
        ITEM_TYPE = itemType;    
    }
    
    public RegistryItemModel( Stage stage, String[] headerTabs, String itemType ) throws Exception
    {
        this( stage, itemType, null );
        attributesList.set( "headerTabs", headerTabs );
    }
    
    public RegistryItemModel( Stage stage, String[] headerTabs, String[] tableTabs, String itemType ) throws Exception
    {
        this( stage, headerTabs, itemType );
        attributesList.set( "tableTabs", tableTabs );
    }
    
    
    public RegistryItemModel( String itemType, String tabName )
    {
    	this();
        ITEM_TYPE = itemType; 
        setTab( tabName );
    }
    
    public RegistryItemModel( Stage stage, String tabName ) throws Exception
    {
    	this();
    	setTab( tabName );
        
        if ( stage != null )
        {
        	attributesList.set( "stage", stage );
        	attributesList.set( "output", getOutput() );
            
        	if ( fields == null || fields.size() == 0 )
            	throw new Exception( "Empty document" );
        }
    }
    
    public RegistryItemModel( Stage stage, String itemType, String tabName ) throws Exception
    {
    	this( itemType, tabName );
        
        if ( stage != null )
        {
        	attributesList.set( "stage", stage );
        	attributesList.set( "output", getOutput() );
            
        	if ( fields == null || fields.size() == 0 )
            	throw new Exception( "Empty document" );
        }
    }
    
    public RegistryItemModel( Stage stage, String[] headerTabs, String itemType, String tabName ) throws Exception
    {
        this( stage, itemType, tabName );
        attributesList.set( "headerTabs", headerTabs );
    }
    
    public RegistryItemModel( Stage stage, String[] headerTabs, String[] tableTabs, String itemType, String tabName ) throws Exception
    {
        this( stage, headerTabs, itemType, tabName );
        attributesList.set( "tableTabs", tableTabs );
    }
    
} // End of interface ** RegistryItemModel ** 