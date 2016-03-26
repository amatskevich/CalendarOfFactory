package by.matskevich.calendaroffactory;

public enum TypeShift {
	TWELFTH(12), SEVEN(7);

	final int hours;

	TypeShift(int hours) {
		this.hours = hours;
	}
}
