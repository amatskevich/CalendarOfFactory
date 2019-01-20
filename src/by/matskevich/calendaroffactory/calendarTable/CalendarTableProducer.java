package by.matskevich.calendaroffactory.calendarTable;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import by.matskevich.calendaroffactory.calendar.CalendarTableLayout;
import by.matskevich.calendaroffactory.util.Constants;

import java.util.Calendar;

public class CalendarTableProducer {

    private final static int weekSize = 7;
    private final Activity activity;
    private final CalendarTableLayout calendar;

    public CalendarTableProducer(Activity activity, CalendarTableLayout calendar) {

        this.activity = activity;
        this.calendar = calendar;
    }

    public void buildTable(final Calendar date, final SpecializationExtenderFactory factory) {

        calendar.removeAllViews();

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

        final Calendar firstDay = getFirstDate(date);

        final SpecializationExtender extender = factory.getSpecializationFactory(firstDay);

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
            final View cell = createField(String.valueOf(i), param, extender);
            tableRow.addView(cell);
            extender.nextDay();
        }
        // add empty fields
        for (int j = dayWeek; j < weekSize; j++) {
            tableRow.addView(createLinear(Color.BLACK, param));
        }

        calendar.addView(tableRow);
    }

    private Calendar getFirstDate(Calendar date) {

        Calendar firstDay = Calendar.getInstance();
        firstDay.clear();
        firstDay.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.getActualMinimum(Calendar.DAY_OF_MONTH));
        return firstDay;
    }

    private TableRow createTableRow(ViewGroup.LayoutParams params) {
        TableRow tableRow = new TableRow(activity);
        tableRow.setBackgroundColor(Color.BLACK);
        tableRow.setPadding(0, 1, 0, 1);
        tableRow.setLayoutParams(params);
        tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
        return tableRow;
    }

    private TextView createText(String text, int gravity) {
        TextView textView = new TextView(activity);
        textView.setText(text);
        textView.setPadding(4, 4, 4, 4);
        textView.setGravity(gravity);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        return textView;
    }

    private LinearLayout createLinear(int backgroundColor, LinearLayout.LayoutParams param) {
        LinearLayout cell = new LinearLayout(activity);
        cell.setBackgroundColor(backgroundColor);
        cell.setGravity(Gravity.CENTER_HORIZONTAL);
        cell.setLayoutParams(param);
        cell.setOrientation(LinearLayout.VERTICAL);
        return cell;
    }

    private View createField(String day, LinearLayout.LayoutParams param, SpecializationExtender extender) {

        LinearLayout cell = createLinear(extender.getColorOfCell(), param);
        TextView upperText = createText(day, Gravity.LEFT);
        upperText.setTextColor(Color.parseColor(Constants.COLOR_DAY));
        upperText.setTypeface(null, Typeface.ITALIC);
        upperText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        upperText.setPadding(4,0,4,0);

        cell.addView(upperText);
        cell.addView(extender.createBottomText(activity));
        return cell;
    }
}
