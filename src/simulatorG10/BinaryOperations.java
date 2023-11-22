package simulatorG10;

import java.lang.*;
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
		int substractionValue = Integer.parseInt(value1, 2) - Integer.parseInt(value2, 2);
		return Integer.toBinaryString(substractionValue);
	}

	/*
	 * Returns hex code from decimal
	 */
	public String GetHexCodeFromDecimal(String value) {
		String hexEquivalentValue = Integer.toHexString(Integer.parseInt(value, 2));
		return hexEquivalentValue;

	}

	public String BinaryMultiplication(String rx, String ry) {
		long result = Integer.parseInt(rx, 2) * Integer.parseInt(ry, 2);
		return Long.toBinaryString(result);
	}

	public String BinaryDivision(String rx, String ry) {
		int numerator = Integer.parseInt(rx, 2);
		int denomenator = Integer.parseInt(ry, 2);
		int remainder = numerator % denomenator;
		int quoteint = numerator / denomenator;
		return Integer.toBinaryString(quoteint) + "&" + Integer.toBinaryString(remainder);
	}

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

}
