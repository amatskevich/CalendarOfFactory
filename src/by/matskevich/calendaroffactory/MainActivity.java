package by.matskevich.calendaroffactory;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	private int selectedRadio;
	protected RadioGroup radioGroup;
	protected Button addDay;
	protected Button decreaseDay;
	protected TextView dateView;
	protected TableLayout table;
	private BusinessLogic bLogic = new BusinessLogic();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
		selectedRadio = radioGroup.getCheckedRadioButtonId();
		addDay = (Button) findViewById(R.id.date_up);
		addDay.setOnClickListener(this);
		decreaseDay = (Button) findViewById(R.id.date_down);
		decreaseDay.setOnClickListener(this);
		dateView = (TextView) findViewById(R.id.date);
		table = (TableLayout) findViewById(R.id.table_layout1);
		refreshViews();
	}

	@Override
	public void onClick(View v) {
		if (v == addDay) {
			bLogic.dayUp();
			refreshViews();
		} else if (v == decreaseDay) {
			bLogic.dayDown();
			refreshViews();
		}

	}

	public void onClickRadioSelect(View v) {
		if (selectedRadio != v.getId()) {
			selectedRadio = v.getId();
			Log.d("onCreate", "selectedTest=" + selectedRadio);
		}
	}

	private void refreshViews() {
		dateView.setText(bLogic.getDate());
		table.removeAllViews();
		for (Shift shift : bLogic.getShiftList()) {
			TextView col1 = createColumn(shift.getCharShift().getChar());
			TextView col2 = createColumn(shift.getStateShift().getState());
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
}
