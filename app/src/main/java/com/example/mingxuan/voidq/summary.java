package com.example.mingxuan.voidq;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

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

public class summary extends Activity {
	Double price;
	String symb,desc,mc,mapname,paymentid,datet,refno;
	TextView symptoms, description, datetime, pricing,mapnamez,valid, rn;
	HttpPost httppost;
	StringBuffer buffer;
	HttpResponse response;
	HttpClient httpclient;
	List<NameValuePair> nameValuePairs;
	ProgressDialog dialog = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		//Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		//Remove notification bar
		//this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		//set content view AFTER ABOVE sequence (to avoid crash)
		this.setContentView(R.layout.summary); 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.summary);
		Typeface type = Typeface.createFromAsset(getAssets(),"fonts/verdana.ttf"); 
		
        TextView Pagename = (TextView)findViewById(R.id.pagename);
        Pagename.setTypeface(type);
		Intent in = getIntent();
		symb = in.getStringExtra("Sym");
		desc = in.getStringExtra("desc");
		mapname=in.getStringExtra("clinicname");
		datet= in.getStringExtra("datetime");
		refno = in.getStringExtra("refno");


		TextView a =  (TextView)findViewById(R.id.lblmessage);
		TextView b =  (TextView)findViewById(R.id.lblbookclinic);
		TextView c =  (TextView)findViewById(R.id.lblsyndrome);
		TextView d =  (TextView)findViewById(R.id.lbldesc);
		TextView h =  (TextView)findViewById(R.id.textView3);
		TextView j =  (TextView)findViewById(R.id.textView4); 
		a.setTypeface(type);
        b.setTypeface(type);
        c.setTypeface(type);
        d.setTypeface(type);
        h.setTypeface(type);
        j.setTypeface(type);



				

				symptoms = (TextView)findViewById(R.id.lblsyndromes);
				symptoms.setText(symb);
				symptoms.setTypeface(type);
				description=(TextView)findViewById(R.id.lbldescz);
				description.setText(desc);
				description.setTypeface(type);
				mapnamez=(TextView)findViewById(R.id.lblbookclinic);
				mapnamez.setText("Booking Clinic - " + mapname);
				mapnamez.setTypeface(type);
				datetime= (TextView)findViewById(R.id.textView4);
				datetime.setText(datet);
				datetime.setTypeface(type);
				rn=(TextView)findViewById(R.id.lblrn);
				rn.setText(refno);
				rn.setTypeface(type);




		Button back = (Button) findViewById(R.id.btnreturn);

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), BookADocHome.class);
				startActivity(i);
				finish();
			}
		});







	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}
	/*public boolean onOptionsItemSelected(MenuItem item) {
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
	}*/

	public void fullname(){

		try{			

			httpclient=new DefaultHttpClient();
			httppost= new HttpPost("http://ezdr.com.sg/appsettings/getdatetime.php"); // make sure the url is correct.
			//add your data
			nameValuePairs = new ArrayList<NameValuePair>(1);
			// Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar, 
			nameValuePairs.add(new BasicNameValuePair("paymentid",paymentid));  // $Edittext_value = $_POST['Edittext_value'];
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			//	response=httpclient.execute(httppost);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			final String response = httpclient.execute(httppost, responseHandler);
			System.out.println("Response : " + response); 
			runOnUiThread(new Runnable() {
				public void run() {
					valid = (TextView)findViewById(R.id.textView4);
					valid.setText(response);
				}
			});



		}catch(Exception e){
			dialog.dismiss();
			System.out.println("Exception : " + e.getMessage());
		}


	}




}
