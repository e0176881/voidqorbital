package com.example.mingxuan.voidq;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.CardRequirements;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentMethodTokenizationParameters;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.TransactionInfo;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;



public class MyBooking extends Activity {
	SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String pUserName = "username"; 
    public static final String pPassword = "password";
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
    Double clat, clng, clong;
    String symb,desc,mc,date,time,ref,clinicid;
    TextView symptoms, description, mcla, pricing,mapnamez,address,cliniccost,medicinecost,total,refnoz;
    PaymentsClient paymentsClient;
    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 991;
    private Card card;
    String specialrefno;
    int finaltotal;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	 //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

       //set content view AFTER ABOVE sequence (to avoid crash)
        //this.setContentView(R.layout.payment); 
        super.onCreate(savedInstanceState);
        // Set View to register.xml
        setContentView(R.layout.mybooking);


        Intent intent = getIntent();


        // 4. get bundle from intent
        Bundle bundle = intent.getExtras();

        // 5. get status value from bundle
        date = bundle.getString("date");
        time = bundle.getString("time");
        clinicid=bundle.getString("clinicid");
        Button btnpay = (Button)findViewById(R.id.btnpay);
        btnpay.setVisibility(View.INVISIBLE);
        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/verdana.ttf");
        TextView a = (TextView)findViewById(R.id.lblmessage);
        TextView b = (TextView)findViewById(R.id.lblbookclinic);
        TextView c = (TextView)findViewById(R.id.lblsyndrome);
        TextView d = (TextView)findViewById(R.id.asd);
        TextView e = (TextView)findViewById(R.id.lblmc);
        TextView g = (TextView)findViewById(R.id.pagename);
        TextView viewmap = (TextView)findViewById(R.id.tvviewmap);
        TextView asdd = (TextView)findViewById(R.id.asdd);
        asdd.setTypeface(type);
        a.setTypeface(type);
        b.setTypeface(type);
        c.setTypeface(type);
        d.setTypeface(type);
        e.setTypeface(type);
        g.setTypeface(type);
        viewmap.setTypeface(type);

        
        Button btnOK = (Button) findViewById(R.id.btnok);
        btnOK.setTypeface(type);
        btnOK.setOnClickListener(new View.OnClickListener() {
       	 @Override
 	    public void onClick(View v) {
 	        finish();
 	    }
  });
        
        viewmap.setOnClickListener(new View.OnClickListener() {
          	 @Override
    	    public void onClick(View v) {
    	        // View Map
          		 Intent intent = new Intent(getBaseContext(), MyBookingViewMap.class);
          		 Bundle extras = new Bundle();
			        extras.putString("clinicname",clinicid);

			        
			        // 4. add bundle to intent
			        intent.putExtras(extras);
			 
			        // 5. start the activity
			        startActivity(intent);
    	    }
     });
        
ImageButton back = (ImageButton) findViewById(R.id.imageButton1);
        
        back.setOnClickListener(new View.OnClickListener() {
        	 @Override
        	    public void onClick(View v) {
        	        finish();
        	    }
         });


             
          	
          	symptoms = (TextView)findViewById(R.id.lblsyndromes);
          	address = (TextView)findViewById(R.id.tvaddress);
          	address.setTypeface(type);
          	
          	symptoms.setTypeface(type);
          	mcla=(TextView)findViewById(R.id.lblmcz);
          
          	mcla.setTypeface(type);
          	mapnamez=(TextView)findViewById(R.id.lblbookclinic);
          	
          	mapnamez.setTypeface(type);
          	cliniccost=(TextView)findViewById(R.id.lblprice);
          	cliniccost.setTypeface(type);
          	medicinecost=(TextView)findViewById(R.id.lblpricez);
          	medicinecost.setTypeface(type);
        total=(TextView)findViewById(R.id.lbltotal);
        total.setTypeface(type);
        refnoz=(TextView) findViewById(R.id.lblrefnoz);
        refnoz.setTypeface(type);

          	getmybooking();



        card = new Card(
                "4242424242424242", //card number
                12, //expMonth
                2019,//expYear
                "123"//cvc
        );

         paymentsClient =
                Wallet.getPaymentsClient(this,
                        new Wallet.WalletOptions.Builder().setEnvironment(WalletConstants.ENVIRONMENT_TEST)
                                .build());

        isReadyToPay();

        findViewById(R.id.btnpay).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PaymentDataRequest request = createPaymentDataRequest();
                        if (request != null) {
                            AutoResolveHelper.resolveTask(
                                    paymentsClient.loadPaymentData(request),
                                    MyBooking.this,
                                    LOAD_PAYMENT_DATA_REQUEST_CODE);
                            // LOAD_PAYMENT_DATA_REQUEST_CODE is a constant integer of your choice,
                            // similar to what you would use in startActivityForResult
                        }
                    }
                });

            }


    private PaymentDataRequest createPaymentDataRequest() {
        PaymentDataRequest.Builder request =
                PaymentDataRequest.newBuilder()
                        .setTransactionInfo(
                                TransactionInfo.newBuilder()
                                        .setTotalPriceStatus(WalletConstants.TOTAL_PRICE_STATUS_FINAL)
                                        .setTotalPrice("1.00")
                                        .setCurrencyCode("USD")
                                        .build())
                        .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_CARD)
                        .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_TOKENIZED_CARD)
                        .setCardRequirements(
                                CardRequirements.newBuilder()
                                        .addAllowedCardNetworks(Arrays.asList(
                                                WalletConstants.CARD_NETWORK_AMEX,
                                                WalletConstants.CARD_NETWORK_DISCOVER,
                                                WalletConstants.CARD_NETWORK_VISA,
                                                WalletConstants.CARD_NETWORK_MASTERCARD))
                                        .build());

        request.setPaymentMethodTokenizationParameters(createTokenizationParameters());
        return request.build();
    }

    private PaymentMethodTokenizationParameters createTokenizationParameters() {
        return PaymentMethodTokenizationParameters.newBuilder()
                .setPaymentMethodTokenizationType(WalletConstants.PAYMENT_METHOD_TOKENIZATION_TYPE_PAYMENT_GATEWAY)
                .addParameter("gateway", "stripe")
                .addParameter("stripe:publishableKey", "pk_test_4riDuIcwQ4yaMA2Wcjw8g3Za")
                .addParameter("stripe:version", "5.1.0")
                .build();
    }

    private void isReadyToPay() {
        IsReadyToPayRequest request = IsReadyToPayRequest.newBuilder()
                .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_CARD)
                .addAllowedPaymentMethod(WalletConstants.PAYMENT_METHOD_TOKENIZED_CARD)
                .build();
        Task<Boolean> task = paymentsClient.isReadyToPay(request);
        task.addOnCompleteListener(
                new OnCompleteListener<Boolean>() {
                    public void onComplete(Task<Boolean> task) {
                        try {
                            boolean result =
                                    task.getResult(ApiException.class);
                            if(result == true) {
                                //show Google as payment option
                            } else {
                                //hide Google as payment option
                            }
                        } catch (ApiException exception) { }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case LOAD_PAYMENT_DATA_REQUEST_CODE:
                switch (resultCode) {
                    case Activity.RESULT_OK:




                   /*     PaymentData paymentData = PaymentData.getFromIntent(data);
                        // You can get some data on the user's card, such as the brand and last 4 digits
                        CardInfo info = paymentData.getCardInfo();
                        // You can also pull the user address from the PaymentData object.
                        UserAddress address = paymentData.getShippingAddress();
                        // This is the raw JSON string version of your Stripe token.
                        String rawToken = paymentData.getPaymentMethodToken().getToken();

                        // Now that you have a Stripe token object, charge that by using the id
                        Token stripeToken = Token.fromString(rawToken); */

                        Stripe stripe = new Stripe(this.getApplicationContext(),"pk_test_4riDuIcwQ4yaMA2Wcjw8g3Za");
                        stripe.createToken(
                                card,
                                new TokenCallback() {
                                    public void onSuccess(Token token) {
                                        Chargelor(token.getId());
                                        Toast.makeText(MyBooking.this,
                                                "Payment made successfully!", Toast.LENGTH_LONG).show();
                                        updateStatus();
                                        //Token successfully created.
                                        //Create a charge or save token to the server and use it later
                                    }
                                    public void onError(Exception error) {
                                        // Show localized error message

                                    }
                                }
                        );

                     /*   if (stripeToken != null) {






                            // This chargeToken function is a call to your own server, which should then connect
                            // to Stripe's API to finish the charge.

                        } */
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(MyBooking.this,
                                "Canceled", Toast.LENGTH_LONG).show();
                        break;
                    case AutoResolveHelper.RESULT_ERROR:
                        Status status = AutoResolveHelper.getStatusFromIntent(data);
                        Toast.makeText(MyBooking.this,
                                "Got error " + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
                        // Log the status for debugging
                        // Generally there is no need to show an error to
                        // the user as the Google Payment API will do that
                        break;
                    default:
                        // Do nothing.
                }
                break; // Breaks the case LOAD_PAYMENT_DATA_REQUEST_CODE
            // Handle any other startActivityForResult calls you may have made.
            default:
                // Do nothing.
        }
    }
    /*public void postData(String description, String token,String amount) {
        // Create a new HttpClient and Post Header
        try {
            URL url = new URL("[YOUR_SERVER_CHARGE_SCRIPT_URL]");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new NameValuePair("method", "charge"));
            params.add(new NameValuePair("description", description));
            params.add(new NameValuePair("source", token));
            params.add(new NameValuePair("amount", amount));

            OutputStream os = null;

            os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getQuery(params));
            writer.flush();
            writer.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    } */
    void enablePayment()
    {
        Button btnpayment = (Button)findViewById(R.id.btnpay);
        btnpayment.setVisibility(View.VISIBLE);
    }


    void Chargelor(String token){

        try{

            httpclient=new DefaultHttpClient();

            httppost= new HttpPost("http://voidq.xyz/appconfig/googlepay.php"); // make sure the url is correct.
            nameValuePairs = new ArrayList<NameValuePair>(5);
            int gg = finaltotal * 100;
            nameValuePairs.add(new BasicNameValuePair("amount",Integer.toString(gg)));
            nameValuePairs.add(new BasicNameValuePair("method","charge".trim()));
            nameValuePairs.add(new BasicNameValuePair("email","e0176881@u.nus.edu".trim()));
            nameValuePairs.add(new BasicNameValuePair("source",token));
            nameValuePairs.add(new BasicNameValuePair("description","Consultation".trim()));
            nameValuePairs.add(new BasicNameValuePair("currency","usd".trim()));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response = httpclient.execute(httppost, responseHandler);
            System.out.println("Response : " + response);


            if(response != null){










            }else{
                // error
            }

        }catch(Exception e){
            dialog.dismiss();
            System.out.println("Exception : " + e.getMessage());
        }

    }

    void updateStatus(){

        try{

            httpclient=new DefaultHttpClient();

            httppost= new HttpPost("http://voidq.xyz/appconfig/updatestatus.php"); // make sure the url is correct.
            nameValuePairs = new ArrayList<NameValuePair>(2);
            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);



            nameValuePairs.add(new BasicNameValuePair("username",sharedpreferences.getString(pUserName, "").trim()));
            nameValuePairs.add(new BasicNameValuePair("refno",specialrefno.trim()));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response = httpclient.execute(httppost, responseHandler);
            System.out.println("Response : " + response);


            if(response.trim().equals("success")){


                Button btnpayment = (Button)findViewById(R.id.btnpay);
                btnpayment.setVisibility(View.INVISIBLE);
             //success paymentt





            }else{
                // error
            }

        }catch(Exception e){
            dialog.dismiss();
            System.out.println("Exception : " + e.getMessage());
        }

    }


   /* void chargeToken(Token token)
    {
        Stripe. = "sk_test_H2zeDPgXY7g781uJO3h2bWGC";

        Map<String, Object> params = new HashMap<>();
        params.put("amount", 1);
        params.put("currency", "usd");
        params.put("source", token);
        params.put("receipt_email", "jenny.rosen@example.com");
        Charge charge = Charge.create(params);
    }*/
    void getmybooking(){

        try{

            httpclient=new DefaultHttpClient();
            httppost= new HttpPost("http://voidq.xyz/appconfig/seeindividualbooking.php"); // make sure the url is correct.
            nameValuePairs = new ArrayList<NameValuePair>(4);
            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


            nameValuePairs.add(new BasicNameValuePair("clinicid",clinicid.trim()));

            nameValuePairs.add(new BasicNameValuePair("username",sharedpreferences.getString(pUserName, "").trim()));
            nameValuePairs.add(new BasicNameValuePair("date",date.trim()));
            nameValuePairs.add(new BasicNameValuePair("time",time.trim()));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response = httpclient.execute(httppost, responseHandler);
            System.out.println("Response : " + response);


            if(response != null){


                            String[] values = response.trim().split(";");
                            //allergies.setText(response);
                            mapnamez.setText(values[2]); //clinic name
                            symptoms.setText(values[0]);

                            mcla.setText(date + "," + time);
                            specialrefno = values[1];
                            refnoz.setText(specialrefno);
                            cliniccost.setText("Clinic Cost: $"+ values[5]);
                            if(Integer.parseInt(values[6]) == -1  || Integer.parseInt(values[7]) == -1)
                            {
                                medicinecost.setText("Medicine Cost: TBC");
                                total.setText("Total: TBC");

                            }
                            else
                            {
                                medicinecost.setText("Medicine Cost: $" + values[6]);
                                int totalcost = Integer.parseInt(values[7]);
                                total.setText("Total: $" + totalcost);
                                    if(getStatus().trim().equals("got")) {
                                        enablePayment();
                                    }

                            }






            }else{
               // error
            }

        }catch(Exception e){
            dialog.dismiss();
            System.out.println("Exception : " + e.getMessage());
        }

    }

    public String getStatus(){
        String fulllname = "";
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        try{


            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost= new HttpPost("http://voidq.xyz/appconfig/getstatus.php"); // make sure the url is correct.
            //add your data
            nameValuePairs = new ArrayList<NameValuePair>(2);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("username",sharedpreferences.getString("username", null)));  // $Edittext_value = $_POST['Edittext_value'];
            nameValuePairs.add(new BasicNameValuePair("refno",specialrefno));
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

}
