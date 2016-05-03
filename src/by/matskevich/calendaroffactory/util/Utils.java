package by.matskevich.calendaroffactory.util;

import java.util.Calendar;

import by.matskevich.calendaroffactory.TypeShift;

public final class Utils {

	/**
	 * Считает как работает определенная смена на определенный день. 
	 * @param typeShift
	 * @param date
	 * @return - индекс stateShift.getEnumConstants()[step]
	 */
	public static int getStepOfCycle(TypeShift typeShift, Calendar date) {
		int days = betweenStartDayEndDay(typeShift.getBasicDate(), date);
		int step = days % typeShift.cycleDays;
		if (step < 0) {
			step += typeShift.cycleDays;
		}
		return step;
	}
	
	private static int betweenStartDayEndDay(Calendar start, Calendar end) {
		return (int) ((end.getTime().getTime() - start.getTime().getTime()) / (1000 * 60 * 60 * 24));
	}
}
