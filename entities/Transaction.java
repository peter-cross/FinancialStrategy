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
	private TAcct dx;					// Dx account for transaction
	@ManyToOne( fetch=FetchType.EAGER )
	private TAcct cx;					// Cx account for transaction
	
	@ManyToOne( fetch=FetchType.EAGER )
	private COA coa;	  // ChOfAccs to which transaction belongs
	
	private static TransactionsGraphics[] tg; 	  // Transactions Graphics canvas
	private static String[]				  charts; // ChOfAccs for Legal Entity
	
	/**
	 * Class mandatory constructor
	 */
	public Transaction()
	{
		super();
	}
	
	/**
	 * Class constructor with provided all transaction info
	 * @param dx Dx T-account
	 * @param cx Cx T-account
	 * @param description Transaction description
	 * @param chart Chart Of Accounts to which transaction belongs
	 */
	public Transaction( TAcct dx, TAcct cx, String description, COA chart )
	{
		this.dx = dx;
		this.cx = cx;
		this.description = Cipher.crypt(description);
		
		row = getTransactionRow( cx, dx );
		
		cx.addCorrDxAcct( row );
		dx.addCorrCxAcct( row );
                
		coa = chart;
	}
	
	/**
	 * Class constructor with specified transaction accounts only
	 * @param dxt Dx T-account
	 * @param cx Cx T-account
	 */
	public Transaction( TAcct dx, TAcct cx )
	{
		this( dx, cx, "", dx.getChOfAccs() );
		
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
	 * @param chOfAccs Array of ChOfAccs' names
	 */
	public static void setChOfAccs( String[] chOfAccs )
	{
		charts = chOfAccs;
	}
	
	/**
	 * Gets transaction's Chart Of Accounts index
	 * @return
	 */
	public int chartIndex()
	{
		if ( charts != null && charts.length > 0 && coa != null )
			return Math.max( 0, Utilities.indexOf( charts, coa.getName() ) );
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
	 * Returns transaction Dx account
	 */
	public TAcct getDx()
	{
		return dx;
	}
	
	/**
	 * Returns transaction Cx account
	 */
	public TAcct getCx()
	{
		return cx;
	}
	
	/**
	 * Returns ChOfAccs to which transaction belongs
	 */
	public COA getChOfAccs()
	{
		return coa;
	}
	
	/**
	 * Returns transaction description
	 */
	public String getDescription()
	{
		return Cipher.decrypt( description );
	}
	
	/**
	 * Sets transaction description
	 * @param description Transaction description
	 */
	private void setDescription( String description )
	{
		this.description = Cipher.crypt( description );
	}
	
	/**
	 * Gets row in which transaction for T-accounts can be placed
	 * @param acct1 First T-account
	 * @param acct2 Second T-account
	 * @return Row number
	 */
	private int getTransactionRow( TAcct acct1, TAcct acct2 )
	{
		// Get max row in which accounts are placed on grid
		int row = Math.max( acct1.getRow(), acct2.getRow() );
		
		// If in this row there is no transactions placed yet
		if ( acct1.getCorrDx().indexOf(row) * acct2.getCorrCx().indexOf(row) == 1 )
			return row;
		
		// Loop for each row starting from next calculated as max row of T-accounts
		for ( int acctRow = row+1; acctRow < TransactionsModelView.ROWS ; acctRow++ )
			// If in this row there is no transactions placed yet
			if ( acct1.getCorrDx().indexOf(acctRow) * acct2.getCorrCx().indexOf(acctRow)  == 1 )
				return acctRow;
		
		return row;
	}
	
	/**
	 * Draws transaction without description on Canvas
	 */
	public void drawTransaction()
	{
		// Draw Cx and Dx T-accounts
		cx.drawTAcct();
		dx.drawTAcct();
		
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
		
		tg[idx].drawTransactionMiddlePart( row, cx.getColumn()+1, dx.getColumn()-1 );
	}
	
	/**
	 * Draws transaction description on Canvas
	 */
	private void drawTransactionDescription()
	{
		int idx = chartIndex();
		
		tg[idx].drawText( Cipher.decrypt(description), row, cx.getColumn()+1, 0.6 );
	}
	
	/**
	 * Draws complete transaction
	 */
	private void createTransaction()
	{
		cx.drawTAcct();
		dx.drawTAcct();
		
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
		for ( int col =  cx.getColumn(); col <= dx.getColumn(); col++ )
			// Clear content of transaction cells
			tg[idx].clearCellContent( row, col );
		
		// Get list of transit T-accounts
		ArrayList<TAcct> accList = transitTAccs();
		
		// Redraw Cx and Dx T-accounts
		cx.redrawCxAcct(row);
		dx.redrawDxAcct(row);
		
		// Add current transaction to the list of transactions that have to be deleted from DB
		TransactionsModelView.addToDelTransactions( this );
		
		// Redraw transit T-accounts
		for ( TAcct acc : accList )
			acc.drawTAcct();
	}
	
	/**
	 * Creates a list of transit T-accounts for specified transaction
	 * @return List of transit T-accounts
	 */
	private ArrayList<TAcct> transitTAccs()
	{
		ArrayList<TAcct> accList = new ArrayList<>();
		
		// Loop for each transaction of Transactions Model
		for ( Transaction tr : TransactionsModelView.getTransactions() )
		{
			TAcct trDx = tr.getDx();
			TAcct trCx = tr.getCx();
			
			// If column of Dx account of current transaction is between columns of Cx and Dx account of the transaction 
			// and transaction row number is not greater than Max row of Dx account of current transaction
			if ( trDx.getColumn() > cx.getColumn() && trDx.getColumn() < dx.getColumn() 
				 && row <= trDx.getMaxRow() )
				accList.add( trDx );
			
			// If column of Cx account of current transaction is between columns of Cx and Dx account of the transaction 
			// and transaction row number is not greater than Max row of Cx account of current transaction
			else if ( trCx.getColumn() > cx.getColumn() && trCx.getColumn() < dx.getColumn() 
					  && row <= trCx.getMaxRow() )
				accList.add( trCx );
		}
		
		return accList;
	}	
}