package ua.kharkiv.nure.dorozhan.musicmessanger;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Dorozhan on 16.05.2015.
 */
public class RequestSong extends AsyncTask {
    private static final String URL_SONG = "http://localhost:9000/song";

    @Override
    protected String doInBackground(Object[] params) {
        try {
            URL url = new URL(URL_SONG);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
