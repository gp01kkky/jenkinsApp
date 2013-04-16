package com.example.jenkinsapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;

public class BuildQueue extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_build_queue);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.build_queue, menu);
		return true;
	}
	public void onClickBuildHistory (View Button){
    	Intent intent = new Intent();
    	intent.setClass(this, BuildHistory.class);
    	startActivity(intent);
    }
 
	public void onClickBuildStatus (View Button){
    	Intent intent = new Intent();
    	intent.setClass(this, BuildStatus.class);
    	startActivity(intent);
    }
	public void onClickBuildQueue (View Button){
    	Intent intent = new Intent();
    	intent.setClass(this, BuildQueue.class);
    	startActivity(intent);
    }

}
