package by.matskevich.calendaroffactory;

public enum CharShift {
	A('А'), V('В'), G('Г'), B('Б');// Order shifts!!
	final char charShift;

	CharShift(char charShift) {
		this.charShift = charShift;
	}

	public String getChar() {
		return "" + charShift;
	}
}
