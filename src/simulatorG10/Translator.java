package simulatorG10;

import java.awt.SecondaryLoop;
import java.security.spec.EncodedKeySpec;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Translator {
	private Map<String, String> inputToWrite = new LinkedHashMap<String, String>();
	private boolean lockPC = false;
	private int tempPC = 0;
	private BinaryOperations binaryOperationsObj = BinaryOperations.GetBinaryOperationsObj();
	private ArrayList<String> readInputFileData;

	public Map<String, String> TranslateIntoHexCode(ArrayList<String> splitLines) {
		readInputFileData = splitLines;

		String[] lineArray;
		for (String line : readInputFileData) {
			lineArray = line.split(" ");
			String operation = lineArray[0].replaceAll(":", "");
			String[] registersInformation = lineArray[1].split(",");
			Decode(operation, registersInformation);

		}
		return inputToWrite;
	}

	private void Decode(String opCode, String[] byteInfo) {
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
		switch (insMetaData.opCode) {
		case LOC: {
			tempPC = lockPC ? tempPC : Integer.parseInt(byteInfo[0]);
			break;
		}
		case Data: {
			if (byteInfo[0].equals("End")) {
				int ind = readInputFileData.lastIndexOf("LOC");
				byteInfo[0] = readInputFileData.get(ind);
			}
			String binaryByteInfo = Integer.toBinaryString(Integer.parseInt(byteInfo[0]));
			inputToWrite.put(Integer.toBinaryString(tempPC++), binaryByteInfo);
			break;
		}
		case RFS: {
			break;
		}
		case NOT: {
			break;
		}
		case End: {
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + insMetaData);
		}
	}

	private void DecodeDoubleRegInfoIntoHex(InstructionMetaData insMetaData, String[] byteInfo) {
		String first = UtilClass.ReturnWithAppendedZeroes(Integer.toBinaryString(Integer.parseInt(byteInfo[0])), 2);
		String second = UtilClass.ReturnWithAppendedZeroes(Integer.toBinaryString(Integer.parseInt(byteInfo[1])), 5);
		switch (insMetaData.opCode) {
		case LDX: {
			StringBuffer bytePatternString = new StringBuffer(insMetaData.bytePattern);
			bytePatternString.replace(8, 10, first);
			bytePatternString.replace(11, 16, second);
			inputToWrite.put(Integer.toBinaryString(tempPC++), bytePatternString.toString());

			break;
		}
		case STX: {
			break;
		}
		case JMA: {
			break;
		}
		case JSR: {
			break;
		}
		case AIR: {
			break;
		}
		case SIR: {
			break;
		}
		case MLT: {
			break;
		}
		case DVD: {
			break;
		}
		case TRR: {
			break;
		}
		case AND: {
			break;
		}
		case ORR: {
			break;
		}
		case IN: {
			break;
		}
		case OUT: {
			break;
		}
		case CHK: {
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + insMetaData);
		}
	}

	private void DecodeTripleRegInfoIntoHex(InstructionMetaData insMetaData, String[] byteInfo) {
		String first = UtilClass.ReturnWithAppendedZeroes(Integer.toBinaryString(Integer.parseInt(byteInfo[0])), 2);
		String second = UtilClass.ReturnWithAppendedZeroes(Integer.toBinaryString(Integer.parseInt(byteInfo[1])), 2);
		String third = UtilClass.ReturnWithAppendedZeroes(Integer.toBinaryString(Integer.parseInt(byteInfo[2])), 5);
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
			StringBuffer bytePatternString = new StringBuffer(insMetaData.bytePattern);
			bytePatternString.replace(6, 8, first);
			bytePatternString.replace(8, 10, second);
			bytePatternString.replace(11, 16, third);
			inputToWrite.put(Integer.toBinaryString(tempPC++), bytePatternString.toString());

			break;

		}
		case JCC: {
			break;
		}
		case SRC: {
			break;
		}
		case RRC: {
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + insMetaData);
		}
	}

	private void DecodeQuadrapleRegInfoIntoHex(InstructionMetaData insMetaData, String[] byteInfo) {
		String first = UtilClass.ReturnWithAppendedZeroes(Integer.toBinaryString(Integer.parseInt(byteInfo[0])), 2);
		String second = UtilClass.ReturnWithAppendedZeroes(Integer.toBinaryString(Integer.parseInt(byteInfo[1])), 2);
		String third = byteInfo[3];
		String four = UtilClass.ReturnWithAppendedZeroes(Integer.toBinaryString(Integer.parseInt(byteInfo[2])), 5);
		switch (insMetaData.opCode) {
		case LDR: {
			StringBuffer bytePatternString = new StringBuffer(insMetaData.bytePattern);
			bytePatternString.replace(6, 8, first);
			bytePatternString.replace(8, 10, second);
			bytePatternString.replace(10, 11, third);
			bytePatternString.replace(11, 16, four);
			inputToWrite.put(Integer.toBinaryString(tempPC++), bytePatternString.toString());
			break;

		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + insMetaData);
		}
	}

}
