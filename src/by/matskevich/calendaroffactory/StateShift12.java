package by.matskevich.calendaroffactory;

public enum StateShift12 implements Statable {

	FIRST_DAY("1-я С УТРА"), SECOND_DAY("2-я С УТРА"), DAY_OFF_AFTER_WORK_DAY("ВЫХОДНОЙ"), AT_NIGHT(
			"В НОЧЬ"), AFTER_NIGHT_AT_NIGHT("С НОЧИ В НОЧЬ"), AFTER_NIGHT(
					"ОТСЫПНОЙ"), DAY_OFF("ВЫХОДНОЙ"), DAY_OFF_BEFORE_WORK_("ЗАВТРА С УТРА");

	final String state;

	StateShift12(String state) {
		this.state = state;
	}

	@Override
	public String getState() {
		return state;
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
