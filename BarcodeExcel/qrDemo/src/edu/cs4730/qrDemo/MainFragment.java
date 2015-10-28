package edu.cs4730.qrDemo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

//import xmu.swordbearer.excel2sqlite.DBAdapter;
//import xmu.swordbearer.excel2sqlite.Excel2SQLiteHelper;
//import xmu.swordbearer.excel2sqlite.MyCursorAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This is a demo of how to the the QR code from ZXing code
 * with the integrator and without.  All of this uses intents to
 * "talk" to read/create a QR code.
 * 
 * the return for the scan intent is in the mainActivity code, which then calls 
 * back into the fragment code to display the information via logthis() method.
 * 
 * NOTE. There is no easy way to tell the simulator to use a picture
 * so you need to scan via a actual device!
 */

public class MainFragment extends Fragment {


	TextView log;
	Button scani, encodei, encodeii,scanii;
	EditText edti, edtii;
	private Handler handler;
	public MainFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View myView = inflater.inflate(R.layout.fragment_main, container, false);

		// logger, to display text/info.
		log = (TextView) myView.findViewById(R.id.log);
		//Excel2SQLiteHelper.createExcel(this);

		handler = new Handler();
		//using only intents
		edti = (EditText) myView.findViewById(R.id.edti);
		scani = (Button) myView.findViewById(R.id.scani);
		encodei = (Button) myView.findViewById(R.id.encodei);
		Button.OnClickListener mScan = new Button.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent("com.google.zxing.client.android.SCAN");
				//intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
				intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE", "QR_CODE_MODE");

				startActivityForResult(intent, 0);
			}
		};
		scani.setOnClickListener(mScan);
		Button.OnClickListener mEncode = new Button.OnClickListener() {
			public void onClick(View v) {
				encodeBarcode("TEXT_TYPE", edti.getText().toString());
			}
		};
		encodei.setOnClickListener(mEncode);


		//using the intentintegrator
		scanii = (Button) myView.findViewById(R.id.scanii);
		encodeii = (Button) myView.findViewById(R.id.encodeii);

		Button.OnClickListener scanQRCode = new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				IntentIntegrator integrator = new IntentIntegrator(getActivity());
				integrator.initiateScan(IntentIntegrator.QR_CODE_TYPES);
			}
		};
		scanii.setOnClickListener(scanQRCode);
		Button.OnClickListener mEncodeii = new Button.OnClickListener() {
			public void onClick(View v) {
				//encodeBarcode2("TEXT_TYPE", edti.getText().toString());
				//encodeii.setText("dd");
				new Thread(new Runnable() {
					public void run() {
						importExcel2Sqlite();
					}
				}).start();
			}
		};
		encodeii.setOnClickListener(mEncodeii);

		return myView;
	}
	private void importExcel2Sqlite() {
		String cocnc =Environment.getExternalStorageDirectory().getAbsolutePath() +"/Download/students.xls";;
		//AssetManager am = this.getAssets();
		InputStream inStream;
		Workbook wb = null;
		try {
			// è¯»å�–.xlsæ–‡æ¡£:æ”¾åœ¨assetsæ–‡ä»¶å¤¹ä¸­
			//inStream = am.open("students.xls");
			File file = new File(cocnc);
			//inStream = am.open(cocnc);
		inStream= new FileInputStream(file);
			// HSSF
			wb = new HSSFWorkbook(inStream);
			inStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (wb == null) {
			//Log.e(TAG, "ä»Žassets è¯»å�–Excelæ–‡æ¡£å‡ºé”™");
			return;
		}
		// DBAdapter å°�è£…äº†SQLiteçš„æ‰€æœ‰æ“�ä½œ
		DBAdapter dbAdapter = new DBAdapter(getActivity());//this
		Sheet sheet1 = wb.getSheetAt(0);//
		Sheet sheet2 = wb.getSheetAt(1);
		if (sheet1 == null) {
			return;
		}
		dbAdapter.open();
		//
		dbAdapter.deleteTable();
		Excel2SQLiteHelper.insertExcelToSqlite(dbAdapter, sheet1);
		//Excel2SQLiteHelper.insertExcelToSqlite(dbAdapter, sheet2);
		dbAdapter.close();
		
		handler.post(new Runnable() {
			public void run() {
				displayData();
			}
		});
	}
	private void displayData() {
		DBAdapter dbAdapter = new DBAdapter(getActivity());
		dbAdapter.open();
		Cursor allDataCursor = dbAdapter.getAllRow(DBAdapter.STU_TABLE);
		MyCursorAdapter adapter = new MyCursorAdapter(getActivity(), allDataCursor);
		String barcode="3607340726682";
		barcode = dbAdapter.getOneRow(barcode);
		
		
		//listView.setAdapter(adapter);
		log.setText(log.getText() + "\n" + adapter.getCount()+barcode);
	}
	//using the intents to call the XYing code.
	private void encodeBarcode(String type, String data) {
		//for other encoding types, see http://code.google.com/p/zxing/source/browse/trunk/androidtest/src/com/google/zxing/client/androidtest/ZXingTestActivity.java
		Intent intent = new Intent("com.google.zxing.client.android.ENCODE");
		intent.putExtra("ENCODE_TYPE", type);
		intent.putExtra("ENCODE_DATA", data);
		startActivity(intent);
	}

	//encode the data, but using the IntentIntegrator
	private void encodeBarcode2(String type, String data) {
		IntentIntegrator integrator = new IntentIntegrator(getActivity());
		integrator.shareText(data);
	}

	/*
	 * simple method to add the log TextView.
	 */
	public void logthis (String newinfo) {
		if (newinfo != "") {
			log.setText(log.getText() + "\n" + newinfo);
			edti.setText(newinfo);
		}
	}

}
