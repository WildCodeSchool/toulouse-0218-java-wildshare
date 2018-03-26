package fr.wcs.testprojet2;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.VideoView;

public class SplashActivity extends AppCompatActivity {
VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();

        videoView = (VideoView)findViewById(R.id.videoView);

        Uri projectfinal = Uri.parse("android.resource://"+ getPackageName()+ "/" + R.raw.projet_final);

        videoView.setVideoURI(projectfinal);

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
            if (isFinishing())
            return;

          startActivity(new Intent(SplashActivity.this, LoginActivity.class));
          finish();
            }
        });
        videoView.start();
    }
}
