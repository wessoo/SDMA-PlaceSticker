package com.example.sdmaplacesticker;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

import java.net.MalformedURLException;
import java.util.ArrayList;

import jp.co.isid.placesticker.lib.DevicePosition;
import jp.co.isid.placesticker.lib.PlaceStickerListener;
import jp.co.isid.placesticker.lib.PlaceStickerReceiver;
import jp.co.isid.placesticker.lib.Exception.PlaceStickerException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

public class MainActivity extends Activity implements IOCallback, PlaceStickerListener {
	private SocketIO socket;
	private PlaceStickerReceiver receiver;
	private WifiManager wifiManager;
	
	public final static String IMG_INDEX = "com.example.sdmaplacesticker.INDEX";
	
	private ArrayList<Integer> thumbIDs;
	private ArrayList<Integer> addedIDs;
	private ArrayList<ImageView> addedThumbs;
	private int imgNum = 0;
	private int approached_index = 0;
	private boolean syncTable = false;
	private float scale;
	private float THUMB_SIZE;
	private float MARGINSLR;
	private float MARGINSTB;
	private Resources res;
	private GridLayout gridlayout;
	private LinearLayout linearlayout;
	private ScrollView scrollview;
	private Vibrator vibrator;
	
	private final Handler myHandler = new Handler();
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	    
	    addedThumbs = new ArrayList<ImageView>();
	    thumbIDs = new ArrayList<Integer>();
        thumbIDs.add(R.drawable.img_1);
        thumbIDs.add(R.drawable.img_2);
        thumbIDs.add(R.drawable.img_3);
        thumbIDs.add(R.drawable.img_4);
        thumbIDs.add(R.drawable.img_5);
        thumbIDs.add(R.drawable.img_6);
        thumbIDs.add(R.drawable.img_7);
        thumbIDs.add(R.drawable.img_8);
        thumbIDs.add(R.drawable.img_9);
        thumbIDs.add(R.drawable.img_10);
        thumbIDs.add(R.drawable.img_11);
        thumbIDs.add(R.drawable.img_12);
        thumbIDs.add(R.drawable.img_13);
        thumbIDs.add(R.drawable.img_14);
        thumbIDs.add(R.drawable.img_15);
        thumbIDs.add(R.drawable.img_16);
        thumbIDs.add(R.drawable.img_17);
        thumbIDs.add(R.drawable.img_18);
        thumbIDs.add(R.drawable.img_19);
        thumbIDs.add(R.drawable.img_20);
        thumbIDs.add(R.drawable.img_21);
        thumbIDs.add(R.drawable.img_22);
        thumbIDs.add(R.drawable.img_23);
        thumbIDs.add(R.drawable.img_24);
        thumbIDs.add(R.drawable.img_25);
	    
        //turn on WiFi
        wifiManager = (WifiManager) this.getSystemService(this.WIFI_SERVICE);
        if(!wifiManager.isWifiEnabled()) {
        	wifiManager.setWifiEnabled(true);
        }
        
	    gridlayout = (GridLayout) findViewById(R.id.gridlayout);
	    linearlayout = (LinearLayout) findViewById(R.id.linearlayout);
	    linearlayout.setBackgroundColor(Color.DKGRAY);
	    scrollview= (ScrollView) findViewById(R.id.scrollview);
	    //gridlayout.setBackgroundColor(Color.BLACK);
	    gridlayout.bringToFront();
	    
	    scale = getResources().getDisplayMetrics().density;
	    res = getResources();
	    vibrator = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
	    
	    //Prepare PlaceSticker receiver
	    receiver = new PlaceStickerReceiver(this);
	    receiver.setPlaceStcikerListener(this);
	    
	    THUMB_SIZE = res.getDimension(R.dimen.thumb_size);
	    MARGINSLR = res.getDimension(R.dimen.marginslr);
	    MARGINSTB = res.getDimension(R.dimen.marginstb);
	    
	    //Handle an orientation change
	    final ArrayList<Integer> data = (ArrayList<Integer>) getLastNonConfigurationInstance();
	    
	    if(data != null) {	  //If images already added from previous orientation  	
	    	addedIDs = new ArrayList<Integer>();
	    	if(data.get(data.size() - 1) == 1) { //syncing
	    		syncTable = true;
	    		linearlayout.setBackgroundColor(Color.LTGRAY);
	    	}
	    	
	    	for(int i = 0; i < data.size() - 1; i++) {
	    		imgNum = data.get(i);
	    		addedIDs.add(imgNum);
	    		
				GridLayout.LayoutParams layoutparams = new GridLayout.LayoutParams();
		        layoutparams.setMargins((int)MARGINSLR, (int)MARGINSTB, (int)MARGINSLR, (int)MARGINSTB);
		        System.out.println("FLAG1");
		        ImageView imageView = new ImageView(this);
		        imageView.setId(imgNum);
		        imageView.setAdjustViewBounds(true);
		        System.out.println("FLAG2");
		        imageView.setMaxWidth((int)THUMB_SIZE);
		        imageView.setMaxHeight((int)THUMB_SIZE);
		        imageView.setLayoutParams(layoutparams);
		        System.out.println("FLAG3");
		        imageView.setImageResource(thumbIDs.get(imgNum - 1));
		        imageView.setOnClickListener(new View.OnClickListener() {
		            public void onClick(View v) {
		            	gridlayout.removeView(v);
		            	addedIDs.remove(addedIDs.indexOf(v.getId()));
		            	addedThumbs.remove(v);
		            }
		        });
		        System.out.println("FLAG4");
		        
		        gridlayout.addView(imageView, gridlayout.getChildCount());
		        addedThumbs.add(imageView); //add thumbnail to arraylist
	    	}
	    	
	    } else { //Else images haven't been added
	    	addedIDs = new ArrayList<Integer>();
	    }
	    
	    try {
        	Log.i("Pre-socket", "Attemping socket");
        	socket = new SocketIO();
        	Log.i("Pre-connect", "Attemping connect");
        	socket.connect("http://sdma.bpoc.org:3001", this);
        } catch (MalformedURLException e) {
        	Log.e("MalformedURL", "Malformed URL exception");
        	e.printStackTrace();
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		ArrayList<Integer> data = addedIDs;
		if(syncTable) 
			data.add(1);
		else
			data.add(0);
		
		return data;
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		if(!wifiManager.isWifiEnabled()) {
        	wifiManager.setWifiEnabled(true);
        }
		
        try {
        	receiver.loadSettingFile(R.raw.config);
			receiver.scanStart();
		} catch (PlaceStickerException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();		
		receiver.scanStop();
	}
	
	public Runnable updateRunnable = new Runnable() {
    	public void run() {
    		updateUI();
    	}
    };
    
	public void updateUI() {		
		
		if(!addedIDs.contains(imgNum)) {			
	        addedIDs.add(imgNum);
	        
			GridLayout.LayoutParams layoutparams = new GridLayout.LayoutParams();
	        layoutparams.setMargins((int)MARGINSLR, (int)MARGINSTB, (int)MARGINSLR, (int)MARGINSTB);
	        
	        ImageView imageView = new ImageView(this);
	        imageView.setId(imgNum);
	        imageView.setAdjustViewBounds(true);
	        imageView.setMaxWidth((int)THUMB_SIZE);
	        imageView.setMaxHeight((int)THUMB_SIZE);
	        imageView.setLayoutParams(layoutparams);
	        imageView.setImageResource(thumbIDs.get(imgNum - 1));
	        imageView.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	gridlayout.removeView(v);
	            	addedIDs.remove(addedIDs.indexOf(v.getId()));
	            	addedThumbs.remove(v);
	            }
	        });
	        
	        gridlayout.addView(imageView, gridlayout.getChildCount());
	        addedThumbs.add(imageView); //add thumbnail to arraylist
		} else {
			Toast.makeText(MainActivity.this, "You have this image already" , Toast.LENGTH_SHORT).show();
		}
    }
	
	private int dpToPx(float dp) {
		return (int) (dp * scale + 0.5f);
	}
	
	private void approachedWork(int index) {
		approached_index = index;
		ImageView approached_img = addedThumbs.get(addedIDs.indexOf(index));
		Animation thumbGrow = AnimationUtils.loadAnimation(this, R.anim.thumb_grow);
		approached_img.bringToFront();
		approached_img.startAnimation(thumbGrow);
		vibrator.vibrate(200);
		thumbGrow.setAnimationListener(anim_listener);		
	}
	
	Animation.AnimationListener anim_listener = new Animation.AnimationListener() {
		
		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub
							
		}
		
		@Override
		public void onAnimationEnd(Animation animation) {
			// TODO Auto-generated method stub	
			Intent intent = new Intent(MainActivity.this, MetadataActivity.class);
			intent.putExtra(IMG_INDEX, approached_index);
			startActivity(intent);
		}
	};
	
	/* SOCKET-IO INHERITED METHODS */	
	@Override
	public void onMessage(JSONObject json, IOAcknowledge ack) {
		try {
			System.out.println("Server said:" + json.toString(2));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onMessage(String data, IOAcknowledge ack) {
		System.out.println("Server said: " + data);
	}

	@Override
	public void onError(SocketIOException socketIOException) {
		System.out.println("an Error occurred");
		socketIOException.printStackTrace();
	}

	@Override
	public void onDisconnect() {
		System.out.println("Connection terminated.");
	}

	@Override
	public void onConnect() {
		System.out.println("Connection established");
	}

	@Override
	public void on(String event, IOAcknowledge ack, Object... args) {
		if(syncTable) {
			imgNum = Integer.parseInt((String) args[0]);
			if(imgNum <= 25 && imgNum >= 1)
				myHandler.post(updateRunnable);
		}
	}
	
	/* PLACESTICKER INHERITED METHODS */
	@Override
	public void onPositionChanged(DevicePosition position, int style) {
		if(position != null){ //near a PlaceSticker
			String psID = position.getNearestPlaceSticker().getId();
			int psDistance = position.getNearestPlaceSticker().getDistance();
			
			if(psID.compareTo("table") == 0 && syncTable == false && psDistance <= 25) { //By the table
				Toast.makeText(MainActivity.this, "Synced with touchtable", Toast.LENGTH_SHORT).show();
				linearlayout.setBackgroundColor(Color.LTGRAY);
				syncTable = true;
			} else if(psID.compareTo("table") != 0) { //By an artwork
				if(addedIDs.contains(Integer.parseInt(psID))) {
					approachedWork(Integer.parseInt(psID));
				}
				//Toast.makeText(MainActivity.this, "Approached image " + psID, Toast.LENGTH_SHORT).show();
				linearlayout.setBackgroundColor(Color.DKGRAY);
				syncTable = false;
			}
			
		}else{ //Not by a table
			//sampleView.setText("Not in Range");
			syncTable = false;
			linearlayout.setBackgroundColor(Color.DKGRAY);
		}
	}

}
