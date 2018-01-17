package entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
	
	public Unit( String code, String name, String unitType, String baseUnit, double rate, int decimals )
	{
		update( code, name, unitType, baseUnit, rate, decimals );
	}
	
	public void update( String code, String name, String unitType, String baseUnit, double rate, int decimals )
	{
		this.code = code;
		this.name = name;
		this.unitType = unitType;
		this.baseUnit = baseUnit;
		this.rate = rate;
		this.decimals = decimals;
	}
	
	public String getCode()
	{
		return code;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getUnitType()
	{
		return unitType;
	}
	
	public String getBaseUnit()
	{
		return baseUnit;
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