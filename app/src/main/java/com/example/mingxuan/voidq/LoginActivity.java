package com.example.mingxuan.voidq;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;




public class LoginActivity extends Activity {
	Button b;
	EditText et,pass;
	TextView tv;
	HttpPost httppost;
	StringBuffer buffer;
	HttpResponse response;
	HttpClient httpclient;
	List<NameValuePair> nameValuePairs;
	ProgressDialog dialog = null;

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
        setContentView(R.layout.login);
        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/verdana.ttf"); 

        //Typeface font = Typeface.createFromAsset(this.getAssets(), "trado.ttf");  
        TextView registerScreen = (TextView) findViewById(R.id.link_to_register);
        TextView requestpassword = (TextView) findViewById(R.id.link_to_register2);
        TextView regg = (TextView) findViewById(R.id.regg);
        regg.setTypeface(type);
        b = (Button)findViewById(R.id.btnLogin);  
        b.setTypeface(type);
        et = (EditText)findViewById(R.id.username);
        pass= (EditText)findViewById(R.id.password);
        tv = (TextView)findViewById(R.id.tv);
       TextView lbu = (TextView)findViewById(R.id.lbusername);
       //TextView lbp = (TextView)findViewById(R.id.lbpassword);
       et.setTypeface(type);
       pass.setTypeface(type);
        TextView Pagename = (TextView)findViewById(R.id.pagename);
        Pagename.setTypeface(type);
        lbu.setTypeface(type);
        //lbp.setTypeface(type);
        
        /*
        Button p = (Button)findViewById(R.id.imgbtnHistory); 
        p.setTypeface(type);
        p.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// Switching to Register screen
				Intent i = new Intent(getApplicationContext(), MyHistory.class);
				startActivity(i);
			}
		});
		*/
        
        b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String uname = et.getText().toString();
				String upw = pass.getText().toString();
				
				if(uname.length() == 0)
				{
					showAlert2();
					
				}
				else if(upw.length() == 0)
				{
					
					showAlert3();
				}
				else
				{
				dialog = ProgressDialog.show(LoginActivity.this, "", 
                        "Validating user...", true);
				 new Thread(new Runnable() {
					    public void run() {
					    	login();
					    }
					  }).start();
				}
			} 
		});
        // Listening to register new account link
        registerScreen.setTypeface(type);
        registerScreen.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// Switching to Register screen
				Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
				startActivity(i);
			}
		});
        requestpassword.setTypeface(type);
 requestpassword.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// Switching to Register screen
			//	Intent i = new Intent(getApplicationContext(), ForgetPassword.class);
			//	startActivity(i);
			}
		});
 ImageButton bck = (ImageButton) findViewById(R.id.imageButton1);
 bck.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
					Intent i = new Intent(getApplicationContext(), voidq.class);
					startActivity(i);
			}
		});

    }
    
    
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.exit_title:
        	this.finish();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        return true;

        default:
        return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onResume() {
       sharedpreferences=getSharedPreferences(MyPREFERENCES, 
    		   MODE_APPEND);
       if (sharedpreferences.getString("username", null) != null)
       {
     
          Intent i = new Intent(getApplicationContext(),BookADocHome.class);
          startActivity(i);
       
       }
       super.onResume();
      
    }

   void login(){


		try{
			
			httpclient=new DefaultHttpClient();
			httppost= new HttpPost("http://voidq.xyz/appconfig/index.php"); // make sure the url is correct.
			//add your data
			nameValuePairs = new ArrayList<NameValuePair>(2);
			// Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar, 
			nameValuePairs.add(new BasicNameValuePair("username",et.getText().toString().trim()));  // $Edittext_value = $_POST['Edittext_value'];
			nameValuePairs.add(new BasicNameValuePair("password",pass.getText().toString().trim()));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			//response=httpclient.execute(httppost);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			final String response = httpclient.execute(httppost, responseHandler);
			System.out.println("Response : " + response); 
			runOnUiThread(new Runnable() {
			    public void run() {
			    //	tv.setText("Response from PHP : " + response);
					dialog.dismiss();
			    }
			});
			
			if(response.contains("success")){
				runOnUiThread(new Runnable() {
				    public void run() {
				    	Toast.makeText(LoginActivity.this,"Login Success", Toast.LENGTH_SHORT).show();
				    	
				    }
				});
				String fulllname = fullname();
				SharedPreferences pref = getApplicationContext().getSharedPreferences(MyPREFERENCES, MODE_APPEND); // 0 - for private mode
				Editor editor = pref.edit();
				editor.putString("username", et.getText().toString().trim()); // Storing string
				editor.putString("password", pass.getText().toString().trim());
				editor.putString("fullname", fulllname);
				editor.commit();
				//showSuccess();
				startActivity(new Intent(LoginActivity.this, BookADocHome.class));
				
			}else{
				showAlert();				
			}
			
		}catch(Exception e){
			dialog.dismiss();
			System.out.println("Exception : " + e.getMessage());
		}
	}
    
    public String fullname(){
    	 String fulllname = "";
    	try{			
			
			httpclient=new DefaultHttpClient();
			httppost= new HttpPost("http://voidq.xyz/appconfig/getfullname.php"); // make sure the url is correct.
			//add your data
			nameValuePairs = new ArrayList<NameValuePair>(1);
			// Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar, 
			nameValuePairs.add(new BasicNameValuePair("username",et.getText().toString().trim()));  // $Edittext_value = $_POST['Edittext_value'];
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			//response=httpclient.execute(httppost);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			final String response = httpclient.execute(httppost, responseHandler);
			System.out.println("Response : " + response); 
			runOnUiThread(new Runnable() {
			    public void run() {
			    //	tv.setText("Response from PHP : " + response);
					dialog.dismiss();
			    }
			});
			
			fulllname = response;
			
		}catch(Exception e){
			dialog.dismiss();
			System.out.println("Exception : " + e.getMessage());
		}
		return fulllname;
    
    }

	public void showSuccess(){
		LoginActivity.this.runOnUiThread(new Runnable() {
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
				builder.setTitle("Login Error.");
				builder.setMessage("Success")
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
    
	public void showAlert(){
		LoginActivity.this.runOnUiThread(new Runnable() {
		    public void run() {
		    	AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
		    	builder.setTitle("Login Error.");
		    	builder.setMessage("Invalid username/password!")  
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
	public void showAlert2(){
		LoginActivity.this.runOnUiThread(new Runnable() {
		    public void run() {
		    	AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
		    	builder.setTitle("Login Error.");
		    	builder.setMessage("Please enter username (HandPhone)!")  
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
	public void showAlert3(){
		LoginActivity.this.runOnUiThread(new Runnable() {
		    public void run() {
		    	AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
		    	builder.setTitle("Login Error.");
		    	builder.setMessage("Please enter password!")  
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