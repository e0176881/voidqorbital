package com.example.mingxuan.voidq;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
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
public class BookDoc extends Fragment implements OnMapReadyCallback {
	GoogleMap gmap ;
	ListView l;
	MapView mapView;
	String oaddress, odays, ohoursmorning, ohoursafternoon, ohoursevening, ocontactno, ofaxnumber, odistance;
	String symb,desc,mc,mapname,clickedname, clat, clong;
	List<NameValuePair> nameValuePairs;

	Double lat,lng;

	SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String pUserName = "username";
    public static final String pPassword = "password";
	View view;



	private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
	private boolean mLocationPermissionGranted;

	private void getLocationPermission() {
		/*
		 * Request location permission, so that we can get the location of the
		 * device. The result of the permission request is handled by a callback,
		 * onRequestPermissionsResult.
		 */
		if (ContextCompat.checkSelfPermission(this.getActivity().getApplicationContext(),
				android.Manifest.permission.ACCESS_FINE_LOCATION)
				== PackageManager.PERMISSION_GRANTED) {
			mLocationPermissionGranted = true;
		} else {
			ActivityCompat.requestPermissions(this.getActivity(),
					new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
					PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
		}
	}
	//MapFragment mapFragment;
	Marker marker;
	@Override

	public void onMapReady(GoogleMap googleMap) {
		gmap = googleMap;



		String readgmap = readMaps();
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

				ViewGroup.LayoutParams params = ((MapFragment)  getFragmentManager().findFragmentById(R.id.map)).getView().getLayoutParams();
				params.height = 1200;
				((MapFragment)  getFragmentManager().findFragmentById(R.id.map)).getView().setLayoutParams(params);
				googleMap.addMarker(new MarkerOptions()
						.position(new LatLng(lat,lng))
						.title(mapname)
						.snippet( oaddress )

				);
				LatLng toCenter = new LatLng(1.424275, 103.838379);


				googleMap.getUiSettings().setScrollGesturesEnabled(true);
				googleMap.getUiSettings().setZoomControlsEnabled(true);
				googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(toCenter, 10));
				googleMap.setMyLocationEnabled(true);
				googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

					@Override
					public void onInfoWindowClick(Marker marker) {
						Bundle extras = new Bundle();
						Intent intent = new Intent(getActivity().getBaseContext(), MapFullView.class);
						String title = marker.getTitle();
						extras.putString("markertitle", title);
						intent.putExtras(extras);
						startActivity(intent);
					}
				});

			}

		}catch (Exception e) {
			e.printStackTrace();
		}





	}

	 @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	 	view = (View) inflater.inflate(R.layout.activity_main, container, false);

	 	checkExpire();
		 getLocationPermission();

		 MapFragment mapFragment = MapFragment.newInstance();
		 mapFragment.getMapAsync(this);
		 getFragmentManager()
				 .beginTransaction()
				 .add(R.id.map, mapFragment)
				 .commit();
		 // Inflate the layout for this fragment
		 if (view != null) {
		        ViewGroup parent = (ViewGroup) view.getParent();
		        if (parent != null) {
		            parent.removeView(view);
		        }
		    }




		 StrictMode.ThreadPolicy policy = new StrictMode.
                 ThreadPolicy.Builder().permitAll().build();
                 StrictMode.setThreadPolicy(policy);

	    Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"fonts/verdana.ttf");
	    TextView lbl = (TextView) view.findViewById(R.id.textViewa);

	    LinearLayout ltop = (LinearLayout)view.findViewById(R.id.top);
	    TextView tba = (TextView) view.findViewById(R.id.tba);
	    tba.setTypeface(type);
	    TextView tbb = (TextView) view.findViewById(R.id.tbb);
	    tbb.setTypeface(type);
	    TextView tbc = (TextView) view.findViewById(R.id.tbc);
	    tbc.setTypeface(type);
	    ScrollView sv = (ScrollView) view.findViewById(R.id.sv123);






	    if(checkbooking().contains("got")){
	    	ltop.setVisibility(View.VISIBLE);
	    	sv.smoothScrollTo(0,0);
	    }

	    ltop.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

		       Intent intent = new Intent(getActivity().getBaseContext(), ViewAllBooking.class);


		       startActivity(intent);
			}

		});


	    String readgmapz = readMaps2();
		try {
	            JSONArray jsonArray = new JSONArray(readgmapz);
	            l=(ListView) view.findViewById(R.id.listView1);
	            SimpleAdapter adapter = new SimpleAdapter(
	            		getActivity(),
	            		list,
	            		R.layout.custom_row_view,
	            		new String[] {"lbname","lbaddress","lbdistance"},
	            		new int[] {R.id.text1,R.id.text2, R.id.text3}){
	                @Override
	            public View getView(int pos, View convertView, ViewGroup parent){
	                View v = convertView;
	                if(v== null){
	                    LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	                    v=vi.inflate(R.layout.custom_row_view, null);
	                }
	                Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"fonts/verdana.ttf");
	                TextView tva = (TextView)v.findViewById(R.id.text1);
	                tva.setText(list.get(pos).get("lbname"));
	                TextView tv = (TextView)v.findViewById(R.id.text2);
	                tv.setText(list.get(pos).get("lbaddress"));
	                tv.setTypeface(type);
	                TextView tvs = (TextView)v.findViewById(R.id.text3);
	                tvs.setText(list.get(pos).get("lbdistance"));
	                tvs.setTypeface(type);
	                return v;
	            }
	            };

              l.setAdapter(null);
              list.clear();
   	            for (int i = 0; i < jsonArray.length(); i++) {



   	            	 JSONObject jsonObject = jsonArray.getJSONObject(i);
      	              mapname =  jsonObject.getString("name");

      	              oaddress =  jsonObject.getString("address");
      	           odistance  =  jsonObject.getString("distance") + " km away";
      	            HashMap<String,String> temp = new HashMap<String,String>();
   			    	temp.put("lbname",mapname);
   			    	temp.put("lbaddress", oaddress);
   			    	temp.put("lbdistance", odistance);
   			    	list.add(temp);

   	            }

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
					Intent intent = new Intent(getActivity().getBaseContext(), SelectClinic.class);
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
					//  getActivity().finish();
					// 5. start the activity
					startActivity(intent);


				}
				else

				   {
					showAlert("Please update your healthbook!");

					   }
				   }

 		           }

 		     );






	}catch (Exception e) {
       e.printStackTrace();
   }

		 lbl.setTypeface(type);


		 TextView viewall = (TextView) view.findViewById(R.id.textViewff);

		 viewall.setTypeface(type);
		 viewall.setOnClickListener(new View.OnClickListener() {

			 public void onClick(View v) {
				 //Intent i = new Intent(getActivity().getApplicationContext(), ViewAllActivity.class);
				 //startActivity(i);

				 // 1. create an intent pass class name or intnet action name
				 //Intent intent = new Intent("com.skytech.ezdr.ViewAllActivity");
				 Intent intent = new Intent(getActivity().getBaseContext(), ViewAllActivity.class);
				 // 2. put key/value data
				 //intent.putExtra("message", "Hello From MainActivity");

				 // 3. or you can add data to a bundle
				 Bundle extras = new Bundle();
				 extras.putString("lat", clat);
				 extras.putString("lng", clong);

				 // 4. add bundle to intent
				 intent.putExtras(extras);

				 // 5. start the activity
				 startActivity(intent);
			 }

		 });
		 setListViewHeightBasedOnChildren(l);
		 return view;
	 }





	static final ArrayList<HashMap<String,String>> list =
		    	new ArrayList<HashMap<String,String>>();

	public static void setListViewHeightBasedOnChildren(final ListView listView) {
		listView.post(new Runnable() {
			@Override
			public void run() {
				ListAdapter listAdapter = listView.getAdapter();
				if (listAdapter == null) {
					return;
				}
				int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
				int listWidth = listView.getMeasuredWidth();
				for (int i = 0; i < listAdapter.getCount(); i++) {
					View listItem = listAdapter.getView(i, null, listView);
					listItem.measure(
							View.MeasureSpec.makeMeasureSpec(listWidth, View.MeasureSpec.EXACTLY),
							View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));


					totalHeight += listItem.getMeasuredHeight();
					Log.d("listItemHeight" + listItem.getMeasuredHeight(), "___________");
				}
				ViewGroup.LayoutParams params = listView.getLayoutParams();
				params.height = (int) ((totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1))));
				listView.setLayoutParams(params);
				listView.requestLayout();

			}
		});
	}
	/* public static void setListViewHeightBasedOnChildren(ListView listView) {
		    ListAdapter listAdapter = listView.getAdapter();
		    if (listAdapter == null)
		        return;

		    int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.UNSPECIFIED);
		    int totalHeight = 0;
		    View view = null;
		    for (int i = 0; i < listAdapter.getCount(); i++) {
		        view = listAdapter.getView(i, view, listView);
		        if (i == 0)
		            view.setLayoutParams(new LayoutParams(desiredWidth, LayoutParams.WRAP_CONTENT));

		        view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
		        totalHeight += view.getMeasuredHeight();
		    }
		    LayoutParams params = listView.getLayoutParams();
		    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		    listView.setLayoutParams(params);
		    listView.requestLayout();
		}
*/
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



	  public String readMaps2() {

	        StringBuilder builder = new StringBuilder();
	        HttpClient client = new DefaultHttpClient();
	        HttpPost httppost= new HttpPost("http://voidq.xyz/appconfig/getnearest.php"); // make sure the url is correct.


	           try {
	        	LatLng latl= getCurrentLocation(this.getActivity());
				nameValuePairs = new ArrayList<NameValuePair>(2);
				// Getting latitude of the current location

				// Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
				nameValuePairs.add(new BasicNameValuePair("currentlat",String.valueOf(latl.latitude)));  // $Edittext_value = $_POST['Edittext_value'];
				nameValuePairs.add(new BasicNameValuePair("currentlng",String.valueOf(latl.longitude)));
				clat = String.valueOf(latl.latitude);
				clong = String.valueOf(latl.longitude);
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
	                String locProvider = locMgr.getBestProvider(criteria, true);
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
	        public String checkbooking(){
	       	 String fulllname = "";
	       	sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
	       	try{


		        HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost= new HttpPost("http://voidq.xyz/appconfig/checkbooking.php"); // make sure the url is correct.
	   			//add your data
	   			nameValuePairs = new ArrayList<NameValuePair>(1);
	   			// Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
	   			nameValuePairs.add(new BasicNameValuePair("username",sharedpreferences.getString("username", null)));  // $Edittext_value = $_POST['Edittext_value'];
	   			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	   			//response=httpclient.execute(httppost);
	   			ResponseHandler<String> responseHandler = new BasicResponseHandler();
	   			final String response = httpclient.execute(httppost, responseHandler);
	   			System.out.println("Response : " + response);


	   			fulllname = response;

	   		}catch(Exception e){

	   			System.out.println("Exception : " + e.getMessage());
	   		}
	   		return fulllname;

	       }

	public void checkExpire(){
		try{


			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost= new HttpPost("http://voidq.xyz/appconfig/checkexpire.php"); // make sure the url is correct.
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			final String response = httpclient.execute(httppost, responseHandler);
			System.out.println("Response : " + response);



		}catch(Exception e){

			System.out.println("Exception : " + e.getMessage());
		}

	}


	public String checkEmptyFields(){
		String status = "";
		sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
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

		getActivity().runOnUiThread(new Runnable() {
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("Error");
				builder.setMessage(message)
						.setCancelable(false)
						.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							}
						});
				AlertDialog alert = builder.create();
				alert.show();
			}
		});
	}

}
