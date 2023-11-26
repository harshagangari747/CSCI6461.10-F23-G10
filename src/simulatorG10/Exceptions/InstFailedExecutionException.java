package simulatorG10.Exceptions;
import simulatorG10.InstructionWord;

public class InstFailedExecutionException extends Exception {
	public InstFailedExecutionException(InstructionWord word, int pcValue,String message) {
		super("Instruction failed to execute. ProgramLoadFile.txt At line :" + (pcValue - 1) + " \n " + word.opCode
				+ "," + ExceptionMessageDecodeBinary(word.gpRegister.GetValue()) + ","
				+ ExceptionMessageDecodeBinary(word.ixRegister.GetValue()) + ","
				+ ExceptionMessageDecodeBoolean(word.indirectAddressing) + ","
				+ ExceptionMessageDecodeBinary(String.valueOf(word.address))+ "\n Exception Message : " +message);
	}

	private static int ExceptionMessageDecodeBoolean(boolean value) {
		return value ? 1 : 0;
	}

	private static int ExceptionMessageDecodeBinary(String value) {
		return Integer.parseInt(value, 2);
	}
}
