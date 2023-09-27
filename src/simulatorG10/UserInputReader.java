package simulatorG10;

public class UserInputReader {
	public String opCode;
	public String gprCode;
	public String ixrCode;
	public String indexCode;
	public String addrCode;

	public UserInputReader() {
	}

	public UserInputReader(String opString, String gprString, String ixrString, String indeString, String adString) {
		this.opCode = opString;
		this.gprCode = gprString;
		this.ixrCode = ixrString;
		this.indexCode = indeString;
		this.addrCode = adString;
	}

//	public String GetUserInput(Registers register) throws Exception 
//	{
//		if(this.ValidateInput())
//		{
//			switch (register) {
//			case PC: {
//				return UtilClass.GetStringFormat(UtilClass.ReturnWithAppendedZeroes(addrCode,12));
//			}
//			case GPR0:
//			case GPR1:
//			case GPR2:
//			case GPR3 :
//			{
//				return UtilClass.GetStringFormat(opCode+gprCode+ixrCode+indexCode+addrCode);
//			}
//			default:
//				throw new IllegalArgumentException("Unexpected value: " + register);
//			}
//		}
//		else {
//			throw new Exception();
//		}
//	}

	private boolean ValidateInput() throws Exception{
		if (ValidateLength(opCode,6) && ValidateLength(gprCode, 2) && ValidateLength(ixrCode, 2)
				&& ValidateLength(indexCode, 1) && ValidateLength(addrCode, 5)) {
			return true;
		} 
		else {
			throw new Exception("One or more input fields are not in expected length. \n Click Help to the input format");
		}
	}

	public boolean ValidateLength(String partialInpString, int expectedlength) throws Exception  {
		if (partialInpString.length() == expectedlength && IsBinaryString(partialInpString))
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

	public String GetValueForSpecificRegister(Registers registerText) throws Exception 
	{
		String textForRegister = "";
		switch (registerText){
		case PC: {
			if(ValidateInput())
			{
				String fullPcCode = UtilClass.ReturnWithAppendedZeroes(this.addrCode, 12);
				textForRegister =  UtilClass.GetStringFormat(fullPcCode);				
			}
		}
		break;
		case GPR0:
		case GPR1:
		case GPR2:
		case GPR3:
		case IXR1:
		case IXR2:
		case IXR3:
		{
			if(ValidateInput())
			{
				textForRegister = GetGprOrIndexValue();				
			}
		}
		break;
		default:
			throw new IllegalArgumentException("Unexpected value: " + registerText);
		}
		return textForRegister;

	}
	
	private String GetGprOrIndexValue()
	{
		return  UtilClass.GetStringFormat(opCode+gprCode+ixrCode+indexCode+addrCode);
	}
}
