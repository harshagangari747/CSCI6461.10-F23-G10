package simulatorG10;

public class UtilClass {
	public static String GetStringFormat(String input)
	{
		if (input.length() != 16) {
            throw new IllegalArgumentException("String must be 16 characters long.");
        }

        StringBuilder formattedString = new StringBuilder();
        for (int i = 0; i < input.length(); i += 4) {
            formattedString.append(input.substring(i, i + 4));
            if (i < input.length() - 4) {
                formattedString.append(" ");
            }
        }
        return formattedString.toString();
	}
}

