package ua.kharkiv.nure.dorozhan.musicmessanger.ui.tabs.audioplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ua.kharkiv.nure.dorozhan.musicmessanger.R;
import ua.kharkiv.nure.dorozhan.musicmessanger.models.Song;

public class SongAdapter extends BaseAdapter {
	private ArrayList<Song> songs;
	private LayoutInflater songInflater;

	public SongAdapter(Context c, ArrayList<Song> songs){
		this.songs = songs;
		songInflater = LayoutInflater.from(c);
	}

	@Override
	public int getCount() {
		return songs.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout songLayout = (LinearLayout) songInflater.inflate
				(R.layout.song, parent, false);
		TextView songView = (TextView)songLayout.findViewById(R.id.song_title);
		TextView performerView = (TextView)songLayout.findViewById(R.id.song_performer);
		TextView albumView = (TextView)songLayout.findViewById(R.id.song_album);
		Song currentSong = songs.get(position);
		songView.setText(currentSong.getTitle());
		performerView.setText(currentSong.getPerformer());
		albumView.setText(currentSong.getAlbum());
		songLayout.setTag(position);
		return songLayout;
	}
}
