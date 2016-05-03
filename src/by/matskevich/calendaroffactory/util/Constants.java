package by.matskevich.calendaroffactory.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public interface Constants {

	public static String EXTRA_DATE = "time";
	public static String EXTRA_SHIFT = "shift";
	public static DateFormat FORMATTER_DATE = new SimpleDateFormat("dd.MM.yyyy");
	public static final String[] WEEK_DAYS = { "Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс" };
}
