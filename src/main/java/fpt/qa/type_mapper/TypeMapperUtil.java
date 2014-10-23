package fpt.qa.type_mapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.databind.deser.DataFormatReaders.Match;

public class TypeMapperUtil {
	private static String[] co_dau = new String[] { "à", "á", "ạ", "ả", "ã",
			"â", "ầ", "ấ", "ậ", "ẩ", "ẫ", "ă", "ằ", "ắ", "ặ", "ẳ", "ẵ", "è",
			"é", "ẹ", "ẻ", "ẽ", "ê", "ề", "ế", "ệ", "ể", "ễ", "ì", "í", "ị",
			"ỉ", "ĩ", "ò", "ó", "ọ", "ỏ", "õ", "ô", "ồ", "ố", "ộ", "ổ", "ỗ",
			"ơ", "ờ", "ớ", "ợ", "ở", "ỡ", "ù", "ú", "ụ", "ủ", "ũ", "ư", "ừ",
			"ứ", "ự", "ử", "ữ", "ỳ", "ý", "ỵ", "ỷ", "ỹ", "đ", "À", "Á", "Ạ",
			"Ả", "Ã", "Â", "Ầ", "Ấ", "Ậ", "Ẩ", "Ẫ", "Ă", "Ằ", "Ắ", "Ặ", "Ẳ",
			"Ẵ", "È", "É", "Ẹ", "Ẻ", "Ẽ", "Ê", "Ề", "Ế", "Ệ", "Ể", "Ễ", "Ì",
			"Í", "Ị", "Ỉ", "Ĩ", "Ò", "Ó", "Ọ", "Ỏ", "Õ", "Ô", "Ồ", "Ố", "Ộ",
			"Ổ", "Ỗ", "Ơ", "Ờ", "Ớ", "Ợ", "Ở", "Ỡ", "Ù", "Ú", "Ụ", "Ủ", "Ũ",
			"Ư", "Ừ", "Ứ", "Ự", "Ử", "Ữ", "Ỳ", "Ý", "Ỵ", "Ỷ", "Ỹ", "Đ", " " };

	private static String[] khong_dau = new String[] { "a", "a", "a", "a", "a",
			"a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "e",
			"e", "e", "e", "e", "e", "e", "e", "e", "e", "e", "i", "i", "i",
			"i", "i", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o",
			"o", "o", "o", "o", "o", "o", "u", "u", "u", "u", "u", "u", "u",
			"u", "u", "u", "u", "y", "y", "y", "y", "y", "d", "A", "A", "A",
			"A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A",
			"A", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "I",
			"I", "I", "I", "I", "O", "O", "O", "O", "O", "O", "O", "O", "O",
			"O", "O", "O", "O", "O", "O", "O", "O", "U", "U", "U", "U", "U",
			"U", "U", "U", "U", "U", "U", "Y", "Y", "Y", "Y", "Y", "D", " " };

	/*
	 * Remove all space, non-diacritic;
	 */
	public static String normalize(String name) {
		name = name.toLowerCase();
		name = name.replaceAll("\\s", "");
		for (int i = 0; i < co_dau.length; ++i) {
			name = name.replaceAll(co_dau[i], khong_dau[i]);
		}
		return name;
	}

	private static String sendGet(String url) throws ClientProtocolException,
			IOException {
		HttpClient client = HttpClientBuilder.create().build();
//		String safeUrl = URLEncoder.encode(url, "UTF-8");
//		System.out.println("Safe URL " + safeUrl);
		HttpGet request = new HttpGet(url);

		HttpResponse response = client.execute(request);

		System.out.println("Response code: "
				+ response.getStatusLine().getStatusCode());

		BufferedReader br = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = br.readLine()) != null) {
			result.append(line);
		}

		return result.toString();
	}

	public static String getGoogleSearch(String keyword, Boolean inQuoted)
			throws IOException {
//		keyword = keyword.substring(0, 20);
		String query = keyword.replaceAll("\\s", "\\+");
		if (inQuoted)
			query = "%22" + query + "%22";
		String url = String
				.format("http://www.google.com/search?client=ubuntu&channel=fs&q=%s&ie=utf-8&oe=utf-8",
						query);
		System.out.println(url);
		
		String response = sendGet(url).toLowerCase();
		return response;
//		System.out.println(response.)
//		Document doc = Jsoup.parse(response);
//		return doc;
	}

	public static int getTotalWord(final String doc, final TypeRecognizer type) {
		int total = 0;
		for (String kw: type.getRelatedKeywords()) {
			total += count(doc, kw);
		}
		return total;
	}
	
	public static int count(String str, String pat) {
		Pattern p = Pattern.compile(pat);
		Matcher m = p.matcher(str);
		int count  = 0;
		while (m.find()) {
			++count;
		}
		return count;
		
	}
}
