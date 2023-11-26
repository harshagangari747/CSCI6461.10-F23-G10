package simulatorG10;
import simulatorG10.Exceptions.*;
//Class to interpret the instruction word that user entered manually
public class UserInputReader {
	public String opCode;
	public String gprCode;
	public String ixrCode;
	public String indexCode;
	public String addrCode;

	// Class's Emtpy Constructor
	public UserInputReader() {
	}

	// Class Constructor
	public UserInputReader(String opString, String gprString, String ixrString, String indeString, String adString) {
		this.opCode = opString;
		this.gprCode = gprString;
		this.ixrCode = ixrString;
		this.indexCode = indeString;
		this.addrCode = adString;
	}

	/*
	 * Validates the input, Checks if the each instruction word is of 16 bits.
	 */
	private boolean ValidateInput() throws Exception {
		if (ValidateLength(opCode, 6) && ValidateLength(gprCode, 2) && ValidateLength(ixrCode, 2)
				&& ValidateLength(indexCode, 1) && ValidateLength(addrCode, 5)) {
			return true;
		} else {
			throw new InstructionFormatException(
					"One or more input fields are not in expected length. \n Click Help to the input format");
		}
	}

	/*
	 * Validates the input, Checks if the each part of instruction word is of
	 * specified length Opcode = 6 bits GPR = 2 bits Index = 2 bits Indirect
	 * Addressing = 1 bit Address = 5 bits
	 */
	private boolean ValidateLength(String partialInpString, int expectedlength) throws Exception {
		if (partialInpString.length() == expectedlength && IsBinaryString(partialInpString))
			return true;
		return false;
	}

	/*
	 * Checks the input is given as only 1's and 0's and no other character
	 */
	private static boolean IsBinaryString(String string) throws Exception {
		for (char c : string.toCharArray()) {
			if (c != '0' && c != '1') {
				throw new InstructionFormatException(
						"Contains illegal characters. Make sure the instruction word is in binary :)");
			}
		}
		return true;
	}

	/*
	 * When user clicked LD button next to registers, the value is computed and
	 * stored to the specified register
	 */
	public String GetValueForSpecificRegister(Registers registerText) throws Exception {
		String textForRegister = "";
		if (ValidateInput()) {
			switch (registerText) {
			case PC: {
				String fullPcCode = UtilClass.ReturnWithAppendedZeroes(this.addrCode, 12);
				textForRegister = UtilClass.GetStringFormat(fullPcCode);
			}
				break;
			case GPR0:
			case GPR1:
			case GPR2:
			case GPR3:
			case IXR1:
			case IXR2:
			case IXR3: {
				textForRegister = GetCompleteUserInput();
				break;
			}
			case MAR: {
				String fullMarCode = UtilClass.ReturnWithAppendedZeroes(this.addrCode, 12);
				textForRegister = UtilClass.GetStringFormat(fullMarCode);
				break;
			}
			case MBR: {
				textForRegister = GetCompleteUserInput();
				break;
			}
			case CC: {
				textForRegister = GetCCInput();
				break;
			}

			default:
				throw new IllegalArgumentException("Unexpected value: " + registerText);
			}
		}
		return textForRegister;

	}

	/*
	 * Assembles 16 bit instruction word from the user input from the different text
	 * fields
	 */
	private String GetCompleteUserInput() throws InstructionFormatException {
		return UtilClass.GetStringFormat(opCode + gprCode + ixrCode + indexCode + addrCode);
	}

	/*
	 * Calculates what value to set for CC register. Typically gpr register + index
	 * register
	 */
	private String GetCCInput() throws InstructionFormatException {
		return UtilClass.GetStringFormat(gprCode + ixrCode);
	}
}