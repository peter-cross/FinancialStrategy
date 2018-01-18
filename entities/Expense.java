package entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import foundation.Cipher;

@Entity
public class Expense 
{
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	private long 	expenseId;
	
	private String 	code;
	private String 	name;
	private String 	category;
	
	public Expense()
	{
		super();
	}
	
	public Expense( String... args )
	{
		update( args );
	}
	
	public void update( String... args )
	{
		code = Cipher.crypt( args[0] );
		name = Cipher.crypt( args[1] );
		category = Cipher.crypt( args[2] );
	}
	
	public String getCode()
	{
		return Cipher.decrypt( code );
	}
	
	public String getName()
	{
		return Cipher.decrypt( name );
	}
	
	public String getCategory()
	{
		return Cipher.decrypt( category );
	}	
}