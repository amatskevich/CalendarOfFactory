package by.matskevich.calendaroffactory.settings;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
	private Map<CharShift, EditText> nameChars = new HashMap<CharShift, EditText>();

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
		nameChars.clear();
		buildTables(table8, CharShift8.class, "8-ми часовые смены");
		buildTables(table12, CharShift12.class, "12-ти часовые смены");
		setRadioButton();
		selectedRadio = radioGroup.getCheckedRadioButtonId();
	}

	private void setRadioButton() {
		if (mSettings.contains(TypeShift.TYPE_SHIFT)) {
			radioGroup.clearCheck();
			type = TypeShift.valueOf(mSettings.getString(TypeShift.TYPE_SHIFT, TypeShift.TWELFTH.toString()));
			if (type == TypeShift.EIGHT) {
				rbutton8.setChecked(true);
			} else {
				rbutton12.setChecked(true);
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
		for (CharShift shift : charShift.getEnumConstants()) {
			// TextView col1 = createColumn(new TextView(this),
			// shift.getChar());
			EditText col2 = createColumn(new EditText(this), shift.getNameChar());
			nameChars.put(shift, col2);
			// col2.setOnEditorActionListener(el);
			TableRow row = new TableRow(this);
			// row.addView(col1);
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
			type = type == TypeShift.EIGHT ? TypeShift.TWELFTH : TypeShift.EIGHT;
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
			editor.putString(charShift.toString(), name);
			charShift.setNameChar(name);
		}
		editor.apply();
	}

}
