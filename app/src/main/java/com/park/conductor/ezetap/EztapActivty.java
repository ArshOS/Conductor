package com.park.conductor.ezetap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.park.conductor.R;


public class EztapActivty extends Activity implements OnClickListener {
	Button btnNative, btnCordova, btnSettings;
	Intent intent = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btnNative = (Button) findViewById(R.id.btnNative);
		btnNative.setOnClickListener(this);
		btnCordova = (Button) findViewById(R.id.btnCordova);
		btnCordova.setOnClickListener(this);
		btnSettings = (Button) findViewById(R.id.btnSettings);
		btnSettings.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.btnNative) {
			intent = new Intent(EztapActivty.this, EzeNativeSampleActivity.class);
			startActivity(intent);
		} else if (id == R.id.btnCordova) {
//			intent = new Intent(EztapActivty.this, EzeCordovaSampleActivity.class);
//			startActivity(intent);
		} else if (id == R.id.btnSettings) {
			intent = new Intent(EztapActivty.this, Setting.class);
			startActivity(intent);
		}
	}


	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}

}
