package fpt.qa.aiml.util;

public class ScheduleDetail {
	private String timePublish; // date publish, ex: monday,7/1/2014
	private String startTime; // 00:12
	private String finishTime; // 02:30
	private String kind; // sport, movie, music, game show
	private String program; // Phim Viet Nam
	private String detail; // phim Ma lÃ ng
	private String description; // phim ráº¥t hay
	private String channel; // phim ráº¥t hay

	public ScheduleDetail() {

	}

	public ScheduleDetail(String timePublish, String startTime,
			String finishTime, String kind, String program, String detail,
			String description, String channel) {
		this.timePublish = timePublish;
		this.startTime = startTime;
		this.finishTime = finishTime;
		this.kind = kind;
		this.program = program;
		this.detail = detail;
		this.description = description;
		this.channel = channel;
	}

	public ScheduleDetail(String timePublish, String startTime,
			String finishTime, String kind, String program, String detail,
			String channel) {
		this.timePublish = timePublish;
		this.startTime = startTime;
		this.finishTime = finishTime;
		this.kind = kind;
		this.program = program;
		this.detail = detail;
		this.channel = channel;
	}

	public String getTimePublish() {
		return timePublish;
	}

	public void setTimePublish(String timePublish) {
		this.timePublish = timePublish;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getProgram() {
		return program;
	}

	public void setProgram(String program) {
		this.program = program;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String Channel) {
		this.kind = Channel;
	}

	protected String convertTime(String intTime) {
		String stringTime = "";
		// 930
		if (intTime.length() <= 3) {
			stringTime += intTime.substring(0, 1) + " giờ "
					+ intTime.substring(1) + " phút";
			// Log.d("", "stringTime3=" + stringTime);
			// 1130
		} else if (intTime.length() > 3) {
			stringTime += intTime.substring(0, 2) + " giờ "
					+ intTime.substring(2, 4) + " phút";
			// Log.d("", "stringTime4=" + stringTime);
		}
		return stringTime;
	}

	public String cutText(String text) {
		String ouput = "";
		if (text.contains("VTV")) {
			// String[] result = text.split("\\w");
			// for ( int x=0; x<result.length; x++) {
			// text += result[x] + " ";
			// }
			ouput = text.substring(0, 1);
			ouput += " " + text.substring(1, 2);
			ouput += " " + text.substring(2, 3);
			ouput += " " + text.substring(3, 4);
		}
		if (text.contains("HBO")) {
			ouput = text.substring(0, 1);
			ouput += " " + text.substring(1, 2);
			ouput += " " + text.substring(2, 3);

		}
		return ouput;
	}

	public String getRobotSay(ScheduleCypher schedule) {
		int type = Integer.parseInt(schedule.getReturn());
		String say = "Tôi hiện không có lịch phát sóng vào thời điểm này";
		if (this.kind.equalsIgnoreCase("MOVIE")) {
			if (type == 1) {
				say = "Bây giờ trên " + cutText(this.channel)
						+ " đang phát sóng phim " + this.program.toUpperCase()
						+ " " + this.detail;
			}
			if (type == 2) {
				say = convertTime(this.startTime) + " trên "
						+ cutText(this.channel) + " phát sóng phim "
						+ this.program.toUpperCase() + " " + this.detail
						+ " và kết thúc vào " + convertTime(this.finishTime);
			}
			if (type == 3) {
				say = "Phim " + this.program.toUpperCase() + " " + this.detail
						+ " phát sóng lúc " + convertTime(startTime) + " trên "
						+ cutText(this.channel) + " và kết thúc vào "
						+ convertTime(this.finishTime);
			}
			if (type == 4) {
				say = "Phim " + this.program.toUpperCase() + " " + this.detail
						+ " phát sóng lúc " + convertTime(startTime) + " ngày "
						+ this.timePublish + " trên " + cutText(this.channel)
						+ " và kết thúc vào " + convertTime(this.finishTime);
			}
			if (type == 5) {
				say = "Có. Phim " + this.program.toUpperCase() + " "
						+ this.detail + " phát sóng lúc "
						+ convertTime(startTime) + " ngày " + this.timePublish
						+ " trên " + cutText(this.channel)
						+ " và kết thúc vào " + convertTime(this.finishTime);
			}
			if (type == 6) {
				say = "Phim " + this.program.toUpperCase() + " " + this.detail
						+ " phát sóng lúc " + convertTime(startTime) + " trên "
						+ cutText(this.channel) + " và kết thúc vào "
						+ convertTime(this.finishTime);
			}
			if (type == 7) {
				say = "Lịch phát sóng";
			}
			if (type == 8) {

			}
			if (type == 0) {
				say = "Phim " + this.program.toUpperCase() + " " + this.detail
						+ " phát sóng lúc " + convertTime(startTime) + " trên "
						+ cutText(this.channel) + " và kết thúc vào "
						+ convertTime(this.finishTime);
			}
			return say;
		}
		if (type == 1) {
			say = "Bây giờ trên " + cutText(this.channel)
					+ " đang phát sóng chương trình   "
					+ this.program.toUpperCase();
		}
		if (type == 2) {
			say = convertTime(this.startTime) + " trên "
					+ cutText(this.channel) + " phát sóng chương trình "
					+ this.program.toUpperCase() + " và kết thúc vào "
					+ convertTime(this.finishTime);
		}
		if (type == 3) {
			say = "Chương trình " + this.program.toUpperCase()
					+ " phát sóng lúc " + convertTime(startTime) + " trên "
					+ cutText(this.channel) + " và kết thúc vào "
					+ convertTime(this.finishTime);
		}
		if (type == 4) {
			say = "Chương trình " + this.program.toUpperCase()
					+ " phát sóng lúc " + convertTime(startTime) + " ngày "
					+ this.timePublish + " trên " + cutText(this.channel)
					+ " và kết thúc vào " + convertTime(this.finishTime);
		}
		if (type == 5) {
			say = "Có. Chương trình " + this.program.toUpperCase() + "-"
					+ this.detail + " phát sóng lúc " + convertTime(startTime)
					+ " ngày " + this.timePublish + " trên "
					+ cutText(this.channel) + " và kết thúc vào "
					+ convertTime(this.finishTime);
		}
		if (type == 6) {
			say = "Chương trình " + this.program.toUpperCase() + "-"
					+ this.detail + " phát sóng lúc " + convertTime(startTime)
					+ " trên " + cutText(this.channel) + " và kết thúc vào "
					+ convertTime(this.finishTime);
		}
		if (type == 7) {
			say = "Lịch phát sóng";
		}
		if (type == 8) {

		}
		if (type == 0) {
			say = "Chương trình " + this.program.toUpperCase() + " "
					+ this.detail + " phát sóng lúc " + convertTime(startTime)
					+ " trên " + cutText(this.channel) + " và kết thúc vào "
					+ convertTime(this.finishTime);
		}

		return say;
	}

	public String getRobotDislay(ScheduleCypher schedule) {
		int type = Integer.parseInt(schedule.getReturn());
		String say = this.program.toUpperCase() + " " + this.detail
				+ " phát sóng lúc " + convertTime(startTime) + " trên "
				+ this.channel + " và kết thúc vào "
				+ convertTime(this.finishTime);
		if (this.kind.equalsIgnoreCase("MOVIE")) {
			if (type == 1) {
				say = "Bây giờ trên " + this.channel + " đang phát sóng phim "
						+ this.program.toUpperCase() + " " + this.detail
						+ " và kết thúc vào " + convertTime(this.finishTime);
			}
			if (type == 2) {
				say = convertTime(this.startTime) + " trên " + this.channel
						+ " phát sóng phim " + this.program.toUpperCase() + " "
						+ this.detail + " và kết thúc vào "
						+ convertTime(this.finishTime);
			}
			if (type == 3) {
				say = "Phim " + this.program.toUpperCase() + " " + this.detail
						+ " phát sóng lúc " + convertTime(startTime) + " trên "
						+ this.channel + " và kết thúc vào "
						+ convertTime(this.finishTime);
			}
			if (type == 4) {
				say = "Phim " + this.program.toUpperCase() + " " + this.detail
						+ " phát sóng lúc " + convertTime(startTime) + " ngày "
						+ this.timePublish + " trên " + this.channel
						+ " và kết thúc vào " + convertTime(this.finishTime);
			}
			if (type == 5) {
				say = "Có. Phim " + this.program.toUpperCase() + " "
						+ this.detail + " phát sóng lúc "
						+ convertTime(startTime) + " ngày " + this.timePublish
						+ " trên " + this.channel + " và kết thúc vào "
						+ convertTime(this.finishTime);
			}
			if (type == 6) {
				say = "Phim " + this.program.toUpperCase() + " " + this.detail
						+ " phát sóng lúc " + convertTime(startTime) + " trên "
						+ this.channel + " và kết thúc vào "
						+ convertTime(this.finishTime);
			}
			if (type == 7) {
				say = "Lịch phát sóng";
			}
			if (type == 8) {

			}
			if (type == 0) {
				say = "Phim " + this.program.toUpperCase() + " " + this.detail
						+ " phát sóng lúc " + convertTime(startTime) + " trên "
						+ this.channel + " và kết thúc vào "
						+ convertTime(this.finishTime);
			}
			return say;
		}
		if (type == 1) {
			say = "Bây giờ trên " + this.channel
					+ " đang phát sóng chương trình "
					+ this.program.toUpperCase() + " " + this.detail;
		}
		if (type == 2) {
			say = convertTime(this.startTime) + " trên " + this.channel
					+ " phát sóng chương trình " + this.program.toUpperCase()
					+ " " + this.detail + " và kết thúc vào "
					+ convertTime(this.finishTime);
		}
		if (type == 3) {
			say = "Chương trình " + this.program.toUpperCase() + " "
					+ this.detail + " phát sóng lúc " + convertTime(startTime)
					+ " trên " + this.channel + " và kết thúc vào "
					+ convertTime(this.finishTime);
		}
		if (type == 4) {
			say = "Chương trình " + this.program.toUpperCase() + " "
					+ this.detail + " phát sóng lúc " + convertTime(startTime)
					+ " ngày " + this.timePublish + " trên " + this.channel
					+ " và kết thúc vào " + convertTime(this.finishTime);
		}
		if (type == 5) {
			say = "Có. " + this.program.toUpperCase() + " " + this.detail
					+ " phát sóng lúc " + convertTime(startTime) + " ngày "
					+ this.timePublish + " trên " + this.channel
					+ " và kết thúc vào " + convertTime(this.finishTime);
		}
		if (type == 6) {
			say = "Chương trình" + this.program.toUpperCase() + " "
					+ this.detail + " phát sóng lúc " + convertTime(startTime)
					+ " trên " + this.channel + " và kết thúc vào "
					+ convertTime(this.finishTime);
		}
		if (type == 7) {
			say = "Lịch phát sóng";
		}
		if (type == 8) {

		}
		if (type == 0) {
			say = "Chương trình" + this.program.toUpperCase() + " "
					+ this.detail + " phát sóng lúc " + convertTime(startTime)
					+ " trên " + this.channel + " và kết thúc vào "
					+ convertTime(this.finishTime);
		}

		return say;
	}
}
