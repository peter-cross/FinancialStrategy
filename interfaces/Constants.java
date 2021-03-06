package interfaces;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;

import entities.HashMap;
import foundation.AssociativeList;

/**
 * Interface Constants - To keep common constants in one place
 * @author Peter Cross
 */
public interface Constants 
{
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Rectangle windowSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
    
    int taskBarHeight = screenSize.height - windowSize.height;
    int taskBarWidth = screenSize.width - windowSize.width;
    
    // All declared fields are implicitly: public static final
	
    int	YES 	= 0,	
        NO 		= 1,
        CANCEL	= 2,

        TODAY	= 0,
        DATE	= 1,
        PERIOD	= 2,
        ALL		= 3,

        WIDTH 	= windowSize.width - taskBarWidth, 	 //800, //
        HEIGHT 	= windowSize.height - taskBarHeight; //550; //
    
} // End of interface ** Constants **