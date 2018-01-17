package entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
	
	public Expense( String code, String name, String category )
	{
		update( code, name, category );
	}
	
	public void update( String code, String name, String category )
	{
		this.code = code;
		this.name = name;
		this.category = category;
	}
	
	public String getCode()
	{
		return code;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getCategory()
	{
		return category;
	}	
}