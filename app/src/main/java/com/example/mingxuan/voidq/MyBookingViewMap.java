package com.example.mingxuan.voidq;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
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

public class MyBookingViewMap extends FragmentActivity implements OnMapReadyCallback {
	GoogleMap map ;
	ListView l;
	MapView mapView;
	String oaddress, odays, ohoursmorning, ohoursafternoon, ohoursevening, ocontactno, ofaxnumber, odistance;
	String symb,desc,mc,mapname,clickedname, clat, clong;
	List<NameValuePair> nameValuePairs;

	Double lat,lng;

	String clinicname;

	@Override
	public void onMapReady(GoogleMap googleMap) {
		map = googleMap;
		String readgmap = readMaps(clinicname);
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




				map.addMarker(new MarkerOptions()
								.position(new LatLng(lat,lng))
								.title(mapname)
								.snippet( oaddress )
						/*.snippet("<div>" +  oaddress + "</br>" + "Operating Days: " + "</br>" + odays + "</br>" + "Operating Hours" + "</br>" + "Morning: " + ohoursmorning + "</br>" + "Afternoon: " + ohoursafternoon + "</br>" + "Evening: " + ohoursevening + "</br>" + "Contact No: " + ocontactno + "</br>" + "Fax Number:" + ofaxnumber + "</div>")*/

				);
				LatLng toCenter = new LatLng(lat, lng);
				map.setMyLocationEnabled(true);

				map.moveCamera(CameraUpdateFactory.newLatLngZoom(toCenter, 12));



			}

		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void onCreate(Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.full_view);
		Intent intent = getIntent();

		Bundle bundle = intent.getExtras();

		// 5. get status value from bundle
		clinicname = bundle.getString("clinicname");
		MapFragment mapFragment = MapFragment.newInstance();
		mapFragment.getMapAsync(this);
		getFragmentManager()
				.beginTransaction()
				.add(R.id.map, mapFragment)
				.commit();
		ViewGroup.LayoutParams params = ((MapFragment)  getFragmentManager().findFragmentById(R.id.map)).getView().getLayoutParams();
		params.height = LinearLayout.LayoutParams.FILL_PARENT;
		((MapFragment)  getFragmentManager().findFragmentById(R.id.map)).getView().setLayoutParams(params);


		Typeface type = Typeface.createFromAsset(getAssets(),"fonts/verdana.ttf");
		TextView Pagename = (TextView)findViewById(R.id.pagename);
		Pagename.setTypeface(type);


		ImageButton bck = (ImageButton) findViewById(R.id.imageButton1);
		bck.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// Switching to Register screen
				finish();
			}
		});

		TextView cm = (TextView) findViewById(R.id.closemap);
		cm.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// Switching to Register screen
				finish();
			}
		});

	}



	public String readMaps(String searchquery) {

		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpPost httppost= new HttpPost("http://voidq.xyz/appconfig/search2.php"); // make sure the url is correct.

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



	public LatLng getCurrentLocation(Context context)
	{
		try
		{
			LocationManager locMgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
			Criteria criteria = new Criteria();
			String locProvider = locMgr.getBestProvider(criteria, false);
			Location location = locMgr.getLastKnownLocation(locProvider);

			// getting GPS status
			boolean isGPSEnabled = locMgr.isProviderEnabled(LocationManager.GPS_PROVIDER);
			// getting network status
			boolean isNWEnabled = locMgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNWEnabled)
			{
				// no network provider is enabled
				return null;
			}
			else
			{
				// First get location from Network Provider
				if (isNWEnabled)
					if (locMgr != null)
						location = locMgr.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

				// if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled)
					if (location == null)
						if (locMgr != null)
							location = locMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			}

			return new LatLng(location.getLatitude(), location.getLongitude());
		}
		catch (NullPointerException ne)
		{
			Log.e("Current Location", "Current Lat Lng is Null");
			return new LatLng(0, 0);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new LatLng(0, 0);
		}
	}
}
