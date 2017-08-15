package application;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import static interfaces.Constants.WIDTH;
import static interfaces.Constants.HEIGHT;
import static interfaces.Constants.taskBarHeight;

/**
 *
 * @author Peter Cross
 */
public class Clock extends Canvas
{
    private final GraphicsContext gc;  
    
    static private Hand[] hand;	// To store clock's hands
    static private int 	hour,	// Current hour
						min;	// Current min		
    
    static final double CLOCK_WIDTH = 400,
                        CLOCK_HEIGHT = 400,
                        PADDING = 40;
    static final int    X_CENTER = WIDTH/2,
                        Y_CENTER = (HEIGHT - taskBarHeight)/2,
                        RECT_STROKE = 2,	// Rectangle stroke
                        FACE_STROKE = 15,	// Face stroke
                        HAND_STROKE = 20,	// Hands stroke
                        MINUTE_HAND_LENGTH = (int) (Math.min(CLOCK_WIDTH/2, CLOCK_HEIGHT/2) * 8/10), // Minute hand length
                        HOUR_HAND_LENGTH   = MINUTE_HAND_LENGTH * 2/3,				     // Hour hand length
                        DEGREE_PER_HOUR = 30,	// Angle in degrees per one hour
                        DEGREE_PER_MINUTE = 6,	// Angle in degrees pe minute
                        NOON_HOUR = 12;		// Noon hour
		
    public static Clock getInstance()
    {
    	return new Clock();
    }
    
    /**
     * Initializes clock object
     */
    private void init()
    {
        final int DELAY = 5000; // Delay in millisec (5 sec)

        getCurrentTime();
        drawClock();

        new Timer().schedule( new TimerTaskListener(), 0, DELAY );
    }	
    
    private static void getCurrentTime()
    {
        // Create instance of Calendar object
        Calendar calendar = Calendar.getInstance();

        // Get current hour from calendar
        hour = calendar.get( Calendar.HOUR_OF_DAY );
        // Get current minute from calendar
        min = calendar.get( Calendar.MINUTE );
    }
    
    private void drawClock()
    {
        gc.clearRect( 0, 0, getWidth(), getHeight() );
    	
    	drawFace();
        drawTickMarks();
        drawHands();
    }
    
    private void drawFace()
    {
        double  x = (WIDTH - CLOCK_WIDTH - 2*PADDING)/2,
                y = (HEIGHT - taskBarHeight - CLOCK_HEIGHT - 2*PADDING)/2;
        
        gc.setStroke( Color.BLACK );
        gc.setLineWidth( FACE_STROKE );
        
        gc.setFill( Color.KHAKI );
        gc.fillOval( x + PADDING, y + PADDING, CLOCK_WIDTH, CLOCK_HEIGHT );
        gc.strokeOval( x + PADDING, y + PADDING, CLOCK_WIDTH, CLOCK_HEIGHT );
    }
    
    private void drawTickMarks()
    {
        final int   TICK_LENGTH = 20,
                    TICK_WIDTH = 5,
                    THICK_TICK_TIMES = 3,
                    CLOCK_HOURS = 12;
        
        int angleDegree,
            tickWidth;
         
        for ( int i = 0; i < CLOCK_HOURS; i++ )
        {
            angleDegree = DEGREE_PER_HOUR * i;
            
            tickWidth = ( i % 3 == 0 ? THICK_TICK_TIMES : 1 ) * TICK_WIDTH;
            
            drawTick( angleDegree, TICK_LENGTH, tickWidth );
        }
    }
    
    private void drawTick( int angleDegree, int tickLength, int tickWidth )
    {
        double angleRadians = radians( angleDegree );

        int h = (int) ( (CLOCK_HEIGHT - PADDING - 2*tickLength)/2 * Math.cos( angleRadians ) );
        int w = (int) ( (CLOCK_WIDTH - PADDING - 2*tickLength)/2 * Math.sin( angleRadians ) );
        
        int xStart = X_CENTER + w,
            yStart = Y_CENTER - h;

        // Calculate vertical and horizontal shift from tick's start where the tick ends
        h = (int) (tickLength * Math.cos( angleRadians ));
        w = (int) (tickLength * Math.sin( angleRadians ));

        // Calculate X and Y coordinates for tick's end
        int xEnd = xStart + w,
            yEnd = yStart - h;

        gc.setStroke( Color.BLACK );
        gc.setLineWidth( tickWidth );
        
        // Draw the tick
        gc.strokeLine( xStart, yStart, xEnd, yEnd );
    }
    
    private void drawHands()
    {
        // Calculate hour and min as a number with decimals
        double hourAndMin = hour + (double) min/60;

        // Calculate angle for hour hand
        double hourAngle = ( hourAndMin <= NOON_HOUR ? hourAndMin : hourAndMin - NOON_HOUR ) * DEGREE_PER_HOUR;
        // Calculate angle for minute hand
        double minAngle = min * DEGREE_PER_MINUTE;

        // Draw hour and minute hands
        hand[0].draw( hourAngle );
        hand[1].draw( minAngle );
    }
    
    private static double radians( double angleDegree )
    {
        return angleDegree * Math.PI / 180;
    }
    
    private Clock()
    {
        super( WIDTH, HEIGHT );
        
        gc = super.getGraphicsContext2D();
        
        // Create Hand objects for hour and minute hands
        hand = new Hand[2];
        hand[0] = new Hand( HOUR_HAND_LENGTH, Color.GRAY );
        hand[1] = new Hand( MINUTE_HAND_LENGTH, Color.DARKGRAY );
        
        init();
    }
    
    //****************************************************************************
    /**
     * Class TimerTaskListener - action listener for timer
     * @author Peter Cross
     * @version May 14, 2017
     */
    private class TimerTaskListener extends TimerTask
    {
        /**
         * Method invoked by timer in specified intervals of time
         */
        @Override
		public void run() 
		{
			int prevHour = hour,	// Store current hour
                prevMin = min;		// Store current min

            // Get current time
            getCurrentTime();

            // If hour or minute changed
            if ( prevHour != hour || prevMin != min )
                // Repaint the clock area
                drawClock();
		}	
    }
    
    //*****************************************************************************
    /**
     * Class Hand
     * @author Peter Cross
     * @version Feb 1, 2017
     */
    private class Hand
    {
        private int 	length;	// Hand's length
        private Color	color;	// Hand's color

        /**
         * Draws the hand at specified angle
         * @param angleDegree Angle in degrees for the hand
         */
        public void draw( double angleDegree )
        {
            // Convert degrees to radians
            double angleRadians = radians( angleDegree );

            // Calculate vertical and horizontal shift for hand's end
            int  h = (int) (length * Math.cos( angleRadians )),
                 w = (int) (length * Math.sin( angleRadians ));

            // Calculate X and Y coordinates for hand's end
            int xHour = X_CENTER + w,
                yHour = Y_CENTER - h;

            gc.setStroke( color );
            gc.setLineWidth( HAND_STROKE );
        
            // Draw the hand starting from the center of the clock
            gc.strokeLine( X_CENTER, Y_CENTER, xHour, yHour  );
        }

        /**
         * Constructor for class Hand
         * @param length Hand's length
         * @param color Hand's color
         */
        public Hand( int length, Color color )
        {
            this.length = length;
            this.color = color;
        }
    } // End of ** class Hand **
    
} // End of ** class Clock **