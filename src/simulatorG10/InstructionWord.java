package simulatorG10;

public class InstructionWord {
	public String instLocation;
	public String instAddress;
	public InstructionWord() {}
	public InstructionWord(String location, String address)
	{
		this.instLocation = location;
		this.instAddress = address;
	}
}

