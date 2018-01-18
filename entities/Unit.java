package entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import foundation.Cipher;

@Entity
public class Unit 
{
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	private long 	unitId;
	
	private String 	code;
	private String 	name;
	private String 	unitType;
	private String 	baseUnit;
	private double 	rate;
	private int 	decimals;
	
	public Unit()
	{
		super();
	}
	
	public Unit( Object... args )
	{
		update( args );
	}
	
	public void update( Object... args )
	{
		code = Cipher.crypt( (String) args[0] );
		name = Cipher.crypt( (String) args[1] );
		unitType = Cipher.crypt( (String) args[2] );
		baseUnit = Cipher.crypt( (String) args[3] );
		rate = (Double) args[4];
		decimals = (Integer) args[5];
	}
	
	public String getCode()
	{
		return Cipher.decrypt( code );
	}
	
	public String getName()
	{
		return Cipher.decrypt( name );
	}
	
	public String getUnitType()
	{
		return Cipher.decrypt( unitType );
	}
	
	public String getBaseUnit()
	{
		return Cipher.decrypt( baseUnit );
	}
	
	public double getRate()
	{
		return rate;
	}
	
	public int getDecimals()
	{
		return decimals;
	}
}