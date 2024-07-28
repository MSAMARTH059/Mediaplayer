package com.smart.mediaplayer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    TextView txt1, txt2, txt3;
    Button btnback, btnfor, btnplay, btnpause;
    SeekBar seekBar;

    private double starttime = 0;
    private double finaltime = 0;
    private int forwardtime = 5000;
    private int backwardtime = 5000;
    private MediaPlayer mediaPlayer;
    private Handler myhandler = new Handler();

    public static int onetimeonly = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnback = findViewById(R.id.button);
        btnfor = findViewById(R.id.button2);
        btnplay = findViewById(R.id.button3);
        btnpause = findViewById(R.id.button4);

        txt1 = findViewById(R.id.textView2);
        txt2 = findViewById(R.id.textView3);
        txt3 = findViewById(R.id.textView4);
        txt2.setText("Song.mp3");

        seekBar =  findViewById(R.id.seekBar);
        seekBar.setClickable(false);
        btnpause.setEnabled(false);

        btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Started playing",Toast.LENGTH_LONG).show();

                if (mediaPlayer==null) {
                    mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.song);
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            mediaPlayer.stop();
                        }
                    });
                }
                finaltime=mediaPlayer.getDuration();
                starttime=mediaPlayer.getCurrentPosition();

                if (onetimeonly==0){
                    seekBar.setMax((int) finaltime);
                    onetimeonly=1;
                }

                mediaPlayer.start();

                txt3.setText(String.format("%d min:%d sec",
                        TimeUnit.MILLISECONDS.toMinutes((long) finaltime),
                        TimeUnit.MILLISECONDS.toSeconds((long)finaltime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finaltime))));

                txt1.setText(String.format("%d min:%d sec",
                        TimeUnit.MILLISECONDS.toMinutes((long) starttime),
                        TimeUnit.MILLISECONDS.toSeconds((long)starttime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) starttime))));

                seekBar.setProgress((int) starttime);
                myhandler.postDelayed(UpdateSongTime,100);
                btnpause.setEnabled(true);
                btnplay.setEnabled(false);
            }
        });
        btnpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
                btnplay.setEnabled(true);
                btnpause.setEnabled(false);
            }
        });
        btnfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp= (int) starttime;
                if(temp+forwardtime<finaltime){
                    starttime=starttime+forwardtime;
                    mediaPlayer.seekTo((int) starttime);
                }
            }
        });
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int) starttime;
                if (temp-backwardtime>0){
                    starttime=starttime-backwardtime;
                    mediaPlayer.seekTo((int) starttime);
                }
            }
        });
    }
    private Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            starttime=mediaPlayer.getCurrentPosition();
            txt1.setText(String.format("%d min:%d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) starttime),
                    TimeUnit.MILLISECONDS.toSeconds((long)starttime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) starttime))));
            seekBar.setProgress((int) starttime);
            myhandler.postDelayed(this,100);
        }
    };
}