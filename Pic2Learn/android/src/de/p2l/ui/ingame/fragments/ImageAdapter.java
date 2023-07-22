package de.p2l.ui.ingame.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import de.p2l.R;
import de.p2l.service.parser.parser.Parser;

/*
ImageAdapter manages list of images (defined by int value from R.drawable) and represents number of image with textview
call ImageAdapter adapter.notifyDataSetChanged() when data list changes
then call view.setAdapter(adapter) to update view
 */
public class ImageAdapter extends BaseAdapter {
    private static final String TAG = "ImageAdapter";
    private Context mContext;
    private List<Integer> items;
    private ColorCoder colorCoder;

    public ImageAdapter(Context c, ArrayList<Integer> items, ColorCoder colorCoder) {
        mContext = c;
        this.items = items;
        this.colorCoder = colorCoder;
    }

    public int getCount() {
        return items.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.fragment_commands_grid_entry, null);
        TextView textView = (TextView) convertView.findViewById(R.id.grid_item_label);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.grid_item_image);
        textView.setText("" + position);
        imageView.setBackgroundResource(items.get(position));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(2, 2, 2, 8);

        //set Colors of Background
        setViewBackGround(imageView, position);
        if (colorCoder.getSelectedPosition()-1 == position) {
            convertView.setBackgroundColor(parent.getResources().getColor(R.color.soft_blue));
        }
        return convertView;
    }


    private void setViewBackGround(ImageView imageView, int position) {
        HashMap<Integer, Integer> colorCode = colorCoder.getColorCode();

        Integer color = colorCode.get(position);
        if (color == null) {
            return;
        }

        imageView.getBackground().mutate();
        imageView.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
    }

}

