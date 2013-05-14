/* This class transform the graph into full screen
 * author@Edward James
 */
package jenkinsapp.activity;

import kkky.jenkinsapp.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.widget.LinearLayout;

import graphview.GraphView.GraphViewData;

public class GraphForm extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {  
		Bundle mode = getIntent().getExtras();
		
    	if(mode !=null)
    	{	//https://ci.jenkins-ci.org/view/All/api/json?pretty=true
    		//data = (GraphViewData[]) mode.get("graphData"); 

    	}
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_graph);
		GraphViewData [] data = BuildStatus.graphData;

		//Getting the latest build & current project
		Intent intent = getIntent();
		//project = (Project) intent.getParcelableExtra("Project");
		//latest = (Build) intent.getParcelableExtra("Build");
		
		LinearLayout layout = (LinearLayout) findViewById(R.id.graph1);
		layout.addView(GraphFactory.buildGraph(true, "", data, this));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		return true;
	}

	
}