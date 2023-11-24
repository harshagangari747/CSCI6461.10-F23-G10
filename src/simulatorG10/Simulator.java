package simulatorG10;

import java.awt.Color;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;

import javax.swing.text.WrappedPlainView;
import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

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
	private boolean isJumpInst = false;
	private int condensedCurrentPc;

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

			condensedCurrentPc = Integer.parseInt(UtilClass.ReturnUnformattedString(currentPc), 2);
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
				if (index < address.size() && !isJumpInst) {
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
		case LOC: {
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
			JumpAndSaveReturn(word);
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
			AddOrSubtractMemoryToRegister(word, true);
			break;
		}
		case SMR: {
			AddOrSubtractMemoryToRegister(word, false);
			break;
		}
		case AIR: {
			AddOrSubtractImmediate(word, true);
			break;
		}
		case SIR: {
			AddOrSubtractImmediate(word, false);
			break;
		}
		case MLT: {
			MultiplyOrDivide(word, true);
			break;
		}
		case DVD: {
			MultiplyOrDivide(word, false);
			break;
		}
		case TRR: {
			RelationalOperatoinsOnBit(word);
			break;
		}
		case ORR: {
			RelationalOperatoinsOnBit(word);
			break;
		}
		case AND: {
			RelationalOperatoinsOnBit(word);
			break;
		}
		case NOT: {
			RelationalOperatoinsOnBit(word);
			break;
		}
		case SRC: {
			ShiftBits(word);
			break;
		}
		case RRC: {
			RotateBits(word);
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
		opConsoleObj.WriteToOutputConsole("Stored " + gprValue + " into address " + eftAddr, Color.CYAN);

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
		opConsoleObj.WriteToOutputConsole("Stored " + ixrValue + " into address " + eftAddr, Color.CYAN);

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
		opConsoleObj.WriteToOutputConsole("Set " + String.valueOf(GprInd) + " to " + addr, Color.CYAN);
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

	/* This method is used to perform operations when we increment the PC */
	private void IncrementPC() throws Exception {
		String nextPcaddr = Integer.toBinaryString(address.get(++index));
		String appendedPcValue = UtilClass.ReturnWithAppendedZeroes(nextPcaddr, 12);
		FrontPanel.SetRegister(Registers.PC, UtilClass.GetStringFormat(appendedPcValue));
	}

	/* This method is used to perform various Jump operations */
	private void Jump(InstructionWord word, boolean isZero, boolean isCCbit, boolean isGreater) throws Exception {
		int eftAddr = Integer.parseInt(calculateEffectiveAddress(word), 2);
		String gprValue = UtilClass.ReturnUnformattedString(GetGprOrIndxContent(word.gpRegister));
		int gprIntValue = Integer.parseInt(gprValue, 2);
		if (isCCbit) {
			String ccBit = FrontPanel.ccValueLbl.getText();
			int registerId = Integer.parseInt(word.gpRegister.GetValue(), 2);
			if (ccBit.charAt(registerId) == '1') {
				String pcString = UtilClass.ReturnWithAppendedZeroes(Integer.toBinaryString(eftAddr), 12);
				FrontPanel.SetRegister(Registers.PC, UtilClass.GetStringFormat(pcString));
			} else {
				IncrementPC();
			}
		}
		else if ((isZero && gprIntValue == 0) || (!isZero && gprIntValue != 0) || (isGreater && gprIntValue >= 0)) {
			String pcString = UtilClass.ReturnWithAppendedZeroes(Integer.toBinaryString(eftAddr), 12);
			FrontPanel.SetRegister(Registers.PC, UtilClass.GetStringFormat(pcString));
		} else {
			IncrementPC();
		}
		isJump(true);

	}

	/* This method is used to perform SOB operation */
	private void SubtractOneAndBranch(InstructionWord word) throws Exception {
		String gprValue = UtilClass.ReturnUnformattedString(GetGprOrIndxContent(word.gpRegister));
		String result = UtilClass.ReturnWithAppendedZeroes(binaryOperationsObj.BinarySubstraction(gprValue, "1"), 16);
		FrontPanel.SetRegister(word.gpRegister, UtilClass.GetStringFormat(result));
		if (binaryOperationsObj.ReturnDecimalFromBinary(result) > 0) {
			int eftAddr = Integer.parseInt(calculateEffectiveAddress(word), 2);
			String pcString = UtilClass.ReturnWithAppendedZeroes(Integer.toBinaryString(eftAddr), 12);
			FrontPanel.SetRegister(Registers.PC, UtilClass.GetStringFormat(pcString));
			isJump(true);
		} else {
			IncrementPC();
		}

	}

	private void AddOrSubtractMemoryToRegister(InstructionWord word, boolean isAddition) throws Exception {
		String eftAddr = calculateEffectiveAddress(word);
		String result = null;
		String gprValue = UtilClass.ReturnUnformattedString(GetGprOrIndxContent(word.gpRegister));
		if (isAddition) {
			result = binaryOperationsObj.BinaryAddition(gprValue, eftAddr);
		} else
			result = binaryOperationsObj.BinarySubstraction(gprValue, eftAddr);
		String paddedResult = UtilClass.ReturnWithAppendedZeroes(result, 16);
		String formattedResult = UtilClass.GetStringFormat(paddedResult.substring(paddedResult.length() - 16));
		FrontPanel.SetRegister(word.gpRegister, formattedResult);
		isJump(false);
	}

	private void AddOrSubtractImmediate(InstructionWord word, boolean isAddition) throws Exception {
		String result = null;
		String gprValue = UtilClass.ReturnUnformattedString(GetGprOrIndxContent(word.gpRegister));
		String immValue = String.valueOf(word.address);
		if (isAddition)
			result = binaryOperationsObj.BinaryAddition(gprValue, immValue);
		else
			result = binaryOperationsObj.BinarySubstraction(gprValue, immValue);
		String paddedResult = UtilClass.ReturnWithAppendedZeroes(result, 16);
		String formattedResult = UtilClass.GetStringFormat(paddedResult.substring(paddedResult.length() - 16));
		FrontPanel.SetRegister(word.gpRegister, formattedResult);
		isJump(false);
	}

	private void MultiplyOrDivide(InstructionWord word, boolean isMulOp) throws Exception {
		if (word.gpRegister == Registers.GPR1 || word.ixRegister == Registers.IXR3 || word.gpRegister == Registers.GPR3
				|| word.ixRegister == Registers.IXR1) {
			throw new Exception("Opearands must be stored in GPR0 or GPR2.");
		}
		String rx = UtilClass.ReturnUnformattedString(GetGprOrIndxContent(word.gpRegister));
		Registers ryReg = word.gpRegister == Registers.GPR0 ? Registers.GPR2 : Registers.GPR0;
		String ry = UtilClass.ReturnUnformattedString(GetGprOrIndxContent(ryReg));
		Registers resRx, resRy;
		String result = null, res0 = null, res1 = null;
		if (isMulOp) {
			result = binaryOperationsObj.BinaryMultiplication(rx, ry);
			if (result.length() > 32) {
				// set cc bit;
			}
			String paddedResult = UtilClass.ReturnWithAppendedZeroes(result, 32);
			res0 = paddedResult.substring(0, 16);
			res1 = paddedResult.substring(16, 32);
		} else {
			result = binaryOperationsObj.BinaryDivision(rx, ry);
			String[] divResult = result.split("&");
			res0 = UtilClass.ReturnWithAppendedZeroes(divResult[0], 16);
			res1 = UtilClass.ReturnWithAppendedZeroes(divResult[1], 16);

		}
		if (word.gpRegister == Registers.GPR0) {
			resRx = Registers.GPR0;
			resRy = Registers.GPR1;
		} else {
			resRx = Registers.GPR2;
			resRy = Registers.GPR3;
		}
		FrontPanel.SetRegister(resRx, UtilClass.GetStringFormat(res0));
		FrontPanel.SetRegister(resRy, UtilClass.GetStringFormat(res1));
		isJump(false);

	}

	private void RelationalOperatoinsOnBit(InstructionWord word) throws Exception {
		String rx = UtilClass.ReturnUnformattedString(GetGprOrIndxContent(word.gpRegister));
		Registers ryReg;
		switch (word.ixRegister) {
		case IXR0: {
			ryReg = Registers.GPR0;
			break;
		}
		case IXR1: {
			ryReg = Registers.GPR1;
			break;
		}
		case IXR2: {
			ryReg = Registers.GPR2;
			break;
		}
		case IXR3: {
			ryReg = Registers.GPR3;
			break;
		}

		default:
			throw new IllegalArgumentException("Register ID " + word.gpRegister + " or " + word.ixRegister
					+ " not found. Only 0,1,2,3 are supported ");
		}

		String ry = UtilClass.ReturnUnformattedString(GetGprOrIndxContent(ryReg));
		boolean areEqual = false;
		String result = "";
		if (word.opCode == OpCodes.TRR) {

			areEqual = binaryOperationsObj.AreRegistersEqual(rx, ry);
			if (areEqual) {
				FrontPanel.SetCCRegister(4, true);
			} else {
				FrontPanel.SetCCRegister(4, false);
			}
			return;
		} else if (word.opCode == OpCodes.ORR) {
			result = binaryOperationsObj.LogicalOr(rx, ry);
		} else if (word.opCode == OpCodes.AND) {
			result = binaryOperationsObj.LogicalAnd(rx, ry);
		} else if (word.opCode == OpCodes.NOT) {
			result = binaryOperationsObj.binaryNot(rx);
		}
		FrontPanel.SetRegister(word.gpRegister, UtilClass.GetStringFormat(result));
		isJump(false);
	}

	private void ShiftBits(InstructionWord word) throws Exception {
		String gprValue = UtilClass.ReturnUnformattedString(GetGprOrIndxContent(word.gpRegister));
		String indexInfo = UtilClass.ReturnRegisterEncoding(word.ixRegister);
		boolean isLogicalShift = (indexInfo.charAt(0) == '1') ? true : false;
		boolean isLeftShift = (indexInfo.charAt(1) == '1') ? true : false;
		int count = Integer.parseInt(String.valueOf(word.address), 2);
		String result;
		if (isLogicalShift) {
			result = binaryOperationsObj.DoLogicalShift(gprValue, count, isLeftShift);
		} else {
			result = binaryOperationsObj.DoArithmeticShift(gprValue, count, isLeftShift);
		}
		FrontPanel.SetRegister(word.gpRegister, UtilClass.GetStringFormat(result));
		isJump(false);
	}

	private void isJump(boolean isJumpInstruction) {
		isJumpInst = isJumpInstruction;
	}

	private void RotateBits(InstructionWord word) throws Exception {
		String gprValue = UtilClass.ReturnUnformattedString(GetGprOrIndxContent(word.gpRegister));
		String indexInfo = UtilClass.ReturnRegisterEncoding(word.ixRegister);
		boolean isLogicalShift = (indexInfo.charAt(0) == '1') ? true : false;
		boolean isLeftShift = (indexInfo.charAt(1) == '1') ? true : false;
		int count = Integer.parseInt(String.valueOf(word.address), 2);
		String result;
		if (isLogicalShift) {
			result = binaryOperationsObj.DoLogicalRotate(gprValue, count, isLeftShift);
		} else {
			throw new Exception("Arithmetic Rotation of bits is not supported!");
		}
		FrontPanel.SetRegister(word.gpRegister, UtilClass.GetStringFormat(result));
		isJump(false);

	}

	private void JumpAndSaveReturn(InstructionWord word) throws Exception {
		int eftAddr = Integer.parseInt(calculateEffectiveAddress(word), 2);
		String nextPc = UtilClass.ReturnWithAppendedZeroes(Integer.toBinaryString(condensedCurrentPc + 1), 16);
		FrontPanel.SetRegister(Registers.GPR3, UtilClass.GetStringFormat(nextPc));
		String pcString = UtilClass.ReturnWithAppendedZeroes(Integer.toBinaryString(eftAddr), 12);
		FrontPanel.SetRegister(Registers.PC, UtilClass.GetStringFormat(pcString));
	}

}
