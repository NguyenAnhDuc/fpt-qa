package fpt.qa.type_mapperTest;

import static org.junit.Assert.*;

import org.junit.Test;

import fpt.qa.type_mapper.ProgramType;
import fpt.qa.type_mapper.TypeMapper;

public class TypeMapperTest {
	@Test
	public void testTypeMapper() {
		TypeMapper tm = new TypeMapper();
		ProgramType type = tm.getType("vtvcab5", "SIX BULLETS");
		System.out.println(type);
		
		assertTrue(ProgramType.FILM == type);
	}
}
