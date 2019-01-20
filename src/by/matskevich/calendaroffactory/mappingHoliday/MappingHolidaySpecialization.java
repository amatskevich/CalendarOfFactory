package by.matskevich.calendaroffactory.mappingHoliday;

import android.app.Activity;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import by.matskevich.calendaroffactory.*;
import by.matskevich.calendaroffactory.calendarTable.SpecializationExtender;

import java.util.Calendar;

public class MappingHolidaySpecialization extends SpecializationExtender {

    private static final float DAY_FACTOR = 0.125f;
    private static final float NIGHT_FACTOR = 0.0625f;
    private static final boolean T = true;
    private static final boolean F = false;

    private Statable statable_1;
    private Statable statable_2;

    public MappingHolidaySpecialization(CharShift[] charShifts, Calendar firstDay) {

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

        boolean[] matchedWorkedHours = matchingWorkedHours(getWorkedHours(statable_1), getWorkedHours(statable_2));
        float[] floats = {0, generateGradientColor(matchedWorkedHours), 100}; //red
        return Color.HSVToColor(floats);
    }

    /**
     *
     * @param workedHours - array with size 24
     * @return 0..1 .
     */
    private float generateGradientColor(boolean[] workedHours) {

        float res = 0;

        for (int i = 0; i < 8 ; i++) {
            if (workedHours[i]) {
                res += NIGHT_FACTOR;
            }
        }

        for (int i = 8; i < 21 ; i++) {
            if (workedHours[i]) {
                res += DAY_FACTOR;
            }
        }

        for (int i = 21; i < 24 ; i++) {
            if (workedHours[i]) {
                res += NIGHT_FACTOR;
            }
        }

        return res;
    }

    private boolean[] matchingWorkedHours(boolean[] workedHours_1, boolean[] workedHours_2) {

        boolean[] res = new boolean[24];
        for (int i = 0; i < res.length; i++) {
            res[i] = workedHours_1[i] || workedHours_2[i];
        }
        return res;
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

    private boolean[] getWorkedHours(Statable statable) {
        if (statable instanceof StateShift12) {
            return getWorkedHoursFor12((StateShift12) statable);
        } else if (statable instanceof StateShift8) {
            return getWorkedHoursFor8((StateShift8) statable);
        } else if (statable instanceof StateShiftDay) {
            return getWorkedHoursForDay((StateShiftDay) statable);
        }
        return new boolean[24];
    }

    private boolean[] getWorkedHoursForDay(StateShiftDay statable) {

        boolean[] res;
        switch (statable) {
            case FIRST_WORK:
                res = new boolean[]{F, F, F, F, F, F, F, F,
                        T, T, T, T, T, T, T, T, T, T, T, T,
                        F, F, F, F};
                break;
            case SECOND_WORK:
                res = new boolean[]{F, F, F, F, F, F, F, F,
                        T, T, T, T, T, T, T, T, T, T, T, T,
                        F, F, F, F};
                break;
            case FIRST_DAY_OFF:
            case SECOND_DAY_OFF:
            default:
                res = new boolean[24];
        }

        return res;
    }

    private boolean[] getWorkedHoursFor8(StateShift8 statable) {
        boolean[] res;
        switch (statable) {
            case FIRST_NIGHT:
                res = new boolean[]{F, F, F, F, F, F, F, F,
                        F, F, F, F, F, F, F, F, F, F, F, F,
                        F, F, F, T};
                break;
            case SECOND_NIGHT:
                res = new boolean[]{T, T, T, T, T, T, T, T,
                        F, F, F, F, F, F, F, F, F, F, F, F,
                        F, F, F, T};
                break;
            case AFTER_NIGHT:
                res = new boolean[]{T, T, T, T, T, T, T, T,
                        F, F, F, F, F, F, F, F, F, F, F, F,
                        F, F, F, F};
                break;
            case FIRST_EVENING:
            case SECOND_EVENING:
                res = new boolean[]{F, F, F, F, F, F, F, F,
                        F, F, F, F, F, F, F, F, T, T, T, T,
                        T, T, T, T,};
                break;
            case FIRST_MORNING:
            case SECOND_MORNING:
                res = new boolean[]{F, F, F, F, F, F, F, F,
                        T, T, T, T, T, T, T, T, F, F, F, F,
                        F, F, F, F};
                break;
            case SHORT_DAY_OFF:
            case DAY_OFF_BEFORE_EVENING:
            case DAY_OFF:
            default:
                res = new boolean[24];
        }
        return res;
    }

    private boolean[] getWorkedHoursFor12(StateShift12 statable) {
        boolean[] res;
        switch (statable) {
            case FIRST_DAY:
            case SECOND_DAY:
                res = new boolean[]{F, F, F, F, F, F, F, F,
                        T, T, T, T, T, T, T, T, T, T, T, T,
                        F, F, F, F};
                break;
            case AT_NIGHT:
                res = new boolean[]{F, F, F, F, F, F, F, F,
                        F, F, F, F, F, F, F, F, F, F, F, F,
                        T, T, T, T};
                break;
            case AFTER_NIGHT_AT_NIGHT:
                res = new boolean[]{T, T, T, T, T, T, T, T,
                        F, F, F, F, F, F, F, F, F, F, F, F,
                        T, T, T, T};
                break;
            case AFTER_NIGHT:
                res = new boolean[]{T, T, T, T, T, T, T, T,
                        F, F, F, F, F, F, F, F, F, F, F, F,
                        F, F, F, F};
                break;
            case DAY_OFF:
            case DAY_OFF_BEFORE_WORK_:
            case DAY_OFF_AFTER_WORK_DAY:
            default:
                res = new boolean[24];
        }
        return res;
    }
}
