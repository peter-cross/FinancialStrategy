package foundation;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Class TaskTimer - modified Timer class to use lambda expressions
 * @author Peter Cross
 *
 */
public class TaskTimer extends Timer 
{
	/**
	 * Schedules to invoke runnable task periodically
	 * @param task Runnable object with task to execute
	 * @param delay Delay between periods of invoking runnable task
	 */
	public void schedule( Runnable task, int delay )
	{
		/**
		 * Anonymous class based on TamerTask class to explicitly execute runnable task
		 */
		TimerTask timerTask = new TimerTask()
		{
			@Override
			public void run() 
			{
				task.run();
			}
		};
		
		// Invoke schedule method from inherited class
		super.schedule( timerTask, 0, delay );
	}
}