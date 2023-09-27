package simulatorG10;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.naming.AuthenticationException;
import javax.swing.DefaultRowSorter;
import javax.swing.event.MenuDragMouseEvent;

public class Memory {
	public static Map<String, String> memory = new HashMap<String, String>();

	public static void LoadIntoMemory(String loc, String addr) {
		try {
			memory.put(loc, addr);
			System.out.println("Loaded " + addr + " into the memory location " + loc);
		} catch (Exception e) {
			new PopUps().ShowPop("Can't Load the file into memory");

		}
	}

	public static void StoreIntoMemory(String MAR, String MBR) throws Exception {
		try {
			int fullMarCode = Integer.parseInt(UtilClass.ReturnUnformattedString(MAR));
			int fullMbrCode = Integer.parseInt(UtilClass.ReturnUnformattedString(MBR));
			Memory.memory.put(String.valueOf(fullMarCode), String.valueOf(fullMbrCode));
			System.out.println("Stored " + fullMbrCode + " into location " + fullMarCode);

		} catch (Exception e) {
			throw new Exception("Can't store the address in the the given location");
		}
	}

	public static String GetFromMemory(String MarText) {
		String unformatterMarText = Integer.parseInt(UtilClass.ReturnUnformattedString(MarText), 10) + "";
		String mbrValue = Memory.memory.get(unformatterMarText);
		String fullMbrText = UtilClass.ReturnWithAppendedZeroes(mbrValue, 16);
		return UtilClass.GetStringFormat(fullMbrText);

	}

}
