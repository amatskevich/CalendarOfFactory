package by.matskevich.calendaroffactory.calendar;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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

		Intent intent = getIntent();
		shift = findShift(intent.getStringExtra(Constants.EXTRA_SHIFT));
		date = createDate(intent.getLongExtra(Constants.EXTRA_DATE, new Date().getTime()));

		calendar = (TableLayout) findViewById(R.id.calendar_view);
		monthText = (TextView) findViewById(R.id.month);
		shiftText = (TextView) findViewById(R.id.shift_char);

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
	}

	private View createField(Statable stateShift, String day, android.widget.TableRow.LayoutParams param) {
		LinearLayout cell = createLinear(stateShift.getColor(), param);
		TextView textDay = createText(day, Gravity.LEFT);
		textDay.setTextColor(Color.parseColor(Constants.COLOR_DAY));
		textDay.setTypeface(null, Typeface.BOLD_ITALIC);
		TextView textSign = createText(stateShift.getStatSign(), Gravity.RIGHT);
		textSign.setTextColor(Color.BLACK);

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
