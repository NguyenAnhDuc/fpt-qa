package fpt.qa.rubyweb;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import modifier.AbsoluteTime;
import modifier.AbsoluteTime.TimeResult;

public class CrawlXemPhimSo {
	public static void writeFile(String filename, String content)
			throws IOException {
		File file = new File(filename);

		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();
	}
	
	public static void main(String[] args) throws IOException {
		AbsoluteTime absoluteTime = new AbsoluteTime();
		TimeResult timeResult = absoluteTime.getAbsoluteTime("hôm nay là ngày gì");
		System.out.println(timeResult.getBeginTime());
		System.out.println(timeResult.getEndTime());
		/*BufferedReader br = new BufferedReader(new FileReader("cin_name.txt"));
		StringBuilder everything = new StringBuilder();
		int count = 0;
		String line = "";
		List<String> lines = new ArrayList<String>();
		try {

			line = br.readLine();
			while (line != null) {
				if (line.isEmpty()) {
					count = 0;
					everything.append(lines.get(0).trim() + "; ");
					for (int  i= 1; i<lines.size()-1;i++){
						everything.append(lines.get(i) + ", " );
					}
					everything.append(lines.get(lines.size()-1));
					everything.append("\n" + "cin_name" + "\t");
					lines = new ArrayList<String>();
					line=br.readLine();
					continue;
				}
				lines.add(line);
				line = br.readLine();
			}
			writeFile("cinemas_var.txt", everything.toString());
		} catch (Exception ex) {
			System.out.println("Line Error: " + line);
			ex.printStackTrace();
		} finally {
			br.close();
		}
		System.out.println("DONE!");*/
	}
}
