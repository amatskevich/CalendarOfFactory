package by.matskevich.calendaroffactory.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public interface Constants {

	DateFormat FORMATTER_DATE = new SimpleDateFormat("dd.MM.yyyy");
	String[] WEEK_DAYS = { "Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс" };
	String[] WEEK_DAYS_FULL = { "Воскресенье", "Понедельник", "Вторник", "Среда",
			"Четверг","Пятница", "Суббота"};
	String COLOR_HEAD = "#F5F6CE";
	String COLOR_DAY = "#850D0D";
	String COLOR_8 = "#FFBABA";
	String COLOR_O = "#BBFFBA";
	String COLOR_20_24 = "#BACFFF";
	String COLOR_16 = "#F1BAFF";
	String COLOR_WHITE = "#FFFFFF";
	String COLOR_ROW1 = "#DFDFDF";
	String COLOR_ROW2 = "#FAFAFA";
}
