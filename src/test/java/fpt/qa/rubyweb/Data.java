package fpt.qa.rubyweb;

import java.util.ArrayList;
import java.util.List;

public class Data {
	public String data_text;
	public String original_text;
	public String head;
	public List<String> modifiers;
	public String head_data;
	public String modifiers_data;
	
	public Data(){
		data_text="";
		original_text = "";
		head=  "";
		modifiers = new ArrayList<String>();
		head_data = "";
		modifiers_data = "";
	}
	
}
