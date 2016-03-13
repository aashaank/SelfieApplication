package com.example.aashankpratap.selfieapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.ActionMode;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MainActivity extends Activity implements AbsListView.MultiChoiceModeListener {

    private static final String TAG = "SelfieApplication.MainActivity";

    private GridView grid;
    private CustomGridViewAdapter customGridViewAdapter ;
    private Uri fileUri ;
    private List<CustomGridImageViewObject> listofImagesPath ;

    private int nr = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        grid = (GridView) findViewById(R.id.grid_view_images);
        listofImagesPath = retriveCapturedImagePath();
        Log.d(TAG,"onCreate");

        if(listofImagesPath != null) {
            customGridViewAdapter = new CustomGridViewAdapter(MainActivity.this, listofImagesPath);
            grid.setAdapter(customGridViewAdapter);
        }

        grid.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        grid.setMultiChoiceModeListener(this);

        grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG,"grid Image onItemLongClick");
                listofImagesPath.get(position).setState(true);
                grid.setItemChecked(position, !customGridViewAdapter.isPositionChecked(position));
                customGridViewAdapter.notifyDataSetChanged();
                return false;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //This is for removing the background color from the FAB
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"fab click camera");
                Intent cameraIntent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
                fileUri = getOutputMediaFile(Constant.MEDIA_TYPE_IMAGE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(cameraIntent, Constant.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        listofImagesPath = retriveCapturedImagePath();
        customGridViewAdapter.imagePathListChanged(listofImagesPath);
        customGridViewAdapter.notifyDataSetChanged();
        grid.invalidateViews();
    }

    private List<CustomGridImageViewObject> retriveCapturedImagePath() {
        List<CustomGridImageViewObject> imageFileList = new ArrayList<CustomGridImageViewObject>();
        CustomGridImageViewObject gridImageViewObject ;
        File f = new File(Constant.IMAGEPATH, Constant.IMAGEFOLDERNAME);
        Log.d(TAG, "retriveCapturedImagePath");
        if(f.exists()) {
            File[] files = f.listFiles();
            Arrays.sort(files);
            for(int i = 0; i<files.length; i++) {
                File file = files[i];
                if(file.isDirectory()) {
                    continue;
                }
                gridImageViewObject = new CustomGridImageViewObject(file.getPath(),false);
                imageFileList.add(gridImageViewObject);
            }
        }
        Log.d(TAG, "retriveCapturedImagePath imageFileList : "+imageFileList);
        return imageFileList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.d(TAG, "onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Log.d(TAG, "onOptionsItemSelected");

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static Uri getOutputMediaFile(int type) {
        Uri uri = null;
        File mediaStorageDir = new File(Constant.IMAGEPATH, Constant.IMAGEFOLDERNAME);
        Log.d(TAG,"getOutputMediaFile");
        if(!mediaStorageDir.exists())
            if(!mediaStorageDir.mkdir())
                return null;

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        if(type == Constant.MEDIA_TYPE_IMAGE) {
            File file = new File(mediaStorageDir.getPath()+File.separator+"IMG_"+timeStamp+".jpg");
            uri = Uri.fromFile(file);
        }
        return uri;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG,"onActivityResult");
        if(requestCode == Constant.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                listofImagesPath = retriveCapturedImagePath();
                Log.d(TAG,"onActivityResult image is successfully captured");
                if(!listofImagesPath.isEmpty()){
                    customGridViewAdapter.imagePathListChanged(listofImagesPath);
                    customGridViewAdapter.notifyDataSetChanged();
                    grid.invalidateViews();
                }
            }
            else if(resultCode == RESULT_CANCELED) {
                //User cancelled the image capture
                Log.d(TAG,"onActivityResult RESULT CANCELED");
            }
            else {
                //Image capture failed,advise user
                Log.d(TAG,"onActivityResult image capture failed");
            }
        }
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        Log.d(TAG,"onItemCheckedStateChanged");
        if (checked) {
            nr++;
            Log.d(TAG,"onItemCheckedStateChanged checked true");
            customGridViewAdapter.setNewSelection(position, checked);
        } else {
            nr--;
            Log.d(TAG,"onItemCheckedStateChanged checked false");
            customGridViewAdapter.removeSelection(position);
        }
        mode.setTitle(nr + "selected");
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        nr = 0;
        Log.d(TAG,"onCreateActionMode");
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.custom_action_bar, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        Log.d(TAG,"onPrepareActionMode");
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        Log.d(TAG,"onActionItemClicked");
        switch (item.getItemId()) {
            case R.id.action_delete: {
                nr = 0;
                Log.d(TAG,"onItemCheckedStateChanged delete");
                customGridViewAdapter.clearSelection();
                mode.finish();
                break;
            }
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        Log.d(TAG,"onDestroyActionMode");
    }
}
