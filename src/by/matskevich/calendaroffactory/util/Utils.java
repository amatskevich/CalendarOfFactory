package by.matskevich.calendaroffactory.util;

import java.util.Calendar;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.view.View;
import by.matskevich.calendaroffactory.R;
import by.matskevich.calendaroffactory.TypeShift;

public final class Utils {

	/**
	 * Считает как работает определенная смена на определенный день.
	 * 
	 * @param typeShift
	 * @param date
	 * @return - индекс stateShift.getEnumConstants()[step]
	 */
	public static int getStepOfCycle(TypeShift typeShift, Calendar date) {
		int days = betweenStartDayEndDay(typeShift.getBasicDate(), date);
		int step = days % typeShift.cycleDays;
		if (step < 0) {
			step += typeShift.cycleDays - 1;
		}
		return step;
	}

	private static int betweenStartDayEndDay(Calendar start, Calendar end) {
		return (int) ((end.getTime().getTime() - start.getTime().getTime()) / (1000 * 60 * 60 * 24));
	}

	public static void onClickRadioSelect(View v, RadioGroupShiftable group) {
		switch (v.getId()) {
		case R.id.radioButton8:
			if (group.getTypeShift() == TypeShift.EIGHT) {
				return;
			}
			group.setTypeShift(TypeShift.EIGHT);
			group.do8();
			break;
		case R.id.radioButtonDay:
			if (group.getTypeShift() == TypeShift.DAY) {
				return;
			}
			group.setTypeShift(TypeShift.DAY);
			group.doDay();
			break;
		default:
			if (group.getTypeShift() == TypeShift.TWELFTH) {
				return;
			}
			group.setTypeShift(TypeShift.TWELFTH);
			group.do12();
		}
		SharedPreferences.Editor editor = group.getSettingsEdit();
		editor.putString(TypeShift.TYPE_SHIFT, group.getTypeShift().toString());
		editor.apply();
	}


	public static String getNameOfTypeShift(TypeShift typeShift, Resources resources) {

		int resource;

		switch (typeShift) {
			case TWELFTH:
				resource = R.string.radio_12;
				break;
			case DAY:
				resource = R.string.radio_day;
				break;
			case EIGHT:
				resource = R.string.radio_8;
				break;
			default:
				return "";
		}
		return resources.getString(resource);
	}

}
