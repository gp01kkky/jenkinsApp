/*
 * This class generate the graph
 * @author Edward James
 */
package jenkinsapp.activity;

import android.content.Context;

import graphview.GraphView;
import graphview.GraphView.GraphViewData;
import graphview.GraphViewSeries;
import graphview.LineGraphView;

public class GraphFactory {

	 public static GraphView buildGraph (boolean scalable, String ProjectTitle, GraphViewData[] data, Context context ){

		 int graphDensity = 10, graphStart = (data.length - graphDensity - 1);
		 
		 if(data.length < graphDensity){
			 graphDensity = data.length-1;
			 graphStart = 0;
			 }

		 GraphView graphView = new LineGraphView(context, ProjectTitle);
		 ((LineGraphView) graphView).setDrawBackground(true);
		 graphView.addSeries(new GraphViewSeries(data));
		 graphView.setViewPort(graphStart, graphDensity);
		 graphView.setScalable(scalable);
		 if (scalable == false){
			 int max = largest(data, graphDensity);
			 int min = smallest(data, graphDensity);
			 graphView.setVerticalLabels(new String[] {(Integer.toString(max))+"%", (Integer.toString((max+min)/2))+"%",(Integer.toString(min))+"%"});
		 }
		 return graphView;
	 }
	 
	 public static int largest(GraphViewData[] data, int range){
		 double largest = data[(data.length)-1].valueY;
		 
		 for (int i=(data.length - range); i<data.length; i++)  {
			 if ((data[i].valueY) > largest){
				 largest = (data[i].valueY);
			 }
		 }
			 
			 return (int)largest;
	 } 
		 
		 public static int smallest(GraphViewData[] data, int range){
			 double smallest = data[(data.length)-1].valueY;
			 
			 for (int i=(data.length - range); i<data.length; i++)  {
				 if ((data[i].valueY) < smallest){
					 smallest = (data[i].valueY);
				 }
		 }
		 
		return (int)smallest;
	 }
}




