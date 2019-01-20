package by.matskevich.calendaroffactory.mappingHoliday;

import by.matskevich.calendaroffactory.CharShift;
import by.matskevich.calendaroffactory.calendarTable.SpecializationExtender;
import by.matskevich.calendaroffactory.calendarTable.SpecializationExtenderFactory;

import java.util.Calendar;

public class HolidayExtenderFactory implements SpecializationExtenderFactory {

    private CharShift[] charShifts;

    public HolidayExtenderFactory(CharShift[] charShifts) {

        this.charShifts = charShifts;
    }

    public SpecializationExtender getSpecializationFactory(Calendar firstDay) {

        return new MappingHolidaySpecialization(charShifts, firstDay);
    }
}
