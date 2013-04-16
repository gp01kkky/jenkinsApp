package com.example.jenkinsapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class Project1buildq extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_project1buildq);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.project1buildq, menu);
		return true;
	}
	
	public void onClickBuildHistory(View Button)
	{
		Intent intent = new Intent();
		intent.setClass(this,BuildHistory.class);
		startActivity(intent);
	}

}
