package fpt.qa.additionalinformation.name_mapper;

import java.util.List;
import java.util.Set;

import fpt.qa.mdnlib.struct.pair.Pair;
/**
 * @author Thien BUI-DUC, hus.ict@gmail.com
 * <p>
 * 03 Sep 2014, 20:39:56
 * <p>
 * A named engine which returns Variation names  or final name of a given type and word . 
 */
public interface NamedEngine {
	/**
	 * Gets final name and type of a name.
	 * @param name
	 * @return Final name and type of a name
	 */
	public String getFinalName(String type, String name);
	
	/**
	 * Gets all variations name of a name.
	 * @param name
	 * @return all variations name of a name
	 */
	public Set<String> getVariationNames(String type, String name) ;
	
	public List< Pair< String, String > > getAllNames();
}
