package mainmc.nothing00.utils;

import java.time.LocalDate;
import java.time.LocalTime;

import org.apache.commons.lang.NumberUtils;

@SuppressWarnings("deprecation")
public class Date {

	private String date;

	public Date() {
		this.date = LocalDate.now().getDayOfMonth() + "/" + LocalDate.now().getMonthValue() + "/"
				+ LocalDate.now().getYear() + " " + LocalTime.now().getHour() + ":" + LocalTime.now().getMinute() + ":"
				+ LocalTime.now().getSecond();
	}
	public Date(int day, int month, int year) {
		if (day > 0 && day < 32 && month > 0 && month < 13)
			this.date = day + "/" + month + "/" + year;
		else
			this.date = null;
	}

	public Date(int day, int month, int year, int h, int m, int s) {
		if (day > 0 && day < 32 && month > 0 && month < 13) {
			this.date = day + "/" + month + "/" + year;
			addTime(h, m, s);
		} else
			this.date = null;
	}

	public Date(String date) {
		this.date = date;
		if (!isValid())
			this.date = null;
	}

	public boolean isValid() {
		String[] split = this.date.split(" ");
		if (split.length != 2)
			return false;
		String[] date = split[0].split("/");
		if (date.length != 3)
			return false;
		String[] time = split[1].split(":");
		if (time.length != 3)
			return false;
		if (!NumberUtils.isNumber(date[0]) || !NumberUtils.isNumber(date[1]) || !NumberUtils.isNumber(date[2])
				|| !NumberUtils.isNumber(time[0]) || !NumberUtils.isNumber(time[1]) || !NumberUtils.isNumber(time[2]))
			return false;
		int day = Integer.parseInt(date[0]);
		int month = Integer.parseInt(date[1]);
		int h = Integer.parseInt(time[0]);
		int m = Integer.parseInt(time[1]);
		int s = Integer.parseInt(time[2]);
		return day > 0 && day < 32 && month > 0 && month < 13 && h >= 0 && h < 24 && m >= 0 && m < 60 && s >= 0
				&& s < 60;

	}

	public void addTime(int h, int m, int s) {
		if (h >= 0 && h < 24 && m >= 0 && m < 60 && s >= 0 && s < 60) {
			String hh = "", mm = "", ss = "";
			if (h < 10)
				hh = "0" + h;
			else
				hh = "" + h;
			if (m < 10)
				mm = "0" + m;
			else
				mm = "" + m;
			if (s < 10)
				ss = "0" + s;
			else
				ss = "" + s;
			this.date += " " + hh + ":" + mm + ":" + ss;
		}
	}

	public String toString() {
		return this.date;
	}

	public boolean isNull() {
		return this.date == null;
	}
	
	public int getDay() {
		return Integer.parseInt(this.date.split(" ")[0].split("/")[0]);
	}

	public int getMonth() {
		return Integer.parseInt(this.date.split(" ")[0].split("/")[1]);
	}

	public int getYear() {
		return Integer.parseInt(this.date.split(" ")[0].split("/")[2]);
	}

	public int getHour() {
		return Integer.parseInt(this.date.split(" ")[1].split(":")[0]);
	}

	public int getMinutes() {
		return Integer.parseInt(this.date.split(" ")[1].split(":")[1]);
	}

	public int getSeconds() {
		return Integer.parseInt(this.date.split(" ")[1].split(":")[2]);
	}

}
