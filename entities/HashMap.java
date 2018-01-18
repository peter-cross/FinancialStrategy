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
	private String hashValue;
	
	private static  List<HashMap> list;       // List of Items
    	
	public HashMap()
	{
		super();
	}
	
	public HashMap( String... args )
	{
		update( args );
	}
	
	public void update( String... args )
	{
		this.hashKey = args[0];
		this.hashValue = Cipher.crypt( args[1] );
	}

	public static String getByKey( String key )
	{
		if ( key.isEmpty() )
			return "";
		
		if ( list != null )
			for ( HashMap d : list )
				if ( d.hashKey.equals( key ) )
					return Cipher.decrypt( d.hashValue );
		
		return "";
	}
	
	public static  List[] createList()
    {
    	if ( list == null )
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
        		//Database.persistToDB( new HashMap( "GL", "G/L Account" ) );
        		
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