/*package fpt.qa.type_mapperTest;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import fpt.qa.type_mapper.TypeMapperUtil;

public class TypeMapperUtilTest {
	
	@Test
	public void normalizeTest() {
		assertEquals("must be true", "toilathuc", TypeMapperUtil.normalize("Tôi là       THỰc"));
		assertEquals("must be true", "123thucdx", TypeMapperUtil.normalize("    123 Thực Dx     "));
		assertEquals("must be true", "banhduccoxuong", TypeMapperUtil.normalize("Bánh đúc có xương"));
		assertEquals("must be true", "dinhducdat", TypeMapperUtil.normalize("Đinh ĐỨc đạt    "));
	}
	
	@Test
	public void testCountString() {
		assertTrue(3 == TypeMapperUtil.count("hello xxxxhello xdfdfdfdfhelyyy helloxxxx", "hello"));
		assertTrue(0 == TypeMapperUtil.count("hello xxxxhello xdfdfdfdfhelyyy helloxxxx", "hellop"));
		assertTrue(1 == TypeMapperUtil.count("xyzxxyy", "yzxx"));
	}
	
	@Test
	public void googleQueryTest() {
		try {
			String doc = TypeMapperUtil.getGoogleSearch("banh duc co xuong", false);
			System.out.println(doc.toString());
		} catch (IOException e) {
			assertTrue(false);
		}
	}
	
	@Test
	public void googleQueryTest2() {
		try {
			String doc = TypeMapperUtil.getGoogleSearch("banh duc co xuong", true);
			System.out.println(doc.toString());
		} catch (IOException e) {
			System.out.println("Error : " + e.getMessage());
			assertTrue(false);
		}
	}
	
	@Test
	public void googleQueryTest3() {
		try {
			String doc = TypeMapperUtil.getGoogleSearch("master chef", true);
			System.out.println(doc.toString());
		} catch (IOException e) {
			System.out.println("Error : " + e.getMessage());
			assertTrue(false);
		}
	}
}
*/