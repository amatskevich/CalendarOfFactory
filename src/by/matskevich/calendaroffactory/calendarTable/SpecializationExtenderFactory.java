package by.matskevich.calendaroffactory.calendarTable;

import java.util.Calendar;

public interface SpecializationExtenderFactory {

    SpecializationExtender getSpecializationFactory(Calendar firstDay);
}
