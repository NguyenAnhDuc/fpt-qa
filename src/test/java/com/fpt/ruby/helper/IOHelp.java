package com.fpt.ruby.helper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class IOHelp {
	public String readFile(String fileName){
		  try(BufferedReader br = new BufferedReader(new FileReader("file.txt"))) {
		        StringBuilder sb = new StringBuilder();
		        String line = br.readLine();

		        while (line != null) {
		            sb.append(line);
		            sb.append(System.lineSeparator());
		            line = br.readLine();
		        }
		        return sb.toString();
		    }
		  catch (Exception ex){
			  ex.printStackTrace();
			  return "";
		  }
	}
	
	
	public static List<String> readFileToList(String fileName){
		List<String> results = new ArrayList<String>();  
		try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
		        StringBuilder sb = new StringBuilder();
		        String line = br.readLine();

		        while (line != null) {
		        	results.add(line.trim());
		        	line = br.readLine();
		        }
		    }
		  catch (Exception ex){
			  ex.printStackTrace();
		  }
		return results;
	}
	
}
