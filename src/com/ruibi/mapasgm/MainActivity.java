package com.ruibi.mapasgm;

/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import java.io.IOException;
import java.util.List;
import java.util.Locale;





import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * This shows how to create a simple activity with a map and a marker on the map.
 * <p>
 * Notice how we deal with the possibility that the Google Play services APK is not
 * installed/enabled/updated on a user's device.
 */
public class MainActivity extends FragmentActivity {
	 private GoogleMap mMap;
	    MarkerOptions markerOptions;
	    LatLng latLng;
	    Button traffic;
	    Button traffic_off;
	    Button findb;
	    
	    private LatLng DEFAULT_POSITION = new LatLng(40.447419, -79.952652);
	    
	    static final CameraPosition iSchool =
	            new CameraPosition.Builder().target(new LatLng(40.447419, -79.952652))
	                    .zoom(20.0f)
	                    .bearing(300)
	                    .tilt(25)
	                    .build();
		
	    
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_main);
	        setUpMapIfNeeded();
	        traffic = (Button) findViewById(R.id.button1);
	        traffic.setOnClickListener(trafficListener);
	        traffic_off = (Button) findViewById(R.id.button2);
	        traffic_off.setOnClickListener(trafficoffListener);
	        mMap.getUiSettings().setZoomControlsEnabled(true);
	        mMap.getUiSettings().setMyLocationButtonEnabled(true);
	        mMap.setMyLocationEnabled(true);
	        mMap.animateCamera( CameraUpdateFactory.zoomTo( 20.0f ) ); 
	        mMap.animateCamera( CameraUpdateFactory.newLatLng(new LatLng(40.44900957083873, -79.95699882507323)));
	        findb = (Button) findViewById(R.id.bfind);
	        findb.setOnClickListener(findClickListener);
	    }

	    @Override
	    protected void onResume() {
	        super.onResume();
	        setUpMapIfNeeded();
	    }

	    private OnClickListener trafficoffListener = new OnClickListener(){
	    	public void onClick(View view){
	    		mMap.setTrafficEnabled(false);
	    	}
	    };
	    
	    private OnClickListener trafficListener = new OnClickListener(){
	    	public void onClick(View view){
	    		mMap.setTrafficEnabled(true);
	    		
	    	}
	    };
	    
	   
	    private void setUpMapIfNeeded() {
	        // Do a null check to confirm that we have not already instantiated the map.
	        if (mMap == null) {
	            // Try to obtain the map from the SupportMapFragment.
	            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
	                    .getMap();
	            // Check if we were successful in obtaining the map.
	            if (mMap != null) {
	                setUpMap();
	            }
	        }
	    }

	  
	    private void setUpMap() {
	        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
	    }
	    
	    private boolean checkReady() {
	        if (mMap == null) {
	            CharSequence map_not_ready="not_ready";
				Toast.makeText(this, map_not_ready, Toast.LENGTH_SHORT).show();
	            return false;
	        }
	        return true;
	    }
	    
	    /*go iSchool*/
	    public void goIschool(View view) {
	        if (!checkReady()) {
	            return;
	        }

	        changeCamera(CameraUpdateFactory.newCameraPosition(iSchool), new CancelableCallback() {
	            @Override
	            public void onFinish() {
	                Toast.makeText(getBaseContext(), "Animation to iSchool complete", Toast.LENGTH_SHORT)
	                        .show();
	            }

	            @Override
	            public void onCancel() {
	                Toast.makeText(getBaseContext(), "Animation to iSchool canceled", Toast.LENGTH_SHORT)
	                        .show();
	            }
	        });
	    }
	    private void changeCamera(CameraUpdate update, CancelableCallback callback) {
	         mMap.animateCamera(update, Math.max(12, 1), callback);
	          
	    }
	
	    
	   /* search position*/
	    
	    OnClickListener findClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Getting reference to EditText to get the user input location
                EditText etLocation = (EditText) findViewById(R.id.tb1);
 
                // Getting user input location
                String location = etLocation.getText().toString();
 
                if(location!=null && !location.equals("")){
                    new GeocoderTask().execute(location);
                }
            }
        };
	      private class GeocoderTask extends AsyncTask<String, Void, List<Address>>{
	    	  
	          @Override
	          protected List<Address> doInBackground(String... locationName) {
	             
	              Geocoder geocoder = new Geocoder(getBaseContext());
	              List<Address> addresses = null;
	   
	              try {
	                  addresses = geocoder.getFromLocationName(locationName[0], 3);
	              } catch (IOException e) {
	                  e.printStackTrace();
	              }
	              return addresses;
	          }
	   
	          @Override
	          protected void onPostExecute(List<Address> addresses) {
	   
	              if(addresses==null || addresses.size()==0){
	                  Toast.makeText(getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
	              }
	              mMap.clear();
	   
	              // Adding Markers on Google Map for each matching address
	              for(int i=0;i<addresses.size();i++){
	   
	                  Address address = (Address) addresses.get(i);
	   
	                  // Creating an instance of GeoPoint, to display in Google Map
	                 latLng = new LatLng(address.getLatitude(), address.getLongitude());
	   
	                  String addressText = String.format("%s, %s",
	                  address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
	                  address.getCountryName());
	   
	                  markerOptions = new MarkerOptions();
	                  markerOptions.position(latLng);
	                  markerOptions.title(addressText);
	   
	                  mMap.addMarker(markerOptions);
	   
	                  // Locate the first location
	                  if(i==0)
	                      mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
	              }
	          }
	      }	 
}
