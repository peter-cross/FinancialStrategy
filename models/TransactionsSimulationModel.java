package models;

import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.transaction.SystemException;

import java.util.List;
import java.util.Vector;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import application.Database;
import entities.LegalEntity;
import entities.TAccount;
import entities.Transaction;
import entities.TransactionsModel;
import views.TransactionsModelView;
import forms.DialogElement;
import forms.TableOutput;
import foundation.AssociativeList;

import static interfaces.Utilities.createModelClass;
import static interfaces.Utilities.getListIndex;

/**
 * Class TransactionsSimulationModel - Stores data for displaying Transactions Model
 * @author Peter Cross
 */
public class TransactionsSimulationModel extends RegistryItemModel 
{
	private static	LinkedHashSet[] list;       			// List of Transaction Simulation Models for each Legal Entity
    
	private static	ArrayList<LegalEntityModel> entities;	// List of Legal Entities
	
	/**
	 * Class default constructor
	 */
	public TransactionsSimulationModel()
	{
		super( "Transactions Model" );
	}
	
	/**
	 * Class constructor
	 * @param entityName Legal entity name for Transactions Model
	 */
	public TransactionsSimulationModel( String entityName )
	{
		super( "Transactions Model", entityName );
	}
	
	/**
	 * Class constructor with specified Stage
	 * @param st Stage object
	 * @throws Exception
	 */
	public TransactionsSimulationModel( Stage st, String entityName ) throws Exception
	{
		super( st, "Transactions Model", entityName );
	}
	
	/**
	 * Class constructor with specified Transactions Model
	 * @param tm TransactionsSimulationModel object
	 */
	public TransactionsSimulationModel( TransactionsModel tm, String entityName )
	{
		super( "Transactions Model", entityName );
		
		fields.set( "title", tm.getName() );
		fields.set( "transactionsModel", tm );
		
		setTab( entityName );
	}
	
	/**
	 * Sets tab name and according parameters for selected Registry View tab
	 * @param entityName Legal Entity name on selected tab
	 */
	@Override
	protected void setTab( String entityName )
	{
		tabName = entityName;
		
		LegalEntityModel lglEntityModel = LegalEntityModel.getByName( entityName );
		tabNum = getListIndex( LegalEntityModel.getItemsList(), lglEntityModel );
		
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
			
			saveToDB();
			
			if ( !list[tabNum].contains( this ) )
				list[tabNum].add( this );
		}
		else if ( !list[tabNum].contains( this ) )
			fields = null;
		
		return null;
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
		if ( list == null )
        	initEmptyList();
        else
        	createNewList();
        	
        return list;
    }
    
	/**
	 * Initializes empty list of Registry Items
	 */
	private static void initEmptyList()
	{
		Class c = createModelClass( "LegalEntityModel" );

		try
        {
            // Get list of Legal Entities
            entities = new ArrayList( ((LinkedHashSet[]) c.getMethod( "createList" ).invoke( null ))[0] );

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

        initFromDB();
	}
	
	/**
	 * Creates new list of Registry Items
	 */
	private static void createNewList()
	{
		Class c = createModelClass( "LegalEntityModel" );

		int numEntities = 1;
    	ArrayList<LegalEntityModel>  newEntities = null;
    	
    	try
        {
            // Get new list of Legal Entities
    		newEntities = new ArrayList( ((LinkedHashSet[]) c.getMethod( "createList" ).invoke( null ))[0] );

            numEntities = Math.max( newEntities.size(), 1 );
        }
        catch ( Exception e ) { }
    	
    	LinkedHashSet[] newList = new LinkedHashSet[numEntities];
		
		for ( int i = 0; i < numEntities; i++ )
    	{
			int idx = -1;
			
			if ( newEntities.size() > 0 )
				idx = entities.indexOf( newEntities.get(i) );
        		
    		if ( idx >= 0 )
    			newList[i] = list[idx];
    		else
    			newList[i] = new LinkedHashSet<>();
    	}
		
		list = newList;
		entities = newEntities;
	}
	
	/**
	 * Initializes Transaction Models by data from database
	 */
	private static void initFromDB()
	{
		// Get list of Transaction Models from database
        List<TransactionsModel> dbTrModels = getFromDB();

        // If list is not empty
        if ( dbTrModels != null && dbTrModels.size() > 0 )
            // Loop for each Transactions Model
            for ( TransactionsModel tm : dbTrModels )
            {
                // Create TransactionsSimulationModel object based on provided Transactions Model
                LegalEntity lglEntity = tm.getLegalEntity();

                int entityNum = 0;

                if ( lglEntity != null )
                {
                    LegalEntityModel  lglEntityModel = LegalEntityModel.getByEntity( lglEntity );

                    if ( lglEntityModel != null )
                    	entityNum = getListIndex( LegalEntityModel.getItemsList(), lglEntityModel );
                }

                list[entityNum].add( new TransactionsSimulationModel( tm, lglEntity.getName() ) );
            }
	}
	
    /**
	 * Gets Transactions Models from database
	 * @return List of TransactionsSimulationModel objects
	 */
	protected static List<TransactionsModel> getFromDB()
    {
    	return getFromDB( "TransactionsModel" );
    }
    
	/**
	 * Saves Transaction Model to database
	 */
	protected void saveToDB()
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
     * Removes TransactionsSimulationModel data from database 
     */
    @Override
    public void removeFromDB()
    {
    	TransactionsModel tm  = fields.get( "transactionsModel" );
    	
    	// If EntityManager object is created and TransactionsSimulationModel argument is specified
        if ( tm != null  )
        {
        	LegalEntity lglEntity = tm.getLegalEntity();
        	
        	if ( lglEntity != null )
        	{
        		LegalEntityModel lglEntityModel = LegalEntityModel.getByEntity( lglEntity );
        		
        		int idx = entities.indexOf( lglEntityModel );
            	if ( idx >= 0 )
            		list[idx].remove( this );
        	}
        	
        	Database.removeFromDB( tm );
        }
    }

	@Override
	protected void init(String[][] header, String[][][] table) 
	{ }
} // End of class ** TransactionsSimulationModel **