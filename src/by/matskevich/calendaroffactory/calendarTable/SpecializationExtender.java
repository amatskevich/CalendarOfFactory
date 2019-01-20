package by.matskevich.calendaroffactory.calendarTable;

import android.app.Activity;
import android.view.View;
import by.matskevich.calendaroffactory.CharShift;
import by.matskevich.calendaroffactory.Statable;
import by.matskevich.calendaroffactory.TypeShift;
import by.matskevich.calendaroffactory.util.Utils;

import java.util.Calendar;

public abstract class SpecializationExtender {

    protected abstract void nextDay();

    protected abstract int getColorOfCell();

    protected abstract View createBottomText(Activity activity);

    protected Statable getFirstState(CharShift shift, Calendar firstDay) {

        TypeShift typeShift = shift.getTypeShift();

        int step = Utils.getStepOfCycle(typeShift, firstDay);
        for (CharShift cs : typeShift.charShift.getEnumConstants()) {
            if (cs == shift) {
                break;
            }
            step = (2 + step) % typeShift.cycleDays;
        }
        return typeShift.stateShift.getEnumConstants()[step];
    }
}
