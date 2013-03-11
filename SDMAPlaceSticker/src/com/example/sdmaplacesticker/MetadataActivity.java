package com.example.sdmaplacesticker;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import jp.co.isid.placesticker.lib.DevicePosition;
import jp.co.isid.placesticker.lib.PlaceStickerListener;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MetadataActivity extends Activity implements PlaceStickerListener {
	Work data;
	private int approached_index;
	private ArrayList<Integer> mThumbIds;
	private TextView lb_title;
	private TextView title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_metadata);
		overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
		// Show the Up button in the action bar.
		//getActionBar().setDisplayHomeAsUpEnabled(true);

		//Get message from intent
		Intent intent = getIntent();
		approached_index = intent.getIntExtra(MainActivity.IMG_INDEX, 0) - 1;
		
		//Build array of image IDs
		mThumbIds = new ArrayList<Integer>();
        mThumbIds.add(R.drawable.carlevarijs);
        mThumbIds.add(R.drawable.rembrandt);
        
        Typeface tfbold = Typeface.createFromAsset(getAssets(), "fonts/AkzidenzGroteskBE-Bold.otf");
        Typeface tfreg = Typeface.createFromAsset(getAssets(), "fonts/Berthold Akzidenz Grotesk BE Light.ttf");
        
		ImageView image = (ImageView) findViewById(R.id.image);
		image.setImageResource(mThumbIds.get(approached_index));
		
		TextView lb_title = (TextView) findViewById(R.id.lb_title);
		TextView lb_artist = (TextView) findViewById(R.id.lb_artist);
		TextView lb_date = (TextView) findViewById(R.id.lb_date);
		TextView lb_place = (TextView) findViewById(R.id.lb_place);
		TextView lb_dimensions = (TextView) findViewById(R.id.lb_dimensions);
		TextView lb_description = (TextView) findViewById(R.id.lb_description);		
		TextView lb_nationality = (TextView) findViewById(R.id.lb_nationality);
		TextView lb_medium = (TextView) findViewById(R.id.lb_medium);
		
		TextView title = (TextView) findViewById(R.id.title);
		TextView artist = (TextView) findViewById(R.id.artist);
		TextView date = (TextView) findViewById(R.id.date);
		TextView place = (TextView) findViewById(R.id.place);
		TextView dimensions = (TextView) findViewById(R.id.dimensions);
		TextView description = (TextView) findViewById(R.id.description);		
		TextView nationality = (TextView) findViewById(R.id.nationality);
		TextView medium = (TextView) findViewById(R.id.medium);
		
		title.setTypeface(tfreg);
		lb_title.setTypeface(tfbold);
		artist.setTypeface(tfreg);
		lb_artist.setTypeface(tfbold);
		date.setTypeface(tfreg);
		lb_date.setTypeface(tfbold);
		place.setTypeface(tfreg);
		lb_place.setTypeface(tfbold);
		dimensions.setTypeface(tfreg);
		lb_dimensions.setTypeface(tfbold);
		description.setTypeface(tfreg);
		lb_description.setTypeface(tfbold);
		nationality.setTypeface(tfreg);
		lb_nationality.setTypeface(tfbold);
		medium.setTypeface(tfreg);
		lb_medium.setTypeface(tfbold);
        
        if(approached_index == 0) {
			title.setText(getResources().getString(R.string.title1));
			artist.setText(getResources().getString(R.string.artist1));
			date.setText(getResources().getString(R.string.date1));
			nationality.setText(getResources().getString(R.string.nationality1));
			place.setText(getResources().getString(R.string.place1));
			dimensions.setText(getResources().getString(R.string.dimensions1));
			medium.setText(getResources().getString(R.string.medium1));
			description.setText(getResources().getString(R.string.description1));
		} else if(approached_index == 1) {
			title.setText(getResources().getString(R.string.title2));
			artist.setText(getResources().getString(R.string.artist2));
			date.setText(getResources().getString(R.string.date2));
			nationality.setText(getResources().getString(R.string.nationality2));
			place.setText(getResources().getString(R.string.place2));
			dimensions.setText(getResources().getString(R.string.dimensions2));
			medium.setText(getResources().getString(R.string.medium2));
			description.setText(getResources().getString(R.string.description2));
		}
        
		
		//Handle XML
		//new FetchTask().execute("http://sdma.bpoc.org:3001/metadata.xml");
        //new FetchTask().execute("android.resource://com.example.sdmaplacesticker/raw/metadata.xml");
		
		//Set alpha
		/*ImageView image = (ImageView) findViewById(R.id.image);
		image.setAlpha(0f);*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private class FetchTask extends AsyncTask<String, Void, Work> {

		@Override
		protected Work doInBackground(String... url) {
			try{
				SAXParserFactory saxPF = SAXParserFactory.newInstance();
				SAXParser saxP = saxPF.newSAXParser();
				XMLReader xmlR = saxP.getXMLReader();
				XMLHandler xmlHandler = new XMLHandler();
				xmlR.setContentHandler(xmlHandler);
				
				//Uri path = Uri.parse("android.resource://com.example.sdmaplacesticker/raw/metadata.xml");
				//xmlR.parse("android.resource://com.example.sdmaplacesticker/raw/metadata.xml");
				xmlR.parse(new InputSource(new URL(url[0]).openStream()));
			} catch (Exception e) {
				System.out.println(e);
			}
			
			return XMLHandler.data;
		}
		
		@Override
		protected void onPostExecute(Work data) {
			ImageView image = (ImageView) findViewById(R.id.image);
			image.setImageResource(mThumbIds.get(approached_index));
			/*ObjectAnimator anim = ObjectAnimator.ofFloat(image, "alpha", 0f, 1f);
			anim.setDuration(300);
			anim.start();*/
			
			Animation fadeImg = AnimationUtils.loadAnimation(MetadataActivity.this, R.anim.fade_img);
			image.startAnimation(fadeImg);
			
			TextView title = (TextView) findViewById(R.id.title);
			/*title.setText(data.getTitle().get(approached_index));
			ObjectAnimator anim2 = ObjectAnimator.ofFloat(title, "alpha", 0f, 1f);
			anim2.setDuration(300);
			anim2.start();*/
			
			TextView artist = (TextView) findViewById(R.id.artist);
			/*artist.setText(data.getArtist().get(approached_index));
			ObjectAnimator anim3 = ObjectAnimator.ofFloat(artist, "alpha", 0f, 1f);
			anim3.setDuration(300);
			anim3.start();*/
			
			TextView date = (TextView) findViewById(R.id.date);
			/*date.setText(data.getDate().get(approached_index));
			ObjectAnimator anim4 = ObjectAnimator.ofFloat(date, "alpha", 0f, 1f);
			anim4.setDuration(300);
			anim4.start();*/
			
			TextView place = (TextView) findViewById(R.id.place);
			/*place.setText(data.getPlace().get(approached_index));
			ObjectAnimator anim5 = ObjectAnimator.ofFloat(place, "alpha", 0f, 1f);
			anim5.setDuration(300);
			anim5.start();*/
			
			TextView dimensions = (TextView) findViewById(R.id.dimensions);
			/*dimensions.setText(data.getDimensions().get(approached_index));
			ObjectAnimator anim6 = ObjectAnimator.ofFloat(dimensions, "alpha", 0f, 1f);
			anim6.setDuration(300);
			anim6.start();*/
			
			TextView description = (TextView) findViewById(R.id.description);
			/*if(approached_index == 0) {
				description.setText(data.getDescription().get(approached_index));
			} else if(approached_index == 1) {
				description.setText(getResources().getString(R.string.description2));
			}
			ObjectAnimator anim7 = ObjectAnimator.ofFloat(description, "alpha", 0f, 1f);
			anim7.setDuration(300);
			anim7.start();*/
			
			TextView nationality = (TextView) findViewById(R.id.nationality);
			/*nationality.setText(data.getNationality().get(approached_index));
			ObjectAnimator anim8 = ObjectAnimator.ofFloat(nationality, "alpha", 0f, 1f);
			anim8.setDuration(300);
			anim8.start();*/
			
			TextView medium = (TextView) findViewById(R.id.medium);
			/*medium.setText(data.getMedium().get(approached_index));
			ObjectAnimator anim9 = ObjectAnimator.ofFloat(medium, "alpha", 0f, 1f);
			anim9.setDuration(300);
			anim9.start();*/
			
			if(approached_index == 0) {
				title.setText(getResources().getString(R.string.title1));
				artist.setText(getResources().getString(R.string.artist1));
				date.setText(getResources().getString(R.string.date1));
				nationality.setText(getResources().getString(R.string.nationality1));
				place.setText(getResources().getString(R.string.place1));
				dimensions.setText(getResources().getString(R.string.dimensions1));
				medium.setText(getResources().getString(R.string.medium1));
				description.setText(getResources().getString(R.string.description1));
			} else if(approached_index == 1) {
				title.setText(getResources().getString(R.string.title2));
				artist.setText(getResources().getString(R.string.artist2));
				date.setText(getResources().getString(R.string.date2));
				nationality.setText(getResources().getString(R.string.nationality2));
				place.setText(getResources().getString(R.string.place2));
				dimensions.setText(getResources().getString(R.string.dimensions2));
				medium.setText(getResources().getString(R.string.medium2));
				description.setText(getResources().getString(R.string.description2));
			}
		}
		
	}

	@Override
	public void onPositionChanged(DevicePosition arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

}
