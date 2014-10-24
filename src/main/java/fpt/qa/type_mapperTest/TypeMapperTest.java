package fpt.qa.type_mapperTest;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

import vn.hus.nlp.utils.UTF8FileUtility;
import fpt.qa.type_mapper.ProgramType;
import fpt.qa.type_mapper.TypeMapper;
import fpt.qa.type_mapper.TypeWithConfidentLevel;

public class TypeMapperTest {
	static List<Tuple> ts = new ArrayList<Tuple>();

	@BeforeClass
	public static void loadData() {
		String[] lines = UTF8FileUtility
				.getLines("src/test/resources/type_mapper/program.txt");
		for (String line : lines) {
			try {
				String[] part = line.split(";");
				Tuple t = new Tuple(part[0], part[1], part[2]);
				ts.add(t);
				System.out.println(t.toString());
			} catch (Exception ex) {
				continue;
			}
		}
	}

	@Test
	public void massiveTest() {
		int totalTest = 0;
		int pass = 0;

		TypeMapper tm = new TypeMapper();
		int i = 0;
		for (Tuple t : ts) {
			if (t.getExpectedType().equals(ProgramType.ALL))
				continue;
			totalTest++;
			if (i >= 222) break;
			ProgramType type = tm.getTypes(t.getChannel(), t.getProgram()).get(0).getType();
			System.out.println("-------------\nCase " + (++i) + ": "
					+ t.toString());
			System.out.println("Result = " + type.toString());

			if (type.equals(t.getExpectedType())) {
				pass++;
				System.out.println("-------------\n");
			} else {
				System.out.println("-------FAIL----\n");
			}

		}
		System.out.println("\n ********** \n " + pass + "/" + totalTest
				+ ".\n Percent : " + (pass * 100 / totalTest) + "%");
	}

//	@Test
	public void singleTest02() {
		TypeMapper tm = new TypeMapper();
		List<TypeWithConfidentLevel> types = tm.getTypes("vtv1", "ĐƯỜNG TỚI THẾ VẬN HỘO");
		System.out.println("RESULT : ");
		for (TypeWithConfidentLevel t: types) {
			System.out.println(t.toString());
		}
//		System.out.println(type);

//		assertTrue(ProgramType.FILM == type);
	}
//	 
//	
//	// @Test
//	public void testCustomRecognizer1() {
//		TypeMapper tm = new TypeMapper();
//		ProgramType type = tm.getType("vtv3", "doi gio hu t20");
//		System.out.println(type);
//
//		assertTrue(ProgramType.FILM == type);
//	}
//
//	// @Test
//	public void singtest01() {
//		TypeMapper tm = new TypeMapper();
//		ProgramType type = tm.getType("vtv3", "phim hoat hinh");
//		System.out.println(type);
//
//		assertTrue(ProgramType.CARTOON == type);
//	}
//
//	// @Test
//	public void testIndividual() {
//		TypeMapper tm = new TypeMapper();
//		ProgramType type = tm.getType("vtv3", "master chef");
//		System.out.println(type);
//
//		assertTrue(ProgramType.GAME_SHOW == type);
//	}

}
