package simulatorG10;

import java.lang.*;
import java.math.BigInteger;
import java.security.interfaces.RSAKey;
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
		int decimalEquivalent = Integer.parseInt(binaryString, 2);
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
		int substractionValue = Integer.parseInt(value1, 2) - Integer.parseInt(value2, 2);
		String result = Integer.toBinaryString(substractionValue);
		if (String.valueOf(result).length() > 16) {
			result = result.substring(result.length() - 16);
		}
		return result;
	}

	/*
	 * Returns hex code from decimal
	 */
	public String GetHexCodeFromDecimal(String value) {
		String hexEquivalentValue = Integer.toHexString(Integer.parseInt(value, 2));
		return hexEquivalentValue;

	}

	/*
	 * Performs binary multiplication of two binary string values "rx" and "ry" and
	 * returns the result as binary string
	 */
	public String BinaryMultiplication(String rx, String ry) {
		long result = Integer.parseInt(rx, 2) * Integer.parseInt(ry, 2);
		return Long.toBinaryString(result);
	}

	/*
	 * Performs binary division of two binary string values "rx" and "ry" and return
	 * the result with quotient and remainder appended with &
	 */
	public String BinaryDivision(String rx, String ry) {
		try {
			int numerator = Integer.parseInt(rx, 2);
			int denomenator = Integer.parseInt(ry, 2);
			int remainder = numerator % denomenator;
			int quoteint = numerator / denomenator;
			return Integer.toBinaryString(quoteint) + "&" + Integer.toBinaryString(remainder);
		} catch (Exception e) {
			return "O";

		}

	}

	/*
	 * Performs equality operation on two binary string "rx" and "ry" and returns
	 * the result as boolean true if they are equal else false
	 */
	public boolean AreRegistersEqual(String rx, String ry) {
		char bit1, bit2;
		for (int i = 0; i < 16; i++) {
			bit1 = rx.charAt(i);
			bit2 = ry.charAt(i);

			if (bit1 != bit2) {
				return false;
			}
		}
		return true;
	}

	/*
	 * Performs AND operation on two binary string "rx" and "ry" and returns the
	 * result as binary string
	 */
	public String LogicalAnd(String rx, String ry) {
		String result = "";
		char bit1, bit2;
		for (int i = 0; i < 16; i++) {
			bit1 = rx.charAt(i);
			bit2 = ry.charAt(i);

			if (bit1 == '1' && bit2 == '1') {
				result += '1';
			} else {
				result += '0';
			}
		}
		return result;
	}

	/*
	 * Performs OR operation on two binary string "rx" and "ry" and returns the
	 * result as binary string
	 */
	public String LogicalOr(String rx, String ry) {
		String result = "";
		char bit1, bit2;
		for (int i = 0; i < 16; i++) {
			bit1 = rx.charAt(i);
			bit2 = ry.charAt(i);

			if (bit1 == '0' && bit2 == '0') {
				result += '0';
			} else {
				result += '1';
			}
		}
		return result;
	}

	/*
	 * Performs NOT operation on single binary string "rx" and returns the result as
	 * binary string
	 */
	public String binaryNot(String rx) {
		String result = "";
		for (int i = 0; i < 16; i++) {
			char bit = rx.charAt(i);
			if (bit == '1')
				result += '0';
			else
				result += '1';
		}
		return result;
	}

	/*
	 * Performs logical shift on given string "rx" by a count of "count" either
	 * right or left and returns the result as binary string
	 */
	public String DoLogicalShift(String rx, int count, boolean isLeftShift) {
		String paddingZeroes = new String("0").repeat(count);
		String buffer = rx;
		String result;
		if (isLeftShift) {
			buffer = buffer.substring(count, rx.length());
			buffer += paddingZeroes;
			result = buffer;
		} else {
			buffer = buffer.substring(0, rx.length() - count);
			paddingZeroes += buffer;
			result = paddingZeroes;
		}
		return result;
	}

	/*
	 * Performs Arithmetic shift on given string "rx" by a count of "count" either
	 * right or left and returns the result as binary string
	 */
	public String DoArithmeticShift(String rx, int count, boolean isLeftShift) {
		String padding;
		String buffer = rx;
		String result;
		if (isLeftShift) {
			padding = new String("0").repeat(count);
			buffer = buffer.substring(count, rx.length());
			buffer += padding;
			result = buffer;
		} else {
			if (rx.charAt(0) == '0')
				padding = new String("0");
			else
				padding = new String("1");
			int i = 1;
			while (i <= count) {
				padding += buffer.substring(0, rx.length() - 1);
				buffer = padding;
				padding = String.valueOf(buffer.charAt(0));
				i++;
			}

			result = buffer;
		}
		return result;
	}

	/*
	 * Performs logical rotate on given string "rx" by a count of "count" either
	 * right or left and returns the result
	 */
	public String DoLogicalRotate(String rx, int count, boolean isLeftRotate) {
		StringBuffer buffer = new StringBuffer(rx);
		String result;
		String bitsToRotate;
		if (isLeftRotate) {
			bitsToRotate = rx.substring(0, count);
			buffer.replace(0, count, "");
			buffer.append(bitsToRotate);
		} else {
			bitsToRotate = rx.substring(rx.length() - count, rx.length());
			buffer.replace(rx.length() - count, rx.length(), "");
			buffer.insert(0, bitsToRotate);
		}
		result = buffer.toString();
		return result;
	}

	/*
	 * Converts the character into its binary string equivalent and returns the
	 * result as binary string
	 */
	public String ConvertCharToBinaryString(char ch) {
		int asciiValue = (int) ch;
		String binaryCode = Integer.toBinaryString(asciiValue);
		return binaryCode;
	}

	/*
	 * Converts the binary string into character type equivalent and returns the
	 * result as a single character
	 */
	public char ConvertBinaryStringToChar(String bString) {
		int value = Integer.parseInt(bString, 2);
		char res = (char) value;
		return res;
	}
}
