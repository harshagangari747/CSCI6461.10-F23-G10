package simulatorG10.Exceptions;

public class InstructionFormatException extends Exception {
	public InstructionFormatException(String exMessage) {
		super("Instruction Format Exception : "+  exMessage);
	}

}
