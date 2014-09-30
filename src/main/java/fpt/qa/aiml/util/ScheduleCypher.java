package fpt.qa.aiml.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ScheduleCypher {
	private String timePublish; // date publish, ex: monday,7/1/2014
	private String startTime; // 00:12
	private String finishTime; // 02:30
	private String kind; // sport, movie, music, game show
	private String program; // Phim Viet Nam
	private String detail; // phim Ma lÃ ng
	// private String description; // phim ráº¥t hay
	private String channel; // phim ráº¥t hay
	private String Return;
	private int queryType;

	public ScheduleCypher() {
		// convertTime();
	}

	public ScheduleCypher(String timePublish, String startTime,
			String finishTime, String kind, String program, String detail,
			String channel, String Return) {
		this.timePublish = timePublish;
		this.startTime = getTimeCode(startTime);
		this.finishTime = getTimeCode(finishTime);
		this.kind = kind;
		this.program = program;
		this.detail = detail;
		// this.description = description;
		this.channel = channel.toUpperCase();
		this.Return = Return;
		this.queryType = getINTCypher();
		this.convertTime();
	}

	public String getTimePublish() {
		return timePublish;
	}

	public void setTimePublish(String timePublish) {
		this.timePublish = timePublish;
	}

	public String getStartTime() {
		try {
			return "" + Integer.parseInt(startTime);
		} catch (Exception e) {
			// TODO: handle exception
			return startTime;
		}
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getFinishTime() {
		try {
			return "" + Integer.parseInt(finishTime);
		} catch (Exception e) {
			// TODO: handle exception
			return finishTime;
		}
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

	protected int getTimeNow() {
		int timeStamp = Integer.parseInt(new SimpleDateFormat("HHmm")
				.format(Calendar.getInstance().getTime()));
		return timeStamp;
	}

	public String getBayGio() {
		Calendar cal = Calendar.getInstance();
		String timeStamp = String.valueOf(cal.get(Calendar.HOUR_OF_DAY))
				+ String.valueOf(cal.get(Calendar.MINUTE));
		return timeStamp;
	}

	public String getNextDay() {
		Calendar cal = Calendar.getInstance();
		int nextDay = cal.get(Calendar.DATE) + 1;
		int month = cal.get(Calendar.MONTH) +1;
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

	protected String getDayNow() {
		String timeStamp = new SimpleDateFormat("dd/MM/yyyy").format(Calendar
				.getInstance().getTime());
		return timeStamp;
	}

	protected String getTimeCode(final String startTime) {
		String timeStamp = startTime;
		if (!startTime.equalsIgnoreCase("null")) {
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
			if (startTime.equalsIgnoreCase("đêm nay")) {
				timeStamp = "2100";
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

	private void convertTime() {
		// if (!startTime.equalsIgnoreCase("null")) {
		// if (startTime.equalsIgnoreCase("bây giờ")) {
		// startTime = new
		// SimpleDateFormat("HHmm").format(Calendar.getInstance().getTime());
		// }
		// if (startTime.equalsIgnoreCase("ngay bây giờ")) {
		// startTime = new
		// SimpleDateFormat("HHmm").format(Calendar.getInstance().getTime());
		// }
		// if (startTime.equalsIgnoreCase("hiện giờ")) {
		// startTime = new
		// SimpleDateFormat("HHmm").format(Calendar.getInstance().getTime());
		// }
		// if (startTime.equalsIgnoreCase("chiều nay")) {
		// timePublish = getDayNow();
		// startTime = "1400";
		// finishTime = "1530";
		// }
		// if (startTime.equalsIgnoreCase("trưa nay")) {
		// timePublish = getDayNow();
		// startTime = "1100";
		// finishTime = "1300";
		// }
		// if (startTime.equalsIgnoreCase("tối nay")) {
		// timePublish = getDayNow();
		// startTime = "1900";
		// finishTime = "2339";
		// }
		// if (startTime.equalsIgnoreCase("chiều nay")) {
		// timePublish = getDayNow();
		// startTime = "1400";
		// finishTime = "1700";
		// }
		// }
		if (!timePublish.equalsIgnoreCase("null")) {
			if (!startTime.equalsIgnoreCase("null")) {
				if (timePublish.equalsIgnoreCase("ngày mai")) {
					timePublish = getNextDay();
				}
				if (timePublish.equalsIgnoreCase("sáng mai")) {
					timePublish = getNextDay();
				}
				if (timePublish.equalsIgnoreCase("trưa mai")) {
					timePublish = getNextDay();
					int temp = Integer.parseInt(startTime);
					if (temp < 300)
						temp = temp + 1200;
					startTime = String.valueOf(temp);
					finishTime = startTime;

				}
				if (timePublish.equalsIgnoreCase("chiều mai")) {
					timePublish = getNextDay();
					int temp = Integer.parseInt(startTime);
					if (600 < temp && temp < 1200)
						temp = temp + 1200;
					startTime = String.valueOf(temp);
					finishTime = startTime;
				}
				if (timePublish.equalsIgnoreCase("tối mai")) {
					timePublish = getNextDay();
					int temp = Integer.parseInt(startTime);
					if (600 < temp && temp < 1200)
						temp = temp + 1200;
					startTime = String.valueOf(temp);
					finishTime = startTime;
				}
				if (timePublish.equalsIgnoreCase("trưa nay")) {
					timePublish = getDayNow();
					int temp = Integer.parseInt(startTime);
					if (temp < 300)
						temp = temp + 1200;
					startTime = String.valueOf(temp);
					finishTime = startTime;

				}
				if (timePublish.equalsIgnoreCase("tối nay")) {
					timePublish = getDayNow();
					int temp = Integer.parseInt(startTime);
					if (600 < temp && temp < 1200)
						temp = temp + 1200;
					startTime = String.valueOf(temp);
					finishTime = startTime;
				}
				if (timePublish.equalsIgnoreCase("đêm nay")) {
					timePublish = getDayNow();
					int temp = Integer.parseInt(startTime);
					if (600 < temp && temp < 1200)
						temp = temp + 1200;
					startTime = String.valueOf(temp);
					finishTime = startTime;
				}
				if (timePublish.equalsIgnoreCase("chiều nay")) {
					timePublish = getDayNow();
					int temp = Integer.parseInt(startTime);
					if (temp < 700)
						temp = temp + 1200;
					startTime = String.valueOf(temp);
					finishTime = startTime;
				}

			} else {
				if (timePublish.equalsIgnoreCase("ngày mai")) {
					timePublish = getNextDay();
				}
				if (timePublish.equalsIgnoreCase("sáng mai")) {
					timePublish = getNextDay();
				}
				if (timePublish.equalsIgnoreCase("trưa mai")) {
					timePublish = getNextDay();
					startTime = "1100";
					finishTime = "1300";

				}
				if (timePublish.equalsIgnoreCase("chiều mai")) {
					timePublish = getNextDay();
					startTime = "1400";
					finishTime = "1700";

				}
				if (timePublish.equalsIgnoreCase("tối mai")) {
					timePublish = getNextDay();
					startTime = "1800";
					finishTime = "2359";
				}
				if (timePublish.equalsIgnoreCase("trưa nay")) {
					timePublish = getDayNow();
					startTime = "1100";
					finishTime = "1300";

				}
				if (timePublish.equalsIgnoreCase("tối nay")) {
					timePublish = getDayNow();
					startTime = "1800";
					finishTime = "2359";
				}
				if (timePublish.equalsIgnoreCase("chiều nay")) {
					timePublish = getDayNow();
					startTime = "1400";
					finishTime = "1700";
				}
				if (timePublish.equalsIgnoreCase("đêm nay")) {
					timePublish = getDayNow();
					startTime = "2100";
					finishTime = "2400";
				}
			}
		}
	}

	// public String getDescription() {
	// return description;
	// }
	// public void setDescription(String description) {
	// this.description = description;
	// }
	public String getChannel() {
		return channel;
	}

	public void setChannel(String Channel) {
		this.channel = Channel.toUpperCase();
	}

	public String getReturn() {
		return Return;
	}

	public void setReturn(String Return) {
		this.Return = Return;
	}

	public int getqueryType() {
		return queryType;
	}

	public void setqueryType(int queryType) {
		this.queryType = queryType;
	}

	public int getINTCypher() {
		int n, s, f, t, d, c, k;
		n = s = f = t = d = c = k = 0;
		if (!program.equalsIgnoreCase("null")) {
			n = 1;
		}
		if (!startTime.equalsIgnoreCase("null")) {
			s = 1;
		}
		if (!finishTime.equalsIgnoreCase("null")) {
			f = 1;
		}
		if (!timePublish.equalsIgnoreCase("null")) {
			t = 1;
		}
		if (!detail.equalsIgnoreCase("null")) {
			d = 1;
		}
		if (!channel.equalsIgnoreCase("null")) {
			c = 1;
		}
		if (!kind.equalsIgnoreCase("null")) {
			k = 1;
		}
		if (n == 1) {
			if (s == 1) {
				if (f == 1) {
					if (t == 1) {
						if (d == 1) {
							if (c == 1) {
								if (k == 0) {
									return 1;
								}
							}
						} else {
							if (c == 1) {
								return 2;
							} else {
								return 3;
							}
						}
					} else {
						return 4;
					}
				} else {
					if (t == 0) {
						if (d == 0) {
							if (c == 0) {
								if (k == 0) {
									return 5;
								}
							} else {
								return 7;
							}
						}
					} else {
						if (t == 1) {
							if (d == 0) {
								if (c == 1) {
									if (k == 0) {
										return 8;
									}
								}
							}
						}
					}
				}
			} else {
				if (f == 0) {
					if (t == 1) {
						if (d == 0) {
							if (c == 1) {
								return 9;
							}
							else {
								if (k == 0) {
									return 23;
								}
							}
						}
					} else {
						if (d == 0) {
							if (c == 1) {
								if (k == 0) {
									return 6;
								}
							} else {
								if (k == 0) {
									return 13;
								}
							}
						}
					}
				}
			}

		} else {
			if (s == 1) {
				if (f == 1) {
					if (t == 1) {
						if (d == 0) {
							if (c == 1) {
								if (k == 1) {
									return 20;
								} else {
									return 14;
								}
							} else {
								if (k == 1) {
									return 21;
								} else {
									return 15;
								}
							}
						}
					}
				} else {
					if (t == 1) {
						if (d == 0) {
							if (c == 1) {
								if (k == 1) {
									return 17;
								} else {
									return 10;
								}
							} else {
								if (c == 0) {
									if (k == 1) {
										return 22;
									} else {
										return 16;
									}
								}
							}
						}
					} else {
						if (d == 0) {
							if (c == 1) {
								if (k == 1) {
									return 18;
								} else {
									return 11;

								}
							}
						}
					}
				}
			} else {
				if (f == 0) {
					if (t == 1) {
						if (d == 0) {
							if (c == 1) {
								if (k == 1) {
									return 19;
								} else {
									return 12;
								}
							}
						}
					}
				}

			}
		}
		return 0;
	}
}
