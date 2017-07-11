package entities;

import javafx.scene.input.MouseEvent;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;

import java.util.List;
import java.util.Vector;
import java.util.ArrayList;

import foundation.Cipher;
import models.TransactionsGraphics;
import views.TransactionsModelView;

/**
 * Class TAccount - to store information for T-accounts
 * @author Peter Cross
 *
 */
@Entity
public class TAccount
{
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	private long 		taccountId;
	
	private String 		name;		// Account name
	private int 		col;		// Cell column number
	private int 		row;		// Cell row number
	
	@ElementCollection( fetch=FetchType.EAGER )
	@Column( name="VALUE" )
	private List<Integer> corrDt; // List of rows for transactions with corresponding Dt accounts
	
	@ElementCollection( fetch=FetchType.EAGER )
	@Column( name="VALUE" )
	private List<Integer> corrCr; // List of rows for transactions with corresponding Cr accounts
	
	private static TransactionsGraphics tg;	// Transactions graphics canvas
	
	/**
	 * Class default constructor
	 */
	public TAccount()
	{
		super();
	}
	
	/**
	 * Class constructor
	 * @param acctName T-account name
	 * @param e Mouse event
	 */
	public TAccount( String acctName, MouseEvent e )
	{
		name = Cipher.crypt(acctName);
		col = TransactionsModelView.getColumn(e);
		row = TransactionsModelView.getRow(e);
		
		corrDt = new ArrayList<>(); 
		corrCr = new ArrayList<>();
	}
	
	/**
	 * Sets graphics context for class objects
	 * @param trGraph Transactions Graphics object
	 */
	public static void setGraphics( TransactionsGraphics trGraph )
	{
		tg = trGraph;
	}
	
	/**
	 * Get row of T-account
	 * @return Row number
	 */
	public int getRow()   
	{
		return row;
	}
	
	/**
	 * Get column of T-account
	 * @return Column number
	 */
	public int getColumn()
	{
		return col;
	}
	
	/**
	 * Get corr. Dt account row number
	 * @return
	 */
	public List<Integer> getCorrDt()
	{
		return corrDt;
	}
	
	/**
	 * Get corr. Cr account row number
	 * @return
	 */
	public List<Integer> getCorrCr()
	{
		return corrCr;
	}
	
	/**
	 * Gets max row number at which there is some transaction
	 * @return Max row number
	 */
	public int getMaxRow()
	{
		// Get list of all rows where there are corr. accounts
		ArrayList<Integer> arr = new ArrayList<>( corrDt );
		arr.addAll( corrCr );
		
		int max = row;
		
		// Loop for each row from the list
		for ( int row : arr )
			max = Math.max( max, row );
		
		return max;
	}
	
	/**
	 * Adds row number for corresponding debit account
	 * @param acctRow Account row number to add
	 */
	public void addCorrDtAccount( int acctRow )
	{
		if ( acctRow >= 0 )
			corrDt.add( acctRow );
	}
	
	/**
	 * Adds row number for corresponding credit account
	 * @param acctRow Account row number to add
	 */
	public void addCorrCrAccount( int acctRow )
	{
		if ( acctRow >= 0 )
			corrCr.add( acctRow );
	}
	
	/**
	 * Deletes row number for corresponding debit account
	 * @param acctRow Row number to delete
	 */
	private void delCorrDtAccount( int acctRow )
	{
		// Try to find specified row in the list of corr.Dt accounts
		int ind = corrDt.indexOf( acctRow );
		
		// If row is found
		if ( ind != -1 )
			corrDt.remove( ind );
	}
	
	/**
	 * Deletes row number for corresponding credit account
	 * @param acctRow Row number to delete
	 */
	private void delCorrCrAccount( int acctRow )
	{
		// Try to find specified row in the list of corr.Cr accounts
		int ind = corrCr.indexOf( acctRow );
		
		// If row is found
		if ( ind != -1 )
			corrCr.remove( ind );
	}
	
	/**
	 * Draws T-account on canvas
	 */
	public void drawTAccount()
	{
		// Loop for each row of T-account
		for ( int r = row; r <= getMaxRow(); r++ )
		{
			// Check if in current row there is corresponding DT or CR account
			int idx1 = corrDt.indexOf(r),
				idx2 = corrCr.indexOf(r);
			
			// If there is corresponding debit account
			if ( idx1 >=0 && idx2 < 0 )
				drawLeftPartOfTransaction(r);
			
			// If there is corresponding credit account
			else if ( idx2 >=0 && idx1 < 0 )
				drawRightPartOfTransaction(r);
			
			// If there is corresponding debit and credit accounts
			else if ( idx1 >=0 && idx2 >= 0 )
				drawBothPartsOfTransaction(r);
			
			// There is no transaction at this row
			else if ( idx1 < 0 && idx2 < 0 )
				drawTAccountWithoutTransaction(r);
		}
	}
	
	/**
	 * Draws left part of T-account transaction
	 * @param acctRow Row to draw 
	 */
	private void drawLeftPartOfTransaction( int acctRow )
	{
		// If it's the most upper row of T-account
		if ( row == acctRow )
		{
			// Draw T-account with transaction left part
			tg.drawTransactionLeftTAccount( row, col );
		
			// Draw T-account name
			drawAccountName();
		}
		else
			// Draw straight vertical line with left part of transaction
			tg.drawTransactionLeftPart( acctRow, acctRow, col );
	}
	
	/**
	 * Draws right part of T-account transaction
	 * @param acctRow Row to draw 
	 */
	private void drawRightPartOfTransaction( int acctRow )
	{
		// If it's the most upper row of T-account
		if ( row == acctRow )
		{
			// Draw T-account with transaction right part
			tg.drawTransactionRightTAccount( row, col );
			
			// Draw T-account name
			drawAccountName();
		}
		else
			// Draw straight vertical line with right part of transaction
			tg.drawTransactionRightPart( acctRow, acctRow, col );
	}
	
	/**
	 * Draws left and right parts of T-account transaction
	 * @param acctRow Row to draw 
	 */
	private void drawBothPartsOfTransaction( int acctRow )
	{
		// If it's the most upper row of T-account
		if ( row == acctRow )
		{
			// Draw T-account with transaction right and left parts
			tg.drawTransactionTAccount( row, col );
			
			// Draw T-account name
			drawAccountName();
		}
		else
			// Draw straight vertical line with right and left part of transaction
			tg.drawTransactionTwoWayPart( acctRow, acctRow, col );
	}
	
	/**
	 * Draws part of T-account without transaction
	 * @param acctRow Row to draw 
	 */
	private void drawTAccountWithoutTransaction( int acctRow )
	{
		tg.clearCellContent( acctRow, col );
		
		// If it's the most upper row of T-account
		if ( row == acctRow )
		{
			// Draw just T-account sign
			tg.drawTAccount( row, col );
			// Draw T-account name
			drawAccountName();
		}
		else
		{
			// Draw vertical line for T-account for the whole height of cell
			tg.drawAccountVerticalLine( acctRow, col );
			
			// Create copy of Transaction Model transactions
			Vector<Transaction> transactions = new Vector( TransactionsModelView.getTransactions() );
			
			// Get list of transactions that have to be deleted from DB
			List<Transaction> toDel = TransactionsModelView.getToDelTransactions();
			// Remove these transactions from copy of existing Transaction Model transactions
			transactions.removeAll( toDel );
			
			// Get list of transactions that have to be added to DB
			List<Transaction> toAdd = TransactionsModelView.getToAddTransactions();
			// Add these transactions to the copy of existing Transaction Model transactions
			transactions.addAll( toAdd );
			
			// Draw transaction that goes though specified row and column
			tg.drawTransitTransaction( acctRow, col, transactions );
		}
	}
	
	/**
	 * Draws T-account name on canvas
	 */
	public void drawAccountName()
	{
		String txt = Cipher.decrypt(name);
		
		tg.drawText( txt, row, col, 0.15 );
	}
	
	/**
	 * Clears content of T-account row
	 */
	public void clearAccountRowContent()
	{
		// Loop for each row of transaction credit account
		for ( int r = row; r <= getMaxRow(); r++ )
			// Clear content of cells for transaction credit account
			tg.clearCellContent( r, col );
	}
	
	/**
	 * Redraws credit account of transaction in specified row
	 * @param acctRow Row of transaction
	 */
	public void redrawCrAccount( int accRow )
	{
		clearAccountRowContent();
		
		// For credit account delete row of corresponding debit account
		delCorrDtAccount( accRow );
		
		// Redraw Transaction credit account
		drawTAccount();
		
		// Loop for each row starting with T-account's row till specified row
		for ( int r = row; r <= accRow; r++ )
			// Draw transit transaction for current row and specified column
			tg.drawTransitTransaction( r, col, TransactionsModelView.getTransactions() );
	}
	
	/**
	 * Redraws debit account of transaction in specified row
	 * @param acctRow Row of transaction
	 */
	public void redrawDtAccount( int accRow )
	{
		clearAccountRowContent();
		
		// For DT account delete row of corresponding credit account
		delCorrCrAccount( accRow );
		
		// Redraw Transaction DT account
		drawTAccount();
		
		// Loop for each row starting with T-account's row till specified row
		for ( int r = row; r <= accRow; r++ )
			// Draw transit transaction for current row and specified column
			tg.drawTransitTransaction( r, col, TransactionsModelView.getTransactions() );
	}
	
	/**
	 * Deletes T-account
	 */
	public void deleteTAccount()
	{
		ArrayList<Transaction> transList = new ArrayList<>();
		
		// Loop through list of all transactions
		for ( Transaction t : TransactionsModelView.getTransactions() )
			// If T-account belongs to current transaction
			if ( this == t.getDt() || this == t.getCr() )
				// Add transaction to list of T-account transactions
				transList.add( t );
		
		// Loop for each transaction of T-account
		for ( Transaction t : transList )
			// Delete current transaction of T-account
			t.deleteTransaction();
		
		// Clear content around T-account's cell to the top and to the right
		tg.clearCellContent( row-1, col );
		tg.clearCellContent( row-1, col+1 );
		tg.clearCellContent( row, col+1 );
		
		// Loop though each row of T-account
		for ( int r = row; r <= getMaxRow(); r++ )
			// Clear cell content for each cell of T-account
			tg.clearCellContent( r, col );
		
		// Loop for each row starting with T-account's row till the bottom of the grid
		for ( int r = row; r < TransactionsModelView.ROWS; r++ )
			// Redraw transit transaction line if there is one
			tg.drawTransitTransaction( r, col, TransactionsModelView.getTransactions() );
		
		// Add this T-account to the list of T-accounts that have to be deleted from DB
		TransactionsModelView.addToDelTAccounts( this );
	}
}