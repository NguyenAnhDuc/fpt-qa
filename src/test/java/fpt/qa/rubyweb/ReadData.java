package fpt.qa.rubyweb;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fpt.ruby.model.QuestionStructure;
import com.fpt.ruby.service.mongo.QuestionStructureService;

public class ReadData {
	static List<Data> datas = new ArrayList<Data>();
	static QuestionStructureService questionStructureService;
	public static void readFile() throws IOException{
		 BufferedReader br = new BufferedReader(new FileReader("movie.txt"));
		    try {
		        String line = br.readLine();
		        while (line != null) {
		        	if  (!line.trim().isEmpty()){
		        		Data data = new Data();
			        	data.data_text = line.trim();
			        	data.head_data = br.readLine().trim();
			        	data.modifiers_data = br.readLine().trim();
			        	datas.add(data);
		        	}
		        	
		        	line = br.readLine();
		        }
		    } finally {
		        br.close();
		    }
	}
	
	public static void process(){
		for (Data data : datas){
			//text
			String[] tokens = data.data_text.split(" ");
			for (String token : tokens) {
				data.original_text += token.split("/")[1]+ " ";
			}
			data.original_text = data.original_text.trim();
			//data.original_text = data.data_text.replaceAll("_", " ").trim();
			
			// head
			String[] headIndexs = data.head_data.split(" ");
			for (String headIndex : headIndexs){
				int headInd = Integer.parseInt(headIndex);
				data.head += tokens[headInd-1].split("/")[1] + " ";
			}
			data.head = data.head.trim();
			// modifiers
			if (!data.modifiers_data.isEmpty() && data.modifiers_data != null){
				List<String> modifierss = new ArrayList<String>();
				String[] modifiers = data.modifiers_data.split(",");
				for (String modifier : modifiers){
					String[] modifierIndexs = modifier.trim().split(" ");
					String aModifier = "";
					for (String modifierIndex : modifierIndexs) {
						int modifierInd = Integer.parseInt(modifierIndex.replaceAll(",", "").trim());
						aModifier += tokens[modifierInd-1].split("/")[1] + " ";
					}
					modifierss.add(aModifier);
				}
				data.modifiers = modifierss;
			}
			System.out.println(data.original_text);
		}
	}
	
	public static void cacheData(){
		for (Data data : datas) {
			QuestionStructure questionStructure = new QuestionStructure();
			questionStructure.setKey(data.original_text.replaceAll("_", " ").trim());
			questionStructure.setHead(data.head.replaceAll("_", " ").trim());
			List<String> modifiers = new ArrayList<String>();
			for (String  modifier : data.modifiers){
				modifiers.add(modifier.replaceAll("_", " ").trim());
			}
			questionStructure.setModifiers(modifiers);
			questionStructureService.cached(questionStructure);
			questionStructureService.save(questionStructure);
		}
	}

	public static void test(){
		for (Data data : datas){
			System.out.println(data.original_text);
		}
		System.out.println("DONE!");
	}
	
	public static void main(String[] args) throws Exception {
		datas = new ArrayList<Data>();
		questionStructureService = new QuestionStructureService();
		readFile();
		process();
		cacheData();
	}
}
