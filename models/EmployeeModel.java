package models;

import javafx.stage.Stage;
import java.util.LinkedHashSet;
import java.util.List;

import application.Database;
import entities.Employee;
import entities.Expense;
import forms.DialogElement;

import static interfaces.Utilities.getListElementBy;

/**
 * Class Employee 
 * @author Peter Cross
 */
public class EmployeeModel extends RegistryItemModel
{
    protected static  LinkedHashSet list;       // List of Items
    
    
    public String toString()
    {
        return (String) fields.get( "name" );
    }
    
    public static EmployeeModel getByEmployee( String e )
    {
        return (EmployeeModel) getListElementBy( list, "employee", e );
        
    } // End of method ** getByEmployee **
    
    
    public static EmployeeModel getByID( String ID )
    {
        return (EmployeeModel) getListElementBy( list, "iD", ID );
        
    } // End of method ** getByID **
    
    
    public static EmployeeModel getByName( String name )
    {
        return (EmployeeModel) getListElementBy( list, "name", name );
        
    } // End of method ** getByName **
    
    public Employee getEmployee()
    {
    	return fields.get( "employee" );
    }
    
    @Override
    protected DialogElement[][] createHeader() 
    {
        DialogElement[][] header = new DialogElement[1][4];
        
        DialogElement hdr;
        
        hdr = new DialogElement( "ID  " );
        hdr.valueType = "Number";
        hdr.width = 60;
        hdr.textValue = fieldTextValue( "iD" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][0] = hdr;
        
        hdr = new DialogElement( "Name     " );
        hdr.valueType = "Text";
        hdr.width = 200;
        hdr.textValue = fieldTextValue( "name" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][1] = hdr;
        
        hdr = new DialogElement( "Phone" );
        hdr.valueType = "Text";
        hdr.width = 200;
        hdr.textValue = fieldTextValue( "phone" );
        header[0][2] = hdr;
        
        hdr = new DialogElement( "Address         " );
        hdr.valueType = "Text";
        hdr.width = 300;
        hdr.textValue = fieldTextValue( "address" );
        header[0][3] = hdr;
    
        return header;
    }

    @Override
    protected void init( String[][] header, String[][][] table ) 
    {
        if ( header.length > 0 )
        {
            fields.set( "iD", header[0][0] );
            fields.set( "name", header[0][1]  );
            fields.set( "phone",  header[0][2] );
            fields.set( "address", header[0][3] );
        }
    }

    public static LinkedHashSet getItemsList()
    {
        return list;
    }
    
    public static LinkedHashSet[] createList()
    {
    	if ( list == null )
            createNewList();
        
        return new LinkedHashSet[] { list };
    }
    
    public static void createNewList()
    {
        list = new LinkedHashSet<>();
        
        createNewList( list, "EmployeeModel" );
    }
    
    public EmployeeModel()
    {
        super( "Employee" );
        list.add( this );
    }
    
    public EmployeeModel( Stage stage ) throws Exception
    {
        super( stage, "Employee", null );
        
        if ( stage != null )
        	list.add( this );
    }

    public static List<Expense> getFromDB()
    {
    	return getFromDB( "Employee" );
    }
    
    @Override
	protected void saveToDB() 
	{
    	String	iD = fields.get( "iD" ),						// Employee ID in the system
                name = fields.get( "name" ),					// Common name
                phone = fields.get( "phone" ),					// Phone number
                address = fields.get( "address" );				// Company address
    	
    	Employee employee = fields.get( "employee" );
    	
    	if ( employee == null )
		{
    		employee = new Employee( iD, name, phone, address );
			fields.set( "employee", employee );
		}
		else
			employee.update( iD, name, phone, address );
	
		try
    	{
    		// Persist Employee Entity data to database
    		Database.persistToDB( employee );
    	}
    	catch ( Exception e ) 
    	{
    		System.out.println( "Error with persisting data" );
    	}
	}
    
    @Override
    public void removeFromDB() throws Exception
    { 
    	removeFromDB( "employee" );
    	
    	list.remove( this );
    }
    
	public EmployeeModel( Employee e )
	{
		super( "Employee" );
    	fields.set( "employee", e );
    	
    	fields.set( "iD", e.getId() );
        fields.set( "name", e.getName() );
        fields.set( "phone", e.getPhone()  );
        fields.set( "address", e.getAddress() );
    }
	
	public static EmployeeModel getInstance( Object e )
	{
		return new EmployeeModel( (Employee) e );
	}
    
} // class ** EmployeeModel **