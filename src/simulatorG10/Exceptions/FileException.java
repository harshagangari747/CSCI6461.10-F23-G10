package simulatorG10.Exceptions;

import simulatorG10.Simulator;

public class FileException extends Exception {
	public FileException(String exMessage)
	{
		super("File Exception : "+exMessage);
		Simulator.faultOccured = true;
	}

}
