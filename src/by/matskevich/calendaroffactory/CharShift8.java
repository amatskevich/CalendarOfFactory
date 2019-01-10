package by.matskevich.calendaroffactory;

public enum CharShift8 implements CharShift {
	A8('А'), B8('Б'), V8('В'), G8('Г'), D8('Д');
	final char charShift;
	private String nameChar;

	private CharShift8(char charShift) {
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

	@Override
	public TypeShift getTypeShift() {
		return TypeShift.EIGHT;
	}

	@Override
	public int getOrder() {
		return this.ordinal();
	}

}
