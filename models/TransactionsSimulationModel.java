package models;

import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.transaction.SystemException;

import java.util.List;
import java.util.Vector;
import java.util.LinkedHashSet;

import application.Database;
import entities.LegalEntity;
import entities.TAccount;
import entities.Transaction;
import entities.TransactionsModel;
import forms.DialogElement;
import forms.TableOutput;
import foundation.AssociativeList;
import views.TransactionsModelView;

import static interfaces.Utilities.createModelClass;
import static interfaces.Utilities.getListIndex;

/**
 * Class TransactionsSimulationModel - Stores data for displaying Transactions Model
 * @author Peter Cross
 *
 */
public class TransactionsSimulationModel extends RegistryItemModel 
{
	private static	LinkedHashSet[] list;       // List of Transaction Simulation Models for each Legal Entity
    
	/**
	 * Class default constructor
	 */
	public TransactionsSimulationModel()
	{
		super( "Transactions Model" );
	}
	
	public TransactionsSimulationModel( String tabName )
	{
		super( "Transactions Model", tabName );
	}
	
	/**
	 * Class constructor with specified Stage
	 * @param st Stage object
	 * @throws Exception
	 */
	public TransactionsSimulationModel( Stage st, String tabName ) throws Exception
	{
		super( st, "Transactions Model", tabName );
	}
	
	/**
	 * Class constructor with specified Transactions Model
	 * @param tm TransactionsSimulationModel object
	 */
	public TransactionsSimulationModel( TransactionsModel tm, String tabName )
	{
		super( "Transactions Model", tabName );
		
		fields.set( "title", tm.getName() );
		fields.set( "transactionsModel", tm );
		
		setTab( tabName );
	}
	
	@Override
	protected void setTab( String tabName )
	{
		this.tabName = tabName;
		
		LegalEntityModel lglEntityModel = LegalEntityModel.getByName( tabName );
		tabNum = getListIndex( LegalEntityModel.getItemsList() , lglEntityModel );
		
		LegalEntity legalEntity = null;
		
		if ( lglEntityModel != null )
			legalEntity = lglEntityModel.getLegalEntity();
		
		if ( legalEntity != null )
			fields.set( "legalEntity", legalEntity );
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
		AssociativeList output = TransactionsModelView.getInstance( fields ).result();
		
		// If there is input or changes
		if ( output != null )
		{
			// Assign output to fields in order to save results
			fields = output;
			
			saveTransactionsModelToDB();
			
			if ( !list[tabNum].contains( this ) )
				list[tabNum].add( this );
		}
		else if ( !list[tabNum].contains( this ) )
			fields = null;
		
		return null;
    }
	
	/**
	 * Saves Transaction Model to database
	 */
	private void saveTransactionsModelToDB()
	{
		// Get Transactions Model object
		TransactionsModel transactionsModel = fields.get( "transactionsModel" );
		
		if ( transactionsModel.getLegalEntity() == null )
		{
			LegalEntity legalEntity = fields.get( "legalEntity" );
			
			if ( legalEntity != null )
				transactionsModel.setLegalEntity( legalEntity );
		}
		
		// Get T-Accounts of Transactions Model
		Vector<TAccount>  accts = transactionsModel.getTAccounts();
		
		// Get transactions of Transactions Model
		Vector<Transaction> trs = transactionsModel.getTransactions();
		
		// If output is empty - remove object from the list
		if ( (trs == null || trs.size() == 0) && (accts == null || accts.size() == 0) )
			//list[legalEntity].remove(this);
			list[tabNum].remove(this);
		
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
			EntityManager em = Database.getEntityManager();
			
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
	
	/**
	 * Returns list of TransactionsSimulationModel objects
	 */
	public static LinkedHashSet[] getItemsList()
    {
        return list;
    }
    
    /**
	 * Creates list of TransactionsSimulationModel objects
	 */
	public static  LinkedHashSet[] createList()
    {
		Class c = createModelClass( "LegalEntityModel" );

        if ( list == null )
        {
        	try
            {
                // Get list of Legal Entities
                LinkedHashSet entities = ((LinkedHashSet[]) c.getMethod( "createList" ).invoke( null ))[0];

                int numEntities = Math.max( entities.size(), 1 );

                // Create arrays for Legal Entities list with size equal to number of Legal Entities
                list = new LinkedHashSet[ numEntities ];
            }
            catch ( Exception e )
            {
                // Create arrays for one Legal Entity
                list = new LinkedHashSet[1];
            }
            
            // Create ArrayList for list of Legal Entities for each Legal Entity
            for ( int i = 0; i < list.length; i++ )
                list[i] = new LinkedHashSet<>();

            // Get list of Transaction Models from database
            List<TransactionsModel> dbTrModels = getTransactionModelsFromDB();

            // If list is not empty
            if ( dbTrModels != null && dbTrModels.size() > 0 )
                // Loop for each Transactions Model
                for ( TransactionsModel tm : dbTrModels )
                {
                    // Create TransactionsSimulationModel object based on provided Transactions Model
                    LegalEntity lglEntity = tm.getLegalEntity();

                    int entity = 0;

                    if ( lglEntity != null )
                    {
                        LegalEntityModel  lglEntityModel = LegalEntityModel.getByEntity( lglEntity );

                        if ( lglEntityModel != null )
                                entity = getListIndex( LegalEntityModel.getItemsList(), lglEntityModel );
                    }

                    list[entity].add( new TransactionsSimulationModel( tm, lglEntity.getName() ) );
                }
        }
        else
        {
        	int numEntities = 1;
        	
        	try
            {
                // Get list of Legal Entities
                LinkedHashSet entities = ((LinkedHashSet[]) c.getMethod( "createList" ).invoke( null ))[0];

                numEntities = Math.max( entities.size(), 1 );
            }
            catch ( Exception e ) { }
        	
        	if ( numEntities != list.length )
        	{
        		LinkedHashSet[] newList = new LinkedHashSet[numEntities];
        		
        		int newSize = Math.min( numEntities, list.length );
        		
        		System.arraycopy( list, 0, newList, 0, newSize );
        		
        		list = newList;
        		
        		for ( int i = newSize; i < list.length; i++ )
                    list[i] = new LinkedHashSet<>();
        	}
        }
	
        return list;
    }
    
    /**
	 * Gets Transactions Models from database
	 * @return List of TransactionsSimulationModel objects
	 */
    private static List<TransactionsModel> getTransactionModelsFromDB()
    {
    	// Get Entity Manager
    	EntityManager em = Database.getEntityManager();
    	
        if ( em != null )
        	try
        	{
        		// Do query for TransactionsSimulationModel entity in DB and return results of query
        		return em.createQuery( "SELECT t FROM TransactionsModel AS t" ).getResultList();
            }
        	catch ( Exception e )
        	{
        		return null;
        	}
		
        return null;
    }
    
    /**
     * Removes TransactionsSimulationModel data from database 
     */
    @Override
    public void removeFromDB()
    {
    	EntityManager em = Database.getEntityManager();
    	EntityTransaction et = null;
		
    	TransactionsModel tm  = fields.get( "transactionsModel" );
    	
    	// If EntityManager object is created and TransactionsSimulationModel argument is specified
        if ( em != null && tm != null  )
        	try
        	{
        		// Get list of T-Accounts for Transactions Model
        		Vector<TAccount>  accts = tm.getTAccounts();
        		
        		// Get list of Transactions for Transactions Model
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

	@Override
	protected void init(String[][] header, String[][][] table) 
	{
		
	}
}