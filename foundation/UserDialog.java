package foundation;

import interfaces.Lambda.DialogRunnable;
import javafx.application.Platform;

/**
 * Class UserDialog - to invoke some code in Event Dispatch Thread
 * @author Peter Cross
 *
 */
public class UserDialog extends Thread 
{
	// Code to invoke
	private Runnable code;
	
	/**
	 * Class Constructor
	 * @param runnable Code to execute
	 */
	public UserDialog( Runnable runnable  )
	{
		code = runnable;
	}
	
	/**
	 * Gets invoked automatically from thread
	 */
	@Override
	public void run() 
	{
		// Execute code later in Event Dispatch Thread
		try
		{
			Platform.runLater( code );
		}
		catch ( Exception e )
		{
			System.out.println( "Could not start Event Dispatch Thread" );
		}
	}
}