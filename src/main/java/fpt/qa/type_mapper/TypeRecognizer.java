package fpt.qa.type_mapper;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.jsoup.nodes.Document;

public class TypeRecognizer {
	private ProgramType type;
	
	// all lowercase, without space;
	private Set<String> dedicatedChannels; // all program in this channel belongs to this type.!
	private Set<String> typeKeywords; // highly confident keyword
	private Set<String> relatedKeywords; // related keyword for searching
	
	private final static Double PERFECT = 1.0;
	private final static Double CONFIDENT = 0.8;
	private final static Double QUITE_CONFIDENT = 0.6;
	private final static Double CLUELESS = 0.0;
	
	public void loadConfig(String[] chns, String[] kws, String[] rks) {
		dedicatedChannels = new TreeSet<String>();
		typeKeywords = new TreeSet<String>();
		relatedKeywords = new TreeSet<String>();
				
		for (String chn: chns) {
			dedicatedChannels.add(TypeMapperUtil.normalize(chn));
		}
		
		for (String kw: kws) {
			typeKeywords.add(TypeMapperUtil.normalize(kw));
		}
		
		for (String rk: rks) {
			relatedKeywords.add(rk);
		}
	}
	
	public void show() {
		System.out.println("DEDICATED CHANNELS: ");
		for (String str: dedicatedChannels) {
			System.out.print (str + " ");
		}
		
		System.out.println();
		System.out.println("TYPE KW: ");
		for (String str: typeKeywords) {
			System.out.print(str + " ");
		}
		
		System.out.println();
		System.out.println("RELATED KW: ");
		for (String str: relatedKeywords) {
			System.out.print(str + " ");
		}
	}
	
	public ProgramType getType() {
		return type;
	}
	
	public void SetType(ProgramType type) {
		this.type = type;
	}
	
	public Set<String> getTypeKeywords() {
		return typeKeywords;
	}
	
	public Set<String> getDedicatedChannels() {
		return dedicatedChannels;
	}
	
	public Set<String> getRelatedKeywords() {
		return relatedKeywords;
	}
	
	public Double belongThisType(String channel, String prog) {
		String nCh = TypeMapperUtil.normalize(channel);
		String nProg = TypeMapperUtil.normalize(prog);
		
		// Channel first
		if (dedicatedChannels.contains(nCh)) {
			return PERFECT;
		}
		
		if (dedicatedChannels.size() > 0) {
			for (String ch: dedicatedChannels) {
				if (nCh.contains(ch)) {
					System.out.println("DedicatedChn: " + nCh + " contains " + ch + " " + dedicatedChannels.size());
					return CONFIDENT;
				}
			}
		}
		for (String kw : typeKeywords) {
			if (nCh.contains(kw)) {
				System.out.println("Ch-kw" + nCh + " contains " + kw);
				return QUITE_CONFIDENT;
			}
		}
		
		// Prog
		if (typeKeywords.contains(prog)) return CONFIDENT;
		for (String kw: typeKeywords) {
			if (nProg.contains(kw)) {
				System.out.println("Prog-Kw" + nProg + " contains " + kw);
				return QUITE_CONFIDENT;
			}
		}
		
		return CLUELESS;
	}
	
}
