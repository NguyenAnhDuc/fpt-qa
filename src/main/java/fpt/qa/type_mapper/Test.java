package fpt.qa.type_mapper;

public class Test {

	public Test() {
		// TODO Auto-generated constructor stub
	}
	public static void main(String[] args) {
		TypeMapper mapper = new TypeMapper("resourcesc/type_mapper.txt");
		
		System.out.println(mapper.getType("", "ai là triệu phú"));
		System.out.println(mapper.getType("star movie",""));
		System.out.println(mapper.getType("vtv3","thời trang và cuộc sống"));
	}
}
