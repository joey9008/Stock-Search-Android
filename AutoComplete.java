package com.webhw9.webhw9;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class AutoComplete extends AutoCompleteTextView {
	public MyAdatper adapter = null;
	public boolean userSelect = false;
	public MainActivity act = null;
	private int requestNum = 0;
	public AutoComplete(Context context){
		super(context);
		init(context);
	}
	public AutoComplete(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context){
		act = (MainActivity)context;
		adapter = new MyAdatper(context);
		setAdapter(adapter);
		addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				if(!userSelect)
				{
					new RequestTask().execute("http://autoc.finance.yahoo.com/autoc?query="+s.toString()+"&callback=YAHOO.Finance.SymbolSuggest.ssCallback");
				}
				else {
					userSelect = false;
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});
		setThreshold(1);
	}
	
	@Override
	protected CharSequence convertSelectionToString(Object selectedItem) 
	{
		String s = (String)selectedItem;
		if(isPerformingCompletion())
		{
			userSelect = true;
			String search_URL = "http://cs-server.usc.edu:34079/examples/servlet/HelloWorldExample?symbol=" + s.substring(0,s.indexOf(','));
			new JSONRequest().execute(search_URL);
		}
		return s.substring(0,s.indexOf(','));
	}
	class MyAdatper extends BaseAdapter implements Filterable {
		List<String> mList;
		private Context mContext;
		private MyFilter mFilter;
		
		public MyAdatper(Context context) {
			mContext = context;
			mList = new ArrayList<String>();
		}
		
		@Override
		public int getCount() {
			return mList == null ? 0 : mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList == null ? null : mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				TextView tv = new TextView(mContext);
				tv.setTextColor(Color.BLACK);
				tv.setTextSize(20);
				tv.setHeight(dpToPx(50));
				tv.setGravity(Gravity.CENTER_VERTICAL);
				
				convertView = tv;
			}
			TextView txt = (TextView) convertView;
			txt.setText(mList.get(position));
			return txt;
		}

		@Override
		public Filter getFilter() {
			if (mFilter == null) {
				mFilter = new MyFilter();
			}
			return mFilter;
		}
		
		private class MyFilter extends Filter {

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults results = new FilterResults();
				if (mList == null) {
					mList = new ArrayList<String>();
				}
				results.values = mList;
				results.count = mList.size();
				return results;
			}

			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
	            if (results.count > 0) {
	                notifyDataSetChanged();
	            } else {
	                notifyDataSetInvalidated();
	            }
			}
		}
	}
	class RequestTask extends AsyncTask<String, String, String>{
		int id;
	    @Override
    	protected String doInBackground(String... args) 
        {
    		requestNum++;
	    	id = requestNum;
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
	        if(id!=requestNum) return;
	        adapter.mList.clear();
	        result = result.substring(result.indexOf('(')+1, result.length()-1);
	        JSONObject js = null;
	        try {
				js = new JSONObject(result);
				JSONArray ra = js.getJSONObject("ResultSet").getJSONArray("Result");
				for(int i=0;i<ra.length();++i)
				{
					JSONObject item = ra.getJSONObject(i);
					String s = "";
					s+=item.getString("symbol") + ",";
					s+=item.getString("name");
					s+="(" + item.getString("exch") + ")";
					adapter.mList.add(s);
				}
				adapter.notifyDataSetChanged();
				showDropDown();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        //Do anything with response..
	    }
	}

	class JSONRequest extends AsyncTask<String, String, String>{
	
	    @Override
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
				act.parseJSON(result);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        //Do anything with response..
	    }
	}
	public int dpToPx(int dp) {
	    DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
	    int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));       
	    return px;
	}
}
