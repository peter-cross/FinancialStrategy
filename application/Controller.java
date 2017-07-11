package application;

import javafx.stage.Stage;
import javafx.scene.control.Button;

import interfaces.Constants;
import views.RegistryView;

import static interfaces.Buttons.newActionButton;

/**
 * Class Controller - GUI controller implementation
 * @author Peter Cross
 *
 */
public class Controller implements Constants
{
	private static RegistryView  primaryView;	// Primary view stage
    private static RegistryView  secondaryView;	// Secondary view stage
    private static Stage 		 stage;			// Stage object for displaying GUI
	
    /**
     * Class constructor
     * @param st Stage to display GUI
     */
    private Controller( Stage st )
	{
		stage = st;
		
		// Create Registry for Primary View and save it
    	primaryView = new RegistryView( stage, "Transactions Simulation Models", "TransactionsSimulationModel", "LegalEntityModel", btnLegalEntities() );
	    
    	// Create Registry for Secondary View stage and save it
    	secondaryView = new RegistryView( stage, "Legal Entities", "LegalEntityModel", btnTransactionModels() );
        
		// Open Transaction Models Journal first
		openTransactionModelsJournal();
	}
	
    // Returns instance of controller
    public static Controller getInstance( Stage st )
	{
		return new Controller(st);
	}
	
    // Returns Application Stage object where GUI is displayed
    public static Stage getStage()
    {
    	return stage;
    }
    
	/**
     * Opens Legal Entities Journal
     * @param e Event
     */
    private <E> void openLegalEntitiesJournal( E e )
    {
        // Hide Primary View
        primaryView.close();
        
        secondaryView.display( WIDTH, HEIGHT );
    }
    
    // Opens Legal Entities Journal without any events
    private void openLegalEntitiesJournal()
    {
    	openLegalEntitiesJournal(null);
    }
    
    /**
     * Opens Transaction Models Journal 
     * @param e Event
     */
    private <E> void openTransactionModelsJournal( E e )
    {
    	// Hide Secondary View
    	secondaryView.close();
        
    	primaryView.display( WIDTH, HEIGHT );
    }
    
    // Opens Transaction Models Journal without any events
    private void openTransactionModelsJournal()
    {
    	openTransactionModelsJournal(null);
    }
    
    /**
     * Creates Button object for invoking Legal Entities Journal
     * @return Create Button object
     */
    private Button btnLegalEntities()
    {
        // Invoke method for creating Action Button and return created object
    	return newActionButton( "Legal Entities", this::openLegalEntitiesJournal );
    }
    
    /**
     * Creates Button object for invoking Transaction Models Journal
     * @return Create Button object
     */
    private Button btnTransactionModels()
    {
    	// Invoke method for creating Action Button and return created object
    	return newActionButton( "Transaction Models", this::openTransactionModelsJournal );
    }
}