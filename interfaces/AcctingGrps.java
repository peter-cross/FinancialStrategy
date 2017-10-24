package interfaces;

import foundation.AssociativeList;
import static interfaces.Utilities.$;

/**
 * Interface AcctingGrps - To create structures of Balance Sheet and Income Statement
 * @author Peter Cross
 */
public interface AcctingGrps 
{
    AssociativeList balSht = balSht(),    // BalSht tree items
                    incStt = incStt();    // IncStt tree items
    
    /**
     * Creates tree items for BalSht
     * @return Created tree
     */
    static AssociativeList balSht()
    {
        AssociativeList groupsList  = new AssociativeList();

        groupsList.set( "root", new String[]
				                { $( "CA" ), 
				                  $( "LTA" ), 
				                  $( "CL" ), 
				                  $( "LTL" ),
				                  $( "SHE" ) } );

        groupsList.set( $( "CA" ), new String[]
                                  { $( "CCE" ),
                                	$( "STI" ),
                                	$( "AR" ),
                                	$( "INV" ),
                                	$( "PE" ) } );

        groupsList.set( $( "AR" ), new String[]
                                   { $( "CR" ),
                                	 $( "TXR" ),
                                	 $( "NR" ),
                                	 $( "DR" ) } );

        groupsList.set( $( "INV" ), new String[]
                                     { $( "MRZ" ),
                                       $( "DM" ),
                                       $( "WIP" ),
                                       $( "FG" ) } );
		
        groupsList.set( $( "LTA" ), new String[]
                                    { $( "FA" ),
                                      $( "NTA" ),
                                      $( "LTI" ),
                                      $( "DPR" ) } );

        groupsList.set( $( "DPR" ), new String[]
                                    { $( "FAD" ),
                                      $( "NTAD" ) } );

        groupsList.set( $( "CL" ), new String[]
                                   { $( "SVP" ),
                                	 $( "CCP" ),
                                	 $( "SR" ),
                                	 $( "PP" ),
                                	 $( "TXP" ),
                                	 $( "AOE" ),
                                	 $( "IP" ),
                                	 $( "CPLTD" ),
                                	 $( "NP" ),
                                	 $( "BP" ),
                                	 $( "FPITX" ),
                                	 $( "DIVP" ) } );

        groupsList.set( $( "TXP" ), new String[]
                                     { $( "INTX" ),
                                       $( "SLTX" ),
                                       $( "PRTX" ),
                                       $( "OTTX" ) } );
		
        groupsList.set( $( "DIVP" ), new String[]
                                     { $( "PSDIV" ),
                                       $( "CSDIV" ) } );

        groupsList.set( $( "LTL" ), new String[]
                                     { $( "CRL" ),
                                       $( "WL" ) } );

        groupsList.set( $( "SHE" ), new String[]
                                    { $( "CST" ),
                                      $( "RE" ) } );

        return groupsList;
	
    } // End of method ** balSht **
	
    /**
     * Creates tree items for IncStt
     * @return Created tree
     */
    static AssociativeList incStt()
    {
        AssociativeList groupsList = new AssociativeList();

        groupsList.set( "root", new String[]
                                { $( "SLR" ),
                                  $( "INTI" ),
                                  $( "INVI" ),
                                  $( "FEG" ),
                                  $( "COGS" ),
                                  $( "OPE" ),
                                  $( "DPR" ),
                                  $( "FEXL" ),
                                  $( "OPRI" ),
                                  $( "INVL" ),
                                  $( "IBIT" ),
                                  $( "INTE" ),
                                  $( "INCTX" ),
                                  $( "NI" ) } );
		
        groupsList.set( $( "SLR" ), new String[]
                                     { $( "CRSL" ),
                                       $( "CSSL" ) } );
		
        groupsList.set( $( "OPRI" ), new String[]
                                      { $( "RD" ),
                                    	$( "DSN" ),
                                    	$( "PCH" ),
                                    	$( "PRD" ),
                                    	$( "MKT" ),
                                    	$( "DST" ),
                                    	$( "CSS" ),
                                    	$( "ADM" ) } );
        return groupsList;

    } // End of method ** incStt **

} // End of interface