package by.matskevich.calendaroffactory;

public enum StateShiftDay implements Statable {
	SECOND_DAY_OFF("2-й ВЫХОДНОЙ"), FIRST_WORK("1-й ДЕНЬ"), SECOND_WORK("2-й ДЕНЬ"), FIRST_DAY_OFF("1-й ВЫХОДНОЙ");

	final String state;

	private StateShiftDay(String state) {
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