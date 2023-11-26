package simulatorG10;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Utility class to perform frequent operations
public class UtilClass {

	/*
	 * Gets the string with format XXXX XXXX XXXX XXXX or XXXX XXXX XXXX or XXXX
	 * XXXX to represent aesthetically and for better readability in the UI
	 */
	public static String GetStringFormat(String input) {

		StringBuilder formattedString = new StringBuilder();
		try {
			if (input.length() != 16 || input.length() != 12 || input.length() != 8) {
				for (int i = 0; i < input.length(); i += 4) {
					formattedString.append(input.substring(i, i + 4));
					if (i < input.length() - 4) {
						formattedString.append(" ");
					}
				}
			}
			return formattedString.toString();

		} catch (Exception e) {
			throw new IllegalArgumentException(
					" Instruction must be 16 characters long. PC/MBR must be 12 characters long.");
		}

	}

	/*
	 * Gets the string with full digits as per specifications It appends zeroes at
	 * the front to make the string 16,12,8,4,0 bits accordingly
	 */
	public static String ReturnWithAppendedZeroes(String text, int totalLength) {
		int requiredLength = totalLength - text.length();
		StringBuilder adjustedString = new StringBuilder();
		for (int i = 0; i < requiredLength; i++) {
			adjustedString.append(Constants.defaultSingleZero);
		}
		adjustedString.append(text);
		if (adjustedString.toString().length() > 16 && adjustedString.toString().length() != 32)
			return adjustedString.toString().substring(1);
		return adjustedString.toString();
	}

	/*
	 * Gets the string by removing spaces from the format XXXX XXXX XXXX XXXX as
	 * mentioned earlier
	 */
	public static String ReturnUnformattedString(String formattedText) {
		String[] partialString = formattedText.split(" ");
		return String.join("", partialString);
	}

	/*
	 * Regex Pattern to match the parts of instruction word. first 6 digits for
	 * opcodes, next 2 digits for GPR next 2 digits for IXR next 1 digit for
	 * Indirect Addressing next 5 digits for address
	 */
	public static Matcher GetGroups(String instruction) {

		String regexPattern = "^(\\d{6})(\\d{2})(\\d{2})(\\d{1})(\\d{5})$";
		Pattern pattern = Pattern.compile(regexPattern);
		Matcher patternMatch = pattern.matcher(instruction);
		if (patternMatch.matches())
			return patternMatch;
		else
			return null;
	}

	/* Returns the register's canonical value */
	public static String ReturnRegisterEncoding(Registers register) {
		if (register == Registers.IXR0)
			return "00";
		else if (register == Registers.IXR1)
			return "01";
		else if (register == Registers.IXR2)
			return "10";
		return "11";
	}
}
