package application;
	
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import foundation.UserDialog;
import interfaces.Constants;
import interfaces.Utilities;
import views.RegistryView;

/** 
 * Class Main - starts the program
 * @author Peter Cross
 * requires : eclipselink.jar, 
 * 			  javax.persistence.jar, 
 * 			  javax.transaction-api.jar, 
 * 			  mysql-connector-java.jar
 *
 */
public final class Main extends Application implements Constants, Utilities
{
	public static final String TITLE = "Financial Strategy v.1.0";
	
	public static Stage 		 stage;	// Stage object for displaying GUI
	private static EntityManager em;	// Java Persistence Entity Manager 
	
	/**
     * Starts the program
     * @param args Command line parameters
     */
    public static void main( String[] args ) 
    {
        // Launch JavaFX GUI
        launch( args );
    }
	
    /**
     * Creates Graphic User Interface to work with the program
     * @param st Stage object for main program window
     */
    @Override
    public void start( Stage st ) 
    {
    	EntityManagerFactory emf =  null;
    	
    	try
    	{
    		// Create Entity Manager Factory object for Persistence Unit FinancialStrategy
        	emf = Persistence.createEntityManagerFactory( "FinancialStrategy" );
        	
        	// Create Entity Manager Object 
    		em = emf.createEntityManager();
        }
    	catch ( Exception e )
    	{
    		em = null;
        }
    	
    	try
        {
        	new UserDialog( this::openAboutBox ).start();
        }
        catch ( Exception e ) { }
    	
    	// Create Registry object for displaying TransactionsModelData Registry Items
		stage = new RegistryView( st, "Transaction Models", "TransactionsModelData" );
    	
		// Display created Registry with specified with and height
		((RegistryView)stage).display( WIDTH, HEIGHT );
    	
    	if ( em != null )
    	{
    		em.clear();
        	em.close();
        	emf.close();
    	}
    } // End of method ** start **
    
    
    
    /**
     * Returns Entity Manager object
     * @return EntityManager object
     */
    public static EntityManager getEntityManager()
    {
    	return em;
    }
    
    /**
     * Removes Entity object's data from database
     * @param obj Entity object
     */
    public static void removeFromDB( Object obj )
    {
    	EntityTransaction et = null;
		
        if ( em != null )
        	try
        	{
        		// Get Entity Transaction
        		et = em.getTransaction();
        		
        		// Start transaction
				et.begin();
				
				if ( obj instanceof List )
		    		for ( Object o : (List<Object>)obj )
						// Remove object's data from DB
						em.remove( o );
		    	else
					// Remove object's data from DB
					em.remove( obj );
				
				// Commit transaction
				et.commit();
        	}
    		catch ( Exception e )
	     	{
	     		if ( et != null )
	     			// Rollback transaction if something failed
	     			et.rollback();
	     	}
    }
    
} // End of class ** Main **