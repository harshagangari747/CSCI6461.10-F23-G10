package simulatorG10;

//Enum to identify the registers in the UserInterface
public enum Registers {
	GPR0("00"), GPR1("01"), GPR2("10"), GPR3("11"), IXR0("00"), IXR1("01"), IXR2("10"), IXR3("11"), PC(""), MAR(""),
	MBR(""), IR(""), MFR(""), PRIV(""), CC("");

	private String label;

	private Registers(String labelName) {
		this.label = labelName;
	}

	public String GetValue() {
		return this.label;
	}
}
