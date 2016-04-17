package by.matskevich.calendaroffactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public final class BusinessLogic {

	private static BusinessLogic bLogic;

	public static final String APP_PREFERENCE = "appsettings";

	private Calendar date;

	private List<Shift> shiftList = new ArrayList<Shift>();

	private TypeShift typeShift;

	private BusinessLogic() {
		date = Calendar.getInstance();
		date.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
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
		int days = betweenStartDayEndDay(typeShift.basicDate, date);
		int step = days % typeShift.cycleDays;
		if (step < 0) {
			step += typeShift.cycleDays;
		}
		for (CharShift cs : typeShift.charShift.getEnumConstants()) {
			shiftList.add(new Shift(typeShift, cs, typeShift.stateShift.getEnumConstants()[step]));
			step = (2 + step) % typeShift.cycleDays;
		}
	}

	private int betweenStartDayEndDay(Calendar start, Calendar end) {
		return (int) ((end.getTime().getTime() - start.getTime().getTime()) / (1000 * 60 * 60 * 24));
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
		DateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		return sdf.format(date.getTime());
	}

	public void changeDate(int year, int month, int day) {
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

	public void changeTypeShift() {
		typeShift = typeShift == TypeShift.TWELFTH ? TypeShift.EIGHT : TypeShift.TWELFTH;
		firstCalculation();
	}

}
