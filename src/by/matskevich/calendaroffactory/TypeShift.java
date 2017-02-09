package by.matskevich.calendaroffactory;

import java.util.Calendar;

public enum TypeShift {
    TWELFTH(8, CharShift12.class, StateShift12.class, "График №30"),
    EIGHT(10, CharShift8.class, StateShift8.class, "График №4"),
    DAY(4, CharShiftDay.class, StateShiftDay.class, "График №15");

    public static final String TYPE_SHIFT = "TypeShift";
    public final int cycleDays;
    private final Calendar basicDate;
    public final Class<? extends CharShift> charShift;
    public final Class<? extends Statable> stateShift;
    public final String numberOfShiftStr;

    TypeShift(int cycle, Class<? extends CharShift> charShift, Class<? extends Statable> stateShift, String numberOfShiftStr) {
        this.cycleDays = cycle;
        Calendar basicDate = Calendar.getInstance();
        basicDate.set(2016, 2, 20);// Shift_A THIRST_DAY
        this.basicDate = basicDate;
        this.charShift = charShift;
        this.stateShift = stateShift;
        this.numberOfShiftStr = numberOfShiftStr;
    }

    public Calendar getBasicDate() {
        return basicDate;
    }

}
