package by.matskevich.calendaroffactory.calendar;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.*;

class SalaryDateManager {

    private final static Map<String, List<Integer>> SALARY_DAYS = new HashMap<String, List<Integer>>(12);

    static {

        SALARY_DAYS.put("0_2018", Arrays.asList(10, 25));
        SALARY_DAYS.put("1_2018", Arrays.asList(9, 23));
        SALARY_DAYS.put("2_2018", Arrays.asList(7, 23));
        SALARY_DAYS.put("3_2018", Arrays.asList(10, 25));
        SALARY_DAYS.put("4_2018", Arrays.asList(10, 25));
        SALARY_DAYS.put("5_2018", Arrays.asList(8, 25));
        SALARY_DAYS.put("6_2018", Arrays.asList(10, 25));
        SALARY_DAYS.put("7_2018", Arrays.asList(10, 24));
        SALARY_DAYS.put("8_2018", Arrays.asList(10, 25));
        SALARY_DAYS.put("9_2018", Arrays.asList(10, 25));
        SALARY_DAYS.put("10_2018", Arrays.asList(9, 23));
        SALARY_DAYS.put("11_2018", Arrays.asList(10, 22));

        SALARY_DAYS.put("0_2019", Arrays.asList(10, 25));
        SALARY_DAYS.put("1_2019", Arrays.asList(8, 25));
        SALARY_DAYS.put("2_2019", Arrays.asList(7, 25));
        SALARY_DAYS.put("3_2019", Arrays.asList(10, 25));
        SALARY_DAYS.put("4_2019", Arrays.asList(10, 24));
        SALARY_DAYS.put("5_2019", Arrays.asList(10, 25));
        SALARY_DAYS.put("6_2019", Arrays.asList(10, 25));
        SALARY_DAYS.put("7_2019", Arrays.asList(9, 23));
        SALARY_DAYS.put("8_2019", Arrays.asList(10, 25));
        SALARY_DAYS.put("9_2019", Arrays.asList(10, 25));
        SALARY_DAYS.put("10_2019", Arrays.asList(6, 25));
        SALARY_DAYS.put("11_2019", Arrays.asList(10, 24));
    }

    private LinearLayout createLinearHor(Activity activity, int backgroundColor) {
        LinearLayout cell = new LinearLayout(activity);
        cell.setBackgroundColor(backgroundColor);
        cell.setGravity(Gravity.CENTER);
        cell.setOrientation(LinearLayout.HORIZONTAL);
        return cell;
    }


    View createBottomSalaryText(final Activity activity, final TextView textSign, int color) {

        final LinearLayout cell = createLinearHor(activity, color);
        final TextView textSalary = createSalaryText(activity);
        textSalary.setLayoutParams(getTextParams());
        textSign.setLayoutParams(getTextParams());
        cell.addView(textSalary);
        cell.addView(textSign);
        return cell;
    }

    private LinearLayout.LayoutParams getTextParams() {

        return new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1f
        );
    }

    private TextView createSalaryText(Activity activity) {
        TextView res = new TextView(activity);
        res.setText("$");
        res.setGravity(Gravity.LEFT);
        res.setTextColor(Color.rgb(0, 51, 0));
        res.setTypeface(null, Typeface.BOLD);
        res.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        res.setPadding(4, 0, 0, 0);
        return res;
    }

    List<Integer> getSalaryDays(final String monthYear) {

        return SALARY_DAYS.get(monthYear);
    }
}
