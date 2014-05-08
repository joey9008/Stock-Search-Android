package com.webhw9.webhw9;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class NewsActivity extends Activity {

	private ListView news_list;
	private JSONArray News;
	private JSONObject item;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news);
		
		news_list = (ListView) findViewById(R.id.news_list);
		News = MainActivity.News;
		String[] newsArray = new String[News.length()];
		for(int i = 0;i<News.length();i++)
		{
			try {
				item = News.getJSONObject(i);
				String Title = item.getString("Title");
				Title = Title.replace("&#039;","'");
				newsArray[i] = Title;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		ArrayAdapter<String> Myadapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1, newsArray);
		news_list.setAdapter(Myadapter);
		String text = "Showing" + News.length() +"headlines";
		Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
		news_list.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
				builder.setTitle(R.string.View_News);
				builder.setPositiveButton(R.string.View, new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent link;
						try {
							link = new Intent(Intent.ACTION_VIEW,Uri.parse(item.getString("Link")));
							startActivity(link);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				builder.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
				});
				AlertDialog theAlertDialog = builder.create();
				theAlertDialog.show();
			}	
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.news, menu);
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

	

}
