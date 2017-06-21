package foundation;

import interfaces.Utilities;

/**
 * Class TreeList -  To store list in the form of tree
 * @author Peter Cross
 */
public class TreeList extends List implements Utilities
{
    /**
     * Adds item to the tree list
     * @param item Item to add
     */
    public void addItem( Object item )
    {
        // Check space for new item
        if ( pointer >= list.length )
            // Allocate additional memory, if necessary
            list = allocateMemory( list );

        // Create TreeItem onject for current item
        list[pointer++] = new TreeItem( item );
    }

    /**
     * Adds item to the tree list
     * @param item Tree Item to add
     */
    public void addTreeItem( TreeItem item )
    {
        // Check space for new item
        if ( pointer >= list.length )
            // Allocate additional memory, if necessary
            list = allocateMemory( list );

        list[pointer++] = item;
    }
	
    /**
     * Add node and children nodes to the tree list
     * @param item Node to add
     * @param node Children nodes
     */
    public void addNode( Object item, Object... node )
    {
        // Check space for new node
        if ( pointer >= list.length )
            // Allocate additional memory, if necessary
            list = allocateMemory( list );

        // Create TreeItem object for current node
        list[pointer++] = new TreeItem( item, node );
    }
	
    public TreeList()
    {
        super();
    }
	
} // End of class ** TreeList **