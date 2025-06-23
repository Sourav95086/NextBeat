package com.nextgenius.nextbeat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class ScanSongActivity extends AppCompatActivity {

    ArrayList<File> mySong;
    ArrayList<String> songname;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_scan_song);






        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse ) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //converting files into strings to show in listview

                        mySong = fetchmp3(Environment.getExternalStorageDirectory());
                        songname = new ArrayList<>();
                        // String[] songname = new String[mySong.size()];
                        for(int i =0 ; i<mySong.size() ; i++){
                            songname.add( mySong.get(i).getName().replace(".mp3","")) ;
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                    Intent intent = new Intent(ScanSongActivity.this , MainActivity.class);
                                    intent.putExtra("mySong",mySong);
                                    intent.putExtra("songname" , songname);
                                    startActivity(intent);
                                    finish();



                            }
                        });

                    }
                }).start();





            }



            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();



    }
    public ArrayList<File> fetchmp3(File file){
        ArrayList<File> songs = new ArrayList<>();
        if(file != null){
            if(file.isDirectory()){
                File[] files = file.listFiles();
                if(files != null){
                    for(int i =0 ; i<files.length ; i++){
                        File CurentFile = files[i];
                        if(CurentFile.isDirectory()){
                            songs.addAll(fetchmp3(CurentFile));
                        }else{
                            String songname = CurentFile.getName();
                            if(songname.endsWith(".mp3")){
                                songs.add(CurentFile);
                            }
                        }

                    }

                }
            }
        }
        return songs;
    }
}