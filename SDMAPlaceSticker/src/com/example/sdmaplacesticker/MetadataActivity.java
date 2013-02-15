package com.example.sdmaplacesticker;

import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MetadataActivity extends Activity {
	Work data;
	private int approached_index;
	
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
		
		//Handle XML
		new FetchTask().execute("http://sdma.bpoc.org:3001/metadata.xml");
	}

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
				System.out.println("FLAG1");
				SAXParserFactory saxPF = SAXParserFactory.newInstance();
				SAXParser saxP = saxPF.newSAXParser();
				XMLReader xmlR = saxP.getXMLReader();
				System.out.println("FLAG2");
				System.out.println("FLAG3");
				XMLHandler xmlHandler = new XMLHandler();
				xmlR.setContentHandler(xmlHandler);
				System.out.println("FLAG4");
				xmlR.parse(new InputSource(new URL(url[0]).openStream()));
				System.out.println("FLAG4.5");
			} catch (Exception e) {
				System.out.println(e);
			}
			
			return XMLHandler.data;
		}
		
		@Override
		protected void onPostExecute(Work data) {
			TextView textview = (TextView) findViewById(R.id.texty);
			textview.setTextSize(20);
			System.out.println("FLAG5");
			//textview.setText("Approached " + data.getTitle().get(1));
			textview.setText("Approached " + data.getTitle().get(approached_index));
			System.out.println("FLAG6");
		}
		
	}

}
