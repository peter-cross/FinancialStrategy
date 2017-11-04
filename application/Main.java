
package application;
	
import javafx.application.Application;
import javafx.stage.Stage;

import foundation.UserDialog;
import interfaces.Utilities;

/** 
 * Class Main - starts the program
 * @author Peter Cross
 * requires : eclipselink.jar, 
 * 			  javax.persistence.jar, 
 * 			  javax.transaction-api.jar, 
 * 			  mysql-connector-java.jar
 */
public final class Main extends Application
{
	public static final String TITLE = "Financial Strategy v.1.4";
	
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
    	Database.start();
        
    	// Invoke openAboutBox in a separate Event Dispatch Thread
    	new UserDialog( Utilities::openAboutBox ).start();
        
    	// Create instance of GUI Controller
    	Controller.getInstance(st);
    
    } // End of method ** start **
    
    /**
     * Gets invoked before exiting the application
     */
    @Override
    public void stop() 
    {
    	Database.stop();
    }     
} // End of class ** Main **