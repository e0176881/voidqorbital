package com.example.mingxuan.voidq;


import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SearchResultsActivity extends Activity {
	public class codeLeanChapter {
		String chapterName;
		String chapterDescription;
		String queryz;
	}
	public class fuckintent {
		
		String queryz;
	}
	ActionBar actionBar;
	TextView tv;
	TextView Pagename;
	ListView l;
	HttpPost httppost;
	StringBuffer buffer;
	HttpResponse response;
	HttpClient httpclient;
	List<NameValuePair> nameValuePairs;
	ProgressDialog dialog = null;
	  public int currentimageindex=0;
	    Timer timer;
	    TimerTask task;
	    ImageView slidingimage;
	    String oaddress, odays, ohoursmorning, ohoursafternoon, ohoursevening, ocontactno, ofaxnumber;
		String symb,desc,mc,mapname,clickedname;
		Double lat,lng;

	    SharedPreferences sharedpreferences;
	    public static final String MyPREFERENCES = "MyPrefs" ;
	    public static final String pUserName = "username"; 
	    public static final String pPassword = "password"; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_search_results);

		// get the action bar
		 actionBar = getActionBar();
		 actionBar.hide();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1248ea")));
        
        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// Enabling Back navigation on Action Bar icon
		actionBar.setDisplayHomeAsUpEnabled(true);

		Typeface type = Typeface.createFromAsset(getAssets(),"fonts/verdana.ttf"); 

		Pagename = (TextView)findViewById(R.id.pagename);
	        Pagename.setTypeface(type);
        l=(ListView) findViewById(R.id.listView1);
        


	    TextView lbl = (TextView) findViewById(R.id.textViewa);
	    
	    lbl.setTypeface(type);

	   


        ImageButton bck = (ImageButton) findViewById(R.id.imageButton1);
        bck.setOnClickListener(new View.OnClickListener() {
       			
       			public void onClick(View v) {
       				// Switching to Register screen
       				finish();
       			}
       		});

           
        
		handleIntent(getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent(intent);
	}

	/**
	 * Handling intent data
	 */
	private void handleIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			Pagename.setText("Search for " + query);
			/**
			 * Use this query to display search results like 
			 * 1. Getting the data from SQLite and showing in listview 
			 * 2. Making webrequest and displaying the data 
			 * For now we just display the query only
			 */
			
	        String readgmap = readMaps(query);
	        try {
	            JSONArray jsonArray = new JSONArray(readgmap);
	            
	            SimpleAdapter adapter = new SimpleAdapter(
	            		this,
	            		list,
	            		R.layout.custom_row_view,
	            		new String[] {"lbname","lbaddress","lbdistance"},
	            		new int[] {R.id.text1,R.id.text2, R.id.text3}
	            		);
	           
	          l.setAdapter(null);
	          list.clear();
		            for (int i = 0; i < jsonArray.length(); i++) {
		            	


		            	 JSONObject jsonObject = jsonArray.getJSONObject(i);
	  	              mapname =  jsonObject.getString("name");

	  	              oaddress =  jsonObject.getString("address");
	  	           
	  	            HashMap<String,String> temp = new HashMap<String,String>();
				    	temp.put("lbname",mapname);
				    	temp.put("lbaddress", oaddress);
				    	temp.put("lbdistance", "1km Away");
				    	list.add(temp);
	  	           
		            }
		          //gg is it time to pas u back ? :P the action bar menu also there le?tat one easy de u comeh ere
			   	  //ArrayAdapter<String> adapterzz=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,list);
			   	  l.setAdapter(adapter);
			   	l.setOnItemClickListener(new OnItemClickListener()

	 		       {    

	 		        @Override

	 		          public void onItemClick(AdapterView<?> a, View v,int position, long id)

					{
						if (checkEmptyFields().trim().equals("can"))
						{

						TextView textview1 = (TextView) v.findViewById(R.id.text1);
						TextView textview2 = (TextView) v.findViewById(R.id.text2);
						TextView textview3 = (TextView) v.findViewById(R.id.text3);
						Toast.makeText(v.getContext(), textview1.getText(), Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(getBaseContext(), SelectClinic.class);
						// 2. put key/value data
						//intent.putExtra("message", "Hello From MainActivity");

						// 3. or you can add data to a bundle
						Bundle extras = new Bundle();
						extras.putString("clinicname", textview1.getText().toString());
						extras.putString("distance", textview3.getText().toString());
						extras.putString("address", textview2.getText().toString());
						//extras.putString("lat", clat);
						//extras.putString("lng", clong);


						// 4. add bundle to intent
						intent.putExtras(extras);

						// 5. start the activity
						startActivity(intent);
					}
	 				        else

						  {
							  showAlert("Please update your healthbook!");

						  }
	 		           }

	 		     });

			    

			    

	   	  
	}catch (Exception e) {
	   e.printStackTrace();
	}

		}
		
		
		
		
	}

	 static final ArrayList<HashMap<String,String>> list = 
		    	new ArrayList<HashMap<String,String>>(); 
	 public String readMaps(String searchquery) {
	    	
	        StringBuilder builder = new StringBuilder();
	        HttpClient client = new DefaultHttpClient();
	        HttpPost httppost= new HttpPost("http://voidq.xyz/appconfig/search.php"); // make sure the url is correct.
			
	        try {
	        	//add your data
				nameValuePairs = new ArrayList<NameValuePair>(1);
				// Getting latitude of the current location

				// Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar, 
				nameValuePairs.add(new BasicNameValuePair("search",searchquery)); 
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response= client.execute(httppost);
	          StatusLine statusLine = response.getStatusLine();
	          int statusCode = statusLine.getStatusCode();
	          if (statusCode == 200) {
	            HttpEntity entity = response.getEntity();
	            InputStream content = entity.getContent();
	            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
	            String line;
	            while ((line = reader.readLine()) != null) {
	              builder.append(line);
	            }
	          } else {
	            Log.e("ERROR", "Failed to load map");
	          }
	        } catch (ClientProtocolException e) {
	          e.printStackTrace();
	        } catch (IOException e) {
	          e.printStackTrace();
	        }
	        return builder.toString();
	      }



	public String checkEmptyFields(){
		String status = "";
		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		try{


			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost= new HttpPost("http://voidq.xyz/appconfig/checkhealth.php"); // make sure the url is correct.
			//add your data
			nameValuePairs = new ArrayList<NameValuePair>(1);
			// Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
			nameValuePairs.add(new BasicNameValuePair("username",sharedpreferences.getString("username", null)));  // $Edittext_value = $_POST['Edittext_value'];
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			//response=httpclient.execute(httppost);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			final String response = httpclient.execute(httppost, responseHandler);
			System.out.println("Response : " + response);


			status = response;

		}catch(Exception e){

			System.out.println("Exception : " + e.getMessage());
		}
		return status;

	}

	public void showAlert(final String message){

		runOnUiThread(new Runnable() {
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(SearchResultsActivity.this);
				builder.setTitle("Error");
				builder.setMessage(message)
						.setCancelable(false)
						.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								finish();
							}
						});
				AlertDialog alert = builder.create();
				alert.show();
			}
		});
	}

	}

