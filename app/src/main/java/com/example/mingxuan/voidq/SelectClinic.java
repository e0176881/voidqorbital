package com.example.mingxuan.voidq;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
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
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class SelectClinic extends FragmentActivity implements OnMapReadyCallback {
	GoogleMap map ;
	MapView mapView;
	HttpPost httppost;
	StringBuffer buffer;
	TextView about;
	HttpClient httpclient;
	List<NameValuePair> nameValuePairs;
	ProgressDialog dialog = null;
	public int currentimageindex=0;
    Timer timer;
    TimerTask task;
    ImageView slidingimage;
    String cname, cdistance,caddress, clat, clng;
    String symb,desc,mc,mapname,clickedname;
    String oaddress, odays, ohoursmorning, ohoursafternoon, ohoursevening, ocontactno, ofaxnumber;
    Double lat,lng;


	@Override
    public void onCreate(Bundle savedInstanceState) {
    	 //Remove title bar
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        super.onCreate(savedInstanceState);
StrictMode.ThreadPolicy policy = new StrictMode.
	    ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy); 
// Set View to register.xml
setContentView(R.layout.selectclinic);


        MapFragment mapFragment = MapFragment.newInstance();
        mapFragment.getMapAsync(this);
        getFragmentManager()
                .beginTransaction()
                .add(R.id.map, mapFragment)
                .commit();
		ViewGroup.LayoutParams params = ((MapFragment)  getFragmentManager().findFragmentById(R.id.map)).getView().getLayoutParams();
		params.height = 600;
		((MapFragment)  getFragmentManager().findFragmentById(R.id.map)).getView().setLayoutParams(params);
     // 1. get passed intent 
        Intent intent = getIntent();
 

        // 4. get bundle from intent
        Bundle bundle = intent.getExtras();
 
        // 5. get status value from bundle
         cname = bundle.getString("clinicname");
         cdistance = bundle.getString("distance");
         caddress = bundle.getString("address");// this.setContentView(R.layout.selectclinic);




		Typeface type = Typeface.createFromAsset(getAssets(),"fonts/verdana.ttf");
        TextView Pagename = (TextView)findViewById(R.id.pagename);
        Pagename.setTypeface(type);

        TextView tvClinicName = (TextView)findViewById(R.id.text1);
        tvClinicName.setTypeface(type);
        tvClinicName.setText(cname);

        TextView tvClinicAdd = (TextView)findViewById(R.id.text2);
        tvClinicAdd.setTypeface(type);
        tvClinicAdd.setText(caddress);

        TextView tvDistance = (TextView)findViewById(R.id.text3);
        tvDistance.setTypeface(type);
        tvDistance.setText(cdistance);




		    

		    Button btnselect = (Button) findViewById(R.id.btnselect);
		    btnselect.setTypeface(type);
		    btnselect.setOnClickListener(new View.OnClickListener() {
	   			
	   			public void onClick(View v) {
	   				// Switching to Register screen
	   				Intent launchmain= new Intent(getApplicationContext(),Book.class);
	            	launchmain.putExtra("clinicname",cname);
	                startActivity(launchmain);
	   			}
	   		});
		    
    }

	@Override
	public void onMapReady(GoogleMap googleMap) {
		map = googleMap;
		String readgmap = readMaps2(cname);
		Typeface type = Typeface.createFromAsset(getAssets(),"fonts/verdana.ttf");
		TextView tva = (TextView)findViewById(R.id.textViewa);
		tva.setTypeface(type);
		TextView tvb = (TextView)findViewById(R.id.textViewb);
		tvb.setTypeface(type);

		TextView tvphone =  (TextView)findViewById(R.id.tbphone);
		tvphone.setTypeface(type);

		TextView weekdays =  (TextView)findViewById(R.id.tbweekdays);
		weekdays.setTypeface(type);

		TextView weekends =  (TextView)findViewById(R.id.tbweekends);
		weekends.setTypeface(type);

		TextView notes =  (TextView)findViewById(R.id.tbaddnotes);
		notes.setTypeface(type);

        TextView charges =  (TextView)findViewById(R.id.tbcharges);
        charges.setTypeface(type);

		try {


			JSONArray jsonArray = new JSONArray(readgmap);
			//    Log.i(ParseJSON.class.getName(),
			//      "Number of entries " + jsonArray.length());
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				mapname =  jsonObject.getString("name");
				oaddress =  jsonObject.getString("address");
				lat=jsonObject.getDouble("lat");
				lng=jsonObject.getDouble("lng");


				googleMap.addMarker(new MarkerOptions()
								.position(new LatLng(lat,lng))
								.title(mapname)
								.snippet( oaddress )
						/*.snippet("<div>" +  oaddress + "</br>" + "Operating Days: " + "</br>" + odays + "</br>" + "Operating Hours" + "</br>" + "Morning: " + ohoursmorning + "</br>" + "Afternoon: " + ohoursafternoon + "</br>" + "Evening: " + ohoursevening + "</br>" + "Contact No: " + ocontactno + "</br>" + "Fax Number:" + ofaxnumber + "</div>")*/

				);


				tvphone.setText(jsonObject.getString("contactno"));
				weekdays.setText("WEEKDAYS : "+ jsonObject.getString("operatingweekdays"));
				weekends.setText("WEEKENDS : "+ jsonObject.getString("operatingweekends"));
				notes.setText("Additional Notes : "+ jsonObject.getString("operatingnotes"));
				charges.setText("Consultation Charge : " + "$" + jsonObject.getString("charge" ));
				LatLng toCenter = new LatLng(jsonObject.getDouble("lat"), jsonObject.getDouble("lng"));
				googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setScrollGesturesEnabled(true);
                googleMap.getUiSettings().setZoomControlsEnabled(true);
				googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(toCenter, 12));



			}

		}catch (Exception e) {
			e.printStackTrace();
		}

		ImageButton bck = (ImageButton) findViewById(R.id.imageButton1);
		bck.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// Switching to Register screen
				onBackPressed();
			}
		});



	}
    public String readMaps() {
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("http://voidq.xyz/appconfig/clinics.php");
        try {
          HttpResponse response = client.execute(httpGet);
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
    public String readMaps2(String searchquery) {
    	
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

  	}