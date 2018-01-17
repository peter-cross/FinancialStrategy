package models;

import javafx.stage.Stage;
import java.util.LinkedHashSet;
import java.util.List;

import application.Database;
import entities.Unit;
import forms.DialogElement;

import static interfaces.Utilities.getListElementBy;

/**
 * Class UnitModel - creates and stores Units of measurement
 * @author Peter Cross
 */
public class UnitModel extends RegistryItemModel
{
    private static  LinkedHashSet  list;  // List of Items
    
    /**
     * Gets string representation of class object
     * @return UnitModel code
     */
    public String toString()
    {
        return (String) fields.get( "code" );
    }
    
    public static UnitModel getByUnit( Unit unit )
    {
    	return (UnitModel) getListElementBy( list, "unit", unit );
    }
    
    /**
     * Get UnitModel object by UnitModel code
     * @param code UnitModel Code
     * @return UnitModel object
     */
    public static UnitModel getByCode( String code )
    {
        return (UnitModel) getListElementBy( list, "code", code );
        
    } // End of method ** getByCode **
    
    /**
     * Get UnitModel object by UnitModel name
     * @param name UnitModel name
     * @return UnitModel object
     */
    public static UnitModel getByName( String name )
    {
        return (UnitModel) getListElementBy( list, "name", name );
        
    } // End of method ** getByName **
    
    /**
     * Creates dialog elements for dialog header
     * @return Array of Header dialog elements
     */
    @Override
    protected DialogElement[][] createHeader() 
    {
        DialogElement[][] header = new DialogElement[1][6];
        
        DialogElement hdr;
        
        hdr = new DialogElement( "Code" );
        hdr.valueType = "Text";
        hdr.width = 60;
        hdr.textValue = fieldTextValue( "code" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][0] = hdr;
        
        hdr = new DialogElement( "Name      " );
        hdr.valueType = "Text";
        hdr.width = 200;
        hdr.textValue = fieldTextValue( "name" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][1] = hdr;
        
        hdr = new DialogElement( "Unit Type" );
        hdr.textChoices = new String[]{ "Weight", "Volume", "Length", "Items" };
        hdr.width = 100;
        hdr.editable = false;
        hdr.textValue = fieldTextValue( "unitType" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][2] = hdr;
        
        hdr = new DialogElement( "Base Unit" );
        hdr.valueType = "List";
        hdr.list = UnitModel.getItemsList();
        hdr.textValue = fieldTextValue( "baseUnit", "UnitModel" );
        hdr.editable = false;
        hdr.width = 100;
        hdr.defaultChoice = -1;
        header[0][3] = hdr;
        
        hdr = new DialogElement( "Rate" );
        hdr.valueType = "Number";
        hdr.width = 60;
        hdr.textValue = fieldTextValue( "rate", "Number" );
        header[0][4] = hdr;
        
        hdr = new DialogElement( "Decimals" );
        hdr.shortName = "Decim.";
        hdr.valueType = "Number";
        hdr.width = 60;
        hdr.textValue = fieldTextValue( "decimals", "Number" );
        header[0][5] = hdr;
        
        return header;
        
    } // End of method ** createHeader **

    /**
     * Saves input information into object attributes
     * @param header Array of input from header
     * @param table Array of input from table
     */
    @Override
    protected void init( String[][] header, String[][][] table ) 
    {
        if ( header.length > 0 )
        {
            fields.set( "code", header[0][0] );
            fields.set( "name", header[0][1] );
            fields.set( "unitType", header[0][2] );
            fields.set( "baseUnit", header[0][3] );
            try
            {
                fields.set( "rate", Double.parseDouble( header[0][4] ) );
            }
            catch ( Exception e1 ) 
            {
            	fields.set( "rate", 0 );
            }
            
            try
            {
                fields.set( "decimals", Integer.parseInt( header[0][5] ) );	
            }
            catch ( Exception e2 ) 
            {
            	fields.set( "decimals", 0 );
            }
        }
    } // End of method ** init **

    
    public Unit  getUnit()
    {
    	return fields.get( "unit" );
    }
    
    /**
     * Get List of Units in ArrayList
     * @return ArrayList with list of Units
     */
    public static LinkedHashSet getItemsList()
    {
        return list;
    }
    
    /**
     * Creates List of Units
     * @return Array with List of Units
     */
    public static LinkedHashSet[] createList()
    {
    	if ( list == null )
            createNewList();
        
        return new LinkedHashSet[] { list };
    }
    
    public static void createNewList()
    {
        list = new LinkedHashSet<>();
        
        createNewList( list, "UnitModel" );
    }
    
    /*
    public static  LinkedHashSet[] createList()
    {
        // Create ArrayList object for List of Units
        list = new LinkedHashSet<>();
        
        // Helper object for creating new list elements
        AssociativeList fields;
        
        fields = new UnitModel().getFields();
        fields.set( "code", "item" );
        fields.set( "name", "item" );
        fields.set( "unitType", "Items" );
        
        fields = new UnitModel().getFields();
        fields.set( "code", "g" );
        fields.set( "name", "gram" );
        fields.set( "unitType", "Weight" );
        
        fields = new UnitModel().getFields();
        fields.set( "code", "mg" );
        fields.set( "name", "milligram" );
        fields.set( "unitType", "Weight" );
        fields.set( "baseUnit", getByCode( "g" ) );
        fields.set( "rate", 0.001 );
        
        fields = new UnitModel().getFields();
        fields.set( "code", "oz" );
        fields.set( "name", "ounce" );
        fields.set( "baseUnit", getByCode( "g" ) );
        fields.set( "rate", 28.3495 );
        fields.set( "unitType", "Weight" );
        
        fields = new UnitModel().getFields();
        fields.set( "code", "lb" );
        fields.set( "name", "pound" );
        fields.set( "baseUnit", getByCode( "g" ) );
        fields.set( "rate", 453.592 );
        fields.set( "unitType", "Weight" );
        
        fields = new UnitModel().getFields();
        fields.set( "code", "kg" );
        fields.set( "name", "kilogram" );
        fields.set( "baseUnit", getByCode( "g" ) );
        fields.set( "rate", 1000 );
        fields.set( "unitType", "Weight" );
        
        fields = new UnitModel().getFields();
        fields.set( "code", "L" );
        fields.set( "name", "Littres" );
        fields.set( "unitType", "Volume" );
        
        fields = new UnitModel().getFields();
        fields.set( "code", "mL" );
        fields.set( "name", "miliLittres" );
        fields.set( "baseUnit", getByCode( "L" ) );
        fields.set( "rate", 0.001 );
        fields.set( "unitType", "Volume" );
        
        return new LinkedHashSet[] { list };
    }
    */
    
    public UnitModel()
    {
        super( "UnitModel" );
        list.add( this );
    }
    
    public UnitModel( Stage stage ) throws Exception
    {
        super( stage, "UnitModel" );
        
        if ( stage != null )
        	list.add( this );
    }

    public static List<Unit> getFromDB()
    {
    	return getFromDB( "Unit" );
    }
    
    @Override
	protected void saveToDB() 
	{
    	String  code = fields.get( "code" ),
    			name = fields.get( "name" ),
    			unitType = fields.get( "unitType" ),
    			baseUnit = fields.get( "baseUnit" );
    	int 	decimals = fields.get( "decimals" );
    	
    	double 	rate;
    	try 
    	{
    		rate = fields.get( "rate" );
    	}
    	catch ( Exception e )
    	{
    		rate = 0;
    	}
    	
    	// Get Unit object from the fields of current model
    	Unit unit = fields.get( "unit" );
    	
    	// If Unit object is not created yet
    	if ( unit == null  )
        {
            // Create instance of Unit Entity
    		unit = new  Unit( code, name, unitType, baseUnit, rate, decimals );
            fields.set( "unit", unit );
        }
            
    	// Otherwise
    	else
            // Update Unit Entity information
    		unit.update( code, name, unitType, baseUnit, rate, decimals );
    	
    	try
    	{
    		// Persist Unit Entity data to database
    		Database.persistToDB( unit );
    	}
    	catch ( Exception e ) 
    	{
    		System.out.println( "Error with persisting data" );
    	}
    }
    
    @Override
    public void removeFromDB() throws Exception
    { 
    	removeFromDB( "unit" );
    	
    	list.remove( this );
    }
    
    public UnitModel( Unit u )
    {
    	super( "UnitModel" );
    	
    	fields.set( "unit", u );
    	fields.set( "code", u.getCode()  );
    	fields.set( "name", u.getName() );
    	fields.set( "unitType", u.getUnitType() );
    	fields.set( "baseUnit", u.getBaseUnit() );
    	fields.set( "rate", u.getRate() );
    	fields.set( "decimals", u.getDecimals() );
    }
    
    /**
     * Gets instance of created UnitModel object
     * @param u Unit entity object
     * @return UnitModel object
     */
    public static UnitModel getInstance( Object u )
    {
    	return new UnitModel( (Unit) u );
    } 
} // End of class ** UnitModel **