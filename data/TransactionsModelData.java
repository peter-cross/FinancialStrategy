package data;

import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.transaction.SystemException;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Vector;

import application.Main;
import entities.TAccount;
import entities.Transaction;
import entities.TransactionsModel;
import forms.DialogElement;
import forms.TableOutput;
import foundation.AssociativeList;
import foundation.RegistryItem;

/**
 * Class TransactionsModelData - Stores data for displaying Transactions Model
 * @author Peter Cross
 *
 */
public class TransactionsModelData extends RegistryItem 
{
	private static  LinkedHashSet<RegistryItem>  list;  // List of Items
    
	/**
	 * Class default constructor
	 */
	public TransactionsModelData()
	{
		super( "Transactions Model" );
		list.add( this );
	}
	
	/**
	 * Class constructor with specified Stage
	 * @param st Stage object
	 * @throws Exception
	 */
	public TransactionsModelData( Stage st ) throws Exception
	{
		super( st, "Transactions Model" );
		
		if ( st != null )
			list.add( this );
	}
	
	/**
	 * Class constructor with specified Transactions Model
	 * @param tm TransactionsModel object
	 */
	private TransactionsModelData( TransactionsModel tm )
	{
		super( "Transactions Model" );
		
		fields.set( "title", tm.getName() );
		fields.set( "transactionsModel", tm );
		
		list.add( this );
	}
	
	/**
	 * Creates header elements for the form
	 */
	@Override
	protected DialogElement[][] createHeader() 
	{
		// Array for header elements
        DialogElement[][] header = new DialogElement[1][1];
        // Helper object for creating new dialog elements
        DialogElement hdr;
        
        hdr = new DialogElement( "Model Title" );
        hdr.attributeName = "title";
        hdr.width = 300;
        // If field text value is specified - pass it to the form
        hdr.textValue = fieldTextValue( "title" );
        hdr.validation = validationCode( hdr.labelName );
        header[0][0] = hdr;
        
        return header;
	}
	
	/**
	 * Displays dialog form and gets output from dialog
	 */
	@Override
    public TableOutput getOutput() throws IllegalStateException, SecurityException, SystemException
    {
		// Create Transactions Model dialog and get results of input
		AssociativeList output = TransactionsModelDialog.getInstance( fields ).result();
		
		// If there is input or changes
		if ( output != null )
		{
			// Assign output to fields in order to save results
			fields = output;
			
			saveTransactionsModelToDB();
	    } 
		
		return null;
    }
	
	/**
	 * Saves Transaction Model to database
	 */
	private void saveTransactionsModelToDB()
	{
		// Get Transactions Model object
		TransactionsModel transactionsModel = fields.get( "transactionsModel" );
		
		// Get T-Accounts of Transactions Model
		Vector<TAccount>  accts = transactionsModel.getTAccounts();
		
		// Get transactions of Transactions Model
		Vector<Transaction> trs = transactionsModel.getTransactions();
		
		// If output is empty - remove object from the list
		if ( (trs == null || trs.size() == 0) && (accts == null || accts.size() == 0) )
			list.remove(this);
		
		// Persist changes to Database
		persistToDB( accts, trs, transactionsModel );
	}
	
	/**
	 * Persists changes to Database
	 * @param accts List of T-Accounts
	 * @param trs	List of Transactions
	 * @param tm	Transactions Model
	 */
	private void persistToDB( Vector<TAccount>  accts, Vector<Transaction> trs, TransactionsModel tm )
	{
		EntityTransaction et = null;
		
		try
     	{
			// Get Entity manager
			EntityManager em = Main.getEntityManager();
			
			et = em.getTransaction();
			
			// Start transaction
			et.begin();
			
			// Persist T-accounts of Transactions Model
			for ( TAccount acct : accts )
				em.persist( acct );
			
			// Persist transactions of Transactions Model
			for ( Transaction tr : trs )
				em.persist( tr );
			
			// Transactions Model
			em.persist( tm );
			
			// Commit changes
			et.commit();
		}
     	catch ( Exception e )
     	{
     		if ( et != null )
     			// Rollback if something went wrong
     			et.rollback();
     	}
	}
	
	@Override
	protected void init( String[][] header, String[][][] table ) 
	{ }
	
	/**
	 * Returns list of TransactionsModelData objects
	 */
	public static LinkedHashSet<RegistryItem> getItemsList()
    {
        return list;
    }
    
	/**
	 * Creates list of TransactionsModelData objects
	 */
	public static LinkedHashSet<RegistryItem>[] createList()
    {
        list = new LinkedHashSet<>();
        
        // Get list of Transaction Models from database
        List<TransactionsModel> dbTrModels = getTransactionModelsFromDB();
        
        // If list is not empty
        if ( dbTrModels != null && dbTrModels.size() > 0 )
        	// Loop for each Transactions Model
        	for ( TransactionsModel tm : dbTrModels )
        		// Create TransactionsModelData object based on provided Transactions Model
        		new TransactionsModelData(tm);
        
        // Return list as 1st item or array
        return new LinkedHashSet[] { list };
    }
    
	/**
	 * Gets Transactions Models from database
	 * @return List of TransactionsModel objects
	 */
    private static List<TransactionsModel> getTransactionModelsFromDB()
    {
    	// Get Entity Manager
    	EntityManager em = Main.getEntityManager();
    	
        if ( em != null )
        	try
        	{
        		// Do query for TransactionsModel entity in DB and return results of query
        		return em.createQuery( "SELECT t FROM TransactionsModel AS t" ).getResultList();
            }
        	catch ( Exception e )
        	{
        		return null;
        	}
		
        return null;
    }
    
    /**
     * Removes TransactionsModel data from database 
     */
    @Override
    public void removeFromDB()
    { 
    	Main.removeFromDB( fields.get( "transactionsModel" ) );
    }
}