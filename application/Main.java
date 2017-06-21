package application;
	
import javafx.application.Application;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import java.util.Vector;

import entities.TAccount;
import entities.Transaction;
import entities.TransactionsModel;
import forms.Registry;
import interfaces.Constants;

/** 
 * Class Main - starts the program
 * @author Peter Cross
 * requires : eclipselink.jar, 
 * 			  javax.persistence.jar, 
 * 			  javax.transaction-api.jar, 
 * 			  mysql-connector-java.jar
 *
 */
public final class Main extends Application implements Constants
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
    	// Create Entity Manager Factory object for Persistence Unit FinancialStrategy
    	EntityManagerFactory emf = Persistence.createEntityManagerFactory( "FinancialStrategy" );
    	
    	try
    	{
    		// Create Entity Manager Object 
    		em = emf.createEntityManager();
        }
    	catch ( Exception e )
    	{
    		em = null;
        }
    	
    	if ( em != null )
    	{
    		// Create Registry object for displaying TransactionsModelData Registry Items
    		stage = new Registry( st, "Transaction Models", "TransactionsModelData" );
        	
    		// Display created Registry with specified with and height
    		((Registry)stage).display( WIDTH, HEIGHT );
        	
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
	
    /**
     * Removes Transaction Model object from database
     * @param tm Transaction Model object
     */
    public static void removeFromDB( TransactionsModel tm )
    {
    	EntityTransaction et = null;
		
    	// If EntityManager object is created and TransactionsModel argument is specified
        if ( em != null && tm != null  )
        	try
        	{
        		// Get list of T-Accounts for Transactions Model
        		Vector<TAccount>  accts = tm.getTAccounts();
        		
        		// Get list of Transctions for Transactions Model
        		Vector<Transaction> trs = tm.getTransactions();
        		
        		et = em.getTransaction();
        		
        		// Start transaction
				et.begin();
				
				// Remove Model's T-Accounts from DB
				for ( TAccount acct : accts )
					em.remove( acct );
				
				// Remove Model's Transactions from DB
				for ( Transaction tr : trs )
					em.remove( tr );
				
				// Remove Transactions Model from DB
				em.remove( tm );
				
				// Commit transaction
				et.commit();
			}
	        catch ( Exception e )
	     	{
	     		if ( et != null )
	     			// Rollback changes
	     			et.rollback();
	     	}
    }
} // End of class ** Main **