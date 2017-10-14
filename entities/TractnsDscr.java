package entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import foundation.Cipher;

@Entity
public class TractnsDscr 
{
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	private int tractnsDscrId;
	
	private String code;		// Transaction description code
	private String description;	// Transaction description
	
	public TractnsDscr()
	{
		super();
	}
	
	public TractnsDscr( String code, String description )
	{
		update( code, description );
	}
	
	
	public void update( String code, String description )
	{
		this.code = Cipher.crypt( code );
		this.description = Cipher.crypt( description );
	}
	
	public String getCode()
	{
		return Cipher.decrypt( code );
	}
	
	public String getDescription()
	{
		return Cipher.decrypt( description );
	}
	
	public String toString()
	{
		return getDescription();
	}	
}