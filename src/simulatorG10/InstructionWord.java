package simulatorG10;

import javax.swing.text.StyledEditorKit.ForegroundAction;

//Class for instructionword given by user
public class InstructionWord {
	OpCodes opCode;
	Registers gpRegister;
	Registers ixRegister;
	boolean indirectAddressing;
	int address;

	/* Class constructor to set the different fields of the instruction word
	 * for the project 1 address values up to 5bits = 2^5 = 0-31 are supported
	 */
	public InstructionWord(String opcode, String gpr, String index, String ia, String _address) throws Exception {
		this.opCode = GetOpcode(opcode);
		this.gpRegister = GetRegistersToSet(gpr, true);
		this.ixRegister = GetRegistersToSet(index, false);
		this.indirectAddressing = ia.equals("1") ? true : false;
		this.address = Integer.parseInt(_address, 10);

	}

	/*
	 * Based on the first six bits, returns the appropriate opcode enum
	 * For project 1 only LDR,STR,LDA,LDX,STX are supported
	 */
	private OpCodes GetOpcode(String opcode) throws Exception {
		switch (opcode) {
		case "000000": {
			return OpCodes.HALT;
		}
		case "000001": {
			return opCode.LDR;
		}
		case "000010": {
			return opCode.STR;
		}
		case "000011": {
			return OpCodes.LDA;
		}
		case "101001": {
			return OpCodes.LDX;
		}
		case "101010": {
			return OpCodes.STX;
		}

		default:
			throw new Exception("Opcode : " + opcode + " is currently not supported!");
		}
	}

	/*
	 * Based on the [6,7] or [8,9] bits, returns the appropriate register enum value
	 * isGPR is to identify if the caller is seeking to get enum for GPR registers or
	 * Index registers
	 */
	private Registers GetRegistersToSet(String register, boolean isGPR) {
		switch (register) {
		case "00": {
			if (isGPR)
				return Registers.GPR0;
			return Registers.IXR0;
		}
		case "01": {
			if (isGPR)
				return Registers.GPR1;
			return Registers.IXR1;
		}
		case "10": {
			if (isGPR)
				return Registers.GPR2;
			return Registers.IXR2;
		}
		case "11": {
			if (isGPR)
				return Registers.GPR2;
			return Registers.IXR2;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + register);
		}
	}

}
