package simulatorG10;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class InstructionMetaData {
	public int instructionParts;
	public String bytePattern;
	public OpCodes opCode;
	public static LinkedHashMap<Integer, LinkedHashMap<OpCodes, InstructionMetaData>> instructionMetaData;
	private static LinkedHashMap<OpCodes, InstructionMetaData> onePartsHashMap;
	private static LinkedHashMap<OpCodes, InstructionMetaData> twoPartsHashMap;
	private static LinkedHashMap<OpCodes, InstructionMetaData> threepartsHashMap;
	private static LinkedHashMap<OpCodes, InstructionMetaData> fourpartsHashMap;

	public InstructionMetaData() {
		instructionMetaData = new LinkedHashMap<Integer, LinkedHashMap<OpCodes, InstructionMetaData>>();
		onePartsHashMap = new LinkedHashMap();
		twoPartsHashMap = new LinkedHashMap();
		threepartsHashMap = new LinkedHashMap();
		fourpartsHashMap = new LinkedHashMap();
		instructionMetaData.put(1, onePartsHashMap);
		instructionMetaData.put(2, twoPartsHashMap);
		instructionMetaData.put(3, threepartsHashMap);
		instructionMetaData.put(4, fourpartsHashMap);

	}

	public static void Store(ArrayList<InstructionMetaData> loadedInstructionMetaData)
	{
		for(InstructionMetaData instMetaData : loadedInstructionMetaData){
			if(instMetaData.instructionParts == 1)
				onePartsHashMap.put(instMetaData.opCode, instMetaData);
			if(instMetaData.instructionParts == 2)
				twoPartsHashMap.put(instMetaData.opCode, instMetaData);
			if(instMetaData.instructionParts == 3)
				threepartsHashMap.put(instMetaData.opCode, instMetaData);
			if(instMetaData.instructionParts == 4)
				fourpartsHashMap.put(instMetaData.opCode, instMetaData);
			 
		}
	}
}