package simulatorG10;

import simulatorG10.Exceptions.*;

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

	/*
	 * This method takes array of input file string where each line is one element.
	 * And passes this information to further decoding by splitting by commas and
	 * spaces.
	 */
	public Map<String, String> TranslateIntoHexCode(ArrayList<String> splitLines) throws Exception {
		readInputFileData = splitLines;

		String[] lineArray;
		for (String line : readInputFileData) {
			lineArray = line.trim().split(" ");
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

	/*
	 * This method takes the split instructions and matches the length of the
	 * address part and accordingly sends the information for further processing
	 */
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

	/* This method decodes instructions with single parameter */
	private void DecodeSingleRegInfoIntoHex(InstructionMetaData insMetaData, String[] byteInfo) throws Exception {
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
				inputToWrite.put(Integer.toBinaryString(tempPC++), null);
			} else {
				inputToWrite.put(Integer.toBinaryString(tempPC++), first);
			}
			break;
		}
		case RFS: {
			StringBuffer bytePatternString = new StringBuffer(insMetaData.bytePattern);
			String address = UtilClass.ReturnWithAppendedZeroes(Integer.toBinaryString(parsedByteInfo), 5);
			bytePatternString.replace(11, 16, address);
			inputToWrite.put(Integer.toBinaryString(tempPC++), bytePatternString.toString());

			break;
		}
		case NOT: {
			StringBuffer bytePatternString = new StringBuffer(insMetaData.bytePattern);
			bytePatternString.replace(6, 8, first);
			inputToWrite.put(Integer.toBinaryString(tempPC++), bytePatternString.toString());
			break;
		}
		case End: {
			inputToWrite.put(Integer.toBinaryString(tempPC++), OpCodes.HALT.GetValue());
			break;
		}
		default:
			throw new OpCodeNotSupportedException(
					"Opcode" + insMetaData.opCode + " is currently not supported for conversion ");
		}
	}

	/* This method decodes instructions with two parameters */
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
		case SIR:
		case IN:
		case OUT:
		case CHK: {
			bytePatternString.replace(6, 8, first);
			bytePatternString.replace(11, 16, second);
			inputToWrite.put(Integer.toBinaryString(tempPC++), bytePatternString.toString());
			break;
		}
		case MLT:
		case DVD:
		case TRR:
		case AND:
		case ORR: {
			bytePatternString.replace(6, 8, first);
			bytePatternString.replace(8, 10, second.substring(3, 5));
			inputToWrite.put(Integer.toBinaryString(tempPC++), bytePatternString.toString());
			break;
		}

		default:
			throw new OpCodeNotSupportedException(
					"Opcode" + insMetaData.opCode + " is currently not supported for conversion ");
		}
	}

	/* This method decodes instructions with three parameters */
	private void DecodeTripleRegInfoIntoHex(InstructionMetaData insMetaData, String[] byteInfo) throws Exception {
		String gprOrInd = UtilClass.ReturnWithAppendedZeroes(Integer.toBinaryString(Integer.parseInt(byteInfo[0])), 2);
		StringBuffer bytePatternString = new StringBuffer(insMetaData.bytePattern);
		Exception exceptio = new Exception();
		switch (insMetaData.opCode) {
		case LDR:
		case LDA:
		case STR:
		case JZ:
		case JNE:
		case SOB:
		case JGE:
		case AMR:
		case SMR:
		case JCC: {
			String indexReg = UtilClass.ReturnWithAppendedZeroes(Integer.toBinaryString(Integer.parseInt(byteInfo[1])),
					2);
			String address = UtilClass.ReturnWithAppendedZeroes(Integer.toBinaryString(Integer.parseInt(byteInfo[2])),
					5);
			bytePatternString.replace(6, 8, gprOrInd);
			bytePatternString.replace(8, 10, indexReg);
			bytePatternString.replace(11, 16, address);
			inputToWrite.put(Integer.toBinaryString(tempPC++), bytePatternString.toString());

			break;

		}
		case JMA:
		case JSR: {
			String address = UtilClass.ReturnWithAppendedZeroes(Integer.toBinaryString(Integer.parseInt(byteInfo[1])),
					5);
			String index = byteInfo[2];
			bytePatternString.replace(8, 10, gprOrInd);
			bytePatternString.replace(10, 11, index);
			bytePatternString.replace(11, 16, address);
			inputToWrite.put(Integer.toBinaryString(tempPC++), bytePatternString.toString());
			break;

		}
		case LDX:
		case STX: {
			String indirect = byteInfo[2];
			String address = UtilClass.ReturnWithAppendedZeroes(Integer.toBinaryString(Integer.parseInt(byteInfo[1])),
					5);
			bytePatternString.replace(8, 10, gprOrInd);
			bytePatternString.replace(10, 11, indirect);
			bytePatternString.replace(11, 16, address);
			inputToWrite.put(Integer.toBinaryString(tempPC++), bytePatternString.toString());
			break;
		}

		default:
			throw new OpCodeNotSupportedException(
					"Opcode" + insMetaData.opCode + " is currently not supported for conversion");

		}
	}

	/* This method decodes instructions with four parameters */
	private void DecodeQuadrapleRegInfoIntoHex(InstructionMetaData insMetaData, String[] byteInfo) throws Exception {
		String gpr = UtilClass.ReturnWithAppendedZeroes(Integer.toBinaryString(Integer.parseInt(byteInfo[0])), 2);
		StringBuffer bytePatternString = new StringBuffer(insMetaData.bytePattern);
		switch (insMetaData.opCode) {
		case LDA:
		case LDR:
		case STR:
		case AMR:
		case SMR:
		case JGE:
		case SOB:
		case JZ:
		case JNE:
		case JCC: {
			String indexReg = UtilClass.ReturnWithAppendedZeroes(Integer.toBinaryString(Integer.parseInt(byteInfo[1])),
					2);
			String indirect = byteInfo[3];
			String address = UtilClass.ReturnWithAppendedZeroes(Integer.toBinaryString(Integer.parseInt(byteInfo[2])),
					5);
			bytePatternString.replace(6, 8, gpr);
			bytePatternString.replace(8, 10, indexReg);
			bytePatternString.replace(10, 11, indirect);
			bytePatternString.replace(11, 16, address);
			inputToWrite.put(Integer.toBinaryString(tempPC++), bytePatternString.toString());
			break;

		}
		case SRC:
		case RRC: {
			String count = UtilClass.ReturnWithAppendedZeroes(Integer.toBinaryString(Integer.parseInt(byteInfo[1])), 4);
			String lr = byteInfo[2];
			String al = byteInfo[3];
			bytePatternString.replace(6, 8, gpr);
			bytePatternString.replace(8, 9, al);
			bytePatternString.replace(9, 10, lr);
			bytePatternString.replace(12, 16, count);
			inputToWrite.put(Integer.toBinaryString(tempPC++), bytePatternString.toString());

			break;
		}
		default:
			throw new OpCodeNotSupportedException(
					"Opcode" + insMetaData.opCode + " is currently not supported for conversion ");
		}
	}

	/*
	 * This method is used to fill the values with address of End. For ex: Data End
	 */
	private void FillUpEndValues() {
		for (int i : endValue.keySet()) {
			inputToWrite.put(Integer.toBinaryString(i), Integer.toBinaryString(tempPC - 1));
		}

	}

}
