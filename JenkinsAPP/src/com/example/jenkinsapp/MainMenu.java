package com.example.jenkinsapp;


import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MainMenu extends Activity {
	ImageView image;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.list);
		
		ListView list = (ListView) findViewById(R.id.paramList);
		//addJenkinsInstances();

		String[] MENUITEMS = getResources().getStringArray(R.array.menu_array);
		
		ListAdapter adapter = new ArrayAdapter<String>(this,
			R.layout.image_list_item, R.id.jenkins_instances,MENUITEMS);
		list.setAdapter(adapter);
		//create an onClickListener here
		list.setOnItemClickListener(new OnItemClickListener(){
		@Override	
		public void onItemClick(AdapterView<?> l, View v, int position, long id) 
		{
			  Intent myIntent;
			  switch(position) {
			  case 0:  myIntent = new Intent(MainMenu.this, My_project.class);
		              	break; 
			  case 1:  myIntent = new Intent(MainMenu.this, My_project.class);
	          			break; 
	  
			  
			  default: 	myIntent = new Intent(MainMenu.this, MainMenu.class);
					break; 			
		}
				 MainMenu.this.startActivity(myIntent);
		}
		});
		
	}
		
		
	
	
}
