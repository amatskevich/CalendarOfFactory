package by.matskevich.calendaroffactory;

import android.graphics.Color;
import by.matskevich.calendaroffactory.util.Constants;

public enum StateShiftDay implements Statable {
	SECOND_DAY_OFF("2-й ВЫХОДНОЙ", "*", Constants.COLOR_WHITE, 0, "Выходной"),

	FIRST_WORK("1-й ДЕНЬ", "8", Constants.COLOR_8, 11.25, "Рабочий"),

	SECOND_WORK("2-й ДЕНЬ", "8", Constants.COLOR_8, 11.25, "Рабочий"),

	FIRST_DAY_OFF("1-й ВЫХОДНОЙ", "*", Constants.COLOR_WHITE, 0, "Выходной");

	final String state;
	final String sign;
	final int color;
	final double hours;
	final String description;

	StateShiftDay(String state, String sign, String color, double workHours, String description) {
		this.state = state;
		this.sign = sign;
		this.color = Color.parseColor(color);
		this.hours = workHours;
		this.description = description;
	}

	@Override
	public String getState() {
		return state;
	}

	@Override
	public String getStatSign() {
		return sign;
	}

	@Override
	public int getColor() {
		return color;
	}

	@Override
	public double getWorkHours() {
		return hours;
	}

	@Override
	public int getNormalHours() {
		return 8;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public Statable next() {
		Statable res = null;
		switch (this) {
		case FIRST_WORK:
			res = SECOND_WORK;
			break;
		case SECOND_WORK:
			res = FIRST_DAY_OFF;
			break;
		case FIRST_DAY_OFF:
			res = SECOND_DAY_OFF;
			break;
		case SECOND_DAY_OFF:
			res = FIRST_WORK;
			break;
		}
		return res;
	}

	@Override
	public Statable before() {
		Statable res = null;
		switch (this) {
		case SECOND_DAY_OFF:
			res = FIRST_DAY_OFF;
			break;
		case FIRST_DAY_OFF:
			res = SECOND_WORK;
			break;
		case SECOND_WORK:
			res = FIRST_WORK;
			break;
		case FIRST_WORK:
			res = SECOND_DAY_OFF;
			break;
		}
		return res;
	}

}
