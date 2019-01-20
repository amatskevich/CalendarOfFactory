package by.matskevich.calendaroffactory.mappingHoliday;

import android.app.Activity;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import by.matskevich.calendaroffactory.CharShift;
import by.matskevich.calendaroffactory.Statable;
import by.matskevich.calendaroffactory.calendarTable.SpecializationExtender;
import by.matskevich.calendaroffactory.util.Constants;

import java.util.Calendar;

public class MappingHolidaySpecialization extends SpecializationExtender {

    private final CharShift[] charShifts;
    private Statable statable_1;
    private Statable statable_2;

    public MappingHolidaySpecialization(CharShift[] charShifts, Calendar firstDay) {
        this.charShifts = charShifts;
        this.statable_1 = getFirstState(charShifts[0], firstDay);
        this.statable_2 = getFirstState(charShifts[1], firstDay);
    }

    @Override
    public void nextDay() {

        statable_1 = statable_1.next();
        statable_2 = statable_2.next();
    }

    @Override
    public int getColorOfCell() {

        return Color.parseColor(Constants.COLOR_WHITE);
    }

    @Override
    public View createBottomText(Activity activity) {

        TextView textView = new TextView(activity);
        textView.setGravity(Gravity.CENTER);
        textView.setText(statable_1.getStatSign() + '/' + statable_2.getStatSign());
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textView.setPadding(1,0,1,0);
        return textView;
    }
}
