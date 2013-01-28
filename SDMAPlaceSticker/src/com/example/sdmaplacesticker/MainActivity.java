package com.example.sdmaplacesticker;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

import java.net.MalformedURLException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.GridLayoutAnimationController;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity implements IOCallback {
	SocketIO socket;
	private int imgNum = 0;
	private ArrayList<Integer> thumbIDs;
	private ArrayList<Integer> addedIDs;
	private float scale;
	private float THUMB_SIZE;
	private float MARGINSLR;
	private float MARGINSTB;
	Resources res;
	GridLayout gridlayout;
	
	private final Handler myHandler = new Handler();
	public Runnable updateRunnable = new Runnable() {
    	public void run() {
    		updateUI();
    	}
    };
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	    
	    thumbIDs = new ArrayList<Integer>();
	    addedIDs = new ArrayList<Integer>();
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
	    
	    gridlayout = (GridLayout) findViewById(R.id.gridlayout);
	    scale = getResources().getDisplayMetrics().density;
	    res = getResources();
	    THUMB_SIZE = res.getDimension(R.dimen.thumb_size);
	    MARGINSLR = res.getDimension(R.dimen.marginslr);
	    MARGINSTB = res.getDimension(R.dimen.marginstb);
	    
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
		imgNum = Integer.parseInt((String) args[0]);
		myHandler.post(updateRunnable);		
	}

}
