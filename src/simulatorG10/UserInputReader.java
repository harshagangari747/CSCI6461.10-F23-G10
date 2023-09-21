package simulatorG10;

public class UserInputReader {
	public String opCode;
	public String gprCode;
	public String ixrCode;
	public String indexCode;
	public String addrCode;
	public UserInputReader() {}
	
	public UserInputReader(String opString,String gprString,String ixrString,String indeString,String adString)
	{
		this.opCode = opString;
		this.gprCode = gprString;
		this.ixrCode = ixrString;
		this.indexCode = indeString;
		this.addrCode = adString;
	}
	
	public String GetUserInput() throws Exception 
	{
		try {
			return this.ValidateInput();
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	private String ValidateInput() throws Exception
	{
		if(ValidateLength(opCode, 6) && ValidateLength(gprCode,2) && ValidateLength(ixrCode, 2) && ValidateLength(indexCode, 1) && ValidateLength(addrCode, 5))
		{
			return UtilClass.GetStringFormat(opCode+gprCode+ixrCode+indexCode+addrCode);
		}
		else {
			throw new Exception("Invalid length in input for one or more fields");
		}
	}
	public boolean ValidateLength(String partialInpString, int expectedlength) throws Exception
	{
		if(partialInpString.length() == expectedlength && IsBinaryString(partialInpString))
			return true;
		return false;
	}
	private static boolean IsBinaryString(String string) throws Exception {
		  for (char c : string.toCharArray()) {
		    if (c != '0' && c != '1') {
		    	throw new Exception("Contains illegal characters. Make sure the instruction word is in binary :)");
		    }
		  }
		  return true;
		}

}

