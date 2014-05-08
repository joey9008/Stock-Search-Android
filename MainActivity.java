package com.webhw9.webhw9;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {


	private TableLayout mainScrollView;
	private TableRow row1;
	private TableRow row2;
	private TableRow row3;
	private AutoCompleteTextView Searchtext;
	private TextView company_info;
	private TextView LastTradePriceOnly;
	private TextView Change;
	
	private TextView Prev_Close;
	private TextView Open;
	private TextView Bid;
	private TextView Ask;
	private TextView st_Yr_Target;
	private TextView Day_Range;
	private TextView wk_Range;
	private TextView Volume;
	private TextView Avg_Vol;
	private TextView Market_Cap;
	
	private ImageView stockChart; 
	public static JSONArray News;
	private String StockChartImageURL;
	Button search_button;
	String company_Symbol;
	String stockURL;
	
	String Name;
	String Symbol;
	String _LastTradePriceOnly;
	String _ChangeinPercent;
	String _ChangeType;
	String _Change;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		
		
		search_button = (Button) findViewById(R.id.search_button);
		Searchtext = (AutoCompleteTextView) findViewById(R.id.Searchtext);
		
		
		
		
		company_info = (TextView) findViewById(R.id.company_info);
		LastTradePriceOnly = (TextView) findViewById(R.id.LastTradePriceOnly);
		Change = (TextView) findViewById(R.id.Change);
		
		Prev_Close = (TextView) findViewById(R.id.Prev_Close);
		Open = (TextView) findViewById(R.id.Open);
		Bid = (TextView) findViewById(R.id.Bid);
		Ask = (TextView) findViewById(R.id.Ask);
		st_Yr_Target = (TextView) findViewById(R.id.st_Yr_Target);
		Day_Range = (TextView) findViewById(R.id.Day_Range);
		wk_Range = (TextView) findViewById(R.id.wk_Range);
		Volume = (TextView) findViewById(R.id.Volume);
		Avg_Vol = (TextView) findViewById(R.id.Avg_Vol);
		Market_Cap = (TextView) findViewById(R.id.Market_Cap);
		
		stockChart = (ImageView) findViewById(R.id.stockChart);
		
		mainScrollView = (TableLayout) findViewById(R.id.mainScrollView);
		row1 = (TableRow) findViewById(R.id.row1);
		row2 = (TableRow) findViewById(R.id.row2);
		row3 = (TableRow) findViewById(R.id.row3);
		search_button.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				
				company_Symbol= Searchtext.getText().toString();
				stockURL = getString(R.string.url) + company_Symbol;
				if(company_Symbol.length() > 0)
				{
					new MyAsyncTask().execute(stockURL);
				}
				else
				{
					mainScrollView.setVisibility(View.INVISIBLE);
		 			row1.setVisibility(View.INVISIBLE);
		 			row2.setVisibility(View.INVISIBLE);
		 			row3.setVisibility(View.INVISIBLE);
					AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
					builder.setTitle(R.string.error);
					builder.setPositiveButton(R.string.ok,null);
					builder.setMessage(R.string.noInfo);
					AlertDialog theAlertDialog = builder.create();
					theAlertDialog.show();
					
				}
			}
		});
		
		
	}

	private class ImgDownload extends AsyncTask <String, Void, Bitmap>
	{
		ImageView stockChart;
	
		public ImgDownload(ImageView stockChart) {
			this.stockChart = stockChart;
		}

		protected Bitmap doInBackground(String... args) {
			Bitmap pic = null;
			try {
				InputStream in = new java.net.URL(args[0]).openStream();
				pic = BitmapFactory.decodeStream(in);
				
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 
			return pic;
		}
		

		protected void onPostExecute(Bitmap result) {

			stockChart.setImageBitmap(result);
	    }
	}
	
	public class MyAsyncTask extends AsyncTask<String, String, String>
	{
        protected String doInBackground(String... args) 
        {
        	String response = null;
        	try{
        		DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpEntity httpEntity = null;
                HttpResponse httpResponse = null;

        		HttpGet httpGet = new HttpGet(args[0]);
        		 
                httpResponse = httpClient.execute(httpGet);
                httpEntity = httpResponse.getEntity();
                response = EntityUtils.toString(httpEntity);
                
                
                
        	}catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        	return response;
        		
        }

        @Override
	    protected void onPostExecute(String result) {
	        super.onPostExecute(result);
	        
				try {
					parseJSON(result);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			
	        //Do anything with response..
	    }
       
	}
	
	
	 public void parseJSON(String response) throws JSONException{
 		JSONObject jsonObj = new JSONObject(response);
 		JSONObject result = jsonObj.getJSONObject("result");
 		Name = result.getString("Name");
 		Symbol = result.getString("Symbol");
 		JSONObject Quote = result.getJSONObject("Quote");
 		_Change = Quote.getString("Change");
 		if(_Change.equals(""))
 		{
 			mainScrollView.setVisibility(View.INVISIBLE);
 			row1.setVisibility(View.INVISIBLE);
 			row2.setVisibility(View.INVISIBLE);
 			row3.setVisibility(View.INVISIBLE);
 			AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			builder.setTitle(R.string.error);
			builder.setPositiveButton(R.string.ok,null);
			builder.setMessage(R.string.notavail);
			AlertDialog theAlertDialog = builder.create();
			theAlertDialog.show();
 		}
 		else
 		{
 			News = result.getJSONObject("News").getJSONArray("Item");
     			_LastTradePriceOnly = Quote.getString("LastTradePriceOnly");
     			_ChangeinPercent = Quote.getString("ChangeinPercent");
	    		_ChangeType = Quote.getString("ChangeType");
	    		company_info.setText(Name + "(" + Symbol+ ")");
	    		LastTradePriceOnly.setText(_LastTradePriceOnly);
	    		Change.setText(_Change + "(" + _ChangeinPercent+ ")");
	    		
	    		if(_ChangeType == "+")
	    		{
	    			ImageView imgView = (ImageView) findViewById(R.id.imageView1);
	    			imgView.setImageResource(R.drawable.up);
	    			Change.setTextColor(Color.GREEN);
	    		}
	    		else
	    		{
	    			ImageView imgView = (ImageView) findViewById(R.id.imageView1);
	    			imgView.setImageResource(R.drawable.down);
	    			Change.setTextColor(Color.RED);
	    		}
	    		String _Prev_Close = Quote.getString("PreviousClose");
	    		String _Open = Quote.getString("Open");
	    		String _Bid = Quote.getString("Bid");
	    		String _Ask = Quote.getString("Ask");
	    		String _YearLow = Quote.getString("YearLow");
	    		String _YearHigh = Quote.getString("YearHigh");
	    		String _DaysLow = Quote.getString("DaysLow");
	    		String _DaysHigh = Quote.getString("DaysHigh");
	    		String _OneyearTargetPrice = Quote.getString("OneyearTargetPrice");
	    		String _Volume = Quote.getString("Volume");
	    		String _AverageDailyVolume = Quote.getString("AverageDailyVolume");
	    		String _MarketCapitalization = Quote.getString("MarketCapitalization");
	    		Prev_Close.setText(_Prev_Close);
	    		Open.setText(_Open);
	    		Bid.setText(_Bid);
	    		Ask.setText(_Ask);
	    		st_Yr_Target.setText(_OneyearTargetPrice);
	    		Day_Range.setText(_DaysLow+"-"+_DaysHigh);
	    		wk_Range.setText(_YearLow+"-"+_YearHigh);
	    		Volume.setText(_Volume);
	    		Avg_Vol.setText(_AverageDailyVolume);
	    		Market_Cap.setText(_MarketCapitalization);
	    		
	    		StockChartImageURL = result.getString("StockChartImageURL");
	    		new ImgDownload((ImageView)stockChart).execute(StockChartImageURL);
	    		
	    		
	    		mainScrollView.setVisibility(View.VISIBLE);
	    		row1.setVisibility(View.VISIBLE);
	 			row2.setVisibility(View.VISIBLE);
	 			row3.setVisibility(View.VISIBLE);

	    		
 		}
 		
 		
 	}
	
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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

	
	public void newsActivity(View v)
	{
		Intent intent = new Intent(this, NewsActivity.class);
		startActivity(intent);
	}
	
	public void facebookPost(View v)
	{
		publishFeedDialog();
	}
	
	
	private void publishFeedDialog() {
        if(Session.getActiveSession() == null || Session.getActiveSession().isOpened() == false)
        {
    	    Session.openActiveSession(this, true, new Session.StatusCallback() {

    	        @SuppressWarnings("deprecation")
    			@Override
    	        public void call(Session session, SessionState state,
    	                Exception exception) {
    	            if (session.isOpened()) {

    	                Request.executeMeRequestAsync(session,
    	                        new Request.GraphUserCallback() {

    	                            @Override
    	                            public void onCompleted(GraphUser user,
    	                                    Response response) {
    	                                if (user != null) {
    	                                	do_post();
    	                                }
    	                            }
    	                        });
    	            }
    	        }
    	    });
        }
        else {
			do_post();
		}
	}

	void do_post()
	{
		final Bundle params = new Bundle();
        params.putString("name", Name);
        params.putString("caption", "Stock Information of "+Name+'(' + Symbol + ')');
        params.putString("description", "Last Trade Price: "+_LastTradePriceOnly+", Change: "+_ChangeType+_Change+"("+_ChangeinPercent+")");
        params.putString("link", "http://finance.yahoo.com/q?s="+Symbol);
        params.putString("picture", StockChartImageURL);
		WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(this,
				Session.getActiveSession(), params))
				.setOnCompleteListener(new OnCompleteListener()
				{
					@Override
					public void onComplete(Bundle values,
							FacebookException error)
					{
						if (error == null)
						{
							final String postId = values.getString("post_id");
							if (postId != null) 
							{
								Toast.makeText(getApplicationContext(),
										"Posted Sucessfully: " + postId,
										Toast.LENGTH_SHORT).show();
							}
							else 
							{
								Toast.makeText(getApplicationContext(),
										"Publish cancelled", Toast.LENGTH_SHORT)
										.show();
							}
						} 
						else if (error instanceof FacebookOperationCanceledException) 
						{
							Toast.makeText(getApplicationContext(),
									"Publish cancelled", Toast.LENGTH_SHORT)
									.show();
						} 
						else 
						{
							Toast.makeText(getApplicationContext(),
									"Error posting story", Toast.LENGTH_SHORT)
									.show();
						}
					}
				}).build();
		feedDialog.show();
	}

	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode,
                resultCode, data);

    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
