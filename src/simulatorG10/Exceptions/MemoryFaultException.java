package simulatorG10.Exceptions;

import simulatorG10.Simulator;

public class MemoryFaultException extends Exception {
	public MemoryFaultException(String exMessage) {
		super("Memory Fault Exception :"+exMessage);
		Simulator.faultOccured = true;
	}

}
