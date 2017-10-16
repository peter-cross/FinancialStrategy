package entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import static interfaces.Utilities.enterTAcctInfo;
import static interfaces.Utilities.enterTractnInfo;

import java.util.ArrayList;

import foundation.AssociativeList;
import foundation.Cipher;
import interfaces.Utilities;
import models.TractnsGraphics;
import views.NodeView;
import views.TractnsModelView;

/**
 * Class TrActn - stores transaction parameters
 * @author Peter Cross
 *
 */
@Entity
public class TrActn
{
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	private long 	 trActnId;
	
	private int 	 row;				// Row number of transaction
	private String	 description;		// TrActn description
	@ManyToOne( fetch=FetchType.EAGER )
	private TAcct dx;					// Dx acct for transaction
	@ManyToOne( fetch=FetchType.EAGER )
	private TAcct cx;					// Cx acct for transaction
	
	@ManyToOne( fetch=FetchType.EAGER )
	private COA coa;	  				// ChOfAccs to which transaction belongs
	
	private static TractnsGraphics[] tg; 	  // Transactions Graphics canvas
	private static String[]			 charts;  // ChOfAccs for Legal Entity
	
	/**
	 * Class mandatory constructor
	 */
	public TrActn()
	{
		super();
	}
	
	/**
	 * Class constructor with provided all transaction info
	 * @param dx Dx T-acct
	 * @param cx Cx T-acct
	 * @param description TrActn description
	 * @param chart ChOfAccs to which transaction belongs
	 */
	public TrActn( TAcct dx, TAcct cx, String description, COA chart )
	{
		this.dx = dx;
		this.cx = cx;
		this.description = Cipher.crypt(description);
		
		row = getTractnRow( cx, dx );
		
		cx.addCorrDxAcct( row );
		dx.addCorrCxAcct( row );
                
		coa = chart;
	}
	
	/**
	 * Class constructor with specified transaction accts only
	 * @param dxt Dx T-acct
	 * @param cx Cx T-acct
	 */
	public TrActn( TAcct dx, TAcct cx )
	{
		this( dx, cx, "", dx.getChOfAccs() );
		
		createTractn();
	}
	
	/**
	 * Set graphics context for drawing transactions
	 * @param trGraph Transactions Graphics object
	 */
	public static void setGraphics( TractnsGraphics[] trGraph )
	{
		tg = trGraph;
	}
	
	/**
	 * Sets names for ChOfAccs array
	 * @param chOfAccs Array of ChOfAccs' names
	 */
	public static void setChOfAccs( String[] chOfAccs )
	{
		charts = chOfAccs;
	}
	
	/**
	 * Gets transaction's ChOfAccs index
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
	 * Returns transaction Dx acct
	 */
	public TAcct getDx()
	{
		return dx;
	}
	
	/**
	 * Returns transaction Cx acct
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
	 * @param description TrActn description
	 */
	private void setDescription( String description )
	{
		this.description = Cipher.crypt( description );
	}
	
	/**
	 * Gets row in which transaction for T-accts can be placed
	 * @param acct1 First T-acct
	 * @param acct2 Second T-acct
	 * @return Row number
	 */
	private int getTractnRow( TAcct acct1, TAcct acct2 )
	{
		// Get max row in which accts are placed on grid
		int row = Math.max( acct1.getRow(), acct2.getRow() );
		
		// If in this row there is no transactions placed yet
		if ( acct1.getCorrDx().indexOf(row) * acct2.getCorrCx().indexOf(row) == 1 )
			return row;
		
		// Loop for each row starting from next calculated as max row of T-accts
		for ( int acctRow = row+1; acctRow < TractnsModelView.ROWS ; acctRow++ )
			// If in this row there is no transactions placed yet
			if ( acct1.getCorrDx().indexOf(acctRow) * acct2.getCorrCx().indexOf(acctRow)  == 1 )
				return acctRow;
		
		return row;
	}
	
	/**
	 * Draws transaction without description on Canvas
	 */
	public void drawTractn()
	{
		// Draw Cx and Dx T-accts
		cx.drawTAcct();
		dx.drawTAcct();
		
		// Draw middle part of transaction
		drawTractnMiddlePart();
		
		// Draw transaction description
		drawTractnDescription();
	}
	
	/**
	 * Draws transaction middle part on Canvas
	 */
	private void drawTractnMiddlePart()
	{
		int idx = chartIndex();
		
		tg[idx].drawTractnMiddlePart( row, cx.getColumn()+1, dx.getColumn()-1 );
	}
	
	/**
	 * Draws transaction description on Canvas
	 */
	private void drawTractnDescription()
	{
		int idx = chartIndex();
		
		tg[idx].drawText( Cipher.decrypt(description), row, cx.getColumn()+1, 0.6 );
	}
	
	/**
	 * Draws complete transaction
	 */
	private void createTractn()
	{
		cx.drawTAcct();
		dx.drawTAcct();
		
		// Draw middle part of transaction
		drawTractnMiddlePart();
		
		int idx = chartIndex();
		
		String[] transInfo = Utilities.enterTractnInfo( (NodeView)tg[idx].getOwner() );
		
		String descr = transInfo[1];
		
		// Enter transaction description
		//setDescription( Utilities.enterTextInfo( tg[idx].getOwner(), "TrActn description") );
		setDescription( descr );
		
		// Draw transaction description on canvas
		drawTractnDescription();
	}
	
	/**
	 * Deletes transaction and redraws the grid content affected
	 */
	public void deleteTractn()
	{
		int idx = chartIndex();
		
		// Loop for each transaction column
		for ( int col =  cx.getColumn(); col <= dx.getColumn(); col++ )
			// Clear content of transaction cells
			tg[idx].clearCellContent( row, col );
		
		// Get list of transit T-accts
		ArrayList<TAcct> accList = transitTAccs();
		
		// Redraw Cx and Dx T-accts
		cx.redrawCxAcct(row);
		dx.redrawDxAcct(row);
		
		// Add current transaction to the list of transactions that have to be deleted from DB
		TractnsModelView.addToDelTractns( this );
		
		// Redraw transit T-accts
		for ( TAcct acc : accList )
			acc.drawTAcct();
	}
	
	/**
	 * Creates a list of transit T-accts for specified transaction
	 * @return List of transit T-accts
	 */
	private ArrayList<TAcct> transitTAccs()
	{
		ArrayList<TAcct> accList = new ArrayList<>();
		
		// Loop for each transaction of Transactions Model
		for ( TrActn tr : TractnsModelView.getTractns() )
		{
			TAcct trDx = tr.getDx();
			TAcct trCx = tr.getCx();
			
			// If column of Dx acct of current transaction is between columns of Cx and Dx acct of the transaction 
			// and transaction row number is not greater than Max row of Dx acct of current transaction
			if ( trDx.getColumn() > cx.getColumn() && trDx.getColumn() < dx.getColumn() 
				 && row <= trDx.getMaxRow() )
				accList.add( trDx );
			
			// If column of Cx acct of current transaction is between columns of Cx and Dx acct of the transaction 
			// and transaction row number is not greater than Max row of Cx acct of current transaction
			else if ( trCx.getColumn() > cx.getColumn() && trCx.getColumn() < dx.getColumn() 
					  && row <= trCx.getMaxRow() )
				accList.add( trCx );
		}
		
		return accList;
	}	
}