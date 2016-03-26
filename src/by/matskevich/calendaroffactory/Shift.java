package by.matskevich.calendaroffactory;

public class Shift {
	private final TypeShift typeShift;
	private final CharShift charShift;
	private Statable stateShift;

	public Shift(TypeShift typeShift, CharShift charShift, Statable stateShift) {
		this.typeShift = typeShift;
		this.charShift = charShift;
		this.stateShift = stateShift;
	}

	public Statable getStateShift() {
		return stateShift;
	}

	public void setStateShift(Statable stateShift) {
		this.stateShift = stateShift;
	}

	public TypeShift getTypeShift() {
		return typeShift;
	}

	public CharShift getCharShift() {
		return charShift;
	}

}
