package foundation;

/**
 * Class Cipher - simple crypting/decrypting
 * @author Peter Cross
 *
 */
public class Cipher 
{
	private final static char CYPHER = 63;	// To cypher the amounts
	private static Cipher instance;
	
	private final char 	keyLetter, 	// The amount to apply to letters
						keyNumeral;	// The amount to apply to numbers

	/**
	 * Class constructor
	 * @param ltrs Amount to apply to rotation of letters
	 * @param numbs Amount to apply to rotation of numbers
	 */
	private Cipher( int ltrs, int numbs )
	{
		keyLetter = (char)(CYPHER + ltrs); 
		keyNumeral = (char)(CYPHER + numbs);
	}
	
	/**
	 * Class default constructor
	 */
	private Cipher()
	{
		this(31, 5);
	}

	/**
	 * Creates instance of class object with specified parameters
	 * @param ltrs Amount to apply to letters
	 * @param numbs mount to apply to numbers
	 * @return Created class object
	 */
	public static Cipher getInstance( int ltrs, int numbs )
	{
		return instance = new Cipher(ltrs, numbs);
	}
	
	/**
	 * Creates instance of class object with default parameters
	 * @return Created class object
	 */
	public static Cipher getInstance()
	{
		return instance = new Cipher();
	}
	
	/**
	 * Crypts provided string
	 * @param in String to crypt
	 * @return Crypted string
	 */
	public static String crypt( String in )
	{
		return instance.transform( in, TransformType.CRYPT );
	}
	
	/**
	 * Decrypts provided string
	 * @param in String to decrypt
	 * @return Decrypted string
	 */
	public static String decrypt( String in )
	{
		return instance.transform( in, TransformType.DECRYPT );
	}
	
	/**
	 * Applies the transform for the filter
	 * @param in the string to filter
	 * @return the transformed string
	 */
	private String transform( String in, TransformType type ) 
	{
		if ( in == null )
			return "";
		
		String result = "";	// To store the result
		char ch;	// To store current string character
		
		// Loop for each string character
		for ( int i = 0; i < in.length(); i++ )
		{
			ch = in.charAt(i);
			
			// If current character is a letter
			if ( Character.isLetter(ch) )
				result += rotateLetter( ch, type );
			
			// If current character is a number
			else if ( Character.isDigit(ch) )
				result += rotateNumber( ch, type );
			
			// Otherwise, if it's another character - leave as it is
			else
				result += ch;
		}
		
		return result;
	}

	/**
	 * Rotates a letter character
	 * @param ch Character to rotate
	 * @return Transformed character
	 */
	private char rotateLetter( char ch, TransformType type )
	{
		char firstChar,	// First valid character
			 lastChar;	// Last valid character
		
		// If specified character is in Upper case
		if ( Character.isUpperCase(ch) )
		{
			firstChar = 'A';
			lastChar = 'Z';
		}
		else 
		{
			firstChar = 'a';
			lastChar = 'z';
		}
		
		// Rotate the character and return the transformed one
		return rotateChar( ch, firstChar, lastChar, keyLetter, type );
	}
	
	/**
	 * Rotates a number character
	 * @param ch Character to rotate
	 * @return Transformed character
	 */
	private char rotateNumber( char ch, TransformType type )
	{
		// Rotate the numeric character and return the transformed one
		return rotateChar( ch, '0', '9', keyNumeral, type );
	}
	
	/**
	 * Rotates a character
	 * @param ch Character to rotate
	 * @param firstValid The first valid character
	 * @param lastValid The last valid character
	 * @param amount The amount to rotate
	 * @return
	 */
	private char rotateChar( char ch, char firstValid, char lastValid, char amount, TransformType type )
	{
		char c;
		
		// Decrypt the amount and try to rotate the character
		if ( type == TransformType.CRYPT )
			c = (char) (ch + (amount - CYPHER) );
		else if ( type == TransformType.DECRYPT )
			c = (char) (ch + (-amount + CYPHER) );
		else
			return ch;
		
		int n;	// To store the number of positions to shift if the result is out of valid range
				
		// While the result is before the first valid character
		while ( c < firstValid )
		{
			// Calculate the shift amount
			n = firstValid - c - 1;
			// Get the result applying the shift to the last valid character
			c = (char) (lastValid - n);
		}
		
		// While the result is after the last valid character
		while ( c > lastValid )
		{
			// Calculate the shift amount
			n = c - lastValid - 1;
			// Get the result applying the shift to the first valid character
			c = (char) (firstValid + n);
		}
		
		return c;
	}
	
	/**
	 * Enumeration for Transformation Types 
	 * @author Peter Cross
	 *
	 */
	private enum TransformType
	{
		CRYPT, DECRYPT;
	}
}