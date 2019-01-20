package by.matskevich.calendaroffactory.mappingHoliday;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.*;
import android.widget.*;
import by.matskevich.calendaroffactory.CharShift;
import by.matskevich.calendaroffactory.R;
import by.matskevich.calendaroffactory.calendar.CalendarTableLayout;
import by.matskevich.calendaroffactory.calendar.MonthRus;
import by.matskevich.calendaroffactory.calendar.OnSwipeTouchListener;
import by.matskevich.calendaroffactory.calendarTable.CalendarTableProducer;
import by.matskevich.calendaroffactory.util.Utils;

import java.util.Calendar;

public class MappingHolidayActivity extends Activity {

    private GeneratingMenuHelper generatingMenuHelper;
    private CharShift[] charShifts = new CharShift[2];
    private Calendar date;
    private TextView monthText;
    private CalendarTableProducer calendarTableProducer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapping_holiday);
        monthText = (TextView) findViewById(R.id.mapping_month);
        date = Calendar.getInstance();
        CalendarTableLayout calendar = (CalendarTableLayout) findViewById(R.id.mapping_calendar_view);
        charShifts[0] = null;
        charShifts[1] = null;
        calendarTableProducer = new CalendarTableProducer(this, calendar);
        calendar.setOnTouchListener(new OnSwipeTouchListener(MappingHolidayActivity.this) {
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
                calendarTableProducer.buildTable(date, new HolidayExtenderFactory(charShifts));
            }
        });

        generatingMenuHelper = new GeneratingMenuHelper(getResources());
        final TextView textView_1 = (TextView) findViewById(R.id.selected_shift_1);
        textView_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v, textView_1, charShifts, 0);
            }
        });

        final TextView textView_2 = (TextView) findViewById(R.id.selected_shift_2);
        textView_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v, textView_2, charShifts, 1);
            }
        });
    }

    private void setMonthText(Calendar d) {
        monthText.setText(MonthRus.values()[d.get(Calendar.MONTH)].name + " " + d.get(Calendar.YEAR));
    }

    private void showPopupMenu(View v, final TextView textView, final CharShift[] charShifts, final int i) {

        PopupMenu popupMenu = new PopupMenu(this, v);
        final SparseArray<CharShift> charShiftMap = new SparseArray<CharShift>();
        generatingMenuHelper.generatingMenu(popupMenu, charShiftMap);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        CharShift charShift = charShiftMap.get(item.getItemId());
                        if (charShift == null) {
                            return false;
                        }
                        String text = Utils.getNameOfTypeShift(charShift.getTypeShift(), getResources())
                                + " : " + charShift.getNameChar();
                        textView.setText(text);
                        textView.setTextColor(Color.BLACK);
                        charShifts[i] = charShift;
                        refreshTable();
                        return true;
                    }
                });
        popupMenu.show();
    }

    private void refreshTable() {

        if (charShifts[0] == null || charShifts[1] == null) {
            return;
        }
        setMonthText(date);
        calendarTableProducer.buildTable(date, new HolidayExtenderFactory(charShifts));
        //TODO remove
//        Toast.makeText(getApplicationContext(), charShifts[0] + ":" + charShifts[1], Toast.LENGTH_SHORT).show();
    }


}
