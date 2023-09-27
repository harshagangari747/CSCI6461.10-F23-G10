package simulatorG10;

import java.io.*;


public class FileHandler {
	private final String filePath = "SupportFiles\\ProgramLoadFile.txt";
	public static File inputfile;

	public void LoadFile() throws FileNotFoundException {
		if (filePath == null || filePath.isEmpty()) {
			throw new FileNotFoundException("File path is null or empty.");
		}

		inputfile = new File(filePath);
		if (!inputfile.exists()) {
			throw new FileNotFoundException("File does not exist: " + filePath);
		}
	}

	public void ConvertTheFile() throws Exception {
		HexToBitConverter hexToBitConverter = HexToBitConverter.GetHexToBitConverterObj();
		String line = "";
		String[] lineArray;
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(inputfile));
			String currentLine;
			while ((currentLine = bufferedReader.readLine()) != null) {
				line = currentLine;
				lineArray = line.split(" ");
				String location = hexToBitConverter.ReturnBitsFromHex(lineArray[0]);
				String address = hexToBitConverter.ReturnBitsFromHex(lineArray[1]);
				Memory.LoadIntoMemory(location,address);
			}
			bufferedReader.close();

		} catch (FileNotFoundException e) {
			throw new IOException("Can't read the file");
		}

		return ;
	}
}
