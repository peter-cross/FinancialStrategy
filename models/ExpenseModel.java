package models;

import javafx.stage.Stage;

import static interfaces.Utilities.getListElementBy;

import java.util.LinkedHashSet;
import java.util.List;

import application.Database;
import entities.Crcy;
import entities.Expense;
import forms.DialogElement;

/**
 * Class Expense - To create and store expenses
 * @author Peter Cross
 */
public class ExpenseModel extends RegistryItemModel
{
    private static  LinkedHashSet  list;       // List of Items
    
    
    /**
     * Gets string representation of class object
     * @return ExpenseModel name
     */
    public String toString()
    {
        return (String) fields.get( "name" );
    }
    
    public static ExpenseModel getByExpense( Expense e )
    {
    	return (ExpenseModel) getListElementBy( list, "expense", e );
    }
    
    /**
     * Get ExpenseModel object by ExpenseModel code
     * @param code ExpenseModel Code
     * @return ExpenseModel object
     */
    public static ExpenseModel getByCode( String code )
    {
        return (ExpenseModel) getListElementBy( list, "code", code );
        
    } // End of method ** getByCode **
    
    /**
     * Get ExpenseModel object by ExpenseModel name
     * @param name ExpenseModel name
     * @return ExpenseModel object
     */
    public static ExpenseModel getByName( String name )
    {
        return (ExpenseModel) getListElementBy( list, "name", name );
        
    } // End of method ** getByName **
    
    
    @Override
    protected DialogElement[][] createHeader() 
    {
        DialogElement[][] header = new DialogElement[1][3];
        
        DialogElement hdr;
        
        hdr = new DialogElement( "Code" );
        hdr.valueType = "Number";
        hdr.width = 70;
        hdr.textValue = fieldTextValue( "code" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][0] = hdr;
        
        hdr = new DialogElement( "Name   " );
        hdr.valueType = "Text";
        hdr.width = 300;
        hdr.textValue = fieldTextValue( "name" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][1] = hdr;
        
        hdr = new DialogElement( "Category" );
        hdr.valueType = "List";
        hdr.textChoices = new String[] { "R & D",
                                         "Design",
                                         "Purchasing",
                                         "Production",
                                         "Marketing",
                                         "Distribution",
                                         "Customer Service",
                                         "Administration",
                                         "Depreciation" };
        hdr.width = 200;
        hdr.editable = false;
        hdr.textValue = fieldTextValue( "category" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][2] = hdr;
        
        return header;
    }

    @Override
    protected void init( String[][] header, String[][][] table ) 
    {
        if ( header.length > 0 )
        {
            fields.set( "code", header[0][0] );
            fields.set( "name", header[0][1]  );
            fields.set( "category", header[0][2] );
        }
    }
    
    public Expense getExpense()
    {
    	return fields.get( "expense" );
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
        
        createNewList( list, "ExpenseModel" );
    }
    
    public ExpenseModel()
    {
        super( "ExpenseModel" );
        list.add( this );
    }
    
    public ExpenseModel( Stage stage ) throws Exception
    {
        super( stage, "ExpenseModel" );
        
        if ( stage != null )
        	list.add( this );
    }

    public static List<Expense> getFromDB()
    {
    	return getFromDB( "Expense" );
    }
    
    @Override
	protected void saveToDB() 
	{
		String  code = fields.get( "code" ),
    			name = fields.get( "name" ),
    			category = fields.get( "category" );
    	
		Expense expense = fields.get( "expense" );
		
		if ( expense == null )
		{
			expense = new Expense( code, name, category );
			fields.set( "expense", expense );
		}
		else
			expense.update( code, name, category );
	
		try
    	{
    		// Persist Expense Entity data to database
    		Database.persistToDB( expense );
    	}
    	catch ( Exception e ) 
    	{
    		System.out.println( "Error with persisting data" );
    	}
	}
    
	@Override
    public void removeFromDB() throws Exception
    { 
    	removeFromDB( "expense" );
    	
    	list.remove( this );
    }
    
	public ExpenseModel( Expense e )
	{
		super( "ExpenseModel" );
		
		fields.set( "expense", e );
    	fields.set( "code", e.getCode()  );
    	fields.set( "name", e.getName() );
    	fields.set( "category", e.getCategory() );
	}
	
	/**
     * Gets instance of created ExpenseModel object
     * @param e Expense entity object
     * @return ExpenseModel object
     */
    public static ExpenseModel getInstance( Object e )
    {
    	return new ExpenseModel( (Expense) e );
    }
    
} // End of class ** ExpenseModel **