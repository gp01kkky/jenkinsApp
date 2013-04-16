package com.example.jenkinsapp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class My_project extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my_project);
		ListView list = (ListView) findViewById(R.id.paramList);
		//addJenkinsInstances();

		String[] MENUITEMS = getResources().getStringArray(R.array.project_list);
		
		ListAdapter adapter = new ArrayAdapter<String>(this,
			R.layout.project_list_item, R.id.jenkins_instances,MENUITEMS);
		list.setAdapter(adapter);
		//create an onClickListener here
		list.setOnItemClickListener(new OnItemClickListener(){
		@Override	
		public void onItemClick(AdapterView<?> l, View v, int position, long id) 
		{
			  Intent myIntent;
			  switch(position) {
			  case 0:  myIntent = new Intent(My_project.this, My_project_menu.class);
		              	break; 
			  case 1:  myIntent = new Intent(My_project.this, My_project_menu.class);
	          			break; 
	  
			  
			  default: 	myIntent = new Intent(My_project.this, My_project.class);
					break; 			
		}
				 My_project.this.startActivity(myIntent);
		}
		});
		
	}
}
