package entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import java.util.ArrayList;

import application.Main;
import foundation.Cipher;
import interfaces.Utilities;
import models.TransactionsGraphics;
import views.TransactionsModelView;

/**
 * Class Transaction - stores transaction parameters
 * @author Peter Cross
 *
 */
@Entity
public class Transaction
{
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	private long 	 transactionId;
	
	private int 	 row;			// Row number of transaction
	private String	 description;	// Transaction description
	@ManyToOne( fetch=FetchType.EAGER )
	private TAccount dt;			// Debit account for transaction
	@ManyToOne( fetch=FetchType.EAGER )
	private TAccount cr;			// Credit account for transaction
	
	private static TransactionsGraphics tg;
	
	/**
	 * Class default constructor
	 */
	public Transaction()
	{
		super();
	}
	
	/**
	 * Class constructor with provided all transaction info
	 * @param dt Dt T-account
	 * @param cr Cr T-account
	 * @param description Transaction description
	 */
	public Transaction( TAccount dt, TAccount cr, String description )
	{
		this.dt = dt;
		this.cr = cr;
		this.description = Cipher.crypt(description);
		
		row = getTransactionRow( cr, dt );
		
		cr.addCorrDtAccount( row );
		dt.addCorrCrAccount( row );
	}
	
	/**
	 * Class constructor with specified transaction accounts only
	 * @param dt Dt T-account
	 * @param cr Cr T-account
	 */
	public Transaction( TAccount dt, TAccount cr )
	{
		this( dt, cr, "" );
		
		createTransaction();
	}
	
	/**
	 * Set graphics context for drawing transactions
	 * @param trGraph Transactions Graphics object
	 */
	public static void setGraphics( TransactionsGraphics trGraph )
	{
		tg = trGraph;
	}
	
	/**
	 * Returns transaction row
	 */
	public int getRow()
	{
		return row;
	}
	
	/**
	 * Returns transaction Dt account
	 */
	public TAccount getDt()
	{
		return dt;
	}
	
	/**
	 * Returns transaction Cr account
	 */
	public TAccount getCr()
	{
		return cr;
	}
	
	/**
	 * Returns transaction description
	 */
	public String getDescription()
	{
		return Cipher.decrypt(description);
	}
	
	/**
	 * Sets transaction description
	 * @param description Transaction description
	 */
	private void setDescription( String description )
	{
		this.description = Cipher.crypt(description);
	}
	
	/**
	 * Gets row in which transaction for T-accounts can be placed
	 * @param acct1 First T-account
	 * @param acct2 Second T-account
	 * @return Row number
	 */
	private int getTransactionRow( TAccount acct1, TAccount acct2 )
	{
		// Get max row in which accounts are placed on grid
		int row = Math.max( acct1.getRow(), acct2.getRow() );
		
		// If in this row there is no transactions placed yet
		if ( acct1.getCorrDt().indexOf(row) * acct2.getCorrCr().indexOf(row) == 1 )
			return row;
		
		// Loop for each row starting from next calculated as max row of T-accounts
		for ( int acctRow = row+1; acctRow < TransactionsModelView.ROWS ; acctRow++ )
			// If in this row there is no transactions placed yet
			if ( acct1.getCorrDt().indexOf(acctRow) * acct2.getCorrCr().indexOf(acctRow)  == 1 )
				return acctRow;
		
		return row;
	}
	
	/**
	 * Draws transaction without description on Canvas
	 */
	public void drawTransaction()
	{
		// Draw Cr and Dt accounts
		cr.drawTAccount();
		dt.drawTAccount();
		
		// Draw middle part of transaction
		drawTransactionMiddlePart();
		
		// Draw transaction description
		drawTransactionDescription();
	}
	
	/**
	 * Draws transaction middle part on Canvas
	 */
	private void drawTransactionMiddlePart()
	{
		tg.drawTransactionMiddlePart( row, cr.getColumn()+1, dt.getColumn()-1 );
	}
	
	/**
	 * Draws transaction description on Canvas
	 */
	private void drawTransactionDescription()
	{
		tg.drawText( Cipher.decrypt(description), row, cr.getColumn()+1, 0.6 );
	}
	
	/**
	 * Draws complete transaction
	 */
	private void createTransaction()
	{
		cr.drawTAccount();
		dt.drawTAccount();
		
		// Draw middle part of transaction
		drawTransactionMiddlePart();
		
		// Enter transaction description
		setDescription( Utilities.enterTextInfo( tg.getOwner(), "Transaction description") );
		
		// Draw transaction description on canvas
		drawTransactionDescription();
	}
	
	/**
	 * Deletes transaction and redraws the grid content affected
	 */
	public void deleteTransaction()
	{
		// Loop for each transaction column
		for ( int col =  cr.getColumn(); col <= dt.getColumn(); col++ )
			// Clear content of transaction cells
			tg.clearCellContent( row, col );
		
		// Get list of transit T-accounts
		ArrayList<TAccount> accList = transitTAccounts();
		
		// Redraw Cr and Dt T-accounts
		cr.redrawCrAccount(row);
		dt.redrawDtAccount(row);
		
		TransactionsModelView.addToDelTransactions( this );
		
		// Redraw transit accounts
		for ( TAccount acc : accList )
			acc.drawTAccount();
	}
	
	/**
	 * Creates a list of transit T-accounts for specified transaction
	 * @return List of transit T-accounts
	 */
	private ArrayList<TAccount> transitTAccounts()
	{
		ArrayList<TAccount> accList = new ArrayList<>();
		
		// Loop for each transaction of Transactions Model
		for ( Transaction tr : TransactionsModelView.getTransactions() )
		{
			TAccount trDt = tr.getDt();
			TAccount trCr = tr.getCr();
			
			// If column of Dt account of current transaction is between columns of Cr and Dt account of the transaction 
			// and transaction row number is not greater than Max row of Dt account of current transaction
			if ( trDt.getColumn() > cr.getColumn() && trDt.getColumn() < dt.getColumn() 
				 && row <= trDt.getMaxRow() )
				accList.add( trDt );
			
			// If column of Cr account of current transaction is between columns of Cr and Dt account of the transaction 
			// and transaction row number is not greater than Max row of Cr account of current transaction
			else if ( trCr.getColumn() > cr.getColumn() && trCr.getColumn() < dt.getColumn() 
					  && row <= trCr.getMaxRow() )
				accList.add( trCr );
		}
		
		return accList;
	}	
}