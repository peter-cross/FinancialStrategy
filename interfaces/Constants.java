package interfaces;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;

import foundation.AssociativeList;

/**
 * Interface Constants - To keep common constants in one place
 * @author Peter Cross
 */
public interface Constants 
{
    AssociativeList dimension = setAnalyticalDimensions();
    
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Rectangle windowSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
    
    int taskBarHeight = screenSize.height - windowSize.height;
    int taskBarWidth = screenSize.width - windowSize.width;
    
    // All declared fields are implicitly: public static final
	
    int	YES 	= 0,	
        NO 		= 1,
        CANCEL	= 2,

        TODAY	= 0,
        DATE	= 1,
        PERIOD	= 2,
        ALL		= 3,

        WIDTH 	= windowSize.width - taskBarWidth, 	 //800, //
        HEIGHT 	= windowSize.height - taskBarHeight; //550; //
    
    /**
     * Creates a list of Analytical Dimensions for G/L accounts
     * @return Associative list of Analytical Dimensions
     */
    static AssociativeList setAnalyticalDimensions()
    {
        AssociativeList d = new AssociativeList();
        
        d.set( "Bank Accounts", "BankAccount" );
        d.set( "Business Partners", "BusinessPartner" );
        d.set( "Contracts", "Contract" );
        d.set( "Employees", "Employee" );
        d.set( "Expenses", "Expense" );
        d.set( "Inventory", "Inventory" );
        d.set( "Legal Entities", "LglEntity" );
        d.set( "Locations", "Location" );
        d.set( "Long Term Assets", "LongTermAsset" );
        d.set( "Marketable Securities", "Securities" );
        d.set( "Products", "Product" );
        d.set( "Shareholders", "Shareholder" );
        d.set( "Warranties", "Warranty" );
        d.set( "Business Lines", "BusinessLine" );
    
        return d;
    }
} // End of interface ** Constants **