package com.syrosoft.tnsl_geo;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.app.AlertDialog;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class HomeScreenActivity extends Activity {
	private ViewPager mPager;
	private RelativeLayout lout;
	AppWidgetHost host;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home_screen);
        
        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(new BkPagerAdapter(getApplicationContext()));
        
        host = new AppWidgetHost(this, 0);

    }

    //Pager 아답터 구현
    private class BkPagerAdapter extends PagerAdapter{
    	private LayoutInflater mInflater;
    	
    	public BkPagerAdapter( Context con) {
			super();
			mInflater = LayoutInflater.from(con);
		}
    	
    	@Override public int getCount() { return 5; }	//여기서는 2개만 할 것이다.
    	
    	//뷰페이저에서 사용할 뷰객체 생성/등록
    	@Override public Object instantiateItem(View pager, int position) {
    		View v = null;
   // 		if(position==0){
    			v = mInflater.inflate(R.layout.layout1, null);
   // 		}
   // 		else if(position==1){
   // 			v = mInflater.inflate(R.layout.layout1, null); 
   //		}
   // 		else if(position==2){
   // 			v = mInflater.inflate(R.layout.layout1, null);
   // 		}
    		registerForContextMenu(v);
    		((ViewPager)pager).addView(v, position);
    		return v; 
    	}
    	
    	//뷰 객체 삭제.
		@Override public void destroyItem(View pager, int position, Object view) {
			((ViewPager)pager).removeView((View)view);
		}

		// instantiateItem메소드에서 생성한 객체를 이용할 것인지
		@Override public boolean isViewFromObject(View view, Object obj) { return view == obj; }
		
		
		@Override public void finishUpdate(View arg0) {}
		@Override public void restoreState(Parcelable arg0, ClassLoader arg1) {}
		@Override public Parcelable saveState() { return null; }
		@Override public void startUpdate(View arg0) {}
    }
    
    @Override
    protected void onStart() {
    	super.onStart();
    	host.startListening();	//start to listen widget related broadcast event.
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	host.stopListening();	//stop to listen widget related broadcast event.
    }
    
	public void onCreateContextMenu (ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
		super.onCreateContextMenu(menu, v, menuInfo);
		Log.i("Q",Integer.toString(v.getId()));
		menu.add(0,1,0,"위젯 추가");
		menu.add(0,2,0,"샘플 2");
		menu.add(0,3,0,"샘플 3");
	}
	public boolean onContextItemSelected (MenuItem item){
		switch (item.getItemId())	{
		case 1:
        	Intent i = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK);
        	int a = host.allocateAppWidgetId();
        	i.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, a);
        	ArrayList<AppWidgetProviderInfo> infos = new ArrayList<AppWidgetProviderInfo>();
        	i.putExtra(AppWidgetManager.EXTRA_CUSTOM_INFO, infos);
        	startActivityForResult(i, 0);
			break;
		case 2:
	
			break;
		case 3:

		}
		return true;
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	
    	if(resultCode == Activity.RESULT_OK){
    		int widgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
    		Bundle a = data.getExtras();
    		Log.i("TestAppWidget", "WidagetID: " + widgetId);
    		AppWidgetProviderInfo info = AppWidgetManager.getInstance(this).getAppWidgetInfo(widgetId);    		
    		addAppWidget(widgetId, info);
    	}
    }
    
    private void addAppWidget(int id, AppWidgetProviderInfo info){
    	
		AppWidgetHostView hv = host.createView(this, id, info);
    	int page = mPager.getCurrentItem();
		Log.i("AppWidget",Integer.toString(page));
		((RelativeLayout)mPager.getChildAt(page)).addView(hv);
    }

	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_screen, menu);
        return true;
    }
}
