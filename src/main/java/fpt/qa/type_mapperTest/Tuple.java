package fpt.qa.type_mapperTest;

import fpt.qa.type_mapper.ProgramType;

public class Tuple {
		String channel;
		String program;
		ProgramType expectedType;
		
		public Tuple() {
		}
		
		public Tuple(String c, String p, ProgramType e) {
			this.channel = c.trim();
			this.program = p.trim();
			this.expectedType = e;
		}
		
		public Tuple(String c, String p, String e) {
			this.channel = c.trim();
			this.program = p.trim();
			this.expectedType = getType(e.trim());
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

		public ProgramType getExpectedType() {
			return expectedType;
		}

		public void setExpectedType(ProgramType expectedType) {
			this.expectedType = expectedType;
		}

		private static ProgramType getType(String type) {
			System.out.println("type = " + type);
			type = type.trim().toUpperCase();
			switch (type) {
				case "SPORT":
					return ProgramType.SPORT;
				case "CARTOON":
					return ProgramType.CARTOON;
				case "GAME_SHOW":
					return ProgramType.GAME_SHOW;
				case "NEWS":
					return ProgramType.NEWS;
				case "FILM":
					return ProgramType.FILM;
				case "MUSIC":
					return ProgramType.MUSIC;
				default:
					return ProgramType.ALL;
			}
		}
		
		public String toString() {
			return String.format("c = %s; p = %s; et = %s",channel, program, expectedType.toString());
		}
	}