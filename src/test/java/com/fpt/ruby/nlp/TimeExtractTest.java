package com.fpt.ruby.nlp;

import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.fpt.ruby.helper.IOHelp;
import com.fpt.ruby.model.TimeExtract;

public class TimeExtractTest {
	@Test
	public void test(){
		String dir = (new IOHelp()).getClass().getClassLoader().getResource("").getPath();
		NlpHelper.normalizeQuestion("init");
		List<String> datas = IOHelp.readFileToList(dir  + "/TestTimeData.txt");
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
		for (String data : datas)
			if (!data.isEmpty()){
				String[] strings = data.split("\\|");
				TimeExtract timeExtract = NlpHelper.getTimeCondition( strings[0] );
				Assert.assertEquals("Begin Time Expected", strings[1].trim(),sdf.format(timeExtract.getBeforeDate()) );
				Assert.assertEquals("End Time Expected", strings[2].trim(),sdf.format(timeExtract.getAfterDate()) );
			}
	}
}
