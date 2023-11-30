package simulatorG10.Exceptions;

public class OpCodeNotSupportedException extends Exception {
	public OpCodeNotSupportedException(String exMessage)
	{
		super("Op code Not Supported Exception : "+ exMessage);
	}

}
