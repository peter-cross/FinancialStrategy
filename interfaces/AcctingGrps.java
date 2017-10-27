package interfaces;

import foundation.AssociativeList;
import static interfaces.Utilities.hash;

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
				                { hash( "CA" ), 
				                  hash( "LTA" ), 
				                  hash( "CL" ), 
				                  hash( "LTL" ),
				                  hash( "SHE" ) } );

        groupsList.set( hash( "CA" ), new String[]
                                  { hash( "CCE" ),
                                	hash( "STI" ),
                                	hash( "AR" ),
                                	hash( "INV" ),
                                	hash( "PE" ) } );

        groupsList.set( hash( "AR" ), new String[]
                                   { hash( "CR" ),
                                	 hash( "TXR" ),
                                	 hash( "NR" ),
                                	 hash( "DR" ) } );

        groupsList.set( hash( "INV" ), new String[]
                                     { hash( "MRZ" ),
                                       hash( "DM" ),
                                       hash( "WIP" ),
                                       hash( "FG" ) } );
		
        groupsList.set( hash( "LTA" ), new String[]
                                    { hash( "FA" ),
                                      hash( "NTA" ),
                                      hash( "LTI" ),
                                      hash( "DPR" ) } );

        groupsList.set( hash( "DPR" ), new String[]
                                    { hash( "FAD" ),
                                      hash( "NTAD" ) } );

        groupsList.set( hash( "CL" ), new String[]
                                   { hash( "SVP" ),
                                	 hash( "CCP" ),
                                	 hash( "SR" ),
                                	 hash( "PP" ),
                                	 hash( "TXP" ),
                                	 hash( "AOE" ),
                                	 hash( "IP" ),
                                	 hash( "CPLTD" ),
                                	 hash( "NP" ),
                                	 hash( "BP" ),
                                	 hash( "FPITX" ),
                                	 hash( "DIVP" ) } );

        groupsList.set( hash( "TXP" ), new String[]
                                     { hash( "INTX" ),
                                       hash( "SLTX" ),
                                       hash( "PRTX" ),
                                       hash( "OTTX" ) } );
		
        groupsList.set( hash( "DIVP" ), new String[]
                                     { hash( "PSDIV" ),
                                       hash( "CSDIV" ) } );

        groupsList.set( hash( "LTL" ), new String[]
                                     { hash( "CRL" ),
                                       hash( "WL" ) } );

        groupsList.set( hash( "SHE" ), new String[]
                                    { hash( "CST" ),
                                      hash( "RE" ) } );

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
                                { hash( "SLR" ),
                                  hash( "INTI" ),
                                  hash( "INVI" ),
                                  hash( "FEG" ),
                                  hash( "COGS" ),
                                  hash( "OPE" ),
                                  hash( "DPR" ),
                                  hash( "FEXL" ),
                                  hash( "OPRI" ),
                                  hash( "INVL" ),
                                  hash( "IBIT" ),
                                  hash( "INTE" ),
                                  hash( "INCTX" ),
                                  hash( "NI" ) } );
		
        groupsList.set( hash( "SLR" ), new String[]
                                     { hash( "CRSL" ),
                                       hash( "CSSL" ) } );
		
        groupsList.set( hash( "OPRI" ), new String[]
                                      { hash( "RD" ),
                                    	hash( "DSN" ),
                                    	hash( "PCH" ),
                                    	hash( "PRD" ),
                                    	hash( "MKT" ),
                                    	hash( "DST" ),
                                    	hash( "CSS" ),
                                    	hash( "ADM" ) } );
        return groupsList;

    } // End of method ** incStt **

} // End of interface