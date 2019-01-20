package by.matskevich.calendaroffactory.mappingHoliday;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.*;
import android.widget.*;
import by.matskevich.calendaroffactory.CharShift;
import by.matskevich.calendaroffactory.R;
import by.matskevich.calendaroffactory.Statable;
import by.matskevich.calendaroffactory.TypeShift;
import by.matskevich.calendaroffactory.calendar.CalendarTableLayout;
import by.matskevich.calendaroffactory.calendar.MonthRus;
import by.matskevich.calendaroffactory.calendar.OnSwipeTouchListener;
import by.matskevich.calendaroffactory.util.Constants;
import by.matskevich.calendaroffactory.util.Utils;

import java.util.Calendar;

public class MappingHolidayActivity extends Activity {

    private GeneratingMenuHelper generatingMenuHelper;
    private CharShift[] charShifts = new CharShift[2];
    private CalendarTableLayout calendar;
    private Calendar date;
    private TextView monthText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapping_holiday);
        monthText = (TextView) findViewById(R.id.mapping_month);
        date = Calendar.getInstance();
        calendar = (CalendarTableLayout) findViewById(R.id.mapping_calendar_view);
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
                calendar.removeAllViews();
                buildTable();
            }
        });

        generatingMenuHelper = new GeneratingMenuHelper(getResources());
        charShifts[0] = null;
        charShifts[1] = null;

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
            Toast.makeText(getApplicationContext(), "not filled", Toast.LENGTH_SHORT).show();
            return;
        }
        setMonthText(date);
        buildTable();
        //TODO remove
        Toast.makeText(getApplicationContext(), charShifts[0] + ":" + charShifts[1], Toast.LENGTH_SHORT).show();
    }

    //***********************************Table***********
    private void buildTable() {

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TableRow.LayoutParams param = new TableRow.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT);
        param.setMargins(1, 0, 1, 1);

        TableRow head = createTableRow(params);
        for (String day : Constants.WEEK_DAYS) {
            TextView tmp = createText(day, Gravity.CENTER_HORIZONTAL);
            tmp.setLayoutParams(param);
            tmp.setBackgroundColor(Color.parseColor(Constants.COLOR_HEAD));
            head.addView(tmp);
        }
        calendar.addView(head);

        final Calendar firstDay = Calendar.getInstance();
        firstDay.clear();
        firstDay.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.getActualMinimum(Calendar.DAY_OF_MONTH));


        //todo replace to separate method with calculated color
        TypeShift typeShift = charShifts[0].getTypeShift();
        CharShift shift = charShifts[0];

        int step = Utils.getStepOfCycle(typeShift, firstDay);
        for (CharShift cs : typeShift.charShift.getEnumConstants()) {
            if (cs == shift) {
                break;
            }
            step = (2 + step) % typeShift.cycleDays;
        }
        Statable stateShift = typeShift.stateShift.getEnumConstants()[step];

        int weekSize = 7;
        int dayWeek = (firstDay.get(Calendar.DAY_OF_WEEK) + 5) % weekSize;// start_from_monday
        int maxDays = firstDay.getActualMaximum(Calendar.DAY_OF_MONTH);
        TableRow tableRow = createTableRow(params);
        // add empty fields
        for (int j = 0; j < dayWeek; j++) {
            tableRow.addView(createLinear(Color.BLACK, param));
        }

        for (int i = 1; i <= maxDays; i++, dayWeek++) {
            if (dayWeek >= weekSize) {
                dayWeek %= weekSize;
                calendar.addView(tableRow);
                tableRow = createTableRow(params);
            }
            final View cell = createField(stateShift, String.valueOf(i), param);
            tableRow.addView(cell);
            stateShift = stateShift.next();
        }
        // add empty fields
        for (int j = dayWeek; j < weekSize; j++) {
            tableRow.addView(createLinear(Color.BLACK, param));
        }

        calendar.addView(tableRow);
    }

    private TableRow createTableRow(ViewGroup.LayoutParams params) {
        TableRow tableRow = new TableRow(this);
        tableRow.setBackgroundColor(Color.BLACK);
        tableRow.setPadding(0, 1, 0, 1);
        tableRow.setLayoutParams(params);
        tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
        return tableRow;
    }

    private TextView createText(String text, int gravity) {
        TextView reson = new TextView(this);
        reson.setText(text);
        reson.setPadding(4, 4, 4, 4);
        reson.setGravity(gravity);
        reson.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        return reson;
    }

    private LinearLayout createLinear(int backgroundColor, LinearLayout.LayoutParams param) {
        LinearLayout cell = new LinearLayout(this);
        cell.setBackgroundColor(backgroundColor);
        cell.setGravity(Gravity.CENTER_HORIZONTAL);
        cell.setLayoutParams(param);
        cell.setOrientation(LinearLayout.VERTICAL);
        return cell;
    }

    private View createField(Statable stateShift, String day, LinearLayout.LayoutParams param) {

        LinearLayout cell = createLinear(stateShift.getColor(), param);
        TextView upperText = createText(day, Gravity.LEFT);
        upperText.setTextColor(Color.parseColor(Constants.COLOR_DAY));
        upperText.setTypeface(null, Typeface.ITALIC);
        upperText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        upperText.setPadding(4,0,4,0);
        TextView textSign = createText(stateShift.getStatSign(), Gravity.RIGHT);
        textSign.setTextColor(Color.BLACK);
        textSign.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        textSign.setPadding(0,0,4,0);

        cell.addView(upperText);
        cell.addView(textSign);
        return cell;
    }

}
