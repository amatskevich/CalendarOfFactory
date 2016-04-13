package by.matskevich.calendaroffactory;

public enum CharShift12 implements CharShift {
	A12('А'), V12('В'), G12('Г'), B12('Б');// Order shifts!!
	final char charShift;
	private String nameChar;

	private CharShift12(char charShift) {
		this.charShift = charShift;
		this.nameChar = String.valueOf(charShift);
	}

	public String getChar() {
		return "" + charShift;
	}

	public String getNameChar() {
		return nameChar;
	}

	public void setNameChar(String nameChar) {
		this.nameChar = nameChar;
	}

}
