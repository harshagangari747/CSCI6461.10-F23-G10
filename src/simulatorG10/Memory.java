package simulatorG10;

import simulatorG10.Exceptions.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.naming.AuthenticationException;
import javax.swing.DefaultRowSorter;
import javax.swing.event.MenuDragMouseEvent;

/*Class to store file contents into memory and 
servers as the base to push or get instructions
*/
public class Memory {
	public static Map<Integer, String> memory = new LinkedHashMap<Integer, String>();
	private static OutputConsole opConsoleObj = OutputConsole.GetOutputConsoleObj();

	// Loads the content addr in to the location specified by loc in the memory
	public static void LoadIntoMemory(int loc, String addr) {
		try {
			memory.put(loc, addr);
			opConsoleObj.WriteToOutputConsole("Loaded " + addr + " into the memory location " + loc, Color.CYAN);
		} catch (Exception e) {
			new PopUps().ShowPop("Can't Load the file into memory");

		}
	}

	/*
	 * Stores specified MBR value into the location MAR in the Memory Memory[MAR] =
	 * MBR
	 */
	public static void StoreIntoMemory(String MAR, String MBR) throws Exception {
		try {
			int fullMarCode = Integer.parseInt(UtilClass.ReturnUnformattedString(MAR), 2);
			String fullMbrCode = UtilClass.ReturnUnformattedString(MBR);
			Memory.memory.put(fullMarCode, fullMbrCode);
			opConsoleObj.WriteToOutputConsole("Stored " + fullMbrCode + " into location " + fullMarCode, Color.CYAN);

		} catch (Exception e) {
			throw new MemoryFaultException("Can't store the address in the the given location");
		}
	}

	/*
	 * Get specified MBR value from the location MAR in the Memory MBR <-
	 * Memory[MAR]
	 */
	public static String GetFromMemory(String MarText) throws Exception {
		int unformatterMarText = Integer.parseInt(UtilClass.ReturnUnformattedString(MarText), 2);
		String mbrValue = Memory.memory.get(unformatterMarText);
		String fullMbrText = UtilClass.ReturnWithAppendedZeroes(mbrValue, 16);
		return UtilClass.GetStringFormat(fullMbrText);

	}

}
