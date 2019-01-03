package by.matskevich.calendaroffactory;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import by.matskevich.calendaroffactory.calendar.CalendarActivity;
import by.matskevich.calendaroffactory.settings.SettingsActivity;
import by.matskevich.calendaroffactory.util.Constants;
import by.matskevich.calendaroffactory.util.RadioGroupShiftable;
import by.matskevich.calendaroffactory.util.Utils;

public class MainActivity extends Activity implements OnClickListener, RadioGroupShiftable {

	private SharedPreferences mSettings;
	protected Button addDay;
	protected Button decreaseDay;
	protected TextView dateView;
	protected TextView dateWeekView;
	protected TableLayout table;
	private BusinessLogic bLogic;

	DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			bLogic.changeDate(year, monthOfYear, dayOfMonth);
			refreshViews();
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		bLogic = BusinessLogic.getInstance();
		mSettings = getSharedPreferences(BusinessLogic.APP_PREFERENCE, Context.MODE_PRIVATE);
		addDay = (Button) findViewById(R.id.date_up);
		addDay.setOnClickListener(this);
		decreaseDay = (Button) findViewById(R.id.date_down);
		decreaseDay.setOnClickListener(this);
		dateView = (TextView) findViewById(R.id.date);
		dateView.setOnClickListener(this);
		dateWeekView = (TextView) findViewById(R.id.dateWeek);
		dateWeekView.setOnClickListener(this);
		table = (TableLayout) findViewById(R.id.table_layout1);
		readNameShift(CharShift8.class);
		readNameShift(CharShift12.class);
	}

	@Override
	protected void onStart() {
		super.onStart();

		// Установление типа
		if (mSettings.contains(TypeShift.TYPE_SHIFT)) {
			TypeShift type = TypeShift.valueOf(mSettings.getString(TypeShift.TYPE_SHIFT, TypeShift.TWELFTH.toString()));
			if (type != bLogic.getTypeShift()) {
				bLogic.changeTypeShift(type);
				((RadioGroup) findViewById(R.id.radioGroup1)).clearCheck();
			}
			switch (type) {
			case EIGHT:
				((RadioButton) findViewById(R.id.radioButton8)).setChecked(true);
				break;
			case DAY:
				((RadioButton) findViewById(R.id.radioButtonDay)).setChecked(true);
				break;
			default:
				((RadioButton) findViewById(R.id.radioButton12)).setChecked(true);
			}
		}
		refreshViews();
	}

	private void readNameShift(Class<? extends CharShift> charShift) {
		for (CharShift shift : charShift.getEnumConstants()) {
			if (mSettings.contains(shift.toString())) {
				shift.setNameChar(mSettings.getString(shift.toString(), "Error"));
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_settings) {
			Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
			startActivity(intent);
			return true;
		} else if (item.getItemId() == R.id.action_about) {
			Intent intent = new Intent(MainActivity.this, AboutActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		if (v == addDay) {
			bLogic.dayUp();
			refreshViews();
		} else if (v == decreaseDay) {
			bLogic.dayDown();
			refreshViews();
		} else if (v == dateView || v == dateWeekView) {
			DatePickerDialog dialog = new DatePickerDialog(MainActivity.this, dateListener, bLogic.getYear(),
					bLogic.getMonth(), bLogic.getDay());
			dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Сегодня", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface d, int id) {
					d.dismiss();
					Calendar c = Calendar.getInstance();
					bLogic.changeDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
					refreshViews();
				}
			});
			dialog.show();
		}

	}

	private void refreshViews() {
		dateView.setText(bLogic.getDate());
		dateWeekView.setText(bLogic.getDateWeek());
		table.removeAllViews();
		boolean rowColor = true;

		class RowListener implements OnClickListener {
			private final CharShift shift;

			public RowListener(CharShift shift) {
				this.shift = shift;
			}

			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, CalendarActivity.class);

				intent.putExtra("shift", shift.toString());
				intent.putExtra("time", bLogic.getDateLong());
				startActivity(intent);
			}
		}

		for (Shift shift : bLogic.getShiftList()) {
			TextView col1 = createColumn(shift.getCharShift().getNameChar());
			TextView col2 = createColumn(shift.getStateShift().getState());
			TableRow row = new TableRow(this);
			row.addView(col1);
			row.setBackgroundColor(Color.parseColor(rowColor ? Constants.COLOR_ROW1 : Constants.COLOR_ROW2));
			row.setPadding(0, 10, 0, 10);
			rowColor = !rowColor;
			row.addView(col2);
			row.setClickable(true);
			row.setOnClickListener(new RowListener(shift.getCharShift()));
			table.addView(row);
		}
	}

	private TextView createColumn(String str) {
		TextView col1 = new TextView(this);
		col1.setText(str);
		col1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
		col1.setTextColor(Color.BLACK);
		col1.setGravity(Gravity.CENTER_HORIZONTAL);
		col1.setTextAppearance(this, android.R.attr.textAppearanceMedium);
		return col1;
	}

	@Override
	public void onClickRadioSelect(View v) {
		Utils.onClickRadioSelect(v, this);
	}

	@Override
	public void do12() {
		refreshViews();
	}

	@Override
	public void doDay() {
		refreshViews();
	}

	@Override
	public void do8() {
		refreshViews();
	}

	@Override
	public Editor getSettingsEdit() {
		return mSettings.edit();
	}

	@Override
	public TypeShift getTypeShift() {
		return bLogic.getTypeShift();
	}

	@Override
	public void setTypeShift(TypeShift type) {
		bLogic.changeTypeShift(type);
	}

}
