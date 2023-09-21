package simulatorG10;

import java.lang.*;
import java.util.*;
public class HexToBitConverter {
	Map<String,String> word ;
	private static HexToBitConverter singletonHToBObj;
	private HexToBitConverter() {}
	
	public static HexToBitConverter GetHexToBitConverterObj()
	{
		if(singletonHToBObj == null)
			return singletonHToBObj = new HexToBitConverter();
		return singletonHToBObj;	
	}
	
	public String ReturnBitsFromHex(String hexCode)
	{
		int integerValue = Integer.parseInt(hexCode, 16);
		  String binaryString = Integer.toBinaryString(integerValue);
		  return binaryString;
	}
	
	public int ReturnDecimalFromBinary(String binaryString)
	{
		int decimalEquivalent = Integer.parseInt(binaryString,10);
		return decimalEquivalent;
	}
}

