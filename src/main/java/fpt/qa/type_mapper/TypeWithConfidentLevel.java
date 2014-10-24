package fpt.qa.type_mapper;

public class TypeWithConfidentLevel implements Comparable<TypeWithConfidentLevel> {
	private ProgramType type;
	private Double clv; // [0 - 1.0]

	public TypeWithConfidentLevel() {
		super();
	}

	public TypeWithConfidentLevel(ProgramType type, Double clv) {
		super();
		this.type = type;
		this.clv = clv;
	}

	public ProgramType getType() {
		return type;
	}

	public void setType(ProgramType type) {
		this.type = type;
	}

	public Double getClv() {
		return clv;
	}

	public void setClv(Double clv) {
		this.clv = clv;
	}

	@Override
	public int compareTo(TypeWithConfidentLevel o) {
		if (clv.compareTo(o.getClv()) != 0) {
			return - clv.compareTo(o.getClv());
		}
		return - type.compareTo(o.getType());
	}
	
	@Override
	public String toString() {
		return String.format("{type: %s, clv: %.3f}", type, clv);
	}
}
