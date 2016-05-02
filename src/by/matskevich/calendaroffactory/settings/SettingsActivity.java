package by.matskevich.calendaroffactory.settings;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import by.matskevich.calendaroffactory.BusinessLogic;
import by.matskevich.calendaroffactory.CharShift;
import by.matskevich.calendaroffactory.CharShift12;
import by.matskevich.calendaroffactory.CharShift8;
import by.matskevich.calendaroffactory.CharShiftDay;
import by.matskevich.calendaroffactory.R;
import by.matskevich.calendaroffactory.TypeShift;

public class SettingsActivity extends Activity {

	private static final String HEAP_8 = "Наименования 8-х часовых смен";
	private static final String HEAP_12 = "Наименования 12-х часовых смен";
	private static final String HEAP_DAY = "Наименования смен 2 через 2 по 12ч";
	private int selectedRadio;
	protected RadioGroup radioGroup;
	private RadioButton rbutton12;
	private RadioButton rbutton8;
	private RadioButton rbuttonDay;
	private TableLayout table8;
	private TableLayout table12;
	private TableLayout tableDay;
	private TypeShift type = TypeShift.TWELFTH;
	private SharedPreferences mSettings;
	private Map<CharShift, EditText> nameChars = new HashMap<CharShift, EditText>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		mSettings = getSharedPreferences(BusinessLogic.APP_PREFERENCE, Context.MODE_PRIVATE);
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
		rbutton8 = (RadioButton) findViewById(R.id.radioButton8);
		rbutton12 = (RadioButton) findViewById(R.id.radioButton12);
		rbuttonDay = (RadioButton) findViewById(R.id.radioButtonDay);
		table8 = (TableLayout) findViewById(R.id.table_shift8);
		table12 = (TableLayout) findViewById(R.id.table_shift12);
		tableDay = (TableLayout) findViewById(R.id.table_shift_day);
		nameChars.clear();
		buildTables(table8, CharShift8.class, HEAP_8);
		buildTables(table12, CharShift12.class, HEAP_12);
		buildTables(tableDay, CharShiftDay.class, HEAP_DAY);
		setRadioButton();
		selectedRadio = radioGroup.getCheckedRadioButtonId();
	}

	private void setRadioButton() {
		if (mSettings.contains(TypeShift.TYPE_SHIFT)) {
			radioGroup.clearCheck();
			type = TypeShift.valueOf(mSettings.getString(TypeShift.TYPE_SHIFT, TypeShift.TWELFTH.toString()));
			switch (type) {
			case EIGHT:
				rbutton8.setChecked(true);
				setVisible8();
				break;
			case DAY:
				rbuttonDay.setChecked(true);
				setVisibleDay();
				break;
			default:
				rbutton12.setChecked(true);
				setVisible12();
			}
		}
	}

	private void buildTables(TableLayout table, Class<? extends CharShift> charShift, String sheap) {

		table.removeAllViews();
		TableRow rowHeap = new TableRow(this);
		TextView heap = new TextView(this);
		heap.setText(sheap);
		heap.setGravity(Gravity.CENTER_HORIZONTAL);
		heap.setBackgroundColor(Color.YELLOW);
		rowHeap.addView(heap);
		table.addView(rowHeap);
		InputFilter[] filterArray = new InputFilter[1];
		filterArray[0] = new InputFilter.LengthFilter(10);
		for (CharShift shift : charShift.getEnumConstants()) {
			EditText col2 = createColumn(new EditText(this), shift.getNameChar());
			col2.setFilters(filterArray);
			col2.setHint(shift.getChar());
			nameChars.put(shift, col2);
			TableRow row = new TableRow(this);
			row.addView(col2);
			table.addView(row);
		}

	}

	private <T extends TextView> T createColumn(T col1, String str) {
		col1.setText(str);
		col1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
		col1.setTextColor(Color.BLACK);
		col1.setGravity(Gravity.LEFT);
		col1.setTextAppearance(this, android.R.attr.textAppearanceMedium);
		return col1;
	}

	public void onClickRadioSelect(View v) {
		if (selectedRadio != v.getId()) {
			selectedRadio = v.getId();
			switch (selectedRadio) {
			case R.id.radioButton8:
				type = TypeShift.EIGHT;
				setVisible8();
				break;
			case R.id.radioButtonDay:
				type = TypeShift.DAY;
				setVisibleDay();
				break;
			default:
				type = TypeShift.TWELFTH;
				setVisible12();
			}
			SharedPreferences.Editor editor = mSettings.edit();
			editor.putString(TypeShift.TYPE_SHIFT, type.toString());
			editor.apply();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		SharedPreferences.Editor editor = mSettings.edit();
		for (CharShift charShift : nameChars.keySet()) {
			String name = nameChars.get(charShift).getText().toString();
			if (name == null || name.trim().isEmpty()) {
				name = charShift.getChar();
			}
			editor.putString(charShift.toString(), name);
			charShift.setNameChar(name);
		}
		editor.apply();
	}

	private void setVisible8() {
		table8.setVisibility(View.VISIBLE);
		table12.setVisibility(View.GONE);
		tableDay.setVisibility(View.GONE);
	}

	private void setVisible12() {
		table8.setVisibility(View.GONE);
		table12.setVisibility(View.VISIBLE);
		tableDay.setVisibility(View.GONE);
	}

	private void setVisibleDay() {
		table8.setVisibility(View.GONE);
		table12.setVisibility(View.GONE);
		tableDay.setVisibility(View.VISIBLE);
	}

}
