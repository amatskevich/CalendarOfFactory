package by.matskevich.calendaroffactory.settings;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import by.matskevich.calendaroffactory.BusinessLogic;
import by.matskevich.calendaroffactory.CharShift;
import by.matskevich.calendaroffactory.CharShift12;
import by.matskevich.calendaroffactory.CharShift8;
import by.matskevich.calendaroffactory.R;
import by.matskevich.calendaroffactory.TypeShift;

public class SettingsActivity extends Activity {

	private int selectedRadio;
	protected RadioGroup radioGroup;
	private RadioButton rbutton12;
	private RadioButton rbutton8;
	private TableLayout table8;
	private TableLayout table12;
	private TypeShift type = TypeShift.TWELFTH;
	private SharedPreferences mSettings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		mSettings = getSharedPreferences(BusinessLogic.APP_PREFERENCE, Context.MODE_PRIVATE);
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
		rbutton8 = (RadioButton) findViewById(R.id.radioButton8);
		rbutton12 = (RadioButton) findViewById(R.id.radioButton12);
		table8 = (TableLayout) findViewById(R.id.table_shift8);
		table12 = (TableLayout) findViewById(R.id.table_shift12);
		buildTables(table8, CharShift8.class);
		buildTables(table12, CharShift12.class);
		setRadioButton();
		selectedRadio = radioGroup.getCheckedRadioButtonId();
	}

	private void setRadioButton() {
		if (mSettings.contains(TypeShift.TYPE_SHIFT)) {
			radioGroup.clearCheck();
			type = TypeShift.valueOf(mSettings.getString(TypeShift.TYPE_SHIFT, TypeShift.TWELFTH.toString()));
			Log.d("MY", type.toString());
			if (type == TypeShift.EIGHT) {
				rbutton8.setChecked(true);
			} else {
				rbutton12.setChecked(true);
			}
		}
	}

	private void buildTables(TableLayout table, Class<? extends CharShift> charShift) {
		table.removeAllViews();
		for (CharShift shift : charShift.getEnumConstants()) {
			TextView col1 = createColumn(shift.getChar());
			TextView col2 = createColumn(shift.getNameChar());
			TableRow row = new TableRow(this);
			row.addView(col1);
			row.addView(col2);
			table.addView(row);
		}
	}

	private TextView createColumn(String str) {
		TextView col1 = new TextView(this);
		col1.setText(str);
		col1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
		col1.setBackgroundColor(Color.WHITE);
		col1.setTextColor(Color.BLACK);
		col1.setGravity(Gravity.CENTER_HORIZONTAL);
		col1.setTextAppearance(this, android.R.attr.textAppearanceMedium);
		return col1;
	}

	public void onClickRadioSelect(View v) {
		if (selectedRadio != v.getId()) {
			selectedRadio = v.getId();
			type = type == TypeShift.EIGHT ? TypeShift.TWELFTH : TypeShift.EIGHT;
			SharedPreferences.Editor editor = mSettings.edit();
			editor.putString(TypeShift.TYPE_SHIFT, type.toString());
			// editor.apply();
			editor.commit();
		}
	}
}
