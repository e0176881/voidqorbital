package com.example.mingxuan.voidq;



import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class HealthBookSignup extends Fragment {
	
    View view;
	  @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        // Inflate the layout for this fragment
		  
		  
		  view = inflater.inflate(R.layout.healthbooksignup, container, false);
		   Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"fonts/verdana.ttf"); 
	        /* TextView Pagename = (TextView)view.findViewById(R.id.pagename) */
	        TextView lb1 = (TextView)view.findViewById(R.id.lbusername);
	        lb1.setTypeface(type);
	        TextView lb2 = (TextView)view.findViewById(R.id.regg);
	        lb2.setTypeface(type);
	        
	       
	        //Pagename.setTypeface(type);
	        TextView loginScreen = (TextView) view.findViewById(R.id.link_to_login);
	        loginScreen.setTypeface(type);
	     
	        loginScreen.setOnClickListener(new OnClickListener() {
	        
	            public void onClick(View arg0) {
	                                // Closing registration screen
	                // Switching to Login Screen/closing register screen
	            	startActivity(new Intent(getActivity(), LoginActivity.class));
	               getActivity().finish();
	            }
	        });

	        TextView signup = (TextView) view.findViewById(R.id.btnBook);
	        signup.setTypeface(type);
	     
	        signup.setOnClickListener(new OnClickListener() {
	        
	            public void onClick(View arg0) {
	                                // Closing registration screen
	                // Switching to Login Screen/closing register screen
	            	startActivity(new Intent(getActivity(), RegisterActivity.class));
	               getActivity().finish();
	            }
	        });
	       
	    
		  
		  
	        return view;
	    
}

	  
	
}
