package by.matskevich.calendaroffactory;

import android.graphics.Color;
import by.matskevich.calendaroffactory.util.Constants;

public enum StateShift8 implements Statable {
	FIRST_NIGHT("1-я НОЧЬ", "24", Constants.COLOR_20_24, 0.5),

	SECOND_NIGHT("2-я НОЧЬ", "24", Constants.COLOR_20_24, 8.5),

	AFTER_NIGHT("ОТСЫПНОЙ", "O", Constants.COLOR_O, 8),

	DAY_OFF("ВЫХОДНОЙ", "*", Constants.COLOR_WHITE, 0),

	DAY_OFF_BEFORE_EVENING("ВЫХОДНОЙ", "*", Constants.COLOR_WHITE, 0),

	FIRST_EVENING("1-я С 4-х", "16", Constants.COLOR_16, 7.5),

	SECOND_EVENING("2-я С 4-х", "16", Constants.COLOR_16, 7.5),

	SHORT_DAY_OFF("ВЫХОДНОЙ", "*", Constants.COLOR_WHITE, 0),

	FIRST_MORNING("1-я С УТРА", "8", Constants.COLOR_8, 8),

	SECOND_MORNING("2-я С УТРА", "8", Constants.COLOR_8, 8);

	final String state;
	final String sign;
	final int color;
	final double hours;

	StateShift8(String state, String sign, String color, double workedHours) {
		this.state = state;
		this.sign = sign;
		this.color = Color.parseColor(color);
		this.hours = workedHours;
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
		return 7;
	}

	@Override
	public Statable next() {
		Statable res = null;
		switch (this) {
		case FIRST_MORNING:
			res = SECOND_MORNING;
			break;
		case SECOND_MORNING:
			res = FIRST_NIGHT;
			break;
		case FIRST_NIGHT:
			res = SECOND_NIGHT;
			break;
		case SECOND_NIGHT:
			res = AFTER_NIGHT;
			break;
		case AFTER_NIGHT:
			res = DAY_OFF;
			break;
		case DAY_OFF:
			res = DAY_OFF_BEFORE_EVENING;
			break;
		case DAY_OFF_BEFORE_EVENING:
			res = FIRST_EVENING;
			break;
		case FIRST_EVENING:
			res = SECOND_EVENING;
			break;
		case SECOND_EVENING:
			res = SHORT_DAY_OFF;
			break;
		case SHORT_DAY_OFF:
			res = FIRST_MORNING;
			break;
		}
		return res;
	}

	@Override
	public Statable before() {
		Statable res = null;
		switch (this) {
		case SHORT_DAY_OFF:
			res = SECOND_EVENING;
			break;
		case SECOND_EVENING:
			res = FIRST_EVENING;
			break;
		case FIRST_EVENING:
			res = DAY_OFF_BEFORE_EVENING;
			break;
		case DAY_OFF_BEFORE_EVENING:
			res = DAY_OFF;
			break;
		case DAY_OFF:
			res = AFTER_NIGHT;
			break;
		case AFTER_NIGHT:
			res = SECOND_NIGHT;
			break;
		case SECOND_NIGHT:
			res = FIRST_NIGHT;
			break;
		case FIRST_NIGHT:
			res = SECOND_MORNING;
			break;
		case SECOND_MORNING:
			res = FIRST_MORNING;
			break;
		case FIRST_MORNING:
			res = SHORT_DAY_OFF;
			break;
		}
		return res;
	}

}
