package simulatorG10;

import java.awt.Color;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;

/*Class to set,get, run instructions
 * Typically our CPU
 * */
public class Simulator {
	private static ArrayList<Integer> address;
	private static Iterator<Integer> iterator;
	private static int index;
	private static OutputConsole opConsoleObj;
	public static boolean haltTriggered = false;
	private BinaryOperations binaryOperationsObj;

	/*
	 * Class constructor to set the address with keys of memory to iterate through
	 * so that we'll keep track of the next instruction
	 */
	public Simulator() {
		if (address == null)
			address = new ArrayList<Integer>(Memory.memory.keySet());

		iterator = address.iterator();
		opConsoleObj = OutputConsole.GetOutputConsoleObj();
		binaryOperationsObj = BinaryOperations.GetBinaryOperationsObj();
	}

	/*
	 * Triggered when user clicked Step Button in the front panel This method runs
	 * the instruction by decoding it and therefore sets,stores,gets information to
	 * and from different registers
	 */
	public void StepIntoTheInstruction() throws Exception {
		String currentPc = FrontPanel.pcValueLbl.getText();
		try {

			int condensedCurrentPc = Integer.parseInt(UtilClass.ReturnUnformattedString(currentPc), 2);
			index = -1;
			while (iterator.hasNext()) {
				if (address.contains(condensedCurrentPc)) {
					index = address.indexOf(condensedCurrentPc);
				}
				break;
			}
			if (address.contains(condensedCurrentPc)) {
				String mbrValueAtMar = Memory.memory.get(condensedCurrentPc);

				// FrontPanel.LoadRegister(Registers.PC,iterator.next().toString());

				// push addr to mar
				FrontPanel.SetRegister(Registers.MAR, currentPc);
				// push value at loc (mar) to mbr
				String formattedMbrValue = UtilClass.ReturnWithAppendedZeroes(mbrValueAtMar, 16);
				FrontPanel.SetRegister(Registers.MBR, UtilClass.GetStringFormat(formattedMbrValue));
				// push mbr to ir
				FrontPanel.SetRegister(Registers.IR, UtilClass.GetStringFormat(formattedMbrValue));
				// decode ir
				DeCodeInstruction(formattedMbrValue);
				// set those bits
				// int indexOfCurrentPc = address.indexOf(condensedCurrentPc);

				// increment PC
				if (index < address.size()) {
					IncrementPC();

				}
			}
		} catch (Exception e) {
			throw new Exception(e.getLocalizedMessage());
		}

	}

	/*
	 * Decode the instruction word which contains 16 bits. Decoding is done using
	 * regular expressions. And based on the opcode value extracted, we perform the
	 * appropriate action
	 */
	private void DeCodeInstruction(String ir) throws Exception {
		Matcher instructionGroups = UtilClass.GetGroups(ir);
		InstructionWord word = new InstructionWord(instructionGroups.group(1), instructionGroups.group(2),
				instructionGroups.group(3), instructionGroups.group(4), instructionGroups.group(5));

		switch (word.opCode) {
		case LOC:
		{
			break;
		}
		case HALT: {
			if (!haltTriggered) {
				haltTriggered = true;
				throw new Exception("HALT Triggered. Program Halted. End of the Program");
			}
		}
		case LDR: {
			LoadRegisterFromMemory(word);
			break;
		}
		case STR: {
			StoreRegisterToMemory(word);
			break;
		}
		case LDA: {
			LoadRegisterWithAddress(word);
			break;
		}
		case LDX: {
			LoadIndexRegisterFromMemory(word);
			break;
		}
		case STX: {
			StoreIndexRegisterToMemory(word);
			break;
		}
		case JZ: {
			Jump(word, true, false, false);
			break;
		}
		case JNE: {
			Jump(word, false, false, false);
			break;
		}
		case JCC: {
			Jump(word, false, true, false);
			break;
		}
		case JMA: {
			String eftAddr = UtilClass.ReturnWithAppendedZeroes(calculateEffectiveAddress(word), 16);
			FrontPanel.pcValueLbl.setText(UtilClass.GetStringFormat(eftAddr));
			break;
		}
		case JGE: {
			Jump(word, false, false, true);
			break;
		}
		case JSR: {
			break;
		}
		case RFS: {
			break;
		}
		case SOB: {
			SubtractOneAndBranch(word);
			break;
		}
		case AMR: {
			break;
		}
		case SMR: {
			break;
		}
		case AIR: {
			break;
		}
		case SIR: {
			break;
		}
		case MLT: {
			break;
		}
		case DVD: {
			break;
		}
		case TRR: {
			break;
		}
		case OR: {
			break;
		}
		case AND: {
			break;
		}
		case SRC: {
			break;
		}
		case NOT: {
			break;
		}
		case RRC: {
			break;
		}
		case IN: {
			break;
		}
		case OUT: {
			break;
		}
		case CHK: {
			break;
		}
		default:
			throw new Exception("Can't perfrom operation. Opcode not found or supported");
		}

	}

	/*
	 * Performs operation LDR Loads the specified GPR at 6,7 bits with the contents
	 * at specified address in the memory Ex : LDR 1,0,addr(18) opcode value :
	 * 000001
	 */
	private void LoadRegisterFromMemory(InstructionWord word) throws Exception {
		String eftAddr = UtilClass.ReturnWithAppendedZeroes(calculateEffectiveAddress(word), 16);
		String gprContent = UtilClass.GetStringFormat(eftAddr);
		SetGprOrIndex(gprContent, word.gpRegister);

	}

	/*
	 * Performs operation STR Stores the specified GPR value at 6,7 bits with the
	 * contents at specified address in the memory Ex : STR 0,0,addr(20) opcode
	 * value : 000010
	 */
	private void StoreRegisterToMemory(InstructionWord word) throws Exception {
		int eftAddr = Integer.parseInt(calculateEffectiveAddress(word), 2);
		String gprValue = UtilClass.ReturnUnformattedString(GetGprOrIndxContent(word.gpRegister));
		Memory.memory.put(eftAddr, gprValue);
		opConsoleObj.WriteToOutputConsole("Stored " + gprValue + " into address " + eftAddr,Color.CYAN);

	}

	/*
	 * Performs operation LDA Loads the specified GPR at 6,7 bits with the address
	 * Ex : LDA 1,0,addr(18) opcode value : 000011
	 */
	private void LoadRegisterWithAddress(InstructionWord word) throws Exception {
		try {
			String eftAddr = UtilClass.ReturnWithAppendedZeroes(calculateEffectiveAddress(word), 16);
			SetGprOrIndex(UtilClass.GetStringFormat(eftAddr), word.gpRegister);
		} catch (Exception e) {
			throw new Exception(e.getLocalizedMessage());

		}
	}

	/*
	 * Performs operation LDA Loads the specified Index register at 8,9 bits with
	 * the specified address in the memory Ex : LDX 1,0,addr(18) opcode value :
	 * 101001
	 */
	private void LoadIndexRegisterFromMemory(InstructionWord word) throws Exception {
		String eftAddr = UtilClass.GetStringFormat(calculateEffectiveAddress(word));
		SetGprOrIndex(eftAddr, word.ixRegister);
	}

	/*
	 * Performs operation STX Stores the specified Index register value at 8,9 bits
	 * into the specified address in the memory Ex : STX 1,0,addr(18) opcode value :
	 * 101010
	 */
	private void StoreIndexRegisterToMemory(InstructionWord word) throws Exception {
		int eftAddr = Integer.parseInt(calculateEffectiveAddress(word), 2);
		String ixrValue = UtilClass.ReturnUnformattedString(GetGprOrIndxContent(word.ixRegister));
		Memory.memory.put(eftAddr, ixrValue);
		opConsoleObj.WriteToOutputConsole("Stored " + ixrValue + " into address " + eftAddr,Color.CYAN);

	}

	/*
	 * Method to calculate the Effective address
	 */
	private String calculateEffectiveAddress(InstructionWord word) {
		StringBuffer effectiveAddress = new StringBuffer();
		BinaryOperations bOperations = BinaryOperations.GetBinaryOperationsObj();
		int addr = -1;
		addr = Integer.parseInt(String.valueOf(word.address), 2);
		if (!word.indirectAddressing) {
			if (word.ixRegister == Registers.IXR0) {
				effectiveAddress.append(Memory.memory.get(addr));
			} else {
				String currentIxrValue = null;
				if (word.ixRegister == Registers.IXR1)
					currentIxrValue = FrontPanel.ixr1ValueLbl.getText();
				else if (word.ixRegister == Registers.IXR2)
					currentIxrValue = FrontPanel.ixr2ValueLbl.getText();
				else if (word.ixRegister == Registers.IXR3)
					currentIxrValue = FrontPanel.ixr3ValueLbl.getText();
				currentIxrValue = UtilClass.ReturnUnformattedString(currentIxrValue);
				String addressString = bOperations.BinaryAddition(currentIxrValue, Memory.memory.get(addr));
				effectiveAddress.append(UtilClass.ReturnWithAppendedZeroes(addressString, 16));
			}
		} else {
			if (word.ixRegister == Registers.IXR0) {
				int nextAddress = Integer.parseInt(Memory.memory.get(addr), 2);
				effectiveAddress.append(Memory.memory.get(nextAddress));
			} else {
				String currentIxrValue = null;
				if (word.ixRegister == Registers.IXR1)
					currentIxrValue = FrontPanel.ixr1ValueLbl.getText();
				else if (word.ixRegister == Registers.IXR2)
					currentIxrValue = FrontPanel.ixr2ValueLbl.getText();
				else if (word.ixRegister == Registers.IXR3)
					currentIxrValue = FrontPanel.ixr3ValueLbl.getText();
				currentIxrValue = UtilClass.ReturnUnformattedString(currentIxrValue);
				String addressString = bOperations.BinaryAddition(currentIxrValue, Memory.memory.get(addr));
				String finalEftAddress = Memory.memory.get(Integer.parseInt(addressString, 2));
				effectiveAddress.append(UtilClass.ReturnWithAppendedZeroes(finalEftAddress, 16));

			}

		}
		return effectiveAddress.toString();
	}

	/*
	 * Set the GPR or Index registers with the value as directed by the instruction
	 * word
	 */
	private void SetGprOrIndex(String addr, Registers GprInd) throws Exception {
		switch (GprInd) {
		case GPR0: {
			FrontPanel.gpr0ValueLbl.setText(addr);
			break;
		}
		case GPR1: {
			FrontPanel.gpr1ValueLbl.setText(addr);
			break;
		}
		case GPR2: {
			FrontPanel.gpr2ValueLbl.setText(addr);
			break;
		}
		case GPR3: {
			FrontPanel.gpr3ValueLbl.setText(addr);
			break;
		}
		case IXR1: {
			FrontPanel.ixr1ValueLbl.setText(addr);
			break;
		}
		case IXR2: {
			FrontPanel.ixr2ValueLbl.setText(addr);
			break;
		}
		case IXR3: {
			FrontPanel.ixr3ValueLbl.setText(addr);
			break;
		}
		default: {
			throw new Exception("Can't set IXR0 Register!");
		}
		}
		opConsoleObj.WriteToOutputConsole("Set " + String.valueOf(GprInd) + " to " + addr,Color.CYAN);
	}

	/*
	 * Gets the value of GPR or index register as directed by the Instruction word
	 */
	private String GetGprOrIndxContent(Registers register) throws Exception {
		StringBuffer gprContent = new StringBuffer();
		switch (register) {
		case GPR0: {
			gprContent.append(FrontPanel.gpr0ValueLbl.getText());
			break;
		}
		case GPR1: {
			gprContent.append(FrontPanel.gpr1ValueLbl.getText());
			break;
		}
		case GPR2: {
			gprContent.append(FrontPanel.gpr2ValueLbl.getText());
			break;
		}
		case GPR3: {
			gprContent.append(FrontPanel.gpr3ValueLbl.getText());
			break;
		}
		case IXR1: {
			gprContent.append(FrontPanel.ixr1ValueLbl.getText());
			break;
		}
		case IXR2: {
			gprContent.append(FrontPanel.ixr2ValueLbl.getText());
			break;
		}
		case IXR3: {
			gprContent.append(FrontPanel.ixr3ValueLbl.getText());
			break;
		}
		default: {
			throw new Exception("Can't get IXR0 value");
		}
		}
		return gprContent.toString();
	}

	private void IncrementPC() {
		String nextPcaddr = Integer.toBinaryString(address.get(++index));
		String appendedPcValue = UtilClass.ReturnWithAppendedZeroes(nextPcaddr, 12);
		FrontPanel.SetRegister(Registers.PC, UtilClass.GetStringFormat(appendedPcValue));
	}

	private void Jump(InstructionWord word, boolean isZero, boolean isCCbit, boolean isGreater) throws Exception {
		int eftAddr = Integer.parseInt(calculateEffectiveAddress(word), 2);
		String gprValue = UtilClass.ReturnUnformattedString(GetGprOrIndxContent(word.gpRegister));
		int gprIntValue = Integer.parseInt(gprValue);
		if ((isZero && gprIntValue == 0) || (!isZero && gprIntValue != 0) || isCCbit
				|| (isGreater && gprIntValue > 0)) {
			String pcString = String.valueOf(eftAddr);
			FrontPanel.SetRegister(Registers.PC, UtilClass.GetStringFormat(pcString));

		} else {
			IncrementPC();
		}

	}

	private void SubtractOneAndBranch(InstructionWord word) throws Exception {
		String gprValue = UtilClass.ReturnUnformattedString(GetGprOrIndxContent(word.gpRegister));
		String result = UtilClass.ReturnWithAppendedZeroes(binaryOperationsObj.BinarySubstraction(gprValue, "1"), 16);
		FrontPanel.SetRegister(word.gpRegister, UtilClass.GetStringFormat(result));
		if (binaryOperationsObj.ReturnDecimalFromBinary(result) > 0) {
			String pcString = calculateEffectiveAddress(word);
			FrontPanel.SetRegister(Registers.PC, UtilClass.GetStringFormat(pcString));
		} else {
			IncrementPC();
		}

	}

}
