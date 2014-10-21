package fpt.qa.additionalinformation.name_mapper;

import java.io.File;
import java.util.*;
import com.fpt.ruby.model.NameMapper;
import com.fpt.ruby.service.NameMapperService;
import vn.hus.nlp.utils.UTF8FileUtility;

public class DataMigration {
	private static NameMapperService ns = new NameMapperService();
	private static final String resource = "/home/someone/Workspace/code/rubyweb/src/main/resources";

	private static void loadDomainMapper(String domain, String data,
			Boolean isDiacritic) {
		String fullPath = resource + File.separator
				+ (!isDiacritic ? ("non-diacritic" + File.separator) : "")
				+ "data-name-mapper" + File.separator + data;
		String[] lines = UTF8FileUtility.getLines(fullPath);

		for (String line : lines) {
			line = line.trim();
			if (line.equals("") || line.startsWith("#"))
				continue;

			String[] parts = line.split("\t");
			String type = parts[0];
			String name = "";
			String allVar = "";
			List<String> vars = new ArrayList<String>();

			if (parts.length < 2)
				continue;
			int sep = parts[1].indexOf(';');

			if (sep > -1) {
				name = parts[1].substring(0, sep);
				allVar = parts[1].substring(sep + 1);
			} else {
				name = parts[1];
			}
			
			vars.add(name);
			StringTokenizer tokenizer = new StringTokenizer(allVar, ",");
			while (tokenizer.hasMoreTokens()) {
				vars.add(tokenizer.nextToken().trim());
			}

			NameMapper n = new NameMapper();
			n.setName(name);
			n.setDomain(domain);
			n.setIsDiacritic(isDiacritic);
			n.setType(type);
			n.setVariant(vars);
			n.setEnteredDate(new Date());
			n.setLastMention(new Date());

			ns.save(n);
		}

	}

	public static void main(String[] args) {
		
		for (int i = 0; i <= 1; ++i) {
			loadDomainMapper("tv", "tv_domain.txt", i != 0);
			loadDomainMapper("food", "foodNames.txt", i != 0);
			loadDomainMapper("movie", "movieNames.txt", i != 0);
		}	 
	}

}
