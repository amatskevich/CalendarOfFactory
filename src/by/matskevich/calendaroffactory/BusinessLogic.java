package by.matskevich.calendaroffactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import by.matskevich.calendaroffactory.util.Constants;
import by.matskevich.calendaroffactory.util.Utils;

public final class BusinessLogic {

	private static BusinessLogic bLogic;

	public static final String APP_PREFERENCE = "appsettings";

	private Calendar date;

	private List<Shift> shiftList = new ArrayList<Shift>();

	private TypeShift typeShift;

	private BusinessLogic() {
		date = Calendar.getInstance();
		int year = date.get(Calendar.YEAR);
		int month = date.get(Calendar.MONTH);
		int day = date.get(Calendar.DAY_OF_MONTH);
		date.clear();
		date.set(year, month, day);
		typeShift = TypeShift.TWELFTH;

		firstCalculation();
	}

	public static BusinessLogic getInstance() {
		if (bLogic == null) {
			bLogic = new BusinessLogic();
		}
		return bLogic;
	}

	private void firstCalculation() {
		shiftList.clear();
		int step = Utils.getStepOfCycle(typeShift, date);
		for (CharShift cs : typeShift.charShift.getEnumConstants()) {
			shiftList.add(new Shift(typeShift, cs, typeShift.stateShift.getEnumConstants()[step]));
			step = (2 + step) % typeShift.cycleDays;
		}
	}

	public void dayUp() {
		date.add(Calendar.DATE, 1);
		for (Shift shift : shiftList) {
			shift.setStateShift(shift.getStateShift().next());
		}
	}

	public void dayDown() {
		date.add(Calendar.DATE, -1);
		for (Shift shift : shiftList) {
			shift.setStateShift(shift.getStateShift().before());
		}
	}

	public TypeShift getTypeShift() {
		return typeShift;
	}

	public String getDate() {
		return Constants.FORMATTER_DATE.format(date.getTime());
	}

	public String getDateWeek() {
		return Constants.WEEK_DAYS_FULL[date.get(Calendar.DAY_OF_WEEK) - 1];
	}

	public Long getDateLong() {
		return date.getTimeInMillis();
	}

	public void changeDate(int year, int month, int day) {
		date.clear();
		date.set(Calendar.YEAR, year);
		date.set(Calendar.MONTH, month);
		date.set(Calendar.DAY_OF_MONTH, day);
		firstCalculation();
	}

	public int getYear() {
		return date.get(Calendar.YEAR);
	}

	public int getMonth() {
		return date.get(Calendar.MONTH);
	}

	public int getDay() {
		return date.get(Calendar.DAY_OF_MONTH);
	}

	public List<Shift> getShiftList() {
		return shiftList;
	}

	public void changeTypeShift(TypeShift type) {
		typeShift = type;
		firstCalculation();
	}

}
