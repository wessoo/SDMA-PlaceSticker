package com.timken.timkenplacesticker;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class CreditsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_credits);
		
		Typeface agb = Typeface.createFromAsset(getAssets(), "fonts/AkzidenzGroteskBE-Bold.otf");
		Typeface agbc = Typeface.createFromAsset(getAssets(), "fonts/Berthold Akzidenz Grotesk BE Light.ttf");
		
		TextView credits_title = (TextView) findViewById(R.id.credits_title);
		TextView credits = (TextView) findViewById(R.id.credits);
		TextView bpoc = (TextView) findViewById(R.id.bpoc);
		TextView isid = (TextView) findViewById(R.id.isid);
		TextView nict = (TextView) findViewById(R.id.nict);
		TextView prime = (TextView) findViewById(R.id.prime);
		TextView timken = (TextView) findViewById(R.id.timken);
		TextView funding = (TextView) findViewById(R.id.funding);
		
		credits_title.setTypeface(agb);
		credits.setTypeface(agbc);
		bpoc.setTypeface(agbc);
		isid.setTypeface(agbc);
		nict.setTypeface(agbc);
		prime.setTypeface(agbc);
		timken.setTypeface(agbc);
		funding.setTypeface(agbc);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.activity_credits, menu);
		return true;
	}

}
