package com.example.sdmaplacesticker;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class MetadataActivity extends Activity implements PlaceStickerListener {
	Work data;
	private int approached_index;
	private ArrayList<Integer> mThumbIds;
	
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
        
        mThumbIds.add(R.drawable.img_1);
        mThumbIds.add(R.drawable.img_2);
        mThumbIds.add(R.drawable.img_3);
        mThumbIds.add(R.drawable.img_4);
        mThumbIds.add(R.drawable.img_5);
        mThumbIds.add(R.drawable.img_6);
        mThumbIds.add(R.drawable.img_7);
        mThumbIds.add(R.drawable.img_8);
        mThumbIds.add(R.drawable.img_9);
        mThumbIds.add(R.drawable.img_10);
        mThumbIds.add(R.drawable.img_11);
        mThumbIds.add(R.drawable.img_12);
        mThumbIds.add(R.drawable.img_13);
        mThumbIds.add(R.drawable.img_14);
        mThumbIds.add(R.drawable.img_15);
        mThumbIds.add(R.drawable.img_16);
        mThumbIds.add(R.drawable.img_17);
        mThumbIds.add(R.drawable.img_18);
        mThumbIds.add(R.drawable.img_19);
        mThumbIds.add(R.drawable.img_20);
        mThumbIds.add(R.drawable.img_21);
        mThumbIds.add(R.drawable.img_22);
        mThumbIds.add(R.drawable.img_23);
        mThumbIds.add(R.drawable.img_24);
        mThumbIds.add(R.drawable.img_25);
		
		//Handle XML
		new FetchTask().execute("http://sdma.bpoc.org:3001/metadata.xml");
		
		//Set alpha
		ImageView image = (ImageView) findViewById(R.id.image);
		image.setAlpha(0f);
		/*TextView title = (TextView) findViewById(R.id.title);
		title.setAlpha(0);
		TextView artist = (TextView) findViewById(R.id.artist);
		artist.setAlpha(0);
		TextView date = (TextView) findViewById(R.id.date);
		date.setAlpha(0);
		TextView place = (TextView) findViewById(R.id.place);
		place.setAlpha(0);
		TextView dimensions = (TextView) findViewById(R.id.dimensions);
		dimensions.setAlpha(0);
		TextView description = (TextView) findViewById(R.id.description);
		description.setAlpha(0);*/
	}
	
	/*@Override
	public void onBackPressed()  
	{  
	    //do whatever you want the 'Back' button to do  
	    //as an example the 'Back' button is set to start a new Activity named 'NewActivity'  
	    Intent intent = new Intent(MetadataActivity.this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    this.startActivity(intent);  

	    return;  
	}*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_metadata, menu);
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
				xmlR.parse(new InputSource(new URL(url[0]).openStream()));
				System.out.println("FLAG4.5");
			} catch (Exception e) {
				System.out.println(e);
			}
			
			return XMLHandler.data;
		}
		
		@Override
		protected void onPostExecute(Work data) {
			ImageView image = (ImageView) findViewById(R.id.image);
			image.setImageResource(mThumbIds.get(approached_index));
			ObjectAnimator anim = ObjectAnimator.ofFloat(image, "alpha", 0f, 1f);
			anim.setDuration(300);
			anim.start();
			
			TextView title = (TextView) findViewById(R.id.title);
			title.setText(data.getTitle().get(approached_index));
			ObjectAnimator anim2 = ObjectAnimator.ofFloat(title, "alpha", 0f, 1f);
			anim2.setDuration(300);
			anim2.start();
			
			TextView artist = (TextView) findViewById(R.id.artist);
			artist.setText(data.getArtist().get(approached_index));
			ObjectAnimator anim3 = ObjectAnimator.ofFloat(artist, "alpha", 0f, 1f);
			anim3.setDuration(300);
			anim3.start();
			
			TextView date = (TextView) findViewById(R.id.date);
			date.setText(data.getDate().get(approached_index));
			ObjectAnimator anim4 = ObjectAnimator.ofFloat(date, "alpha", 0f, 1f);
			anim4.setDuration(300);
			anim4.start();
			
			TextView place = (TextView) findViewById(R.id.place);
			place.setText(data.getPlace().get(approached_index));
			ObjectAnimator anim5 = ObjectAnimator.ofFloat(place, "alpha", 0f, 1f);
			anim5.setDuration(300);
			anim5.start();
			
			TextView dimensions = (TextView) findViewById(R.id.dimensions);
			dimensions.setText(data.getDimensions().get(approached_index));
			ObjectAnimator anim6 = ObjectAnimator.ofFloat(dimensions, "alpha", 0f, 1f);
			anim6.setDuration(300);
			anim6.start();
			
			TextView description = (TextView) findViewById(R.id.description);
			description.setText(data.getDescription().get(approached_index));
			ObjectAnimator anim7 = ObjectAnimator.ofFloat(description, "alpha", 0f, 1f);
			anim7.setDuration(300);
			anim7.start();
		}
		
	}

	@Override
	public void onPositionChanged(DevicePosition arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

}
