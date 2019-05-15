package com.example.mingxuan.voidq;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
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
import java.util.UUID;

public class mapok extends Activity {
	
	HttpPost httppost;
	StringBuffer buffer;
	String pay_key;
	HttpClient httpclient;
	List<NameValuePair> nameValuePairs;
	ProgressDialog dialog = null;
	public int currentimageindex=0;
 double price = 0.0;
    Timer timer;
    TimerTask task;
    ImageView slidingimage;
    String symb,desc,mc,mapname,clinicid;
    TextView symptoms, description, mcla, pricing,mapnamez;
    private int[] IMAGE_IDS = {
            R.drawable.pic2, R.drawable.pic3,
            R.drawable.pic4, R.drawable.pic5
        };
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Intent in = getIntent();
    	symb = in.getStringExtra("Sym");
    	desc = in.getStringExtra("desc");
    	mc = in.getStringExtra("datetime");
    	mapname=in.getStringExtra("clinicname");
    	clinicid=in.getStringExtra("clinicid");
    	 //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

       //set content view AFTER ABOVE sequence (to avoid crash)
        //this.setContentView(R.layout.payment); 
        super.onCreate(savedInstanceState);
        // Set View to register.xml
        setContentView(R.layout.payment);
       /*Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent); */
        
        TextView g = (TextView)findViewById(R.id.pagename);
        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/verdana.ttf");
        TextView a = (TextView)findViewById(R.id.lblmessage);
        TextView b = (TextView)findViewById(R.id.lblbookclinic);
        TextView c = (TextView)findViewById(R.id.lblsyndrome);
        TextView d = (TextView)findViewById(R.id.asd);
        TextView e = (TextView)findViewById(R.id.lblmc);
        a.setTypeface(type);
        b.setTypeface(type);
        c.setTypeface(type);
        d.setTypeface(type);
        e.setTypeface(type);
        g.setTypeface(type);
        pricing = (TextView)findViewById(R.id.textView2);
        pricing.setTypeface(type);
        Button checkout = (Button)findViewById(R.id.btncheckout);
        checkout.setTypeface(type);
        
ImageButton back = (ImageButton) findViewById(R.id.imageButton1);
        
        back.setOnClickListener(new View.OnClickListener() {
        	 @Override
        	    public void onClick(View v) {
        	        finish();
        	    }
         });


		TextView tvcancel = (TextView) findViewById(R.id.tvcancel);

		tvcancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});


            	
            	symptoms = (TextView)findViewById(R.id.lblsyndromes);

            		symptoms.setText(symb);
            	
            	symptoms.setTypeface(type);
            	//description=(TextView)findViewById(R.id.lbldescz);
            	//description.setText(desc);
            	//description.setTypeface(type);
            	mcla=(TextView)findViewById(R.id.lblmcz);
            	mcla.setText(mc);
            	mcla.setTypeface(type);
            	mapnamez=(TextView)findViewById(R.id.lblbookclinic);
            	mapnamez.setText( mapname);
            	mapnamez.setTypeface(type);


     
        // Listening to Login Screen link
        checkout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // --- find the text view --
				Book();
            }
         });
  
      
    }


	public void Book(){
		try{
			UUID uniqueKey = UUID.randomUUID();
			SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPrefs", MODE_APPEND);
			String[] timedate = mc.split(",");
			httpclient=new DefaultHttpClient();
			httppost= new HttpPost("http://voidq.xyz/appconfig/book.php"); // make sure the url is correct.
			//add your data
			nameValuePairs = new ArrayList<NameValuePair>(6);
			// Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
			nameValuePairs.add(new BasicNameValuePair("clinicid",clinicid.toString().trim()));
			nameValuePairs.add(new BasicNameValuePair("symptoms",symb.toString().trim()));
			nameValuePairs.add(new BasicNameValuePair("date",timedate[0].toString().trim()));
			nameValuePairs.add(new BasicNameValuePair("time",timedate[1].toString().trim()));
			nameValuePairs.add(new BasicNameValuePair("username",pref.getString("username", null).toString().trim()));
			nameValuePairs.add(new BasicNameValuePair("refno",uniqueKey.toString().replace("-", "").trim()));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			//response=httpclient.execute(httppost);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			final String response = httpclient.execute(httppost, responseHandler);
			System.out.println("Response : " + response);
			if(response.contains("success")){
				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(mapok.this,"Booking Success", Toast.LENGTH_SHORT).show();

					}
				});
				Intent launchmain= new Intent(getApplicationContext(),summary.class);
				launchmain.putExtra("Sym", symb.toString());
				launchmain.putExtra("desc", symptoms.getText().toString());
				launchmain.putExtra("datetime", timedate[0].toString() + "," + timedate[1].toString());
				launchmain.putExtra("clinicname",mapname);
				launchmain.putExtra("refno",uniqueKey.toString().replace("-", ""));
				startActivity(launchmain);
			}else{
				// do nothing bah
			}


			}catch(Exception e){
			dialog.dismiss();
			System.out.println("Exception : " + e.getMessage());
		}

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
    public void onBackPressed() {
       return ; // Do Here what ever you want do on back press;
    }
}
