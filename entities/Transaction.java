package entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import java.util.ArrayList;

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
	
	private int 	 row;					// Row number of transaction
	private String	 description;			// Transaction description
	@ManyToOne( fetch=FetchType.EAGER )
	private TAccount dt;					// DT account for transaction
	@ManyToOne( fetch=FetchType.EAGER )
	private TAccount cr;					// CR account for transaction
	
	@ManyToOne( fetch=FetchType.EAGER )
	private ChartOfAccounts chartOfAccounts;	  // Chart of Accounts to which transaction belongs
	
	private static TransactionsGraphics[] tg; 	  // Transactions Graphics canvas
	private static String[]				  charts; // Chart of Accounts for Legal Entity
	
	/**
	 * Class mandatory constructor
	 */
	public Transaction()
	{
		super();
	}
	
	/**
	 * Class constructor with provided all transaction info
	 * @param dt DT T-account
	 * @param cr CR T-account
	 * @param description Transaction description
	 * @param chart Chart Of Accounts to which transaction belongs
	 */
	public Transaction( TAccount dt, TAccount cr, String description, ChartOfAccounts chart )
	{
		this.dt = dt;
		this.cr = cr;
		this.description = Cipher.crypt(description);
		
		row = getTransactionRow( cr, dt );
		
		cr.addCorrDtAccount( row );
		dt.addCorrCrAccount( row );
                
		chartOfAccounts = chart;
	}
	
	/**
	 * Class constructor with specified transaction accounts only
	 * @param dt DT T-account
	 * @param cr CR T-account
	 */
	public Transaction( TAccount dt, TAccount cr )
	{
		this( dt, cr, "", dt.getChartOfAccounts() );
		
		createTransaction();
	}
	
	/**
	 * Set graphics context for drawing transactions
	 * @param trGraph Transactions Graphics object
	 */
	public static void setGraphics( TransactionsGraphics[] trGraph )
	{
		tg = trGraph;
	}
	
	/**
	 * Sets names for Chart Of Accounts array
	 * @param chartOfAccounts Array of Chart Of Accounts' names
	 */
	public static void setChartsOfAccounts( String[] chartOfAccounts )
	{
		charts = chartOfAccounts;
	}
	
	/**
	 * Gets transaction's Chart Of Accounts index
	 * @return
	 */
	public int chartIndex()
	{
		if ( charts != null && charts.length > 0 && chartOfAccounts != null )
			return Math.max( 0, Utilities.indexOf( charts, chartOfAccounts.getName() ) );
		else
			return 0;
	}
	
	/**
	 * Returns transaction row
	 */
	public int getRow()
	{
		return row;
	}
	
	/**
	 * Returns transaction DT account
	 */
	public TAccount getDt()
	{
		return dt;
	}
	
	/**
	 * Returns transaction CR account
	 */
	public TAccount getCr()
	{
		return cr;
	}
	
	/**
	 * Returns Chart Of Accounts to which transaction belongs
	 */
	public ChartOfAccounts getChartOfAccounts()
	{
		return chartOfAccounts;
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
		// Draw CR and DT T-accounts
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
		int idx = chartIndex();
		
		tg[idx].drawTransactionMiddlePart( row, cr.getColumn()+1, dt.getColumn()-1 );
	}
	
	/**
	 * Draws transaction description on Canvas
	 */
	private void drawTransactionDescription()
	{
		int idx = chartIndex();
		
		tg[idx].drawText( Cipher.decrypt(description), row, cr.getColumn()+1, 0.6 );
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
		
		int idx = chartIndex();
		
		// Enter transaction description
		setDescription( Utilities.enterTextInfo( tg[idx].getOwner(), "Transaction description") );
		
		// Draw transaction description on canvas
		drawTransactionDescription();
	}
	
	/**
	 * Deletes transaction and redraws the grid content affected
	 */
	public void deleteTransaction()
	{
		int idx = chartIndex();
		
		// Loop for each transaction column
		for ( int col =  cr.getColumn(); col <= dt.getColumn(); col++ )
			// Clear content of transaction cells
			tg[idx].clearCellContent( row, col );
		
		// Get list of transit T-accounts
		ArrayList<TAccount> accList = transitTAccounts();
		
		// Redraw CR and DT T-accounts
		cr.redrawCrAccount(row);
		dt.redrawDtAccount(row);
		
		// Add current transaction to the list of transactions that have to be deleted from DB
		TransactionsModelView.addToDelTransactions( this );
		
		// Redraw transit T-accounts
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