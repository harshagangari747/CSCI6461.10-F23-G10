package simulatorG10.Exceptions;

import simulatorG10.Simulator;

public class InstructionFormatException extends Exception {
	public InstructionFormatException(String exMessage) {
		super("Instruction Format Exception : "+  exMessage);
		Simulator.faultOccured = true;
	}

}

