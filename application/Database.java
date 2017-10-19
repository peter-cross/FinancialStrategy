package application;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import java.util.List;

import entities.TAcct;
import entities.TrActn;
import entities.TractnsModel;

import interfaces.Lambda.ObjectAction;

/**
 * Class Database - methods to work with DB through Java Persistence
 * @author Peter Cross
 *
 */
public class Database 
{
	private static EntityManager em;	// Java Persistence Entity Manager 
	
	/**
	 * Prepares to work with DB
	 */
	public static void start()
	{
		// Create instance of Entity Manager
    	em = createEntityManager();
	}
	
	/**
     * Clears and closes the Entity Manager
     */
    public static void stop() 
    {
    	if ( em != null )
    	{
            em.clear();
            em.close();
        }
    }
    
    /**
     * Persists object data to database
     * @param obj Object to persist
     */
    public static void persistToDB( Object obj )
    {
    	performToDB( obj, o -> em.persist(o) );
    }
    
    /**
     * Removes Entity object's data from database
     * @param obj Entity object
     */
    public static void removeFromDB( Object obj )
    {
    	performToDB( obj, o -> em.remove(o) );
    }
    
    /**
     * Performs action on Database Entity data to database
     * @param obj Object to perform some action
     * @param action What action to perform
     */
    private static void performToDB( Object obj, ObjectAction action )
    {
    	// Get instance of Entity Manager
    	em = getEntityManager();
    	
    	// Entity TrActn object
    	EntityTransaction et = null;
		
    	// If instance of Entity Manager was successfully created
        if ( em != null )
        	try
        	{
        		// Get Entity TrActn
        		et = em.getTransaction();
        		
        		// Start transaction
				et.begin();
				
				// If object is a list of something
				if ( obj instanceof List )
					// Loop through the list of objects
		    		for ( Object o : (List<Object>) obj )
						// Perform action on each object's data to DB
		    			action.perform(o);
				
				// Otherwise, if it's a separate object
		    	else
					// Perform action on object's data to DB
					action.perform(obj);
				
				// Commit transaction
				et.commit();
        	}
    		catch ( Exception e )
	     	{
	     		// If Entity TrActn was successfully created
    			if ( et != null )
	     			// Rollback transaction because something failed
	     			et.rollback();
	     	}
    }
    
    /**
     * Removes TransactionModel object from database
     * @param tm TransactionModel object
     */
    public static void removeFromDB( TractnsModel tm )
    {
    	EntityTransaction et = null;
		
    	// If EntityManager object is created and TractnsSimulationModel argument is specified
        if ( em != null && tm != null  )
        {
        	try
        	{
        		et = em.getTransaction();
        		
        		// Start transaction
				et.begin();
				
				// Remove Model's T-Accts from DB
				for ( TAcct acct : tm.getTAccs() )
					em.remove( acct );
				
				// Remove Model's Transactions from DB
				for ( TrActn tr : tm.getTractns() )
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
    }
    
    /**
     * Returns Entity Manager object
     * @return EntityManager object
     */
    public static EntityManager getEntityManager()
    {
    	// Return instance of Entity Manager
	    return em;
    } 
    
    /**
     * Creates Entity Manager object
     * @return EntityManager object
     */
    private static EntityManager createEntityManager()
    {
    	try
    	{
    		// Create Entity Manager Factory object for Persistence unit "FinancialStrategy"
    		EntityManagerFactory emf = Persistence.createEntityManagerFactory( "FinancialStrategy" );
        	
        	// Create instance of Entity Manager 
    		return emf.createEntityManager();
        }
    	catch ( Exception e )
    	{
    		return null;
    	}
    }
}