package simulatorG10;

import javax.swing.text.StyledEditorKit.ForegroundAction;

//Class for instructionword given by user
public class InstructionWord {
	OpCodes opCode;
	Registers gpRegister;
	Registers ixRegister;
	boolean indirectAddressing;
	int address;

	/*
	 * Class constructor to set the different fields of the instruction word for the
	 * project 1 address values up to 5bits = 2^5 = 0-31 are supported
	 */
	public InstructionWord(String opcode, String gpr, String index, String ia, String _address) throws Exception {
		this.opCode = GetOpcode(opcode);
		this.gpRegister = GetRegistersToSet(gpr, true);
		this.ixRegister = GetRegistersToSet(index, false);
		this.indirectAddressing = ia.equals("1") ? true : false;
		this.address = Integer.parseInt(_address, 10);

	}

	/*
	 * Based on the first six bits, returns the appropriate opcode enum For project
	 * 1 only LDR,STR,LDA,LDX,STX are supported
	 */
	private OpCodes GetOpcode(String opcode) throws Exception {
		switch (opcode) {
		case "000000": {
			return opCode.HALT;
		}
		case "": {
			return OpCodes.LOC;
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
		case "100001": {
			return OpCodes.LDX;
		}
		case "100010": {
			return OpCodes.STX;
		}
		case "001000": {
			return OpCodes.JZ;
		}
		case "001001": {
			return opCode.JNE;
		}
		case "001010": {
			return opCode.JCC;
		}
		case "001011": {
			return opCode.JMA;
		}
		case "001100": {
			return opCode.JSR;
		}
		case "001101": {
			return opCode.RFS;
		}
		case "001110": {
			return opCode.SOB;
		}
		case "001111": {
			return opCode.JGE;
		}
		case "000100": {
			return opCode.AMR;
		}
		case "000101": {
			return opCode.SMR;
		}
		case "000110": {
			return opCode.AIR;
		}
		case "000111": {
			return opCode.SIR;
		}
		case "010000": {
			return opCode.MLT;
		}
		case "010001": {
			return opCode.DVD;
		}
		case "010010": {
			return opCode.TRR;
		}
		case "010100": {
			return opCode.ORR;
		}
		case "010011": {
			return opCode.AND;
		}
		case "010101": {
			return opCode.NOT;
		}
		case "011001": {
			return opCode.SRC;
		}
		case "011010": {
			return opCode.RRC;
		}
		case "110001": {
			return opCode.IN;
		}
		case "110010": {
			return opCode.OUT;
		}

		default:
			throw new Exception("Opcode : " + opcode + " is currently not supported!");
		}
	}

	/*
	 * Based on the [6,7] or [8,9] bits, returns the appropriate register enum value
	 * isGPR is to identify if the caller is seeking to get enum for GPR registers
	 * or Index registers
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
				return Registers.GPR3;
			return Registers.IXR3;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + register);
		}
	}

}
