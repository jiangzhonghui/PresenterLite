package com.stylingandroid.presenterlite.presentation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.stylingandroid.presenterlite.R;
import com.stylingandroid.presenterlite.StandaloneDisplayActivity;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

public class DisplayLayout extends FrameLayout
{
	private List<String> slides = new ArrayList<String>();
	
	private int current = -1;
	
	private static final String TAG = StandaloneDisplayActivity.TAG;
	
	private SlideLayout currentSlide;
	
	private FragmentManager fragMgr = null;
	
	private int inAnim = -1;
	private int outAnim = -1;
	
	public DisplayLayout( Context context, AttributeSet attrs )
	{
		super( context, attrs );
		
		if( context instanceof FragmentActivity )
		{
			fragMgr = ((FragmentActivity)context).getSupportFragmentManager();
		}
		
		TypedArray ta = context.obtainStyledAttributes( attrs, R.styleable.DisplayLayout );
		String slidesStr = ta.getString( R.styleable.DisplayLayout_slides );
		try
		{
			InputStream is = getResources().getAssets().open( slidesStr );
			BufferedReader reader = new BufferedReader(
					new InputStreamReader( is ));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			reader.close();
			
			JSONArray arr = new JSONArray( sb.toString() );
			for( int i = 0; i < arr.length(); i++ )
			{
				String slide = arr.optString( i );
				if( slide != null && !slide.isEmpty() ) slides.add( slide );
			}
		}
		catch( IOException e )
		{
			Log.e( TAG, "Error reading slides", e );
		} 
		catch (JSONException e)
		{
			Log.e( TAG, "Error parsing slides", e );
		}
		
		inAnim = ta.getResourceId( R.styleable.DisplayLayout_inAnimation, -1 );
		outAnim = ta.getResourceId( R.styleable.DisplayLayout_outAnimation, -1 );

		ta.recycle();

		if( !slides.isEmpty() )
		{
			go( 0, false );
		}
	}

	public void go( int pos, boolean animate )
	{
		int id = getResources().getIdentifier( slides.get( pos ), null, null );
		SlideFragment slide = new SlideFragment( id );
		if( slide != null )
		{
			FragmentTransaction tx = fragMgr.beginTransaction();
			int in = inAnim;
			int out = outAnim;
			if( currentSlide != null )
			{
				if( currentSlide.getInAnimation() > 0 )
				{
					in = currentSlide.getInAnimation();
				}
				if( currentSlide.getOutAnimation() > 0 )
				{
					out = currentSlide.getOutAnimation();
				}
			}
			if( animate )
			{
				if( in > 0 && out > 0 );
				{
					tx.setCustomAnimations( in, out );
				}
			}
			tx.replace( getId(), slide );
			tx.commit();
			current = pos;
		}
	}
	
	public void setCurrentSlide( SlideLayout slide )
	{
		this.currentSlide = slide;
	}
	
/*	public void go( int pos, boolean animate )
	{
		
		if( getChildCount() > 1 )
		{
			removeViews( 0, getChildCount() - 1 );
		}
		Log.d( TAG, "GO: " + pos );
		int id = getResources().getIdentifier( slides.get( pos ), null, null );
		View v = inflator.inflate( id, this, false );
		currentSlide = (SlideLayout)v.findViewById( R.id.slide );
		Animation in = getInAnimation();
		Animation out = getOutAnimation();
		if( !animate )
		{
			setInAnimation( null );
			setOutAnimation( null );
			removeAllViews();
		}
		if( in != null )
		{
			in.setAnimationListener( new AnimationListener()
			{
				
				public void onAnimationStart( Animation animation )
				{
					// TODO Auto-generated method stub
					
				}
				
				public void onAnimationRepeat( Animation animation )
				{
					// TODO Auto-generated method stub
					
				}
				
				public void onAnimationEnd( Animation animation )
				{
					if( getChildCount() > 1 )
					{
						removeViews( 0, getChildCount() - 1 );
					}
				}
			} );
		}
		else
		{
			removeAllViews();
		}
		addView( v );
		current = pos;
		showNext();
		setInAnimation( in );
		setOutAnimation( out );
	}*/
	
	public void next()
	{
		next( false );
	}
	
	public void next( boolean animate )
	{
		if( current < slides.size() - 1 )
		{
			go( current + 1, animate );
		}
	}
	
	public void previous()
	{
		if( current > 0 )
		{
			go( current - 1, false );
		}
	}
	
	public void advance()
	{
		if( currentSlide.hasMorePhases() )
		{
			currentSlide.nextPhase();
		}
		else
		{
			next( true );
		}
	}
}
