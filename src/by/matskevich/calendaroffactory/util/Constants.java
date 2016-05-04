package by.matskevich.calendaroffactory.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public interface Constants {

	public static final String EXTRA_DATE = "time";
	public static final String EXTRA_SHIFT = "shift";
	public static final DateFormat FORMATTER_DATE = new SimpleDateFormat("dd.MM.yyyy");
	public static final String[] WEEK_DAYS = { "Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс" };
	public static final String COLOR_HEAD = "#F5F6CE";
	public static final String COLOR_DAY = "#850D0D";
	public static final String COLOR_8 = "#FFBABA";
	public static final String COLOR_O = "#BBFFBA";
	public static final String COLOR_20_24 = "#BACFFF";
	public static final String COLOR_16 = "#F1BAFF";
	public static final String COLOR_WHITE = "#FFFFFF";
	public static final String COLOR_ROW1 = "#DFDFDF";
	public static final String COLOR_ROW2 = "#FAFAFA";
}
