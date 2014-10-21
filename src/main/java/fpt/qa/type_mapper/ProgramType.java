package fpt.qa.type_mapper;

import org.apache.commons.lang.enums.Enum;

public class ProgramType {
	
	private EnumType type ;
	private String channel ;
	private String program ;
	public ProgramType() {
		// TODO Auto-generated constructor stub
	}
	
	public ProgramType(EnumType type, String channel, String program) {
		this.type = type ;
		this.channel = channel;
		this.program = program;
	}

	public EnumType getType() {
		return type;
	}

	public void setType(EnumType type) {
		this.type = type;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getProgram() {
		return program;
	}

	public void setProgram(String program) {
		this.program = program;
	}

	@Override
	public String toString() {
		return "ProgramType [type=" + type + ", channel=" + channel
				+ ", program=" + program + "]";
	}
}
