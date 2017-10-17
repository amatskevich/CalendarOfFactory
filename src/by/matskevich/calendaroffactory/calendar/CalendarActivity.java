package by.matskevich.calendaroffactory.calendar;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import by.matskevich.calendaroffactory.BusinessLogic;
import by.matskevich.calendaroffactory.CharShift;
import by.matskevich.calendaroffactory.CharShift12;
import by.matskevich.calendaroffactory.CharShift8;
import by.matskevich.calendaroffactory.CharShiftDay;
import by.matskevich.calendaroffactory.R;
import by.matskevich.calendaroffactory.Statable;
import by.matskevich.calendaroffactory.TypeShift;
import by.matskevich.calendaroffactory.util.Constants;
import by.matskevich.calendaroffactory.util.Utils;
import by.matskevich.calendaroffactory.workedHours.WorkHoursCalculator;
import by.matskevich.calendaroffactory.workedHours.WorkHoursDto;

public class CalendarActivity extends Activity {

    private static Map<Pair<Statable, Calendar>, WorkHoursDto> cashWorksHours =
            new HashMap<Pair<Statable, Calendar>, WorkHoursDto>();

    private TableLayout calendar;
    private TextView monthText;
    private TextView shiftText;
    private TableLayout workedHoursTable;
    private TextView fullHours;
    private TextView normalHours;
    private TextView overHours;
    private TextView holidayHours;
    private TextView numberOfShift;

    private Calendar currentDate;
    private Calendar date;
    private CharShift shift;
    private TypeShift typeShift;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        currentDate = Calendar.getInstance();
        currentDate.setFirstDayOfWeek(Calendar.MONDAY);

        Intent intent = getIntent();
        try {
            shift = findShift(intent.getStringExtra("shift"));
        } catch (IllegalArgumentException iae) {
            Log.e("mtsk", "not find shift", iae);
            showToastWithException();
            finish();
            return;
        }
        date = createDate(intent.getLongExtra("time", new Date().getTime()));

        calendar = (TableLayout) findViewById(R.id.calendar_view);
        monthText = (TextView) findViewById(R.id.month);
        shiftText = (TextView) findViewById(R.id.shift_char);
        workedHoursTable = (TableLayout) findViewById(R.id.worked_hours_view);
        fullHours = (TextView) findViewById(R.id.fullHours);
        normalHours = (TextView) findViewById(R.id.normalHours);
        overHours = (TextView) findViewById(R.id.overHours);
        holidayHours = (TextView) findViewById(R.id.holidayHours);
        numberOfShift = (TextView) findViewById(R.id.numberOfShift_text);

        calendar.setOnTouchListener(new OnSwipeTouchListener(CalendarActivity.this) {
            public void onSwipeRight() {
                rebuildView(-1);
            }

            public void onSwipeLeft() {
                rebuildView(1);
            }

            private void rebuildView(int i) {
                date.add(Calendar.MONTH, i);
                setMonthText(date);
                calendar.removeAllViews();
                buildTable();
            }
        });

        setMonthText(date);
        shiftText.setText("Смена: " + shift.getNameChar());

        buildTable();
    }

    private void showToastWithException() {

        Toast toast = Toast.makeText(getApplicationContext(),
                R.string.exception_try_again, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        ViewGroup group = (ViewGroup) toast.getView();
        TextView messageTextView = (TextView) group.getChildAt(0);
        messageTextView.setTextSize(20);
        toast.show();
    }

    private void setMonthText(Calendar d) {
        monthText.setText(MonthRus.values()[d.get(Calendar.MONTH)].name + " " + d.get(Calendar.YEAR));
    }

    private void buildTable() {
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        TableRow.LayoutParams param = new TableRow.LayoutParams();
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

        int step = Utils.getStepOfCycle(typeShift, firstDay);
        for (CharShift cs : typeShift.charShift.getEnumConstants()) {
            if (cs == shift) {
                break;
            }
            step = (2 + step) % typeShift.cycleDays;
        }
        Statable stateShift = typeShift.stateShift.getEnumConstants()[step];

        buildWorkedHours(new Pair(stateShift, firstDay));

        int weekSize = 7;
        int dayWeek = (firstDay.get(Calendar.DAY_OF_WEEK) + 5) % weekSize;// start_from_monday
        int maxDays = firstDay.getActualMaximum(Calendar.DAY_OF_MONTH);
        TableRow tableRow = createTableRow(params);
        // add empty fields
        for (int j = 0; j < dayWeek; j++) {
            tableRow.addView(createLinear(Color.WHITE, param));
        }
        for (int i = 1; i <= maxDays; i++, dayWeek++) {
            if (dayWeek >= weekSize) {
                dayWeek %= weekSize;
                calendar.addView(tableRow);
                tableRow = createTableRow(params);
            }
            tableRow.addView(createField(stateShift, ((Integer) i).toString(), param));
            stateShift = stateShift.next();
        }
        // add empty fields
        for (int j = dayWeek; j < weekSize; j++) {
            tableRow.addView(createLinear(Color.WHITE, param));
        }

        calendar.addView(tableRow);
        setCurrentDateIfNeed();
    }

    private void buildWorkedHours(final Pair pair) {

        if (!cashWorksHours.containsKey(pair)) {
            cashWorksHours.put(pair, WorkHoursCalculator.calculate((Calendar) pair.second,
                    (Statable) pair.first));
        }
        WorkHoursDto workHoursDto = cashWorksHours.get(pair);
        fullHours.setText(workHoursDto.getFullHoursText());
        normalHours.setText(workHoursDto.getNormalHoursText());
        overHours.setText(workHoursDto.getOverHoursText());
        holidayHours.setText(workHoursDto.getHolidayHoursText());
        numberOfShift.setText(shift.getTypeShift().numberOfShiftStr);
        workedHoursTable.setVisibility(workHoursDto.isSupported() ? View.VISIBLE : View.INVISIBLE);
    }

    private void setCurrentDateIfNeed() {
        if (date.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR)
                && date.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH)) {
            int curNumRow = currentDate.get(Calendar.WEEK_OF_MONTH);
            int curNumCol = (currentDate.get(Calendar.DAY_OF_WEEK) + 5) % 7;// 0 - Monday
            try {
                TableRow curRow = (TableRow) calendar.getChildAt(curNumRow);
                LinearLayout curField = (LinearLayout) curRow.getChildAt(curNumCol);
                View txt = curField.getChildAt(0);
                txt.setBackgroundColor(Color.YELLOW);
            } catch (ClassCastException ex) {
                Log.e("mtsk", "Not marked current date", ex);
            } catch (NullPointerException ex) {
                Log.e("mtsk", "Not marked current date", ex);
            }
        }
    }

    private View createField(Statable stateShift, String day, android.widget.TableRow.LayoutParams param) {
        LinearLayout cell = createLinear(stateShift.getColor(), param);
        TextView textDay = createText(day, Gravity.LEFT);
        textDay.setTextColor(Color.parseColor(Constants.COLOR_DAY));
        textDay.setTypeface(null, Typeface.ITALIC);
        textDay.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        textDay.setPadding(4,0,4,0);
        TextView textSign = createText(stateShift.getStatSign(), Gravity.RIGHT);
        textSign.setTextColor(Color.BLACK);
        textSign.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        textSign.setPadding(4,0,4,0);

        cell.addView(textDay);
        cell.addView(textSign);
        return cell;
    }

    private LinearLayout createLinear(int backgroundColor, android.widget.TableRow.LayoutParams param) {
        LinearLayout cell = new LinearLayout(this);
        cell.setBackgroundColor(backgroundColor);
        cell.setGravity(Gravity.CENTER_HORIZONTAL);
        cell.setLayoutParams(param);
        cell.setOrientation(LinearLayout.VERTICAL);
        return cell;
    }

    private TextView createText(String text, int gravity) {
        TextView reson = new TextView(this);
        reson.setText(text);
        reson.setPadding(4, 4, 4, 4);
        reson.setGravity(gravity);
        reson.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        return reson;
    }

    private TableRow createTableRow(LayoutParams params) {
        TableRow tableRow = new TableRow(this);
        tableRow.setBackgroundColor(Color.BLACK);
        tableRow.setPadding(0, 1, 0, 1);
        tableRow.setLayoutParams(params);
        tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
        return tableRow;
    }

    private Calendar createDate(long longExtra) {
        Calendar res = Calendar.getInstance();
        res.setTimeInMillis(longExtra);
        return res;
    }

    private CharShift findShift(String stringExtra) {
        CharShift res;
        typeShift = BusinessLogic.getInstance().getTypeShift();
        switch (typeShift) {
            case TWELFTH:
                res = CharShift12.valueOf(stringExtra);
                break;
            case EIGHT:
                res = CharShift8.valueOf(stringExtra);
                break;
            case DAY:
                res = CharShiftDay.valueOf(stringExtra);
                break;
            default:
                throw new RuntimeException();
        }
        return res;
    }

}
