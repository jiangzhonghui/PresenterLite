package com.stylingandroid.presenterlite;

import com.stylingandroid.presenterlite.presentation.DisplayLayout;

import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;

public class StandaloneDisplayActivity extends FragmentActivity
{
	public static final String TAG = "PresenterLite";

	private WakeLock wakeLock;
	protected DisplayLayout display = null; 

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		
		PowerManager pm = (PowerManager) getSystemService( POWER_SERVICE );
		wakeLock = pm.newWakeLock( PowerManager.FULL_WAKE_LOCK, TAG );
		setContentView( R.layout.display );
		display = (DisplayLayout) findViewById( R.id.display );
		display.setSystemUiVisibility( View.STATUS_BAR_HIDDEN );
		getActionBar().hide();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		wakeLock.acquire();
	}
	
	@Override
	protected void onPause()
	{
		wakeLock.release();
		super.onPause();
	}
	
	@Override
	public boolean onTouchEvent( MotionEvent event )
	{
		if( event.getAction() == MotionEvent.ACTION_UP )
		{
			display.advance();
			return true;
		}
		return super.onTouchEvent( event );
	}
}
