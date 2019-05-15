package com.example.mingxuan.voidq;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
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
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
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


public class SelectTime extends Activity {
	Button b;
	EditText et,pass;
	TextView tv;
	ListView l;
	HttpPost httppost;
	StringBuffer buffer;
	String clat, clng, odistance;
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
	    private int[] IMAGE_IDS = {
	            R.drawable.pic2, R.drawable.pic3,
	            R.drawable.pic4, R.drawable.pic5
	        };
	    SharedPreferences sharedpreferences;
	    public static final String MyPREFERENCES = "MyPrefs" ;
	    public static final String pUserName = "username"; 
	    public static final String pPassword = "password"; 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

       //set content view AFTER ABOVE sequence (to avoid crash)
        //this.setContentView(R.layout.login); 
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectime);
        
        
        
     // 1. get passed intent 
        Intent intent = getIntent();
 

        // 4. get bundle from intent
        Bundle bundle = intent.getExtras();
 
        // 5. get status value from bundle
         clat = bundle.getString("lat");
         clng = bundle.getString("lng");
        
        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/verdana.ttf"); 

        TextView Pagename = (TextView)findViewById(R.id.pagename);
        Pagename.setTypeface(type);
       
        String readgmap = readMaps2();
        try {
            JSONArray jsonArray = new JSONArray(readgmap);
            l=(ListView) findViewById(R.id.listView1);
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
  	            odistance  =  jsonObject.getString("distance");
  	            HashMap<String,String> temp = new HashMap<String,String>();
			    	temp.put("lbname",mapname);
			    	temp.put("lbaddress", oaddress);
			    	temp.put("lbdistance", odistance);
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

		     });

		    

		    

   	  
}catch (Exception e) {
   e.printStackTrace();
}
	      

        

 ImageButton bck = (ImageButton) findViewById(R.id.imageButton1);
 bck.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// Switching to Register screen
				finish();
			}
		});

    }
    
	 static final ArrayList<HashMap<String,String>> list = 
		    	new ArrayList<HashMap<String,String>>(); 
	  public String readMaps2() {
	    	
	        StringBuilder builder = new StringBuilder();
	        HttpClient client = new DefaultHttpClient();
	        HttpPost httppost= new HttpPost("http://voidq.xyz/appconfig/viewall.php"); // make sure the url is correct.
			
	        try {
	        	
				nameValuePairs = new ArrayList<NameValuePair>(2);
				// Getting latitude of the current location
		      
				// Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar, 
				nameValuePairs.add(new BasicNameValuePair("currentlat",clat));  // $Edittext_value = $_POST['Edittext_value'];
				nameValuePairs.add(new BasicNameValuePair("currentlng",clng)); 
				
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

 
  
    

    
  
}