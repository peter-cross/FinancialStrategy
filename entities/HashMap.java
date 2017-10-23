package entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import application.Database;
import foundation.Cipher;

@Entity
public class HashMap 
{
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	private long hashMapId;
	
	private String hashKey;
	private String value;
	
	private static  List<HashMap> list;       // List of Items
    	
	public HashMap()
	{
		super();
	}
	
	public HashMap( String key, String value )
	{
		update( key, value );
	}
	
	public void update( String key, String value )
	{
		this.hashKey = key;
		this.value = Cipher.crypt( value );
	}

	public static String getByKey( String key )
	{
		if ( key.isEmpty() )
			return "";
		
		if ( list != null )
			for ( HashMap d : list )
				if ( d.hashKey.equals( key ) )
					return Cipher.decrypt( d.value );
		
		return "";
	}
	
	public static  List[] createList()
    {
    	if ( list == null )
            //createNewList();
    		loadListFromDB();
        
        return new List[] { list };
    }
    
	
	private static void loadListFromDB()
	{
		List<HashMap> lst = getFromDB();
		
		if ( lst == null )
			return;
		
		list = new ArrayList<HashMap>();
		
		for ( HashMap newHsh : lst )
			list.add( newHsh );
	}
	
	private static List<HashMap> getFromDB()
    {
    	// Get Entity Manager
    	EntityManager em = Database.getEntityManager();
    	
        if ( em != null )
        	try
        	{
        		List lst = em.createQuery( "SELECT c FROM HashMap AS c" ).getResultList();
        		// Do query for Entity class in DB and return results of query
        		return (List<HashMap>)lst;
            }
        	catch ( Exception e )
        	{
        		return null;
        	}
		
        return null;
    }
}