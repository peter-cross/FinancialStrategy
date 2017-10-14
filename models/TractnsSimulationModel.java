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
import entities.LglEntity;
import entities.TAcct;
import entities.TrActn;
import entities.TractnsModel;
import views.TractnsModelView;
import forms.DialogElement;
import forms.TableOutput;
import foundation.AssociativeList;

import static interfaces.Utilities.createModelClass;
import static interfaces.Utilities.getListIndex;

/**
 * Class TractnsSimulationModel - Stores data for displaying Transactions Model
 * @author Peter Cross
 */
public class TractnsSimulationModel extends RegistryItemModel 
{
	private static	LinkedHashSet[] list;       			// List of TrActn Simulation Models for each Legal Entity
    
	private static	ArrayList<LglEntityModel> entities;	// List of Legal Entities
	
	private static final String transModelStr = "Transactions Model";
	
	/**
	 * Class default constructor
	 */
	public TractnsSimulationModel()
	{
		super( transModelStr );
	}
	
	/**
	 * Class constructor
	 * @param entityName Legal entity name for Transactions Model
	 */
	public TractnsSimulationModel( String entityName )
	{
		super( transModelStr, entityName );
	}
	
	/**
	 * Class constructor with specified Stage
	 * @param st Stage object
	 * @throws Exception
	 */
	public TractnsSimulationModel( Stage st, String entityName ) throws Exception
	{
		super( st, transModelStr, entityName );
	}
	
	/**
	 * Class constructor with specified Transactions Model
	 * @param tm TractnsSimulationModel object
	 */
	public TractnsSimulationModel( TractnsModel tm, String entityName )
	{
		super( transModelStr, entityName );
		
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
		
		// Get Legal Entity Model by entity name
		LglEntityModel lglEntityModel = LglEntityModel.getByName( entityName );
		
		// Find index of legal Entity Model and assign it as Tab number
		tabNum = Math.max( 0, getListIndex( LglEntityModel.getItemsList(), lglEntityModel ) );
		
		LglEntity legalEntity = null;
		
		// If Legal Entity Model was found
		if ( lglEntityModel != null )
			// Get Legal Entity from Legal Entity Model
			legalEntity = lglEntityModel.getLegalEntity();
		
		// If Legal Entity is specified
		if ( legalEntity != null )
			// Set Legal Entity in fields attribute
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
		AssociativeList output = TractnsModelView.getInstance( fields ).result();
		
		// If there is input or changes
		if ( output != null )
		{
			// Assign output to fields in order to save results
			fields = output;
			
			// Save entered data to database
			saveToDB();
			
			// If list for current tab does not contain current Transactions Simulation Model
			if ( !list[tabNum].contains( this ) )
				// Add it to list for current tab
				list[tabNum].add( this );
		}
		// If there are no input or changes, but list for current tab contains current Transactions Model
		else if ( !list[tabNum].contains( this ) )
			// Assign null to fields attribute
			fields = null;
		
		return null;
    }
	
	/**
	 * Returns list of TractnsSimulationModel objects
	 */
	public static LinkedHashSet[] getItemsList()
    {
        return list;
    }
    
    /**
	 * Creates list of TractnsSimulationModel objects
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
		Class c = createModelClass( "LglEntityModel" );

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

        // Initialize model by data from database
        initFromDB();
	}
	
	/**
	 * Creates new list of Registry Items
	 */
	private static void createNewList()
	{
		// Get instance of Legal Entity Model Class
		Class c = createModelClass( "LglEntityModel" );

		// Min number of entities is one
		int numEntities = 1;
		
		// Array List to store new list of Legal Entities
    	ArrayList<LglEntityModel>  newEntities = null;
    	
    	try
        {
            // Get new list of Legal Entities from class static method createList
    		newEntities = new ArrayList( ((LinkedHashSet[]) c.getMethod( "createList" ).invoke( null ))[0] );

            // If list is empty - make number of entities one
    		numEntities = Math.max( newEntities.size(), 1 );
        }
        catch ( Exception e ) { }
    	
    	// Linked HashSet for new list of Legal Entities
    	LinkedHashSet[] newList = new LinkedHashSet[numEntities];
		
		// Loop for each current legal entity
    	for ( int i = 0; i < numEntities; i++ )
    	{
			int idx = -1;
			
			// If there is non-empty list of new Legal Entities
			if ( newEntities.size() > 0 )
				// Try to get in list of existing Legal Entities index of new Legal Entity with index i
				idx = entities.indexOf( newEntities.get(i) );
        		
    		// If entity was found
			if ( idx >= 0 )
				// Assign reference to found Legal Entity for new list
    			newList[i] = list[idx];
			
			//Otherwise
    		else
    			// Create empty Linked HashSet list for current entity
    			newList[i] = new LinkedHashSet<>();
    	}
		
		list = newList;
		entities = newEntities;
	}
	
	/**
	 * Initializes TrActn Models by data from database
	 */
	private static void initFromDB()
	{
		// Get list of TrActn Models from database
        List<TractnsModel> dbTrModels = getFromDB();

        // If list is not empty
        if ( dbTrModels != null && dbTrModels.size() > 0 )
            // Loop for each Transactions Model
            for ( TractnsModel tm : dbTrModels )
            {
                // Create TractnsSimulationModel object based on provided Transactions Model
                LglEntity lglEntity = tm.getLglEntity();

                int entityNum = 0;

                // If Legal Entity is assigned
                if ( lglEntity != null )
                {
                    // Get Legal Entity Model by Legal Entity instance
                	LglEntityModel  lglEntityModel = LglEntityModel.getByEntity( lglEntity );

                    // If Legal Entity Model is found
                	if ( lglEntityModel != null )
                		// Get entity index by Legal Entity Model
                    	entityNum = getListIndex( LglEntityModel.getItemsList(), lglEntityModel );
                }

                // Create new instance of class based on provided TractnsModel and legal Entity name
                list[entityNum].add( new TractnsSimulationModel( tm, lglEntity.getName() ) );
            }
	}
	
    /**
	 * Gets Transactions Models from database
	 * @return List of TractnsSimulationModel objects
	 */
	protected static List<TractnsModel> getFromDB()
    {
    	return getFromDB( "TractnsModel" );
    }
    
	/**
	 * Saves TrActn Model to database
	 */
	protected void saveToDB()
	{
		// Get Transactions Model object
		TractnsModel transactionsModel = fields.get( "transactionsModel" );
		
		// If Legal Entity of TractnsModel is not specified
		if ( transactionsModel.getLglEntity() == null )
		{
			// Try to get Legal Entity instance from fields 
			LglEntity legalEntity = fields.get( "legalEntity" );
			
			// If Legal Entity is specified 
			if ( legalEntity != null )
				// Set Legal Entity for TractnsModel object
				transactionsModel.setLegalEntity( legalEntity );
		}
		
		// Get T-Accts of Transactions Model
		Vector<TAcct>  accts = transactionsModel.getTAccs();
		
		// Get transactions of Transactions Model
		Vector<TrActn> trs = transactionsModel.getTransactions();
		
		// If output is empty - remove object from the list
		if ( (trs == null || trs.size() == 0) && (accts == null || accts.size() == 0) )
			list[tabNum].remove(this);
		
		// Persist changes to Database
		persistToDB( accts, trs, transactionsModel );
	}
	
	/**
	 * Persists changes to Database
	 * @param accts List of T-Accts
	 * @param trs	List of Transactions
	 * @param tm	Transactions Model
	 */
	private void persistToDB( Vector<TAcct>  accts, Vector<TrActn> trs, TractnsModel tm )
	{
		EntityTransaction et = null;
		
		try
     	{
			// Get Entity manager
			EntityManager em = Database.getEntityManager();
			
			et = em.getTransaction();
			
			// Start transaction
			et.begin();
			
			// Persist T-accts of Transactions Model
			for ( TAcct acct : accts )
				em.persist( acct );
			
			// Persist transactions of Transactions Model
			for ( TrActn tr : trs )
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
     * Removes TractnsSimulationModel data from database 
     */
    @Override
    public void removeFromDB()
    {
    	TractnsModel tm  = fields.get( "transactionsModel" );
    	
    	// If EntityManager object is created and TractnsSimulationModel argument is specified
        if ( tm != null  )
        {
        	// Get LglEntity from TractnsModel
        	LglEntity lglEntity = tm.getLglEntity();
        	
        	// If LglEntity is specified
        	if ( lglEntity != null )
        	{
        		// Get Legal Entity Model by Legal Entity
        		LglEntityModel lglEntityModel = LglEntityModel.getByEntity( lglEntity );
        		
        		// Try to find Legal Entity Model in current list of entities
        		int idx = entities.indexOf( lglEntityModel );
        		
        		// If Legal Entity Model is found
            	if ( idx >= 0 )
            		// Remove it from current tab list
            		list[idx].remove( this );
        	}
        	
        	Database.removeFromDB( tm );
        }
    }

	@Override
	protected void init( String[][] header, String[][][] table ) 
	{ }
	
} // End of class ** TractnsSimulationModel **