package simulatorG10;

public class UtilClass {
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
			throw new IllegalArgumentException("String must be 16 characters long.");
		}

	}

	public static String ReturnWithAppendedZeroes(String text, int totalLength) {
		int requiredLength = totalLength - text.length();
		StringBuilder adjustedString = new StringBuilder();
		for (int i = 0; i < requiredLength; i++) {
			adjustedString.append(Constants.defaultSingleZero);
		}
		adjustedString.append(text);
		return adjustedString.toString();
	}
}
