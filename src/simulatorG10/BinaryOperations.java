package simulatorG10;

import java.lang.*;
import java.util.*;

//Class to perform binary operations and conversion - follows singleton pattern
public class BinaryOperations {
	Map<String, String> word;
	private static BinaryOperations singletonHToBObj;

	private BinaryOperations() {
	}

	/*
	 * Returns instance of BinaryOperationsClass
	 */
	public static BinaryOperations GetBinaryOperationsObj() {
		if (singletonHToBObj == null)
			return singletonHToBObj = new BinaryOperations();
		return singletonHToBObj;
	}

	/*
	 * Returns stringified binary number (base 2) from hexadecimal code (base 16)
	 */
	public String ReturnBitsFromHex(String hexCode) {
		int integerValue = Integer.parseInt(hexCode, 16);
		String binaryString = Integer.toBinaryString(integerValue);
		return binaryString;
	}

	/*
	 * Returns decimal form (base 10) from binary string (base 2)
	 */
	public int ReturnDecimalFromBinary(String binaryString) {
		int decimalEquivalent = Integer.parseInt(binaryString, 10);
		return decimalEquivalent;
	}

	/*
	 * Returns binary string (base 2) resulted from the addition of two addresses
	 */
	public String BinaryAddition(String addr, String effectiveAddr) {
		int addition = Integer.parseInt(addr, 2) + Integer.parseInt(effectiveAddr, 2);

		return Integer.toBinaryString(addition);
	}

	/*
	 * Returns Bytecode from hex code
	 */
	public String ReturnHexFromByteCode(String binaryString) {
		int integerValue = Integer.parseInt(binaryString, 2);
		String hexString = Integer.toHexString(integerValue);
		return hexString;
	}

	/*
	 * Returns binary string by taking to values
	 */
	public String BinarySubstraction(String value1, String value2) {
		int substractionValue = Integer.parseInt(value1,2) - Integer.parseInt(value2,2);
		return Integer.toBinaryString(substractionValue);
	}

	/*
	 * Returns hex code from decimal
	 */
	public String GetHexCodeFromDecimal(String value) {
		String hexEquivalentValue = Integer.toHexString(Integer.parseInt(value, 2));
		return hexEquivalentValue;

	}
	
	public String BinaryMultiplication(String rx, String ry)
	{
		long result = Integer.parseInt(rx,2)*Integer.parseInt(ry,2);
		return Long.toBinaryString(result);
	}
	
	public String BinaryDivision(String rx, String ry)
	{
		int numerator = Integer.parseInt(rx,2);
		int denomenator = Integer.parseInt(ry,2);
		int remainder = numerator % denomenator;
		int quoteint = numerator/denomenator;
		return remainder+"&"+quoteint;
	}
	
}
