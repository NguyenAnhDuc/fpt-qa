package fpt.qa.aiml.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class searchTV {
	private static String urlAIML = "http://tech.fpt.com.vn/AIML/api/bots/53ba1e47e4b0e77d64db19e0/chat?token=0a3e4baa-5226-4d81-be99-10f20e9def6b&request=";
	private static String urlCYPHER = "http://fti.pagekite.me/graph4mrc/excute?token=0a3e4baa-5226-4d81-be99-10f20e9def6b&query=";
	final static String token = "0a3e4baa-5226-4d81-be99-10f20e9def6b";

	public static boolean isJSONValid(String test) {
		try {
			new JSONObject(test);
		} catch (JSONException ex) {
			try {
				new JSONArray(test);
			} catch (JSONException e) {
				return false;
			}
		}
		return true;
	}

	protected static int getTimeNow() {
		int timeStamp = Integer.parseInt(new SimpleDateFormat("HHmm")
				.format(Calendar.getInstance().getTime()));
		return timeStamp;
	}

	public static String getNextDay() {
		Calendar cal = Calendar.getInstance();
		int nextDay = cal.get(Calendar.DATE) + 1;
		int month = cal.get(Calendar.MONTH) + 1;
		String nextDaySTR = "" + nextDay;
		String Month = "" + month;
		if (nextDay < 10) {
			nextDaySTR = "0" + nextDay;
		}
		if (month < 10) {
			Month = "0" + month;
		}
		return nextDaySTR + "/" + Month + "/" + cal.get(Calendar.YEAR);
	}

	protected static String getDayNow() {
		String timeStamp = new SimpleDateFormat("dd/MM/yyyy").format(Calendar
				.getInstance().getTime());
		return timeStamp;
	}

	protected static String getTimeCode(final String startTime) {
		String timeStamp = startTime;
		if (!startTime.equalsIgnoreCase(" ")) {
			if (startTime.equalsIgnoreCase("bây giờ")) {
				timeStamp = new SimpleDateFormat("HHmm").format(Calendar
						.getInstance().getTime());
			}
			if (startTime.equalsIgnoreCase("ngay bây giờ")) {
				timeStamp = new SimpleDateFormat("HHmm").format(Calendar
						.getInstance().getTime());
			}
			if (startTime.equalsIgnoreCase("chiều nay")) {
				timeStamp = "1300";
			}
			if (startTime.equalsIgnoreCase("trưa nay")) {
				timeStamp = "1130";
			}
			if (startTime.equalsIgnoreCase("tối nay")) {
				timeStamp = "1830";
			}
		}
		return timeStamp;
	}

	protected String getDayCode(final String timePublish) {
		String timeStamp = timePublish;
		if (!timePublish.equalsIgnoreCase("null")) {
			if (timePublish.equalsIgnoreCase("ngày mai")) {
				timeStamp = getNextDay();
			}
			if (timePublish.equalsIgnoreCase("sáng mai")) {
				timeStamp = getNextDay();
			}
			if (timePublish.equalsIgnoreCase("trưa mai")) {
				timeStamp = getNextDay();
			}
		}
		return timeStamp;
	}

	private static ArrayList<ScheduleDetail> cypherParse(String request) {
		URL requestUrl;
		ArrayList<ScheduleDetail> arrScheduleDetail = new ArrayList<ScheduleDetail>();
		try {
			requestUrl = new URL(urlCYPHER
					+ URLEncoder.encode(request, "UTF-8"));
			//System.out.println(requestUrl);
			URLConnection conn = requestUrl.openConnection();
			//System.out.println(conn);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "UTF-8"));
			String inputText;

			JSONObject jsonObject1, jsonObject2, jsonObject3, jsonObject4;
			JSONArray jsonArray;
			String str;
			while ((inputText = in.readLine()) != null) {
				if (isJSONValid(inputText)) {
					jsonObject1 = new JSONObject(inputText);
					str = jsonObject1.getString("result");
					System.out.println(str);
					str.trim();
					str = str.replaceAll("\n", "");
					str = str.replaceAll(",\"n\"", "},{\"n\"");
					JSONObject jsonData = new JSONObject(str);
					JSONObject nJson = (JSONObject) jsonData.get("n");
					JSONObject dataJson = (JSONObject) nJson.get("data");
					String start = dataJson.getInt("startTime") +"";
					String fininsh = dataJson.getInt("finishTime") +"";
					ScheduleDetail scheduleDetail = new ScheduleDetail(
							dataJson.getString("timePublish"),
							start,
							fininsh,
							dataJson.getString("kind"),
							dataJson.getString("name"),
							dataJson.getString("detail"),
							dataJson.getString("description"),
							dataJson.getString("key"));
					arrScheduleDetail.add(scheduleDetail);
					//System.out.println(str);
				}
			}
			in.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrScheduleDetail;
	}

	private static ScheduleCypher parseToScheduleCypher(String detailJSON) {
		ScheduleCypher scheduleCypher = new ScheduleCypher();
		try {
			JSONObject cypherJSON = new JSONObject(detailJSON);
			String name = cypherJSON.getString("name");
			String startTime = cypherJSON.getString("startTime");
			String finishTime = cypherJSON.getString("finishTime");
			String timePublish = cypherJSON.getString("timePublish");
			String detail = cypherJSON.getString("detail");
			String kind = cypherJSON.getString("kind");
			String channel = cypherJSON.getString("channel");
			String Return = cypherJSON.getString("return");
			scheduleCypher = new ScheduleCypher(timePublish, startTime,
					finishTime, kind, name, detail, channel, Return);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return scheduleCypher;
	}

	private static String queryCypher(ScheduleCypher scheduleCypher) {
		String queryCypher = "";
		ArrayList<ScheduleDetail> arrScheduleDetail = new ArrayList<ScheduleDetail>();
		String queryReturn = " return n order by n.timePublish, n.startTime limit 6";
		// JSONObject cypherJSON = new JSONObject(detailJSON);
		String name = scheduleCypher.getProgram();
		String startTime = scheduleCypher.getStartTime();
		String finishTime = scheduleCypher.getFinishTime();
		String timePublish = scheduleCypher.getTimePublish();
		String detail = scheduleCypher.getDetail();
		String kind = scheduleCypher.getKind();
		String channel = scheduleCypher.getChannel();
		String Return = scheduleCypher.getReturn();

		if (scheduleCypher.getqueryType() == 1
				|| scheduleCypher.getqueryType() == 2) {
			queryCypher = "Start n=node:node_auto_index";
			queryCypher += "(name='" + name + "')";
			queryCypher += " where n.startTime <= " + getTimeCode(startTime)
					+ "";
			queryCypher += " and n.finishTime>" + getTimeCode(finishTime);
			queryCypher += " and n.timePublish = '" + timePublish + "'";
			queryCypher += " and n.key = '" + channel + "'";
		}
		if (scheduleCypher.getqueryType() == 3) {
			queryCypher = "Start n=node:node_auto_index";
			queryCypher += "(name='" + name + "')";
			queryCypher += " where n.startTime <= " + getTimeCode(startTime)
					+ "";
			queryCypher += " and n.finishTime>" + getTimeCode(finishTime);
			queryCypher += " and n.timePublish = '" + timePublish + "'";
		}
		if (scheduleCypher.getqueryType() == 4) {
			queryCypher = "Start n=node:node_auto_index";
			queryCypher += "(name='" + name + "')";
			queryCypher += " where n.startTime <= " + getTimeCode(startTime)
					+ "";
			queryCypher += " and n.finishTime>" + getTimeCode(finishTime);
			queryCypher += " and n.timePublish = '" + getDayNow() + "'";

		}
		if (scheduleCypher.getqueryType() == 5) {
			queryCypher = "Start n=node:node_auto_index";
			queryCypher += "(name='" + name + "')";
			queryCypher += " where n.finishTime >= " + getTimeCode(startTime)
					+ "";
			queryCypher += " and n.timePublish = '" + getDayNow() + "'";
		}
		if (scheduleCypher.getqueryType() == 6) {
			queryCypher = "Start n=node:node_auto_index";
			queryCypher += "(name='" + name + "')";
			queryCypher += " where n.key = '" + channel + "'";
			queryCypher += " and n.timePublish = '" + getDayNow() + "'";
		}
		if (scheduleCypher.getqueryType() == 7) {
			queryCypher = "Start n=node:node_auto_index";
			queryCypher += "(name='" + name + "')";
			queryCypher += " where n.finishTime >= " + getTimeCode(startTime)
					+ "";
			queryCypher += " and n.key = '" + channel + "'";
			queryCypher += " and n.timePublish = '" + getDayNow() + "'";
		}
		if (scheduleCypher.getqueryType() == 8) {
			queryCypher = "Start n=node:node_auto_index";
			queryCypher += "(name='" + name + "')";
			queryCypher += " where n.finishTime >= " + getTimeCode(startTime)
					+ "";
			queryCypher += " and n.key = '" + channel + "'";
			queryCypher += " and n.timePublish = '" + timePublish + "'";
		}
		if (scheduleCypher.getqueryType() == 9) {
			queryCypher = "Start n=node:node_auto_index";
			queryCypher += "(name='" + name + "')";
			queryCypher += " where n.timePublish = '" + timePublish + "'";
			if (!kind.equalsIgnoreCase("null")) {
				queryCypher += " and n.key = '" + channel + "'";
			}
		}
		if (scheduleCypher.getqueryType() == 10) {
			queryCypher = "Start n=node:node_auto_index";
			queryCypher += "(key='" + channel + "')";
			queryCypher += " where n.finishTime >= " + getTimeCode(startTime)
					+ "";
			queryCypher += " and n.timePublish = '" + timePublish + "'";
		}
		if (scheduleCypher.getqueryType() == 11) {
			queryCypher = "Start n=node:node_auto_index";
			queryCypher += "(key='" + channel + "')";
			queryCypher += " where n.finishTime >= " + getTimeCode(startTime)
					+ "";
			queryCypher += " and n.timePublish = '" + getDayNow() + "'";
		}
		if (scheduleCypher.getqueryType() == 12) {
			queryCypher = "Start n=node:node_auto_index";
			queryCypher += "(key='" + channel + "')";
			queryCypher += " where n.timePublish = " + timePublish + "";
		}
		if (scheduleCypher.getqueryType() == 13) {
			queryCypher = "Start n=node:node_auto_index";
			queryCypher += "(name='" + name + "')";
			queryCypher += " where n.timePublish = '" + getDayNow() + "'";
			queryCypher += " and n.finishTime>" + getTimeNow();
			queryCypher += " or n.timePublish = '" + getNextDay() + "'";
		}
		if (scheduleCypher.getqueryType() == 14) {
			queryCypher = "Start n=node:node_auto_index";
			queryCypher += "(key='" + channel + "')";
			queryCypher += " where n.startTime <= " + getTimeCode(startTime)
					+ "";
			queryCypher += " and n.finishTime>" + getTimeCode(finishTime);
			queryCypher += " and n.timePublish = '" + getDayNow() + "'";
			if (!kind.equalsIgnoreCase("null")) {
				queryCypher += " and n.kind = '" + kind + "'";
			}
		}
		if (scheduleCypher.getqueryType() == 15) {
			queryCypher = "Match (n)";
			queryCypher += "";
			queryCypher += " where n.startTime <= " + getTimeCode(startTime)
					+ "";
			queryCypher += " and n.finishTime>" + getTimeCode(finishTime);
			queryCypher += " and n.timePublish = '" + timePublish + "'";
			if (!kind.equalsIgnoreCase("null")) {
				queryCypher += " and n.kind = '" + kind + "'";
			}
		}
		if (scheduleCypher.getqueryType() == 16) {
			queryCypher = "Match (n)";
			queryCypher += "";
			queryCypher += " where n.startTime <= " + getTimeCode(startTime)
					+ "";
			queryCypher += " and n.timePublish = '" + timePublish + "'";
			if (!kind.equalsIgnoreCase("null")) {
				queryCypher += " and n.kind = '" + kind + "'";
			}
		}
		if (scheduleCypher.getqueryType() == 17) {
			queryCypher = "Start n=node:node_auto_index";
			queryCypher += "(key='" + channel + "')";
			queryCypher += " where n.finishTime>" + getTimeCode(startTime);
			queryCypher += " and n.timePublish = '" + timePublish + "'";
			if (!kind.equalsIgnoreCase("null")) {
				queryCypher += " and n.kind = '" + kind + "'";
			}
		}
		if (scheduleCypher.getqueryType() == 18) {
			queryCypher = "Start n=node:node_auto_index";
			queryCypher += "(key='" + channel + "')";
			queryCypher += " where n.finishTime>" + getTimeCode(startTime);
			if (!kind.equalsIgnoreCase("null")) {
				queryCypher += " and n.kind = '" + kind + "'";
			}
		}
		if (scheduleCypher.getqueryType() == 19) {
			queryCypher = "Start n=node:node_auto_index";
			queryCypher += "(key='" + channel + "')";
			queryCypher += " where n.timePublish = '" + timePublish + "'";
			if (!kind.equalsIgnoreCase("null")) {
				queryCypher += " and n.kind = '" + kind + "'";
			}
		}
		if (scheduleCypher.getqueryType() == 20) {
			queryCypher = "Start n=node:node_auto_index";
			queryCypher += "(key='" + channel + "')";
			queryCypher += " where n.startTime <= " + getTimeCode(startTime)
					+ "";
			queryCypher += " and n.finishTime>" + getTimeCode(finishTime);
			queryCypher += " and n.timePublish = '" + getDayNow() + "'";
			if (!kind.equalsIgnoreCase("null")) {
				queryCypher += " and n.kind = '" + kind + "'";
			}
		}
		if (scheduleCypher.getqueryType() == 21) {
			queryCypher = "Match (n)";
			queryCypher += " where n.startTime <= " + getTimeCode(startTime)
					+ "";
			queryCypher += " and n.finishTime>" + getTimeCode(finishTime);
			queryCypher += " and n.timePublish = '" + timePublish + "'";
			if (!kind.equalsIgnoreCase("null")) {
				queryCypher += " and n.kind = '" + kind + "'";
			}
		}
		if (scheduleCypher.getqueryType() == 22) {
			queryCypher = "Match (n)";
			queryCypher += " where n.finishTime > " + getTimeCode(startTime)
					+ "";
			queryCypher += " and n.timePublish = '" + timePublish + "'";
			if (!kind.equalsIgnoreCase("null")) {
				queryCypher += " and n.kind = '" + kind + "'";
			}
		}
		if (scheduleCypher.getqueryType() == 23) {
			queryCypher = "Start n=node:node_auto_index";
			queryCypher += "(name='" + name + "')";
			queryCypher += " where n.timePublish = '" + timePublish + "'";
		}
		if (scheduleCypher.getqueryType() == 0) {
			queryCypher = "Start n=node:node_auto_index";
			if (!kind.equalsIgnoreCase("null")) {
				queryCypher += "(name='" + name + "')";
			} else
				queryCypher += "(key='VTV1')";
			queryCypher += " where n.finishTime >= " + getTimeNow() + "";
			queryCypher += " and n.timePublish = '" + getDayNow() + "'";
		}
		queryCypher += queryReturn;
		System.out.println("query"+queryCypher);
		return queryCypher;
	}

	private static String getAnswer(String request) {
		URL requestUrl;
		String sayBot = "";
		String displayBot = "";
		try {
			requestUrl = new URL(urlAIML + URLEncoder.encode(request, "UTF-8"));
			//System.out.println(requestUrl);
			URLConnection conn = requestUrl.openConnection();
			//System.out.println(conn);
			// InputStream in = new BufferedInputStream(conn.getInputStream());

			BufferedReader in = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String inputText;
			while ((inputText = in.readLine()) != null) {
				if (!isJSONValid(inputText)) {
					displayBot = sayBot = "Opps!";
					break;
				}
				JSONObject resultJSON = new JSONObject(inputText);
				String response = resultJSON.getString("response");
				if (!isJSONValid(response)) {
					displayBot = sayBot = response;
					break;
				}
				JSONObject responseJSON = new JSONObject(response);
				String key = responseJSON.getString("key");
				String result = responseJSON.getString("result");
				String detail = responseJSON.getString("detail");
				if (key.equalsIgnoreCase("cypher")) {
					if (!isJSONValid(response)) {
						sayBot = "NotJSON" + detail;
						break;
					}
					ScheduleCypher schedule = parseToScheduleCypher(detail);
					String query = queryCypher(schedule);
					//System.out.println("query: " + query);
					ArrayList<ScheduleDetail> arrScheduleDetail = cypherParse(query);
					// ROBOTSAY
					if (arrScheduleDetail.size() == 0) {
						if (schedule.getReturn().equalsIgnoreCase("5")) {
							displayBot = sayBot = "Không, "
									+ schedule.getProgram()
									+ " phát sóng vào thời điểm khác";
						} else {
							if (schedule.getProgram().equalsIgnoreCase("null")) {
								displayBot = sayBot = "Tôi không có lịch phát sóng vào thời gian này";
							} else
								sayBot = displayBot = schedule.getProgram()
										+ " không phát sóng vào thời gian này";
						}
					}
					if (arrScheduleDetail.size() > 1)
						displayBot += "Bạn có thể xem: \r\n";
					for (int j = 0; j < arrScheduleDetail.size(); j++) {
						ScheduleDetail sch = arrScheduleDetail.get(j);

						if (sch.getTimePublish().equalsIgnoreCase(getDayNow())) {
							if (!schedule.getStartTime().equalsIgnoreCase(
									"null")) {
								if (Math.abs(Integer.parseInt(schedule
										.getStartTime())
										- Integer.parseInt(sch.getStartTime())) >= 0
										&& Math.abs(Integer.parseInt(schedule
												.getStartTime())
												- Integer.parseInt(sch
														.getStartTime())) < 15) {
									sayBot = sch.getRobotSay(schedule);
									displayBot = sch.getRobotDislay(schedule)
											+ "\r\n";
									break;
								}
							} else {
								if (Integer.parseInt(sch.getFinishTime()) > getTimeNow()) {
									sayBot = sch.getRobotSay(schedule);
									displayBot = sch.getRobotDislay(schedule)
											+ "\r\n";
									break;
								}
							}
							sayBot += " " + sch.getRobotSay(schedule) + ".";
							displayBot += sch.getRobotDislay(schedule) + "\r\n";
						} else {
							if (!schedule.getStartTime().equalsIgnoreCase(
									"null")) {
								if (Math.abs(Integer.parseInt(schedule
										.getStartTime())
										- Integer.parseInt(sch.getStartTime())) >= 0
										&& Math.abs(Integer.parseInt(schedule
												.getStartTime())
												- Integer.parseInt(sch
														.getStartTime())) < 15) {
									sayBot = sch.getRobotSay(schedule);
									displayBot = sch.getRobotDislay(schedule)
											+ "\r\n";
									break;
								}
							}
							sayBot += " " + sch.getRobotSay(schedule) + ".";
							displayBot += sch.getRobotDislay(schedule) + "\r\n";
						}
						if (j > 4)
							break;
					}
					break;
				}
			}
			in.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			sayBot = "Opps! ";
			e.printStackTrace();
		}
		return displayBot;
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		int i = 0 ;
		FileOutputStream out = new FileOutputStream("result.txt");
		// Tạo thiết bị viết
		PrintWriter output = new PrintWriter(out, true);// auto flush
		List< String > list = fileReader( "questions_tv" );
		for(String query : list) {
			System.out.println("query[" + i++ +"] = " +query);
			output.println(query);
			output.println(getAnswer(query));
			output.println("==================================================");
			System.out.println("===============================================");
		}
		
	}
	 
	public static List<String> fileReader(String path) {
		List<String> list = new ArrayList<String>();
		try {
			FileInputStream is = new FileInputStream(path);
			Scanner input = new Scanner(is, "UTF-8");
			while (input.hasNextLine()) {
				String line = input.nextLine();
				if(line.equalsIgnoreCase(""));
					list.add(line.trim());
			}
			is.close();
			input.close();
			
		} catch (IOException e) {
			System.err.println(e.getMessage());
			//e.printStackTrace();
		}
		return list ;
	}
	
	public static void Write2File(String fileName, String query ,String answer)
			throws IOException {
		// Tạo luồng xuất
		FileOutputStream out = new FileOutputStream(fileName);
		// Tạo thiết bị viết
		PrintWriter output = new PrintWriter(out, true);// auto flush
		// ghi 1 chuỗi ra file
		output.println(query);
		output.println(answer);
		output.println("==================================================");
		// sau khi làm việc xong, nhớ đóng luồng
	}
}
