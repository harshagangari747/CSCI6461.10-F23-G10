package simulatorG10.Exceptions;

public class MemoryFaultException extends Exception {
	public MemoryFaultException(String exMessage) {
		super("Memory Fault Exception :"+exMessage);
	}

}
