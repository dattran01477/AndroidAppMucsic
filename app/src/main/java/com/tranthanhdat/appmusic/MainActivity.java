package com.tranthanhdat.appmusic;

import android.Manifest;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chibde.visualizer.CircleBarVisualizer;
import com.jgabrielfreitas.core.BlurImageView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.tranthanhdat.appmusic.com.tranthanhdat.appmusic.ultils.MusicUltils;

import java.io.File;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    //Componets Giao dien
    private View parent_view;
    private AppCompatSeekBar  seek_barProgress;
    private FloatingActionButton btn_play;
    private TextView tv_songCurrentDuration,tv_songTotalDuration;
    private CircularImageView image;
    private CircleBarVisualizer circleBarVisualizer;
    private BlurImageView blurImageView;
    //Mediaplay
    private MediaPlayer mediaPlayer;
    private Handler mhandler=new Handler();

    private MusicUltils musicUtils;

    public static final int MY_REQUEST_CODE = 100;
    public static final int AUDIO_PERMISSION_REQUEST_CODE = 102;

    public String URI;

    String storagePermission[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        storagePermission = new String[]{Manifest.permission.RECORD_AUDIO};
        requestStoragePermission();
        setComponents();

        doStart();

    }

    private void doStart() {
        int duration = this.mediaPlayer.getDuration();
        tv_songTotalDuration.setText(musicUtils.milliSecondToTime(duration));
        if(!mediaPlayer.isPlaying()){
            mediaPlayer.start();
        }
    }

    private void setComponents() {
        //get URI from intent
        // Intent truyền sang.
        Intent intent = this.getIntent();
        this.URI= intent.getStringExtra("URI");
        Uri myUri1 = Uri.fromFile(new File(URI));

        //get componet to activity .xml
        parent_view=findViewById(R.id.parent_view);
        seek_barProgress=findViewById(R.id.seek_aong_progessbar);
        btn_play=findViewById(R.id.btn_play);
        tv_songCurrentDuration=findViewById(R.id.tv_song_current_duration);
        tv_songTotalDuration=findViewById(R.id.total_duration);
        image=findViewById(R.id.image);
        blurImageView=findViewById(R.id.background_blur);

        //blurImage
        blurImageView.setBlur(5);

        musicUtils=new MusicUltils();
        //init mediaplayer
        setPlayer();
    }

    public void controlClick(View view) {
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this,storagePermission,AUDIO_PERMISSION_REQUEST_CODE);
    }

    private void setPlayer(){
        mediaPlayer=new MediaPlayer();

        try {
            mediaPlayer.setDataSource(this.URI);
            mediaPlayer.prepare();
        } catch (IOException e) {
            Toast.makeText(this, "File nhạc bị lỗi", Toast.LENGTH_SHORT).show();
        }

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                btn_play.setImageResource(R.drawable.ic_play);
            }

        });

        circleBarVisualizer = findViewById(R.id.visualizer);
        // set custom color to the line.
        circleBarVisualizer.setColor(ContextCompat.getColor(this, R.color.colorDarkDrange));
        // Set you media player to the visualizer.
         circleBarVisualizer.setPlayer(mediaPlayer.getAudioSessionId());
    }

}
