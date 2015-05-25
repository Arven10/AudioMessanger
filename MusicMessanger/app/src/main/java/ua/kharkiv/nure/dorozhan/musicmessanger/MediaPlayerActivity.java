package ua.kharkiv.nure.dorozhan.musicmessanger;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MediaPlayerActivity extends Activity{
    private MediaPlayer mMediaPlayer;
    private TextView mSongName;
    private TextView mDuration;
    private double timeElapsed = 0;
    private double finalTime;
    private int forwardTime = 2000;
    private int backwardTime = 2000;
    private Handler mDurationHandler = new Handler();
    private SeekBar mSeekbar;
    private Button mListOfPeople;
    private static final String SONG_NAME = "Sample_Song.mp3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);
        initializeMediaFields();
        mListOfPeople = (Button) findViewById(R.id.listOfPeople);
    }

    private void initializeMediaFields(){
        mSongName = (TextView) findViewById(R.id.songName);
        mMediaPlayer = MediaPlayer.create(this, R.raw.sample_song);
        mDuration = (TextView) findViewById(R.id.songDuration);
        mSeekbar = (SeekBar) findViewById(R.id.seekBar);
        finalTime = mMediaPlayer.getDuration();
        mSongName.setText(SONG_NAME);
        mSeekbar.setMax((int) finalTime);
    }

    public void play(View view) {
        mMediaPlayer.start();
        timeElapsed = mMediaPlayer.getCurrentPosition();
        mSeekbar.setProgress((int) timeElapsed);
        mDurationHandler.postDelayed(updateSeekBarTime, 100);
    }

    private Runnable updateSeekBarTime = new Runnable() {
        public void run() {
            timeElapsed = mMediaPlayer.getCurrentPosition();
            mSeekbar.setProgress((int) timeElapsed);
            double timeRemaining = finalTime - timeElapsed;
            mDuration.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining),
                    TimeUnit.MILLISECONDS.toSeconds((long) timeRemaining) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining))));
            mDurationHandler.postDelayed(this, 100);
        }
    };

    public void pause(View view) {
        mMediaPlayer.pause();
    }

    public void forward(View view) {
        if ((timeElapsed + forwardTime) <= finalTime) {
            timeElapsed = timeElapsed + forwardTime;
            mMediaPlayer.seekTo((int) timeElapsed);
        }
    }

    public void rewind(View view) {
        if ((timeElapsed - backwardTime) > 0) {
            timeElapsed = timeElapsed - backwardTime;
            mMediaPlayer.seekTo((int) timeElapsed);
        }
    }
}
