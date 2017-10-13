package entities;

import javafx.scene.input.MouseEvent;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;

import java.util.List;
import java.util.Vector;
import java.util.ArrayList;

import models.TractnsGraphics;
import views.TractnsModelView;
import foundation.Cipher;
import interfaces.Utilities;

/**
 * Class TAcct - Entity implementation for TAcct Model
 * @author Peter Cross
 *
 */
@Entity
public class TAcct
{
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	private long 		tacctId;
	
	private String 		name;		// Acct name
	private int 		col;		// Cell column number
	private int 		row;		// Cell row number
	
	@ElementCollection( fetch=FetchType.EAGER )
	@Column( name="VALUE" )
	private List<Integer> corrDx; 	// List of rows for transactions with corresponding Dx accts
	
	@ElementCollection( fetch=FetchType.EAGER )
	@Column( name="VALUE" )
	private List<Integer> corrCx; 	// List of rows for transactions with corresponding Cx accts
	
	@ManyToOne( fetch=FetchType.EAGER )
	private COA coa;	  			// ChOfAccs to which T-acct belongs
	
	@ManyToOne( fetch=FetchType.EAGER )
	private GL gl;				  	// G/L Acct of T-acct
	
	private static TractnsGraphics[] tg;	  // Transactions graphics canvas
	private static String[]				  charts; // ChOfAccs for Legal Entity
	
	/**
	 * Class mandatory constructor
	 */
	public TAcct()
	{
		super();
	}
	
	/**
	 * Class constructor
	 * @param acctName T-acct name
	 * @param e Mouse event
	 * @param chart ChOfAccs to which T-Acct belongs
	 */
	public TAcct( String acctName, MouseEvent e, COA chart )
	{
		name = Cipher.crypt( acctName );
		col = TractnsModelView.getColumn( e );
		row = TractnsModelView.getRow( e );
		
		corrDx = new ArrayList<>(); 
		corrCx = new ArrayList<>();
                
        coa = chart;
	}
	
	/**
	 * Class constructor
	 * @param acctName T-acct name
	 * @param e Mouse event
	 * @param chart ChOfAccs to which T-Acct belongs
	 * @param glAcc G/L acct for created T-Acct
	 */
	public TAcct( String acctName, MouseEvent e, COA chart, GL glAcc )
	{
		this( acctName, e, chart );
	
		gl = glAcc;
	}
	
	/**
	 * Sets graphics context for class objects
	 * @param trGraph Transactions Graphics object
	 */
	public static void setGraphics( TractnsGraphics[] trGraph )
	{
		tg = trGraph;
	}
	
	/**
	 * Sets ChOfAccs' names 
	 * @param chOfAccs Array of ChOfAccs' names 
	 */
	public static void setChOfAccs( String[] chOfAccs )
	{
		charts = chOfAccs;
	}
	
	/**
	 * Gets index of ChOfAccs to which T-acct belongs
	 * @return Returns index of ChOfAccs
	 */
	public int chartIndex()
	{
		if ( charts != null && charts.length > 0 && coa != null )
			return Math.max( 0, Utilities.indexOf( charts, coa.getName() ) );
		else
			return 0;
	}
	
	/**
	 * Get row of T-acct
	 * @return Row number
	 */
	public int getRow()   
	{
		return row;
	}
	
	/**
	 * Get column of T-acct
	 * @return Column number
	 */
	public int getColumn()
	{
		return col;
	}
	
	/**
	 * Get corr. Dx acct row number
	 * @return
	 */
	public List<Integer> getCorrDx()
	{
		return corrDx;
	}
	
	/**
	 * Gets corr. Cx acct row number
	 * @return
	 */
	public List<Integer> getCorrCx()
	{
		return corrCx;
	}
	
	/**
	 * Gets ChOfAccs to which T-acct belongs
	 * @return
	 */
	public COA getChOfAccs()
	{
		return coa;
	}
	
	/**
	 * Gets G/L for T-acct
	 * @return
	 */
	public GL getGL()
	{
		return gl;
	}
	
	/**
	 * Gets max row number at which there is some transaction
	 * @return Max row number
	 */
	public int getMaxRow()
	{
		// Get list of all rows where there are corr. accts
		ArrayList<Integer> arr = new ArrayList<>( corrDx );
		arr.addAll( corrCx );
		
		int max = row;
		
		// Loop for each row from the list
		for ( int row : arr )
			max = Math.max( max, row );
		
		return max;
	}
	
	/**
	 * Adds row number for corresponding debix acct
	 * @param acctRow Acct row number to add
	 */
	public void addCorrDxAcct( int acctRow )
	{
		if ( acctRow >= 0 )
			corrDx.add( acctRow );
	}
	
	/**
	 * Adds row number for corresponding credix acct
	 * @param acctRow Acct row number to add
	 */
	public void addCorrCxAcct( int acctRow )
	{
		if ( acctRow >= 0 )
			corrCx.add( acctRow );
	}
	
	/**
	 * Deletes row number for corresponding debix acct
	 * @param acctRow Row number to delete
	 */
	private void delCorrDxAcct( int acctRow )
	{
		// Try to find specified row in the list of corr.Dx accts
		int ind = corrDx.indexOf( acctRow );
		
		// If row is found
		if ( ind != -1 )
			corrDx.remove( ind );
	}
	
	/**
	 * Deletes row number for corresponding credix acct
	 * @param acctRow Row number to delete
	 */
	private void delCorrCxAcct( int acctRow )
	{
		// Try to find specified row in the list of corr.Cr accts
		int ind = corrCx.indexOf( acctRow );
		
		// If row is found
		if ( ind != -1 )
			corrCx.remove( ind );
	}
	
	/**
	 * Draws T-acct on canvas
	 */
	public void drawTAcct()
	{
		// Loop for each row of T-acct
		for ( int r = row; r <= getMaxRow(); r++ )
		{
			// Check if in current row there is corresponding Dx or Cx acct
			int idx1 = corrDx.indexOf(r),
				idx2 = corrCx.indexOf(r);
			
			// If there is corresponding debix acct
			if ( idx1 >=0 && idx2 < 0 )
				drawLeftPartOfTransaction(r);
			
			// If there is corresponding credix acct
			else if ( idx2 >=0 && idx1 < 0 )
				drawRightPartOfTransaction(r);
			
			// If there is corresponding debix and credix accts
			else if ( idx1 >=0 && idx2 >= 0 )
				drawBothPartsOfTransaction(r);
			
			// There is no transaction at this row
			else if ( idx1 < 0 && idx2 < 0 )
				drawTAcctWithoutTransaction(r);
		}
	}
	
	/**
	 * Draws left part of T-acct transaction
	 * @param acctRow Row to draw 
	 */
	private void drawLeftPartOfTransaction( int acctRow )
	{
		int idx = chartIndex();
		
		// If it's the most upper row of T-acct
		if ( row == acctRow )
		{
			// Draw T-acct with transaction left part
			tg[idx].drawTransactionLeftTAcct( row, col );
		
			// Draw T-acct name
			drawAcctName();
		}
		else
			// Draw straight vertical line with left part of transaction
			tg[idx].drawTransactionLeftPart( acctRow, acctRow, col );
	}
	
	/**
	 * Draws right part of T-acct transaction
	 * @param acctRow Row to draw 
	 */
	private void drawRightPartOfTransaction( int acctRow )
	{
		int idx = chartIndex();
		
		// If it's the most upper row of T-acct
		if ( row == acctRow )
		{
			// Draw T-acct with transaction right part
			tg[idx].drawTransactionRightTAcct( row, col );
			
			// Draw T-acct name
			drawAcctName();
		}
		else
			// Draw straight vertical line with right part of transaction
			tg[idx].drawTransactionRightPart( acctRow, acctRow, col );
	}
	
	/**
	 * Draws left and right parts of T-acct transaction
	 * @param acctRow Row to draw 
	 */
	private void drawBothPartsOfTransaction( int acctRow )
	{
		int idx = chartIndex();
		
		// If it's the most upper row of T-acct
		if ( row == acctRow )
		{
			// Draw T-acct with transaction right and left parts
			tg[idx].drawTransactionTAcct( row, col );
			
			// Draw T-acct name
			drawAcctName();
		}
		else
			// Draw straight vertical line with right and left part of transaction
			tg[idx].drawTransactionTwoWayPart( acctRow, acctRow, col );
	}
	
	/**
	 * Draws part of T-acct without transaction
	 * @param acctRow Row to draw 
	 */
	private void drawTAcctWithoutTransaction( int acctRow )
	{
		int idx = chartIndex();
		
		tg[idx].clearCellContent( acctRow, col );
		
		// If it's the most upper row of T-acct
		if ( row == acctRow )
		{
			// Draw just T-acct sign
			tg[idx].drawTAcct( row, col );
			// Draw T-acct name
			drawAcctName();
		}
		else
		{
			// Draw vertical line for T-acct for the whole height of cell
			tg[idx].drawAcctVerticalLine( acctRow, col );
			
			// Create copy of TrActn Model transactions
			Vector<TrActn> transactions = new Vector( TractnsModelView.getTransactions() );
			
			// Get list of transactions that have to be deleted from DB
			List<TrActn> toDel = TractnsModelView.getToDelTransactions();
			// Remove these transactions from copy of existing TrActn Model transactions
			transactions.removeAll( toDel );
			
			// Get list of transactions that have to be added to DB
			List<TrActn> toAdd = TractnsModelView.getToAddTransactions();
			// Add these transactions to the copy of existing TrActn Model transactions
			transactions.addAll( toAdd );
			
			// Draw transaction that goes though specified row and column
			tg[idx].drawTransitTransaction( acctRow, col, transactions );
		}
	}
	
	/**
	 * Draws T-acct name on canvas
	 */
	public void drawAcctName()
	{
		// Get index for ChOfAccs for current Tab
		int idx = chartIndex();
		
		// If G/L Acct for T-acct is specified
		if ( gl != null )
			// Display number of G/L acct above T-acct name
			tg[idx].drawText( gl.getGlNumber(), row-1, col, 0.80 );
		
		// Display name of T-acct
		tg[idx].drawText( Cipher.decrypt(name), row, col, 0.15 );
	}
	
	/**
	 * Clears content of T-acct row
	 */
	public void clearAcctRowContent()
	{
		int idx = chartIndex();
		
		// Loop for each row of transaction credit acct
		for ( int r = row; r <= getMaxRow(); r++ )
			// Clear content of cells for transaction credit acct
			tg[idx].clearCellContent( r, col );
	}
	
	/**
	 * Redraws credix acct of transaction in specified row
	 * @param acctRow Row of transaction
	 */
	public void redrawCxAcct( int accRow )
	{
		clearAcctRowContent();
		
		// For credix acct delete row of corresponding debix acct
		delCorrDxAcct( accRow );
		
		// Redraw TrActn credix acct
		drawTAcct();
		
		// Get index of T-acct's ChOfAccs
		int idx = chartIndex();
		
		// Get all transactions in TractnsModelView form for specified ChOfAccs
		Vector<TrActn> transactions = TractnsModelView.getTransactions( idx );
		
		// Loop for each row starting with T-acct's row till specified row
		for ( int r = row; r <= accRow; r++ )
			// Draw transit transaction for current row and specified column
			tg[idx].drawTransitTransaction( r, col, transactions );
	}
	
	/**
	 * Redraws debix acct of transaction in specified row
	 * @param acctRow Row of transaction
	 */
	public void redrawDxAcct( int accRow )
	{
		clearAcctRowContent();
		
		// For Dx acct delete row of corresponding credix acct
		delCorrCxAcct( accRow );
		
		// Redraw TrActn Dx acct
		drawTAcct();
		
		// Get index of T-acct's ChOfAccs
		int idx = chartIndex();
		
		// Get all transactions in TractnsModelView form for specified ChOfAccs
		Vector<TrActn> transactions = TractnsModelView.getTransactions( idx );
				
		// Loop for each row starting with T-acct's row till specified row
		for ( int r = row; r <= accRow; r++ )
			// Draw transit transaction for current row and specified column
			tg[idx].drawTransitTransaction( r, col, transactions );
	}
	
	/**
	 * Deletes T-acct
	 */
	public void deleteTAcct()
	{
		ArrayList<TrActn> transList = new ArrayList<>();
		
		// Get index of T-acct's ChOfAccs
		int idx = chartIndex();
		
		// Get all transactions in TractnsModelView form for specified ChOfAccs
		Vector<TrActn> transactions = TractnsModelView.getTransactions( idx );
		
		// Loop through list of all transactions
		for ( TrActn t : transactions )
			// If T-acct belongs to current transaction
			if ( this == t.getDx() || this == t.getCx() )
				// Add transaction to list of T-acct transactions
				transList.add( t );
		
		// Loop for each transaction of T-acct
		for ( TrActn t : transList )
			// Delete current transaction of T-acct
			t.deleteTransaction();
		
		// Clear T-Acct cells and cells around it's 1st cell
		clearTAcctCellsAndArountdIt( idx );
		
		// Loop for each row starting with T-acct's row till the bottom of the grid
		for ( int r = row; r < TractnsModelView.ROWS; r++ )
			// Redraw transit transaction line if there is one
			tg[idx].drawTransitTransaction( r, col, transactions );
		
		// Add this T-acct to the list of T-accts that have to be deleted from DB
		TractnsModelView.addToDelTAccts( this );
	}
	
	/**
	 * Clears T-Acct cells and cells to the top and to the right of 1st cell
	 * @param idx ChOfAccs index
	 */
	private void clearTAcctCellsAndArountdIt( int idx )
	{
		// Clear content around T-acct's cell to the top and to the right
		tg[idx].clearCellContent( row-1, col );
		tg[idx].clearCellContent( row-1, col+1 );
		tg[idx].clearCellContent( row, col+1 );
		
		// Loop though each row of T-acct
		for ( int r = row; r <= getMaxRow(); r++ )
			// Clear cell content for each cell of T-acct
			tg[idx].clearCellContent( r, col );
	}
}