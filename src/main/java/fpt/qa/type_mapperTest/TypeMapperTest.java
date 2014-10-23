package fpt.qa.type_mapperTest;

import static org.junit.Assert.*;

import org.junit.Test;

import fpt.qa.type_mapper.ProgramType;
import fpt.qa.type_mapper.TypeMapper;

public class TypeMapperTest {
	@Test
	public void testTypeMapper() {
		TypeMapper tm = new TypeMapper();
		ProgramType type = tm.getType("k+ns", "CHAMPIONS LEAGUE 2014-2015 TỔNG HỢP LƯỢT ĐẤU THỨ 3 - SỐ 1");
		System.out.println(type);
		
		assertTrue(ProgramType.SPORT == type);
	}
}
