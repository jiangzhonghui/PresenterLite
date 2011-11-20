package com.stylingandroid.presenterlite.presentation;

import com.stylingandroid.presenterlite.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SlideFragment extends Fragment
{
	private int resource;

	public SlideFragment( int resource )
	{
		this.resource = resource;
	}
	
	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState )
	{
		View v = inflater.inflate( resource, container, false );
		if( container instanceof DisplayLayout )
		{
			((DisplayLayout)container).setCurrentSlide( (SlideLayout )v.findViewById( R.id.slide ) );
		}
		return v;
	}
}
