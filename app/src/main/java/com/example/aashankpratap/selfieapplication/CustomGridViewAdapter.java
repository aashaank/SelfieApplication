package com.example.aashankpratap.selfieapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.File;
import java.util.List;

/**
 * Created by AASHANK PRATAP on 3/13/2016.
 */
public class CustomGridViewAdapter extends BaseAdapter {

    Context mContext ;
    private List<CustomGridImageViewObject> imagesPath ;
    int position ;

    public static final String TAG = "SelfieApplication.CustomGridViewAdapter" ;
    private static LayoutInflater inflater = null ;

    public CustomGridViewAdapter(Context context, List<CustomGridImageViewObject> listofPaths) {
        mContext = context;
        imagesPath = listofPaths;
        Log.d(TAG, "CustomGridViewAdapter");
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void imagePathListChanged(List<CustomGridImageViewObject> listofPaths) {
        imagesPath = listofPaths;
        Log.d(TAG, "imagePathListChanged");
    }

    @Override
    public int getCount() {
        Log.d(TAG, "getCount");
        return imagesPath.size();
    }

    @Override
    public Object getItem(int position) {
        Log.d(TAG, "getItem");
        if(imagesPath!=null)
            return  imagesPath.get(position);
        else
            return null;
    }

    @Override
    public long getItemId(int position) {
        this.position = position ;
        Log.d(TAG, "getItemId");
        return position;
    }

    public boolean isPositionChecked(int position) {
        boolean result = imagesPath.get(position).getState();
        Log.d(TAG, "isPositionChecked");
        return result == false ? false : result ;
    }

    public void clearSelection() {
        Log.d(TAG, "clearSelection");
        String filePath = imagesPath.get(position).getName();
        File file = new File(filePath);
        if(file.exists()) {
            Log.d(TAG,"Deleting the file : "+filePath);
            file.delete();
        }
        imagesPath.remove(position);
        notifyDataSetChanged();
    }

    public void setNewSelection(int position, boolean checked) {
        Log.d(TAG, "setNewSelection");
        imagesPath.get(position).setState(checked);
    }

    public void removeSelection(int position) {
        Log.d(TAG, "removeSelection");
        imagesPath.remove(position);
        notifyDataSetChanged();
    }


    public class RowHolder {
        ImageView imageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RowHolder rowHolder = new RowHolder();
        View rowView ;
        Log.d(TAG, "getView");

        rowView = inflater.inflate(R.layout.grid_view_layout, null);
        rowHolder.imageView = (ImageView) rowView.findViewById(R.id.imgView);

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inDither = false ;
        bitmapOptions.inJustDecodeBounds = false ;
        bitmapOptions.inPurgeable = true ;
        bitmapOptions.inSampleSize = 4 ;

        Bitmap bitmap = BitmapFactory.decodeFile(imagesPath.get(position).getName(), bitmapOptions);
        rowHolder.imageView.setImageBitmap(bitmap);

        return rowView;
    }
}
