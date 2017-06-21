package foundation;

import interfaces.Encapsulation;

/**
 * Class TreeItem - To create Items for Tree Structures
 * @author Peter Cross
 */
public class TreeItem extends Item implements Encapsulation
{
    private Data value;     // Value of tree item 

    public TreeList nodes;  // List of children nodes for current tree item

    /**
     * Adds nodes to the list of children nodes
     * @param node  Array or list of nodes to add
     */
    public void addNodes( Object... node )
    {
        for ( Object obj : node ) 
            nodes.addItem( obj );    
    }
	
    public TreeItem ( Object item )
    {
        value = Data.create( item );
        nodes = new TreeList();
    }
	
    public TreeItem ( Object item, Object... node )
    {
        this( item );

        addNodes( node );
    }	
	
} // End of class ** TreeItem **