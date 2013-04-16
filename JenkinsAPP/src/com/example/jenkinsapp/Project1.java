package com.example.jenkinsapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class Project1 extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_project1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.project1, menu);
		return true;
	}
	
	public void onClickBuildq(View Button)
	{
		Intent intent = new Intent();
		intent.setClass(this,Project1buildq.class);
		startActivity(intent);
	}
	
}
