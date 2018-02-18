package by.matskevich.calendaroffactory.calendar;

import java.util.*;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.*;
import android.view.ViewGroup.LayoutParams;
import android.widget.*;
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

    private CalendarTableLayout calendar;
    private TextView monthText;
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
    private SalaryDateManager salaryManager;
    private CalendarPopupManager popupManager;

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

        calendar = (CalendarTableLayout) findViewById(R.id.calendar_view);
        monthText = (TextView) findViewById(R.id.month);
        TextView shiftText = (TextView) findViewById(R.id.shift_char);
        workedHoursTable = (TableLayout) findViewById(R.id.worked_hours_view);
        fullHours = (TextView) findViewById(R.id.fullHours);
        normalHours = (TextView) findViewById(R.id.normalHours);
        overHours = (TextView) findViewById(R.id.overHours);
        holidayHours = (TextView) findViewById(R.id.holidayHours);
        numberOfShift = (TextView) findViewById(R.id.numberOfShift_text);
        popupManager = new CalendarPopupManager();

        calendar.setOnTouchListener(new OnSwipeTouchListener(CalendarActivity.this) {
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

            @Override
            public void onTouchPopupShow() {

                // get a reference to the already created main layout
                ScrollView calendarLayout = (ScrollView)
                        findViewById(R.id.activity_calendar);

                // inflate the layout of the popup window
                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.popup_calendar_legend, null);

                // create the popup window
                final PopupWindow popupWindow = new PopupWindow(popupView,
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,
                        true);

                // show the popup window
                popupWindow.showAtLocation(calendarLayout, Gravity.CENTER_HORIZONTAL, 0, -50);

                final TableLayout popupTable = (TableLayout) popupView.findViewById(R.id.popup_table);
                popupManager.buildPopupTable(popupTable, CalendarActivity.this);

                // dismiss the popup window when touched
                popupView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });
            }
        });

        setMonthText(date);
        shiftText.setText("Смена: " + shift.getNameChar());

        salaryManager = new SalaryDateManager();

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

        TableRow.LayoutParams param = new TableRow.LayoutParams(
                0, LayoutParams.MATCH_PARENT);
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

        final List<Integer> salaryDays = salaryManager
                .getSalaryDays(date.get(Calendar.MONTH) + "_" + date.get(Calendar.YEAR));
        int weekSize = 7;
        int dayWeek = (firstDay.get(Calendar.DAY_OF_WEEK) + 5) % weekSize;// start_from_monday
        int maxDays = firstDay.getActualMaximum(Calendar.DAY_OF_MONTH);
        TableRow tableRow = createTableRow(params);
        // add empty fields
        for (int j = 0; j < dayWeek; j++) {
            tableRow.addView(createLinear(Color.BLACK, param));
        }

        for (Integer i = 1; i <= maxDays; i++, dayWeek++) {
            if (dayWeek >= weekSize) {
                dayWeek %= weekSize;
                calendar.addView(tableRow);
                tableRow = createTableRow(params);
            }
            final boolean isSalaryDay = salaryDays != null && salaryDays.contains(i);
            final View cell = createField(stateShift, i.toString(), param, isSalaryDay);
            tableRow.addView(cell);
            stateShift = stateShift.next();
        }
        // add empty fields
        for (int j = dayWeek; j < weekSize; j++) {
            tableRow.addView(createLinear(Color.BLACK, param));
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

    private View createField(Statable stateShift, String day,
                             LinearLayout.LayoutParams param, final boolean salaryDay) {

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

        final View bottomText;
        if (salaryDay) {
            bottomText = salaryManager.createBottomSalaryText(this, textSign, stateShift.getColor());
        } else {
            bottomText = textSign;
        }
        cell.addView(upperText);
        cell.addView(bottomText);
        return cell;
    }

    private LinearLayout createLinear(int backgroundColor, LinearLayout.LayoutParams param) {
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
