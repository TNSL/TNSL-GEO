package com.example.googlemapset;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends FragmentActivity implements LocationListener {
    private GoogleMap mmap;
    private LocationManager locationManager;										// 위치 서비스하는 객체
    private String provider;																// 프로바이더
    boolean locationTag=true;
    private int mPresentRad;
    private LatLng loc;
    private int mZoomLevel;
    SeekBar seek;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);									
        setContentView(R.layout.activity_main);								
        mPresentRad = 10;
        mZoomLevel = 15;
      	GooglePlayServicesUtil.isGooglePlayServicesAvailable(MainActivity.this);
       	locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, true);
        
        if(provider==null){  //위치정보 설정이 안되어 있으면 설정하는 엑티비티로 이동합니다
         	new AlertDialog.Builder(MainActivity.this)
	        .setTitle("위치서비스 동의")
	        .setNeutralButton("이동" ,new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
				}
			}).setOnCancelListener(new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					finish();
				}
			})
	        .show();
        }else{   //프로바이더 설정이 되어 있으면 현재위치를 받아옵니다
    		locationManager.requestLocationUpdates(provider, 0, 0, MainActivity.this);
        	setUpMapIfNeeded();
        }
        
        seek=(SeekBar)findViewById(R.id.seekbar);
        
        seek.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
            //drag change
			@Override
			public void onProgressChanged(SeekBar seekBar, int process,
					boolean fromUser) {
					mPresentRad  = process * 50;			// 기본값은 10 process로 최대 1000까지
					
					DrawCircle();
			}
                    // drag start
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			
			}
                      // Drag stop
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}

     });
        
        
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {						//위치설정 엑티비티 종료 후 
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 0:
			 locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		        Criteria criteria = new Criteria();
		        provider = locationManager.getBestProvider(criteria, true);
		        if(provider==null){//사용자가 위치설정동의 안했을때 종료 
					finish();
			}else{//사용자가 위치설정 동의 했을때 
				locationManager.requestLocationUpdates(provider, 1L, 2F, MainActivity.this);
		        	setUpMapIfNeeded();
			}
			break;
		}
	}
    
    @Override
	public void onBackPressed() {
		this.finish();
	}

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }
    
    private void setUpMapIfNeeded() {
		if (mmap == null) {
				mmap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		if (mmap != null) {
			setUpMap();
		}
		}
	}
    
    private void setUpMap() {
    	mmap.setMyLocationEnabled(true);
    	mmap.getMyLocation();
		
	}
 
    
    @Override
    public void onLocationChanged(Location location) {
    	if(locationTag){//한번만 위치를 가져오기 위해서 tag를 주었습니다
    	Log.d("myLog"  , "onLocationChanged: !!"  + "onLocationChanged!!");
	        double lat =  location.getLatitude();
	        double lng = location.getLongitude();
	       loc = new LatLng(lat, lng);
	       DrawCircle();
	
	        Toast.makeText(MainActivity.this, "위도  : " + lat +  " 경도: "  + lng ,  Toast.LENGTH_SHORT).show();
	       locationTag=false;
    	}

    }
    public void DrawCircle()
    {
    	mmap.clear();
    	CameraPosition cp = new CameraPosition.Builder().target((loc)).zoom(mZoomLevel).build();
    	  mmap.animateCamera(CameraUpdateFactory.newCameraPosition(cp)); // 지정위치로 이동
	        CircleOptions circleOptions = new CircleOptions()
          .center(loc)   //set center
          .radius(mPresentRad)   //set radius in meters
          .fillColor(Color.TRANSPARENT)  //default
          .strokeColor(0x55ff0000)
          .strokeWidth(4);
	        
          mmap.addCircle(circleOptions);
    	
    }
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
}
