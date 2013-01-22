package com.example.sdmaplacesticker;

import java.net.MalformedURLException;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.hellogridview.R;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends Activity implements IOCallback {
	SocketIO socket;
	ViewGroup container = null;	
	private String imgNum = "0";
	
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
	    
	    //container = (Grid)
	    final LayoutTransition transitioner = new LayoutTransition();
	    final ImageAdapter adapter = new ImageAdapter(this);
	    
	    final GridView gridview = (GridView) findViewById(R.id.gridview);
	    gridview.setLayoutTransition(transitioner);
	    gridview.setAdapter(adapter);
	    
	    try {
        	Log.i("Pre-socket", "Attemping socket");
        	socket = new SocketIO();
        	Log.i("Pre-connect", "Attemping connect");
        	socket.connect("http://sdma.bpoc.org:3001", this);
        } catch (MalformedURLException e) {
        	Log.e("MalformedURL", "Malformed URL exception");
        	e.printStackTrace();
        }
	    
	    gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            //Toast.makeText(MainActivity.this, "Go to gallery 10" , Toast.LENGTH_SHORT).show();
	            
	        	//adapter.remove(position);
	        	//adapter.notifyDataSetChanged();
	        	
	        	gridview.removeViewAt(position);
	            /*Button newButton = new Button(MainActivity.this);
	            newButton.setText("BOX");
	            GridView gridview = (GridView) findViewById(R.id.gridview);
	            gridview.addView(newButton, Math.min(1, gridview.getChildCount()));*/
	        }
	    });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public void updateUI() {
		/*Button newButton = new Button(MainActivity.this);
        newButton.setText("BOX");
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.addView(newButton, Math.min(1, container.getChildCount()));*/
		
		//Toast.makeText(MainActivity.this, "Got image " + imgNum , Toast.LENGTH_SHORT).show();
    }
	
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
		imgNum = (String) args[0];
		//imgNum = Integer.parseInt((String) args[0]);
		//System.out.println("Server triggered event '" + event + "'");
		
		Log.i("TestApp", imgNum);				
		myHandler.post(updateRunnable);
		
		//Add a thumbnail to Grid
		
		
        
		//Toast.makeText(MainActivity.this, "Got image " + imgNum , Toast.LENGTH_SHORT).show();
	}

}
