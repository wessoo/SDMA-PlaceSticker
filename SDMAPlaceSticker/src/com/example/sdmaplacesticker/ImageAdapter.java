package com.example.sdmaplacesticker;

import java.util.ArrayList;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

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
            imageView.setLayoutParams(new GridView.LayoutParams(130, 130));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds.get(position));
        return imageView;
    }
    
    public void remove(int position) {
    	mThumbIds.remove(position);
    }
    
    /*private Integer[] mThumbIds = {
            R.drawable.img_1, R.drawable.img_2,
            R.drawable.img_3, R.drawable.img_4,
            R.drawable.img_5, R.drawable.img_6,
            R.drawable.img_7, R.drawable.img_8,
            R.drawable.img_9, R.drawable.img_10,
            R.drawable.img_11, R.drawable.img_12,
            R.drawable.img_13, R.drawable.img_14,
            R.drawable.img_15, R.drawable.img_16,
            R.drawable.img_17, R.drawable.img_18,
            R.drawable.img_19, R.drawable.img_20,
            R.drawable.img_21, R.drawable.img_22,
            R.drawable.img_23, R.drawable.img_24,
            R.drawable.img_25
    };*/
}
