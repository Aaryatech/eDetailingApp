package com.ats.edetailingapp.activity;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import com.ats.edetailingapp.R;
import com.ats.edetailingapp.util.Utility;

public class VideoActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    private String filePath;
    private VideoView videoView;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        filePath = getIntent().getStringExtra(Utility.FILE_PATH);

        videoView = (VideoView) findViewById(R.id.disp_videoview);
        videoView.setOnCompletionListener(this);
        MediaController ctlr = new MediaController(this);
        videoView.setVideoPath(filePath);
        progressDialog = new ProgressDialog(VideoActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
        ctlr.setMediaPlayer(videoView);
        videoView.setMediaController(ctlr);
        progressDialog.dismiss();
        videoView.requestFocus();
        videoView.start();

    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            videoView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER));
        } else {
            videoView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        finish();
    }

}
