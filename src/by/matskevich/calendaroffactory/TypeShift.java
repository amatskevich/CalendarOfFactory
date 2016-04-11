package by.matskevich.calendaroffactory;

import java.util.Calendar;

public enum TypeShift {
	TWELFTH(8, CharShift12.class, StateShift12.class), EIGHT(10, CharShift8.class, StateShift8.class);

	public static final String TYPE_SHIFT = "TypeShift";
	final int cycleDays;
	final Calendar basicDate;
	final Class<? extends CharShift> charShift;
	final Class<? extends Statable> stateShift;

	private TypeShift(int cycle, Class<? extends CharShift> charShift, Class<? extends Statable> stateShift) {
		this.cycleDays = cycle;
		Calendar basicDate = Calendar.getInstance();
		basicDate.set(2016, 2, 20);// Shift_A THIRST_DAY
		this.basicDate = basicDate;
		this.charShift = charShift;
		this.stateShift = stateShift;
	}
}
