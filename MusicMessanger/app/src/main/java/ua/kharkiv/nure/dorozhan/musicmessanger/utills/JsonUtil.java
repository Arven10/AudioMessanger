package ua.kharkiv.nure.dorozhan.musicmessanger.utills;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import ua.kharkiv.nure.dorozhan.musicmessanger.models.Song;
import ua.kharkiv.nure.dorozhan.musicmessanger.models.User;

public class JsonUtil {

    public static JSONObject userToJsonObject(User user) throws JSONException {
        JSONObject userJsonObject = new JSONObject();
        userJsonObject.put("id", user.getUserId());
        userJsonObject.put("userName", user.getUsername());
        userJsonObject.put("email", user.getEmail());
        userJsonObject.put("photoUrl", user.getPhotoUrl());
        userJsonObject.put("latitude", user.getLatitude());
        userJsonObject.put("longitude", user.getLongitude());
        return userJsonObject;
    }

    public static JSONObject songToJsonObject(Song song) throws JSONException {
        JSONObject songJsonObject = new JSONObject();
        songJsonObject.put("title", song.getTitle());
        songJsonObject.put("performer", song.getPerformer());
        songJsonObject.put("album", song.getAlbum());
        return songJsonObject;
    }

    public static User jsonObjectToUser(JSONObject jsonUser) throws JSONException {
        User user = new User();
        user.setUserId(jsonUser.getString("id"));
        user.setUsername(jsonUser.getString("userName"));
        user.setEmail(jsonUser.getString("email"));
        user.setPhotoUrl(jsonUser.getString("photoUrl"));
        user.setLatitude(jsonUser.getDouble("latitude"));
        user.setLongitude(jsonUser.getDouble("longitude"));
        return user;
    }
}
