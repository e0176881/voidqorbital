package com.example.mingxuan.voidq;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.time.Timepoint;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class Book extends Activity {
	SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String pUserName = "username"; 
    public static final String pPassword = "password";
	private long mLastClickTime = 0;
	private long mLastClickTime2 = 0;
	Button bookbutton;
	  public int currentimageindex=0;
	HttpPost httppost;
	StringBuffer buffer;
	HttpResponse response;
	HttpClient httpclient;
	List<NameValuePair> nameValuePairs;
	ProgressDialog dialog = null;
	    Timer timer;
	    TimerTask task;
	    TextView lbsyn, txtTime,txtDate;
	    ImageView slidingimage;
	    CheckBox cough,flu,bodypain,fever, diarrhea, vomiting, headache,others;
	    String text,mcz, clickedname;
	    EditText symptoms;
	    RadioButton mc1,mc2,mc3;
	    private int[] IMAGE_IDS = {
	            R.drawable.pic2, R.drawable.pic3,
	            R.drawable.pic4, R.drawable.pic5
	        };
	    private Button incButton;
	    private Button decButton;
	    private EditText editText;
	 
	    private int max_range = 20;
	    private int min_range = 0;
	    private int initialvalues = 1;
    private int mYear, mMonth, mDay, mHour, mMinute;

	 Calendar c;
	 Timepoint[] timepoints;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	 //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.
        	    ThreadPolicy.Builder().permitAll().build();
        	    StrictMode.setThreadPolicy(policy); 
        //Remove notification bar
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

       //set content view AFTER ABOVE sequence (to avoid crash)
        this.setContentView(R.layout.abc); 
        super.onCreate(savedInstanceState);
        // Set View to register.xml
        setContentView(R.layout.abc);
        
        Intent intent = getIntent();
        
        // 2. get message value from intent
        clickedname = intent.getStringExtra("clinicname");
        
        

        bookbutton = (Button) findViewById(R.id.imgbtnBook);
        cough = (CheckBox)findViewById(R.id.checkBox1);
        flu=(CheckBox)findViewById(R.id.checkBox3);
        bodypain=(CheckBox)findViewById(R.id.checkBox4);
        fever=(CheckBox)findViewById(R.id.checkBox2);
        others=(CheckBox)findViewById(R.id.checkBox5);
        diarrhea=(CheckBox)findViewById(R.id.cbDiarrhea);
        vomiting=(CheckBox)findViewById(R.id.cbVomiting);
        headache=(CheckBox)findViewById(R.id.cbHeadache);
        symptoms=(EditText)findViewById(R.id.editText1);
        //mc1=(RadioButton)findViewById(R.id.rbMC);
        //mc2=(RadioButton)findViewById(R.id.rbMC2);
        //mc3=(RadioButton)findViewById(R.id.rbMC3);
        TextView lbtime = (TextView)findViewById(R.id.lbtime);
		TextView lbdate = (TextView)findViewById(R.id.lbdate);
        lbsyn = (TextView)findViewById(R.id.lblSymptoms);
        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/verdana.ttf"); 
        TextView Pagename = (TextView)findViewById(R.id.pagename);
        Pagename.setTypeface(type);
        symptoms.setVisibility(View.GONE);
        bookbutton.setTypeface(type);    
        cough.setTypeface(type);
        flu.setTypeface(type);
        bodypain.setTypeface(type);
        fever.setTypeface(type);   
        others.setTypeface(type);
        diarrhea.setTypeface(type);
        vomiting.setTypeface(type);
        headache.setTypeface(type);
        symptoms.setTypeface(type);
        //mc1.setTypeface(type);
        //mc2.setTypeface(type);
        //mc3.setTypeface(type);
       lbtime.setTypeface(type);
       lbdate.setTypeface(type);
         txtTime=(TextView)findViewById(R.id.displaytime);
		txtDate=(TextView)findViewById(R.id.displaydate);

		 c = Calendar.getInstance();
		final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {


			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
								  int dayOfMonth) {
				// TODO Auto-generated method stub
				c.set(Calendar.YEAR, year);
				c.set(Calendar.MONTH, monthOfYear);
				c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				updateLabel();
			}

		};

		lbdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (SystemClock.elapsedRealtime() - mLastClickTime2 < 100){
					return;
				}
				mLastClickTime2 = SystemClock.elapsedRealtime();
				DatePickerDialog datePickerDialog = new DatePickerDialog(Book.this, date, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
						c.get(Calendar.DAY_OF_MONTH));
				datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
				datePickerDialog.show();

			}
		});



		lbtime.setOnClickListener(new View.OnClickListener() {


			public void onClick(View v) {
				if(txtDate.getText().toString().equals(""))
				{
					showAlert100();
					return;
				}


				if (SystemClock.elapsedRealtime() - mLastClickTime < 100){
					return;
				}
				mLastClickTime = SystemClock.elapsedRealtime();
				final TimePickerDialog tpd = TimePickerDialog.newInstance(
						new TimePickerDialog.OnTimeSetListener() {
							@Override
							public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
								String curTime = String.format("%02d:%02d", hourOfDay, minute);
								SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
								String str = sdf.format(new Date());
								if(checkiftaken(getClinicID(),curTime,txtDate.getText().toString()).equals("cannot"))
								{
									showAlert4();

								}
								Date date1=null;
								Date date2= new Date();
								SimpleDateFormat lame = new SimpleDateFormat("dd/MM/yy");
								try {
									 date1 = lame.parse(txtDate.getText().toString());
									 date2 = lame.parse(date2.toString());
								} catch(Exception e)
								{

								}
								if(date1.compareTo(date2) <=0) {


									if (Integer.parseInt(curTime.replace(":", "")) < Integer.parseInt(str.replace(":", ""))) {
										showAlert10();
									}
								}
								txtTime.setText(curTime);
							//	tpd.setOnCancelListener(this);
							}
						},
						c.get(Calendar.HOUR_OF_DAY),
						c.get(Calendar.MINUTE),
						false
				);
				String response=	getoperatinghours(getClinicID());
				if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ||
						c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
							String[] split = response.split(",");
							String[] weekE = split[1].split("-");
							String[] actual = weekE[0].split(":");
							String[] end= weekE[1].split(":");

				//	Timepoint newtime = new Timepoint(weekE);
						tpd.setMinTime(Integer.parseInt(actual[0].toString()),Integer.parseInt(actual[1].toString()),0);
						tpd.setMaxTime(Integer.parseInt(end[0].toString()),Integer.parseInt(end[1].toString()),0);
				}
				else
				{
					String[] split = response.split(",");
					String[] weekE = split[0].split("-");
					String[] actual = weekE[0].split(":");
					String[] end= weekE[1].split(":");

					//	Timepoint newtime = new Timepoint(weekE);
					tpd.setMinTime(Integer.parseInt(actual[0].toString()),Integer.parseInt(actual[1].toString()),0);
					tpd.setMaxTime(Integer.parseInt(end[0].toString()),Integer.parseInt(end[1].toString()),0);
				}



			//	tpd.setDisabledTimes();
				tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialogInterface) {
						Log.d("TimePicker", "Dialog was cancelled");
					}
				});
				tpd.setVersion(TimePickerDialog.Version.VERSION_2);
				tpd.setTimeInterval(1,15);
				tpd.show(getFragmentManager(), "TimePickerDialog");
			}



		});

        //


        lbsyn.setTypeface(type);
        //lbmc.setText(clickedname);
        // Listening to Login Screen link
        bookbutton.setOnClickListener(new OnClickListener() {
        	@Override
            public void onClick(View v) {
                // --- find the text view --
        		
        		text = null;
            	  if(cough.isChecked())
                  {
                  text = "Cough"; 
                  }
                  if(flu.isChecked())
                  {
                	  if(text != null && text != "" && text != "null")
                	  {
                		  text = text + "," + " Flu"; 
                	  }
                	  else
                	  {
                		  text = "Flu"; 
                	  }
                  }
                  if(bodypain.isChecked())
                  {
                	  if(text != null && text != "" && text != "null")
                	  {
                		  text = text + "," + " Bodyache"; 
                	  }
                	  else
                	  {
                		  text = "Bodyache"; 
                	  }
                  }
                  if(fever.isChecked())
                  {
                	  if(text != null && text != "" && text != "null")
                	  {
                		  text = text + "," + " Fever"; 
                	  }
                	  else
                	  {
                		  text = "Fever"; 
                	  }
                  }
                  if(vomiting.isChecked())
                  {
                	  if(text != null && text != "" && text != "null")
                	  {
                		  text = text + "," + " Vomiting"; 
                	  }
                	  else
                	  {
                		  text = "Vomiting"; 
                	  }
                  }
                  if(diarrhea.isChecked())
                  {
                	  if(text != null && text != "" && text != "null")
                	  {
                		  text = text + "," + " Diarrhoea"; 
                	  }
                	  else
                	  {
                		  text = "Diarrhoea"; 
                	  }
                  }
                  if(headache.isChecked())
                  {
                	  if(text != null && text != "" && text != "null")
                	  {
                		  text = text + "," + " Headache"; 
                	  }
                	  else
                	  {
                		  text = "Headache"; 
                	  }
                  }
                  
                  String sym = symptoms.getText().toString();
                  if(others.isChecked())
                  {
                	  if(text != null && text != "" && text != "null")
                	  {
                		  text = text + "," + " Others : "; 
                	  }
                	  else
                	  {
                		  text = "Others : "; 
                	  }
                	  
                	  
                      if(sym.length() != 0){
                    	  
                    	  text = text + sym;
                      }
                  }
                  


                  


                  if(text == null && sym.length() == 0)
                  {
                	  //Please select or enter Symptoms
                	  sAlert();
                  }
				else if(txtDate.getText().toString().matches(""))
				{
					displayAlert("Please select date");
				}

				else if(txtTime.getText().toString().matches(""))
				{
					displayAlert("Please select time");
				}

                  else
                  {

                      	Intent launchmain= new Intent(getApplicationContext(),mapok.class);
					  launchmain.putExtra("clinicid", getClinicID().toString());
                    	launchmain.putExtra("Sym", text);
                    	launchmain.putExtra("desc", symptoms.getText().toString());
                    	launchmain.putExtra("datetime", txtDate.getText().toString() + "," + txtTime.getText().toString());
                    	launchmain.putExtra("clinicname",clickedname);
                        startActivity(launchmain);
                      

                  }
            }
         });
        
        
     

        
        
       others.setOnClickListener(new OnClickListener() {
     
    	  @Override
    	  public void onClick(View v) {
                    //is chkIos checked?
    		if (((CheckBox) v).isChecked()) {
    			symptoms.setVisibility(View.VISIBLE);
    		}
    		else
    		{
    			symptoms.setVisibility(View.GONE);
    		
    		}
     
    	  }
    	});
        
ImageButton back = (ImageButton) findViewById(R.id.imageButton1);
        
        back.setOnClickListener(new OnClickListener() {
        	 @Override
        	    public void onClick(View v) {
        	        finish();
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
    
    void sAlert()
    {
    	try{
    	showAlert();
    	}catch(Exception e){
			System.out.println("Exception : " + e.getMessage());
		}
    }
    void sAlert2()
    {
    	
    	showAlert2();
    }


	public String readMaps2() {
			sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
			StringBuilder builder = new StringBuilder();
			HttpClient client = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://voidq.xyz/appconfig/backuptimeslot.php"); // make sure the url is correct.

			try {

				nameValuePairs = new ArrayList<NameValuePair>(2);
				// Getting latitude of the current location

				// Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
				nameValuePairs.add(new BasicNameValuePair("clinicid", getClinicID().toString()));
				nameValuePairs.add(new BasicNameValuePair("date", txtDate.getText().toString()));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = client.execute(httppost);
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


	private void updateLabel() {
		String myFormat = "dd/MM/yy"; //In which you need put here
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

		txtDate.setText(sdf.format(c.getTime()));
	}
	public String getClinicID(){
		String clinicid = "";
		try{

			httpclient=new DefaultHttpClient();
			httppost= new HttpPost("http://voidq.xyz/appconfig/getclinic.php"); // make sure the url is correct.
			//add your data
			nameValuePairs = new ArrayList<NameValuePair>(1);
			// Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
			nameValuePairs.add(new BasicNameValuePair("clinic",clickedname.toString().trim()));  // $Edittext_value = $_POST['Edittext_value'];
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			//response=httpclient.execute(httppost);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			final String response = httpclient.execute(httppost, responseHandler);
			System.out.println("Response : " + response);

			clinicid = response;

		}catch(Exception e){
			dialog.dismiss();
			System.out.println("Exception : " + e.getMessage());
		}
		return clinicid;

	}


	public String checkiftaken(String clinicidz,String timez,String datez){
	String reply = "";
		try{

			httpclient=new DefaultHttpClient();
			httppost= new HttpPost("http://voidq.xyz/appconfig/timing.php"); // make sure the url is correct.
			//add your data
			nameValuePairs = new ArrayList<NameValuePair>(3);
			// Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
			nameValuePairs.add(new BasicNameValuePair("timing",timez.toString().trim()));
			nameValuePairs.add(new BasicNameValuePair("clinic",clinicidz.toString().trim()));
			nameValuePairs.add(new BasicNameValuePair("date",datez.toString().trim())); // $Edittext_value = $_POST['Edittext_value'];
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			//response=httpclient.execute(httppost);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			final String response = httpclient.execute(httppost, responseHandler);
			System.out.println("Response : " + response);
			reply = response;




		}catch(Exception e){
			dialog.dismiss();
			System.out.println("Exception : " + e.getMessage());
		}
	return reply.trim();

	}

	public String getoperatinghours(String clinicidz){
			String reply="";
		try{

			httpclient=new DefaultHttpClient();
			httppost= new HttpPost("http://voidq.xyz/appconfig/oph.php"); // make sure the url is correct.
			//add your data
			nameValuePairs = new ArrayList<NameValuePair>(1);
			// Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
			nameValuePairs.add(new BasicNameValuePair("clinicid",clinicidz.toString().trim()));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			//response=httpclient.execute(httppost);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			final String response = httpclient.execute(httppost, responseHandler);
			System.out.println("Response : " + response);
			reply = response;




		}catch(Exception e){
			dialog.dismiss();
			System.out.println("Exception : " + e.getMessage());
		}

return reply.trim().toString();
	}

	public void showAlert4(){
		this.runOnUiThread(new Runnable() {
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(Book.this);
				builder.setTitle("Error.");
				builder.setMessage("This time slot has been taken! Please reselect")
						.setCancelable(false)
						.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								TextView lbtime = (TextView)findViewById(R.id.lbtime);
								lbtime.setPressed(true);
								lbtime.invalidate();
								lbtime.performClick();
							}
						});
				AlertDialog alert = builder.create();
				alert.show();
			}
		});
	}

	public void showAlert10(){
		this.runOnUiThread(new Runnable() {
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(Book.this);
				builder.setTitle("Error.");
				builder.setMessage("Already past time")
						.setCancelable(false)
						.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								TextView lbtime = (TextView)findViewById(R.id.lbtime);
								lbtime.setPressed(true);
								lbtime.invalidate();
								lbtime.performClick();
							}
						});
				AlertDialog alert = builder.create();
				alert.show();
			}
		});
	}

	public void showAlert(){
		this.runOnUiThread(new Runnable() {
		    public void run() {
		    	AlertDialog.Builder builder = new AlertDialog.Builder(Book.this);
		    	builder.setTitle("Error.");
		    	builder.setMessage("Please select symptoms or enter your symtoms!")  
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
	public void displayAlert(final String msgz){
		this.runOnUiThread(new Runnable() {
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(Book.this);
				builder.setTitle("Error.");
				builder.setMessage(msgz)
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
		this.runOnUiThread(new Runnable() {
		    public void run() {
		    	AlertDialog.Builder builder = new AlertDialog.Builder(Book.this);
		    	builder.setTitle("Error.");
		    	builder.setMessage("Please select number of days MC.")  
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
	public void showAlert100(){
		this.runOnUiThread(new Runnable() {
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(Book.this);
				builder.setTitle("Error");
				builder.setMessage("Please Select Date")
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