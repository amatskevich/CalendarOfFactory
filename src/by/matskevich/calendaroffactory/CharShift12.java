package by.matskevich.calendaroffactory;

public enum CharShift12 implements CharShift {
	A('А'), V('В'), G('Г'), B('Б');// Order shifts!!
	final char charShift;

	CharShift12(char charShift) {
		this.charShift = charShift;
	}

	public String getChar() {
		return "" + charShift;
	}
}
