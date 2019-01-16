package org.zerock.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import org.springframework.stereotype.Component;

//시간관련한 클래스가 들어있는 곳들
@Component
public class TranDate {
	
	private static LocalDate ld = LocalDate.now();
	private static LocalTime lt = LocalTime.now();
	private static DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
	private static DateTimeFormatter time_formatter = DateTimeFormatter.ofPattern("HHmmss");
	
	public static String from_date() {
		LocalDate minus_date = ld.minusMonths(1);
		return date_formatter.format(minus_date).toString();
	}

	// 현재날짜
	public static String to_date() {
		return date_formatter.format(ld).toString();
	}

	// 현재시각
	public static String tran_date() {
		StringBuilder sb = new StringBuilder();
		sb.append(date_formatter.format(ld).toString());
		sb.append(time_formatter.format(lt).toString());
		return sb.toString();
	}
}
