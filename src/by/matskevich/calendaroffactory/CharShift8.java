package by.matskevich.calendaroffactory;

public enum CharShift8 implements CharShift {
	A('А'), B('Б'), V('В'), G('Г'), D('Д');// Order shifts!!
	final char charShift;

	CharShift8(char charShift) {
		this.charShift = charShift;
	}

	public String getChar() {
		return "" + charShift;
	}

}
