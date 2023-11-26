package simulatorG10;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.json.*;
import org.json.JSONArray;
import org.json.simple.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import simulatorG10.Exceptions.FileException;

/*
 * Class to perform file operations like read file content and store into memory
 * */
public class FileHandler {
	private final String jsonFilePath = "InstructionsMetaData.json";
	private final String inputTextFilePath = "ProgramLoadFile.txt";
	private final String hexFilePath = "HexFile.txt";
	public File inputfile;

	/*
	 * Loads the input file "ProgramLoadFile.txt and loads it
	 */
	public void LoadFile(FileTypes fileTypes) throws Exception {

		if (fileTypes == FileTypes.JSONFile) {

			if (jsonFilePath == null || jsonFilePath.isEmpty()) {
				throw new FileNotFoundException("File path is null or empty.");
			}
			inputfile = new File(jsonFilePath);
		} else if (fileTypes == FileTypes.IPLFile) {
			if (inputTextFilePath == null || inputTextFilePath.isEmpty()) {
				throw new FileNotFoundException("File path is null or empty.");
			}
			inputfile = new File(inputTextFilePath);
		} else if (fileTypes == fileTypes.HexFile) {
			if (inputTextFilePath == null || inputTextFilePath.isEmpty()) {
				throw new FileNotFoundException("File path is null or empty.");
			}
			inputfile = new File(hexFilePath);
		}

		if (!inputfile.exists()) {
			throw new FileException(
					"One of the files does not exist: " + jsonFilePath + " or " + inputTextFilePath);
		}
	}

	/*
	 * Returns File instance after loading it to read file further
	 */
	public File GetTextFromFile(String path) throws Exception {
		if (path == null || path.isEmpty()) {
			throw new FileException("File path is null or empty.");
		}

		inputfile = new File(path);
		if (!inputfile.exists()) {
			throw new FileException("File does not exist: " + path);
		}
		return inputfile;
	}

	/*
	 * Convert the file content which is in hexadecimal format (base 16) into
	 * integer and binary format and store the results in the memory
	 */
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
				int location = Integer.parseInt(binaryOperation.ReturnBitsFromHex(lineArray[0]), 2);
				String address = binaryOperation.ReturnBitsFromHex(lineArray[1]);
				Memory.LoadIntoMemory(location, address);
			}
			bufferedReader.close();

		} catch (FileNotFoundException e) {
			throw new FileException(e.getLocalizedMessage());
		}

		return;
	}

	/* This method reads the json metadata file and stores in the memory */
	public void ReadJsonFile() throws Exception {
		JSONParser parser = new JSONParser();
		org.json.simple.JSONArray jsonArray = (org.json.simple.JSONArray) parser.parse(new FileReader(inputfile));

		ArrayList<InstructionMetaData> metaDataArrayList = new ArrayList<>();
		try {
			for (int i = 0; i < jsonArray.size(); i++) {
				InstructionMetaData metaDataObj = new InstructionMetaData();
				JSONObject jsonObject = (JSONObject) jsonArray.get(i);
				metaDataObj.instructionParts = Integer.parseInt(jsonObject.get("instructionParts").toString());
				metaDataObj.bytePattern = (String) jsonObject.get("bytePattern");
				OpCodes opCode = OpCodes.valueOf(((String) jsonObject.get("opCode")).replaceAll(":", ""));
				metaDataObj.opCode = opCode;
				metaDataArrayList.add(metaDataObj);
			}

		} catch (Exception e) {
			throw new FileException(e.getLocalizedMessage());

		}

		InstructionMetaData.Store(metaDataArrayList);
	}

	/* This method is used to read input file and user file */
	public ArrayList<String> ReadTextFile() throws Exception {
		ArrayList<String> textFileInputHashMap = new ArrayList<String>();
		String line = "";
		String[] lineArray;
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(inputfile));
			String currentLine;
			while ((currentLine = bufferedReader.readLine()) != null) {
				textFileInputHashMap.add(currentLine);
			}
			bufferedReader.close();
		} catch (Exception e) {
			throw new FileException("Can't read generated input file : \"ProgramLoadFile.txt\"");
		}
		return textFileInputHashMap;
	}

	/*
	 * This method writes the decoded high-level codes as hexacodes in HexFile.txt
	 */
	public void WriteToHexFile(LinkedHashMap<String, String> inputHexData) throws Exception {
		BinaryOperations binOperationsObj = BinaryOperations.GetBinaryOperationsObj();
		FileWriter fileWriterObj = new FileWriter(hexFilePath);
		for (String line : inputHexData.keySet()) {
			String locationHexValue = binOperationsObj.GetHexCodeFromDecimal(line);
			String addressHexValue = binOperationsObj.GetHexCodeFromDecimal(inputHexData.get(line));
			fileWriterObj.write(locationHexValue + Constants.SINGLESPACE_STRING + addressHexValue + "\n");

		}
		fileWriterObj.close();

	}

}
