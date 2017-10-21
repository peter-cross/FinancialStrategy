package interfaces;

import entities.Dictionary;
import foundation.AssociativeList;

/**
 * Interface AcctingGrps - To create structures of Balance Sheet and Income Statement
 * @author Peter Cross
 */
public interface AcctingGrps 
{
    AssociativeList balSht = balSht(),    // BalSht tree items
                    incStt = incStt();    // IncStt tree items
    
    static String term( String key )
    {
    	return Dictionary.getByKey(key);
    }
    
    /**
     * Creates tree items for BalSht
     * @return Created tree
     */
    static AssociativeList balSht()
    {
        AssociativeList groupsList  = new AssociativeList();

        groupsList.set( "root", new String[]
				                { term( "CA" ), 
				                  term( "LTA" ), 
				                  term( "CL" ), 
				                  term( "LTL" ),
				                  term( "SHE" ) } );

        groupsList.set( term( "CA" ), new String[]
                                  { term( "CCE" ),
                                	term( "STI" ),
                                	term( "AR" ),
                                	term( "INV" ),
                                	term( "PE" ) } );

        groupsList.set( term( "AR" ), new String[]
                                   { term( "CR" ),
                                	 term( "TXR" ),
                                	 term( "NR" ),
                                	 term( "DR" ) } );

        groupsList.set( term( "INV" ), new String[]
                                     { term( "MRZ" ),
                                       term( "DM" ),
                                       term( "WIP" ),
                                       term( "FG" ) } );
		
        groupsList.set( term( "LTA" ), new String[]
                                    { term( "FA" ),
                                      term( "NTA" ),
                                      term( "LTI" ),
                                      term( "DPR" ) } );

        groupsList.set( term( "DPR" ), new String[]
                                    { term( "FAD" ),
                                      term( "NTAD" ) } );

        groupsList.set( term( "CL" ), new String[]
                                   { term( "SVP" ),
                                	 term( "CCP" ),
                                	 term( "SR" ),
                                	 term( "PP" ),
                                	 term( "TXP" ),
                                	 term( "AOE" ),
                                	 term( "IP" ),
                                	 term( "CPLTD" ),
                                	 term( "NP" ),
                                	 term( "BP" ),
                                	 term( "FPITX" ),
                                	 term( "DIVP" ) } );

        groupsList.set( term( "TXP" ), new String[]
                                     { term( "INTX" ),
                                       term( "SLTX" ),
                                       term( "PRTX" ),
                                       term( "OTTX" ) } );
		
        groupsList.set( term( "DIVP" ), new String[]
                                     { term( "PSDIV" ),
                                       term( "CSDIV" ) } );

        groupsList.set( term( "LTL" ), new String[]
                                     { term( "CRL" ),
                                       term( "WL" ) } );

        groupsList.set( term( "SHE" ), new String[]
                                    { term( "CST" ),
                                      term( "RE" ) } );

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
                                { term( "SLR" ),
                                  term( "INTI" ),
                                  term( "INVI" ),
                                  term( "FEG" ),
                                  term( "COGS" ),
                                  term( "OPE" ),
                                  term( "DPR" ),
                                  term( "FEXL" ),
                                  term( "OPRI" ),
                                  term( "INVL" ),
                                  term( "IBIT" ),
                                  term( "INTE" ),
                                  term( "INCTX" ),
                                  term( "NI" ) } );
		
        groupsList.set( term( "SLR" ), new String[]
                                     { term( "CRSL" ),
                                       term( "CSSL" ) } );
		
        groupsList.set( term( "OPRI" ), new String[]
                                      { term( "RD" ),
                                    	term( "DSN" ),
                                    	term( "PCH" ),
                                    	term( "PRD" ),
                                    	term( "MKT" ),
                                    	term( "DST" ),
                                    	term( "CSS" ),
                                    	term( "ADM" ) } );
        return groupsList;

    } // End of method ** incStt **

} // End of interface