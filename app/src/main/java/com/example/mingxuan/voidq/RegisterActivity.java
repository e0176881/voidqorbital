package com.example.mingxuan.voidq;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
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

import java.util.ArrayList;
import java.util.List;
public class RegisterActivity extends Activity {
	static final int DATE_DIALOG_ID = 999;
	Button b;
	EditText et,pass,email,cfmpw,handphone;
	TextView tv;
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
        this.setContentView(R.layout.register); 
        super.onCreate(savedInstanceState);
        // Set View to register.xml
        setContentView(R.layout.register);
        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/verdana.ttf"); 
        TextView Pagename = (TextView)findViewById(R.id.pagename);
        Pagename.setTypeface(type);
        TextView lbu = (TextView)findViewById(R.id.lbusername);
        TextView lbp = (TextView)findViewById(R.id.lbpassword);
        
        //TextView lbe = (TextView)findViewById(R.id.lbemail);
        lbu.setTypeface(type);
        lbp.setTypeface(type);


        

        b = (Button)findViewById(R.id.btnRegister); 
        b.setTypeface(type);
        pass= (EditText)findViewById(R.id.reg_password);
        cfmpw=(EditText)findViewById(R.id.reg_cfmpassword);
        tv = (TextView)findViewById(R.id.TextViewaaa);
        email = (EditText)findViewById(R.id.reg_email);
        handphone=(EditText) findViewById(R.id.reg_phone);
   
        b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String upw = pass.getText().toString();
				String cpww =cfmpw.getText().toString();
				String uemail = email.getText().toString();
				String hp  = handphone.getText().toString();



				if(uemail.length() == 0)
				{
					showAlert4();
				}

				else if(!isValidEmail(uemail))
				{
					showAlert2();
				}

				else if(upw.length() == 0)
				{
					
					showAlert3();
				}
				else if(cpww.length() == 0)
				{
				showAlert6();
				}
				else if(!cpww.equals(upw))
				{
					showAlert7();
				}

				else if(hp.length() == 0 )
				{
					showAlert8();
				}

				else {
				dialog = ProgressDialog.show(RegisterActivity.this, "", 
                        "Registering...", true);
				 new Thread(new Runnable() {
					    public void run() {
					    	Register();
					    }
					  }).start();				
					}
			}
		});
        //Button frgtpw = (Button) findViewById(R.id.link_to_register2);
        //frgtpw.setTypeface(type);
        TextView loginScreen = (TextView) findViewById(R.id.link_to_login);
        loginScreen.setTypeface(type);
     // Listening to Login Screen link
        /*frgtpw.setOnClickListener(new View.OnClickListener() {
        
            public void onClick(View arg0) {
                                // Closing registration screen
                // Switching to Login Screen/closing register screen
            	Intent i = new Intent(getApplicationContext(), ForgetPassword.class);
				startActivity(i);
            }
        });
        */
        // Listening to Login Screen link
        loginScreen.setOnClickListener(new OnClickListener() {
        
            public void onClick(View arg0) {
                                // Closing registration screen
                // Switching to Login Screen/closing register screen
                finish();
            }
        });
        ImageButton bck = (ImageButton) findViewById(R.id.imageButton1);
        bck.setOnClickListener(new OnClickListener() {
       			
       			public void onClick(View v) {
       				// Switching to Register screen
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

	public final static boolean isValidEmail(CharSequence target) {
		return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
	}
  	public void showAlert(){
  		RegisterActivity.this.runOnUiThread(new Runnable() {
  		    public void run() {
  		    	AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
  		    	builder.setTitle("Error");
  		    	builder.setMessage("Username,Email or Handphone is already used")
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
  		RegisterActivity.this.runOnUiThread(new Runnable() {
  		    public void run() {
  		    	AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
  		    	builder.setTitle("Error");
  		    	builder.setMessage("Email address not valid!")
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
  		RegisterActivity.this.runOnUiThread(new Runnable() {
  		    public void run() {
  		    	AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
  		    	builder.setTitle("Error");
  		    	builder.setMessage("Please enter Password!")  
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
  	public void showAlert4(){
  		RegisterActivity.this.runOnUiThread(new Runnable() {
  		    public void run() {
  		    	AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
  		    	builder.setTitle("Error");
  		    	builder.setMessage("Please enter your Email Address!")  
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


	public void showAlert6(){
		RegisterActivity.this.runOnUiThread(new Runnable() {
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
				builder.setTitle("Error");
				builder.setMessage("Please enter Confirm Password")
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
	public void showAlert7(){
		RegisterActivity.this.runOnUiThread(new Runnable() {
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
				builder.setTitle("Error");
				builder.setMessage("Password not same, pls reconfirm")
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

    public void showAlert5(){
        RegisterActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle("Success");
                builder.setMessage("Registration Success")
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

	public void showAlert8(){
		RegisterActivity.this.runOnUiThread(new Runnable() {
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
				builder.setTitle("Error");
				builder.setMessage("Please enter your handphone number")
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

	void Register(){
		try{

			httpclient=new DefaultHttpClient();
			httppost= new HttpPost("http://voidq.xyz/appconfig/register.php"); // make sure the url is correct.
			nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("username",email.getText().toString().trim()));  // $Edittext_value = $_POST['Edittext_value'];
			nameValuePairs.add(new BasicNameValuePair("password",pass.getText().toString().trim()));
			nameValuePairs.add(new BasicNameValuePair("Email",email.getText().toString().trim()));
			nameValuePairs.add(new BasicNameValuePair("handphone",handphone.getText().toString().trim()));

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			final String response = httpclient.execute(httppost, responseHandler);
			System.out.println("Response : " + response);
			runOnUiThread(new Runnable() {
				public void run() {
					// 	tv.setText("Response from PHP : " + response);
					dialog.dismiss();
				}
			});


			if(response.contains("success")){
				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(RegisterActivity.this,"Registration Success", Toast.LENGTH_SHORT).show();

					}
				});

				//SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPrefs", MODE_APPEND); // 0 - for private mode
				//Editor editor = pref.edit();
				//editor.putString("username", et.getText().toString().trim()); // Storing string
				//editor.commit();
				showAlert5();
				startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

			}else{
				showAlert();
			}

		}catch(Exception e){
			dialog.dismiss();
			System.out.println("Exception : " + e.getMessage());
		}
	}
}