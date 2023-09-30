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

	/*Returns stringified binary number (base 2)
	 * from hexadecimal code (base 16)
	 * */
	public String ReturnBitsFromHex(String hexCode) {
		int integerValue = Integer.parseInt(hexCode, 16);
		String binaryString = Integer.toBinaryString(integerValue);
		return binaryString;
	}

	/*Returns decimal form (base 10)
	 * from binary string (base 2)
	 * */
	public int ReturnDecimalFromBinary(String binaryString) {
		int decimalEquivalent = Integer.parseInt(binaryString, 10);
		return decimalEquivalent;
	}

	/* Returns binary string (base 2)
	 * resulted from the addition of two addresses
	 * */
	public String BinaryAddition(String addr, String effectiveAddr) {
		int addition = Integer.parseInt(addr, 2) + Integer.parseInt(effectiveAddr, 2);

		return Integer.toBinaryString(addition);
	}
}
