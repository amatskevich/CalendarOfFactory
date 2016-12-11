package by.matskevich.calendaroffactory;

import android.graphics.Color;
import by.matskevich.calendaroffactory.util.Constants;

public enum StateShiftDay implements Statable {
	SECOND_DAY_OFF("2-й ВЫХОДНОЙ", "*", Constants.COLOR_WHITE, 0),

	FIRST_WORK("1-й ДЕНЬ", "8", Constants.COLOR_8, 11.25),

	SECOND_WORK("2-й ДЕНЬ", "8", Constants.COLOR_8, 11.25),

	FIRST_DAY_OFF("1-й ВЫХОДНОЙ", "*", Constants.COLOR_WHITE, 0);

	final String state;
	final String sign;
	final int color;
	final double hours;

	StateShiftDay(String state, String sign, String color, double workHours) {
		this.state = state;
		this.sign = sign;
		this.color = Color.parseColor(color);
		this.hours = workHours;
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
