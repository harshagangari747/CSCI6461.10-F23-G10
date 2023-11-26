package simulatorG10.Exceptions;

public class FileException extends Exception {
	public FileException(String exMessage)
	{
		super("File Exception : "+exMessage);
	}

}
