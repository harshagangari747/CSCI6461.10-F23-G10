package simulatorG10;

import java.util.ArrayList;
import java.util.Iterator;


public class Simulator {
	ArrayList<Integer> address;
	Iterator<Integer> iterator;
//	public Simulator()
//	{
//		address = (ArrayList<Integer>) Memory.memory.keySet();	
//		iterator = address.iterator();
//	}
	public void StepIntoTheInstruction() throws Exception {
		String currentPc = FrontPanel.pcValueLbl.getText();
//		if (address.contains(address)) {
//			while (iterator.hasNext())// end of the list )
//			{
//				
//				// increment PC
//				FrontPanel.LoadRegister(Registers.PC,iterator.next().toString());
//
//				// push addr to mar
//				FrontPanel.LoadRegister(Registers.MAR,iterator.);
//				// push value at loc (mar) to mbr
//				// push mbr to ir
//				// decode ir
//				// set those bits
//			}
//		} else {
//			throw new Exception(
//					"Address " + currentPc + " doesn't exist in the memory/file. Please give a valid address");
//		}
	}

}
