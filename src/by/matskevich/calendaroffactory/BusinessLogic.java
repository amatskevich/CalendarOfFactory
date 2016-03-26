package by.matskevich.calendaroffactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public final class BusinessLogic {

	private int cycle = 8;// cycle 8 days for TypeShift.TWELFTH
	private Calendar date;

	private List<Shift> shiftList;

	private TypeShift typeShift;

	public BusinessLogic() {
		date = Calendar.getInstance();
		date.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
		typeShift = TypeShift.TWELFTH;
		shiftList = new ArrayList<Shift>();
		firstCalculation();
	}

	private void firstCalculation() {
		Calendar basicDate = Calendar.getInstance();
		basicDate.set(2016, 2, 20);// Shift_A THIRST_DAY
		int days = betweenStartDayEndDay(basicDate, date);
		int step = days % cycle;
		for (CharShift cs : CharShift.values()) {
			shiftList.add(new Shift(typeShift, cs, StateShift12.values()[step]));
			step = (2 + step) % cycle;
		}
	}

	private int betweenStartDayEndDay(Calendar start, Calendar end) {
		// SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");

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

	public void setTypeShift(TypeShift typeShift) {
		this.typeShift = typeShift;
	}

	public String getDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		return sdf.format(date.getTime());
	}

	public List<Shift> getShiftList() {
		return shiftList;
	}

}
