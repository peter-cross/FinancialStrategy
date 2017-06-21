package forms;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.net.URL;
import java.util.Scanner;

import interfaces.Constants;
import interfaces.Encapsulation;
import interfaces.Utilities;

/**
 * Class SimpleReport - to create simple reports and display them in separate window
 * @author Peter Cross
 *
 */
public class SimpleReport extends Stage implements Encapsulation, Utilities, Printable, Constants
{
    /*          Properties   	                                                                                      */
    /******************************************************************************************************************/
    protected Stage		form;			// Form which created this dialog
    protected Scene		scene;			// Scene object
    protected String 		title;			// Report title

    protected Component 	content;		// Area for content
    protected String[]		output;			// Output string array
    protected String		outputStr;		// Output string

    /*          Methods                                                                                               */
    /******************************************************************************************************************/

    /**
     * Adds content to the report
     */
    public void addContent()
    {
        // If nothing to display
        if ( output == null )
            return;

        // If output contains only 2 lines of text
        if ( output.length < 3 )
        {
            // Display output as message in dialog window
            displayMessage( output );
            return;
        }			

        // Set report's width and height
        double width = Math.min( WIDTH, 800 ) - 5;
        double height = Math.min( HEIGHT, 500 ) - 5;

        // Create scene object with vertical box
        scene = new Scene( new VBox(), width, height );

        outputStr = "";		// To store output string
		
        int maxLength = 0,	// To store the length of the longest output line
            rows = 0;		// To store the number of rows in output string
        
        // Loop for each output line
        for ( String line : output ) 
        {
            // If output line is not empty
            if ( line != null && !line.isEmpty() ) 
            {
                // Add line to output string
                outputStr += line;
                // Get the maximum length among output lines to this point
                maxLength = Math.max( line.trim().length(), maxLength );
                // Increment rows counter
                rows++;
                
                // If output line starts with new-line symbol
                if ( line.startsWith("\n") ) 
                    // Increment rows counter
                    rows++;
            }
        }
		
        // Create text object from array
        Text text = new Text( outputStr );

        // Set font for text object
        text.setFont( new Font( "Courier New", 13 ) );

        // Create scroll pane object
        ScrollPane scrollPane = new ScrollPane( );
		
        // Set text object as scroll pane content
        scrollPane.setContent( text );
        // Set size of scroll pane
        scrollPane.setPrefSize( width - 15, height - 15 );
        // Set up scroll pane background color
        scrollPane.setStyle( "-fx-background: white;" );

        // Get pane from scene object
        Pane pane = (Pane) scene.getRoot();
        // Add scroll pane to the pane
        pane.getChildren().add( scrollPane );
		
    } // End of method ** addContent **
	
    /**
     * Prints report on the printer
     * @param gr Graphics object to print
     * @param pageFmt Page format
     * @param pageNum Page number
     * @return Number representing if page is valid
     */
    @Override
    public int print( Graphics gr, PageFormat pageFmt, int pageNum ) throws PrinterException 
    {
        // Create Scanner object for output string
        Scanner out = new Scanner( outputStr );

        // Create array for each output line
        String[] line = new String[2];

        int pointer = 0; // To store lines pointer

        // Loop while there is a line in output string
        while( out.hasNextLine() )
        {
            // If not enough space in the array - allocate additional memory
            if ( pointer >= line.length )
                line = allocateMemory( line );
			
            // Store output line in the array
            line[pointer++] = out.nextLine();
	}
		
        // Close Scanner
        out.close();
		
        // Set mono font for reports
        java.awt.Font font = new java.awt.Font( "Courier New", java.awt.Font.PLAIN, 10 );
        gr.setFont( font );

        // Get font height
        int lineHeight = gr.getFontMetrics(font).getHeight();

        // Get page height
        double pageHeight = pageFmt.getImageableHeight();

        // Get lines per page
        int linesPerPage = ( (int)pageHeight/lineHeight );

        // Get number of page breaks for the report
        int numBreaks = (pointer-1)/linesPerPage;

        // Create array for page breaks positions
        int[] pageBreaks = new int[numBreaks];

        // Loop for each page break
        for ( int b = 0; b < numBreaks; b++ ) 
            // Store in the array position where page ends
            pageBreaks[b] = (b+1) * linesPerPage; 
			
        // Convert graphics object for output to 2D graphics 
        Graphics2D g2d = (Graphics2D) gr;

        // Transform page for 2D graphics object
        g2d.translate( pageFmt.getImageableX(), pageFmt.getImageableY() );

        /* Draw each line that is on this page.
         * Increment 'y' position by lineHeight
         * for each line.
         */
        int y = 0; 

        // Get from which line to start
        int start = (pageNum == 0) ? 0 : pageBreaks[pageNum-1];

        // Get till which line to loop
        int end   = (pageNum == pageBreaks.length) ? pointer : pageBreaks[pageNum];

        // Loop for page lines
        for ( int i = start; i < end; i++ ) 
        {
            // Get Y position of the line
            y += lineHeight;

            // Draw text line from lines array
            gr.drawString( line[i], 10, y );
	}
		
	return PAGE_EXISTS;
		
    } // End of method ** print **
	
    /**
     * Adds buttons to form
     */
    public void addButtons()
    {
        // Store this report object for action listener
        SimpleReport rp = this;

        // Create Print button object
        Button btnPrint = new Button( "Print" );

        // Set event handler on press button event 
        btnPrint.setOnAction( e -> 
        {
            // Create object for printer job
            PrinterJob job = PrinterJob.getPrinterJob();

            // Get default page format
            PageFormat pageFormat = job.defaultPage();

            // Get paper settings for default page format
            Paper page = pageFormat.getPaper();
			
            // Pass current report object to printer job
            job.setPrintable( (Printable) rp );

            // Set printable area for the page
            page.setImageableArea( 0, 0, pageFormat.getWidth(), pageFormat.getHeight() );

            // Pass page settings to page format
            pageFormat.setPaper( page );
			
            // validate page format settings and open page dialog
            job.pageDialog( job.validatePage( pageFormat ) );
			
	    // Open print dialog and try to print it if not cancelled
            if ( job.printDialog() ) 
                try 
                {
                    // Try to print the report
                    job.print();
                } 
                catch ( PrinterException ex ) 
                {}
		    
            // Close dialog window
            close();
        } );
		
        // Create Close button object
        Button btnClose = new Button( "Close" );
        // Set event handler on press button event
        btnClose.setOnAction( e -> close() );

        // Create horizontal box object
        HBox buttons = new HBox();
        // Add Print and Close buttons to the box
        buttons.getChildren().addAll( btnPrint, btnClose  );

        // Set padding settings for the buttons
        buttons.setPadding( new Insets( 5, 5, 5, 5 ) );
        buttons.setSpacing( 5 );

        // Set buttons alignment to the center
        buttons.setAlignment( Pos.CENTER );

        // Get pane from Scene object
        Pane pane = (Pane) scene.getRoot();

        // Add buttons box to the pane
        pane.getChildren().addAll( buttons );
		
    } // End of method ** addButtons **
	
    /**
     * Displays the form
     */
    public void displayForm()
    {
        // Add buttons to the form
        addButtons();

        // Get URL object for style sheet
        URL styleSheet = getClass().getResource( "application.css" );

        // If style sheet is specified
        if ( styleSheet != null )
        {
            // Get style property from Scene object
            ObservableList<String> style = scene.getStylesheets();
            // Add style sheet to style attribute
            style.add( styleSheet.toExternalForm() );	
        }

        // Set Scene for the report
        setScene( scene );
		
        // Set report title
        setTitle( title );

        // Display
        show();
	
    } // End of method ** displayForm **
	
    /**
     * Initializes the form
     */
    public void init()
    {
        // Add report content
        addContent();

        // Display form
        displayForm();
    }
	
    /*          Constructors                                                                                          */
    /******************************************************************************************************************/
    public SimpleReport( Stage stage, String title, String[] output )
    {
        this.title = title;
        this.form = stage;
        this.output = output;

        init();
    }

} // End of class ** SimpleReport **