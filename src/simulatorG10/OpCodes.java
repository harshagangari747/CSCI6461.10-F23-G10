package simulatorG10;

import javax.xml.stream.events.EndDocument;

//Enum for identifying the opcodes
public enum OpCodes {

	LOC(""), Data(""), End(""), HALT("000000"), LDR("000001"), STR("000010"), LDA("000011"), LDX("100001"),
	STX("100010"), JZ("001000"), JNE("001001"), JCC("001010"), JMA("001011"), JGE("001111"), JSR("001100"),
	RFS("001101"), SOB("001110"), AMR("000100"), SMR("000101"), AIR("000110"), SIR("000111"), MLT("010000"),
	DVD("010001"), TRR("010010"), OR("010100"), AND("010011"), NOT("010101"), SRC("011001"), RRC("011010"),
	IN("110001"), OUT("110010"), CHK("110011");

	public final String label;

	private OpCodes(String labelName) {
		this.label = labelName;
	}

	public String GetValue() {
		return this.label;
	}
}
