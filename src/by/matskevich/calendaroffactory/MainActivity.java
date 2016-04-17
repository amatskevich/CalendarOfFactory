package by.matskevich.calendaroffactory;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import by.matskevich.calendaroffactory.settings.SettingsActivity;

public class MainActivity extends Activity implements OnClickListener {

	private static final String ROW1 = "#B9EFFF";
	private static final String ROW2 = "#FAFAFA";

	private SharedPreferences mSettings;
	protected Button addDay;
	protected Button decreaseDay;
	protected TextView dateView;
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
				bLogic.changeTypeShift();
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
		} else if (v == dateView) {
			new DatePickerDialog(MainActivity.this, dateListener, bLogic.getYear(), bLogic.getMonth(), bLogic.getDay())
					.show();
		}

	}

	private void refreshViews() {
		dateView.setText(bLogic.getDate());
		table.removeAllViews();
		boolean rowColor = true;
		for (Shift shift : bLogic.getShiftList()) {
			TextView col1 = createColumn(shift.getCharShift().getNameChar());
			TextView col2 = createColumn(shift.getStateShift().getState());
			TableRow row = new TableRow(this);
			row.addView(col1);
			row.setBackgroundColor(Color.parseColor(rowColor ? ROW1 : ROW2));
			rowColor = rowColor ? false : true;
			row.addView(col2);
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
}
