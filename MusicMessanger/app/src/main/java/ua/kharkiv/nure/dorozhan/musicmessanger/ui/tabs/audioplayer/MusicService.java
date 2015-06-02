package ua.kharkiv.nure.dorozhan.musicmessanger.ui.tabs.audioplayer;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import ua.kharkiv.nure.dorozhan.musicmessanger.R;
import ua.kharkiv.nure.dorozhan.musicmessanger.models.Song;

public class MusicService extends Service implements
MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
MediaPlayer.OnCompletionListener {
	private static final int NOTIFY_ID = 1;
	private MediaPlayer player;
	private ArrayList<Song> songs;
	private int songPosition;
	private final IBinder musicBinder = new MusicBinder();
	private String songTitle;
	private boolean shuffle = false;
	private Random rand;

	public void onCreate(){
		super.onCreate();
		songPosition = 0;
		rand = new Random();
		player = new MediaPlayer();
		initMusicPlayer();
	}

	public void initMusicPlayer(){
		player.setWakeMode(getApplicationContext(),
				PowerManager.PARTIAL_WAKE_LOCK);
		player.setAudioStreamType(AudioManager.STREAM_MUSIC);
		player.setOnPreparedListener(this);
		player.setOnCompletionListener(this);
		player.setOnErrorListener(this);
	}

	public void setList(ArrayList<Song> songs){
		this.songs = songs;
	}

	public class MusicBinder extends Binder {
		MusicService getService() { 
			return MusicService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return musicBinder;
	}

	@Override
	public boolean onUnbind(Intent intent){
		player.stop();
		player.release();
		return false;
	}

	public void playSong(){
		player.reset();
		Song currentSong = songs.get(songPosition);
		songTitle = currentSong.getTitle();
		long currentSongId = currentSong.getId();
		Uri trackUri = ContentUris.withAppendedId(
				android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				currentSongId);
		try{ 
			player.setDataSource(getApplicationContext(), trackUri);
		}
		catch(Exception e){
			Log.e("MUSIC SERVICE", "Error setting data source", e);
		}
		player.prepareAsync();
	}

	public void setSongPosition(int songPosition){
		this.songPosition = songPosition;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		if (player.getCurrentPosition() > 0) {
			mp.reset();
			playNext();
		}
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		mp.reset();
		return false;
	}

	// hmmmm
	@Override
	public void onPrepared(MediaPlayer mp) {
		mp.start();
		Intent notIntent = new Intent(this, AudioPlayerTab.class);
		notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pendInt = PendingIntent.getActivity(this, 0,
				notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		Notification.Builder builder = new Notification.Builder(this);

		builder.setContentIntent(pendInt)
		.setSmallIcon(R.drawable.play_song_notification)
		.setTicker(songTitle)
		.setOngoing(true)
		.setContentTitle(getResources().getString(R.string.SongNotification))
		.setContentText(songTitle);
		Notification not = builder.build();
		startForeground(NOTIFY_ID, not);
	}

	public int getPosition(){
		return player.getCurrentPosition();
	}

	public int getDuration(){
		return player.getDuration();
	}

	public boolean isPlaying(){
		return player.isPlaying();
	}

	public void pausePlayer(){
		player.pause();
	}

	public void seek(int position){
		player.seekTo(position);
	}

	public void startPlayer(){
		player.start();
	}

	public void playPrevious(){
		songPosition--;
		if (songPosition < 0) {
			songPosition = songs.size() - 1;
		}
		playSong();
	}

	public void playNext(){
		if (shuffle) {
			int newSong = songPosition;
			while(newSong == songPosition) {
				newSong = rand.nextInt(songs.size());
			}
			songPosition = newSong;
		} else {
			songPosition++;
			if(songPosition >= songs.size()) {
				songPosition = 0;
			}
		}
		playSong();
	}

	@Override
	public void onDestroy() {
		stopForeground(true);
	}

	public void setShuffle(){
		shuffle = !shuffle;
	}

}
