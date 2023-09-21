package simulatorG10;

import java.util.HashMap;
import java.util.Map;

import javax.naming.AuthenticationException;

public class Memory {
	public static  Map<Integer,InstructionWord> memory = new HashMap<Integer, InstructionWord>();
	
	public static  void LoadIntoMemory(InstructionWord word)
	{
		int memoryLocationKey = HexToBitConverter.GetHexToBitConverterObj().ReturnDecimalFromBinary(word.instLocation);
		try {
			memory.put(memoryLocationKey, word);
			System.out.println("Loaded "+word.instAddress +" into the memory location "+ word.instLocation);
		} catch (Exception e) {
			new PopUps().ShowPop("Can't Load the file into memory");
			
		}
	}

}
