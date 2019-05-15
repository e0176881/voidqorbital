package com.example.mingxuan.voidq;


import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.mingxuan.voidq.adapter.TitleNavigationAdapter;
import com.example.mingxuan.voidq.model.SpinnerNavItem;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.maps.GoogleMap;

import org.apache.http.NameValuePair;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
public class BookADocHome extends FragmentActivity{
	SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String pUserName = "username"; 
    public static final String pPassword = "password";
	List<NameValuePair> nameValuePairs;
	private GoogleSignInClient mGoogleSignInClient;
	// action bar
	private ActionBar actionBar;
	 ListView l;
	// Title navigation Spinner data
	private ArrayList<SpinnerNavItem> navSpinner;

	// Navigation adapter
	private TitleNavigationAdapter adapter;
	List<Fragment> fragList = new ArrayList<Fragment>();
	// Refresh menu item
	private MenuItem refreshMenuItem;
	GoogleMap map ;
	String oaddress, odays, ohoursmorning, ohoursafternoon, ohoursevening, ocontactno, ofaxnumber;
	String symb,desc,mc,mapname,clickedname;
	public static Context appContext;
	Double lat,lng;
	ArrayList <String> list = new ArrayList <String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 StrictMode.ThreadPolicy policy = new StrictMode.
	        	    ThreadPolicy.Builder().permitAll().build();
	        	    StrictMode.setThreadPolicy(policy); 
		setContentView(R.layout.fragment);
		try {
	        ViewConfiguration config = ViewConfiguration.get(this);
	        Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
	        if(menuKeyField != null) {
	            menuKeyField.setAccessible(true);
	            menuKeyField.setBoolean(config, false);
	        }
	    } catch (Exception ex) {
	        // Ignore
	    }
		appContext = getApplicationContext();
		//ActionBar
        ActionBar actionbar = getActionBar();
        
		actionbar.setTitle(Html.fromHtml("<font color='#FFFFFF'>voidQ </font>"));
		
		// Action Bar background
		actionbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1248ea")));
        
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        Tab PlayerTab = actionbar.newTab().setText(Html.fromHtml("<font color='#FFFFFF'>&nbsp;Book a Doctor</font>"))/*.setIcon(R.drawable.bookadr2)*/;
        Tab StationsTab = actionbar.newTab().setText(Html.fromHtml("<font color='#FFFFFF'>&nbsp;My Healthbook</font>"))/*.setIcon(R.drawable.myhealth)*/;
		Tab StatementTab = actionbar.newTab().setText(Html.fromHtml("<font color='#FFFFFF'>&nbsp;Appointments</font>"))/*.setIcon(R.drawable.myhealth)*/;


		actionbar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));
		


        Fragment PlayerFragment = new BookDoc();
        Fragment StationsFragment = new HealthBook();
			Fragment StatementFragment = new idk();

        PlayerTab.setTabListener(new MyTabsListener(PlayerFragment));
        
        
        StationsTab.setTabListener(new MyTabsListener(StationsFragment));

        StatementTab.setTabListener(new MyTabsListener(StatementFragment));



        actionbar.addTab(PlayerTab);
        actionbar.addTab(StationsTab);
		actionbar.addTab(StatementTab);
        actionbar.setIcon(R.drawable.ic_launcher);

		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestEmail()
				.build();
		mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
	}

	
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        MenuInflater inflater = getMenuInflater();
	        inflater.inflate(R.menu.activity_main_actions, menu);
	        sharedpreferences=getSharedPreferences("MyPrefs", 
	        		MODE_APPEND);
	        		if (sharedpreferences.getString("username", null) != null)
	        		 {
	        		 
	        		menu.findItem(R.id.menuitem_logout).setVisible(true);
	        		 
	        		menu.findItem(R.id.menuitem_login).setVisible(false);
	        		 
	        		menu.findItem(R.id.menuitem_signup).setVisible(false);
	        		 }
	        		 else
	        		 {
	        		 
	        		menu.findItem(R.id.menuitem_login).setVisible(false);
	        		 
	        		menu.findItem(R.id.menuitem_logout).setVisible(true);
	        		 
	        		menu.findItem(R.id.menuitem_signup).setVisible(true);
	        		 }
	        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
			SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
					.getActionView();
			searchView.setSearchableInfo(searchManager
					.getSearchableInfo(getComponentName()));

			return super.onCreateOptionsMenu(menu);
	    }

	    
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch(item.getItemId()) {
				case R.id.menuitem_search:
					Toast.makeText(appContext, "search", Toast.LENGTH_SHORT).show();
					return true;
				case R.id.menuitem_login:
					Toast.makeText(appContext, "login", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(getBaseContext(), LoginActivity.class);
					startActivity(intent);
					return true;
				case R.id.menuitem_logout:
					Toast.makeText(appContext, "logout", Toast.LENGTH_SHORT).show();
					// Switching to Register screen
					mGoogleSignInClient.signOut();

					SharedPreferences settings = getSharedPreferences("MyPrefs", 0);
					//settings.edit().clear().commit();
					settings.edit().remove("username").commit();
					Intent i = new Intent(getApplicationContext(), voidq.class);
					startActivity(i);
					finish();
					return true;
				case R.id.menuitem_about:
					Toast.makeText(appContext, "about", Toast.LENGTH_SHORT).show();
					Intent i2 = new Intent(getApplicationContext(), MainOperation.class);
					startActivity(i2);
					finish();
					return true;
				case R.id.menuitem_signup:
					Toast.makeText(appContext, "signup", Toast.LENGTH_SHORT).show();
					Intent intent2 = new Intent(getBaseContext(), RegisterActivity.class);
					startActivity(intent2);
					return true;
			}
			return false;
		}
		
	  
	    
	}

class MyTabsListener implements ActionBar.TabListener {
	public Fragment fragment;
	
	public MyTabsListener(Fragment fragment) {
		this.fragment = fragment;
	}
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		Toast.makeText(BookADocHome.appContext, "Reselected!", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		ft.replace(R.id.fragment_container, fragment);
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		ft.remove(fragment);
	}
	
}