package by.matskevich.calendaroffactory.calendar;

import android.app.Activity;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import by.matskevich.calendaroffactory.BusinessLogic;
import by.matskevich.calendaroffactory.Statable;
import by.matskevich.calendaroffactory.TypeShift;

import java.util.HashMap;
import java.util.Map;

public class CalendarPopupManager {

    public void buildPopupTable(final TableLayout popupTable, final Activity activity) {

        final TypeShift typeShift = BusinessLogic.getInstance().getTypeShift();
        final Statable[] stateShifts = typeShift.stateShift.getEnumConstants();
        final Map<String, Statable> map = new HashMap<String, Statable>();
        for (final Statable stateShift : stateShifts) {

            map.put(stateShift.getDescription(), stateShift);
        }

        int i = 0;

        for (final Map.Entry<String, Statable> entry : map.entrySet()) {

            i = ++i == map.size() ? -1 : i;
            final Statable state = entry.getValue();
            final TableRow tableRow = createTableRow(activity, state.getColor(), i);
            final View signCell = createSignField(state, activity);
            final View descriptionCell = createDescriptionField(entry.getKey(), activity);
            tableRow.addView(signCell);
            tableRow.addView(descriptionCell);
            popupTable.addView(tableRow);
        }

    }

    private TextView createText(String text, int gravity, Activity activity) {

        final TextView textView = new TextView(activity);
        textView.setText(text);
        textView.setGravity(gravity);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        return textView;
    }

    private TableRow createTableRow(Activity activity, int color, int position) {

        final TableRow tableRow = new TableRow(activity);
        tableRow.setBackgroundColor(color);

        TableLayout.LayoutParams llp = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT);

        switch (position) {
            case 1:
                llp.setMargins(20, 20, 20, 0);
                break;
            case -1:
                llp.setMargins(20, 2, 20, 20);
                break;
            default:
                llp.setMargins(20, 2, 20, 0);

        }

        tableRow.setLayoutParams(llp);
        tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
        return tableRow;
    }

    private View createSignField(Statable stateShift, Activity activity) {

        TextView textSign = createText(stateShift.getStatSign(), Gravity.CENTER, activity);
        textSign.setTextColor(Color.BLACK);
        textSign.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        textSign.setPadding(40,0,40,0);
        return textSign;
    }

    private View createDescriptionField(final String description, Activity activity) {

        TextView descriptionText = createText(description, Gravity.CENTER_VERTICAL, activity);
        descriptionText.setTextColor(Color.BLACK);
        descriptionText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        descriptionText.setPadding(40,0,40,0);
        return descriptionText;
    }
}
