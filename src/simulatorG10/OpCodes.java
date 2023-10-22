package simulatorG10;

import javax.xml.stream.events.EndDocument;

//Enum for identifying the opcodes
public enum OpCodes {

	LOC(""), Data(""), End(""), HALT("000000"), LDR("000001"), STR("000010"), LDA("0000011"), LDX("101001"),
	STX("101010"), JZ(""), JNE(""), JCC(""), JMA(""), JGE(""), JSR(""), RFS(""), SOB(""), AMR(""), SMR(""), AIR(""),
	SIR(""), MLT(""), DVD(""), TRR(""), ORR(""), AND(""), NOT(""), SRC(""), RRC(""), IN(""), OUT(""), CHK("");

	public final String label;

	private OpCodes(String labelName) {
		this.label = labelName;
	}

}
