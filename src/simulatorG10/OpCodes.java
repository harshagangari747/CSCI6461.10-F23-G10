package simulatorG10;

public enum OpCodes {
	
	HALT("00"),
	LDR("01"),
	STR("02"),
	LDA("03"),
	LDX("41"),
	STX("42"),
	JZ("10");
	public final String label;

	private OpCodes(String labelName)
	{
		this.label = labelName;
	}
			

}
