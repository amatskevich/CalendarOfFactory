package by.matskevich.calendaroffactory;

import java.util.Calendar;

public enum TypeShift {
	TWELFTH(8, CharShift12.class, StateShift12.class), EIGHT(10, CharShift8.class, StateShift8.class), DAY(4,
			CharShiftDay.class, StateShiftDay.class);

	public static final String TYPE_SHIFT = "TypeShift";
	public final int cycleDays;
	private final Calendar basicDate;
	public final Class<? extends CharShift> charShift;
	public final Class<? extends Statable> stateShift;

	TypeShift(int cycle, Class<? extends CharShift> charShift, Class<? extends Statable> stateShift) {
		this.cycleDays = cycle;
		Calendar basicDate = Calendar.getInstance();
		basicDate.set(2016, 2, 20);// Shift_A THIRST_DAY
		this.basicDate = basicDate;
		this.charShift = charShift;
		this.stateShift = stateShift;
	}

	public Calendar getBasicDate() {
		return basicDate;
	}
	
}
