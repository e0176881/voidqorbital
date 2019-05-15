package com.example.mingxuan.voidq;



import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HealthBook extends Fragment {
	static final int DATE_DIALOG_ID = 999;
	Button b;
	EditText nric, fullname, address, allergies, medicalhistory, medication, email,hp ;
	Editable inric, ifullname, iaddress, iallergies, imedicalhistory, imedication ;
	TextView tv;
	HttpPost httppost;
	StringBuffer buffer;
	
	HttpClient httpclient;
	List<NameValuePair> nameValuePairs;
	ProgressDialog dialog = null;
	public int currentimageindex=0;
    Timer timer;
    TimerTask task;
    ImageView slidingimage;
    
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String pUserName = "username"; 
    public static final String pPassword = "password"; 
    

    View view;
	  @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        // Inflate the layout for this fragment
		  
		  
		  view = inflater.inflate(R.layout.healthbooklor, container, false);
		   Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"fonts/verdana.ttf"); 
	        /* TextView Pagename = (TextView)view.findViewById(R.id.pagename) */
	        TextView lb1 = (TextView)view.findViewById(R.id.lbusername);
	        lb1.setTypeface(type);
	        TextView lb2 = (TextView)view.findViewById(R.id.lbusername2);
	        lb2.setTypeface(type);
	        TextView lb3 = (TextView)view.findViewById(R.id.lbusername3);
	        lb3.setTypeface(type);
	        TextView lb4 = (TextView)view.findViewById(R.id.lbusername4);
	        lb4.setTypeface(type);
	        TextView lb5 = (TextView)view.findViewById(R.id.lbusername5);
	        lb5.setTypeface(type);
		  TextView lb6 = (TextView)view.findViewById(R.id.lbemail);
		  lb6.setTypeface(type);
	       
	        //Pagename.setTypeface(type);
	      

	        
	        b = (Button)view.findViewById(R.id.btnBook);  
	        nric = (EditText)view.findViewById(R.id.his_NRIC);
	        fullname= (EditText)view.findViewById(R.id.his_fullname);
	        allergies = (EditText)view.findViewById(R.id.allergies);
	        medication = (EditText)view.findViewById(R.id.medication);
	        medicalhistory = (EditText)view.findViewById(R.id.medicalhistory);
	        Button updatee = (Button)view.findViewById(R.id.btnUpdate);  
	        tv = (TextView)view.findViewById(R.id.TextViewaaa);
	        email = (EditText) view.findViewById(R.id.his_emailadd);
	        hp= (EditText) view.findViewById(R.id.his_hp);
	       nric.setTypeface(type);
	       fullname.setTypeface(type);
	       allergies.setTypeface(type);
	       medication.setTypeface(type);
	       medicalhistory.setTypeface(type);
	       email.setTypeface(type);
	       hp.setTypeface(type);
	       updatee.setTypeface(type);
	       tv.setTypeface(type);
	       
	       
	       
	        b.setVisibility(View.GONE);
				b.setTypeface(type);
	        updatee.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					
			    

					
					dialog = ProgressDialog.show(getActivity(), "", 
	                        "Updating...", true);
					 new Thread(new Runnable() {
						    public void run() {
						    	Register2();					      
						    }
						  }).start();	
					
		          
						
				}
			});
	    
						  
						    				      
						  			
				
			


	        
	        Thread thread = new Thread()
	        {
	            @Override
	            public void run() {
	               
	                  if(isNetworkConnected()!=false)
	                  {
	                     
	                       Register();
	                  } else
	                  {
	                	  noDB();
	                  }
	            }
	        };
	        thread.start();
	    
		  
		  
	        return view;
	    
}

	  
	    void noDB()
	    {
	    	
	    	sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
	    	nric.setText(sharedpreferences.getString("NRIC", null));
	    	fullname.setText(sharedpreferences.getString("fullname", null));
			email.setText(sharedpreferences.getString("email", null));

			allergies.setText(sharedpreferences.getString("allergies", null));
	  	medication.setText(sharedpreferences.getString("medication", null));
	    	medicalhistory.setText(sharedpreferences.getString("medicalhistory", null));
	        
			
	    }
	    void Register2(){
	    	
	  		try{			
	  			 
	  			httpclient=new DefaultHttpClient();
	  			httppost= new HttpPost("http://voidq.xyz/appconfig/myhistory.php"); // make sure the url is correct.
	  			nameValuePairs = new ArrayList<NameValuePair>(8);
	  			sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

				nameValuePairs.add(new BasicNameValuePair("username",sharedpreferences.getString(pUserName, "").trim()));
	  			nameValuePairs.add(new BasicNameValuePair("NRIC",nric.getText().toString().trim()));  // $Edittext_value = $_POST['Edittext_value'];
	  			nameValuePairs.add(new BasicNameValuePair("fullname",fullname.getText().toString().trim()));
				nameValuePairs.add(new BasicNameValuePair("email",email.getText().toString().trim()));
				nameValuePairs.add(new BasicNameValuePair("allergies",allergies.getText().toString().trim()));
	  			nameValuePairs.add(new BasicNameValuePair("medication",medication.getText().toString().trim())); 
	  			nameValuePairs.add(new BasicNameValuePair("medicalhistory",medicalhistory.getText().toString().trim()));
				nameValuePairs.add(new BasicNameValuePair("handphone",hp.getText().toString().trim()));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	  			
	  			ResponseHandler<String> responseHandler = new BasicResponseHandler();
	  			final String response = httpclient.execute(httppost, responseHandler);
	  			System.out.println("Response : " + response); 
	  			getActivity().runOnUiThread(new Runnable() {
	  			    public void run() {
	  			    	tv.setText("Response from PHP : " + response);
	  					dialog.dismiss();
	  			    }
	  			});
	  		
	  			
	  			if(response.contains("success")){
	  				getActivity().runOnUiThread(new Runnable() {
	  				    public void run() {
	  				    	Editor editor = sharedpreferences.edit();
	  						editor.putString("NRIC", nric.getText().toString().trim()); // Storing string
	  						editor.putString("fullname", fullname.getText().toString().trim());
							editor.putString("email", email.getText().toString().trim());
	  						editor.putString("allergies",allergies.getText().toString().trim());
	  						editor.putString("medication",medication.getText().toString().trim());
	  						editor.putString("medicalhistory",medicalhistory.getText().toString().trim());
	  						
	  						editor.commit();


	  				    	Toast.makeText(getActivity(),"History Updated!", Toast.LENGTH_SHORT).show();
	  				    	
	  				    }
	  				});
	  				
	  			
	  		
	  			}
	  			else if(response.trim().equals("handphone number already exist"))
				{
					showAlert("Handphone number in use by another person");
				}
				else if(response.trim().equals("You have not post anything"))
				{
					showAlert("Update error");
				}
				else
				{
					showAlert("No changes made");
				}



	  			
	  		}catch(Exception e){
	  			dialog.dismiss();
	  			System.out.println("Exception : " + e.getMessage());
	  		}
	  		
	  	}
	    void Register(){
	    	
	  		try{			
	  			 
	  			httpclient=new DefaultHttpClient();
	  			httppost= new HttpPost("http://voidq.xyz/appconfig/gethistory.php"); // make sure the url is correct.
	  			nameValuePairs = new ArrayList<NameValuePair>(1);
	  			sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
	  	        


	  			nameValuePairs.add(new BasicNameValuePair("username",sharedpreferences.getString(pUserName, "").trim()));
	  			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	  			
	  			ResponseHandler<String> responseHandler = new BasicResponseHandler();
	  			final String response = httpclient.execute(httppost, responseHandler);
	  			System.out.println("Response : " + response); 
	  		
	  			
	  			if(response != null){
	  				if( response != "" & response != "empty")
	  				{
	  					getActivity().runOnUiThread(new Runnable() {
					    public void run() {
					    	String[] values = response.split(",");
					    	//allergies.setText(response);
					    	nric.setText(values[0].trim(),TextView.BufferType.EDITABLE);
					    	fullname.setText(values[1].trim(),TextView.BufferType.EDITABLE);
							email.setText(values[2].trim());
							email.setEnabled(false);
					    	allergies.setText(values[3].trim(),TextView.BufferType.EDITABLE);
					  	medication.setText(values[4].trim(),TextView.BufferType.EDITABLE);
					    	medicalhistory.setText(values[5].trim(),TextView.BufferType.EDITABLE);
							hp.setText(values[6].trim(),TextView.BufferType.EDITABLE);
					    	Editor editor = sharedpreferences.edit();
							editor.putString("NRIC", values[0].toString().trim()); // Storing string
							editor.putString("fullname", values[1].toString().trim());
							editor.putString("email", values[2].toString().trim());
							
							editor.putString("allergies",values[3].toString().trim());
							editor.putString("medication",values[4].toString().trim());
							editor.putString("medicalhistory",values[5].toString().trim());
							
							editor.commit();
					    }
					});
	  				} else
	  				{
	  					nric.setText("",TextView.BufferType.EDITABLE);
				    	fullname.setText("",TextView.BufferType.EDITABLE);

				    	
				    	allergies.setText("",TextView.BufferType.EDITABLE);
				  	medication.setText("",TextView.BufferType.EDITABLE);
				    	medicalhistory.setText("",TextView.BufferType.EDITABLE);
						hp.setText("",TextView.BufferType.EDITABLE);
					}
	  				
	  			}else{
	  				showAlert("Retrieve Error");
	  			}
	  			
	  		}catch(Exception e){
	  			dialog.dismiss();
	  			System.out.println("Exception : " + e.getMessage());
	  		}
	  		
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
	  	private boolean isNetworkConnected() {
	  	  ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
	  	  NetworkInfo ni = cm.getActiveNetworkInfo();
	  	  if (ni == null) {
	  	   // There are no active networks.
	  	   return false;
	  	  } else
	  	   return true;
	  	   
	  	 }
	
}
