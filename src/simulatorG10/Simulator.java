package simulatorG10;

import java.lang.constant.ConstantDesc;
import java.net.SecureCacheResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;

import javax.naming.BinaryRefAddr;
import javax.sql.rowset.CachedRowSet;
import javax.swing.event.MenuDragMouseEvent;
import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

/*Class to set,get, run instructions
 * Typically our CPU
 * */
public class Simulator {
	private static ArrayList<Integer> address;
	private static Iterator<Integer> iterator;
	private static int index;

	/*
	 * Class constructor to set the address with keys of memory to iterate through
	 * so that we'll keep track of the next instruction
	 */
	public Simulator() {
		if (address == null)
			address = new ArrayList<Integer>(Memory.memory.keySet());

		iterator = address.iterator();
	}

	/*
	 * Triggered when user clicked Step Button in the front panel This method runs
	 * the instruction by decoding it and therefore sets,stores,gets information to
	 * and from different registers
	 */
	public void StepIntoTheInstruction() throws Exception {
		String currentPc = FrontPanel.pcValueLbl.getText();
		try {

			int condensedCurrentPc = Integer.parseInt(UtilClass.ReturnUnformattedString(currentPc), 10);
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
				int indexOfCurrentPc = address.indexOf(condensedCurrentPc);

				// increment PC
				if (index < address.size()) {
					String appendedPcValue = UtilClass.ReturnWithAppendedZeroes(address.get(++index).toString(), 12);
					FrontPanel.SetRegister(Registers.PC, UtilClass.GetStringFormat(appendedPcValue));

				}
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
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
		case LDR: {
			LoadRegisterFromMemory(word);
		}
			break;
		case STR: {
			StoreRegisterToMemory(word);
		}
			break;
		case LDA: {
			LoadRegisterWithAddress(word);
		}
			break;
		case LDX: {
			LoadIndexRegisterFromMemory(word);
		}
			break;
		case STX: {
			StoreIndexRegisterToMemory(word);
		}
			break;
		default:
			throw new Exception("Can't perfrom operation. Opcode not found");
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
		String eftAddr = calculateEffectiveAddress(word);
		String gprValue = UtilClass.ReturnUnformattedString(GetGprOrIndxContent(word.gpRegister));
		Memory.memory.put(Integer.parseInt(eftAddr, 10), gprValue);
		System.out.println("Stored " + gprValue + " into address " + eftAddr);

	}

	/*
	 * Performs operation LDA Loads the specified GPR at 6,7 bits with the address
	 * Ex : LDA 1,0,addr(18) opcode value : 000011
	 */
	private void LoadRegisterWithAddress(InstructionWord word) throws Exception {
		String eftAddr = calculateEffectiveAddress(word);
		SetGprOrIndex(eftAddr, word.gpRegister);
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
		String eftAddr = calculateEffectiveAddress(word);
		String ixrValue = UtilClass.ReturnUnformattedString(GetGprOrIndxContent(word.gpRegister));
		Memory.memory.put(Integer.parseInt(eftAddr, 10), ixrValue);
		System.out.println("Stored " + ixrValue + " into address " + eftAddr);

	}

	/*
	 * Method to calculate the Effective address
	 */
	private String calculateEffectiveAddress(InstructionWord word) {
		StringBuffer effectiveAddress = new StringBuffer();
		BinaryOperations bOperations = BinaryOperations.GetBinaryOperationsObj();
		if (!word.indirectAddressing) {
			if (word.ixRegister == Registers.IXR0)
				effectiveAddress.append(Memory.memory.get(word.address));
			else {
				String currentIxrValue = null;
				if (word.ixRegister == Registers.IXR1)
					currentIxrValue = FrontPanel.ixr1ValueLbl.getText();
				else if (word.ixRegister == Registers.IXR2)
					currentIxrValue = FrontPanel.ixr2ValueLbl.getText();
				else if (word.ixRegister == Registers.IXR3)
					currentIxrValue = FrontPanel.ixr3ValueLbl.getText();
				currentIxrValue = UtilClass.ReturnUnformattedString(currentIxrValue);
				String addressString = bOperations.BinaryAddition(currentIxrValue, Memory.memory.get(word.address));
				effectiveAddress.append(UtilClass.ReturnWithAppendedZeroes(addressString, 16));
			}
		} else {
			if (word.ixRegister == Registers.IXR0) {
				String nextAddress = Memory.memory.get(word.address);
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
				String addressString = bOperations.BinaryAddition(currentIxrValue, Memory.memory.get(word.address));
				String finalEftAddress = Memory.memory.get(Integer.parseInt(addressString, 10));
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

}
