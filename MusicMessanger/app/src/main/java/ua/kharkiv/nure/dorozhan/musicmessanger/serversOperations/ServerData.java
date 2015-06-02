package ua.kharkiv.nure.dorozhan.musicmessanger.serversOperations;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ua.kharkiv.nure.dorozhan.musicmessanger.models.Song;
import ua.kharkiv.nure.dorozhan.musicmessanger.models.User;
import ua.kharkiv.nure.dorozhan.musicmessanger.utills.JsonUtil;

public class ServerData {
    private static final String POST = "POST";
    private static final String GET = "GET";

    private static final String SERVER = "http://localhost:9000";
    private static final String UPDATE_USER_ROUTE = "/api/v1/user/";
    private static final String UPDATE_SONG_ROUTE = "/api/v1/song/";
    private static final String LIST_OF_USERS = "/api/v1/users/";

    public static void sendUserData(User user) throws IOException, JSONException {
        URL url = new URL(SERVER + UPDATE_USER_ROUTE + user.getUserId());
        HttpURLConnection connectionToServer = getHttpConnectToServer(url, POST);
        JSONObject userJsonObject = JsonUtil.userToJsonObject(user);
        sendJsonToServer(connectionToServer, userJsonObject);
    }

    // Users whose listen same song and have same location (100 m for example)
    public static List<User> getListOfUsers(Song song, String userId) throws IOException, JSONException{
        URL url = new URL(SERVER + LIST_OF_USERS + userId);
        HttpURLConnection connectionToServer = getHttpConnectToServer(url, POST);
        JSONObject songJsonObject = JsonUtil.songToJsonObject(song);
        sendJsonToServer(connectionToServer, songJsonObject);
        List<User> listOfUsers = receiveDataFromServer(connectionToServer);
        return listOfUsers;
    }

    private static List<User> receiveDataFromServer(HttpURLConnection connectionToServer)
            throws IOException, JSONException {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(connectionToServer.getInputStream()));
        String next;
        List<User> listOfUsers = new ArrayList<User>();
        while ((next = bufferedReader.readLine()) != null){
            JSONArray jsonArrayUsers = new JSONArray(next);
            for (int i = 0; i < jsonArrayUsers.length(); i++) {
                JSONObject jsonUser = (JSONObject) jsonArrayUsers.get(i);
                User user = JsonUtil.jsonObjectToUser(jsonUser);
                listOfUsers.add(user);
            }
        }
        return listOfUsers;
    }

    private static void sendJsonToServer(HttpURLConnection connectionToServer, JSONObject jsonObject)
            throws IOException{
        OutputStream out = connectionToServer.getOutputStream();
        String message = jsonObject.toString();
        Log.e("Json", message);
        out.write(message.getBytes("UTF-8"));
        int respCode = connectionToServer.getResponseCode();
        Log.e("Resp code", respCode + "");
        out.flush();
        out.close();
    }

    private static HttpURLConnection getHttpConnectToServer(URL url, String typeOfRequest) throws IOException {
        URLConnection connection = url.openConnection();
        HttpURLConnection httpConnection = (HttpURLConnection) connection;
        httpConnection.setRequestMethod(typeOfRequest);
        httpConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        httpConnection.setRequestProperty("Accept", "application/json");
        httpConnection.setDoInput(true);
        httpConnection.setDoOutput(true);
        return httpConnection;
    }
}
