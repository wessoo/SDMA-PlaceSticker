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
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

public class MainActivity extends Activity implements IOCallback, PlaceStickerListener {
	private SocketIO socket;
	private PlaceStickerReceiver receiver;
	private WifiManager wifiManager;
	private ArrayList<Integer> thumbIDs;
	private ArrayList<Integer> addedIDs;
	private int imgNum = 0;
	private boolean syncTable = false;
	private float scale;
	private float THUMB_SIZE;
	private float MARGINSLR;
	private float MARGINSTB;
	Resources res;
	GridLayout gridlayout;
	LinearLayout linearlayout;
	ScrollView scrollview;
	
	private final Handler myHandler = new Handler();
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	    
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
	    
	    scale = getResources().getDisplayMetrics().density;
	    res = getResources();
	    
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
		            }
		        });
		        System.out.println("FLAG4");
		        
		        gridlayout.addView(imageView, gridlayout.getChildCount());
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
		getMenuInflater().inflate(R.menu.activity_main, menu);
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
		
		//if(!addedIDs.contains(imgNum)) {			
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
	            }
	        });
	        //gridlayout.addView(imageView, Math.min(1, gridlayout.getChildCount()));
	        gridlayout.addView(imageView, gridlayout.getChildCount());
		/*} else {
			Toast.makeText(MainActivity.this, "You have this image already" , Toast.LENGTH_SHORT).show();
		}*/
    }
	
	private int dpToPx(float dp) {
		return (int) (dp * scale + 0.5f);
	}
	
	private void thumbApproached(int index) {
		
	}
	
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
			myHandler.post(updateRunnable);
		}
	}
	
	/* PLACESTICKER INHERITED METHODS */
	@Override
	public void onPositionChanged(DevicePosition position, int style) {
		if(position != null){
			String psID = position.getNearestPlaceSticker().getId();
			int psDistance = position.getNearestPlaceSticker().getDistance();
			
			if(psID.compareTo("table") == 0 && syncTable == false && psDistance <= 25) { //By the table
				Toast.makeText(MainActivity.this, "Synced with touchtable", Toast.LENGTH_SHORT).show();
				linearlayout.setBackgroundColor(Color.LTGRAY);
				syncTable = true;
			} else { //By an artwork
				
			}
			
		}else{ //Not by the table
			//sampleView.setText("Not in Range");
			syncTable = false;
			linearlayout.setBackgroundColor(Color.DKGRAY);
		}
	}

}
