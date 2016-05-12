package by.matskevich.calendaroffactory.util;

import android.content.SharedPreferences.Editor;
import android.view.View;
import by.matskevich.calendaroffactory.TypeShift;

public interface RadioGroupShiftable {
	
	void onClickRadioSelect(View v);

	void do12();

	void doDay();

	void do8();

	Editor getSettingsEdit();

	void setTypeShift(TypeShift type);

	TypeShift getTypeShift();


}
