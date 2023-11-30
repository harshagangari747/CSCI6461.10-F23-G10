package simulatorG10.Exceptions;

import simulatorG10.Simulator;

public class OpCodeNotSupportedException extends Exception {
	public OpCodeNotSupportedException(String exMessage)
	{
		super("Op code Not Supported Exception : "+ exMessage);
		Simulator.faultOccured = true;
	}

}
