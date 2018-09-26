package by.matskevich.calendaroffactory.mappingHoliday;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import by.matskevich.calendaroffactory.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChooseStaffsActivity extends Activity {

    private Spinner spinnerFirst;
    private Spinner spinnerSecond;
    private RadioGroup radioGroupFirst;
    private RadioGroup radioGroupSecond;
    private Button button;

    private HashMap<Integer, TypeShift> radiobuttonMap = new HashMap<Integer, TypeShift>(6);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_staffs);

        initMap();

        spinnerFirst = (Spinner) findViewById(R.id.spinner_1);
        spinnerSecond = (Spinner) findViewById(R.id.spinner_2);

        radioGroupFirst = (RadioGroup) findViewById(R.id.radioGroupStaffFirst);
        radioGroupSecond = (RadioGroup) findViewById(R.id.radioGroupStaffSecond);

        button = (Button) findViewById(R.id.show_common_holiday);
        addListener(button);

        radioGroupFirst.setOnCheckedChangeListener(createChangeListener(spinnerFirst));
        radioGroupSecond.setOnCheckedChangeListener(createChangeListener(spinnerSecond));
        initSpinners();
    }

    private void addListener(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ChooseStaffsActivity.this, MappingHolidaysActivity.class);

                intent.putExtra("shift_1", spinnerFirst.getId());
                intent.putExtra("shift_2", spinnerSecond.getId());
                startActivity(intent);
            }
        });
    }

    private void initSpinners() {

    }

    private void initMap() {
        radiobuttonMap.clear();
        radiobuttonMap.put(R.id.radio_1_12, TypeShift.TWELFTH);
        radiobuttonMap.put(R.id.radio_1_8, TypeShift.EIGHT);
        radiobuttonMap.put(R.id.radio_1_day, TypeShift.DAY);
        radiobuttonMap.put(R.id.radio_2_12, TypeShift.TWELFTH);
        radiobuttonMap.put(R.id.radio_2_8, TypeShift.EIGHT);
        radiobuttonMap.put(R.id.radio_2_day, TypeShift.DAY);
    }

    private RadioGroup.OnCheckedChangeListener createChangeListener(final Spinner spinner) {
        return new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final RadioGroup group, final int checkedId) {

                refreshSpinner(spinner, radiobuttonMap.get(checkedId));
            }
        };
    }

    private void refreshSpinner(final Spinner spinner, final TypeShift typeShift) {

        if (typeShift == null) {
            return;
        }

        final List<SpinnerItem> items = new ArrayList<SpinnerItem>();
        for (CharShift cs : typeShift.charShift.getEnumConstants()) {
            items.add(new SpinnerItem(cs, cs.getNameChar()));
        }
        ArrayAdapter<SpinnerItem> adapter = new ArrayAdapter<SpinnerItem>(this, android.R.layout.simple_spinner_item, items);
        spinner.setAdapter(adapter);
    }


}
