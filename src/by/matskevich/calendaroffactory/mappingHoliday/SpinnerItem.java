package by.matskevich.calendaroffactory.mappingHoliday;

import by.matskevich.calendaroffactory.CharShift;

public class SpinnerItem {

    private final CharShift shift;

    private final String label;

    public SpinnerItem(CharShift shift, String label) {
        this.shift = shift;
        this.label = label;
    }

    public CharShift getShift() {
        return shift;
    }

    @Override
    public String toString() {
        return label;
    }
}
