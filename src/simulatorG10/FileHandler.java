package simulatorG10;

import java.io.*;

/*
 * Class to perform file operations like read file content and store into memory
 * */
public class FileHandler {
	private final String filePath = "SupportFiles\\ProgramLoadFile.txt";
	public static File inputfile;

	/*
	 * Loads the input file "ProgramLoadFile.txt and loads it
	 * */
	public void LoadFile() throws FileNotFoundException {
		if (filePath == null || filePath.isEmpty()) {
			throw new FileNotFoundException("File path is null or empty.");
		}

		inputfile = new File(filePath);
		if (!inputfile.exists()) {
			throw new FileNotFoundException("File does not exist: " + filePath);
		}
	}

	/*
	 * Convert the file content which is in hexadecimal format (base 16)
	 * into integer and binary format and store the results in the memory
	 * */
	public void ConvertTheFile() throws Exception {
		BinaryOperations binaryOperation = BinaryOperations.GetBinaryOperationsObj();
		String line = "";
		String[] lineArray;
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(inputfile));
			String currentLine;
			while ((currentLine = bufferedReader.readLine()) != null) {
				line = currentLine;
				lineArray = line.split(" ");
				int location = Integer.parseInt(binaryOperation.ReturnBitsFromHex(lineArray[0]),10);
				String address = binaryOperation.ReturnBitsFromHex(lineArray[1]);
				Memory.LoadIntoMemory(location,address);
			}
			bufferedReader.close();

		} catch (FileNotFoundException e) {
			throw new IOException("Can't read the file");
		}

		return ;
	}
}
