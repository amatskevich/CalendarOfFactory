package by.matskevich.calendaroffactory;

public enum CharShiftDay implements CharShift {
	TEAM_1("1 бригада"), TEAM_2("2 бригада");

	final String charShift;
	private String nameChar;

	private CharShiftDay(String charShift) {
		this.charShift = charShift;
		this.nameChar = String.valueOf(charShift);
	}
	
	@Override
	public String getChar() {
		return charShift;
	}

	@Override
	public String getNameChar() {
		return nameChar;
	}

	@Override
	public void setNameChar(String name) {
		this.nameChar = name;
	}

}
