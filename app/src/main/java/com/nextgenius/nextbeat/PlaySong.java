package com.nextgenius.nextbeat;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.util.ArrayList;

public class PlaySong extends AppCompatActivity {
    TextView songname ;
    ImageView forward , pause , backward;
    MediaPlayer mediaPlayer;
    SeekBar seekBar;
    int click = 0;
    int whichsong = 0 ;
    Thread updateseek;
    ImageView back_button;
    Switch aSwitch;
    Boolean check = false;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_play_song);


        //intents
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        ArrayList<File> songs = (ArrayList)bundle.getParcelableArrayList("list");
        String Songname = intent.getStringExtra("item");
        int positions = intent.getIntExtra("position",0);
        whichsong = positions;


        //geting id's
        songname = findViewById(R.id.textView);
        pause =findViewById(R.id.pause);
        forward = findViewById(R.id.forward);
        backward = findViewById(R.id.backward);
        seekBar = findViewById(R.id.seekbar);
        back_button = findViewById(R.id.back_button);
        aSwitch = findViewById(R.id.loop);


        //setting song name
        songname.setText(Songname);


        //playing music
        playMusic(songs,positions);




        //seting maximum seekbar
        seekBar.setMax(mediaPlayer.getDuration());


        //seekbar progress method
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

            //thread for updating seekbar
            updateseek = new Thread() {
                @Override
                public void run() {
                    try {
                        while (mediaPlayer != null && mediaPlayer.isPlaying()) {
                            int currentPosition = mediaPlayer.getCurrentPosition();
                            runOnUiThread(() -> seekBar.setProgress(currentPosition));
                            sleep(500);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
updateseek.start();


        //on click for play/pause button

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                click++;
                if(click % 2 != 0){
                    mediaPlayer.pause();
                    pause.setImageResource(R.drawable.play);
                }else{

                    mediaPlayer.start();
                    pause.setImageResource(R.drawable.pause);
                }
            }
        });

        //on click for forward button

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(songs.size()-1>whichsong){
                    whichsong++;
                }else{
                    Toast.makeText(PlaySong.this, "No Further Song Exist", Toast.LENGTH_SHORT).show();
                }

               mediaPlayer.stop();
               String newname = songs.get(whichsong).getName().toString();
               newname = newname.replace(".mp3" , "");
               songname.setText(newname);
               playMusic(songs,whichsong);
               click  =2;
               pause.setImageResource(R.drawable.pause);
            }
        });

        //on click for backward button

        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(whichsong>0){
                    whichsong--;
                }else{
                    Toast.makeText(PlaySong.this, "No Further Song Exist", Toast.LENGTH_SHORT).show();
                }
                mediaPlayer.stop();
                String newname = songs.get(whichsong).getName().toString();
                newname = newname.replace(".mp3" , "");
                songname.setText(newname);
                playMusic(songs,whichsong);
                pause.setImageResource(R.drawable.pause);
                click = 2;
            }
        });

        //onclick for backbutton

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

        //onClick listner on switch

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                check = isChecked;
            }
        });



    }

    //method to play music
    public void playMusic(ArrayList<File> songs , int position ) {
        Uri uri = Uri.parse(songs.get(position).toString());
        mediaPlayer = MediaPlayer.create(this , uri);
        mediaPlayer.start();


            //code execuded after music is over means completely played

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if(check){
                        mediaPlayer.seekTo(0);
                        seekBar.setProgress(0);
                        mediaPlayer.start();
                    }else{
                        if(whichsong<songs.size()-1){
                            whichsong++;
                            mediaPlayer.stop();
                            mediaPlayer.release();
                            playMusic(songs , whichsong);
                            String nextname = songs.get(whichsong).getName().toString();
                            nextname = nextname.replace(".mp3" , "");
                            songname.setText(nextname);
                        }else{
                            Toast.makeText(PlaySong.this, "End of playList", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            });


    }

}