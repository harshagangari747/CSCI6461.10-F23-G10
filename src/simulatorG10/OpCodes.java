package simulatorG10;

//Enum for identifyisng the opcodes
public enum OpCodes {

	HALT("000000"), LDR("000001"), STR("000010"), LDA("0000011"), LDX("101001"), STX("101010");

	public final String label;

	private OpCodes(String labelName) {
		this.label = labelName;
	}

}
