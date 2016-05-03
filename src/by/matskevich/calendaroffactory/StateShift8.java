package by.matskevich.calendaroffactory;

public enum StateShift8 implements Statable {
	FIRST_NIGHT("1-я НОЧЬ", "24"), SECOND_NIGHT("2-я НОЧЬ", "24"), AFTER_NIGHT("ОТСЫПНОЙ", "O"), DAY_OFF("ВЫХОДНОЙ",
			"*"), DAY_OFF_BEFORE_EVENING("ВЫХОДНОЙ", "*"), FIRST_EVENING("1-я С 4-х", "16"), SECOND_EVENING("2-я С 4-х",
					"16"), SHORT_DAY_OFF("ВЫХОДНОЙ",
							"*"), FIRST_MORNING("1-я С УТРА", "8"), SECOND_MORNING("2-я С УТРА", "8"),;

	final String state;
	final String sign;

	private StateShift8(String state, String sign) {
		this.state = state;
		this.sign = sign;
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
