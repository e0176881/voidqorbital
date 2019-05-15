package com.example.mingxuan.voidq;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.apache.http.HttpResponse;
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


public class voidq extends Activity implements LocationListener {
	
	// flag for Internet connection status
    Boolean isInternetPresent = false;
     
    // Connection detector class
    ConnectionDetector cd;
	
	SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String pUserName = "username"; 
    public static final String pPassword = "password"; 
    boolean enabled;
    private static final int RC_SIGN_IN = 9001;
    LocationManager lm;
    private GoogleSignInClient mGoogleSignInClient;
    private static final String TAG = "SignInActivity";
    HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	 //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

       //set content view AFTER ABOVE sequence (to avoid crash)
    
       
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ezdrmain);

        Toast.makeText(getBaseContext(), "This app require internet connection & gps to work", Toast.LENGTH_LONG).show();
        
        cd = new ConnectionDetector(getApplicationContext());
        
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

       
       // 	  lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, this);
          
      
        
         enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

        StrictMode.ThreadPolicy policy = new StrictMode.
                ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        // Check if enabled and if not send user to the GPS settings

        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/verdana.ttf");
        TextView tv1 = (TextView)findViewById(R.id.tv1);
        tv1.setTypeface(type);

        

        
        Button nbtnLogin = (Button) findViewById(R.id.btnLogin);
        nbtnLogin.setTypeface(type);
        nbtnLogin.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// Switching to Register screen
				  
				 
				  
				Intent i = new Intent(getApplicationContext(), LoginActivity.class);
				startActivity(i);
				finish();
			}
		});
        TextView registerScreen = (TextView) findViewById(R.id.newreg);
        registerScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Switching to Register screen



                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
                //finish();
            }
        });


        SignInButton gsi = findViewById(R.id.sign_in_button);
        gsi.setSize(SignInButton.SIZE_WIDE);
        gsi.setColorScheme(SignInButton.COLOR_DARK);
        gsi.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

              signIn();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)

                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
      
    }
    @Override
    protected void onResume() {
       sharedpreferences=getSharedPreferences(MyPREFERENCES, 
    		   MODE_APPEND);
       if (sharedpreferences.getString("username", null) != null)
       {
           FirebaseMessaging.getInstance().subscribeToTopic(sharedpreferences.getString(pUserName, "").replace("@",""));
          Intent i = new Intent(getApplicationContext(),BookADocHome.class);
          startActivity(i);
          finish();
       
       }
       if (!enabled) {
			  showAlert();
	          
	        }
       super.onResume();
      
    }
    
    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
 
        // Setting Dialog Title
        alertDialog.setTitle(title);
 
        // Setting Dialog Message
        alertDialog.setMessage(message);
         
        // Setting alert dialog icon
        //alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);
 
        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
 
        // Showing Alert Message
        alertDialog.show();
    }
    
    public void showAlert(){
    	new AlertDialog.Builder(this)
        .setTitle("GPS Off")
        .setMessage("Do you want to switch on the gps?")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {          
            	  Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		            startActivity(intent);
            }
        })
        .setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) { 
            	showAlert();
            }
        })
        .show();
    
	}

    public void showAlert1(){
        voidq.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(voidq.this);
                builder.setTitle("Login Error.");
                builder.setMessage("Please relog")
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
    

    	  @Override
    	    public void onProviderEnabled(String provider) {
    	         
    	        /******** Called when User on Gps  *********/
    	         
    	        Toast.makeText(getBaseContext(), "Gps turned on ", Toast.LENGTH_LONG).show();
    	        Intent i = new Intent(getApplicationContext(),voidq.class);
    	          startActivity(i);
    	        
    	    }
    		@Override
    		public void onLocationChanged(Location location) {
    			// TODO Auto-generated method stub
    			
    		}
    		@Override
    		public void onStatusChanged(String provider, int status, Bundle extras) {
    			// TODO Auto-generated method stub
    			
    		}
    		@Override
    		public void onProviderDisabled(String provider) {
    			 
    			  isInternetPresent = cd.isConnectingToInternet();
    			  
                  // check for Internet status
                 
                      // Internet Connection is Present
                      // make HTTP requests
 
  					  showAlert();
  			          

                  

    		}



    private void signIn() {


        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {

            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            System.out.println("test");
            // Signed in successfully, show authenticated UI.
            updateUI(account);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI(@Nullable GoogleSignInAccount account) {

        if(account!=null) {

            Register(account.getEmail(), account.getGivenName());

        }

    }



    void Register(String email,String name){
        try{

            httpclient=new DefaultHttpClient();
            httppost= new HttpPost("http://voidq.xyz/appconfig/reggoogle.php"); // make sure the url is correct.
            nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("Email",email.toString().trim()));
            nameValuePairs.add(new BasicNameValuePair("fullname",name.toString().trim()));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response = httpclient.execute(httppost, responseHandler);
            System.out.println("Response : " + response);


            if(response.trim().equals("success") || response.trim().equals("ok") ){


                Toast.makeText(getBaseContext(), "Logged in as " + email, Toast.LENGTH_LONG).show();
                SharedPreferences pref = getApplicationContext().getSharedPreferences(MyPREFERENCES, MODE_APPEND); // 0 - for private mode
                Editor editor = pref.edit();
                editor.putString("username", email);
                editor.commit();
                startActivity(new Intent(voidq.this, BookADocHome.class));

            }else{
                Toast.makeText(getBaseContext(), "Email already exist", Toast.LENGTH_LONG).show();
                mGoogleSignInClient.signOut();
            }

        }catch(Exception e){

            System.out.println("Exception : " + e.getMessage());
        }
    }
}