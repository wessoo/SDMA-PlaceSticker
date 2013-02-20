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
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
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
	private GridView gridview;
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
        
        gridview = (GridView) findViewById(R.id.gridview);
	    gridview.setBackgroundColor(Color.DKGRAY);
        gridview.setAdapter(new ImageAdapter(this));
	    
	    scale = getResources().getDisplayMetrics().density;
	    res = getResources();
	    vibrator = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
	    
	    //Prepare PlaceSticker receiver
	    receiver = new PlaceStickerReceiver(this);
	    receiver.setPlaceStcikerListener(this);
	    
	    THUMB_SIZE = res.getDimension(R.dimen.thumb_size);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
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
		//ImageView approached_img = addedThumbs.get(addedIDs.indexOf(index)); //translation to which image
		ImageView approached_img = addedThumbs.get(index);
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
		//if(syncTable) {
			imgNum = Integer.parseInt((String) args[0]);
			if(imgNum <= 25 && imgNum >= 1)
				myHandler.post(updateRunnable);
		//}
	}
	
	/* PLACESTICKER INHERITED METHODS */
	@Override
	public void onPositionChanged(DevicePosition position, int style) {
		if(position != null){ //near a PlaceSticker
			String psID = position.getNearestPlaceSticker().getId();
			int psDistance = position.getNearestPlaceSticker().getDistance();
			
			//Toast.makeText(MainActivity.this, "Approached image " + psID, Toast.LENGTH_SHORT).show();
			
			approachedWork(Integer.parseInt(psID));
			
		}
	}
	
	
	//Internal ImageAdapter class
	public class ImageAdapter extends BaseAdapter {
	    private Context mContext;
	    
	    // references to our images
	    private ArrayList<Integer> mThumbIds;
	    
	    public ImageAdapter(Context c) {
	        mContext = c;
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
	    }

	    public int getCount() {
	        return mThumbIds.size();
	    }

	    public Object getItem(int position) {
	        return null;
	    }

	    public long getItemId(int position) {
	        return 0;
	    }

	    // create a new ImageView for each item referenced by the Adapter
	    public View getView(int position, View convertView, ViewGroup parent) {
	        ImageView imageView;
	        if (convertView == null) {  // if it's not recycled, initialize some attributes
	            imageView = new ImageView(mContext);
	            System.out.println(dpToPx(res.getDimension(R.dimen.thumb_size)));
	            imageView.setLayoutParams(new GridView.LayoutParams(dpToPx(THUMB_SIZE), dpToPx(THUMB_SIZE)));
	            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	        } else {
	            imageView = (ImageView) convertView;
	        }

	        imageView.setImageResource(mThumbIds.get(position));
	        addedThumbs.add(imageView);
	        return imageView;
	    }
	    
	    public void remove(int position) {
	    	mThumbIds.remove(position);
	    }
	}
	
}
