package com.example.mingxuan.voidq;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainOperation extends Activity {
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
    

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	 //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

       //set content view AFTER ABOVE sequence (to avoid crash)
        this.setContentView(R.layout.aboutus); 
        super.onCreate(savedInstanceState);
        // Set View to register.xml
        setContentView(R.layout.aboutus);
        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/verdana.ttf"); 
        TextView Pagename = (TextView)findViewById(R.id.pagename);
        Pagename.setTypeface(type);
     
      
        about = (TextView)findViewById(R.id.lblmessage);
        
        
        about.setTypeface(type);
ImageButton back = (ImageButton) findViewById(R.id.imageButton1);
        
        back.setOnClickListener(new View.OnClickListener() {
        	 @Override
        	    public void onClick(View v) {
        	        finish();
        	    }
         });
        Thread thread = new Thread()
        {
            @Override
            public void run() {
               
                  
                     
                       RetrieveAboutus();
                    
              
            }
        };
        thread.start();
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
    

    void RetrieveAboutus(){
  		try{			
  			 
  			httpclient=new DefaultHttpClient();
  			httppost= new HttpPost("http://voidq.xyz/appconfig/getaboutus.php"); // make sure the url is correct.
  			nameValuePairs = new ArrayList<NameValuePair>(7);
  			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
  			
  			ResponseHandler<String> responseHandler = new BasicResponseHandler();
  			final String response = httpclient.execute(httppost, responseHandler);
  			System.out.println("Response : " + response); 
  		
  			
  			if(response != null){
  				
  				runOnUiThread(new Runnable() {
				    public void run() {
				    	
				    	//allergies.setText(response);
				    	about.setText(response);
				    
				    }
				});
  			
  		
  			}else{
  				showAlert();				
  			}
  			
  		}catch(Exception e){
  			dialog.dismiss();
  			System.out.println("Exception : " + e.getMessage());
  		}
  	}
  	public void showAlert(){
  		MainOperation.this.runOnUiThread(new Runnable() {
  		    public void run() {
  		    	AlertDialog.Builder builder = new AlertDialog.Builder(MainOperation.this);
  		    	builder.setTitle("Error");
  		    	builder.setMessage("Error")  
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