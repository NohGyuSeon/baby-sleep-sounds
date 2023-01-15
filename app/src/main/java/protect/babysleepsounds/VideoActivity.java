package protect.babysleepsounds;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import es.dmoral.toasty.Toasty;

public class VideoActivity extends YouTubeBaseActivity {
    YouTubePlayerView playerView;
    YouTubePlayer player;

    // API_KEY enclosed, it is from "do it android"
    private static String API_KEY = "AIzaSyDdSkoPeRAC0LKicMbl68yVDwyLhPey2nc";
    private static String videoId1 = "PunVHiOxUEk";
    private static String videoId2 = "vcKxZLQETgE";
    private static String videoId3 = "wr8LX3tsbLQ";
    private static String videoId;

    // video index
    public static int position = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        initPlayer();

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playVideo();
                switch (position) {
                    case 1:
                        Toasty.success(getApplication(), R.string.Funky_Veggies, Toast.LENGTH_SHORT, true).show();
                        break;
                    case 2:
                        Toasty.success(getApplication(), R.string.Super_Simple_Songs, Toast.LENGTH_SHORT, true).show();
                        break;
                    case 3:
                        Toasty.success(getApplication(), R.string.Lellobee, Toast.LENGTH_SHORT, true).show();
                        break;
                }
            }
        });

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopVideo();
                Toasty.success(getApplication(), R.string.stopVideo, Toast.LENGTH_SHORT, true).show();
            }
        });

        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivityForResult(intent, 101);
                finish();
            }
        });

        Button button4 = findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position) {
                    case 1:
                        videoId = videoId1;
                        position = 2;
                        break;
                    case 2:
                        videoId = videoId2;
                        position = 3;
                        break;
                    case 3:
                        videoId = videoId3;
                        position = 1;
                        break;
                }

                Toasty.info(getApplication(), R.string.updateVideo, Toast.LENGTH_SHORT, true).show();
            }
        });

    }

    public void initPlayer() {
        playerView = findViewById(R.id.playerView);

        playerView.initialize(API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                player = youTubePlayer;

                player.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                    @Override
                    public void onLoading() {}

                    @Override
                    public void onLoaded(String id) {
                        player.play();
                    }

                    @Override
                    public void onAdStarted() {}

                    @Override
                    public void onVideoStarted() {}

                    @Override
                    public void onVideoEnded() {}

                    @Override
                    public void onError(YouTubePlayer.ErrorReason errorReason) {}
                });

            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }

        });
    }

    public void playVideo() {
        if (player != null) {
            if (player.isPlaying()) {
                player.pause();
            }

            player.cueVideo(videoId);
        }
    }

    public void stopVideo() {
        if (player != null) {
            if (player.isPlaying()) {
                player.pause();
            }

        }
    }

}
