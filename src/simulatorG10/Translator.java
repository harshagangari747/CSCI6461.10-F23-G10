package simulatorG10;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import java.util.Map;

public class Translator {
	private Map<String, String> inputToWrite = new LinkedHashMap<String, String>();
	// private boolean lockPC = false;
	private int tempPC = 0;
	// private BinaryOperations binaryOperationsObj =
	// BinaryOperations.GetBinaryOperationsObj();
	private ArrayList<String> readInputFileData;
	private Map<Integer, String> endValue = new LinkedHashMap<Integer, String>();

	public Map<String, String> TranslateIntoHexCode(ArrayList<String> splitLines) throws Exception {
		readInputFileData = splitLines;

		String[] lineArray;
		for (String line : readInputFileData) {
			lineArray = line.split(" ");
			String operation = lineArray[0].replaceAll(":", "");
			String[] registersInformation = lineArray[1].split(",");
			try {
				Decode(operation, registersInformation);
			} catch (Exception e) {
				throw new Exception(e.getLocalizedMessage());
			}

		}
		FillUpEndValues();
		return inputToWrite;
	}

	private void Decode(String opCode, String[] byteInfo) throws Exception {
		InstructionMetaData instruction;
		if (byteInfo.length == 1) {
			instruction = InstructionMetaData.instructionMetaData.get(1).get(OpCodes.valueOf(opCode));
			DecodeSingleRegInfoIntoHex(instruction, byteInfo);

		}
		if (byteInfo.length == 2) {
			instruction = InstructionMetaData.instructionMetaData.get(2).get(OpCodes.valueOf(opCode));
			DecodeDoubleRegInfoIntoHex(instruction, byteInfo);

		}
		if (byteInfo.length == 3) {
			instruction = InstructionMetaData.instructionMetaData.get(3).get(OpCodes.valueOf(opCode));
			DecodeTripleRegInfoIntoHex(instruction, byteInfo);
		}
		if (byteInfo.length == 4) {
			instruction = InstructionMetaData.instructionMetaData.get(4).get(OpCodes.valueOf(opCode));
			DecodeQuadrapleRegInfoIntoHex(instruction, byteInfo);
		}

	}

	private void DecodeSingleRegInfoIntoHex(InstructionMetaData insMetaData, String[] byteInfo) {
		int parsedByteInfo = -1;
		String parsedByteInfoString = null;
		try {
			parsedByteInfo = Integer.parseInt(byteInfo[0]);
		} catch (Exception e) {
			parsedByteInfoString = byteInfo[0];
		}
		String first = UtilClass.ReturnWithAppendedZeroes(Integer.toBinaryString(parsedByteInfo), 2);

		switch (insMetaData.opCode) {
		case LOC: {
			tempPC = parsedByteInfo;
			break;
		}
		case Data: {
			if (byteInfo[0].equals("End")) {
				endValue.put(tempPC, null);
			} else {
				inputToWrite.put(Integer.toBinaryString(tempPC++), first);
			}
			break;
		}
		case RFS: {

			break;
		}
		case NOT: {
			StringBuffer bytePatternString = new StringBuffer(insMetaData.bytePattern);
			bytePatternString.replace(6, 8, first);
			break;
		}
		case End: {
			inputToWrite.put(Integer.toBinaryString(tempPC++), OpCodes.HALT.GetValue());
			break;
		}
		default:
			throw new IllegalArgumentException(
					"Opcode" + insMetaData.opCode + " is currently not supported for conversion ");
		}
	}

	private void DecodeDoubleRegInfoIntoHex(InstructionMetaData insMetaData, String[] byteInfo) throws Exception {
		String first = UtilClass.ReturnWithAppendedZeroes(Integer.toBinaryString(Integer.parseInt(byteInfo[0])), 2);
		String second = UtilClass.ReturnWithAppendedZeroes(Integer.toBinaryString(Integer.parseInt(byteInfo[1])), 5);
		StringBuffer bytePatternString = new StringBuffer(insMetaData.bytePattern);
		switch (insMetaData.opCode) {
		case LDX:
		case STX:
		case JMA:
		case JSR: {
			bytePatternString.replace(8, 10, first);
			bytePatternString.replace(11, 16, second);
			inputToWrite.put(Integer.toBinaryString(tempPC++), bytePatternString.toString());

			break;
		}

		case AIR:
		case SIR: {
			bytePatternString.replace(6, 8, first);
			bytePatternString.replace(11, 16, second);
			inputToWrite.put(Integer.toBinaryString(tempPC++), bytePatternString.toString());
			break;
		}
		case MLT:
		case DVD:
		case TRR:
		case AND:
		case OR: {
			bytePatternString.replace(6, 8, first);
			bytePatternString.replace(8, 10, second.substring(3, 5));
			inputToWrite.put(Integer.toBinaryString(tempPC++), bytePatternString.toString());
			break;
		}
		case IN:
		case OUT:
		case CHK: {
			throw new Exception("");
		}
		default:
			throw new IllegalArgumentException(
					"Opcode" + insMetaData.opCode + " is currently not supported for conversion ");
		}
	}

	private void DecodeTripleRegInfoIntoHex(InstructionMetaData insMetaData, String[] byteInfo) {
		String first = UtilClass.ReturnWithAppendedZeroes(Integer.toBinaryString(Integer.parseInt(byteInfo[0])), 2);
		String second = UtilClass.ReturnWithAppendedZeroes(Integer.toBinaryString(Integer.parseInt(byteInfo[1])), 2);
		String third = UtilClass.ReturnWithAppendedZeroes(Integer.toBinaryString(Integer.parseInt(byteInfo[2])), 5);
		StringBuffer bytePatternString = new StringBuffer(insMetaData.bytePattern);
		switch (insMetaData.opCode) {

		case LDR:
		case LDA:
		case STR:
		case JZ:
		case JNE:
		case SOB:
		case JGE:
		case AMR:
		case SMR: {
			bytePatternString.replace(6, 8, first);
			bytePatternString.replace(8, 10, second);
			bytePatternString.replace(11, 16, third);
			inputToWrite.put(Integer.toBinaryString(tempPC++), bytePatternString.toString());

			break;

		}
		case JCC: {
			break;
		}

		default:
			throw new IllegalArgumentException(
					"Opcode" + insMetaData.opCode + " is currently not supported for conversion ");
		}
	}

	private void DecodeQuadrapleRegInfoIntoHex(InstructionMetaData insMetaData, String[] byteInfo) {
		String first = UtilClass.ReturnWithAppendedZeroes(Integer.toBinaryString(Integer.parseInt(byteInfo[0])), 2);
		StringBuffer bytePatternString = new StringBuffer(insMetaData.bytePattern);
		switch (insMetaData.opCode) {
		case LDR: {
			String second = UtilClass.ReturnWithAppendedZeroes(Integer.toBinaryString(Integer.parseInt(byteInfo[1])), 2);
			String third = byteInfo[3];
			String four = UtilClass.ReturnWithAppendedZeroes(Integer.toBinaryString(Integer.parseInt(byteInfo[2])), 5);
			bytePatternString.replace(6, 8, first);
			bytePatternString.replace(8, 10, second);
			bytePatternString.replace(10, 11, third);
			bytePatternString.replace(11, 16, four);
			inputToWrite.put(Integer.toBinaryString(tempPC++), bytePatternString.toString());
			break;

		}
		case SRC:
		case RRC: {
			String four = UtilClass.ReturnWithAppendedZeroes(Integer.toBinaryString(Integer.parseInt(byteInfo[1])), 4);
			String third = byteInfo[2];
			String second = byteInfo[3];
			bytePatternString.replace(6, 8, first);
			bytePatternString.replace(8, 9, second);
			bytePatternString.replace(9, 10, third);
			bytePatternString.replace(12, 16, four);
			inputToWrite.put(Integer.toBinaryString(tempPC++), bytePatternString.toString());

			break;
		}
		default:
			throw new IllegalArgumentException(
					"Opcode" + insMetaData.opCode + " is currently not supported for conversion ");
		}
	}

	private void FillUpEndValues() {
		for (int i : endValue.keySet()) {
			inputToWrite.put(Integer.toBinaryString(i), Integer.toBinaryString(tempPC-1));
		}

	}

}
