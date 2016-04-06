package by.matskevich.calendaroffactory;

import java.util.Calendar;

public enum TypeShift {
	TWELFTH(8, 2016, 2, 19, CharShift12.class, StateShift12.class), EIGHT(10, 2016, 2, 19, CharShift8.class,
			StateShift8.class);

	final int cycleDays;
	final Calendar basicDate;
	final Class<? extends CharShift> charShift;
	final Class<? extends Statable> stateShift;

	TypeShift(int cycle, int year, int month, int day, Class<? extends CharShift> charShift,
			Class<? extends Statable> stateShift) {
		this.cycleDays = cycle;
		Calendar basicDate = Calendar.getInstance();
		basicDate.set(year, month, day);// Shift_A THIRST_DAY
		this.basicDate = basicDate;
		this.charShift = charShift;
		this.stateShift = stateShift;
	}
}
