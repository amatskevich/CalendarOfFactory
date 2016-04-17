package by.matskevich.calendaroffactory;

import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		TextView version = (TextView) findViewById(R.id.version_text);
		String numVers;
		try {
			numVers = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			numVers = "?";
			e.printStackTrace();
		}
		version.setText("Версия - " + numVers);
		;
	}
}
