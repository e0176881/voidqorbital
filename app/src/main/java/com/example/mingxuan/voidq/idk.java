package com.example.mingxuan.voidq;


import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class idk extends Fragment {


	SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String pUserName = "username";
    public static final String pPassword = "password";
	View view;





	 @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	 	view = (View) inflater.inflate(R.layout.mypastbooking, container, false);








		 StrictMode.ThreadPolicy policy = new StrictMode.
                 ThreadPolicy.Builder().permitAll().build();
                 StrictMode.setThreadPolicy(policy);




		 TextView viewcurrent = (TextView)view.findViewById(R.id.textviewappointmentz);
		 viewcurrent.setOnClickListener(new View.OnClickListener() {

			 public void onClick(View v) {
				 //Intent i = new Intent(getActivity().getApplicationContext(), ViewAllActivity.class);
				 //startActivity(i);

				 // 1. create an intent pass class name or intnet action name
				 //Intent intent = new Intent("com.skytech.ezdr.ViewAllActivity");
				 Intent intent = new Intent(getActivity().getBaseContext(), ViewAllBooking.class);


				 // 5. start the activity
				 startActivity(intent);
			 }

		 });


		 TextView viewall = (TextView) view.findViewById(R.id.textviewappointment);


		 viewall.setOnClickListener(new View.OnClickListener() {

			 public void onClick(View v) {
				 //Intent i = new Intent(getActivity().getApplicationContext(), ViewAllActivity.class);
				 //startActivity(i);

				 // 1. create an intent pass class name or intnet action name
				 //Intent intent = new Intent("com.skytech.ezdr.ViewAllActivity");
				 Intent intent = new Intent(getActivity().getBaseContext(), PastBookings.class);


				 // 5. start the activity
				 startActivity(intent);
			 }

		 });



		 return view;
	 }

















}
