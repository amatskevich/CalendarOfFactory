package by.matskevich.calendaroffactory.mappingHoliday;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TableLayout;
import by.matskevich.calendaroffactory.*;
import by.matskevich.calendaroffactory.calendar.CalendarActivity;
import by.matskevich.calendaroffactory.calendar.CalendarTableLayout;
import by.matskevich.calendaroffactory.calendar.OnSwipeTouchListener;

import java.util.Calendar;

public class MappingHolidaysActivity extends Activity {

    private CharShift shift1;
    private CharShift shift2;
    private CalendarTableLayout calendar;
    private Calendar date;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        try {
            shift1 = findShift(intent.getStringExtra("type_shift_1"), intent.getStringExtra("char_shift_1"));
            shift2 = findShift(intent.getStringExtra("type_shift_2"), intent.getStringExtra("char_shift_2"));
        } catch (IllegalArgumentException iae) {
            Log.e("mtsk", "not find shift", iae);
            finish();
            return;
        }

        date = Calendar.getInstance();
        date.setFirstDayOfWeek(Calendar.MONDAY);

        calendar = (CalendarTableLayout) findViewById(R.id.mapping_view);
        calendar.setOnTouchListener(new OnSwipeTouchListener(MappingHolidaysActivity.this) { //todo continuing
            public boolean onSwipeRight() {
                rebuildView(-1);
                return true;
            }

            public boolean onSwipeLeft() {
                rebuildView(1);
                return true;
            }

            private void rebuildView(int i) {
                date.add(Calendar.MONTH, i);
                setMonthText(date);
                calendar.removeAllViews();
                buildTable();
            }
        });

        buildTable();
    }


    private CharShift findShift(String typeShiftStr, String charShiftStr) {

        TypeShift typeShift = TypeShift.valueOf(typeShiftStr);
        CharShift res;
        switch (typeShift) {
            case TWELFTH:
                res = CharShift12.valueOf(charShiftStr);
                break;
            case EIGHT:
                res = CharShift8.valueOf(charShiftStr);
                break;
            case DAY:
                res = CharShiftDay.valueOf(charShiftStr);
                break;
            default:
                throw new RuntimeException();
        }
        return res;
    }
}
