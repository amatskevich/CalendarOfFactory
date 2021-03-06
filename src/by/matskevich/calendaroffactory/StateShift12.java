package by.matskevich.calendaroffactory;

import android.graphics.Color;
import by.matskevich.calendaroffactory.util.Constants;

public enum StateShift12 implements Statable {

	FIRST_DAY("1-я С УТРА", "8", Constants.COLOR_8, 12, "В день"),

	SECOND_DAY("2-я С УТРА", "8", Constants.COLOR_8, 12, "В день"),

	DAY_OFF_AFTER_WORK_DAY("ВЫХОДНОЙ", "*", Constants.COLOR_WHITE, 0, "Выходной"),

	AT_NIGHT("В НОЧЬ", "20", Constants.COLOR_20_24, 4, "В ночь"),

	AFTER_NIGHT_AT_NIGHT("С НОЧИ В НОЧЬ", "20", Constants.COLOR_20_24, 12, "В ночь"),

	AFTER_NIGHT("ОТСЫПНОЙ", "O", Constants.COLOR_O, 8, "Отсыпной"),

	DAY_OFF("ВЫХОДНОЙ", "*", Constants.COLOR_WHITE, 0, "Выходной"),

	DAY_OFF_BEFORE_WORK_("ЗАВТРА С УТРА", "*", Constants.COLOR_WHITE, 0, "Выходной");

	final String state;
	final String sign;
	final int color;
	final int hours;
	final String description;

	StateShift12(String state, String sign, String color, int workHours, String description) {
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
		case FIRST_DAY:
			res = SECOND_DAY;
			break;
		case SECOND_DAY:
			res = DAY_OFF_AFTER_WORK_DAY;
			break;
		case DAY_OFF_AFTER_WORK_DAY:
			res = AT_NIGHT;
			break;
		case AT_NIGHT:
			res = AFTER_NIGHT_AT_NIGHT;
			break;
		case AFTER_NIGHT_AT_NIGHT:
			res = AFTER_NIGHT;
			break;
		case AFTER_NIGHT:
			res = DAY_OFF;
			break;
		case DAY_OFF:
			res = DAY_OFF_BEFORE_WORK_;
			break;
		case DAY_OFF_BEFORE_WORK_:
			res = FIRST_DAY;
			break;
		}
		return res;
	}

	@Override
	public Statable before() {
		Statable res = null;
		switch (this) {
		case FIRST_DAY:
			res = DAY_OFF_BEFORE_WORK_;
			break;
		case SECOND_DAY:
			res = FIRST_DAY;
			break;
		case DAY_OFF_AFTER_WORK_DAY:
			res = SECOND_DAY;
			break;
		case AT_NIGHT:
			res = DAY_OFF_AFTER_WORK_DAY;
			break;
		case AFTER_NIGHT_AT_NIGHT:
			res = AT_NIGHT;
			break;
		case AFTER_NIGHT:
			res = AFTER_NIGHT_AT_NIGHT;
			break;
		case DAY_OFF:
			res = AFTER_NIGHT;
			break;
		case DAY_OFF_BEFORE_WORK_:
			res = DAY_OFF;
			break;
		}
		return res;
	}
}
