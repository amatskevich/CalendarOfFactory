package by.matskevich.calendaroffactory.calendar;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
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

public class CalendarActivity extends Activity {

	private TableLayout calendar;
	private TextView monthText;
	private TextView shiftText;

	private Calendar date;
	private CharShift shift;
	private TypeShift typeShift;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar);

		calendar = (TableLayout) findViewById(R.id.calendar_view);
		monthText = (TextView) findViewById(R.id.month);
		shiftText = (TextView) findViewById(R.id.shift_char);

		Intent intent = getIntent();
		shift = findShift(intent.getStringExtra(Constants.EXTRA_SHIFT));
		date = createDate(intent.getLongExtra(Constants.EXTRA_DATE, new Date().getTime()));
		monthText.setText(MonthRus.values()[date.get(Calendar.MONTH)].name);
		shiftText.setText(shift.getNameChar());

		buildTable();
	}

	private void buildTable() {
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		TableRow.LayoutParams param = new TableRow.LayoutParams();
		param.setMargins(0, 0, 1, 1);

		TableRow head = createTableRow(params);
		for (String day : Constants.WEEK_DAYS) {
			head.addView(createField(day, "", param));
		}
		calendar.addView(head);

		Calendar firstDay = Calendar.getInstance();
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

		int weekSize = 7;
		int dayWeek = (firstDay.get(Calendar.DAY_OF_WEEK) + 5) % weekSize;// start_from_monday
		int maxDays = firstDay.getActualMaximum(Calendar.DAY_OF_MONTH);
		TableRow tableRow = createTableRow(params);
		// add empty fields
		for (int j = 0; j < dayWeek; j++) {
			tableRow.addView(createField(" ", "", param));
		}
		for (int i = 1; i <= maxDays; i++, dayWeek++) {
			if (dayWeek >= weekSize) {
				dayWeek %= weekSize;
				calendar.addView(tableRow);
				tableRow = createTableRow(params);
			}
			tableRow.addView(createField(stateShift.getStatSign(), ((Integer) i).toString(), param));
			stateShift = stateShift.next();
		}
		// add empty fields
		for (int j = dayWeek; j < weekSize; j++) {
			tableRow.addView(createField(" ", "", param));
		}

		calendar.addView(tableRow);
	}

	private View createField(String sign, String day, android.widget.TableRow.LayoutParams param) {
		LinearLayout cell = createLinear(param);
		TextView textSign = createText(sign, Gravity.LEFT);
		TextView textDay = createText(day, Gravity.RIGHT);

		cell.addView(textSign);
		cell.addView(textDay);
		return cell;
	}

	private LinearLayout createLinear(android.widget.TableRow.LayoutParams param) {
		LinearLayout cell = new LinearLayout(this);
		cell.setBackgroundColor(Color.rgb(255, 255, 255));
		cell.setGravity(Gravity.CENTER_HORIZONTAL);
		cell.setLayoutParams(param);
		cell.setOrientation(LinearLayout.VERTICAL);
		return cell;
	}

	private TextView createText(String text, int gravity) {
		TextView reson = new TextView(this);
		reson.setText(text);
		reson.setPadding(0, 0, 4, 4);
		reson.setGravity(gravity);
		return reson;
	}

	private TableRow createTableRow(LayoutParams params) {
		TableRow tableRow = new TableRow(this);
		tableRow.setBackgroundColor(Color.BLACK);
		tableRow.setPadding(0, 1, 0, 1);
		tableRow.setLayoutParams(params);
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
