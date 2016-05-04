package by.matskevich.calendaroffactory;

import android.graphics.Color;
import by.matskevich.calendaroffactory.util.Constants;

public enum StateShiftDay implements Statable {
	SECOND_DAY_OFF("2-й ВЫХОДНОЙ", "*", Constants.COLOR_WHITE),

	FIRST_WORK("1-й ДЕНЬ", "8", Constants.COLOR_8),

	SECOND_WORK("2-й ДЕНЬ", "8", Constants.COLOR_8),

	FIRST_DAY_OFF("1-й ВЫХОДНОЙ", "*", Constants.COLOR_WHITE);

	final String state;
	final String sign;
	final int color;

	private StateShiftDay(String state, String sign, String color) {
		this.state = state;
		this.sign = sign;
		this.color = Color.parseColor(color);
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
