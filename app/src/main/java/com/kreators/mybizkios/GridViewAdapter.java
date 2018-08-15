package com.kreators.mybizkios;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter{
    //public ArrayList<Drawable> allItemsResourceID;
    public ArrayList<String> allItemsResourceID;
    public ArrayList<ObjEntity> itemList;
    private LayoutInflater inflater;
    Context context;
    //public GridViewAdapter(Context context, ArrayList<Drawable> images, ArrayList<ObjEntity> parmItemList) {
    public GridViewAdapter(Context context, ArrayList<String> images, ArrayList<ObjEntity> parmItemList) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        allItemsResourceID = images;
        itemList = parmItemList;
        Log.d("Adapter", "Create Image Adapter " + allItemsResourceID.size());
    }

    @Override
    public int getCount() {
        return allItemsResourceID.size();
    }
    @Override
    public Object getItem(int position) {
        return allItemsResourceID.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    public int getObjItemId(int position) {
        return itemList.get(position).getId();
    }
    public ObjEntity getObj(int position) {
        return itemList.get(position);
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //final ViewHolder holder;
        View view = convertView;
        if (view == null) {
            //view = inflater.inflate(R.layout.image_inflter, parent, false);
            view = inflater.inflate(R.layout.fragment_item_custom, parent, false);
            //holder = new ViewHolder();
            assert view != null;
            //holder.imageView = (ImageView) view.findViewById(R.id.ivImageInflator);
            //view.setTag(holder);
        } else {
            //holder = (ViewHolder) view.getTag();
        }

        TextView txt = (TextView)view.findViewById(R.id.fragment_item_text);
        ImageView img = (ImageView)view.findViewById(R.id.fragment_item_imgview);
        Bitmap myBitmap;


        if (!allItemsResourceID.get(position).equals("")) {
            //holder.imageView.setImageDrawable(allItemsResourceID.get(position));
            File imgFile = new  File(allItemsResourceID.get(position));

            if(imgFile.exists()){

                myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                //ImageView myImage = (ImageView) findViewById(R.id.imageviewTest);

                //myImage.setImageBitmap(myBitmap);
            }
            else myBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.itemdefault);
            //holder.imageView.setImageBitmap(myBitmap);
        }
        else myBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.itemdefault);

        img.setImageBitmap(myBitmap);
        txt.setText(itemList.get(position).getName());
        txt.setTextColor(Color.BLACK);
        return view;
    }
}
