package edu.cs4730.qrDemo;

import android.support.v4.app.FragmentActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/*
 * Most of the code is in the MainFragment.  The activity has the 
 * onActivityResult() method here, to get the information on the return from scanning a QR code.
 * 	
 */

public class MainActivity extends FragmentActivity {
	MainFragment myMainFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
			myMainFragment = new MainFragment();
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, myMainFragment).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	//wait for result from startActivityForResult calls.
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		
		//code to handle the intentintegrator, then 
		IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		if (scanResult != null) {
			// handle scan result
			String contents = scanResult.getContents();
			if (contents != null) {
				//myMainFragment.logthis("[II] Scan result is "+ scanResult.toString());
				myMainFragment.logthis(""+ scanResult.toString());
			} else {
				myMainFragment.logthis("[II] Scan failed or canceled");
			}

		} else if (requestCode == 0) {
			//normal intent return codes.
			if (resultCode == Activity.RESULT_OK) {
				String contents = intent.getStringExtra("SCAN_RESULT");
				String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
				// Handle successful scan
				myMainFragment.logthis("[I] scan Result is " + contents);
				myMainFragment.logthis("[I] scan Format is " + format);
			} else if (resultCode == Activity.RESULT_CANCELED) {
				// Handle cancel
				myMainFragment.logthis("[I] scan cancel");
			}
		}
	}
}
